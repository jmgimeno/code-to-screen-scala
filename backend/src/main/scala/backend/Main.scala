package backend

import zio.*
import zio.http.*
import zio.http.ChannelEvent.{Read, UserEvent, UserEventTriggered}

object Main extends ZIOAppDefault:

  type Connections = Ref[Set[WebSocketChannel]]

  val ws: SocketApp[Connections] =
    Handler.fromZIO(ZIO.service[Connections]).flatMap { connections =>
      Handler.webSocket { channel =>
        channel.receiveAll {
          case UserEventTriggered(UserEvent.HandshakeComplete) =>
            connections.update(_ + channel)
          case Read(WebSocketFrame.Close(_, _)) =>
            connections.update(_ - channel)
          case Read(WebSocketFrame.Text(code)) =>
            connections.get.flatMap { channels =>
              ZIO.foreachDiscard(channels) { ch =>
                ch.send(Read(WebSocketFrame.Text(code)))
                  .orElse(connections.update(_ - ch))
              }
            }
          case _ => ZIO.unit
        }
      }
    }

  val socketApp: Http[Connections, Nothing, Request, Response] =
    Http.collectZIO[Request] { case Method.GET -> Root / "subscribe" =>
      ws.toResponse
    }

  val filesApp: Http[Any, Throwable, Request, Response] =
    Http.collectHttp[Request] {
      case Method.GET -> Root / file =>
        Http.fromResource(s"public/$file")
      case Method.GET -> Root =>
        Http.fromResource("public/index.html")

    }

  val app: Http[Connections, Throwable, Request, Response] = socketApp ++ filesApp

  val run: ZIO[Any, Nothing, Nothing] =
    Server
      .serve(app.withDefaultErrorResponse)
      .provide(
        ZLayer.fromZIO(Ref.make(Set.empty[WebSocketChannel])),
        Server.defaultWithPort(8080).orDie
      )

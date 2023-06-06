package backend

import zio.*
import zio.http.*
import zio.http.ChannelEvent.UserEventTriggered
import zio.http.ChannelEvent.UserEvent
import zio.http.ChannelEvent.Read
import io.netty.util.internal.ThrowableUtil

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

  val app: RHttpApp[Connections] =
    Http.collectZIO[Request] {
      case Method.GET -> Root / "subscribe" =>
        ws.toResponse
      case Method.GET -> Root / file =>
        Handler.getResource(s"public/$file").toResponse
      case Method.GET -> Root =>
        ZIO.fromEither(URL.decode("/index.html")).map(Response.redirect(_))
    }

  val run: ZIO[Any, Nothing, Nothing] =
    Server
      .serve(app.withDefaultErrorResponse)
      .provide(ZLayer.fromZIO(Ref.make(Set.empty[WebSocketChannel])), Server.default.orDie)

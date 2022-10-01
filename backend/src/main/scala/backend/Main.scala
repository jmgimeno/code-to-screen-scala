package backend

import zio.*
import zhttp.http.*
import zhttp.service.*
import zhttp.service.ChannelEvent.*
import zhttp.socket.*

object Main extends ZIOAppDefault:

  type Connections = Ref[Set[WebSocketChannel]]

  val ws: Http[Connections, Nothing, WebSocketChannelEvent, Unit] =
    Http.fromZIO(ZIO.service[Connections]).flatMap { connections =>
      Http.collectZIO[WebSocketChannelEvent] {
        case ChannelEvent(ch, UserEventTriggered(UserEvent.HandshakeComplete)) =>
          connections.update(_ + ch)
        case ChannelEvent(ch, ChannelRead(WebSocketFrame.Close(_, _))) =>
          connections.update(_ - ch)
        case ChannelEvent(ch, ChannelRead(WebSocketFrame.Text(code))) =>
          connections.get.flatMap { channels =>
            ZIO.foreachDiscard(channels)(ch =>
              ch.writeAndFlush(WebSocketFrame.Text(code)).orElse(connections.update(_ - ch))
            )
          }
      }
    }

  val app: Http[Connections, Nothing, Request, Response] =
    Http.collectHttp[Request] {
      case Method.GET -> !! / "subscribe" =>
        ws.toSocketApp.toHttp
      case Method.GET -> !! / file =>
        Http.fromResource(s"public/$file").orElse(Http.notFound)
      case Method.GET -> !! =>
        Response.redirect("/index.html").toHttp
    }

  val run =
    (for
      port <- System.envOrElse("PORT", "8080").map(_.toInt)
      _ <- Console.printLine(s"Starting server on port $port")
      _ <- Server.start(port, app)
    yield ()).provide(ZLayer.fromZIO(Ref.make(Set.empty[WebSocketChannel])))

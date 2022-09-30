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
    Http.collectZIO[Request] { case Method.GET -> !! / "subscribe" =>
      ws.toSocketApp.toResponse
    }

  val run =
    Server
      .start(8080, app)
      .provide(ZLayer.fromZIO(Ref.make(Set.empty[WebSocketChannel])))

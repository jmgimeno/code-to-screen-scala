package frontend

import com.raquo.laminar.api.L.*
import io.laminext.websocket.WebSocket
import org.scalajs.dom
import scala.scalajs.js

final case class Program(id: Int, code: String)

val ws: WebSocket[String, String] = WebSocket.path("/subscribe").string.build(managed = true)

val programs: Signal[List[Program]] =
  ws.received.foldLeft(List.empty)((programs, code) =>
    programs :+ Program(programs.length + 1, code)
  )

@main def MainPage(): Unit =
  val containerNode = dom.document.querySelector("#app")
  render(containerNode, RootElement)

def RootElement =
  div(
    ws.connect,
    NavBar,
    Tabs
  )

def NavBar =
  nav(
    cls := "navbar navbar-expand-sm bg-light navbar-light",
    div(
      cls := "container-fluid",
      div(
        cls := "navbar-brand text-dark me-3",
        "Code to Screen"
      ),
      button(
        cls := "navbar-toggler",
        typ := "button",
        dataAttr("bs-toggle") := "collapse",
        dataAttr("bs-target") := "#navbarNav",
        span(
          cls := "navbar-toggler-icon"
        )
      ),
      div(
        cls := "collapse navbar-collapse",
        idAttr := "navbarNav",
        ul(
          cls := "nav navbar-nav",
          li(
            cls := "nav-item",
            a(
              cls := "nav-link active",
              dataAttr("bs-toggle") := "tab",
              href := "#post",
              "Post"
            )
          ),
          li(
            cls := "nav-item",
            a(
              cls := "nav-link",
              cls.toggle("disabled") <-- programs.map(_.isEmpty),
              dataAttr("bs-toggle") := "tab",
              href := "#show",
              "Show"
            )
          )
        ),
        div(
          cls := "navbar-nav ms-auto",
          a(
            href := "http://github.com",
            target := "_blank",
            cls := "d-none d-md-block d-lg-block d-xl-block d-xxl-block",
            img(src := "./GitHub-Mark-32px.png")
          )
        )
      )
    )
  )

def Tabs =
  div(
    cls := "tab-content",
    div(
      cls := "tab-pane active",
      idAttr := "post",
      FormTab
    ),
    div(
      cls := "tab-pane fade",
      idAttr := "show",
      ShowTab
    )
  )

def FormTab =
  div(
    cls := "container p-3",
    form(
      onSubmit --> doPost,
      label(
        forId := "code",
        "Enter the code"
      ),
      textArea(
        cls := "form-control",
        idAttr := "code",
        rows := 20
      ),
      button(
        typ := "submit",
        cls := "mt-2 btn btn-secondary",
        "Submit"
      )
    )
  )

def doPost(event: dom.Event): Unit =
  event.preventDefault()
  val codeArea = dom.document.getElementById("code").asInstanceOf[dom.html.TextArea]
  val code = codeArea.value
  ws.sendOne(code)
  codeArea.value = ""

def ShowTab =
  div(
    cls := "container",
    div(
      cls := "navbar row",
      ul(
        cls := "nav navbar-nav flex-column col-sm-1 navbar-light m-2",
        children <-- programs.split(_.id)(renderProgramTab)
      ),
      div(
        cls := "tab-content col-sm-10",
        children <-- programs.split(_.id)(renderProgramCode)
      )
    )
  )

def renderProgramTab(id: Int, initialProgram: Program, program: Signal[Program]) =
  li(
    cls := "nav-item",
    a(
      cls := "nav-link",
      cls.toggle("active") := initialProgram.id == 1,
      dataAttr("bs-toggle") := "tab",
      href := s"#code${id}",
      s"Code ${id}"
    )
  )

def renderProgramCode(id: Int, initialProgram: Program, program: Signal[Program]) =
  div(
    cls := "tab-pane container",
    cls.toggle("active") := initialProgram.id == 1,
    idAttr := s"code${id}",
    pre(
      cls := "mt-3",
      code(
        initialProgram.code,
        onMountCallback(ctx => js.Dynamic.global.hljs.highlightElement(ctx.thisNode.ref))
      )
    )
  )

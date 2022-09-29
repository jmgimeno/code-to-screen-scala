package frontend

import org.scalajs.dom
import scala.scalajs.js
import com.raquo.laminar.api.L.*

final case class Program(id: Int, code: String)

val posts: Var[List[Program]] = Var(List())

def doPost(event: dom.Event): Unit =
  event.preventDefault()
  val codeArea = dom.document.getElementById("code").asInstanceOf[dom.html.TextArea]
  val code = codeArea.value
  val id = posts.now().size + 1
  posts.update(_ :+ Program(id, code))
  codeArea.value = ""

val Post =
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

def renderProgramTab(id: Int, initialProgram: Program, program: Signal[Program]) =
  li(
    cls := "nav-item",
    a(
      cls := "nav-link",
      cls.toggle("active") <-- program.map(_.id == 1),
      dataAttr("bs-toggle") := "tab",
      href := s"#code${id}",
      s"Code ${id}"
    )
  )

def renderProgramCode(id: Int, initialProgram: Program, program: Signal[Program]) =
  div(
    cls := "tab-pane container",
    cls.toggle("active") <-- program.map(_.id == 1),
    idAttr := s"code${id}",
    pre(
      cls := "mt-3",
      code(
        child <-- program.map(_.code),
        onMountCallback(ctx => js.Dynamic.global.hljs.highlightElement(ctx.thisNode.ref))
      )
    )
  )

val Show =
  div(
    cls := "container",
    div(
      cls := "navbar row",
      ul(
        cls := "nav navbar-nav flex-column col-sm-1 navbar-light m-2",
        children <-- posts.signal.split(_.id)(renderProgramTab)
      ),
      div(
        cls := "tab-content col-sm-10",
        children <-- posts.signal.split(_.id)(renderProgramCode)
      )
    )
  )

@main def MainPage(): Unit =
  val rootElement =
    div(
      cls := "container-fluid",
      div(
        cls := "navbar navbar-expand-sm bg-light navbar-light",
        div(
          cls := "navbar-brand text-dark me-3",
          "Code to Screen"
        ),
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
              cls.toggle("disabled") <-- posts.signal.map(_.isEmpty),
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
      ),
      div(
        cls := "tab-content",
        div(
          cls := "tab-pane active",
          idAttr := "post",
          Post
        ),
        div(
          cls := "tab-pane fade",
          idAttr := "show",
          Show
        )
      )
    )

  // In most other examples, containerNode will be set to this behind the scenes
  val containerNode = dom.document.querySelector("#app")

  render(containerNode, rootElement)

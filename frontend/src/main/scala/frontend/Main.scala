package frontend

import org.scalajs.dom
import com.raquo.laminar.api.L.*

var posts: List[String] =
  List(
    """def main(args: Array[String]): Unit = {
      | println("Code 1")
      |}""".stripMargin,
    """def main(args: Array[String]): Unit = {
      | println("Code 2")
      |}""".stripMargin
  )

def doPost(event: dom.Event): Unit =
  event.preventDefault()
  dom.window.alert("Called submit !!!")

def Post =
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

def Show =
  div(
    cls := "navbar row",
    ul(
      cls := "nav navbar-nav flex-column col-md-1 navbar-light m-2",
      posts.zipWithIndex.map { case (_, index) =>
        li(
          cls := "nav-item",
          a(
            cls := "nav-link",
            cls.toggle("active") := index == 0,
            dataAttr("bs-toggle") := "tab",
            href := s"#code$index",
            s"Code ${index + 1}"
          )
        )
      }
    ),
    div(
      cls := "tab-content col-md-11",
      posts.zipWithIndex.map { case (post, index) =>
        div(
          cls := "tab-pane container",
          cls.toggle("active") := index == 0,
          idAttr := s"code$index",
          pre(
            cls := "mt-3",
            code(cls := "language-scala", post)
          )
        )
      }
    )
  )

@main def MainPage(): Unit =
  val nameVar = Var(initial = "world")
  val rootElement =
    div(
      cls := "container-fluid",
      div(
        cls := "navbar navbar-expand-md bg-light navbar-light",
        div(
          cls := "navbar-brand text-dark",
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

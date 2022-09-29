package frontend

import org.scalajs.dom
import com.raquo.laminar.api.L.*

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
              href := "#",
              cls := "nav-link active ",
              dataAttr("bs-toggle") := "tab",
              href := "#post",
              "Post"
            )
          ),
          li(
            cls := "nav-item",
            a(
              href := "#",
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
          p(
            "Post"
          )
        ),
        div(
          cls := "tab-pane fade",
          idAttr := "show",
          p(
            "Show"
          )
        )
      )
    )

  // In most other examples, containerNode will be set to this behind the scenes
  val containerNode = dom.document.querySelector("#app")

  render(containerNode, rootElement)

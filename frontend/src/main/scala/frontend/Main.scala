package frontend

import org.scalajs.dom
import com.raquo.laminar.api.L.*

@main def MainPage(): Unit =
  val nameVar = Var(initial = "world")
  val rootElement =
    nav(
      cls := "navbar navbar-expand-md fixed-top bg-light",
      div(
        cls := "container-fluid",
        a(href := "#", cls := "navbar-brand", "Code to Screen"),
        div(
          cls := "collapse navbar-collapse d-flex justify-content-between",
          idAttr := "navbarCollapse",
          ul(
            cls := "navbar-nav",
            li(cls := "nav-item", a(href := "#", cls := "nav-link active", "Post")),
            li(cls := "nav-item", a(href := "#", cls := "nav-link", "Show"))
          ),
          a(
            href := "http://github.com",
            target := "_blank",
            cls := "d-none d-md-block d-lg-block d-xl-block d-xxl-block",
            img(src := "./GitHub-Mark-32px.png")
          )
        )
      )
    )

  // In most other examples, containerNode will be set to this behind the scenes
  val containerNode = dom.document.querySelector("#app")

  render(containerNode, rootElement)

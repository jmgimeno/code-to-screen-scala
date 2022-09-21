import org.scalajs.linker.interface.ModuleSplitStyle

lazy val backend = project.in(file("backend"))
    .enablePlugins(ScalaJSPlugin)
    .settings(
        scalaVersion := "3.2.0",
        scalaJSUseMainModuleInitializer := true,
        scalaJSLinkerConfig ~= {
            _.withModuleKind(ModuleKind.ESModule)
              .withModuleSplitStyle(
                ModuleSplitStyle.SmallModulesFor(List("backend"))
              )
        },
        libraryDependencies ++= Seq(
            "org.scala-js" %%% "scalajs-dom" % "2.2.0",
            "com.raquo" %%% "laminar" % "0.14.2"
        )
    )
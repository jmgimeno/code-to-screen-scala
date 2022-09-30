import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion := "3.2.0"

lazy val frontend = project
  .in(file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("frontend"))
        )
    },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.2.0",
      "com.raquo" %%% "laminar" % "0.14.2"
    )
  )

lazy val backend = project
  .in(file("backend"))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.1",
      "io.d11" %% "zhttp" % "2.0.0-RC11"
    )
  )

lazy val root = project
  .in(file("."))
  .aggregate(frontend, backend)

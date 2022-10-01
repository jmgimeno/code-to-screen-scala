ThisBuild / scalaVersion := "3.2.0"

lazy val frontend = project
  .in(file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule).withSourceMap(false)
    },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.2.0",
      "com.raquo" %%% "laminar" % "0.14.2",
      "io.laminext" %%% "websocket" % "0.14.4"
    )
  )

lazy val backend = project
  .in(file("backend"))
  .settings(
    assembly / assemblyJarName := "code-to-screen-standalone.jar",
    assembly / mainClass := Some("backend.Main"),
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "io.netty.versions.properties") => MergeStrategy.first
      case x =>
        val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.1",
      "io.d11" %% "zhttp" % "2.0.0-RC11"
    )
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := "code-to-screen"
  )
  .aggregate(frontend, backend)

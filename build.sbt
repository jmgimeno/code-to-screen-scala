import org.scalajs.linker.interface.ModuleSplitStyle

ThisBuild / scalaVersion := "3.3.0"

lazy val frontend = project
  .in(file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    Compile / fastLinkJS / scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withSourceMap(false)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("frontend"))
        )
    },
    Compile / fullOptJS / scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.CommonJSModule)
        .withSourceMap(false)
    },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.2.0",
      "com.raquo" %%% "laminar" % "15.0.1",
      "io.laminext" %%% "websocket" % "0.15.0"
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
      "dev.zio" %% "zio" % "2.0.15",
      "dev.zio" %% "zio-http" % "3.0.0-RC2"
    )
  )

lazy val root = project
  .in(file("."))
  .settings(
    name := "code-to-screen"
  )
  .aggregate(frontend, backend)

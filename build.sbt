import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    inThisBuild(
      List(
        organization := "com.bcm",
        scalaVersion := "2.12.6",
        version := "0.1.0-SNAPSHOT"
      )),
    name := "transit",
    libraryDependencies += scalaTest % Test,
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    // scalaJSUseMainModuleInitializer := true,
  )

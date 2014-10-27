// needed for docker:
import NativePackagerKeys._



name := "koeb_me"

version := "0.1.0"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.jsoup" % "jsoup" % "1.8.1"
) 

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

// play.Project.playScalaSettings
lazy val root = (project in file(".")).enablePlugins(PlayScala)

// for docker:
maintainer in Docker := "Alexander Köb <nerdkram@koeb.me>"

dockerBaseImage in Docker := "debian:stable"

dockerExposedPorts in Docker := Seq(9000)

dockerExposedVolumes in Docker := Seq("/data")

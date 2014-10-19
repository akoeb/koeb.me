name := "koeb_me"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.jsoup" % "jsoup" % "1.8.1"
)     


play.Project.playScalaSettings

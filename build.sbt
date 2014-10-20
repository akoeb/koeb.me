name := "koeb_me"

version := "0.1.0"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.jsoup" % "jsoup" % "1.8.1"
)     


play.Project.playScalaSettings

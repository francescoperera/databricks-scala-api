name := "databricks-scala-api"

version := "0.2.1"

crossScalaVersions := Seq("2.10.6", "2.11.8")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "ch.qos.logback" % "logback-core" % "1.1.7" % "test",
  "org.slf4j" % "slf4j-api" % "1.7.21" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.21" % "test",
  "io.spray" %% "spray-json" % "1.3.2",
  "org.asynchttpclient" % "async-http-client" % "2.0.7",
  "com.typesafe.scala-logging" %% "scala-logging-api" % "2.1.2",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.5.0"
)

organization := "io.findify"

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

bintrayOrganization := Some("findify")
name := "databricks-scala-api"

version := "0.1.2"

crossScalaVersions := Seq("2.10.6", "2.11.8")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "ch.qos.logback" % "logback-core" % "1.1.7" % "test",
  "org.slf4j" % "slf4j-api" % "1.7.21" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.21" % "test",
  "io.spray" %% "spray-json" % "1.3.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0.3",
  "com.typesafe.scala-logging" %% "scala-logging-api" % "2.1.2",
"com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
)

organization := "io.findify"

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

bintrayOrganization := Some("findify")
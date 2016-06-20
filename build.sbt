name := "databricks-api"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "ch.qos.logback" % "logback-core" % "1.1.7" % "test",
  "org.slf4j" % "slf4j-api" % "1.7.21" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.21" % "test",
  "io.spray" %% "spray-json" % "1.3.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.4.0"
)

organization := "io.findify"

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

bintrayOrganization := Some("findify")
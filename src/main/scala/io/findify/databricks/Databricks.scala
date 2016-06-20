package io.findify.databricks

import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.{Http, HttpExt}
import io.findify.databricks.api._
import io.findify.databricks.calls.{Dbfs, Jobs}
import spray.json._

/**
  * Created by shutty on 5/24/16.
  */

class Databricks(auth:Auth)(implicit system:ActorSystem = ActorSystem.create("databricks")) {
  val http = Http(system)
  val dbfs = new Dbfs(auth, http)
  val jobs = new Jobs(auth, http)
}

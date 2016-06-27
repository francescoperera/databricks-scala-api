package io.findify.databricks

import java.nio.file.{Files, Paths}

import io.findify.databricks.api._
import io.findify.databricks.calls.{Dbfs, Jobs}
import spray.json._

/**
  * Created by shutty on 5/24/16.
  */

class Databricks(auth:Auth) {
  val dbfs = new Dbfs(auth)
  val jobs = new Jobs(auth)
}

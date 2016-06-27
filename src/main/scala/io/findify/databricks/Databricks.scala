package io.findify.databricks

import java.nio.file.{Files, Paths}

import io.findify.databricks.api._
import io.findify.databricks.calls.{Dbfs, Jobs}
import org.asynchttpclient.DefaultAsyncHttpClient
import spray.json._

/**
  * Created by shutty on 5/24/16.
  */

class Databricks(auth:Auth) {
  private lazy val client = new DefaultAsyncHttpClient()
  val dbfs = new Dbfs(auth, client)
  val jobs = new Jobs(auth, client)
  def close = client.close()
}

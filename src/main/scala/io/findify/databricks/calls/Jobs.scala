package io.findify.databricks.calls

import io.findify.databricks.Auth
import io.findify.databricks.api._
import org.asynchttpclient.AsyncHttpClient
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{writePretty, read => readjson}

import scala.concurrent.Future

/**
  * Created by shutty on 6/20/16.
  */
class Jobs(auth:Auth, client:AsyncHttpClient) extends ApiCall(s"https://${auth.hostname}/api/2.0/jobs", auth, client) {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val formats = Serialization.formats(NoTypeHints)

  def create(job:Job) = {
    postJson("create", writePretty(job)).map(readjson[CreateJobResponse])
  }

  def reset(id:Int, job:Job) = {
    postJson("reset", writePretty(ResetJob(id, job))).map(readjson[EmptyResponse])
  }

  def delete(id:Int) = {
    postJson("delete", writePretty(DeleteJob(id))).map(readjson[EmptyResponse])
  }

  def list() = getJson("list").map(readjson[JobList])

  def get(id:Int) = getJson("get", Map("job_id" -> id.toString)).map(readjson[JobWithId])
}

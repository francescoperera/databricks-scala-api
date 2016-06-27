package io.findify.databricks.calls

import io.findify.databricks.Auth
import io.findify.databricks.api._
import spray.json._
import scala.concurrent.Future

/**
  * Created by shutty on 6/20/16.
  */
class Jobs(auth:Auth) extends ApiCall(s"https://${auth.hostname}/api/2.0/jobs", auth) {
  import io.findify.databricks.api.DatabricksJsonProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  def create(job:Job) = {
    postJson("create", job.toJson).map(_.convertTo[CreateJobResponse])
  }

  def create2(job:Job) = {
    postJson("create", job.toJson).map(_.prettyPrint)
  }

  def reset(id:Int, job:Job) = {
    postJson("reset", ResetJob(id, job).toJson).map(_.convertTo[EmptyResponse])
  }

  def delete(id:Int) = {
    postJson("delete", DeleteJob(id).toJson).map(_.convertTo[EmptyResponse])
  }

  def list() = getJson("list").map(_.convertTo[JobList])

  def get(id:Int) = getJson("get", Map("job_id" -> id.toString)).map(_.convertTo[JobWithId])
}

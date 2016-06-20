package io.findify.databricks.calls

import akka.http.scaladsl.HttpExt
import io.findify.databricks.Auth
import io.findify.databricks.api.{CreateJobResponse, Job, ResetJob}
import spray.json._

import scala.concurrent.Future

/**
  * Created by shutty on 6/20/16.
  */
class Jobs(auth:Auth, client:HttpExt) extends ApiCall(s"https://${auth.hostname}/api/2.0/dbfs", client, auth) {
  import io.findify.databricks.api.DatabricksJsonProtocol._
  import client.system.dispatcher
  def create(job:Job):Future[Int] = {
    postJson("/create", job.toJson).map(_.convertTo[CreateJobResponse]).map(_.job_id)
  }

  def reset(id:Int, job:Job) = {
    postJson("/reset", ResetJob(id, job).toJson)
  }
}

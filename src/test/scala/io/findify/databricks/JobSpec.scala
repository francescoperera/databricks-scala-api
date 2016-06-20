package io.findify.databricks

import io.findify.databricks.api._
import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by shutty on 6/20/16.
  */
class JobSpec extends FlatSpec with DatabricksSupport with Matchers {
  val job = Job(
    name = s"test_dummy",
    new_cluster = Some(Cluster(
      spark_version = "1.6.1-ubuntu15.10-hadoop2",
      node_type_id = Some("memory-optimized"),
      aws_attributes = AWSAttributes(availability = Some("ON_DEMAND"), zone_id = Some("us-east-1a")),
      num_workers = 1
    )),
    libraries = Some(List(
      Library(jar = Some(s"dbfs:/FileStore/job-jars/recommender_2.10.jar")),
      Library(maven = Some(Maven("com.github.dcshock:consul-rest-client:0.10"))),
      Library(maven = Some(Maven("joda-time:joda-time:2.9.4"))),
      Library(maven = Some(Maven("io.spray:spray-json_2.10:1.3.2"))),
      Library(maven = Some(Maven("com.datastax.spark:spark-cassandra-connector_2.10:1.6.0")))
    )),
    spark_jar_task = Some(JarTask(
      jar_uri = Some(s"recommender_2.10.jar"),
      main_class_name = s"io.findify.recommender.runner.Main"
    )),
    email_notifications = Notifications(Some(List("monitoring@findify.io"))),
    timeout_seconds = 10000,
    max_retries = Some(3),
    schedule = Some(CronSchedule(
      quartz_cron_expression = s"0 0 0 * * ?",
      timezone_id = "UTC"
    ))
  )
  def jobid = Await.result(client.jobs.list(), 10.seconds).jobs.find(_.settings.name == "test_dummy").map(_.job_id).get

  "Databricks API / Jobs" should "create job" in {
    val result = Await.result(client.jobs.create(job), 10.seconds)
    assert(result.job_id > 0)
  }

  it should "update job" in {
    val id = jobid
    val result = Await.result(client.jobs.reset(id, job), 10.seconds)
    assert(result == EmptyResponse())
  }

  it should "get job info" in {
    val id = jobid
    val result = Await.result(client.jobs.get(id), 10.seconds)
    assert(result.settings == job)

  }

  it should "delete job" in {
    val id = jobid
    val result = Await.result(client.jobs.delete(id), 10.seconds)
    assert(result == EmptyResponse())

  }
}

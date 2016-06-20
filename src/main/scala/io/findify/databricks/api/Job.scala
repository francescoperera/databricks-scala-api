package io.findify.databricks.api

/**
  * Created by shutty on 5/25/16.
  */
case class AWSAttributes(zone_id:Option[String], availability:Option[String])
case class Cluster(spark_version:String, node_type_id:Option[String], aws_attributes:AWSAttributes, num_workers:Int)
case class Maven(coordinates:String, exclusions:Option[List[String]] = None, repo:Option[String] = None)
case class Library(jar:Option[String] = None,maven:Option[Maven] = None)
case class JarTask(jar_uri:Option[String] = None, main_class_name:String, parameters:Option[List[String]] = None)
case class Notifications(on_failure:Option[List[String]] = None, on_start:Option[List[String]] = None, on_success:Option[List[String]] = None)
case class CronSchedule(quartz_cron_expression:String, timezone_id:String)
case class Job(name:String,
               existing_cluster_id:Option[String] = None,
               new_cluster:Option[Cluster] = None,
               libraries:Option[List[Library]] = None,
               spark_jar_task:Option[JarTask],
               email_notifications:Notifications,
               timeout_seconds:Int = 0,
               schedule:Option[CronSchedule] = None,
               max_retries:Option[Int] = None
              )

case class CreateJobResponse(job_id:Int)
case class JobWithId(job_id:Int, settings:Job)
case class ResetJob(job_id:Int, new_settings:Job)
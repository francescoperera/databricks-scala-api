package io.findify.databricks.api

/**
  * Created by shutty on 5/25/16.
  */
case class AWSAttributes(zone_id:String, availability:String)
case class Cluster(spark_version:String, node_type_id:String, aws_attributes:AWSAttributes, num_workers:Int)
case class Maven(coordinates:String, exclusions:Option[List[String]] = None, repo:Option[String] = None)
case class Library(jar:Option[String] = None,maven:Option[Maven] = None)
case class JarTask(jar_uri:Option[String] = None, main_class_name:String, parameters:List[String] = Nil)
case class Notifications(on_failure:String)
case class CronSchedule(quartz_cron_expression:String, timezone_id:String)
case class Job(name:String,
               existing_cluster_id:Option[String] = None,
               new_cluster:Option[Cluster] = None,
               libraries:Option[List[Library]] = None,
               spark_jar_task:JarTask,
               email_notifications:Notifications,
               timeout_seconds:Int = 0,
               schedule:Option[CronSchedule] = None,
               max_retries:Int = 0
              )

case class CreateJobResponse(job_id:Int)

case class ResetJob(job_id:Int, new_settings:Job)
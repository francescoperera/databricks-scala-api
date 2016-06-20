package io.findify.databricks.api

/**
  * Created by shutty on 6/20/16.
  */
case class JobList(jobs:List[JobListEntry])
case class JobListEntry(job_id:Int, settings:Job)

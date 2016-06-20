package io.findify.databricks.api

import spray.json.DefaultJsonProtocol

/**
  * Created by shutty on 5/25/16.
  */
object DatabricksJsonProtocol extends DefaultJsonProtocol {
  implicit val createFmt = jsonFormat2(CreateStream)
  implicit val createResponseFmt = jsonFormat1(CreateStreamResponse)
  implicit val addFmt = jsonFormat2(AddBlock.apply)
  implicit val closeFmt = jsonFormat1(CloseStream)

  implicit val awsAttrFmt = jsonFormat2(AWSAttributes)
  implicit val clusterFmt = jsonFormat4(Cluster)
  implicit val mavenFmt = jsonFormat3(Maven)
  implicit val libraryFmt = jsonFormat2(Library)
  implicit val jarFmt = jsonFormat3(JarTask)
  implicit val notifyFmt = jsonFormat1(Notifications)
  implicit val scheduleFmt = jsonFormat2(CronSchedule)
  implicit val jobFmt = jsonFormat9(Job)
  implicit val jobresetFmt = jsonFormat2(ResetJob)
  implicit val createJobResponseFmt = jsonFormat1(CreateJobResponse)
  implicit val readBlockResponseFmt = jsonFormat2(ReadBlockResponse)
  implicit val statusResponse = jsonFormat3(StatusResponse)
  implicit val exceptionFmt = jsonFormat2(DatabricksException)
  implicit val deleteFmt = jsonFormat2(Delete)
  implicit val emptyFmt = jsonFormat0(EmptyResponse)
}

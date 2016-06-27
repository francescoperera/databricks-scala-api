package io.findify.databricks.calls

import java.nio.ByteBuffer
import java.util.Base64
import io.findify.databricks.Auth
import io.findify.databricks.api._
import spray.json._

import scala.concurrent.Future
/**
  * Created by shutty on 5/26/16.
  */
class Dbfs(auth:Auth) extends ApiCall(s"https://${auth.hostname}/api/2.0/dbfs", auth) {
  import DatabricksJsonProtocol._
  import scala.concurrent.ExecutionContext.Implicits.global

  def put(data:Array[Byte], path:String):Future[EmptyResponse] = {
    def upload(chunks: Array[Array[Byte]], offset:Int, handle:Int):Future[JsValue] = {
      if (offset < chunks.length)
        postJson("add-block", AddBlock(chunks(offset), handle).toJson).flatMap(_ => upload(chunks, offset + 1, handle))
      else
        Future.successful("{}".parseJson)
    }
    logger.debug(s"Uploading ${data.length} bytes to $path")
    for (
      createStream <- postJson("create", CreateStream(path, overwrite = true).toJson).map(_.convertTo[CreateStreamResponse]);
      _ <- upload(data.sliding(10000, 10000).toArray, 0, createStream.handle);
      closeStream <- postJson("close", CloseStream(createStream.handle).toJson)
    ) yield {
      logger.debug("Upload finished")
      EmptyResponse()
    }
  }

  def read(length:Int, offset:Int, path:String):Future[Array[Byte]] = {
    getJson("read", Map("length" -> length.toString, "offset" -> offset.toString, "path" -> path))
      .map(_.convertTo[ReadBlockResponse].data)
      .map(Base64.getDecoder.decode)
  }

  def getStatus(path:String):Future[StatusResponse] = {
    getJson("get-status", Map("path" -> path))
      .map(_.convertTo[Either[StatusResponse, DatabricksException]])
      .map {
        case Left(status) => status
        case Right(ex) => throw ex
      }
  }

  def delete(del:Delete):Future[EmptyResponse] = {
    postJson("delete", del.toJson)
      .map(_.convertTo[Either[EmptyResponse, DatabricksException]])
      .map {
        case Left(empty) => empty
        case Right(ex) => throw ex
      }

  }
}

package io.findify.databricks.calls

import java.nio.ByteBuffer
import java.util.Base64

import akka.http.scaladsl.HttpExt
import io.findify.databricks.Auth
import io.findify.databricks.api._
import spray.json._

import scala.concurrent.Future
/**
  * Created by shutty on 5/26/16.
  */
class Dbfs(auth:Auth, client:HttpExt) extends ApiCall(s"https://${auth.hostname}/api/2.0/dbfs", client, auth) {
  import DatabricksJsonProtocol._
  import client.system.dispatcher
  def put(data:Array[Byte], path:String):Future[Int] = {
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
      createStream.handle
    }
  }

  def read(length:Int, offset:Int, path:String):Future[Array[Byte]] = {
    get("read", Map("length" -> length.toString, "offset" -> offset.toString, "path" -> path))
      .map(_.utf8String.parseJson.convertTo[ReadBlockResponse].data)
      .map(Base64.getDecoder.decode)
  }

  def getStatus(path:String) = {
    get("get-status", Map("path" -> path))
      .map(_.utf8String.parseJson.convertTo[Either[StatusResponse, DatabricksException]])
      .map {
        case Left(status) => status
        case Right(ex) => throw ex
      }
  }

  def getStatus2(path:String) = {
    get("get-status", Map("path" -> path))
      .map(_.utf8String)
  }
}

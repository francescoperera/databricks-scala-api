package io.findify.databricks.calls

import java.nio.ByteBuffer
import java.util.Base64

import io.findify.databricks.Auth
import io.findify.databricks.api._
import org.asynchttpclient.AsyncHttpClient
import org.json4s._
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{write, read => jsonread}

import scala.concurrent.Future
/**
  * Created by shutty on 5/26/16.
  */
class Dbfs (auth:Auth, client:AsyncHttpClient) extends ApiCall(s"https://${auth.hostname}/api/2.0/dbfs", auth, client) {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val formats = Serialization.formats(NoTypeHints)

  def put(data:Array[Byte], path:String):Future[EmptyResponse] = {
    def upload(chunks: Array[Array[Byte]], offset:Int, handle:Int):Future[String] = {
      if (offset < chunks.length) {
        logger.info(s"Upload progress: $offset chunks of ${chunks.length}")
        postJson("add-block", write(AddBlock(chunks(offset), handle))).flatMap(_ => upload(chunks, offset + 1, handle))
      } else
        Future.successful("{}")
    }
    logger.debug(s"Uploading ${data.length} bytes to $path")
    for (
      createStream <- postJson("create", write(CreateStream(path, overwrite = true))).map(jsonread[CreateStreamResponse]);
      _ <- upload(data.sliding(100000, 100000).toArray, 0, createStream.handle);
      closeStream <- postJson("close", write(CloseStream(createStream.handle)))
    ) yield {
      logger.debug("Upload finished")
      EmptyResponse()
    }
  }

  def read(length:Int, offset:Int, path:String):Future[Array[Byte]] = {
    getJson("read", Map("length" -> length.toString, "offset" -> offset.toString, "path" -> path))
      .map(x => jsonread[ReadBlockResponse](x).data)
      .map(Base64.getDecoder.decode)
  }

  def getStatus(path:String):Future[StatusResponse] = {
    getJson("get-status", Map("path" -> path))
      .map(jsonread[Either[StatusResponse, DatabricksException]])
      .map {
        case Left(status) => status
        case Right(ex) => throw ex
      }
  }

  def delete(del:Delete):Future[EmptyResponse] = {
    postJson("delete", write(del))
      .map(jsonread[Either[EmptyResponse, DatabricksException]])
      .map {
        case Left(empty) => empty
        case Right(ex) => throw ex
      }
  }

  def list(path:String) = {
    getJson("list", Map("path" -> path))
      .map(jsonread[FileList])
  }

  def mkdir(path:String) = {
    postJson("mkdir", write(Mkdir(path)))
      .map(jsonread[Either[EmptyResponse, DatabricksException]])
      .map {
        case Left(empty) => empty
        case Right(ex) => throw ex
      }
  }
}

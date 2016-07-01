package io.findify.databricks

import java.nio.ByteBuffer

import io.findify.databricks.api.{DatabricksException, Delete, EmptyResponse, StatusResponse}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.util.{Failure, Random, Try}
import scala.concurrent.duration._
/**
  * Created by shutty on 6/20/16.
  */
class DbfsSpec extends FlatSpec with DatabricksSupport with Matchers {

  "Databricks API / Dbfs" should "upload a sample file" in {
    val data = "hello".getBytes()
    val result = Await.result(client.dbfs.put(data, "/test-upload"), 60.seconds)
    assert(result == EmptyResponse())
  }

  it should "read the file" in {
    val read = new String(Await.result(client.dbfs.read(30000, 0, "/test-upload"), 60.seconds))
    assert(read == "hello")
  }

  it should "get file info for existing files" in {
    val info = Await.result(client.dbfs.getStatus("/test-upload"), 10.seconds)
    assert(info == StatusResponse(5, false, "/test-upload"))
  }

  it should "fail on non-existent files" in {
    val info = Try(Await.result(client.dbfs.getStatus("/not-exists"), 10.seconds))
    assert(info.isFailure)
  }

  it should "list root" in {
    val info = Await.result(client.dbfs.list("/"), 10.seconds)
    assert(info.files.exists(_.path.contains("test-upload")))
  }

  it should "create folder" in {
    val info = Await.result(client.dbfs.mkdir("/test-dir"), 10.seconds)
    assert(info == EmptyResponse())
  }

  it should "remove folder" in {
    val info = Await.result(client.dbfs.delete(Delete("/test-dir", true)), 10.seconds)
    assert(info == EmptyResponse())
  }

  it should "remove file" in {
    val remove = Await.result(client.dbfs.delete(Delete("/test-upload")), 10.seconds)
    val info = Try(Await.result(client.dbfs.getStatus("/test-upload"), 10.seconds))
    assert(info.isFailure)
  }


}

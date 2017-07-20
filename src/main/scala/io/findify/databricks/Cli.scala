package io.findify.databricks

import io.findify.databricks.api.Delete

import scala.concurrent.Await
import scala.concurrent.duration._
/**
  * Created by shutty on 6/28/16.
  */
object Cli {

  def main(args: Array[String]) = {
    //val host = Option(System.getenv("DBC_HOST")).getOrElse(throw new IllegalArgumentException("DBC_HOST is not defined"))
    val host  = "weightwatchers-poc.cloud.databricks.com"
    val user = Option(System.getenv("DATABRICKS_USERNAME")).getOrElse(throw new IllegalArgumentException("DBC_USER is not defined"))
    val pass = Option(System.getenv("DATABRICKS_PASSWORD")).getOrElse(throw new IllegalArgumentException("DBC_PASS is not defined"))
    val dbc = new Databricks(Auth(host, user, pass))

    println(args.toList)
    args.toList match {
      case "ls" :: path :: Nil =>
        Await.result(dbc.dbfs.list(path), 10.seconds).files.foreach( f => {
          println(s"${f.path}\t${f.file_size}")
        })
      case "rm" :: path :: Nil =>
        Await.result(dbc.dbfs.delete(Delete(path)), 10.seconds)
        println("done")
      case "rmdir" :: path :: Nil =>
        Await.result(dbc.dbfs.delete(Delete(path, true)), 10.seconds)
        println("done")
      case "mkdir" :: path :: Nil =>
        Await.result(dbc.dbfs.mkdir(path), 10.seconds)
        println("done")
      case _ =>
        println(args.toList)
        println("not supported")
    }
    dbc.close
  }
}

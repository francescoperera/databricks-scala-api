package io.findify.databricks

/**
  * Created by shutty on 6/20/16.
  */
trait DatabricksSupport {
  val username = System.getenv("DBUSER")
  val password = System.getenv("DBPASS")
  val host = System.getenv("DBHOST")

  lazy val client = new Databricks(Auth(host, username, password))

}

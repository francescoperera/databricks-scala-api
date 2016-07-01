package io.findify.databricks

import io.findify.databricks.calls.{Dbfs, Jobs}
import org.asynchttpclient.{DefaultAsyncHttpClient, DefaultAsyncHttpClientConfig}

/**
  * Created by shutty on 5/24/16.
  */

class Databricks(auth:Auth) {
  private lazy val client = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().setCompressionEnforced(true).setKeepAlive(true).build())
  val dbfs = new Dbfs(auth, client)
  val jobs = new Jobs(auth, client)
  def close = client.close()
}

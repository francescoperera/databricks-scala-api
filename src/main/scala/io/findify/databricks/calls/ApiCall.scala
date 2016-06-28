package io.findify.databricks.calls

import com.typesafe.scalalogging.slf4j.LazyLogging
import io.findify.databricks.Auth
import org.asynchttpclient.util.{AuthenticatorUtils, Base64}
import org.asynchttpclient._

import scala.collection.JavaConverters._
import scala.compat.java8.FutureConverters._
import scala.concurrent.Future

/**
  * Created by shutty on 6/20/16.
  */
abstract class ApiCall(endpoint:String, auth:Auth, client:AsyncHttpClient) extends LazyLogging {
  import scala.concurrent.ExecutionContext.Implicits.global

  protected def get(method:String, params:Map[String,String] = Map()):Future[String] = {
    logger.info(s"Sending GET request to $endpoint/$method, params = $params")
    val request = new RequestBuilder()
      .setUrl(s"$endpoint/$method")
      .setQueryParams(params.map { case (k, v) => new Param(k, v) }.toList.asJava)
      .setMethod("GET")
      .setHeader("Authorization", AuthenticatorUtils.perRequestAuthorizationHeader(
        new Realm.Builder(auth.username, auth.password).setScheme(Realm.AuthScheme.BASIC).setUsePreemptiveAuth(true).build())
      )
    toScala(client.prepareRequest(request).execute().toCompletableFuture).map(response => {
      logger.debug(s"Got response: ${response.getStatusCode}")
      response.getResponseBody
    })
  }

  protected def getJson(method:String, params:Map[String,String] = Map()):Future[String] = {
    get(method, params)
  }

  protected def postJson(method:String, json:String):Future[String] = {
      val request = new RequestBuilder()
        .setUrl(s"$endpoint/$method")
        .setBody(json)
        .setMethod("POST")
        .setHeader("Authorization", AuthenticatorUtils.perRequestAuthorizationHeader(
          new Realm.Builder(auth.username, auth.password).setScheme(Realm.AuthScheme.BASIC).setUsePreemptiveAuth(true).build())
        )

    logger.info(s"Sending POST request to $endpoint/$method")
    logger.debug("data = ${json.take(80)}...")
    toScala(client.prepareRequest(request).execute().toCompletableFuture).map(response => {
      logger.debug(s"got response: ${response.getResponseBody}")
      response.getResponseBody
    })
  }
}

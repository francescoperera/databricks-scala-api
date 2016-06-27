package io.findify.databricks.calls

import com.typesafe.scalalogging.slf4j.LazyLogging
import io.findify.databricks.Auth
import org.asynchttpclient.util.{AuthenticatorUtils, Base64}
import org.asynchttpclient.{DefaultAsyncHttpClient, Param, Realm, RequestBuilder}
import spray.json._

import scala.collection.JavaConverters._
import scala.compat.java8.FutureConverters._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shutty on 6/20/16.
  */
abstract class ApiCall(endpoint:String, auth:Auth) extends LazyLogging {
  val client = new DefaultAsyncHttpClient()
  import scala.concurrent.ExecutionContext.Implicits.global

  protected def get(method:String, params:Map[String,String] = Map()):Future[String] = {
    logger.debug(s"Sending GET request to $endpoint/$method, params = $params")
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

  protected def getJson(method:String, params:Map[String,String] = Map()):Future[JsValue] = {
    get(method, params).map(_.parseJson)
  }

  protected def postJson(method:String, data:JsValue):Future[JsValue] = {
      val request = new RequestBuilder()
        .setUrl(s"$endpoint/$method")
        .setBody(data.compactPrint)
        .setMethod("POST")
        .setHeader("Authorization", AuthenticatorUtils.perRequestAuthorizationHeader(
          new Realm.Builder(auth.username, auth.password).setScheme(Realm.AuthScheme.BASIC).setUsePreemptiveAuth(true).build())
        )

    logger.debug(s"Sending POST request to $endpoint/$method, data = ${data.compactPrint}")
    toScala(client.prepareRequest(request).execute().toCompletableFuture).map(response => {
      logger.debug(s"got response: ${response.getResponseBody}")
      response.getResponseBody.parseJson
    })
  }
}

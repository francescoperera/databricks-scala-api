package io.findify.databricks.calls

import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}
import akka.http.scaladsl.model.{HttpHeader, HttpMethods, HttpRequest, Uri}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.typesafe.scalalogging.LazyLogging
import io.findify.databricks.Auth
import spray.json._

import scala.concurrent.Future

/**
  * Created by shutty on 6/20/16.
  */
abstract class ApiCall(endpoint:String, client:HttpExt, auth:Auth) extends LazyLogging {
  implicit val system = client.system
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  protected def get(method:String, params:Map[String,String] = Map()) = {
    logger.debug(s"Sending GET request to $endpoint/put, params = $params")
    client.singleRequest(HttpRequest(
      uri = Uri(s"$endpoint/$method").withQuery(Query(params)),
      method = HttpMethods.GET,
      headers = List[HttpHeader](Authorization(BasicHttpCredentials(auth.username, auth.password)))
    )).flatMap( _.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
  }
  protected def getJson(method:String, params:Map[String,String] = Map()):Future[JsValue] = {
    get(method, params).map(_.utf8String.parseJson)
  }

  protected def postJson(method:String, data:JsValue):Future[JsValue] = {
    logger.debug(s"Sending POST request to $endpoint/put")
    client.singleRequest(HttpRequest(
      uri = s"$endpoint/$method",
      method = HttpMethods.POST,
      entity = data.compactPrint,
      headers = List[HttpHeader](Authorization(BasicHttpCredentials(auth.username, auth.password)))
    ))
      .map( _.entity.dataBytes.runFold(ByteString(""))(_ ++ _))
      .flatMap(_.map(_.utf8String.parseJson))
  }
}

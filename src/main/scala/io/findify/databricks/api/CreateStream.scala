package io.findify.databricks.api

/**
  * Created by shutty on 5/25/16.
  */
case class CreateStream(path:String, overwrite:Boolean)
case class CreateStreamResponse(handle:Int)

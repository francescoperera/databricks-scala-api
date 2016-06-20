package io.findify.databricks.api

/**
  * Created by shutty on 6/20/16.
  */
case class DatabricksException(error_code:String, message:String) extends Exception(error_code)

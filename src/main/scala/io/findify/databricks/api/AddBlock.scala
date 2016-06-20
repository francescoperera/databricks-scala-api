package io.findify.databricks.api

import java.util.Base64

/**
  * Created by shutty on 5/25/16.
  */
case class AddBlock(data:String, handle:Int)
object AddBlock {
  def apply(data:Array[Byte], handle:Int, x:Int=1) = new AddBlock(Base64.getEncoder.encodeToString(data), handle)
}

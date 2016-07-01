package io.findify.databricks.api

/**
  * Created by shutty on 6/30/16.
  */
case class FileListEntry(path:String, is_dir:Boolean, file_size:Int)
case class FileList(files:List[FileListEntry])

# Databricks API for Scala

This tiny library wraps official Databricks API v2.0 into an easy to use tool to do groundwork for uploading your
code to databricks and creating jobs there. Supported API methods are:
* Dbfs: put, read, getStatus, delete
* Jobs: create, reset, delete, get, list

Other methods may be implemented in the latest versions of the library.

### Installation

for SBT, add to your `build.sbt`:

    libraryDependencies += "io.findify" %% "databricks-scala-api" % "0.2.0"

The library supports both scala 2.10 and 2.11 (but keep in mind that databricks in 2016 still does not support 2.11).


### Usage

Uploading jar to databricks dbfs:

    val dbc = new Databricks(Auth(host, user, pass))
    val jar = Files.readAllBytes(Paths.get("path_to_jar.jar"))
    dbc.dbfs.put(jar, s"/FileStore/job-jars/foo.jar")

all the API methods return `Future`, but some of the replies are empty on success.

Updating job definition:

      dbc.jobs.reset(123, Job(
        name = "hello-world",
        new_cluster = Some(Cluster(
          spark_version = "1.6.1-ubuntu15.10-hadoop2",
          node_type_id = Some("memory-optimized"),
          aws_attributes = AWSAttributes(availability = Some("ON_DEMAND"), zone_id = Some("us-east-1a")),
          num_workers = 1
        )),
        libraries = Some(List(
          Library(jar = Some(s"dbfs:/FileStore/job-jars/hello-world.jar")),
          Library(maven = Some(Maven("ch.qos.logback:logback-classic:1.1.7"))),
          Library(maven = Some(Maven("org.slf4j:slf4j-api:1.7.21"))),
          Library(maven = Some(Maven("com.logentries:logentries-appender:1.1.32")))
        )),
        spark_jar_task = Some(JarTask(
          jar_uri = Some(s"hello-world.jar"),
          main_class_name = s"com.example.Main"
        )),
        email_notifications = Notifications(Some(List("monitoring@example.com"))),
        timeout_seconds = 300,
        max_retries = Some(3),
        schedule = Some(CronSchedule(
          quartz_cron_expression = s"0 0 6 * * ?",
          timezone_id = "UTC"
        ))
      ))

### Licence

The MIT License (MIT)

Copyright (c) 2016 Findify AB

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
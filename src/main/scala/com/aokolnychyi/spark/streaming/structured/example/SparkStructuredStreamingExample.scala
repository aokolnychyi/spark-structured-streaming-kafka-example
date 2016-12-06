package com.aokolnychyi.spark.streaming.structured.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.ProcessingTime

object SparkStructuredStreamingExample {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Spark Structured Streaming Example")
      .master("local[4]")
      .getOrCreate()

    import spark.implicits._

    val df = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "spark_sql_test_topic")
      .load()

    val values = df.selectExpr("CAST(value AS STRING)").as[String]

    values.writeStream
      .trigger(ProcessingTime("5 seconds"))
      .outputMode("append")
      .format("console")
      .start()
      .awaitTermination()
  }

}

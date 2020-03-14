package com.gilcu2.flights

import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}

object Flights {

  def load(path: String)(
    implicit spark: SparkSession): DataFrame = {

    val schema = StructType(Array(
      StructField("_id", StringType, true),
      StructField("dofW", IntegerType, true),
      StructField("carrier", StringType, true),
      StructField("origin", StringType, true),
      StructField("dest", StringType, true),
      StructField("crsdephour", IntegerType, true),
      StructField("crsdeptime", DoubleType, true),
      StructField("crsarrtime", DoubleType, true),
      StructField("crselapsedtime", DoubleType, true),
      StructField("label", DoubleType, true),
      StructField("pred_dtree", DoubleType, true)
    ))

    spark.read.format("json").option("inferSchema", "false").
      schema(schema).load(path)

  }

}

package com.gilcu2.flights

import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

case class Flight(id: String, dofW: Integer, carrier: String,
                  origin: String, dest: String, crsdephour: Integer, crsdeptime: Double,
                  depdelay: Double, crsarrtime: Double, arrdelay: Double,
                  crselapsedtime: Double, dist: Double) extends Serializable

object Flights {

  val idField = "id"
  val dofWField = "dofW"
  val carrierField = "carrier"
  val originField = "origin"
  val destField = "dest"
  val crsdephourField = "crsdephour"
  val crsdeptimeField = "crsdeptime"
  val depdelayField = "depdelay"
  val crsarrtimeField = "crsarrtime"
  val arrdelayField = "arrdelay"
  val crselapsedtimeField = "crselapsedtime"
  val distField = "dist"

  def load(source: Dataset[String])(
    implicit spark: SparkSession): Dataset[Flight] = {
    import spark.implicits._

    val schema = StructType(Array(
      StructField(idField, StringType, true),
      StructField(dofWField, IntegerType, true),
      StructField(carrierField, StringType, true),
      StructField(originField, StringType, true),
      StructField(destField, StringType, true),
      StructField(crsdephourField, IntegerType, true),
      StructField(crsdeptimeField, DoubleType, true),
      StructField(depdelayField, DoubleType, true),
      StructField(crsarrtimeField, DoubleType, true),
      StructField(arrdelayField, DoubleType, true),
      StructField(crselapsedtimeField, DoubleType, true),
      StructField(distField, DoubleType, true)
    ))

    spark.read
      .option("inferSchema", "false").schema(schema)
      .json(source)
      .as[Flight]

  }


}

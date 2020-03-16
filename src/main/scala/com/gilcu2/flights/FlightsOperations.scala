package com.gilcu2.flights

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.{avg, desc}

object FlightsOperations {

  def countBy(df: DataFrame, field: String): DataFrame = df.groupBy(field).count()

  def sqlQuery(df: DataFrame, query: String)(
    implicit spark: SparkSession): DataFrame = {
    df.createOrReplaceTempView("flights")
    spark.catalog.cacheTable("flights")
    spark.sql(query)
  }

  def longestDepartures(df: DataFrame)(
    implicit spark: SparkSession): DataFrame = {
    import spark.implicits._
    df.select($"carrier", $"origin", $"dest", $"depdelay", $"crsdephour")
      .filter($"depdelay" > 5)
      .orderBy(desc("depdelay"))
  }

  def aggregateAvrgQuery(df: DataFrame, groupByField:String,avrgField:String)={
    df.groupBy(groupByField).agg(avg(avrgField))
  }


}

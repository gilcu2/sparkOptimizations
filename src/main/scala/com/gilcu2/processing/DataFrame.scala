package com.gilcu2.processing

import org.apache.spark.sql.DataFrame

object DataFrame {

  def count(df: DataFrame): Long = df.count()

}

package com.gilcu2

import com.gilcu2.flights.{Flights, FlightsOperations}
import com.gilcu2.interfaces.{ConfigValuesTrait, LineArgumentValuesTrait, SparkMainTrait}
import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession
import org.rogach.scallop.ScallopConf

object FlightsMain extends SparkMainTrait {

  def process(configValues0: ConfigValuesTrait, lineArguments0: LineArgumentValuesTrait)(
    implicit spark: SparkSession): Unit = {
    import spark.implicits._

    val configValues = configValues0.asInstanceOf[ConfigValues]
    val lineArguments = lineArguments0.asInstanceOf[CommandParameterValues]

    val flightLanes = spark.read.textFile(configValues.flightsPath)
    val flights = Flights.load(flightLanes).cache()
    flights.show()

    if(lineArguments.groupByCarrier) {
      val countByCarrier = FlightsOperations.countBy(flights.toDF(),"carrier")
      countByCarrier.show()
    }

  }

  def getConfigValues(conf: Config): ConfigValuesTrait = {
    val flightsPath = conf.getString("flightsData")
    ConfigValues(flightsPath)
  }

  def getLineArgumentsValues(args: Array[String], configValues: ConfigValuesTrait): LineArgumentValuesTrait = {

    val parsedArgs = new CommandLineParameterConf(args.filter(_.nonEmpty))
    parsedArgs.verify

    val logCountsAndTimes = parsedArgs.logCountsAndTimes()
    val groupByCarrier = parsedArgs.groupByCarrier()

    CommandParameterValues(logCountsAndTimes,groupByCarrier)
  }

  class CommandLineParameterConf(arguments: Seq[String]) extends ScallopConf(arguments) {
    val logCountsAndTimes = opt[Boolean]()
    val groupByCarrier = opt[Boolean]()

  }

  case class CommandParameterValues(logCountsAndTimes: Boolean,groupByCarrier:Boolean)
    extends LineArgumentValuesTrait

  case class ConfigValues( flightsPath: String) extends ConfigValuesTrait

}

package com.gilcu2.flights

import com.gilcu2.testUtil.SparkSessionTestWrapper
import org.scalatest.{FlatSpec, FunSuite, GivenWhenThen, Matchers}
import com.gilcu2.testUtil.UtilTest._

class FlightsOperationsTest extends FlatSpec with Matchers with GivenWhenThen with SparkSessionTestWrapper {

  behavior of "Flights"

  val jsonLines =
    """
      |{"id":"ATL_BOS_2018-01-01_DL_104","fldate":"2018-01-01","month":1,"dofW":1,"carrier":"DL","src":"ATL","dst":"BOS","crsdephour":9,"crsdeptime":850,"depdelay":0.0,"crsarrtime":1116,"arrdelay":0.0,"crselapsedtime":146.0,"dist":946.0}
      |{"id":"ATL_BOS_2018-01-01_DL_1200","fldate":"2018-01-01","month":1,"dofW":1,"carrier":"DL","src":"ATL","dst":"BOS","crsdephour":11,"crsdeptime":1122,"depdelay":8.0,"crsarrtime":1349,"arrdelay":0.0,"crselapsedtime":147.0,"dist":946.0}
      |{"id":"ATL_BOS_2018-01-01_DL_1500","fldate":"2018-01-01","month":1,"dofW":1,"carrier":"AL","src":"ATL","dst":"BOS","crsdephour":14,"crsdeptime":1356,"depdelay":9.0,"crsarrtime":1623,"arrdelay":0.0,"crselapsedtime":147.0,"dist":946.0}
      |{"id":"ATL_BOS_2018-01-01_DL_1800","fldate":"2018-01-01","month":1,"dofW":1,"carrier":"AL","src":"ATL","dst":"BOS","crsdephour":16,"crsdeptime":1620,"depdelay":0.0,"crsarrtime":1851,"arrdelay":3.0,"crselapsedtime":151.0,"dist":946.0}
    """.computeCleanLines

  it should "return the number of rows" in {
    import spark.implicits._

    Given("the flights")
    val dsLines = spark.createDataset(jsonLines)
    val flights = Flights.load(dsLines)

    When("count the rows")
    val n = flights.count()

    Then("the count must be the expected")
    n shouldBe 4

  }

  it should "count the rows by carrier" in {
    import spark.implicits._

    Given("the flights")
    val dsLines = spark.createDataset(jsonLines)
    val flights = Flights.load(dsLines)

    When("count the rows by carrier")
    val countByCarrier = FlightsOperations.countBy(flights.toDF(),"carrier")
    val countByCarrierValues=countByCarrier.collect().map(x=>(x.getString(0),x.getLong(1))).toSet

    Then("results must be the expected")
    countByCarrierValues shouldBe Set(("DL",2),("AL",2))

  }

  it should "execute the query" in {
    import spark.implicits._

    Given("the flights")
    val dsLines = spark.createDataset(jsonLines)
    val flights = Flights.load(dsLines)

    And("the query")
    val query="select carrier,origin,dest, depdelay,crsdephour,dist,dofW from flights where depdelay > 5 order by depdelay desc limit 5"

    When("the query is executed")
    val queryResults = FlightsOperations.sqlQuery(flights.toDF(),query)

    Then("the count must be the expected")
    queryResults.count() shouldBe 2

  }

  it should "find the longest departures" in {
    import spark.implicits._

    Given("the flights")
    val dsLines = spark.createDataset(jsonLines)
    val flights = Flights.load(dsLines)

    When("the query is executed")
    val queryResults = FlightsOperations.longestDepartures(flights.toDF())

    Then("the count must be the expected")
    queryResults.count() shouldBe 2

  }

  it should "find the average delay by carrier" in {
    import spark.implicits._

    Given("the flights")
    val dsLines = spark.createDataset(jsonLines)
    val flights = Flights.load(dsLines)

    When("the query is executed")
    val queryResults = FlightsOperations.aggregateAvrgQuery(flights.toDF(),"carrier","depdelay")
    val resultsValues = queryResults.collect()

    Then("the count must be the expected")
    queryResults.count() shouldBe 2

  }
}

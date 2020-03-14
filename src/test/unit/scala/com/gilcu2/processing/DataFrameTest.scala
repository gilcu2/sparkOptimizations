package com.gilcu2.processing

import com.gilcu2.testUtil.SparkSessionTestWrapper
import org.scalatest.{FlatSpec, GivenWhenThen, Matchers}
import com.gilcu2.testUtil.UtilTest._

class DataFrameTest extends FlatSpec with Matchers with GivenWhenThen with SparkSessionTestWrapper {

  behavior of "DataFrame"

  val jsonLines =
    """
      |{"name":"Michael"}
      |{"name":"Andy", "age":30}
      |{"name":"Justin", "age":19}
    """.computeCleanLines

  it should "return the number of rows of the dataframe" in {
    import spark.implicits._

    Given("the dataframe")
    val dsLines = spark.createDataset(jsonLines)
    val df = spark.read.json(dsLines)

    When("count the rows")
    val n = df.count()

    Then("the count must be the expected")
    n shouldBe 3

  }

}

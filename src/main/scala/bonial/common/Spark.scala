package bonial.common

import org.apache.spark.sql.SparkSession

trait Spark{
  //val appConfig: EtlAppConfig
  val appName: String

  lazy val ss: SparkSession = SparkSession.builder
    .appName(appName)
    .config("spark.master", "local")
    .getOrCreate()
}

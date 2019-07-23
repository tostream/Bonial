package bonial.etl


import bonial.common.Entity
import org.apache.spark.sql.{SaveMode, _}

class Executor(ss: SparkSession) extends Serializable {
  def readJsonFile[T <: Entity]
  (path: String)
  (implicit encoder: Encoder[T]): Dataset[T] =
    try {
      ss
        .read
        .json(path)
        .as[T]
    } catch {
      case e: AnalysisException => ss.emptyDataset[T]
    }
  def runJob(): Unit ={

  }
}

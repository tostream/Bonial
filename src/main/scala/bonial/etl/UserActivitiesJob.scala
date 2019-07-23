/*  Bonial User Activities
//  Change history
//  --------------------------------------------------------------------
//  Date                        Change
//  --------------------------------------------------------------------
//  06/10/19    Ben Fung       initial.
//  06/10/19    Ben Fung       updated to using dataset instead of dataframe to perform the transformation.
//  --------------------------------------------------------------------
//  clicksSource: source path of brochure_clicks.json
//  entersSource: source path of enters.json
//  exitsSource: source path of exits.json
//  pageTurnSource: source path of page_turns.json
//  resultPath: result destination path
*/
package bonial.etl

import bonial.common.Spark
import bonial.model.staging.{TempEnters, TempExits, TempPageTurns}
import bonial.model.{BrochureClick, Enter, Exit, PageTurn, UserActivities}
import org.apache.spark.sql.{ Dataset}

object UserActivitiesJob extends Spark {

  val clicksSource = "C:\\Users\\BenFung\\IdeaProjects\\BenFung\\brochure_clicks.json"
  val entersSource = "C:\\Users\\BenFung\\IdeaProjects\\BenFung\\enters.json"
  val exitsSource = "C:\\Users\\BenFung\\IdeaProjects\\BenFung\\exits.json"
  val pageTurnSource = "C:\\Users\\BenFung\\IdeaProjects\\BenFung\\page_turns.json"
  val resultPath = "C:\\Users\\BenFung\\IdeaProjects\\BenFung\\result.json"

  override val appName = "UserActivities"

  def main(args: Array[String]): Unit = {
    val executor = new Executor(ss)

    import ss.implicits._
    val brochure_clicksDS = executor.readJsonFile[BrochureClick](clicksSource)
    val entersDS = executor.readJsonFile[Enter](entersSource)
    val exitsDS = executor.readJsonFile[Exit](exitsSource)
    val page_turnsDS = executor.readJsonFile[PageTurn](pageTurnSource)

    val total_enters_ds = entersDS.groupByKey(_.brochure_click_uuid).count().
      withColumnRenamed("count(1)", "count").as[TempEnters]
    val total_exits_ds = exitsDS.groupByKey(_.brochure_click_uuid).count().
      withColumnRenamed("count(1)", "count").as[TempExits]
    val total_page_turns_ds = page_turnsDS.groupByKey(_.brochure_click_uuid).count().
      withColumnRenamed("count(1)", "count").as[TempPageTurns]
    val enterCondition = brochure_clicksDS("brochure_click_uuid") === total_enters_ds("value")
    val exitsCondition = ($"_1.brochure_click_uuid") equalTo total_exits_ds("value")
    val pageTurnsCondition = ($"_1._1.brochure_click_uuid") equalTo total_page_turns_ds("value")

    val join_enters_ds = brochure_clicksDS.
      joinWith(total_enters_ds, enterCondition, "left").
      joinWith(total_exits_ds, exitsCondition, "left").
      joinWith(total_page_turns_ds, pageTurnsCondition, "left").
      map(toUserActivities)
    saveJSONFile(join_enters_ds,resultPath)
  }

  def toUserActivities(t: (((BrochureClick, TempEnters), TempExits), TempPageTurns)): UserActivities = t match {
    case (((brochure_clicks, enters), exits), null) =>
      UserActivities(
        brochure_clicks.user_ident,
        enters.count,
        if (Option(exits).map(_.count).isEmpty) Option[Long](0) else Option(exits.count),
        enters.count
      )
    case (((brochure_clicks, enters), exits), page_turns) =>
      UserActivities(
        brochure_clicks.user_ident,
        enters.count,
        if (Option(exits).map(_.count).isEmpty) Option[Long](0) else Option(exits.count),
        enters.count + page_turns.count
      )
  }

  def saveJSONFile(result_df: Dataset[UserActivities], path: String): Unit = {
    result_df.coalesce(1).write.format("json").mode("overwrite").save(path)
  }
}

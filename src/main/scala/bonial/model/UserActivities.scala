package bonial.model

import bonial.common.Entity
import java.sql.Timestamp

case class UserActivities(
                           user_ident: String,
                           Total_enters: Long,
                           Total_exits: Option[Long],
                           Total_viewed: Long
                         ) extends Entity

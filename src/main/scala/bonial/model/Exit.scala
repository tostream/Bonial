package bonial.model

import bonial.common.Entity

case class Exit (
                  event: String,
                  brochure_click_uuid: String,
                  date_time: String,
                  page_view_mode: String,
                  page: String,
                  user_agent: String,
                  ip_address: String
                ) extends Entity


package bonial.model

import bonial.common.Entity

case class BrochureClick(
                          brochure_click_id: String,
                          brochure_click_uuid: String,
                          brochure_id: String,
                          date_time: String,
                          event: String,
                          ip: String,
                          location_device_lat: String,
                          location_device_lng: String,
                          location_intended_lat: String,
                          location_intended_lng: String,
                          page: String,
                          page_type: String,
                          traffic_source_type: String,
                          traffic_source_value: String,
                          treatment: String,
                          user_agent: String,
                          user_ident: String,
                          user_zip: String,
                          visit_id: String,
                          visit_origin_type: String
                        ) extends Entity
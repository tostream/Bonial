package bonial.model

import bonial.common.Entity

case class Brochure(rand_id: String,
                    brochure_content_data_id: String,
                    brochure_distribution_area_id: String,
                    brochure_image_data_id: String,
                    date_created: String,
                    date_published: String,
                    display_profile_id: String,
                    external_tracking_url_on_enter: String,
                    frontpage_factor: String,
                    group_id: String,
                    history_timestamp: String,
                    id: String,
                    last_updated: String,
                    max_distance: String,
                    operation: String,
                    page_count: String,
                    published_from: String,
                    published_until: String,
                    publisher_id: String,
                    publisher_type: String,
                    state: String,
                    title: String,
                    brochure_type: String,
                    valid_from: String,
                    valid_until: String,
                    version: String,
                    virtual_order_id: String,
                    virtual_sub_order_id: String
                   ) extends Entity
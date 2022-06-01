CREATE GLOBAL TEMPORARY TABLE TMP_APPROVER_NOMI_DETAIL
(
  claim_id                          NUMBER(18),
  claim_group_id                    NUMBER(18),
  promotion_id                      NUMBER(18),
  nomination_time_period_id         NUMBER(18),
  team_id                           NUMBER(18),
  nominee_pax_id                    NUMBER(18),
  nominator_pax_id                  NUMBER(18),
  award_amount_fixed                NUMBER(14,2) ,
  award_amount_type_fixed           NUMBER(1,0) ,
  award_amount_type_range           NUMBER(1,0) ,
  award_amount_type_none            NUMBER(1,0) ,
  award_amount                      NUMBER(18,4) ,
  min_val                           NUMBER(14,2),
  max_val                           NUMBER(14,2),
  nominator_name                    VARCHAR2(255 CHAR),
  nominee_name                      VARCHAR2(255 CHAR),
  country_name                      VARCHAR2(255 CHAR),
  org_name                          VARCHAR2(4000 CHAR),
  job_position_name                 VARCHAR2(4000 CHAR),
  department_name                   VARCHAR2(4000 CHAR),
  no_of_times_won                   NUMBER(18),
  won_flag                          NUMBER(1),
  most_recent_date_won              DATE,
  recent_time_period_won            VARCHAR2(4000 CHAR),
  submitted_date                    DATE,
  level_index                       NUMBER(1),
  calcualtor_id                     NUMBER(18),
  avator_url                        VARCHAR2(100 CHAR),
  is_cumulative_nomination          NUMBER(1),
  team_cnt                          NUMBER(18),
  is_team                           NUMBER(1),
  ecard_image                       VARCHAR2(200 CHAR),
  card_video_url                    VARCHAR2(400 CHAR),
  card_video_image_url              VARCHAR2(400 CHAR),
  submitter_comments                VARCHAR2(4000 CHAR),
  submitter_comments_lang_id        VARCHAR2(10 CHAR),
  why_attachment_name               VARCHAR2(400 CHAR),
  why_attachment_url                VARCHAR2(400 CHAR),
  certificate_id                    NUMBER(12),
  approval_status_type              VARCHAR2(18 CHAR),
  previous_level_name               VARCHAR2(4000 CHAR),
  next_level_name                   VARCHAR2(4000 CHAR),
  last_budget_request_date          DATE,
  budget_period_name                VARCHAR2(4000 CHAR),
  own_card_name                     VARCHAR2(200 CHAR),
  other_payout_quantity             NUMBER(10),
  payout_description_asset_code     VARCHAR2(1000 CHAR),
  more_info_comments                VARCHAR2(4000),
  winner_more_info_comments         VARCHAR2(4000),
  non_winner_comments               VARCHAR2(4000),
  notification_date                 DATE
)
ON COMMIT PRESERVE  ROWS
/

CREATE GLOBAL TEMPORARY TABLE TMP_NOMINATION_WINDOW_MODAL_DL
(
  claim_item_id                     NUMBER(18),
  claim_group_id                    NUMBER(18),
  approval_round                    NUMBER(18),
  time_period_name                  VARCHAR2(4000),
  promotion_name                    VARCHAR2(4000),
  win_count                         NUMBER(10),
  points_won                        NUMBER(18,4) ,
  currency_code                     VARCHAR2(3),
  cash_won                          NUMBER(18,4) ,
  team_id                           NUMBER(18),
  approver_user_id                  NUMBER(18),
  activity_id                       NUMBER(18),
  payout_description_asset_code     VARCHAR2(4000)
  )
 ON COMMIT PRESERVE  ROWS
/

CREATE GLOBAL TEMPORARY TABLE TMP_WINNER_NOMINATION_DETAIL
(
   claim_id                     NUMBER(18),
   team_id                      NUMBER(18),
   promotion_name               VARCHAR2(4000 CHAR),
   date_approved                DATE,
   currency_label               VARCHAR2(3 CHAR),
   ecard_name                   VARCHAR2(200 CHAR),
   card_video_url               VARCHAR2(400 CHAR),
   card_video_image_url         VARCHAR2(400 CHAR),
   team_nomination              NUMBER(1),
   time_period_name             VARCHAR2(4000 CHAR),
   team_name                    VARCHAR2(100 CHAR),
   nominee_first_name           VARCHAR2(255 CHAR),
   nominee_last_name            VARCHAR2(255 CHAR),
   award_amount                 NUMBER(18,4),
   nominee_hierarchy_id         NUMBER(18),
   nominee_org_name             VARCHAR2(4000 CHAR),
   nominee_job_position         VARCHAR2(4000 CHAR),
   nominee_country_code         VARCHAR2(5 CHAR),
   nominee_country_name         VARCHAR2(255 CHAR),
   nominee_avator_url           VARCHAR2(100 CHAR),
   nominator_user_id            NUMBER(18),
   nominator_first_name         VARCHAR2(255 CHAR),
   nominator_last_name          VARCHAR2(255 CHAR),
   nominator_hierarchy_id       NUMBER(18),
   nominator_org_name           VARCHAR2(4000 CHAR),
   nominator_job_position       VARCHAR2(4000 CHAR),
   nominator_country_code       VARCHAR2(5 CHAR),  
   nominator_country_name       VARCHAR2(4000 CHAR),
   nominator_avator_url         VARCHAR2(100 CHAR),
   submitter_comments           VARCHAR2(4000 CHAR),
   submitter_comments_lang_id   VARCHAR2(10 CHAR),
   date_submitted               DATE,
   certificate_id               NUMBER(18),
   own_card_name                VARCHAR2(200),
   nominator_department         VARCHAR2(100),
   nominee_department           VARCHAR2(100),
   level_number                 NUMBER(18),
   level_name                   VARCHAR2(300 CHAR),
   payout_description_asset_code VARCHAR2(4000 CHAR)
)
ON COMMIT PRESERVE  ROWS
/

CREATE GLOBAL TEMPORARY TABLE TMP_WINNER_NOMINATION_SUMMARY
(
   activity_id              NUMBER(18),
   claim_id                 NUMBER(18),
   nominee_first_name       VARCHAR2(255 CHAR),
   nominee_last_name        VARCHAR2(255 CHAR),
   nominee_user_id          NUMBER(18),
   time_period_id           NUMBER(18),
   time_period_name         VARCHAR2(4000 CHAR),
   nominee_hierarchy_id     NUMBER(18),
   nominee_org_name         VARCHAR2(4000 CHAR),
   nominee_job_position     VARCHAR2(4000 CHAR),
   nominee_country_code     VARCHAR2(5 CHAR),
   nominee_country_name     VARCHAR2(255 CHAR),
   nominee_avator_url       VARCHAR2(100 CHAR),
   nominator_first_name     VARCHAR2(255 CHAR),
   nominator_last_name      VARCHAR2(255 CHAR),
   nominator_user_id        NUMBER(18),
   nominator_hierarchy_id   NUMBER(18),
   nominator_org_name       VARCHAR2(4000 CHAR),
   nominator_job_position   VARCHAR2(4000 CHAR),
   nominator_country_code   VARCHAR2(5 CHAR),  
   nominator_country_name   VARCHAR2(4000 CHAR),
   submitter_comments       VARCHAR2(4000 CHAR),
   promotion_name           VARCHAR2(4000 CHAR),
   team_id                  NUMBER(18),
   team_name                VARCHAR2(100 CHAR),
   level_id                 NUMBER(18),
   level_name               VARCHAR2(4000 CHAR),
   promotion_id             NUMBER(18),
   date_approved            DATE,
   nominator_department     VARCHAR2(100),
   nominee_department       VARCHAR2(100)
)
ON COMMIT PRESERVE  ROWS
/

CREATE GLOBAL TEMPORARY TABLE TEMP_HIER_LEVEL_NODES
(
  parent_node_id  NUMBER(18),
  node_id         NUMBER(18),
  node_path       VARCHAR2(4000 CHAR),
  node_type_id    NUMBER(18) ,
  hier_level      NUMBER(18),
  user_id         NUMBER(18)
)
ON COMMIT PRESERVE ROWS
/

CREATE GLOBAL TEMPORARY TABLE TMP_APPROVER_CUMUL_NOMI_DETAIL
(
  claim_id                  NUMBER(18),
  nominator_pax_id          NUMBER(18),
  nominator_name            VARCHAR2(255 CHAR),
  submitted_date            DATE,
  is_cumulative_nomination  NUMBER(1),
  submitter_comments        VARCHAR2(4000 CHAR),
  why_attachment_name       VARCHAR2(400 CHAR),
  why_attachment_url        VARCHAR2(400 CHAR),
  certificate_id            NUMBER(12),
  more_info_comments        VARCHAR2(4000)
)
ON COMMIT PRESERVE  ROWS
/

CREATE GLOBAL TEMPORARY TABLE TMP_BUDGET_TRANSFER
(
  USER_NODE_ID  NUMBER
)
ON COMMIT PRESERVE ROWS
/
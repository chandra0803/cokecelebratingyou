DROP TABLE TMP_WINNER_NOMINATION_DETAIL
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
   nominee_avatar_url           VARCHAR2(100 CHAR),
   nominator_user_id            NUMBER(18),
   nominator_first_name         VARCHAR2(255 CHAR),
   nominator_last_name          VARCHAR2(255 CHAR),
   nominator_hierarchy_id       NUMBER(18),
   nominator_org_name           VARCHAR2(4000 CHAR),
   nominator_job_position       VARCHAR2(4000 CHAR),
   nominator_country_code       VARCHAR2(5 CHAR),  
   nominator_country_name       VARCHAR2(4000 CHAR),
   nominator_avatar_url         VARCHAR2(100 CHAR),
   submitter_comments           VARCHAR2(4000 CHAR),
   submitter_comments_lang_id   VARCHAR2(10 CHAR),
   date_submitted               DATE,
   certificate_id               NUMBER(18),
   own_card_name                VARCHAR2(200),
   nominator_department         VARCHAR2(100),
   nominee_department           VARCHAR2(100),
   level_number                 NUMBER(18),
   level_name                   VARCHAR2(300 CHAR),
   payout_description_asset_code VARCHAR2(4000 CHAR),
   claim_group_id               NUMBER(18),
   nominee_user_id              NUMBER(18)
)
ON COMMIT PRESERVE  ROWS
/


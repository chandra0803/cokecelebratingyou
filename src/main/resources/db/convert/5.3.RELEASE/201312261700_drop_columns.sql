ALTER TABLE ACTIVITY DROP COLUMN GAME_ATTEMPT_ID
/
ALTER TABLE budget_master DROP COLUMN budget_overdraw_approver
/
ALTER TABLE promo_approval_participant DROP COLUMN fax_number
/
ALTER TABLE merch_order DROP COLUMN pax_goal_id
/
ALTER TABLE promotion DROP COLUMN game_attempts_active
/
ALTER TABLE promotion DROP COLUMN number_attempts_given
/
ALTER TABLE promotion DROP COLUMN playing_attempts_method
/
ALTER TABLE promotion DROP COLUMN attempts_expiration_date
/
ALTER TABLE promotion DROP COLUMN low_payment_threshold
/
ALTER TABLE promotion DROP COLUMN game_payout_type
/
ALTER TABLE rpt_awardmedia_detail DROP COLUMN game_attempts
/
ALTER TABLE RPT_AWARDMEDIA_SUMMARY DROP COLUMN game_attempts
/
ALTER TABLE rpt_claim_detail DROP COLUMN submitter_game_attempts
/
ALTER TABLE rpt_claim_summary DROP COLUMN submitter_game_attempts
/
ALTER TABLE promo_payout_group DROP COLUMN SUBMITTER_GAME_ATTEMPTS
/
ALTER TABLE promo_payout_group DROP COLUMN TEAM_MEMBER_GAME_ATTEMPTS
/
ALTER TABLE RPT_QUIZ_ACTIVITY_DETAIL DROP COLUMN game_attempts
/
ALTER TABLE rpt_recognition_detail DROP COLUMN recvr_game_attempts
/
ALTER TABLE rpt_recognition_summary DROP COLUMN GAME_ATTEMPTS
/
ALTER TABLE STAGE_AWARD_LEVEL_IMPORT DROP COLUMN GAME_ATTEMPTS
/
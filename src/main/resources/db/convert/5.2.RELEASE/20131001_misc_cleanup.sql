DROP PROCEDURE prc_core_rpt_refresh
/
DROP PROCEDURE PRC_CP_RPT_REFRESH_DET
/
DROP PROCEDURE PRC_CP_RPT_REFRESH_SUM
/
DROP PROCEDURE prc_goalquest_rpt_refresh_det
/
DROP PROCEDURE prc_goalquest_rpt_refresh_sum
/
DROP PROCEDURE prc_claim_report_extract
/
DROP PROCEDURE PRC_AWARDS_EXTRACT
/
DROP PROCEDURE prc_hierarchy_load_test
/
DROP PROCEDURE prc_mc_report_extract_test
/
DROP PROCEDURE PRC_PARTICIPANT_EXTRACT
/
DROP PROCEDURE PRC_QUIZ_ANALYSIS_EXT
/
DROP PROCEDURE prc_rpt_cp_achieve_extract
/
DROP PROCEDURE prc_rpt_cp_mgr_achieve_extract
/
DROP PROCEDURE PRC_RPT_CP_PROGRESS_EXTRACT
/
DROP PROCEDURE PRC_RPT_CP_SELECTION_EXTRACT
/
DROP PROCEDURE PRC_RPT_G3_AWD_ERNG_EXTRACT
/
DROP PROCEDURE prc_rpt_goal_achieve_extract
/
DROP PROCEDURE PRC_RPT_GOAL_PROGRESS_EXTRACT
/
DROP PROCEDURE PRC_RPT_GOAL_PARTNER_EXTRACT
/
DROP PROCEDURE prc_rpt_goal_selection_extract
/
DROP PROCEDURE PRC_RPT_PROD_ACTIVITY_EXTRACT
/
DROP PROCEDURE prc_rpt_nomination_extract
/
DROP PROCEDURE PRC_RPT_PARTICIPATION_ACT_EXT
/
DROP PROCEDURE prc_rpt_points_extract
/
DROP PROCEDURE PRC_RPT_PROD_ACTIVITY_EXTRACT
/
DROP PROCEDURE PRC_RPT_PROG_REF_DTL_EXTRACT
/
DROP PROCEDURE PRC_RPT_PURL_CNTR_EXTRACT
/
DROP PROCEDURE PRC_RPT_RECOG_EXTRACT
/
DROP PROCEDURE prc_stage_award_level_test
/
DROP PROCEDURE prc_stage_referral_load
/
DROP PROCEDURE prc_stg_referral_record_insert
/
DROP PROCEDURE prc_recog_rpt_refresh
/
DROP PACKAGE pkg_quiz_extracts
/
DROP PROCEDURE prc_quiz_rpt_refresh
/
DROP PROCEDURE pkg_cert_extracts
/
DROP FUNCTION fnc_get_badge_count
/
DROP FUNCTION fnc_get_badges_count
/
DROP PACKAGE pkg_cert_extract
/
DROP PACKAGE pkg_rpt_participant_extract
/
DROP PROCEDURE prc_core_rpt_points_extract
/
DROP PROCEDURE prc_goalquest_rpt_refresh_detail
/
DROP PROCEDURE prc_goalquest_rpt_refresh_summary
/
DROP PROCEDURE prc_insert_rpt_refresh_date
/
DROP PROCEDURE prc_nom_rpt_nomination_extract
/
DROP PROCEDURE prc_rpt_goal_achievement_extract
/
DROP PROCEDURE prc_rpt_recog_purl_contrib_extract
/
DROP PROCEDURE prc_sptlght_recog_rpt_refresh
/
DROP PROCEDURE prc_stage_participant_load
/

--remove invalid picklist.nomination.promo.email.notification.type [goal_selection_survey, achiever_survey, non_achiever_survey]
delete  cms_content_data where content_id in (
  Select cd2.content_id FROM cms_content_data cd1, cms_content_data cd2
 WHERE CD1.content_id IN
          (SELECT id
             FROM cms_content
            WHERE content_key_id IN
                     (SELECT id
                        FROM cms_content_key
                       WHERE asset_id IN
                                (SELECT ID
                                   FROM cms_asset
                                  WHERE code =
                                           'picklist.promo.email.notification.type.items')))
       AND CD1.key = 'CODE'
       AND DBMS_LOB.SUBSTR (CD1.VALUE, 300, 1) in( 'achiever_survey', 'goal_selection_survey' , 'non_achiever_survey')
       AND CD1.CONTENT_ID = CD2.CONTENT_ID)
/

DELETE FROM  cms_content where id NOT in (
  Select cd1.content_id FROM cms_content_data cd1)
/

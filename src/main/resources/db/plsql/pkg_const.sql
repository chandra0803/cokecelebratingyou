CREATE OR REPLACE PACKAGE pkg_const
IS
/*********************************************************************
 Purpose: Defines system level constants 

 MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         04/12/2016  Initial version  
   nagarajs        02/03/2017  Added more constants 
   nagarajs        05/24/2017  Added more constants 
   Gorantla        03/12/2019  G6-1446  AWS - Enable Large Audience functionality.
**********************************************************************/

gc_return_code_success           CONSTANT NUMBER := 0;
gc_return_code_failure           CONSTANT NUMBER := 99;

gc_error                         CONSTANT execution_log.severity%TYPE := 'ERROR';
gc_debug                         CONSTANT execution_log.severity%TYPE := 'DEBUG';
gc_warn                          CONSTANT execution_log.severity%TYPE := 'WARN';
gc_info                          CONSTANT execution_log.severity%TYPE := 'INFO';

gc_initial_version               CONSTANT NUMBER(18) := 1;

gc_pax_status_active             CONSTANT participant.status%TYPE := 'active';
gc_pax_status_inactive           CONSTANT participant.status%TYPE := 'inactive';

gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := 'live';
gc_promotion_status_expired      CONSTANT promotion.promotion_status%TYPE := 'expired';

gc_promotion_type_nomination     CONSTANT promotion.promotion_type%TYPE := 'nomination';
gc_promotion_type_recognition    CONSTANT promotion.promotion_type%TYPE := 'recognition';

gc_award_type_cash               CONSTANT promotion.award_type%TYPE := 'cash';
gc_award_type_other              CONSTANT promotion.award_type%TYPE := 'other';
gc_award_type_points             CONSTANT promotion.award_type%TYPE := 'points';

gc_approval_status_approved      CONSTANT claim_item_approver.approval_status_type%TYPE := 'approv';
gc_approval_status_more_info     CONSTANT claim_item_approver.approval_status_type%TYPE := 'more_info';
gc_approval_status_non_winner    CONSTANT claim_item_approver.approval_status_type%TYPE := 'non_winner';
gc_approval_status_pending       CONSTANT claim_item_approver.approval_status_type%TYPE := 'pend';
gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := 'winner';

gc_journal_status_post           CONSTANT journal.status_type%TYPE := 'post';

gc_elg_type_giver                rpt_pax_promo_eligibility.giver_recvr_type%TYPE := 'giver';
gc_elg_type_receiver             rpt_pax_promo_eligibility.giver_recvr_type%TYPE := 'receiver';

gc_act_disc_sweep                CONSTANT activity.activity_discrim%TYPE := 'sweep';
gc_act_disc_sweeplevel           CONSTANT activity.activity_discrim%TYPE := 'sweeplevel';

gc_database_currency             CONSTANT VARCHAR2(30) := 'USD'; 
gc_default_date_pattern          CONSTANT VARCHAR2(30) := 'MM/DD/YYYY';
gc_default_top_n_count           CONSTANT NUMBER(18) := 20;
gc_min_search_date               CONSTANT DATE := TO_DATE('01/01/1900', 'MM/DD/YYYY'); 
gc_max_search_date               CONSTANT DATE := TO_DATE('12/31/2199', 'MM/DD/YYYY'); 

gc_null_id                       CONSTANT NUMBER(18) := -1;
gc_search_all_values             CONSTANT VARCHAR2(30) := 'all';

gc_dir_path_like_reports         CONSTANT all_directories.directory_path%TYPE := '%/work/wip/%reports';
gc_dir_path_like_datadump        CONSTANT all_directories.directory_path%TYPE := '%/rdsdbdata/datapump';  -- 03/12/2019

gc_cms_ac_promotion_badge        CONSTANT cms_asset.code%TYPE := 'promotion.badge';
gc_default_language              CONSTANT os_propertyset.entity_name%TYPE :=  'default.language'; --02/03/2017

gc_cms_key_fld_country_name      CONSTANT cms_content_data.key%TYPE := 'COUNTRY_NAME'; 
gc_cms_key_fld_promotion_name    CONSTANT cms_content_data.key%TYPE := 'PROMOTION_NAME_'; 
gc_cms_key_fld_bud_master_name   CONSTANT cms_content_data.key%TYPE := 'BUDGET_NAME'; 
gc_cms_key_fld_goal_level_name   CONSTANT cms_content_data.key%TYPE := 'GOALS'; --05/24/2017
                      --02/03/2017

gc_cms_list_approval_status      CONSTANT cms_asset.code%TYPE := 'picklist.approval.status.items';
gc_cms_list_department_type      CONSTANT cms_asset.code%TYPE := 'picklist.department.type.items';
gc_cms_list_month_name           CONSTANT cms_asset.code%TYPE := 'picklist.monthname.type.items';
gc_cms_list_nomi_behavior        CONSTANT cms_asset.code%TYPE := 'picklist.promo.nomination.behavior.items';
gc_cms_list_reco_behavior        CONSTANT cms_asset.code%TYPE := 'picklist.promo.recognition.behavior.items'; --02/03/2017
gc_cms_list_org_role             CONSTANT cms_asset.code%TYPE := 'picklist.hierarchyrole.type.items';
gc_cms_list_position_type        CONSTANT cms_asset.code%TYPE := 'picklist.positiontype.items';
gc_cms_list_prom_approval_type   CONSTANT cms_asset.code%TYPE := 'picklist.promotion.approval.option.type.items';
gc_cms_list_pax_status           CONSTANT cms_asset.code%TYPE := 'picklist.participantstatus.items';                     --05/24/2017
gc_cms_list_promo_approv_opt     CONSTANT cms_asset.code%TYPE := 'picklist.promotion.approval.option.reason.type.items'; --05/24/2017
gc_cms_list_promo_recog_beha     CONSTANT cms_asset.code%TYPE := 'picklist.promo.recognition.behavior.items';            --05/24/2017

gc_ref_text_hierarchy_rollup     CONSTANT gtt_id_list.ref_text_1%TYPE := 'hierarchy_rollup';
gc_ref_text_node_hier_level      CONSTANT gtt_id_list.ref_text_1%TYPE := 'node_hier_level';

END pkg_const;
/

CREATE OR REPLACE PACKAGE PKG_QUERY_AWARDS_RECEIVED
IS
  /******************************************************************************
  NAME:       pkg_awards
  PURPOSE:    Get Awards Received Details
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  06/19/2014           RAMKUMAR         Initial code.
  07/22/2014           VINAY            1) Removed below mentioned parameters from all procedures
                                           p_in_filterDepartments, p_in_filterPromotions, p_in_filterCountries, p_in_promotionId, p_in_department, p_in_countryId
                                        2) Updated the procedures with get_array_varchar( ) function for multi select parameters( p_in_promotionId, p_in_departments, p_in_countryIds ).
 09/25/2014           Suresh J         Bug Fix 56601 - Excluded OnTheSpot points and counts when Promotion Status is INACTIVE. This change applied in all the Org related procedures(6 procs)
 05/05/2016           Suresh J         New Nomination Report Changes
 07/04/2016          Ravi Arumugam    Bug #67200 -In the report 'Awards Received - List of Recipients' Total is not getting displayed
 12/09/2016          nagarajs         G5.6.3.3 report changes
 01/23/2017           Suresh J         Rewrote the queries for G5.6.3.3 reports performance tuning
 04/20/2017          Ravi Dhanekula    WIP 30684 FIxed issues with expired promotions not showing up.
  ******************************************************************************/
PROCEDURE prc_getAwardsSummaryPax(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_rs_getAwdSmryPaxRes OUT sys_refcursor,
    p_out_totals_data OUT sys_refcursor); --07/04/2016 Bug# 67200 Changed p_out_rs_getAwdSmryPaxResTot to p_out_totals_data
    
PROCEDURE prc_getAwardsDetailPaxRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_rs_getAwdDtlPaxRes OUT sys_refcursor,
    p_out_totals_data        OUT SYS_REFCURSOR); --07/04/2016 Bug# 67200 Changed p_out_rs_getAwdSmryPaxResTot to p_out_totals_data
    
PROCEDURE prc_getTotPtsIss_PaxBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
PROCEDURE prc_getOthAwdIss_PaxBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
PROCEDURE prc_getRecvNotRcvAwd_PaxPieRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
--PROCEDURE prc_getTotPtsIss_PerBarRes(
--    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
--    p_in_jobPosition       IN VARCHAR,
--    p_in_participantStatus IN VARCHAR,
--    p_in_localeDatePattern IN VARCHAR,
--    p_in_parentNodeId      IN VARCHAR,
--    p_in_fromDate          IN VARCHAR,
--    p_in_toDate            IN VARCHAR,
--    p_in_onTheSpotIncluded IN VARCHAR,
--    p_in_rowNumStart       IN NUMBER,
--    p_in_rowNumEnd         IN NUMBER,
--    p_in_languageCode      IN VARCHAR,
--    p_in_nodeAndBelow      IN VARCHAR,
--    p_in_departments       IN VARCHAR,
--    p_in_promotionId       IN VARCHAR,
--    p_in_award_type        IN VARCHAR,     --05/05/2016    
--    p_in_countryIds        IN VARCHAR,
--    p_in_userid            IN NUMBER,
--    p_in_sortColName       IN VARCHAR,
--    p_in_sortedBy          IN VARCHAR,
--    p_out_return_code OUT NUMBER,
--    p_out_result_set OUT sys_refcursor);
PROCEDURE prc_getAwardsSummary(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_rs_getAwdSmryResult    OUT sys_refcursor,
    p_out_rs_getAwdSmryResultTot OUT SYS_REFCURSOR);
PROCEDURE prc_getAwardsFirstDetail(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_rs_getAwdfirdtlresult    OUT sys_refcursor,
    p_out_rs_getAwdfirdtlResultTot OUT SYS_REFCURSOR);
PROCEDURE prc_getAwardsSecondDetail(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_rs_getAwdsecdtlresult    OUT sys_refcursor,
    p_out_rs_getAwdsecdtlResultTot OUT SYS_REFCURSOR);
PROCEDURE prc_getTotPtsIss_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
PROCEDURE prc_getOthAwdIss_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
PROCEDURE prc_getRecvNotRcvAwd_PieRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
PROCEDURE prc_getRcvNotRcvAwd_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
PROCEDURE prc_getPerRcvAwd_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT sys_refcursor);
END;
/

CREATE OR REPLACE PACKAGE BODY PKG_QUERY_AWARDS_RECEIVED
IS
  /******************************************************************************
  NAME:       pkg_awards
  PURPOSE:    Get Awards Received Details
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  06/19/2014           RAMKUMAR         Initial code.
  07/22/2014           VINAY            1) Removed below mentioned parameters from all procedures
                                           p_in_filterDepartments, p_in_filterPromotions, p_in_filterCountries, p_in_promotionId, p_in_department, p_in_countryId
                                        2) Updated the procedures with get_array_varchar( ) function for multi select parameters( p_in_promotionId, p_in_departments, p_in_countryIds ).
  07/23/2014           nagarajs         Bug 53061 - Fixed to return the translated promotion name from content manager.
  07/31/2014           nagarajs         Bug 54209 - Fixed prc_getAwardsDetailPaxRes for promotion name is not displaying for OTS 
  07/31/2014           nagarajs         Bug 54207 - Fixed prc_getAwardsFirstDetail for OTS count is wrong
  08/19/2014           Swati            Bug 55857 - Fixed prc_getRecvNotRcvAwd_PaxPieRes for Received vs not Received chart shows no data found 
  08/20/2014           Swati            Bug 55274 - Fixed prc_getTotPtsIss_PaxBarRes and all other reports for Awards Received-List Of Recipients, Chart Displays all the data though in the summary table a particular PAX is selected
  05/12/2015           Suresh J         SSI Award Received CR Changes    
  07/22/2015           Suresh J         Bug 63093 - Report shows wrong other value for DTGT contest 
  08/12/2015           Suresh J          Bug 63641 - In award report Sweepstakes Won value is not matching with summary table with dashboard chart
  08/28/2015           Suresh J          Bug 63665 - In Award report selecting SSI promotion name with badge is not displaying data  
  01/06/2016           nagarajs          Bug 65134 - "Awards Received - list of Recipients Report" - Barchat - "Other Awards Rec" - When No data, not rendering 0 size bar chart
  05/05/2016           Suresh J         New Nomination Report Changes  
  05/25/2016           nagarajs         Modified PRC_GETTOTPTSISS_ORGBARRES for new nomination report changes
  10/06/2016           nagarajs         New nominaion changes in multiple places
  10/18/2016           nagarajs         New nominaion changes in multiple places
  12/09/2016           nagarajs         G5.6.3.3 report changes
  04/12/2017           Suresh J         G6-1938 OTS related promotions are displayed for Award Type = Cash/Other/Plateau
  04/20/2017           Ravi Dhanekula   WIP 30684 - Fixed issue with expired promotion not showing up.
  12/01/2017           Chidamba         G6-3427 - Award received participant by organization report is showing count mismatch with summary table and pie chart
                                        [note: Removed badge_promotion and added in EXIST condition since no data is been pulled for report]
  03/02/2018           Chidamba         G6-3432 - Changes to the Report due to OTS cards Enhancement    
  ******************************************************************************/
  gc_str_token        CONSTANT VARCHAR2 (1) := '#';  --03/02/2018
  
PROCEDURE prc_getAwardsSummaryPax(
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_rs_getAwdSmryPaxRes OUT sys_refcursor,
    p_out_totals_data         OUT sys_refcursor)        --07/04/2016 Bug# 67200 Changed p_out_rs_getAwdSmryPaxResTot to p_out_totals_data
IS
  /******************************************************************************
  NAME:       prc_getAwardsSummaryPax
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  RAMKUMAR        06/19/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Ravi Arumugam   07/04/2016   Bug# 67200-In the report 'Awards Received - List of Recipients' Total is not getting displayed  
 Suresh J        05/05/2016  New nomination changes     
 nagarajs        10/06/2016  New nomination changes    
Sherif Basha     10/18/2016  Bug 69557 Other award type values are displayed under Award Type Points filter .Added filter for ssi promotions
nagarajs       12/09/2016  G5.6.3.3 report changes   
Suresh J       01/23/2017  Rewrote the queries for G5.6.3.3 reports performance tuning 
Ravi Dhanekula 01/26/2017 Fixed issue with totals data.   
Gorantla       10/23/2017 JIRA# G6-3195 Report points receive details are mismatching between summary and PAX level in Award Received report                
Chidamba       12/13/2017 G6-3158 - This report when Award Type = Plateau drill down to PAX level should show summation of Award Quantity; before the fix 
                          it shows award quantity base on levels. Since level is not shown is summary it appears like duplicate in screen.  
  ******************************************************************************/
     
    -- constants
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getAwardsSummaryPax';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);
    v_sortCol       VARCHAR2(200);
    v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016
    v_data_sort     VARCHAR2(50);
    v_fetch_count   INTEGER;
    v_parm_list     execution_log.text_line%TYPE;

    CURSOR cur_query_ref IS                                             --01/23/2017
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position,
           CAST(NULL AS NUMBER) AS user_id, 
           --CAST(NULL AS VARCHAR2(2000)) AS level_name, --12/13/2017
           CAST(NULL AS NUMBER) AS points_received_count, 
           CAST(NULL AS NUMBER) AS plateau_earned_count, 
           CAST(NULL AS NUMBER) AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER) AS on_the_spot_deposited_count, 
           CAST(NULL AS NUMBER) AS other, 
           CAST(NULL AS NUMBER) AS cash_received_count
           
    FROM dual;
 
    rec_query      cur_query_ref%ROWTYPE;    
      
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
BEGIN


   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_jobPosition>' || p_in_jobPosition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_onTheSpotIncluded >' || p_in_onTheSpotIncluded
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_award_type >' || p_in_award_type
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_userid >' || p_in_userid
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<~';

   --prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);  

-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end


  v_stage := 'Get currency value';
  BEGIN    --12/09/2016
   SELECT ccc.bpom_entered_rate
     INTO v_cash_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_userid
      AND is_primary = 1
      AND ua.country_id = c.country_id
      AND c.country_code = ccc.to_cur;
  EXCEPTION
    WHEN OTHERS THEN
      v_cash_currency_value := 1;
  END;  
  
  v_stage   := 'getAwardsSummaryPaxRes';
  v_sortCol := p_in_sortColName || ' ' || NVL(p_in_sortedBy, ' ');
  
  BEGIN
   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;
  
  
    DELETE temp_table_session;
    INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code, cms_value, key
      FROM mv_cms_asset_value
     WHERE key = 'LEVEL_NAME'
           AND locale = p_in_languageCode;     


    OPEN p_out_rs_getAwdSmryPaxRes FOR 
            SELECT s.*
            FROM (
            SELECT      COUNT(rs.participant_name) OVER() AS total_records,
                       -- calc record sort order
                        ROW_NUMBER() OVER (
                         ORDER BY
                         -- sort totals record first
                         DECODE(rs.participant_name, NULL, 0, 99),
                         -- ascending sorts
                         DECODE(v_data_sort, 'participant_name',                        rs.participant_name),
                         DECODE(v_data_sort, 'org_name',                                lower(rs.org_name)),
                         DECODE(v_data_sort, 'country',                                 lower(rs.country)),
                         DECODE(v_data_sort, 'department',                              lower(rs.department)),
                         DECODE(v_data_sort, 'job_position',                            rs.job_position),
                         DECODE(v_data_sort, 'points_received_count',                   rs.points_received_count),
                         DECODE(v_data_sort, 'plateau_earned_count',                    rs.plateau_earned_count),
                         DECODE(v_data_sort, 'sweepstakes_won_count',                   rs.sweepstakes_won_count),
                         DECODE(v_data_sort, 'on_the_spot_deposited_count',             rs.on_the_spot_deposited_count),                     
                         DECODE(v_data_sort, 'other',                                   rs.other),                     
                         DECODE(v_data_sort, 'cash_received_count',                     rs.cash_received_count),                                          
                         --DECODE(v_data_sort, 'level_name',                              rs.level_name),         --12/13/2017                                       
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/participant_name',                        rs.participant_name) DESC,
                         DECODE(v_data_sort, 'desc/org_name',                                lower(rs.org_name)) DESC,
                         DECODE(v_data_sort, 'desc/country',                                 lower(rs.country)) DESC,
                         DECODE(v_data_sort, 'desc/department',                              lower(rs.department)) DESC,
                         DECODE(v_data_sort, 'desc/job_position',                            lower(rs.job_position)) DESC,
                         DECODE(v_data_sort, 'desc/points_received_count',                   rs.points_received_count) DESC,
                         DECODE(v_data_sort, 'desc/plateau_earned_count',                    rs.plateau_earned_count) DESC,
                         DECODE(v_data_sort, 'desc/sweepstakes_won_count',                   rs.sweepstakes_won_count) DESC,
                         DECODE(v_data_sort, 'desc/on_the_spot_deposited_count',             rs.on_the_spot_deposited_count) DESC,                     
                         DECODE(v_data_sort, 'desc/other',                                   rs.other) DESC,                     
                         DECODE(v_data_sort, 'desc/cash_received_count',                     rs.cash_received_count) DESC,                                          
                         --DECODE(v_data_sort, 'desc/level_name',                              rs.level_name) DESC,  --12/13/2017                                         
                         -- default sort fields
                         LOWER(rs.participant_name),
                         rs.participant_name
                       ) -1 AS rec_seq,
                       rs.*
            FROM (
                        SELECT  rs.participant_name,
                                rs.org_name,
                                rs.country,
                                rs.department,
                                rs.job_position,
                                rs.user_id,
                               --rs.level_name,--12/13/2017
                                SUM(POINTS_RECEIVED_COUNT) POINTS_RECEIVED_COUNT,
                                SUM(PLATEAU_EARNED_COUNT) PLATEAU_EARNED_COUNT,
                                SUM(SWEEPSTAKES_WON_COUNT) SWEEPSTAKES_WON_COUNT,
                                SUM(ON_THE_SPOT_DEPOSITED_COUNT) ON_THE_SPOT_DEPOSITED_COUNT,
                                SUM(OTHER)  OTHER,
                                SUM(CASH_RECEIVED_COUNT) CASH_RECEIVED_COUNT
                FROM
                  (
                  SELECT PARTICIPANT_NAME                   AS PARTICIPANT_NAME,
                    ras.org_name                             AS ORG_NAME,
                    c.cms_value                              AS COUNTRY,
                    dpt.cms_name                             AS DEPARTMENT,
                    j.cms_name                               AS JOB_POSITION,
                    SUM (NVL (ras.points_received_count, 0)) AS POINTS_RECEIVED_COUNT,
                    SUM (NVL(plateau_earned_count,0))               AS PLATEAU_EARNED_COUNT,
                    SUM (sweepstakes_won_count)              AS SWEEPSTAKES_WON_COUNT,
                    SUM (on_the_spot_deposited_count)        AS ON_THE_SPOT_DEPOSITED_COUNT,
                    ras.user_id                              AS USER_ID
                    ,SUM(other) AS OTHER  --05/12/2015
                    ,SUM (CASH_RECEIVED_COUNT) * v_cash_currency_value    AS CASH_RECEIVED_COUNT --05/05/2016 --12/09/2016
                    ,LEVEL_NAME                              AS LEVEL_NAME          --05/05/2016
                  FROM
                    (SELECT rad.pax_last_name
                       ||', '||
                       rad.pax_first_name AS PARTICIPANT_NAME,
                      nh.node_name          AS ORG_NAME,
                      rad.country_id,
                      rad.department            AS DEPARTMENT,
                      rad.position_type         AS JOB_POSITION,
                      NVL (rad.media_amount, 0) AS POINTS_RECEIVED_COUNT,
                      plateau_earned            AS PLATEAU_EARNED_COUNT,
                      sweepstakes_won           AS SWEEPSTAKES_WON_COUNT,
                      0                         AS ON_THE_SPOT_DEPOSITED_COUNT,
                      rad.user_id,
                      RPT_AWARDMEDIA_DETAIL_ID ID
                      ,1 AS OTHER  --05/12/2015   --10/06/2016        
                     ,NVL (rad.media_amount, 0) AS CASH_RECEIVED_COUNT    --05/05/2016
                      ,(SELECT t.cms_name as level_name 
                         FROM temp_table_session t 
                         WHERE t.asset_code=rad.asset_key_level_name 
                          AND  t.cms_code = 'LEVEL_NAME') AS LEVEL_NAME --05/05/2016     
                    FROM RPT_AWARDMEDIA_DETAIL rad,
                      rpt_hierarchy nh,
                      promotion p
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_c  -- country
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                    WHERE rad.node_id         = nh.node_id
                      AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
                      AND rad.promotion_id      = p.promotion_id
                      AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                      AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = p.promotion_id )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type ) 
                      --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                      --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
                    AND ( ( p_in_nodeAndBelow = 1
                    AND (rad.node_id         IN
                              (SELECT child_node_id
                              FROM rpt_hierarchy_rollup
                              WHERE node_id IN                                                              --01/13/2017
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                              ) ))
                    OR ( p_in_nodeAndBelow = 0
                    AND rad.node_id       IN                                                                
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017

                       ))
                    --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired'))--commented out on 04/20/2017
                    --             OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016  
                    AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
                    AND NVL (TRUNC (media_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                    AND ( rad.media_amount <> 0
                    OR plateau_earned      <> 0
                    OR sweepstakes_won     <> 0)
                    AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015        
                    UNION ALL
                    SELECT rad.user_last_name
                       ||', '||
                       rad.user_first_name AS PARTICIPANT_NAME,
                      nh.node_name           AS ORG_NAME,
                      rad.country_id,
                      rad.department                  AS DEPARTMENT,
                      rad.position_type               AS JOB_POSITION,
                      NVL (rad.transaction_amount, 0) AS POINTS_RECEIVED_COUNT,
                      0                               AS PLATEAU_EARNED_COUNT,
                      0                               AS SWEEPSTAKES_WON_COUNT,
                      1                               AS ON_THE_SPOT_DEPOSITED_COUNT,
                      rad.user_id,
                      rad.qcard_detail_id ID
                      ,0 AS OTHER  --05/12/2015
                      ,0 AS CASH_RECEIVED_COUNT    --05/05/2016      
                      ,NULL AS LEVEL_NAME          --05/05/2016                        
                    FROM rpt_qcard_detail rad,
                      rpt_hierarchy nh,
                      participant p
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_c   -- country           --01/13/2017 end 
                    WHERE rad.node_id         = nh.node_id
                      AND gil_c.ref_text_1 = gc_ref_text_country_id     --01/13/2017 start
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type ) 
                    AND rad.user_id           = p.user_id
                    --01/13/2017 Start
                    AND ( ( p_in_nodeAndBelow = 1
                    AND (rad.node_id         IN
                              (SELECT child_node_id
                              FROM rpt_hierarchy_rollup
                              WHERE node_id IN                                                              --01/13/2017
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                              ) ))
                    OR ( p_in_nodeAndBelow = 0
                    AND rad.node_id       IN                                                                
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                       ))
                    --01/13/2017 End
                    --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                    AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                    AND NVL (TRUNC (trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                    AND p.status          = NVL (p_in_participantStatus, p.status)
            UNION ALL  --05/12/2015
                    SELECT rad.pax_last_name
                       ||', '||
                       rad.pax_first_name           AS PARTICIPANT_NAME,
                      nh.node_name                    AS ORG_NAME,
                      rad.country_id,
                      rad.department                  AS DEPARTMENT,
                      rad.position_type               AS JOB_POSITION,
            --          NVL (rad.payout_amount, 0)      AS POINTS_RECEIVED_COUNT,  
                      CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END AS POINTS_RECEIVED_COUNT,   
                      0                               AS PLATEAU_EARNED_COUNT,
                      0                               AS SWEEPSTAKES_WON_COUNT,
                      0                               AS ON_THE_SPOT_DEPOSITED_COUNT,   --08/12/2015
                      rad.user_id,
                      rad.rpt_ssi_award_detail_id ID
                      ,CASE WHEN rad.payout_type = 'other' THEN rad.other ELSE 0 END AS OTHER
                      ,0 AS CASH_RECEIVED_COUNT    --05/05/2016 
                      ,NULL AS LEVEL_NAME          --05/05/2016                             
                    FROM rpt_ssi_award_detail rad,
                      rpt_hierarchy nh,
                      participant p,
                      promotion promo
                    --  ,badge_promotion bp                       --01/13/2017 start   -- 10/23/2017
                     , gtt_id_list gil_dt  -- department type   
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_c  -- country
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                    WHERE rad.node_id         = nh.node_id
                    AND rad.user_id           = p.user_id
                    AND rad.promotion_id      = promo.promotion_id
                      AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                      AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                           -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) -- 10/23/2017
                              OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 10/23/2017
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type ) 
                    AND rad.payout_type = NVL(p_in_award_type,rad.payout_type) -- 10/18/2016  Bug 69557        
                    --AND (( p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016 --commented out on 04/20/2017
                    AND ( ( p_in_nodeAndBelow = 1
                    AND (rad.node_id         IN
                              (SELECT child_node_id
                              FROM rpt_hierarchy_rollup
                              WHERE node_id IN                                                              --01/13/2017
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                              ) ))
                    OR ( p_in_nodeAndBelow = 0
                    AND rad.node_id       IN                                                                
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                       ))
                    --01/13/2017 End
                    AND NVL (TRUNC (payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                    AND p.status          = NVL (p_in_participantStatus, p.status)
            --05/12/2015 Ends
                    ) ras,
                    (SELECT c.country_id,
                      v.cms_value
                    FROM mv_cms_asset_value v,
                      country c
                    WHERE v.asset_code = c.cm_asset_code
                    AND v.key          = 'COUNTRY_NAME'
                    AND v.locale       =
                      (SELECT string_val
                      FROM os_propertyset
                      WHERE entity_name = 'default.language'
                      )
                    AND v.locale <> p_in_languageCode   --02/07/2017
    --                    AND NOT EXISTS                 --02/07/2017   
    --                      (SELECT *
    --                      FROM mv_cms_asset_value,
    --                        country c
    --                      WHERE v.asset_code = c.cm_asset_code
    --                      AND v.key          = c.name_cm_key
    --                      AND locale         =p_in_languageCode
    --                      )
                    UNION ALL
                    SELECT c.country_id,
                      v.cms_value
                    FROM mv_cms_asset_value v,
                      country c
                    WHERE v.asset_code = c.cm_asset_code
                    AND v.key          = 'COUNTRY_NAME'
                    AND v.locale       = p_in_languageCode
                    ) c ,
                    (SELECT cms_code,
                      cms_name
                    FROM mv_cms_code_value vw
                    WHERE asset_code = 'picklist.department.type.items'
                    AND locale       =
                      (SELECT string_val
                      FROM os_propertyset
                      WHERE entity_name = 'default.language'
                      )
                    AND NOT EXISTS
                      (SELECT *
                      FROM mv_cms_code_value
                      WHERE asset_code = 'picklist.department.type.items'
                      AND locale       =p_in_languageCode
                      )
                    UNION ALL
                    SELECT cms_code,
                      cms_name
                    FROM mv_cms_code_value
                    WHERE asset_code = 'picklist.department.type.items'
                    AND locale       =p_in_languageCode
                    ) dpt ,
                    (SELECT cms_code,
                      cms_name
                    FROM mv_cms_code_value vw
                    WHERE asset_code = 'picklist.positiontype.items'
                    AND locale       =
                      (SELECT string_val
                      FROM os_propertyset
                      WHERE entity_name = 'default.language'
                      )
                    AND NOT EXISTS
                      (SELECT *
                      FROM mv_cms_code_value
                      WHERE asset_code = 'picklist.positiontype.items'
                      AND locale       =p_in_languageCode
                      )
                    UNION ALL
                    SELECT cms_code,
                      cms_name
                    FROM mv_cms_code_value
                    WHERE asset_code = 'picklist.positiontype.items'
                    AND locale       = p_in_languageCode
                    ) j
                  WHERE dpt.cms_code(+) = ras.department
                  AND j.cms_code(+)     = ras.job_position
                  AND ras.country_id    = c.country_id(+)
                  GROUP BY      ras.participant_name,
                                ras.org_name,
                                dpt.cms_name,
                                j.cms_name,
                                ras.user_id,
                                c.cms_value,
                                level_name                     
                    ) RS
                  GROUP BY GROUPING SETS
                          ((), (
                                rs.participant_name,
                                rs.org_name,
                                rs.country,
                                rs.department,
                                rs.job_position,
                                --rs.level_name, --12/13/2017
                                rs.user_id                    
                               )
                          ) 
            ) rs) s
                         WHERE (  s.rec_seq = 0   -- totals record
                      OR -- reduce sequenced data set to just the output page's records
                         (   s.rec_seq > p_in_rowNumStart
                         AND s.rec_seq < p_in_rowNumEnd )
                      )
                ORDER BY s.rec_seq;

   -- query totals data
   v_stage := 'FETCH p_out_rs_getAwdSmryPaxRes totals record';
   FETCH p_out_rs_getAwdSmryPaxRes INTO rec_query;
   v_fetch_count := p_out_rs_getAwdSmryPaxRes%ROWCOUNT;
   
   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT   rec_query.points_received_count         AS points_received_count,
            rec_query.plateau_earned_count          AS plateau_earned_count,
            rec_query.sweepstakes_won_count         AS sweepstakes_won_count,
            rec_query.on_the_spot_deposited_count   AS on_the_spot_deposited_count,
            rec_query.other                         AS other,
            rec_query.cash_received_count           AS cash_received_count
   FROM dual
   WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_rs_getAwdSmryPaxRes NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
     OPEN p_out_rs_getAwdSmryPaxRes FOR
    SELECT CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS NUMBER) AS user_id,
           CAST(NULL AS VARCHAR2(2000)) AS level_name,
           CAST(NULL AS NUMBER) AS points_received_count, 
           CAST(NULL AS NUMBER) AS plateau_earned_count, 
           CAST(NULL AS NUMBER) AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER) AS on_the_spot_deposited_count, 
           CAST(NULL AS NUMBER) AS other, 
           CAST(NULL AS NUMBER) AS cash_received_count,
           CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq
    FROM dual
    WHERE 0=1;
   
   END IF;

  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_rs_getAwdSmryPaxRes FOR SELECT NULL FROM DUAL;
  END;
  p_out_return_code := '00';
  
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_totals_data /*p_out_rs_getAwdSmryPaxResTot*/ FOR SELECT NULL FROM DUAL; --07/04/2016 Bug# 67200 Changed p_out_rs_getAwdSmryPaxResTot to p_out_totals_data

END; -- procedure end
--FIRST
PROCEDURE prc_getAwardsDetailPaxRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_rs_getAwdDtlPaxRes OUT sys_refcursor,
    --p_out_rs_getAwdDtlPaxResTot 
    p_out_totals_data OUT SYS_REFCURSOR) --07/04/2016 Bug# 67200 Changed p_out_rs_getAwdSmryPaxResTot to p_out_totals_data
IS
  /******************************************************************************
  NAME:       prc_getAwardsDetailPaxRes
  Date                 Author           Description
  -------     ---------------           ------------------------------------------------
  RAMKUMAR        06/19/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)   
  Suresh J        06/03/2015            Bug 62472 - Currency symbol is missing 
  Swati           06/12/2015            Bug 62752 - On the Awards Received report - do not display total for Other Value column    
  Suresh J        08/11/2015            Bug 63639 - In award report sort is not working for the column Other Value  
  Suresh J        06/21/2016            Bug 67094 - No Data found error
  Ravi Arumugam   07/04/2016           Bug 67200  -In the report 'Awards Received - List of Recipients' Total is not getting displayed  
  Suresh J        05/05/2016           New nomination changes
  nagarajs        10/06/2016           New nomination changes
  Sherif Basha     10/18/2016           Bug 69557 Other award type values are displayed under Award Type Points filter .Added filter for ssi promotions
  nagarajs        12/09/2016           G5.6.3.3 report changes
  Suresh J         01/23/2017          Rewrote the queries for G5.6.3.3 reports performance tuning  
  Suresh J         03/10/2017          G6-1927 Syntax Error  
  Gorantla         10/16/2017          G6-3156 All SSI Type contest point recive deatils are mismatching between summary and extarcy in Award Received report
  Chidamba         12/12/2017          G6-3158 fix to show all awards received while drilling down the participant. By this way Online report and extract matches. 
  ******************************************************************************/
     
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getAwardsDetailPaxRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);
    v_sortcol       VARCHAR2(200);  
    v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016

    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    v_fetch_count             INTEGER;
    v_data_sort               VARCHAR2(50);

    CURSOR cur_query_ref IS
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS DATE)           AS award_date, 
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS VARCHAR2(2000)) AS promotion_name, 
           CAST(NULL AS NUMBER)         AS points_received_count, 
           CAST(NULL AS NUMBER)         AS plateau_earned_count, 
           CAST(NULL AS NUMBER)         AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER)         AS on_the_spot_serial, 
           CAST(NULL AS VARCHAR2(2000))  AS other, 
           CAST(NULL AS NUMBER)         AS other_value, 
           CAST(NULL AS NUMBER)         AS cash_received_count, 
           CAST(NULL AS VARCHAR2(2000)) AS level_name           
    FROM dual;
    
    rec_query      cur_query_ref%ROWTYPE;    

BEGIN

-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  v_stage := 'Get currency value';
  BEGIN    --12/09/2016
   SELECT ccc.bpom_entered_rate
     INTO v_cash_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_userid
      AND is_primary = 1
      AND ua.country_id = c.country_id
      AND c.country_code = ccc.to_cur;
  EXCEPTION
    WHEN OTHERS THEN
      v_cash_currency_value := 1;
  END;  
  
  BEGIN

  DELETE temp_table_session;
  INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
  SELECT asset_code, cms_value, key
     FROM mv_cms_asset_value
     WHERE key IN ( 'LEVEL_NAME','PAYOUT_DESCRIPTION_','OTS_BATCH_NAME_') --03/02/2018 added OTS_BATCH_NAME
           AND locale = p_in_languageCode;     
  
  IF p_in_sortColName = 'OTHER_VALUE' THEN    --08/11/2015
    v_sortCol := 'TO_NUMBER(REGEXP_REPLACE('||p_in_sortColName||', ''[^0-9]+'', ''''))'|| ' ' || NVL(p_in_sortedBy, ' ');   --08/11/2015 
  ELSE   
    v_sortCol := p_in_sortColName || ' ' || NVL(p_in_sortedBy, ' ');
  END IF;
      
   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;

  v_stage   := 'getAwardsDetailPaxRes';  
  
    OPEN p_out_rs_getAwdDtlPaxRes FOR 
    SELECT s.*
    FROM ( SELECT COUNT(rs.promotion_name) OVER() AS total_records,
         rec_seq,         
         award_date, 
         participant_name, 
         org_name, 
         country, 
         department, 
         job_position, 
         promotion_name, 
         points_received_count, 
         plateau_earned_count, 
         sweepstakes_won_count, 
         on_the_spot_serial,
         other,other_value,
         cash_received_count, 
         level_name
     FROM
    (SELECT  NVL(rec_seq,0) rec_seq,
             award_date, 
             participant_name, 
             org_name, 
             country, 
             department, 
             job_position, 
             promotion_name, 
             on_the_spot_serial,
             SUM(points_received_count) points_received_count, --12/12/2017
             SUM(plateau_earned_count)  plateau_earned_count,  --12/12/2017
             SUM(sweepstakes_won_count) sweepstakes_won_count, --12/12/2017 
--             points_received_count, --12/12/2017
--             plateau_earned_count,  --12/12/2017
--             sweepstakes_won_count, --12/12/2017
             other,
             --SUM(other_value) other_value,   --03/10/2017
             other_value,    --03/10/2017
             SUM(cash_received_count) * v_cash_currency_value as cash_received_count --12/09/2016 --12/12/2017
             --cash_received_count * v_cash_currency_value as cash_received_count  --12/12/2017
             ,level_name 
      FROM  --05/12/2015
    ( SELECT -- calc record sort order 
            ROW_NUMBER() OVER (
                     ORDER BY
                     -- sort totals record first
                     DECODE(rs.promotion_name, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'award_date',                              rs.award_date),
                     DECODE(v_data_sort, 'participant_name',                        rs.participant_name),
                     DECODE(v_data_sort, 'org_name',                                lower(rs.org_name)),
                     DECODE(v_data_sort, 'country',                                 lower(rs.country)),
                     DECODE(v_data_sort, 'department',                              lower(rs.department)),
                     DECODE(v_data_sort, 'job_position',                            rs.job_position),
                     DECODE(v_data_sort, 'promotion_name',                          rs.promotion_name),
                     DECODE(v_data_sort, 'points_received_count',                   rs.points_received_count),
                     DECODE(v_data_sort, 'plateau_earned_count',                    rs.plateau_earned_count),
                     DECODE(v_data_sort, 'sweepstakes_won_count',                   rs.sweepstakes_won_count),
                     DECODE(v_data_sort, 'on_the_spot_serial',                      rs.on_the_spot_serial),                     
                     DECODE(v_data_sort, 'other',                                   rs.other),                     
                     DECODE(v_data_sort, 'other_value',                             rs.other_value),                                          
                     DECODE(v_data_sort, 'cash_received_count',                     rs.cash_received_count),                                          
                     DECODE(v_data_sort, 'level_name',                              rs.level_name),                                          
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/award_date',                              rs.award_date) DESC,
                     DECODE(v_data_sort, 'desc/participant_name',                        rs.participant_name) DESC,
                     DECODE(v_data_sort, 'desc/org_name',                                lower(rs.org_name)) DESC,
                     DECODE(v_data_sort, 'desc/country',                                 lower(rs.country)) DESC,
                     DECODE(v_data_sort, 'desc/department',                              lower(rs.department)) DESC,
                     DECODE(v_data_sort, 'desc/job_position',                            lower(rs.job_position)) DESC,
                     DECODE(v_data_sort, 'desc/promotion_name',                          rs.promotion_name) DESC,
                     DECODE(v_data_sort, 'desc/points_received_count',                   rs.points_received_count) DESC,
                     DECODE(v_data_sort, 'desc/plateau_earned_count',                    rs.plateau_earned_count) DESC,
                     DECODE(v_data_sort, 'desc/sweepstakes_won_count',                   rs.sweepstakes_won_count) DESC,
                     DECODE(v_data_sort, 'desc/on_the_spot_serial',                      rs.on_the_spot_serial) DESC,                     
                     DECODE(v_data_sort, 'desc/other',                                   rs.other) DESC,                     
                     DECODE(v_data_sort, 'desc/other_value',                             rs.other_value) DESC,                                          
                     DECODE(v_data_sort, 'desc/cash_received_count',                     rs.cash_received_count) DESC,                                          
                     DECODE(v_data_sort, 'desc/level_name',                              rs.level_name) DESC,                                          
                     -- default sort fields
                     LOWER(rs.promotion_name),
                     rs.promotion_name
                   )  AS rec_seq,   --12/12/2017 removed -1
                   rs.*
      FROM (SELECT rad.media_date AS AWARD_DATE,
                    rad.pax_last_name
                    ||', '||
                    rad.pax_first_name                                                                       AS PARTICIPANT_NAME,
                    nh.node_name                                                                               AS ORG_NAME,
                    fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)            AS COUNTRY,
                    fnc_cms_picklist_value('picklist.department.type.items',rad.department, p_in_languageCode) AS DEPARTMENT,
                    fnc_cms_picklist_value('picklist.positiontype.items',rad.position_type, p_in_languageCode) AS JOB_POSITION,
                    cp.promotion_name                                                                         AS PROMOTION_NAME,
                    --NVL(rad.media_amount,0)                                                                    AS POINTS_RECEIVED_COUNT, --05/05/2016
                    CASE WHEN rad.award_payout_type = 'points' THEN                                                                       --05/05/2016
                      NVL(rad.media_amount,0)
                      ELSE 0
                    END                                                                                        AS POINTS_RECEIVED_COUNT,
                    plateau_earned                                                                             AS PLATEAU_EARNED_COUNT,
                    sweepstakes_won                                                                            AS SWEEPSTAKES_WON_COUNT,
                    NULL                                                                                       AS ON_THE_SPOT_SERIAL
                    ,(SELECT cms_name FROM temp_table_session WHERE asset_code = rad.asset_key_level_name)  AS OTHER  --05/12/2015 --10/06/2016
            --        ,0 AS OTHER_VALUE  --05/12/2015
                    ,CASE WHEN rad.award_payout_type = 'other' THEN                                                                       --10/06/2016
                      TO_CHAR(NVL (rad.media_amount, 0) )
                      ELSE '0'
                    END  AS OTHER_VALUE  --06/03/2015
                     ,CASE WHEN rad.award_payout_type = 'cash' THEN                                                                       --05/05/2016
                      NVL (rad.media_amount, 0) 
                      ELSE 0
                     END                                                                                   AS CASH_RECEIVED_COUNT    
                      ,(SELECT t.cms_name as level_name 
                         FROM temp_table_session t 
                         WHERE t.asset_code=rad.asset_key_level_name 
                          AND  t.cms_code = 'LEVEL_NAME')                                                     AS LEVEL_NAME --05/05/2016     
      FROM RPT_AWARDMEDIA_DETAIL rad,
        rpt_hierarchy nh,
        promotion p,
        country c,
        (SELECT asset_code, cms_value promotion_name 
           FROM mv_cms_asset_value 
          WHERE key = 'PROMOTION_NAME_'
            AND locale = p_in_languageCode) cp --07/2/32014
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE rad.node_id        = nh.node_id
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
      AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
      AND rad.promotion_id     = p.promotion_id
      AND rad.country_id       = c.country_id
      AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
      AND rad.user_id          = p_in_userid
      AND p.promo_name_asset_code = cp.asset_code (+) --07/2/32014
      --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
      AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
      AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      AND (rad.media_amount <> 0
      OR SWEEPSTAKES_WON    <> 0
      OR PLATEAU_EARNED     <> 0)
      UNION ALL
      SELECT rad.trans_date AS AWARD_DATE,
        rad.user_last_name
        ||', '||
        rad.user_first_name                                                                        AS PARTICIPANT_NAME,
        nh.node_name                                                                               AS ORG_NAME,
        fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)            AS COUNTRY,
        fnc_cms_picklist_value('picklist.department.type.items',rad.department, p_in_languageCode) AS DEPARTMENT,
        fnc_cms_picklist_value('picklist.positiontype.items',rad.position_type, p_in_languageCode) AS JOB_POSITION,
        CASE WHEN rad.batch_description IS NULL THEN 
        fnc_cms_asset_code_val_extr( 'report.awards.participant', 'ONTHESPOT', p_in_languageCode ) --07/31/2014 Bug 54209    --03/02/2018
        WHEN rad.batch_description like '%'||gc_str_token THEN REPLACE(rad.batch_description,gc_str_token)                    --03/02/2018  
        ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code=rad.batch_description) END   AS PROMOTION_NAME, --03/02/2018
        NVL(rad.transaction_amount,0)                                                              AS POINTS_RECEIVED_COUNT,
        0                                                                                          AS PLATEAU_EARNED_COUNT,
        0                                                                                          AS SWEEPSTAKES_WON_COUNT,
        cert_num                                                                                   AS ON_THE_SPOT_SERIAL
        ,NULL AS OTHER  --05/12/2015 --10/06/2016
--        ,0 AS OTHER_VALUE  --05/12/2015
        ,'0' AS OTHER_VALUE  --06/03/2015
        ,0                                                                                         AS CASH_RECEIVED_COUNT    --05/05/2016
        ,NULL                                                                                      AS LEVEL_NAME --05/05/2016        
      FROM rpt_qcard_detail rad,
        rpt_hierarchy nh,
        country c,
        participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c   -- country           --01/13/2017 end 
      WHERE rad.node_id       = nh.node_id
      AND rad.user_id         = p.user_id
      AND gil_c.ref_text_1 = gc_ref_text_country_id     --01/13/2017 start
      AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
      AND gil_dt.ref_text_1 = gc_ref_text_department_type
      AND (  gil_dt.ref_text_2 = gc_search_all_values
              OR gil_dt.ref_text_2 = rad.department ) 
      AND gil_pt.ref_text_1 = gc_ref_text_position_type
      AND (  gil_pt.ref_text_2 = gc_search_all_values
             OR gil_pt.ref_text_2 = rad.position_type ) 
      AND rad.country_id      = c.country_id
      AND rad.user_id         = p_in_userid
      --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
      AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
      AND p.status            = NVL (p_in_participantStatus, p.status)
      AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
UNION ALL --05/12/2015
      SELECT rad.payout_issue_date AS AWARD_DATE,
        rad.pax_last_name
        ||', '||
        rad.pax_first_name                                                                      AS PARTICIPANT_NAME,
        nh.node_name                                                                               AS ORG_NAME,
        fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)            AS COUNTRY,
        fnc_cms_picklist_value('picklist.department.type.items',rad.department, p_in_languageCode) AS DEPARTMENT,
        fnc_cms_picklist_value('picklist.positiontype.items',rad.position_type, p_in_languageCode) AS JOB_POSITION,
        fnc_cms_asset_code_val_extr( rad.ssi_contest_name, 'CONTEST_NAME', p_in_languageCode ) AS PROMOTION_NAME,   
        CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END            AS POINTS_RECEIVED_COUNT,
        0                                                                                          AS PLATEAU_EARNED_COUNT,
        0                                                                                          AS SWEEPSTAKES_WON_COUNT,
        NULL                                                                                   AS ON_THE_SPOT_SERIAL
--        ,CASE WHEN rad.payout_type = 'other' THEN to_char(rad.other) ELSE '0' END                                     AS OTHER   --10/06/2016
         ,rad.payout_description AS OTHER --10/06/2016
--        ,CASE WHEN rad.payout_type = 'other' THEN TO_CHAR(NVL (rad.payout_amount, 0)) ELSE '0' END    AS OTHER_VALUE  --06/03/2015
        ,CASE WHEN rad.payout_type = 'other' THEN NVL(cur.currency_symbol,'$')|| TO_CHAR(NVL (rad.payout_amount, 0)) ELSE NVL(cur.currency_symbol,'$')||'0' END    AS OTHER_VALUE  --06/03/2015
        ,0                                                                                         AS CASH_RECEIVED_COUNT    --05/05/2016
        ,NULL                                                                                      AS LEVEL_NAME             --05/05/2016                         
      FROM rpt_ssi_award_detail rad,
        rpt_hierarchy nh,
        country c,
        participant p
        ,promotion promo        
        ,(
            select sc.ssi_contest_id, 
                 sc.activity_measure_type,
                 sc.activity_measure_cur_code,
                 c.currency_code,
                 c.currency_symbol 
           from ssi_contest sc,
                currency c 
           where sc.activity_measure_type = 'currency'  and 
                 UPPER(sc.activity_measure_cur_code) = c.currency_code (+) 
                 ) cur
         -- ,badge_promotion bp                       --01/13/2017 start  -- 10/16/2017
         , gtt_id_list gil_dt  -- department type   
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE rad.node_id       = nh.node_id
      AND rad.user_id         = p.user_id
      AND rad.promotion_id      = promo.promotion_id
      AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
      AND (  gil_p.ref_text_2 = gc_search_all_values
              OR gil_p.id = promo.promotion_id 
           -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) -- 10/16/2017
           OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 10/16/2017
      AND gil_c.ref_text_1 = gc_ref_text_country_id 
      AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
      AND gil_dt.ref_text_1 = gc_ref_text_department_type
      AND (  gil_dt.ref_text_2 = gc_search_all_values
              OR gil_dt.ref_text_2 = rad.department ) 
      AND gil_pt.ref_text_1 = gc_ref_text_position_type
      AND (  gil_pt.ref_text_2 = gc_search_all_values
             OR gil_pt.ref_text_2 = rad.position_type ) 
      AND rad.country_id      = c.country_id
      AND rad.user_id         = p_in_userid
      AND rad.ssi_contest_id = cur.ssi_contest_id  (+)      
      AND p.status            = NVL (p_in_participantStatus, p.status)
      AND rad.payout_type = NVL(p_in_award_type,rad.payout_type) -- 10/18/2016  Bug 69557
      --AND (( p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
      AND NVL(TRUNC(payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      ) rs ORDER BY rec_seq
     )  rs         
              GROUP BY GROUPING SETS
              ((), (rs.rec_seq,
                    rs.award_date,
                    rs.participant_name,
                    rs.org_name,
                    rs.country,
                    rs.department,
                    rs.job_position,
                    rs.promotion_name,
                    rs.level_name,                     
                    rs.on_the_spot_serial,
                    rs.other_value,   --03/10/2017
                    rs.other)
              ) 
        ) rs 
        ) s         
   ORDER BY s.rec_seq;
 
   -- query totals data
   v_stage := 'FETCH p_out_rs_getAwdDtlPaxRes totals record';
   FETCH p_out_rs_getAwdDtlPaxRes INTO rec_query;
   v_fetch_count := p_out_rs_getAwdDtlPaxRes%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT   rec_query.points_received_count         AS points_received_count,
            rec_query.plateau_earned_count          AS plateau_earned_count,
            rec_query.sweepstakes_won_count         AS sweepstakes_won_count,
            rec_query.other                         AS other,
            rec_query.other_value                   AS other_value,
            rec_query.cash_received_count           AS cash_received_count
   FROM dual
   WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
    OPEN p_out_rs_getAwdDtlPaxRes FOR
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS DATE)           AS award_date, 
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS VARCHAR2(2000)) AS promotion_name, 
           CAST(NULL AS NUMBER)         AS points_received_count, 
           CAST(NULL AS NUMBER)         AS plateau_earned_count, 
           CAST(NULL AS NUMBER)         AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER)         AS on_the_spot_serial, 
           CAST(NULL AS VARCHAR2(2000)) AS other, 
           CAST(NULL AS NUMBER)         AS other_value, 
           CAST(NULL AS NUMBER)         AS cash_received_count, 
           CAST(NULL AS VARCHAR2(2000)) AS level_name
    FROM dual
    WHERE 0=1;
   
   END IF;

   
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_rs_getAwdDtlPaxRes FOR SELECT NULL FROM DUAL;
  END;
                  
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_totals_data /*p_out_rs_getAwdDtlPaxResTot*/ FOR SELECT NULL FROM DUAL; --07/04/2016 Bug# 67200 Changed p_out_rs_getAwdSmryPaxResTot to p_out_totals_data

END; --procedure end
PROCEDURE prc_getTotPtsIss_PaxBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
/*******************************************************************************
   Purpose: 

   Person         Date         Comments
   -----------   ----------   -----------------------------------------------------
  ????             ?????      Creation
  nagarajs       12/09/2016   G5.6.3.3 report changes
  Suresh J       01/23/2017   Rewrote the queries for G5.6.3.3 reports performance tuning  
  Loganathan     03/28/2019   Bug 78869 - File loaded recognition points deposited and then 
                              reversed for a participant record is not present in the Awards Received extracts
*******************************************************************************/
IS
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getTotPtsIss_PaxBarRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);
    v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016
  
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';
      
BEGIN

-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  v_stage := 'Get currency value';
  BEGIN    --12/09/2016
   SELECT ccc.bpom_entered_rate
     INTO v_cash_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_userid
      AND is_primary = 1
      AND ua.country_id = c.country_id
      AND c.country_code = ccc.to_cur;
  EXCEPTION
    WHEN OTHERS THEN
      v_cash_currency_value := 1;
  END;  
  
  v_stage := 'getTotPtsIss_PaxBarRes';
  BEGIN
    OPEN p_out_result_set FOR SELECT PARTICIPANT_NAME, MEDIA_AMOUNT, USER_ID FROM
    (SELECT participant_name,
      SUM(media_amount) media_amount,
      user_id
    FROM
      (SELECT participant_name,
        CASE WHEN p_in_award_type IN ('points', 'cash') 
        THEN DECODE(p_in_award_type,'cash',media_amount*v_cash_currency_value, media_amount)
        ELSE 1
        END media_amount,        
        user_id
      FROM RPT_AWARDMEDIA_DETAIL rad,
        promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016  
      AND p.promotion_id = rad.promotion_id
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) --commented out on 04/20/2017
          --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
      AND TRUNC(rad.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
        AND ( ( p_in_nodeAndBelow = 1
        AND (rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) ))
                OR ( p_in_nodeAndBelow = 0
                AND rad.node_id       IN                                                                
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                   ))
      AND rad.participant_current_status = NVL(p_in_participantStatus,rad.participant_current_status)
      UNION ALL
      SELECT user_full_name AS participant_name,
        transaction_amount  AS media_amount,
        rad.user_id
      FROM rpt_qcard_detail rad,
        participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
      WHERE gil_c.ref_text_1 = gc_ref_text_country_id 
        AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
        AND gil_u.ref_text_1 = gc_ref_text_user_id 
        AND (  gil_u.ref_text_2 = gc_search_all_values
              OR gil_u.id = rad.user_id )
        AND gil_dt.ref_text_1 = gc_ref_text_department_type
        AND (  gil_dt.ref_text_2 = gc_search_all_values
              OR gil_dt.ref_text_2 = rad.department ) 
        AND gil_pt.ref_text_1 = gc_ref_text_position_type
        AND (  gil_pt.ref_text_2 = gc_search_all_values
             OR gil_pt.ref_text_2 = rad.position_type ) 
        AND rad.user_id = p.user_id
        AND TRUNC(rad.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        AND ( ( p_in_nodeAndBelow = 1
        AND (rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) ))
                OR ( p_in_nodeAndBelow = 0
                AND rad.node_id       IN                                                                
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                   ))
      --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
      AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
      AND p.status                     = NVL (p_in_participantStatus, p.status)
      AND NVL(transaction_amount,0)	   > 0 --03/28/2019  Bug 78869
        --05/12/2015
      UNION ALL
      SELECT pax_last_name||', '|| pax_first_name AS participant_name,
        CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) 
             WHEN rad.payout_type = 'other' THEN 1 --100/10/06/2016
        ELSE 0 END  AS media_amount, 
        rad.user_id
      FROM rpt_ssi_award_detail rad,
        participant p
          ,promotion promo 
         -- ,badge_promotion bp                       --01/13/2017 start  10/23/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE rad.payout_type = NVL(p_in_award_type,rad.payout_type) --10/06/2016 
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
            AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                   -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) -- 10/16/2017 -- 10/23/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 10/16/2017  -- 10/23/2017
            AND gil_c.ref_text_1 = gc_ref_text_country_id 
            AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
            AND gil_u.ref_text_1 = gc_ref_text_user_id 
            AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
            AND rad.user_id = p.user_id
            AND rad.promotion_id      = promo.promotion_id      
            --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
      AND TRUNC(rad.payout_issue_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        AND ( ( p_in_nodeAndBelow = 1
        AND (rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) ))
                OR ( p_in_nodeAndBelow = 0
                AND rad.node_id       IN                                                                
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                   ))
      AND p.status                     = NVL (p_in_participantStatus, p.status)
      AND (CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) 
             WHEN rad.payout_type = 'other' THEN 1 
        ELSE 0 END)> 0 --03/28/2019  Bug 78869
      ) ras
    --WHERE media_amount > 0 --03/28/2019  Bug 78869
    GROUP BY ras.user_id,
      ras.participant_name
    ORDER BY media_amount DESC
    ) rs WHERE rownum  < 21 ;
    p_out_return_code := '00';
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
PROCEDURE prc_getOthAwdIss_PaxBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
  /*******************************************************************************
   Purpose: 

   Person         Date         Comments
   -----------   ----------   -----------------------------------------------------
  ????             ?????      Creation
  nagarajs       12/09/2016  G5.6.3.3 report changes
 Suresh J       01/23/2017  Rewrote the queries for G5.6.3.3 reports performance tuning 
 Gorantla       10/23/2017  JIRA# G6-3195 Report points receive details are mismatching between summary and PAX level in Award Received report 
*******************************************************************************/
IS
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getOthAwdIss_PaxBarRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);

    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';

BEGIN
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end


  v_stage := 'getOthAwdIss_PaxBarRes';
  BEGIN
    OPEN p_out_result_set FOR SELECT plateau_earned, sweepstakes_won, onthespot,other, participant_name, user_id FROM  --05/12/2015
    (SELECT SUM (plateau_earned) AS plateau_earned,
      SUM (sweepstakes_won)      AS sweepstakes_won,
      SUM (onthespot)            AS onthespot,
      SUM (other)                AS other,    --05/12/2015      
      ras.participant_name       AS participant_name,
      ras.user_id                AS user_id
    FROM
      (SELECT NVL (plateau_earned, 0) plateau_earned,
        NVL (sweepstakes_won, 0) sweepstakes_won,
        0 onthespot,
        0 other,   --05/12/2015        
        rad.participant_name,
        rad.user_id
      FROM RPT_AWARDMEDIA_DETAIL rad,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016  
          AND p.promotion_id = rad.promotion_id
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) --commented out on 04/20/2017
          --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
          AND TRUNC(rad.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
          AND ( ( p_in_nodeAndBelow = 1
          AND (rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) ))
                OR ( p_in_nodeAndBelow = 0
                AND rad.node_id       IN                                                                
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                   ))
      --AND p.promotion_status             = NVL(p_in_promotionStatus,p.promotion_status) --12/09/2016
      AND rad.participant_current_status = NVL(p_in_participantStatus,rad.participant_current_status)
      --AND NVL(rad.position_type,'JOB') = NVL(p_in_jobPosition,NVL(rad.position_type,'JOB'))    --05/29/2015 --12/09/2016
      UNION ALL
      SELECT 0 plateau_earned,
        0 sweepstakes_won,
        1 onthespot,
        0 other,   --05/12/2015        
        rad.user_full_name AS participant_name,
        rad.user_id
      FROM rpt_qcard_detail rad,
        participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
      WHERE gil_c.ref_text_1 = gc_ref_text_country_id 
        AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
        AND gil_u.ref_text_1 = gc_ref_text_user_id 
        AND (  gil_u.ref_text_2 = gc_search_all_values
              OR gil_u.id = rad.user_id )
        AND gil_dt.ref_text_1 = gc_ref_text_department_type
        AND (  gil_dt.ref_text_2 = gc_search_all_values
              OR gil_dt.ref_text_2 = rad.department ) 
        AND gil_pt.ref_text_1 = gc_ref_text_position_type
        AND (  gil_pt.ref_text_2 = gc_search_all_values
             OR gil_pt.ref_text_2 = rad.position_type ) 
        AND rad.user_id = p.user_id
        AND TRUNC(rad.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        AND ( ( p_in_nodeAndBelow = 1
        AND (rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) ))
                OR ( p_in_nodeAndBelow = 0
                AND rad.node_id       IN                                                                
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                   ))
      --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
      AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
      AND p.status                     = NVL (p_in_participantStatus, p.status)
        --05/12/2015
      UNION ALL
      SELECT 0 plateau_earned,
        0 sweepstakes_won,
        0 onthespot,
        rad.other other,  --07/22/2015
        rad.pax_last_name||', '||rad.pax_first_name AS participant_name,
        rad.user_id
      FROM rpt_ssi_award_detail rad,
        participant p
       ,promotion promo        
         -- ,badge_promotion bp                       --01/13/2017 start
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE rad.payout_type = NVL(p_in_award_type,rad.payout_type) --10/06/2016 
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
            AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                   -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id))  -- 10/23/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))   -- 10/23/2017
            AND gil_c.ref_text_1 = gc_ref_text_country_id 
            AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
            AND gil_u.ref_text_1 = gc_ref_text_user_id 
            AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
            AND rad.user_id = p.user_id
            AND rad.promotion_id      = promo.promotion_id      
          --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
        --AND promo.promotion_status   = NVL (p_in_promotionStatus, promo.promotion_status)  --12/09/2016
      AND TRUNC(rad.payout_issue_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        AND ( ( p_in_nodeAndBelow = 1
        AND (rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) ))
                OR ( p_in_nodeAndBelow = 0
                AND rad.node_id       IN                                                                
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                   ))
      AND p.status                     = NVL (p_in_participantStatus, p.status)
      ) ras
    GROUP BY ras.user_id,
      ras.participant_name
    ORDER BY ( plateau_earned + sweepstakes_won + onthespot+other) DESC,
      sweepstakes_won DESC
    ) WHERE ROWNUM     <21 ;
    p_out_return_code := '00';
    
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
PROCEDURE prc_getRecvNotRcvAwd_PaxPieRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
IS
/******************************************************************************
  NAME:       prc_getRecvNotRcvAwd_PaxPieRes
   Author                 Date          Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014        Initial..(the queries were in Java before the release 5.4. So no comments available so far)  
  Swati                 10/15/2014        Bug 57411 - In Reports column 'Eligible Participants' count should display based on the 
                                          Participant Status = Inactive/active selected in the Change Filter window
  Swati                 10/31/2014        Bug 57676 - Reports-Recognition Given/Received - Results are not correct when Participant Status = 'Show all'. 
  nagarajs              12/09/2016        G5.6.3.3 report changes                                       
  Suresh J              01/23/2017        Rewrote the queries for G5.6.3.3 reports performance tuning  
******************************************************************************/
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getRecvNotRcvAwd_PaxPieRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);

    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';
  
BEGIN
  v_stage := 'getRecvNotRcvAwd_PaxPieRes';
  BEGIN

-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
  pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
  pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
  pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
  pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
  pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   
  pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end
  
  DELETE gtt_recog_elg_count; --08/05/2014 Start
    
    IF fnc_check_promo_aud('receiver',NULL,p_in_promotionId) = 1  
       OR 
       --p_in_onTheSpotIncluded ='Y'   --04/12/2017
       ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017      
     THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt, node_id, 'nodesum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_node pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_pn  -- parent node                     
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                              OR gil_pn.id = pe.node_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
            GROUP BY pe.node_id);
        
  ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
         pe.node_id
  FROM   rpt_pax_promo_elig_speaud_node pe
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_pn  -- node                     
   WHERE     pe.giver_recvr_type = 'receiver'
          AND  ( pe.promotion_id = NVL(p_in_promotionId,-2)
               OR  pe.promotion_id = NVL((select eligible_promotion_id from badge_promotion where promotion_id = p_in_promotionId),-2) )   --08/28/201  
          AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
          AND (  gil_pn.ref_text_2 = gc_search_all_values
                  OR gil_pn.id = pe.node_id )
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = pe.department_type ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = pe.position_type ) 
  GROUP BY pe.node_id);

ELSE 

INSERT INTO gtt_recog_elg_count
SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                       FROM   
                                     (                                     
                                     SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                          eligible_cnt,  
                                                       ppe.node_id  
                                                  FROM (SELECT *  
                                                          FROM rpt_pax_promo_eligibility  
                                                         WHERE giver_recvr_type = 'receiver' ) ppe,  
                                                       promotion P,                                                        
                                                       rpt_participant_employer rad
                                                         --, badge_promotion bp                       --01/13/2017 start --12/01/2017
                                                         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                         , gtt_id_list gil_pt  -- position type
                                                         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                                                 WHERE ppe.promotion_id = P.promotion_id  
                                                        AND ppe.participant_id = rad.user_id(+)
                                                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                                                        AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                  OR gil_p.id = p.promotion_id 
                                                                 -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = p.promotion_id) --12/01/2017
                                                                  OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = p.promotion_id)))  -- 10/23/2017
                                                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                              OR gil_dt.ref_text_2 = rad.department_type ) 
                                                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                             OR gil_pt.ref_text_2 = rad.position_type ) 
                                                        --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                       AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                              NVL (p_in_participantStatus,  
                                                                   DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                   
                                                      GROUP BY ppe.node_id                                     
                                     ) elg, 
                                     ( SELECT child_node_id node_id,node_id path_node_id 
                                        FROM rpt_hierarchy_rollup
                                             ,gtt_id_list gil_pn  --parent node                                          
                                        WHERE   gil_pn.ref_text_1 = gc_ref_text_parent_node_id
                                                AND (  gil_pn.ref_text_2 = gc_ref_text_parent_node_id
                                                      OR gil_pn.ref_text_2 = node_id ) 
                                        )npn 
                                     WHERE elg.node_id(+) = npn.node_id 
                                     GROUP BY npn.path_node_id);     
END IF;    --08/05/2014 End

    OPEN p_out_result_set FOR SELECT ROUND(DECODE(nvl(pax_elig,0),0,0,(((pax_rec /pax_elig) * 100))),2)--08/19/2014 Bug 55857
  AS
    RECEIVED_AWARD_PCT, ROUND(DECODE(nvl(pax_elig,0),0,100,(((pax_elig - pax_rec)/pax_elig)*100)),2)--08/19/2014 Bug 55857
  AS
    HAVE_NOTRECEIVED_AWARD_PCT FROM
    (SELECT pax_rec,
      ( SELECT SUM(eligible_count) FROM gtt_recog_elg_count      
      ) pax_elig
    FROM
      (SELECT COUNT(user_id) pax_rec
      FROM
        (
        SELECT --rad.user_id  --05/05/2016
          CASE 
            WHEN rad.award_payout_type <> 'cash' 
                THEN rad.user_id 
            ELSE NULL    
          END  AS user_id  --05/05/2016      
        FROM RPT_AWARDMEDIA_DETAIL rad,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
        WHERE p_in_award_type = 'points' --05/05/2016
        AND rad.plateau_earned = 0        
        AND rad.promotion_id   = p.promotion_id
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) --commented out on 04/20/2017
          --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
          AND TRUNC(rad.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
            AND ( rad.node_id         IN
                      (SELECT child_node_id
                      FROM rpt_hierarchy_rollup
                      WHERE node_id IN                                                              --01/13/2017
                            (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                       ))
      --AND p.promotion_status             = NVL(p_in_promotionStatus,p.promotion_status) --12/09/2016
      AND rad.participant_current_status = NVL(p_in_participantStatus,rad.participant_current_status)
    UNION    --05/05/2016
        SELECT --rad.user_id   --05/05/2016
          CASE 
            WHEN rad.award_payout_type = 'cash' 
                THEN rad.user_id 
            ELSE NULL    
          END  AS user_id  --05/05/2016      
        FROM RPT_AWARDMEDIA_DETAIL rad,
              promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_u  -- user
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
            WHERE p_in_award_type = 'cash' 
              AND rad.promotion_id   = p.promotion_id
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) --commented out on 04/20/2017
              --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
              AND TRUNC(rad.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
              AND ( rad.node_id         IN
                          (SELECT child_node_id
                          FROM rpt_hierarchy_rollup
                          WHERE node_id IN                                                              --01/13/2017
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                           ))
              --AND p.promotion_status             = NVL(p_in_promotionStatus,p.promotion_status) --12/09/2016
              AND rad.participant_current_status = NVL(p_in_participantStatus,rad.participant_current_status)
    --Start
    UNION    --05/05/2016
        SELECT rad.user_id   --05/05/2016
        FROM RPT_AWARDMEDIA_DETAIL rad,
          promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_u  -- user
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
            WHERE p_in_award_type = 'plateau' 
              AND rad.plateau_earned > 0
              AND rad.promotion_id   = p.promotion_id
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) --commented out on 04/20/2017
              --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
              AND ( rad.node_id         IN
                          (SELECT child_node_id
                          FROM rpt_hierarchy_rollup
                          WHERE node_id IN                                                              --01/13/2017
                                (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                           ))
              --AND p.promotion_status             = NVL(p_in_promotionStatus,p.promotion_status) --12/09/2016
              AND rad.participant_current_status = NVL(p_in_participantStatus,rad.participant_current_status)
              AND TRUNC(rad.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)              
        UNION
        SELECT rad.user_id   --05/05/2016
        FROM rpt_qcard_detail rad,
          participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
      WHERE p_in_award_type = 'points'
        AND gil_c.ref_text_1 = gc_ref_text_country_id 
        AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
        AND gil_u.ref_text_1 = gc_ref_text_user_id 
        AND (  gil_u.ref_text_2 = gc_search_all_values
              OR gil_u.id = rad.user_id )
        AND gil_dt.ref_text_1 = gc_ref_text_department_type
        AND (  gil_dt.ref_text_2 = gc_search_all_values
              OR gil_dt.ref_text_2 = rad.department ) 
        AND gil_pt.ref_text_1 = gc_ref_text_position_type
        AND (  gil_pt.ref_text_2 = gc_search_all_values
             OR gil_pt.ref_text_2 = rad.position_type ) 
        AND rad.user_id = p.user_id
        AND TRUNC(rad.trans_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        AND ( rad.node_id         IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN                                                              --01/13/2017
                        (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                  WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                  ) )
          --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
          AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
          AND p.status                     = NVL (p_in_participantStatus, p.status)
        UNION
        SELECT --rad.user_id,  --05/05/2016
          CASE WHEN rad.payout_type <> 'other' 
               THEN rad.user_id 
               ELSE NULL 
          END AS user_id
        FROM rpt_ssi_award_detail rad,
          participant p
          ,promotion promo          
          --,badge_promotion bp                       --01/13/2017 start --12/01/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
        WHERE p_in_award_type = 'points' 
            AND rad.user_id       = p.user_id
            AND rad.promotion_id      = promo.promotion_id
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
            AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                       OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
            AND gil_c.ref_text_1 = gc_ref_text_country_id 
            AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
            AND gil_u.ref_text_1 = gc_ref_text_user_id 
            AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
            --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
            AND TRUNC(rad.payout_issue_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            AND ( rad.node_id         IN
                      (SELECT child_node_id
                      FROM rpt_hierarchy_rollup
                      WHERE node_id IN                                                              --01/13/2017
                            (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                      ))
            AND p.status                     = NVL (p_in_participantStatus, p.status)
        UNION
        SELECT --rad.user_id,  --05/05/2016
          CASE WHEN rad.payout_type = 'other' 
               THEN rad.user_id 
               ELSE NULL 
          END AS user_id
        FROM rpt_ssi_award_detail rad,
          participant p
          ,promotion promo   
          --,badge_promotion bp                       --01/13/2017 start --12/01/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- country
         , gtt_id_list gil_u  -- user
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
        WHERE p_in_award_type = 'other' 
            AND rad.user_id       = p.user_id
            AND rad.promotion_id      = promo.promotion_id
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
            AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                       OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
            AND gil_c.ref_text_1 = gc_ref_text_country_id 
            AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
            AND gil_u.ref_text_1 = gc_ref_text_user_id 
            AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
            --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
            AND TRUNC(rad.payout_issue_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            AND ( rad.node_id         IN
                      (SELECT child_node_id
                      FROM rpt_hierarchy_rollup
                      WHERE node_id IN                                                              --01/13/2017
                            (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                      ))
            AND p.status                     = NVL (p_in_participantStatus, p.status)

    --05/12/2015
    UNION ALL   --05/05/2016
        SELECT rad.user_id AS user_id
        FROM RPT_AWARDMEDIA_DETAIL rad,
             promotion p          
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_c  -- country
             , gtt_id_list gil_u  -- user
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             WHERE p_in_award_type = 'other'
              AND rad.award_payout_type = 'other' 
              AND rad.promotion_id      = p.promotion_id
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
              --       OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) --01/13/2017 end
          AND TRUNC(rad.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015      
            AND ( ( p_in_nodeAndBelow = 1
            AND (rad.node_id         IN
                      (SELECT child_node_id
                      FROM rpt_hierarchy_rollup
                      WHERE node_id IN                                                              --01/13/2017
                            (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                      ) ))
                    OR ( p_in_nodeAndBelow = 0
                    AND rad.node_id       IN                                                                
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                       ))
          AND rad.participant_current_status = NVL(p_in_participantStatus,rad.participant_current_status)
        )
      )
    ) ;
    p_out_return_code := '00';
    
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END prc_getRecvNotRcvAwd_PaxPieRes; --End procedure

--PROCEDURE prc_getTotPtsIss_PerBarRes(
--    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
--    p_in_jobPosition       IN VARCHAR,
--    p_in_participantStatus IN VARCHAR,
--    p_in_localeDatePattern IN VARCHAR,
--    p_in_parentNodeId      IN VARCHAR,
--    p_in_fromDate          IN VARCHAR,
--    p_in_toDate            IN VARCHAR,
--    p_in_onTheSpotIncluded IN VARCHAR,
--    p_in_rowNumStart       IN NUMBER,
--    p_in_rowNumEnd         IN NUMBER,
--    p_in_languageCode      IN VARCHAR,
--    p_in_nodeAndBelow      IN VARCHAR,
--    p_in_departments       IN VARCHAR,
--    p_in_promotionId       IN VARCHAR,
--    p_in_award_type        IN VARCHAR,     --05/05/2016
--    p_in_countryIds        IN VARCHAR,
--    p_in_userid            IN NUMBER,
--    p_in_sortColName       IN VARCHAR,
--    p_in_sortedBy          IN VARCHAR,
--    p_out_return_code OUT NUMBER,
--    p_out_result_set OUT sys_refcursor)
--IS
--  c_delimiter     CONSTANT VARCHAR2 (1) := '|';
--  c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
--  v_team_ids      VARCHAR2 (4000);
--  c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getTotPtsIss_PerBarRes';
--  c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
--  c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
--  v_stage         VARCHAR2(500);
--BEGIN
--  v_stage := 'getTotPtsIss_PerBarRes';
--  BEGIN
--    OPEN p_out_result_set FOR SELECT month_trans.cms_name || '-' || TO_CHAR (ss.month_sort, 'YY')
--  AS
--    MONTH_NAME, total_points_count
--  AS
--    TOTAL_POINTS_ISSUED FROM
--    ( WITH month_list AS
--    (SELECT ADD_MONTHS ( TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1) month_sort
--    FROM DUAL
--      CONNECT BY LEVEL <= MONTHS_BETWEEN ( TRUNC ( TO_DATE(p_in_toDate,p_in_localeDatePattern), 'mm'), TRUNC ( TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm')) + 1
--    )
--  SELECT ml.month_sort,
--    NVL (rpt_c.totalpoints, 0) total_points_count
--  FROM
--    (SELECT TRUNC (media_date, 'mm') AS month_year,
--      SUM (totalpoints) totalpoints
--    FROM
--      (SELECT ras.points totalpoints,
--        TRUNC (ras.media_date) media_date
--      FROM RPT_AWARDMEDIA_DETAIL ras,
--        promotion p
--      WHERE TRUNC (ras.media_date) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
--      AND ( ( p_in_nodeAndBelow = 1
--      AND (ras.node_id         IN
--        (SELECT child_node_id
--        FROM rpt_hierarchy_rollup
--        WHERE node_id IN
--          (SELECT * FROM TABLE ( get_array_varchar ( NVL (p_in_parentNodeId, 0)))
--          )
--        ) ))
--      OR ( p_in_nodeAndBelow = 0
--      AND (ras.node_id      IN
--        (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
--        )) ))
--      AND ( (p_in_promotionId IS NULL)
--      OR (p_in_promotionId    IS NOT NULL
--      AND ras.promotion_id     IN
--        (SELECT * FROM TABLE ( get_array_varchar ( p_in_promotionId))
--        ))
--        )
--      AND ras.promotion_id               = p.promotion_id
--      AND p.promotion_status             =NVL (p_in_promotionStatus, p.promotion_status)
--      AND ras.participant_current_status = NVL (p_in_participantStatus, ras.participant_current_status)
--      AND NVL(ras.position_type,'job')   = NVL (p_in_jobPosition, NVL(ras.position_type,'job'))
--      AND ( (p_in_departments           IS NULL)
--      OR (p_in_departments              IS NOT NULL
--      AND department                    IN
--        (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
--        )))
--      AND ((p_in_userid IS NULL) -- 08/20/2014 Bug 55274
--          OR ras.user_id     IN
--            (SELECT * FROM TABLE(get_array_varchar(p_in_userid))
--            ))
--      AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = ras.journal_id)  --05/28/2015  Bug 62477          
--      UNION ALL
--      SELECT rad.transaction_amount totalpoints,
--        TRUNC (rad.trans_date) media_date
--      FROM rpt_qcard_detail rad,
--        participant p
--      WHERE rad.user_id        = p.user_id
--      AND ( (p_in_departments IS NULL)
--      OR (p_in_departments    IS NOT NULL
--      AND department          IN
--        (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
--        )))
--      AND NVL (rad.position_type, 'JOB') = NVL (p_in_jobPosition, NVL (rad.position_type, 'JOB'))
--      AND 'Y'                            = p_in_onTheSpotIncluded
--      AND p.status                       = NVL (p_in_participantStatus, p.status)
--      AND NVL (TRUNC (trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
--      AND ( (p_in_countryIds IS NULL)
--      OR (p_in_countryIds    IS NOT NULL
--      AND country_id         IN
--        (SELECT               *
--        FROM TABLE ( get_array_varchar ( p_in_countryIds))
--        )))
--       AND ((p_in_userid IS NULL) -- 08/20/2014 Bug 55274
--          OR rad.user_id     IN
--            (SELECT * FROM TABLE(get_array_varchar(p_in_userid))
--            ))
--      AND ( ( p_in_nodeAndBelow = 1
--      AND (node_id             IN
--        (SELECT child_node_id
--        FROM rpt_hierarchy_rollup
--        WHERE node_id IN
--          (SELECT * FROM TABLE ( get_array_varchar ( NVL (p_in_parentNodeId, 0)))
--          )
--        ) ))
--      OR ( p_in_nodeAndBelow = 0 
--      AND node_id           IN
--        (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
--        )) )
----05/12/2015
--      UNION ALL
--      SELECT CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END totalpoints,
--        TRUNC (rad.payout_issue_date) media_date
--      FROM rpt_ssi_award_detail rad,
--        participant p
--        ,promotion promo        
--      WHERE rad.user_id        = p.user_id
--     AND rad.promotion_id      = promo.promotion_id 
--        AND ( (p_in_promotionId IS NULL)
--        OR promo.promotion_id        IN
--          (SELECT * FROM TABLE ( get_array_varchar ( p_in_promotionId))
--          ) 
--        OR promo.promotion_id        IN     --08/28/2015 
--          (select eligible_promotion_id from badge_promotion where promotion_id IN (SELECT * FROM TABLE ( get_array_varchar ( p_in_promotionId)))
--           )   --08/28/2015
--          )
--     --AND promo.promotion_status   = NVL (p_in_promotionStatus, promo.promotion_status)  --12/09/2016
--     AND ( (p_in_departments IS NULL)
--      OR (p_in_departments    IS NOT NULL
--      AND department          IN
--        (SELECT * FROM TABLE ( get_array_varchar ( p_in_departments))
--        )))
--      AND NVL (rad.position_type, 'JOB') = NVL (p_in_jobPosition, NVL (rad.position_type, 'JOB'))
--      AND p.status                       = NVL (p_in_participantStatus, p.status)
--      AND NVL (TRUNC (payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
--      AND ( (p_in_countryIds IS NULL)
--      OR (p_in_countryIds    IS NOT NULL
--      AND country_id         IN
--        (SELECT               *
--        FROM TABLE ( get_array_varchar ( p_in_countryIds))
--        )))
--       AND ((p_in_userid IS NULL) -- 08/20/2014 Bug 55274
--          OR rad.user_id     IN
--            (SELECT * FROM TABLE(get_array_varchar(p_in_userid))
--            ))
--      AND ( ( p_in_nodeAndBelow = 1
--      AND (node_id             IN
--        (SELECT child_node_id
--        FROM rpt_hierarchy_rollup
--        WHERE node_id IN
--          (SELECT * FROM TABLE ( get_array_varchar ( NVL (p_in_parentNodeId, 0)))
--          )
--        ) ))
--      OR ( p_in_nodeAndBelow = 0 
--      AND node_id           IN
--        (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )
--        )) )
----05/12/2015
--      )
--    GROUP BY TRUNC (media_date, 'mm')
--    ) rpt_c,
--    month_list ml
--  WHERE ml.month_sort = rpt_c.month_year(+)
--  ORDER BY ml.month_sort
--    ) ss,
--    (SELECT cms_code,
--      cms_name
--    FROM mv_cms_code_value vw
--    WHERE asset_code = 'picklist.monthname.type.items'
--    AND locale       =
--      (SELECT string_val
--      FROM os_propertyset
--      WHERE entity_name = 'default.language'
--      )
--    AND NOT EXISTS
--      (SELECT *
--      FROM mv_cms_code_value
--      WHERE asset_code = 'picklist.monthname.type.items'
--      AND locale       =p_in_languageCode
--      )
--    UNION ALL
--    SELECT cms_code,
--      cms_name
--    FROM mv_cms_code_value
--    WHERE asset_code                                 = 'picklist.monthname.type.items'
--    AND locale                                       =p_in_languageCode
--    ) month_trans WHERE UPPER (month_trans.cms_code) = UPPER (TO_CHAR (ss.month_sort, 'MON')) ;
--    p_out_return_code                               := '00';
--
--  EXCEPTION
--  WHEN OTHERS THEN
--    p_out_return_code := '99';
--    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
--    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
--  END;
--END; --End procedure

PROCEDURE prc_getAwardsSummary(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_rs_getAwdSmryResult OUT sys_refcursor,
    p_out_rs_getAwdSmryResultTot OUT SYS_REFCURSOR)
IS

  /******************************************************************************
  NAME:       prc_getAwardsSummary
  Date                 Author          Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014     Initial..(the queries were in Java before the release 5.4. So no comments available so far)    
  Swati                 10/15/2014     Bug 57411 - In Reports column 'Eligible Participants' count should display based on the 
                                       Participant Status = Inactive/active selected in the Change Filter window
  Swati                 10/31/2014     Bug 57676 - Reports-Recognition Given/Received - Results are not correct when Participant Status = 'Show all'.
  Suresh J              02/19/2016     Bug 65768 - Incorrect total shown for Have not received column 
  Suresh J              05/05/2016     New nomination changes                          
  nagarajs             07/26/2016      Bug 67730 - Admin View \Awards Received - Participation by Organization Report :
                                       There is a mismatch in the Points total between the Chart table and the summary table
  nagarajs              10/06/2016     New nomination changes 
  nagarajs              12/09/2016     G5.6.3.3 report changes
  Suresh J              01/23/2017     Rewrote the queries for G5.6.3.3 reports performance tuning
  nagarajs              03/07/2017     G6-1876- In Awards Received - By Organization report column RECEIVED POINTS AWARD is showing wrong average.
  nagarajs              03/09/2017     G6-1891 - Eligible count not showing when multiple  country selected
  Suresh J              03/09/2017     G6-1894 - Elig Pax count is not matching before and after drill down
  Suresh J              03/09/2017     G6-1897 - Do not show org units with no data in charts and summary  
  Suresh J              03/14/2017     G6-1885 - Summary is not getting displayed on selecting PAX status = Inactive
  Chidamba              02/19/2018     G6-3872 - There is a mismatch in the Points total between the Chart table and the summary table.
 ******************************************************************************/
    -- constants
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getAwardsSummary';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);  
    v_data_sort     VARCHAR2(50);
    v_fetch_count   INTEGER;
    v_parm_list     execution_log.text_line%TYPE;
        
    v_sortCol             VARCHAR2(200);
    v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016

    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';

    CURSOR cur_query_ref IS
    SELECT  CAST(NULL AS NUMBER) AS total_records,
            CAST(NULL AS NUMBER) AS rec_seq,
            CAST(NULL AS VARCHAR2(2000)) AS org_name,
            CAST(NULL AS NUMBER) AS eligible_participants,
            CAST(NULL AS NUMBER) AS received_award,
            CAST(NULL AS NUMBER) AS received_award_pct,
            CAST(NULL AS NUMBER) AS have_notreceived_award,
            CAST(NULL AS NUMBER) AS have_notreceived_award_pct,
            CAST(NULL AS NUMBER) AS points_received,
            CAST(NULL AS NUMBER) AS plateau_earned_count,
            CAST(NULL AS NUMBER) AS sweepstakes_won_count,
            CAST(NULL AS NUMBER) AS on_the_spot_deposited_count,
            CAST(NULL AS NUMBER) AS other,
            CAST(NULL AS NUMBER) AS node_id,                    
            CAST(NULL AS NUMBER) AS cash_received
    FROM dual;
    
 
    rec_query      cur_query_ref%ROWTYPE;    
BEGIN

   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '	p_in_jobPosition	:='||	p_in_jobPosition	||CHR(10)||
'	p_in_participantStatus	:='||	p_in_participantStatus	||CHR(10)||
'	p_in_localeDatePattern	:='||	p_in_localeDatePattern	||CHR(10)||
'	p_in_parentNodeId	:='||	p_in_parentNodeId	||CHR(10)||
'	p_in_fromDate	:='||	p_in_fromDate	||CHR(10)||
'	p_in_toDate	:='||	p_in_toDate	||CHR(10)||
'	p_in_onTheSpotIncluded	:='||	p_in_onTheSpotIncluded	||CHR(10)||
'	p_in_rowNumStart	:='||	p_in_rowNumStart	||CHR(10)||
'	p_in_rowNumEnd	:='||	p_in_rowNumEnd	||CHR(10)||
'	p_in_languageCode	:='||	p_in_languageCode	||CHR(10)||
'	p_in_nodeAndBelow	:='||	p_in_nodeAndBelow	||CHR(10)||
'	p_in_departments	:='||	p_in_departments	||CHR(10)||
'	p_in_promotionId	:='||	p_in_promotionId	||CHR(10)||
'	p_in_award_type	:='||	p_in_award_type	||CHR(10)||
'	p_in_countryIds	:='||	p_in_countryIds	||CHR(10)||
'	p_in_userid	:='||	p_in_userid	||CHR(10)||
'	p_in_sortColName	:='||	p_in_sortColName	||CHR(10)||
'	p_in_sortedBy	:='||	p_in_sortedBy	
	;

   --prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);  

    -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
  pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
  pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
  pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
  pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
  pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   --03/14/2017
  pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;

  v_stage := 'Get currency value';
  BEGIN    --12/09/2016
   SELECT ccc.bpom_entered_rate
     INTO v_cash_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_userid
      AND is_primary = 1
      AND ua.country_id = c.country_id
      AND c.country_code = ccc.to_cur;
  EXCEPTION
    WHEN OTHERS THEN
      v_cash_currency_value := 1;
  END;  
  
  v_stage   := 'getAwardsSummaryResults';
  v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;
  
  DELETE gtt_recog_elg_count; --08/05/2014 Start
    
    IF fnc_check_promo_aud('receiver',NULL,p_in_promotionId) = 1  
       OR 
       --p_in_onTheSpotIncluded ='Y' --04/12/2017
       ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
         
     THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt, node_id, 'nodesum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_node pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_c   --country
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND pe.node_id in (SELECT node_id
                              FROM rpt_hierarchy
                              WHERE parent_node_id IN                                                              
                                    (SELECT g.id FROM gtt_id_list g                                         
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                              ) 
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
                      AND gil_c.ref_text_1 = gc_ref_text_country_id
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                             --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                             OR gil_c.id = pe.country_id )  --03/09/2017
            GROUP BY pe.node_id)
    UNION ALL
    SELECT eligible_cnt, node_id, 'teamsum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_team pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_pn  -- parent node
                     , gtt_id_list gil_c   --country                 
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                              OR gil_pn.id = pe.node_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
                      AND gil_c.ref_text_1 = gc_ref_text_country_id
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                             --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                             OR gil_c.id = pe.country_id )  --03/09/2017
            GROUP BY pe.node_id);
        
    ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
      INSERT INTO gtt_recog_elg_count
      SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_node pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion id
             , gtt_id_list gil_c   --country             
       WHERE     pe.giver_recvr_type = 'receiver'
              AND pe.node_id in (SELECT node_id
                      FROM rpt_hierarchy
                      WHERE parent_node_id IN                                                              
                            (SELECT g.id FROM gtt_id_list g                                         
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                      ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                     --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                      OR gil_c.id = pe.country_id )  --03/09/2017
      GROUP BY pe.node_id)
      UNION ALL
      SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_team pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_pn  -- node       
             , gtt_id_list gil_p  -- promotion id
             , gtt_id_list gil_c  --country                           
       WHERE     pe.giver_recvr_type = 'receiver'
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
              AND (  gil_pn.ref_text_2 = gc_search_all_values
                      OR gil_pn.id = pe.node_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type ) 
              AND gil_c.ref_text_1 = gc_ref_text_country_id
               AND (  gil_c.ref_text_2 = gc_search_all_values
                      --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                      OR gil_c.id = pe.country_id )  --03/09/2017v 
      GROUP BY pe.node_id);


  ELSE 
        INSERT INTO gtt_recog_elg_count
        SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
        SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                               FROM   
                                             (                                     
                                             SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                                  eligible_cnt,  
                                                               ppe.node_id  
                                                          FROM (SELECT *  
                                                                  FROM rpt_pax_promo_eligibility  
                                                                 WHERE giver_recvr_type = 'receiver' ) ppe,  
                                                               promotion P,                                                        
                                                               rpt_participant_employer rad
                                                               , user_address ua  --03/09/2017
                                                                 , country c        --03/09/2017
                                                                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                                 , gtt_id_list gil_pt  -- position type
                                                                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                                                                 , gtt_id_list gil_c   -- country --03/09/2017   
                                                                 
                                                         WHERE ppe.promotion_id = P.promotion_id  
                                                                AND ppe.participant_id = rad.user_id(+)
                                                                AND ppe.participant_id = ua.user_id (+) --03/09/2017
                                                                AND ua.country_id = c.country_id (+)   --03/09/2017
                                                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                                                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                          OR gil_p.id = p.promotion_id)
                                                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                      OR gil_dt.ref_text_2 = rad.department_type ) 
                                                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                     OR gil_pt.ref_text_2 = rad.position_type )
                                                                AND gil_c.ref_text_1 = gc_ref_text_country_id   --03/09/2017
                                                                AND (  gil_c.ref_text_2 = gc_search_all_values   --03/09/2017
                                                                     OR gil_c.id = c.country_id ) 
                                                                --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                      NVL (p_in_participantStatus,  
                                                                           DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                   
                                                              GROUP BY ppe.node_id                                     
                                             ) elg, 
                                             ( SELECT child_node_id node_id,node_id path_node_id 
                                                FROM rpt_hierarchy_rollup
                                                     ,gtt_id_list gil_pn  --parent node                                          
                                                WHERE  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = node_id )
                                                ) npn 
                                             WHERE elg.node_id(+) = npn.node_id 
                                             GROUP BY npn.path_node_id)
        UNION ALL                                        
        SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
        SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                              eligible_cnt,  
                                                           ppe.node_id  
                                                      FROM (SELECT *  
                                                              FROM rpt_pax_promo_eligibility
                                                                   ,gtt_id_list gil_pn  --parent node
                                                             WHERE giver_recvr_type = 'receiver'  
                                                                      AND  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                              OR gil_pn.id = node_id ) ) ppe,  
                                                           promotion P,                                                        
                                                           rpt_participant_employer rad
                                                           , user_address ua  --03/09/2017
                                                                 , country c        --03/09/2017
                                                             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                             , gtt_id_list gil_pt  -- position type
                                                             , gtt_id_list gil_pn  -- node       
                                                             , gtt_id_list gil_p  -- promotion id      
                                                             , gtt_id_list gil_c  -- country                     
                                                     WHERE ppe.promotion_id = P.promotion_id  
                                                          AND ppe.participant_id = rad.user_id(+)
                                                          AND ppe.participant_id = ua.user_id (+) --03/09/2017
                                                          AND ua.country_id = c.country_id (+)   --03/09/2017
                                                          AND   gil_p.ref_text_1 = gc_ref_text_promotion_id
                                                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                  OR gil_p.id = ppe.promotion_id ) 
                                                          AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = ppe.node_id )
                                                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                  OR gil_dt.ref_text_2 = rad.department_type ) 
                                                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                 OR gil_pt.ref_text_2 = rad.position_type )
                                                          AND gil_c.ref_text_1 = gc_ref_text_country_id   --03/09/2017
                                                          AND (  gil_c.ref_text_2 = gc_search_all_values   --03/09/2017
                                                                     OR gil_c.id = c.country_id ) 
                                                          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                          AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                  NVL (p_in_participantStatus,  
                                                                       DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                                          
                                                  GROUP BY ppe.node_id);     
                                             
    END IF;    --08/05/2014 End

BEGIN

   v_stage := 'OPEN p_out_rs_getAwdSmryResult';
    OPEN p_out_rs_getAwdSmryResult FOR 
    SELECT s.*
     FROM ( -- sequence the data
        SELECT 
                   COUNT(rs.org_name) OVER() AS total_records
                   -- calc record sort order
                   ,ROW_NUMBER() OVER (
                     ORDER BY
                     -- sort totals record first
                     DECODE(rs.org_name, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'org_name',                        rs.org_name),
                     DECODE(v_data_sort, 'eligible_participants',           rs.eligible_participants),
                     DECODE(v_data_sort, 'received_award',                  rs.received_award),
                     DECODE(v_data_sort, 'received_award_pct',              rs.received_award_pct),
                     DECODE(v_data_sort, 'have_notreceived_award',          rs.have_notreceived_award),
                     DECODE(v_data_sort, 'have_notreceived_award_pct',      rs.have_notreceived_award_pct),
                     DECODE(v_data_sort, 'points_received',                 rs.points_received),
                     DECODE(v_data_sort, 'plateau_earned_count',            rs.plateau_earned_count),
                     DECODE(v_data_sort, 'sweepstakes_won_count',           rs.sweepstakes_won_count),
                     DECODE(v_data_sort, 'on_the_spot_deposited_count',     rs.on_the_spot_deposited_count),                     
                     DECODE(v_data_sort, 'node_id',                         rs.node_id),                     
                     DECODE(v_data_sort, 'cash_received',                   rs.cash_received),                                          
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/org_name',                       rs.org_name) DESC,
                     DECODE(v_data_sort, 'desc/eligible_participants',          rs.eligible_participants) DESC,
                     DECODE(v_data_sort, 'desc/received_award',                 rs.received_award) DESC,
                     DECODE(v_data_sort, 'desc/received_award_pct',             rs.received_award_pct) DESC,
                     DECODE(v_data_sort, 'desc/have_notreceived_award',         rs.have_notreceived_award) DESC,
                     DECODE(v_data_sort, 'desc/have_notreceived_award_pct',     rs.have_notreceived_award_pct) DESC,
                     DECODE(v_data_sort, 'desc/points_received',                rs.points_received) DESC,
                     DECODE(v_data_sort, 'desc/plateau_earned_count',           rs.plateau_earned_count) DESC,
                     DECODE(v_data_sort, 'desc/sweepstakes_won_count',          rs.sweepstakes_won_count) DESC,
                     DECODE(v_data_sort, 'desc/on_the_spot_deposited_count',    rs.on_the_spot_deposited_count) DESC,                     
                     DECODE(v_data_sort, 'desc/node_id',                        rs.node_id) DESC,                     
                     DECODE(v_data_sort, 'desc/cash_received',                  rs.cash_received) DESC,                                          
                     -- default sort fields
                     LOWER(rs.org_name),
                     rs.org_name
                   ) -1 AS rec_seq
                   ,rs.*
    FROM
      (
      SELECT org_name,  --03/07/2017
             eligible_participants,
             received_award,
             nvl ( round ( decode ( eligible_participants, 0, 0, (nvl (received_award, 0) / eligible_participants) * 100), 2), 0)                                as received_award_pct,
             have_notreceived_award,
             nvl ( round ( decode ( eligible_participants, 0, 0, ( (eligible_participants              - nvl (received_award, 0)) / eligible_participants) * 100), 2), 0) as have_notreceived_award_pct,
             points_received,
             plateau_earned_count,
             sweepstakes_won_count,
             on_the_spot_deposited_count,
             other,   
             node_id,
             cash_received
        FROM (
      SELECT dtl.Org                                                                                                                         AS ORG_NAME,
        SUM(NVL (dtl.pax_elig, 0))                                                                                                                 AS ELIGIBLE_PARTICIPANTS,
        SUM(NVL (dtl.award_received, 0))                                                                                                           AS RECEIVED_AWARD,
        --SUM(NVL ( ROUND ( DECODE ( dtl.pax_elig, 0, 0, (NVL (dtl.award_received, 0) / dtl.pax_elig) * 100), 2), 0))                                AS RECEIVED_AWARD_PCT, --03/07/2017
        SUM(NVL ( (dtl.pax_elig                                                     - NVL (dtl.award_received, 0)), 0))                            AS HAVE_NOTRECEIVED_AWARD,
        --SUM(NVL ( ROUND ( DECODE ( dtl.pax_elig, 0, 0, ( (dtl.pax_elig              - NVL (dtl.award_received, 0)) / dtl.pax_elig) * 100), 2), 0)) AS HAVE_NOTRECEIVED_AWARD_PCT, --03/07/2017
        SUM(NVL (dtl.media_amount, 0))                                                                                                             AS POINTS_RECEIVED,
        SUM(NVL (dtl.plateau_earned, 0))                                                                                                           AS PLATEAU_EARNED_COUNT,
        SUM(NVL (dtl.sweepstakes_won, 0))                                                                                                          AS SWEEPSTAKES_WON_COUNT,
        SUM(NVL (dtl.OnTheSpot, 0))                                                                                                                AS ON_THE_SPOT_DEPOSITED_COUNT,
        SUM(NVL (dtl.Other    , 0))                            AS OTHER,   --05/12/2015
        dtl.node_id                                                                                                                           AS NODE_ID
        ,SUM(NVL (dtl.cash_received    , 0)* v_cash_currency_value)                                                                                  AS cash_received   --05/12/2015 --12/09/2016
      FROM
        (SELECT dtl.node_name AS Org,
          dtl.node_id,
          award_received,
          media_amount,
          plateau_earned,
          sweepstakes_won,
          OnTheSpot,
          Other,  --05/12/2015          
          (SELECT eligible_count FROM gtt_recog_elg_count WHERE record_type = 'nodesum' AND node_id = dtl.node_id) pax_elig
          ,cash_received
        FROM
          ( SELECT DISTINCT ras.detail_node_id,
            ras.header_node_id
          FROM rpt_awardmedia_summary ras,
            promotion p
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion 
                     , gtt_id_list gil_c  -- country --03/09/2017                    
          WHERE p.promotion_id      = ras.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = ras.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = ras.job_position ) 
                  AND gil_c.ref_text_1 = gc_ref_text_country_id   --03/09/2017
                  AND (  gil_c.ref_text_2 = gc_search_all_values   --03/09/2017
                        OR gil_c.id = ras.country_id ) 
          --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
          AND ras.record_type LIKE '%node%'
          AND pax_status                = NVL (p_in_participantStatus, pax_status)
          AND NVL (ras.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          GROUP BY detail_node_id,
            header_node_id,
            record_type
          UNION
          SELECT NODE_ID,
            PARENT_NODE_ID
          FROM rpt_hierarchy
              ,gtt_id_list gil_pn  --parent node                                          
          WHERE   gil_pn.ref_text_1 = gc_ref_text_parent_node_id                --03/09/2017
                  AND gil_pn.id =parent_node_id                                 --03/09/2017
             ) raD,
          (SELECT node_id,
            node_name,
            award_received,
            media_amount,
            plateau_earned,
            sweepstakes_won,
            OnTheSpot      
           ,Other  --05/12/2015 
           ,cash_received  --05/05/2016                 
          FROM rpt_hierarchy rh,
            (SELECT npn.path_node_id,
              SUM (award_received) award_received,
              SUM (media_amount) media_amount,
              SUM (plateau_earned) plateau_earned,
              SUM (sweepstakes_won) sweepstakes_won,
              SUM (OnTheSpot) OnTheSpot
             ,SUM(Other) Other   --05/12/2015 
             ,SUM(cash_received) cash_received   --05/12/2015                          
--              SUM(elg_pax) elg_pax
            FROM
              (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup
              ) npn,
              (SELECT ras.node_id,
                COUNT ( DISTINCT ras.user_id) award_received,
                SUM (ras.media_amount) media_amount,
                SUM (plateau_earned) plateau_earned,
                SUM (sweepstakes_won) sweepstakes_won,
                SUM (OnTheSpot) OnTheSpot
                ,SUM(Other) Other   --05/12/2015  
                ,SUM(cash_received) cash_received   --05/05/2016 
              FROM
                (
SELECT rad.user_id,
                  rad.node_id,
                  --rad.media_amount,   --05/05/2016
                  CASE WHEN rad.award_payout_type = 'points' THEN  --05/05/2016
                  NVL(rad.media_amount,0)
                  ELSE 0
                  END    AS media_amount,            
                  rad.plateau_earned,
                  rad.sweepstakes_won,
                  0 AS OnTheSpot
                  ,CASE WHEN rad.award_payout_type = 'other' THEN  --10/06/2016
                  1
                  ELSE 0
                  END  AS Other   --05/12/2015   --10/06/2016     
                 ,CASE WHEN rad.award_payout_type = 'cash' THEN  --05/05/2016
                  NVL(rad.media_amount,0)
                  ELSE 0
                  END                      as cash_received    
                FROM RPT_AWARDMEDIA_DETAIL rad,
                  promotion p
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end 
                 , gtt_id_list gil_c  -- country                                                          
                WHERE p.promotion_id      = rad.promotion_id
                AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND ( ( gil_p.ref_text_2 = gc_search_all_values 
                  --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                       AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded ))  --04/12/2017                         
                      OR ( gil_p.ref_text_2 = gc_search_all_values and p_in_award_type <> 'points' ) --12/01/2017
                      OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                AND rad.participant_current_status  = NVL ( p_in_participantStatus, rad.participant_current_status)
                AND NVL ( rad.media_date, TRUNC ( SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
                AND p.promotion_type <> 'self_serv_incentives'  --06/04/2015                
                UNION ALL
                SELECT rad.user_id,
                  rh.node_id,
                  rad.transaction_amount media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  1 OnTheSpot
                  ,0 Other   --05/12/2015 
                  ,0 AS cash_received    --05/05/2016                 
                FROM rpt_qcard_detail rad,
                  rpt_hierarchy rh,
                  participant p
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_c  -- country
                WHERE rh.node_id         = rad.node_id
                AND rad.user_id          = p.user_id
                --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                AND p.status             = NVL (p_in_participantStatus, p.status)
                AND NVL (TRUNC (rad.trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)  -- 02/19/2018
                UNION ALL
                SELECT rad.user_id,
                  rh.node_id,
                  CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 OnTheSpot             --08/12/2015
                  ,CASE WHEN rad.payout_type = 'other' THEN rad.other ELSE 0 END Other --05/12/2015     
                  ,0 AS cash_received    --05/05/2016             
                FROM rpt_ssi_award_detail rad,
                     rpt_hierarchy rh,
                     participant p
                     ,promotion promo
                     --,badge_promotion bp                       --01/13/2017 start --12/01/2017
                     , gtt_id_list gil_dt  -- department type   
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                     , gtt_id_list gil_c  -- country
                     , gtt_id_list gil_u  -- user                     --03/14/2017                                                                              
                WHERE   rh.node_id         = rad.node_id
                        AND rad.user_id          = p.user_id
                        AND rad.promotion_id      = promo.promotion_id
                          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = promo.promotion_id 
                                  -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id))--12/01/2017
                                   OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                          AND gil_c.ref_text_1 = gc_ref_text_country_id 
                          AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = rad.country_id )
                          AND gil_u.ref_text_1 = gc_ref_text_user_id      --03/14/2017 
                          AND (  gil_u.ref_text_2 = gc_search_all_values
                                  OR gil_u.id = rad.user_id )
                          --AND rad.user_id         = p_in_userid           --03/14/2017
                          AND p.status            = NVL (p_in_participantStatus, p.status)
                          --AND (( p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                            AND  p_in_nodeAndBelow = 1
                            AND (rad.node_id         IN
                              (SELECT child_node_id
                              FROM rpt_hierarchy_rollup
                              WHERE node_id IN                                                              --01/13/2017
                                    (SELECT g.id FROM gtt_id_list g                                         --01/13/2017
                                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   --01/13/2017
                              ) )
                                    AND p.status             = NVL (p_in_participantStatus, p.status)
                                    AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                                ) ras
                                  GROUP BY ras.node_id
                                  ) dtl
                                WHERE dtl.node_id(+) = npn.node_id            
                                GROUP BY npn.path_node_id
                                )
                              WHERE rh.node_id = path_node_id
                              ) dtl
                            WHERE raD.detail_node_id         = dtl.node_id 
                            AND dtl.node_id                  = dtl.node_id 
                            AND NVL (raD.header_node_id, 0) IN
                                    (SELECT g.id FROM gtt_id_list g                                             --01/13/2017
                                                          WHERE g.ref_text_1 = gc_ref_text_parent_node_id )     --01/13/2017
        UNION ALL
        SELECT rh.node_name||' Team' Org,
          rh.node_id,
          NVL (award_received, 0) award_received,
          NVL (media_amount, 0) media_amount,
          NVL (plateau_earned, 0) plateau_earned,
          NVL (sweepstakes_won, 0) sweepstakes_won,
          NVL (onTheSpot, 0) AS OnTheSpot,
          NVL (Other, 0) AS Other,  --05/12/2015                   
          (select eligible_count FROM gtt_recog_elg_count WHERE record_type = 'teamsum' AND node_id = rh.node_id) pax_elig, --08/05/2014
          NVL (cash_received, 0) * v_cash_currency_value AS cash_received  --05/05/2016  --12/09/2016
        FROM rpt_hierarchy rh,
          (SELECT COUNT (DISTINCT ras.user_id) award_received,
            ras.node_id,
            ras.node_name,
            SUM (ras.media_amount) media_amount,
            SUM (plateau_earned) plateau_earned,
            SUM (sweepstakes_won) sweepstakes_won,
            SUM( onTheSpot) AS onTheSpot
           ,SUM( Other) As Other   --05/12/2015 
           ,SUM(cash_received) AS cash_received   --05/05/2016                      
          FROM
            (SELECT rad.user_id,
              rh.node_id,
              rh.node_name,
              --rad.media_amount,   --05/05/2016
              CASE WHEN rad.award_payout_type = 'points' THEN  --05/05/2016
              NVL(rad.media_amount,0)
              ELSE 0
              END AS media_amount,
              rad.plateau_earned,
              rad.sweepstakes_won,
              0 AS OnTheSpot
             ,CASE WHEN rad.award_payout_type = 'other' THEN  --05/05/2016
              1
              ELSE 0
              END  AS Other   --05/12/2015 --10/06/2016
             ,CASE WHEN rad.award_payout_type = 'cash' THEN  --05/05/2016
              NVL(rad.media_amount,0)
              ELSE 0
              END                    AS cash_received    --05/05/2016
            FROM RPT_AWARDMEDIA_DETAIL rad,
              rpt_hierarchy rh,
              promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end
             , gtt_id_list gil_c  -- country                                                           
            WHERE rh.node_id           = rad.node_id 
              AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
              AND rad.promotion_id      = p.promotion_id
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND ( ( gil_p.ref_text_2 = gc_search_all_values 
                      --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                      AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded ))  --04/12/2017
                      OR (gil_p.ref_text_2 = gc_search_all_values and p_in_award_type <> 'points')  --12/01/2017
                      OR gil_p.id = p.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
            --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
            AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
            AND NVL (rad.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015            
            AND p.promotion_type <> 'self_serv_incentives'  --06/04/2015
            UNION ALL
            SELECT rad.user_id,
              rh.node_id,
              rh.node_name,
              rad.transaction_amount media_amount,
              0 AS plateau_earned,
              0 AS sweepstakes_won,
              1 OnTheSpot
             ,0 other   --05/12/2015  
             ,0 cash_received   --05/05/2016            
            FROM rpt_qcard_detail rad,
              rpt_hierarchy rh,
              participant p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_c   -- country
            WHERE rh.node_id         = rad.node_id
                     AND rad.user_id          = p.user_id
                     --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                      AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      AND p.status             = NVL (p_in_participantStatus, p.status)
                      AND NVL (TRUNC (rad.trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                UNION ALL
                SELECT rad.user_id,
                  rh.node_id,
                  rh.node_name,
                  CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 AS OnTheSpot         --08/12/2015
                 ,CASE WHEN rad.payout_type = 'other' THEN rad.other ELSE 0 END Other 
                 ,0 AS cash_received             
                FROM rpt_ssi_award_detail rad,
                  rpt_hierarchy rh,
                  participant p
                  ,promotion promo              
                  --,badge_promotion bp                       --01/13/2017 start --12/01/2017
                 , gtt_id_list gil_dt  -- department type   
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end  
                 , gtt_id_list gil_c -- country                                                         
                WHERE rh.node_id         = rad.node_id
                AND rad.user_id          = p.user_id
                AND rad.promotion_id      = promo.promotion_id
                      AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                      AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                              --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
            OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type ) 
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                        --AND (( p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                        AND p.status             = NVL (p_in_participantStatus, p.status)
                        AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                )ras
            GROUP BY ras.node_id,
            ras.node_name
          ) dtl
        WHERE rh.node_id         = dtl.node_id(+)
        AND NVL (rh.node_id, 0) IN
                (SELECT g.id FROM gtt_id_list g  
                        WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
        ) dtl
        GROUP BY GROUPING SETS
              ((), (dtl.Org,
                    dtl.node_id                     
                    )
              ) 
      ) )rs 
      ) s
             WHERE (  s.rec_seq = 0   -- totals record
          OR -- reduce sequenced data set to just the output page's records
             (   s.rec_seq > p_in_rowNumStart
             AND s.rec_seq < p_in_rowNumEnd )
          )
          AND eligible_participants <> 0          --03/09/2017
    ORDER BY s.rec_seq;

   -- query totals data
   v_stage := 'FETCH p_out_rs_getAwdSmryResult totals record';
   FETCH p_out_rs_getAwdSmryResult INTO rec_query;
   v_fetch_count := p_out_rs_getAwdSmryResult%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_rs_getAwdSmryResultTot FOR
   SELECT   rec_query.eligible_participants       AS eligible_participants,
            rec_query.received_award              AS received_award,
            rec_query.received_award_pct          AS received_award_pct,
            rec_query.have_notreceived_award      AS have_notreceived_award,
            rec_query.have_notreceived_award_pct  AS have_notreceived_award_pct,
            rec_query.points_received             AS points_received,
            rec_query.plateau_earned_count        AS plateau_earned_count,
            rec_query.sweepstakes_won_count       AS sweepstakes_won_count,
            rec_query.on_the_spot_deposited_count AS on_the_spot_deposited_count,
            rec_query.other                       AS other,
            rec_query.cash_received               AS cash_received      
   FROM dual
   WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_rs_getAwdSmryResult NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
     OPEN p_out_rs_getAwdSmryResult FOR
     SELECT CAST(NULL AS NUMBER) AS total_records,
            CAST(NULL AS NUMBER) AS rec_seq,
            CAST(NULL AS VARCHAR2(2000)) AS org_name,
            CAST(NULL AS NUMBER) AS eligible_participants,
            CAST(NULL AS NUMBER) AS received_award,
            CAST(NULL AS NUMBER) AS received_award_pct,
            CAST(NULL AS NUMBER) AS have_notreceived_award,
            CAST(NULL AS NUMBER) AS have_notreceived_award_pct,
            CAST(NULL AS NUMBER) AS points_received,
            CAST(NULL AS NUMBER) AS plateau_earned_count,
            CAST(NULL AS NUMBER) AS sweepstakes_won_count,
            CAST(NULL AS NUMBER) AS on_the_spot_deposited_count,
            CAST(NULL AS NUMBER) AS other,
            CAST(NULL AS NUMBER) AS node_id,                    
            CAST(NULL AS NUMBER) AS cash_received
    FROM dual
    WHERE 0=1;
   
   END IF;

   p_out_return_code := '00';

    EXCEPTION
    WHEN OTHERS THEN
      p_out_return_code := '99';
      prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
      OPEN p_out_rs_getAwdSmryResult FOR SELECT NULL FROM DUAL;
      OPEN p_out_rs_getAwdSmryResultTot FOR SELECT NULL FROM DUAL;
    END;
END; -- procedure end
--FIRST
PROCEDURE prc_getAwardsFirstDetail(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_rs_getAwdfirdtlresult OUT sys_refcursor,
    p_out_rs_getAwdfirdtlResultTot OUT SYS_REFCURSOR)
IS
   /******************************************************************************
  NAME:       prc_getAwardsFirstDetail
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far) 
  nagarajs              09/18/2014            Bug 56671 Fix - Sorting Issue          
  Ramkumar              03/02/2015            Bug 59560 Fix - last_name and first_name instead of full name from RPT_QCARD_DETAIL
  Suresh J              06/03/2015            Bug 62472 - Currency symbol is missing
  Swati                 06/12/2015            Bug 62752 - On the Awards Received report - do not display total for Other Value column  
  Suresh J               08/11/2015           Bug 63678 - In award report by ORG sort is not working for the column Other Value
  Suresh J              05/05/2016           New nomination changes
  nagarajs              06/27/2016           Bug 67271 - Awards Received - Participation by Organization 
                                            When admin click on "Bloomington_ Team" error message is getting displayed  
  nagarajs              10/06/2016           New nomination changes
  Sherif Basha          10/27/2016           Bug 69762 -- the view mv_cms_asset_value reference is moved to the temp table for performance issue fix
  nagarajs              12/09/2016          G5.6.3.3 report changes
  Suresh J              01/23/2017          Rewrote the queries for G5.6.3.3 reports performance tuning
  Chidamba              03/02/2018          G6-3432	- Changes to the Report due to OTS cards Enhancement 
  ******************************************************************************/
     
  c_delimiter     CONSTANT VARCHAR2 (1) := '|';
  c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
  v_team_ids      VARCHAR2 (4000);
  c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getAwardsFirstDetail';
  c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
  c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage         VARCHAR2(500);
  v_sortCol       VARCHAR2(200);
  v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016
  v_data_sort     VARCHAR2(50);
  v_fetch_count   INTEGER;

    CURSOR cur_query_ref IS
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS VARCHAR2(2000)) AS promotion_name, 
           CAST(NULL AS NUMBER) AS promotion_id, 
           CAST(NULL AS NUMBER) AS points_received_count, 
           CAST(NULL AS NUMBER) AS plateau_earned_count, 
           CAST(NULL AS NUMBER) AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER) AS on_the_spot_deposited_count, 
           CAST(NULL AS NUMBER) AS other, 
           CAST(NULL AS NUMBER) AS other_value, 
           CAST(NULL AS NUMBER) AS user_id, 
           CAST(NULL AS NUMBER) AS cash_received_count, 
           CAST(NULL AS VARCHAR2(2000)) AS level_name
    FROM dual;
 
    rec_query      cur_query_ref%ROWTYPE;    
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  

 BEGIN
  
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end
    
    v_stage := 'Get currency value';
    BEGIN    --12/09/2016
    SELECT ccc.bpom_entered_rate
     INTO v_cash_currency_value
     FROM user_address ua,
          country c,
          cash_currency_current ccc 
    WHERE ua.user_id = p_in_userid
      AND is_primary = 1
      AND ua.country_id = c.country_id
      AND c.country_code = ccc.to_cur;
    EXCEPTION
    WHEN OTHERS THEN
      v_cash_currency_value := 1;
    END;  
  
    DELETE temp_table_session;
    INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code, cms_value, key
      FROM mv_cms_asset_value
     WHERE key IN ('LEVEL_NAME','OTS_BATCH_NAME_')   --03/02/2018 added OTS_BATCH_NAME
       AND locale = p_in_languageCode 
     UNION ALL                                      -- added 10/27/2016 Bug 69762 
    SELECT asset_code, cms_value , key 
      FROM mv_cms_asset_value 
     WHERE key = 'PROMOTION_NAME_'
       AND locale = p_in_languageCode;               


    
      IF p_in_sortColName = 'OTHER_VALUE' THEN    --08/11/2015
        v_sortCol := 'TO_NUMBER(REGEXP_REPLACE('||p_in_sortColName||', ''[^0-9]+'', ''''))'|| ' ' || NVL(p_in_sortedBy, ' ');   --08/11/2015
      ELSE   
        v_sortCol := p_in_sortColName || ' ' || NVL(p_in_sortedBy, ' ');
      END IF;

        -- set data sort
        IF (LOWER(p_in_sortedBy) = 'desc') THEN
          v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
        ELSE
          v_data_sort := LOWER(p_in_sortColName);
        END IF;
    
    BEGIN
    
    OPEN p_out_rs_getAwdFirDtlResult FOR
        SELECT s.*
        FROM (
          SELECT 
           COUNT(rs.org_name) OVER() AS total_records,
           -- calc record sort order
           ROW_NUMBER() OVER (
                     ORDER BY
                     -- sort totals record first
                     DECODE(rs.org_name, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'participant_name',                        rs.participant_name),
                     DECODE(v_data_sort, 'org_name',                                lower(rs.org_name)),
                     DECODE(v_data_sort, 'department',                              lower(rs.department)),
                     DECODE(v_data_sort, 'job_position',                            rs.job_position),
                     DECODE(v_data_sort, 'promotion_name',                          rs.promotion_name),
                     DECODE(v_data_sort, 'promotion_id',                            rs.promotion_id),
                     DECODE(v_data_sort, 'points_received_count',                   rs.points_received_count),
                     DECODE(v_data_sort, 'plateau_earned_count',                    rs.plateau_earned_count),
                     DECODE(v_data_sort, 'sweepstakes_won_count',                   rs.sweepstakes_won_count),
                     DECODE(v_data_sort, 'on_the_spot_deposited_count',             rs.on_the_spot_deposited_count),                     
                     DECODE(v_data_sort, 'other',                                   rs.other),                     
                     DECODE(v_data_sort, 'other_value',                             rs.other_value),                                          
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/participant_name',                        rs.participant_name) DESC,
                     DECODE(v_data_sort, 'desc/org_name',                                lower(rs.org_name)) DESC,
                     DECODE(v_data_sort, 'desc/department',                              lower(rs.department)) DESC,
                     DECODE(v_data_sort, 'desc/job_position',                            lower(rs.job_position)) DESC,
                     DECODE(v_data_sort, 'desc/promotion_name',                          rs.promotion_name) DESC,
                     DECODE(v_data_sort, 'desc/promotion_id',                            rs.promotion_id) DESC,
                     DECODE(v_data_sort, 'desc/points_received_count',                   rs.points_received_count) DESC,
                     DECODE(v_data_sort, 'desc/plateau_earned_count',                    rs.plateau_earned_count) DESC,
                     DECODE(v_data_sort, 'desc/sweepstakes_won_count',                   rs.sweepstakes_won_count) DESC,
                     DECODE(v_data_sort, 'desc/on_the_spot_deposited_count',             rs.on_the_spot_deposited_count) DESC,                     
                     DECODE(v_data_sort, 'desc/other',                                   rs.other) DESC,                     
                     DECODE(v_data_sort, 'desc/other_value',                             rs.other_value) DESC,                                          
                     -- default sort fields
                     LOWER(rs.org_name),
                     rs.org_name
                   ) -1 AS rec_seq
                    --,rs.org_name
                ,rs.participant_name,
                rs.org_name,
                rs.country,
                rs.department,
                rs.job_position,
                rs.promotion_name,
                rs.promotion_id,
                rs.points_received_count,
                rs.plateau_earned_count,
                rs.sweepstakes_won_count,
                rs.on_the_spot_deposited_count, 
                rs.other,                     
                rs.other_value,
                rs.cash_received_count,
                rs.level_name,  
                rs.user_id                  
    FROM
      (
  SELECT --rs.*
        rs.participant_name, 
        org_name, 
        country, 
        department, 
        job_position, 
        promotion_name, 
        promotion_id, 
        user_id, 
        level_name,
        SUM(points_received_count) points_received_count, 
        SUM(plateau_earned_count)  plateau_earned_count, 
        SUM(sweepstakes_won_count) sweepstakes_won_count, 
        SUM(on_the_spot_deposited_count) on_the_spot_deposited_count, 
        SUM(other) other, 
        SUM(other_value) other_value, 
        SUM(cash_received_count) cash_received_count 
    FROM (
    SELECT rad.pax_last_name
         ||', '||
         rad.pax_first_name                                                                         AS PARTICIPANT_NAME,
        nh.node_name                                                                                  AS ORG_NAME,
        fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode)               AS COUNTRY,
        fnc_cms_picklist_value ( 'picklist.department.type.items', rad.department, p_in_languageCode) AS DEPARTMENT,
        fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode)  AS JOB_POSITION,
        cp.promotion_name                                                                             AS PROMOTION_NAME,
        rad.promotion_id                                                                              AS PROMOTION_ID,
        --SUM(NVL(rad.media_amount,0))                                                                  AS POINTS_RECEIVED_COUNT, --05/05/2016
        CASE WHEN rad.award_payout_type = 'points' THEN  --05/05/2016
              SUM(NVL(rad.media_amount,0))
              ELSE 0
        END                                                                                             AS POINTS_RECEIVED_COUNT,
        SUM(NVL(plateau_earned,0))                                                                    AS PLATEAU_EARNED_COUNT,
        SUM(NVL(sweepstakes_won,0))                                                                   AS SWEEPSTAKES_WON_COUNT,
        0                                                                                             AS ON_THE_SPOT_DEPOSITED_COUNT,
        rad.user_id
        ,0 AS OTHER  --05/12/2015 
--        ,0 AS OTHER_VALUE  --05/12/2015 
        ,CASE WHEN rad.award_payout_type = 'other' THEN  --10/06/2016
              1
              ELSE 0
        END AS OTHER_VALUE  --06/03/2015   
         ,CASE WHEN rad.award_payout_type = 'cash' THEN  --05/05/2016
              SUM(NVL(rad.media_amount,0))
              ELSE 0
          END                                                                                       AS CASH_RECEIVED_COUNT    --05/05/2016
          ,(SELECT t.cms_name as level_name 
             FROM temp_table_session t 
             WHERE t.asset_code=rad.asset_key_level_name 
              AND  t.cms_code = 'LEVEL_NAME')                                                        AS LEVEL_NAME             --05/05/2016     
      FROM RPT_AWARDMEDIA_DETAIL rad,
           rpt_hierarchy nh,
           promotion p,
           country c,
           (SELECT asset_code, cms_name promotion_name 
              FROM temp_table_session 
             WHERE cms_code = 'PROMOTION_NAME_' ) cp --07/23/2014   -- 10/27/2016 Bug 69762
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end     
             , gtt_id_list gil_c  -- country                                                      
            WHERE rad.node_id    = nh.node_id
              AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
              AND rad.promotion_id = p.promotion_id
              AND rad.country_id   = c.country_id
              AND p.promo_name_asset_code = cp.asset_code (+) --07/23/2014
              AND rad.node_id     IN (SELECT g.id FROM gtt_id_list g                                         
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )

              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
              AND rad.media_amount              <> 0
              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
              AND p.promotion_type <> 'self_serv_incentives'     --06/04/2015         
              AND NVL (TRUNC (media_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
              GROUP BY rad.pax_last_name
                 ||', '||
                 rad.pax_first_name,
                nh.node_name,
                fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode),
                fnc_cms_picklist_value ( 'picklist.department.type.items', rad.department, p_in_languageCode),
                fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode),
                cp.promotion_name,
                rad.promotion_id,
                rad.user_id
                ,rad.asset_key_level_name  --06/27/2016
                ,rad.award_payout_type              --05/05/2016
      UNION ALL
      SELECT rad.pax_last_name
         ||', '||
         rad.pax_first_name                                                                         AS PARTICIPANT_NAME,
        nh.node_name                                                                                  AS ORG_NAME,
        fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode)               AS COUNTRY,
        fnc_cms_picklist_value ( 'picklist.department.type.items', rad.department, p_in_languageCode) AS DEPARTMENT,
        fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode)  AS JOB_POSITION,
        cp.promotion_name                                                                            AS PROMOTION_NAME,
        rad.promotion_id                                                                              AS PROMOTION_ID,
        --SUM(NVL(rad.media_amount,0))                                                                  AS POINTS_RECEIVED_COUNT, --05/05/2016
        CASE WHEN rad.award_payout_type = 'points' THEN  --05/05/2016
              SUM(NVL(rad.media_amount,0))
              ELSE 0
        END                                                                                             AS POINTS_RECEIVED_COUNT,
        SUM(NVL(plateau_earned,0))                                                                    AS PLATEAU_EARNED_COUNT,
        SUM(NVL(sweepstakes_won,0))                                                                   AS SWEEPSTAKES_WON_COUNT,
        0                                                                                             AS ON_THE_SPOT_DEPOSITED_COUNT,
        rad.user_id
        ,0 AS OTHER  --05/12/2015 --10/06/2016
--        ,0 AS OTHER_VALUE  --05/12/2015
        ,CASE WHEN rad.award_payout_type = 'other' THEN  --10/06/2016
              1
              ELSE 0
        END AS OTHER_VALUE  --06/03/2015        
         ,CASE WHEN rad.award_payout_type = 'cash' THEN  --05/05/2016
              SUM(NVL(rad.media_amount,0))
              ELSE 0
         END                                                               AS CASH_RECEIVED_COUNT    --05/05/2016
          ,(SELECT t.cms_name as level_name 
             FROM temp_table_session t 
             WHERE t.asset_code=rad.asset_key_level_name 
              AND  t.cms_code = 'LEVEL_NAME')                                                        AS LEVEL_NAME --05/05/2016     
      FROM RPT_AWARDMEDIA_DETAIL rad,
        rpt_hierarchy nh,
        promotion p,
        country c,
        (SELECT asset_code, cms_name promotion_name 
           FROM temp_table_session 
          WHERE cms_code = 'PROMOTION_NAME_') cp --07/23/2014  --10/27/2016 Bug 69762
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end  
         , gtt_id_list gil_pn           
         , gtt_id_list gil_c                                              
        WHERE rad.node_id    = nh.node_id
          AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type)  --10/06/2016
          AND rad.promotion_id = p.promotion_id
          AND rad.country_id   = c.country_id
          AND p.promo_name_asset_code = cp.asset_code (+) --07/23/2014
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
          AND (gil_pn.ref_text_1 = gc_ref_text_parent_node_id  
          AND gil_pn.id = rad.node_id)      
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
          AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
          AND NVL (TRUNC (media_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015    
          AND p.promotion_type <> 'self_serv_incentives'  --06/04/2015             
          AND NVL (rad.media_amount, 0) = 0
          GROUP BY rad.pax_last_name
             ||', '||
             rad.pax_first_name,
            nh.node_name,
            fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode),
            fnc_cms_picklist_value ( 'picklist.department.type.items', rad.department, p_in_languageCode),
            fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode),
            cp.promotion_name,
            rad.promotion_id,
            rad.user_id
            ,rad.asset_key_level_name  --06/27/2016
            ,rad.award_payout_type          --05/05/2016
      UNION ALL
      SELECT rad.user_last_name
         ||', '||
         rad.user_first_name                                                                             AS PARTICIPANT_NAME,
        rad.node_name                                                                                AS ORG_NAME,
        fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode)              AS COUNTRY,
        fnc_cms_picklist_value ( 'picklist.department.type.items', department, p_in_languageCode)    AS DEPARTMENT,
        fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode) AS JOB_POSITION,
        CASE WHEN rad.batch_description IS NULL THEN 
        fnc_cms_asset_code_val_extr( 'report.awards.participant', 'ONTHESPOT', p_in_languageCode ) --07/31/2014 Bug 54209    --03/02/2018
        WHEN rad.batch_description like '%'||gc_str_token  THEN  REPLACE(rad.batch_description,gc_str_token)                    --03/02/2018 
        ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code=rad.batch_description) END   AS PROMOTION_NAME, --03/02/2018        
        NULL                                                                                         AS PROMOTION_ID,
        SUM(NVL(rad.transaction_amount,0))                                                           AS POINTS_RECEIVED_COUNT,
        0                                                                                            AS PLATEAU_EARNED_COUNT,
        0                                                                                            AS SWEEPSTAKES_WON_COUNT,
        COUNT(1)                                                                                     AS ON_THE_SPOT_DEPOSITED_COUNT, --07/31/2014 Bug 54207 
        rad.user_id
        ,0 AS OTHER  --05/12/2015
--        ,0 AS OTHER_VALUE  --05/12/2015
        ,0 AS OTHER_VALUE  --06/03/2015 --10/06/2016
        ,0     AS CASH_RECEIVED_COUNT    --05/05/2016
        ,NULL  AS LEVEL_NAME             --05/05/2016                                     
      FROM RPT_QCARD_DETAIL rad,
        rpt_hierarchy nh,
        country c,
        participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_pn    
         , gtt_id_list gil_c                                                     
      WHERE rad.node_id  = nh.node_id
          AND rad.user_id    = p.user_id
          AND rad.country_id = c.country_id
          AND (gil_pn.ref_text_1 = gc_ref_text_parent_node_id  
          AND gil_pn.id = rad.node_id)      
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
          AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
          AND p.status             = NVL (p_in_participantStatus, p.status)
          AND NVL (TRUNC (trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      GROUP BY rad.user_last_name
         ||', '||
         rad.user_first_name,
        rad.node_name,
        fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode),
        fnc_cms_picklist_value ( 'picklist.department.type.items', rad.department, p_in_languageCode),
        fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode),
        rad.user_id,
	rad.batch_description --03/02/2018
--05/12/2015
      UNION ALL
      SELECT rad.pax_last_name
         ||', '||
         rad.pax_first_name                                                                             AS PARTICIPANT_NAME,
        rad.node_name                                                                                AS ORG_NAME,
        fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode)              AS COUNTRY,
        fnc_cms_picklist_value ( 'picklist.department.type.items', department, p_in_languageCode)    AS DEPARTMENT,
        fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode) AS JOB_POSITION,
        fnc_cms_asset_code_val_extr( rad.ssi_contest_name, 'CONTEST_NAME', p_in_languageCode )   AS PROMOTION_NAME,
        rad.ssi_contest_id                                                                                   AS PROMOTION_ID,
        SUM(NVL(CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END,0))  AS POINTS_RECEIVED_COUNT,
        0                                                                                            AS PLATEAU_EARNED_COUNT,
        0                                                                                            AS SWEEPSTAKES_WON_COUNT,
        0                                                                                     AS ON_THE_SPOT_DEPOSITED_COUNT, --07/31/2014 Bug 54207   --08/12/2015 
        rad.user_id
        ,0                                  AS OTHER , 
--        ,SUM(CASE WHEN rad.payout_type = 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END)            AS OTHER_VALUE         --06/03/2015
    --      ,CASE WHEN rad.payout_type = 'other' THEN NVL(cur.currency_symbol,'$') TO_CHAR(SUM(NVL (rad.payout_amount, 0))) ELSE NVL(cur.currency_symbol,'$')'0' END    AS OTHER_VALUE       --06/03/2015 --10/06/2016
        CASE WHEN rad.payout_type = 'other' THEN  --10/06/2016
                  1
                  ELSE 0
            END AS OTHER_VALUE
       ,0 AS CASH_RECEIVED_COUNT    --05/05/2016
       ,NULL  AS LEVEL_NAME             --05/05/2016                  
      FROM rpt_ssi_award_detail rad,
        rpt_hierarchy nh,
        country c,
        participant p
        ,promotion promo 
        ,(
            select sc.ssi_contest_id, 
                 sc.activity_measure_type,
                 sc.activity_measure_cur_code,
                 c.currency_code,
                 c.currency_symbol 
           from ssi_contest sc,
                currency c 
           where sc.activity_measure_type = 'currency'  and 
                 UPPER(sc.activity_measure_cur_code) = c.currency_code (+) 
                 ) cur
         --, badge_promotion bp   --12/01/2017      
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end  
         , gtt_id_list gil_pn              
         , gtt_id_list gil_c  -- country                                           
      WHERE rad.node_id  = nh.node_id
          AND rad.user_id    = p.user_id
          AND rad.country_id = c.country_id
          AND rad.ssi_contest_id = cur.ssi_contest_id (+)
          AND rad.promotion_id      = promo.promotion_id
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = promo.promotion_id 
                  --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                   OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
          AND (gil_pn.ref_text_1 = gc_ref_text_parent_node_id  
          AND gil_pn.id = rad.node_id)      
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          --AND (( p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
        --AND promo.promotion_status   = NVL (/*p_in_promotionStatus*/, promo.promotion_status) --12/09/2016
          AND p.status             = NVL (p_in_participantStatus, p.status)
          AND NVL (TRUNC (payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
      GROUP BY 
         rad.pax_last_name||', '||rad.pax_first_name,
        rad.node_name,
        fnc_cms_asset_code_val_extr (c.cm_asset_code, c.name_cm_key, p_in_languageCode),
        fnc_cms_picklist_value ( 'picklist.department.type.items', rad.department, p_in_languageCode),
        fnc_cms_picklist_value ('picklist.positiontype.items', rad.position_type, p_in_languageCode),
        rad.user_id,
        fnc_cms_asset_code_val_extr( rad.ssi_contest_name, 'CONTEST_NAME', p_in_languageCode )
        ,rad.ssi_contest_id 
        ,cur.currency_symbol,rad.payout_type               
      ) rs
        GROUP BY GROUPING SETS
              ((), (
                    rs.participant_name,
                    rs.org_name,
                    rs.country,
                    rs.department,
                    rs.job_position,
                    rs.promotion_name,
                    rs.promotion_id,
                    rs.user_id,
                    rs.level_name   
                     )
              ) 
        ) rs ) s
             WHERE (  s.rec_seq = 0   -- totals record
          OR -- reduce sequenced data set to just the output page's records
             (   s.rec_seq > p_in_rowNumStart
             AND s.rec_seq < p_in_rowNumEnd )
          )
    ORDER BY s.rec_seq;

   -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_rs_getAwdfirdtlresult INTO rec_query;
   v_fetch_count := p_out_rs_getAwdfirdtlresult%ROWCOUNT;

   v_stage := 'OPEN p_out_rs_getAwdfirdtlResultTot';
   OPEN p_out_rs_getAwdfirdtlResultTot FOR
   SELECT   rec_query.points_received_count         AS points_received_count,
            rec_query.plateau_earned_count          AS plateau_earned_count,
            rec_query.sweepstakes_won_count         AS sweepstakes_won_count,
            rec_query.on_the_spot_deposited_count   AS on_the_spot_deposited_count,
            rec_query.other                         AS other,
            rec_query.other_value                   AS other_value,
            rec_query.cash_received_count           AS cash_received_count
   FROM dual
   WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
     OPEN p_out_rs_getAwdfirdtlresult FOR
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS VARCHAR2(2000)) AS promotion_name, 
           CAST(NULL AS NUMBER) AS promotion_id, 
           CAST(NULL AS NUMBER) AS points_received_count, 
           CAST(NULL AS NUMBER) AS plateau_earned_count, 
           CAST(NULL AS NUMBER) AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER) AS on_the_spot_deposited_count, 
           CAST(NULL AS NUMBER) AS other, 
           CAST(NULL AS NUMBER) AS other_value, 
           CAST(NULL AS NUMBER) AS user_id, 
           CAST(NULL AS NUMBER) AS cash_received_count, 
           CAST(NULL AS VARCHAR2(2000)) AS level_name
    FROM dual
    WHERE 0=1;
   
   END IF;

  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_rs_getAwdFirDtlResultTot FOR SELECT NULL FROM DUAL;
END;
END; --procedure end
--SECOND
PROCEDURE prc_getAwardsSecondDetail(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_rs_getAwdsecdtlresult OUT sys_refcursor,
    p_out_rs_getAwdsecdtlResultTot OUT SYS_REFCURSOR)
IS
   /******************************************************************************
  NAME:       prc_getAwardsSecondDetail
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014      Initial..(the queries were in Java before the release 5.4. So no comments available so far)                                 
  Ramkumar              03/02/2015      Bug 59560 Fix - last_name and first_name instead of full name from RPT_QCARD_DETAIL
  Suresh J              06/03/2015      Bug 62472 - Currency symbol is missing  
  Swati                 06/12/2015      Bug 62752 - On the Awards Received report - do not display total for Other Value column
  nagarajs              10/08/2015      Bug 63678 - For SSI promotions, no result displaying   
  nagarajs              06/27/2016      Bug 67271 - Awards Received - Participation by Organization 
                                        When admin click on "Bloomington_ Team" error message is getting displayed   
  Sherif Basha          10/18/2016      Bug 69529 - the input promotion id (for ssi is ssi_contest_id) is passed from java in turn
                                        fetched from prc_getAwardsFirstDetail.so ssi_contest_id will be passed here will be input for ssi here
 Sherif Basha          10/24/2016       Bug 69713 - When multiple promotions are passed the ssi query the fails due to comma concatenation.
 nagarajs             12/09/2016        G5.6.3.3 report changes
 Suresh J             01/23/2017        Rewrote the queries for G5.6.3.3 reports performance tuning 
 Suresh J             03/07/2017        G6-1878 -  parsererror is show when Basic = Cash   
  Chidamba            03/02/2018          G6-3432	- Changes to the Report due to OTS cards Enhancement 
 ******************************************************************************/
     
  c_delimiter     CONSTANT VARCHAR2 (1) := '|';
  c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
  v_team_ids      VARCHAR2 (4000);
  c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getAwardsSecondDetail';
  c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
  c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage         VARCHAR2(500);
  v_sortCol             VARCHAR2(200);
  v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016

  v_data_sort     VARCHAR2(50);
  v_fetch_count   INTEGER;

  CURSOR cur_query_ref IS
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS DATE)           AS award_date, 
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS VARCHAR2(2000)) AS promotion_name, 
           CAST(NULL AS NUMBER)         AS points_received_count, 
           CAST(NULL AS NUMBER)         AS plateau_earned_count, 
           CAST(NULL AS NUMBER)         AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER)         AS on_the_spot_serial, 
           CAST(NULL AS VARCHAR2(2000)) AS other, 
           CAST(NULL AS NUMBER)         AS other_value, 
           CAST(NULL AS NUMBER)         AS cash_received_count, 
           CAST(NULL AS VARCHAR2(2000)) AS level_name
    FROM dual;
 
   rec_query      cur_query_ref%ROWTYPE;    
  
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  

BEGIN
  
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  v_stage   := 'getAwardsSecDtlResults';
  v_sortCol := p_in_sortColName || ' ' || NVL(p_in_sortedBy, ' ');
 
   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;

  BEGIN
     v_stage := 'Get currency value';
      BEGIN    --12/09/2016
       SELECT ccc.bpom_entered_rate
         INTO v_cash_currency_value
         FROM user_address ua,
              country c,
              cash_currency_current ccc 
        WHERE ua.user_id = p_in_userid
          AND is_primary = 1
          AND ua.country_id = c.country_id
          AND c.country_code = ccc.to_cur;
      EXCEPTION
        WHEN OTHERS THEN
          v_cash_currency_value := 1;
      END;  
      
    DELETE temp_table_session;
    INSERT INTO temp_table_session (asset_code, cms_name, cms_code)
    SELECT asset_code, cms_value, key
      FROM mv_cms_asset_value
     WHERE key IN ( 'LEVEL_NAME','PAYOUT_DESCRIPTION_','OTS_BATCH_NAME_') --03/02/2018 added OTS_BATCH_NAME
           AND locale = p_in_languageCode;    

        v_stage := 'OPEN p_out_rs_getAwdsecdtlresult';
        OPEN p_out_rs_getAwdsecdtlresult FOR
        SELECT s.*
        FROM (
          SELECT 
           COUNT(rs.promotion_name) OVER() AS total_records,
           -- calc record sort order
           ROW_NUMBER() OVER (
             ORDER BY
             -- sort totals record first
             DECODE(rs.promotion_name, NULL, 0, 99),
             -- ascending sorts
             DECODE(v_data_sort, 'award_date',                              rs.award_date),
             DECODE(v_data_sort, 'participant_name',                        rs.participant_name),
             DECODE(v_data_sort, 'org_name',                                lower(rs.org_name)),
             DECODE(v_data_sort, 'country',                                 lower(rs.country)),
             DECODE(v_data_sort, 'department',                              lower(rs.department)),
             DECODE(v_data_sort, 'job_position',                            rs.job_position),
             DECODE(v_data_sort, 'promotion_name',                          rs.promotion_name),
             DECODE(v_data_sort, 'points_received_count',                   rs.points_received_count),
             DECODE(v_data_sort, 'plateau_earned_count',                    rs.plateau_earned_count),
             DECODE(v_data_sort, 'sweepstakes_won_count',                   rs.sweepstakes_won_count),
             DECODE(v_data_sort, 'on_the_spot_serial',                      rs.on_the_spot_serial),                     
             DECODE(v_data_sort, 'other',                                   rs.other),                     
             DECODE(v_data_sort, 'other_value',                             rs.other_value),                                          
             DECODE(v_data_sort, 'cash_received_count',                     rs.cash_received_count),                                          
             DECODE(v_data_sort, 'level_name',                              rs.level_name),                                          
             -- descending sorts
             DECODE(v_data_sort, 'desc/award_date',                              rs.award_date) DESC,
             DECODE(v_data_sort, 'desc/participant_name',                        rs.participant_name) DESC,
             DECODE(v_data_sort, 'desc/org_name',                                lower(rs.org_name)) DESC,
             DECODE(v_data_sort, 'desc/country',                                 lower(rs.country)) DESC,
             DECODE(v_data_sort, 'desc/department',                              lower(rs.department)) DESC,
             DECODE(v_data_sort, 'desc/job_position',                            lower(rs.job_position)) DESC,
             DECODE(v_data_sort, 'desc/promotion_name',                          rs.promotion_name) DESC,
             DECODE(v_data_sort, 'desc/points_received_count',                   rs.points_received_count) DESC,
             DECODE(v_data_sort, 'desc/plateau_earned_count',                    rs.plateau_earned_count) DESC,
             DECODE(v_data_sort, 'desc/sweepstakes_won_count',                   rs.sweepstakes_won_count) DESC,
             DECODE(v_data_sort, 'desc/on_the_spot_serial',                      rs.on_the_spot_serial) DESC,                     
             DECODE(v_data_sort, 'desc/other',                                   rs.other) DESC,                     
             DECODE(v_data_sort, 'desc/other_value',                             rs.other_value) DESC,                                          
             DECODE(v_data_sort, 'desc/cash_received_count',                     rs.cash_received_count) DESC,                                          
             DECODE(v_data_sort, 'desc/level_name',                              rs.level_name) DESC,                                          
             -- default sort fields
             LOWER(rs.promotion_name),
             rs.promotion_name
           ) -1 AS rec_seq,
         award_date, 
         participant_name, 
         org_name, 
         country, 
         department, 
         job_position, 
         promotion_name, 
         points_received_count, 
         plateau_earned_count, 
         sweepstakes_won_count, 
         on_the_spot_serial,
         other,other_value,
         (cash_received_count * v_cash_currency_value) as cash_received_count, 
         level_name
     FROM
    ( 
    SELECT  rs.award_date,
            rs.participant_name,
            rs.org_name,
            rs.country,
            rs.department,
            rs.job_position,
            rs.promotion_name,
            rs.level_name,
            SUM(rs.points_received_count) points_received_count, 
            SUM(rs.plateau_earned_count)  plateau_earned_count, 
            SUM(rs.sweepstakes_won_count) sweepstakes_won_count, 
            SUM(rs.on_the_spot_serial)    on_the_spot_serial,
            rs.other,
            --SUM(rs.other)                 other,
            other_value              other_value,           --03/07/2017
            SUM(rs.cash_received_count)   cash_received_count                  
    FROM
      (SELECT rad.media_date AS AWARD_DATE,
        rad.pax_last_name
        ||', '||
        rad.pax_first_name                                                                        AS PARTICIPANT_NAME,
        nh.node_name                                                                                AS ORG_NAME,
        fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)             AS COUNTRY,
        fnc_cms_picklist_value('picklist.department.type.items',rad.department, p_in_languageCode)  AS DEPARTMENT,
        fnc_cms_picklist_value('picklist.positiontype.items', rad.position_type, p_in_languageCode) AS JOB_POSITION,
        cp.promotion_name                                                                          AS PROMOTION_NAME,
        --NVL (rad.media_amount, 0)                                                                   AS POINTS_RECEIVED_COUNT,  --05/05/2016
        CASE WHEN rad.award_payout_type = 'points' THEN                                                                       --05/05/2016
        NVL (rad.media_amount, 0)
        ELSE 0
        END                                                                   AS POINTS_RECEIVED_COUNT,
        plateau_earned                                                                              AS PLATEAU_EARNED_COUNT,
        sweepstakes_won                                                                             AS SWEEPSTAKES_WON_COUNT,
        NULL                                                                                        AS ON_THE_SPOT_SERIAL
        ,(SELECT cms_name FROM temp_table_session WHERE asset_code = rad.asset_key_level_name) AS OTHER  --05/12/2015 --10/06/2016
--        ,0 AS OTHER_VALUE  --05/12/2015
        ,CASE WHEN rad.award_payout_type = 'other' THEN                                                                       --05/05/2016
        TO_CHAR(NVL (rad.media_amount, 0))
        ELSE '0'
        END   AS OTHER_VALUE  --06/03/2015  --10/06/2016
        ,CASE WHEN rad.award_payout_type = 'cash' THEN                                                                       --05/05/2016
        NVL (rad.media_amount, 0)
        ELSE 0
        END                                                               AS CASH_RECEIVED_COUNT    --05/05/2016
          ,(SELECT t.cms_name as level_name 
             FROM temp_table_session t 
             WHERE t.asset_code=rad.asset_key_level_name 
              AND  t.cms_code = 'LEVEL_NAME')                                                     AS LEVEL_NAME --05/05/2016     
      FROM RPT_AWARDMEDIA_DETAIL rad,
        rpt_hierarchy nh,
        promotion p,
        country c,
        (SELECT asset_code, cms_value promotion_name 
           FROM mv_cms_asset_value 
          WHERE key = 'PROMOTION_NAME_'
            AND locale = p_in_languageCode) cp --07/23/2014
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end
         , gtt_id_list gil_c  -- country                                                           
      WHERE       rad.node_id        = nh.node_id
              AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
              AND rad.promotion_id     = p.promotion_id
              AND rad.country_id       = c.country_id
              AND p.promo_name_asset_code = cp.asset_code (+) --07/23/2014
              AND rad.user_id          = NVL(p_in_userid, rad.user_id)
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
              AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
              AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
              AND p.promotion_type <> 'self_serv_incentives'        
              AND rad.media_amount <> 0
      UNION ALL
      SELECT rad.media_date AS AWARD_DATE,
        rad.pax_last_name
        ||', '||
        rad.pax_first_name                                                                        AS PARTICIPANT_NAME,
        nh.node_name                                                                                AS ORG_NAME,
        fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)             AS COUNTRY,
        fnc_cms_picklist_value('picklist.department.type.items',rad.department, p_in_languageCode)  AS DEPARTMENT,
        fnc_cms_picklist_value('picklist.positiontype.items', rad.position_type, p_in_languageCode) AS JOB_POSITION,
        cp.promotion_name                                                                          AS PROMOTION_NAME,
         --NVL (rad.media_amount, 0)                                                                   AS POINTS_RECEIVED_COUNT,  --05/05/2016
        CASE WHEN rad.award_payout_type = 'points' THEN                                                                       --05/05/2016
        NVL (rad.media_amount, 0)
        ELSE 0
        END                                                                   AS POINTS_RECEIVED_COUNT,
        plateau_earned                                                                              AS PLATEAU_EARNED_COUNT,
        sweepstakes_won                                                                             AS SWEEPSTAKES_WON_COUNT,
        NULL                                                                                        AS ON_THE_SPOT_SERIAL
        ,(SELECT cms_name FROM temp_table_session WHERE asset_code = rad.asset_key_level_name) AS OTHER  --05/12/2015 --10/06/2016
--        ,0 AS OTHER_VALUE  --05/12/2015
        ,CASE WHEN rad.award_payout_type = 'other' THEN                                                                       --05/05/2016
        TO_CHAR(NVL (rad.media_amount, 0))
        ELSE '0'
        END   AS OTHER_VALUE  --06/03/2015  --10/06/2016
        ,CASE WHEN rad.award_payout_type = 'cash' THEN                                                                       --05/05/2016
        NVL (rad.media_amount, 0)
        ELSE 0
        END                                                               AS CASH_RECEIVED_COUNT    --05/05/2016
          ,(SELECT t.cms_name as level_name 
             FROM temp_table_session t 
             WHERE t.asset_code=rad.asset_key_level_name 
              AND  t.cms_code = 'LEVEL_NAME')                                                     AS LEVEL_NAME --05/05/2016     
      FROM RPT_AWARDMEDIA_DETAIL rad,
        rpt_hierarchy nh,
        promotion p,
        country c,
        (SELECT asset_code, cms_value promotion_name 
           FROM mv_cms_asset_value 
          WHERE key = 'PROMOTION_NAME_'
            AND locale = p_in_languageCode) cp --07/23/2014
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end
         , gtt_id_list gil_c  -- country                                                           
          WHERE rad.node_id        = nh.node_id
                  AND rad.award_payout_type = NVL(p_in_award_type,rad.award_payout_type) --10/06/2016
                  AND rad.promotion_id     = p.promotion_id
                  AND rad.country_id       = c.country_id
                  AND p.promo_name_asset_code = cp.asset_code (+) --07/23/2014
                  AND rad.user_id          = NVL(p_in_userid, rad.user_id)
                     AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                      AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = p.promotion_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                      AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                      AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                      AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015        
                      AND NVL(rad.media_amount,0) = 0
      UNION ALL
      SELECT trans_date                                                                             AS AWARD_DATE,
        rad.user_last_name
        ||', '||
        rad.user_first_name                                                                       AS PARTICIPANT_NAME,
        rad.node_name                                                                               AS ORG_NAME,
        fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)             AS COUNTRY,
        fnc_cms_picklist_value('picklist.department.type.items',department, p_in_languageCode)      AS DEPARTMENT,
        fnc_cms_picklist_value('picklist.positiontype.items', rad.position_type, p_in_languageCode) AS JOB_POSITION,
	    CASE WHEN rad.batch_description IS NULL THEN 
        fnc_cms_asset_code_val_extr( 'report.awards.participant', 'ONTHESPOT', p_in_languageCode ) --07/31/2014 Bug 54209    --03/02/2018
        WHEN rad.batch_description like '%'||gc_str_token THEN REPLACE(rad.batch_description,gc_str_token)                     --03/02/2018 
        ELSE (SELECT t.cms_name FROM temp_table_session t WHERE t.asset_code=rad.batch_description) END   AS PROMOTION_NAME, --03/02/2018
        rad.transaction_amount                                                                      AS POINTS_RECEIVED_COUNT,
        0                                                                                           AS PLATEAU_EARNED_COUNT,
        0                                                                                           AS SWEEPSTAKES_WON_COUNT,
        cert_num                                                                                    AS ON_THE_SPOT_SERIAL
        ,NULL                                                                                          AS OTHER  --05/12/2015 --10/06/2016
--        ,0                                                                                          AS OTHER_VALUE  --05/12/2015
        ,'0'                                                                                      AS OTHER_VALUE  --06/03/2015
        ,0                                                                                          AS CASH_RECEIVED_COUNT    --05/05/2016 
        ,NULL                                                                                          AS LEVEL_NAME --05/05/2016  --06/27/2016    
      FROM RPT_QCARD_DETAIL rad,
        rpt_hierarchy nh,
        country c,
        participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c   -- country
      WHERE rad.node_id       = nh.node_id
      AND rad.user_id         = p.user_id
      AND rad.country_id      = c.country_id
      AND rad.user_id         = NVL(p_in_userid, rad.user_id)
      --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
      AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
      AND p.status            = NVL (p_in_participantStatus, p.status)
      AND gil_dt.ref_text_1 = gc_ref_text_department_type
      AND (  gil_dt.ref_text_2 = gc_search_all_values
              OR gil_dt.ref_text_2 = rad.department ) 
      AND gil_pt.ref_text_1 = gc_ref_text_position_type
      AND (  gil_pt.ref_text_2 = gc_search_all_values
             OR gil_pt.ref_text_2 = rad.position_type )
      AND gil_c.ref_text_1 = gc_ref_text_country_id 
      AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
      AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
--05/12/2015
      UNION ALL
      SELECT rad.payout_issue_date                                                                 AS AWARD_DATE,
        rad.pax_last_name
        ||', '||
        rad.pax_first_name                                                                       AS PARTICIPANT_NAME,
        rad.node_name                                                                               AS ORG_NAME,
        fnc_cms_asset_code_val_extr( c.cm_asset_code, c.name_cm_key, p_in_languageCode)             AS COUNTRY,
        fnc_cms_picklist_value('picklist.department.type.items',department, p_in_languageCode)      AS DEPARTMENT,
        fnc_cms_picklist_value('picklist.positiontype.items', rad.position_type, p_in_languageCode) AS JOB_POSITION,
        fnc_cms_asset_code_val_extr( rad.ssi_contest_name, 'CONTEST_NAME', p_in_languageCode )  AS PROMOTION_NAME,
        CASE WHEN rad.payout_type <> 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END           AS POINTS_RECEIVED_COUNT,
        0                                                                                           AS PLATEAU_EARNED_COUNT,
        0                                                                                           AS SWEEPSTAKES_WON_COUNT,
        NULL                                                                                        AS ON_THE_SPOT_SERIAL
 --       ,CASE WHEN rad.payout_type = 'other' THEN TO_CHAR(rad.other) ELSE '0' END                                     AS OTHER   --10/06/2016
           ,rad.payout_description AS OTHER --10/06/2016
--        ,CASE WHEN rad.payout_type = 'other' THEN NVL (rad.payout_amount, 0) ELSE 0 END         AS OTHER_VALUE  --06/03/2015
        ,CASE WHEN rad.payout_type = 'other' 
            THEN NVL(cur.currency_symbol,'$')|| TO_CHAR(NVL (rad.payout_amount, 0)) 
            ELSE NVL(cur.currency_symbol,'$')||'0' END                                          AS OTHER_VALUE  --06/03/2015
        ,0                                                                                          AS CASH_RECEIVED_COUNT    --05/05/2016
        ,NULL                                                                                          AS LEVEL_NAME --05/05/2016  --06/27/2016                        
      FROM rpt_ssi_award_detail rad,
        rpt_hierarchy nh,
        country c,
        participant p
        ,promotion promo        
        ,(
            select sc.ssi_contest_id, 
                 sc.activity_measure_type,
                 sc.activity_measure_cur_code,
                 c.currency_code,
                 c.currency_symbol 
           from ssi_contest sc,
                currency c 
           where sc.activity_measure_type = 'currency'  and 
                 UPPER(sc.activity_measure_cur_code) = c.currency_code (+) 
                 ) cur
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end
         , gtt_id_list gil_c  -- country                                                           
      WHERE rad.node_id       = nh.node_id
          AND rad.user_id         = p.user_id
          AND rad.country_id      = c.country_id
          AND rad.promotion_id      = promo.promotion_id
          AND rad.ssi_contest_id = cur.ssi_contest_id (+)
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = rad.ssi_contest_id )
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          --AND (( p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
        --AND promo.promotion_status   = NVL (/*p_in_promotionStatus*/, promo.promotion_status) --12/09/2016
          AND rad.user_id         = NVL(p_in_userid, rad.user_id)
          AND p.status            = NVL (p_in_participantStatus, p.status)
          AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
--05/12/2015
      ) rs
              GROUP BY GROUPING SETS
              ((), (
                    rs.award_date,
                    rs.participant_name,
                    rs.org_name,
                    rs.country,
                    rs.department,
                    rs.job_position,
                    rs.promotion_name,
                    rs.level_name
                    ,rs.other
                    ,other_value                       --03/07/2017                     
                    )
              ) 
    ) rs ) s
             WHERE (  s.rec_seq = 0   -- totals record
          OR -- reduce sequenced data set to just the output page's records
             (   s.rec_seq > p_in_rowNumStart
             AND s.rec_seq < p_in_rowNumEnd )
          )
    ORDER BY s.rec_seq;

   -- query totals data
   v_stage := 'FETCH p_out_rs_getAwdsecdtlresult totals record';
   FETCH p_out_rs_getAwdsecdtlresult INTO rec_query;
   v_fetch_count := p_out_rs_getAwdsecdtlresult%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_rs_getAwdsecdtlResultTot FOR
   SELECT   rec_query.points_received_count         AS points_received_count,
            rec_query.plateau_earned_count          AS plateau_earned_count,
            rec_query.sweepstakes_won_count         AS sweepstakes_won_count,
            --rec_query.other                         AS other,
            rec_query.other_value                   AS other_value,
            rec_query.cash_received_count           AS cash_received_count
   FROM dual
   WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
    OPEN p_out_rs_getAwdsecdtlresult FOR
    SELECT CAST(NULL AS NUMBER) AS total_records,
           CAST(NULL AS NUMBER) AS rec_seq,
           CAST(NULL AS DATE)           AS award_date, 
           CAST(NULL AS VARCHAR2(2000)) AS participant_name, 
           CAST(NULL AS VARCHAR2(2000)) AS org_name, 
           CAST(NULL AS VARCHAR2(2000)) AS country,
           CAST(NULL AS VARCHAR2(2000)) AS department, 
           CAST(NULL AS VARCHAR2(2000)) AS job_position, 
           CAST(NULL AS VARCHAR2(2000)) AS promotion_name, 
           CAST(NULL AS NUMBER)         AS points_received_count, 
           CAST(NULL AS NUMBER)         AS plateau_earned_count, 
           CAST(NULL AS NUMBER)         AS sweepstakes_won_count, 
           CAST(NULL AS NUMBER)         AS on_the_spot_serial, 
           CAST(NULL AS NUMBER)         AS other, 
           CAST(NULL AS NUMBER)         AS other_value, 
           CAST(NULL AS NUMBER)         AS cash_received_count, 
           CAST(NULL AS VARCHAR2(2000)) AS level_name
    FROM dual
    WHERE 0=1;
   
   END IF;
  p_out_return_code := '00';

EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
  OPEN p_out_rs_getAwdSecDtlResultTot FOR SELECT NULL FROM DUAL;
END;
END prc_getAwardsSecondDetail;--procedure end
PROCEDURE prc_getTotPtsIss_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code     OUT NUMBER,
    p_out_result_set      OUT sys_refcursor)
IS
 /******************************************************************************
  NAME:       pkg_awards
  PURPOSE:    Get Awards Received Details
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
 ??????????           ????????         Initial code.   
 nagarajs             06/30/2016       Bug 67295 - Award Received-Participant by organization -Summary table 
                                       point received column count is mismatched with bar chart for USA org_unit  
 nagarajs              12/09/2016       G5.6.3.3 report changes '                              
Suresh J               01/23/2017       Rewrote the queries for G5.6.3.3 reports performance tuning       
Suresh J              03/09/2017        G6-1897 - Do not show org units with no data in charts and summary  
Suresh J              03/14/2017        G6-1885 - Summary is not getting displayed on selecting PAX status = Inactive  
Chidamba              12/07/2017        G6-3427 - Show org unit in chart if it has no points earned.
Chidamba              12/20/2017        G6-3428 - Populating gtt_recog_elg_count to show chart if eligible pax available in node
  ******************************************************************************/
  c_delimiter     CONSTANT VARCHAR2 (1) := '|';
  c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
  v_team_ids      VARCHAR2 (4000);
  c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getTotPtsIss_OrgBarRes';
  c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
  c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage         VARCHAR2(500);
  v_parm_list     execution_log.text_line%TYPE;  
  
  v_cash_currency_value  cash_currency_current.bpom_entered_rate%TYPE; --12/09/2016
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';
        
 BEGIN
  
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  v_stage := 'getTotPtsIss_OrgBarRes';
  BEGIN

   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list :=  '	p_in_jobPosition	:='||	p_in_jobPosition	||CHR(10)||
'	p_in_participantStatus	:='||	p_in_participantStatus	||CHR(10)||
'	p_in_localeDatePattern	:='||	p_in_localeDatePattern	||CHR(10)||
'	p_in_parentNodeId	:='||	p_in_parentNodeId	||CHR(10)||
'	p_in_fromDate	:='||	p_in_fromDate	||CHR(10)||
'	p_in_toDate	:='||	p_in_toDate	||CHR(10)||
'	p_in_onTheSpotIncluded	:='||	p_in_onTheSpotIncluded	||CHR(10)||
'	p_in_rowNumStart	:='||	p_in_rowNumStart	||CHR(10)||
'	p_in_rowNumEnd	:='||	p_in_rowNumEnd	||CHR(10)||
'	p_in_languageCode	:='||	p_in_languageCode	||CHR(10)||
'	p_in_nodeAndBelow	:='||	p_in_nodeAndBelow	||CHR(10)||
'	p_in_departments	:='||	p_in_departments	||CHR(10)||
'	p_in_promotionId	:='||	p_in_promotionId	||CHR(10)||
'	p_in_award_type	:='||	p_in_award_type	||CHR(10)||
'	p_in_countryIds	:='||	p_in_countryIds	||CHR(10)||
'	p_in_userid	:='||	p_in_userid	||CHR(10)||
'	p_in_sortColName	:='||	p_in_sortColName	||CHR(10)||
'	p_in_sortedBy	:='||	p_in_sortedBy	
	;

    -- prc_execution_log_entry(c_process_name, c_release_level, 'INFO', v_stage || v_parm_list , NULL);  

      v_stage := 'Get currency value';
      BEGIN    --12/09/2016
       SELECT ccc.bpom_entered_rate
         INTO v_cash_currency_value
         FROM user_address ua,
              country c,
              cash_currency_current ccc 
        WHERE ua.user_id = p_in_userid
          AND is_primary = 1
          AND ua.country_id = c.country_id
          AND c.country_code = ccc.to_cur;
      EXCEPTION
        WHEN OTHERS THEN
          v_cash_currency_value := 1;
      END;  
      
      
  /************************* 12/20/2017  START *******************************/
  
  DELETE gtt_recog_elg_count; --08/05/2014 Start
    
    IF fnc_check_promo_aud('receiver',NULL,p_in_promotionId) = 1  
       OR 
       --p_in_onTheSpotIncluded ='Y' --04/12/2017
       ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
         
     THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt, node_id, 'nodesum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_node pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_c   --country
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND pe.node_id in (SELECT node_id
                              FROM rpt_hierarchy
                              WHERE parent_node_id IN                                                              
                                    (SELECT g.id FROM gtt_id_list g                                         
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                              ) 
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
                      AND gil_c.ref_text_1 = gc_ref_text_country_id
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                             --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                             OR gil_c.id = pe.country_id )  --03/09/2017
            GROUP BY pe.node_id)
    UNION ALL
    SELECT eligible_cnt, node_id, 'teamsum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_team pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_pn  -- parent node
                     , gtt_id_list gil_c   --country                 
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                              OR gil_pn.id = pe.node_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
                      AND gil_c.ref_text_1 = gc_ref_text_country_id
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                             --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                             OR gil_c.id = pe.country_id )  --03/09/2017
            GROUP BY pe.node_id);
        
    ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
      INSERT INTO gtt_recog_elg_count
      SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_node pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion id
             , gtt_id_list gil_c   --country             
       WHERE     pe.giver_recvr_type = 'receiver'
              AND pe.node_id in (SELECT node_id
                      FROM rpt_hierarchy
                      WHERE parent_node_id IN                                                              
                            (SELECT g.id FROM gtt_id_list g                                         
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                      ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                     --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                      OR gil_c.id = pe.country_id )  --03/09/2017
      GROUP BY pe.node_id)
      UNION ALL
      SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_team pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_pn  -- node       
             , gtt_id_list gil_p  -- promotion id
             , gtt_id_list gil_c  --country                           
       WHERE     pe.giver_recvr_type = 'receiver'
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
              AND (  gil_pn.ref_text_2 = gc_search_all_values
                      OR gil_pn.id = pe.node_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type ) 
              AND gil_c.ref_text_1 = gc_ref_text_country_id
               AND (  gil_c.ref_text_2 = gc_search_all_values
                      --OR gil_c.ref_text_2 = pe.country_id )  --03/09/2017
                      OR gil_c.id = pe.country_id )  --03/09/2017v 
      GROUP BY pe.node_id);


  ELSE 
        INSERT INTO gtt_recog_elg_count
        SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
        SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                               FROM   
                                             (                                     
                                             SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                                  eligible_cnt,  
                                                               ppe.node_id  
                                                          FROM (SELECT *  
                                                                  FROM rpt_pax_promo_eligibility  
                                                                 WHERE giver_recvr_type = 'receiver' ) ppe,  
                                                               promotion P,                                                        
                                                               rpt_participant_employer rad
                                                               , user_address ua  --03/09/2017
                                                                 , country c        --03/09/2017
                                                                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                                 , gtt_id_list gil_pt  -- position type
                                                                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                                                                 , gtt_id_list gil_c   -- country --03/09/2017   
                                                                 
                                                         WHERE ppe.promotion_id = P.promotion_id  
                                                                AND ppe.participant_id = rad.user_id(+)
                                                                AND ppe.participant_id = ua.user_id (+) --03/09/2017
                                                                AND ua.country_id = c.country_id (+)   --03/09/2017
                                                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                                                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                          OR gil_p.id = p.promotion_id)
                                                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                      OR gil_dt.ref_text_2 = rad.department_type ) 
                                                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                     OR gil_pt.ref_text_2 = rad.position_type )
                                                                AND gil_c.ref_text_1 = gc_ref_text_country_id   --03/09/2017
                                                                AND (  gil_c.ref_text_2 = gc_search_all_values   --03/09/2017
                                                                     OR gil_c.id = c.country_id ) 
                                                                --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                      NVL (p_in_participantStatus,  
                                                                           DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                   
                                                              GROUP BY ppe.node_id                                     
                                             ) elg, 
                                             ( SELECT child_node_id node_id,node_id path_node_id 
                                                FROM rpt_hierarchy_rollup
                                                     ,gtt_id_list gil_pn  --parent node                                          
                                                WHERE  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = node_id )
                                                ) npn 
                                             WHERE elg.node_id(+) = npn.node_id 
                                             GROUP BY npn.path_node_id)
        UNION ALL                                        
        SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
        SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                              eligible_cnt,  
                                                           ppe.node_id  
                                                      FROM (SELECT *  
                                                              FROM rpt_pax_promo_eligibility
                                                                   ,gtt_id_list gil_pn  --parent node
                                                             WHERE giver_recvr_type = 'receiver'  
                                                                      AND  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                              OR gil_pn.id = node_id ) ) ppe,  
                                                           promotion P,                                                        
                                                           rpt_participant_employer rad
                                                           , user_address ua  --03/09/2017
                                                                 , country c        --03/09/2017
                                                             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                             , gtt_id_list gil_pt  -- position type
                                                             , gtt_id_list gil_pn  -- node       
                                                             , gtt_id_list gil_p  -- promotion id      
                                                             , gtt_id_list gil_c  -- country                     
                                                     WHERE ppe.promotion_id = P.promotion_id  
                                                          AND ppe.participant_id = rad.user_id(+)
                                                          AND ppe.participant_id = ua.user_id (+) --03/09/2017
                                                          AND ua.country_id = c.country_id (+)   --03/09/2017
                                                          AND   gil_p.ref_text_1 = gc_ref_text_promotion_id
                                                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                  OR gil_p.id = ppe.promotion_id ) 
                                                          AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = ppe.node_id )
                                                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                  OR gil_dt.ref_text_2 = rad.department_type ) 
                                                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                 OR gil_pt.ref_text_2 = rad.position_type )
                                                          AND gil_c.ref_text_1 = gc_ref_text_country_id   --03/09/2017
                                                          AND (  gil_c.ref_text_2 = gc_search_all_values   --03/09/2017
                                                                     OR gil_c.id = c.country_id ) 
                                                          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                          AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                  NVL (p_in_participantStatus,  
                                                                       DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                                          
                                                  GROUP BY ppe.node_id);     
                                             
    END IF;
    /************************ 12/20/2017 END ****************************/ 
    
    OPEN p_out_result_set FOR  --05/25/2016
    SELECT org_name, earned_count FROM
    (SELECT org_name,
      NVL(earned_count,0) earned_count,
      org_id --12/07/2017 G6-3158    
    FROM
      (SELECT dtl.node_name    AS ORG_NAME, 
              ras.detail_node_id org_id, --12/07/2017 G6-3158
        CASE WHEN p_in_award_type = 'points' THEN POINTS_RECEIVED_COUNT 
             WHEN p_in_award_type = 'cash' THEN CASH_RECEIVED_COUNT * v_cash_currency_value --12/09/2016
             WHEN p_in_award_type = 'other' THEN OTHER_RECEIVED_COUNT    
             WHEN p_in_award_type = 'merchandise' THEN PLATEAU_EARNED_COUNT --10/18/2016
        END earned_count             
      FROM
        (SELECT DISTINCT ras.detail_node_id,
          ras.header_node_id,
          ras.record_type
        FROM rpt_awardmedia_summary ras,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end          
        WHERE p.promotion_id = ras.promotion_id
                  AND ras.record_type LIKE '%node%'
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = ras.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = ras.job_position )
                 --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                 AND NVL(pax_status,'x')   = NVL(p_in_participantStatus, NVL(pax_status,'x'))     --03/14/2017
                 AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            GROUP BY detail_node_id,
              header_node_id,
              record_type
        UNION
        SELECT rh.node_id detail_node_id,
          rh.parent_node_id header_node_id,
          'nodesum' AS record_type
        FROM rpt_hierarchy rh
        WHERE parent_node_id IN       
               (SELECT g.id 
                           FROM gtt_id_list g                                         
                           WHERE g.ref_text_1 = gc_ref_text_parent_node_id )       
          ) ras,
        (SELECT h.node_id ,
          h.node_name,
          SUM(DECODE(award_payout_type ,'cash',media_amount,0))AS CASH_RECEIVED_COUNT      ,
          SUM(DECODE(award_payout_type ,'points',media_amount,0))AS POINTS_RECEIVED_COUNT,
          SUM(DECODE(award_payout_type ,'other',other,0))AS OTHER_RECEIVED_COUNT,--10/18/2016
          NVL(SUM(plateau_earned),0) PLATEAU_EARNED_COUNT     
        FROM
          (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup
          )npn,
          rpt_hierarchy h,
          (SELECT ra2.node_id,
            ra2.award_payout_type,
            SUM(plateau_earned) plateau_earned,
            SUM(media_amount) media_amount,
            SUM(other) other
          FROM
            (SELECT rad.node_id,
              award_payout_type,
              plateau_earned plateau_earned,
              media_amount media_amount,
              1 AS Other     --10/18/2016            
            FROM RPT_AWARDMEDIA_DETAIL rad,
              promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end
             , gtt_id_list gil_u  --user             
             , gtt_id_list gil_c  --country                                              
            WHERE rad.promotion_id   = p.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type )        
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                    AND NVL(rad.participant_current_status,'x') = NVL(p_in_participantStatus, NVL(rad.participant_current_status,'x'))  --03/14/2017
                    AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                     AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --06/30/2016
                     AND p.promotion_type <> 'self_serv_incentives'  --06/30/2016
            UNION ALL
            SELECT  rad.node_id,
              'points' award_payout_type,
              0 plateau_earned,
               NVL (rad.transaction_amount, 0) media_amount,
               0 AS other
            FROM rpt_qcard_detail rad,
              participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_c  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user     
           WHERE rad.user_id       = p.user_id
              AND gil_c.ref_text_1 = gc_ref_text_country_id --01/13/2017 start
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
              AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
            ----AND NVL(p_in_promotionStatus,'showall') in ('showall','live')  --12/09/2016      --12/09/2016   
            AND NVL(p.status,'x')          = NVL (p_in_participantStatus, NVL(p.status,'x'))     --03/14/2017   
            AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            UNION ALL
            SELECT  rad.node_id,
              payout_type,
              0 plateau_earned,
              payout_amount,
              CASE WHEN payout_type= 'other' THEN other ELSE 0 END AS other   --10/18/2016 --12/01/2017
            FROM rpt_ssi_award_detail rad,
              participant p
             ,promotion promo
             --, badge_promotion bp                       --03/14/2017 --12/01/2017
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end  
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country                                                                      
            WHERE rad.user_id       = p.user_id
            AND rad.promotion_id      = promo.promotion_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = promo.promotion_id 
                          --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id))    --03/14/2014 --12/01/2017
                           OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                    AND NVL(p.status,'x')          = NVL (p_in_participantStatus, NVL(p.status,'x'))
                    AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                    ) ra2
          GROUP BY ra2.node_id,
                   ra2.award_payout_type
          ) dtl
        WHERE dtl.node_id(+) = npn.node_id
        AND npn.path_node_id = h.node_id
        GROUP BY h.node_id ,
          h.node_name
        ) dtl
      WHERE ras.detail_node_id       = dtl.node_id
      AND NVL(ras.header_node_id,0) IN     
          ( SELECT g.id 
               FROM gtt_id_list g                                         
               WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
      AND ras.record_type LIKE '%node%'
      AND p_in_nodeAndBelow = 1
      UNION
      SELECT rh.node_name
        ||' Team'              AS org_name, 
        rh.node_id,--12/07/2017 G6-3158
        CASE WHEN p_in_award_type = 'points' THEN POINTS_RECEIVED_COUNT 
             WHEN p_in_award_type = 'cash' THEN CASH_RECEIVED_COUNT 
             WHEN p_in_award_type = 'other' THEN OTHER_RECEIVED_COUNT    
             WHEN p_in_award_type = 'merchandise' THEN PLATEAU_EARNED_COUNT --10/18/2016
        END earned_count 
      FROM rpt_hierarchy rh,
        (SELECT ra2.node_id,
          SUM(DECODE(award_payout_type ,'cash',media_amount,0))AS CASH_RECEIVED_COUNT      ,
          SUM(DECODE(award_payout_type ,'points',media_amount,0))AS POINTS_RECEIVED_COUNT,
          SUM(DECODE(award_payout_type ,'other',other,0))AS OTHER_RECEIVED_COUNT,
          NVL(SUM(plateau_earned),0) PLATEAU_EARNED_COUNT   
        FROM
          (SELECT rad.node_id,
              award_payout_type,
              plateau_earned plateau_earned,
              media_amount media_amount,
              1 AS Other  --10/18/2016        
          FROM RPT_AWARDMEDIA_DETAIL rad,
                promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end       
             , gtt_id_list gil_u  --user       
             , gtt_id_list gil_c  -- country                                             
          WHERE rad.promotion_id   = p.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type )             
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                    --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                  --AND p.promotion_status  = NVL(p_in_promotionStatus,p.promotion_status)  --12/09/2016
                  AND NVL(rad.participant_current_status,'x') = NVL(p_in_participantStatus, NVL(rad.participant_current_status,'x'))   --03/14/2017
                  AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --06/30/2016
                  AND p.promotion_type <> 'self_serv_incentives'  --06/30/2016
        UNION ALL
          SELECT node_id,
               'points' award_payout_type,
              0 plateau_earned,
               NVL (rad.transaction_amount, 0) media_amount,
               0 AS other          
          FROM rpt_qcard_detail rad,
            participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_u  --user                                                           
         , gtt_id_list gil_c  -- country           
          WHERE rad.user_id       = p.user_id
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
              OR gil_c.id = rad.country_id )
         --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
          AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
         -- --AND NVL(p_in_promotionStatus,'showall') in ('showall','live')  --12/09/2016  --12/09/2016         
          AND NVL(p.status,'x')          = NVL (p_in_participantStatus, NVL(p.status,'x'))   --03/14/2017
          AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        UNION ALL
        SELECT rad.node_id,
              payout_type,
              0 plateau_earned,
              payout_amount,
              CASE WHEN payout_type= 'other' THEN 1 ELSE 0 END other    --10/18/2016          
          FROM rpt_ssi_award_detail rad,
            participant p
          ,promotion promo
          --,badge_promotion bp  --12/01/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user     
         , gtt_id_list gil_c  -- country                
          WHERE rad.user_id       = p.user_id          
              AND rad.promotion_id      = promo.promotion_id
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = promo.promotion_id 
                          --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                          OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
                AND gil_c.ref_text_1 = gc_ref_text_country_id 
                AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
              AND NVL(p.status,'x')          = NVL (p_in_participantStatus, NVL(p.status,'x'))   --03/14/2017
              AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
              ) ra2
        GROUP BY ra2.node_id
        ) dtl
      WHERE rh.node_id       = dtl.node_id (+)
      AND rh.is_deleted      = 0
      AND NVL(rh.node_id,0) IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
      )ls
    ORDER BY earned_count DESC
    ) WHERE --EARNED_COUNT <> 0 AND  --12/07/2017 G6-3158
     (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = org_id) > 0 AND --03/09/2017  --12/07/2017 G6-3158
            ROWNUM    <= 20 ;
    p_out_return_code                               := '00';
    
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage ||v_parm_list|| SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
PROCEDURE prc_getOthAwdIss_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
/*******************************************************************************
   Purpose: 

   Person         Date         Comments
   -----------   ----------   -----------------------------------------------------
  ????             ?????      Creation
  nagarajs       12/09/2016  G5.6.3.3 report changes
  Suresh J       01/23/2017  Rewrote the queries for G5.6.3.3 reports performance tuning  
*******************************************************************************/
IS
  c_delimiter     CONSTANT VARCHAR2 (1) := '|';
  c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
  v_team_ids      VARCHAR2 (4000);
  c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getOthAwdIss_OrgBarRes';
  c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
  c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
  v_stage         VARCHAR2(500);
    
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';

BEGIN
  
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  v_stage := 'prc_getOthAwdIss_OrgBarRes';
  BEGIN
    OPEN p_out_result_set FOR SELECT org_name, plateau_earned, sweepstakes_won, on_the_spot,other FROM
    (SELECT org_name,
      NVL(SUM(plateau_earned),0) plateau_earned,
      NVL(SUM(sweepstakes_won),0) sweepstakes_won,
      NVL(SUM(on_the_spot),0) on_the_spot,
      NVL(SUM(other),0) other      
    FROM
      (SELECT dtl.node_name    AS ORG_NAME,
        NVL(plateau_earned,0)  AS plateau_earned,
        NVL(sweepstakes_won,0) AS sweepstakes_won,
        NVL(OnTheSpot,0) on_the_spot,
        NVL(Other,0) Other      --05/12/2015        
      FROM
        (SELECT DISTINCT ras.detail_node_id,
          ras.header_node_id,
          ras.record_type
        FROM rpt_awardmedia_summary ras,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
        WHERE p.promotion_id = ras.promotion_id
              AND ras.record_type LIKE '%node%'
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = ras.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = ras.job_position ) 
              --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                AND NVL(pax_status,'x')   = NVL(p_in_participantStatus, NVL(pax_status,'x'))
                AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                GROUP BY detail_node_id,
                  header_node_id,
                  record_type
                UNION
                SELECT rh.node_id detail_node_id,
                  rh.parent_node_id header_node_id,
                  'nodesum' AS record_type
                FROM rpt_hierarchy rh
                WHERE parent_node_id 
                             IN   (SELECT g.id 
                                   FROM gtt_id_list g                                         
                                   WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
        ) ras,
        (SELECT h.node_id ,
          h.node_name,
          SUM(plateau_earned) plateau_earned,
          SUM(sweepstakes_won) sweepstakes_won,
          SUM(OnTheSpot) OnTheSpot,
          SUM(Other) Other      --05/12/2015          
        FROM
          (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup
          )npn,
          rpt_hierarchy h,
          (SELECT ra2.node_id,
            SUM(plateau_earned) plateau_earned,
            SUM(sweepstakes_won) sweepstakes_won,
            SUM(OnTheSpot) OnTheSpot,
            SUM(Other) Other      --05/12/2015            
          FROM
            (SELECT rad.user_id ,
              rad.node_id,
              plateau_earned plateau_earned,
              sweepstakes_won sweepstakes_won,
              0 AS OnTheSpot,
              0 AS Other      --05/12/2015              
            FROM RPT_AWARDMEDIA_DETAIL rad,
              promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
            WHERE rad.promotion_id   = p.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                  AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                  AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            UNION ALL
            SELECT rad.user_id,
              rad.node_id,
              0 plateau_earned,
              0 sweepstakes_won,
              1 AS OnTheSpot,
              0 AS Other      --05/12/2015              
            FROM rpt_qcard_detail rad,
              participant p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
            WHERE rad.user_id       = p.user_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                    AND gil_c.ref_text_1 = gc_ref_text_country_id 
                    AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                    --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                    AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                    AND p.status          = NVL (p_in_participantStatus, p.status)
                    AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            UNION ALL
            SELECT rad.user_id,
              rad.node_id,
              0 plateau_earned,
              0 sweepstakes_won,
              0 AS OnTheSpot,
              rad.other AS Other    --07/22/2015
            FROM rpt_ssi_award_detail rad,
              participant p
             ,promotion promo
             --, badge_promotion bp --12/01/2017
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
            WHERE rad.user_id       = p.user_id
                    AND rad.payout_type = 'other'
                    AND rad.promotion_id      = promo.promotion_id
                    AND gil_dt.ref_text_1 = gc_ref_text_department_type
                    AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                          OR gil_pt.ref_text_2 = rad.position_type ) 
                    AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                    AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                              --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                              OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                     AND gil_u.ref_text_1 = gc_ref_text_user_id 
                     AND (  gil_u.ref_text_2 = gc_search_all_values
                              OR gil_u.id = rad.user_id )
                     AND gil_c.ref_text_1 = gc_ref_text_country_id 
                     AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                     --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                     AND p.status          = NVL (p_in_participantStatus, p.status)
                     AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            UNION ALL --05/05/2016
            SELECT rad.user_id,
              rad.node_id,
              0 plateau_earned,
              0 sweepstakes_won,
              0 AS OnTheSpot,
              rad.media_amount AS Other    --07/22/2015
            FROM RPT_AWARDMEDIA_DETAIL rad,
             promotion promo
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
            WHERE       rad.promotion_id      = promo.promotion_id
                    AND gil_dt.ref_text_1 = gc_ref_text_department_type
                    AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                          OR gil_pt.ref_text_2 = rad.position_type ) 
                    AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                    AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id )
                     AND gil_u.ref_text_1 = gc_ref_text_user_id 
                     AND (  gil_u.ref_text_2 = gc_search_all_values
                              OR gil_u.id = rad.user_id )
                     AND gil_c.ref_text_1 = gc_ref_text_country_id 
                     AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                     --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                     AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
                     AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            ) ra2
          GROUP BY ra2.node_id
          ) dtl
        WHERE dtl.node_id(+) = npn.node_id
        AND npn.path_node_id = h.node_id
        GROUP BY h.node_id ,
          h.node_name
        ) dtl
      WHERE ras.detail_node_id       = dtl.node_id
      AND NVL(ras.header_node_id,0) IN  
                               (SELECT g.id 
                                       FROM gtt_id_list g                                         
                                       WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
      AND ras.record_type LIKE '%node%'
      AND p_in_nodeAndBelow = 1
      UNION
      SELECT rh.node_name
        ||' Team'              AS org_name,
        NVL(plateau_earned,0)  AS plateau_earned,
        NVL(sweepstakes_won,0) AS sweepstakes_won,
        OnTheSpot on_the_spot,
        Other
      FROM rpt_hierarchy rh,
        (SELECT ra2.node_id,
          SUM(plateau_earned) plateau_earned,
          SUM(sweepstakes_won) sweepstakes_won,
          SUM(OnTheSpot) OnTheSpot,
          SUM(Other) Other
        FROM
          (SELECT --rad.user_id ,   --05/05/2016
              CASE 
                WHEN rad.award_payout_type <> 'cash'   --05/05/2016
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,         
            rad.node_id,
            plateau_earned plateau_earned,
            sweepstakes_won sweepstakes_won,
            0 AS OnTheSpot,
            0 AS Other      --05/12/2015      
          FROM RPT_AWARDMEDIA_DETAIL rad,
            promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE     p_in_award_type = 'points' 
                    AND plateau_earned = 0
                    AND rad.promotion_id   = p.promotion_id
                    AND gil_dt.ref_text_1 = gc_ref_text_department_type
                    AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                          OR gil_pt.ref_text_2 = rad.position_type ) 
                    AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                    AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = p.promotion_id )
                     AND gil_u.ref_text_1 = gc_ref_text_user_id 
                     AND (  gil_u.ref_text_2 = gc_search_all_values
                              OR gil_u.id = rad.user_id )
                     AND gil_c.ref_text_1 = gc_ref_text_country_id 
                     AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                     --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                     AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                     AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        UNION ALL
        SELECT --rad.user_id ,   --05/05/2016
              CASE 
                WHEN rad.award_payout_type = 'cash'   --05/05/2016
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,         
            rad.node_id,
            plateau_earned plateau_earned,
            sweepstakes_won sweepstakes_won,
            0 AS OnTheSpot,
            0 AS Other      --05/12/2015      
          FROM RPT_AWARDMEDIA_DETAIL rad,
                promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'cash' 
                AND rad.promotion_id   = p.promotion_id
                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                AND (  gil_pt.ref_text_2 = gc_search_all_values
                      OR gil_pt.ref_text_2 = rad.position_type ) 
                AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                 AND gil_u.ref_text_1 = gc_ref_text_user_id 
                 AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                 AND gil_c.ref_text_1 = gc_ref_text_country_id 
                 AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                 --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                  AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                  AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        UNION ALL
        SELECT --rad.user_id ,   --05/05/2016
              CASE 
                WHEN rad.award_payout_type <> 'cash'   --05/05/2016
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,         
            rad.node_id,
            plateau_earned plateau_earned,
            sweepstakes_won sweepstakes_won,
            0 AS OnTheSpot,
            0 AS Other      --05/12/2015      
          FROM RPT_AWARDMEDIA_DETAIL rad,
            promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
           WHERE p_in_award_type = 'plateau' 
                  AND plateau_earned > 0
                  AND rad.promotion_id   = p.promotion_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                          OR gil_pt.ref_text_2 = rad.position_type ) 
                    AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                    AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = p.promotion_id )
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                           OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                  AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                  AND NVL(TRUNC(media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          UNION ALL
          SELECT rad.user_id,
            rad.node_id,
            0 plateau_earned,
            0 sweepstakes_won,
            1 AS OnTheSpot,
            0 AS Other      --05/12/2015            
          FROM rpt_qcard_detail rad,
               participant p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'points' 
                  AND rad.user_id       = p.user_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                  AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                  AND p.status          = NVL (p_in_participantStatus, p.status)
                  AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
        UNION ALL
        SELECT --rad.user_id,  --05/05/2016
          CASE WHEN rad.payout_type <> 'other' 
               THEN rad.user_id 
               ELSE NULL 
          END AS user_id,
            rad.node_id,
            0 plateau_earned,
            0 sweepstakes_won,
            0 AS OnTheSpot,
            rad.other AS Other      --05/12/2015            
          FROM rpt_ssi_award_detail rad,
            participant p
          ,promotion promo     
          --,badge_promotion bp   --12/01/2017     
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
          , gtt_id_list gil_u  --user                                                           
          , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'points' 
              AND rad.user_id       = p.user_id
              AND rad.promotion_id      = promo.promotion_id
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
              AND p.status          = NVL (p_in_participantStatus, p.status)
              AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          UNION ALL
        SELECT --rad.user_id,  --05/05/2016
          CASE WHEN rad.payout_type = 'other' 
               THEN rad.user_id 
               ELSE NULL 
          END AS user_id,
            rad.node_id,
            0 plateau_earned,
            0 sweepstakes_won,
            0 AS OnTheSpot,
            rad.other AS Other      --05/12/2015            
          FROM rpt_ssi_award_detail rad,
            participant p
          ,promotion promo    
         -- ,badge_promotion bp  --12/01/2017      
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
          , gtt_id_list gil_u  --user                                                           
          , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'other'
              AND rad.user_id       = p.user_id          
              AND rad.promotion_id      = promo.promotion_id
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
            --AND promo.promotion_status   = NVL (p_in_promotionStatus, promo.promotion_status)  --12/09/2016
              AND p.status          = NVL (p_in_participantStatus, p.status)
              AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
       UNION ALL  --05/05/2016
        SELECT rad.user_id,  --05/05/2016
            rad.node_id,
            0 plateau_earned,
            0 sweepstakes_won,
            0 AS OnTheSpot,
            rad.media_amount AS Other      --05/12/2015            
          FROM RPT_AWARDMEDIA_DETAIL rad,
               promotion promo 
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'other'
              AND rad.award_payout_type = 'other'
              AND rad.promotion_id      = promo.promotion_id
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
              --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
              AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
              AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
--05/12/2015
          ) ra2
        GROUP BY ra2.node_id
        ) dtl
      WHERE rh.node_id       = dtl.node_id (+)
      AND rh.is_deleted      = 0
      AND NVL(rh.node_id,0) IN
            (SELECT g.id 
               FROM gtt_id_list g                                         
               WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
      )ls
    GROUP BY ls.org_name
    ORDER BY (plateau_earned + sweepstakes_won + on_the_spot ) DESC
    ) WHERE ROWNUM    <= 20 ;
    p_out_return_code := '00';
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
PROCEDURE prc_getRecvNotRcvAwd_PieRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
IS
/******************************************************************************
  NAME:       prc_getRecvNotRcvAwd_PieRes
   Author                  Date          Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula           07/02/2014        Initial..(the queries were in Java before the release 5.4. So no comments available so far)  
  Swati                    10/15/2014        Bug 57411 - In Reports column 'Eligible Participants' count should display based on the 
                                             Participant Status = Inactive/active selected in the Change Filter window
  Swati                    10/31/2014        Bug 57676 - Reports-Recognition Given/Received - Results are not correct when Participant Status = 'Show all'.                                        
  nagarajs                 11/13/2015        Bug 64406 - Issue in the chart 'Overall Received vs Not Received Awards' when user enters into the team details
  nagarajs                 12/09/2016        G5.6.3.3 report changes
  Suresh J                 01/23/2017        Rewrote the queries for G5.6.3.3 reports performance tuning  
******************************************************************************/
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getRecvNotRcvAwd_PieRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);

    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';     

BEGIN
  
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),  gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  DELETE gtt_recog_elg_count; --08/05/2014 Start
    
    IF fnc_check_promo_aud('receiver',NULL,p_in_promotionId) = 1  
        OR 
        --p_in_onTheSpotIncluded ='Y'  --04/12/2017
         ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
     THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt, node_id, 'nodesum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_node pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND pe.node_id in (SELECT node_id
                              FROM rpt_hierarchy
                              WHERE parent_node_id IN                                                              
                                    (SELECT g.id FROM gtt_id_list g                                         
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                              ) 
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
            GROUP BY pe.node_id)
    UNION ALL
    SELECT eligible_cnt, node_id, 'teamsum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_team pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_pn  -- parent node                     
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                              OR gil_pn.id = pe.node_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type ) 
            GROUP BY pe.node_id);

        
    ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
      INSERT INTO gtt_recog_elg_count
      SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_node pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion id
       WHERE     pe.giver_recvr_type = 'receiver'
              AND pe.node_id in (SELECT node_id
                      FROM rpt_hierarchy
                      WHERE parent_node_id IN                                                              
                            (SELECT g.id FROM gtt_id_list g                                         
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                      ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type ) 
      GROUP BY pe.node_id)
      UNION ALL
      SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_team pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_pn  -- node       
             , gtt_id_list gil_p  -- promotion id                           
       WHERE     pe.giver_recvr_type = 'receiver'
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
              AND (  gil_pn.ref_text_2 = gc_search_all_values
                      OR gil_pn.id = pe.node_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type ) 
      GROUP BY pe.node_id);


  ELSE 
        INSERT INTO gtt_recog_elg_count
        SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
        SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                               FROM   
                                             (                                     
                                             SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                                  eligible_cnt,  
                                                               ppe.node_id  
                                                          FROM (SELECT *  
                                                                  FROM rpt_pax_promo_eligibility  
                                                                 WHERE giver_recvr_type = 'receiver' ) ppe,  
                                                               promotion P,                                                        
                                                               rpt_participant_employer rad
                                                                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                                 , gtt_id_list gil_pt  -- position type
                                                                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                                                         WHERE ppe.promotion_id = P.promotion_id  
                                                                AND ppe.participant_id = rad.user_id(+)
                                                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                                                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                          OR gil_p.id = p.promotion_id)
                                                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                      OR gil_dt.ref_text_2 = rad.department_type ) 
                                                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                     OR gil_pt.ref_text_2 = rad.position_type ) 
                                                                --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                      NVL (p_in_participantStatus,  
                                                                           DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                   
                                                              GROUP BY ppe.node_id                                     
                                             ) elg, 
                                             ( SELECT child_node_id node_id,node_id path_node_id 
                                                FROM rpt_hierarchy_rollup
                                                     ,gtt_id_list gil_pn  --parent node                                          
                                                WHERE  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = node_id )
                                                ) npn 
                                             WHERE elg.node_id(+) = npn.node_id 
                                             GROUP BY npn.path_node_id)
        UNION ALL                                        
        SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
        SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                              eligible_cnt,  
                                                           ppe.node_id  
                                                      FROM (SELECT *  
                                                              FROM rpt_pax_promo_eligibility
                                                                   ,gtt_id_list gil_pn  --parent node
                                                             WHERE giver_recvr_type = 'receiver'  
                                                                      AND  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                              OR gil_pn.id = node_id ) ) ppe,  
                                                           promotion P,                                                        
                                                           rpt_participant_employer rad
                                                             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                             , gtt_id_list gil_pt  -- position type
                                                             , gtt_id_list gil_pn  -- node       
                                                             , gtt_id_list gil_p  -- promotion id                           
                                                     WHERE ppe.promotion_id = P.promotion_id  
                                                          AND ppe.participant_id = rad.user_id(+)
                                                          AND   gil_p.ref_text_1 = gc_ref_text_promotion_id
                                                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                  OR gil_p.id = ppe.promotion_id ) 
                                                          AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = ppe.node_id )
                                                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                  OR gil_dt.ref_text_2 = rad.department_type ) 
                                                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                                                          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                          AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                  NVL (p_in_participantStatus,  
                                                                       DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                                          
                                                  GROUP BY ppe.node_id);     
                                             
    END IF;    --08/05/2014 End

  v_stage := 'prc_getRecvNotRcvAwd_PieRes';
  BEGIN
    OPEN p_out_result_set FOR 
    SELECT ROUND(DECODE(pax_elig,0,0,(((pax_rec /pax_elig) * 100))),2)
      AS
    RECEIVED_AWARD_PCT, ROUND(DECODE(pax_elig,0,100,(((pax_elig - pax_rec)/pax_elig)*100)),2)
  AS
    HAVE_NOTRECEIVED_AWARD_PCT FROM
    (SELECT pax_rec,
      ( SELECT SUM(eligible_count) FROM gtt_recog_elg_count  
      WHERE ( ( p_in_nodeAndBelow = 1         --11/13/2015
        AND node_id IN
          (SELECT g.id 
               FROM gtt_id_list g                                         
               WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))                            
      OR ( p_in_nodeAndBelow = 0        
      AND node_id       IN (SELECT g.id 
                           FROM gtt_id_list g                                         
                           WHERE g.ref_text_1 = gc_ref_text_parent_node_id ) ) )    
      ) pax_elig
    FROM
      (SELECT COUNT(user_id) pax_rec
      FROM
        (SELECT --rad.user_id      --05/05/2016
          CASE 
            WHEN rad.award_payout_type <> 'cash' 
                THEN user_id 
            ELSE NULL    
          END AS user_id   --05/05/2016
        FROM RPT_AWARDMEDIA_DETAIL rad,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user                                                           
        WHERE p_in_award_type = 'points'
            AND plateau_earned = 0 
            AND rad.promotion_id   = p.promotion_id
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
            AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
            AND gil_u.ref_text_1 = gc_ref_text_user_id 
            AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
            --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
            AND ( ( p_in_nodeAndBelow = 1         --11/13/2015
                    AND rad.node_id IN
                      (SELECT child_node_id
                      FROM rpt_hierarchy_rollup
                      WHERE node_id IN
                        (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )))                            --11/13/2015
                  OR ( p_in_nodeAndBelow = 0 AND rad.node_id       IN          
                        (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))
                 )
            AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
            AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                
    --Start
    UNION
    SELECT  CASE 
                WHEN rad.award_payout_type = 'cash' 
                    THEN user_id 
                ELSE NULL    
            END AS user_id   --05/05/2016
            FROM RPT_AWARDMEDIA_DETAIL rad,
                  promotion p
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                 , gtt_id_list gil_u  --user                                                           
            WHERE p_in_award_type = 'cash' 
                AND rad.promotion_id   = p.promotion_id
                AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
                AND gil_u.ref_text_1 = gc_ref_text_user_id 
                AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
                --AND p.promotion_status = 'live'  --12/09/2016 --commented out on 04/20/2017
                AND ( ( p_in_nodeAndBelow = 1         --11/13/2015
                        AND rad.node_id IN
                          (SELECT child_node_id
                          FROM rpt_hierarchy_rollup
                          WHERE node_id IN
                            (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )))                            --11/13/2015
                      OR ( p_in_nodeAndBelow = 0 AND rad.node_id       IN          
                            (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))
                     )
                AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                
        --End
        UNION
        SELECT rad.user_id
        FROM rpt_qcard_detail rad,
          participant p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_u  --user                                                           
         , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'points' 
                AND rad.user_id       = p.user_id
                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
                AND gil_u.ref_text_1 = gc_ref_text_user_id 
                AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                AND gil_c.ref_text_1 = gc_ref_text_country_id 
                AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
                AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                --AND 'Y'                 = p_in_onTheSpotIncluded    --04/12/2017
                AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                ----AND NVL(p_in_promotionStatus,'showall') in ('showall','live')  --12/09/2016   --09/25/2014         --12/09/2016 
                AND p.status     = NVL (p_in_participantStatus, p.status)
                AND ( ( p_in_nodeAndBelow = 1         
                AND rad.node_id IN
                  (SELECT child_node_id
                  FROM rpt_hierarchy_rollup
                  WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
                  ))                            
              OR ( p_in_nodeAndBelow = 0        
                   AND rad.node_id  IN  
                   (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )      
                 ))
        UNION
        SELECT --rad.user_id    --05/05/2016
                CASE WHEN rad.payout_type <> 'other' 
                    THEN rad.user_id 
                ELSE NULL 
                END AS user_id        
        FROM rpt_ssi_award_detail rad,
              participant p
             , promotion promo
             --, badge_promotion bp  --12/01/2017
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'points' 
                AND rad.user_id       = p.user_id
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) -12/01/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND rad.promotion_id      = promo.promotion_id
          --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
          AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND p.status     = NVL (p_in_participantStatus, p.status)
            AND ( ( p_in_nodeAndBelow = 1         --11/13/2015
            AND rad.node_id IN
              (SELECT child_node_id
              FROM rpt_hierarchy_rollup
              WHERE node_id IN
                (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
              ))                            --11/13/2015
          OR ( p_in_nodeAndBelow = 0        --11/13/2015
          AND rad.node_id       IN          --11/13/2015
            (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )  ) )
    --05/12/2015
    --Start
        UNION
        SELECT --rad.user_id    --05/05/2016
                CASE WHEN rad.payout_type = 'other' 
                    THEN rad.user_id 
                ELSE NULL 
                END AS user_id        
        FROM rpt_ssi_award_detail rad,
          participant p
          ,promotion promo          
            -- , badge_promotion bp  --12/01/2017
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'other' 
                AND rad.user_id       = p.user_id
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                     -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                     OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND rad.promotion_id      = promo.promotion_id
          --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
          AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND p.status     = NVL (p_in_participantStatus, p.status)
            AND ( ( p_in_nodeAndBelow = 1         --11/13/2015
            AND rad.node_id IN
              (SELECT child_node_id
              FROM rpt_hierarchy_rollup
              WHERE node_id IN
                (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
              ))                            --11/13/2015
          OR ( p_in_nodeAndBelow = 0        --11/13/2015
          AND rad.node_id       IN          --11/13/2015
            (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )  ) )
    --End
    --Start
        UNION
        SELECT rad.user_id    --05/05/2016
        FROM RPT_AWARDMEDIA_DETAIL rad,
             promotion promo          
             --, badge_promotion bp  --12/01/2017
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'other'
          AND rad.award_payout_type = 'other'
          AND rad.promotion_id      = promo.promotion_id
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
          AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                     -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                     OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND rad.promotion_id      = promo.promotion_id
          --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
          AND ( ( p_in_nodeAndBelow = 1         --11/13/2015
          AND rad.node_id IN
              (SELECT child_node_id
              FROM rpt_hierarchy_rollup
              WHERE node_id IN
                (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
              ))                            --11/13/2015
              OR ( p_in_nodeAndBelow = 0        --11/13/2015
              AND rad.node_id       IN          --11/13/2015
                (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )  ) 
             )
          AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
    --Start
    UNION
    SELECT --rad.user_id      --05/05/2016
          CASE 
            WHEN rad.award_payout_type <> 'cash' 
                THEN user_id 
            ELSE NULL    
          END AS user_id   --05/05/2016
        FROM RPT_AWARDMEDIA_DETAIL rad,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user                                                           
        WHERE p_in_award_type = 'plateau' 
            AND plateau_earned > 0
            AND rad.promotion_id   = p.promotion_id
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
            AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = p.promotion_id )
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
            --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
            AND gil_u.ref_text_1 = gc_ref_text_user_id 
            AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )

            AND ( ( p_in_nodeAndBelow = 1         --11/13/2015
            AND rad.node_id IN
              (SELECT child_node_id
              FROM rpt_hierarchy_rollup
              WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
              ))                            --11/13/2015
              OR ( p_in_nodeAndBelow = 0        --11/13/2015
            AND rad.node_id       IN          --11/13/2015
                (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
               ))
            AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
            AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015    
    --End
        )
      )
    ) ;
    p_out_return_code := '00';
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
  /******************************************************************************
  NAME:       prc_getRcvNotRcvAwd_OrgBarRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014            Initial..(the queries were in Java before the release 5.4. So no comments available so far)  
  Suresh J              09/16/2014       Bug Fix 56589 - Replaced the entire query with the correct one to fix various Issue   
  nagarajs             12/09/2016        G5.6.3.3 report changes   
  Suresh J             03/09/2017        G6-1897 - Do not show org units with no data in charts and summary                          
  ******************************************************************************/
PROCEDURE prc_getRcvNotRcvAwd_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
IS
/******************************************************************************
  NAME:       prc_getRcvNotRcvAwd_OrgBarRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014      Initial..(the queries were in Java before the release 5.4. So no comments available so far)  
  Swati                    10/15/2014        Bug 57411 - In Reports column 'Eligible Participants' count should display based on the 
                                             Participant Status = Inactive/active selected in the Change Filter window
  Swati                    10/31/2014        Bug 57676 - Reports-Recognition Given/Received - Results are not correct when Participant Status = 'Show all'.  
  nagarajs                12/09/2016        G5.6.3.3 report changes              
  Suresh J                01/23/2017        Rewrote the queries for G5.6.3.3 reports performance tuning
  Suresh J                03/09/2017        G6-1897 - Do not show org units with no data in charts and summary                            
******************************************************************************/
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getRcvNotRcvAwd_OrgBarRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);
    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;

    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';     

 BEGIN
prc_execution_log_entry (c_process_name, 1, 'INFO', c_process_name , NULL);
-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),  gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end

  v_stage := 'getRcvNotRcvAwd_OrgBarRes';
  BEGIN

  DELETE gtt_recog_elg_count; --08/05/2014 Start
    
    IF fnc_check_promo_aud('receiver',NULL,p_in_promotionId) = 1  
        OR 
        --p_in_onTheSpotIncluded ='Y'   --04/12/2017  
        ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017

     THEN
    INSERT INTO gtt_recog_elg_count
    SELECT eligible_cnt, node_id, 'nodesum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_node pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_c   -- country
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND pe.node_id in (SELECT node_id
                              FROM rpt_hierarchy
                              WHERE parent_node_id IN                                                              
                                    (SELECT g.id FROM gtt_id_list g                                         
                                              WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                              ) 
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = pe.country_id )
            GROUP BY pe.node_id)
    UNION ALL
    SELECT eligible_cnt, node_id, 'teamsum' record_type
      FROM (  SELECT SUM (pe.elig_count) eligible_cnt, pe.node_id
                FROM rpt_pax_promo_elig_allaud_team pe
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_pn  -- parent node       
                     , gtt_id_list gil_c   -- country              
               WHERE     pe.giver_recvr_type = 'receiver'
                      AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                              OR gil_pn.id = pe.node_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = pe.department_type ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = pe.position_type )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = pe.country_id )
            GROUP BY pe.node_id);

        
    ELSIF NVL(INSTR(p_in_promotionId,','),0) = 0 THEN
      INSERT INTO gtt_recog_elg_count
      SELECT eligible_cnt,node_id,'nodesum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_node pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion id
             , gtt_id_list gil_c  -- country
       WHERE     pe.giver_recvr_type = 'receiver'
              AND pe.node_id in (SELECT node_id
                      FROM rpt_hierarchy
                      WHERE parent_node_id IN                                                              
                            (SELECT g.id FROM gtt_id_list g                                         
                                      WHERE g.ref_text_1 = gc_ref_text_parent_node_id )   
                      ) 
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type ) 
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = pe.country_id )
      GROUP BY pe.node_id)
      UNION ALL
      SELECT eligible_cnt,node_id,'teamsum' record_type FROM (SELECT SUM(pe.elig_count) eligible_cnt,
             pe.node_id
        FROM   rpt_pax_promo_elig_speaud_team pe
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_pn  -- node       
             , gtt_id_list gil_p  -- promotion id
             , gtt_id_list gil_c  -- country                           
       WHERE     pe.giver_recvr_type = 'receiver'
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = pe.promotion_id ) 
              AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
              AND (  gil_pn.ref_text_2 = gc_search_all_values
                      OR gil_pn.id = pe.node_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = pe.department_type ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = pe.position_type ) 
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = pe.country_id )
      GROUP BY pe.node_id);


  ELSE 
        INSERT INTO gtt_recog_elg_count
        SELECT eligible_cnt,node_id,'nodesum' record_type FROM (
        SELECT SUM(eligible_cnt) eligible_cnt,TO_NUMBER(TRIM(npn.path_node_id)) node_id  
                                               FROM   
                                             (                                     
                                             SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                                  eligible_cnt,  
                                                               ppe.node_id  
                                                          FROM (SELECT *  
                                                                  FROM rpt_pax_promo_eligibility  
                                                                 WHERE giver_recvr_type = 'receiver' ) ppe,  
                                                               promotion P,                                                        
                                                               rpt_participant_employer rad
                                                                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                                 , gtt_id_list gil_pt  -- position type
                                                                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                                                         WHERE ppe.promotion_id = P.promotion_id  
                                                                AND ppe.participant_id = rad.user_id(+)
                                                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                                                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                          OR gil_p.id = p.promotion_id)
                                                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                      OR gil_dt.ref_text_2 = rad.department_type ) 
                                                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                     OR gil_pt.ref_text_2 = rad.position_type ) 
                                                                --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                               AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                      NVL (p_in_participantStatus,  
                                                                           DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                   
                                                              GROUP BY ppe.node_id                                     
                                             ) elg, 
                                             ( SELECT child_node_id node_id,node_id path_node_id 
                                                FROM rpt_hierarchy_rollup
                                                     ,gtt_id_list gil_pn  --parent node                                          
                                                WHERE  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = node_id )
                                                ) npn 
                                             WHERE elg.node_id(+) = npn.node_id 
                                             GROUP BY npn.path_node_id)
        UNION ALL                                        
        SELECT eligible_cnt,node_id,'teamsum' record_type FROM (
        SELECT /*+ USE_HASH(p,rad,ppe) ORDERED */ COUNT (DISTINCT ppe.participant_id)  
                                                              eligible_cnt,  
                                                           ppe.node_id  
                                                      FROM (SELECT *  
                                                              FROM rpt_pax_promo_eligibility
                                                                   ,gtt_id_list gil_pn  --parent node
                                                             WHERE giver_recvr_type = 'receiver'  
                                                                      AND  gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                                      AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                              OR gil_pn.id = node_id ) ) ppe,  
                                                           promotion P,                                                        
                                                           rpt_participant_employer rad
                                                             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                                                             , gtt_id_list gil_pt  -- position type
                                                             , gtt_id_list gil_pn  -- node       
                                                             , gtt_id_list gil_p  -- promotion id
                                                     WHERE ppe.promotion_id = P.promotion_id  
                                                          AND ppe.participant_id = rad.user_id(+)
                                                          AND   gil_p.ref_text_1 = gc_ref_text_promotion_id
                                                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                                                  OR gil_p.id = ppe.promotion_id ) 
                                                          AND gil_pn.ref_text_1 = gc_ref_text_parent_node_id 
                                                          AND (  gil_pn.ref_text_2 = gc_search_all_values
                                                                  OR gil_pn.id = ppe.node_id )
                                                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                                  OR gil_dt.ref_text_2 = rad.department_type ) 
                                                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                                 OR gil_pt.ref_text_2 = rad.position_type )
                                                          --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                                                          AND DECODE(rad.termination_date,NULL,'active','inactive') =  
                                                                  NVL (p_in_participantStatus,  
                                                                       DECODE(rad.termination_date,NULL,'active','inactive'))                                                                                                          
                                                  GROUP BY ppe.node_id);     
                                             
    END IF;    --08/05/2014 End

    
    OPEN p_out_result_set FOR 
    SELECT ORG_NAME, RECEIVED_AWARD_PCT, HAVE_NOTRECEIVED_AWARD_PCT FROM
        (   SELECT ROWNUM RN, rs.*
    FROM
      (SELECT dtl.Org                                                                                                                         AS ORG_NAME,
        NVL (dtl.pax_elig, 0)                                                                                                                 AS ELIGIBLE_PARTICIPANTS,
        NVL (dtl.award_received, 0)                                                                                                           AS RECEIVED_AWARD,
        NVL ( ROUND ( DECODE ( dtl.pax_elig, 0, 0, (NVL (dtl.award_received, 0) / dtl.pax_elig) * 100), 2), 0)                                AS RECEIVED_AWARD_PCT,
        NVL ( (dtl.pax_elig                                                     - NVL (dtl.award_received, 0)), 0)                            AS HAVE_NOTRECEIVED_AWARD,
        NVL ( ROUND ( DECODE ( dtl.pax_elig, 0, 0, ( (dtl.pax_elig              - NVL (dtl.award_received, 0)) / dtl.pax_elig) * 100), 2), 0) AS HAVE_NOTRECEIVED_AWARD_PCT,
        dtl.node_id                                                                                                                           AS NODE_ID
      FROM
        (SELECT dtl.node_name AS Org,
          dtl.node_id,
          award_received,
          media_amount,
          plateau_earned,
          sweepstakes_won,
          OnTheSpot,
          (SELECT eligible_count FROM gtt_recog_elg_count WHERE record_type = 'nodesum' AND node_id = dtl.node_id) pax_elig
        FROM
          ( SELECT DISTINCT ras.detail_node_id,
            ras.header_node_id
          FROM rpt_awardmedia_summary ras,
            promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end 
          WHERE       p.promotion_id      = ras.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = ras.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = ras.job_position )
                  --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                  AND ras.record_type LIKE '%node%'
                  AND pax_status                = NVL (p_in_participantStatus, pax_status)
                  AND NVL (ras.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  GROUP BY detail_node_id,
                    header_node_id,
                    record_type
          UNION
          SELECT NODE_ID,
                PARENT_NODE_ID
          FROM rpt_hierarchy
          WHERE parent_node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
          ) raD,
          (SELECT node_id,
            node_name,
            award_received,
            media_amount,
            plateau_earned,
            sweepstakes_won,
            OnTheSpot            
          FROM rpt_hierarchy rh,
            (SELECT npn.path_node_id,
              SUM (award_received) award_received,
              SUM (media_amount) media_amount,
              SUM (plateau_earned) plateau_earned,
              SUM (sweepstakes_won) sweepstakes_won,
              SUM (OnTheSpot) OnTheSpot
--              SUM(elg_pax) elg_pax
            FROM
              (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup
              ) npn,
              (SELECT ras.node_id,
                COUNT ( DISTINCT ras.user_id) award_received,
                SUM (ras.media_amount) media_amount,
                SUM (plateau_earned) plateau_earned,
                SUM (sweepstakes_won) sweepstakes_won,
                SUM (OnTheSpot) OnTheSpot
              FROM
                (SELECT --rad.user_id,   --05/05/2016
                  CASE 
                    WHEN rad.award_payout_type <> 'cash' 
                        THEN rad.user_id 
                    ELSE NULL    
                  END AS user_id,   --05/05/2016
                  rad.node_id,
                  rad.media_amount,
                  rad.plateau_earned,
                  rad.sweepstakes_won,
                  0 AS OnTheSpot
                FROM RPT_AWARDMEDIA_DETAIL rad,
                      promotion p
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                     , gtt_id_list gil_c  -- country                                                           
                WHERE p_in_award_type = 'points'  
                AND plateau_earned = 0
                AND p.promotion_id      = rad.promotion_id
                AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                AND (  (gil_p.ref_text_2 = gc_search_all_values 
                          --AND 'Y' = p_in_onTheSpotIncluded  --04/12/2017
                          AND ( p_in_award_type = 'points' AND 'Y' = p_in_onTheSpotIncluded )  --04/12/2017
                          )
                      OR gil_p.id = p.promotion_id )
                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type )
                AND gil_c.ref_text_1 = gc_ref_text_country_id 
                AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                AND rad.participant_current_status  = NVL ( p_in_participantStatus, rad.participant_current_status)
                AND NVL ( rad.media_date, TRUNC ( SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                
    UNION
    SELECT --rad.user_id,   --05/05/2016
                      CASE 
                        WHEN rad.award_payout_type = 'cash' 
                            THEN rad.user_id 
                        ELSE NULL    
                      END AS user_id,   --05/05/2016
                      rad.node_id,
                      rad.media_amount,
                      rad.plateau_earned,
                      rad.sweepstakes_won,
                      0 AS OnTheSpot
                    FROM RPT_AWARDMEDIA_DETAIL rad,
                          promotion p
                         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                         , gtt_id_list gil_pt  -- position type
                         , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                         , gtt_id_list gil_c   -- country                                                           
                    WHERE p_in_award_type = 'cash'  
                    AND p.promotion_id      = rad.promotion_id
                    AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                    AND (  (gil_p.ref_text_2 = gc_search_all_values 
                            AND 'N' = p_in_onTheSpotIncluded )  --04/12/2017 
                          OR gil_p.id = p.promotion_id )
                    AND gil_dt.ref_text_1 = gc_ref_text_department_type
                    AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type )
                    AND gil_c.ref_text_1 = gc_ref_text_country_id 
                    AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                    --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                    AND rad.participant_current_status  = NVL ( p_in_participantStatus, rad.participant_current_status)
                    AND NVL ( rad.media_date, TRUNC ( SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                    AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
        UNION
        SELECT --rad.user_id,   --05/05/2016
                          CASE 
                            WHEN rad.award_payout_type <> 'cash' 
                                THEN rad.user_id 
                            ELSE NULL    
                          END AS user_id,   --05/05/2016
                          rad.node_id,
                          rad.media_amount,
                          rad.plateau_earned,
                          rad.sweepstakes_won,
                          0 AS OnTheSpot
                        FROM RPT_AWARDMEDIA_DETAIL rad,
                          promotion p
                         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                         , gtt_id_list gil_pt  -- position type
                         , gtt_id_list gil_p  -- promotion          --01/13/2017 end         
                         , gtt_id_list gil_c  -- country                                                  
                        WHERE p_in_award_type = 'merchandise'  --10/18/2016
                            AND plateau_earned > 0
                            AND p.promotion_id      = rad.promotion_id
                            AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                            AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = p.promotion_id )
                            AND gil_dt.ref_text_1 = gc_ref_text_department_type
                            AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                            AND gil_pt.ref_text_1 = gc_ref_text_position_type
                            AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type )
                            AND gil_c.ref_text_1 = gc_ref_text_country_id 
                            AND (  gil_c.ref_text_2 = gc_search_all_values
                                      OR gil_c.id = rad.country_id )
                            --AND (( p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                            AND rad.participant_current_status  = NVL ( p_in_participantStatus, rad.participant_current_status)
                            AND NVL ( rad.media_date, TRUNC ( SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                            AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
                UNION ALL
                SELECT rad.user_id,
                  rh.node_id,
                  rad.transaction_amount media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  1 OnTheSpot
                FROM rpt_qcard_detail rad,
                  rpt_hierarchy rh,
                  participant p
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_c   -- country
                WHERE p_in_award_type = 'points'  
                    AND rh.node_id         = rad.node_id
                    AND rad.user_id          = p.user_id
                    AND 'Y'                  = p_in_onTheSpotIncluded
                    AND p.status             = NVL (p_in_participantStatus, p.status)
                    AND gil_dt.ref_text_1 = gc_ref_text_department_type
                    AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type )
                    AND gil_c.ref_text_1 = gc_ref_text_country_id 
                    AND (  gil_c.ref_text_2 = gc_search_all_values
                             OR gil_c.id = rad.country_id )
                    AND NVL (TRUNC (rad.trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                UNION ALL
                SELECT --rad.user_id,     --05/05/2016
                  CASE WHEN rad.payout_type <> 'other'         --05/05/2016
                    THEN rad.user_id 
                    ELSE NULL
                  END AS user_id,                
                  rh.node_id,
                  rad.payout_amount media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 OnTheSpot
                FROM rpt_ssi_award_detail rad,
                  rpt_hierarchy rh,
                  participant p
                  ,promotion promo  
                  --,badge_promotion bp --12/01/2017
                  , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                  , gtt_id_list gil_pt  -- position type
                  , gtt_id_list gil_p  -- promotion          --01/13/2017 end   
                  , gtt_id_list gil_c -- country                                                        
                WHERE p_in_award_type = 'points'  
                        AND rh.node_id         = rad.node_id
                        AND rad.user_id          = p.user_id
                        AND rad.promotion_id      = promo.promotion_id
                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = promo.promotion_id 
                                  --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                                  OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                        AND gil_c.ref_text_1 = gc_ref_text_country_id 
                        AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = rad.country_id )
                        --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                        AND p.status             = NVL (p_in_participantStatus, p.status)
                        AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                UNION ALL
                SELECT --rad.user_id,     --05/05/2016
                  CASE WHEN rad.payout_type = 'other'         --05/05/2016
                    THEN rad.user_id 
                    ELSE NULL
                  END AS user_id,                
                  rh.node_id,
                  rad.payout_amount media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 OnTheSpot
                FROM rpt_ssi_award_detail rad,
                  rpt_hierarchy rh,
                  participant p
                  ,promotion promo                  
                 -- ,badge_promotion bp --12/01/2017
                  , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                  , gtt_id_list gil_pt  -- position type
                  , gtt_id_list gil_p  -- promotion          --01/13/2017 end   
                  , gtt_id_list gil_c   -- country                                                        
                WHERE p_in_award_type = 'other'  
                        AND rh.node_id         = rad.node_id
                        AND rad.user_id          = p.user_id
                        AND rad.promotion_id      = promo.promotion_id
                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = promo.promotion_id 
                                  --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                                  OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                        AND gil_c.ref_text_1 = gc_ref_text_country_id 
                        AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = rad.country_id )
                        --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                        AND p.status             = NVL (p_in_participantStatus, p.status)
                        AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                        --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                        AND p.status             = NVL (p_in_participantStatus, p.status)
                        AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                UNION ALL
                SELECT rad.user_id,     --05/05/2016
                  rad.node_id,
                  rad.media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 OnTheSpot
                FROM RPT_AWARDMEDIA_DETAIL rad,
                     promotion promo   
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end         
                     , gtt_id_list gil_c  -- country                                                  
                WHERE p_in_award_type = 'other'
                          AND rad.award_payout_type = 'other'  
                          AND rad.promotion_id      = promo.promotion_id
                          AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = promo.promotion_id )
                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                          AND gil_c.ref_text_1 = gc_ref_text_country_id 
                          AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = rad.country_id )
                          AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  
                          AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
                          AND NVL (TRUNC (rad.media_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                ) ras
              GROUP BY ras.node_id
              ) dtl
            WHERE dtl.node_id(+) = npn.node_id            
            GROUP BY npn.path_node_id
            )
          WHERE rh.node_id = path_node_id
          ) dtl
        WHERE raD.detail_node_id         = dtl.node_id
        AND dtl.node_id                  = dtl.node_id
        AND p_in_nodeAndBelow = 1
        AND NVL (raD.header_node_id, 0) IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
        UNION ALL
        SELECT rh.node_name||' Team' Org,
          rh.node_id,
          NVL (award_received, 0) award_received,
          NVL (media_amount, 0) media_amount,
          NVL (plateau_earned, 0) plateau_earned,
          NVL (sweepstakes_won, 0) sweepstakes_won,
          NVL (onTheSpot, 0) AS OnTheSpot,
          (select eligible_count FROM gtt_recog_elg_count WHERE record_type = 'teamsum' AND node_id = rh.node_id) pax_elig --08/05/2014
        FROM rpt_hierarchy rh,
          (SELECT COUNT (DISTINCT ras.user_id) award_received,
            ras.node_id,
            ras.node_name,
            SUM (ras.media_amount) media_amount,
            SUM (plateau_earned) plateau_earned,
            SUM (sweepstakes_won) sweepstakes_won,
            SUM( onTheSpot) AS onTheSpot
          FROM
            (SELECT --rad.user_id,   --05/05/2016
              CASE 
                WHEN rad.award_payout_type <> 'cash' 
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,   --05/05/2016
              rh.node_id,
              rh.node_name,
              rad.media_amount,
              rad.plateau_earned,
              rad.sweepstakes_won,
              0 AS OnTheSpot
            FROM RPT_AWARDMEDIA_DETAIL rad,
              rpt_hierarchy rh,
              promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end
             , gtt_id_list gil_c   -- country                                                           
            WHERE         p_in_award_type = 'points'
                      AND plateau_earned = 0 
                      AND p.promotion_id      = rad.promotion_id
                      AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                      AND ((gil_p.ref_text_2 = gc_search_all_values AND 'Y'  = p_in_onTheSpotIncluded)
                                OR gil_p.id = p.promotion_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      --AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                      AND rh.node_id           = rad.node_id
                      AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
                      AND NVL (rad.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                      AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
        UNION ALL
        SELECT --rad.user_id,   --05/05/2016
              CASE 
                WHEN rad.award_payout_type <> 'cash' 
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,   --05/05/2016
              rh.node_id,
              rh.node_name,
              rad.media_amount,
              rad.plateau_earned,
              rad.sweepstakes_won,
              0 AS OnTheSpot
            FROM RPT_AWARDMEDIA_DETAIL rad,
                 rpt_hierarchy rh,
                 promotion p
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end   
                 , gtt_id_list gil_c  -- country                                                        
            WHERE p_in_award_type = 'points'
                      AND plateau_earned > 0 
                      AND p.promotion_id      = rad.promotion_id
                      AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                      AND (( gil_p.ref_text_2 = gc_search_all_values AND 'Y' = p_in_onTheSpotIncluded)
                              OR gil_p.id = p.promotion_id )
                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type )
                      AND gil_c.ref_text_1 = gc_ref_text_country_id 
                      AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                      --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
                      AND rh.node_id           = rad.node_id
                      AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
                      AND NVL (rad.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                      AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
        --Start
        UNION ALL
        SELECT --rad.user_id,   --05/05/2016
                      CASE 
                        WHEN rad.award_payout_type = 'cash' 
                            THEN rad.user_id 
                        ELSE NULL    
                      END AS user_id,   --05/05/2016
                      rh.node_id,
                      rh.node_name,
                      rad.media_amount,
                      rad.plateau_earned,
                      rad.sweepstakes_won,
                      0 AS OnTheSpot
                    FROM RPT_AWARDMEDIA_DETAIL rad,
                      rpt_hierarchy rh,
                      promotion p
                         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                         , gtt_id_list gil_pt  -- position type
                         , gtt_id_list gil_p  -- promotion          --01/13/2017 end   
                         , gtt_id_list gil_c  -- country                                                        
                      WHERE p_in_award_type = 'cash'
                              AND p.promotion_id      = rad.promotion_id
                              AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                              AND (( gil_p.ref_text_2 = gc_search_all_values AND 'N' = p_in_onTheSpotIncluded)  --04/12/2017
                                      OR gil_p.id = p.promotion_id )
                              AND gil_dt.ref_text_1 = gc_ref_text_department_type
                              AND (  gil_dt.ref_text_2 = gc_search_all_values
                                      OR gil_dt.ref_text_2 = rad.department ) 
                              AND gil_pt.ref_text_1 = gc_ref_text_position_type
                              AND (  gil_pt.ref_text_2 = gc_search_all_values
                                     OR gil_pt.ref_text_2 = rad.position_type )
                              AND gil_c.ref_text_1 = gc_ref_text_country_id 
                              AND (  gil_c.ref_text_2 = gc_search_all_values
                                      OR gil_c.id = rad.country_id )
                              AND p.promotion_status = 'live'  --12/09/2016
                              AND rh.node_id           = rad.node_id
                              AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
                              AND NVL (rad.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
                              --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
                              AND rh.node_id           = rad.node_id
                              AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
                              AND NVL (rad.media_date, TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015
        --End
            UNION ALL
            SELECT rad.user_id,
              rh.node_id,
              rh.node_name,
              rad.transaction_amount media_amount,
              0 AS plateau_earned,
              0 AS sweepstakes_won,
              1 OnTheSpot
            FROM   rpt_qcard_detail rad,
                   rpt_hierarchy rh,
                   participant p
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_c  -- country
            WHERE p_in_award_type = 'points' 
            AND rh.node_id         = rad.node_id
            AND rad.user_id          = p.user_id
            AND 'Y'                  = p_in_onTheSpotIncluded
            AND p.status             = NVL (p_in_participantStatus, p.status)
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
            AND NVL (TRUNC (rad.trans_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
            UNION ALL
            SELECT --rad.user_id,   --05/05/2016
              CASE 
                WHEN rad.payout_type <> 'other' 
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,   --05/05/2016
              rh.node_id,
              rh.node_name,
              rad.payout_amount media_amount,
              0 AS plateau_earned,
              0 AS sweepstakes_won,
              0 OnTheSpot
            FROM rpt_ssi_award_detail rad,
              rpt_hierarchy rh,
              participant p
                 , promotion promo
               --  , badge_promotion bp --12/01/2017
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end      
                 , gtt_id_list gil_c  -- country                                                     
            WHERE p_in_award_type = 'points'  
            AND rh.node_id         = rad.node_id
            AND rad.user_id          = p.user_id
            AND rad.promotion_id      = promo.promotion_id
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id 
            AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
            AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
            AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
           --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
            AND p.status             = NVL (p_in_participantStatus, p.status)
            AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                UNION ALL
                SELECT --rad.user_id,   --05/05/2016
                  CASE 
                    WHEN rad.payout_type = 'other' 
                        THEN rad.user_id 
                    ELSE NULL    
                  END AS user_id,   --05/05/2016
                  rh.node_id,
                  rh.node_name,
                  rad.payout_amount media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 OnTheSpot
                FROM rpt_ssi_award_detail rad,
                  rpt_hierarchy rh,
                  participant p
                  ,promotion promo              
                 --, badge_promotion bp --12/01/2017
                 , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                 , gtt_id_list gil_pt  -- position type
                 , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                 , gtt_id_list gil_c  -- country                                                           
            WHERE p_in_award_type = 'other'  
                    AND rh.node_id         = rad.node_id
                    AND rad.user_id          = p.user_id
                    AND rad.promotion_id      = promo.promotion_id
                    AND gil_dt.ref_text_1 = gc_ref_text_department_type
                    AND (  gil_dt.ref_text_2 = gc_search_all_values
                              OR gil_dt.ref_text_2 = rad.department ) 
                    AND gil_pt.ref_text_1 = gc_ref_text_position_type
                    AND (  gil_pt.ref_text_2 = gc_search_all_values
                             OR gil_pt.ref_text_2 = rad.position_type )
                    AND gil_c.ref_text_1 = gc_ref_text_country_id 
                    AND (  gil_c.ref_text_2 = gc_search_all_values
                              OR gil_c.id = rad.country_id )
                    AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                    AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                              -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id))--12/01/2017
                              OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                    --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                    AND p.status             = NVL (p_in_participantStatus, p.status)
                    AND NVL (TRUNC (rad.payout_issue_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                UNION ALL
                SELECT rad.user_id,   --05/05/2016
                  rad.node_id,
                  rad.node_name,
                  rad.media_amount,
                  0 AS plateau_earned,
                  0 AS sweepstakes_won,
                  0 OnTheSpot
                FROM RPT_AWARDMEDIA_DETAIL rad,
                     promotion promo
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end
                     , gtt_id_list gil_c  -- country                                                           
                WHERE p_in_award_type = 'other'  
                  AND rad.award_payout_type = 'other'                
                  AND rad.promotion_id      = promo.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = promo.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                   --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016--commented out on 04/20/2017
                   AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
                   AND NVL (TRUNC (rad.media_date), TRUNC (SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
    --End
            ) ras
          GROUP BY ras.node_id,
            ras.node_name
          ) dtl
        WHERE rh.node_id         = dtl.node_id(+)
        AND NVL (rh.node_id, 0) IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
            ) dtl
      ) rs ORDER BY 2
    ) WHERE ELIGIBLE_PARTICIPANTS <> 0 AND    --03/09/2017
            ROWNUM    <= 20;
    p_out_return_code := '00';
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
PROCEDURE prc_getPerRcvAwd_OrgBarRes(
    --p_in_promotionStatus   IN VARCHAR, --12/09/2016
    p_in_jobPosition       IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_toDate            IN VARCHAR,
    p_in_onTheSpotIncluded IN VARCHAR,
    p_in_rowNumStart       IN NUMBER,
    p_in_rowNumEnd         IN NUMBER,
    p_in_languageCode      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_promotionId       IN VARCHAR,
    p_in_award_type        IN VARCHAR,     --05/05/2016    
    p_in_countryIds        IN VARCHAR,
    p_in_userid            IN NUMBER,
    p_in_sortColName       IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code OUT NUMBER,
    p_out_result_set OUT sys_refcursor)
IS
/*******************************************************************************
   Purpose: 

   Person         Date         Comments
   -----------   ----------   -----------------------------------------------------
  ????             ?????      Creation
  nagarajs       12/09/2016  G5.6.3.3 report changes
  Suresh J      01/23/2017   Rewrote the queries for G5.6.3.3 reports performance tuning  
  Suresh J      03/09/2017   G6-1897 - Do not show org units with no data in charts and summary
*******************************************************************************/
    c_delimiter     CONSTANT VARCHAR2 (1) := '|';
    c2_delimiter    CONSTANT VARCHAR2 (1) := '"';
    v_team_ids      VARCHAR2 (4000);
    c_process_name  CONSTANT execution_log.process_name%TYPE         := 'prc_getPerRcvAwd_OrgBarRes';
    c_release_level CONSTANT execution_log.release_level%TYPE        := 1.0;
    c_created_by    CONSTANT rpt_promo_node_activity.created_by%TYPE := 0;
    v_stage         VARCHAR2(500);

    gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
    gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
    gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
    gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
    gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
    gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';  
    gc_ref_text_user_id              CONSTANT gtt_id_list.ref_text_1%TYPE := 'user_id';
      
BEGIN

prc_execution_log_entry (c_process_name, 1, 'INFO', 'prc_getPerRcvAwd_OrgBarRes'
||'	p_in_jobPosition	:='||	p_in_jobPosition	||';'||CHR(10)
||'	p_in_participantStatus	:='||	p_in_participantStatus	||';'||CHR(10)
||'	p_in_localeDatePattern	:='||	p_in_localeDatePattern	||';'||CHR(10)
||'	p_in_parentNodeId	:='||	p_in_parentNodeId	||';'||CHR(10)
||'	p_in_fromDate	:='||	p_in_fromDate	||';'||CHR(10)
||'	p_in_toDate	:='||	p_in_toDate	||';'||CHR(10)
||'	p_in_onTheSpotIncluded	:='||	p_in_onTheSpotIncluded	||';'||CHR(10)
||'	p_in_rowNumStart	:='||	p_in_rowNumStart	||';'||CHR(10)
||'	p_in_rowNumEnd	:='||	p_in_rowNumEnd	||';'||CHR(10)
||'	p_in_languageCode	:='||	p_in_languageCode	||';'||CHR(10)
||'	p_in_nodeAndBelow	:='||	p_in_nodeAndBelow	||';'||CHR(10)
||'	p_in_departments	:='||	p_in_departments	||';'||CHR(10)
||'	p_in_promotionId	:='||	p_in_promotionId	||';'||CHR(10)
||'	p_in_award_type	:='||	p_in_award_type	||';'||CHR(10)
||'	p_in_countryIds	:='||	p_in_countryIds	||';'||CHR(10)
||'	p_in_userid	:='||	p_in_userid	||';'||CHR(10)
||'	p_in_sortColName	:='||	p_in_sortColName	||';'||CHR(10)
||'	p_in_sortedBy	:='||	p_in_sortedBy	||';'||CHR(10)
 , NULL);

-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   --01/13/2017 start
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),  gc_ref_text_country_id, 1);  
   pkg_report_common.p_stage_search_criteria(NVL(TO_CHAR(p_in_userid), gc_search_all_values),      gc_ref_text_user_id, 1);   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  --01/13/2017 end


  v_stage := 'getPerRcvAwd_OrgBarRes';
  BEGIN

    OPEN p_out_result_set FOR SELECT org, person_received_award FROM
    (SELECT dtl.node_name          AS Org,
      NVL(person_received_award,0) AS person_received_award,
      ras.detail_node_id As Org_id
    FROM
      (SELECT ras.detail_node_id,
        ras.header_node_id
      FROM rpt_awardmedia_summary ras,
           promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
      WHERE p.promotion_id = ras.promotion_id
              AND ras.record_type LIKE '%node%'
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = ras.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = ras.job_position ) 
              AND p.promotion_status = 'live'  --12/09/2016
              AND pax_status   = NVL(p_in_participantStatus, pax_status)
      GROUP BY detail_node_id,
        header_node_id,
        record_type
      UNION
      SELECT NODE_ID detail_node_id,
        PARENT_NODE_ID header_node_id
      FROM rpt_hierarchy
      WHERE parent_node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
      ) ras,
      (SELECT h.node_id ,
        h.node_name,
        SUM(person_received_award) person_received_award
      FROM
        (SELECT child_node_id node_id,node_id path_node_id FROM rpt_hierarchy_rollup
        )npn,
        rpt_hierarchy h,
        (SELECT ras.node_id,
          COUNT(DISTINCT ras.user_id) person_received_award
        FROM
          (SELECT --rad.user_id,    --05/05/2016
           CASE 
            WHEN rad.award_payout_type <> 'cash' 
                THEN rad.user_id 
            ELSE NULL    
           END AS user_id,   --05/05/2016
            rad.node_id
          FROM RPT_AWARDMEDIA_DETAIL rad,
               promotion p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user             
             , gtt_id_list gil_c  --country                                              
          WHERE p_in_award_type = 'points'
                  AND plateau_earned = 0 
                  AND rad.promotion_id   = p.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
                  AND rad.node_id IN
                    (SELECT child_node_id
                    FROM rpt_hierarchy_rollup
                    WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
                          AND CHILD_NODE_ID<>node_id
                    )
                  AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                  AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                    
        UNION ALL
        SELECT --rad.user_id,    --05/05/2016
                   CASE 
                    WHEN rad.award_payout_type <> 'cash' 
                        THEN rad.user_id 
                    ELSE NULL    
                   END AS user_id,   --05/05/2016
                    rad.node_id
                  FROM RPT_AWARDMEDIA_DETAIL rad,
                    promotion p
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                     , gtt_id_list gil_u  --user      
                     , gtt_id_list gil_c  --country                                                     
                  WHERE p_in_award_type = 'merchandise' --10/18/2016 replace 'plautea'
                          AND plateau_earned > 0 
                          AND rad.promotion_id   = p.promotion_id
                          AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = p.promotion_id )
                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                          AND gil_u.ref_text_1 = gc_ref_text_user_id 
                          AND (  gil_u.ref_text_2 = gc_search_all_values
                                  OR gil_u.id = rad.user_id )
                          AND gil_c.ref_text_1 = gc_ref_text_country_id 
                          AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = rad.country_id )
                          --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
                          AND rad.node_id IN
                            (SELECT child_node_id
                            FROM rpt_hierarchy_rollup
                            WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
                                  AND CHILD_NODE_ID<>node_id
                            )
                          AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                          AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                          AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                    
        UNION ALL
        SELECT --rad.user_id,    --05/05/2016
                   CASE 
                    WHEN rad.award_payout_type = 'cash' 
                        THEN rad.user_id 
                    ELSE NULL    
                   END AS user_id,   --05/05/2016
                    rad.node_id
                  FROM RPT_AWARDMEDIA_DETAIL rad,
                    promotion p
                     , gtt_id_list gil_dt  -- department type   --01/13/2017 start
                     , gtt_id_list gil_pt  -- position type
                     , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
                     , gtt_id_list gil_u  --user   
                     , gtt_id_list gil_c  --country                                                        
                  WHERE p_in_award_type = 'cash'
                  AND rad.promotion_id   = p.promotion_id
                          AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = p.promotion_id )
                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                 OR gil_pt.ref_text_2 = rad.position_type ) 
                          AND gil_u.ref_text_1 = gc_ref_text_user_id 
                          AND (  gil_u.ref_text_2 = gc_search_all_values
                                  OR gil_u.id = rad.user_id )
                          AND gil_c.ref_text_1 = gc_ref_text_country_id 
                          AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = rad.country_id )
                          --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
                          AND rad.node_id IN
                            (SELECT child_node_id
                            FROM rpt_hierarchy_rollup
                            WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
                                  AND CHILD_NODE_ID<>node_id
                            )
                          AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                          AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                          AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                    
        --End
          UNION ALL
          SELECT rad.user_id,
            rad.node_id
          FROM rpt_qcard_detail rad,
            participant p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_u  --user                 
             , gtt_id_list gil_c  -- country                                                       
          WHERE p_in_award_type = 'points' 
          AND rad.user_id       = p.user_id
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND rad.node_id IN
            (SELECT child_node_id
            FROM rpt_hierarchy_rollup
            WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
                  AND CHILD_NODE_ID<>node_id
            )
          AND 'Y'  = p_in_onTheSpotIncluded
          AND p.status           = NVL (p_in_participantStatus, p.status)
          UNION ALL
          SELECT --rad.user_id,   --05/05/2016
                  CASE 
                    WHEN rad.payout_type <> 'other' 
                        THEN rad.user_id 
                    ELSE NULL    
                  END AS user_id,   --05/05/2016
            rad.node_id
          FROM rpt_ssi_award_detail rad,
               participant p
             , promotion promo
             --, badge_promotion bp  --12/01/2017           
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'points' 
                  AND rad.user_id       = p.user_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                              --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                              OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                 AND rad.promotion_id      = promo.promotion_id
                 --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                 AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                 AND rad.node_id IN
                    (SELECT child_node_id
                    FROM rpt_hierarchy_rollup
                    WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )                    
                          AND child_node_id<>node_id
                    )
                 AND p.status           = NVL (p_in_participantStatus, p.status)
          UNION ALL
          SELECT --rad.user_id,   --05/05/2016
                  CASE 
                    WHEN rad.payout_type = 'other' 
                        THEN rad.user_id 
                    ELSE NULL    
                  END AS user_id,   --05/05/2016
            rad.node_id
          FROM rpt_ssi_award_detail rad,
                participant p
              ,promotion promo            
             --, badge_promotion bp  --12/01/2017           
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'other' 
                  AND rad.user_id       = p.user_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                              --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                              OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                  AND rad.promotion_id      = promo.promotion_id
                  --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                  AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND rad.node_id IN
                            (SELECT child_node_id
                            FROM rpt_hierarchy_rollup
                            WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )                    
                                  AND child_node_id<>node_id
                            )
                  AND p.status           = NVL (p_in_participantStatus, p.status)
          UNION ALL
          SELECT rad.user_id,   --05/05/2016
                 rad.node_id
          FROM RPT_AWARDMEDIA_DETAIL rad,
               promotion promo    
             --, badge_promotion bp   --12/01/2017     
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
             , gtt_id_list gil_u  --user                                                           
             , gtt_id_list gil_c  -- country
          WHERE p_in_award_type = 'other'
                  AND rad.award_payout_type = 'other' 
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                              OR gil_p.id = promo.promotion_id 
                              --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                  AND rad.promotion_id      = promo.promotion_id
                  --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                  AND rad.node_id IN
                            (SELECT child_node_id
                            FROM rpt_hierarchy_rollup
                            WHERE node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )                    
                                  AND child_node_id<>node_id
                            )
                  AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
--05/12/2015
          ) ras
        GROUP BY ras.node_id
        ) dtl
      WHERE dtl.node_id(+) = npn.node_id
      AND npn.path_node_id = h.node_id
      GROUP BY h.node_id ,
        h.node_name
      ) dtl
    WHERE ras.detail_node_id       = dtl.node_id
    AND NVL(ras.header_node_id,0) IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
    AND p_in_nodeAndBelow = 1
    UNION
    SELECT rh.node_name
      ||' Team' Org,
      NVL(Person_Received_Award,0),
      rh.node_id
    FROM rpt_hierarchy rh,
      (SELECT COUNT(DISTINCT ras.user_id) Person_Received_Award,
        ras.node_id,
        ras.node_name
      FROM
        (SELECT --rad.user_id ,   --05/05/2016
          CASE 
            WHEN rad.award_payout_type <> 'cash' 
                THEN rad.user_id 
            ELSE NULL    
          END AS user_id,   --05/05/2016
          rh.node_id,
          rh.node_name
        FROM RPT_AWARDMEDIA_DETAIL rad,
          rpt_hierarchy rh,
          promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user           
         , gtt_id_list gil_c  --country                                                
        WHERE p_in_award_type = 'points'
              AND plateau_earned = 0 
              AND rh.node_id         = rad.node_id
              AND p.promotion_id       = rad.promotion_id
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
              AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = p.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = rad.department ) 
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                     OR gil_pt.ref_text_2 = rad.position_type ) 
              AND gil_u.ref_text_1 = gc_ref_text_user_id 
              AND (  gil_u.ref_text_2 = gc_search_all_values
                      OR gil_u.id = rad.user_id )
              AND gil_c.ref_text_1 = gc_ref_text_country_id 
              AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
              --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
              AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
              AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
              AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                    
    UNION ALL
    SELECT --rad.user_id ,   --05/05/2016
              CASE 
                WHEN rad.award_payout_type <> 'cash' 
                    THEN rad.user_id 
                ELSE NULL    
              END AS user_id,   --05/05/2016
              rh.node_id,
              rh.node_name
            FROM RPT_AWARDMEDIA_DETAIL rad,
              rpt_hierarchy rh,
              promotion p
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user       
         , gtt_id_list gil_c  --country                                                    
            WHERE p_in_award_type = 'merchandise'
                  AND plateau_earned > 0 
                  AND rh.node_id         = rad.node_id
                  AND p.promotion_id       = rad.promotion_id
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id 
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                          OR gil_p.id = p.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                          OR gil_c.id = rad.country_id )
                  --AND p.promotion_status = 'live'  --12/09/2016--commented out on 04/20/2017
                  AND rad.participant_current_status = NVL(p_in_participantStatus, rad.participant_current_status)
                  AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND NOT EXISTS (select j2.journal_id from rpt_ssi_award_detail j2 where j2.journal_id = rad.journal_id)  --05/12/2015                    
        UNION ALL
        SELECT rad.user_id,
          rad.node_id,
          rad.node_name
        FROM rpt_qcard_detail rad,
              participant p
             , gtt_id_list gil_dt  -- department type   --01/13/2017 start
             , gtt_id_list gil_pt  -- position type
             , gtt_id_list gil_u   -- user                                                           
             , gtt_id_list gil_c   -- country
        WHERE p_in_award_type = 'points' 
        AND rad.user_id       = p.user_id
          AND gil_dt.ref_text_1 = gc_ref_text_department_type
          AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = rad.department ) 
          AND gil_pt.ref_text_1 = gc_ref_text_position_type
          AND (  gil_pt.ref_text_2 = gc_search_all_values
                 OR gil_pt.ref_text_2 = rad.position_type ) 
          AND gil_u.ref_text_1 = gc_ref_text_user_id 
          AND (  gil_u.ref_text_2 = gc_search_all_values
                  OR gil_u.id = rad.user_id )
          AND gil_c.ref_text_1 = gc_ref_text_country_id 
          AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = rad.country_id )
          AND NVL(TRUNC(trans_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
          AND ((p_in_parentNodeId IS NOT NULL AND rad.node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))
                OR p_in_parentNodeId IS NULL)
          AND 'Y'                = p_in_onTheSpotIncluded
          AND p.status           = NVL (p_in_participantStatus, p.status)
        UNION ALL
        SELECT --rad.user_id,  --05/05/2016
          CASE WHEN rad.payout_type <> 'other' 
               THEN rad.user_id 
               ELSE NULL 
          END AS user_id,
          rad.node_id,
          rad.node_name
        FROM rpt_ssi_award_detail rad,
          participant p
          ,promotion promo
         -- ,badge_promotion bp --12/01/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user                                                           
         , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'points' 
                AND rad.user_id       = p.user_id
                AND rad.promotion_id      = promo.promotion_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                      --OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                      OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                  --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                  AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND ((p_in_parentNodeId IS NOT NULL AND rad.node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))
                        OR p_in_parentNodeId IS NULL)
                  AND p.status           = NVL (p_in_participantStatus, p.status)
        UNION ALL
        SELECT --rad.user_id,  --05/05/2016
          CASE WHEN rad.payout_type = 'other' 
               THEN rad.user_id 
               ELSE NULL 
          END AS user_id,
          rad.node_id,
          rad.node_name
        FROM rpt_ssi_award_detail rad,
          participant p
          ,promotion promo          
        --  ,badge_promotion bp --12/01/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user                                                           
         , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'other' 
                AND rad.user_id       = p.user_id
                AND rad.promotion_id      = promo.promotion_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                     -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                     OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                  --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                  AND NVL(TRUNC(rad.payout_issue_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND ((p_in_parentNodeId IS NOT NULL AND rad.node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))
                        OR p_in_parentNodeId IS NULL)
                  AND p.status           = NVL (p_in_participantStatus, p.status)
        UNION ALL
        SELECT rad.user_id,  --05/05/2016
          rad.node_id,
          rad.node_name
        FROM RPT_AWARDMEDIA_DETAIL rad,
             promotion promo          
          --,badge_promotion bp --12/01/2017
         , gtt_id_list gil_dt  -- department type   --01/13/2017 start
         , gtt_id_list gil_pt  -- position type
         , gtt_id_list gil_p  -- promotion          --01/13/2017 end                                                           
         , gtt_id_list gil_u  --user                                                           
         , gtt_id_list gil_c  -- country
        WHERE p_in_award_type = 'other'
                  AND rad.award_payout_type = 'other' 
                  AND rad.promotion_id      = promo.promotion_id
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                          OR gil_dt.ref_text_2 = rad.department ) 
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                         OR gil_pt.ref_text_2 = rad.position_type ) 
                  AND gil_u.ref_text_1 = gc_ref_text_user_id 
                  AND (  gil_u.ref_text_2 = gc_search_all_values
                          OR gil_u.id = rad.user_id )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id 
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = rad.country_id )
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id --01/13/2017 start
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = promo.promotion_id 
                     -- OR (gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)) --12/01/2017
                     OR ( EXISTS (select 1 from badge_promotion bp  WHERE  gil_p.id = bp.promotion_id AND bp.eligible_promotion_id = promo.promotion_id)))  -- 12/01/2017
                  --AND ((p_in_promotionId IS NULL AND promo.promotion_status IN('live','expired')) OR (p_in_promotionId IS NOT NULL AND promo.promotion_status = 'live'))  --12/09/2016  --commented out on 04/20/2017
                  AND NVL(TRUNC(rad.media_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                  AND ((p_in_parentNodeId IS NOT NULL AND rad.node_id IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id ))
                        OR p_in_parentNodeId IS NULL)
                  AND rad.participant_current_status = NVL (p_in_participantStatus, rad.participant_current_status)
              ) ras
      GROUP BY ras.node_id,
        ras.node_name
      ) dtl
    WHERE rh.node_id       = dtl.node_id (+)
    AND NVL(rh.node_id,0) IN (SELECT g.id FROM gtt_id_list g WHERE g.ref_text_1 = gc_ref_text_parent_node_id )
    ORDER BY 2 DESC
    ) WHERE --PERSON_RECEIVED_AWARD <> 0 AND          --03/09/2017  --12/07/2017 G6-3158
    (SELECT eligible_count FROM gtt_recog_elg_count WHERE node_id = org_id) > 0 AND  --12/07/2017 G6-3158 
    ROWNUM    <= 20 ORDER BY Org ASC ;
    p_out_return_code := '00';
    
  EXCEPTION
  WHEN OTHERS THEN
    p_out_return_code := '99';
    prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM, NULL);
    OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
  END;
END; --End proc
END;
/

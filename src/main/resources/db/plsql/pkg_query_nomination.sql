CREATE OR REPLACE PACKAGE pkg_query_nomination
IS
/*********************************************************************
Purpose: Provides nomination query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         04/22/2016  Initial version  
**********************************************************************/

PROCEDURE prc_summary_by_org
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
);

PROCEDURE prc_nominators_by_org
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
);

PROCEDURE prc_nominees_by_org
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
);

PROCEDURE prc_nominations_by_month
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
);

END pkg_query_nomination;

/
CREATE OR REPLACE PACKAGE BODY      pkg_query_nomination
IS
/*********************************************************************
Purpose: Provides nomination query utilities.

MODIFICATION HISTORY
   Person          Date        Comments
   ------------    ----------  ---------------------------------------
   J Flees         04/22/2016  Initial version  
   nagarajs       02/14/2017  Bug 71359 - Nominations - Nominations Activity by Organization Report\
   nagarajs       02/22/2017  Return data for expired promotions too 
   Sherif Basha   03/17/2017  [JIRA] (G6-1893) Nomination Activity by Organisation - Owner/Manager report - The summary details don't seem to be correct
                               --convert cash,other_award_amt stored as USD values into logged in user's currency 
**********************************************************************/
-- package constants
gc_release_level                 CONSTANT execution_log.release_level%type := 1.0;
gc_pkg_name                      CONSTANT VARCHAR2(30) := UPPER('pkg_query_nomination');

gc_return_code_success           CONSTANT NUMBER := pkg_const.gc_return_code_success;
gc_return_code_failure           CONSTANT NUMBER := pkg_const.gc_return_code_failure;

gc_error                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_error;
gc_debug                         CONSTANT execution_log.severity%TYPE := pkg_const.gc_debug;
gc_warn                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_warn;
gc_info                          CONSTANT execution_log.severity%TYPE := pkg_const.gc_info;

gc_pax_status_active             CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_active;
gc_pax_status_inactive           CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_inactive;

gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;

gc_promotion_type_nomination     CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_nomination;            

gc_award_type_cash               CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_cash;
gc_award_type_other              CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_other;
gc_award_type_points             CONSTANT promotion.award_type%TYPE := pkg_const.gc_award_type_points;

gc_approval_status_winner        CONSTANT claim_item_approver.approval_status_type%TYPE := pkg_const.gc_approval_status_winner;

gc_elg_type_giver                CONSTANT rpt_pax_promo_eligibility.giver_recvr_type%TYPE := pkg_const.gc_elg_type_giver;
gc_elg_type_receiver             CONSTANT rpt_pax_promo_eligibility.giver_recvr_type%TYPE := pkg_const.gc_elg_type_receiver;

gc_null_id                       CONSTANT NUMBER(18) := pkg_const.gc_null_id;
gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;

gc_cms_list_month_name           CONSTANT cms_asset.code%TYPE := pkg_const.gc_cms_list_month_name;

gc_node_name_team_suffix         CONSTANT VARCHAR2(30) := ' Team';

gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';
gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';


-----------------------
-- Private package processes
-----------------------
-- Stages the eligible count table based on whether input promotions have an all pax or specified audience.
-- Note: Process assumes the query parameters have already been staged to the temp ID list table.
PROCEDURE p_stage_eligible_counts
( p_in_giver_recvr_type    IN VARCHAR2,
  p_in_promotion_type      IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_user_status_desc    IN VARCHAR2,
  p_in_promotion_status    IN VARCHAR2
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'p_stage_eligible_counts';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_is_all_audience    NUMBER;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_giver_recvr_type >' || p_in_giver_recvr_type
      || '<, p_in_promotion_type >' || p_in_promotion_type
      || '<, p_in_promotion_id_list >' || p_in_promotion_id_list
      || '<, p_in_user_status_desc >' || p_in_user_status_desc
      || '<, p_in_promotion_status >' || p_in_promotion_status
      || '<~';     

   v_stage := 'check promo audience';
   v_is_all_audience := fnc_check_promo_aud(p_in_giver_recvr_type, p_in_promotion_type, p_in_promotion_id_list);
   IF (v_is_all_audience = 1) THEN
      -- at least one promotion associated with an all pax audience
      v_stage := 'INSERT gtt_recog_elg_count (all pax audience)';
      INSERT INTO gtt_recog_elg_count
      ( node_id,
        record_type,
        eligible_count
      )
      (  -- sum counts for child node(s) of input parent node(s)
         SELECT pe.node_id,
                pe.giver_recvr_type AS record_type,
                SUM(pe.elig_count) AS eligible_cnt
           FROM rpt_hierarchy h,
                rpt_pax_promo_elig_allaud_node pe,
                gtt_id_list gil_c,  -- country
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt  -- position type
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = h.parent_node_id 
            AND h.node_id = pe.node_id
            AND pe.giver_recvr_type = p_in_giver_recvr_type
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id
            AND (  gil_c.ref_text_2 = gc_search_all_values
                OR gil_c.id = pe.country_id )
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
          GROUP BY pe.node_id,
                pe.giver_recvr_type
          UNION ALL
         -- sum counts for parent team(s)
         SELECT pe.node_id,
                pe.giver_recvr_type AS record_type,
                SUM(pe.elig_count) AS eligible_count
           FROM rpt_pax_promo_elig_allaud_team pe,
                gtt_id_list gil_c,  -- country
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt  -- position type
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = pe.node_id
            AND pe.giver_recvr_type = p_in_giver_recvr_type
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id
            AND (  gil_c.ref_text_2 = gc_search_all_values
                OR gil_c.id = pe.country_id )
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
          GROUP BY pe.node_id,
                pe.giver_recvr_type
      );

   ELSIF NVL(INSTR(p_in_promotion_id_list,','),0) = 0 THEN
      -- one/zero promotion with specified audience
      v_stage := 'INSERT gtt_recog_elg_count (one specified audience)';
      INSERT INTO gtt_recog_elg_count
      ( node_id,
        record_type,
        eligible_count
      )
      (  -- sum counts for child node(s) of input parent node(s)
         SELECT pe.node_id,
                pe.giver_recvr_type AS record_type,
                SUM(pe.elig_count) AS eligible_cnt
           FROM rpt_hierarchy h,
                rpt_pax_promo_elig_speaud_node pe,
                gtt_id_list gil_c,  -- country
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt  -- position type
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = h.parent_node_id 
            AND h.node_id = pe.node_id
            AND pe.giver_recvr_type = p_in_giver_recvr_type
            AND pe.promotion_type = gc_promotion_type_nomination
             -- table uses promo ID -1 for all promotions 
            AND pe.promotion_id = NVL(p_in_promotion_id_list, -1)
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id
            AND (  gil_c.ref_text_2 = gc_search_all_values
                OR gil_c.id = pe.country_id )
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
          GROUP BY pe.node_id,
                pe.giver_recvr_type
          UNION ALL
         -- sum counts for parent team(s)
         SELECT pe.node_id,
                pe.giver_recvr_type AS record_type,
                SUM(pe.elig_count) AS eligible_count
           FROM rpt_pax_promo_elig_speaud_team pe,
                gtt_id_list gil_c,  -- country
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt  -- position type
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = pe.node_id
            AND pe.giver_recvr_type = p_in_giver_recvr_type
            AND pe.promotion_type = gc_promotion_type_nomination
             -- table uses promo ID -1 for all promotions 
            AND pe.promotion_id = NVL(p_in_promotion_id_list, -1)
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id
            AND (  gil_c.ref_text_2 = gc_search_all_values
                OR gil_c.id = pe.country_id )
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active -- 10/15/2014 Bug 57411 --10/31/2014 Bug 57676
          GROUP BY pe.node_id,
                pe.giver_recvr_type
      );

   ELSE
      -- multiple promotions with specified audience
      v_stage := 'INSERT gtt_recog_elg_count (multi-specified audience)';
      INSERT INTO gtt_recog_elg_count
      ( node_id,
        record_type,
        eligible_count
      )
      (  SELECT dr.node_id,
                dr.record_type,
                dr.eligible_count
           FROM ( -- build data record
                  WITH w_node AS
                  (  -- get input parent node(s)
                     SELECT h.node_id AS org_id,
                            h.node_id
                       FROM gtt_id_list gil,
                            rpt_hierarchy h
                      WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                        AND gil.id = h.node_id 
                        AND h.is_deleted = 0
                      UNION ALL
                     -- get child rollup nodes of input parent node(s) 
                     SELECT h.node_id AS org_id,
                            hr.child_node_id AS node_id
                       FROM gtt_id_list gil,
                            rpt_hierarchy h,
                            rpt_hierarchy_rollup hr
                      WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                        AND gil.id = h.parent_node_id 
                        AND h.is_deleted = 0
                        AND h.node_id = hr.node_id
                  )
                  -- ensure each org node returns a record
                  SELECT wn.org_id AS node_id,
                         NVL(rd.giver_recvr_type, p_in_giver_recvr_type) AS record_type,
                         NVL(rd.eligible_count, 0) AS eligible_count
                    FROM ( -- get raw data
                           -- sum counts for org nodes
                           SELECT wn.org_id,
                                  ppe.giver_recvr_type,
                                  COUNT (DISTINCT ppe.participant_id) AS eligible_count
                             FROM w_node wn,
                                  rpt_pax_promo_eligibility ppe,
                                  promotion p,
                                  rpt_participant_employer paxe,
                                  user_address ua,
                                  gtt_id_list gil_c,  -- country
                                  gtt_id_list gil_dt, -- department type
                                  gtt_id_list gil_pt, -- position type
                                  gtt_id_list gil_p   -- promotion
                            WHERE wn.node_id = ppe.node_id
                              AND ppe.promotion_id = p.promotion_id
                              AND ppe.participant_id = paxe.user_id (+)
                              AND ppe.participant_id = ua.user_id (+)
                              AND ua.is_primary (+) = 1
                              AND ppe.giver_recvr_type = p_in_giver_recvr_type
                              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                              AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = ppe.promotion_id )
                              AND p.promotion_type = gc_promotion_type_nomination
                              --AND p.promotion_status = NVL(p_in_promotion_status, p.promotion_status)  --02/22/2017
                              AND gil_dt.ref_text_1 = gc_ref_text_department_type
                              AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = paxe.department_type )
                              AND gil_pt.ref_text_1 = gc_ref_text_position_type
                              AND (  gil_pt.ref_text_2 = gc_search_all_values
                                  OR gil_pt.ref_text_2 = paxe.position_type )
                              AND (p_in_user_status_desc IS NULL
                                  OR p_in_user_status_desc
                                     = DECODE(paxe.termination_date, NULL, gc_pax_status_active, gc_pax_status_inactive) )
                              AND gil_c.ref_text_1 = gc_ref_text_country_id
                              AND (  gil_c.ref_text_2 = gc_search_all_values
                                  OR gil_c.id = ua.country_id )
                            GROUP BY wn.org_id,
                                  ppe.giver_recvr_type
                         ) rd,
                         w_node wn
                      -- restrict to just the org nodes
                   WHERE wn.org_id = wn.node_id
                      -- outer join so all queried org nodes appear in result set
                     AND wn.org_id = rd.org_id (+)
                ) dr
      );

   END IF;

EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
END p_stage_eligible_counts;

-----------------------
-- Public package processes
-----------------------
-- Queries nominations based on input parameters and summerizes the results by organizational node
PROCEDURE prc_summary_by_org
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR,
  p_out_totals_data        OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;
   --*******************************************************************************
-- Purpose: This procedure queries the nomination summary  result set 
--
-- Person         Date        Comments
-- -------------- ----------  -----------------------------------------------------
--DeepakrajS      04/09/2019 BUG 75188 Added logic to display awards in respective logged in user currency code
--*******************************************************************************

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_summary_by_org';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_data_sort          VARCHAR2(50);
   v_from_date          DATE;
   v_to_date            DATE;
   v_fetch_count        INTEGER;
   v_cash_currency_value   cash_currency_current.bpom_entered_rate%TYPE;
   v_budget_media_value    NUMBER(12,4); --04/09/2019
   v_in_pax_countryratio   country.budget_media_value%TYPE; --04/09/2019

   -- build query ref cursor record type
   CURSOR cur_query_ref IS
   SELECT CAST(NULL AS NUMBER) AS org_id,
          CAST(NULL AS VARCHAR2(255)) AS org_name,
          CAST(NULL AS NUMBER) AS eligible_nominators,
          CAST(NULL AS NUMBER) AS actual_nominators,
          CAST(NULL AS NUMBER) AS pct_eligible_nominators,
          CAST(NULL AS NUMBER) AS eligible_nominees,
          CAST(NULL AS NUMBER) AS actual_nominees,
          CAST(NULL AS NUMBER) AS pct_eligible_nominees,
          CAST(NULL AS NUMBER) AS nominations_submitted,
          CAST(NULL AS NUMBER) AS nominations_received,
          CAST(NULL AS NUMBER) AS nominations_won,
          CAST(NULL AS NUMBER) AS points_received,
          CAST(NULL AS NUMBER) AS cash_received,
          CAST(NULL AS NUMBER) AS other_qty_received,
          CAST(NULL AS NUMBER) AS other_value_received,
          CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq
     FROM dual;

   rec_query      cur_query_ref%ROWTYPE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart      
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<, p_in_userId >' || p_in_userId
      || '<~';     
      
            

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
       --G6-1893 03/17/2017
   -- stage viewer's currency value
   v_stage := 'Get currency value';
     BEGIN    
        SELECT ccc.bpom_entered_rate
          INTO v_cash_currency_value
          FROM user_address ua,
               country c,
               cash_currency_current ccc 
         WHERE ua.user_id = p_in_userId
           AND is_primary = 1
           AND ua.country_id = c.country_id
           AND c.currency_code = ccc.to_cur;
     EXCEPTION
        WHEN OTHERS THEN
          v_cash_currency_value := 1;
     END;
     
     --Added on 04/09/2019
        v_stage := 'Get Logged in User Media value';
         BEGIN
             SELECT c.BUDGET_MEDIA_VALUE
               INTO v_in_pax_countryratio
               FROM user_address ua, country c
              WHERE ua.user_id = p_in_userId 
              AND ua.country_id = c.country_id;
                                                                               
              SELECT (budget_media_value / v_in_pax_countryratio)
               INTO v_budget_media_value  
               FROM country c   
              WHERE c.cm_asset_code ='country_data.country.usa'; 
         EXCEPTION WHEN OTHERS THEN
            v_budget_media_value := 1;                        
         END; 

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),   gc_ref_text_country_id, 1);

   -- stage eligible counts
   v_stage := 'stage eligible counts';
   DELETE gtt_recog_elg_count; --08/05/2014 Start
   p_stage_eligible_counts(gc_elg_type_giver,    gc_promotion_type_nomination, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);
   p_stage_eligible_counts(gc_elg_type_receiver, gc_promotion_type_nomination, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   WITH w_node AS
   (  -- get input parent node(s)
      SELECT h.node_id AS org_id,
             h.node_name || gc_node_name_team_suffix AS org_name,
             h.node_id
        FROM gtt_id_list gil,
             rpt_hierarchy h
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = h.node_id 
         AND h.is_deleted = 0
       UNION ALL
      -- get child rollup nodes of input parent node(s) 
      SELECT h.node_id AS org_id,
             h.node_name AS org_name,
             hr.child_node_id AS node_id
        FROM gtt_id_list gil,
             rpt_hierarchy h,
             rpt_hierarchy_rollup hr
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = h.parent_node_id 
         AND h.is_deleted = 0
         AND h.node_id = hr.node_id
   )
   SELECT sd.*
     FROM ( -- sequence the data
            SELECT org.*,
                   COUNT(DISTINCT org.org_id) OVER() AS total_records,
                   -- calc record sort order
                   ROW_NUMBER() OVER (ORDER BY
                     -- sort totals record first
                     DECODE(org.org_id, NULL, 0, 99),
                     -- ascending sorts
                     DECODE(v_data_sort, 'org_id', org.org_id),
                     DECODE(v_data_sort, 'org_name', LOWER(org.org_name)),
                     DECODE(v_data_sort, 'eligible_nominators', org.eligible_nominators),
                     DECODE(v_data_sort, 'actual_nominators', org.actual_nominators),
                     DECODE(v_data_sort, 'pct_eligible_nominators', org.pct_eligible_nominators),
                     DECODE(v_data_sort, 'eligible_nominees', org.eligible_nominees),
                     DECODE(v_data_sort, 'actual_nominees', org.actual_nominees),
                     DECODE(v_data_sort, 'pct_eligible_nominees', org.pct_eligible_nominees),
                     DECODE(v_data_sort, 'nominations_submitted', org.nominations_submitted),
                     DECODE(v_data_sort, 'nominations_received', org.nominations_received),
                     DECODE(v_data_sort, 'nominations_won', org.nominations_won),
                     DECODE(v_data_sort, 'points_received', org.points_received),
                     DECODE(v_data_sort, 'cash_received', org.cash_received),
                     DECODE(v_data_sort, 'other_qty_received', org.other_qty_received),
                     DECODE(v_data_sort, 'other_value_received', org.other_value_received),
                     -- descending sorts
                     DECODE(v_data_sort, 'desc/org_id', org.org_id) DESC,
                     DECODE(v_data_sort, 'desc/org_name', LOWER(org.org_name)) DESC,
                     DECODE(v_data_sort, 'desc/eligible_nominators', org.eligible_nominators) DESC,
                     DECODE(v_data_sort, 'desc/actual_nominators', org.actual_nominators) DESC,
                     DECODE(v_data_sort, 'desc/pct_eligible_nominators', org.pct_eligible_nominators) DESC,
                     DECODE(v_data_sort, 'desc/eligible_nominees', org.eligible_nominees) DESC,
                     DECODE(v_data_sort, 'desc/actual_nominees', org.actual_nominees) DESC,
                     DECODE(v_data_sort, 'desc/pct_eligible_nominees', org.pct_eligible_nominees) DESC,
                     DECODE(v_data_sort, 'desc/nominations_submitted', org.nominations_submitted) DESC,
                     DECODE(v_data_sort, 'desc/nominations_received', org.nominations_received) DESC,
                     DECODE(v_data_sort, 'desc/nominations_won', org.nominations_won) DESC,
                     DECODE(v_data_sort, 'desc/points_received', org.points_received) DESC,
                     DECODE(v_data_sort, 'desc/cash_received', org.cash_received) DESC,
                     DECODE(v_data_sort, 'desc/other_qty_received', org.other_qty_received) DESC,
                     DECODE(v_data_sort, 'desc/other_value_received', org.other_value_received) DESC,
                     -- default sort fields
                     LOWER(org.org_name),
                     org.org_id
                   ) -1 AS rec_seq
              FROM ( -- data grouped by org node
                     SELECT fd.org_id,
                            fd.org_name,
                            -- givers
                            SUM(fd.eligible_nominators) AS eligible_nominators,
                            SUM(fd.actual_nominators) AS actual_nominators,
                            DECODE(SUM(fd.eligible_nominators),
                              0, 0,
                              NVL(ROUND((SUM(fd.actual_nominators) / SUM(fd.eligible_nominators)) * 100, 0), 0)
                            ) AS pct_eligible_nominators,
                            -- receivers
                            SUM(fd.eligible_nominees) AS eligible_nominees,
                            SUM(fd.actual_nominees) AS actual_nominees,
                            DECODE(SUM(fd.eligible_nominees),
                              0, 0,
                              NVL(ROUND((SUM(fd.actual_nominees) / SUM(fd.eligible_nominees)) * 100, 0), 0)
                            ) AS pct_eligible_nominees,
                            -- nominations
                            SUM(fd.nominations_submitted) AS nominations_submitted,
                            SUM(fd.nominations_received) AS nominations_received,
                            SUM(fd.nominations_won) AS nominations_won,
                            -- receiver awards
                            SUM(fd.points_received) AS points_received,
                            SUM(fd.cash_received) AS cash_received,
                            SUM(fd.other_qty_received) AS other_qty_received,
                            SUM(fd.other_value_received) AS other_value_received
                       FROM ( -- build full data set
                              SELECT wn.org_id,
                                     wn.org_name,
                                     -- givers
                                     NVL(rec_g.eligible_count, 0) AS eligible_nominators,
                                     -- only count users matching the search node
                                     NVL(COUNT(DISTINCT DECODE(rd.node_id, rd.giver_node_id, rd.giver_user_id, NULL)), 0) AS actual_nominators,
                                     -- receivers
                                     NVL(rec_r.eligible_count, 0) AS eligible_nominees,
                                     NVL(COUNT(DISTINCT DECODE(rd.node_id, rd.recvr_node_id, rd.recvr_user_id, NULL)), 0) AS actual_nominees,
                                     -- nominations
                                     NVL(COUNT(DISTINCT DECODE(rd.node_id, rd.giver_node_id, rd.claim_id, NULL)), 0) AS nominations_submitted,
                                     NVL(COUNT(DECODE(rd.node_id, rd.recvr_node_id, rd.claim_item_id, NULL)), 0) AS nominations_received,
                                     NVL(COUNT(CASE WHEN (rd.node_id = rd.recvr_node_id AND rd.is_winner = 1 ) THEN rd.claim_item_id END), 0) AS nominations_won,
                                     -- giver awards
                                     NVL(SUM( DECODE(rd.node_id, rd.giver_node_id, rd.points_award_amt)), 0) AS points_issued,
                                     NVL(SUM( DECODE(rd.node_id, rd.giver_node_id, rd.cash_award_amt)), 0) AS cash_issued,
                                     NVL(SUM( DECODE(rd.node_id, rd.giver_node_id, rd.other_award_cnt)), 0) AS other_qty_issued,
                                     -- receiver awards
                                     NVL(SUM( DECODE(rd.node_id, rd.recvr_node_id, rd.points_award_amt)), 0) AS points_received,
                                     NVL(SUM( DECODE(rd.node_id, rd.recvr_node_id, rd.cash_award_amt)), 0) AS cash_received,
                                     NVL(SUM( DECODE(rd.node_id, rd.recvr_node_id, rd.other_award_cnt)), 0) AS other_qty_received,
                                     NVL(SUM( DECODE(rd.node_id, rd.recvr_node_id, rd.other_award_amt)), 0) AS other_value_received
                                FROM ( -- get raw data for org nodes and their child nodes
                                       SELECT wn2.org_id,
                                              wn2.node_id,
                                              dtl.claim_id,
                                              dtl.claim_item_id,
                                              dtl.giver_node_id,
                                              dtl.recvr_node_id,
                                              dtl.giver_user_id,
                                              dtl.recvr_user_id,
                                              CASE WHEN (gc_approval_status_winner
                                                          IN (dtl.r1_approval_status,
                                                              dtl.r2_approval_status,
                                                              dtl.r3_approval_status,
                                                              dtl.r4_approval_status,
                                                              dtl.r5_approval_status)) THEN 1
                                                   ELSE 0
                                              END is_winner,
                                              ROUND(dtl.cash_award_amt * v_cash_currency_value,2) cash_award_amt,--G6-1893 03/17/2017, --04/09/2019
                                             -- dtl.points_award_amt, --04/09/2019
                                             ROUND(dtl.points_award_amt * v_budget_media_value,2) as points_award_amt, --04/09/2019
                                              ( CASE WHEN (dtl.r1_award_type = gc_award_type_other AND dtl.r1_approval_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                                              + CASE WHEN (dtl.r2_award_type = gc_award_type_other AND dtl.r2_approval_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                                              + CASE WHEN (dtl.r3_award_type = gc_award_type_other AND dtl.r3_approval_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                                              + CASE WHEN (dtl.r4_award_type = gc_award_type_other AND dtl.r4_approval_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                                              + CASE WHEN (dtl.r5_award_type = gc_award_type_other AND dtl.r5_approval_status = gc_approval_status_winner) THEN 1 ELSE 0 END
                                              ) AS other_award_cnt,
                                             ROUND (dtl.other_award_amt * v_cash_currency_value,2) other_award_amt     --G6-1893 03/17/2017 --04/09/2019
                                         FROM w_node wn2,
                                              rpt_nomination_detail dtl,
                                              gtt_id_list gil_c,  -- country
                                              gtt_id_list gil_dt, -- department type
                                              gtt_id_list gil_p,  -- promotion
                                              gtt_id_list gil_pt  -- position type
                                           -- restrict by required fields
                                        WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
                                          AND (  wn2.node_id = dtl.giver_node_id
                                              OR wn2.node_id = dtl.recvr_node_id )
                                          AND dtl.claim_id IS NOT NULL
                                          --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                                           -- restrict by optional fields
                                          AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                          AND (  gil_p.ref_text_2 = gc_search_all_values
                                              OR gil_p.id = dtl.promotion_id )
                                          AND gil_c.ref_text_1 = gc_ref_text_country_id
                                          AND (  gil_c.ref_text_2 = gc_search_all_values
                                              OR gil_c.id = dtl.giver_country_id
                                              OR gil_c.id = dtl.recvr_country_id )
                                          AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                          AND (  gil_dt.ref_text_2 = gc_search_all_values
                                              OR gil_dt.ref_text_2 = dtl.giver_position_type
                                              OR gil_dt.ref_text_2 = dtl.recvr_position_type )
                                          AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                          AND (  gil_pt.ref_text_2 = gc_search_all_values
                                              OR gil_pt.ref_text_2 = dtl.giver_position_type
                                              OR gil_pt.ref_text_2 = dtl.recvr_position_type )
                                          AND (  p_in_participantStatus IS NULL
                                              OR dtl.giver_pax_status = p_in_participantStatus
                                              OR dtl.recvr_pax_status = p_in_participantStatus )
                                     ) rd,
                                     w_node wn,
                                     gtt_recog_elg_count rec_g,   -- eligible giver (nominator)
                                     gtt_recog_elg_count rec_r    -- eligible receiver (nominee)
                                  -- restrict to just the org nodes
                               WHERE wn.org_id = wn.node_id
                                  -- outer join so all queried org nodes appear in result set
                                 AND wn.org_id = rd.org_id (+)
                                 AND wn.org_id = rec_r.node_id (+)
                                 AND gc_elg_type_receiver = rec_r.record_type (+)
                                 AND wn.org_id = rec_g.node_id (+)
                                 AND gc_elg_type_giver = rec_g.record_type (+)
                               GROUP BY wn.org_id,
                                     wn.org_name,
                                     rec_g.eligible_count,
                                     rec_r.eligible_count
                            ) fd
                      GROUP BY GROUPING SETS
                            ((),
                             (fd.org_id,
                              fd.org_name)
                            )
                   ) org
          ) sd
    WHERE (  sd.rec_seq = 0   -- totals record
          OR -- reduce sequenced data set to just the output page's records
             (   sd.rec_seq > p_in_rowNumStart
             AND sd.rec_seq < p_in_rowNumEnd )
          )
    ORDER BY sd.rec_seq
      ;

   -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.eligible_nominators      AS eligible_nominators,
          rec_query.actual_nominators        AS actual_nominators,
          rec_query.pct_eligible_nominators  AS pct_eligible_nominators,
          rec_query.eligible_nominees        AS eligible_nominees,
          rec_query.actual_nominees          AS actual_nominees,
          rec_query.pct_eligible_nominees    AS pct_eligible_nominees,
          rec_query.nominations_submitted    AS nominations_submitted,
          rec_query.nominations_received     AS nominations_received,
          rec_query.nominations_won          AS nominations_won,
          rec_query.points_received          AS points_received,
          rec_query.cash_received            AS cash_received,
          rec_query.other_qty_received       AS other_qty_received,
          rec_query.other_value_received     AS other_value_received 
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS org_id,
             CAST(NULL AS VARCHAR2(255)) AS org_name,
             CAST(NULL AS NUMBER) AS eligible_nominators,
             CAST(NULL AS NUMBER) AS actual_nominators,
             CAST(NULL AS NUMBER) AS pct_eligible_nominators,
             CAST(NULL AS NUMBER) AS eligible_nominees,
             CAST(NULL AS NUMBER) AS actual_nominees,
             CAST(NULL AS NUMBER) AS pct_eligible_nominees,
             CAST(NULL AS NUMBER) AS nominations_submitted,
             CAST(NULL AS NUMBER) AS nominations_received,
             CAST(NULL AS NUMBER) AS nominations_won,
             CAST(NULL AS NUMBER) AS points_received,
             CAST(NULL AS NUMBER) AS cash_received,
             CAST(NULL AS NUMBER) AS other_qty_received,
             CAST(NULL AS NUMBER) AS other_value_received,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual
       WHERE 0=1;
   END IF;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;


END prc_summary_by_org;

-----------------------
-- Queries nominators based on input parameters and summerizes the results by org node
PROCEDURE prc_nominators_by_org
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_nominators_by_org';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_from_date          DATE;
   v_to_date            DATE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart      
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<, p_in_userId >' || p_in_userId
      || '<~';     

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),   gc_ref_text_country_id, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   WITH w_node AS
   (  -- get input parent node(s)
      SELECT h.node_id AS org_id,
             h.node_name || gc_node_name_team_suffix AS org_name,
             h.is_leaf,
             h.node_id
        FROM gtt_id_list gil,
             rpt_hierarchy h
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = h.node_id 
         AND h.is_deleted = 0
       UNION ALL
      -- get child rollup nodes of input parent node(s) 
      SELECT h.node_id AS org_id,
             h.node_name AS org_name,
             h.is_leaf,
             hr.child_node_id AS node_id
        FROM gtt_id_list gil,
             rpt_hierarchy h,
             rpt_hierarchy_rollup hr
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = h.parent_node_id 
         AND h.is_deleted = 0
         AND h.node_id = hr.node_id
   )
   -- data grouped by org node
   SELECT wn.org_id,
          wn.org_name,
          NVL(COUNT(DISTINCT rd.giver_user_id), 0) AS actual_nominators
     FROM ( -- get raw data for org nodes and their child nodes
            SELECT wn2.org_id,
                   dtl.giver_user_id
              FROM w_node wn2,
                   rpt_nomination_detail dtl,
                   gtt_id_list gil_c,  -- country
                   gtt_id_list gil_dt, -- department type
                   gtt_id_list gil_ns, -- nomination status
                   gtt_id_list gil_p,  -- promotion
                   gtt_id_list gil_pt  -- position type
                -- restrict by required fields
             WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
               AND wn2.node_id = dtl.giver_node_id
               AND dtl.claim_id IS NOT NULL
               --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                -- restrict by optional fields
               AND gil_p.ref_text_1 = gc_ref_text_promotion_id
               AND (  gil_p.ref_text_2 = gc_search_all_values
                   OR gil_p.id = dtl.promotion_id )
               AND gil_c.ref_text_1 = gc_ref_text_country_id
               AND (  gil_c.ref_text_2 = gc_search_all_values
                   OR gil_c.id = dtl.giver_country_id )
               AND gil_dt.ref_text_1 = gc_ref_text_department_type
               AND (  gil_dt.ref_text_2 = gc_search_all_values
                   OR gil_dt.ref_text_2 = dtl.giver_position_type )
               AND gil_pt.ref_text_1 = gc_ref_text_position_type
               AND (  gil_pt.ref_text_2 = gc_search_all_values
                   OR gil_pt.ref_text_2 = dtl.giver_position_type )
               AND (  p_in_participantStatus IS NULL
                   OR dtl.giver_pax_status = p_in_participantStatus 
                   OR dtl.recvr_pax_status = p_in_participantStatus )  --02/14/2017 show count based on giver/recvr status 
          ) rd,
          w_node wn
       -- restrict to just the org nodes
    WHERE wn.org_id = wn.node_id
       -- outer join so all queried org nodes appear in result set
      AND wn.org_id = rd.org_id (+)
    GROUP BY wn.org_id,
          wn.org_name
    ORDER BY NVL(COUNT(DISTINCT rd.giver_user_id), 0) DESC,
          LOWER(wn.org_name),
          wn.org_id
      ;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;

END prc_nominators_by_org;

-----------------------
-- Queries nominees based on input parameters and summerizes the results by org node
PROCEDURE prc_nominees_by_org
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_nominees_by_org';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_from_date          DATE;
   v_to_date            DATE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart      
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<, p_in_userId >' || p_in_userId
      || '<~';     

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),   gc_ref_text_country_id, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   WITH w_node AS
   (  -- get input parent node(s)
      SELECT h.node_id AS org_id,
             h.node_name || gc_node_name_team_suffix AS org_name,
             h.is_leaf,
             h.node_id
        FROM gtt_id_list gil,
             rpt_hierarchy h
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = h.node_id 
         AND h.is_deleted = 0
       UNION ALL
      -- get child rollup nodes of input parent node(s) 
      SELECT h.node_id AS org_id,
             h.node_name AS org_name,
             h.is_leaf,
             hr.child_node_id AS node_id
        FROM gtt_id_list gil,
             rpt_hierarchy h,
             rpt_hierarchy_rollup hr
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = h.parent_node_id 
         AND h.is_deleted = 0
         AND h.node_id = hr.node_id
   )
   -- data grouped by org node
   SELECT wn.org_id,
          wn.org_name,
          NVL(COUNT(DISTINCT rd.recvr_user_id), 0) AS actual_nominees
     FROM ( -- get raw data for org nodes and their child nodes
            SELECT wn2.org_id,
                   dtl.recvr_user_id
              FROM w_node wn2,
                   rpt_nomination_detail dtl,
                   gtt_id_list gil_c,  -- country
                   gtt_id_list gil_dt, -- department type
                   gtt_id_list gil_ns, -- nomination status
                   gtt_id_list gil_p,  -- promotion
                   gtt_id_list gil_pt  -- position type
                -- restrict by required fields
             WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
               AND wn2.node_id = dtl.recvr_node_id
               AND dtl.claim_id IS NOT NULL
               --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                -- restrict by optional fields
               AND gil_p.ref_text_1 = gc_ref_text_promotion_id
               AND (  gil_p.ref_text_2 = gc_search_all_values
                   OR gil_p.id = dtl.promotion_id )
               AND gil_c.ref_text_1 = gc_ref_text_country_id
               AND (  gil_c.ref_text_2 = gc_search_all_values
                   OR gil_c.id = dtl.recvr_country_id )
               AND gil_dt.ref_text_1 = gc_ref_text_department_type
               AND (  gil_dt.ref_text_2 = gc_search_all_values
                   OR gil_dt.ref_text_2 = dtl.recvr_position_type )
               AND gil_pt.ref_text_1 = gc_ref_text_position_type
               AND (  gil_pt.ref_text_2 = gc_search_all_values
                   OR gil_pt.ref_text_2 = dtl.recvr_position_type )
               AND (  p_in_participantStatus IS NULL
                   OR dtl.giver_pax_status = p_in_participantStatus --02/14/2017 show count based on giver/recvr status 
                   OR dtl.recvr_pax_status = p_in_participantStatus )
          ) rd,
          w_node wn
       -- restrict to just the org nodes
    WHERE wn.org_id = wn.node_id
       -- outer join so all queried org nodes appear in result set
      AND wn.org_id = rd.org_id (+)
    GROUP BY wn.org_id,
          wn.org_name
    ORDER BY NVL(COUNT(DISTINCT rd.recvr_user_id), 0) DESC,
          LOWER(wn.org_name),
          wn.org_id
      ;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;

END prc_nominees_by_org;

-----------------------
-- Queries nominations based on input parameters and summerizes the results by month
PROCEDURE prc_nominations_by_month
( p_in_parentNodeId        IN VARCHAR2,
  p_in_jobPosition         IN VARCHAR2,
  p_in_promotionId         IN VARCHAR2,
  p_in_departments         IN VARCHAR2,
  p_in_countryIds          IN VARCHAR2,
  p_in_participantStatus   IN VARCHAR2,
  p_in_localeDatePattern   IN VARCHAR2,
  p_in_fromDate            IN VARCHAR2,
  p_in_toDate              IN VARCHAR2,
  p_in_languageCode        IN VARCHAR2,
  p_in_rowNumStart         IN NUMBER,
  p_in_rowNumEnd           IN NUMBER,
  p_in_sortColName         IN VARCHAR2,
  p_in_sortedBy            IN VARCHAR2,
  p_in_nodeAndBelow        IN VARCHAR2,
  p_in_userId              IN NUMBER,
  p_out_return_code        OUT NUMBER,
  p_out_data               OUT SYS_REFCURSOR
) IS
   PRAGMA AUTONOMOUS_TRANSACTION;

   c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_nominations_by_month';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_from_date          DATE;
   v_to_date            DATE;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart      
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<, p_in_userId >' || p_in_userId
      || '<~';     

   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values),   gc_ref_text_country_id, 1);

   -- query data
   v_stage := 'OPEN p_out_data';
   OPEN p_out_data FOR
   WITH w_month_list AS
   (  -- build list of months within search range
      SELECT rd.month_sort,
             NVL(cms.cms_name, TO_CHAR(rd.month_sort, 'Mon')) || '-' || TO_CHAR(rd.month_sort, 'YY') AS month_name
        FROM ( -- get raw data
               SELECT ADD_MONTHS(TRUNC(v_from_date, 'mm'), LEVEL - 1) month_sort
                 FROM dual
              CONNECT BY LEVEL <= MONTHS_BETWEEN(TRUNC(v_to_date, 'mm'), TRUNC(v_from_date, 'mm')) + 1
             ) rd,
             ( -- get CMS data
               SELECT rd.cms_name,
                      rd.cms_code
                 FROM ( -- get raw data
                        SELECT ccv.asset_code,
                               ccv.cms_name,
                               ccv.cms_code,
                               -- the input language code has precedence over the default language
                               ROW_NUMBER() OVER (PARTITION BY ccv.cms_code ORDER BY DECODE(ccv.locale, p_in_languageCode, 1, 99)) AS rec_seq
                          FROM mv_cms_code_value ccv
                         WHERE ccv.asset_code = gc_cms_list_month_name
                           AND (  ccv.locale = p_in_languageCode
                               OR ccv.locale = (SELECT osp.string_val
                                                  FROM os_propertyset osp
                                                 WHERE osp.entity_name = 'default.language' )
                               )
                      ) rd
                WHERE rd.rec_seq = 1
             ) cms
       WHERE TO_CHAR(rd.month_sort, 'mon') = cms.cms_code (+)
   )
   , w_node AS
   (  -- get query node(s)
      SELECT DISTINCT
             DECODE(TO_NUMBER(p_in_nodeAndBelow), 0, hr.node_id, 1, hr.child_node_id) AS node_id
        FROM gtt_id_list gil,
             rpt_hierarchy_rollup hr
       WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
         AND gil.id = hr.node_id
   )
   SELECT wml.month_name,
          NVL(rd.actual_nominators, 0) AS actual_nominators,
          NVL(rd.actual_nominees, 0) AS actual_nominees
     FROM ( -- get raw data for query nodes
            SELECT TRUNC(dtl.date_submitted, 'MM') AS month_sort,
                   -- only count users matching the search node
                   COUNT(DISTINCT DECODE(wn.node_id, dtl.giver_node_id, dtl.giver_user_id, NULL)) AS actual_nominators,
                   COUNT(DISTINCT DECODE(wn.node_id, dtl.recvr_node_id, dtl.recvr_user_id, NULL)) AS actual_nominees
              FROM rpt_nomination_detail dtl,
                   w_node wn,
                   gtt_id_list gil_c,  -- country
                   gtt_id_list gil_dt, -- department type
                   gtt_id_list gil_p,  -- promotion
                   gtt_id_list gil_pt  -- position type
                -- restrict by required fields
             WHERE dtl.date_submitted BETWEEN v_from_date AND v_to_date
               AND (  wn.node_id = dtl.giver_node_id
                   OR wn.node_id = dtl.recvr_node_id )
               AND dtl.claim_id IS NOT NULL
               --AND dtl.promotion_status = gc_promotion_status_active --02/22/2017
                -- restrict by optional fields
               AND gil_p.ref_text_1 = gc_ref_text_promotion_id
               AND (  gil_p.ref_text_2 = gc_search_all_values
                   OR gil_p.id = dtl.promotion_id )
               AND gil_c.ref_text_1 = gc_ref_text_country_id
               AND (  gil_c.ref_text_2 = gc_search_all_values
                   OR gil_c.id = dtl.giver_country_id
                   OR gil_c.id = dtl.recvr_country_id )
               AND gil_dt.ref_text_1 = gc_ref_text_department_type
               AND (  gil_dt.ref_text_2 = gc_search_all_values
                   OR gil_dt.ref_text_2 = dtl.giver_position_type
                   OR gil_dt.ref_text_2 = dtl.recvr_position_type )
               AND gil_pt.ref_text_1 = gc_ref_text_position_type
               AND (  gil_pt.ref_text_2 = gc_search_all_values
                   OR gil_pt.ref_text_2 = dtl.giver_position_type
                   OR gil_pt.ref_text_2 = dtl.recvr_position_type )
               AND (  p_in_participantStatus IS NULL
                   OR dtl.giver_pax_status = p_in_participantStatus
                   OR dtl.recvr_pax_status = p_in_participantStatus )
             GROUP BY TRUNC(dtl.date_submitted, 'MM')
          ) rd,
          w_month_list wml
    WHERE wml.month_sort = rd.month_sort (+)
    ORDER BY wml.month_sort
      ;

   -- successful
   p_out_return_code := gc_return_code_success;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, gc_release_level, gc_error, v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := gc_return_code_failure;
      OPEN p_out_data FOR SELECT NULL FROM dual;

END prc_nominations_by_month;


END pkg_query_nomination;
/

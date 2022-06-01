CREATE OR REPLACE PACKAGE pkg_query_recog_recipients
IS
    PROCEDURE prc_getRecogsReceivedByPax (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getPointsReceivedByPax (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getRecogsReceivedByPromo (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getPointsReceivedByPromo (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getRecogsReceivedMetrics (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getParticipationRateByPax (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);

    PROCEDURE prc_getRecogsReceivedScatter (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR);
        
    PROCEDURE prc_getSummaryReceivedByPax (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR);

    PROCEDURE prc_getActivityReceivedByPax (        
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR);
END pkg_query_recog_recipients;
/

CREATE OR REPLACE PACKAGE BODY pkg_query_recog_recipients
IS
-- package constants

gc_null_id                       CONSTANT NUMBER(18) := pkg_const.gc_null_id;
gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
gc_pax_status_active             CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_active;
gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;
gc_elg_type_given                CONSTANT rpt_pax_promo_eligibility.giver_recvr_type%TYPE := pkg_const.gc_elg_type_receiver;
gc_promotion_type_recognition    CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_recognition;
gc_cms_list_position_type        CONSTANT cms_asset.code%TYPE := pkg_const.gc_cms_list_position_type;
gc_cms_list_department_type      CONSTANT cms_asset.code%TYPE := pkg_const.gc_cms_list_department_type;

gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
gc_ref_text_country_id           CONSTANT gtt_id_list.ref_text_1%TYPE := 'country_id';

-----------------------
-- Private package processes
-----------------------
-- Stages the eligible count table based on whether input promotions have an all pax or specified audience.
-- Note: Process assumes the query parameters have already been staged to the temp ID list table.
PROCEDURE p_stg_receiver_eligible_counts
( p_in_given_received_type    IN VARCHAR2,
  p_in_promotion_type      IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_user_status_desc    IN VARCHAR2,
  p_in_promotion_status    IN VARCHAR2
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'p_stg_receiver_eligible_counts';
   v_stage              execution_log.text_line%TYPE;
   v_parm_list          execution_log.text_line%TYPE;
   v_is_all_audience    NUMBER;
BEGIN
   -- initialize variables
   v_stage := 'initialize variables';
   v_parm_list := '~Parms'
      ||  ': p_in_given_received_type >' || p_in_given_received_type
      || '<, p_in_promotion_type >' || p_in_promotion_type
      || '<, p_in_promotion_id_list >' || p_in_promotion_id_list
      || '<, p_in_user_status_desc >' || p_in_user_status_desc
      || '<, p_in_promotion_status >' || p_in_promotion_status
      || '<~';     

   v_stage := 'check promo audience';
   v_is_all_audience := fnc_check_promo_aud(p_in_given_received_type, p_in_promotion_type, p_in_promotion_id_list);
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
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt,  -- position type
                gtt_id_list gil_c   -- country
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = h.parent_node_id 
            AND h.node_id = pe.node_id
            AND pe.giver_recvr_type = p_in_given_received_type
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id
            AND (  gil_c.ref_text_2 = gc_search_all_values
                OR gil_c.id = pe.country_id )
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active 
          GROUP BY pe.node_id,
                pe.giver_recvr_type
          UNION ALL
         -- sum counts for parent team(s)
         SELECT pe.node_id,
                pe.giver_recvr_type AS record_type,
                SUM(pe.elig_count) AS eligible_count
           FROM rpt_pax_promo_elig_allaud_team pe,
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt,  -- position type
                gtt_id_list gil_c    -- country
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = pe.node_id
            AND pe.giver_recvr_type = p_in_given_received_type
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
            AND gil_c.ref_text_1 = gc_ref_text_country_id
            AND (  gil_c.ref_text_2 = gc_search_all_values
                OR gil_c.id = pe.country_id )
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active 
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
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt,  -- position type
                gtt_id_list gil_c    -- country
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = h.parent_node_id 
            AND h.node_id = pe.node_id
            AND pe.giver_recvr_type = p_in_given_received_type
            AND pe.promotion_type = gc_promotion_type_recognition
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
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active 
          GROUP BY pe.node_id,
                pe.giver_recvr_type
          UNION ALL
         -- sum counts for parent team(s)
         SELECT pe.node_id,
                pe.giver_recvr_type AS record_type,
                SUM(pe.elig_count) AS eligible_count
           FROM rpt_pax_promo_elig_speaud_team pe,
                gtt_id_list gil_dt, -- department type
                gtt_id_list gil_pn, -- parent node
                gtt_id_list gil_pt,  -- position type
                gtt_id_list gil_c   -- country
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = pe.node_id
            AND pe.giver_recvr_type = p_in_given_received_type
            AND pe.promotion_type = gc_promotion_type_recognition
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
            AND NVL(p_in_user_status_desc, gc_pax_status_active) = gc_pax_status_active 
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
                         NVL(rd.giver_recvr_type, p_in_given_received_type) AS record_type,
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
                                  gtt_id_list gil_dt, -- department type
                                  gtt_id_list gil_pt, -- position type
                                  gtt_id_list gil_p,   -- promotion
                                  gtt_id_list gil_c -- country
                            WHERE wn.node_id = ppe.node_id
                              AND ppe.promotion_id = p.promotion_id
                              AND ppe.participant_id = paxe.user_id (+)
                              AND ppe.participant_id = ua.user_id (+)
                              AND ua.is_primary (+) = 1
                              AND ppe.giver_recvr_type = p_in_given_received_type
                              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                              AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = ppe.promotion_id )
                              AND p.promotion_type = gc_promotion_type_recognition
                              AND p.promotion_status = NVL(p_in_promotion_status, p.promotion_status)
                              AND gil_dt.ref_text_1 = gc_ref_text_department_type
                              AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = paxe.department_type )
                              AND gil_pt.ref_text_1 = gc_ref_text_position_type
                              AND (  gil_pt.ref_text_2 = gc_search_all_values
                                  OR gil_pt.ref_text_2 = paxe.position_type )
                              AND gil_c.ref_text_1 = gc_ref_text_country_id
                              AND (  gil_c.ref_text_2 = gc_search_all_values
                                    OR gil_c.id = ua.country_id )
                              AND (p_in_user_status_desc IS NULL
                                  OR p_in_user_status_desc
                                     = DECODE(paxe.termination_date, NULL, gc_pax_status_active) )
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
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parm_list || SQLCODE || ':' || SQLERRM, NULL);
END p_stg_receiver_eligible_counts;

    /* CHART QUERIES */
PROCEDURE prc_getRecogsReceivedByPax (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getRecogsReceivedByPax
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes     
   Gorantla            04/09/2018       G6-3993/Bug 75760 - Report charts errors with long script running error due to many recognitions
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getRecogsReceivedByPax' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   --OPEN p_out_data FOR
      OPEN p_out_data FOR
   SELECT d.receiver_name,             --04/09/2018
           d.recognitions_cnt       --04/09/2018
   FROM (                           --04/09/2018
    SELECT rownum as row_num,       --04/09/2018
           receiver_name,              --04/09/2018
           recognitions_cnt         --04/09/2018
   FROM  ( 
   SELECT receiver_name,
          recognitions_cnt
     FROM ( --get sum data
           SELECT dtl.recvr_full_name as receiver_name,
                  COUNT(dtl.recvr_activity_id) AS recognitions_cnt
             FROM rpt_recognition_detail dtl,
                  promotion p,
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt,  -- position type
                  gtt_id_list gil_c     --country
               -- restrict by required fields
            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
              AND dtl.promotion_id = p.promotion_id
              AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status)  
               -- restrict by optional fields
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = dtl.recvr_department )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = dtl.recvr_job_position )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = dtl.recvr_country_id )
              AND (  p_in_participantStatus IS NULL
                  OR dtl.recvr_pax_status = p_in_participantStatus )
              AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = dtl.recvr_node_id 
                           )
            GROUP BY dtl.recvr_full_name
           ) 
    ORDER BY recognitions_cnt DESC)                           --04/09/2018 
    ) d WHERE d.row_num < 21 ;      --04/09/2018

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getRecogsReceivedByPax;

PROCEDURE prc_getPointsReceivedByPax (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getPointsReceivedByPax
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
   Gorantla            04/09/2018       G6-3993/Bug 75760 - Report charts errors with long script running error due to many recognitions
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getPointsReceivedByPax' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   --OPEN p_out_data FOR
      OPEN p_out_data FOR
   SELECT d.receiver_name,             --04/09/2018
           d.recognition_points_cnt       --04/09/2018
   FROM (                           --04/09/2018 
    SELECT rownum as row_num,       --04/09/2018
           receiver_name,              --04/09/2018
           recognition_points_cnt         --04/09/2018
   FROM  (
   SELECT receiver_name,
          recognition_points_cnt
     FROM ( --get sum data
           SELECT dtl.recvr_full_name AS receiver_name,
                  NVL (SUM (dtl.award_amt), 0) AS recognition_points_cnt
             FROM rpt_recognition_detail dtl,
                  promotion p,
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt,  -- position type
                  gtt_id_list gil_c    -- country
               -- restrict by required fields
            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
              AND dtl.promotion_id = p.promotion_id
              AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status)  
               -- restrict by optional fields
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = dtl.recvr_department )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = dtl.recvr_job_position )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = dtl.recvr_country_id )
              AND (  p_in_participantStatus IS NULL
                  OR dtl.recvr_pax_status = p_in_participantStatus )
              AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = dtl.recvr_node_id 
                           )
            GROUP BY dtl.recvr_full_name
           ) 
    ORDER BY recognition_points_cnt DESC
        )                           --04/09/2018
    ) d WHERE d.row_num < 21 ;      --04/09/2018

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getPointsReceivedByPax;

    PROCEDURE prc_getRecogsReceivedByPromo (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getRecogsReceivedByPromo
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getRecogsReceivedByPromo' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
   SELECT promotion_name,
          recognitions_cnt
     FROM ( --get sum data
           SELECT dtl.promotion_name,
                  COUNT(dtl.recvr_activity_id) AS recognitions_cnt
             FROM rpt_recognition_detail dtl,
                  promotion p,
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt,  -- position type
                  gtt_id_list gil_c    -- country
               -- restrict by required fields
            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
              AND dtl.promotion_id = p.promotion_id
              AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status)  
               -- restrict by optional fields
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = dtl.recvr_department )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = dtl.recvr_job_position )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = dtl.recvr_country_id )
              AND (  p_in_participantStatus IS NULL
                  OR dtl.recvr_pax_status = p_in_participantStatus )
              AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = dtl.recvr_node_id 
                           )
            GROUP BY dtl.promotion_name
           ) 
    ORDER BY recognitions_cnt DESC;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getRecogsReceivedByPromo;

PROCEDURE prc_getPointsReceivedByPromo (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getPointsReceivedByPromo
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getPointsReceivedByPromo' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
   SELECT promotion_name,
          recognition_points_cnt
     FROM ( --get sum data
           SELECT dtl.promotion_name,
                  NVL (SUM (dtl.award_amt), 0) AS recognition_points_cnt
             FROM rpt_recognition_detail dtl,
                  promotion p,
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt,  -- position type
                  gtt_id_list gil_c    --country
               -- restrict by required fields
            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
              AND dtl.promotion_id = p.promotion_id
              AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status)  
               -- restrict by optional fields
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = dtl.recvr_department )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = dtl.recvr_job_position )
              AND (  p_in_participantStatus IS NULL
                  OR dtl.recvr_pax_status = p_in_participantStatus )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = dtl.recvr_country_id )
              AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = dtl.recvr_node_id 
                           )
            GROUP BY dtl.promotion_name 
           ) 
    ORDER BY recognition_points_cnt DESC;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getPointsReceivedByPromo; 

PROCEDURE prc_getRecogsReceivedMetrics 
       (p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getRecogsReceivedMetrics
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getRecogsReceivedMetrics' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
      -- stage eligible counts
   v_stage := 'stage eligible counts';
   DELETE gtt_recog_elg_count;
   p_stg_receiver_eligible_counts(gc_elg_type_given, gc_promotion_type_recognition, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);
                            
   OPEN p_out_data FOR
   SELECT MAX(rd.rec_rank) top_value, 
               COUNT(rd.recvr_user_id) total, 
               ROUND(COUNT(rd.recvr_user_id) / MIN(rec_g.eligible_count), 2) overall_org_avg 
     FROM ( --get sum data
           SELECT RANK() OVER (PARTITION BY recvr_user_id  ORDER BY rpt_recognition_detail_id ASC) AS rec_rank, 
                  recvr_user_id
             FROM rpt_recognition_detail dtl,
                  promotion p,
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt,  -- position type
                  gtt_id_list gil_c    --country
               -- restrict by required fields
            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
              AND dtl.promotion_id = p.promotion_id
              AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status)  
               -- restrict by optional fields
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = dtl.recvr_department )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = dtl.recvr_job_position )
              AND (  p_in_participantStatus IS NULL
                  OR dtl.recvr_pax_status = p_in_participantStatus )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = dtl.recvr_country_id )
              AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = dtl.recvr_node_id 
                           ) 
           ) rd,
           ( SELECT DECODE(SUM(eligible_count), 0, 1, SUM(eligible_count))  AS eligible_count 
               FROM gtt_recog_elg_count) rec_g;    -- eligible receiver 

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getRecogsReceivedMetrics;     

PROCEDURE prc_getParticipationRateByPax (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS    
/******************************************************************************
  NAME:       prc_getParticipationRateByPax
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
 ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getParticipationRateByPax' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_data_sort           VARCHAR2(50);  
   
BEGIN

   v_stage := 'initialize variables';
    v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);

   -- stage eligible counts
   v_stage := 'stage eligible counts';
   DELETE gtt_recog_elg_count;
   p_stg_receiver_eligible_counts(gc_elg_type_given, gc_promotion_type_recognition, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);
                     
   -- stage actual counts
   v_stage := 'stage actual counts';
   DELETE gtt_recog_actual_count;
   INSERT INTO gtt_recog_actual_count
   WITH w_node AS
       (  -- get input parent node(s)
          SELECT h.node_id AS org_id,
                 h.node_name  AS org_name,
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
     -- build full data set
      SELECT COUNT(DISTINCT rd.recvr_user_id) actual_cnt,
             wn.org_id as node_id
        FROM ( -- get raw data for org nodes and their child nodes
               SELECT wn2.org_id,
                      wn2.node_id,
                      dtl.recvr_activity_id,
                      dtl.recvr_user_id,
                      dtl.recvr_sweepstakes_won,
                      dtl.recvr_plateau_earned,
                      award_amt
                 FROM w_node wn2,
                      rpt_recognition_detail dtl,
                      promotion p,
                      gtt_id_list gil_dt, -- department type
                      gtt_id_list gil_p,  -- promotion
                      gtt_id_list gil_pt,  -- position type
                      gtt_id_list gil_c
                   -- restrict by required fields
                WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                  AND wn2.node_id = dtl.recvr_node_id 
                  AND dtl.promotion_id = p.promotion_id
                  AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status) 
                   -- restrict by optional fields
                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                  AND (  gil_p.ref_text_2 = gc_search_all_values
                      OR gil_p.id = dtl.promotion_id )
                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                      OR gil_dt.ref_text_2 = dtl.recvr_department )
                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                      OR gil_pt.ref_text_2 = dtl.recvr_job_position )
                  AND gil_c.ref_text_1 = gc_ref_text_country_id
                  AND (  gil_c.ref_text_2 = gc_search_all_values
                      OR gil_c.id = dtl.recvr_country_id )
                  AND (  p_in_participantStatus IS NULL
                      OR dtl.recvr_pax_status = p_in_participantStatus )
             ) rd,
             w_node wn
          -- restrict to just the org nodes
       WHERE wn.org_id = wn.node_id
          -- outer join so all queried org nodes appear in result set
         AND wn.org_id = rd.org_id (+)
       GROUP BY wn.org_id;
                                   
               
   OPEN p_out_data FOR
      SELECT CASE WHEN eligible_pct > 100 THEN 100 ELSE eligible_pct END AS eligible_pct,
             CASE WHEN eligible_pct > 100 THEN 0 
                  WHEN not_eligible_pct > 100 THEN 100 
             ELSE not_eligible_pct END AS not_eligible_pct
        FROM ( SELECT ROUND( NVL ( DECODE (SUM (eligible_count), 0, 0, (SUM (actual_count) / SUM (eligible_count)) * 100), 0), 2) eligible_pct,
                      ROUND( NVL ( DECODE (SUM (eligible_count), 0, 0, (((SUM(eligible_count)  -SUM(actual_count))/SUM(eligible_count))*100)),0),2) not_eligible_pct
                 FROM (SELECT *
                         FROM gtt_recog_elg_count elg,
                              gtt_recog_actual_count act
                        WHERE elg.node_id = act.node_id
                      )
             );
               
   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getParticipationRateByPax;

PROCEDURE prc_getRecogsReceivedScatter (        
        p_in_giverReceiver    IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getRecogsReceivedScatter
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getRecogsReceivedScatter' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
   OPEN p_out_data FOR
   SELECT receiver_name,
          recognitions_cnt,
          days_since_last_rec
     FROM ( --get sum data
           SELECT dtl.recvr_full_name AS receiver_name,
                  COUNT (dtl.recvr_activity_id) AS recognitions_cnt,
                  (TRUNC (SYSDATE) - MAX (TRUNC (dtl.trans_date)))AS days_since_last_rec
             FROM rpt_recognition_detail dtl,
                  promotion p,
                  gtt_id_list gil_dt, -- department type
                  gtt_id_list gil_p,  -- promotion
                  gtt_id_list gil_pt,  -- position type
                  gtt_id_list gil_c    --country
               -- restrict by required fields
            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
              AND dtl.promotion_id = p.promotion_id
              AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status)  
               -- restrict by optional fields
              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
              AND (  gil_p.ref_text_2 = gc_search_all_values
                  OR gil_p.id = dtl.promotion_id )
              AND gil_dt.ref_text_1 = gc_ref_text_department_type
              AND (  gil_dt.ref_text_2 = gc_search_all_values
                  OR gil_dt.ref_text_2 = dtl.recvr_department )
              AND gil_pt.ref_text_1 = gc_ref_text_position_type
              AND (  gil_pt.ref_text_2 = gc_search_all_values
                  OR gil_pt.ref_text_2 = dtl.recvr_job_position )
              AND (  p_in_participantStatus IS NULL
                  OR dtl.recvr_pax_status = p_in_participantStatus )
              AND gil_c.ref_text_1 = gc_ref_text_country_id
              AND (  gil_c.ref_text_2 = gc_search_all_values
                  OR gil_c.id = dtl.recvr_country_id )
              AND EXISTS (SELECT 1
                            FROM gtt_id_list gil,
                                 rpt_hierarchy h,
                                 rpt_hierarchy_rollup hr
                           WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                             AND gil.id = h.node_id 
                             AND h.is_deleted = 0
                             AND h.node_id = hr.node_id
                             AND hr.child_node_id = dtl.recvr_node_id 
                           )
            GROUP BY dtl.recvr_full_name 
           ) 
    ORDER BY recognitions_cnt DESC;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_getRecogsReceivedScatter;        

PROCEDURE prc_getSummaryReceivedByPax (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR)
    IS
    
 /******************************************************************************
  NAME:       prc_getSummaryReceivedByPax
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
 ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getSummaryReceivedByPax' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_default_locale      os_propertyset.string_val%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_fetch_count         INTEGER;
  v_data_sort           VARCHAR2(50);

   CURSOR cur_query_ref IS
    SELECT CAST(NULL AS NUMBER) AS recvr_user_id,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_name,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_country,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_node,
             CAST(NULL AS NUMBER) AS org_id,
             CAST(NULL AS VARCHAR2(4000)) AS recvr_department,
             CAST(NULL AS VARCHAR2(4000)) AS recvr_job_position,
             CAST(NULL AS NUMBER) AS recog_count,
             CAST(NULL AS NUMBER) AS recog_points,
             CAST(NULL AS NUMBER) AS plateau_earned_count,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;

   rec_query      cur_query_ref%ROWTYPE;    
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
   EXCEPTION
     WHEN OTHERS THEN
       v_default_locale := 'en_US';
   END;
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
   v_stage:= 'Insert temp_table_session';
  DELETE temp_table_session;
  INSERT INTO temp_table_session
   SELECT asset_code,cms_name,cms_code
     FROM (SELECT asset_code,cms_value as cms_name,key as cms_code ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn
               FROM mv_cms_asset_value 
              WHERE key = 'COUNTRY_NAME'
                AND locale IN (v_default_locale, p_in_languageCode)
              UNION ALL
             SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn 
               FROM mv_cms_code_value 
              WHERE asset_code IN ( 'picklist.positiontype.items','picklist.department.type.items')
                AND locale IN (v_default_locale, p_in_languageCode)
                AND cms_status = 'true' )
     WHERE rn = 1;
    
   OPEN p_out_data FOR 
     SELECT sd.*
         FROM ( -- sequence the data
                SELECT org.*,
                       COUNT(DISTINCT org.recvr_user_id) OVER() AS total_records,
                       -- calc record sort order
                       ROW_NUMBER() OVER (ORDER BY
                         -- sort totals record first
                         DECODE(org.recvr_user_id, NULL, 0, 99),
                         -- ascending sorts
                         DECODE(v_data_sort, 'recvr_user_id', org.recvr_user_id),
                         DECODE(v_data_sort, 'receiver_name', LOWER(org.receiver_name)),
                         DECODE(v_data_sort, 'receiver_country', org.receiver_country),
                         DECODE(v_data_sort, 'receiver_node', org.receiver_node),
                         DECODE(v_data_sort, 'recvr_department', org.recvr_department),
                         DECODE(v_data_sort, 'recvr_job_position', org.recvr_job_position),
                         DECODE(v_data_sort, 'recog_count', org.recog_count),
                         DECODE(v_data_sort, 'recog_points', org.recog_points),
                         DECODE(v_data_sort, 'plateau_earned_count', org.plateau_earned_count),
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/recvr_user_id', org.recvr_user_id) DESC,
                         DECODE(v_data_sort, 'desc/receiver_name', LOWER(org.receiver_name)) DESC,
                         DECODE(v_data_sort, 'desc/receiver_country', org.receiver_country) DESC,
                         DECODE(v_data_sort, 'desc/receiver_node', org.receiver_node) DESC,
                         DECODE(v_data_sort, 'desc/recvr_department', org.recvr_department) DESC,
                         DECODE(v_data_sort, 'desc/recvr_job_position', org.recvr_job_position) DESC,
                         DECODE(v_data_sort, 'desc/recog_count', org.recog_count) DESC,
                         DECODE(v_data_sort, 'desc/recog_points', org.recog_points) DESC,
                         DECODE(v_data_sort, 'desc/plateau_earned_count', org.plateau_earned_count) DESC,
                         -- default sort fields
                         LOWER(org.receiver_name),
                         org.recvr_user_id
                       ) -1 AS rec_seq
                  FROM ( -- data grouped by org node
                      SELECT recvr_user_id,
                             receiver_name,
                             receiver_country,
                             receiver_node,
                             organization_id,
                             recvr_department,
                             recvr_job_position,
                             NVL(SUM(recognitions_cnt),0)       AS recog_count,
                             NVL(SUM(recognition_points),0)     AS recog_points,
                             NVL(SUM(plateau_earned_count),0)   AS plateau_earned_count
                        FROM (  SELECT dtl.recvr_user_id        AS recvr_user_id,
                                       dtl.recvr_full_name      AS receiver_name,
                                       dtl.recvr_node_name      AS receiver_node,
                                       dtl.recvr_node_id        AS organization_id, 
                                       NVL(tts_c.cms_name, ' ') AS receiver_country,
                                       NVL(tts_p.cms_name, ' ') AS recvr_job_position,
                                       NVL(tts_d.cms_name, ' ') AS recvr_department,
                                       COUNT(dtl.recvr_activity_id) AS recognitions_cnt,
                                       SUM(dtl.award_amt)           AS recognition_points,
                                       SUM(dtl.recvr_plateau_earned) AS plateau_earned_count
                                  FROM rpt_recognition_detail dtl,
                                       promotion p,
                                       country c,
                                       temp_table_session tts_c, -- cm country name
                                       temp_table_session tts_p, -- cm department type
                                       temp_table_session tts_d, -- cm position type
                                       gtt_id_list gil_dt, -- department type
                                       gtt_id_list gil_p,  -- promotion
                                       gtt_id_list gil_pt,  -- position type
                                       gtt_id_list gil_c    -- country
                                      -- restrict by required fields
                                WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                                  AND dtl.promotion_id = p.promotion_id
                                  AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status) 
                                  AND dtl.recvr_country_id = c.country_id(+)
                                  AND c.cm_asset_code = tts_c.asset_code(+)
                                  AND c.name_cm_key = tts_c.cms_code (+)
                                  AND gc_cms_list_position_type = tts_p.asset_code(+)
                                  AND dtl.recvr_job_position = tts_p.cms_code (+)
                                  AND gc_cms_list_department_type = tts_d.asset_code(+)
                                  AND dtl.recvr_department = tts_d.cms_code (+)                           
                                   -- restrict by optional fields
                                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                  AND (  gil_p.ref_text_2 = gc_search_all_values
                                      OR gil_p.id = dtl.promotion_id )
                                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                                      OR gil_dt.ref_text_2 = dtl.recvr_department )
                                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                                      OR gil_pt.ref_text_2 = dtl.recvr_job_position )
                                  AND gil_c.ref_text_1 = gc_ref_text_country_id
                                  AND (  gil_c.ref_text_2 = gc_search_all_values
                                      OR gil_c.id = dtl.recvr_country_id )
                                  AND (  p_in_participantStatus IS NULL
                                      OR dtl.recvr_pax_status = p_in_participantStatus )
                                  AND EXISTS (SELECT 1
                                                FROM gtt_id_list gil,
                                                     rpt_hierarchy h,
                                                     rpt_hierarchy_rollup hr
                                               WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                                                 AND gil.id = h.node_id 
                                                 AND h.is_deleted = 0
                                                 AND h.node_id = hr.node_id
                                                 AND hr.child_node_id = dtl.recvr_node_id
                                              )
                                GROUP BY dtl.recvr_user_id,
                                         dtl.recvr_node_id,   
                                         dtl.recvr_full_name,
                                         dtl.recvr_node_name,
                                         dtl.recvr_user_id,
                                         NVL(tts_c.cms_name, ' '),
                                         NVL(tts_p.cms_name, ' '),
                                         NVL(tts_d.cms_name, ' ')
                                                 
                             )
                        GROUP BY GROUPING SETS ((),(recvr_user_id,
                                                    receiver_name,
                                                    receiver_country,
                                                    receiver_node,
                                                    organization_id,
                                                    recvr_department,
                                                    recvr_job_position)) 
                       ) org  
               ) sd
        WHERE (  sd.rec_seq = 0   -- totals record
              OR -- reduce sequenced data set to just the output page's records
                 (   sd.rec_seq > p_in_rowNumStart
                 AND sd.rec_seq < p_in_rowNumEnd )
              )
        ORDER BY sd.rec_seq;
                              
   -- query totals data   
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.recog_count            AS recog_count,
          rec_query.recog_points           AS recog_points,
          rec_query.plateau_earned_count   AS plateau_earned_count
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS recvr_user_id,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_name,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_country,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_node,
             CAST(NULL AS NUMBER) AS org_id,
             CAST(NULL AS VARCHAR2(4000)) AS recvr_department,
             CAST(NULL AS VARCHAR2(4000)) AS recvr_job_position,
             CAST(NULL AS NUMBER) AS recog_count,
             CAST(NULL AS NUMBER) AS recog_points,
             CAST(NULL AS NUMBER) AS plateau_earned_count,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual
       WHERE 0=1;
   END IF;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;
END prc_getSummaryReceivedByPax;

PROCEDURE prc_getActivityReceivedByPax (
        p_in_giverReceiver       IN     VARCHAR,
        p_in_userId              IN     NUMBER,
        p_in_languageCode        IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_countryIds          IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_localeDatePattern   IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortedOn            IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_out_return_code        OUT    NUMBER,
        p_out_data               OUT    SYS_REFCURSOR,
        p_out_totals_data        OUT    SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getActivityReceivedByPax
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
 ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_getActivityReceivedByPax' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_default_locale      os_propertyset.string_val%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_fetch_count         INTEGER;
  v_data_sort           VARCHAR2(50);

   CURSOR cur_query_ref IS
    SELECT CAST(NULL AS DATE) AS date_approved,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_name,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_country,
             CAST(NULL AS VARCHAR2(4000)) AS recvr_node_name,
             CAST(NULL AS VARCHAR2(4000)) AS department,
             CAST(NULL AS VARCHAR2(4000)) AS job_position,
             CAST(NULL AS VARCHAR2(4000)) AS sender_name,
             CAST(NULL AS VARCHAR2(4000)) AS promotion_name,
             CAST(NULL AS NUMBER) AS recognition_points,
             CAST(NULL AS NUMBER) AS plateau_earned_count,
             CAST(NULL AS NUMBER) AS claim_id,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;

   rec_query      cur_query_ref%ROWTYPE;    
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_promotionId >' || p_in_promotionId
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_countryIds >' || p_in_countryIds
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_parentNodeId >' || p_in_parentNodeId  
      || '<, p_in_sortedOn >' || p_in_sortedOn              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
   v_stage := 'convert input strings to date';
   v_from_date := TO_DATE(p_in_fromDate, p_in_localeDatePattern);
   v_to_date   := TO_DATE(p_in_toDate,   p_in_localeDatePattern);
   
   BEGIN
    SELECT string_val 
      INTO v_default_locale
      FROM os_propertyset
     WHERE entity_name = 'default.language';
   EXCEPTION
     WHEN OTHERS THEN
       v_default_locale := 'en_US';
   END;
   
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values),  gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values),  gc_ref_text_promotion_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_countryIds, gc_search_all_values), gc_ref_text_country_id, 1);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
   v_stage:= 'Insert temp_table_session';
  DELETE temp_table_session;
  INSERT INTO temp_table_session
   SELECT asset_code,cms_name,cms_code
     FROM (SELECT asset_code,cms_value as cms_name,key as cms_code ,  RANK() OVER(PARTITION BY asset_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn
               FROM mv_cms_asset_value 
              WHERE key = 'COUNTRY_NAME'
                AND locale IN (v_default_locale, p_in_languageCode)
              UNION ALL
             SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn 
               FROM mv_cms_code_value 
              WHERE asset_code IN ( 'picklist.positiontype.items','picklist.department.type.items')
                AND locale IN (v_default_locale, p_in_languageCode)
                AND cms_status = 'true' )
     WHERE rn = 1;
    
   OPEN p_out_data FOR 
     SELECT sd.*
         FROM ( -- sequence the data
                SELECT TRUNC(org.date_approved) AS date_approved,
                       org.receiver_name,
                       org.receiver_country ,
                       org.recvr_node_name,
                       org.department,
                       org.job_position,
                       org.sender_name,
                       org.promotion_name,
                       org.recognition_points,
                       org.plateau_earned_count,
                       org.claim_id,
                       COUNT( org.receiver_name) OVER() AS total_records,
                       -- calc record sort order
                       ROW_NUMBER() OVER (ORDER BY
                         -- sort totals record first
                         DECODE(org.receiver_name, NULL, 0, 99),
                         -- ascending sorts
                         DECODE(v_data_sort, 'date_approved', org.date_approved),
                         DECODE(v_data_sort, 'receiver_name', LOWER(org.receiver_name)),
                         DECODE(v_data_sort, 'receiver_country', org.receiver_country),
                         DECODE(v_data_sort, 'recvr_node_name', org.recvr_node_name),
                         DECODE(v_data_sort, 'department', org.department),
                         DECODE(v_data_sort, 'job_position', org.job_position),
                         DECODE(v_data_sort, 'sender_name', org.sender_name),
                         DECODE(v_data_sort, 'promotion_name', org.promotion_name),
                         DECODE(v_data_sort, 'recognition_points', org.recognition_points),
                         DECODE(v_data_sort, 'plateau_earned_count', org.plateau_earned_count),
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/date_approved', org.date_approved) DESC,
                         DECODE(v_data_sort, 'desc/receiver_name', LOWER(org.receiver_name)) DESC,
                         DECODE(v_data_sort, 'desc/receiver_country', org.receiver_country) DESC,
                         DECODE(v_data_sort, 'desc/recvr_node_name', org.recvr_node_name) DESC,
                         DECODE(v_data_sort, 'desc/department', org.department) DESC,
                         DECODE(v_data_sort, 'desc/job_position', org.job_position) DESC,
                         DECODE(v_data_sort, 'desc/sender_name', org.sender_name) DESC,
                         DECODE(v_data_sort, 'desc/promotion_name', org.promotion_name) DESC,
                         DECODE(v_data_sort, 'desc/recognition_points', org.recognition_points) DESC,
                         DECODE(v_data_sort, 'desc/plateau_earned_count', org.plateau_earned_count) DESC,
                         -- default sort fields
                         LOWER(org.receiver_name)
                       ) -1 AS rec_seq
                  FROM ( -- data grouped by org node
                      SELECT claim_id,
                             date_approved,
                             receiver_name             ,
                             receiver_country          ,
                             recvr_node_name        ,
                             department             ,
                             job_position           ,
                             sender_name            ,
                             promotion_name         ,
                             NVL(SUM(recognition_points),0)     AS recognition_points,
                             NVL(SUM(plateau_earned_count),0)   AS plateau_earned_count
                        FROM (  SELECT dtl.claim_id,
                                       dtl.date_approved AS date_approved,
                                       dtl.recvr_full_name      AS receiver_name,
                                       NVL(tts_c.cms_name, ' ') AS receiver_country,
                                       dtl.recvr_node_name      AS recvr_node_name,
                                       NVL(tts_d.cms_name, ' ') AS department,
                                       NVL(tts_p.cms_name, ' ') AS job_position,
                                       dtl.giver_full_name AS sender_name,
                                       p.promotion_name,
                                       SUM(dtl.award_amt)       AS recognition_points,
                                       SUM(dtl.recvr_plateau_earned) AS plateau_earned_count
                                  FROM rpt_recognition_detail dtl,
                                       promotion p,
                                       country c,
                                       temp_table_session tts_c, -- cm country name
                                       temp_table_session tts_p, -- cm department type
                                       temp_table_session tts_d, -- cm position type
                                       gtt_id_list gil_dt, -- department type
                                       gtt_id_list gil_p,  -- promotion
                                       gtt_id_list gil_pt,  -- position type
                                       gtt_id_list gil_c    -- country
                                      -- restrict by required fields
                                WHERE dtl.recvr_user_id = p_in_userId
                                  AND TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                                  AND dtl.promotion_id = p.promotion_id
                                  AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status) 
                                  AND dtl.recvr_country_id = c.country_id(+)
                                  AND c.cm_asset_code = tts_c.asset_code(+)
                                  AND c.name_cm_key = tts_c.cms_code (+)
                                  AND gc_cms_list_position_type = tts_p.asset_code(+)
                                  AND dtl.recvr_job_position = tts_p.cms_code (+)
                                  AND gc_cms_list_department_type = tts_d.asset_code(+)
                                  AND dtl.recvr_department = tts_d.cms_code (+)                           
                                   -- restrict by optional fields
                                  AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                  AND (  gil_p.ref_text_2 = gc_search_all_values
                                      OR gil_p.id = dtl.promotion_id )
                                  AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                  AND (  gil_dt.ref_text_2 = gc_search_all_values
                                      OR gil_dt.ref_text_2 = dtl.recvr_department )
                                  AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                  AND (  gil_pt.ref_text_2 = gc_search_all_values
                                      OR gil_pt.ref_text_2 = dtl.recvr_job_position )
                                  AND gil_c.ref_text_1 = gc_ref_text_country_id
                                  AND (  gil_c.ref_text_2 = gc_search_all_values
                                      OR gil_c.id = dtl.recvr_country_id )
                                  AND (  p_in_participantStatus IS NULL
                                      OR dtl.recvr_pax_status = p_in_participantStatus )
                                  AND EXISTS (SELECT 1
                                                FROM gtt_id_list gil,
                                                     rpt_hierarchy h,
                                                     rpt_hierarchy_rollup hr
                                               WHERE gil.ref_text_1 = gc_ref_text_parent_node_id
                                                 AND gil.id = h.node_id 
                                                 AND h.is_deleted = 0
                                                 AND h.node_id = hr.node_id
                                                 AND hr.child_node_id = dtl.recvr_node_id
                                              )
                                GROUP BY dtl.claim_id,
                                         dtl.date_approved,
                                         dtl.recvr_full_name     ,
                                         NVL(tts_c.cms_name, ' '),
                                         dtl.recvr_node_name     ,
                                         NVL(tts_d.cms_name, ' '),
                                         NVL(tts_p.cms_name, ' '),
                                         p.promotion_name,
                                         dtl.giver_full_name
                                                 
                             )
                        GROUP BY GROUPING SETS ((),(claim_id,
                                                     date_approved,
                                                     receiver_name             ,
                                                     receiver_country          ,
                                                     recvr_node_name        ,
                                                     department             ,
                                                     job_position           ,
                                                     promotion_name,
                                                     sender_name         )) 
                       ) org  
               ) sd
        WHERE (  sd.rec_seq = 0   -- totals record
              OR -- reduce sequenced data set to just the output page's records
                 (   sd.rec_seq > p_in_rowNumStart
                 AND sd.rec_seq < p_in_rowNumEnd )
              )
        ORDER BY sd.rec_seq;
                              
   -- query totals data   
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.recognition_points     AS recognition_points,
          rec_query.plateau_earned_count   AS plateau_earned_count
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS DATE) AS date_approved,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_name,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_country,
             CAST(NULL AS VARCHAR2(4000)) AS recvr_node_name,
             CAST(NULL AS VARCHAR2(4000)) AS department,
             CAST(NULL AS VARCHAR2(4000)) AS job_position,
             CAST(NULL AS VARCHAR2(4000)) AS sender_name,
             CAST(NULL AS VARCHAR2(4000)) AS promotion_name,
             CAST(NULL AS NUMBER) AS recognition_points,
             CAST(NULL AS NUMBER) AS plateau_earned_count,
             CAST(NULL AS NUMBER) AS claim_id,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual
       WHERE 0=1;
   END IF;

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
      OPEN p_out_totals_data FOR SELECT NULL FROM dual;
END prc_getActivityReceivedByPax;

END pkg_query_recog_recipients;
/

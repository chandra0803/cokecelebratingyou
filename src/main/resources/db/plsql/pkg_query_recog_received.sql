
create or replace PACKAGE pkg_query_recog_received
    IS
  
PROCEDURE prc_get_byorgsummaryResults(
    p_in_parentNodeId       IN VARCHAR,
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,    
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,      
    p_in_nodeAndBelow       IN VARCHAR,
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR,
    p_out_totals_data       OUT SYS_REFCURSOR);

PROCEDURE prc_get_RecogrecvdPieresults(
    p_in_parentNodeId       IN VARCHAR,        
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,
    p_in_nodeAndBelow       IN VARCHAR,             
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR
    );
        
PROCEDURE prc_get_RecogrecvdTimeresults(
    p_in_parentNodeId       IN VARCHAR,        
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,
    p_in_nodeAndBelow       IN VARCHAR,             
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR
    );
        
PROCEDURE prc_get_RecogRatePieresults(
    p_in_parentNodeId       IN VARCHAR,        
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,
    p_in_nodeAndBelow       IN VARCHAR,             
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR
    );
        
PROCEDURE prc_get_RecogPointsBarresults(
    p_in_parentNodeId       IN VARCHAR,        
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,
    p_in_nodeAndBelow       IN VARCHAR,             
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR
    );
        
PROCEDURE prc_get_RecogPointstimeresults(
    p_in_parentNodeId       IN VARCHAR,        
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,
    p_in_nodeAndBelow       IN VARCHAR,             
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR
    );
        
PROCEDURE prc_get_Recogratebarresults(
    p_in_parentNodeId       IN VARCHAR,        
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,
    p_in_nodeAndBelow       IN VARCHAR,             
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR
    );
        
PROCEDURE prc_get_RecogdetailResults(
    p_in_parentNodeId       IN VARCHAR,
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,    
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,      
    p_in_nodeAndBelow       IN VARCHAR,
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR,
    p_out_totals_data       OUT SYS_REFCURSOR
    );
         
END pkg_query_recog_received;
/

create or replace PACKAGE BODY pkg_query_recog_received
IS
-- package constants

gc_null_id                       CONSTANT NUMBER(18) := pkg_const.gc_null_id;
gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
gc_pax_status_active             CONSTANT participant.status%TYPE := pkg_const.gc_pax_status_active;
gc_promotion_status_active       CONSTANT promotion.promotion_status%TYPE := pkg_const.gc_promotion_status_active;
gc_elg_type_receiver             CONSTANT rpt_pax_promo_eligibility.giver_recvr_type%TYPE := pkg_const.gc_elg_type_receiver;
gc_promotion_type_recognition    CONSTANT promotion.promotion_type%TYPE := pkg_const.gc_promotion_type_recognition;

gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
gc_node_name_team_suffix         CONSTANT VARCHAR2(30) := ' Team';
            

-----------------------
-- Private package processes
-----------------------
-- Stages the eligible count table based on whether input promotions have an all pax or specified audience.
-- Note: Process assumes the query parameters have already been staged to the temp ID list table.
PROCEDURE p_stg_received_eligible_counts
( p_in_giver_recvr_type    IN VARCHAR2,
  p_in_promotion_type      IN VARCHAR2,
  p_in_promotion_id_list   IN VARCHAR2,
  p_in_user_status_desc    IN VARCHAR2,
  p_in_promotion_status    IN VARCHAR2
) IS
   c_process_name       CONSTANT execution_log.process_name%TYPE := 'p_stg_received_eligible_counts';
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
                gtt_id_list gil_pt  -- position type
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = h.parent_node_id 
            AND h.node_id = pe.node_id
            AND pe.giver_recvr_type = p_in_giver_recvr_type
            AND pe.promotion_type = gc_promotion_type_recognition
             -- table uses promo ID -1 for all promotions 
            AND pe.promotion_id = NVL(p_in_promotion_id_list, -1)
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
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
                gtt_id_list gil_pt  -- position type
          WHERE gil_pn.ref_text_1 = gc_ref_text_parent_node_id
            AND gil_pn.id = pe.node_id
            AND pe.giver_recvr_type = p_in_giver_recvr_type
            AND pe.promotion_type = gc_promotion_type_recognition
             -- table uses promo ID -1 for all promotions 
            AND pe.promotion_id = NVL(p_in_promotion_id_list, -1)
            AND gil_dt.ref_text_1 = gc_ref_text_department_type
            AND (  gil_dt.ref_text_2 = gc_search_all_values
                OR gil_dt.ref_text_2 = pe.department_type )
            AND gil_pt.ref_text_1 = gc_ref_text_position_type
            AND (  gil_pt.ref_text_2 = gc_search_all_values
                OR gil_pt.ref_text_2 = pe.position_type )
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
                              AND p.promotion_type = gc_promotion_type_recognition
                              AND p.promotion_status = NVL(p_in_promotion_status, p.promotion_status)
                              AND gil_dt.ref_text_1 = gc_ref_text_department_type
                              AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = paxe.department_type )
                              AND gil_pt.ref_text_1 = gc_ref_text_position_type
                              AND (  gil_pt.ref_text_2 = gc_search_all_values
                                  OR gil_pt.ref_text_2 = paxe.position_type )
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
END p_stg_received_eligible_counts;

-----------------------
-- Public package processes
-----------------------
PROCEDURE prc_get_byorgsummaryResults(
    p_in_parentNodeId       IN VARCHAR,
    p_in_promotionId        IN VARCHAR2,
    p_in_jobposition        IN VARCHAR2,
    p_in_departments        IN VARCHAR,
    p_in_participantStatus  IN VARCHAR,
    p_in_promotionstatus    IN VARCHAR2,
    p_in_localeDatePattern  IN VARCHAR,
    p_in_fromDate           IN VARCHAR,
    p_in_toDate             IN VARCHAR,
    p_in_languageCode       IN VARCHAR,    
    p_in_rowNumStart        IN NUMBER,
    p_in_rowNumEnd          IN NUMBER,
    p_in_sortColName        IN VARCHAR,
    p_in_sortedBy           IN VARCHAR,      
    p_in_nodeAndBelow       IN VARCHAR,
    p_out_return_code       OUT NUMBER,
    p_out_data              OUT SYS_REFCURSOR,
    p_out_totals_data       OUT SYS_REFCURSOR)
IS
  /******************************************************************************
  NAME:       prc_get_byorgsummaryResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes 
   Soma N              11/07/2018       Bug  78849 Commented one line to avoid  duplicate output(looks to me cartesian) result of probable oracle upgrade(to 12.2) issue  
                                       --wn.org_id = wn.node_id --Soma 11/07/2018 forced to add to avoid duplicate results                                         
   nagarajs           11/19/2018       Fixed the above duplicate issue.Also Uncommented the above change to return correct recognition counts
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_byorgsummaryResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_fetch_count         INTEGER;
  v_data_sort           VARCHAR2(50);

   CURSOR cur_query_ref IS
    SELECT CAST(NULL AS VARCHAR2(4000)) AS org_name,
             CAST(NULL AS NUMBER) AS node_id,
             CAST(NULL AS NUMBER) AS eligible_cnt,
             CAST(NULL AS NUMBER) AS actual_cnt,
             CAST(NULL AS NUMBER) AS per_gave_receive,
             CAST(NULL AS NUMBER) AS total_recognition_cnt,
             CAST(NULL AS NUMBER) AS points,
             CAST(NULL AS NUMBER) AS plateau_earned,
             CAST(NULL AS NUMBER) AS sweepstakes_won,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;

   rec_query      cur_query_ref%ROWTYPE;    
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);

   -- stage eligible counts
   v_stage := 'stage eligible counts';
   DELETE gtt_recog_elg_count;
   p_stg_received_eligible_counts(gc_elg_type_receiver, gc_promotion_type_recognition, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);
   
                             
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
                       COUNT(DISTINCT org.node_id) OVER() AS total_records,
                       -- calc record sort order
                       ROW_NUMBER() OVER (ORDER BY
                         -- sort totals record first
                         DECODE(org.node_id, NULL, 0, 99),
                         -- ascending sorts
                         DECODE(v_data_sort, 'node_id', org.node_id),
                         DECODE(v_data_sort, 'org_name', LOWER(org.org_name)),
                         DECODE(v_data_sort, 'eligible_cnt', org.eligible_cnt),
                         DECODE(v_data_sort, 'actual_cnt', org.actual_cnt),
                         DECODE(v_data_sort, 'per_gave_receive', org.per_gave_receive),
                         DECODE(v_data_sort, 'total_recognition_cnt', org.total_recognition_cnt),
                         DECODE(v_data_sort, 'points', org.points),
                         DECODE(v_data_sort, 'plateau_earned', org.plateau_earned),
                         DECODE(v_data_sort, 'sweepstakes_won', org.sweepstakes_won),
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/node_id', org.node_id) DESC,
                         DECODE(v_data_sort, 'desc/org_name', LOWER(org.org_name)) DESC,
                         DECODE(v_data_sort, 'desc/eligible_cnt', org.eligible_cnt) DESC,
                         DECODE(v_data_sort, 'desc/actual_cnt', org.actual_cnt) DESC,
                         DECODE(v_data_sort, 'desc/per_gave_receive', org.per_gave_receive) DESC,
                         DECODE(v_data_sort, 'desc/total_recognition_cnt', org.total_recognition_cnt) DESC,
                         DECODE(v_data_sort, 'desc/points', org.points) DESC,
                         DECODE(v_data_sort, 'desc/plateau_earned', org.plateau_earned) DESC,
                         DECODE(v_data_sort, 'desc/sweepstakes_won', org.sweepstakes_won) DESC,
                         -- default sort fields
                         LOWER(org.org_name),
                         org.node_id
                       ) -1 AS rec_seq
                  FROM ( -- data grouped by org node
                         SELECT org_name, 
                                node_id, 
                                SUM (eligible_cnt) AS eligible_cnt, 
                                SUM (actual_cnt) AS actual_cnt, 
                                ROUND ( DECODE (SUM (eligible_cnt), 0, 0,(SUM(actual_cnt) / SUM (eligible_cnt)) * 100), 2) AS per_gave_receive,
                                SUM(recognition_cnt) AS total_recognition_cnt,   
                                SUM(points) AS points, 
                                SUM(plateau_earned_count) AS plateau_earned, 
                                SUM(sweepstakes_won_count) AS sweepstakes_won 
                           FROM ( -- build full data set
                                  SELECT wn.org_id as node_id,
                                         wn.org_name,
                                         NVL(rec_r.eligible_count, 0) AS eligible_cnt,
                                         COUNT(DISTINCT rd.recvr_user_id) actual_cnt,
                                         --COUNT(rd.recvr_activity_id) AS recognition_cnt, --11/19/2018
                                         COUNT(rd.recvr_user_id) AS recognition_cnt, --11/19/2018
                                         NVL(SUM(rd.recvr_plateau_earned),0) AS plateau_earned_count,
                                         NVL(SUM(rd.recvr_sweepstakes_won),0) AS sweepstakes_won_count,
                                         NVL(SUM(rd.award_amt),0) as points
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
                                                  gtt_id_list gil_pt  -- position type
                                               -- restrict by required fields
                                            WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                                              AND dtl.reason_denied IS NULL    
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
                                              AND (  p_in_participantStatus IS NULL
                                                  OR dtl.recvr_pax_status = p_in_participantStatus )
                                         ) rd,
                                         w_node wn,
                                         gtt_recog_elg_count rec_r    -- eligible receiver
                                      -- restrict to just the org nodes
                                   WHERE wn.org_id = wn.node_id  --Soma N 11/07/2018 Bug  78849  commented to avoid duplicates --11/19/2018 uncommented
                                      -- outer join so all queried org nodes appear in result set
                                     AND 
                                     wn.org_id = rd.org_id (+)
                                     AND wn.org_id = rec_r.node_id (+)
                                     AND gc_elg_type_receiver = rec_r.record_type (+)
                                   GROUP BY wn.org_id ,
                                         wn.org_name,
                                         NVL(rec_r.eligible_count,0)
                                )
                          GROUP BY GROUPING SETS
                                ((),
                                 (node_id,
                                  org_name)
                                )
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
   SELECT rec_query.eligible_cnt            AS eligible_cnt,
          rec_query.actual_cnt              AS actual_cnt,
          rec_query.per_gave_receive        AS per_gave_receive,
          rec_query.total_recognition_cnt   AS total_recognition_cnt,
          rec_query.points                  AS points,
          rec_query.plateau_earned          AS plateau_earned,
          rec_query.sweepstakes_won         AS sweepstakes_won
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS VARCHAR2(4000)) AS org_name,
             CAST(NULL AS NUMBER) AS node_id,
             CAST(NULL AS NUMBER) AS eligible_cnt,
             CAST(NULL AS NUMBER) AS actual_cnt,
             CAST(NULL AS NUMBER) AS per_gave_receive,
             CAST(NULL AS NUMBER) AS total_recognition_cnt,
             CAST(NULL AS NUMBER) AS points,
             CAST(NULL AS NUMBER) AS plateau_earned,
             CAST(NULL AS NUMBER) AS sweepstakes_won,
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
END prc_get_byorgsummaryResults;
    
    PROCEDURE prc_get_RecogrecvdPieresults(
        p_in_parentNodeId       IN VARCHAR,        
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,
        p_in_nodeAndBelow       IN VARCHAR,             
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR
        ) IS
/******************************************************************************
  NAME:       prc_get_RecogrecvdPieresults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_RecogrecvdPieresults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
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
        SELECT node_name,
               recognition_cnt
          FROM ( -- data grouped by org node
                 SELECT node_name, 
                        ROW_NUMBER() OVER (ORDER BY node_name DESC) AS rec_seq, 
                        NVL(recognition_cnt, 0) AS recognition_cnt
                   FROM ( -- build full data set
                          SELECT wn.org_id as node_id,
                                 wn.org_name as node_name,
                                 COUNT(rd.recvr_activity_id) AS recognition_cnt
                            FROM ( -- get raw data for org nodes and their child nodes
                                   SELECT wn2.org_id,
                                          wn2.node_id,
                                          dtl.recvr_activity_id
                                     FROM w_node wn2,
                                          rpt_recognition_detail dtl,
                                          promotion p,
                                          gtt_id_list gil_dt, -- department type
                                          gtt_id_list gil_p,  -- promotion
                                          gtt_id_list gil_pt  -- position type
                                       -- restrict by required fields
                                    WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                                      AND dtl.reason_denied IS NULL    
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
                                      AND (  p_in_participantStatus IS NULL
                                          OR dtl.recvr_pax_status = p_in_participantStatus )
                                 ) rd,
                                 w_node wn
                              -- restrict to just the org nodes
                           WHERE wn.org_id = wn.node_id
                              -- outer join so all queried org nodes appear in result set
                             AND wn.org_id = rd.org_id (+)
                        GROUP BY wn.org_id,
                                 wn.org_name
                        )
                    ) s
                   ORDER BY s.rec_seq; 

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_get_RecogrecvdPieresults;
        
PROCEDURE prc_get_RecogrecvdTimeresults(
        p_in_parentNodeId       IN VARCHAR,        
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,
        p_in_nodeAndBelow       IN VARCHAR,             
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR
        ) IS
/******************************************************************************
  NAME:       prc_get_RecogrecvdTimeresults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_RecogrecvdTimeresults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_default_locale      os_propertyset.string_val%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
   
  v_stage:= 'Insert temp_table_session';
  DELETE temp_table_session;
  INSERT INTO temp_table_session
   SELECT asset_code,cms_name,UPPER(cms_code)
     FROM (SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn 
             FROM mv_cms_code_value 
            WHERE asset_code = 'picklist.monthname.type.items'
              AND locale IN (v_default_locale, p_in_languageCode)
              AND cms_status = 'true')
     WHERE rn = 1;
                             
   OPEN p_out_data FOR
    SELECT month_name,
           recognition_cnt
      FROM (  -- build full data set
              SELECT ts.cms_code||' - '||ml.year_char AS month_name,
                     ROW_NUMBER() OVER (ORDER BY month_sort ASC) AS rec_seq,
                     NVL(recognition_cnt,0) AS recognition_cnt
                FROM ( -- get raw data for month and year
                       SELECT TO_CHAR(dtl.date_approved,'MON') AS month_char , 
                              TO_CHAR(dtl.date_approved,'YY') AS year_char, 
                              COUNT(recvr_activity_id) AS recognition_cnt
                         FROM rpt_recognition_detail dtl,
                              promotion p,
                              gtt_id_list gil_dt, -- department type
                              gtt_id_list gil_p,  -- promotion
                              gtt_id_list gil_pt  -- position type
                           -- restrict by required fields
                        WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                          AND dtl.reason_denied IS NULL    
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
                        GROUP BY TO_CHAR(dtl.date_approved,'MON')  , 
                              TO_CHAR(dtl.date_approved,'YY') 
                      ) rd,
                      (SELECT TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'MON' ) AS month_char, 
                              TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'yy' ) AS year_char, 
                              ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM  - 1) month_sort 
                         FROM dual 
                      CONNECT BY LEVEL <= MONTHS_BETWEEN(TRUNC(TO_DATE(p_in_toDate,p_in_localeDatePattern), 'mm'), TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm')) + 1 
                      ) ml,
                      temp_table_session ts
               WHERE ml.month_char = rd.month_char (+)
                 AND ml.year_char  = rd.year_char (+)
                 AND ml.month_char = ts.cms_code
            ) s
      ORDER BY s.rec_seq; 
 -- successful
  p_out_return_code := 0;

  COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_get_RecogrecvdTimeresults;   
PROCEDURE prc_get_RecogRatePieresults(
        p_in_parentNodeId       IN VARCHAR,        
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,
        p_in_nodeAndBelow       IN VARCHAR,             
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR
        ) IS
/******************************************************************************
  NAME:       prc_get_RecogRatePieresults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_RecogRatePieresults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_data_sort           VARCHAR2(50);  
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);

   -- stage eligible counts
   v_stage := 'stage eligible counts';
   DELETE gtt_recog_elg_count;
   p_stg_received_eligible_counts(gc_elg_type_receiver, gc_promotion_type_recognition, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);
                     
   -- stage actual counts
   v_stage := 'stage actual counts';
   DELETE gtt_recog_actual_count;
   INSERT INTO gtt_recog_actual_count 
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
      SELECT COUNT(DISTINCT rd.recvr_user_id) actual_cnt,
             wn.org_id as node_id
        FROM ( -- get raw data for org nodes and their child nodes
               SELECT wn2.org_id,
                      wn2.node_id,
                      dtl.recvr_user_id
                 FROM w_node wn2,
                      rpt_recognition_detail dtl,
                      promotion p,
                      gtt_id_list gil_dt, -- department type
                      gtt_id_list gil_p,  -- promotion
                      gtt_id_list gil_pt  -- position type
                   -- restrict by required fields
                WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date                                               
                  AND dtl.reason_denied IS NULL   
                  AND dtl.promotion_id = p.promotion_id
                  AND wn2.node_id = dtl.recvr_node_id
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
                      OR gil_pt.ref_text_2 = dtl.recvr_job_position  )
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
END prc_get_RecogRatePieresults;
        
PROCEDURE prc_get_RecogPointsBarresults(
        p_in_parentNodeId       IN VARCHAR,        
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,
        p_in_nodeAndBelow       IN VARCHAR,             
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR
        ) IS
/******************************************************************************
  NAME:       prc_get_RecogPointsBarresults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_RecogPointsBarresults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
                             
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
        SELECT node_name,
               points
          FROM ( -- data grouped by org node
                 SELECT node_name, 
                        NVL(points, 0) AS points
                   FROM ( -- build full data set
                          SELECT wn.org_id as node_id,
                                 wn.org_name as node_name,
                                 SUM(rd.award_amt) AS points
                            FROM ( -- get raw data for org nodes and their child nodes
                                   SELECT wn2.org_id,
                                          wn2.node_id,
                                          dtl.award_amt
                                     FROM w_node wn2,
                                          rpt_recognition_detail dtl,
                                          promotion p,
                                          gtt_id_list gil_dt, -- department type
                                          gtt_id_list gil_p,  -- promotion
                                          gtt_id_list gil_pt  -- position type
                                       -- restrict by required fields
                                    WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                                      AND dtl.reason_denied IS NULL    
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
                                      AND (  p_in_participantStatus IS NULL
                                          OR dtl.recvr_pax_status = p_in_participantStatus )
                                 ) rd,
                                 w_node wn
                              -- restrict to just the org nodes
                           WHERE wn.org_id = wn.node_id
                              -- outer join so all queried org nodes appear in result set
                             AND wn.org_id = rd.org_id (+)
                        GROUP BY wn.org_id,
                                 wn.org_name
                        )
                    ) s
                   ORDER BY points DESC; 

   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_get_RecogPointsBarresults;
        
PROCEDURE prc_get_RecogPointstimeresults(
        p_in_parentNodeId       IN VARCHAR,        
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,
        p_in_nodeAndBelow       IN VARCHAR,             
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR
        ) IS
/******************************************************************************
  NAME:       prc_get_RecogPointstimeresults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_RecogPointstimeresults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_default_locale      os_propertyset.string_val%TYPE;
  v_from_date           DATE;
  v_to_date             DATE; 
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);
   
   
  v_stage:= 'Insert temp_table_session';
  DELETE temp_table_session;
  INSERT INTO temp_table_session
   SELECT asset_code,cms_name,UPPER(cms_code)
     FROM (SELECT asset_code,cms_name,cms_code,  RANK() OVER(PARTITION BY asset_code, cms_code ORDER BY DECODE(p_in_languageCode,locale,1,2)) rn 
             FROM mv_cms_code_value 
            WHERE asset_code = 'picklist.monthname.type.items'
              AND locale IN (v_default_locale, p_in_languageCode)
              AND cms_status = 'true')
     WHERE rn = 1;
                             
   OPEN p_out_data FOR
    SELECT month_name,
           points
      FROM (  -- build full data set
              SELECT ts.cms_code||' - '||ml.year_char AS month_name,
                     ROW_NUMBER() OVER (ORDER BY month_sort ASC) AS rec_seq,
                     NVL(points,0) AS points
                FROM ( -- get raw data for month and year
                       SELECT TO_CHAR(dtl.date_approved,'MON') AS month_char , 
                              TO_CHAR(dtl.date_approved,'YY') AS year_char, 
                              SUM(award_amt) AS points
                         FROM rpt_recognition_detail dtl,
                              promotion p,
                              gtt_id_list gil_dt, -- department type
                              gtt_id_list gil_p,  -- promotion
                              gtt_id_list gil_pt  -- position type
                           -- restrict by required fields
                        WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date
                          AND dtl.reason_denied IS NULL    
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
                        GROUP BY TO_CHAR(dtl.date_approved,'MON')  , 
                              TO_CHAR(dtl.date_approved,'YY') 
                      ) rd,
                      (SELECT TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'MON' ) AS month_char, 
                              TO_CHAR(ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM - 1), 'yy' ) AS year_char, 
                              ADD_MONTHS(TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm'), ROWNUM  - 1) month_sort 
                         FROM dual 
                      CONNECT BY LEVEL <= MONTHS_BETWEEN(TRUNC(TO_DATE(p_in_toDate,p_in_localeDatePattern), 'mm'), TRUNC(TO_DATE(p_in_fromDate,p_in_localeDatePattern), 'mm')) + 1 
                      ) ml,
                      temp_table_session ts
               WHERE ml.month_char = rd.month_char (+)
                 AND ml.year_char  = rd.year_char (+)
                 AND ml.month_char = ts.cms_code
            ) s
      ORDER BY s.rec_seq; 
 -- successful
  p_out_return_code := 0;

  COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_get_RecogPointstimeresults;
        
PROCEDURE prc_get_Recogratebarresults(
        p_in_parentNodeId       IN VARCHAR,        
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,
        p_in_nodeAndBelow       IN VARCHAR,             
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR
        ) IS
        
/******************************************************************************
  NAME:       prc_get_Recogratebarresults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_Recogratebarresults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_data_sort           VARCHAR2(50);  
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);

   -- stage eligible counts
   v_stage := 'stage eligible counts';
   DELETE gtt_recog_elg_count;
   p_stg_received_eligible_counts(gc_elg_type_receiver, gc_promotion_type_recognition, p_in_promotionId, p_in_participantStatus, gc_promotion_status_active);
                     
   -- stage actual counts
   v_stage := 'stage actual counts';
   DELETE gtt_recog_actual_count;
   INSERT INTO gtt_recog_actual_count 
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
      SELECT COUNT(DISTINCT rd.recvr_user_id) actual_cnt,
             wn.org_id as node_id
        FROM ( -- get raw data for org nodes and their child nodes
               SELECT wn2.org_id,
                      wn2.node_id,
                      dtl.recvr_user_id
                 FROM w_node wn2,
                      rpt_recognition_detail dtl,
                      promotion p,
                      gtt_id_list gil_dt, -- department type
                      gtt_id_list gil_p,  -- promotion
                      gtt_id_list gil_pt  -- position type
                   -- restrict by required fields
                WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date                                               
                  AND dtl.reason_denied IS NULL   
                  AND dtl.promotion_id = p.promotion_id
                  AND wn2.node_id = dtl.recvr_node_id
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
                      OR gil_pt.ref_text_2 = dtl.recvr_job_position  )
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
      SELECT h.node_name AS org_name, 
             NVL (eligible_count, 0) - NVL (actual_count, 0) havenot_given_cnt, 
             NVL (actual_count, 0) have_given_cnt
        FROM gtt_recog_elg_count elg,
             gtt_recog_actual_count act,
             rpt_hierarchy h
       WHERE elg.node_id = act.node_id
         AND elg.node_id = h.node_id;
                 
   -- successful
   p_out_return_code := 0;

   COMMIT;  -- release DML locks on temp table
EXCEPTION
   WHEN OTHERS THEN
      prc_execution_log_entry(c_process_name, 1, 'ERROR', v_stage || v_parms || SQLCODE || ':' || SQLERRM, NULL);
      ROLLBACK;  -- release DML locks on temp table
      p_out_return_code := 99;
      OPEN p_out_data FOR SELECT NULL FROM dual;
END prc_get_Recogratebarresults;
        
PROCEDURE prc_get_RecogdetailResults(
        p_in_parentNodeId       IN VARCHAR,
        p_in_promotionId        IN VARCHAR2,
        p_in_jobposition        IN VARCHAR2,
        p_in_departments        IN VARCHAR,
        p_in_participantStatus  IN VARCHAR,
        p_in_promotionstatus    IN VARCHAR2,
        p_in_localeDatePattern  IN VARCHAR,
        p_in_fromDate           IN VARCHAR,
        p_in_toDate             IN VARCHAR,
        p_in_languageCode       IN VARCHAR,    
        p_in_rowNumStart        IN NUMBER,
        p_in_rowNumEnd          IN NUMBER,
        p_in_sortColName        IN VARCHAR,
        p_in_sortedBy           IN VARCHAR,      
        p_in_nodeAndBelow       IN VARCHAR,
        p_out_return_code       OUT NUMBER,
        p_out_data              OUT SYS_REFCURSOR,
        p_out_totals_data       OUT SYS_REFCURSOR
         )
    IS
  /******************************************************************************
  NAME:       prc_get_RecogdetailResults
  Author                Date            Description
  ----------           ---------------  ------------------------------------------------
   nagarajs            04/03/2017       Initial Creation - G6.2 Query redesign for perfomance changes                                      
  ******************************************************************************/
  c_process_name        CONSTANT execution_log.process_name%TYPE := 'prc_get_RecogdetailResults' ;
  v_stage               execution_log.text_line%TYPE;
  v_parms               execution_log.text_line%TYPE;
  v_from_date           DATE;
  v_to_date             DATE;
  v_fetch_count         INTEGER;
  v_data_sort           VARCHAR2(50);

   CURSOR cur_query_ref IS
    SELECT CAST(NULL AS NUMBER) AS recvr_user_id,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_name,
             CAST(NULL AS NUMBER) AS recognitions_cnt,
             CAST(NULL AS NUMBER) AS recognition_points,
             CAST(NULL AS NUMBER) AS plateau_earned_count,
             CAST(NULL AS NUMBER) AS sweepstakes_won_count,
             CAST(NULL AS NUMBER) AS total_records,
             CAST(NULL AS NUMBER) AS rec_seq
        FROM dual;

   rec_query      cur_query_ref%ROWTYPE;    
   
BEGIN

   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_jobposition >' || p_in_jobposition
      || '<, p_in_departments >' || p_in_departments
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_promotionstatus >' || p_in_promotionstatus
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate              
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_languageCode >' || p_in_languageCode
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_sortColName >' || p_in_sortColName              
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<, p_in_nodeAndBelow >' || p_in_nodeAndBelow
      || '<';

   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
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
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values),  gc_ref_text_department_type);   
                             
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
                         DECODE(v_data_sort, 'recognitions_cnt', org.recognitions_cnt),
                         DECODE(v_data_sort, 'recognition_points', org.recognition_points),
                         DECODE(v_data_sort, 'plateau_earned_count', org.plateau_earned_count),
                         DECODE(v_data_sort, 'sweepstakes_won_count', org.sweepstakes_won_count),
                         -- descending sorts
                         DECODE(v_data_sort, 'desc/recvr_user_id', org.recvr_user_id) DESC,
                         DECODE(v_data_sort, 'desc/receiver_name', LOWER(org.receiver_name)) DESC,
                         DECODE(v_data_sort, 'desc/recognitions_cnt', org.recognitions_cnt) DESC,
                         DECODE(v_data_sort, 'desc/recognition_points', org.recognition_points) DESC,
                         DECODE(v_data_sort, 'desc/plateau_earned_count', org.plateau_earned_count) DESC,
                         DECODE(v_data_sort, 'desc/sweepstakes_won_count', org.sweepstakes_won_count) DESC,
                         -- default sort fields
                         LOWER(org.receiver_name),
                         org.recvr_user_id
                       ) -1 AS rec_seq
                  FROM ( SELECT recvr_user_id,
                                recvr_full_name receiver_name, 
                                NVL(COUNT(DISTINCT dtl.recvr_activity_id),0) recognitions_cnt, 
                                NVL(SUM(dtl.award_amt),0)             AS recognition_points, 
                                NVL(SUM(dtl.recvr_plateau_earned),0)  AS plateau_earned_count, 
                                NVL(SUM(dtl.recvr_sweepstakes_won),0) AS sweepstakes_won_count 
                           FROM rpt_recognition_detail dtl,
                                promotion p,
                                gtt_id_list gil,
                                gtt_id_list gil_dt, -- department type
                                gtt_id_list gil_p,  -- promotion
                                gtt_id_list gil_pt  -- position type
                                -- restrict by required fields
                          WHERE TRUNC(dtl.date_approved) BETWEEN v_from_date AND v_to_date                                               
                            AND dtl.reason_denied IS NULL   
                            AND dtl.promotion_id = p.promotion_id
                            AND p.promotion_status = NVL ( p_in_promotionstatus,p.promotion_status) 
                            AND gil.ref_text_1 = gc_ref_text_parent_node_id
                            AND gil.id = dtl.recvr_node_id
                             -- restrict by optional fields
                            AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                            AND (  gil_p.ref_text_2 = gc_search_all_values
                                OR gil_p.id = dtl.promotion_id )
                            AND gil_dt.ref_text_1 = gc_ref_text_department_type
                            AND (  gil_dt.ref_text_2 = gc_search_all_values
                                OR gil_dt.ref_text_2 = dtl.recvr_department )
                            AND gil_pt.ref_text_1 = gc_ref_text_position_type
                            AND (  gil_pt.ref_text_2 = gc_search_all_values
                                OR gil_pt.ref_text_2 = dtl.recvr_job_position  )
                            AND (  p_in_participantStatus IS NULL
                                OR dtl.recvr_pax_status = p_in_participantStatus )
                          GROUP BY GROUPING SETS((), (recvr_user_id,
                                                      recvr_full_name)
                                                 )
                       ) org
              ) sd
         -- reduce sequenced data set to just the output page's records
        WHERE sd.rec_seq >= p_in_rowNumStart
          AND sd.rec_seq < p_in_rowNumEnd 
        ORDER BY sd.rec_seq;


 -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
   SELECT rec_query.recognitions_cnt        AS recognitions_cnt,
          rec_query.recognition_points      AS recognition_points,
          rec_query.plateau_earned_count    AS plateau_earned_count,
          rec_query.sweepstakes_won_count   AS sweepstakes_won_count
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS NUMBER) AS recvr_user_id,
             CAST(NULL AS VARCHAR2(4000)) AS receiver_name,
             CAST(NULL AS NUMBER) AS recognitions_cnt,
             CAST(NULL AS NUMBER) AS recognition_points,
             CAST(NULL AS NUMBER) AS plateau_earned_count,
             CAST(NULL AS NUMBER) AS sweepstakes_won_count,
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
END prc_get_RecogdetailResults;
      
END pkg_query_recog_received;

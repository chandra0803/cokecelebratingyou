CREATE OR REPLACE PACKAGE pkg_query_behaviors_report
IS
    PROCEDURE prc_getTabularResults(
            p_in_departments       IN VARCHAR,
            p_in_rowNumEnd         IN NUMBER,
            p_in_rowNumStart       IN NUMBER,
            p_in_jobPosition       IN VARCHAR,
            p_in_participantStatus IN VARCHAR,
            p_in_toDate            IN VARCHAR,
            p_in_localeDatePattern IN VARCHAR,
            p_in_fromDate          IN VARCHAR,
            p_in_giverReceiver     IN VARCHAR,
            p_in_parentNodeId      IN VARCHAR,
            p_in_promotionId       IN VARCHAR,
            p_in_languageCode      IN VARCHAR,
            p_in_sortColName       IN VARCHAR,
            p_in_sortedBy          IN VARCHAR,
            p_out_return_code      OUT NUMBER,
            p_out_data             OUT SYS_REFCURSOR,
            p_out_totals_data      OUT SYS_REFCURSOR );
    PROCEDURE prc_getBarchartResults(
            p_in_departments       IN VARCHAR,
            p_in_rowNumEnd         IN NUMBER,
            p_in_rowNumStart       IN NUMBER,
            p_in_jobPosition       IN VARCHAR,
            p_in_participantStatus IN VARCHAR,
            p_in_toDate            IN VARCHAR,
            p_in_localeDatePattern IN VARCHAR,
            p_in_fromDate          IN VARCHAR,
            p_in_giverReceiver     IN VARCHAR,
            p_in_parentNodeId      IN VARCHAR,
            p_in_promotionId       IN VARCHAR,
            p_in_languageCode      IN VARCHAR,
            p_in_sortColName       IN VARCHAR,
            p_in_sortedBy          IN VARCHAR,
            p_out_return_code      OUT NUMBER,
            p_out_data             OUT SYS_REFCURSOR);
    PROCEDURE prc_getPiechartResults(
            p_in_departments       IN VARCHAR,
            p_in_rowNumEnd         IN NUMBER,
            p_in_rowNumStart       IN NUMBER,
            p_in_jobPosition       IN VARCHAR,
            p_in_participantStatus IN VARCHAR,
            p_in_toDate            IN VARCHAR,
            p_in_localeDatePattern IN VARCHAR,
            p_in_fromDate          IN VARCHAR,
            p_in_giverReceiver     IN VARCHAR,
            p_in_parentNodeId      IN VARCHAR,
            p_in_promotionId       IN VARCHAR,
            p_in_languageCode      IN VARCHAR,
            p_in_sortColName       IN VARCHAR,
            p_in_sortedBy          IN VARCHAR,
            p_out_return_code      OUT NUMBER,
            p_out_data             OUT SYS_REFCURSOR );
END pkg_query_behaviors_report;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_behaviors_report
IS

--Constants
gc_search_all_values             CONSTANT VARCHAR2(30)  := pkg_const.gc_search_all_values;
gc_cms_list_nomi_behavior        CONSTANT VARCHAR2(100) := pkg_const.gc_cms_list_nomi_behavior;
gc_cms_list_reco_behavior        CONSTANT VARCHAR2(100) := pkg_const.gc_cms_list_reco_behavior;
gc_default_language              CONSTANT VARCHAR2(30)  := pkg_const.gc_default_language;

gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';

PROCEDURE prc_getTabularResults(
        p_in_departments       IN VARCHAR,
        p_in_rowNumEnd         IN NUMBER,
        p_in_rowNumStart       IN NUMBER,
        p_in_jobPosition       IN VARCHAR,
        p_in_participantStatus IN VARCHAR,
        p_in_toDate            IN VARCHAR,
        p_in_localeDatePattern IN VARCHAR,
        p_in_fromDate          IN VARCHAR,
        p_in_giverReceiver     IN VARCHAR,
        p_in_parentNodeId      IN VARCHAR,
        p_in_promotionId       IN VARCHAR,
        p_in_languageCode      IN VARCHAR,
        p_in_sortColName       IN VARCHAR,
        p_in_sortedBy          IN VARCHAR,
        p_out_return_code      OUT NUMBER,
        p_out_data             OUT SYS_REFCURSOR,
        p_out_totals_data      OUT SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_get_byorgsummaryResults
  Author           Date            Description
  ----------      ------------    ------------------------------------------------
  nagarajs        04/26/2016     Initial creation
  nagarajs        01/16/2017    Rewritten for perfoamnce changes in 5.6.3.3  
  Gorantla        06/22/2018    Bug 75789 Behavior Activity Report Spins when the recognition and 
                                nomination promotions were submitted and approved with a newly created same behavior with same case.                   
******************************************************************************/
    c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_getTabularResults';
    v_stage              VARCHAR2(500);  
    v_parms               execution_log.text_line%TYPE;
    v_data_sort           VARCHAR2(50);
    v_fetch_count         INTEGER;
    
    CURSOR cur_query_ref IS
     SELECT 
          CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq,
          CAST(NULL AS VARCHAR2(4000)) AS behavior,
          CAST(NULL AS NUMBER) AS b_cnt,
          CAST(NULL AS NUMBER) AS is_leaf,
          CAST(NULL AS NUMBER) AS detail_node_id,
          CAST(NULL AS VARCHAR2(4000)) AS node_name
     FROM dual;

   rec_query      cur_query_ref%ROWTYPE;    

BEGIN
   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_departments >' || p_in_departments
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';
      
  -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortColName);
   ELSE
      v_data_sort := LOWER(p_in_sortColName);
   END IF;
   
  -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  
  
  v_stage := 'open p_out_data';
  OPEN p_out_data FOR 
  SELECT s.*
    FROM (
    SELECT COUNT(rs.detail_node_id) OVER() AS total_records,
           ROW_NUMBER() OVER 
           (ORDER BY
           -- sort totals record first
           DECODE(rs.detail_node_id, NULL, 0, 99),
           -- ascending sorts
           DECODE(v_data_sort, 'behavior',          LOWER(rs.behavior)),
           DECODE(v_data_sort, 'node_name',         LOWER(rs.node_name)),
           -- descending sorts
           DECODE(v_data_sort, 'desc/behavior',     LOWER(rs.behavior)) DESC,
           DECODE(v_data_sort, 'desc/node_name',   LOWER(rs.node_name)) DESC,
            -- default sort fields
             LOWER(rs.node_name),
             rs.detail_node_id
           ) -1 AS rec_seq,
           rs.*
      FROM (SELECT cms.cms_name AS behavior, 
                   SUM(b_cnt) AS b_cnt, --06/22/2018
                   is_leaf,
                   detail_node_id ,
                   node_name         
              FROM ( SELECT behavior, 
                            SUM(behavior_cnt) AS b_cnt,
                            NVL (is_leaf, 0) is_leaf, 
                            detail_node_id detail_node_id, 
                            node_name node_name 
                       FROM (SELECT rpt.behavior, 
                                    behavior_cnt, 
                                    NVL (rpt.is_leaf, 0) is_leaf, 
                                    rpt.detail_node_id detail_node_id, 
                                    rh.node_name node_name 
                               FROM rpt_behavior_summary rpt, 
                                    rpt_hierarchy rh, 
                                    promotion p,
                                    gtt_id_list gil_hr, -- hierarchy rollup 
                                    gtt_id_list gil_dt,  -- department type
                                    gtt_id_list gil_pt,  -- position type
                                    gtt_id_list gil_p  -- promotion         
                              WHERE rpt.detail_node_id = rh.node_id 
                                AND rpt.record_type LIKE '%node%' 
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                AND gil_hr.id = rh.parent_node_id
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = p.promotion_id )
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = rpt.department ) 
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = rpt.job_position) 
                               AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                   OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))
                                AND rpt.promotion_id = p.promotion_id 
                                AND rpt.giver_recvr_type = p_in_giverReceiver
                                AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                                AND rpt.pax_status = NVL (p_in_participantStatus, rpt.pax_status) 
                              UNION ALL 
                             SELECT rpt.behavior, 
                                    behavior_cnt, 
                                    NVL (rpt.is_leaf, 0) is_leaf, 
                                    rpt.detail_node_id detail_node_id, 
                                    rh.node_name || ' Team' node_name 
                               FROM rpt_behavior_summary rpt, 
                                    rpt_hierarchy rh, 
                                    promotion p,
                                    gtt_id_list gil_hr, -- hierarchy rollup 
                                    gtt_id_list gil_dt,  -- department type
                                    gtt_id_list gil_pt,  -- position type
                                    gtt_id_list gil_p  -- promotion  
                              WHERE rpt.detail_node_id = rh.node_id 
                                AND rpt.record_type LIKE '%team%' 
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                AND gil_hr.id = rh.node_id
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = p.promotion_id )
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = rpt.department ) 
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = rpt.job_position ) 
                               AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                   OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) 
                                AND rpt.giver_recvr_type = p_in_giverReceiver
                                AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                                AND rpt.pax_status = NVL (p_in_participantStatus,rpt.pax_status) 
                                AND rpt.promotion_id = p.promotion_id 
                            ) 
                            GROUP BY GROUPING SETS
                                  ((), (behavior,
                                        NVL (is_leaf, 0),
                                        detail_node_id, 
                                        node_name))               
                   ) rs, 
                   (SELECT cms_code, 
                            cms_name 
                       FROM mv_cms_code_value vw 
                      WHERE asset_code IN (gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = gc_default_language) 
                        AND NOT EXISTS (SELECT * FROM mv_cms_code_value WHERE asset_code IN ( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale =p_in_languageCode)
                   GROUP BY cms_code,
                            cms_name
                      UNION ALL 
                     SELECT cms_code, 
                            cms_name 
                       FROM mv_cms_code_value 
                      WHERE asset_code IN( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale = p_in_languageCode
                      GROUP BY cms_code,
                            cms_name
                   )cms    
             WHERE rs.behavior = cms.cms_code(+)
             GROUP BY cms.cms_name , --06/22/2018 start
                   is_leaf,
                   detail_node_id ,
                   node_name    --06/22/2018 end
             ) rs
            ) s
      WHERE (  s.rec_seq = 0   -- totals record
            OR -- reduce sequenced data set to just the output page's records
             (   s.rec_seq > p_in_rowNumStart
             AND s.rec_seq < p_in_rowNumEnd )
            )
      ORDER BY s.rec_seq;
 

     -- query totals data
   v_stage := 'FETCH p_out_data totals record';
   FETCH p_out_data INTO rec_query;
   v_fetch_count := p_out_data%ROWCOUNT;

   v_stage := 'OPEN p_out_totals_data';
   OPEN p_out_totals_data FOR
     SELECT cms.cms_name AS behavior, 
           SUM(b_cnt)    AS b_cnt --06/22/2018  
      FROM ( SELECT behavior, 
                    SUM(behavior_cnt) AS b_cnt
               FROM (SELECT rpt.behavior, 
                            behavior_cnt, 
                            NVL (rpt.is_leaf, 0) is_leaf, 
                            rpt.detail_node_id detail_node_id, 
                            rh.node_name node_name 
                       FROM rpt_behavior_summary rpt, 
                            rpt_hierarchy rh, 
                            promotion p,
                            gtt_id_list gil_hr, -- hierarchy rollup 
                            gtt_id_list gil_dt,  -- department type
                            gtt_id_list gil_pt,  -- position type
                            gtt_id_list gil_p  -- promotion         
                      WHERE rpt.detail_node_id = rh.node_id 
                        AND rpt.record_type LIKE '%node%' 
                        AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                        AND gil_hr.id = rh.parent_node_id
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                            OR gil_p.id = p.promotion_id )
                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                            OR gil_dt.ref_text_2 = rpt.department ) 
                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                            OR gil_pt.ref_text_2 = rpt.job_position ) 
                       AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                           OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))
                        AND rpt.promotion_id = p.promotion_id 
                        AND rpt.giver_recvr_type = p_in_giverReceiver
                        AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                        AND rpt.pax_status = NVL (p_in_participantStatus, rpt.pax_status) 
                      UNION ALL 
                     SELECT rpt.behavior, 
                            behavior_cnt, 
                            NVL (rpt.is_leaf, 0) is_leaf, 
                            rpt.detail_node_id detail_node_id, 
                            rh.node_name || ' Team' node_name 
                       FROM rpt_behavior_summary rpt, 
                            rpt_hierarchy rh, 
                            promotion p,
                            gtt_id_list gil_hr, -- hierarchy rollup 
                            gtt_id_list gil_dt,  -- department type
                            gtt_id_list gil_pt,  -- position type
                            gtt_id_list gil_p  -- promotion  
                      WHERE rpt.detail_node_id = rh.node_id 
                        AND rpt.record_type LIKE '%team%' 
                        AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                        AND gil_hr.id = rh.node_id
                        AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                        AND (  gil_p.ref_text_2 = gc_search_all_values
                            OR gil_p.id = p.promotion_id )
                        AND gil_dt.ref_text_1 = gc_ref_text_department_type
                        AND (  gil_dt.ref_text_2 = gc_search_all_values
                            OR gil_dt.ref_text_2 = rpt.department ) 
                        AND gil_pt.ref_text_1 = gc_ref_text_position_type
                        AND (  gil_pt.ref_text_2 = gc_search_all_values
                            OR gil_pt.ref_text_2 = rpt.job_position ) 
                       AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                           OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) 
                        AND rpt.giver_recvr_type = p_in_giverReceiver
                        AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                        AND rpt.pax_status = NVL (p_in_participantStatus,rpt.pax_status) 
                        AND rpt.promotion_id = p.promotion_id 
                    ) 
                    GROUP BY behavior              
           ) rs, 
           (SELECT cms_code, 
                    cms_name 
               FROM mv_cms_code_value vw 
              WHERE asset_code IN (gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = gc_default_language) 
                AND NOT EXISTS (SELECT * FROM mv_cms_code_value WHERE asset_code IN ( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                AND locale =p_in_languageCode)
           GROUP BY cms_code,
                    cms_name
              UNION ALL 
             SELECT cms_code, 
                    cms_name 
               FROM mv_cms_code_value 
              WHERE asset_code IN( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                AND locale = p_in_languageCode
              GROUP BY cms_code,
                    cms_name
           )cms    
     WHERE rs.behavior = cms.cms_code(+)
     GROUP BY cms.cms_name; --06/22/2018

   v_stage := 'OPEN p_out_data NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_data FOR
      SELECT CAST(NULL AS VARCHAR2(4000)) AS behavior,
          CAST(NULL AS NUMBER) AS b_cnt,
          CAST(NULL AS NUMBER) AS is_leaf,
          CAST(NULL AS NUMBER) AS detail_node_id,
          CAST(NULL AS VARCHAR2(4000)) AS node_name,
          CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq
        FROM dual
       WHERE 0=1;
   END IF;

   p_out_return_code := '00';
EXCEPTION
  WHEN OTHERS THEN
     p_out_return_code := '99';
     prc_execution_log_entry (c_process_name,1,'ERROR',v_stage || SQLERRM|| v_parms,NULL);
     OPEN p_out_data FOR SELECT NULL FROM DUAL;
     OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
END prc_getTabularResults;
/* prc_getBarchartResults */
PROCEDURE prc_getBarchartResults(
        p_in_departments       IN VARCHAR,
        p_in_rowNumEnd         IN NUMBER,
        p_in_rowNumStart       IN NUMBER,
        p_in_jobPosition       IN VARCHAR,
        p_in_participantStatus IN VARCHAR,
        p_in_toDate            IN VARCHAR,
        p_in_localeDatePattern IN VARCHAR,
        p_in_fromDate          IN VARCHAR,
        p_in_giverReceiver     IN VARCHAR,
        p_in_parentNodeId      IN VARCHAR,
        p_in_promotionId       IN VARCHAR,
        p_in_languageCode      IN VARCHAR,
        p_in_sortColName       IN VARCHAR,
        p_in_sortedBy          IN VARCHAR,
        p_out_return_code      OUT NUMBER,
        p_out_data             OUT SYS_REFCURSOR )
IS
  c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_getBarchartResults';
  v_stage              VARCHAR2(500);
  v_parms               execution_log.text_line%TYPE;
BEGIN
   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_departments >' || p_in_departments
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';
         
  -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  
  
  v_stage := 'open p_out_data';
 OPEN p_out_data FOR
   SELECT s.*
    FROM (
    SELECT ROW_NUMBER() OVER (ORDER BY rs.b_cnt DESC, LOWER(rs.behavior)) AS rec_seq,
           rs.*
      FROM (SELECT cms.cms_name AS behavior, 
                   SUM(b_cnt) AS b_cnt --06/22/2018        
              FROM ( SELECT behavior, 
                            SUM(behavior_cnt) AS b_cnt
                       FROM (SELECT rpt.behavior, 
                                    behavior_cnt, 
                                    NVL (rpt.is_leaf, 0) is_leaf, 
                                    rpt.detail_node_id detail_node_id, 
                                    rh.node_name node_name 
                               FROM rpt_behavior_summary rpt, 
                                    rpt_hierarchy rh, 
                                    promotion p,
                                    gtt_id_list gil_hr, -- hierarchy rollup 
                                    gtt_id_list gil_dt,  -- department type
                                    gtt_id_list gil_pt,  -- position type
                                    gtt_id_list gil_p  -- promotion         
                              WHERE rpt.detail_node_id = rh.node_id 
                                AND rpt.record_type LIKE '%node%' 
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                AND gil_hr.id = rh.parent_node_id
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = p.promotion_id )
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = rpt.department ) 
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = rpt.job_position ) 
                               AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                   OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))
                                AND rpt.promotion_id = p.promotion_id 
                                AND rpt.giver_recvr_type = p_in_giverReceiver
                                AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                                AND rpt.pax_status = NVL (p_in_participantStatus, rpt.pax_status) 
                              UNION ALL 
                             SELECT rpt.behavior, 
                                    behavior_cnt, 
                                    NVL (rpt.is_leaf, 0) is_leaf, 
                                    rpt.detail_node_id detail_node_id, 
                                    rh.node_name || ' Team' node_name 
                               FROM rpt_behavior_summary rpt, 
                                    rpt_hierarchy rh, 
                                    promotion p,
                                    gtt_id_list gil_hr, -- hierarchy rollup 
                                    gtt_id_list gil_dt,  -- department type
                                    gtt_id_list gil_pt,  -- position type
                                    gtt_id_list gil_p  -- promotion  
                              WHERE rpt.detail_node_id = rh.node_id 
                                AND rpt.record_type LIKE '%team%' 
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                AND gil_hr.id = rh.node_id
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = p.promotion_id )
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = rpt.department ) 
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = rpt.job_position ) 
                               AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                   OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) 
                                AND rpt.giver_recvr_type = p_in_giverReceiver
                                AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                                AND rpt.pax_status = NVL (p_in_participantStatus,rpt.pax_status) 
                                AND rpt.promotion_id = p.promotion_id 
                            ) 
                            GROUP BY behavior              
                   ) rs, 
                   (SELECT cms_code, 
                            cms_name 
                       FROM mv_cms_code_value vw 
                      WHERE asset_code IN (gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = gc_default_language) 
                        AND NOT EXISTS (SELECT * FROM mv_cms_code_value WHERE asset_code IN ( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale =p_in_languageCode)
                   GROUP BY cms_code,
                            cms_name
                      UNION ALL 
                     SELECT cms_code, 
                            cms_name 
                       FROM mv_cms_code_value 
                      WHERE asset_code IN( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale = p_in_languageCode
                      GROUP BY cms_code,
                            cms_name
                   )cms    
             WHERE rs.behavior = cms.cms_code(+)
             GROUP BY cms.cms_name --06/22/2018
             ) rs
            ) s
           ORDER BY s.rec_seq;
           
  p_out_return_code := '00';
  
EXCEPTION
  WHEN OTHERS THEN
     p_out_return_code := '99';
     prc_execution_log_entry (c_process_name,1,'ERROR',v_stage || SQLERRM|| v_parms,NULL);
     OPEN p_out_data FOR SELECT NULL FROM DUAL;
END prc_getBarchartResults;
/* prc_getPiechartResults */
PROCEDURE prc_getPiechartResults(
        p_in_departments       IN VARCHAR,
        p_in_rowNumEnd         IN NUMBER,
        p_in_rowNumStart       IN NUMBER,
        p_in_jobPosition       IN VARCHAR,
        p_in_participantStatus IN VARCHAR,
        p_in_toDate            IN VARCHAR,
        p_in_localeDatePattern IN VARCHAR,
        p_in_fromDate          IN VARCHAR,
        p_in_giverReceiver     IN VARCHAR,
        p_in_parentNodeId      IN VARCHAR,
        p_in_promotionId       IN VARCHAR,
        p_in_languageCode      IN VARCHAR,
        p_in_sortColName       IN VARCHAR,
        p_in_sortedBy          IN VARCHAR,
        p_out_return_code      OUT NUMBER,
        p_out_data             OUT SYS_REFCURSOR )
IS
        
c_process_name       CONSTANT execution_log.process_name%TYPE := 'prc_getPiechartResults';
v_stage              VARCHAR2(500);
v_parms               execution_log.text_line%TYPE;

BEGIN
   v_stage := 'initialize variables';
   v_parms := '~Parms'
      ||  ': p_in_departments >' || p_in_departments
      || '<, p_in_rowNumEnd >' || p_in_rowNumEnd
      || '<, p_in_rowNumStart >' || p_in_rowNumStart
      || '<, p_in_jobPosition >' || p_in_jobPosition
      || '<, p_in_participantStatus >' || p_in_participantStatus
      || '<, p_in_toDate >' || p_in_toDate
      || '<, p_in_localeDatePattern >' || p_in_localeDatePattern
      || '<, p_in_fromDate >' || p_in_fromDate
      || '<, p_in_giverReceiver >' || p_in_giverReceiver
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortColName >' || p_in_sortColName
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';
   
  -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  
  
  v_stage := 'open p_out_data';
  OPEN p_out_data FOR
    SELECT s.*
    FROM (
    SELECT ROW_NUMBER() OVER (ORDER BY rs.b_cnt DESC, LOWER(rs.behavior)) AS rec_seq,
           rs.*
      FROM (SELECT cms.cms_name AS behavior, 
                   SUM(b_cnt) as b_cnt  --06/22/2018       
              FROM ( SELECT behavior, 
                            SUM(behavior_cnt) AS b_cnt
                       FROM (SELECT rpt.behavior, 
                                    behavior_cnt, 
                                    NVL (rpt.is_leaf, 0) is_leaf, 
                                    rpt.detail_node_id detail_node_id, 
                                    rh.node_name node_name 
                               FROM rpt_behavior_summary rpt, 
                                    rpt_hierarchy rh, 
                                    promotion p,
                                    gtt_id_list gil_hr, -- hierarchy rollup 
                                    gtt_id_list gil_dt,  -- department type
                                    gtt_id_list gil_pt,  -- position type
                                    gtt_id_list gil_p  -- promotion         
                              WHERE rpt.detail_node_id = rh.node_id 
                                AND rpt.record_type LIKE '%node%' 
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                AND gil_hr.id = rh.parent_node_id
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = p.promotion_id )
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = rpt.department ) 
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = rpt.job_position ) 
                               AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                   OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live'))
                                AND rpt.promotion_id = p.promotion_id 
                                AND rpt.giver_recvr_type = p_in_giverReceiver
                                AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                                AND rpt.pax_status = NVL (p_in_participantStatus, rpt.pax_status) 
                              UNION ALL 
                             SELECT rpt.behavior, 
                                    behavior_cnt, 
                                    NVL (rpt.is_leaf, 0) is_leaf, 
                                    rpt.detail_node_id detail_node_id, 
                                    rh.node_name || ' Team' node_name 
                               FROM rpt_behavior_summary rpt, 
                                    rpt_hierarchy rh, 
                                    promotion p,
                                    gtt_id_list gil_hr, -- hierarchy rollup 
                                    gtt_id_list gil_dt,  -- department type
                                    gtt_id_list gil_pt,  -- position type
                                    gtt_id_list gil_p  -- promotion  
                              WHERE rpt.detail_node_id = rh.node_id 
                                AND rpt.record_type LIKE '%team%' 
                                AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                AND gil_hr.id = rh.node_id
                                AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                AND (  gil_p.ref_text_2 = gc_search_all_values
                                    OR gil_p.id = p.promotion_id )
                                AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                AND (  gil_dt.ref_text_2 = gc_search_all_values
                                    OR gil_dt.ref_text_2 = rpt.department ) 
                                AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                AND (  gil_pt.ref_text_2 = gc_search_all_values
                                    OR gil_pt.ref_text_2 = rpt.job_position ) 
                               AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                   OR (p_in_promotionId IS NOT NULL AND p.promotion_status = 'live')) 
                                AND rpt.giver_recvr_type = p_in_giverReceiver
                                AND TRUNC (rpt.date_submitted) BETWEEN TO_DATE ( p_in_fromDate,p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern)
                                AND rpt.pax_status = NVL (p_in_participantStatus,rpt.pax_status) 
                                AND rpt.promotion_id = p.promotion_id 
                            ) 
                            GROUP BY behavior              
                   ) rs, 
                   (SELECT cms_code, 
                            cms_name 
                       FROM mv_cms_code_value vw 
                      WHERE asset_code IN (gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale = (SELECT string_val FROM os_propertyset WHERE entity_name = gc_default_language) 
                        AND NOT EXISTS (SELECT * FROM mv_cms_code_value WHERE asset_code IN ( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale =p_in_languageCode)
                   GROUP BY cms_code,
                            cms_name
                      UNION ALL 
                     SELECT cms_code, 
                            cms_name 
                       FROM mv_cms_code_value 
                      WHERE asset_code IN( gc_cms_list_nomi_behavior, gc_cms_list_reco_behavior)
                        AND locale = p_in_languageCode
                      GROUP BY cms_code,
                            cms_name
                   )cms    
             WHERE rs.behavior = cms.cms_code(+)
             GROUP BY cms.cms_name --06/22/2018
             ) rs
            ) s
           ORDER BY s.rec_seq;
           
    p_out_return_code := '00'; 
           
EXCEPTION
  WHEN OTHERS THEN
     p_out_return_code := '99';
     prc_execution_log_entry (c_process_name,1,'ERROR',v_stage || SQLERRM|| v_parms,NULL);
     OPEN p_out_data FOR SELECT NULL FROM DUAL;
END prc_getPiechartResults;

END pkg_query_behaviors_report;
/
CREATE OR REPLACE PACKAGE pkg_query_badge_activity
IS
  /******************************************************************************
  NAME:       pkg_query_badge_activity
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  07/22/2014           VINAY            1) Updated p_in_department to p_in_departments
                                        2) Updated the p_in_parentNodeId data type from NUMBER to VARCHAR
                                        
  08/01/2014           VINAY            1) Removed Promotion Column from prc_getPaxLevel procedure( Bug 53813 )                                   
  04/21/2016           Suresh J         New Nomination - Badge Report changes 
   01/13/2017           nagarajs         G56.3.3 report changes 
  ******************************************************************************/
PROCEDURE prc_getBadgesEarnedBarChart(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,   
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT SYS_REFCURSOR);
PROCEDURE prc_getActivityByOrgSummary(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,    
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_resultsCursor    OUT SYS_REFCURSOR,
    p_out_totalsCursor     OUT SYS_REFCURSOR);
PROCEDURE prc_getActivityTeamLevel(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,     
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_resultsCursor    OUT SYS_REFCURSOR,
    p_out_totalsCursor     OUT SYS_REFCURSOR);
PROCEDURE prc_getPaxLevel(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,    
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_resultsCursor    OUT SYS_REFCURSOR);
END pkg_query_badge_activity;
/

CREATE OR REPLACE PACKAGE BODY pkg_query_badge_activity
  /******************************************************************************
  NAME:       pkg_query_badge_activity
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  04/21/2016           Suresh J         New Nomination - Badge Report changes 
  ******************************************************************************/
IS

gc_search_all_values             CONSTANT VARCHAR2(30) := pkg_const.gc_search_all_values;
gc_ref_text_position_type        CONSTANT gtt_id_list.ref_text_1%TYPE := 'position_type';
gc_ref_text_department_type      CONSTANT gtt_id_list.ref_text_1%TYPE := 'department_type';
gc_ref_text_parent_node_id       CONSTANT gtt_id_list.ref_text_1%TYPE := 'parent_node_id';
gc_ref_text_promotion_id         CONSTANT gtt_id_list.ref_text_1%TYPE := 'promotion_id';
gc_cms_key_fld_country_name      CONSTANT cms_content_data.key%TYPE := 'COUNTRY_NAME'; 
gc_cms_key_fld_promotion_name    CONSTANT cms_content_data.key%TYPE := 'PROMOTION_NAME_'; 
gc_cms_key_fld_badge_name        CONSTANT cms_content_data.key%TYPE := 'HTML_KEY';

  /* getBadgesEarnedBarChartResults */
PROCEDURE prc_getBadgesEarnedBarChart(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,     
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_result_set       OUT SYS_REFCURSOR)
IS
/******************************************************************************
  NAME:       prc_getBadgesEarnedBarChart
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  04/21/2016           Suresh J         New Nomination - Badge Report changes
  01/13/2017           nagarajs         Rewritten query for G5.6.3.3 report changes 
  03/28/2018           Gorantla         G6-3990 Badge Activity Report Extract not pulling data for expired badges
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getBadgesEarnedBarChart' ;
  v_stage        VARCHAR2 (500);
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
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortedOn >' || p_in_sortedOn
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';

-- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  
   
  v_stage := 'open p_out_result_set';
  OPEN p_out_result_set FOR 
    SELECT s.*
      FROM (SELECT ROW_NUMBER() OVER (ORDER BY rs.badges_earned DESC, LOWER(rs.name)) AS rec_seq,
                   rs.name as node_name, 
                   rs.badges_earned
              FROM (SELECT rh.node_id,
                           rh.node_name|| ' Team' NAME,
                           NVL (team.badges_earned, 0) badges_earned
                      FROM rpt_hierarchy rh,
                           gtt_id_list gil_hr, -- hierarchy rollup 
                           (SELECT node_id AS NODE_ID,
                                   COUNT (1)     AS badges_earned
                             FROM rpt_badge_detail rad
                                  ,promotion p                                                                   
                                  , gtt_id_list gil_hr -- hierarchy rollup 
                                  , gtt_id_list gil_dt  -- department type
                                  , gtt_id_list gil_pt  -- position type
                                  , gtt_id_list gil_p  -- promotion                                                                   
                            WHERE rad.promotion_id = p.promotion_id   
                              AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                              AND gil_hr.id = rad.node_id
                              AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                              AND (  gil_p.ref_text_2 = gc_search_all_values
                                     OR gil_p.id = p.promotion_id )
                              AND gil_dt.ref_text_1 = gc_ref_text_department_type
                              AND (  gil_dt.ref_text_2 = gc_search_all_values
                                     OR gil_dt.ref_text_2 = rad.department ) 
                              AND gil_pt.ref_text_1 = gc_ref_text_position_type
                              AND (  gil_pt.ref_text_2 = gc_search_all_values
                                     OR gil_pt.ref_text_2 = rad.position_type ) 
                              AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                      OR (p_in_promotionId IS NOT NULL AND p.promotion_status IN('live','expired')))  -- 03/28/2018
                              AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
                              AND NVL (TRUNC (rad.earned_date), TRUNC (SYSDATE)) BETWEEN TO_DATE ( p_in_fromDate, p_in_localeDatePattern) AND TO_DATE ( p_in_toDate, p_in_localeDatePattern)
                            GROUP BY node_id
                           ) team
                     WHERE team.node_id(+) = rh.node_id
                       AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                       AND gil_hr.id = rh.node_id
                     UNION ALL
                    SELECT dtl.node_id,
                           dtl.node_name AS NAME,
                           badges_earned
                      FROM (SELECT node_id,
                                   parent_node_id
                              FROM rpt_hierarchy rad,
                                   gtt_id_list gil_hr -- hierarchy rollup 
                             WHERE gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                               AND gil_hr.id = rad.parent_node_id
                            ) rad,
                            (SELECT node_id,
                                    node_name,
                                    badges_earned badges_earned
                               FROM rpt_hierarchy rh,
                                     (SELECT npn.path_node_id,
                                             NVL(SUM (badges_earned),0) badges_earned
                                        FROM (SELECT child_node_id node_id,
                                                     node_id path_node_id
                                                FROM rpt_hierarchy_rollup
                                              ) npn,
                                             (SELECT rad.node_id,
                                                     COUNT(1) badges_earned
                                                FROM rpt_badge_detail rad
                                                     , promotion p 
                                                     , gtt_id_list gil_dt  -- department type  
                                                     , gtt_id_list gil_pt  -- position type
                                                     , gtt_id_list gil_p  -- promotion 
                                               WHERE rad.promotion_id = p.promotion_id 
                                                 AND gil_p.ref_text_1 = gc_ref_text_promotion_id  
                                                 AND (  gil_p.ref_text_2 = gc_search_all_values
                                                       OR gil_p.id = p.promotion_id )
                                                 AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                 AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                       OR gil_dt.ref_text_2 = rad.department ) 
                                                 AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                 AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                       OR gil_pt.ref_text_2 = rad.position_type ) 
                                                 AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                                      OR (p_in_promotionId IS NOT NULL AND p.promotion_status IN('live','expired')))  -- 03/28/2018
                                                 AND rad.participant_current_status = NVL( p_in_participantStatus, rad.participant_current_status)
                                                 AND NVL(TRUNC(rad.earned_date), TRUNC( SYSDATE) ) BETWEEN TO_DATE( p_in_fromDate, p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern )
                                               GROUP BY rad.node_id
                                             ) dtl
                                       WHERE dtl.node_id(+) = npn.node_id
                                       GROUP BY npn.path_node_id
                                      )
                                WHERE rh.node_id = path_node_id
                             ) dtl
                       WHERE p_in_nodeAndBelow          = 1
                         AND rad.node_id                  = dtl.node_id
                    ) RS
           ) s 
      ORDER BY s.rec_seq;
  
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM|| v_parms, NULL);
  OPEN p_out_result_set FOR SELECT NULL FROM DUAL;
END prc_getBadgesEarnedBarChart;
/* Tables */
/* getBadgeActivityByOrgSummaryTabularResults */
PROCEDURE prc_getActivityByOrgSummary(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,     
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_resultsCursor    OUT SYS_REFCURSOR,
    p_out_totalsCursor     OUT SYS_REFCURSOR)
IS

/******************************************************************************
  NAME:       prc_getActivityByOrgSummary
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014      Initial..(the queries were in Java before the release 5.4. So no comments available so far)   
 Sherif Basha           11/07/2016      Bug 69916 - Badges earned count is mismatch for minnesota when admin has drill down the data   
                                        (promotion_id filter is missing)                           
 01/13/2017           nagarajs          Rewritten query for G5.6.3.3 report changes
 03/10/2017           Suresh J          G6-1909 - Pagination is not working  
 03/28/2018           Gorantla          G6-3990 Badge Activity Report Extract not pulling data for expired badges  
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getActivityByOrgSummary' ;
  v_stage        VARCHAR2 (500);  
  v_data_sort    VARCHAR2(100);
  v_fetch_count  INTEGER;
  v_parms        execution_log.text_line%TYPE;
  
  CURSOR cur_query_ref IS
   SELECT CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq,
          CAST(NULL AS NUMBER) AS node_id,
          CAST(NULL AS VARCHAR2(4000)) AS node_name,
          CAST(NULL AS NUMBER) AS badges_earned          
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
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortedOn >' || p_in_sortedOn
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';
   
   -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
  -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;  
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1); 
   
   v_stage := 'open p_out_resultsCursor';
  OPEN p_out_resultsCursor FOR 
    SELECT s.*
      FROM (SELECT COUNT(rs.node_id) OVER() AS total_records,
                   ROW_NUMBER() OVER 
                   (ORDER BY
                   -- sort totals record first
                   DECODE(rs.node_id, NULL, 0, 99),
                   -- ascending sorts
                   DECODE(v_data_sort, 'badges_earned',      rs.badges_earned),
                   DECODE(v_data_sort, 'name',               LOWER(rs.name)),
                   -- descending sorts
                   DECODE(v_data_sort, 'desc/badges_earned', rs.badges_earned) DESC,
                   DECODE(v_data_sort, 'desc/name',          LOWER(rs.name)) DESC,
                    -- default sort fields
                     LOWER(rs.name),
                     rs.node_id
                   ) -1 AS rec_seq,
                   rs.*
              FROM (SELECT node_id,
                           name,
                           SUM(badges_earned) as badges_earned
                      FROM (SELECT rh.node_id,
                                   rh.node_name|| ' Team' NAME,
                                   NVL (team.badges_earned, 0) badges_earned
                              FROM rpt_hierarchy rh,
                                   gtt_id_list gil_hr, -- hierarchy rollup 
                                   (SELECT node_id AS NODE_ID,
                                           COUNT (1)     AS badges_earned
                                     FROM rpt_badge_detail rad
                                          ,promotion p                                                                   
                                          , gtt_id_list gil_hr -- hierarchy rollup 
                                          , gtt_id_list gil_dt  -- department type
                                          , gtt_id_list gil_pt  -- position type
                                          , gtt_id_list gil_p  -- promotion                                                                   
                                    WHERE rad.promotion_id = p.promotion_id   
                                      AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                      AND gil_hr.id = rad.node_id
                                      AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                                      AND (  gil_p.ref_text_2 = gc_search_all_values
                                             OR gil_p.id = p.promotion_id )
                                      AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                      AND (  gil_dt.ref_text_2 = gc_search_all_values
                                             OR gil_dt.ref_text_2 = rad.department ) 
                                      AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                      AND (  gil_pt.ref_text_2 = gc_search_all_values
                                             OR gil_pt.ref_text_2 = rad.position_type ) 
                                      AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                              OR (p_in_promotionId IS NOT NULL AND p.promotion_status IN('live','expired')))  -- 03/28/2018
                                      AND rad.participant_current_status = NVL ( p_in_participantStatus, rad.participant_current_status)
                                      AND NVL (TRUNC (rad.earned_date), TRUNC (SYSDATE)) BETWEEN TO_DATE ( p_in_fromDate, p_in_localeDatePattern) AND TO_DATE ( p_in_toDate, p_in_localeDatePattern)
                                    GROUP BY node_id
                                   ) team
                             WHERE team.node_id(+) = rh.node_id
                               AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                               AND gil_hr.id = rh.node_id
                             UNION ALL
                            SELECT dtl.node_id,
                                   dtl.node_name AS NAME,
                                   badges_earned
                              FROM (SELECT node_id,
                                           parent_node_id
                                      FROM rpt_hierarchy rad,
                                           gtt_id_list gil_hr -- hierarchy rollup 
                                     WHERE gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                                       AND gil_hr.id = rad.parent_node_id
                                    ) rad,
                                    (SELECT node_id,
                                            node_name,
                                            badges_earned badges_earned
                                       FROM rpt_hierarchy rh,
                                             (SELECT npn.path_node_id,
                                                     NVL(SUM (badges_earned),0) badges_earned
                                                FROM (SELECT child_node_id node_id,
                                                             node_id path_node_id
                                                        FROM rpt_hierarchy_rollup
                                                      ) npn,
                                                     (SELECT rad.node_id,
                                                             COUNT(1) badges_earned
                                                        FROM rpt_badge_detail rad
                                                             , promotion p 
                                                             , gtt_id_list gil_dt  -- department type  
                                                             , gtt_id_list gil_pt  -- position type
                                                             , gtt_id_list gil_p  -- promotion 
                                                       WHERE rad.promotion_id = p.promotion_id 
                                                         AND gil_p.ref_text_1 = gc_ref_text_promotion_id  
                                                         AND (  gil_p.ref_text_2 = gc_search_all_values
                                                               OR gil_p.id = p.promotion_id )
                                                         AND gil_dt.ref_text_1 = gc_ref_text_department_type
                                                         AND (  gil_dt.ref_text_2 = gc_search_all_values
                                                               OR gil_dt.ref_text_2 = rad.department ) 
                                                         AND gil_pt.ref_text_1 = gc_ref_text_position_type
                                                         AND (  gil_pt.ref_text_2 = gc_search_all_values
                                                               OR gil_pt.ref_text_2 = rad.position_type ) 
                                                         AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                                              OR (p_in_promotionId IS NOT NULL AND p.promotion_status IN('live','expired')))  -- 03/28/2018
                                                         AND rad.participant_current_status = NVL( p_in_participantStatus, rad.participant_current_status)
                                                         AND NVL(TRUNC(rad.earned_date), TRUNC( SYSDATE) ) BETWEEN TO_DATE( p_in_fromDate, p_in_localeDatePattern) AND TO_DATE ( p_in_toDate,p_in_localeDatePattern )
                                                       GROUP BY rad.node_id
                                                     ) dtl
                                               WHERE dtl.node_id(+) = npn.node_id
                                               GROUP BY npn.path_node_id
                                              )
                                        WHERE rh.node_id = path_node_id
                                     ) dtl
                               WHERE p_in_nodeAndBelow          = 1
                                 AND rad.node_id                  = dtl.node_id
                           ) d
                         GROUP BY GROUPING SETS
                                  ((),(node_id,
                                       name)) 
                    ) RS
           ) s 
     WHERE (  s.rec_seq = 0   -- totals record                      --03/10/2017
          OR -- reduce sequenced data set to just the output page's records
             (   s.rec_seq > p_in_rowNumStart
             AND s.rec_seq < p_in_rowNumEnd )
          )
      ORDER BY s.rec_seq;
  
  -- query totals data
   v_stage := 'FETCH p_out_resultsCursor totals record';
   FETCH p_out_resultsCursor INTO rec_query;
   v_fetch_count := p_out_resultsCursor%ROWCOUNT;

   v_stage := 'OPEN p_out_totalsCursor';
   OPEN p_out_totalsCursor FOR
   SELECT rec_query.badges_earned     AS badges_earned
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_resultsCursor NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_resultsCursor FOR
      SELECT CAST(NULL AS NUMBER) AS total_records,
              CAST(NULL AS NUMBER) AS rec_seq,
              CAST(NULL AS NUMBER) AS node_id,
              CAST(NULL AS VARCHAR2(4000)) AS node_name,
              CAST(NULL AS NUMBER) AS badges_earneds              
        FROM dual
       WHERE 0=1;
   END IF;
   
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM|| v_parms, NULL);
  OPEN p_out_resultsCursor FOR SELECT NULL FROM DUAL;
  OPEN p_out_totalsCursor FOR SELECT NULL FROM DUAL;
END prc_getActivityByOrgSummary;
/* getBadgeActivityTeamLevelTabularResults */
PROCEDURE prc_getActivityTeamLevel(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,  
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_resultsCursor    OUT SYS_REFCURSOR,
    p_out_totalsCursor     OUT SYS_REFCURSOR)
IS

/******************************************************************************
  NAME:       prc_getActivityTeamLevel
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014       Initial..(the queries were in Java before the release 5.4. So no comments available so far)                                 
  nagarajs             01/13/2017        Rewritten query for G5.6.3.3 report changes
  Gorantla             03/28/2018        G6-3990 Badge Activity Report Extract not pulling data for expired badges
 ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getActivityTeamLevel' ;
  v_stage        VARCHAR2 (500); 
  v_data_sort    VARCHAR2(100);
  v_fetch_count  INTEGER;  
  v_parms        execution_log.text_line%TYPE;
  
  CURSOR cur_query_ref IS
   SELECT CAST(NULL AS NUMBER) AS total_records,
          CAST(NULL AS NUMBER) AS rec_seq,
          CAST(NULL AS VARCHAR2(1000)) AS pax_name,
          CAST(NULL AS VARCHAR2(4000)) AS pax_country,
          CAST(NULL AS NUMBER) AS badges_earned,
          CAST(NULL AS NUMBER) AS pax_id          
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
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortedOn >' || p_in_sortedOn
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';
  
   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;   
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  
   
  -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;
   
  OPEN p_out_resultsCursor FOR
  SELECT s.*
  FROM (SELECT COUNT(rs.pax_id) OVER() AS total_records,
               ROW_NUMBER() OVER 
               (ORDER BY
               -- sort totals record first
               DECODE(rs.pax_id, NULL, 0, 99),
               -- ascending sorts
               DECODE(v_data_sort, 'badges_earned',      rs.badges_earned),
               DECODE(v_data_sort, 'pax_name',           LOWER(rs.pax_name)),
               DECODE(v_data_sort, 'pax_country',        LOWER(rs.pax_country)),
               -- descending sorts
               DECODE(v_data_sort, 'desc/badges_earned', rs.badges_earned) DESC,
               DECODE(v_data_sort, 'desc/pax_name',      LOWER(rs.pax_name)) DESC,
               DECODE(v_data_sort, 'desc/pax_country',   LOWER(rs.pax_country)) DESC,
                -- default sort fields
                 LOWER(rs.pax_name),
                 rs.pax_id
               ) -1 AS rec_seq,
               rs.*
          FROM (SELECT participant_name          AS pax_name,
                       country                   AS pax_country,
                       NVL(SUM(badges_earned),0) AS badges_earned,
                       pax_id                    AS pax_id
                  FROM (SELECT user_full_name    AS participant_name,
                               mv_cn.cms_value   AS COUNTRY,
                               1                 AS badges_earned,
                               rad.user_id       AS pax_id
                          FROM rpt_badge_detail rad,
                               rpt_hierarchy nh,
                               country c,
                               promotion p,    
                               gtt_id_list gil_hr,  -- hierarchy                                                          
                               gtt_id_list gil_dt,  -- department type  
                               gtt_id_list gil_pt,  -- position type
                               gtt_id_list gil_p,  -- promotion 
                               mv_cms_asset_value mv_cn --country        
                         WHERE rad.node_id  = nh.node_id
                           AND rad.country_id = c.country_id 
                           AND c.name_cm_key = mv_cn.key (+)
                           AND c.cm_asset_code = mv_cn.asset_code (+)
                           AND p_in_languageCode = mv_cn.locale (+)                                            
                           AND rad.promotion_id = p.promotion_id
                           AND gil_hr.ref_text_1 = gc_ref_text_parent_node_id 
                           AND gil_hr.id = rad.node_id
                           AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                           AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = p.promotion_id )
                           AND gil_dt.ref_text_1 = gc_ref_text_department_type
                           AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                           AND gil_pt.ref_text_1 = gc_ref_text_position_type
                           AND (  gil_pt.ref_text_2 = gc_search_all_values
                                  OR gil_pt.ref_text_2 = rad.position_type ) 
                           AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                 OR (p_in_promotionId IS NOT NULL AND p.promotion_status IN('live','expired')))  -- 03/28/2018         
                           AND rad.participant_current_status = NVL( p_in_participantStatus, rad.participant_current_status)
                           AND NVL(TRUNC(earned_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                       )
                 GROUP BY GROUPING SETS
                      ((),( participant_name,
                            country,
                            pax_id))
               ) rs
        ) s;
  -- query totals data
   v_stage := 'FETCH p_out_resultsCursor totals record';
   FETCH p_out_resultsCursor INTO rec_query;
   v_fetch_count := p_out_resultsCursor%ROWCOUNT;

   v_stage := 'OPEN p_out_totalsCursor';
   OPEN p_out_totalsCursor FOR
   SELECT rec_query.badges_earned     AS badges_earned
     FROM dual
    WHERE v_fetch_count = 1;

   v_stage := 'OPEN p_out_resultsCursor NULL record';
   IF (v_fetch_count = 0) THEN
      -- reset data cursor with a null record
      OPEN p_out_resultsCursor FOR
      SELECT CAST(NULL AS NUMBER) AS total_records,
              CAST(NULL AS NUMBER) AS rec_seq,
              CAST(NULL AS VARCHAR2(100)) AS pax_name,
              CAST(NULL AS VARCHAR2(4000)) AS pax_country,
              CAST(NULL AS NUMBER) AS badges_earneds,
              CAST(NULL AS NUMBER) AS pax_id                
        FROM dual
       WHERE 0=1;
   END IF;-- query totals data
  
  p_out_return_code :='00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM|| v_parms, NULL);
  OPEN p_out_resultsCursor FOR SELECT NULL FROM DUAL;
  OPEN p_out_totalsCursor FOR SELECT NULL FROM DUAL;
END prc_getActivityTeamLevel;
/* getBadgeActivityParticipantLevelTabularResults */
PROCEDURE prc_getPaxLevel(
    p_in_userId            IN NUMBER,
    p_in_promotionId       IN VARCHAR2,  
    p_in_languageCode      IN VARCHAR,
    p_in_rowNumEnd         IN NUMBER,
    p_in_rowNumStart       IN NUMBER,
    p_in_toDate            IN VARCHAR,
    p_in_localeDatePattern IN VARCHAR,
    p_in_fromDate          IN VARCHAR,
    p_in_participantStatus IN VARCHAR,
    p_in_jobPosition       IN VARCHAR,
    p_in_departments       IN VARCHAR,
    p_in_parentNodeId      IN VARCHAR,
    p_in_nodeAndBelow      IN VARCHAR,
    p_in_sortedOn          IN VARCHAR,
    p_in_sortedBy          IN VARCHAR,
    p_out_return_code      OUT NUMBER,
    p_out_resultsCursor    OUT SYS_REFCURSOR)
IS
 /******************************************************************************
  NAME:       prc_getPaxLevel
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
  Ravi Dhanekula        07/02/2014       Initial..(the queries were in Java before the release 5.4. So no comments available so far)                                 
  nagarajs             01/13/2017        Rewritten query for G5.6.3.3 report changes
  Gorantla             03/28/2018        G6-3990 Badge Activity Report Extract not pulling data for expired badges
  ******************************************************************************/
  c_process_name CONSTANT execution_log.process_name%TYPE := 'prc_getPaxLevel' ;
  v_stage        VARCHAR2 (500);
  v_data_sort    VARCHAR2 (100);
  v_parms        execution_log.text_line%TYPE;
  
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
      || '<, p_in_parentNodeId >' || p_in_parentNodeId
      || '<, p_in_promotionId >' || p_in_promotionId
      || '<, p_in_userId >' || p_in_userId
      || '<, p_in_languageCode >' || p_in_languageCode              
      || '<, p_in_sortedOn >' || p_in_sortedOn
      || '<, p_in_sortedBy >' || p_in_sortedBy
      || '<';
  
  -- set data sort
   IF (LOWER(p_in_sortedBy) = 'desc') THEN
      v_data_sort := LOWER(p_in_sortedBy || '/' || p_in_sortedOn);
   ELSE
      v_data_sort := LOWER(p_in_sortedOn);
   END IF;  

   -- stage input lists
   v_stage := 'stage input lists';
   DELETE gtt_id_list;  
   pkg_report_common.p_stage_search_criteria(NVL(p_in_parentNodeId, pkg_report_common.f_get_root_node), gc_ref_text_parent_node_id, 1); 
   pkg_report_common.p_stage_search_criteria(NVL(p_in_departments, gc_search_all_values), gc_ref_text_department_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_jobPosition, gc_search_all_values), gc_ref_text_position_type);
   pkg_report_common.p_stage_search_criteria(NVL(p_in_promotionId, gc_search_all_values), gc_ref_text_promotion_id, 1);  
   
  OPEN p_out_resultsCursor FOR
  SELECT s.*
  FROM (SELECT COUNT(rs.participant_name) OVER() AS total_records,
               ROW_NUMBER() OVER 
               (ORDER BY
               -- ascending sorts
               DECODE(v_data_sort, 'earned_date',               rs.earned_date),
               DECODE(v_data_sort, 'participant_name',          LOWER(rs.participant_name)),
               DECODE(v_data_sort, 'org_name',                  LOWER(rs.org_name)),
               DECODE(v_data_sort, 'badge_name',                LOWER(rs.badge_name)),
               DECODE(v_data_sort, 'promotion_name',            LOWER(rs.promotion_name)),
               -- descending sortss
               DECODE(v_data_sort, 'desc/earned_date',          rs.earned_date) DESC,
               DECODE(v_data_sort, 'desc/participant_name',     LOWER(rs.participant_name)) DESC,
               DECODE(v_data_sort, 'desc/org_name',             LOWER(rs.org_name)) DESC,
               DECODE(v_data_sort, 'desc/badge_name',           LOWER(rs.badge_name)) DESC,
               DECODE(v_data_sort, 'desc/promotion_name',       LOWER(rs.promotion_name)) DESC,
                -- default sort fields
                 LOWER(rs.participant_name),
                 rs.org_name
               ) -1 AS rec_seq,
               rs.*
          FROM (SELECT earned_date,
                       participant_name ,
                       node_name as org_name,
                       badge_name,
                       promotion_name 
                  FROM (SELECT TRUNC(earned_date) AS earned_date,
                               user_full_name    AS participant_name,
                               nh.node_name,
                               mv_pn.cms_value   AS promotion_name,
                               mv_bn.cms_value   AS badge_name
                          FROM rpt_badge_detail rad,
                               rpt_hierarchy nh,
                               promotion p,                                                           
                               gtt_id_list gil_dt,  -- department type  
                               gtt_id_list gil_pt,  -- position type
                               gtt_id_list gil_p,  -- promotion 
                               mv_cms_asset_value mv_pn, --promotion 
                               mv_cms_asset_value mv_bn --promotion        
                         WHERE rad.node_id  = nh.node_id 
                           AND p.promo_name_asset_code       = mv_pn.asset_code (+)
                           AND gc_cms_key_fld_promotion_name = mv_pn.key (+)
                           AND p_in_languageCode             = mv_pn.locale (+)   
                           AND rad.badge_name                = mv_bn.asset_code (+)
                           AND gc_cms_key_fld_badge_name     = mv_bn.key (+)
                           AND p_in_languageCode             = mv_bn.locale (+)                                            
                           AND rad.promotion_id = p.promotion_id
                           AND rad.user_id = NVL(p_in_userId, rad.user_id) 
                           AND gil_p.ref_text_1 = gc_ref_text_promotion_id
                           AND (  gil_p.ref_text_2 = gc_search_all_values
                                  OR gil_p.id = p.promotion_id )
                           AND gil_dt.ref_text_1 = gc_ref_text_department_type
                           AND (  gil_dt.ref_text_2 = gc_search_all_values
                                  OR gil_dt.ref_text_2 = rad.department ) 
                           AND gil_pt.ref_text_1 = gc_ref_text_position_type
                           AND (  gil_pt.ref_text_2 = gc_search_all_values
                                  OR gil_pt.ref_text_2 = rad.position_type ) 
                           AND ((p_in_promotionId IS NULL AND p.promotion_status IN('live','expired')) 
                                 OR (p_in_promotionId IS NOT NULL AND p.promotion_status IN('live','expired')))  -- 03/28/2018         
                           AND rad.participant_current_status = NVL( p_in_participantStatus, rad.participant_current_status)
                           AND NVL(TRUNC(earned_date), TRUNC(SYSDATE)) BETWEEN TO_DATE(p_in_fromDate,p_in_localeDatePattern) AND TO_DATE(p_in_toDate,p_in_localeDatePattern)
                       )
               ) rs
        ) s;
        
  p_out_return_code := '00';
EXCEPTION
WHEN OTHERS THEN
  p_out_return_code := '99';
  prc_execution_log_entry (c_process_name, 1, 'ERROR', v_stage || SQLERRM|| v_parms, NULL);
  OPEN p_out_resultsCursor FOR SELECT NULL FROM DUAL;
END prc_getPaxLevel;
END pkg_query_badge_activity;
/
CREATE OR REPLACE PACKAGE pkg_query_challengepoint AS
/******************************************************************************
   NAME:       pkg_query_challenge_point
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        7/31/2014      keshaboi       1. Created this package.
              09/04/2014     poddutur       1. Added alias name (manager_name)
             09/30/2014    Suresh J        Bug 57052 - CP Selection Charts are not matching with Summary Total   
******************************************************************************/
/* getChallengePointProgressTabularResults  */
  PROCEDURE prc_getCPProgressTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getChallengePointProgressDetailResults  */
 PROCEDURE prc_getCPProgressDetailRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getProgressToGoalChart  */
    PROCEDURE prc_getCPToGoalChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointProgressResultsChart  */
     PROCEDURE prc_getCPProgressResChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);    
      
      /* getChallengePointAchievementTabularResults  */
     PROCEDURE prc_getCPAchievementTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getChallengePointAchievementDetailResults  */
 PROCEDURE prc_getCPAchievementDetailRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getChallengePointAchievementPercentageAchievedChart  */
 PROCEDURE prc_getCPPctAchievedChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointAchievementCountAchievedChart  */
 PROCEDURE prc_getCPCountAchievedChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointAchievementResultsChart  */
 PROCEDURE prc_getCPAchievementResChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointSelectionTabularResults  */
 PROCEDURE prc_getCPSelectionTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getChallengePointSelectionDetailResults  */
    PROCEDURE prc_getCPSelectionDetailRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER);
             
      /* getChallengePointSelectionValidLevelNumbers  */
 PROCEDURE prc_getCPSelectionValidLvlNums (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointSelectionTotalsChart  */
 PROCEDURE prc_getCPSelectionTotalsChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getSelectionPercentageChart  */
 PROCEDURE prc_getCPSelectionPctChart(
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointSelectionByOrgChart  */
 PROCEDURE prc_getCPSelectionByOrgChart(
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);   
        
/* getChallengePointManagerDetailTabularResults  */
 PROCEDURE prc_getCPManagerDetailTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getChallengePointManagerTabularResults  */
PROCEDURE prc_getCPManagerTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
      /* getManagerTotalPointsEarnedChart  */
PROCEDURE prc_getMgrTotalPtsEarnedChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointProgramSummaryTabularResults  */
PROCEDURE prc_getCPProgramSummaryTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR, 
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR);
      
     /* getChallengePointGoalAchievementChart  */
PROCEDURE prc_getCPGoalAchievementChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
      
      /* getChallengePointIncrementalChart  */
PROCEDURE prc_getCPIncrementalChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR);
    
END pkg_query_challengepoint;
/
CREATE OR REPLACE PACKAGE BODY pkg_query_challengepoint
AS
/* getChallengePointProgressTabularResults  */
  PROCEDURE prc_getCPProgressTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPProgressTabRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPProgressTabRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROW_NUMBER() OVER(ORDER BY node_name ASC) RN, 
     rs.* 
     FROM 
     (SELECT NVL(SUM(total_selected),0)                     AS total_selected, 
     SUM(NVL(nbr_threshold_reached,0))                        AS nbr_threshold_reached, 
     NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,  
     SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))                   AS sum_nbr_pax_25_percent, 
     SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))                   AS sum_nbr_pax_50_percent,  
     SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))                   AS sum_nbr_pax_75_percent,  
     SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))                AS sum_nbr_pax_76_99_percent,  
     SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))                  AS sum_nbr_pax_100_percent,  
     SUM(NVL(base,0))                                       AS base,  
     SUM(NVL(goal,0))                                       AS goal,  
     SUM(NVL(actual_result,0))                              AS actual_result, 
     CASE WHEN SUM(NVL(goal,0)) = 0 THEN 0 
     ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2) 
     END                                                    AS percent_of_goal, 
     rh.node_name                                           NODE_NAME,  
     rh.node_type_name NODE_TYPE_NAME,  
     0 IS_LEAF,  
     rh.parent_node_id PARENT_NODE_ID, 
     rh.header_node_id HEADER_NODE_ID,  
     rh.detail_node_id DETAIL_NODE_ID,  
     rh.hier_level HIER_LEVEL  
     FROM (SELECT DISTINCT rpt.record_type, 
     rh.node_name,  
     rh.node_type_name,  
     rpt.is_leaf,  
     rh.parent_node_id, 
     rpt.header_node_id,  
     rpt.detail_node_id,  
     rpt.hier_level   
     FROM rpt_hierarchy rh, 
     rpt_cp_selection_summary rpt 
     WHERE detail_node_id                            = rh.node_id 
     AND rpt.record_type LIKE ''%node%''  
     AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) )) 
     ) rh, 
     (SELECT total_selected , 
     level_id, 
     nbr_threshold_reached, 
     nbr_pax_25_percent_of_cp , 
     nbr_pax_50_percent_of_cp, 
     nbr_pax_75_percent_of_cp, 
     nbr_pax_76_99_percent_of_cp, 
     nbr_pax_100_percent_of_cp, 
     rpt.base_quantity base,  
     rpt.goal,  
     rpt.actual_result, 
     rh.node_id AS node_id,  
     rh.node_name node_name  
     FROM  
     rpt_hierarchy rh, 
     promotion p,  
     promo_challengepoint promoGoal, 
     rpt_cp_selection_summary rpt , 
     goalquest_goallevel gl  
     WHERE rpt.level_id                               = gl.goallevel_id (+) 
     AND rpt.promotion_id                            = promoGoal.promotion_id (+) 
     AND rpt.promotion_id                              = p.promotion_id (+) 
     AND detail_node_id                            = rh.node_id  
     AND rpt.record_type LIKE ''%node%''  
     AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) )) 
     AND rpt.pax_status                                  = NVL('''||p_in_participantStatus||''',rpt.pax_status) 
     AND  NVL(rpt.job_position,''JOB'')                                = NVL('''||p_in_jobPosition||''',NVL(rpt.job_position,''JOB''))  
     AND (('''||p_in_departments||''' IS NULL) OR ('''||p_in_departments||''' is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')))))  
     AND (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')))))  
     AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))
     ) rpc 
     WHERE rh.detail_node_id                            = rpc.node_id (+) 
     GROUP BY rh.record_type,  
     rh.node_name,  
     rh.node_type_name,  
     rh.is_leaf,  
     rh.parent_node_id, 
     rh.header_node_id,  
     rh.detail_node_id,  
     rh.hier_level  
     UNION  
     SELECT NVL(SUM(total_selected),0)                     AS total_selected, 
     SUM(NVL(nbr_threshold_reached,0))                        AS nbr_threshold_reached, 
     NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,  
     SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))            AS sum_nbr_pax_25_percent,  
     SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))            AS sum_nbr_pax_50_percent,  
     SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))            AS sum_nbr_pax_75_percent,  
     SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))         AS sum_nbr_pax_76_99_percent,  
     SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))           AS sum_nbr_pax_100_percent,  
     SUM(NVL(base,0))                                AS base,  
     SUM(NVL(goal,0))                                AS goal,  
     SUM(NVL(actual_result,0))                       AS actual_result, 
     CASE WHEN SUM(NVL(goal,0)) = 0 THEN 0 
     ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2) 
     END AS percent_of_goal, 
     DECODE (instr(rh.record_type,''team''),0,rh.node_name,rh.node_name 
     || '' Team'')AS NODE_NAME,  
     rh.node_type_name node_type_name, 
     1 is_leaf,  
     rh.parent_node_id parent_node_id, 
     rh.header_node_id header_node_id,  
     rh.detail_node_id detail_node_id,  
     rh.hier_level hier_level  
     FROM (SELECT DISTINCT rpt.record_type, 
     rh.node_name,  
     rh.node_type_name,  
     rpt.is_leaf,  
     rh.parent_node_id, 
     rpt.header_node_id,  
     rpt.detail_node_id,  
     rpt.hier_level  
     FROM rpt_hierarchy rh, 
     rpt_cp_selection_summary rpt 
     WHERE detail_node_id                            = rh.node_id 
     AND rpt.record_type LIKE ''%team%''  
     AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) )) 
     ) rh, 
     (SELECT total_selected , 
     level_id, 
     nbr_threshold_reached, 
     nbr_pax_25_percent_of_cp , 
     nbr_pax_50_percent_of_cp, 
     nbr_pax_75_percent_of_cp, 
     nbr_pax_76_99_percent_of_cp, 
     nbr_pax_100_percent_of_cp, 
     rpt.base_quantity base,  
     rpt.goal,  
     rpt.actual_result, 
     rh.node_id AS node_id,  
     rh.node_name node_name  
     FROM  
     rpt_hierarchy rh, 
     promotion p,  
     promo_challengepoint promoGoal, 
     rpt_cp_selection_summary rpt , 
     goalquest_goallevel gl  
     WHERE rpt.level_id                               = gl.goallevel_id (+) 
     AND rpt.promotion_id                            = promoGoal.promotion_id (+) 
     AND rpt.promotion_id                              = p.promotion_id (+) 
     AND detail_node_id                            = rh.node_id  
      AND rpt.record_type LIKE ''%team%''  
     AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) )) 
     AND rpt.pax_status                                  = NVL('''||p_in_participantStatus||''',rpt.pax_status) 
     AND  NVL(rpt.job_position,''JOB'')                                = NVL('''||p_in_jobPosition||''',NVL(rpt.job_position,''JOB''))  
     AND (('''||p_in_departments||''' IS NULL) OR ('''||p_in_departments||''' is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')))))  
     AND (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')))))  
     AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' '')) 
     ) rpc 
     WHERE rh.detail_node_id                            = rpc.node_id (+) and rh.is_leaf = 1 
     GROUP BY rh.record_type,  
     rh.node_name,  
     rh.node_type_name,  
     rh.is_leaf,  
     rh.parent_node_id, 
     rh.header_node_id,  
     rh.detail_node_id,  
     rh.hier_level  
     ) rs  
     ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getChallengePointProgressTabularResultsSize  */
     --OPEN p_out_size_data FOR
     SELECT count(1) INTO p_out_size_data --AS MAX_SIZE
     FROM 
     (SELECT ROW_NUMBER() OVER(ORDER BY node_name ASC) RN, 
     rs.* 
     FROM 
     (SELECT NVL(SUM(total_selected),0)                     AS total_selected, 
     SUM(NVL(nbr_threshold_reached,0))                        AS nbr_threshold_reached, 
     NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,  
     SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))                   AS sum_nbr_pax_25_percent, 
     SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))                   AS sum_nbr_pax_50_percent,  
     SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))                   AS sum_nbr_pax_75_percent,  
     SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))                AS sum_nbr_pax_76_99_percent,  
     SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))                  AS sum_nbr_pax_100_percent,  
     SUM(NVL(base,0))                                       AS base,  
     SUM(NVL(goal,0))                                       AS goal,  
     SUM(NVL(actual_result,0))                              AS actual_result, 
     CASE WHEN SUM(NVL(goal,0)) = 0 THEN 0 
     ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2) 
     END                                                    AS percent_of_goal, 
     rh.node_name                                           NODE_NAME,  
     rh.node_type_name NODE_TYPE_NAME,  
     0 IS_LEAF,  
     rh.parent_node_id PARENT_NODE_ID, 
     rh.header_node_id HEADER_NODE_ID,  
     rh.detail_node_id DETAIL_NODE_ID,  
     rh.hier_level HIER_LEVEL  
     FROM (SELECT DISTINCT rpt.record_type, 
     rh.node_name,  
     rh.node_type_name,  
     rpt.is_leaf,  
     rh.parent_node_id, 
     rpt.header_node_id,  
     rpt.detail_node_id,  
     rpt.hier_level   
     FROM rpt_hierarchy rh, 
     rpt_cp_selection_summary rpt 
     WHERE detail_node_id                            = rh.node_id 
     AND rpt.record_type LIKE '%node%'  
     AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     ) rh, 
     (SELECT total_selected , 
     level_id, 
     nbr_threshold_reached, 
     nbr_pax_25_percent_of_cp , 
     nbr_pax_50_percent_of_cp, 
     nbr_pax_75_percent_of_cp, 
     nbr_pax_76_99_percent_of_cp, 
     nbr_pax_100_percent_of_cp, 
     rpt.base_quantity base,  
     rpt.goal,  
     rpt.actual_result, 
     rh.node_id AS node_id,  
     rh.node_name node_name  
     FROM  
     rpt_hierarchy rh, 
     promotion p,  
     promo_challengepoint promoGoal, 
     rpt_cp_selection_summary rpt , 
     goalquest_goallevel gl  
     WHERE rpt.level_id                               = gl.goallevel_id (+) 
     AND rpt.promotion_id                            = promoGoal.promotion_id (+) 
     AND rpt.promotion_id                              = p.promotion_id (+) 
     AND detail_node_id                            = rh.node_id  
     AND rpt.record_type LIKE '%node%'  
     AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     AND rpt.pax_status                                  = NVL(p_in_participantStatus,rpt.pax_status) 
     AND  NVL(rpt.job_position,'JOB')                                = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))  
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))  
     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
     ) rpc 
     WHERE rh.detail_node_id                            = rpc.node_id (+) 
     GROUP BY rh.record_type,  
     rh.node_name,  
     rh.node_type_name,  
     rh.is_leaf,  
     rh.parent_node_id, 
     rh.header_node_id,  
     rh.detail_node_id,  
     rh.hier_level  
     UNION  
     SELECT NVL(SUM(total_selected),0)                     AS total_selected, 
     SUM(NVL(nbr_threshold_reached,0))                        AS nbr_threshold_reached, 
     NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,  
     SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))            AS sum_nbr_pax_25_percent,  
     SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))            AS sum_nbr_pax_50_percent,  
     SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))            AS sum_nbr_pax_75_percent,  
     SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))         AS sum_nbr_pax_76_99_percent,  
     SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))           AS sum_nbr_pax_100_percent,  
     SUM(NVL(base,0))                                AS base,  
     SUM(NVL(goal,0))                                AS goal,  
     SUM(NVL(actual_result,0))                       AS actual_result, 
     CASE WHEN SUM(NVL(goal,0)) = 0 THEN 0 
     ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2) 
     END AS percent_of_goal, 
     DECODE (instr(rh.record_type,'team'),0,rh.node_name,rh.node_name 
     || ' Team')AS NODE_NAME,  
     rh.node_type_name node_type_name, 
     1 is_leaf,  
     rh.parent_node_id parent_node_id, 
     rh.header_node_id header_node_id,  
     rh.detail_node_id detail_node_id,  
     rh.hier_level hier_level  
     FROM (SELECT DISTINCT rpt.record_type, 
     rh.node_name,  
     rh.node_type_name,  
     rpt.is_leaf,  
     rh.parent_node_id, 
     rpt.header_node_id,  
     rpt.detail_node_id,  
     rpt.hier_level  
     FROM rpt_hierarchy rh, 
     rpt_cp_selection_summary rpt 
     WHERE detail_node_id                            = rh.node_id 
     AND rpt.record_type LIKE '%team%'  
     AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     ) rh, 
     (SELECT total_selected , 
     level_id, 
     nbr_threshold_reached, 
     nbr_pax_25_percent_of_cp , 
     nbr_pax_50_percent_of_cp, 
     nbr_pax_75_percent_of_cp, 
     nbr_pax_76_99_percent_of_cp, 
     nbr_pax_100_percent_of_cp, 
     rpt.base_quantity base,  
     rpt.goal,  
     rpt.actual_result, 
     rh.node_id AS node_id,  
     rh.node_name node_name  
     FROM  
     rpt_hierarchy rh, 
     promotion p,  
     promo_challengepoint promoGoal, 
     rpt_cp_selection_summary rpt , 
     goalquest_goallevel gl  
     WHERE rpt.level_id                               = gl.goallevel_id (+) 
     AND rpt.promotion_id                            = promoGoal.promotion_id (+) 
     AND rpt.promotion_id                              = p.promotion_id (+) 
     AND detail_node_id                            = rh.node_id  
     AND rpt.record_type LIKE '%team%'  
     AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     AND rpt.pax_status                                  = NVL(p_in_participantStatus,rpt.pax_status) 
     AND  NVL(rpt.job_position,'JOB')                                = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))  
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))  
     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
     ) rpc 
     WHERE rh.detail_node_id                            = rpc.node_id (+) and rh.is_leaf = 1 
     GROUP BY rh.record_type,  
     rh.node_name,  
     rh.node_type_name,  
     rh.is_leaf,  
     rh.parent_node_id, 
     rh.header_node_id,  
     rh.detail_node_id,  
     rh.hier_level  
     ) rs  
       ) 
     WHERE RN > p_in_rowNumStart 
     AND RN   < p_in_rowNumEnd;
/* getChallengePointProgressTabularResultsTotals  */
     OPEN p_out_totals_data FOR
     SELECT NVL(SUM(total_selected),0) total_selected,   
          NVL ( (SUM(total_selected) - SUM(all_level_selected)), 0) no_goal_selected,  
          NVL(SUM(all_level_selected),0) all_level_selected,   
          NVL(SUM(nbr_threshold_reached),0) nbr_threshold_reached,   
          NVL(SUM(sum_nbr_pax_25_percent),0) sum_nbr_pax_25_percent,   
          NVL(SUM(sum_nbr_pax_50_percent),0) sum_nbr_pax_50_percent,   
          NVL(SUM(sum_nbr_pax_75_percent),0) sum_nbr_pax_75_percent,   
          NVL(SUM(sum_nbr_pax_76_99_percent),0) sum_nbr_pax_76_99_percent,   
          NVL(SUM(sum_nbr_pax_100_percent),0) sum_nbr_pax_100_percent, 
          NVL(SUM(base),0)                                       AS base,   
               NVL(SUM(goal),0)                                       AS goal,   
               NVL(SUM(actual_result),0)                              AS actual_result,  
               CASE WHEN NVL(SUM(goal),0) = 0 THEN 0  
               ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2)  
               END                                                    AS percent_of_goal 
          FROM   
          (SELECT NVL(SUM(total_selected),0)                     AS total_selected,   
              NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,   
              SUM(NVL(nbr_threshold_reached,0))                      AS nbr_threshold_reached, 
              SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))                   AS sum_nbr_pax_25_percent,   
              SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))                   AS sum_nbr_pax_50_percent,   
              SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))                   AS sum_nbr_pax_75_percent,   
              SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))                AS sum_nbr_pax_76_99_percent,   
              SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))                  AS sum_nbr_pax_100_percent,   
               SUM(NVL(base,0))                                       AS base,   
               SUM(NVL(goal,0))                                       AS goal,   
               SUM(NVL(actual_result,0))                              AS actual_result,  
               CASE WHEN SUM(NVL(goal,0)) = 0 THEN 0  
               ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2)  
               END                                                    AS percent_of_goal,  
              rh.node_name                                           NODE_NAME,   
              rh.node_type_name NODE_TYPE_NAME,   
              0 IS_LEAF,   
              rh.parent_node_id PARENT_NODE_ID,  
              rh.header_node_id HEADER_NODE_ID,   
              rh.detail_node_id DETAIL_NODE_ID,   
              rh.hier_level HIER_LEVEL   
            FROM (SELECT DISTINCT rpt.record_type,  
                         rh.node_name,   
                         rh.node_type_name,   
                         rpt.is_leaf,   
                         rh.parent_node_id,  
                         rpt.header_node_id,   
                         rpt.detail_node_id,   
                         rpt.hier_level    
                     FROM rpt_hierarchy rh,   
                          rpt_cp_selection_summary rpt  
                    WHERE detail_node_id                            = rh.node_id  
                      AND rpt.record_type LIKE '%node%'   
                      AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))  
                   ) rh,  
             (SELECT total_selected ,  
                level_id,  
                nbr_threshold_reached,  
                nbr_pax_25_percent_of_cp ,  
                nbr_pax_50_percent_of_cp,  
                nbr_pax_75_percent_of_cp,  
                nbr_pax_76_99_percent_of_cp,  
                nbr_pax_100_percent_of_cp,  
                rpt.base_quantity base,   
                rpt.goal,   
                rpt.actual_result,  
                rh.node_id AS node_id,   
                rh.node_name node_name   
                FROM   
                rpt_hierarchy rh,  
                promotion p,   
                promo_challengepoint promoGoal,  
                rpt_cp_selection_summary rpt ,  
                goalquest_goallevel gl   
              WHERE rpt.level_id                               = gl.goallevel_id (+)  
              AND rpt.promotion_id                            = promoGoal.promotion_id (+)  
              AND rpt.promotion_id                              = p.promotion_id (+)  
              AND detail_node_id                            = rh.node_id   
              AND rpt.record_type LIKE '%node%'   
              AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))  
              AND rpt.pax_status                                  = NVL(p_in_participantStatus,rpt.pax_status)  
              AND  NVL(rpt.job_position,'JOB')                                = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))  
              AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))   
              AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))   
              AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))    
              ) rpc  
             WHERE rh.detail_node_id                            = rpc.node_id (+)  
            GROUP BY rh.record_type,   
              rh.node_name,   
              rh.node_type_name,   
              rh.is_leaf,   
              rh.parent_node_id,  
              rh.header_node_id,   
              rh.detail_node_id,   
              rh.hier_level   
            UNION   
            SELECT NVL(SUM(total_selected),0)                     AS total_selected,  
              NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,   
              SUM(NVL(nbr_threshold_reached,0))               AS nbr_threshold_reached, 
              SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))            AS sum_nbr_pax_25_percent,   
              SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))            AS sum_nbr_pax_50_percent,   
              SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))            AS sum_nbr_pax_75_percent,   
              SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))         AS sum_nbr_pax_76_99_percent,   
              SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))           AS sum_nbr_pax_100_percent, 
              SUM(NVL(base,0))                                       AS base,   
               SUM(NVL(goal,0))                                       AS goal,   
               SUM(NVL(actual_result,0))                              AS actual_result,  
               CASE WHEN SUM(NVL(goal,0)) = 0 THEN 0  
               ELSE ROUND((SUM(NVL(actual_result,0)) / SUM(goal)) * 100, 2)  
               END                                                    AS percent_of_goal,   
              DECODE (instr(rh.record_type,'team'),0,rh.node_name,rh.node_name   
              || ' Team')AS NODE_NAME,   
              rh.node_type_name node_type_name,  
              1 is_leaf,   
              rh.parent_node_id parent_node_id,  
              rh.header_node_id header_node_id,   
              rh.detail_node_id detail_node_id,   
              rh.hier_level hier_level   
            FROM (SELECT DISTINCT rpt.record_type,  
                         rh.node_name,   
                         rh.node_type_name,  
                         rpt.is_leaf,   
                         rh.parent_node_id,  
                         rpt.header_node_id,   
                         rpt.detail_node_id,   
                         rpt.hier_level   
                     FROM rpt_hierarchy rh,   
                          rpt_cp_selection_summary rpt  
                    WHERE detail_node_id                            = rh.node_id  
                      AND rpt.record_type LIKE '%team%'   
                      AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))  
                   ) rh,  
             (SELECT total_selected ,  
                 level_id,  
                 nbr_threshold_reached,  
                nbr_pax_25_percent_of_cp ,  
                nbr_pax_50_percent_of_cp,  
                nbr_pax_75_percent_of_cp,  
                nbr_pax_76_99_percent_of_cp,  
                nbr_pax_100_percent_of_cp,  
                rpt.base_quantity base,   
                rpt.goal,   
                rpt.actual_result,  
                rh.node_id AS node_id,   
                rh.node_name node_name   
                FROM   
                rpt_hierarchy rh,  
                promotion p,   
                promo_challengepoint promoGoal,  
                rpt_cp_selection_summary rpt ,  
                goalquest_goallevel gl   
              WHERE rpt.level_id                               = gl.goallevel_id (+)  
              AND rpt.promotion_id                            = promoGoal.promotion_id (+)  
              AND rpt.promotion_id                              = p.promotion_id (+)  
              AND detail_node_id                            = rh.node_id   
              AND rpt.record_type LIKE '%team%'   
              AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))  
              AND rpt.pax_status                                  = NVL(p_in_participantStatus,rpt.pax_status)  
              AND  NVL(rpt.job_position,'JOB')                                = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))  
              AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))   
              AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))  
              AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))     
              ) rpc  
             WHERE rh.detail_node_id                            = rpc.node_id (+) and rh.is_leaf = 1  
            GROUP BY rh.record_type,   
              rh.node_name,   
              rh.node_type_name,   
              rh.is_leaf,   
              rh.parent_node_id,  
              rh.header_node_id,   
              rh.detail_node_id,   
              rh.hier_level   
          ) rs ;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL ;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPProgressTabRes;       
 
/******************************************************************************
  NAME:       prc_getCPProgressDetailRes
  Author              Date           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
 --Suresh J        09/25/2014         Bug #56779 - Rounding off pct column
                   09/25/2014        Bug Fix 56919 -- Sorting Issue 
 --chidamba       08/22/2017        G6-2898- Challengepoint Progress "Percentage of Goal" is rounding the half up.
                                    G6-2888 - Challengepoint Progress Base total is not matching with the result set.
******************************************************************************/
     
/* getChallengePointProgressDetailResults  */
 PROCEDURE prc_getCPProgressDetailRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPProgressDetailRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPProgressDetailRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN,
     rs.*
     FROM 
     (SELECT gsd.last_name
     || '' , ''
     || gsd.first_name
     || '' ''
     || gsd.middle_init pax_name,
     hier.node_name, 
     gsd.promotion_name,
     ''Level ''||gl.sequence_num level_selected,
     NVL(gsd.base_quantity,0) base_quantity,
     NVL(gsd.amount_to_achieve,0) amount_to_achieve,
     NVL(gsd.current_value,0) current_value,
     gsd.progress_challengepoint percent_of_challengepoint, -- 08/22/2017   G6-2898
     NVL(gsd.basic_award_earned,0) achieved 
     FROM promo_challengepoint promoGoal, 
     rpt_cp_selection_detail gsd
     LEFT OUTER JOIN RPT_HIERARCHY hier
      ON gsd.node_id = hier.node_id
     LEFT OUTER JOIN PROMOTION promo
     ON gsd.promotion_id = promo.promotion_id
     RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
     ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
      LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
      ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID
      WHERE NVL(gsd.node_id,0)                            IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))
     AND gsd.promotion_id                                = promoGoal.promotion_id 
     AND gsd.level_id IS NOT NULL                                                    
     AND gsd.user_status                                 = NVL('''||p_in_participantStatus||''',gsd.user_status)
     AND  NVL(gsd.job_position,''JOB'')                                = NVL('''||p_in_jobPosition||''',NVL(gsd.job_position,''JOB''))
     AND NVL(promo.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(promo.promotion_status,'' '')) 
     AND (('''||p_in_departments||''' IS NULL) OR ('''||p_in_departments||''' is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||'''))))) 
     AND (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||'''))))) 
     ORDER BY '|| v_sortCol ||'   --09/25/2014
      ) rs
--     ORDER BY '|| v_sortCol ||'    --09/25/2014
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getChallengePointProgressDetailResultsSize  */
 -- OPEN p_out_size_data FOR
         SELECT COUNT (1) INTO p_out_size_data --AS MAX_SIZE
     FROM promo_challengepoint promoGoal,
     rpt_cp_selection_detail gsd
     LEFT OUTER JOIN RPT_HIERARCHY hier
     ON gsd.node_id = hier.node_id 
     LEFT OUTER JOIN PROMOTION promo
     ON gsd.promotion_id = promo.promotion_id
     RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
     ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
     ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID 
     WHERE NVL(gsd.node_id,0)                            IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND gsd.promotion_id                                = promoGoal.promotion_id
     AND gsd.level_id IS NOT NULL                                                    
     AND gsd.user_status                                 = NVL(p_in_participantStatus,gsd.user_status)
     AND  NVL(gsd.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
     AND NVL(promo.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' '))  
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))));
/* getChallengePointProgressDetailResultsTotals  */
     OPEN p_out_totals_data FOR
     SELECT NVL(SUM(base_quantity),0)           AS BASE_QUANTITY,
     NVL(SUM(amount_to_achieve),0)            AS AMOUNT_ACHIEVED,
     NVL(SUM(current_value),0)                AS CURRENT_VALUE,
--     CASE WHEN NVL(SUM(amount_to_achieve),0) = 0 THEN 0
--     ELSE ROUND((NVL(SUM(current_value),0) /NVL(SUM(amount_to_achieve),0)) * 100)    --09/25/2014
--     END  percent_of_challengepoint,     
      CASE WHEN NVL(SUM(amount_to_achieve),0) = 0 THEN 0   -- 09/11/2017   G6-2898
      ELSE
           NVL(ROUND(
           (SUM(CASE WHEN rounding_method = 'standard' THEN NVL(ROUND(rs.current_value,achievement_precision),0)
                WHEN rounding_method = 'up' AND achievement_precision = 0 THEN NVL(CEIL(rs.current_value) ,0) 
                WHEN rounding_method = 'up' AND achievement_precision <> 0 THEN NVL(ROUND(rs.current_value,achievement_precision) ,0)                            
                WHEN rounding_method = 'down' THEN NVL(TRUNC(rs.current_value,achievement_precision) ,0)
                ELSE 0 END )
            /   NVL(SUM(amount_to_achieve),0)
            )*100 ,max(achievement_precision))
            ,0)
     END   percent_of_challengepoint,   -- 09/11/2017   G6-2898     
     NVL(SUM(achieved),0)                     AS ACHIEVED
     FROM
     (SELECT gsd.last_name
     || ' , '
     || gsd.first_name
     || ' '
     || gsd.middle_init pax_name,
     hier.node_name,
     gsd.promotion_name,
     'Level '||gl.sequence_num level_selected,
     NVL(gsd.base_quantity,0) base_quantity,
     NVL(gsd.amount_to_achieve,0) amount_to_achieve,
     NVL(gsd.current_value,0) current_value,
     NVL(gsd.basic_award_earned,0) achieved,
     achievement_precision,
     rounding_method,
     gsd.progress_challengepoint
     FROM (select CASE WHEN p.achievement_precision ='zero' THEN 0
                                WHEN p.achievement_precision ='one' THEN 1
                                WHEN p.achievement_precision ='two' THEN 2
                                WHEN p.achievement_precision ='three' THEN 3
                                WHEN p.achievement_precision ='four' THEN 4
                                ELSE 0 END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promoGoal, --08/22/2017  G6-2898 achievement precision included in computation  
     rpt_cp_selection_detail gsd 
     LEFT OUTER JOIN RPT_HIERARCHY hier
     ON gsd.node_id = hier.node_id
     LEFT OUTER JOIN PROMOTION promo
     ON gsd.promotion_id = promo.promotion_id
      RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
     ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
     ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID
     WHERE NVL(gsd.node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND gsd.promotion_id                                = promoGoal.promotion_id
     AND gsd.level_id IS NOT NULL             --08/22/2017 G6-2888
     AND gsd.user_status                                 = NVL(p_in_participantStatus,gsd.user_status)
     AND  NVL(gsd.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
     AND NVL(promo.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' '))  
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))  
     ) rs;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
       --  OPEN p_out_size_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPProgressDetailRes;       
/* getProgressToGoalChart  */
 PROCEDURE prc_getCPToGoalChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPToGoalChart' ;
      v_stage          VARCHAR2 (500);
   BEGIN
      v_stage   := 'getCPToGoalChart';
     OPEN p_out_data FOR     
     SELECT NVL(SUM(sum_nbr_pax_25_percent),0) sum_nbr_pax_25_percent, 
     NVL(SUM(sum_nbr_pax_50_percent),0) sum_nbr_pax_50_percent, 
     NVL(SUM(sum_nbr_pax_75_percent),0) sum_nbr_pax_75_percent, 
     NVL(SUM(sum_nbr_pax_76_99_percent),0) sum_nbr_pax_76_99_percent,
     NVL(SUM(sum_nbr_pax_100_percent),0) sum_nbr_pax_100_percent 
     FROM 
     ( SELECT NVL(SUM(total_selected),0)                     AS total_selected,
           NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected,
           SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))                   AS sum_nbr_pax_25_percent,
           SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))                   AS sum_nbr_pax_50_percent, 
           SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))                   AS sum_nbr_pax_75_percent, 
           SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))                AS sum_nbr_pax_76_99_percent, 
           SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))                  AS sum_nbr_pax_100_percent, 
           rh.node_name                                           NODE_NAME, 
           rh.node_type_name NODE_TYPE_NAME, 
           0 IS_LEAF, 
           rh.parent_node_id PARENT_NODE_ID,
           rh.header_node_id HEADER_NODE_ID, 
           rh.detail_node_id DETAIL_NODE_ID, 
           rh.hier_level HIER_LEVEL 
         FROM (SELECT DISTINCT rpt.record_type,
                      rh.node_name, 
                      rh.node_type_name,
                      rpt.is_leaf, 
                      rh.parent_node_id,
                      rpt.header_node_id, 
                      rpt.detail_node_id, 
                      rpt.hier_level  
                  FROM rpt_hierarchy rh, 
                       rpt_cp_selection_summary rpt
                 WHERE detail_node_id                            = rh.node_id
                   AND rpt.record_type LIKE '%node%' 
                   AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
                ) rh,
          (SELECT total_selected ,
             level_id,
             nbr_threshold_reached,
             nbr_pax_25_percent_of_cp ,
             nbr_pax_50_percent_of_cp,
             nbr_pax_75_percent_of_cp,
             nbr_pax_76_99_percent_of_cp,
             nbr_pax_100_percent_of_cp,
             rpt.base_quantity base, 
             rpt.goal, 
             rpt.actual_result,
             rh.node_id AS node_id,
             rh.node_name node_name 
             FROM 
             rpt_hierarchy rh,
             promotion p, 
             promo_challengepoint promoGoal,
             rpt_cp_selection_summary rpt ,
             goalquest_goallevel gl 
           WHERE rpt.level_id                               = gl.goallevel_id (+)
           AND rpt.promotion_id                            = promoGoal.promotion_id (+)
           AND rpt.promotion_id                              = p.promotion_id (+)
           AND detail_node_id                            = rh.node_id 
            AND rpt.record_type LIKE '%node%' 
           AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
           AND rpt.pax_status                                  = NVL(p_in_participantStatus,rpt.pax_status)
           AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
           AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
           AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
           AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))  
           ) rpc
          WHERE rh.detail_node_id                            = rpc.node_id (+)
          AND p_in_nodeAndBelow = 1
         GROUP BY rh.record_type, 
           rh.node_name, 
           rh.node_type_name,
           rh.is_leaf, 
           rh.parent_node_id,
           rh.header_node_id, 
           rh.detail_node_id, 
           rh.hier_level 
         UNION 
         SELECT NVL(SUM(total_selected),0)                     AS total_selected,
         NVL(SUM(DECODE(level_id,NULL,0,total_selected)),0) AS all_level_selected, 
         SUM(NVL(NBR_PAX_25_PERCENT_OF_CP,0))            AS sum_nbr_pax_25_percent, 
         SUM(NVL(NBR_PAX_50_PERCENT_OF_CP,0))            AS sum_nbr_pax_50_percent, 
         SUM(NVL(NBR_PAX_75_PERCENT_OF_CP,0))            AS sum_nbr_pax_75_percent, 
         SUM(NVL(NBR_PAX_76_99_PERCENT_OF_CP,0))         AS sum_nbr_pax_76_99_percent, 
         SUM(NVL(NBR_PAX_100_PERCENT_OF_CP,0))           AS sum_nbr_pax_100_percent, 
         DECODE (instr(rh.record_type,'team'),0,rh.node_name,rh.node_name 
         || ' Team')AS NODE_NAME, 
         rh.node_type_name node_type_name,
         1 is_leaf, 
         rh.parent_node_id parent_node_id,
         rh.header_node_id header_node_id, 
         rh.detail_node_id detail_node_id, 
         rh.hier_level hier_level 
       FROM (SELECT DISTINCT rpt.record_type,
     rh.node_name, 
                    rh.node_type_name,
                    rpt.is_leaf, 
                    rh.parent_node_id,
                    rpt.header_node_id, 
                    rpt.detail_node_id, 
                    rpt.hier_level 
                FROM rpt_hierarchy rh, 
      rpt_cp_selection_summary rpt
     WHERE detail_node_id                            = rh.node_id
                 AND rpt.record_type LIKE '%team%' 
                 AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
              ) rh,
        (SELECT total_selected ,
            level_id,
            nbr_threshold_reached,
           nbr_pax_25_percent_of_cp ,
           nbr_pax_50_percent_of_cp,
           nbr_pax_75_percent_of_cp,
           nbr_pax_76_99_percent_of_cp,
           nbr_pax_100_percent_of_cp,
           rpt.base_quantity base, 
           rpt.goal, 
           rpt.actual_result,
           rh.node_id AS node_id,
           rh.node_name node_name 
           FROM 
           rpt_hierarchy rh,
           promotion p, 
           promo_challengepoint promoGoal,
           rpt_cp_selection_summary rpt ,
           goalquest_goallevel gl 
         WHERE rpt.level_id                               = gl.goallevel_id (+)
         AND rpt.promotion_id                            = promoGoal.promotion_id (+)
         AND rpt.promotion_id                              = p.promotion_id (+)
         AND detail_node_id                            = rh.node_id 
         AND rpt.record_type LIKE '%team%' 
         AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
         AND rpt.pax_status                                  = NVL(p_in_participantStatus,rpt.pax_status)
         AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
         AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
         AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
         AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
         ) rpc
        WHERE rh.detail_node_id                            = rpc.node_id (+) and rh.is_leaf = 1 
       GROUP BY rh.record_type, 
         rh.node_name, 
         rh.node_type_name,
         rh.is_leaf, 
         rh.parent_node_id,
         rh.header_node_id, 
         rh.detail_node_id, 
         rh.hier_level 
      ) rs;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getCPToGoalChart;       
/* getChallengePointProgressResultsChart  */
    PROCEDURE prc_getCPProgressResChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPProgressResChart' ;
      v_stage          VARCHAR2 (500);
   BEGIN
      v_stage   := 'getCPProgressResChart';
     OPEN p_out_data FOR     
     SELECT * 
     FROM 
       (SELECT gsd.last_name 
         || ' , ' 
         || gsd.first_name 
         || ' ' 
         || gsd.middle_init pax_name, 
         gsd.promotion_name, 
         hier.node_name, 
         gsd.base_quantity base_quantity, 
         gsd.amount_to_achieve amount_to_achieve, 
         gsd.current_value current_value 
       FROM promo_challengepoint promoGoal, 
         rpt_cp_selection_detail gsd 
       LEFT OUTER JOIN RPT_HIERARCHY hier 
       ON gsd.node_id = hier.node_id 
       LEFT OUTER JOIN PROMOTION promo 
       ON gsd.promotion_id = promo.promotion_id 
       RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg 
       ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID 
       LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl 
       ON pg.GOALLEVEL_ID        = gl.GOALLEVEL_ID 
       WHERE NVL(gsd.node_id,0) IN 
         (SELECT child_node_id 
           FROM rpt_hierarchy_rollup 
           WHERE node_id IN 
           ( SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
         ) 
       AND gsd.promotion_id                = promoGoal.promotion_id 
       AND gsd.user_status                 = NVL(p_in_participantStatus,gsd.user_status) 
       AND NVL(gsd.job_position,'JOB')     = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
       AND NVL(promo.promotion_status,' ') = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' ')) 
       AND ((p_in_departments                  IS NULL) 
       OR (p_in_departments                    IS NOT NULL 
       AND gsd.department                 IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
         ))) 
       AND ((p_in_promotionId   IS NULL) 
       OR (p_in_promotionId     IS NOT NULL 
       AND gsd.promotion_id IN 
         (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
         ))) 
         AND p_in_nodeAndBelow = 1
       ORDER BY current_value DESC 
       ) 
     WHERE ROWNUM <=10;
  p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;            
 END prc_getCPProgressResChart;  
 
/******************************************************************************
  NAME:       prc_getCPAchievementTabRes
  Date               Author           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
 Suresh J          09/23/2014      Bug Fix 56790  -  Challengepoint % Goal Achievement Chart is displayed when Summary has No Data.
 Chidamba          08/24/2017      G6-2895 Challengepoint % Goal should display according to achievement precision 
  ******************************************************************************/   
 
/* getChallengePointAchievementTabularResults  */
     PROCEDURE prc_getCPAchievementTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPAchievementTabRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
 BEGIN
      v_stage   := 'getCPAchievementTabRes';    
      
      l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, rs.*
  FROM (SELECT node_name AS node_name,
               node_id AS node_id,
               total_selected AS total_selected,
               NVL (level_1_total_selected, 0) AS level_1_selected_cnt,
               NVL (level_1_nbr_achieved, 0) AS level_1_achieved_cnt,
               CASE
                  WHEN NVL (level_1_total_selected, 0) = 0
                  THEN 0
                  ELSE 
                     NVL (ROUND ((level_1_nbr_achieved / level_1_total_selected)* 100,2),0)
               END
                  AS level_1_achieved_percent,
               NVL (level_1_awards, 0) AS level_1_award_cnt,
               NVL (level_2_total_selected, 0) AS level_2_selected_cnt,
               NVL (level_2_nbr_achieved, 0) AS level_2_achieved_cnt,
               CASE
                  WHEN NVL (level_2_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_2_nbr_achieved / level_2_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_2_ACHIEVED_PERCENT,
               NVL (level_2_awards, 0) AS LEVEL_2_AWARD_CNT,
               NVL (level_3_total_selected, 0) AS LEVEL_3_SELECTED_CNT,
               NVL (level_3_nbr_achieved, 0) AS LEVEL_3_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_3_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_3_nbr_achieved / level_3_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS level_3_achieved_percent,
               NVL (level_3_awards, 0) AS LEVEL_3_AWARD_CNT,
               NVL (level_4_total_selected, 0) AS LEVEL_4_SELECTED_CNT,
               NVL (level_4_nbr_achieved, 0) AS LEVEL_4_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_4_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_4_nbr_achieved / level_4_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_4_ACHIEVED_PERCENT,
               NVL (level_4_awards, 0) AS LEVEL_4_AWARD_CNT,
               NVL (level_5_total_selected, 0) AS LEVEL_5_SELECTED_CNT,
               NVL (level_5_nbr_achieved, 0) AS LEVEL_5_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_5_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_5_nbr_achieved / level_5_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_5_ACHIEVED_PERCENT,
               NVL (level_5_awards, 0) AS LEVEL_5_AWARD_CNT,
               NVL (level_6_total_selected, 0) AS LEVEL_6_SELECTED_CNT,
               NVL (level_6_nbr_achieved, 0) AS LEVEL_6_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_6_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_6_nbr_achieved / level_6_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_6_ACHIEVED_PERCENT,
               NVL (level_6_awards, 0) AS LEVEL_6_AWARD_CNT,
               NVL (base, 0) AS BASE_QUANTITY,
               NVL (goal, 0) AS CHALLENGEPOINT,
               NVL (actual_result, 0) AS ACTUAL_RESULT,
               CASE
                  WHEN NVL (goal, 0) = 0 THEN 0
                  --ELSE ROUND ( (NVL (actual_result, 0) / goal) * 100, 2)  --08/24/2017  G6-2895 commented
               ELSE ROUND ( (NVL (actual_result, 0) / goal) * 100, achievement_precision)  --08/24/2017  G6-2895   
               END
                  AS PERCENT_ACHIEVED,
               0 AS IS_LEAF
          FROM (WITH pivot_set
                     AS (SELECT total_selected,
                                nbr_achieved,
                                awards,
                                NVL (sequence_num, 0) sequence_num,
                                rh.node_id AS node_id,
                                rh.node_name node_name,
                                base,
                                goal,
                                actual_result,
                                achievement_precision --08/24/2017  G6-2895 
                           FROM (SELECT DISTINCT
                                        rh.node_id AS node_id,
                                        rh.node_name node_name
                                   FROM rpt_hierarchy rh,
                                        rpt_cp_selection_summary rpt
                                  WHERE     detail_node_id = rh.node_id
                                        AND rpt.record_type LIKE ''%node%''
                                        AND NVL (header_node_id, 0) IN (SELECT *
                                                                          FROM TABLE (
                                                                                  get_array_varchar (
                                                                                     '''||p_in_parentNodeId||'''))))
                                rh,
                                (  SELECT SUM (total_selected) total_selected,
                                          SUM (nbr_challengepoint_achieved)
                                             nbr_achieved,
                                          SUM (calculated_payout) AS awards,
                                          gl.sequence_num,
                                          rh.node_id AS node_id,
                                          rh.node_name node_name,
                                          SUM (rpt.base_quantity) base,
                                          SUM (rpt.goal) goal,
                                          SUM (rpt.actual_result) actual_result,  --08/24/2017  G6-2895                                        
                                          MAX (achievement_precision) achievement_precision --08/24/2017  G6-2895  
                                     FROM rpt_hierarchy rh,
                                          promotion p,
                                          --promo_challengepoint promoGoal,  --08/24/2017  G6-2895   starts
                                          (select CASE WHEN p.achievement_precision =''zero'' THEN 0
                                                       WHEN p.achievement_precision =''one'' THEN 1
                                                       WHEN p.achievement_precision =''two'' THEN 2
                                                       WHEN p.achievement_precision =''three'' THEN 3
                                                       WHEN p.achievement_precision =''four'' THEN 4
                                                   ELSE 0 
                                                   END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promoGoal,  --08/24/2017  G6-2895   ends
                                          rpt_cp_selection_summary rpt,
                                          goalquest_goallevel gl
                                    WHERE     rpt.level_id = gl.goallevel_id(+)
                                          AND rpt.promotion_id =
                                                 promoGoal.promotion_id(+)
                                          AND rpt.promotion_id =
                                                 p.promotion_id(+)
                                          AND detail_node_id = rh.node_id
                                          AND rpt.record_type LIKE ''%node%''
                                          AND NVL (header_node_id, 0) IN (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_parentNodeId||''')))
                                          AND rpt.pax_status =
                                                 NVL ('''||p_in_participantStatus||''',
                                                      rpt.pax_status)
                                          AND NVL (rpt.job_position, ''JOB'') =
                                                 NVL (
                                                    '''||p_in_jobPosition||''',
                                                    NVL (rpt.job_position,
                                                         ''JOB''))
                                          AND (   ('''||p_in_departments||'''
                                                      IS NULL)
                                               OR (    '''||p_in_departments||'''
                                                          IS NOT NULL
                                                   AND rpt.department IN (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_departments||''')))))
                                          AND (   ('''||p_in_promotionId||'''
                                                      IS NULL)
                                               OR (    '''||p_in_promotionId||'''
                                                          IS NOT NULL
                                                   AND rpt.promotion_id IN (SELECT *
                                                                              FROM TABLE (
                                                                                      get_array_varchar (
                                                                                         '''||p_in_promotionId||''')))))
                                          AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' '')) 
                                 GROUP BY gl.sequence_num,
                                          rh.node_id,
                                          rh.node_name) rpc
                          WHERE rh.node_id = rpc.node_id(+))
                SELECT node_name,
                       node_id,
                       (  NVL (level_1_total_selected, 0)
                        + NVL (level_2_total_selected, 0)
                        + NVL (level_3_total_selected, 0)
                        + NVL (level_4_total_selected, 0)
                        + NVL (level_5_total_selected, 0)
                        + NVL (level_6_total_selected, 0))
                          total_selected,
                       level_1_total_selected,
                       level_1_nbr_achieved,
                       level_1_awards,
                       level_2_total_selected,
                       level_2_nbr_achieved,
                       level_2_awards,
                       level_3_total_selected,
                       level_3_nbr_achieved,
                       level_3_awards,
                       level_4_total_selected,
                       level_4_nbr_achieved,
                       level_4_awards,
                       level_5_total_selected,
                       level_5_nbr_achieved,
                       level_5_awards,
                       level_6_total_selected,
                       level_6_nbr_achieved,
                       level_6_awards,
                       (  NVL (level_1_base, 0)
                        + NVL (level_2_base, 0)
                        + NVL (level_3_base, 0)
                        + NVL (level_4_base, 0)
                        + NVL (level_5_base, 0)
                        + NVL (level_6_base, 0))
                          base,
                       (  NVL (level_1_goal, 0)
                        + NVL (level_2_goal, 0)
                        + NVL (level_3_goal, 0)
                        + NVL (level_4_goal, 0)
                        + NVL (level_5_goal, 0)
                        + NVL (level_6_goal, 0))
                          goal,
                       (  NVL (level_1_actual_result, 0)
                        + NVL (level_2_actual_result, 0)
                        + NVL (level_3_actual_result, 0)
                        + NVL (level_4_actual_result, 0)
                        + NVL (level_5_actual_result, 0)
                        + NVL (level_6_actual_result, 0))
                          actual_result,
                          GREATEST
                       (  NVL (level_1_achievement_precision,0),
                          NVL (level_2_achievement_precision,0),
                          NVL (level_3_achievement_precision,0),
                          NVL (level_4_achievement_precision,0),
                          NVL (level_5_achievement_precision,0),
                          NVL (level_6_achievement_precision,0)) achievement_precision --08/24/2017  G6-2895 
                  FROM pivot_set PIVOT (SUM (total_selected) total_selected,
                                       SUM (nbr_achieved) nbr_achieved,
                                       SUM (awards) awards,
                                       SUM (base) base,
                                       SUM (goal) goal,
                                       SUM (actual_result) actual_result,
                                       MAX (achievement_precision) achievement_precision --08/24/2017  G6-2895 
                                 FOR sequence_num
                                 IN  (0 AS no_goal,
                                     1 AS level_1,
                                     2 AS level_2,
                                     3 AS level_3,
                                     4 AS level_4,
                                     5 AS level_5,
                                     6 AS level_6)) t WHERE '''||p_in_nodeAndBelow||''' = ''1'')    --09/23/2014
        UNION
        SELECT node_name || '' Team'' AS NODE_NAME,
               node_id AS NODE_ID,
               total_selected AS TOTAL_SELECTED,
               NVL (level_1_total_selected, 0) AS LEVEL_1_SELECTED_CNT,
               NVL (level_1_nbr_achieved, 0) AS LEVEL_1_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_1_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_1_nbr_achieved / level_1_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_1_ACHIEVED_PERCENT,
               NVL (level_1_awards, 0) AS LEVEL_1_AWARD_CNT,
               NVL (level_2_total_selected, 0) AS LEVEL_2_SELECTED_CNT,
               NVL (level_2_nbr_achieved, 0) AS LEVEL_2_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_2_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_2_nbr_achieved / level_2_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_2_ACHIEVED_PERCENT,
               NVL (level_2_awards, 0) AS LEVEL_2_AWARD_CNT,
               NVL (level_3_total_selected, 0) AS LEVEL_3_SELECTED_CNT,
               NVL (level_3_nbr_achieved, 0) AS LEVEL_3_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_3_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_3_nbr_achieved / level_3_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_3_ACHIEVED_PERCENT,
               NVL (level_3_awards, 0) AS LEVEL_3_AWARD_CNT,
               NVL (level_4_total_selected, 0) AS LEVEL_4_SELECTED_CNT,
               NVL (level_4_nbr_achieved, 0) AS LEVEL_4_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_4_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_4_nbr_achieved / level_4_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_4_ACHIEVED_PERCENT,
               NVL (level_4_awards, 0) AS LEVEL_4_AWARD_CNT,
               NVL (level_5_total_selected, 0) AS LEVEL_5_SELECTED_CNT,
               NVL (level_5_nbr_achieved, 0) AS LEVEL_5_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_5_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_5_nbr_achieved / level_5_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_5_ACHIEVED_PERCENT,
               NVL (level_5_awards, 0) AS LEVEL_5_AWARD_CNT,
               NVL (level_6_total_selected, 0) AS LEVEL_6_SELECTED_CNT,
               NVL (level_6_nbr_achieved, 0) AS LEVEL_6_ACHIEVED_CNT,
               CASE
                  WHEN NVL (level_6_total_selected, 0) = 0
                  THEN
                     0
                  ELSE
                     NVL (
                        ROUND (
                             (level_6_nbr_achieved / level_6_total_selected)
                           * 100,
                           2),
                        0)
               END
                  AS LEVEL_6_ACHIEVED_PERCENT,
               NVL (level_6_awards, 0) AS LEVEL_6_AWARD_CNT,
               NVL (base, 0) AS BASE_QUANTITY,
               NVL (goal, 0) AS AMT_TO_ACHIEVE,
               NVL (actual_result, 0) AS CURRENT_VALUE,
               CASE
                  WHEN NVL (goal, 0) = 0 THEN 0
                 --ELSE ROUND ( (NVL (actual_result, 0) / goal) * 100, 2)   --08/24/2017  G6-2895  commented
               ELSE    ROUND ( (NVL (actual_result, 0) / goal) * 100, achievement_precision)  --08/24/2017  G6-2895 
               END
                  AS PERCENT_ACHIEVED,
               1 AS IS_LEAF
          FROM (WITH pivot_set
                     AS (SELECT total_selected,
                                nbr_achieved,
                                awards,
                                NVL (sequence_num, 0) sequence_num,
                                rh.node_id AS node_id,
                                rh.node_name node_name,
                                base,
                                goal,
                                actual_result,
                                achievement_precision --08/24/2017  G6-2895 
                           FROM (SELECT DISTINCT
                                        rh.node_id AS node_id,
                                        rh.node_name node_name
                                   FROM rpt_hierarchy rh,
                                        rpt_cp_selection_summary rpt
                                  WHERE     detail_node_id = rh.node_id
                                        AND rpt.record_type LIKE ''%team%''
                                        AND NVL (detail_node_id, 0) IN (SELECT *
                                                                          FROM TABLE (
                                                                                  get_array_varchar (
                                                                                     '''||p_in_parentNodeId||'''))))
                                rh,
                                (SELECT SUM (total_selected) total_selected,
                                        SUM (nbr_challengepoint_achieved)
                                           nbr_achieved,
                                        SUM (calculated_payout) AS awards,
                                        gl.sequence_num,
                                        rh.node_id AS node_id,
                                        rh.node_name node_name,
                                        SUM (rpt.base_quantity) base,
                                        SUM (rpt.goal) goal,
                                        SUM (rpt.actual_result) actual_result,  --08/24/2017  G6-2895  
                                       MAX (achievement_precision) achievement_precision --08/24/2017  G6-2895 
                                   FROM rpt_hierarchy rh,
                                        promotion p,
                                        --promo_challengepoint promoGoal, --08/24/2017  G6-2895  starts
                                        (select CASE WHEN p.achievement_precision =''zero'' THEN 0
                                                     WHEN p.achievement_precision =''one'' THEN 1
                                                     WHEN p.achievement_precision =''two'' THEN 2
                                                     WHEN p.achievement_precision =''three'' THEN 3
                                                     WHEN p.achievement_precision =''four'' THEN 4
                                                ELSE 0
                                                END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promoGoal, --08/24/2017  G6-2895  ends
                                        rpt_cp_selection_summary rpt,
                                        goalquest_goallevel gl
                                  WHERE     rpt.level_id = gl.goallevel_id(+)
                                        AND rpt.promotion_id =
                                               promoGoal.promotion_id(+)
                                        AND rpt.promotion_id =
                                               p.promotion_id(+)
                                        AND detail_node_id = rh.node_id
                                        AND rpt.record_type LIKE ''%team%''
                                                         AND NVL (detail_node_id, 0) IN
                                                                (SELECT *
                                                                   FROM TABLE (
                                                                           get_array_varchar (
                                                                              '''||p_in_parentNodeId||''')))
                                                         AND rpt.pax_status =
                                                                NVL (
                                                                   '''||p_in_participantStatus||''',
                                                                   rpt.pax_status)
                                                        AND  NVL(rpt.job_position,''JOB'')
                                                        = NVL('''||p_in_jobPosition||''',NVL(rpt.job_position,''JOB''))
                                                         AND ( ('''||p_in_departments||''' IS NULL)
                                                              OR ('''||p_in_departments||'''
                                                                     IS NOT NULL
                                                                  AND rpt.department IN
                                                                         (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_departments||''')))))
                                                         AND ( ('''||p_in_promotionId||''' IS NULL)
                                                              OR ('''||p_in_promotionId||'''
                                                                     IS NOT NULL
                                                                  AND rpt.promotion_id IN
                                                                         (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_promotionId||''')))))
                                                         AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' '')) 
                                                GROUP BY gl.sequence_num,
                                                         rh.node_id,
                                                         rh.node_name) rpc
                                         WHERE rh.node_id = rpc.node_id(+))
                               SELECT node_name,
                                      node_id,
                                      (  NVL (level_1_total_selected, 0)
                                       + NVL (level_2_total_selected, 0)
                                       + NVL (level_3_total_selected, 0)
                                       + NVL (level_4_total_selected, 0)
                                       + NVL (level_5_total_selected, 0)
                                       + NVL (level_6_total_selected, 0))
                                         total_selected,
                                      level_1_total_selected,
                                      level_1_nbr_achieved,
                                      level_1_awards,
                                      level_2_total_selected,
                                      level_2_nbr_achieved,
                                      level_2_awards,
                                      level_3_total_selected,
                                      level_3_nbr_achieved,
                                      level_3_awards,
                                      level_4_total_selected,
                                      level_4_nbr_achieved,
                                      level_4_awards,
                                      level_5_total_selected,
                                      level_5_nbr_achieved,
                                      level_5_awards,
                                      level_6_total_selected,
                                      level_6_nbr_achieved,
                                      level_6_awards,
                                      (  NVL (level_1_base, 0)
                                       + NVL (level_2_base, 0)
                                       + NVL (level_3_base, 0)
                                       + NVL (level_4_base, 0)
                                       + NVL (level_5_base, 0)
                                       + NVL (level_6_base, 0))
                                         base,
                                      (  NVL (level_1_goal, 0)
                                       + NVL (level_2_goal, 0)
                                       + NVL (level_3_goal, 0)
                                       + NVL (level_4_goal, 0)
                                       + NVL (level_5_goal, 0)
                                       + NVL (level_6_goal, 0))
                                         goal,
                                      (  NVL (level_1_actual_result, 0)
                                       + NVL (level_2_actual_result, 0)
                                       + NVL (level_3_actual_result, 0)
                                       + NVL (level_4_actual_result, 0)
                                       + NVL (level_5_actual_result, 0)
                                       + NVL (level_6_actual_result, 0))
                                         actual_result,
                                         GREATEST
                                      (  NVL (level_1_achievement_precision,0),
                                         NVL (level_2_achievement_precision,0),
                                         NVL (level_3_achievement_precision,0),
                                         NVL (level_4_achievement_precision,0),
                                         NVL (level_5_achievement_precision,0),
                                         NVL (level_6_achievement_precision,0)) achievement_precision --08/24/2017  G6-2895 
                                 FROM pivot_set PIVOT (SUM (total_selected) total_selected,
                                                      SUM (nbr_achieved) nbr_achieved,
                                                      SUM (awards) awards,
                                                      SUM (base) base,
                                                      SUM (goal) goal,
                                                      SUM (actual_result) actual_result,
                                                      MAX (achievement_precision) achievement_precision --08/24/2017  G6-2895 
                                                FOR sequence_num
                                                IN  (0 AS no_goal,
                                                    1 AS level_1,
                                                    2 AS level_2,
                                                    3 AS level_3,
                                                    4 AS level_4,
                                                    5 AS level_5,
                                                    6 AS level_6)) t)) rs
       ORDER BY '|| v_sortCol ||'    
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
         OPEN p_out_data FOR l_query;
   
/* getChallengePointAchievementResultsSize  */
    -- OPEN p_out_size_data FOR
     SELECT count(1) INTO  p_out_size_data --AS MAX_SIZE
           FROM (SELECT node_name 
               FROM (WITH pivot_set 
                          AS (SELECT total_selected, 
                                     nbr_achieved, 
                                     awards, 
                                     NVL (sequence_num, 0) sequence_num, 
                                     rh.node_id AS node_id, 
                                     rh.node_name node_name, 
                                     base, 
                                     goal, 
                                     actual_result 
                                FROM (SELECT DISTINCT 
                                             rh.node_id AS node_id, 
                                             rh.node_name node_name 
                                        FROM rpt_hierarchy rh, 
                                             rpt_cp_selection_summary rpt 
                                       WHERE detail_node_id = rh.node_id 
                                             AND rpt.record_type LIKE '%node%' 
                                             AND NVL (header_node_id, 0) IN 
                                                    (SELECT * 
                                                       FROM TABLE ( 
                                                               get_array_varchar ( 
                                                                  p_in_parentNodeId)))) rh, 
                                     (  SELECT SUM (total_selected) total_selected, 
                                               SUM (nbr_challengepoint_achieved) 
                                                  nbr_achieved, 
                                               SUM (calculated_payout) AS awards, 
                                               gl.sequence_num, 
                                               rh.node_id AS node_id, 
                                               rh.node_name node_name, 
                                               SUM (rpt.base_quantity) base, 
                                               SUM (rpt.goal) goal, 
                                               SUM (rpt.actual_result) actual_result 
                                          FROM rpt_hierarchy rh, 
                                               promotion p, 
                                               promo_challengepoint promoGoal, 
                                               rpt_cp_selection_summary rpt, 
                                               goalquest_goallevel gl 
                                         WHERE rpt.level_id = gl.goallevel_id(+) 
                                               AND rpt.promotion_id = 
                                                      promoGoal.promotion_id(+) 
                                               AND rpt.promotion_id = 
                                                      p.promotion_id(+) 
                                               AND detail_node_id = rh.node_id 
                                               AND rpt.record_type LIKE '%node%' 
                                               AND NVL (header_node_id, 0) IN 
                                                      (SELECT * 
                                                         FROM TABLE ( 
                                                                 get_array_varchar ( 
                                                                    p_in_parentNodeId))) 
                                               AND rpt.pax_status = 
                                                      NVL (p_in_participantStatus, 
                                                           rpt.pax_status) 
                                              AND  NVL(rpt.job_position,'JOB')       
                                                        = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
                                               AND ( (p_in_departments IS NULL) 
                                                    OR (p_in_departments IS NOT NULL 
                                                        AND rpt.department IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_departments))))) 
                                               AND ( (p_in_promotionId IS NULL) 
                                                    OR (p_in_promotionId IS NOT NULL 
                                                        AND rpt.promotion_id IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_promotionId))))) 
                                               AND NVL (p.promotion_status, ' ') = 
                                                      NVL ( 
                                                         p_in_promotionStatus, 
                                                         NVL (p.promotion_status, 
                                                              ' ')) 
                                      GROUP BY gl.sequence_num, 
                                               rh.node_id, 
                                               rh.node_name) rpc 
                               WHERE rh.node_id = rpc.node_id(+)) 
                     SELECT node_name, 
                            node_id, 
                              NVL (level_1_total_selected, 0) 
                            + NVL (level_2_total_selected, 0) 
                            + NVL (level_3_total_selected, 0) 
                            + NVL (level_4_total_selected, 0) 
                            + NVL (level_5_total_selected, 0) 
                            + NVL (level_6_total_selected, 0) 
                               total_selected, 
                            no_goal_total_selected, 
                            level_1_total_selected, 
                            level_1_nbr_achieved, 
                            level_1_awards, 
                            level_2_total_selected, 
                            level_2_nbr_achieved, 
                            level_2_awards, 
                            level_3_total_selected, 
                            level_3_nbr_achieved, 
                            level_3_awards, 
                            level_4_total_selected, 
                            level_4_nbr_achieved, 
                            level_4_awards, 
                            level_5_total_selected, 
                            level_5_nbr_achieved, 
                            level_5_awards, 
                            level_6_total_selected, 
                            level_6_nbr_achieved, 
                            level_6_awards, 
                            (  NVL (level_1_base, 0) 
                             + NVL (level_2_base, 0) 
                             + NVL (level_3_base, 0) 
                             + NVL (level_4_base, 0) 
                             + NVL (level_5_base, 0) 
                             + NVL (level_6_base, 0)) 
                               base, 
                            (  NVL (level_1_goal, 0) 
                             + NVL (level_2_goal, 0) 
                             + NVL (level_3_goal, 0) 
                             + NVL (level_4_goal, 0) 
                             + NVL (level_5_goal, 0) 
                             + NVL (level_6_goal, 0)) 
                               goal, 
                            (  NVL (level_1_actual_result, 0) 
                             + NVL (level_2_actual_result, 0) 
                             + NVL (level_3_actual_result, 0) 
                             + NVL (level_4_actual_result, 0) 
                             + NVL (level_5_actual_result, 0) 
                             + NVL (level_6_actual_result, 0)) 
                               actual_result 
                       FROM pivot_set PIVOT (SUM (total_selected) total_selected, 
                                            SUM (nbr_achieved) nbr_achieved, 
                                            SUM (awards) awards, 
                                            SUM (base) base, 
                                            SUM (goal) goal, 
                                            SUM (actual_result) actual_result 
                                      FOR sequence_num 
                                      IN  (0 AS no_goal, 
                                          1 AS level_1, 
                                          2 AS level_2, 
                                          3 AS level_3, 
                                          4 AS level_4, 
                                          5 AS level_5, 
                                          6 AS level_6)) t WHERE  p_in_nodeAndBelow = '1' )  --09/23/2014 
             UNION 
             SELECT node_name 
               FROM (WITH pivot_set 
                          AS (SELECT total_selected, 
                                     nbr_achieved, 
                                     awards, 
                                     NVL (sequence_num, 0) sequence_num, 
                                     rh.node_id AS node_id, 
                                     rh.node_name node_name, 
                                     base, 
                                     goal, 
                                     actual_result 
                                FROM (SELECT DISTINCT 
                                             rh.node_id AS node_id, 
                                             rh.node_name node_name 
                                        FROM rpt_hierarchy rh, 
                                             rpt_cp_selection_summary rpt 
                                       WHERE detail_node_id = rh.node_id 
                                             AND rpt.record_type LIKE '%team%' 
                                             AND NVL (detail_node_id, 0) IN 
                                                    (SELECT * 
                                                       FROM TABLE ( 
                                                               get_array_varchar ( 
                                                                  p_in_parentNodeId)))) rh, 
                                     (  SELECT SUM (total_selected) total_selected, 
                                               SUM (nbr_challengepoint_achieved) 
                                                  nbr_achieved, 
                                               SUM (calculated_payout) AS awards, 
                                               gl.sequence_num, 
                                               rh.node_id AS node_id, 
                                               rh.node_name node_name, 
                                               SUM (rpt.base_quantity) base, 
                                               SUM (rpt.goal) goal, 
                                               SUM (rpt.actual_result) actual_result 
                                          FROM rpt_hierarchy rh, 
                                               promotion p, 
                                               promo_challengepoint promoGoal, 
                                               rpt_cp_selection_summary rpt, 
                                               goalquest_goallevel gl 
                                         WHERE rpt.level_id = gl.goallevel_id(+) 
                                               AND rpt.promotion_id = 
                                                      promoGoal.promotion_id(+) 
                                               AND rpt.promotion_id = 
                                                      p.promotion_id(+) 
                                               AND detail_node_id = rh.node_id 
                                               AND rpt.record_type LIKE '%team%' 
                                               AND NVL (detail_node_id, 0) IN 
                                                      (SELECT * 
                                                         FROM TABLE ( 
                                                                 get_array_varchar ( 
                                                                    p_in_parentNodeId))) 
                                               AND rpt.pax_status = 
                                                      NVL (p_in_participantStatus, 
                                                           rpt.pax_status) 
                                               AND  NVL(rpt.job_position,'JOB')       
                                                        = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
                                               AND ( (p_in_departments IS NULL) 
                                                    OR (p_in_departments IS NOT NULL 
                                                        AND rpt.department IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_departments))))) 
                                               AND ( (p_in_promotionId IS NULL) 
                                                    OR (p_in_promotionId IS NOT NULL 
                                                        AND rpt.promotion_id IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_promotionId))))) 
                                               AND NVL (p.promotion_status, ' ') = 
                                                      NVL ( 
                                                         p_in_promotionStatus, 
                                                         NVL (p.promotion_status, 
                                                              ' ')) 
                                      GROUP BY gl.sequence_num, 
                                               rh.node_id, 
                                               rh.node_name) rpc 
                               WHERE rh.node_id = rpc.node_id(+)) 
                     SELECT node_name, 
                            node_id, 
                            (  NVL (level_1_total_selected, 0) 
                             + NVL (level_2_total_selected, 0) 
                             + NVL (level_3_total_selected, 0) 
                             + NVL (level_4_total_selected, 0) 
                             + NVL (level_5_total_selected, 0) 
                             + NVL (level_6_total_selected, 0)) 
                               total_selected, 
                            no_goal_total_selected, 
                            level_1_total_selected, 
                            level_1_nbr_achieved, 
                            level_1_awards, 
                            level_2_total_selected, 
                            level_2_nbr_achieved, 
                            level_2_awards, 
                            level_3_total_selected, 
                            level_3_nbr_achieved, 
                            level_3_awards, 
                            level_4_total_selected, 
                            level_4_nbr_achieved, 
                            level_4_awards, 
                            level_5_total_selected, 
                            level_5_nbr_achieved, 
                            level_5_awards, 
                            level_6_total_selected, 
                            level_6_nbr_achieved, 
                            level_6_awards, 
                            (  NVL (level_1_base, 0) 
                             + NVL (level_2_base, 0) 
                             + NVL (level_3_base, 0) 
                             + NVL (level_4_base, 0) 
                             + NVL (level_5_base, 0) 
                             + NVL (level_6_base, 0)) 
                               base, 
                            (  NVL (level_1_goal, 0) 
                             + NVL (level_2_goal, 0) 
                             + NVL (level_3_goal, 0) 
                             + NVL (level_4_goal, 0) 
                             + NVL (level_5_goal, 0) 
                             + NVL (level_6_goal, 0)) 
                               goal, 
                            (  NVL (level_1_actual_result, 0) 
                             + NVL (level_2_actual_result, 0) 
                             + NVL (level_3_actual_result, 0) 
                             + NVL (level_4_actual_result, 0) 
                             + NVL (level_5_actual_result, 0) 
                             + NVL (level_6_actual_result, 0)) 
                               actual_result 
                       FROM pivot_set PIVOT (SUM (total_selected) total_selected, 
                                            SUM (nbr_achieved) nbr_achieved, 
                                            SUM (awards) awards, 
                                            SUM (base) base, 
                                            SUM (goal) goal, 
                                            SUM (actual_result) actual_result 
                                      FOR sequence_num 
                                      IN  (0 AS no_goal, 
                                          1 AS level_1, 
                                          2 AS level_2, 
                                          3 AS level_3, 
                                          4 AS level_4, 
                                          5 AS level_5, 
                                          6 AS level_6)) t)) ;
/* getChallengePointAchievementTabularResultsTotals  */
     OPEN p_out_totals_data FOR
     SELECT NVL (SUM (RS.TOTAL_SELECTED), 0) AS TOTAL_SELECTED, 
            NVL (SUM (RS.LEVEL_1_SELECTED_CNT), 0) AS LEVEL_1_SELECTED_CNT, 
            NVL (SUM (RS.LEVEL_1_ACHIEVED_CNT), 0) AS LEVEL_1_ACHIEVED_CNT, 
            CASE 
               WHEN NVL (SUM (RS.LEVEL_1_SELECTED_CNT), 0) = 0 
               THEN 
                  0 
               ELSE 
                  NVL ( 
                     ROUND ( 
                        (NVL (SUM (RS.LEVEL_1_ACHIEVED_CNT), 0) 
                         / NVL (SUM (RS.LEVEL_1_SELECTED_CNT), 0))*100, 
                        2), 
                     0) 
            END 
               AS LEVEL_1_ACHIEVED_PERCENT, 
            NVL (SUM (RS.LEVEL_1_AWARD_CNT), 0) AS LEVEL_1_AWARD_CNT, 
            NVL (SUM (RS.LEVEL_2_SELECTED_CNT), 0) AS LEVEL_2_SELECTED_CNT, 
            NVL (SUM (RS.LEVEL_2_ACHIEVED_CNT), 0) AS LEVEL_2_ACHIEVED_CNT, 
            CASE 
               WHEN NVL (SUM (RS.LEVEL_2_SELECTED_CNT), 0) = 0 
               THEN 
                  0 
               ELSE 
                  NVL ( 
                     ROUND ( 
                        (NVL (SUM (RS.LEVEL_2_ACHIEVED_CNT), 0) 
                         / NVL (SUM (RS.LEVEL_2_SELECTED_CNT), 0))*100, 
                        2), 
                     0) 
            END 
               AS LEVEL_2_ACHIEVED_PERCENT, 
            NVL (SUM (RS.LEVEL_2_AWARD_CNT), 0) AS LEVEL_2_AWARD_CNT, 
            NVL (SUM (RS.LEVEL_3_SELECTED_CNT), 0) AS LEVEL_3_SELECTED_CNT, 
            NVL (SUM (RS.LEVEL_3_ACHIEVED_CNT), 0) AS LEVEL_3_ACHIEVED_CNT, 
            CASE 
               WHEN NVL (SUM (RS.LEVEL_3_SELECTED_CNT), 0) = 0 
               THEN 
                  0 
               ELSE 
                  NVL ( 
                     ROUND ( 
                        (NVL (SUM (RS.LEVEL_3_ACHIEVED_CNT), 0) 
                         / NVL (SUM (RS.LEVEL_3_SELECTED_CNT), 0))*100, 
                        2), 
                     0) 
            END 
               AS LEVEL_3_ACHIEVED_PERCENT, 
            NVL (SUM (RS.LEVEL_3_AWARD_CNT), 0) AS LEVEL_3_AWARD_CNT, 
            NVL (SUM (RS.LEVEL_4_SELECTED_CNT), 0) AS LEVEL_4_SELECTED_CNT, 
            NVL (SUM (RS.LEVEL_4_ACHIEVED_CNT), 0) AS LEVEL_4_ACHIEVED_CNT, 
            CASE 
               WHEN NVL (SUM (RS.LEVEL_4_SELECTED_CNT), 0) = 0 
               THEN 
                  0 
               ELSE 
                  NVL ( 
                     ROUND ( 
                        (NVL (SUM (RS.LEVEL_4_ACHIEVED_CNT), 0) 
                         / NVL (SUM (RS.LEVEL_4_SELECTED_CNT), 0))*100, 
                        2), 
                     0) 
            END 
               AS LEVEL_4_ACHIEVED_PERCENT, 
            NVL (SUM (RS.LEVEL_4_AWARD_CNT), 0) AS LEVEL_4_AWARD_CNT, 
            NVL (SUM (RS.LEVEL_5_SELECTED_CNT), 0) AS LEVEL_5_SELECTED_CNT, 
            NVL (SUM (RS.LEVEL_5_ACHIEVED_CNT), 0) AS LEVEL_5_ACHIEVED_CNT, 
            CASE 
               WHEN NVL (SUM (RS.LEVEL_5_SELECTED_CNT), 0) = 0 
               THEN 
                  0 
               ELSE 
                  NVL ( 
                     ROUND ( 
                        (NVL (SUM (RS.LEVEL_5_ACHIEVED_CNT), 0) 
                         / NVL (SUM (RS.LEVEL_5_SELECTED_CNT), 0))*100, 
                        2), 
                     0) 
            END 
               AS LEVEL_5_ACHIEVED_PERCENT, 
            NVL (SUM (RS.LEVEL_5_AWARD_CNT), 0) AS LEVEL_5_AWARD_CNT, 
            NVL (SUM (RS.LEVEL_6_SELECTED_CNT), 0) AS LEVEL_6_SELECTED_CNT, 
            NVL (SUM (RS.LEVEL_6_ACHIEVED_CNT), 0) AS LEVEL_6_ACHIEVED_CNT, 
            CASE 
               WHEN NVL (SUM (RS.LEVEL_6_SELECTED_CNT), 0) = 0 
               THEN 
                  0 
               ELSE 
                  NVL ( 
                     ROUND ( 
                        (NVL (SUM (RS.LEVEL_6_ACHIEVED_CNT), 0) 
                         / NVL (SUM (RS.LEVEL_6_SELECTED_CNT), 0))*100, 
                        2), 
                     0) 
            END 
               AS LEVEL_6_ACHIEVED_PERCENT, 
            NVL (SUM (RS.LEVEL_6_AWARD_CNT), 0) AS LEVEL_6_AWARD_CNT, 
            NVL (SUM (RS.BASE_QUANTITY), 0) AS BASE_QUANTITY, 
            NVL (SUM (RS.CHALLENGEPOINT), 0) AS CHALLENGEPOINT, 
            NVL (SUM (RS.ACTUAL_RESULT), 0) AS ACTUAL_RESULT, 
            CASE 
               WHEN NVL (SUM (RS.CHALLENGEPOINT), 0) = 0 
               THEN 
                  0 
               ELSE 
                 /* ROUND ( 
                     (NVL (SUM (RS.ACTUAL_RESULT), 0) 
                      / NVL (SUM (RS.CHALLENGEPOINT), 0)) 
                     * 100, 
                     2) */  --08/24/2017  G6-2895 
                  ROUND ( 
                     (NVL (SUM (RS.ACTUAL_RESULT), 0) 
                      / NVL (SUM (RS.CHALLENGEPOINT), 0)) 
                     * 100, 
                     MAX(ACHIEVEMENT_PRECISION))  --08/24/2017  G6-2895 
                 
            END 
               AS PERCENT_ACHIEVED 
       FROM (SELECT node_name AS NODE_NAME, 
                    node_id AS NODE_ID, 
                    total_selected AS TOTAL_SELECTED, 
                    NVL (level_1_total_selected, 0) AS LEVEL_1_SELECTED_CNT, 
                    NVL (level_1_nbr_achieved, 0) AS LEVEL_1_ACHIEVED_CNT, 
                    NVL (level_1_awards, 0) AS LEVEL_1_AWARD_CNT, 
                    NVL (level_2_total_selected, 0) AS LEVEL_2_SELECTED_CNT, 
                    NVL (level_2_nbr_achieved, 0) AS LEVEL_2_ACHIEVED_CNT, 
                    NVL (level_2_awards, 0) AS LEVEL_2_AWARD_CNT, 
                    NVL (level_3_total_selected, 0) AS LEVEL_3_SELECTED_CNT, 
                    NVL (level_3_nbr_achieved, 0) AS LEVEL_3_ACHIEVED_CNT, 
                    NVL (level_3_awards, 0) AS LEVEL_3_AWARD_CNT, 
                    NVL (level_4_total_selected, 0) AS LEVEL_4_SELECTED_CNT, 
                    NVL (level_4_nbr_achieved, 0) AS LEVEL_4_ACHIEVED_CNT, 
                    NVL (level_4_awards, 0) AS LEVEL_4_AWARD_CNT, 
                    NVL (level_5_total_selected, 0) AS LEVEL_5_SELECTED_CNT, 
                    NVL (level_5_nbr_achieved, 0) AS LEVEL_5_ACHIEVED_CNT, 
                    NVL (level_5_awards, 0) AS LEVEL_5_AWARD_CNT, 
                    NVL (level_6_total_selected, 0) AS LEVEL_6_SELECTED_CNT, 
                    NVL (level_6_nbr_achieved, 0) AS LEVEL_6_ACHIEVED_CNT, 
                    NVL (level_6_awards, 0) AS LEVEL_6_AWARD_CNT, 
                    NVL (base, 0) AS BASE_QUANTITY, 
                    NVL (goal, 0) AS CHALLENGEPOINT, 
                    NVL (actual_result, 0) AS ACTUAL_RESULT, 
                    NVL (achievement_precision,0) AS ACHIEVEMENT_PRECISION,  --08/24/2017  G6-2895 
                    0 AS IS_LEAF 
               FROM (WITH pivot_set 
                          AS (SELECT total_selected, 
                                     nbr_achieved, 
                                     awards, 
                                     NVL (sequence_num, 0) sequence_num, 
                                     rh.node_id AS node_id, 
                                     rh.node_name node_name, 
                                     base, 
                                     goal, 
                                     actual_result,
                                     achievement_precision --08/24/2017  G6-2895   
                                FROM (SELECT DISTINCT 
                                             rh.node_id AS node_id, 
                                             rh.node_name node_name 
                                        FROM rpt_hierarchy rh, 
                                             rpt_cp_selection_summary rpt 
                                       WHERE detail_node_id = rh.node_id 
                                             AND rpt.record_type LIKE '%node%' 
                                             AND NVL (header_node_id, 0) IN 
                                                    (SELECT * 
                                                       FROM TABLE ( 
                                                               get_array_varchar ( 
                                                                  p_in_parentNodeId)))) rh, 
                                     (  SELECT SUM (total_selected) total_selected, 
                                               SUM (nbr_challengepoint_achieved) 
                                                  nbr_achieved, 
                                               SUM (calculated_payout) AS awards, 
                                               gl.sequence_num, 
                                               rh.node_id AS node_id, 
                                               rh.node_name node_name, 
                                               SUM (rpt.base_quantity) base, 
                                               SUM (rpt.goal) goal, 
                                               SUM (rpt.actual_result) actual_result,--08/24/2017  G6-2895 
                                               MAX (achievement_precision) achievement_precision --08/24/2017  G6-2895 
                                          FROM rpt_hierarchy rh, 
                                               promotion p, 
                                               --promo_challengepoint promoGoal, --08/24/2017  G6-2895  starts
                                               (select CASE WHEN p.achievement_precision ='zero' THEN 0
                                                            WHEN p.achievement_precision ='one' THEN 1
                                                            WHEN p.achievement_precision ='two' THEN 2
                                                            WHEN p.achievement_precision ='three' THEN 3
                                                            WHEN p.achievement_precision ='four' THEN 4
                                                       ELSE 0 END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promoGoal, --08/24/2017  G6-2895   ends
                                               rpt_cp_selection_summary rpt, 
                                               goalquest_goallevel gl 
                                         WHERE rpt.level_id = gl.goallevel_id(+) 
                                               AND rpt.promotion_id = 
                                                      promoGoal.promotion_id(+) 
                                               AND rpt.promotion_id = 
                                                      p.promotion_id(+) 
                                               AND detail_node_id = rh.node_id 
                                               AND rpt.record_type LIKE '%node%' 
                                               AND NVL (header_node_id, 0) IN 
                                                      (SELECT * 
                                                         FROM TABLE ( 
                                                                 get_array_varchar ( 
                                                                    p_in_parentNodeId))) 
                                               AND rpt.pax_status = 
                                                      NVL (p_in_participantStatus, 
                                                           rpt.pax_status) 
                                              AND  NVL(rpt.job_position,'JOB')       
                                                        = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
                                               AND ( (p_in_departments IS NULL) 
                                                    OR (p_in_departments IS NOT NULL 
                                                        AND rpt.department IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_departments))))) 
                                               AND ( (p_in_promotionId IS NULL) 
                                                    OR (p_in_promotionId IS NOT NULL 
                                                        AND rpt.promotion_id IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_promotionId))))) 
                                               AND NVL (p.promotion_status, ' ') = 
                                                      NVL ( 
                                                         p_in_promotionStatus, 
                                                         NVL (p.promotion_status, 
                                                              ' ')) 
                                      GROUP BY gl.sequence_num, 
                                               rh.node_id, 
                                               rh.node_name) rpc 
                               WHERE rh.node_id = rpc.node_id(+)) 
                     SELECT node_name, 
                            node_id, 
                            (  NVL (level_1_total_selected, 0) 
                             + NVL (level_2_total_selected, 0) 
                             + NVL (level_3_total_selected, 0) 
                             + NVL (level_4_total_selected, 0) 
                             + NVL (level_5_total_selected, 0) 
                             + NVL (level_6_total_selected, 0)) 
                               total_selected, 
                            no_goal_total_selected, 
                            level_1_total_selected, 
                            level_1_nbr_achieved, 
                            level_1_awards, 
                            level_2_total_selected, 
                            level_2_nbr_achieved, 
                            level_2_awards, 
                            level_3_total_selected, 
                            level_3_nbr_achieved, 
                            level_3_awards, 
                            level_4_total_selected, 
                            level_4_nbr_achieved, 
                            level_4_awards, 
                            level_5_total_selected, 
                            level_5_nbr_achieved, 
                            level_5_awards, 
                            level_6_total_selected, 
                            level_6_nbr_achieved, 
                            level_6_awards, 
                            (  NVL (level_1_base, 0) 
                             + NVL (level_2_base, 0) 
                             + NVL (level_3_base, 0) 
                             + NVL (level_4_base, 0) 
                             + NVL (level_5_base, 0) 
                             + NVL (level_6_base, 0)) 
                               base, 
                            (  NVL (level_1_goal, 0) 
                             + NVL (level_2_goal, 0) 
                             + NVL (level_3_goal, 0) 
                             + NVL (level_4_goal, 0) 
                             + NVL (level_5_goal, 0) 
                             + NVL (level_6_goal, 0)) 
                               goal, 
                            (  NVL (level_1_actual_result, 0) 
                             + NVL (level_2_actual_result, 0) 
                             + NVL (level_3_actual_result, 0) 
                             + NVL (level_4_actual_result, 0) 
                             + NVL (level_5_actual_result, 0) 
                             + NVL (level_6_actual_result, 0)) 
                               actual_result,
                            GREATEST
                            (  NVL (level_1_achievement_precision,0),
                               NVL (level_2_achievement_precision,0),
                               NVL (level_3_achievement_precision,0),
                               NVL (level_4_achievement_precision,0),
                               NVL (level_5_achievement_precision,0),
                               NVL (level_6_achievement_precision,0)) achievement_precision --08/24/2017  G6-2895 
                       FROM pivot_set PIVOT (SUM (total_selected) total_selected, 
                                            SUM (nbr_achieved) nbr_achieved, 
                                            SUM (awards) awards, 
                                            SUM (base) base, 
                                            SUM (goal) goal, 
                                            SUM (actual_result) actual_result ,
                                            MAX (achievement_precision) achievement_precision  --08/24/2017  G6-2895 
                                      FOR sequence_num 
                                      IN  (0 AS no_goal, 
                                          1 AS level_1, 
                                          2 AS level_2, 
                                          3 AS level_3, 
                                          4 AS level_4, 
                                          5 AS level_5, 
                                          6 AS level_6)) t WHERE  p_in_nodeAndBelow = '1')  --09/23/2014 
             UNION 
             SELECT node_name || ' Team' AS NODE_NAME, 
                    node_id AS NODE_ID, 
                    total_selected AS TOTAL_SELECTED, 
                    NVL (level_1_total_selected, 0) AS LEVEL_1_SELECTED_CNT, 
                    NVL (level_1_nbr_achieved, 0) AS LEVEL_1_ACHIEVED_CNT, 
                    NVL (level_1_awards, 0) AS LEVEL_1_AWARD_CNT, 
                    NVL (level_2_total_selected, 0) AS LEVEL_2_SELECTED_CNT, 
                    NVL (level_2_nbr_achieved, 0) AS LEVEL_2_ACHIEVED_CNT, 
                    NVL (level_2_awards, 0) AS LEVEL_2_AWARD_CNT, 
                    NVL (level_3_total_selected, 0) AS LEVEL_3_SELECTED_CNT, 
                    NVL (level_3_nbr_achieved, 0) AS LEVEL_3_ACHIEVED_CNT, 
                    NVL (level_3_awards, 0) AS LEVEL_3_AWARD_CNT, 
                    NVL (level_4_total_selected, 0) AS LEVEL_4_SELECTED_CNT, 
                    NVL (level_4_nbr_achieved, 0) AS LEVEL_4_ACHIEVED_CNT, 
                    NVL (level_4_awards, 0) AS LEVEL_4_AWARD_CNT, 
                    NVL (level_5_total_selected, 0) AS LEVEL_5_SELECTED_CNT, 
                    NVL (level_5_nbr_achieved, 0) AS LEVEL_5_ACHIEVED_CNT, 
                    NVL (level_5_awards, 0) AS LEVEL_5_AWARD_CNT, 
                    NVL (level_6_total_selected, 0) AS LEVEL_6_SELECTED_CNT, 
                    NVL (level_6_nbr_achieved, 0) AS LEVEL_6_ACHIEVED_CNT, 
                    NVL (level_6_awards, 0) AS LEVEL_6_AWARD_CNT, 
                    NVL (base, 0) AS BASE_QUANTITY, 
                    NVL (goal, 0) AS AMT_TO_ACHIEVE, 
                    NVL (actual_result, 0) AS CURRENT_VALUE,
                    NVL (achievement_precision,0) AS  ACHIEVEMENT_PRECISION, --08/24/2017  G6-2895 
                    1 AS IS_LEAF 
               FROM (WITH pivot_set 
                          AS (SELECT total_selected, 
                                     nbr_achieved, 
                                     awards, 
                                     NVL (sequence_num, 0) sequence_num, 
                                     rh.node_id AS node_id, 
                                     rh.node_name node_name, 
                                     base, 
                                     goal, 
                                     actual_result,
                                     achievement_precision   --08/24/2017  G6-2895 
                                FROM (SELECT DISTINCT 
                                             rh.node_id AS node_id, 
                                             rh.node_name node_name 
                                        FROM rpt_hierarchy rh, 
                                             rpt_cp_selection_summary rpt 
                                       WHERE detail_node_id = rh.node_id 
                                             AND rpt.record_type LIKE '%team%' 
                                             AND NVL (detail_node_id, 0) IN 
                                                    (SELECT * 
                                                       FROM TABLE ( 
                                                               get_array_varchar ( 
                                                                  p_in_parentNodeId)))) rh, 
                                     (  SELECT SUM (total_selected) total_selected, 
                                               SUM (nbr_challengepoint_achieved) 
                                                  nbr_achieved, 
                                               SUM (calculated_payout) AS awards, 
                                               gl.sequence_num, 
                                               rh.node_id AS node_id, 
                                               rh.node_name node_name, 
                                               SUM (rpt.base_quantity) base, 
                                               SUM (rpt.goal) goal, 
                                               SUM (rpt.actual_result) actual_result,
                                               MAX(achievement_precision) achievement_precision --08/24/2017  G6-2895 
                                          FROM rpt_hierarchy rh, 
                                               promotion p, 
                                               --promo_challengepoint promoGoal, --08/24/2017  G6-2895 
                                               (select CASE WHEN p.achievement_precision ='zero' THEN 0
                                                            WHEN p.achievement_precision ='one' THEN 1
                                                            WHEN p.achievement_precision ='two' THEN 2
                                                            WHEN p.achievement_precision ='three' THEN 3
                                                            WHEN p.achievement_precision ='four' THEN 4
                                                       ELSE 0 END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promogoal , --08/24/2017  G6-2895 
                                               rpt_cp_selection_summary rpt, 
                                               goalquest_goallevel gl 
                                         WHERE rpt.level_id = gl.goallevel_id(+) 
                                               AND rpt.promotion_id = 
                                                      promoGoal.promotion_id(+) 
                                               AND rpt.promotion_id = 
                                                      p.promotion_id(+) 
                                               AND detail_node_id = rh.node_id 
                                               AND rpt.record_type LIKE '%team%' 
                                               AND NVL (detail_node_id, 0) IN 
                                                      (SELECT * 
                                                         FROM TABLE ( 
                                                                 get_array_varchar ( 
                                                                    p_in_parentNodeId))) 
                                               AND rpt.pax_status = 
                                                      NVL (p_in_participantStatus, 
                                                           rpt.pax_status) 
                                               AND  NVL(rpt.job_position,'JOB')       
                                                        = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB')) 
                                               AND ( (p_in_departments IS NULL) 
                                                    OR (p_in_departments IS NOT NULL 
                                                        AND rpt.department IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_departments))))) 
                                               AND ( (p_in_promotionId IS NULL) 
                                                    OR (p_in_promotionId IS NOT NULL 
                                                        AND rpt.promotion_id IN 
                                                               (SELECT * 
                                                                  FROM TABLE ( 
                                                                          get_array_varchar ( 
                                                                             p_in_promotionId))))) 
                                               AND NVL (p.promotion_status, ' ') = 
                                                      NVL ( 
                                                         p_in_promotionStatus, 
                                                         NVL (p.promotion_status, 
                                                              ' ')) 
                                      GROUP BY gl.sequence_num, 
                                               rh.node_id, 
                                               rh.node_name) rpc 
                               WHERE rh.node_id = rpc.node_id(+)) 
                     SELECT node_name, 
                            node_id, 
                            (  NVL (level_1_total_selected, 0) 
                             + NVL (level_2_total_selected, 0) 
                             + NVL (level_3_total_selected, 0) 
                             + NVL (level_4_total_selected, 0) 
                             + NVL (level_5_total_selected, 0) 
                             + NVL (level_6_total_selected, 0)) 
                               total_selected, 
                            no_goal_total_selected, 
                            level_1_total_selected, 
                            level_1_nbr_achieved, 
                            level_1_awards, 
                            level_2_total_selected, 
                            level_2_nbr_achieved, 
                            level_2_awards, 
                            level_3_total_selected, 
                            level_3_nbr_achieved, 
                            level_3_awards, 
                            level_4_total_selected, 
                            level_4_nbr_achieved, 
                            level_4_awards, 
                            level_5_total_selected, 
                            level_5_nbr_achieved, 
                            level_5_awards, 
                            level_6_total_selected, 
                            level_6_nbr_achieved, 
                            level_6_awards, 
                            (  NVL (level_1_base, 0) 
                             + NVL (level_2_base, 0) 
                             + NVL (level_3_base, 0) 
                             + NVL (level_4_base, 0) 
                             + NVL (level_5_base, 0) 
                             + NVL (level_6_base, 0)) 
                               base, 
                            (  NVL (level_1_goal, 0) 
                             + NVL (level_2_goal, 0) 
                             + NVL (level_3_goal, 0) 
                             + NVL (level_4_goal, 0) 
                             + NVL (level_5_goal, 0) 
                             + NVL (level_6_goal, 0)) 
                               goal, 
                            (  NVL (level_1_actual_result, 0) 
                             + NVL (level_2_actual_result, 0) 
                             + NVL (level_3_actual_result, 0) 
                             + NVL (level_4_actual_result, 0) 
                             + NVL (level_5_actual_result, 0) 
                             + NVL (level_6_actual_result, 0)) 
                               actual_result,
                             GREATEST
                             ( NVL(level_1_achievement_precision,0),
                               NVL(level_2_achievement_precision,0),
                               NVL(level_3_achievement_precision,0),
                               NVL(level_4_achievement_precision,0),
                               NVL(level_5_achievement_precision,0),
                               NVL(level_6_achievement_precision,0)) achievement_precision --08/24/2017  G6-2895 
                       FROM pivot_set PIVOT (SUM (total_selected) total_selected, 
                                            SUM (nbr_achieved) nbr_achieved, 
                                            SUM (awards) awards, 
                                            SUM (base) base, 
                                            SUM (goal) goal, 
                                            SUM (actual_result) actual_result,
                                            MAX (achievement_precision) achievement_precision
                                      FOR sequence_num 
                                      IN  (0 AS no_goal, 
                                          1 AS level_1, 
                                          2 AS level_2, 
                                          3 AS level_3, 
                                          4 AS level_4, 
                                          5 AS level_5, 
                                          6 AS level_6)) t)) RS ;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         --OPEN p_out_size_data FOR SELECT NULL FROM DUAL;
          p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPAchievementTabRes;   

/******************************************************************************
  NAME:       prc_getCPAchievementTabRes
  Author              Date           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
 --Suresh J        10/17/2014       Bug 57494  - Pax Achievement Base Value not matching
 --Swati           04/16/2015       Bug 61511 - Reports: The Point value displayed in the Participant Achievement Report is wrong
 --Chidamba        08/24/2017       G6-2895 - Challengepoint % Goal should display according to achievement precision
******************************************************************************/
/* getChallengePointAchievementDetailResults  */
 PROCEDURE prc_getCPAchievementDetailRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPAchievementDetailRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPAchievementDetailRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, rs.* 
               FROM (SELECT    gsd.last_name 
                            || '' , '' 
                            || gsd.first_name 
                            || '' '' 
                            || gsd.middle_init 
                               AS PAX_NAME, 
                            hier.node_name AS NODE_NAME, 
                            gsd.promotion_name AS PROMO_NAME, 
                            ''Level '' || gl.sequence_num AS LEVEL_NUMBER, 
                            NVL (gsd.base_quantity, 0) AS BASE, 
                            NVL (gsd.amount_to_achieve, 0) AS CHALLENGEPOINT, 
                            NVL (gsd.current_value, 0) AS ACTUAL, 
                            /*CASE                 --08/24/2017  G6-2895 
                               WHEN NVL (gsd.amount_to_achieve, 0) = 0 
                               THEN 
                                  0 
                               ELSE 
                                  ROUND ( 
                                       (  NVL (gsd.current_value, 0) 
                                        / gsd.amount_to_achieve) 
                                     * 100, 
                                     2) 
                            END */                     --08/24/2017  G6-2895  commented
                            progress_challengepoint    --08/24/2017  G6-2895 
                               AS PERCENT_OF_CHALLENGEPOINT, 
                            gsd.achieved AS ACHIEVED, 
                              NVL (gsd.calculated_payout, 0) 
                           -- + NVL (gsd.basic_award_deposited, 0) --04/16/2015 Bug 61511
                               AS TOTAL_POINTS 
                       FROM promo_challengepoint promoGoal, 
                            promotion p, 
                       rpt_cp_selection_detail gsd         
           LEFT OUTER JOIN rpt_hierarchy hier  
           ON gsd.node_id = hier.node_id  
           LEFT OUTER JOIN promotion promo  
           ON gsd.promotion_id = promo.promotion_id  
           RIGHT OUTER JOIN goalquest_paxgoal pg  
           ON gsd.pax_goal_id = pg.pax_goal_id  
           LEFT OUTER JOIN goalquest_goallevel gl  
           ON pg.goallevel_id      = gl.goallevel_id 
                      WHERE     NVL (gsd.node_id, 0) IN (SELECT * 
                                                           FROM TABLE ( 
                                                                   get_array_varchar ( 
                                                                      '''||p_in_parentNodeId||'''))) 
                            AND gsd.promotion_id = promoGoal.promotion_id 
                            AND gsd.promotion_id = p.promotion_id 
                            AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' '')) 
                            AND gsd.user_status = 
                                   NVL ( '''||p_in_participantStatus||''', gsd.user_status) 
                            AND NVL(gsd.job_position,''JOB'')               
                            = NVL('''||p_in_jobPosition||''',NVL(gsd.job_position,''JOB''))
                            AND (   ( '''||p_in_departments||''' IS NULL) 
                                 OR (    '''||p_in_departments||''' IS NOT NULL 
                                     AND gsd.department IN (SELECT * 
                                                              FROM TABLE ( 
                                                                      get_array_varchar ( 
                                                                         '''||p_in_departments||'''))))) 
                            AND (   ( '''||p_in_promotionId||''' IS NULL) 
                                 OR (    '''||p_in_promotionId||''' IS NOT NULL 
                                     AND gsd.promotion_id IN (SELECT * 
                                                                FROM TABLE ( 
                                                                        get_array_varchar ( 
                                                                           '''||p_in_promotionId||'''))))) AND pg.goallevel_id is not null) rs         --10/17/2014
                                                                           ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getChallengePointAchievementDetailResultsSize  */
    -- OPEN p_out_size_data FOR
     SELECT count(1) INTO  p_out_size_data --AS MAX_SIZE
     FROM promo_goalquest promoGoal, 
          promotion p, 
      RPT_CP_SELECTION_DETAIL gsd 
     LEFT OUTER JOIN RPT_HIERARCHY hier 
      ON gsd.node_id = hier.node_id 
     LEFT OUTER JOIN PROMOTION promo 
     ON gsd.promotion_id = promo.promotion_id 
     RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg 
     ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
     ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID 
      WHERE NVL(gsd.node_id,0)                            IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     AND gsd.promotion_id                                = promoGoal.promotion_id 
      AND gsd.promotion_id = p.promotion_id 
      AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
     AND gsd.user_status                                 = NVL(p_in_participantStatus,gsd.user_status) 
       AND NVL(gsd.job_position,'JOB')                   = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
      AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))
     AND pg.goallevel_id is not null;      --10/17/2014;
/* getChallengePointAchievementDetailResultsTotals  */
     OPEN p_out_totals_data FOR
     SELECT NVL(SUM(BASE),0)                     AS BASE, 
     NVL(SUM(CHALLENGEPOINT),0)                          AS CHALLENGEPOINT,
     NVL(SUM(ACTUAL),0)                        AS ACTUAL,
     CASE WHEN NVL(SUM(CHALLENGEPOINT),0) = 0 THEN 0
       ELSE ROUND((NVL(SUM(ACTUAL),0) /SUM(CHALLENGEPOINT)) * 100,max(achievement_precision))-- 2)   --08/24/2017  G6-2895 
     END AS PERCENT_OF_CHALLENGEPOINT,
     NVL(SUM(TOTAL_POINTS),0)                        AS TOTAL_POINTS 
     FROM (
     SELECT gsd.last_name
     || ' , '
     || gsd.first_name 
     || ' '
     || gsd.middle_init AS PAX_NAME,
     hier.node_name     AS NODE_NAME,
     gsd.promotion_name AS PROMO_NAME,
     'Level '
     || gl.sequence_num           AS LEVEL_NUMBER,
     NVL(gsd.base_quantity,0)                     AS BASE,
     NVL(gsd.amount_to_achieve,0)                 AS CHALLENGEPOINT,
     NVL(gsd.current_value,0)                     AS ACTUAL,
      gsd.achieved                 AS IS_ACHIEVED,
      NVL(gsd.calculated_payout,0) AS CHALLENGEPOINT_POINTS,
     NVL(gsd.basic_award_deposited,0) AS BASIC_POINTS_EARNED,
      NVL(achievement_precision,0) AS  ACHIEVEMENT_PRECISION,    --08/24/2017  G6-2895 
      NVL(gsd.calculated_payout,0) --+ NVL(gsd.basic_award_deposited,0)   --04/16/2015 Bug 61511  
      AS TOTAL_POINTS
     FROM --promo_goalquest promoGoal,
     (select CASE WHEN p.achievement_precision ='zero' THEN 0
                  WHEN p.achievement_precision ='one' THEN 1
                  WHEN p.achievement_precision ='two' THEN 2
                  WHEN p.achievement_precision ='three' THEN 3
                  WHEN p.achievement_precision ='four' THEN 4
                  ELSE 0 END  achievement_precision,p.promotion_id,p.rounding_method from promo_goalquest p ) promogoal,   --08/24/2017  G6-2895 
          promotion p, 
     RPT_CP_SELECTION_DETAIL gsd 
     LEFT OUTER JOIN RPT_HIERARCHY hier
     ON gsd.node_id = hier.node_id
     LEFT OUTER JOIN PROMOTION promo
     ON gsd.promotion_id = promo.promotion_id 
     RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg 
     ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl 
      ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID
      WHERE NVL(gsd.node_id,0)                            IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND gsd.promotion_id                                = promoGoal.promotion_id 
     AND gsd.promotion_id = p.promotion_id 
      AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
     AND gsd.user_status                                 = NVL(p_in_participantStatus,gsd.user_status)
      AND NVL(gsd.job_position,'JOB')                 = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
      AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
     AND pg.goallevel_id is not null      --10/17/2014
      ) rs;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         --OPEN p_out_size_data FOR SELECT NULL FROM DUAL;
          p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPAchievementDetailRes;       

/******************************************************************************
  NAME:       prc_getCPPctAchievedChart
  Date               Author           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
 Suresh J          09/23/2014      Bug Fix 56790  -  Challengepoint % Goal Achievement Chart is displayed when Summary has No Data.
  ******************************************************************************/   

/* getChallengePointAchievementPercentageAchievedChart  */
 PROCEDURE prc_getCPPctAchievedChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPPctAchievedChart' ;
      v_stage          VARCHAR2 (500);
   BEGIN
      v_stage   := 'getCPPctAchievedChart';
     OPEN p_out_data FOR     
     SELECT ROUND(DECODE(SUM(level_1_selected_cnt),0,0,(SUM(level_1_achieved_cnt) /SUM(level_1_selected_cnt))*100),2) AS LEVEL_1_ACHIEVED_PERCENT, 
     ROUND(DECODE(SUM(level_2_selected_cnt),0,0,(SUM(level_2_achieved_cnt) /SUM(level_2_selected_cnt))*100),2) AS LEVEL_2_ACHIEVED_PERCENT, 
     ROUND(DECODE(SUM(level_3_selected_cnt),0,0,(SUM(level_3_achieved_cnt) /SUM(level_3_selected_cnt))*100),2) AS LEVEL_3_ACHIEVED_PERCENT,
     ROUND(DECODE(SUM(level_4_selected_cnt),0,0,(SUM(level_4_achieved_cnt) /SUM(level_4_selected_cnt))*100),2) AS LEVEL_4_ACHIEVED_PERCENT,
     ROUND(DECODE(SUM(level_5_selected_cnt),0,0,(SUM(level_5_achieved_cnt) /SUM(level_5_selected_cnt))*100),2) AS LEVEL_5_ACHIEVED_PERCENT, 
     ROUND(DECODE(SUM(level_6_selected_cnt),0,0,(SUM(level_6_achieved_cnt) /SUM(level_6_selected_cnt))*100),2) AS LEVEL_6_ACHIEVED_PERCENT
     FROM
     (SELECT node_name     AS NODE_NAME,
      node_id    AS NODE_ID,
      NVL(level_1_total_selected,0)    AS LEVEL_1_SELECTED_CNT,
     NVL(level_1_nbr_achieved,0)   AS LEVEL_1_ACHIEVED_CNT, 
      NVL(level_2_total_selected,0)    AS LEVEL_2_SELECTED_CNT,
      NVL(level_2_nbr_achieved,0)   AS LEVEL_2_ACHIEVED_CNT, 
      NVL(level_3_total_selected,0)    AS LEVEL_3_SELECTED_CNT,
      NVL(level_3_nbr_achieved,0)   AS LEVEL_3_ACHIEVED_CNT, 
      NVL(level_4_total_selected,0)    AS LEVEL_4_SELECTED_CNT,
      NVL(level_4_nbr_achieved,0)   AS LEVEL_4_ACHIEVED_CNT, 
      NVL(level_5_total_selected,0)    AS LEVEL_5_SELECTED_CNT, 
      NVL(level_5_nbr_achieved,0)   AS LEVEL_5_ACHIEVED_CNT,
      NVL(level_6_total_selected,0)    AS LEVEL_6_SELECTED_CNT, 
      NVL(level_6_nbr_achieved,0)   AS LEVEL_6_ACHIEVED_CNT,
      0      AS IS_LEAF
       FROM
      ( WITH pivot_set AS
      (SELECT total_selected ,
     nbr_achieved,
     awards,
     NVL(sequence_num,0) sequence_num,
     rh.node_id AS node_id,
     rh.node_name node_name,
     base,
     goal,
     actual_result
      FROM (SELECT DISTINCT rh.node_id AS node_id,
      rh.node_name node_name
       FROM rpt_hierarchy rh,
     rpt_cp_selection_summary rpt
     WHERE detail_node_id    = rh.node_id
     AND rpt.record_type LIKE '%node%'
       AND NVL(header_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     ) rh,
     (SELECT SUM(total_selected) total_selected,
       SUM(nbr_challengepoint_achieved) nbr_achieved,
       SUM(calculated_payout)  AS awards,
     gl.sequence_num,
     rh.node_id AS node_id,
     rh.node_name node_name,
     SUM(rpt.base_quantity) base,
     SUM(rpt.goal) goal,
     SUM(rpt.actual_result) actual_result
     FROM
     rpt_hierarchy rh,
     promotion p,
     promo_challengepoint promoGoal,
     rpt_cp_selection_summary rpt ,
     goalquest_goallevel gl
      WHERE rpt.level_id    = gl.goallevel_id (+)
      AND rpt.promotion_id    = promoGoal.promotion_id (+)
      AND rpt.promotion_id   = p.promotion_id (+)
      AND detail_node_id    = rh.node_id
       AND rpt.record_type LIKE '%node%'
      AND NVL(header_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
      AND rpt.pax_status    = NVL(p_in_participantStatus,rpt.pax_status)
     AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
      AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
      AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
      GROUP BY  gl.sequence_num,
     rh.node_id ,
     rh.node_name
      ) rpc
     WHERE rh.node_id    = rpc.node_id (+)
      )
       SELECT node_name,
      node_id,
    (NVL(level_1_total_selected,0)+NVL(level_2_total_selected,0)+NVL(level_3_total_selected,0)+NVL(level_4_total_selected,0)+NVL(level_5_total_selected,0)+NVL(level_6_total_selected,0)) total_selected,
      no_goal_total_selected,
      level_1_total_selected,
      level_1_nbr_achieved,
      level_1_awards,
      level_2_total_selected,
      level_2_nbr_achieved, 
      level_2_awards, 
      level_3_total_selected,
      level_3_nbr_achieved,
      level_3_awards,
      level_4_total_selected,
      level_4_nbr_achieved,
      level_4_awards,
      level_5_total_selected,
      level_5_nbr_achieved,
      level_5_awards,
      level_6_total_selected,
      level_6_nbr_achieved,
      level_6_awards,
      (NVL(level_1_base,0)+NVL(level_2_base,0)+NVL(level_3_base,0)+NVL(level_4_base,0)+NVL(level_5_base,0)+NVL(level_6_base,0)) base,
      (NVL(level_1_goal,0)+NVL(level_2_goal,0)+NVL(level_3_goal,0)+NVL(level_4_goal,0)+NVL(level_5_goal,0)+NVL(level_6_goal,0)) goal,
    (NVL(level_1_actual_result,0)+NVL(level_2_actual_result,0)+NVL(level_3_actual_result,0)+NVL(level_4_actual_result,0)+NVL(level_5_actual_result,0)+NVL(level_6_actual_result,0)) actual_result
       FROM pivot_set PIVOT (SUM(total_selected) total_selected, SUM(nbr_achieved) nbr_achieved, SUM(awards) awards,SUM(base) base,sum(goal) goal, sum(actual_result) actual_result FOR sequence_num IN (0 AS no_goal, 1 AS level_1, 2 AS level_2, 3 AS level_3, 4 AS level_4, 5 AS level_5, 6 AS level_6) ) t
    WHERE  p_in_nodeAndBelow = '1'    --09/23/2014
     )
     UNION
     SELECT node_name||' Team'     AS NODE_NAME,
      node_id    AS NODE_ID,
      NVL(level_1_total_selected,0)    AS LEVEL_1_SELECTED_CNT,
      NVL(level_1_nbr_achieved,0)   AS LEVEL_1_ACHIEVED_CNT, 
      NVL(level_2_total_selected,0)    AS LEVEL_2_SELECTED_CNT,
      NVL(level_2_nbr_achieved,0)   AS LEVEL_2_ACHIEVED_CNT,
      NVL(level_3_total_selected,0)    AS LEVEL_3_SELECTED_CNT,
      NVL(level_3_nbr_achieved,0)   AS LEVEL_3_ACHIEVED_CNT, 
      NVL(level_4_total_selected,0)    AS LEVEL_4_SELECTED_CNT,
      NVL(level_4_nbr_achieved,0)   AS LEVEL_4_ACHIEVED_CNT, 
      NVL(level_5_total_selected,0)    AS LEVEL_5_SELECTED_CNT,
      NVL(level_5_nbr_achieved,0)   AS LEVEL_5_ACHIEVED_CNT,
      NVL(level_6_total_selected,0)    AS LEVEL_6_SELECTED_CNT,
      NVL(level_6_nbr_achieved,0)   AS LEVEL_6_ACHIEVED_CNT,
      1      AS IS_LEAF
       FROM  
       (WITH pivot_set AS
      (SELECT total_selected ,
     nbr_achieved,
     awards,
     NVL(sequence_num,0) sequence_num,
     rh.node_id AS node_id,
     rh.node_name node_name,
     base,
     goal, 
     actual_result
      FROM (SELECT DISTINCT rh.node_id AS node_id,
      rh.node_name node_name
       FROM rpt_hierarchy rh,
      rpt_cp_selection_summary rpt
      WHERE detail_node_id    = rh.node_id
     AND rpt.record_type LIKE '%team%'
     AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     ) rh,
     (SELECT SUM(total_selected) total_selected,
       SUM(nbr_challengepoint_achieved) nbr_achieved,
       SUM(calculated_payout)  AS awards,
     gl.sequence_num,
     rh.node_id AS node_id,
     rh.node_name node_name,
     SUM(rpt.base_quantity) base,
     SUM(rpt.goal) goal,
     SUM(rpt.actual_result) actual_result
     FROM
     rpt_hierarchy rh,
     promotion p,
     promo_challengepoint promoGoal,
     rpt_cp_selection_summary rpt ,
     goalquest_goallevel gl
      WHERE rpt.level_id    = gl.goallevel_id (+)
      AND rpt.promotion_id    = promoGoal.promotion_id (+)
      AND rpt.promotion_id   = p.promotion_id (+)
      AND detail_node_id    = rh.node_id 
       AND rpt.record_type LIKE '%team%' 
      AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
      AND rpt.pax_status    = NVL(p_in_participantStatus,rpt.pax_status)
     AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
      AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
      AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
      AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
      GROUP BY  gl.sequence_num,
     rh.node_id ,
     rh.node_name
      ) rpc
     WHERE rh.node_id    = rpc.node_id (+)
      )
       SELECT node_name,
      node_id,
    (NVL(level_1_total_selected,0)+NVL(level_2_total_selected,0)+NVL(level_3_total_selected,0)+NVL(level_4_total_selected,0)+NVL(level_5_total_selected,0)+NVL(level_6_total_selected,0)) total_selected,
      no_goal_total_selected,
      level_1_total_selected,
      level_1_nbr_achieved,
      level_1_awards,
      level_2_total_selected,
      level_2_nbr_achieved,
      level_2_awards,
      level_3_total_selected,
      level_3_nbr_achieved, 
      level_3_awards,
      level_4_total_selected,
      level_4_nbr_achieved,
      level_4_awards,
      level_5_total_selected,
      level_5_nbr_achieved,
      level_5_awards,
      level_6_total_selected,
      level_6_nbr_achieved,
      level_6_awards,
      (NVL(level_1_base,0)+NVL(level_2_base,0)+NVL(level_3_base,0)+NVL(level_4_base,0)+NVL(level_5_base,0)+NVL(level_6_base,0)) base,
      (NVL(level_1_goal,0)+NVL(level_2_goal,0)+NVL(level_3_goal,0)+NVL(level_4_goal,0)+NVL(level_5_goal,0)+NVL(level_6_goal,0)) goal,
    (NVL(level_1_actual_result,0)+NVL(level_2_actual_result,0)+NVL(level_3_actual_result,0)+NVL(level_4_actual_result,0)+NVL(level_5_actual_result,0)+NVL(level_6_actual_result,0)) actual_result
       FROM pivot_set PIVOT (SUM(total_selected) total_selected, SUM(nbr_achieved) nbr_achieved, SUM(awards) awards,SUM(base) base,sum(goal) goal, sum(actual_result) actual_result FOR sequence_num IN (0 AS no_goal, 1 AS level_1, 2 AS level_2, 3 AS level_3, 4 AS level_4, 5 AS level_5, 6 AS level_6) ) t 
     )
     );
     p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;       
 END prc_getCPPctAchievedChart;       
 
/******************************************************************************
  NAME:       prc_getCPCountAchievedChart
  Date               Author           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
 Suresh J          09/23/2014      Bug Fix 56790  -  Challengepoint % Goal Achievement Chart is displayed when Summary has No Data.
  ******************************************************************************/   

/* getChallengePointAchievementCountAchievedChart  */
PROCEDURE prc_getCPCountAchievedChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPCountAchievedChart' ;
      v_stage          VARCHAR2 (500);
   BEGIN
      v_stage   := 'getCPCountAchievedChart';
     OPEN p_out_data FOR     
     SELECT SUM(level_1_achieved_cnt) AS LEVEL_1_ACHIEVED_CNT, 
     SUM(level_2_achieved_cnt)  AS LEVEL_2_ACHIEVED_CNT,
     SUM(level_3_achieved_cnt)  AS LEVEL_3_ACHIEVED_CNT, 
     SUM(level_4_achieved_cnt)  AS LEVEL_4_ACHIEVED_CNT, 
     SUM(level_5_achieved_cnt)  AS LEVEL_5_ACHIEVED_CNT,
     SUM(level_6_achieved_cnt)  AS LEVEL_6_ACHIEVED_CNT
     FROM 
     (SELECT node_name AS NODE_NAME,
       node_id AS NODE_ID,
       NVL(level_1_nbr_achieved,0)    AS LEVEL_1_ACHIEVED_CNT,
       NVL(level_2_nbr_achieved,0)    AS LEVEL_2_ACHIEVED_CNT,
       NVL(level_3_nbr_achieved,0)    AS LEVEL_3_ACHIEVED_CNT,
       NVL(level_4_nbr_achieved,0)    AS LEVEL_4_ACHIEVED_CNT,
       NVL(level_5_nbr_achieved,0)    AS LEVEL_5_ACHIEVED_CNT,
       NVL(level_6_nbr_achieved,0)    AS LEVEL_6_ACHIEVED_CNT,
       0  AS IS_LEAF
     FROM
       ( WITH pivot_set AS
       (SELECT total_selected ,
     nbr_achieved,
     awards,
     NVL(sequence_num,0) sequence_num,
     rh.node_id AS node_id,
     rh.node_name node_name,
     base,
     goal,
     actual_result
       FROM (SELECT DISTINCT rh.node_id AS node_id,
     rh.node_name node_name
        FROM rpt_hierarchy rh,
     rpt_cp_selection_summary rpt
     WHERE detail_node_id = rh.node_id
       AND rpt.record_type LIKE '%node%'
       AND NVL(header_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
        ) rh,
        (SELECT SUM(total_selected) total_selected,
      SUM(nbr_challengepoint_achieved) nbr_achieved,
      SUM(calculated_payout)  AS awards,
     gl.sequence_num,
     rh.node_id AS node_id,
     rh.node_name node_name,
     SUM(rpt.base_quantity) base,
     SUM(rpt.goal) goal,
     SUM(rpt.actual_result) actual_result
     FROM
     rpt_hierarchy rh,
     promotion p,
     promo_challengepoint promoGoal,
     rpt_cp_selection_summary rpt ,
     goalquest_goallevel gl
     WHERE rpt.level_id   = gl.goallevel_id (+)
     AND rpt.promotion_id  = promoGoal.promotion_id (+)
     AND rpt.promotion_id  = p.promotion_id (+)
     AND detail_node_id    = rh.node_id
      AND rpt.record_type LIKE '%node%'
     AND NVL(header_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND rpt.pax_status  = NVL(p_in_participantStatus,rpt.pax_status)
    AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
     AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
     GROUP BY  gl.sequence_num,
     rh.node_id ,
     rh.node_name
     ) rpc
      WHERE rh.node_id    = rpc.node_id (+)
         ) SELECT node_name,
          node_id, 
     (NVL(level_1_total_selected,0)+NVL(level_2_total_selected,0)+NVL(level_3_total_selected,0)+NVL(level_4_total_selected,0)+NVL(level_5_total_selected,0)+NVL(level_6_total_selected,0)) total_selected,
     no_goal_total_selected,
     level_1_total_selected, 
     level_1_nbr_achieved, 
     level_1_awards,
     level_2_total_selected,
     level_2_nbr_achieved,
     level_2_awards,
     level_3_total_selected,
     level_3_nbr_achieved,
     level_3_awards,
     level_4_total_selected,
     level_4_nbr_achieved,
     level_4_awards,
     level_5_total_selected,
     level_5_nbr_achieved,
     level_5_awards,
     level_6_total_selected,
     level_6_nbr_achieved,
     level_6_awards,
     (NVL(level_1_base,0)+NVL(level_2_base,0)+NVL(level_3_base,0)+NVL(level_4_base,0)+NVL(level_5_base,0)+NVL(level_6_base,0)) base,
     (NVL(level_1_goal,0)+NVL(level_2_goal,0)+NVL(level_3_goal,0)+NVL(level_4_goal,0)+NVL(level_5_goal,0)+NVL(level_6_goal,0)) goal,
     (NVL(level_1_actual_result,0)+NVL(level_2_actual_result,0)+NVL(level_3_actual_result,0)+NVL(level_4_actual_result,0)+NVL(level_5_actual_result,0)+NVL(level_6_actual_result,0)) actual_result
     FROM pivot_set PIVOT (SUM(total_selected) total_selected, SUM(nbr_achieved) nbr_achieved, SUM(awards) awards,SUM(base) base,sum(goal) goal, sum(actual_result) actual_result FOR sequence_num IN (0 AS no_goal, 1 AS level_1, 2 AS level_2, 3 AS level_3, 4 AS level_4, 5 AS level_5, 6 AS level_6) ) t
     WHERE  p_in_nodeAndBelow = '1'    --09/23/2014
      )
      UNION
      SELECT node_name||' Team' AS NODE_NAME,
     node_id AS NODE_ID,
     NVL(level_1_nbr_achieved,0)    AS LEVEL_1_ACHIEVED_CNT,
     NVL(level_2_nbr_achieved,0)    AS LEVEL_2_ACHIEVED_CNT, 
     NVL(level_3_nbr_achieved,0)    AS LEVEL_3_ACHIEVED_CNT,
     NVL(level_4_nbr_achieved,0)    AS LEVEL_4_ACHIEVED_CNT,
     NVL(level_5_nbr_achieved,0)    AS LEVEL_5_ACHIEVED_CNT,
     NVL(level_6_nbr_achieved,0)    AS LEVEL_6_ACHIEVED_CNT,
     1  AS IS_LEAF 
     FROM
     (WITH pivot_set AS
     (SELECT total_selected ,
       nbr_achieved,
       awards,
       NVL(sequence_num,0) sequence_num,
       rh.node_id AS node_id,
       rh.node_name node_name,
       base,
       goal,
       actual_result
     FROM (SELECT DISTINCT rh.node_id AS node_id,
     rh.node_name node_name
        FROM rpt_hierarchy rh,
     rpt_cp_selection_summary rpt
       WHERE detail_node_id    = rh.node_id
     AND rpt.record_type LIKE '%team%'
     AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
      ) rh,
      (SELECT SUM(total_selected) total_selected,
        SUM(nbr_challengepoint_achieved) nbr_achieved,
        SUM(calculated_payout)  AS awards,
       gl.sequence_num,
       rh.node_id AS node_id,
       rh.node_name node_name,
       SUM(rpt.base_quantity) base,
       SUM(rpt.goal) goal,
       SUM(rpt.actual_result) actual_result
       FROM
       rpt_hierarchy rh,
       promotion p,
       promo_challengepoint promoGoal,
       rpt_cp_selection_summary rpt ,
       goalquest_goallevel gl 
     WHERE rpt.level_id   = gl.goallevel_id (+)
     AND rpt.promotion_id    = promoGoal.promotion_id (+)
     AND rpt.promotion_id  = p.promotion_id (+)
     AND detail_node_id    = rh.node_id
      AND rpt.record_type LIKE '%team%'
     AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND rpt.pax_status  = NVL(p_in_participantStatus,rpt.pax_status)
     AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
     AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
     GROUP BY  gl.sequence_num,
     rh.node_id ,
       rh.node_name
     ) rpc
      WHERE rh.node_id    = rpc.node_id (+)
     )
     SELECT node_name,
     node_id,
      (NVL(level_1_total_selected,0)+NVL(level_2_total_selected,0)+NVL(level_3_total_selected,0)+NVL(level_4_total_selected,0)+NVL(level_5_total_selected,0)+NVL(level_6_total_selected,0)) total_selected,
     no_goal_total_selected,
     level_1_total_selected,
     level_1_nbr_achieved,
     level_1_awards,
     level_2_total_selected,
     level_2_nbr_achieved,
     level_2_awards,
     level_3_total_selected,
     level_3_nbr_achieved,
     level_3_awards,
     level_4_total_selected,
     level_4_nbr_achieved,
     level_4_awards,
     level_5_total_selected,
     level_5_nbr_achieved,
     level_5_awards,
     level_6_total_selected,
     level_6_nbr_achieved,
     level_6_awards,
     (NVL(level_1_base,0)+NVL(level_2_base,0)+NVL(level_3_base,0)+NVL(level_4_base,0)+NVL(level_5_base,0)+NVL(level_6_base,0)) base,
         (NVL(level_1_goal,0)+NVL(level_2_goal,0)+NVL(level_3_goal,0)+NVL(level_4_goal,0)+NVL(level_5_goal,0)+NVL(level_6_goal,0)) goal,
     (NVL(level_1_actual_result,0)+NVL(level_2_actual_result,0)+NVL(level_3_actual_result,0)+NVL(level_4_actual_result,0)+NVL (level_5_actual_result,0)+NVL (level_6_actual_result,0)) actual_result
     FROM pivot_set PIVOT (SUM(total_selected) total_selected, SUM(nbr_achieved) nbr_achieved, SUM(awards)  awards,SUM(base) base,sum(goal) goal, sum(actual_result) actual_result FOR sequence_num IN (0 AS no_goal, 1 AS level_1, 2 AS level_2, 3 AS level_3, 4 AS level_4, 5 AS level_5, 6 AS level_6) ) t
         )
         );
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getCPCountAchievedChart; 
/* getChallengePointAchievementResultsChart  */
PROCEDURE prc_getCPAchievementResChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPAchievementResChart' ;
      v_stage          VARCHAR2 (500);
   BEGIN
      v_stage   := 'getCPAchievementResChart';
     OPEN p_out_data FOR     
     SELECT * 
     FROM
     (SELECT gsd.last_name
     || ' , ' 
     || gsd.first_name
     || ' ' 
     || gsd.middle_init pax_name,
     gsd.promotion_name,
     hier.node_name,
     NVL(gsd.base_quantity,0) base_quantity,
      NVL(gsd.amount_to_achieve,0) CHALLENGPOINT,
     NVL(gsd.current_value,0) ACTUAL_RESULTS 
     FROM promo_goalquest promoGoal,
      RPT_CP_SELECTION_DETAIL gsd 
    LEFT OUTER JOIN RPT_HIERARCHY hier 
     ON gsd.node_id = hier.node_id 
     LEFT OUTER JOIN PROMOTION promo 
     ON gsd.promotion_id = promo.promotion_id 
     RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg 
      ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID 
     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
     ON pg.GOALLEVEL_ID      = gl.GOALLEVEL_ID 
     WHERE NVL(gsd.node_id,0) IN
    (SELECT child_node_id 
     FROM rpt_hierarchy_rollup 
     WHERE node_id       IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
      ) 
     AND gsd.promotion_id                                = promoGoal.promotion_id
     AND gsd.user_status                                 = NVL(p_in_participantStatus,gsd.user_status)
    AND NVL(gsd.job_position,'JOB')                 = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
     AND NVL(promo.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' '))  
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
     ORDER BY actual_results desc, PAX_NAME ASC ) 
     where rownum <= 10 ;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getCPAchievementResChart; 
/* getChallengePointSelectionTabularResults  */
PROCEDURE prc_getCPSelectionTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPSelectionTabRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPSelectionTabRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN, 
         rs.* 
       FROM 
     ( SELECT node_name           AS NODE_NAME, 
       node_id                  AS NODE_ID, 
       total_participant        AS TOTAL_PARTICIPANTS, 
       NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED, 
       NVL(level_1_selected,0)  AS LEVEL_1_SELECTED, 
       NVL(level_1_sele_perc,0) AS LEVEL_1_SELECTED_PERCENT, 
       NVL(level_2_selected,0)  AS LEVEL_2_SELECTED, 
       NVL(level_2_sele_perc,0) AS LEVEL_2_SELECTED_PERCENT, 
       NVL(level_3_selected,0)  AS LEVEL_3_SELECTED, 
       NVL(level_3_sele_perc,0) AS LEVEL_3_SELECTED_PERCENT, 
       NVL(level_4_selected,0)  AS LEVEL_4_SELECTED, 
       NVL(level_4_sele_perc,0) AS LEVEL_4_SELECTED_PERCENT, 
       NVL(level_5_selected,0)  AS LEVEL_5_SELECTED, 
       NVL(level_5_sele_perc,0) AS LEVEL_5_SELECTED_PERCENT, 
       NVL(level_6_selected,0)  AS LEVEL_6_SELECTED, 
       NVL(level_6_sele_perc,0) AS LEVEL_6_SELECTED_PERCENT, 
       0                        AS IS_LEAF 
     FROM 
       ( WITH pivot_set AS 
       (SELECT total_participants AS sum_total_pax, 
         NVL(sequence_num,0) sequence_num, 
         rh.node_id AS node_id, 
         rh.node_name node_name 
       FROM 
         (SELECT DISTINCT rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           rpt_cp_selection_summary rpt 
         WHERE detail_node_id = rh.node_id 
         AND rpt.record_type LIKE ''%node%'' 
         AND NVL(header_node_id,0) IN 
           (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ) 
           ) 
         ) rh, 
         (SELECT SUM(NVL(total_participants,0)) total_participants , 
           gl.sequence_num, 
           rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           promotion p, 
           promo_challengepoint promoGoal, 
           rpt_cp_selection_summary rpt , 
           goalquest_goallevel gl 
         WHERE rpt.level_id   = gl.goallevel_id (+) 
         AND rpt.promotion_id = promoGoal.promotion_id (+) 
         AND rpt.promotion_id = p.promotion_id 
         AND detail_node_id   = rh.node_id 
         AND rpt.record_type LIKE ''%node%'' 
         AND NVL(header_node_id,0) IN 
           (SELECT                  * 
           FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ) 
           ) 
         AND rpt.pax_status   = NVL('''||p_in_participantStatus||''',rpt.pax_status) 
         AND  NVL(rpt.job_position,''JOB'')                     = NVL('''||p_in_jobPosition||''',NVL(rpt.job_position,''JOB''))
         AND (('''||p_in_departments||'''   IS NULL) 
         OR ('''||p_in_departments||'''     IS NOT NULL 
         AND rpt.department  IN 
           (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')) 
           ))) 
         AND (('''||p_in_promotionId||'''   IS NULL) 
         OR ('''||p_in_promotionId||'''     IS NOT NULL 
         AND rpt.promotion_id IN 
           (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')) 
           ))) 
         AND NVL(p.promotion_status,'' '') = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' '')) 
         GROUP BY sequence_num, 
           rh.node_id , 
           rh.node_name 
         ) rpc 
       WHERE rh.node_id = rpc.node_id (+) 
       UNION
       SELECT total_participants AS sum_total_pax, 
         NVL(sequence_num,0) sequence_num, 
         rh.node_id AS node_id, 
         rh.node_name 
         || '' Team'' node_name 
       FROM 
         (SELECT DISTINCT rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           rpt_cp_selection_summary rpt 
         WHERE detail_node_id = rh.node_id 
         AND rpt.record_type LIKE ''%team%'' 
         AND NVL(detail_node_id,0) IN 
           (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ) 
           ) 
         ) rh, 
         (SELECT SUM(NVL(total_participants,0)) total_participants , 
           gl.sequence_num, 
           rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           promotion p, 
           promo_challengepoint promoGoal, 
           rpt_cp_selection_summary rpt , 
           goalquest_goallevel gl 
         WHERE rpt.level_id   = gl.goallevel_id (+) 
         AND rpt.promotion_id = promoGoal.promotion_id (+) 
         AND rpt.promotion_id = p.promotion_id (+) 
         AND detail_node_id   = rh.node_id 
         AND rpt.record_type LIKE ''%team%'' 
         AND NVL(detail_node_id,0) IN 
           (SELECT                  * 
           FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ) 
           ) 
         AND rpt.pax_status   = NVL('''||p_in_participantStatus||''',rpt.pax_status) 
        AND  NVL(rpt.job_position,''JOB'')                     = NVL('''||p_in_jobPosition||''',NVL(rpt.job_position,''JOB''))
         AND (('''||p_in_departments||'''   IS NULL) 
         OR ('''||p_in_departments||'''     IS NOT NULL 
         AND rpt.department  IN 
           (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')) 
           ))) 
         AND (('''||p_in_promotionId||'''   IS NULL) 
         OR ('''||p_in_promotionId||'''     IS NOT NULL 
         AND rpt.promotion_id IN 
           (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')) 
           ))) 
         AND NVL(p.promotion_status,'' '') = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' '')) 
         GROUP BY sequence_num, 
           rh.node_id , 
           rh.node_name 
         ) rpc 
       WHERE rh.node_id = rpc.node_id (+) 
       ) 
     SELECT t.node_name, 
       t.node_id, 
       (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant, 
       t.no_goal_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)     +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.no_goal_selected,0)/(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END no_goal_sele_perc, 
       t.level_1_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)     +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_1_selected,0)/(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_1_sele_perc, 
       t.level_2_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)     +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_2_selected,0)/(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_2_sele_perc, 
       t.level_3_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_3_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_3_sele_perc, 
       t.level_4_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_4_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_4_sele_perc, 
       t.level_5_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_5_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_5_sele_perc, 
       t.level_6_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_6_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_6_sele_perc 
     FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected, 6 AS level_6_selected) ) t 
       ) 
      ) rs 
       ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
       /* getChallengePointSelectionResultsSize  */
     SELECT count(1) INTO  p_out_size_data --AS MAX_SIZE 
    FROM 
     ( SELECT node_name           AS NODE_NAME, 
       node_id                  AS NODE_ID, 
       total_participant        AS TOTAL_PARTICIPANTS, 
       NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED, 
       NVL(level_1_selected,0)  AS LEVEL_1_SELECTED, 
       NVL(level_1_sele_perc,0) AS LEVEL_1_SELECTED_PERCENT, 
       NVL(level_2_selected,0)  AS LEVEL_2_SELECTED, 
       NVL(level_2_sele_perc,0) AS LEVEL_2_SELECTED_PERCENT, 
       NVL(level_3_selected,0)  AS LEVEL_3_SELECTED, 
       NVL(level_3_sele_perc,0) AS LEVEL_3_SELECTED_PERCENT, 
       NVL(level_4_selected,0)  AS LEVEL_4_SELECTED, 
       NVL(level_4_sele_perc,0) AS LEVEL_4_SELECTED_PERCENT, 
       NVL(level_5_selected,0)  AS LEVEL_5_SELECTED, 
       NVL(level_5_sele_perc,0) AS LEVEL_5_SELECTED_PERCENT, 
       NVL(level_6_selected,0)  AS LEVEL_6_SELECTED, 
       NVL(level_6_sele_perc,0) AS LEVEL_6_SELECTED_PERCENT, 
       0                        AS IS_LEAF 
     FROM 
       ( WITH pivot_set AS 
       (SELECT total_participants AS sum_total_pax, 
         NVL(sequence_num,0) sequence_num, 
         rh.node_id AS node_id, 
         rh.node_name node_name 
       FROM 
         (SELECT DISTINCT rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           rpt_cp_selection_summary rpt 
         WHERE detail_node_id = rh.node_id 
         AND rpt.record_type LIKE '%node%' 
         AND NVL(header_node_id,0) IN 
           (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ) 
           ) 
         ) rh, 
         (SELECT SUM(NVL(total_participants,0)) total_participants , 
           gl.sequence_num, 
           rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           promotion p, 
           promo_challengepoint promoGoal, 
           rpt_cp_selection_summary rpt , 
           goalquest_goallevel gl 
         WHERE rpt.level_id   = gl.goallevel_id (+) 
         AND rpt.promotion_id = promoGoal.promotion_id (+) 
         AND rpt.promotion_id = p.promotion_id 
         AND detail_node_id   = rh.node_id 
         AND rpt.record_type LIKE '%node%' 
         AND NVL(header_node_id,0) IN 
           (SELECT                  * 
           FROM TABLE(get_array_varchar( p_in_parentNodeId ) ) 
           ) 
         AND rpt.pax_status   = NVL(p_in_participantStatus,rpt.pax_status) 
        AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
         AND ((p_in_departments   IS NULL) 
         OR (p_in_departments     IS NOT NULL 
         AND rpt.department  IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
           ))) 
         AND ((p_in_promotionId   IS NULL) 
         OR (p_in_promotionId     IS NOT NULL 
         AND rpt.promotion_id IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
           ))) 
         AND NVL(p.promotion_status,' ') = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
         GROUP BY sequence_num, 
           rh.node_id , 
           rh.node_name 
         ) rpc 
       WHERE rh.node_id = rpc.node_id (+) 
       UNION
       SELECT total_participants AS sum_total_pax, 
         NVL(sequence_num,0) sequence_num, 
         rh.node_id AS node_id, 
         rh.node_name 
         || ' Team' node_name 
       FROM 
         (SELECT DISTINCT rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           rpt_cp_selection_summary rpt 
         WHERE detail_node_id = rh.node_id 
         AND rpt.record_type LIKE '%team%' 
         AND NVL(detail_node_id,0) IN 
           (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ) 
           ) 
         ) rh, 
         (SELECT SUM(NVL(total_participants,0)) total_participants , 
           gl.sequence_num, 
           rh.node_id AS node_id, 
           rh.node_name node_name 
         FROM rpt_hierarchy rh, 
           promotion p, 
           promo_challengepoint promoGoal, 
           rpt_cp_selection_summary rpt , 
           goalquest_goallevel gl 
         WHERE rpt.level_id   = gl.goallevel_id (+) 
         AND rpt.promotion_id = promoGoal.promotion_id (+) 
         AND rpt.promotion_id = p.promotion_id (+) 
         AND detail_node_id   = rh.node_id 
         AND rpt.record_type LIKE '%team%' 
         AND NVL(detail_node_id,0) IN 
           (SELECT                  * 
           FROM TABLE(get_array_varchar( p_in_parentNodeId ) ) 
           ) 
         AND rpt.pax_status   = NVL(p_in_participantStatus,rpt.pax_status) 
         AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
         AND ((p_in_departments   IS NULL) 
         OR (p_in_departments     IS NOT NULL 
         AND rpt.department  IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_departments)) 
           ))) 
         AND ((p_in_promotionId   IS NULL) 
         OR (p_in_promotionId     IS NOT NULL 
         AND rpt.promotion_id IN 
           (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)) 
           ))) 
         AND NVL(p.promotion_status,' ') = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
         GROUP BY sequence_num, 
           rh.node_id , 
           rh.node_name 
         ) rpc 
       WHERE rh.node_id = rpc.node_id (+) 
       ) 
     SELECT t.node_name, 
       t.node_id, 
       (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant, 
       t.no_goal_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)     +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.no_goal_selected,0)/(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END no_goal_sele_perc, 
       t.level_1_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)     +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_1_selected,0)/(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_1_sele_perc, 
       t.level_2_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)     +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_2_selected,0)/(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_2_sele_perc, 
       t.level_3_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_3_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_3_sele_perc, 
       t.level_4_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_4_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_4_sele_perc, 
       t.level_5_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_5_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_5_sele_perc, 
       t.level_6_selected, 
       CASE 
         WHEN (NVL(t.no_goal_selected,0)      +NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) > 0 
         THEN ROUND(NVL(t.level_6_selected,0) /(NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) *100,2) 
         ELSE 0 
       END level_6_sele_perc 
     FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected, 6 AS level_6_selected) ) t 
       ) 
      ) ;
/* getChallengePointSelectionTabularResultsTotals  */
          OPEN p_out_totals_data FOR
          SELECT TOTAL_PARTICIPANTS, 
            NO_GOAL_SELECTED,  
            LEVEL_1_SELECTED,      
            CASE WHEN TOTAL_PARTICIPANTS > 0 THEN    
            ROUND((LEVEL_1_SELECTED /TOTAL_PARTICIPANTS)*100,2)                
            ELSE 0 END LEVEL_1_SELE_PERC,            
            LEVEL_2_SELECTED,      
            CASE WHEN TOTAL_PARTICIPANTS > 0 THEN    
            ROUND((LEVEL_2_SELECTED /TOTAL_PARTICIPANTS)*100,2)                
            ELSE 0 END LEVEL_2_SELE_PERC,            
            LEVEL_3_SELECTED,      
            CASE WHEN TOTAL_PARTICIPANTS > 0 THEN    
            ROUND((level_3_selected /TOTAL_PARTICIPANTS)*100,2)    
            ELSE 0 END LEVEL_3_SELE_PERC,            
            LEVEL_4_SELECTED,      
            CASE WHEN TOTAL_PARTICIPANTS > 0 THEN    
            ROUND((level_4_selected /TOTAL_PARTICIPANTS)*100,2)    
            ELSE 0 END LEVEL_4_SELE_PERC,            
            LEVEL_5_SELECTED,      
            CASE WHEN TOTAL_PARTICIPANTS > 0 THEN    
            ROUND((level_5_selected /TOTAL_PARTICIPANTS)*100,2)    
            ELSE 0 END LEVEL_5_SELE_PERC,            
            LEVEL_6_SELECTED,      
            CASE WHEN TOTAL_PARTICIPANTS > 0 THEN    
            ROUND((level_6_selected /TOTAL_PARTICIPANTS)*100,2)    
            ELSE 0 END LEVEL_6_SELE_PERC              
            FROM                 
            (            
            SELECT NVL(SUM(TOTAL_PARTICIPANTS),0) TOTAL_PARTICIPANTS,     
            NVL(SUM(NO_GOAL_SELECTED),0) NO_GOAL_SELECTED,         
            NVL(SUM(LEVEL_1_SELECTED),0) LEVEL_1_SELECTED, 
            NVL(SUM(LEVEL_2_SELECTED),0) LEVEL_2_SELECTED, 
            NVL(SUM(LEVEL_3_SELECTED),0) LEVEL_3_SELECTED, 
            NVL(SUM(LEVEL_4_SELECTED),0) LEVEL_4_SELECTED, 
            NVL(SUM(LEVEL_5_SELECTED),0) LEVEL_5_SELECTED, 
            NVL(SUM(LEVEL_6_SELECTED),0) LEVEL_6_SELECTED   
            FROM                 
            (            
            SELECT total_participant   AS TOTAL_PARTICIPANTS,     
            NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED,      
            NVL(level_1_selected,0)  AS LEVEL_1_SELECTED,            
            NVL(level_2_selected,0)  AS LEVEL_2_SELECTED,            
            NVL(level_3_selected,0)  AS LEVEL_3_SELECTED,            
            NVL(level_4_selected,0)  AS LEVEL_4_SELECTED,            
            NVL(level_5_selected,0)  AS LEVEL_5_SELECTED,            
            NVL(level_6_selected,0)  AS LEVEL_6_SELECTED             
            FROM                 
            ( WITH pivot_set AS      
            (SELECT total_participants AS sum_total_pax, 
            NVL(sequence_num,0) sequence_num,            
            rh.node_id AS node_id,              
            rh.node_name node_name     
            FROM (SELECT DISTINCT rh.node_id AS node_id,            
            rh.node_name node_name     
            FROM rpt_hierarchy rh,              
            rpt_cp_selection_summary rpt               
            WHERE detail_node_id = rh.node_id    
            AND rpt.record_type LIKE '%node%'     
            AND NVL(header_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))              
            ) rh,      
          (SELECT total_participants ,        
          gl.sequence_num,         
          rh.node_id AS node_id,               
          rh.node_name node_name      
          FROM 
          rpt_hierarchy rh,             
          promotion p,    
          promo_challengepoint promoGoal,       
          rpt_cp_selection_summary rpt ,              
          goalquest_goallevel gl 
          WHERE rpt.level_id   = gl.goallevel_id (+)             
          AND rpt.promotion_id= promoGoal.promotion_id (+) 
          AND rpt.promotion_id= p.promotion_id (+)       
          AND detail_node_id    = rh.node_id       
          AND rpt.record_type LIKE '%node%'      
          AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))              
          AND rpt.pax_status  = NVL(p_in_participantStatus,rpt.pax_status)   
          AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))  
          AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))         
          AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))        
          AND NVL(p.promotion_status,' ')= NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))               
          ) rpc      
          where rh.node_id    = rpc.node_id (+)   
          )             
          SELECT t.node_name,   
          t.node_id,          
          (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant,             
          t.no_goal_selected,      
          t.level_1_selected,        
          t.level_2_selected,        
          t.level_3_selected,        
          t.level_4_selected,        
          t.level_5_selected,        
          t.level_6_selected         
          FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS    
          level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected,6 AS level_6_selected) ) t          
          )             
          UNION ALL --09/11/2014 Chidamba Bug #56428              
          SELECT total_participant,             
          NVL(no_goal_selected,0),          
          NVL(level_1_selected,0),            
          NVL(level_2_selected,0),            
          NVL(level_3_selected,0),            
          NVL(level_4_selected,0),            
          NVL(level_5_selected,0),            
          NVL(level_6_selected,0)             
          FROM 
          ( WITH pivot_set AS       
          (SELECT total_participants AS sum_total_pax,   
          NVL(sequence_num,0) sequence_num,             
          rh.node_id AS node_id,               
          rh.node_name || ' Team' node_name                 
          FROM (SELECT DISTINCT rh.node_id AS node_id,             
          rh.node_name node_name      
          FROM rpt_hierarchy rh,               
          rpt_cp_selection_summary rpt                
          WHERE detail_node_id   = rh.node_id   
          AND rpt.record_type LIKE '%team%'      
          AND NVL(detail_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))   
          ) rh,       
          (SELECT total_participants ,         
          gl.sequence_num,         
          rh.node_id AS node_id,               
          rh.node_name node_name      
          FROM 
          rpt_hierarchy rh,             
          promotion p,    
          promo_challengepoint promoGoal,       
          rpt_cp_selection_summary rpt ,              
          goalquest_goallevel gl 
          WHERE rpt.level_id    = gl.goallevel_id (+)            
          AND rpt.promotion_id  = promoGoal.promotion_id (+)                
          AND rpt.promotion_id    = p.promotion_id (+)   
          AND detail_node_id   = rh.node_id        
          AND rpt.record_type LIKE '%team%'      
          AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
          AND rpt.pax_status    = NVL(p_in_participantStatus,rpt.pax_status)                 
         AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
          AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))         
          AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))        
          AND NVL(p.promotion_status,' ')    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))           
          ) rpc      
          where rh.node_id   = rpc.node_id (+)    
          )             
          SELECT t.node_name,   
          t.node_id,          
            (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant,             
          t.no_goal_selected,      
          t.level_1_selected,        
          t.level_2_selected,        
          t.level_3_selected,        
          t.level_4_selected,        
          t.level_5_selected,        
          t.level_6_selected         
          FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS    
          level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected,6 AS level_6_selected) ) t          
          )             
          )             
          ) rs ;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPSelectionTabRes;      
/* getChallengePointSelectionDetailResults  */
    PROCEDURE prc_getCPSelectionDetailRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPSelectionDetailRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPSelectionDetailRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN,   
            rs.*    
            FROM   
            (SELECT gsd.last_name     
            || '' , ''    
          || gsd.first_name  
          || '' ''  
           || gsd.middle_init AS PAX_NAME,    
           hier.node_name     AS NODE_NAME,   
              CASE WHEN gl.sequence_num IS NULL 
                              THEN NULL 
          ELSE  
         ''Level '' || gl.sequence_num END LEVEL_NUMBER, 
           gsd.promotion_name AS PROMO_NAME    
          FROM promo_challengepoint promoGoal,   
               promotion p,  
           RPT_CP_SELECTION_DETAIL gsd  
           LEFT OUTER JOIN RPT_HIERARCHY hier   
           ON gsd.node_id = hier.node_id    
            LEFT OUTER JOIN PROMOTION promo   
            ON gsd.promotion_id = promo.promotion_id    
            LEFT OUTER JOIN GOALQUEST_PAXGOAL pg     
           ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID  
           LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl  
          ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID    
           WHERE NVL(gsd.node_id,0)                            IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''' ) ))   
           AND gsd.promotion_id                                = promoGoal.promotion_id    
            AND gsd.promotion_id = p.promotion_id  
            AND NVL(p.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))  
          AND gsd.user_status                                 = NVL('''||p_in_participantStatus||''',gsd.user_status)   
         AND NVL(gsd.job_position,''JOB'')                 = NVL('''||p_in_jobPosition||''',NVL(gsd.job_position,''JOB''))
           AND (('''||p_in_departments||''' IS NULL) OR ('''||p_in_departments||''' is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_departments||''')))))   
           AND (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar('''||p_in_promotionId||''')))))  
           ORDER BY '|| v_sortCol ||' --09/15/2014 nagarajs Bug 56238    
             ) rs 
         --ORDER BY '|| v_sortCol ||' --09/15/2014 nagarajs Bug 56238    
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
/* getChallengePointSelectionDetailResultsSize  */
 --OPEN p_out_size_data FOR
         SELECT COUNT (1) INTO  p_out_size_data -- AS MAX_SIZE 
     FROM promo_challengepoint promoGoal,
         promotion p, 
     RPT_CP_SELECTION_DETAIL gsd   
     LEFT OUTER JOIN RPT_HIERARCHY hier
     ON gsd.node_id = hier.node_id
      LEFT OUTER JOIN PROMOTION promo 
     ON gsd.promotion_id = promo.promotion_id
     LEFT OUTER JOIN GOALQUEST_PAXGOAL pg   -- RIGTH  --09/15/2014 nagarajs Bug 56238    
     ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID 
     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
     ON pg.GOALLEVEL_ID                                  = gl.GOALLEVEL_ID
     WHERE NVL(gsd.node_id,0)                            IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND gsd.promotion_id                                = promoGoal.promotion_id
    --  AND gsd.level_id IS NOT NULL         --09/15/2014 nagarajs Bug 56238                                            
     AND gsd.promotion_id = p.promotion_id 
     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
     AND gsd.user_status                                 = NVL(p_in_participantStatus,gsd.user_status)
      AND NVL(gsd.job_position,'JOB')                 = NVL(p_in_jobPosition,NVL(gsd.job_position,'JOB'))
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND gsd.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))   
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND gsd.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) ;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         --OPEN p_out_size_data FOR SELECT NULL FROM DUAL; 
          p_out_size_data := NULL;       
 END prc_getCPSelectionDetailRes;
/* getChallengePointSelectionValidLevelNumbers  */
  PROCEDURE prc_getCPSelectionValidLvlNums (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPSelectionValidLvlNums' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getCPSelectionValidLvlNums';
     OPEN p_out_data FOR     
     SELECT DISTINCT gl.SEQUENCE_NUM AS LEVEL_NUMBER   
               FROM  
               promotion p,  
               promo_challengepoint promoGoal,          
               goalquest_goallevel gl            
               WHERE   
               promoGoal.promotion_id                            = gl.promotion_id   
               AND gl.promotion_id = p.promotion_id 
               AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND p.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))   
               AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))  ;
    p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;        
 END prc_getCPSelectionValidLvlNums;
/* getChallengePointSelectionTotalsChart  */
PROCEDURE prc_getCPSelectionTotalsChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPSelectionTotalsChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getCPSelectionTotalsChart';
     OPEN p_out_data FOR     
          SELECT SUM(NO_GOAL_SELECTED) AS NO_GOAL_SELECTED_CNT, 
          SUM(LEVEL_1_SELECTED)      AS LEVEL_1_SELECTED_CNT, 
          SUM(LEVEL_2_SELECTED)      AS LEVEL_2_SELECTED_CNT, 
          SUM(LEVEL_3_SELECTED)      AS LEVEL_3_SELECTED_CNT, 
          SUM(LEVEL_4_SELECTED)      AS LEVEL_4_SELECTED_CNT, 
          SUM(LEVEL_5_SELECTED)      AS LEVEL_5_SELECTED_CNT,             
          SUM(LEVEL_6_SELECTED)      AS LEVEL_6_SELECTED_CNT  
          FROM 
          ( SELECT total_participant        AS TOTAL_PARTICIPANTS, 
            NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED, 
            NVL(level_1_selected,0)  AS LEVEL_1_SELECTED, 
            NVL(level_2_selected,0)  AS LEVEL_2_SELECTED, 
            NVL(level_3_selected,0)  AS LEVEL_3_SELECTED, 
            NVL(level_4_selected,0)  AS LEVEL_4_SELECTED, 
            NVL(level_5_selected,0)  AS LEVEL_5_SELECTED,              
            NVL(level_6_selected,0)  AS LEVEL_6_SELECTED  
            FROM 
            ( WITH pivot_set AS 
            (SELECT total_participants AS sum_total_pax, 
            NVL(sequence_num,0) sequence_num, 
            rh.node_id AS node_id, 
            rh.node_name node_name 
            FROM (SELECT DISTINCT rh.node_id AS node_id, 
            rh.node_name node_name 
            FROM rpt_hierarchy rh, 
            rpt_cp_selection_summary rpt 
            WHERE detail_node_id      = rh.node_id 
            AND rpt.record_type LIKE '%node%' 
            AND NVL(header_node_id,0)       IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
            ) rh, 
            (SELECT total_participants , 
            gl.sequence_num, 
            rh.node_id AS node_id, 
            rh.node_name node_name 
            FROM 
            rpt_hierarchy rh, 
            promotion p, 
            promo_challengepoint promoGoal, 
            rpt_cp_selection_summary rpt , 
            goalquest_goallevel gl 
            WHERE rpt.level_id   = gl.goallevel_id (+) 
            AND rpt.promotion_id     = promoGoal.promotion_id (+) 
            AND rpt.promotion_id     = p.promotion_id (+) 
            AND detail_node_id         = rh.node_id 
            AND rpt.record_type LIKE '%node%' 
            AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
            AND rpt.pax_status       = NVL(p_in_participantStatus,rpt.pax_status) 
           AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
            AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
            AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
            AND NVL(p.promotion_status,' ')     = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
            ) rpc 
            where rh.node_id    = rpc.node_id (+) 
            ) 
            SELECT t.node_name, 
            t.node_id, 
                      (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)) total_participant, 
            t.no_goal_selected, 
            t.level_1_selected, 
            t.level_2_selected, 
            t.level_3_selected, 
            t.level_4_selected, 
            t.level_5_selected,         
            t.level_6_selected  
            FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS 
            level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected,6 AS level_6_selected) ) t 
            ) 
            where p_in_nodeAndBelow = 1
            UNION ALL  --09/30/2014
          SELECT total_participant AS TOTAL_PARTICIPANTS, 
          NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED, 
          NVL(level_1_selected,0)  AS LEVEL_1_SELECTED, 
          NVL(level_2_selected,0)  AS LEVEL_2_SELECTED, 
          NVL(level_3_selected,0)  AS LEVEL_3_SELECTED, 
          NVL(level_4_selected,0)  AS LEVEL_4_SELECTED, 
          NVL(level_5_selected,0)  AS LEVEL_5_SELECTED,              
          NVL(level_6_selected,0)  AS LEVEL_6_SELECTED  
          FROM 
          ( WITH pivot_set AS 
          (SELECT total_participants AS sum_total_pax, 
          NVL(sequence_num,0) sequence_num, 
          rh.node_id AS node_id, 
          rh.node_name || ' Team' node_name 
          FROM (SELECT DISTINCT rh.node_id AS node_id, 
          rh.node_name node_name 
          FROM rpt_hierarchy rh, 
          rpt_cp_selection_summary rpt 
          WHERE detail_node_id   = rh.node_id 
          AND rpt.record_type LIKE '%team%' 
          AND NVL(detail_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
          ) rh, 
          (SELECT total_participants , 
          gl.sequence_num, 
          rh.node_id AS node_id, 
          rh.node_name node_name 
          FROM 
          rpt_hierarchy rh, 
          promotion p, 
          promo_challengepoint promoGoal, 
          rpt_cp_selection_summary rpt , 
          goalquest_goallevel gl 
          WHERE rpt.level_id    = gl.goallevel_id (+) 
          AND rpt.promotion_id  = promoGoal.promotion_id (+) 
          AND rpt.promotion_id    = p.promotion_id (+) 
          AND detail_node_id   = rh.node_id 
          AND rpt.record_type LIKE '%team%' 
          AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
          AND rpt.pax_status    = NVL(p_in_participantStatus,rpt.pax_status) 
          AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
          AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))  
          AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
          AND NVL(p.promotion_status,' ')    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' ')) 
          ) rpc   
          where rh.node_id   = rpc.node_id (+) 
          ) 
          SELECT t.node_name, 
          t.node_id, 
                    (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)) total_participant, 
          t.no_goal_selected, 
          t.level_1_selected, 
          t.level_2_selected, 
          t.level_3_selected, 
          t.level_4_selected, 
          t.level_5_selected, 
          t.level_6_selected  
          FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS 
          level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected, 6 AS level_6_selected) ) t 
          ) 
          );             
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;       
 END prc_getCPSelectionTotalsChart;
/* getSelectionPercentageChart  */
PROCEDURE prc_getCPSelectionPctChart(
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPSelectionPctChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getCPSelectionPctChart';
     OPEN p_out_data FOR     
         SELECT    CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((NO_GOAL_SELECTED /TOTAL_PARTICIPANTS)*100,2)                 
         ELSE 0 END NO_GOAL_SELECTED_PERCENT,     
         CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((LEVEL_1_SELECTED /TOTAL_PARTICIPANTS)*100,2)                 
         ELSE 0 END LEVEL_1_SELECTED_PERCENT,                 
         CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((LEVEL_2_SELECTED /TOTAL_PARTICIPANTS)*100,2)                 
         ELSE 0 END LEVEL_2_SELECTED_PERCENT,                
         CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((level_3_selected /TOTAL_PARTICIPANTS)*100,2)     
         ELSE 0 END LEVEL_3_SELECTED_PERCENT,                  
         CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((level_4_selected /TOTAL_PARTICIPANTS)*100,2)     
         ELSE 0 END LEVEL_4_SELECTED_PERCENT,                   
         CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((level_5_selected /TOTAL_PARTICIPANTS)*100,2)     
         ELSE 0 END LEVEL_5_SELECTED_PERCENT,                  
         CASE WHEN TOTAL_PARTICIPANTS > 0 THEN     
         ROUND((level_6_selected /TOTAL_PARTICIPANTS)*100,2)     
         ELSE 0 END LEVEL_6_SELECTED_PERCENT               
         FROM                  
         (      
         SELECT NVL(SUM(TOTAL_PARTICIPANTS),0) TOTAL_PARTICIPANTS,      
         NVL(SUM(NO_GOAL_SELECTED),0) NO_GOAL_SELECTED,          
         NVL(SUM(LEVEL_1_SELECTED),0) LEVEL_1_SELECTED,  
         NVL(SUM(LEVEL_2_SELECTED),0) LEVEL_2_SELECTED,  
         NVL(SUM(LEVEL_3_SELECTED),0) LEVEL_3_SELECTED,  
         NVL(SUM(LEVEL_4_SELECTED),0) LEVEL_4_SELECTED,  
         NVL(SUM(LEVEL_5_SELECTED),0) LEVEL_5_SELECTED,  
         NVL(SUM(LEVEL_6_SELECTED),0) LEVEL_6_SELECTED    
         FROM                  
         (SELECT total_participant   AS TOTAL_PARTICIPANTS,      
         NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED,       
         NVL(level_1_selected,0)  AS LEVEL_1_SELECTED,             
         NVL(level_2_selected,0)  AS LEVEL_2_SELECTED,             
         NVL(level_3_selected,0)  AS LEVEL_3_SELECTED,             
         NVL(level_4_selected,0)  AS LEVEL_4_SELECTED,            
         NVL(level_5_selected,0)  AS LEVEL_5_SELECTED,             
         NVL(level_6_selected,0)  AS LEVEL_6_SELECTED              
         FROM                  
         ( WITH pivot_set AS       
         (SELECT total_participants AS sum_total_pax,    
         NVL(sequence_num,0) sequence_num,             
         rh.node_id AS node_id,               
         rh.node_name node_name      
         FROM (SELECT DISTINCT rh.node_id AS node_id,             
         rh.node_name node_name      
         FROM rpt_hierarchy rh,               
         rpt_cp_selection_summary rpt                
         WHERE detail_node_id = rh.node_id     
         AND rpt.record_type LIKE '%node%'      
         AND NVL(header_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))               
         ) rh,         
       (SELECT total_participants ,         
       gl.sequence_num,          
       rh.node_id AS node_id,                
       rh.node_name node_name       
       FROM      
       rpt_hierarchy rh,              
       promotion p,     
       promo_challengepoint promoGoal,        
       rpt_cp_selection_summary rpt ,               
       goalquest_goallevel gl   
       WHERE rpt.level_id   = gl.goallevel_id (+)              
       AND rpt.promotion_id= promoGoal.promotion_id (+)    
       AND rpt.promotion_id= p.promotion_id (+)        
       AND detail_node_id    = rh.node_id        
       AND rpt.record_type LIKE '%node%'       
       AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))               
       AND rpt.pax_status  = NVL(p_in_participantStatus,rpt.pax_status)    
       AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))       
       AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))          
       AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))          
       AND NVL(p.promotion_status,' ')= NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))                
       ) rpc       
       where rh.node_id    = rpc.node_id (+)    
       )              
       SELECT t.node_name,    
       t.node_id,           
       (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant,              
       t.no_goal_selected,       
       t.level_1_selected,         
       t.level_2_selected,         
       t.level_3_selected,         
       t.level_4_selected,         
       t.level_5_selected,         
       t.level_6_selected          
       FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS     
       level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected,6 AS level_6_selected) ) t  where   p_in_nodeAndBelow = 1         
       )              
       UNION ALL  --09/30/2014   
       SELECT total_participant as total_participants,              
       NVL(no_goal_selected,0) as no_goal_selected,           
       NVL(level_1_selected,0) as level_1_selected,             
       NVL(level_2_selected,0) as level_2_selected,             
       NVL(level_3_selected,0) as level_3_selected,             
       NVL(level_4_selected,0) as level_4_selected,             
       NVL(level_5_selected,0) as level_5_selected,             
       NVL(level_6_selected,0) as level_6_selected              
       FROM       
       ( WITH pivot_set AS        
       (SELECT total_participants AS sum_total_pax,    
       NVL(sequence_num,0) sequence_num,              
       rh.node_id AS node_id,                
       rh.node_name || ' Team' node_name                  
       FROM (SELECT DISTINCT rh.node_id AS node_id,              
       rh.node_name node_name       
       FROM rpt_hierarchy rh,                
       rpt_cp_selection_summary rpt                 
       WHERE detail_node_id   = rh.node_id    
       AND rpt.record_type LIKE '%team%'       
       AND NVL(detail_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))    
       ) rh,        
       (SELECT total_participants ,          
       gl.sequence_num,          
       rh.node_id AS node_id,                
       rh.node_name node_name       
       FROM   
       rpt_hierarchy rh,              
       promotion p,     
       promo_challengepoint promoGoal,        
       rpt_cp_selection_summary rpt ,               
       goalquest_goallevel gl  
       WHERE rpt.level_id    = gl.goallevel_id (+)             
       AND rpt.promotion_id  = promoGoal.promotion_id (+)                 
       AND rpt.promotion_id    = p.promotion_id (+)    
       AND detail_node_id   = rh.node_id         
       AND rpt.record_type LIKE '%team%'       
       AND NVL(detail_node_id,0)   IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))  
       AND rpt.pax_status    = NVL(p_in_participantStatus,rpt.pax_status)                  
       AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))       
       AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments)))))          
       AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))         
       AND NVL(p.promotion_status,' ')    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))            
       ) rpc        
       where rh.node_id   = rpc.node_id (+)     
       )              
       SELECT t.node_name,    
       t.node_id,           
         (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant,  
       t.no_goal_selected,       
       t.level_1_selected,         
       t.level_2_selected,         
       t.level_3_selected,         
       t.level_4_selected,         
       t.level_5_selected,         
       t.level_6_selected            
       FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS level_2_selected, 3 AS     
       level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected,6 AS level_6_selected) ) t           
       )              
       )              
       ) rs ; 
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;       
 END prc_getCPSelectionPctChart;
/* getChallengePointSelectionByOrgChart  */
  PROCEDURE prc_getCPSelectionByOrgChart(
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPSelectionByOrgChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getCPSelectionByOrgChart';
     OPEN p_out_data FOR     
    SELECT NODE_NAME,
     NODE_ID,
     total_participants        AS TOTAL_PARTICIPANTS, 
     NVL(NO_GOAL_SELECTED,0) NO_GOAL_SELECTED, 
     NVL(LEVEL_1_SELECTED,0) LEVEL_1_SELECTED, 
     NVL(LEVEL_2_SELECTED,0) LEVEL_2_SELECTED, 
     NVL(LEVEL_3_SELECTED,0) LEVEL_3_SELECTED, 
     NVL(LEVEL_4_SELECTED,0) LEVEL_4_SELECTED, 
     NVL(LEVEL_5_SELECTED,0) LEVEL_5_SELECTED,
     NVL(LEVEL_6_SELECTED,0) LEVEL_6_SELECTED
     FROM 
     (SELECT node_name          AS NODE_NAME, 
       node_id       AS NODE_ID, 
       total_participant        AS TOTAL_PARTICIPANTS, 
       NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED, 
       NVL(level_1_selected,0)  AS LEVEL_1_SELECTED, 
       NVL(level_2_selected,0)  AS LEVEL_2_SELECTED,  
       NVL(level_3_selected,0)  AS LEVEL_3_SELECTED, 
       NVL(level_4_selected,0)  AS LEVEL_4_SELECTED,  
       NVL(level_5_selected,0)  AS LEVEL_5_SELECTED,
       NVL(level_6_selected,0)  AS LEVEL_6_SELECTED,
       0  AS IS_LEAF 
       FROM 
       ( WITH pivot_set AS 
       (SELECT total_participants AS sum_total_pax, 
       NVL(sequence_num,0) sequence_num, 
       rh.node_id AS node_id, 
       rh.node_name node_name 
       FROM (SELECT DISTINCT rh.node_id AS node_id, 
       rh.node_name node_name 
       FROM rpt_hierarchy rh, 
       rpt_cp_selection_summary rpt 
       WHERE detail_node_id      = rh.node_id 
       AND rpt.record_type LIKE '%node%' 
       AND NVL(header_node_id,0)     IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
       ) rh,
       (SELECT total_participants ,
       gl.sequence_num, 
       rh.node_id AS node_id, 
       rh.node_name node_name 
       FROM 
       rpt_hierarchy rh, 
       promotion p, 
       promo_challengepoint promoGoal, 
       rpt_cp_selection_summary rpt ,
       goalquest_goallevel gl 
       WHERE rpt.level_id         = gl.goallevel_id (+) 
       AND rpt.promotion_id      = promoGoal.promotion_id (+)
       AND rpt.promotion_id        = p.promotion_id (+)
       AND detail_node_id      = rh.node_id 
       AND rpt.record_type LIKE '%node%' 
       AND NVL(header_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
       AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) 
      AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
       AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
       AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
       AND NVL(p.promotion_status,' ')         = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))  
       ) rpc
       where rh.node_id      = rpc.node_id (+)
       ) 
       SELECT t.node_name, 
       t.node_id, 
       (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant, 
       t.no_goal_selected, 
       t.level_1_selected, 
       t.level_2_selected, 
       t.level_3_selected, 
       t.level_4_selected, 
       t.level_5_selected,
       t.level_6_selected
       FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS 
       level_2_selected, 3 AS level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected, 6 AS level_6_selected) ) t  where   p_in_nodeAndBelow = 1       
       ) 
       UNION ALL --09/30/2014
     SELECT node_name          AS NODE_NAME, 
     node_id       AS NODE_ID, 
     total_participant        AS TOTAL_PARTICIPANTS, 
     NVL(no_goal_selected,0)  AS NO_GOAL_SELECTED, 
     NVL(level_1_selected,0)  AS LEVEL_1_SELECTED, 
     NVL(level_2_selected,0)  AS LEVEL_2_SELECTED,  
     NVL(level_3_selected,0)  AS LEVEL_3_SELECTED, 
     NVL(level_4_selected,0)  AS LEVEL_4_SELECTED,  
     NVL(level_5_selected,0)  AS LEVEL_5_SELECTED,
     NVL(level_6_selected,0)  AS LEVEL_6_SELECTED,
     1  AS IS_LEAF 
     FROM 
     ( WITH pivot_set AS 
     (SELECT total_participants AS sum_total_pax, 
     NVL(sequence_num,0) sequence_num, 
     rh.node_id AS node_id, 
     rh.node_name || ' Team' node_name 
     FROM (SELECT DISTINCT rh.node_id AS node_id, 
     rh.node_name node_name 
     FROM rpt_hierarchy rh, 
     rpt_cp_selection_summary rpt 
     WHERE detail_node_id      = rh.node_id 
     AND rpt.record_type LIKE '%team%' 
     AND NVL(detail_node_id,0)     IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     ) rh,
     (SELECT total_participants ,
     gl.sequence_num, 
     rh.node_id AS node_id, 
     rh.node_name node_name 
     FROM 
     rpt_hierarchy rh, 
     promotion p, 
     promo_challengepoint promoGoal, 
     rpt_cp_selection_summary rpt ,
     goalquest_goallevel gl 
     WHERE rpt.level_id         = gl.goallevel_id (+) 
     AND rpt.promotion_id      = promoGoal.promotion_id (+)
     AND rpt.promotion_id        = p.promotion_id (+)
     AND detail_node_id      = rh.node_id 
     AND rpt.record_type LIKE '%team%' 
     AND NVL(detail_node_id,0)    IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) )) 
     AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) 
     AND  NVL(rpt.job_position,'JOB')                     = NVL(p_in_jobPosition,NVL(rpt.job_position,'JOB'))
     AND ((p_in_departments IS NULL) OR (p_in_departments is NOT NULL AND rpt.department IN (SELECT * FROM TABLE(get_array_varchar(p_in_departments))))) 
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND rpt.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId))))) 
     AND NVL(p.promotion_status,' ')         = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))  
     ) rpc
     where rh.node_id      = rpc.node_id (+)
     ) 
     SELECT t.node_name, 
     t.node_id, 
     (NVL(t.no_goal_selected,0)+NVL(t.level_1_selected,0)+NVL(t.level_2_selected,0)+NVL(t.level_3_selected,0)+NVL(t.level_4_selected,0)+NVL(t.level_5_selected,0)+NVL(t.level_6_selected,0)) total_participant, 
     t.no_goal_selected, 
     t.level_1_selected, 
     t.level_2_selected, 
     t.level_3_selected, 
     t.level_4_selected, 
     t.level_5_selected,
     t.level_6_selected
     FROM pivot_set PIVOT (SUM(sum_total_pax) FOR sequence_num IN (0 AS no_goal_selected, 1 AS level_1_selected, 2 AS 
     level_2_selected, 3 AS level_3_selected, 4 AS level_4_selected, 5 AS level_5_selected, 6 AS level_6_selected) ) t 
      ) 
    ORDER BY NODE_NAME ASC 
      ) ;      
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;       
 END prc_getCPSelectionByOrgChart;

/*-----------------------------------------------------------------------------
  NAME:       prc_getCPManagerDetailTabRes
  
  Date             Author       Description
  ----------      -----------   -----------------------------------------------
                                Initial..
  Arun S          10/01/2014    Bug Fix 57053  -  Reports - Challengepoint Manager Achievement 
                                - Displayed error when we open this Report
------------------------------------------------------------------------------*/   
/* getChallengePointManagerDetailTabularResults  */
 PROCEDURE prc_getCPManagerDetailTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPManagerDetailTabRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPManagerDetailTabRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'SELECT ROWNUM RN,
         gs.manager_name manager_name,
         NVL (gs.total_nbr_pax, 0) TOTAL_PAX,
         NVL (gs.pax_sel_goal, 0) TOTAL_PAX_GOAL_SELECTED,
         NVL (gs.nbr_pax_achieving, 0) TOTAL_PAX_ACHIEVED,
         NVL ( ROUND ( DECODE (gs.pax_sel_goal, 0, 0, (gs.nbr_pax_achieving  / gs.pax_sel_goal) * 100), 2), 0) PERCENT_SEL_PAX_ACHIEVING,
         NVL ( ROUND ( DECODE (gs.total_nbr_pax, 0, 0, (gs.nbr_pax_achieving / gs.total_nbr_pax) * 100), 2), 0) PERCENT_TOT_PAX_ACHIEVING,
         NVL ( ROUND ( DECODE (gs.team_points, 0, 0, (gs.manager_points      / gs.team_points) * 100), 2), 0) MANAGE_OVERRIDE_PERCENTAGE,
         NVL (gs.team_points, 0) TOTAL_POINTS_BY_TEAM,
         NVL (gs.manager_points, 0) TOTAL_MGR_POINTS,
         NVL ( ROUND ( DECODE (gs.nbr_pax_achieving, 0, 0, gs.manager_points / gs.nbr_pax_achieving), 2), 0) manager_payout_per_achiever,
         gs.promotion_name
       FROM
         (SELECT override.manager_name,
           rpt.promotion_id,
           SUM (rpt.total_participants) total_nbr_pax,
           SUM (DECODE (rpt.level_id, NULL, 0, rpt.total_participants)) pax_sel_goal,
           SUM (rpt.nbr_challengepoint_achieved) nbr_pax_achieving,
           NVL (SUM (rpt.calculated_payout), 0) team_points,
           NVL (override.manager_points, 0) manager_points,
           promo.promotion_name promotion_name
         FROM promo_challengepoint promoGoal,
           promotion promo,
           user_node un,
           (SELECT mgr_user_id,
             rpt.mgr_last_name
             || '',''
             || rpt.mgr_first_name AS manager_name,
             rpt.promotion_id,
             SUM (override_amount) manager_points
           FROM rpt_cp_manager_override rpt,
             promotion promo
           WHERE rpt.promotion_id = promo.promotion_id
           AND rpt.mgr_user_id    = '''||p_in_managerUserId||'''
           AND ( ( '''||p_in_promotionId||''' IS NULL)
           OR ( '''||p_in_promotionId||'''    IS NOT NULL
           AND rpt.promotion_id  IN
             (SELECT * FROM TABLE ( get_array ( '''||p_in_promotionId||'''))
             )))
           AND NVL(promo.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(promo.promotion_status,'' '')) 
           GROUP BY rpt.mgr_last_name,
             rpt.mgr_first_name,
             mgr_user_id,
             rpt.promotion_id
           ) override,
           rpt_cp_selection_summary rpt
         LEFT OUTER JOIN goalquest_goallevel gl
         ON rpt.level_id            = gl.goallevel_id
         WHERE rpt.promotion_id     = promoGoal.promotion_id
         AND promogoal.promotion_id = promo.promotion_id
         AND rpt.detail_node_id    IN
           (SELECT child_node_id
           FROM rpt_hierarchy_rollup
           --WHERE node_id = '''||p_in_parentNodeId||'''
          WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''')))   --10/01/2014 Bug Fix 57053
           )
         AND rpt.record_type LIKE ''%node%'' --team --09/12/2014 nagarajs Bug #56464
         AND ( rpt.promotion_id IN
           (SELECT               *
           FROM TABLE ( CAST ( get_array_varchar ( '''||p_in_promotionId||''') AS ARRAY_VARCHAR))
           )
         OR '''||p_in_promotionId||'''          IS NULL)
         AND NVL(promo.promotion_status,'' '')                    = NVL('''||p_in_promotionStatus||''',NVL(promo.promotion_status,'' ''))
         AND rpt.detail_node_id     = un.node_id
         AND un.user_id             = override.mgr_user_id
         AND promo.promotion_id     = override.promotion_id
         AND un.role                = ''own''
         GROUP BY override.manager_name,
           override.manager_points,
           rpt.promotion_id,
           promo.promotion_name
         ) gs
         ORDER BY '|| v_sortCol ||'
   ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;
      
      OPEN p_out_data FOR l_query;
   
/* getChallengePointManagerDetailTabularSize  */
     SELECT count(1) into p_out_size_data
     FROM
       (SELECT override.manager_name,
         rpt.promotion_id,
         SUM (rpt.total_participants) total_nbr_pax,
         SUM (DECODE (rpt.level_id, NULL, 0, rpt.total_participants)) pax_sel_goal,
         SUM (rpt.nbr_challengepoint_achieved) nbr_pax_achieving,
         NVL (SUM (rpt.calculated_payout), 0) team_points,
         NVL (override.manager_points, 0) manager_points
       FROM rpt_hierarchy rh,
         promo_challengepoint promoGoal,
         promotion promo,
         user_node un,
         (SELECT mgr_user_id,
           rpt.mgr_last_name
           || ','
           || rpt.mgr_first_name AS manager_name,
           rpt.promotion_id,
           SUM (override_amount) manager_points
         FROM rpt_cp_manager_override rpt,
           promotion promo
         WHERE rpt.promotion_id = promo.promotion_id
         AND rpt.mgr_user_id    = p_in_managerUserId
         AND ( ( p_in_promotionId IS NULL)
         OR ( p_in_promotionId    IS NOT NULL
         AND rpt.promotion_id  IN
           (SELECT * FROM TABLE ( get_array ( p_in_promotionId))
           )))
         AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
         AND NVL(promo.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' ')) 
         GROUP BY rpt.mgr_last_name,
           rpt.mgr_first_name,
           mgr_user_id,
           rpt.promotion_id
         ) override,
         rpt_cp_selection_summary rpt
       LEFT OUTER JOIN goalquest_goallevel gl
       ON rpt.level_id            = gl.goallevel_id
       WHERE rpt.detail_node_id   = rh.node_id
       AND rpt.promotion_id       = promoGoal.promotion_id
       AND promogoal.promotion_id = promo.promotion_id
       AND rpt.detail_node_id    IN
         (SELECT child_node_id
         FROM rpt_hierarchy_rollup
         WHERE node_id --= p_in_parentNodeId
         IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --10/01/2014 Bug Fix 57053
         )
       AND rpt.record_type LIKE '%node%' --team --09/12/2014 nagarajs Bug #56464
       AND ( rpt.promotion_id IN
         (SELECT               *
         FROM TABLE ( CAST ( get_array_varchar ( p_in_promotionId) AS ARRAY_VARCHAR))
         )
       OR p_in_promotionId          IS NULL)       
       AND NVL(promo.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' ')) 
       AND rh.node_id             = un.node_id
       AND un.user_id             = override.mgr_user_id
       AND promo.promotion_id     = override.promotion_id
       AND un.role                = 'own'
       GROUP BY override.manager_name,
         override.manager_points,
         rpt.promotion_id
       ) gs;
/* getChallengePointManagerDetailTabularTotals  */
     OPEN p_out_totals_data FOR
     SELECT NVL (SUM(gs.total_nbr_pax), 0) TOTAL_PAX,
       NVL (SUM(gs.pax_sel_goal), 0) TOTAL_PAX_GOAL_SELECTED,
       NVL (SUM(gs.nbr_pax_achieving), 0) TOTAL_PAX_ACHIEVED,
       NVL ( ROUND ( DECODE (SUM(gs.pax_sel_goal), 0, 0, (SUM(gs.nbr_pax_achieving)  / SUM(gs.pax_sel_goal)) * 100), 2), 0) PERCENT_SEL_PAX_ACHIEVING,
       NVL ( ROUND ( DECODE (SUM(gs.total_nbr_pax), 0, 0, (SUM(gs.nbr_pax_achieving) / SUM(gs.total_nbr_pax)) * 100), 2), 0) PERCENT_TOT_PAX_ACHIEVING,
       NVL ( ROUND ( DECODE (SUM(gs.team_points), 0, 0, (SUM(gs.manager_points)      / SUM(gs.team_points)) * 100), 2), 0) MANAGE_OVERRIDE_PERCENTAGE,
       NVL (SUM(gs.team_points), 0) TOTAL_POINTS_BY_TEAM,
       NVL (SUM(gs.manager_points), 0) TOTAL_MGR_POINTS,
       NVL ( ROUND( DECODE (SUM(gs.nbr_pax_achieving), 0, 0, SUM(gs.manager_points) / SUM(gs.nbr_pax_achieving)), 2), 0) manager_payout_per_achiever
     FROM
       (SELECT override.manager_name,
         rpt.promotion_id,
         SUM (rpt.total_participants) total_nbr_pax,
         SUM (DECODE (rpt.level_id, NULL, 0, rpt.total_participants)) pax_sel_goal,
         SUM (rpt.nbr_challengepoint_achieved) nbr_pax_achieving,
         NVL (SUM (rpt.calculated_payout), 0) team_points,
         NVL (override.manager_points, 0) manager_points
       FROM rpt_hierarchy rh,
         promo_challengepoint promoGoal,
         promotion promo,
         user_node un,
         (SELECT mgr_user_id,
           rpt.mgr_last_name
           || ','
           || rpt.mgr_first_name AS manager_name,
           rpt.promotion_id,
           SUM (override_amount) manager_points
         FROM rpt_cp_manager_override rpt,
           promotion promo
         WHERE rpt.promotion_id = promo.promotion_id
         AND rpt.mgr_user_id    = p_in_managerUserId
         AND ( ( p_in_promotionId IS NULL)
         OR ( p_in_promotionId    IS NOT NULL
         AND rpt.promotion_id  IN
           (SELECT * FROM TABLE ( get_array ( p_in_promotionId))
           )))
         AND NVL(promo.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(promo.promotion_status,' ')) 
         GROUP BY rpt.mgr_last_name,
           rpt.mgr_first_name,
           mgr_user_id,
           rpt.promotion_id
         ) override,
         rpt_cp_selection_summary rpt
       LEFT OUTER JOIN goalquest_goallevel gl
       ON rpt.level_id            = gl.goallevel_id
       WHERE rpt.detail_node_id   = rh.node_id
       AND rpt.promotion_id       = promoGoal.promotion_id
       AND promogoal.promotion_id = promo.promotion_id
       AND rpt.detail_node_id    IN
         (SELECT child_node_id
         FROM rpt_hierarchy_rollup
         WHERE node_id --= p_in_parentNodeId
         IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --10/01/2014 Bug Fix 57053
         )
       AND rpt.record_type LIKE '%node%' --team --09/12/2014 nagarajs Bug #56464
       AND ( rpt.promotion_id IN
         (SELECT               *
         FROM TABLE ( CAST ( get_array_varchar ( p_in_promotionId) AS ARRAY_VARCHAR))
         )
       OR p_in_promotionId          IS NULL)
       AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
       AND rh.node_id             = un.node_id
       AND un.user_id             = override.mgr_user_id
       AND promo.promotion_id     = override.promotion_id
       AND un.role                = 'own'
       GROUP BY override.manager_name,
         override.manager_points,
         rpt.promotion_id
       ) gs;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);
         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data :=NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPManagerDetailTabRes; 

/******************************************************************************
  NAME:       prc_getCPManagerTabRes
  Date               Author           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J          09/19/2014        Bug Fix 56754  - Calculation Issue in Summary, Size and Total queries
  Suresh J         09/30/2014        Bug Fix 57053  -- Syntax error.. Updated to support more than one Parent Node ID
  ******************************************************************************/   
/* getChallengePointManagerTabularResults  */
PROCEDURE prc_getCPManagerTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPManagerTabRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol        VARCHAR2(200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPManagerTabRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
      || '  '
      || 'SELECT ROWNUM RN,s.*  --09/19/2014
      FROM    --09/19/2014
        (SELECT gs.mgr_user_id manager_id,   --09/19/2014
         gs.manager_name manager_name,
         SUM(NVL (gs.total_nbr_pax, 0)) TOTAL_PAX,  --09/19/2014
         SUM(NVL (gs.pax_sel_goal, 0)) TOTAL_PAX_GOAL_SELECTED,    --09/19/2014
         SUM(NVL (gs.nbr_pax_achieving, 0)) TOTAL_PAX_ACHIEVED,    --09/19/2014
         NVL ( ROUND ( DECODE (SUM(NVL (gs.pax_sel_goal, 0)), 0, 0, (SUM(NVL (gs.nbr_pax_achieving, 0))  / SUM(NVL (gs.pax_sel_goal, 0))) * 100), 2), 0) PERCENT_SEL_PAX_ACHIEVING,    --09/19/2014
         NVL ( ROUND ( DECODE (SUM(NVL (gs.total_nbr_pax, 0)), 0, 0, (SUM(NVL (gs.nbr_pax_achieving, 0)) / SUM(NVL (gs.total_nbr_pax, 0))) * 100), 2), 0) PERCENT_TOT_PAX_ACHIEVING,   --09/19/2014 
         NVL ( ROUND ( DECODE (SUM(NVL (gs.team_points, 0)), 0, 0, (SUM(NVL (gs.manager_points, 0))      / SUM(NVL (gs.team_points, 0))) * 100), 2), 0) MANAGE_OVERRIDE_PERCENTAGE,    --09/19/2014
         SUM(NVL (gs.team_points, 0)) TOTAL_POINTS_BY_TEAM,   --09/19/2014
         SUM(NVL (gs.manager_points, 0)) TOTAL_MGR_POINTS,    --09/19/2014
         NVL ( ROUND(DECODE (SUM(NVL (gs.nbr_pax_achieving, 0)), 0, 0, SUM(NVL (gs.manager_points, 0)) / SUM(NVL (gs.nbr_pax_achieving, 0))),2), 0) manager_payout_per_achiever   --09/19/2014
       FROM
         (SELECT override.manager_name,
           mgr_user_id,
           SUM (rpt.total_participants) total_nbr_pax,
           SUM (DECODE (rpt.level_id, NULL, 0, rpt.total_participants)) pax_sel_goal,
           SUM (rpt.nbr_challengepoint_achieved) nbr_pax_achieving,
           NVL (SUM (rpt.calculated_payout), 0) team_points,
           NVL (override.manager_points, 0) manager_points
           ,override.promotion_id  --09/19/2014
         FROM rpt_hierarchy rh,
           promo_challengepoint promoGoal,
           promotion promo,
           user_node un,
           (SELECT mgr_user_id,
             rpt.mgr_last_name
             || '',''
             || rpt.mgr_first_name AS manager_name,
             rpt.promotion_id, --09/12/2014 nagarajs Bug #56464
             SUM (override_amount) manager_points
           FROM rpt_cp_manager_override rpt,
             promotion promo
           WHERE rpt.promotion_id = promo.promotion_id
           AND rpt.node_id       IN
             (SELECT child_node_id
             FROM rpt_hierarchy_rollup
--             WHERE node_id = '''||p_in_parentNodeId||'''    --09/30/2014
             WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''')))   --09/30/2014
             )
           AND ( ( '''||p_in_promotionId||''' IS NULL)
           OR ( '''||p_in_promotionId||'''    IS NOT NULL
           AND rpt.promotion_id  IN
             (SELECT * FROM TABLE ( get_array ( '''||p_in_promotionId||'''))
             )))
           AND promo.promotion_status = NVL ( '''||p_in_promotionStatus||''', promo.promotion_status)
           GROUP BY rpt.mgr_last_name,
             rpt.mgr_first_name,
             mgr_user_id,
             rpt.promotion_id --09/12/2014 nagarajs Bug #56464
           ) override,
           rpt_cp_selection_summary rpt
         LEFT OUTER JOIN goalquest_goallevel gl
         ON rpt.level_id            = gl.goallevel_id
         WHERE rpt.detail_node_id   = rh.node_id
         AND rpt.promotion_id       = promoGoal.promotion_id
         AND promogoal.promotion_id = promo.promotion_id
         AND rpt.detail_node_id    IN
           (SELECT child_node_id
           FROM rpt_hierarchy_rollup
--           WHERE node_id = '''||p_in_parentNodeId||'''   09/30/2014
             WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( '''||p_in_parentNodeId||''')))   --09/30/2014
           )
         AND rpt.record_type LIKE ''%node%'' --team --09/12/2014 nagarajs Bug #56464
         AND ( rpt.promotion_id IN
           (SELECT               *
           FROM TABLE ( CAST ( get_array_varchar ( '''||p_in_promotionId||''') AS ARRAY_VARCHAR))
           )
         OR '''||p_in_promotionId||'''          IS NULL)
         AND promo.promotion_status = NVL ( '''||p_in_promotionStatus||''', promo.promotion_status)
         AND rh.node_id             = un.node_id
         AND un.user_id             = override.mgr_user_id
         AND promo.promotion_id     = override.promotion_id --09/12/2014 nagarajs Bug #56464
         AND un.role                = ''own''
         GROUP BY override.manager_name,
           override.manager_points,
           override.promotion_id,  --09/19/2014
           override.mgr_user_id
         ) gs
       group by gs.mgr_user_id,gs.manager_name   --09/19/2014    
      ORDER BY '|| v_sortCol ||'    --09/19/2014
   ) s ) WHERE RN > ' || p_in_rowNumStart || ' AND RN < ' || p_in_rowNumEnd;   --09/19/2014

     OPEN p_out_data FOR l_query;

/* getChallengePointManagerResultsSize  */
    SELECT COUNT(1) into p_out_size_data    --09/19/2014  
    FROM (   --09/19/2014
    SELECT COUNT(1)   --09/19/2014 
    FROM  --09/19/2014
       (SELECT override.promotion_id,mgr_user_id,override.manager_name,   --09/19/2014
         SUM (rpt.total_participants) total_nbr_pax,
         SUM (DECODE (rpt.level_id, NULL, 0, rpt.total_participants)) pax_sel_goal,
         SUM (rpt.nbr_challengepoint_achieved) nbr_pax_achieving,
         NVL (SUM (rpt.calculated_payout), 0) team_points,
         NVL (override.manager_points, 0) manager_points
       FROM rpt_hierarchy rh,
         promo_challengepoint promoGoal,
         promotion promo,
         user_node un,
         (SELECT mgr_user_id,
           rpt.mgr_last_name
           || ','
           || rpt.mgr_first_name AS manager_name,
           rpt.promotion_id, --09/12/2014 nagarajs Bug #56464
           SUM (override_amount) manager_points
         FROM rpt_cp_manager_override rpt,
           promotion promo
         WHERE rpt.promotion_id = promo.promotion_id
         AND rpt.node_id       IN
           (SELECT child_node_id
           FROM rpt_hierarchy_rollup
--           WHERE node_id = p_in_parentNodeId   --09/30/2014
             WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --09/30/2014
           )
         AND ( ( p_in_promotionId IS NULL)
         OR ( p_in_promotionId    IS NOT NULL
         AND rpt.promotion_id  IN
           (SELECT * FROM TABLE ( get_array ( p_in_promotionId))
           )))
         AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
         GROUP BY rpt.mgr_last_name,
           rpt.mgr_first_name,
           mgr_user_id,
           rpt.promotion_id --09/12/2014 nagarajs Bug #56464
         ) override,
         rpt_cp_selection_summary rpt
       LEFT OUTER JOIN goalquest_goallevel gl
       ON rpt.level_id            = gl.goallevel_id
       WHERE rpt.detail_node_id   = rh.node_id
       AND rpt.promotion_id       = promoGoal.promotion_id
       AND promogoal.promotion_id = promo.promotion_id
       AND rpt.detail_node_id    IN
         (SELECT child_node_id
         FROM rpt_hierarchy_rollup
--         WHERE node_id = p_in_parentNodeId   --09/30/2014
           WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --09/30/2014
         )
       AND rpt.record_type LIKE '%node%' --team --09/12/2014 nagarajs Bug #56464
       AND ( rpt.promotion_id IN
         (SELECT               *
         FROM TABLE ( CAST ( get_array_varchar ( p_in_promotionId) AS ARRAY_VARCHAR))
         )
       OR p_in_promotionId          IS NULL)
       AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
       AND rh.node_id             = un.node_id
       AND un.user_id             = override.mgr_user_id
       AND promo.promotion_id     = override.promotion_id --09/12/2014 nagarajs Bug #56464
       AND un.role                = 'own'
       GROUP BY override.manager_name,
         override.manager_points,
         override.promotion_id,  --09/19/2014
         override.mgr_user_id
       ) gs group by gs.mgr_user_id,gs.manager_name );

/* getChallengePointManagerTabularResultsTotals  */
     OPEN p_out_totals_data FOR
     SELECT NVL (SUM(gs.total_nbr_pax), 0) TOTAL_PAX,    --09/19/2014
       NVL (SUM(gs.pax_sel_goal), 0) TOTAL_PAX_GOAL_SELECTED,   --09/19/2014
       NVL (SUM(gs.nbr_pax_achieving), 0) TOTAL_PAX_ACHIEVED,   --09/19/2014
       NVL ( ROUND ( DECODE (SUM(gs.pax_sel_goal), 0, 0, (SUM(gs.nbr_pax_achieving)  / SUM(gs.pax_sel_goal)) * 100), 2), 0) PERCENT_SEL_PAX_ACHIEVING,  --09/19/2014
       NVL ( ROUND ( DECODE (SUM(gs.total_nbr_pax), 0, 0, (SUM(gs.nbr_pax_achieving) / SUM(gs.total_nbr_pax)) * 100), 2), 0) PERCENT_TOT_PAX_ACHIEVING,   --09/19/2014
       NVL ( ROUND ( DECODE (SUM(gs.team_points), 0, 0, (SUM(gs.manager_points)      / SUM(gs.team_points)) * 100), 2), 0) MANAGE_OVERRIDE_PERCENTAGE,     --09/19/2014
       NVL (SUM(gs.team_points), 0) TOTAL_POINTS_BY_TEAM,  --09/19/2014
       NVL (SUM(gs.manager_points), 0) TOTAL_MGR_POINTS,   --09/19/2014
       NVL ( ROUND( DECODE (SUM(gs.nbr_pax_achieving), 0, 0, SUM(gs.manager_points) / SUM(gs.nbr_pax_achieving)), 2), 0) manager_payout_per_achiever   --09/19/2014
     FROM
       (SELECT override.promotion_id,override.manager_name,   --09/19/2014
         SUM (rpt.total_participants) total_nbr_pax,
         SUM (DECODE (rpt.level_id, NULL, 0, rpt.total_participants)) pax_sel_goal,
         SUM (rpt.nbr_challengepoint_achieved) nbr_pax_achieving,
         NVL (SUM (rpt.calculated_payout), 0) team_points,
         NVL (override.manager_points, 0) manager_points
       FROM rpt_hierarchy rh,
         promo_challengepoint promoGoal,
         promotion promo,
         user_node un,
         (SELECT mgr_user_id,
           rpt.mgr_last_name
           || ','
           || rpt.mgr_first_name AS manager_name,
           rpt.promotion_id, --09/12/2014 nagarajs Bug #56464
           SUM (override_amount) manager_points
         FROM rpt_cp_manager_override rpt,
           promotion promo
         WHERE rpt.promotion_id = promo.promotion_id
         AND rpt.node_id       IN
           (SELECT child_node_id
           FROM rpt_hierarchy_rollup
--           WHERE node_id = p_in_parentNodeId   --09/30/2014
             WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --09/30/2014
           )
         AND ( ( p_in_promotionId IS NULL)
         OR ( p_in_promotionId    IS NOT NULL
         AND rpt.promotion_id  IN
           (SELECT * FROM TABLE ( get_array ( p_in_promotionId))
           )))
         AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
         GROUP BY rpt.mgr_last_name,
           rpt.mgr_first_name,
           mgr_user_id,
           rpt.promotion_id --09/12/2014 nagarajs Bug #56464
         ) override,
         rpt_cp_selection_summary rpt
       LEFT OUTER JOIN goalquest_goallevel gl
       ON rpt.level_id            = gl.goallevel_id
       WHERE rpt.detail_node_id   = rh.node_id
       AND rpt.promotion_id       = promoGoal.promotion_id
       AND promogoal.promotion_id = promo.promotion_id
       AND rpt.detail_node_id    IN
         (SELECT child_node_id
         FROM rpt_hierarchy_rollup
--         WHERE node_id = p_in_parentNodeId      --09/30/2014
           WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --09/30/2014
         )
       AND rpt.record_type LIKE '%node%' --team --09/12/2014 nagarajs Bug #56464
       AND ( rpt.promotion_id IN
         (SELECT               *
         FROM TABLE ( CAST ( get_array_varchar ( p_in_promotionId) AS ARRAY_VARCHAR))
         )
       OR p_in_promotionId          IS NULL)
       AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
       AND rh.node_id             = un.node_id
       AND un.user_id             = override.mgr_user_id
       AND promo.promotion_id     = override.promotion_id --09/12/2014 nagarajs Bug #56464
       AND un.role                = 'own'
       GROUP BY override.manager_name,
         override.promotion_id,  --09/19/2014
         override.manager_points,
         override.mgr_user_id   --09/19/2014
       ) gs;
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data := NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;         
 END prc_getCPManagerTabRes; 
/* getManagerTotalPointsEarnedChart  */
PROCEDURE prc_getMgrTotalPtsEarnedChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getMgrTotalPtsEarnedChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getMgrTotalPtsEarnedChart';
     OPEN p_out_data FOR     
     SELECT *
     FROM
       (SELECT mgr_user_id,
         rpt.mgr_last_name
         || ','
         || rpt.mgr_first_name AS manager_name,
         SUM (override_amount) manager_points
       FROM rpt_cp_manager_override rpt,
         promotion promo
       WHERE rpt.promotion_id = promo.promotion_id
       AND rpt.node_id       IN
         (SELECT child_node_id
         FROM rpt_hierarchy_rollup
--         WHERE node_id = p_in_parentNodeId   --09/30/2014
           WHERE node_id IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId )))   --09/30/2014         
         )
       AND ( ( p_in_promotionId IS NULL)
       OR ( p_in_promotionId    IS NOT NULL
       AND rpt.promotion_id  IN
         (SELECT * FROM TABLE ( get_array ( p_in_promotionId))
         )))
       AND promo.promotion_status = NVL ( p_in_promotionStatus, promo.promotion_status)
       GROUP BY rpt.mgr_last_name,
         rpt.mgr_first_name,
         mgr_user_id
       ORDER BY MANAGER_POINTS DESC
       )
     WHERE ROWNUM <=20;     
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getMgrTotalPtsEarnedChart; 

/* getChallengePointProgramSummaryTabularResults  */
PROCEDURE prc_getCPProgramSummaryTabRes (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR, 
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId         IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR,
      p_out_size_data          OUT    NUMBER,
      p_out_totals_data        OUT    SYS_REFCURSOR)
   IS
/******************************************************************************
  NAME:       prc_getCPProgramSummaryTabRes
  Date               Author           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J          09/17/2014        Bug Fix 56452  - Calculation Issue in Summary and Total queries
  Swati             09/10/2014         Bug 56431 - Reports - Challenge point Program Summary - "Participant Status " filter is not working
                    09/19/2014        Bug 56431 - No Data found alert message is not displayed when there is no data for Participant Status  = Inactive
  Suresh J          09/24/2014      Bug 56452 -- Formula update % increase columns
  Suresh J          09/30/2014     Bug Fix 57054 -- Syntax Error.. Updated to support multiple ParentNode ID  
  ******************************************************************************/   
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPProgramSummaryTabRes' ;
      v_stage          VARCHAR2 (500);
      v_sortCol                 VARCHAR2 (200);
      l_query        VARCHAR2 (32767);
   BEGIN
      v_stage   := 'getCPProgramSummaryTabRes';
      
       l_query := 'SELECT * FROM 
  ( ';
      v_sortCol := ' ' || p_in_sortColName || ' ' || p_in_sortedBy;  
      
      l_query := l_query
         || '  '
         || 'WITH pivot as
      (  SELECT achieved,
               goal_achieved,
               SUM (goal_achieved) OVER() total_pax,
               base,
               actual_result,
                goal_value
           FROM
             (SELECT rh.node_id AS node_id,
                rh.node_name node_name,
                NVL(achieved,1) achieved,
                goal_achieved,
                 base ,
                 actual_result,
                 goal_value
               FROM (SELECT DISTINCT rh.node_id AS node_id,
                          rh.node_name node_name
                    FROM rpt_hierarchy rh,
                         rpt_cp_selection_summary rpt
                    WHERE detail_node_id                            = rh.node_id
                      AND rpt.record_type LIKE ''%node%''
--                      AND NVL(header_node_id,0)                           = NVL('''||p_in_parentNodeId||''',0)     --09/30/2014
                      AND NVL(header_node_id,0)  IN (SELECT * FROM TABLE(get_array_varchar(NVL('''||p_in_parentNodeId||''',0))))   --09/30/2014
                      AND rpt.pax_status = NVL('''||p_in_participantStatus||''',rpt.pax_status) -- 09/10/2014 Bug 56431
                   ) rh,
                (SELECT r.node_id, COUNT ( CASE WHEN pax_goal_id is not null THEN NVL(achieved,0) ELSE NULL END) goal_achieved,
                    NVL(sum (current_value),0) actual_result,
                    NVL(sum (amount_to_achieve),0) goal_value,
                    NVL(sum (base_quantity),0) base,
                    achieved
                FROM rpt_cp_selection_detail rpt,
                    rpt_hierarchy_rollup r,
                    promotion p,
                    promo_challengepoint promoGoal
                where rpt.node_id = r.child_node_id
                    AND rpt.promotion_id                            = promoGoal.promotion_id (+)
                    AND rpt.promotion_id                            = p.promotion_id (+)
                    AND (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array('''||p_in_promotionId||''')))))
                    AND NVL(p.promotion_status,'' '')  = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))
                    AND rpt.user_status   = NVL('''||p_in_participantStatus||''',rpt.user_status) -- 09/10/2014 Bug 56431
                group by  r.node_id,achieved) dtl
             WHERE rh.node_id = dtl.node_id (+)
            UNION
            SELECT rh.node_id AS node_id,
               rh.node_name||'' Team'' node_name,
               NVL(achieved,1) achieved,
               goal_achieved,
               base ,
               actual_result,
               goal_value
            FROM (SELECT DISTINCT rh.node_id AS node_id,
                       rh.node_name node_name
                  FROM rpt_hierarchy rh,
                         rpt_cp_selection_summary rpt
                   WHERE detail_node_id                            = rh.node_id
                    AND rpt.record_type LIKE ''%team%''
--                    AND NVL(detail_node_id,0)                           = NVL('''||p_in_parentNodeId||''',0)  --09/30/2014
                    AND NVL(detail_node_id,0) IN (SELECT * FROM TABLE(get_array_varchar(NVL('''||p_in_parentNodeId||''',0))))   --09/30/2014
                    AND rpt.pax_status = NVL('''||p_in_participantStatus||''',rpt.pax_status) -- 09/10/2014 Bug 56431
                 ) rh,
                (SELECT rpt.node_id,
                    COUNT ( CASE WHEN pax_goal_id is not null THEN NVL(achieved,0) ELSE NULL END) goal_achieved,
                    NVL(sum (current_value),0) actual_result,
                    NVL(sum (amount_to_achieve),0) goal_value,
                    NVL(sum (base_quantity),0) base,
                    achieved
                FROM rpt_cp_selection_detail rpt,
                    promotion p,
                    promo_challengepoint promoGoal
--                where rpt.node_id = NVL('''||p_in_parentNodeId||''',0)      --09/30/2014
                  where rpt.node_id IN (SELECT * FROM TABLE(get_array_varchar(NVL('''||p_in_parentNodeId||''',0))))   --09/30/2014
                    AND rpt.promotion_id  = promoGoal.promotion_id (+)
                    AND rpt.promotion_id  = p.promotion_id (+)
                    AND (('''||p_in_promotionId||''' IS NULL) OR ('''||p_in_promotionId||''' is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array('''||p_in_promotionId||''')))))
                    AND NVL(p.promotion_status,'' '') = NVL('''||p_in_promotionStatus||''',NVL(p.promotion_status,'' ''))
                    AND rpt.user_status   = NVL('''||p_in_participantStatus||''',rpt.user_status) -- 09/10/2014 Bug 56431
                group by  rpt.node_id,achieved) dtl
              WHERE rh.node_id                            = dtl.node_id (+)
               )
               )
               SELECT fnc_cms_asset_code_val_extr( ''report.challengepoint.summary'',''ACHIEVED_NO_GOAL'', '''||p_in_languageCode||''' ) goals,
                       NVL(SUM(goal_achieved),0) nbr_pax,
                   DECODE(NVL(total_pax,0),0 ,0 ,ROUND(((NVL(SUM(goal_achieved),0) /NVL(total_pax,0)) * 100),2)) perc_of_pax,
                      NVL(SUM(base),0) total_baseline_objective,
                   NVL(SUM(goal_value),0) total_goal_value,
                    NVL(SUM(actual_result),0) total_actual_production ,
--             DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((((NVL(SUM(actual_result),0)-NVL(SUM(base),0)) /NVL(SUM(actual_result),0)) * 100),2)) perc_increase_baseline,
--                 DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND(((NVL(SUM(actual_result),0)-NVL(SUM(base),0))* 100)/NVL(SUM(base),0),2))perc_increase_baseline,  --09/17/2014
                   DECODE(NVL(SUM(base),0),0 ,NVL(SUM(actual_result),0) ,ROUND(((NVL(SUM(actual_result),0)-NVL(SUM(base),0))* 100)/NVL(SUM(base),0),2))perc_increase_baseline, --09/17/2014    --09/24/2014
                  (NVL(SUM(actual_result),0) - NVL(SUM(base),0) ) unit_dol_inc_baseline,
--                   DECODE(NVL(SUM(goal_value),0),0 ,0 ,ROUND((((NVL(SUM(goal_value),0)-NVL(SUM(base),0)) /NVL(SUM(goal_value),0)) * 100),2)) perc_increase_goal,
--                  DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2)perc_increase_goal,  --09/17/2014
                    DECODE(NVL(SUM(goal_value),0),0,NVL(SUM(actual_result),0), ROUND(ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2) ) perc_increase_goal, --09/17/2014  --09/24/2014 
--                        (NVL(SUM(goal_value),0) - NVL(SUM(base),0) ) unit_dol_inc_goal,
                   (NVL(SUM(actual_result),0) -  NVL(SUM(goal_value),0)) unit_dol_inc_goal  --Bug # 56452
               FROM pivot
              WHERE achieved = 0
            GROUP BY total_pax
           UNION
            SELECT fnc_cms_asset_code_val_extr( ''report.challengepoint.summary'',''ACHIEVED_GOAL'', '''||p_in_languageCode||''' ) goals,
                    NVL(SUM(goal_achieved),0) nbr_pax,
                   DECODE(NVL(total_pax,0),0 ,0 ,ROUND(((NVL(SUM(goal_achieved),0) /NVL(total_pax,0)) * 100),2)) perc_of_pax,
                   NVL(SUM(base),0) total_baseline_objective,
                   NVL(SUM(goal_value),0) total_goal_value,
                   NVL(SUM(actual_result),0) total_actual_production ,
--             DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((((NVL(SUM(actual_result),0)-NVL(SUM(base),0)) /NVL(SUM(actual_result),0)) * 100),2)) perc_increase_baseline,
--                 DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND(((NVL(SUM(actual_result),0)-NVL(SUM(base),0))* 100)/NVL(SUM(base),0),2))perc_increase_baseline,   --09/17/2014
                   DECODE(NVL(SUM(base),0),0 ,NVL(SUM(actual_result),0) ,ROUND(((NVL(SUM(actual_result),0)-NVL(SUM(base),0))* 100)/NVL(SUM(base),0),2))perc_increase_baseline,   --09/17/2014  --09/24/2014
                  (NVL(SUM(actual_result),0) - NVL(SUM(base),0) ) unit_dol_inc_baseline,
--                   DECODE(NVL(SUM(goal_value),0),0 ,0 ,ROUND((((NVL(SUM(goal_value),0)-NVL(SUM(base),0)) /NVL(SUM(goal_value),0)) * 100),2)) perc_increase_goal,
--                  DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2)perc_increase_goal,  --09/17/2014
                    DECODE(NVL(SUM(goal_value),0),0,NVL(SUM(actual_result),0), ROUND(ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2) ) perc_increase_goal, --09/17/2014  --09/24/2014 
--                        (NVL(SUM(goal_value),0) - NVL(SUM(base),0) ) unit_dol_inc_goal,
                   (NVL(SUM(actual_result),0) -  NVL(SUM(goal_value),0)) unit_dol_inc_goal  --Bug # 56452
              FROM pivot
             WHERE achieved = 1
              GROUP BY total_pax ORDER BY '|| v_sortCol ||'
   ) WHERE ROWNUM BETWEEN ' || p_in_rowNumStart || ' AND ' || p_in_rowNumEnd;
   
      OPEN p_out_data FOR l_query;
            /* getChallengePointProgramSummaryResultsSize  */
     SELECT count(*)  INTO  p_out_size_data 
     FROM (
     WITH pivot as
     (SELECT rh.node_id AS node_id,
      rh.node_name node_name,
      NVL(NBR_CHALLENGEPOINT_ACHIEVED,0) NBR_CHALLENGEPOINT_ACHIEVED,
      NVL(total_participants,0) total_participants,
      NVL(CHALLENGEPOINT,0) CHALLENGEPOINT,
      ACHIEVED,
      NVL(base,0) base,
      NVL(actual_result,0)  actual_result
      FROM (SELECT DISTINCT rh.node_id AS node_id,
                rh.node_name node_name
           FROM rpt_hierarchy rh,
                rpt_cp_selection_summary rpt
          WHERE detail_node_id                            = rh.node_id
            AND rpt.record_type LIKE '%node%'
            AND NVL(header_node_id,0)                          IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
            AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) -- 09/10/2014 Bug 56431
         ) rh,
     (SELECT SUM(NBR_CHALLENGEPOINT_ACHIEVED) NBR_CHALLENGEPOINT_ACHIEVED,
           SUM(TOTAL_PARTICIPANTS) total_participants,
           SUM(rpt.GOAL) CHALLENGEPOINT,
           SUM(rpt.base_quantity) base,
           SUM(rpt.actual_result) actual_result,
           CASE WHEN SUM(1) > 0 THEN 1
           ELSE 0
           END ACHIEVED,
          rh.node_id AS node_id,
          rh.node_name node_name
      FROM
      rpt_hierarchy rh,
      promotion p,
      promo_challengepoint promoGoal,
      rpt_cp_selection_summary rpt ,
      goalquest_goallevel gl
     WHERE rpt.level_id                               = gl.goallevel_id (+)
     AND rpt.promotion_id                            = promoGoal.promotion_id (+)
     AND rpt.promotion_id                              = p.promotion_id (+)
     AND detail_node_id                            = rh.node_id
      AND rpt.record_type LIKE '%node%'
     AND NVL(header_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))
     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
     AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) -- 09/10/2014 Bug 56431
      GROUP BY  rh.node_id ,
       rh.node_name
     ) rpc
     WHERE rh.node_id                            = rpc.node_id (+)
     UNION
     SELECT rh.node_id AS node_id,
       rh.node_name||'Team' node_name,
       NVL(NBR_CHALLENGEPOINT_ACHIEVED,0) NBR_CHALLENGEPOINT_ACHIEVED,
       NVL(total_participants,0) total_participants,
       NVL(CHALLENGEPOINT,0) CHALLENGEPOINT,
       ACHIEVED,
       NVL(base,0) base,
       NVL(actual_result,0)  actual_result
     FROM (SELECT DISTINCT rh.node_id AS node_id,
                 rh.node_name node_name
            FROM rpt_hierarchy rh,
                 rpt_cp_selection_summary rpt
           WHERE detail_node_id                            = rh.node_id
             AND rpt.record_type LIKE '%team%'
             AND NVL(detail_node_id,0)                          IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
             AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) -- 09/10/2014 Bug 56431
          ) rh,
     (SELECT SUM(NBR_CHALLENGEPOINT_ACHIEVED) NBR_CHALLENGEPOINT_ACHIEVED,
            SUM(TOTAL_PARTICIPANTS) total_participants,
            SUM(rpt.GOAL) CHALLENGEPOINT,
            SUM(rpt.base_quantity) base,
            SUM(rpt.actual_result) actual_result,
            CASE WHEN SUM(0) > 0 THEN 1
            ELSE 0
            END ACHIEVED,
           rh.node_id AS node_id,
           rh.node_name node_name
       FROM
       rpt_hierarchy rh,
       promotion p,
       promo_challengepoint promoGoal,
       rpt_cp_selection_summary rpt ,
       goalquest_goallevel gl
     WHERE rpt.level_id                               = gl.goallevel_id (+)
     AND rpt.promotion_id                            = promoGoal.promotion_id (+)
     AND rpt.promotion_id                              = p.promotion_id (+)
     AND detail_node_id                            = rh.node_id
      AND rpt.record_type LIKE '%team%'
     AND NVL(detail_node_id,0)                           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))
     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
     AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) -- 09/10/2014 Bug 56431
    GROUP BY rh.node_id ,
       rh.node_name
     ) rpc
      WHERE rh.node_id                            = rpc.node_id (+)
             )
    SELECT 'Goals Achieved' goals,achieved,perc_achieved,total_baseline_production, -- 09/19/2014 Bug 56431 
    total_challenge_production,total_actual_production,perc_increase_baseline,
    unit_dol_inc_baseline,perc_increase_challengpoint,unit_dol_inc_challengpoint
    FROM
        (SELECT NVL(SUM(nbr_challengepoint_achieved),0) achieved,
            DECODE(NVL(SUM(total_participants),0),0 ,0 ,ROUND(((NVL(SUM(nbr_challengepoint_achieved),0) /NVL(SUM(total_participants),0)) * 100),2)) perc_achieved,
            NVL(SUM(base),0) total_baseline_production,
            NVL(SUM(challengepoint),0) total_challenge_production,
            NVL(SUM(actual_result),0) total_actual_production ,
            NULL perc_increase_baseline,
            (NVL(SUM(actual_result),0) - NVL(SUM(base),0) ) unit_dol_inc_baseline,
            NULL perc_increase_challengpoint,
            (NVL(SUM(challengepoint),0) - NVL(SUM(base),0) ) unit_dol_inc_challengpoint
          FROM pivot
         WHERE achieved = 1)
    WHERE achieved <> 0
    OR perc_achieved <> 0
    OR total_baseline_production <> 0
    OR total_challenge_production <> 0
    OR total_actual_production <> 0
    OR perc_increase_baseline <> 0
    OR unit_dol_inc_baseline <> 0
    OR perc_increase_challengpoint <> 0
    OR unit_dol_inc_challengpoint <> 0     
    UNION
    SELECT 'Goals Not Achieved' goals,achieved,perc_achieved,total_baseline_production,
    total_challenge_production,total_actual_production,perc_increase_baseline,
    unit_dol_inc_baseline,perc_increase_challengpoint,unit_dol_inc_challengpoint
    FROM
        (SELECT NVL(SUM(nbr_challengepoint_achieved),0) achieved,
            DECODE(NVL(SUM(total_participants),0),0 ,0 ,ROUND(((NVL(SUM(nbr_challengepoint_achieved),0) /NVL(SUM(total_participants),0)) * 100),2)) perc_achieved,
            NVL(SUM(base),0) total_baseline_production,
            NVL(SUM(challengepoint),0) total_challenge_production,
            NVL(SUM(actual_result),0) total_actual_production ,
            NULL perc_increase_baseline,
            (NVL(SUM(actual_result),0) - NVL(SUM(base),0) ) unit_dol_inc_baseline,
            NULL perc_increase_challengpoint,
            (NVL(SUM(challengepoint),0) - NVL(SUM(base),0) ) unit_dol_inc_challengpoint
          FROM pivot
         WHERE achieved = 0)
    WHERE achieved <> 0
    OR perc_achieved <> 0
    OR total_baseline_production <> 0
    OR total_challenge_production <> 0
    OR total_actual_production <> 0
    OR perc_increase_baseline <> 0
    OR unit_dol_inc_baseline <> 0
    OR perc_increase_challengpoint <> 0
    OR unit_dol_inc_challengpoint <> 0             
           );           
/* getChallengePointProgramSummaryTabularResultsTotals  */
 OPEN p_out_totals_data FOR    --09/17/2014
SELECT NVL(SUM(NBR_PAX),0) nbr_pax,
      DECODE(NVL(SUM(PERC_OF_PAX),0),0 ,0 ,ROUND(((NVL(SUM(PERC_OF_PAX),0) /NVL(sum(PERC_OF_PAX),0)) * 100),2)) perc_of_pax,
      NVL(SUM(total_baseline_objective),0) total_baseline_objective,
      NVL(SUM(total_goal_value),0) total_goal_value,
      NVL(SUM(total_actual_production),0) total_actual_production ,
      DECODE(NVL(SUM(total_baseline_objective),0),0 ,NVL(SUM(total_actual_production),0) ,ROUND(((NVL(SUM(total_actual_production),0)-NVL(SUM(total_baseline_objective),0))* 100)/NVL(SUM(total_baseline_objective),0),2)) perc_increase_baseline,  --09/24/2014            
      (NVL(SUM(total_actual_production),0) - NVL(SUM(total_baseline_objective),0) ) unit_dol_inc_baseline,
      DECODE(NVL(SUM(total_goal_value),0),0,NVL(SUM(total_actual_production),0), ROUND(ROUND((NVL(SUM(total_actual_production),0)-NVL(SUM(total_goal_value),0))* 100) /NVL(SUM(total_goal_value),0),2) ) perc_increase_goal, --09/17/2014   --09/24/2014       
      (NVL(SUM(total_actual_production),0) -  NVL(SUM(total_goal_value),0)) unit_dol_inc_goal
FROM ( SELECT * FROM 
  (   WITH pivot as
      (  SELECT achieved,
               goal_achieved,
               SUM (goal_achieved) OVER() total_pax,
               base,
               actual_result,
                goal_value
           FROM
             (SELECT rh.node_id AS node_id,
                rh.node_name node_name,
                NVL(achieved,1) achieved,
                goal_achieved,
                 base ,
                 actual_result,
                 goal_value
               FROM (SELECT DISTINCT rh.node_id AS node_id,
                          rh.node_name node_name
                    FROM rpt_hierarchy rh,
                         rpt_cp_selection_summary rpt
                    WHERE detail_node_id                            = rh.node_id
                      AND rpt.record_type LIKE '%node%'
                      AND NVL(header_node_id,0)           IN (SELECT * FROM TABLE(get_array_varchar( p_in_parentNodeId ) ))
                      AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) -- 09/10/2014 Bug 56431
                   ) rh,
                (SELECT r.node_id, COUNT ( CASE WHEN pax_goal_id is not null THEN NVL(achieved,0) ELSE NULL END) goal_achieved,
                    NVL(sum (current_value),0) actual_result,
                    NVL(sum (amount_to_achieve),0) goal_value,
                    NVL(sum (base_quantity),0) base,
                    achieved
                FROM rpt_cp_selection_detail rpt,
                    rpt_hierarchy_rollup r,
                    promotion p,
                    promo_challengepoint promoGoal
                where rpt.node_id = r.child_node_id
                    AND rpt.promotion_id                            = promoGoal.promotion_id (+)
                    AND rpt.promotion_id                            = p.promotion_id (+)
                    AND (('' IS NULL) OR ('' is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array('')))))
                    AND NVL(p.promotion_status,' ')  = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
                    AND rpt.user_status   = NVL(p_in_participantStatus,rpt.user_status) -- 09/10/2014 Bug 56431
                group by  r.node_id,achieved) dtl
             WHERE rh.node_id = dtl.node_id (+)
            UNION
            SELECT rh.node_id AS node_id,
               rh.node_name||' Team' node_name,
               NVL(achieved,1) achieved,
               goal_achieved,
               base ,
               actual_result,
               goal_value
            FROM (SELECT DISTINCT rh.node_id AS node_id,
                       rh.node_name node_name
                  FROM rpt_hierarchy rh,
                         rpt_cp_selection_summary rpt
                   WHERE detail_node_id                            = rh.node_id
                    AND rpt.record_type LIKE '%team%'
--                    AND NVL(detail_node_id,0)                           = NVL(p_in_parentNodeId,0)   --09/30/2014
                    AND NVL(detail_node_id,0) IN (SELECT * FROM TABLE(get_array_varchar(NVL(p_in_parentNodeId,0) )))   --09/30/2014
                    AND rpt.pax_status = NVL(p_in_participantStatus,rpt.pax_status) -- 09/10/2014 Bug 56431
                 ) rh,
                (SELECT rpt.node_id,
                    COUNT ( CASE WHEN pax_goal_id is not null THEN NVL(achieved,0) ELSE NULL END) goal_achieved,
                    NVL(sum (current_value),0) actual_result,
                    NVL(sum (amount_to_achieve),0) goal_value,
                    NVL(sum (base_quantity),0) base,
                    achieved
                FROM rpt_cp_selection_detail rpt,
                    promotion p,
                    promo_challengepoint promoGoal
--                where rpt.node_id = NVL(p_in_parentNodeId,0)    --09/30/2014
                  where rpt.node_id IN (SELECT * FROM TABLE(get_array_varchar(NVL(p_in_parentNodeId,0))))   --09/30/2014
                    AND rpt.promotion_id  = promoGoal.promotion_id (+)
                    AND rpt.promotion_id  = p.promotion_id (+)
                    AND (('' IS NULL) OR ('' is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array('')))))
                    AND NVL(p.promotion_status,' ') = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
                    AND rpt.user_status   = NVL(p_in_participantStatus,rpt.user_status) -- 09/10/2014 Bug 56431
                group by  rpt.node_id,achieved) dtl
              WHERE rh.node_id                            = dtl.node_id (+)
               )
               )
               SELECT 'Goals Not Achieved' goals,
                       NVL(SUM(goal_achieved),0) nbr_pax,
                   DECODE(NVL(total_pax,0),0 ,0 ,ROUND(((NVL(SUM(goal_achieved),0) /NVL(total_pax,0)) * 100),2)) perc_of_pax,
                      NVL(SUM(base),0) total_baseline_objective,
                   NVL(SUM(goal_value),0) total_goal_value,
                    NVL(SUM(actual_result),0) total_actual_production ,
--             DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((((NVL(SUM(actual_result),0)-NVL(SUM(base),0)) /NVL(SUM(actual_result),0)) * 100),2)) perc_increase_baseline,
                 DECODE(NVL(SUM(base),0),0 ,NVL(SUM(actual_result),0) ,ROUND(((NVL(SUM(actual_result),0)-NVL(SUM(base),0))* 100)/NVL(SUM(base),0),2))perc_increase_baseline,  --09/24/2014
                  (NVL(SUM(actual_result),0) - NVL(SUM(base),0) ) unit_dol_inc_baseline,
--                   DECODE(NVL(SUM(goal_value),0),0 ,0 ,ROUND((((NVL(SUM(goal_value),0)-NVL(SUM(base),0)) /NVL(SUM(goal_value),0)) * 100),2)) perc_increase_goal,
--                  DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2)perc_increase_goal,
             DECODE(NVL(SUM(goal_value),0),0,NVL(SUM(actual_result),0), ROUND(ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2) ) perc_increase_goal, --09/17/2014  --09/24/2014
--                        (NVL(SUM(goal_value),0) - NVL(SUM(base),0) ) unit_dol_inc_goal,
                   (NVL(SUM(actual_result),0) -  NVL(SUM(goal_value),0)) unit_dol_inc_goal  --Bug # 56452
               FROM pivot
              WHERE achieved = 0
            GROUP BY total_pax
           UNION
            SELECT 'Goals Achieved' goals,
                    NVL(SUM(goal_achieved),0) nbr_pax,
                   DECODE(NVL(total_pax,0),0 ,0 ,ROUND(((NVL(SUM(goal_achieved),0) /NVL(total_pax,0)) * 100),2)) perc_of_pax,
                   NVL(SUM(base),0) total_baseline_objective,
                   NVL(SUM(goal_value),0) total_goal_value,
                   NVL(SUM(actual_result),0) total_actual_production ,
--             DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((((NVL(SUM(actual_result),0)-NVL(SUM(base),0)) /NVL(SUM(actual_result),0)) * 100),2)) perc_increase_baseline,
                 DECODE(NVL(SUM(base),0),0 ,NVL(SUM(actual_result),0) ,ROUND(((NVL(SUM(actual_result),0)-NVL(SUM(base),0))* 100)/NVL(SUM(base),0),2))perc_increase_baseline,  --09/24/2014 
                  (NVL(SUM(actual_result),0) - NVL(SUM(base),0) ) unit_dol_inc_baseline,
--                   DECODE(NVL(SUM(goal_value),0),0 ,0 ,ROUND((((NVL(SUM(goal_value),0)-NVL(SUM(base),0)) /NVL(SUM(goal_value),0)) * 100),2)) perc_increase_goal,
                 --DECODE(NVL(SUM(actual_result),0),0 ,0 ,ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2)perc_increase_goal,
                  DECODE(NVL(SUM(goal_value),0),0,NVL(SUM(actual_result),0), ROUND(ROUND((NVL(SUM(actual_result),0)-NVL(SUM(goal_value),0))* 100) /NVL(SUM(goal_value),0),2) ) perc_increase_goal, --09/17/2014  --09/24/2014 
--                        (NVL(SUM(goal_value),0) - NVL(SUM(base),0) ) unit_dol_inc_goal,
                   (NVL(SUM(actual_result),0) -  NVL(SUM(goal_value),0)) unit_dol_inc_goal  --Bug # 56452
              FROM pivot
             WHERE achieved = 1
              GROUP BY total_pax ORDER BY  GOALS asc
   ) );
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
         p_out_size_data :=NULL;
         OPEN p_out_totals_data FOR SELECT NULL FROM DUAL; 
 END prc_getCPProgramSummaryTabRes;  
/* getChallengePointGoalAchievementChart  */
PROCEDURE prc_getCPGoalAchievementChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPGoalAchievementChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getCPGoalAchievementChart';
     OPEN p_out_data FOR
     SELECT COUNT(CASE WHEN rpt.achieved = 1 THEN rpt.user_id ELSE NULL END) total_achieved ,
     COUNT(CASE WHEN rpt.achieved = 0 THEN rpt.user_id ELSE NULL END) total_not_achieved
     FROM rpt_hierarchy_rollup  rh,
     promotion p,
     promo_challengepoint promoGoal,
     rpt_cp_selection_detail rpt ,
     goalquest_goallevel gl
     WHERE rpt.level_id                               = gl.goallevel_id (+)
     AND rpt.promotion_id                             = promoGoal.promotion_id (+)
     AND rpt.promotion_id                             = p.promotion_id (+)
     AND rpt.node_id                               = rh.child_node_id
--     AND NVL(rh.node_id,0)                         = NVL(p_in_parentNodeId,0)    --09/30/2014
     AND NVL(rh.node_id,0) IN (SELECT * FROM TABLE(get_array_varchar(NVL(p_in_parentNodeId,0) )))   --09/30/2014
     AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array_varchar(p_in_promotionId)))))
     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '));     
p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL;
 END prc_getCPGoalAchievementChart; 
/* getChallengePointIncrementalChart  */
PROCEDURE prc_getCPIncrementalChart (
      p_in_departments         IN     VARCHAR,
      p_in_jobPosition         IN     VARCHAR,
      p_in_languageCode        IN     VARCHAR,
      p_in_managerUserId       IN     VARCHAR,
      p_in_nodeAndBelow        IN     VARCHAR,
      p_in_parentNodeId        IN     VARCHAR,
      p_in_participantStatus   IN     VARCHAR,
      p_in_promotionId        IN     VARCHAR,
      p_in_promotionStatus     IN     VARCHAR,
      p_in_sortColName         IN     VARCHAR,
      p_in_sortedBy            IN     VARCHAR,
      p_in_rowNumStart         IN     NUMBER,
      p_in_rowNumEnd           IN     NUMBER,
      p_out_return_code        OUT    NUMBER,
      p_out_data               OUT    SYS_REFCURSOR)
/******************************************************************************
  NAME:       prc_getCPIncrementalChart
  Date               Author           Description
  ----------        -----------    ------------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J          02/20/2015     Bug Fix 59798 -- CP Summary Chart Translation Issue  
  ******************************************************************************/   
   IS
      c_process_name   CONSTANT execution_log.process_name%TYPE := 'prc_getCPIncrementalChart' ;
      v_stage          VARCHAR2 (500);
 BEGIN
      v_stage   := 'getCPIncrementalChart';
     OPEN p_out_data FOR
      SELECT *
       FROM (
     WITH pivot as
            (SELECT rh.node_id AS node_id,
                rh.node_name node_name,
                NVL(ACHIEVED,1) ACHIEVED,
                base ,
                actual_result
              FROM (SELECT DISTINCT rh.node_id AS node_id,
                          rh.node_name node_name
                     FROM rpt_hierarchy rh,
                          rpt_cp_selection_summary rpt
                     WHERE detail_node_id                            = rh.node_id
                      AND rpt.record_type LIKE '%node%'
--                     AND NVL(header_node_id,0)                           = NVL(p_in_parentNodeId,0)   --09/30/2014
                     AND NVL(header_node_id,0) IN (SELECT * FROM TABLE(get_array_varchar(NVL(p_in_parentNodeId,0) )))   --09/30/2014
                  ) rh,
                (SELECT r.node_id, NVL(sum (current_value),0) actual_result, NVL(sum (AMOUNT_TO_ACHIEVE),0) goal_value,
                     NVL(sum (base_quantity),0) base,
                     achieved
                   FROM rpt_cp_selection_detail rpt,
                       rpt_hierarchy_rollup r,
                      promotion p,
                      promo_challengepoint promoGoal
                 where rpt.node_id = r.child_node_id
                   AND rpt.promotion_id                            = promoGoal.promotion_id (+)
                    AND rpt.promotion_id                            = p.promotion_id (+)
                   AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotionId)))))
                     AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
                  group by  r.node_id,achieved) dtl
               WHERE rh.node_id = dtl.node_id (+)
              UNION
              SELECT rh.node_id AS node_id,
                 rh.node_name||'Team' node_name,
                NVL(ACHIEVED,1) ACHIEVED,
                base ,
                actual_result
              FROM (SELECT DISTINCT rh.node_id AS node_id,
                          rh.node_name node_name
                     FROM rpt_hierarchy rh,
                         rpt_cp_selection_summary rpt
                    WHERE detail_node_id                            = rh.node_id
                      AND rpt.record_type LIKE '%team%'
--                    AND NVL(detail_node_id,0)                           = NVL(p_in_parentNodeId,0)   --09/30/2014
                    AND NVL(detail_node_id,0) IN (SELECT * FROM TABLE(get_array_varchar(NVL(p_in_parentNodeId,0) )))   --09/30/2014
                  ) rh,
               (SELECT rpt.node_id, NVL(sum (current_value),0) actual_result, NVL(sum (AMOUNT_TO_ACHIEVE),0) goal_value,
                    NVL(sum (base_quantity),0) base,
                     achieved
                  FROM rpt_cp_selection_detail rpt,
                      promotion p,
                      promo_challengepoint promoGoal
--                where rpt.node_id = NVL(p_in_parentNodeId,0)  --09/30/2014
                where rpt.node_id IN (SELECT * FROM TABLE(get_array_varchar(NVL(p_in_parentNodeId,0) )))   --09/30/2014
                 AND rpt.promotion_id                            = promoGoal.promotion_id (+)
                  AND rpt.promotion_id                            = p.promotion_id (+)
                  AND ((p_in_promotionId IS NULL) OR (p_in_promotionId is NOT NULL AND P.promotion_id IN (SELECT * FROM TABLE(get_array(p_in_promotionId)))))
                  AND NVL(p.promotion_status,' ')                    = NVL(p_in_promotionStatus,NVL(p.promotion_status,' '))
                 group by  rpt.node_id,achieved) dtl
            WHERE rh.node_id                            = dtl.node_id (+)
            )
--          SELECT 'Goals Not Achieved' goals,  --02/20/2015
            SELECT fnc_cms_asset_code_val_extr( 'report.challengepoint.summary','ACHIEVED_NO_GOAL', p_in_languageCode) goals,  --02/20/2015
                   NVL(SUM(BASE),0) base,
                    NVL(SUM(actual_result),0) actual_result
              FROM pivot
             WHERE achieved = 0
           UNION
--          SELECT 'Goals Achieved' goals,     --02/20/2015
            SELECT fnc_cms_asset_code_val_extr( 'report.challengepoint.summary','ACHIEVED_GOAL', p_in_languageCode) goals,     --02/20/2015
                    NVL(SUM(BASE),0) base,
                    NVL(SUM(actual_result),0) actual_result
             FROM pivot
             WHERE achieved = 1
            UNION
 --        SELECT 'Totals' goals,              --02/20/2015
           SELECT fnc_cms_asset_code_val_extr( 'report.challengepoint.summary','TOTALS', p_in_languageCode) goals,     --02/20/2015
            NVL(SUM(BASE),0) base,
            NVL(SUM(actual_result),0) actual_result
            FROM pivot  );
            p_out_return_code := '00';
 EXCEPTION
      WHEN OTHERS
      THEN
         p_out_return_code := '99';
         prc_execution_log_entry (c_process_name,
                                  1,
                                  'ERROR',
                                  v_stage || SQLERRM,
                                  NULL);

         OPEN p_out_data FOR SELECT NULL FROM DUAL; 
 END prc_getCPIncrementalChart; 
END pkg_query_challengepoint;
/

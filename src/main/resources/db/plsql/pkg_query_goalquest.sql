CREATE OR REPLACE PACKAGE pkg_query_goalquest
IS
    PROCEDURE prc_getGQProgressTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQProgressDetailRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getProgressToGoalChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQProgressResChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQSelectionTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQSelectionValidLvlNums (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQSelectionDetailRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER);

    PROCEDURE prc_getGQSelectionTotalsChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQSelectionByOrgChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQAchievementTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQAchievementDetailRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQPctAchievedChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQCountAchievedChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQAchievementResChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQManagerTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getMgrTotalPtsEarnedChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQManagerDetailTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQProgramSummaryTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQGoalAchievementChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQIncrementalChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);

    PROCEDURE prc_getGQSelectionPctChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR);
END pkg_query_goalquest;



/
CREATE OR REPLACE PACKAGE BODY      pkg_query_goalquest
IS
    PROCEDURE prc_getGQProgressTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
/*-----------------------------------------------------------------------------
  Purpose: Initial set of queries were in java.
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Swati            09/25/2014    Bug 56887 - GoalQuest progress reports are not removing the inactivated, or removing participants from the reports. 
                            They are remaining on the reports.
  Ravi Dhanekula 01/15/2016 Bug # 65257 --GoalQuest reports fail to open for managers of multiple org units
                                              Modified procedures 1). prc_getGQProgressTabRes
                                                                             2). prc_getGQGoalAchievementChart
                                                                             3). prc_getGQIncrementalChart
-----------------------------------------------------------------------------*/    
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQProgressTabRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    
    l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROW_NUMBER () OVER (ORDER BY '|| v_sortCol ||') RN, rs.*
                      FROM (  SELECT SUM (total_participants)
                                         AS total_participants,
                                     SUM (
                                         DECODE (goal_level,
                                                 NULL, total_participants,
                                                 0))
                                         AS no_goal_selected,
                                     SUM (
                                         DECODE (goal_level,
                                                 NULL, 0,
                                                 total_participants))
                                         AS nbr_selected,
                                     SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_25_percent,
                                     SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_50_percent,
                                     SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_75_percent,
                                     SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_76_99_percent,
                                     SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_100_percent,
                                     SUM (NVL (base_quantity, 0)) AS base,
                                     SUM (NVL (goal, 0)) AS goal,
                                     SUM (NVL (actual_result, 0))
                                         AS actual_result,
                                     CASE
                                         WHEN SUM (NVL (goal, 0)) = 0
                                         THEN
                                             0
                                         ELSE
                                             ROUND (
                                                   (  SUM (
                                                          NVL (actual_result, 0))
                                                    / SUM (goal))
                                                 * 100,
                                                 2)
                                     END
                                         AS percent_of_goal,
                                     rh.node_name AS node_name,
                                     rh.node_id AS node_id
                                FROM rpt_hierarchy rh,
                                     promo_goalquest promoGoal,
                                     promotion p,
                                     rpt_goal_selection_summary rpt
                                     LEFT OUTER JOIN goalquest_goallevel gl
                                         ON rpt.goal_level = gl.goallevel_id
                               WHERE     rpt.record_type LIKE ''%node%''
                                     AND rpt.header_node_id IN (SELECT *
                                                                  FROM TABLE (
                                                                           get_array_varchar (
                                                                               '''||p_in_parentNodeId||''')))
                                     AND rpt.detail_node_id = rh.node_id
                                     AND rpt.pax_status =
                                             NVL ('''||p_in_participantStatus||''',
                                                  rpt.pax_status)
                                     AND NVL(rpt.job_position,''JOB'') =
                                             NVL ('''||p_in_jobPosition||''',
                                                  NVL(rpt.job_position,''JOB''))
                                     AND (   ('''||p_in_departments||''' IS NULL)
                                          OR rpt.department IN (SELECT *
                                                                  FROM TABLE (
                                                                           get_array_varchar (
                                                                               '''||p_in_departments||'''))))
                                     AND (   ('''||p_in_promotionId||''' IS NULL)
                                          OR rpt.promotion_id IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array_varchar (
                                                                                 '''||p_in_promotionId||'''))))
                                     AND p.promotion_status =
                                             NVL ('''||p_in_promotionStatus||''',
                                                  p.promotion_status)
                                     AND rpt.promotion_id =
                                             promoGoal.promotion_id
                                     AND p.promotion_id = rpt.promotion_id
                            GROUP BY rh.node_name, rh.node_id
                            UNION
                              SELECT SUM (total_participants)
                                         AS total_participants,
                                     SUM (
                                         DECODE (goal_level,
                                                 NULL, total_participants,
                                                 0))
                                         AS no_goal_selected,
                                     SUM (
                                         DECODE (goal_level,
                                                 NULL, 0,
                                                 total_participants))
                                         AS nbr_selected,
                                     SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_25_percent,
                                     SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_50_percent,
                                     SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_75_percent,
                                     SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_76_99_percent,
                                     SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                         AS sum_nbr_pax_100_percent,
                                     SUM (NVL (base_quantity, 0)) AS base,
                                     SUM (NVL (goal, 0)) AS goal,
                                     SUM (NVL (actual_result, 0))
                                         AS actual_result,
                                     CASE
                                         WHEN SUM (NVL (goal, 0)) = 0
                                         THEN
                                             0
                                         ELSE
                                             ROUND (
                                                   (  SUM (
                                                          NVL (actual_result, 0))
                                                    / SUM (goal))
                                                 * 100,
                                                 2)
                                     END
                                         AS percent_of_goal,
                                     node_name || '' Team'' AS NODE_NAME,
                                     rh.node_id
                                FROM rpt_hierarchy rh,
                                     promotion p,
                                     promo_goalquest promoGoal,
                                     rpt_goal_selection_summary rpt,
                                     goalquest_goallevel gl
                               WHERE     rpt.goal_level = gl.goallevel_id(+)
                                     AND rpt.promotion_id =
                                             promoGoal.promotion_id(+)
                                     AND rpt.promotion_id = p.promotion_id(+)
                                     AND detail_node_id = rh.node_id(+)
                                     AND rpt.record_type LIKE ''%team%''
                                     AND NVL (detail_node_id, 0) IN (SELECT *
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    '''||p_in_parentNodeId||''')))
                                     AND rpt.pax_status =
                                             NVL ('''||p_in_participantStatus||''',
                                                  rpt.pax_status)
                                     AND rpt.job_position =
                                             NVL ('''||p_in_jobPosition||''',
                                                  rpt.job_position)
                                     AND (   ('''||p_in_departments||''' IS NULL)
                                          OR (    '''||p_in_departments||''' IS NOT NULL
                                              AND rpt.department IN (SELECT *
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    '''||p_in_departments||''')))))
                                     AND (   ('''||p_in_promotionId||''' IS NULL)
                                          OR (    '''||p_in_promotionId||''' IS NOT NULL
                                              AND rpt.promotion_id IN (SELECT *
                                                                         FROM TABLE (
                                                                                  get_array_varchar (
                                                                                      '''||p_in_promotionId||''')))))
                                     AND NVL (p.promotion_status, '' '') =
                                             NVL ('''||p_in_promotionStatus||''',
                                                  NVL (p.promotion_status, '' ''))
                            GROUP BY --sequence_num,--09/25/2014 Bug 56887
                            rh.node_id, rh.node_name) rs)
             WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;

    OPEN p_out_data FOR l_query;

        --getGQProgressTabResTotals
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (rs.total_participants), 0) total_participants,
                   NVL (SUM (rs.no_goal_selected), 0) no_goal_selected,
                   NVL (SUM (rs.nbr_selected), 0) nbr_selected,
                   NVL (SUM (rs.sum_nbr_pax_25_percent), 0)
                       sum_nbr_pax_25_percent,
                   NVL (SUM (rs.sum_nbr_pax_50_percent), 0)
                       sum_nbr_pax_50_percent,
                   NVL (SUM (rs.sum_nbr_pax_75_percent), 0)
                       sum_nbr_pax_75_percent,
                   NVL (SUM (rs.sum_nbr_pax_76_99_percent), 0)
                       sum_nbr_pax_76_99_percent,
                   NVL (SUM (rs.sum_nbr_pax_100_percent), 0)
                       sum_nbr_pax_100_percent,
                   NVL (SUM (base), 0) AS base,
                   NVL (SUM (goal), 0) AS goal,
                   NVL (SUM (actual_result), 0) AS actual_result,
                   CASE
                       WHEN NVL (SUM (goal), 0) = 0
                       THEN
                           0
                       ELSE
                           ROUND (
                                 (SUM (NVL (actual_result, 0)) / SUM (goal))
                               * 100,
                               2)
                   END
                       AS percent_of_goal
              FROM (  SELECT SUM (total_participants) AS total_participants,
                             SUM (
                                 DECODE (goal_level, NULL, total_participants, 0))
                                 AS no_goal_selected,
                             SUM (
                                 DECODE (goal_level, NULL, 0, total_participants))
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             NVL (SUM (base_quantity), 0) AS base,
                             NVL (SUM (goal), 0) AS goal,
                             NVL (SUM (actual_result), 0) AS actual_result,
                             rh.node_name node_name,
                             rh.node_id
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion p,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     rpt.record_type LIKE '%node%'
                             AND rpt.header_node_id IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_parentNodeId)))
                             AND rpt.detail_node_id = rh.node_id
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR rpt.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                             AND (   (p_in_promotionId IS NULL)
                                  OR rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND p.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          p.promotion_status)
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND p.promotion_id = rpt.promotion_id
                    GROUP BY rh.node_name, rh.node_id
                    UNION
                      SELECT SUM (total_participants) AS total_participants,
                             SUM (
                                 DECODE (goal_level, NULL, total_participants, 0))
                                 AS no_goal_selected,
                             SUM (
                                 DECODE (goal_level, NULL, 0, total_participants))
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             SUM (NVL (base_quantity, 0)) AS base,
                             SUM (NVL (goal, 0)) AS goal,
                             SUM (NVL (actual_result, 0)) AS actual_result,
                             node_name || ' Team' AS NODE_NAME,
                             rh.node_id
                        FROM rpt_hierarchy rh,
                             promotion p,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     detail_node_id = rh.node_id
                             AND rpt.record_type LIKE '%team%'
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR rpt.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
--                             AND NVL (rpt.detail_node_id, 0) =
--                                     NVL (p_in_parentNodeId, 0)
                               AND NVL (rpt.detail_node_id, 0) IN (SELECT *----01/15/2016--, Bug # 65257
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    NVL(p_in_parentNodeId,0))))
                             AND rpt.promotion_id = p.promotion_id
                             AND (   (p_in_promotionId IS NULL)
                                  OR rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND (   p.promotion_status IS NULL
                                  OR p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status))
                    GROUP BY rh.node_name, rh.node_id) rs;



        --getGQProgressTabResSize
        SELECT COUNT (1) INTO p_out_size_data
              FROM (  SELECT SUM (total_participants) AS total_participants,
                             SUM (
                                 DECODE (goal_level, NULL, total_participants, 0))
                                 AS no_goal_selected,
                             SUM (
                                 DECODE (goal_level, NULL, 0, total_participants))
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             rh.node_name node_name,
                             rh.node_id
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion p,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     rpt.record_type LIKE '%node%'
                             AND rpt.header_node_id IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_parentNodeId)))
                             AND rpt.detail_node_id = rh.node_id
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR rpt.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                             AND (   (p_in_promotionId IS NULL)
                                  OR rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND p.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          p.promotion_status)
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND p.promotion_id = rpt.promotion_id
                    GROUP BY rh.node_name, rh.node_id
                    UNION
                      SELECT SUM (total_participants) AS total_participants,
                             SUM (
                                 DECODE (goal_level, NULL, total_participants, 0))
                                 AS no_goal_selected,
                             SUM (
                                 DECODE (goal_level, NULL, 0, total_participants))
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             node_name || ' Team' AS NODE_NAME,
                             rh.node_id
                        FROM rpt_hierarchy rh,
                             promotion p,
                             promo_goalquest promoGoal,
                             rpt_goal_selection_summary rpt,
                             goalquest_goallevel gl
                       WHERE     rpt.goal_level = gl.goallevel_id(+)
                             AND rpt.promotion_id = promoGoal.promotion_id(+)
                             AND rpt.promotion_id = p.promotion_id(+)
                             AND detail_node_id = rh.node_id(+)
                             AND rpt.record_type LIKE '%team%'
                             AND NVL (detail_node_id, 0) IN (SELECT *
                                                               FROM TABLE (
                                                                        get_array_varchar (
                                                                            p_in_parentNodeId)))
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR (    p_in_departments IS NOT NULL
                                      AND rpt.department IN (SELECT *
                                                               FROM TABLE (
                                                                        get_array_varchar (
                                                                            p_in_departments)))))
                             AND (   (p_in_promotionId IS NULL)
                                  OR (    p_in_promotionId IS NOT NULL
                                      AND rpt.promotion_id IN (SELECT *
                                                                 FROM TABLE (
                                                                          get_array_varchar (
                                                                              p_in_promotionId)))))
                             AND NVL (p.promotion_status, ' ') =
                                     NVL (p_in_promotionStatus,
                                          NVL (p.promotion_status, ' '))
                    GROUP BY --sequence_num, --09/25/2014 Bug 56887
                    rh.node_id, rh.node_name) rs;

        p_out_return_code := '00';
    EXCEPTION
        WHEN OTHERS
        THEN
            prc_execution_log_entry (c_process_name,
                                     1,
                                     'ERROR',
                                     v_stage || SQLERRM,
                                     NULL);

            OPEN p_out_data FOR SELECT NULL FROM DUAL;

            p_out_size_data := NULL;

            OPEN p_out_totals_data FOR SELECT NULL FROM DUAL;
    END prc_getGQProgressTabRes;



    PROCEDURE prc_getGQProgressDetailRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
/*-----------------------------------------------------------------------------
  Purpose: Initial set of queries were in java.
  
  Modification history
  Person        Date        Comments
  -----------   ----------  --------------------------------------------------
  Swati         04/06/2015  Bug 60462 - Goalquest: Goal value and % of Goal total value are not accepting decimel values in goal quest progress report
  Swati         04/09/2015  Bug 61203 - GoalQuest Progress - Zero values are not getting displayed in both summary and in export output
  Sherif Basha  12/23/2016  Bug 70628 - GoalQuest Progress->Bonfire home designs->Bonfire home designs Team->Total amount is mismatched for Goal column tabel 
-----------------------------------------------------------------------------*/        
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQProgressDetailRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    
    l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                        FROM (SELECT    gsd.last_name
                                     || '' , ''
                                     || gsd.first_name
                                     || '' ''
                                     || gsd.middle_init
                                         AS pax_name,
                                     gsd.promotion_name AS promo_name,
                                     hier.node_name AS node_name,
                                     gl.sequence_num AS sequence_num,
                                     ''Level '' || gl.sequence_num AS level_name,
                                     gsd.base_quantity AS base_quantity,
                                     nvl(gsd.amount_to_achieve,0) AS amount_to_achieve, --04/09/2015 Bug 61203 Added NVL
                                     nvl(gsd.current_value,0) AS current_value,
                                     nvl(ROUND (gsd.percent_of_goal, 2),0)
                                         AS percent_of_goal,
                                     nvl(gsd.achieved,0) AS achieved
                                FROM promo_goalquest promoGoal,
                                     promotion p,
                                     RPT_GOAL_SELECTION_DETAIL gsd
                                     LEFT OUTER JOIN RPT_HIERARCHY hier
                                         ON gsd.node_id = hier.node_id
                                     LEFT OUTER JOIN PROMOTION promo
                                         ON gsd.promotion_id = promo.promotion_id
                                     RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                                         ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                                     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                         ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
                               WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array_varchar (
                                                                                 '''||p_in_parentNodeId||''')))
                                     AND (   ('''||p_in_promotionId||''' IS NULL)
                                          OR gsd.promotion_id IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array_varchar (
                                                                                 '''||p_in_promotionId||'''))))
                                     AND gsd.promotion_id =
                                             promoGoal.promotion_id
                                     AND gsd.promotion_id = p.promotion_id
                                     AND NVL (p.promotion_status, '' '') =
                                             NVL ('''||p_in_promotionStatus||''',
                                                  NVL (p.promotion_status, '' ''))
                                     AND gsd.user_status =
                                             NVL ('''||p_in_participantStatus||''',
                                                  gsd.user_status)
                                     AND NVL(gsd.job_position,''JOB'') =
                                             NVL ('''||p_in_jobPosition||''',
                                                  NVL(gsd.job_position,''JOB''))
                                     AND (   ('''||p_in_departments||''' IS NULL)
                                          OR gsd.department IN (SELECT *
                                                                  FROM TABLE (
                                                                           get_array_varchar (
                                                                               '''||p_in_departments||''')))))
                             rs
                    ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;
        
    OPEN p_out_data FOR 
   l_query;
   

        --getGQProgressDetailResTotals
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (base_quantity), 0) AS BASE_QUANTITY,
                   NVL (SUM (amount_to_achieve), 0) AS AMOUNT_TO_ACHIEVE,
                   NVL (SUM (current_value), 0) AS CURRENT_VALUE,
                    /* ROUND (
                           NVL (SUM (current_value), 0)
                         / NVL (SUM (amount_to_achieve), 0),
                         2)
                   * 100*/ --Commented and replaced with case for Bug 56309 on 9/5/2014 by Ramkumar
                   case when SUM (NVL(amount_to_achieve,0)) <> 0 then  
                          ROUND ((NVL (SUM (NVL(current_value,0)), 0)  / NVL (SUM (NVL(amount_to_achieve,0)), 0)  * 100),2)--04/06/2015 Bug 60462
                     ELSE 0
                   end
                   AS PERCENT_ACHIEVED,
                   NVL (SUM (achieved), 0) AS ACHIEVED
              FROM (SELECT    gsd.last_name
                           || ' , '
                           || gsd.first_name
                           || ' '
                           || gsd.middle_init
                               pax_name,
                           gsd.promotion_name,
                           hier.node_name,
                           gl.sequence_num,
                           gsd.goal_level_name goal_level_name,
                           gsd.base_quantity base_quantity,
                           gsd.amount_to_achieve amount_to_achieve,
                           gsd.current_value current_value,
                           gsd.percent_of_goal percent_of_goal,
                           gsd.achieved achieved
                      FROM promo_goalquest promoGoal,
                           promotion p,
                           RPT_GOAL_SELECTION_DETAIL gsd
                           LEFT OUTER JOIN RPT_HIERARCHY hier
                               ON gsd.node_id = hier.node_id
                           LEFT OUTER JOIN PROMOTION promo
                               ON gsd.promotion_id = promo.promotion_id
                           RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                               ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                           LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                               ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
                     WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_parentNodeId)))
                           AND (   (p_in_promotionId IS NULL)
                                OR gsd.promotion_id IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_promotionId))))
                           AND gsd.promotion_id = promoGoal.promotion_id
                           AND gsd.promotion_id = p.promotion_id
                           AND NVL (p.promotion_status, ' ') =
                                   NVL (p_in_promotionStatus,
                                        NVL (p.promotion_status, ' '))
                           AND gsd.user_status =
                                   NVL (p_in_participantStatus,
                                        gsd.user_status)
                           AND NVL(gsd.job_position,'JOB') =               --     12/23/2016  Bug 70628 handling job_position null values
                                  NVL (p_in_jobPosition,NVL(gsd.job_position,'JOB'))  --     12/23/2016  Bug 70628 handling job_position null values
                           AND (   (p_in_departments IS NULL)
                                OR gsd.department IN (SELECT *
                                                        FROM TABLE (
                                                                 get_array_varchar (
                                                                     p_in_departments)))))
                   rs;



        --getGQProgressDetailResSize        
            SELECT COUNT (1) INTO p_out_size_data
              FROM promo_goalquest promoGoal,
                   promotion p,
                   RPT_GOAL_SELECTION_DETAIL gsd
                   LEFT OUTER JOIN RPT_HIERARCHY hier
                       ON gsd.node_id = hier.node_id
                   LEFT OUTER JOIN PROMOTION promo
                       ON gsd.promotion_id = promo.promotion_id
                   RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                       ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                   LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                       ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
             WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                  FROM TABLE (
                                                           get_array_varchar (
                                                               p_in_parentNodeId)))
                   AND (   p_in_promotionId IS NULL
                        OR gsd.promotion_id IN (SELECT *
                                                  FROM TABLE (
                                                           get_array_varchar (
                                                               p_in_promotionId))))
                   AND gsd.promotion_id = promoGoal.promotion_id
                   AND gsd.promotion_id = p.promotion_id
                   AND NVL (p.promotion_status, ' ') =
                           NVL (p_in_promotionStatus,
                                NVL (p.promotion_status, ' '))
                   AND gsd.user_status =
                           NVL (p_in_participantStatus, gsd.user_status)
                   AND NVL(gsd.job_position,'JOB') =               --     12/23/2016  Bug 70628 handling job_position null values
                          NVL (p_in_jobPosition,NVL(gsd.job_position,'JOB'))  --     12/23/2016  Bug 70628 handling job_position null values
                   AND (   p_in_departments IS NULL
                        OR gsd.department IN (SELECT *
                                                FROM TABLE (
                                                         get_array_varchar (
                                                             p_in_departments))));

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
    END prc_getGQProgressDetailRes;



    PROCEDURE prc_getProgressToGoalChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
   /*-----------------------------------------------------------------------------
--  Purpose:  Query used for Progress To Goal Chart. Initial set of queries were in java.
  
--  Modification history
--  Person        Date        Comments
--  -----------   ----------  --------------------------------------------------
--  Suresh J     06/18/2015    Bug 62280 - Chart is not matching with Summary pax level reprot  
-----------------------------------------------------------------------------*/    
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getProgressToGoalChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
    
        OPEN p_out_data FOR
            SELECT SUM (SUM_NBR_PAX_25_PERCENT) AS SUM_25,
                   SUM (SUM_NBR_PAX_50_PERCENT) AS SUM_50,
                   SUM (SUM_NBR_PAX_75_PERCENT) AS SUM_75,
                   SUM (SUM_NBR_PAX_76_99_PERCENT) AS SUM_76_99,
                   SUM (SUM_NBR_PAX_100_PERCENT) AS SUM_100
              FROM (  SELECT rh.node_name NODE_NAME,
                             rh.node_type_name NODE_TYPE_NAME,
                             SUM (total_participants) AS total_participants,
                             SUM (
                                 CASE
                                     WHEN goal_level IS NULL
                                     THEN
                                         total_participants
                                     ELSE
                                         0
                                 END)
                                 AS no_goal_selected,
                             SUM (
                                 CASE
                                     WHEN goal_level IS NULL THEN 0
                                     ELSE total_participants
                                 END)
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             rh.parent_node_id PARENT_NODE_ID,
                             rpt.header_node_id HEADER_NODE_ID,
                             rpt.detail_node_id DETAIL_NODE_ID
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion p,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                 ON rpt.GOAL_LEVEL = gl.GOALLEVEL_ID
                       WHERE     rpt.record_type LIKE '%node%'
                             AND p_in_nodeAndBelow = 'true'   --06/18/2015
                             AND rpt.header_node_id IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_parentNodeId)))
                             AND rpt.detail_node_id = rh.node_id
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR rpt.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                             AND (   (p_in_promotionId IS NULL)
                                  OR rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND p.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          p.promotion_status)
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND p.promotion_id = rpt.promotion_id
                    GROUP BY rpt.record_type,
                             rh.node_name,
                             rh.node_type_name,
                             rpt.is_leaf,
                             rh.parent_node_id,
                             rpt.header_node_id,
                             rpt.detail_node_id
                    UNION
                      SELECT DECODE (INSTR (rpt.record_type, 'team'),
                                     0, node_name,
                                     node_name || ' Team')
                                 AS NODE_NAME,
                             rh.node_type_name node_type_name,
                             SUM (total_participants) AS total_participants,
                             SUM (
                                 CASE
                                     WHEN goal_level IS NULL
                                     THEN
                                         total_participants
                                     ELSE
                                         0
                                 END)
                                 AS no_goal_selected,
                             SUM (
                                 CASE
                                     WHEN goal_level IS NULL THEN 0
                                     ELSE total_participants
                                 END)
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             rh.parent_node_id parent_node_id,
                             rpt.header_node_id header_node_id,
                             rpt.detail_node_id detail_node_id
                        FROM rpt_hierarchy rh,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN promo_goalquest promoGoal
                                 ON rpt.promotion_id = promoGoal.promotion_id
                             LEFT OUTER JOIN promotion p
                                 ON rpt.promotion_id = p.promotion_id
                             LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                 ON rpt.GOAL_LEVEL = gl.GOALLEVEL_ID
                       WHERE     detail_node_id = rh.node_id
                             AND rpt.record_type LIKE '%team%'
                             AND p_in_nodeAndBelow = 'true'   --06/18/2015                             
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR rpt.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                             AND NVL (rpt.detail_node_id, 0) IN (SELECT *
                                                                   FROM TABLE (
                                                                            get_array_varchar (
                                                                                p_in_parentNodeId)))
                             AND (   (p_in_promotionId IS NULL)
                                  OR rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND (   p.promotion_status IS NULL
                                  OR p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status))
                    GROUP BY rpt.record_type,
                             rh.node_name,
                             rh.node_type_name,
                             rpt.is_leaf,
                             rh.parent_node_id,
                             rpt.header_node_id,
                             rpt.detail_node_id
UNION --06/18/2015
                      SELECT DECODE (INSTR (rpt.record_type, 'team'),   --06/18/2015
                                     0, node_name,
                                     node_name || ' Team')
                                 AS NODE_NAME,
                             rh.node_type_name node_type_name,
                             SUM (total_participants) AS total_participants,
                             SUM (
                                 CASE
                                     WHEN goal_level IS NULL
                                     THEN
                                         total_participants
                                     ELSE
                                         0
                                 END)
                                 AS no_goal_selected,
                             SUM (
                                 CASE
                                     WHEN goal_level IS NULL THEN 0
                                     ELSE total_participants
                                 END)
                                 AS nbr_selected,
                             SUM (NVL (NBR_PAX_25_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_25_percent,
                             SUM (NVL (NBR_PAX_50_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_50_percent,
                             SUM (NVL (NBR_PAX_75_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_75_percent,
                             SUM (NVL (NBR_PAX_76_99_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_76_99_percent,
                             SUM (NVL (NBR_PAX_100_PERCENT_OF_GOAL, 0))
                                 AS sum_nbr_pax_100_percent,
                             rh.parent_node_id parent_node_id,
                             rpt.header_node_id header_node_id,
                             rpt.detail_node_id detail_node_id
                        FROM rpt_hierarchy rh,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN promo_goalquest promoGoal
                                 ON rpt.promotion_id = promoGoal.promotion_id
                             LEFT OUTER JOIN promotion p
                                 ON rpt.promotion_id = p.promotion_id
                             LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                 ON rpt.GOAL_LEVEL = gl.GOALLEVEL_ID
                       WHERE     detail_node_id = rh.node_id
                             AND rpt.record_type LIKE '%team%'
                             AND p_in_nodeAndBelow = 'false'   --06/18/2015                             
                             AND rpt.pax_status =
                                     NVL (p_in_participantStatus, rpt.pax_status)
                             AND rpt.job_position =
                                     NVL (p_in_jobPosition, rpt.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR rpt.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                             AND NVL (rpt.detail_node_id, 0) IN (SELECT *
                                                                   FROM TABLE (
                                                                            get_array_varchar (
                                                                                p_in_parentNodeId)))
                             AND (   (p_in_promotionId IS NULL)
                                  OR rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND (   p.promotion_status IS NULL
                                  OR p.promotion_status =
                                         NVL (p_in_promotionStatus,
                                              p.promotion_status))
                    GROUP BY rpt.record_type,
                             rh.node_name,
                             rh.node_type_name,
                             rpt.is_leaf,
                             rh.parent_node_id,
                             rpt.header_node_id,
                             rpt.detail_node_id
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
    END prc_getProgressToGoalChart;

    PROCEDURE prc_getGQProgressResChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQProgressResChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (  SELECT    gsd.last_name
                             || ' , '
                             || gsd.first_name
                             || ' '
                             || gsd.middle_init
                                 AS pax_name,
                             gsd.promotion_name AS promo_name,
                             hier.node_name AS node_name,
                             gsd.base_quantity AS base_quantity,
                             gsd.amount_to_achieve AS amount_to_achieve,
                             gsd.current_value AS current_value
                        FROM promo_goalquest promoGoal,
                             RPT_GOAL_SELECTION_DETAIL gsd
                             LEFT OUTER JOIN RPT_HIERARCHY hier
                                 ON gsd.node_id = hier.node_id
                             LEFT OUTER JOIN PROMOTION promo
                                 ON gsd.promotion_id = promo.promotion_id
                             RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                                 ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                             LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                 ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
                       WHERE     NVL (gsd.node_id, 0) IN (SELECT child_node_id
                                                            FROM rpt_hierarchy_rollup
                                                           WHERE node_id IN (SELECT *
                                                                               FROM TABLE (
                                                                                        get_array_varchar (
                                                                                            p_in_parentNodeId))))
                             AND (   (p_in_promotionId IS NULL)
                                  OR gsd.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     get_array_varchar (
                                                                         p_in_promotionId))))
                             AND gsd.promotion_id = promoGoal.promotion_id
                             AND gsd.user_status =
                                     NVL (p_in_participantStatus,
                                          gsd.user_status)
                             AND gsd.job_position =
                                     NVL (p_in_jobPosition, gsd.job_position)
                             AND (   (p_in_departments IS NULL)
                                  OR gsd.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                    ORDER BY PAX_NAME ASC)
             WHERE ROWNUM <= 10;

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
    END prc_getGQProgressResChart;


    PROCEDURE prc_getGQSelectionTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQSelectionTabRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    
    l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
  FROM (SELECT node_name AS NODE_NAME,
               node_id AS NODE_ID,
               total_participant AS TOTAL_PARTICIPANTS,
               NVL (no_goal_selected, 0) AS NO_GOAL_SELECTED,
               NVL (level_1_selected, 0) AS LEVEL_1_SELECTED,
               NVL (level_1_sele_perc, 0) AS LEVEL_1_SELECTED_PERCENT,
               NVL (level_2_selected, 0) AS LEVEL_2_SELECTED,
               NVL (level_2_sele_perc, 0) AS LEVEL_2_SELECTED_PERCENT,
               NVL (level_3_selected, 0) AS LEVEL_3_SELECTED,
               NVL (level_3_sele_perc, 0) AS LEVEL_3_SELECTED_PERCENT,
               NVL (level_4_selected, 0) AS LEVEL_4_SELECTED,
               NVL (level_4_sele_perc, 0) AS LEVEL_4_SELECTED_PERCENT,
               NVL (level_5_selected, 0) AS LEVEL_5_SELECTED,
               NVL (level_5_sele_perc, 0) AS LEVEL_5_SELECTED_PERCENT,
               NVL (level_6_selected, 0) AS LEVEL_6_SELECTED,
               NVL (level_6_sele_perc, 0) AS LEVEL_6_SELECTED_PERCENT,
               0 AS IS_LEAF
          FROM (WITH pivot_set
                     AS (SELECT total_participants AS sum_total_pax,
                                NVL (sequence_num, 0) sequence_num,
                                rh.node_id AS node_id,
                                rh.node_name node_name
                           FROM (SELECT DISTINCT
                                        rh.node_id AS node_id,
                                        rh.node_name node_name
                                   FROM rpt_hierarchy rh,
                                        rpt_goal_selection_summary rpt
                                  WHERE     detail_node_id = rh.node_id
                                        AND rpt.record_type LIKE ''%node%''
                                        AND NVL (header_node_id, 0) IN (SELECT *
                                                                          FROM TABLE (
                                                                                  get_array_varchar (
                                                                                     '''||p_in_parentNodeId||'''))))
                                rh,
                                (  SELECT SUM (NVL (total_participants, 0))
                                             total_participants,
                                          gl.sequence_num,
                                          rh.node_id AS node_id,
                                          rh.node_name node_name
                                     FROM rpt_hierarchy rh,
                                          promotion p,
                                          promo_goalquest promoGoal,
                                          rpt_goal_selection_summary rpt,
                                          goalquest_goallevel gl
                                    WHERE     rpt.goal_level =
                                                 gl.goallevel_id(+)
                                          AND rpt.promotion_id =
                                                 promoGoal.promotion_id(+)
                                          AND rpt.promotion_id = p.promotion_id
                                          AND detail_node_id = rh.node_id
                                          AND rpt.record_type LIKE ''%node%''
                                          AND NVL (header_node_id, 0) IN (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_parentNodeId||''')))
                                          AND rpt.pax_status =
                                                 NVL ('''||p_in_participantStatus||''',
                                                      rpt.pax_status)
                                          AND rpt.job_position =
                                                 NVL ('''||p_in_jobPosition||''',
                                                      rpt.job_position)
                                          AND (   ('''||p_in_departments||''' IS NULL)
                                               OR (    '''||p_in_departments||'''
                                                          IS NOT NULL
                                                   AND rpt.department IN (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_departments||''')))))
                                          AND (   ('''||p_in_promotionId||''' IS NULL)
                                               OR (    '''||p_in_promotionId||'''
                                                          IS NOT NULL
                                                   AND rpt.promotion_id IN (SELECT *
                                                                              FROM TABLE (
                                                                                      get_array_varchar (
                                                                                         '''||p_in_promotionId||''')))))
                                          AND NVL (p.promotion_status, '' '') =
                                                 NVL (
                                                    '''||p_in_promotionStatus||''',
                                                    NVL (p.promotion_status,
                                                         '' ''))
                                 GROUP BY sequence_num,
                                          rh.node_id,
                                          rh.node_name) rpc
                          WHERE rh.node_id = rpc.node_id(+)
                         UNION
                         SELECT total_participants AS sum_total_pax,
                                NVL (sequence_num, 0) sequence_num,
                                rh.node_id AS node_id,
                                rh.node_name || '' Team'' node_name
                           FROM (SELECT DISTINCT
                                        rh.node_id AS node_id,
                                        rh.node_name node_name
                                   FROM rpt_hierarchy rh,
                                        rpt_goal_selection_summary rpt
                                  WHERE     detail_node_id = rh.node_id
                                        AND rpt.record_type LIKE ''%team%''
                                        AND NVL (detail_node_id, 0) IN (SELECT *
                                                                          FROM TABLE (
                                                                                  get_array_varchar (
                                                                                     '''||p_in_parentNodeId||'''))))
                                rh,
                                (  SELECT SUM (NVL (total_participants, 0))
                                             total_participants,
                                          gl.sequence_num,
                                          rh.node_id AS node_id,
                                          rh.node_name node_name
                                     FROM rpt_hierarchy rh,
                                          promotion p,
                                          promo_goalquest promoGoal,
                                          rpt_goal_selection_summary rpt,
                                          goalquest_goallevel gl
                                    WHERE     rpt.goal_level =
                                                 gl.goallevel_id(+)
                                          AND rpt.promotion_id =
                                                 promoGoal.promotion_id(+)
                                          AND rpt.promotion_id =
                                                 p.promotion_id(+)
                                          AND detail_node_id = rh.node_id
                                          AND rpt.record_type LIKE ''%team%''
                                          AND NVL (detail_node_id, 0) IN (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_parentNodeId||''')))
                                          AND rpt.pax_status =
                                                 NVL ('''||p_in_participantStatus||''',
                                                      rpt.pax_status)
                                          AND rpt.job_position =
                                                 NVL ('''||p_in_jobPosition||''',
                                                      rpt.job_position)
                                          AND (   ('''||p_in_departments||''' IS NULL)
                                               OR (    '''||p_in_departments||'''
                                                          IS NOT NULL
                                                   AND rpt.department IN (SELECT *
                                                                            FROM TABLE (
                                                                                    get_array_varchar (
                                                                                       '''||p_in_departments||''')))))
                                          AND (   ('''||p_in_promotionId||''' IS NULL)
                                               OR (    '''||p_in_promotionId||'''
                                                          IS NOT NULL
                                                   AND rpt.promotion_id IN (SELECT *
                                                                              FROM TABLE (
                                                                                      get_array_varchar (
                                                                                         '''||p_in_promotionId||''')))))
                                          AND NVL (p.promotion_status, '' '') =
                                                 NVL (
                                                    '''||p_in_promotionStatus||''',
                                                    NVL (p.promotion_status,
                                                         '' ''))
                                 GROUP BY sequence_num,
                                          rh.node_id,
                                          rh.node_name) rpc
                          WHERE rh.node_id = rpc.node_id(+))
                SELECT t.node_name,
                       t.node_id,
                       (  NVL (t.no_goal_selected, 0)
                        + NVL (t.level_1_selected, 0)
                        + NVL (t.level_2_selected, 0)
                        + NVL (t.level_3_selected, 0)
                        + NVL (t.level_4_selected, 0)
                        + NVL (t.level_5_selected, 0)
                        + NVL (t.level_6_selected, 0))
                          total_participant,
                       t.no_goal_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.no_goal_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          no_goal_sele_perc,
                       t.level_1_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.level_1_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          level_1_sele_perc,
                       t.level_2_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.level_2_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          level_2_sele_perc,
                       t.level_3_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.level_3_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          level_3_sele_perc,
                       t.level_4_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.level_4_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          level_4_sele_perc,
                       t.level_5_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.level_5_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          level_5_sele_perc,
                       t.level_6_selected,
                       CASE
                          WHEN (  NVL (t.no_goal_selected, 0)
                                + NVL (t.level_1_selected, 0)
                                + NVL (t.level_2_selected, 0)
                                + NVL (t.level_3_selected, 0)
                                + NVL (t.level_4_selected, 0)
                                + NVL (t.level_5_selected, 0)
                                + NVL (t.level_6_selected, 0)) > 0
                          THEN
                             ROUND (
                                  NVL (t.level_6_selected, 0)
                                / (  NVL (t.no_goal_selected, 0)
                                   + NVL (t.level_1_selected, 0)
                                   + NVL (t.level_2_selected, 0)
                                   + NVL (t.level_3_selected, 0)
                                   + NVL (t.level_4_selected, 0)
                                   + NVL (t.level_5_selected, 0)
                                   + NVL (t.level_6_selected, 0))
                                * 100,
                                2)
                          ELSE
                             0
                       END
                          level_6_sele_perc
                  FROM pivot_set PIVOT (SUM (sum_total_pax)
                                 FOR sequence_num
                                 IN  (0 AS no_goal_selected,
                                     1 AS level_1_selected,
                                     2 AS level_2_selected,
                                     3 AS level_3_selected,
                                     4 AS level_4_selected,
                                     5 AS level_5_selected,
                                     6 AS level_6_selected)) t)) rs
                    ORDER BY '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;
        
    OPEN p_out_data FOR 
   l_query;



        --getGQSelectionTabResTotals
        OPEN p_out_totals_data FOR
            SELECT TOTAL_PARTICIPANTS,
                   NO_GOAL_SELECTED,
                   LEVEL_1_SELECTED,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (LEVEL_1_SELECTED / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_1_SELE_PERC,
                   LEVEL_2_SELECTED,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (LEVEL_2_SELECTED / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_2_SELE_PERC,
                   LEVEL_3_SELECTED,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_3_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_3_SELE_PERC,
                   LEVEL_4_SELECTED,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_4_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_4_SELE_PERC,
                   LEVEL_5_SELECTED,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_5_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_5_SELE_PERC,
                   LEVEL_6_SELECTED,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_6_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_6_SELE_PERC
              FROM (SELECT NVL (SUM (TOTAL_PARTICIPANTS), 0) TOTAL_PARTICIPANTS,
                           NVL (SUM (NO_GOAL_SELECTED), 0) NO_GOAL_SELECTED,
                           NVL (SUM (LEVEL_1_SELECTED), 0) LEVEL_1_SELECTED,
                           NVL (SUM (LEVEL_2_SELECTED), 0) LEVEL_2_SELECTED,
                           NVL (SUM (LEVEL_3_SELECTED), 0) LEVEL_3_SELECTED,
                           NVL (SUM (LEVEL_4_SELECTED), 0) LEVEL_4_SELECTED,
                           NVL (SUM (LEVEL_5_SELECTED), 0) LEVEL_5_SELECTED,
                           NVL (SUM (LEVEL_6_SELECTED), 0) LEVEL_6_SELECTED
                      FROM (SELECT total_participant AS TOTAL_PARTICIPANTS,
                                   NVL (no_goal_selected, 0)
                                       AS NO_GOAL_SELECTED,
                                   NVL (level_1_selected, 0)
                                       AS LEVEL_1_SELECTED,
                                   NVL (level_2_selected, 0)
                                       AS LEVEL_2_SELECTED,
                                   NVL (level_3_selected, 0)
                                       AS LEVEL_3_SELECTED,
                                   NVL (level_4_selected, 0)
                                       AS LEVEL_4_SELECTED,
                                   NVL (level_5_selected, 0)
                                       AS LEVEL_5_SELECTED,
                                   NVL (level_6_selected, 0)
                                       AS LEVEL_6_SELECTED
                              FROM (WITH pivot_set
                                         AS (SELECT total_participants
                                                        AS sum_total_pax,
                                                    NVL (sequence_num, 0)
                                                        sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM (SELECT DISTINCT
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            rpt_goal_selection_summary rpt
                                                      WHERE     detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%node%'
                                                            AND NVL (
                                                                    header_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId))))
                                                    rh,
                                                    (SELECT total_participants,
                                                            gl.sequence_num,
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            promotion p,
                                                            promo_goalquest promoGoal,
                                                            rpt_goal_selection_summary rpt,
                                                            goalquest_goallevel gl
                                                      WHERE     rpt.goal_level =
                                                                    gl.goallevel_id(+)
                                                            AND rpt.promotion_id =
                                                                    promoGoal.promotion_id(+)
                                                            AND rpt.promotion_id =
                                                                    p.promotion_id(+)
                                                            AND detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%node%'
                                                            AND NVL (
                                                                    header_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId)))
                                                            AND rpt.pax_status =
                                                                    NVL (
                                                                        p_in_participantStatus,
                                                                        rpt.pax_status)
                                                            AND rpt.job_position =
                                                                    NVL (
                                                                        p_in_jobPosition,
                                                                        rpt.job_position)
                                                            AND (   (p_in_departments
                                                                         IS NULL)
                                                                 OR (    p_in_departments
                                                                             IS NOT NULL
                                                                     AND rpt.department IN (SELECT *
                                                                                              FROM TABLE (
                                                                                                       get_array_varchar (
                                                                                                           p_in_departments)))))
                                                            AND (   (p_in_promotionId
                                                                         IS NULL)
                                                                 OR (    p_in_promotionId
                                                                             IS NOT NULL
                                                                     AND rpt.promotion_id IN (SELECT *
                                                                                                FROM TABLE (
                                                                                                         get_array_varchar (
                                                                                                             p_in_promotionId)))))
                                                            AND NVL (
                                                                    p.promotion_status,
                                                                    ' ') =
                                                                    NVL (
                                                                        p_in_promotionStatus,
                                                                        NVL (
                                                                            p.promotion_status,
                                                                            ' ')))
                                                    rpc
                                              WHERE rh.node_id = rpc.node_id(+))
                                    SELECT t.node_name,
                                           t.node_id,
                                           (  NVL (t.no_goal_selected, 0)
                                            + NVL (t.level_1_selected, 0)
                                            + NVL (t.level_2_selected, 0)
                                            + NVL (t.level_3_selected, 0)
                                            + NVL (t.level_4_selected, 0)
                                            + NVL (t.level_5_selected, 0)
                                            + NVL (t.level_6_selected, 0))
                                               total_participant,
                                           t.no_goal_selected,
                                           t.level_1_selected,
                                           t.level_2_selected,
                                           t.level_3_selected,
                                           t.level_4_selected,
                                           t.level_5_selected,
                                           t.level_6_selected
                                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                                     FOR sequence_num
                                                     IN  (0 AS no_goal_selected,
                                                         1 AS level_1_selected,
                                                         2 AS level_2_selected,
                                                         3 AS level_3_selected,
                                                         4 AS level_4_selected,
                                                         5 AS level_5_selected,
                                                         6 AS level_6_selected)) t)
                            UNION ALL --added UNION ALL instead of UNION --09/03/2014 nagarajs Bug #55802
                            SELECT total_participant,
                                   NVL (no_goal_selected, 0),
                                   NVL (level_1_selected, 0),
                                   NVL (level_2_selected, 0),
                                   NVL (level_3_selected, 0),
                                   NVL (level_4_selected, 0),
                                   NVL (level_5_selected, 0),
                                   NVL (level_6_selected, 0)
                              FROM (WITH pivot_set
                                         AS (SELECT total_participants
                                                        AS sum_total_pax,
                                                    NVL (sequence_num, 0)
                                                        sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name || ' Team'
                                                        node_name
                                               FROM (SELECT DISTINCT
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            rpt_goal_selection_summary rpt
                                                      WHERE     detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%team%'
                                                            AND NVL (
                                                                    detail_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId))))
                                                    rh,
                                                    (SELECT total_participants,
                                                            gl.sequence_num,
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            promotion p,
                                                            promo_goalquest promoGoal,
                                                            rpt_goal_selection_summary rpt,
                                                            goalquest_goallevel gl
                                                      WHERE     rpt.goal_level =
                                                                    gl.goallevel_id(+)
                                                            AND rpt.promotion_id =
                                                                    promoGoal.promotion_id(+)
                                                            AND rpt.promotion_id =
                                                                    p.promotion_id(+)
                                                            AND detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%team%'
                                                            AND NVL (
                                                                    detail_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId)))
                                                            AND rpt.pax_status =
                                                                    NVL (
                                                                        p_in_participantStatus,
                                                                        rpt.pax_status)
                                                            AND rpt.job_position =
                                                                    NVL (
                                                                        p_in_jobPosition,
                                                                        rpt.job_position)
                                                            AND (   (p_in_departments
                                                                         IS NULL)
                                                                 OR (    p_in_departments
                                                                             IS NOT NULL
                                                                     AND rpt.department IN (SELECT *
                                                                                              FROM TABLE (
                                                                                                       get_array_varchar (
                                                                                                           p_in_departments)))))
                                                            AND (   (p_in_promotionId
                                                                         IS NULL)
                                                                 OR (    p_in_promotionId
                                                                             IS NOT NULL
                                                                     AND rpt.promotion_id IN (SELECT *
                                                                                                FROM TABLE (
                                                                                                         get_array_varchar (
                                                                                                             p_in_promotionId)))))
                                                            AND NVL (
                                                                    p.promotion_status,
                                                                    ' ') =
                                                                    NVL (
                                                                        p_in_promotionStatus,
                                                                        NVL (
                                                                            p.promotion_status,
                                                                            ' ')))
                                                    rpc
                                              WHERE rh.node_id = rpc.node_id(+))
                                    SELECT t.node_name,
                                           t.node_id,
                                           (  NVL (t.no_goal_selected, 0)
                                            + NVL (t.level_1_selected, 0)
                                            + NVL (t.level_2_selected, 0)
                                            + NVL (t.level_3_selected, 0)
                                            + NVL (t.level_4_selected, 0)
                                            + NVL (t.level_5_selected, 0)
                                            + NVL (t.level_6_selected, 0))
                                               total_participant,
                                           t.no_goal_selected,
                                           t.level_1_selected,
                                           t.level_2_selected,
                                           t.level_3_selected,
                                           t.level_4_selected,
                                           t.level_5_selected,
                                           t.level_6_selected
                                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                                     FOR sequence_num
                                                     IN  (0 AS no_goal_selected,
                                                         1 AS level_1_selected,
                                                         2 AS level_2_selected,
                                                         3 AS level_3_selected,
                                                         4 AS level_4_selected,
                                                         5 AS level_5_selected,
                                                         6 AS level_6_selected)) t)))
                   rs;



        --getGQSelectionResSize        
            SELECT COUNT (1) INTO p_out_size_data
              FROM (SELECT node_name AS NODE_NAME,
                           node_id AS NODE_ID,
                           total_participant AS TOTAL_PARTICIPANTS,
                           NVL (no_goal_selected, 0) AS NO_GOAL_SELECTED,
                           NVL (level_1_selected, 0) AS LEVEL_1_SELECTED,
                           NVL (level_1_sele_perc, 0)
                               AS LEVEL_1_SELECTED_PERCENT,
                           NVL (level_2_selected, 0) AS LEVEL_2_SELECTED,
                           NVL (level_2_sele_perc, 0)
                               AS LEVEL_2_SELECTED_PERCENT,
                           NVL (level_3_selected, 0) AS LEVEL_3_SELECTED,
                           NVL (level_3_sele_perc, 0)
                               AS LEVEL_3_SELECTED_PERCENT,
                           NVL (level_4_selected, 0) AS LEVEL_4_SELECTED,
                           NVL (level_4_sele_perc, 0)
                               AS LEVEL_4_SELECTED_PERCENT,
                           NVL (level_5_selected, 0) AS LEVEL_5_SELECTED,
                           NVL (level_5_sele_perc, 0)
                               AS LEVEL_5_SELECTED_PERCENT,
                           0 AS IS_LEAF
                      FROM (WITH pivot_set
                                 AS (SELECT total_participants AS sum_total_pax,
                                            NVL (sequence_num, 0) sequence_num,
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%node%'
                                                    AND NVL (header_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId))))
                                            rh,
                                            (SELECT total_participants,
                                                    gl.sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    promotion p,
                                                    promo_goalquest promoGoal,
                                                    rpt_goal_selection_summary rpt,
                                                    goalquest_goallevel gl
                                              WHERE     rpt.goal_level =
                                                            gl.goallevel_id(+)
                                                    AND rpt.promotion_id =
                                                            promoGoal.promotion_id(+)
                                                    AND rpt.promotion_id =
                                                            p.promotion_id(+)
                                                    AND detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%node%'
                                                    AND NVL (header_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId)))
                                                    AND rpt.pax_status =
                                                            NVL (
                                                                p_in_participantStatus,
                                                                rpt.pax_status)
                                                    AND rpt.job_position =
                                                            NVL (
                                                                p_in_jobPosition,
                                                                rpt.job_position)
                                                    AND (   (p_in_departments
                                                                 IS NULL)
                                                         OR (    p_in_departments
                                                                     IS NOT NULL
                                                             AND rpt.department IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_departments)))))
                                                    AND (   (p_in_promotionId
                                                                 IS NULL)
                                                         OR (    p_in_promotionId
                                                                     IS NOT NULL
                                                             AND rpt.promotion_id IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_promotionId)))))
                                                    AND NVL (
                                                            p.promotion_status,
                                                            ' ') =
                                                            NVL (
                                                                p_in_promotionStatus,
                                                                NVL (
                                                                    p.promotion_status,
                                                                    ' '))) rpc
                                      WHERE rh.node_id = rpc.node_id(+))
                            SELECT t.node_name,
                                   t.node_id,
                                   (  NVL (t.no_goal_selected, 0)
                                    + NVL (t.level_1_selected, 0)
                                    + NVL (t.level_2_selected, 0)
                                    + NVL (t.level_3_selected, 0))
                                       total_participant,
                                   t.no_goal_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.no_goal_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       no_goal_sele_perc,
                                   t.level_1_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_1_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_1_sele_perc,
                                   t.level_2_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_2_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_2_sele_perc,
                                   t.level_3_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_3_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_3_sele_perc,
                                   t.level_4_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)
                                             + NVL (t.level_4_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_4_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)
                                                  + NVL (t.level_4_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_4_sele_perc,
                                   t.level_5_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)
                                             + NVL (t.level_4_selected, 0)
                                             + NVL (t.level_5_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_5_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)
                                                  + NVL (t.level_4_selected, 0)
                                                  + NVL (t.level_5_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_5_sele_perc
                              FROM pivot_set PIVOT (SUM (sum_total_pax)
                                             FOR sequence_num
                                             IN  (0 AS no_goal_selected,
                                                 1 AS level_1_selected,
                                                 2 AS level_2_selected,
                                                 3 AS level_3_selected,
                                                 4 AS level_4_selected,
                                                 5 AS level_5_selected)) t)
                    UNION
                    SELECT node_name,
                           node_id,
                           total_participant,
                           NVL (no_goal_selected, 0),
                           NVL (level_1_selected, 0),
                           NVL (level_1_sele_perc, 0),
                           NVL (level_2_selected, 0),
                           NVL (level_2_sele_perc, 0),
                           NVL (level_3_selected, 0),
                           NVL (level_3_sele_perc, 0),
                           NVL (level_4_selected, 0),
                           NVL (level_4_sele_perc, 0),
                           NVL (level_5_selected, 0),
                           NVL (level_5_sele_perc, 0),
                           1 AS IS_LEAF
                      FROM (WITH pivot_set
                                 AS (SELECT total_participants AS sum_total_pax,
                                            NVL (sequence_num, 0) sequence_num,
                                            rh.node_id AS node_id,
                                            rh.node_name || ' Team' node_name
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%team%'
                                                    AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId))))
                                            rh,
                                            (SELECT total_participants,
                                                    gl.sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    promotion p,
                                                    promo_goalquest promoGoal,
                                                    rpt_goal_selection_summary rpt,
                                                    goalquest_goallevel gl
                                              WHERE     rpt.goal_level =
                                                            gl.goallevel_id(+)
                                                    AND rpt.promotion_id =
                                                            promoGoal.promotion_id(+)
                                                    AND rpt.promotion_id =
                                                            p.promotion_id(+)
                                                    AND detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%team%'
                                                    AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId)))
                                                    AND rpt.pax_status =
                                                            NVL (
                                                                p_in_participantStatus,
                                                                rpt.pax_status)
                                                    AND rpt.job_position =
                                                            NVL (
                                                                p_in_jobPosition,
                                                                rpt.job_position)
                                                    AND (   (p_in_departments
                                                                 IS NULL)
                                                         OR (    p_in_departments
                                                                     IS NOT NULL
                                                             AND rpt.department IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_departments)))))
                                                    AND (   (p_in_promotionId
                                                                 IS NULL)
                                                         OR (    p_in_promotionId
                                                                     IS NOT NULL
                                                             AND rpt.promotion_id IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_promotionId)))))
                                                    AND NVL (
                                                            p.promotion_status,
                                                            ' ') =
                                                            NVL (
                                                                p_in_promotionStatus,
                                                                NVL (
                                                                    p.promotion_status,
                                                                    ' '))) rpc
                                      WHERE rh.node_id = rpc.node_id(+))
                            SELECT t.node_name,
                                   t.node_id,
                                   (  NVL (t.no_goal_selected, 0)
                                    + NVL (t.level_1_selected, 0)
                                    + NVL (t.level_2_selected, 0)
                                    + NVL (t.level_3_selected, 0))
                                       total_participant,
                                   t.no_goal_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.no_goal_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       no_goal_sele_perc,
                                   t.level_1_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_1_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_1_sele_perc,
                                   t.level_2_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_2_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_2_sele_perc,
                                   t.level_3_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_3_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_3_sele_perc,
                                   t.level_4_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)
                                             + NVL (t.level_4_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_4_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)
                                                  + NVL (t.level_4_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_4_sele_perc,
                                   t.level_5_selected,
                                   CASE
                                       WHEN (  NVL (t.no_goal_selected, 0)
                                             + NVL (t.level_1_selected, 0)
                                             + NVL (t.level_2_selected, 0)
                                             + NVL (t.level_3_selected, 0)
                                             + NVL (t.level_4_selected, 0)
                                             + NVL (t.level_5_selected, 0)) > 0
                                       THEN
                                           ROUND (
                                                 NVL (t.level_5_selected, 0)
                                               / (  NVL (t.no_goal_selected, 0)
                                                  + NVL (t.level_1_selected, 0)
                                                  + NVL (t.level_2_selected, 0)
                                                  + NVL (t.level_3_selected, 0)
                                                  + NVL (t.level_4_selected, 0)
                                                  + NVL (t.level_5_selected, 0)),
                                               2)
                                       ELSE
                                           0
                                   END
                                       level_5_sele_perc
                              FROM pivot_set PIVOT (SUM (sum_total_pax)
                                             FOR sequence_num
                                             IN  (0 AS no_goal_selected,
                                                 1 AS level_1_selected,
                                                 2 AS level_2_selected,
                                                 3 AS level_3_selected,
                                                 4 AS level_4_selected,
                                                 5 AS level_5_selected)) t));

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
    END prc_getGQSelectionTabRes;



    PROCEDURE prc_getGQSelectionValidLvlNums (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQSelectionValidLvlNums' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT DISTINCT gl.SEQUENCE_NUM AS LEVEL_NUMBER
              FROM promotion p,
                   promo_goalquest promoGoal,
                   goalquest_goallevel gl
             WHERE     promoGoal.promotion_id = gl.promotion_id
                   AND gl.promotion_id = p.promotion_id
                   AND p.promotion_type = 'goalquest'
                   AND (   (p_in_promotionId IS NULL)
                        OR (p.promotion_id IN (SELECT *
                                                 FROM TABLE (
                                                          get_array_varchar (
                                                              p_in_promotionId)))))
                   AND p.promotion_status =
                           NVL (p_in_promotionStatus, p.promotion_status);

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
    END prc_getGQSelectionValidLvlNums;



    PROCEDURE prc_getGQSelectionDetailRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQSelectionDetailRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
                        FROM (SELECT    gsd.last_name
                                     || '' , ''
                                     || gsd.first_name
                                     || '' ''
                                     || gsd.middle_init
                                         AS PAX_NAME,
                                     hier.node_name AS NODE_NAME,
                                     CASE
                                         WHEN gl.sequence_num IS NULL THEN NULL
                                         ELSE ''Level '' || gl.sequence_num
                                     END
                                         LEVEL_NUMBER,
                                     gsd.promotion_name AS PROMO_NAME
                                FROM promo_goalquest promoGoal,
                                     promotion p,
                                     RPT_GOAL_SELECTION_DETAIL gsd
                                     LEFT OUTER JOIN RPT_HIERARCHY hier
                                         ON gsd.node_id = hier.node_id
                                     LEFT OUTER JOIN PROMOTION promo
                                         ON gsd.promotion_id = promo.promotion_id
                                     LEFT OUTER JOIN GOALQUEST_PAXGOAL pg
                                         ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                                     LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                         ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
                               WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array_varchar (
                                                                                 '''||p_in_parentNodeId||''')))
                                     AND (   gsd.promotion_id IN (SELECT *
                                                                    FROM TABLE (
                                                                             CAST (
                                                                                 get_array_varchar (
                                                                                     '''||p_in_promotionId||''') AS ARRAY_VARCHAR)))
                                          OR '''||p_in_promotionId||''' IS NULL)
                                     AND gsd.promotion_id =
                                             promoGoal.promotion_id
                                     AND gsd.promotion_id = p.promotion_id
                                     AND NVL (p.promotion_status, '' '') =
                                             NVL ('''||p_in_promotionStatus||''',
                                                  NVL (p.promotion_status, '' ''))
                                     AND gsd.user_status =
                                             NVL ('''||p_in_participantStatus||''',
                                                  gsd.user_status)
                                     AND NVL (gsd.job_position, ''job'') =
                                             NVL ('''||p_in_jobPosition||''',
                                                  NVL (gsd.job_position, ''job''))
                                     AND (   ('''||p_in_departments||''' IS NULL)
                                          OR (gsd.department IN (SELECT *
                                                                   FROM TABLE (
                                                                            get_array_varchar (
                                                                                '''||p_in_departments||'''))))))
                             rs
                    ORDER BY  '|| v_sortCol ||'
    ) WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;
        
    OPEN p_out_data FOR 
   l_query;



        --getGQSelectionDetailResSize        
            SELECT COUNT (1) INTO p_out_size_data
              FROM promo_goalquest promoGoal,
                   promotion p,
                   RPT_GOAL_SELECTION_DETAIL gsd
                   LEFT OUTER JOIN RPT_HIERARCHY hier
                       ON gsd.node_id = hier.node_id
                   LEFT OUTER JOIN PROMOTION promo
                       ON gsd.promotion_id = promo.promotion_id
                   RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                       ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                   LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                       ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
             WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                  FROM TABLE (
                                                           get_array_varchar (
                                                               p_in_parentNodeId)))
                   AND (   gsd.promotion_id IN (SELECT *
                                                  FROM TABLE (
                                                           CAST (
                                                               get_array_varchar (
                                                                   p_in_promotionId) AS ARRAY_VARCHAR)))
                        OR p_in_promotionId IS NULL)
                   AND gsd.promotion_id = promoGoal.promotion_id
                   AND gsd.promotion_id = p.promotion_id
                   AND NVL (p.promotion_status, ' ') =
                           NVL (p_in_promotionStatus,
                                NVL (p.promotion_status, ' '))
                   AND gsd.user_status =
                           NVL (p_in_participantStatus, gsd.user_status)
                   AND NVL (gsd.job_position, 'job') =
                           NVL (p_in_jobPosition,
                                NVL (gsd.job_position, 'job'))
                   AND (   (p_in_departments IS NULL)
                        OR (gsd.department IN (SELECT *
                                                 FROM TABLE (
                                                          get_array_varchar (
                                                              p_in_departments)))));

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
    END prc_getGQSelectionDetailRes;


    PROCEDURE prc_getGQSelectionTotalsChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQSelectionTotalsChart' ;
        v_stage                   VARCHAR2 (500);
/******************************************************************************
  NAME:       prc_getGQSelectionTotalsChart
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Raju G               01/22/2015     Bug Fix 59194   - GQ Summary and Bar Chart results are not matching      
  ******************************************************************************/

    BEGIN
        OPEN p_out_data FOR
            SELECT NO_GOAL_SELECTED,
                   LEVEL_1_SELECTED,
                   LEVEL_2_SELECTED,
                   LEVEL_3_SELECTED,
                   LEVEL_4_SELECTED,
                   LEVEL_5_SELECTED,
                   LEVEL_6_SELECTED
              FROM (SELECT NVL (SUM (TOTAL_PARTICIPANTS), 0) TOTAL_PARTICIPANTS,
                           NVL (SUM (NO_GOAL_SELECTED), 0) NO_GOAL_SELECTED,
                           NVL (SUM (LEVEL_1_SELECTED), 0) LEVEL_1_SELECTED,
                           NVL (SUM (LEVEL_2_SELECTED), 0) LEVEL_2_SELECTED,
                           NVL (SUM (LEVEL_3_SELECTED), 0) LEVEL_3_SELECTED,
                           NVL (SUM (LEVEL_4_SELECTED), 0) LEVEL_4_SELECTED,
                           NVL (SUM (LEVEL_5_SELECTED), 0) LEVEL_5_SELECTED,
                           NVL (SUM (LEVEL_6_SELECTED), 0) LEVEL_6_SELECTED
                      FROM (SELECT total_participant AS TOTAL_PARTICIPANTS,
                                   NVL (no_goal_selected, 0)
                                       AS NO_GOAL_SELECTED,
                                   NVL (level_1_selected, 0)
                                       AS LEVEL_1_SELECTED,
                                   NVL (level_2_selected, 0)
                                       AS LEVEL_2_SELECTED,
                                   NVL (level_3_selected, 0)
                                       AS LEVEL_3_SELECTED,
                                   NVL (level_4_selected, 0)
                                       AS LEVEL_4_SELECTED,
                                   NVL (level_5_selected, 0)
                                       AS LEVEL_5_SELECTED,
                                   NVL (level_6_selected, 0)
                                       AS LEVEL_6_SELECTED
                              FROM (WITH pivot_set
                                         AS (SELECT total_participants
                                                        AS sum_total_pax,
                                                    NVL (sequence_num, 0)
                                                        sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM (SELECT DISTINCT
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            rpt_goal_selection_summary rpt
                                                      WHERE     detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%node%'
                                                            AND NVL (
                                                                    header_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId))))
                                                    rh,
                                                    (SELECT total_participants,
                                                            gl.sequence_num,
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            promotion p,
                                                            promo_goalquest promoGoal,
                                                            rpt_goal_selection_summary rpt,
                                                            goalquest_goallevel gl
                                                      WHERE     rpt.goal_level =
                                                                    gl.goallevel_id(+)
                                                            AND rpt.promotion_id =
                                                                    promoGoal.promotion_id(+)
                                                            AND rpt.promotion_id =
                                                                    p.promotion_id(+)
                                                            AND detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%node%'
                                                            AND NVL (
                                                                    header_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId)))
                                                            AND rpt.pax_status =
                                                                    NVL (
                                                                        p_in_participantStatus,
                                                                        rpt.pax_status)
                                                            AND rpt.job_position =
                                                                    NVL (
                                                                        p_in_jobPosition,
                                                                        rpt.job_position)
                                                            AND (   (p_in_departments
                                                                         IS NULL)
                                                                 OR (    p_in_departments
                                                                             IS NOT NULL
                                                                     AND rpt.department IN (SELECT *
                                                                                              FROM TABLE (
                                                                                                       get_array_varchar (
                                                                                                           p_in_departments)))))
                                                            AND (   (p_in_promotionId
                                                                         IS NULL)
                                                                 OR (    p_in_promotionId
                                                                             IS NOT NULL
                                                                     AND rpt.promotion_id IN (SELECT *
                                                                                                FROM TABLE (
                                                                                                         get_array_varchar (
                                                                                                             p_in_promotionId)))))
                                                            AND NVL (
                                                                    p.promotion_status,
                                                                    ' ') =
                                                                    NVL (
                                                                        p_in_promotionStatus,
                                                                        NVL (
                                                                            p.promotion_status,
                                                                            ' ')))
                                                    rpc
                                              WHERE rh.node_id = rpc.node_id(+))
                                    SELECT t.node_name,
                                           t.node_id,
                                           (  NVL (t.no_goal_selected, 0)
                                            + NVL (t.level_1_selected, 0)
                                            + NVL (t.level_2_selected, 0)
                                            + NVL (t.level_3_selected, 0)
                                            + NVL (t.level_4_selected, 0)
                                            + NVL (t.level_5_selected, 0)
                                            + NVL (t.level_6_selected, 0))
                                               total_participant,
                                           t.no_goal_selected,
                                           t.level_1_selected,
                                           t.level_2_selected,
                                           t.level_3_selected,
                                           t.level_4_selected,
                                           t.level_5_selected,
                                           t.level_6_selected
                                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                                     FOR sequence_num
                                                     IN  (0 AS no_goal_selected,
                                                         1 AS level_1_selected,
                                                         2 AS level_2_selected,
                                                         3 AS level_3_selected,
                                                         4 AS level_4_selected,
                                                         5 AS level_5_selected,
                                                         6 AS level_6_selected)) t)
                             WHERE p_in_nodeAndBelow = 'true'
                            UNION ALL    --01/22/2015     
                            SELECT total_participant AS TOTAL_PARTICIPANTS,
                                   NVL (no_goal_selected, 0)
                                       AS NO_GOAL_SELECTED,
                                   NVL (level_1_selected, 0)
                                       AS LEVEL_1_SELECTED,
                                   NVL (level_2_selected, 0)
                                       AS LEVEL_2_SELECTED,
                                   NVL (level_3_selected, 0)
                                       AS LEVEL_3_SELECTED,
                                   NVL (level_4_selected, 0)
                                       AS LEVEL_4_SELECTED,
                                   NVL (level_5_selected, 0)
                                       AS LEVEL_5_SELECTED,
                                   NVL (level_6_selected, 0)
                                       AS LEVEL_6_SELECTED
                              FROM (WITH pivot_set
                                         AS (SELECT total_participants
                                                        AS sum_total_pax,
                                                    NVL (sequence_num, 0)
                                                        sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name || ' Team'
                                                        node_name
                                               FROM (SELECT DISTINCT
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            rpt_goal_selection_summary rpt
                                                      WHERE     detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%team%'
                                                            AND NVL (
                                                                    detail_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId))))
                                                    rh,
                                                    (SELECT total_participants,
                                                            gl.sequence_num,
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            promotion p,
                                                            promo_goalquest promoGoal,
                                                            rpt_goal_selection_summary rpt,
                                                            goalquest_goallevel gl
                                                      WHERE     rpt.goal_level =
                                                                    gl.goallevel_id(+)
                                                            AND rpt.promotion_id =
                                                                    promoGoal.promotion_id(+)
                                                            AND rpt.promotion_id =
                                                                    p.promotion_id(+)
                                                            AND detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%team%'
                                                            AND NVL (
                                                                    detail_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId)))
                                                            AND rpt.pax_status =
                                                                    NVL (
                                                                        p_in_participantStatus,
                                                                        rpt.pax_status)
                                                            AND rpt.job_position =
                                                                    NVL (
                                                                        p_in_jobPosition,
                                                                        rpt.job_position)
                                                            AND (   (p_in_departments
                                                                         IS NULL)
                                                                 OR (    p_in_departments
                                                                             IS NOT NULL
                                                                     AND rpt.department IN (SELECT *
                                                                                              FROM TABLE (
                                                                                                       get_array_varchar (
                                                                                                           p_in_departments)))))
                                                            AND (   (p_in_promotionId
                                                                         IS NULL)
                                                                 OR (    p_in_promotionId
                                                                             IS NOT NULL
                                                                     AND rpt.promotion_id IN (SELECT *
                                                                                                FROM TABLE (
                                                                                                         get_array_varchar (
                                                                                                             p_in_promotionId)))))
                                                            AND NVL (
                                                                    p.promotion_status,
                                                                    ' ') =
                                                                    NVL (
                                                                        p_in_promotionStatus,
                                                                        NVL (
                                                                            p.promotion_status,
                                                                            ' ')))
                                                    rpc
                                              WHERE rh.node_id = rpc.node_id(+))
                                    SELECT t.node_name,
                                           t.node_id,
                                           (  NVL (t.no_goal_selected, 0)
                                            + NVL (t.level_1_selected, 0)
                                            + NVL (t.level_2_selected, 0)
                                            + NVL (t.level_3_selected, 0)
                                            + NVL (t.level_4_selected, 0)
                                            + NVL (t.level_5_selected, 0)
                                            + NVL (t.level_6_selected, 0))
                                               total_participant,
                                           t.no_goal_selected,
                                           t.level_1_selected,
                                           t.level_2_selected,
                                           t.level_3_selected,
                                           t.level_4_selected,
                                           t.level_5_selected,
                                           t.level_6_selected
                                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                                     FOR sequence_num
                                                     IN  (0 AS no_goal_selected,
                                                         1 AS level_1_selected,
                                                         2 AS level_2_selected,
                                                         3 AS level_3_selected,
                                                         4 AS level_4_selected,
                                                         5 AS level_5_selected,
                                                         6 AS level_6_selected)) t)))
                   rs;

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
    END prc_getGQSelectionTotalsChart;



    PROCEDURE prc_getGQSelectionByOrgChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQSelectionByOrgChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT node_name AS NODE_NAME,
                   node_id AS NODE_ID,
                   NVL (no_goal_selected, 0) AS NO_GOAL_SELECTED,
                   NVL (level_1_selected, 0) AS LEVEL_1_SELECTED,
                   NVL (level_2_selected, 0) AS LEVEL_2_SELECTED,
                   NVL (level_3_selected, 0) AS LEVEL_3_SELECTED,
                   NVL (level_4_selected, 0) AS LEVEL_4_SELECTED,
                   NVL (level_5_selected, 0) AS LEVEL_5_SELECTED,
                   NVL (level_6_selected, 0) AS LEVEL_6_SELECTED
              FROM (WITH pivot_set
                         AS (SELECT total_participants AS sum_total_pax,
                                    NVL (sequence_num, 0) sequence_num,
                                    rh.node_id AS node_id,
                                    rh.node_name node_name
                               FROM (SELECT DISTINCT
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM rpt_hierarchy rh,
                                            rpt_goal_selection_summary rpt
                                      WHERE     detail_node_id = rh.node_id
                                            AND rpt.record_type LIKE '%node%'
                                            AND NVL (header_node_id, 0) IN (SELECT *
                                                                              FROM TABLE (
                                                                                       get_array_varchar (
                                                                                           p_in_parentNodeId))))
                                    rh,
                                    (  SELECT SUM (NVL (total_participants, 0))
                                                  total_participants,
                                              gl.sequence_num,
                                              rh.node_id AS node_id,
                                              rh.node_name node_name
                                         FROM rpt_hierarchy rh,
                                              promotion p,
                                              promo_goalquest promoGoal,
                                              rpt_goal_selection_summary rpt,
                                              goalquest_goallevel gl
                                        WHERE     rpt.goal_level =
                                                      gl.goallevel_id(+)
                                              AND rpt.promotion_id =
                                                      promoGoal.promotion_id(+)
                                              AND rpt.promotion_id =
                                                      p.promotion_id
                                              AND detail_node_id = rh.node_id
                                              AND rpt.record_type LIKE '%node%'
                                              AND NVL (header_node_id, 0) IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array_varchar (
                                                                                             p_in_parentNodeId)))
                                              AND rpt.pax_status =
                                                      NVL (
                                                          p_in_participantStatus,
                                                          rpt.pax_status)
                                              AND rpt.job_position =
                                                      NVL (p_in_jobPosition,
                                                           rpt.job_position)
                                              AND (   (p_in_departments IS NULL)
                                                   OR (    p_in_departments
                                                               IS NOT NULL
                                                       AND rpt.department IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array_varchar (
                                                                                             p_in_departments)))))
                                              AND (   (p_in_promotionId IS NULL)
                                                   OR (    p_in_promotionId
                                                               IS NOT NULL
                                                       AND rpt.promotion_id IN (SELECT *
                                                                                  FROM TABLE (
                                                                                           get_array_varchar (
                                                                                               p_in_promotionId)))))
                                              AND NVL (p.promotion_status, ' ') =
                                                      NVL (
                                                          p_in_promotionStatus,
                                                          NVL (
                                                              p.promotion_status,
                                                              ' '))
                                     GROUP BY sequence_num,
                                              rh.node_id,
                                              rh.node_name) rpc
                              WHERE rh.node_id = rpc.node_id(+))
                    SELECT t.node_name,
                           t.node_id,
                           t.no_goal_selected,
                           t.level_1_selected,
                           t.level_2_selected,
                           t.level_3_selected,
                           t.level_4_selected,
                           t.level_5_selected,
                           t.level_6_selected
                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                     FOR sequence_num
                                     IN  (0 AS no_goal_selected,
                                         1 AS level_1_selected,
                                         2 AS level_2_selected,
                                         3 AS level_3_selected,
                                         4 AS level_4_selected,
                                         5 AS level_5_selected,
                                         6 AS level_6_selected)) t)
             WHERE p_in_nodeAndBelow = 'true'
            UNION
            SELECT node_name AS NODE_NAME,
                   node_id AS NODE_ID,
                   NVL (no_goal_selected, 0) AS NO_GOAL_SELECTED,
                   NVL (level_1_selected, 0) AS LEVEL_1_SELECTED,
                   NVL (level_2_selected, 0) AS LEVEL_2_SELECTED,
                   NVL (level_3_selected, 0) AS LEVEL_3_SELECTED,
                   NVL (level_4_selected, 0) AS LEVEL_4_SELECTED,
                   NVL (level_5_selected, 0) AS LEVEL_5_SELECTED,
                   NVL (level_6_selected, 0) AS LEVEL_6_SELECTED
              FROM (WITH pivot_set
                         AS (SELECT total_participants AS sum_total_pax,
                                    NVL (sequence_num, 0) sequence_num,
                                    rh.node_id AS node_id,
                                    rh.node_name || ' Team' node_name
                               FROM (SELECT DISTINCT
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM rpt_hierarchy rh,
                                            rpt_goal_selection_summary rpt
                                      WHERE     detail_node_id = rh.node_id
                                            AND rpt.record_type LIKE '%team%'
                                            AND NVL (detail_node_id, 0) IN (SELECT *
                                                                              FROM TABLE (
                                                                                       get_array_varchar (
                                                                                           p_in_parentNodeId))))
                                    rh,
                                    (  SELECT SUM (NVL (total_participants, 0))
                                                  total_participants,
                                              gl.sequence_num,
                                              rh.node_id AS node_id,
                                              rh.node_name node_name
                                         FROM rpt_hierarchy rh,
                                              promotion p,
                                              promo_goalquest promoGoal,
                                              rpt_goal_selection_summary rpt,
                                              goalquest_goallevel gl
                                        WHERE     rpt.goal_level =
                                                      gl.goallevel_id(+)
                                              AND rpt.promotion_id =
                                                      promoGoal.promotion_id(+)
                                              AND rpt.promotion_id =
                                                      p.promotion_id(+)
                                              AND detail_node_id = rh.node_id
                                              AND rpt.record_type LIKE '%team%'
                                              AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array_varchar (
                                                                                             p_in_parentNodeId)))
                                              AND rpt.pax_status =
                                                      NVL (
                                                          p_in_participantStatus,
                                                          rpt.pax_status)
                                              AND rpt.job_position =
                                                      NVL (p_in_jobPosition,
                                                           rpt.job_position)
                                              AND (   (p_in_departments IS NULL)
                                                   OR (    p_in_departments
                                                               IS NOT NULL
                                                       AND rpt.department IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array_varchar (
                                                                                             p_in_departments)))))
                                              AND (   (p_in_promotionId IS NULL)
                                                   OR (    p_in_promotionId
                                                               IS NOT NULL
                                                       AND rpt.promotion_id IN (SELECT *
                                                                                  FROM TABLE (
                                                                                           get_array_varchar (
                                                                                               p_in_promotionId)))))
                                              AND NVL (p.promotion_status, ' ') =
                                                      NVL (
                                                          p_in_promotionStatus,
                                                          NVL (
                                                              p.promotion_status,
                                                              ' '))
                                     GROUP BY sequence_num,
                                              rh.node_id,
                                              rh.node_name) rpc
                              WHERE rh.node_id = rpc.node_id(+))
                    SELECT t.node_name,
                           t.node_id,
                           (  NVL (t.no_goal_selected, 0)
                            + NVL (t.level_1_selected, 0)
                            + NVL (t.level_2_selected, 0)
                            + NVL (t.level_3_selected, 0)
                            + NVL (t.level_4_selected, 0)
                            + NVL (t.level_5_selected, 0)
                            + NVL (t.level_6_selected, 0))
                               total_participant,
                           t.no_goal_selected,
                           t.level_1_selected,
                           t.level_2_selected,
                           t.level_3_selected,
                           t.level_4_selected,
                           t.level_5_selected,
                           t.level_6_selected
                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                     FOR sequence_num
                                     IN  (0 AS no_goal_selected,
                                         1 AS level_1_selected,
                                         2 AS level_2_selected,
                                         3 AS level_3_selected,
                                         4 AS level_4_selected,
                                         5 AS level_5_selected,
                                         6 AS level_6_selected)) t);

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
    END prc_getGQSelectionByOrgChart;

/******************************************************************************
  NAME:       prc_getGQAchievementTabRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J               08/29/2014     Bug Fix 56150  - Fixed Syntax error of invalid table name      
 Suresh J               10/20/2014     Bug 57491  -Total Participants count is not matching after drilldown                                
  ******************************************************************************/


    PROCEDURE prc_getGQAchievementTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQAchievementTabRes' ;
        v_stage                   VARCHAR2 (500);        
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
 
  BEGIN
 l_query  := 'SELECT *
              FROM 
              ( ';      --08/29/2014
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
  FROM (  SELECT node_name AS NODE_NAME,
                 node_id AS NODE_ID,
                 SUM (total_participants) AS total_participants,
                 SUM (total_selected) AS TOTAL_SELECTED,
                 SUM (NVL (level_1_total_selected, 0)) AS LEVEL_1_SELECTED_CNT,
                 SUM (NVL (level_1_nbr_achieved, 0)) AS LEVEL_1_ACHIEVED_CNT,
                 CASE
                    WHEN SUM (NVL (level_1_total_selected, 0)) = 0
                    THEN 0
                    ELSE NVL (ROUND ((  SUM (level_1_nbr_achieved)/ SUM (level_1_total_selected)) * 100,2),0)
                 END AS LEVEL_1_ACHIEVED_PERCENT,
                 SUM (NVL (level_1_awards, 0)) AS LEVEL_1_AWARD_CNT,
                 SUM (NVL (level_2_total_selected, 0)) AS LEVEL_2_SELECTED_CNT,
                 SUM (NVL (level_2_nbr_achieved, 0)) AS LEVEL_2_ACHIEVED_CNT,
                 CASE
                    WHEN SUM (NVL (level_2_total_selected, 0)) = 0
                    THEN 0
                    ELSE NVL (ROUND ((  SUM (level_2_nbr_achieved)/ SUM (level_2_total_selected))* 100,2),0) END  AS LEVEL_2_ACHIEVED_PERCENT,
                 SUM (NVL (level_2_awards, 0)) AS LEVEL_2_AWARD_CNT,
                 SUM (NVL (level_3_total_selected, 0)) AS LEVEL_3_SELECTED_CNT,
                 SUM (NVL (level_3_nbr_achieved, 0)) AS LEVEL_3_ACHIEVED_CNT,
                 CASE
                    WHEN SUM (NVL (level_3_total_selected, 0)) = 0
                    THEN
                       0
                    ELSE NVL (ROUND ((  SUM (level_3_nbr_achieved)/ SUM (level_3_total_selected)) * 100,2),
                          0) END AS LEVEL_3_ACHIEVED_PERCENT,
                 SUM (NVL (level_3_awards, 0)) AS LEVEL_3_AWARD_CNT,
                 SUM (NVL (level_4_total_selected, 0)) AS LEVEL_4_SELECTED_CNT,
                 SUM (NVL (level_4_nbr_achieved, 0)) AS LEVEL_4_ACHIEVED_CNT,
                 CASE
                    WHEN SUM (NVL (level_4_total_selected, 0)) = 0
                    THEN 0
                    ELSE NVL (ROUND ((  SUM (level_4_nbr_achieved) / SUM (level_4_total_selected))* 100,2),0)
                 END AS LEVEL_4_ACHIEVED_PERCENT,
                 SUM (NVL (level_4_awards, 0)) AS LEVEL_4_AWARD_CNT,
                 SUM (NVL (level_5_total_selected, 0)) AS LEVEL_5_SELECTED_CNT,
                 SUM (NVL (level_5_nbr_achieved, 0)) AS LEVEL_5_ACHIEVED_CNT,
                 CASE
                    WHEN SUM (NVL (level_5_total_selected, 0)) = 0
                    THEN
                       0
                    ELSE NVL (ROUND ((  SUM (level_5_nbr_achieved)/ SUM (level_5_total_selected)) * 100,2),0)
                 END AS LEVEL_5_ACHIEVED_PERCENT,
                 SUM (NVL (level_5_awards, 0)) AS LEVEL_5_AWARD_CNT,
                 SUM (NVL (level_6_total_selected, 0)) AS LEVEL_6_SELECTED_CNT,
                 SUM (NVL (level_6_nbr_achieved, 0)) AS LEVEL_6_ACHIEVED_CNT,
                 CASE
                    WHEN SUM (NVL (level_6_total_selected, 0)) = 0
                    THEN 0
                    ELSE NVL (ROUND ((  SUM (level_6_nbr_achieved)/ SUM (level_6_total_selected))* 100,2),0)
                 END AS LEVEL_6_ACHIEVED_PERCENT,
                 SUM (NVL (level_6_awards, 0)) AS LEVEL_6_AWARD_CNT,
                 SUM (NVL (BASE_QUANTITY, 0)) AS BASE_QUANTITY,
                 SUM (NVL (AMT_TO_ACHIEVE, 0)) AS AMT_TO_ACHIEVE,
                 SUM (NVL (CURRENT_VALUE, 0)) AS CURRENT_VALUE,
                 CASE
                    WHEN SUM (NVL (AMT_TO_ACHIEVE, 0)) = 0
                    THEN 0
                    ELSE ROUND ((  SUM (NVL (CURRENT_VALUE, 0))/ SUM (AMT_TO_ACHIEVE))* 100,2)
                 END AS PERCENT_ACHIEVED
            FROM (SELECT node_name AS NODE_NAME,
                         node_id AS NODE_ID,
                         total_participants,
                         total_selected AS TOTAL_SELECTED,
                         NVL (level_1_total_selected, 0)
                            AS level_1_total_selected,
                         NVL (level_1_nbr_achieved, 0) AS level_1_nbr_achieved,
                         NVL (level_1_awards, 0) AS level_1_awards,
                         NVL (level_2_total_selected, 0)
                            AS level_2_total_selected,
                         NVL (level_2_nbr_achieved, 0) AS level_2_nbr_achieved,
                         NVL (level_2_awards, 0) AS level_2_awards,
                         NVL (level_3_total_selected, 0)
                            AS level_3_total_selected,
                         NVL (level_3_nbr_achieved, 0) AS level_3_nbr_achieved,
                         NVL (level_3_awards, 0) AS level_3_awards,
                         NVL (level_4_total_selected, 0)
                            AS level_4_total_selected,
                         NVL (level_4_nbr_achieved, 0) AS level_4_nbr_achieved,
                         NVL (level_4_awards, 0) AS level_4_awards,
                         NVL (level_5_total_selected, 0)
                            AS level_5_total_selected,
                         NVL (level_5_nbr_achieved, 0) AS level_5_nbr_achieved,
                         NVL (level_5_awards, 0) AS level_5_awards,
                         NVL (level_6_total_selected, 0)
                            AS level_6_total_selected,
                         NVL (level_6_nbr_achieved, 0) AS level_6_nbr_achieved,
                         NVL (level_6_awards, 0) AS level_6_awards,
                         NVL (base, 0) AS BASE_QUANTITY,
                         NVL (goal, 0) AS AMT_TO_ACHIEVE,
                         NVL (actual_result, 0) AS CURRENT_VALUE,
                         0 AS IS_LEAF
                    FROM (WITH pivot_set
                               AS (SELECT total_selected,
                                          total_participants,
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
                                                  rpt_goal_selection_summary rpt
                                            WHERE     detail_node_id =
                                                         rh.node_id
                                                  AND rpt.record_type LIKE
                                                         ''%node%''
                                                  AND NVL (header_node_id, 0) IN (SELECT *
                                                                                    FROM TABLE (
                                                                                            get_array_varchar (
                                                                                               '''||p_in_parentNodeId||'''))))
                                          rh,
                                          (  SELECT SUM (total_selected)
                                                       total_selected,
                                                    SUM (total_participants)
                                                       total_participants,
                                                    SUM (nbr_goal_achieved)
                                                       nbr_achieved,
                                                    SUM (calculated_payout)
                                                       AS awards,
                                                    gl.sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name,
                                                    SUM (rpt.base_quantity) base,
                                                    SUM (rpt.goal) goal,
                                                    SUM (rpt.actual_result)
                                                       actual_result
                                               FROM rpt_hierarchy rh,
                                                    promotion p,
                                                    promo_goalquest promoGoal,
                                                    rpt_goal_selection_summary rpt,
                                                    goalquest_goallevel gl
                                              WHERE     rpt.goal_level =
                                                           gl.goallevel_id(+)
                                                    AND rpt.promotion_id =
                                                           promoGoal.promotion_id(+)
                                                    AND rpt.promotion_id =
                                                           p.promotion_id(+)
                                                    AND detail_node_id =
                                                           rh.node_id
                                                    AND rpt.record_type LIKE
                                                           ''%node%''
                                                    AND NVL (header_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                              get_array_varchar (
                                                                                                 '''||p_in_parentNodeId||''')))
                                                    AND rpt.pax_status =
                                                           NVL (
                                                              '''||p_in_participantStatus||''',
                                                              rpt.pax_status)
                                                    AND rpt.job_position =
                                                           NVL ('''||p_in_jobPosition||''',
                                                                rpt.job_position)
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
                                                    AND NVL (p.promotion_status,
                                                             '' '') =
                                                           NVL (
                                                              '''||p_in_promotionStatus||''',
                                                              NVL (
                                                                 p.promotion_status,
                                                                 '' ''))
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
                                  + NVL (level_5_total_selected, 0))
                                    total_selected,
--                                 total_participants,     10/20/2014
                                 (NVL (no_goal_total_participants,0) +                 --10/20/2014
                                 NVL(level_1_total_participants,0)+
                                 NVL(level_2_total_participants,0)+
                                 NVL(level_3_total_participants,0)+
                                 NVL(level_4_total_participants,0)+
                                 NVL(level_5_total_participants,0)) total_participants,
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
                                                 SUM (total_participants) total_participants,   --10/20/2014
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
                                               6 AS level_6)) t)
                  UNION
                  SELECT node_name || '' Team'' AS NODE_NAME,
                         node_id AS NODE_ID,
                         total_participants,
                         total_selected AS TOTAL_SELECTED,
                         NVL (level_1_total_selected, 0)
                            AS LEVEL_1_SELECTED_CNT,
                         NVL (level_1_nbr_achieved, 0) AS LEVEL_1_ACHIEVED_CNT,
                         NVL (level_1_awards, 0) AS LEVEL_1_AWARD_CNT,
                         NVL (level_2_total_selected, 0)
                            AS LEVEL_2_SELECTED_CNT,
                         NVL (level_2_nbr_achieved, 0) AS LEVEL_2_ACHIEVED_CNT,
                         NVL (level_2_awards, 0) AS LEVEL_2_AWARD_CNT,
                         NVL (level_3_total_selected, 0)
                            AS LEVEL_3_SELECTED_CNT,
                         NVL (level_3_nbr_achieved, 0) AS LEVEL_3_ACHIEVED_CNT,
                         NVL (level_3_awards, 0) AS LEVEL_3_AWARD_CNT,
                         NVL (level_4_total_selected, 0)
                            AS LEVEL_4_SELECTED_CNT,
                         NVL (level_4_nbr_achieved, 0) AS LEVEL_4_ACHIEVED_CNT,
                         NVL (level_4_awards, 0) AS LEVEL_4_AWARD_CNT,
                         NVL (level_5_total_selected, 0)
                            AS LEVEL_5_SELECTED_CNT,
                         NVL (level_5_nbr_achieved, 0) AS LEVEL_5_ACHIEVED_CNT,
                         NVL (level_5_awards, 0) AS LEVEL_5_AWARD_CNT,
                         NVL (level_6_total_selected, 0)
                            AS LEVEL_6_SELECTED_CNT,
                         NVL (level_6_nbr_achieved, 0) AS LEVEL_6_ACHIEVED_CNT,
                         NVL (level_6_awards, 0) AS LEVEL_6_AWARD_CNT,
                         NVL (base, 0) AS BASE_QUANTITY,
                         NVL (goal, 0) AS AMT_TO_ACHIEVE,
                         NVL (actual_result, 0) AS CURRENT_VALUE,
                         1 AS IS_LEAF
                    FROM (WITH pivot_set
                               AS (SELECT total_selected,
                                          total_participants,
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
                                                  rpt_goal_selection_summary rpt
                                            WHERE     detail_node_id =
                                                         rh.node_id
                                                  AND rpt.record_type LIKE
                                                         ''%team%''
                                                  AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                    FROM TABLE (
                                                                                            get_array_varchar (
                                                                                               '''||p_in_parentNodeId||'''))))
                                          rh,
                                          (  SELECT SUM (total_selected)
                                                       total_selected,
                                                    SUM (total_participants)
                                                       total_participants,
                                                    SUM (nbr_goal_achieved)
                                                       nbr_achieved,
                                                    SUM (calculated_payout)
                                                       AS awards,
                                                    gl.sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name,
                                                    SUM (rpt.base_quantity) base,
                                                    SUM (rpt.goal) goal,
                                                    SUM (rpt.actual_result)
                                                       actual_result
                                               FROM rpt_hierarchy rh,
                                                    promotion p,
                                                    promo_goalquest promoGoal,
                                                    rpt_goal_selection_summary rpt,
                                                    goalquest_goallevel gl
                                              WHERE     rpt.goal_level =
                                                           gl.goallevel_id(+)
                                                    AND rpt.promotion_id =
                                                           promoGoal.promotion_id(+)
                                                    AND rpt.promotion_id =
                                                           p.promotion_id(+)
                                                    AND detail_node_id =
                                                           rh.node_id
                                                    AND rpt.record_type LIKE
                                                           ''%team%''
                                                    AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                              get_array_varchar (
                                                                                                 '''||p_in_parentNodeId||''')))
                                                    AND rpt.pax_status =
                                                           NVL (
                                                              '''||p_in_participantStatus||''',
                                                              rpt.pax_status)
                                                    AND rpt.job_position =
                                                           NVL ('''||p_in_jobPosition||''',
                                                                rpt.job_position)
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
                                                    AND NVL (p.promotion_status,
                                                             '' '') =
                                                           NVL (
                                                              '''||p_in_promotionStatus||''',
                                                              NVL (
                                                                 p.promotion_status,
                                                                 '' ''))
                                           GROUP BY gl.sequence_num,
                                                    rh.node_id,
                                                    rh.node_name) rpc
                                    WHERE rh.node_id = rpc.node_id(+))
                          SELECT node_name,
                                 node_id,
--                                 total_participants,    10/20/2014
                                 (NVL (no_goal_total_participants,0) +                 --10/20/2014
                                 NVL(level_1_total_participants,0)+
                                 NVL(level_2_total_participants,0)+
                                 NVL(level_3_total_participants,0)+
                                 NVL(level_4_total_participants,0)+
                                 NVL(level_5_total_participants,0)) total_participants,
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
                                    actual_result
                            FROM pivot_set PIVOT (SUM (total_selected) total_selected,
                                                 SUM (total_participants) total_participants,   --10/20/2014                            
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
                                               6 AS level_6)) t)) rs
        GROUP BY NODE_ID, NODE_NAME) RS
                    ORDER BY '|| v_sortCol ||' 
    ) WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;

        OPEN p_out_data FOR 
   l_query;

        --getGQAchievementTabResTotals
        OPEN p_out_totals_data FOR
            SELECT *
              FROM ( (SELECT SUM (total_participants) AS total_participants,
                             SUM (total_selected) AS TOTAL_SELECTED,
                             SUM (NVL (level_1_total_selected, 0))
                                 AS LEVEL_1_SELECTED_CNT,
                             SUM (NVL (level_1_nbr_achieved, 0))
                                 AS LEVEL_1_ACHIEVED_CNT,
                             CASE
                                 WHEN SUM (NVL (level_1_total_selected, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     NVL (
                                         ROUND (
                                               (  SUM (level_1_nbr_achieved)
                                                / SUM (level_1_total_selected))
                                             * 100,
                                             2),
                                         0)
                             END
                                 AS LEVEL_1_ACHIEVED_PERCENT,
                             SUM (NVL (level_1_awards, 0)) AS LEVEL_1_AWARD_CNT,
                             SUM (NVL (level_2_total_selected, 0))
                                 AS LEVEL_2_SELECTED_CNT,
                             SUM (NVL (level_2_nbr_achieved, 0))
                                 AS LEVEL_2_ACHIEVED_CNT,
                             CASE
                                 WHEN SUM (NVL (level_2_total_selected, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     NVL (
                                         ROUND (
                                               (  SUM (level_2_nbr_achieved)
                                                / SUM (level_2_total_selected))
                                             * 100,
                                             2),
                                         0)
                             END
                                 AS LEVEL_2_ACHIEVED_PERCENT,
                             SUM (NVL (level_2_awards, 0)) AS LEVEL_2_AWARD_CNT,
                             SUM (NVL (level_3_total_selected, 0))
                                 AS LEVEL_3_SELECTED_CNT,
                             SUM (NVL (level_3_nbr_achieved, 0))
                                 AS LEVEL_3_ACHIEVED_CNT,
                             CASE
                                 WHEN SUM (NVL (level_3_total_selected, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     NVL (
                                         ROUND (
                                               (  SUM (level_3_nbr_achieved)
                                                / SUM (level_3_total_selected))
                                             * 100,
                                             2),
                                         0)
                             END
                                 AS LEVEL_3_ACHIEVED_PERCENT,
                             SUM (NVL (level_3_awards, 0)) AS LEVEL_3_AWARD_CNT,
                             SUM (NVL (level_4_total_selected, 0))
                                 AS LEVEL_4_SELECTED_CNT,
                             SUM (NVL (level_4_nbr_achieved, 0))
                                 AS LEVEL_4_ACHIEVED_CNT,
                             CASE
                                 WHEN SUM (NVL (level_4_total_selected, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     NVL (
                                         ROUND (
                                               (  SUM (level_4_nbr_achieved)
                                                / SUM (level_4_total_selected))
                                             * 100,
                                             2),
                                         0)
                             END
                                 AS LEVEL_4_ACHIEVED_PERCENT,
                             SUM (NVL (level_4_awards, 0)) AS LEVEL_4_AWARD_CNT,
                             SUM (NVL (level_5_total_selected, 0))
                                 AS LEVEL_5_SELECTED_CNT,
                             SUM (NVL (level_5_nbr_achieved, 0))
                                 AS LEVEL_5_ACHIEVED_CNT,
                             CASE
                                 WHEN SUM (NVL (level_5_total_selected, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     NVL (
                                         ROUND (
                                               (  SUM (level_5_nbr_achieved)
                                                / SUM (level_5_total_selected))
                                             * 100,
                                             2),
                                         0)
                             END
                                 AS LEVEL_5_ACHIEVED_PERCENT,
                             SUM (NVL (level_5_awards, 0)) AS LEVEL_5_AWARD_CNT,
                             SUM (NVL (level_6_total_selected, 0))
                                 AS LEVEL_6_SELECTED_CNT,
                             SUM (NVL (level_6_nbr_achieved, 0))
                                 AS LEVEL_6_ACHIEVED_CNT,
                             CASE
                                 WHEN SUM (NVL (level_6_total_selected, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     NVL (
                                         ROUND (
                                               (  SUM (level_6_nbr_achieved)
                                                / SUM (level_6_total_selected))
                                             * 100,
                                             2),
                                         0)
                             END
                                 AS LEVEL_6_ACHIEVED_PERCENT,
                             SUM (NVL (level_6_awards, 0)) AS LEVEL_6_AWARD_CNT,
                             SUM (NVL (BASE_QUANTITY, 0)) AS BASE_QUANTITY,
                             SUM (NVL (AMT_TO_ACHIEVE, 0)) AS AMT_TO_ACHIEVE,
                             SUM (NVL (CURRENT_VALUE, 0)) AS CURRENT_VALUE,
                             CASE
                                 WHEN SUM (NVL (AMT_TO_ACHIEVE, 0)) = 0
                                 THEN
                                     0
                                 ELSE
                                     ROUND (
                                           (  SUM (NVL (CURRENT_VALUE, 0))
                                            / SUM (AMT_TO_ACHIEVE))
                                         * 100,
                                         2)
                             END
                                 AS PERCENT_ACHIEVED
                        FROM (SELECT node_name AS NODE_NAME,
                                     node_id AS NODE_ID,
                                     total_participants,
                                     total_selected AS TOTAL_SELECTED,
                                     NVL (level_1_total_selected, 0)
                                         AS level_1_total_selected,
                                     NVL (level_1_nbr_achieved, 0)
                                         AS level_1_nbr_achieved,
                                     NVL (level_1_awards, 0) AS level_1_awards,
                                     NVL (level_2_total_selected, 0)
                                         AS level_2_total_selected,
                                     NVL (level_2_nbr_achieved, 0)
                                         AS level_2_nbr_achieved,
                                     NVL (level_2_awards, 0) AS level_2_awards,
                                     NVL (level_3_total_selected, 0)
                                         AS level_3_total_selected,
                                     NVL (level_3_nbr_achieved, 0)
                                         AS level_3_nbr_achieved,
                                     NVL (level_3_awards, 0) AS level_3_awards,
                                     NVL (level_4_total_selected, 0)
                                         AS level_4_total_selected,
                                     NVL (level_4_nbr_achieved, 0)
                                         AS level_4_nbr_achieved,
                                     NVL (level_4_awards, 0) AS level_4_awards,
                                     NVL (level_5_total_selected, 0)
                                         AS level_5_total_selected,
                                     NVL (level_5_nbr_achieved, 0)
                                         AS level_5_nbr_achieved,
                                     NVL (level_5_awards, 0) AS level_5_awards,
                                     NVL (level_6_total_selected, 0)
                                         AS level_6_total_selected,
                                     NVL (level_6_nbr_achieved, 0)
                                         AS level_6_nbr_achieved,
                                     NVL (level_6_awards, 0) AS level_6_awards,
                                     NVL (base, 0) AS BASE_QUANTITY,
                                     NVL (goal, 0) AS AMT_TO_ACHIEVE,
                                     NVL (actual_result, 0) AS CURRENT_VALUE,
                                     0 AS IS_LEAF
                                FROM (WITH pivot_set
                                           AS (SELECT total_selected,
                                                      total_participants,
                                                      nbr_achieved,
                                                      awards,
                                                      NVL (sequence_num, 0)
                                                          sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      base,
                                                      goal,
                                                      actual_result
                                                 FROM (SELECT DISTINCT
                                                              rh.node_id
                                                                  AS node_id,
                                                              rh.node_name
                                                                  node_name
                                                         FROM rpt_hierarchy rh,
                                                              rpt_goal_selection_summary rpt
                                                        WHERE     detail_node_id =
                                                                      rh.node_id
                                                              AND rpt.record_type LIKE
                                                                      '%node%'
                                                              AND NVL (
                                                                      header_node_id,
                                                                      0) IN (SELECT *
                                                                               FROM TABLE (
                                                                                        get_array_varchar (
                                                                                            p_in_parentNodeId))))
                                                      rh,
                                                      (  SELECT SUM (
                                                                    total_selected)
                                                                    total_selected,
                                                                SUM (
                                                                    total_participants)
                                                                    total_participants,
                                                                SUM (
                                                                    nbr_goal_achieved)
                                                                    nbr_achieved,
                                                                SUM (
                                                                    calculated_payout)
                                                                    AS awards,
                                                                gl.sequence_num,
                                                                rh.node_id
                                                                    AS node_id,
                                                                rh.node_name
                                                                    node_name,
                                                                SUM (
                                                                    rpt.base_quantity)
                                                                    base,
                                                                SUM (rpt.goal)
                                                                    goal,
                                                                SUM (
                                                                    rpt.actual_result)
                                                                    actual_result
                                                           FROM rpt_hierarchy rh,
                                                                promotion p,
                                                                promo_goalquest promoGoal,
                                                                rpt_goal_selection_summary rpt,
                                                                goalquest_goallevel gl
                                                          WHERE     rpt.goal_level =
                                                                        gl.goallevel_id(+)
                                                                AND rpt.promotion_id =
                                                                        promoGoal.promotion_id(+)
                                                                AND rpt.promotion_id =
                                                                        p.promotion_id(+)
                                                                AND detail_node_id =
                                                                        rh.node_id
                                                                AND rpt.record_type LIKE
                                                                        '%node%'
                                                                AND NVL (
                                                                        header_node_id,
                                                                        0) IN (SELECT *
                                                                                 FROM TABLE (
                                                                                          get_array_varchar (
                                                                                              p_in_parentNodeId)))
                                                                AND rpt.pax_status =
                                                                        NVL (
                                                                            p_in_participantStatus,
                                                                            rpt.pax_status)
                                                                AND rpt.job_position =
                                                                        NVL (
                                                                            p_in_jobPosition,
                                                                            rpt.job_position)
                                                                AND (   (p_in_departments
                                                                             IS NULL)
                                                                     OR (    p_in_departments
                                                                                 IS NOT NULL
                                                                         AND rpt.department IN (SELECT *
                                                                                                  FROM TABLE (
                                                                                                           get_array_varchar (
                                                                                                               p_in_departments)))))
                                                                AND (   (p_in_promotionId
                                                                             IS NULL)
                                                                     OR (    p_in_promotionId
                                                                                 IS NOT NULL
                                                                         AND rpt.promotion_id IN (SELECT *
                                                                                                    FROM TABLE (
                                                                                                             get_array_varchar (
                                                                                                                 p_in_promotionId)))))
                                                                AND NVL (
                                                                        p.promotion_status,
                                                                        ' ') =
                                                                        NVL (
                                                                            p_in_promotionStatus,
                                                                            NVL (
                                                                                p.promotion_status,
                                                                                ' '))
                                                       GROUP BY gl.sequence_num,
                                                                rh.node_id,
                                                                rh.node_name)
                                                      rpc
                                                WHERE rh.node_id =
                                                          rpc.node_id(+))
                                      SELECT node_name,
                                             node_id,
                                             (  NVL (level_1_total_selected, 0)
                                              + NVL (level_2_total_selected, 0)
                                              + NVL (level_3_total_selected, 0)
                                              + NVL (level_4_total_selected, 0)
                                              + NVL (level_5_total_selected, 0))
                                                 total_selected,
--                                             total_participants,            10/20/2014
                                             (NVL (no_goal_total_participants,0) +                 --10/20/2014
                                             NVL(level_1_total_participants,0)+
                                             NVL(level_2_total_participants,0)+
                                             NVL(level_3_total_participants,0)+
                                             NVL(level_4_total_participants,0)+
                                             NVL(level_5_total_participants,0)) total_participants,
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
                                        FROM pivot_set PIVOT (SUM (
                                                                  total_selected) total_selected,
                                                   SUM(total_participants) total_participants,      --10/20/2014                                                                  
                                                             SUM (nbr_achieved) nbr_achieved,
                                                             SUM (awards) awards,
                                                             SUM (base) base,
                                                             SUM (goal) goal,
                                                             SUM (
                                                                 actual_result) actual_result
                                                       FOR sequence_num
                                                       IN  (0 AS no_goal,
                                                           1 AS level_1,
                                                           2 AS level_2,
                                                           3 AS level_3,
                                                           4 AS level_4,
                                                           5 AS level_5,
                                                           6 AS level_6)) t)
                              UNION
                              SELECT node_name || ' Team' AS NODE_NAME,
                                     node_id AS NODE_ID,
                                     total_participants,
                                     total_selected AS TOTAL_SELECTED,
                                     NVL (level_1_total_selected, 0)
                                         AS LEVEL_1_SELECTED_CNT,
                                     NVL (level_1_nbr_achieved, 0)
                                         AS LEVEL_1_ACHIEVED_CNT,
                                     NVL (level_1_awards, 0)
                                         AS LEVEL_1_AWARD_CNT,
                                     NVL (level_2_total_selected, 0)
                                         AS LEVEL_2_SELECTED_CNT,
                                     NVL (level_2_nbr_achieved, 0)
                                         AS LEVEL_2_ACHIEVED_CNT,
                                     NVL (level_2_awards, 0)
                                         AS LEVEL_2_AWARD_CNT,
                                     NVL (level_3_total_selected, 0)
                                         AS LEVEL_3_SELECTED_CNT,
                                     NVL (level_3_nbr_achieved, 0)
                                         AS LEVEL_3_ACHIEVED_CNT,
                                     NVL (level_3_awards, 0)
                                         AS LEVEL_3_AWARD_CNT,
                                     NVL (level_4_total_selected, 0)
                                         AS LEVEL_4_SELECTED_CNT,
                                     NVL (level_4_nbr_achieved, 0)
                                         AS LEVEL_4_ACHIEVED_CNT,
                                     NVL (level_4_awards, 0)
                                         AS LEVEL_4_AWARD_CNT,
                                     NVL (level_5_total_selected, 0)
                                         AS LEVEL_5_SELECTED_CNT,
                                     NVL (level_5_nbr_achieved, 0)
                                         AS LEVEL_5_ACHIEVED_CNT,
                                     NVL (level_5_awards, 0)
                                         AS LEVEL_5_AWARD_CNT,
                                     NVL (level_6_total_selected, 0)
                                         AS LEVEL_6_SELECTED_CNT,
                                     NVL (level_6_nbr_achieved, 0)
                                         AS LEVEL_6_ACHIEVED_CNT,
                                     NVL (level_6_awards, 0)
                                         AS LEVEL_6_AWARD_CNT,
                                     NVL (base, 0) AS BASE_QUANTITY,
                                     NVL (goal, 0) AS AMT_TO_ACHIEVE,
                                     NVL (actual_result, 0) AS CURRENT_VALUE,
                                     1 AS IS_LEAF
                                FROM (WITH pivot_set
                                           AS (SELECT total_selected,
                                                      total_participants,
                                                      nbr_achieved,
                                                      awards,
                                                      NVL (sequence_num, 0)
                                                          sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      base,
                                                      goal,
                                                      actual_result
                                                 FROM (SELECT DISTINCT
                                                              rh.node_id
                                                                  AS node_id,
                                                              rh.node_name
                                                                  node_name
                                                         FROM rpt_hierarchy rh,
                                                              rpt_goal_selection_summary rpt
                                                        WHERE     detail_node_id =
                                                                      rh.node_id
                                                              AND rpt.record_type LIKE
                                                                      '%team%'
                                                              AND NVL (
                                                                      detail_node_id,
                                                                      0) IN (SELECT *
                                                                               FROM TABLE (
                                                                                        get_array_varchar (
                                                                                            p_in_parentNodeId))))
                                                      rh,
                                                      (  SELECT SUM (
                                                                    total_selected)
                                                                    total_selected,
                                                                SUM (
                                                                    total_participants)
                                                                    total_participants,
                                                                SUM (
                                                                    nbr_goal_achieved)
                                                                    nbr_achieved,
                                                                SUM (
                                                                    calculated_payout)
                                                                    AS awards,
                                                                gl.sequence_num,
                                                                rh.node_id
                                                                    AS node_id,
                                                                rh.node_name
                                                                    node_name,
                                                                SUM (
                                                                    rpt.base_quantity)
                                                                    base,
                                                                SUM (rpt.goal)
                                                                    goal,
                                                                SUM (
                                                                    rpt.actual_result)
                                                                    actual_result
                                                           FROM rpt_hierarchy rh,
                                                                promotion p,
                                                                promo_goalquest promoGoal,
                                                                rpt_goal_selection_summary rpt,
                                                                goalquest_goallevel gl
                                                          WHERE     rpt.goal_level =
                                                                        gl.goallevel_id(+)
                                                                AND rpt.promotion_id =
                                                                        promoGoal.promotion_id(+)
                                                                AND rpt.promotion_id =
                                                                        p.promotion_id(+)
                                                                AND detail_node_id =
                                                                        rh.node_id
                                                                AND rpt.record_type LIKE
                                                                        '%team%'
                                                                AND NVL (
                                                                        detail_node_id,
                                                                        0) IN (SELECT *
                                                                                 FROM TABLE (
                                                                                          get_array_varchar (
                                                                                              p_in_parentNodeId)))
                                                                AND rpt.pax_status =
                                                                        NVL (
                                                                            p_in_participantStatus,
                                                                            rpt.pax_status)
                                                                AND rpt.job_position =
                                                                        NVL (
                                                                            p_in_jobPosition,
                                                                            rpt.job_position)
                                                                AND (   (p_in_departments
                                                                             IS NULL)
                                                                     OR (    p_in_departments
                                                                                 IS NOT NULL
                                                                         AND rpt.department IN (SELECT *
                                                                                                  FROM TABLE (
                                                                                                           get_array_varchar (
                                                                                                               p_in_departments)))))
                                                                AND (   (p_in_promotionId
                                                                             IS NULL)
                                                                     OR (    p_in_promotionId
                                                                                 IS NOT NULL
                                                                         AND rpt.promotion_id IN (SELECT *
                                                                                                    FROM TABLE (
                                                                                                             get_array_varchar (
                                                                                                                 p_in_promotionId)))))
                                                                AND NVL (
                                                                        p.promotion_status,
                                                                        ' ') =
                                                                        NVL (
                                                                            p_in_promotionStatus,
                                                                            NVL (
                                                                                p.promotion_status,
                                                                                ' '))
                                                       GROUP BY gl.sequence_num,
                                                                rh.node_id,
                                                                rh.node_name)
                                                      rpc
                                                WHERE rh.node_id =
                                                          rpc.node_id(+))
                                      SELECT node_name,
                                             node_id,
--                                             total_participants,     --10/20/2014
                                             (NVL (no_goal_total_participants,0) +                 --10/20/2014
                                             NVL(level_1_total_participants,0)+
                                             NVL(level_2_total_participants,0)+
                                             NVL(level_3_total_participants,0)+
                                             NVL(level_4_total_participants,0)+
                                             NVL(level_5_total_participants,0)) total_participants,
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
                                                 actual_result
                                        FROM pivot_set PIVOT (SUM (
                                                                  total_selected) total_selected,
                                                                SUM(total_participants) total_participants,      --10/20/2014                                                                  
                                                             SUM (nbr_achieved) nbr_achieved,
                                                             SUM (awards) awards,
                                                             SUM (base) base,
                                                             SUM (goal) goal,
                                                             SUM (
                                                                 actual_result) actual_result
                                                       FOR sequence_num
                                                       IN  (0 AS no_goal,
                                                           1 AS level_1,
                                                           2 AS level_2,
                                                           3 AS level_3,
                                                           4 AS level_4,
                                                           5 AS level_5,
                                                           6 AS level_6)) t))
                             rs));



        --getGQAchievementResSize       
            SELECT COUNT (1) INTO p_out_size_data
              FROM (  SELECT node_name AS NODE_NAME,
                             node_id AS NODE_ID,
                             SUM (total_participants) AS total_participants,
                             SUM (total_selected) AS TOTAL_SELECTED,
                             SUM (NVL (level_1_total_selected, 0))
                                 AS LEVEL_1_SELECTED_CNT,
                             SUM (NVL (level_1_nbr_achieved, 0))
                                 AS LEVEL_1_ACHIEVED_CNT,
                             SUM (NVL (level_1_awards, 0)) AS LEVEL_1_AWARD_CNT,
                             SUM (NVL (level_2_total_selected, 0))
                                 AS LEVEL_2_SELECTED_CNT,
                             SUM (NVL (level_2_nbr_achieved, 0))
                                 AS LEVEL_2_ACHIEVED_CNT,
                             SUM (NVL (level_2_awards, 0)) AS LEVEL_2_AWARD_CNT,
                             SUM (NVL (level_3_total_selected, 0))
                                 AS LEVEL_3_SELECTED_CNT,
                             SUM (NVL (level_3_nbr_achieved, 0))
                                 AS LEVEL_3_ACHIEVED_CNT,
                             SUM (NVL (level_3_awards, 0)) AS LEVEL_3_AWARD_CNT,
                             SUM (NVL (level_4_total_selected, 0))
                                 AS LEVEL_4_SELECTED_CNT,
                             SUM (NVL (level_4_nbr_achieved, 0))
                                 AS LEVEL_4_ACHIEVED_CNT,
                             SUM (NVL (level_4_awards, 0)) AS LEVEL_4_AWARD_CNT,
                             SUM (NVL (level_5_total_selected, 0))
                                 AS LEVEL_5_SELECTED_CNT,
                             SUM (NVL (level_5_nbr_achieved, 0))
                                 AS LEVEL_5_ACHIEVED_CNT,
                             SUM (NVL (level_5_awards, 0)) AS LEVEL_5_AWARD_CNT,
                             SUM (NVL (level_6_total_selected, 0))
                                 AS LEVEL_6_SELECTED_CNT,
                             SUM (NVL (level_6_nbr_achieved, 0))
                                 AS LEVEL_6_ACHIEVED_CNT,
                             SUM (NVL (level_6_awards, 0)) AS LEVEL_6_AWARD_CNT,
                             SUM (NVL (BASE_QUANTITY, 0)) AS BASE_QUANTITY,
                             SUM (NVL (AMT_TO_ACHIEVE, 0)) AS AMT_TO_ACHIEVE,
                             SUM (NVL (CURRENT_VALUE, 0)) AS CURRENT_VALUE
                        FROM (SELECT node_name AS NODE_NAME,
                                     node_id AS NODE_ID,
                                     total_participants,
                                     total_selected AS TOTAL_SELECTED,
                                     NVL (level_1_total_selected, 0)
                                         AS level_1_total_selected,
                                     NVL (level_1_nbr_achieved, 0)
                                         AS level_1_nbr_achieved,
                                     NVL (level_1_awards, 0) AS level_1_awards,
                                     NVL (level_2_total_selected, 0)
                                         AS level_2_total_selected,
                                     NVL (level_2_nbr_achieved, 0)
                                         AS level_2_nbr_achieved,
                                     NVL (level_2_awards, 0) AS level_2_awards,
                                     NVL (level_3_total_selected, 0)
                                         AS level_3_total_selected,
                                     NVL (level_3_nbr_achieved, 0)
                                         AS level_3_nbr_achieved,
                                     NVL (level_3_awards, 0) AS level_3_awards,
                                     NVL (level_4_total_selected, 0)
                                         AS level_4_total_selected,
                                     NVL (level_4_nbr_achieved, 0)
                                         AS level_4_nbr_achieved,
                                     NVL (level_4_awards, 0) AS level_4_awards,
                                     NVL (level_5_total_selected, 0)
                                         AS level_5_total_selected,
                                     NVL (level_5_nbr_achieved, 0)
                                         AS level_5_nbr_achieved,
                                     NVL (level_5_awards, 0) AS level_5_awards,
                                     NVL (level_6_total_selected, 0)
                                         AS level_6_total_selected,
                                     NVL (level_6_nbr_achieved, 0)
                                         AS level_6_nbr_achieved,
                                     NVL (level_6_awards, 0) AS level_6_awards,
                                     NVL (base, 0) AS BASE_QUANTITY,
                                     NVL (goal, 0) AS AMT_TO_ACHIEVE,
                                     NVL (actual_result, 0) AS CURRENT_VALUE,
                                     0 AS IS_LEAF
                                FROM (WITH pivot_set
                                           AS (SELECT total_selected,
                                                      total_participants,
                                                      nbr_achieved,
                                                      awards,
                                                      NVL (sequence_num, 0)
                                                          sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      base,
                                                      goal,
                                                      actual_result
                                                 FROM (SELECT DISTINCT
                                                              rh.node_id
                                                                  AS node_id,
                                                              rh.node_name
                                                                  node_name
                                                         FROM rpt_hierarchy rh,
                                                              rpt_goal_selection_summary rpt
                                                        WHERE     detail_node_id =
                                                                      rh.node_id
                                                              AND rpt.record_type LIKE
                                                                      '%node%'
                                                              AND NVL (
                                                                      header_node_id,
                                                                      0) IN (SELECT *
                                                                               FROM TABLE (
                                                                                        get_array_varchar (
                                                                                            p_in_parentNodeId))))
                                                      rh,
                                                      (  SELECT SUM (
                                                                    total_selected)
                                                                    total_selected,
                                                                SUM (
                                                                    total_participants)
                                                                    total_participants,
                                                                SUM (
                                                                    nbr_goal_achieved)
                                                                    nbr_achieved,
                                                                SUM (
                                                                    calculated_payout)
                                                                    AS awards,
                                                                gl.sequence_num,
                                                                rh.node_id
                                                                    AS node_id,
                                                                rh.node_name
                                                                    node_name,
                                                                SUM (
                                                                    rpt.base_quantity)
                                                                    base,
                                                                SUM (rpt.goal) goal,
                                                                SUM (
                                                                    rpt.actual_result)
                                                                    actual_result
                                                           FROM rpt_hierarchy rh,
                                                                promotion p,
                                                                promo_goalquest promoGoal,
                                                                rpt_goal_selection_summary rpt,
                                                                goalquest_goallevel gl
                                                          WHERE     rpt.goal_level =
                                                                        gl.goallevel_id(+)
                                                                AND rpt.promotion_id =
                                                                        promoGoal.promotion_id(+)
                                                                AND rpt.promotion_id =
                                                                        p.promotion_id(+)
                                                                AND detail_node_id =
                                                                        rh.node_id
                                                                AND rpt.record_type LIKE
                                                                        '%node%'
                                                                AND NVL (
                                                                        header_node_id,
                                                                        0) IN (SELECT *
                                                                                 FROM TABLE (
                                                                                          get_array_varchar (
                                                                                              p_in_parentNodeId)))
                                                                AND rpt.pax_status =
                                                                        NVL (
                                                                            p_in_participantStatus,
                                                                            rpt.pax_status)
                                                                AND rpt.job_position =
                                                                        NVL (
                                                                            p_in_jobPosition,
                                                                            rpt.job_position)
                                                                AND (   (p_in_departments
                                                                             IS NULL)
                                                                     OR (    p_in_departments
                                                                                 IS NOT NULL
                                                                         AND rpt.department IN (SELECT *
                                                                                                  FROM TABLE (
                                                                                                           get_array_varchar (
                                                                                                               p_in_departments)))))
                                                                AND (   (p_in_promotionId
                                                                             IS NULL)
                                                                     OR (    p_in_promotionId
                                                                                 IS NOT NULL
                                                                         AND rpt.promotion_id IN (SELECT *
                                                                                                    FROM TABLE (
                                                                                                             get_array_varchar (
                                                                                                                 p_in_promotionId)))))
                                                                AND NVL (
                                                                        p.promotion_status,
                                                                        ' ') =
                                                                        NVL (
                                                                            p_in_promotionStatus,
                                                                            NVL (
                                                                                p.promotion_status,
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
                                              + NVL (level_5_total_selected, 0))
                                                 total_selected,
--                                             total_participants,                 --10/20/2014
                                             (NVL (no_goal_total_participants,0) +                 --10/20/2014
                                             NVL(level_1_total_participants,0)+
                                             NVL(level_2_total_participants,0)+
                                             NVL(level_3_total_participants,0)+
                                             NVL(level_4_total_participants,0)+
                                             NVL(level_5_total_participants,0)) total_participants,
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
                                        FROM pivot_set PIVOT (SUM (
                                                                  total_selected) total_selected,
                                                             SUM(total_participants) total_participants,      --10/20/2014                                                                  
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
                                                           6 AS level_6)) t)
                              UNION
                              SELECT node_name || ' Team' AS NODE_NAME,
                                     node_id AS NODE_ID,
                                     total_participants,
                                     total_selected AS TOTAL_SELECTED,
                                     NVL (level_1_total_selected, 0)
                                         AS LEVEL_1_SELECTED_CNT,
                                     NVL (level_1_nbr_achieved, 0)
                                         AS LEVEL_1_ACHIEVED_CNT,
                                     NVL (level_1_awards, 0) AS LEVEL_1_AWARD_CNT,
                                     NVL (level_2_total_selected, 0)
                                         AS LEVEL_2_SELECTED_CNT,
                                     NVL (level_2_nbr_achieved, 0)
                                         AS LEVEL_2_ACHIEVED_CNT,
                                     NVL (level_2_awards, 0) AS LEVEL_2_AWARD_CNT,
                                     NVL (level_3_total_selected, 0)
                                         AS LEVEL_3_SELECTED_CNT,
                                     NVL (level_3_nbr_achieved, 0)
                                         AS LEVEL_3_ACHIEVED_CNT,
                                     NVL (level_3_awards, 0) AS LEVEL_3_AWARD_CNT,
                                     NVL (level_4_total_selected, 0)
                                         AS LEVEL_4_SELECTED_CNT,
                                     NVL (level_4_nbr_achieved, 0)
                                         AS LEVEL_4_ACHIEVED_CNT,
                                     NVL (level_4_awards, 0) AS LEVEL_4_AWARD_CNT,
                                     NVL (level_5_total_selected, 0)
                                         AS LEVEL_5_SELECTED_CNT,
                                     NVL (level_5_nbr_achieved, 0)
                                         AS LEVEL_5_ACHIEVED_CNT,
                                     NVL (level_5_awards, 0) AS LEVEL_5_AWARD_CNT,
                                     NVL (level_6_total_selected, 0)
                                         AS LEVEL_6_SELECTED_CNT,
                                     NVL (level_6_nbr_achieved, 0)
                                         AS LEVEL_6_ACHIEVED_CNT,
                                     NVL (level_6_awards, 0) AS LEVEL_6_AWARD_CNT,
                                     NVL (base, 0) AS BASE_QUANTITY,
                                     NVL (goal, 0) AS AMT_TO_ACHIEVE,
                                     NVL (actual_result, 0) AS CURRENT_VALUE,
                                     1 AS IS_LEAF
                                FROM (WITH pivot_set
                                           AS (SELECT total_selected,
                                                      total_participants,
                                                      nbr_achieved,
                                                      awards,
                                                      NVL (sequence_num, 0)
                                                          sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      base,
                                                      goal,
                                                      actual_result
                                                 FROM (SELECT DISTINCT
                                                              rh.node_id
                                                                  AS node_id,
                                                              rh.node_name
                                                                  node_name
                                                         FROM rpt_hierarchy rh,
                                                              rpt_goal_selection_summary rpt
                                                        WHERE     detail_node_id =
                                                                      rh.node_id
                                                              AND rpt.record_type LIKE
                                                                      '%team%'
                                                              AND NVL (
                                                                      detail_node_id,
                                                                      0) IN (SELECT *
                                                                               FROM TABLE (
                                                                                        get_array_varchar (
                                                                                            p_in_parentNodeId))))
                                                      rh,
                                                      (  SELECT SUM (
                                                                    total_selected)
                                                                    total_selected,
                                                                SUM (
                                                                    total_participants)
                                                                    total_participants,
                                                                SUM (
                                                                    nbr_goal_achieved)
                                                                    nbr_achieved,
                                                                SUM (
                                                                    calculated_payout)
                                                                    AS awards,
                                                                gl.sequence_num,
                                                                rh.node_id
                                                                    AS node_id,
                                                                rh.node_name
                                                                    node_name,
                                                                SUM (
                                                                    rpt.base_quantity)
                                                                    base,
                                                                SUM (rpt.goal) goal,
                                                                SUM (
                                                                    rpt.actual_result)
                                                                    actual_result
                                                           FROM rpt_hierarchy rh,
                                                                promotion p,
                                                                promo_goalquest promoGoal,
                                                                rpt_goal_selection_summary rpt,
                                                                goalquest_goallevel gl
                                                          WHERE     rpt.goal_level =
                                                                        gl.goallevel_id(+)
                                                                AND rpt.promotion_id =
                                                                        promoGoal.promotion_id(+)
                                                                AND rpt.promotion_id =
                                                                        p.promotion_id(+)
                                                                AND detail_node_id =
                                                                        rh.node_id
                                                                AND rpt.record_type LIKE
                                                                        '%team%'
                                                                AND NVL (
                                                                        detail_node_id,
                                                                        0) IN (SELECT *
                                                                                 FROM TABLE (
                                                                                          get_array_varchar (
                                                                                              p_in_parentNodeId)))
                                                                AND rpt.pax_status =
                                                                        NVL (
                                                                            p_in_participantStatus,
                                                                            rpt.pax_status)
                                                                AND rpt.job_position =
                                                                        NVL (
                                                                            p_in_jobPosition,
                                                                            rpt.job_position)
                                                                AND (   (p_in_departments
                                                                             IS NULL)
                                                                     OR (    p_in_departments
                                                                                 IS NOT NULL
                                                                         AND rpt.department IN (SELECT *
                                                                                                  FROM TABLE (
                                                                                                           get_array_varchar (
                                                                                                               p_in_departments)))))
                                                                AND (   (p_in_promotionId
                                                                             IS NULL)
                                                                     OR (    p_in_promotionId
                                                                                 IS NOT NULL
                                                                         AND rpt.promotion_id IN (SELECT *
                                                                                                    FROM TABLE (
                                                                                                             get_array_varchar (
                                                                                                                 p_in_promotionId)))))
                                                                AND NVL (
                                                                        p.promotion_status,
                                                                        ' ') =
                                                                        NVL (
                                                                            p_in_promotionStatus,
                                                                            NVL (
                                                                                p.promotion_status,
                                                                                ' '))
                                                       GROUP BY gl.sequence_num,
                                                                rh.node_id,
                                                                rh.node_name) rpc
                                                WHERE rh.node_id = rpc.node_id(+))
                                      SELECT node_name,
                                             node_id,
--                                             total_participants,      --10/20/2014
                                             (NVL (no_goal_total_participants,0) +                 --10/20/2014
                                             NVL(level_1_total_participants,0)+
                                             NVL(level_2_total_participants,0)+
                                             NVL(level_3_total_participants,0)+
                                             NVL(level_4_total_participants,0)+
                                             NVL(level_5_total_participants,0)) total_participants,
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
                                                 actual_result
                                        FROM pivot_set PIVOT (SUM (
                                                                  total_selected) total_selected,
                                                                SUM(total_participants) total_participants,      --10/20/2014                                                                  
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
                                                           6 AS level_6)) t)) rs
                    GROUP BY NODE_ID, NODE_NAME) RS;

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
    END prc_getGQAchievementTabRes;


    PROCEDURE prc_getGQAchievementDetailRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getGQAchievementDetailRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J               09/01/2014     Bug Fix 56150  - Fixed SQL error    
  Swati                  04/07/2015     Bug 61130 - Report: GoalQuest Participant Achievement - Total '% of Goal' is wrong  
  ******************************************************************************/    
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQAchievementDetailRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    
    l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN, rs.*
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
                                     NVL (gsd.amount_to_achieve, 0) AS GOAL,
                                     NVL (gsd.current_value, 0) AS ACTUAL,
                                     ROUND (NVL (gsd.percent_of_goal, 0) ,2) --04/07/2015 Bug 61130
                                         AS PERCENT_OF_GOAL,
                                     NVL (gsd.achieved, 0) AS IS_ACHIEVED,
                                     NVL (gsd.calculated_payout, 0) AS POINTS
                                FROM promo_goalquest promoGoal,
                                     promotion p,
                                     rpt_goal_selection_detail gsd
                                     LEFT OUTER JOIN rpt_hierarchy hier
                                         ON gsd.node_id = hier.node_id
                                     LEFT OUTER JOIN promotion promo
                                         ON gsd.promotion_id = promo.promotion_id
                                     RIGHT OUTER JOIN goalquest_paxgoal pg
                                         ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                                     LEFT OUTER JOIN goalquest_goallevel gl
                                         ON pg.goallevel_id = gl.goallevel_id
                               WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array_varchar (
                                                                                 '''||p_in_parentNodeId||''')))
                                     AND gsd.promotion_id =
                                             promoGoal.promotion_id
                                     AND gsd.promotion_id = p.promotion_id
                                     AND NVL (p.promotion_status, '' '') =
                                             NVL ('''||p_in_promotionStatus||''',
                                                  NVL (p.promotion_status, '' ''))
                                     AND gsd.user_status =
                                             NVL ('''||p_in_participantStatus||''',
                                                  gsd.user_status)
                                     AND gsd.job_position =
                                             NVL ('''||p_in_jobPosition||''',
                                                  gsd.job_position)
                                     AND (   ('''||p_in_departments||''' IS NULL)
                                          OR gsd.department IN (SELECT *
                                                                  FROM TABLE (
                                                                           get_array_varchar (
                                                                               '''||p_in_departments||'''))))
                                     AND (   gsd.promotion_id IN (SELECT *
                                                                    FROM TABLE (
                                                                             CAST (
                                                                                 get_array_varchar (
                                                                                     '''||p_in_promotionId||''') AS ARRAY_VARCHAR)))
                                          OR '''||p_in_promotionId||''' IS NULL)) rs
                    ORDER BY '|| v_sortCol ||'
      ) RS
     WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ; --09/01/2014

     OPEN p_out_data FOR 
   l_query;

        --getGQAchievementDetailResTotals
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (BASE), 0) AS BASE,
                   NVL (SUM (GOAL), 0) AS GOAL,
                   NVL (SUM (ACTUAL), 0) AS ACTUAL,
                   --ROUND (NVL (SUM (PERCENT_OF_GOAL) / 100, 0), 2)
                    case when SUM (NVL(GOAL,0)) <> 0 then  
                          ROUND ((NVL (SUM (NVL(ACTUAL,0)), 0)  / NVL (SUM (NVL(GOAL,0)), 0)  * 100),2)--04/07/2015 Bug 61130
                     ELSE 0
                   end
                       AS PERCENT_OF_GOAL,
                   NVL (SUM (POINTS), 0) AS POINTS
              FROM (SELECT    gsd.last_name
                           || ' , '
                           || gsd.first_name
                           || ' '
                           || gsd.middle_init
                               AS PAX_NAME,
                           hier.node_name AS NODE_NAME,
                           gsd.promotion_name AS PROMO_NAME,
                           'Level ' || gl.sequence_num AS LEVEL_NUMBER,
                           NVL (gsd.base_quantity, 0) AS BASE,
                           NVL (gsd.amount_to_achieve, 0) AS GOAL,
                           NVL (gsd.current_value, 0) AS ACTUAL,
                           NVL (gsd.percent_of_goal, 0) AS PERCENT_OF_GOAL,
                           gsd.achieved AS IS_ACHIEVED,
                           NVL (gsd.calculated_payout, 0) AS POINTS
                      FROM promo_goalquest promoGoal,
                           promotion p,
                           RPT_GOAL_SELECTION_DETAIL gsd
                           LEFT OUTER JOIN RPT_HIERARCHY hier
                               ON gsd.node_id = hier.node_id
                           LEFT OUTER JOIN PROMOTION promo
                               ON gsd.promotion_id = promo.promotion_id
                           RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                               ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                           LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                               ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
                     WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_parentNodeId)))
                           AND gsd.promotion_id = promoGoal.promotion_id
                           AND gsd.promotion_id = p.promotion_id
                           AND NVL (p.promotion_status, ' ') =
                                   NVL (p_in_promotionStatus,
                                        NVL (p.promotion_status, ' '))
                           AND gsd.user_status =
                                   NVL (p_in_participantStatus,
                                        gsd.user_status)
                           AND gsd.job_position =
                                   NVL (p_in_jobPosition, gsd.job_position)
                           AND (   (p_in_departments IS NULL)
                                OR gsd.department IN (SELECT *
                                                        FROM TABLE (
                                                                 get_array_varchar (
                                                                     p_in_departments))))
                           AND (   gsd.promotion_id IN (SELECT *
                                                          FROM TABLE (
                                                                   CAST (
                                                                       get_array_varchar (
                                                                           p_in_promotionId) AS ARRAY_VARCHAR)))
                                OR p_in_promotionId IS NULL)) rs;



        --getGQAchievementDetailResSize
        SELECT COUNT (1) INTO p_out_size_data
              FROM promo_goalquest promoGoal,
                   promotion p,
                   RPT_GOAL_SELECTION_DETAIL gsd
                   LEFT OUTER JOIN RPT_HIERARCHY hier
                       ON gsd.node_id = hier.node_id
                   LEFT OUTER JOIN PROMOTION promo
                       ON gsd.promotion_id = promo.promotion_id
                   RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                       ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                   LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                       ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
             WHERE     NVL (gsd.node_id, 0) IN (SELECT *
                                                  FROM TABLE (
                                                           get_array_varchar (
                                                               p_in_parentNodeId)))
                   AND gsd.promotion_id = promoGoal.promotion_id
                   AND gsd.promotion_id = p.promotion_id
                   AND NVL (p.promotion_status, ' ') =
                           NVL (p_in_promotionStatus,
                                NVL (p.promotion_status, ' '))
                   AND gsd.user_status =
                           NVL (p_in_participantStatus, gsd.user_status)
                   AND gsd.job_position =
                           NVL (p_in_jobPosition, gsd.job_position)
                   AND (   p_in_departments IS NULL
                        OR gsd.department IN (SELECT *
                                                FROM TABLE (
                                                         get_array_varchar (
                                                             p_in_departments))))
                   AND (   gsd.promotion_id IN (SELECT *
                                                  FROM TABLE (
                                                           CAST (
                                                               get_array_varchar (
                                                                   p_in_promotionId) AS ARRAY_VARCHAR)))
                        OR p_in_promotionId IS NULL);

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
    END prc_getGQAchievementDetailRes;



    PROCEDURE prc_getGQPctAchievedChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQPctAchievedChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT ROUND (
                       DECODE (
                           SUM (level_1_selected_cnt),
                           0, 0,
                             (  SUM (level_1_achieved_cnt)
                              / SUM (level_1_selected_cnt))
                           * 100),
                       2)
                       AS LEVEL_1_ACHIEVED_PERCENT,
                   ROUND (
                       DECODE (
                           SUM (level_2_selected_cnt),
                           0, 0,
                             (  SUM (level_2_achieved_cnt)
                              / SUM (level_2_selected_cnt))
                           * 100),
                       2)
                       AS LEVEL_2_ACHIEVED_PERCENT,
                   ROUND (
                       DECODE (
                           SUM (level_3_selected_cnt),
                           0, 0,
                             (  SUM (level_3_achieved_cnt)
                              / SUM (level_3_selected_cnt))
                           * 100),
                       2)
                       AS LEVEL_3_ACHIEVED_PERCENT,
                   ROUND (
                       DECODE (
                           SUM (level_4_selected_cnt),
                           0, 0,
                             (  SUM (level_4_achieved_cnt)
                              / SUM (level_4_selected_cnt))
                           * 100),
                       2)
                       AS LEVEL_4_ACHIEVED_PERCENT,
                   ROUND (
                       DECODE (
                           SUM (level_5_selected_cnt),
                           0, 0,
                             (  SUM (level_5_achieved_cnt)
                              / SUM (level_5_selected_cnt))
                           * 100),
                       2)
                       AS LEVEL_5_ACHIEVED_PERCENT,
                   ROUND (
                       DECODE (
                           SUM (level_6_selected_cnt),
                           0, 0,
                             (  SUM (level_6_achieved_cnt)
                              / SUM (level_6_selected_cnt))
                           * 100),
                       2)
                       AS LEVEL_6_ACHIEVED_PERCENT
              FROM (SELECT node_name AS NODE_NAME,
                           node_id AS NODE_ID,
                           NVL (level_1_total_selected, 0)
                               AS LEVEL_1_SELECTED_CNT,
                           NVL (level_1_nbr_achieved, 0)
                               AS LEVEL_1_ACHIEVED_CNT,
                           NVL (level_2_total_selected, 0)
                               AS LEVEL_2_SELECTED_CNT,
                           NVL (level_2_nbr_achieved, 0)
                               AS LEVEL_2_ACHIEVED_CNT,
                           NVL (level_3_total_selected, 0)
                               AS LEVEL_3_SELECTED_CNT,
                           NVL (level_3_nbr_achieved, 0)
                               AS LEVEL_3_ACHIEVED_CNT,
                           NVL (level_4_total_selected, 0)
                               AS LEVEL_4_SELECTED_CNT,
                           NVL (level_4_nbr_achieved, 0)
                               AS LEVEL_4_ACHIEVED_CNT,
                           NVL (level_5_total_selected, 0)
                               AS LEVEL_5_SELECTED_CNT,
                           NVL (level_5_nbr_achieved, 0)
                               AS LEVEL_5_ACHIEVED_CNT,
                           NVL (level_6_total_selected, 0)
                               AS LEVEL_6_SELECTED_CNT,
                           NVL (level_6_nbr_achieved, 0)
                               AS LEVEL_6_ACHIEVED_CNT,
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
                                            actual_result
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%node%'
                                                    AND NVL (header_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId))))
                                            rh,
                                            (  SELECT SUM (total_selected)
                                                          total_selected,
                                                      SUM (nbr_goal_achieved)
                                                          nbr_achieved,
                                                      SUM (calculated_payout)
                                                          AS awards,
                                                      gl.sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      SUM (rpt.base_quantity)
                                                          base,
                                                      SUM (rpt.goal) goal,
                                                      SUM (rpt.actual_result)
                                                          actual_result
                                                 FROM rpt_hierarchy rh,
                                                      promotion p,
                                                      promo_goalquest promoGoal,
                                                      rpt_goal_selection_summary rpt,
                                                      goalquest_goallevel gl
                                                WHERE     rpt.goal_level =
                                                              gl.goallevel_id(+)
                                                      AND rpt.promotion_id =
                                                              promoGoal.promotion_id(+)
                                                      AND rpt.promotion_id =
                                                              p.promotion_id(+)
                                                      AND p.promotion_type =
                                                              'goalquest'
                                                      AND detail_node_id =
                                                              rh.node_id
                                                      AND rpt.record_type LIKE
                                                              '%node%'
                                                      AND NVL (header_node_id, 0) IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_parentNodeId)))
                                                      AND rpt.pax_status =
                                                              NVL (
                                                                  p_in_participantStatus,
                                                                  rpt.pax_status)
                                                      AND rpt.job_position =
                                                              NVL (
                                                                  p_in_jobPosition,
                                                                  rpt.job_position)
                                                      AND (   (p_in_departments
                                                                   IS NULL)
                                                           OR (    p_in_departments
                                                                       IS NOT NULL
                                                               AND rpt.department IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_departments)))))
                                                      AND (   (p_in_promotionId
                                                                   IS NULL)
                                                           OR (    p_in_promotionId
                                                                       IS NOT NULL
                                                               AND rpt.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array_varchar (
                                                                                                       p_in_promotionId)))))
                                                      AND NVL (
                                                              p.promotion_status,
                                                              ' ') =
                                                              NVL (
                                                                  p_in_promotionStatus,
                                                                  NVL (
                                                                      p.promotion_status,
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
                                                 6 AS level_6)) t)
                     WHERE p_in_nodeAndBelow = 'true'
                    UNION
                    SELECT node_name || ' Team' AS NODE_NAME,
                           node_id AS NODE_ID,
                           NVL (level_1_total_selected, 0)
                               AS LEVEL_1_SELECTED_CNT,
                           NVL (level_1_nbr_achieved, 0)
                               AS LEVEL_1_ACHIEVED_CNT,
                           NVL (level_2_total_selected, 0)
                               AS LEVEL_2_SELECTED_CNT,
                           NVL (level_2_nbr_achieved, 0)
                               AS LEVEL_2_ACHIEVED_CNT,
                           NVL (level_3_total_selected, 0)
                               AS LEVEL_3_SELECTED_CNT,
                           NVL (level_3_nbr_achieved, 0)
                               AS LEVEL_3_ACHIEVED_CNT,
                           NVL (level_4_total_selected, 0)
                               AS LEVEL_4_SELECTED_CNT,
                           NVL (level_4_nbr_achieved, 0)
                               AS LEVEL_4_ACHIEVED_CNT,
                           NVL (level_5_total_selected, 0)
                               AS LEVEL_5_SELECTED_CNT,
                           NVL (level_5_nbr_achieved, 0)
                               AS LEVEL_5_ACHIEVED_CNT,
                           NVL (level_6_total_selected, 0)
                               AS LEVEL_6_SELECTED_CNT,
                           NVL (level_6_nbr_achieved, 0)
                               AS LEVEL_6_ACHIEVED_CNT,
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
                                            actual_result
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%team%'
                                                    AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId))))
                                            rh,
                                            (  SELECT SUM (total_selected)
                                                          total_selected,
                                                      SUM (nbr_goal_achieved)
                                                          nbr_achieved,
                                                      SUM (calculated_payout)
                                                          AS awards,
                                                      gl.sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      SUM (rpt.base_quantity)
                                                          base,
                                                      SUM (rpt.goal) goal,
                                                      SUM (rpt.actual_result)
                                                          actual_result
                                                 FROM rpt_hierarchy rh,
                                                      promotion p,
                                                      promo_goalquest promoGoal,
                                                      rpt_goal_selection_summary rpt,
                                                      goalquest_goallevel gl
                                                WHERE     rpt.goal_level =
                                                              gl.goallevel_id(+)
                                                      AND rpt.promotion_id =
                                                              promoGoal.promotion_id(+)
                                                      AND rpt.promotion_id =
                                                              p.promotion_id(+)
                                                      AND detail_node_id =
                                                              rh.node_id
                                                      AND p.promotion_type =
                                                              'goalquest'
                                                      AND rpt.record_type LIKE
                                                              '%team%'
                                                      AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_parentNodeId)))
                                                      AND rpt.pax_status =
                                                              NVL (
                                                                  p_in_participantStatus,
                                                                  rpt.pax_status)
                                                      AND rpt.job_position =
                                                              NVL (
                                                                  p_in_jobPosition,
                                                                  rpt.job_position)
                                                      AND (   (p_in_departments
                                                                   IS NULL)
                                                           OR (    p_in_departments
                                                                       IS NOT NULL
                                                               AND rpt.department IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_departments)))))
                                                      AND (   (p_in_promotionId
                                                                   IS NULL)
                                                           OR (    p_in_promotionId
                                                                       IS NOT NULL
                                                               AND rpt.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array_varchar (
                                                                                                       p_in_promotionId)))))
                                                      AND NVL (
                                                              p.promotion_status,
                                                              ' ') =
                                                              NVL (
                                                                  p_in_promotionStatus,
                                                                  NVL (
                                                                      p.promotion_status,
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
                                                 6 AS level_6)) t));

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
    END prc_getGQPctAchievedChart;

    PROCEDURE prc_getGQCountAchievedChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQCountAchievedChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT SUM (level_1_achieved_cnt) AS LEVEL_1_ACHIEVED_CNT,
                   SUM (level_2_achieved_cnt) AS LEVEL_2_ACHIEVED_CNT,
                   SUM (level_3_achieved_cnt) AS LEVEL_3_ACHIEVED_CNT,
                   SUM (level_4_achieved_cnt) AS LEVEL_4_ACHIEVED_CNT,
                   SUM (level_5_achieved_cnt) AS LEVEL_5_ACHIEVED_CNT,
                   SUM (level_6_achieved_cnt) AS LEVEL_6_ACHIEVED_CNT
              FROM (SELECT node_name AS NODE_NAME,
                           node_id AS NODE_ID,
                           NVL (level_1_nbr_achieved, 0)
                               AS LEVEL_1_ACHIEVED_CNT,
                           NVL (level_2_nbr_achieved, 0)
                               AS LEVEL_2_ACHIEVED_CNT,
                           NVL (level_3_nbr_achieved, 0)
                               AS LEVEL_3_ACHIEVED_CNT,
                           NVL (level_4_nbr_achieved, 0)
                               AS LEVEL_4_ACHIEVED_CNT,
                           NVL (level_5_nbr_achieved, 0)
                               AS LEVEL_5_ACHIEVED_CNT,
                           NVL (level_6_nbr_achieved, 0)
                               AS LEVEL_6_ACHIEVED_CNT,
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
                                            actual_result
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%node%'
                                                    AND NVL (header_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId))))
                                            rh,
                                            (  SELECT SUM (total_selected)
                                                          total_selected,
                                                      SUM (nbr_goal_achieved)
                                                          nbr_achieved,
                                                      SUM (calculated_payout)
                                                          AS awards,
                                                      gl.sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      SUM (rpt.base_quantity)
                                                          base,
                                                      SUM (rpt.goal) goal,
                                                      SUM (rpt.actual_result)
                                                          actual_result
                                                 FROM rpt_hierarchy rh,
                                                      promotion p,
                                                      promo_goalquest promoGoal,
                                                      rpt_goal_selection_summary rpt,
                                                      goalquest_goallevel gl
                                                WHERE     rpt.goal_level =
                                                              gl.goallevel_id(+)
                                                      AND rpt.promotion_id =
                                                              promoGoal.promotion_id(+)
                                                      AND rpt.promotion_id =
                                                              p.promotion_id(+)
                                                      AND detail_node_id =
                                                              rh.node_id
                                                      AND p.promotion_type =
                                                              'goalquest'
                                                      AND rpt.record_type LIKE
                                                              '%node%'
                                                      AND NVL (header_node_id, 0) IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_parentNodeId)))
                                                      AND rpt.pax_status =
                                                              NVL (
                                                                  p_in_participantStatus,
                                                                  rpt.pax_status)
                                                      AND rpt.job_position =
                                                              NVL (
                                                                  p_in_jobPosition,
                                                                  rpt.job_position)
                                                      AND (   (p_in_departments
                                                                   IS NULL)
                                                           OR (    p_in_departments
                                                                       IS NOT NULL
                                                               AND rpt.department IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_departments)))))
                                                      AND (   (p_in_promotionId
                                                                   IS NULL)
                                                           OR (    p_in_promotionId
                                                                       IS NOT NULL
                                                               AND rpt.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array_varchar (
                                                                                                       p_in_promotionId)))))
                                                      AND NVL (
                                                              p.promotion_status,
                                                              ' ') =
                                                              NVL (
                                                                  p_in_promotionStatus,
                                                                  NVL (
                                                                      p.promotion_status,
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
                                                 6 AS level_6)) t)
                     WHERE p_in_nodeAndBelow = 'true'
                    UNION
                    SELECT node_name || ' Team' AS NODE_NAME,
                           node_id AS NODE_ID,
                           NVL (level_1_nbr_achieved, 0)
                               AS LEVEL_1_ACHIEVED_CNT,
                           NVL (level_2_nbr_achieved, 0)
                               AS LEVEL_2_ACHIEVED_CNT,
                           NVL (level_3_nbr_achieved, 0)
                               AS LEVEL_3_ACHIEVED_CNT,
                           NVL (level_4_nbr_achieved, 0)
                               AS LEVEL_4_ACHIEVED_CNT,
                           NVL (level_5_nbr_achieved, 0)
                               AS LEVEL_5_ACHIEVED_CNT,
                           NVL (level_6_nbr_achieved, 0)
                               AS LEVEL_6_ACHIEVED_CNT,
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
                                            actual_result
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            '%team%'
                                                    AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   p_in_parentNodeId))))
                                            rh,
                                            (  SELECT SUM (total_selected)
                                                          total_selected,
                                                      SUM (nbr_goal_achieved)
                                                          nbr_achieved,
                                                      SUM (calculated_payout)
                                                          AS awards,
                                                      gl.sequence_num,
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name,
                                                      SUM (rpt.base_quantity)
                                                          base,
                                                      SUM (rpt.goal) goal,
                                                      SUM (rpt.actual_result)
                                                          actual_result
                                                 FROM rpt_hierarchy rh,
                                                      promotion p,
                                                      promo_goalquest promoGoal,
                                                      rpt_goal_selection_summary rpt,
                                                      goalquest_goallevel gl
                                                WHERE     rpt.goal_level =
                                                              gl.goallevel_id(+)
                                                      AND rpt.promotion_id =
                                                              promoGoal.promotion_id(+)
                                                      AND rpt.promotion_id =
                                                              p.promotion_id(+)
                                                      AND detail_node_id =
                                                              rh.node_id
                                                      AND p.promotion_type =
                                                              'goalquest'
                                                      AND rpt.record_type LIKE
                                                              '%team%'
                                                      AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_parentNodeId)))
                                                      AND rpt.pax_status =
                                                              NVL (
                                                                  p_in_participantStatus,
                                                                  rpt.pax_status)
                                                      AND rpt.job_position =
                                                              NVL (
                                                                  p_in_jobPosition,
                                                                  rpt.job_position)
                                                      AND (   (p_in_departments
                                                                   IS NULL)
                                                           OR (    p_in_departments
                                                                       IS NOT NULL
                                                               AND rpt.department IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_departments)))))
                                                      AND (   (p_in_promotionId
                                                                   IS NULL)
                                                           OR (    p_in_promotionId
                                                                       IS NOT NULL
                                                               AND rpt.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array_varchar (
                                                                                                       p_in_promotionId)))))
                                                      AND NVL (
                                                              p.promotion_status,
                                                              ' ') =
                                                              NVL (
                                                                  p_in_promotionStatus,
                                                                  NVL (
                                                                      p.promotion_status,
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
                                                 6 AS level_6)) t));

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
    END prc_getGQCountAchievedChart;

    PROCEDURE prc_getGQAchievementResChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQAchievementResChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (  SELECT    gsd.last_name
                             || ' , '
                             || gsd.first_name
                             || ' '
                             || gsd.middle_init
                                 AS pax_name,
                             gsd.promotion_name AS promo_name,
                             hier.node_name AS node_name,
                             gsd.base_quantity AS base_quantity,
                             gsd.amount_to_achieve AS amount_to_achieve,
                             gsd.current_value AS current_value
                        FROM promo_goalquest promoGoal,
                             RPT_GOAL_SELECTION_DETAIL gsd
                             LEFT OUTER JOIN RPT_HIERARCHY hier
                                 ON gsd.node_id = hier.node_id
                             LEFT OUTER JOIN PROMOTION promo
                                 ON gsd.promotion_id = promo.promotion_id
                             RIGHT OUTER JOIN GOALQUEST_PAXGOAL pg
                                 ON gsd.PAX_GOAL_ID = pg.PAX_GOAL_ID
                             LEFT OUTER JOIN GOALQUEST_GOALLEVEL gl
                                 ON pg.GOALLEVEL_ID = gl.GOALLEVEL_ID
                       WHERE     NVL (gsd.node_id, 0) IN (SELECT child_node_id
                                                            FROM rpt_hierarchy_rollup
                                                           WHERE node_id IN (SELECT *
                                                                               FROM TABLE (
                                                                                        get_array_varchar (
                                                                                            p_in_parentNodeId))))
                             AND gsd.promotion_id = promoGoal.promotion_id
                             AND gsd.user_status =
                                     NVL (p_in_participantStatus,
                                          gsd.user_status)
                             AND gsd.job_position =
                                     NVL (p_in_jobPosition, gsd.job_position)
                             AND (   p_in_departments IS NULL
                                  OR gsd.department IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       p_in_departments))))
                             AND (   gsd.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     CAST (
                                                                         get_array_varchar (
                                                                             p_in_promotionId) AS ARRAY_VARCHAR)))
                                  OR p_in_promotionId IS NULL)
                    ORDER BY current_value DESC, PAX_NAME ASC)
             WHERE ROWNUM <= 20;

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
    END prc_getGQAchievementResChart;

PROCEDURE prc_getGQManagerTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
/******************************************************************************
  NAME:       prc_getGQManagerTabRes
   Author           Date           Description
  ----------       -----------    ---------------------------------------------
                                    Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J          08/29/2014      Bug Fix 56149  - Fixed SQL Syntax error in l_query       
  Suresh J          09/12/2014        Bug Fix 56398 - Fixed teamsum issue       
  Swati                10/29/2014        Bug 57636 - Reports>>GoalQuest Manager Achievement 
  Ravi Dhanekula    01/20/2016      Bug # 65311 - GoalQuest Manager Achievement reports fail to open for managers/owner of multiple org units
  nagarajs          02/15/2016      Bug 65740 - Admin View/Reports/Manager/Goal Quest Manager Achievement Report : "Total Manager Points"
                                   total mismatches between the summary table and the extracted sheet.
 nagarajs          02/17/2016      Bug 65763 - Goal Quest Manager Achievement Report :- Duplicate columns are getting displayed in the extracted excel 
                                   sheet , because of this the total is getting mismatched
  ******************************************************************************/    
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQManagerTabRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
  l_query VARCHAR2(32767);
  
  BEGIN
 l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN, --10/29/2014 Bug 57636 starts
                                fs.mgr_user_id AS MGR_USER_ID,
                                fs.manager_name AS manager_name,
                                total_nbr_pax TOTAL_PAX,
                                pax_sel_goal TOTAL_PAX_GOAL_SELECTED,
                                nbr_pax_achieving TOTAL_PAX_ACHIEVED,
                                NVL (
                                     ROUND (
                                         DECODE (
                                             fs.pax_sel_goal,
                                             0, 0,
                                               (  fs.nbr_pax_achieving
                                                / fs.pax_sel_goal)
                                             * 100),
                                         2),
                                     0)
                                     PERCENT_SEL_PAX_ACHIEVING,
                                 NVL (
                                     ROUND (
                                         DECODE (
                                             fs.total_nbr_pax,
                                             0, 0,
                                               (  fs.nbr_pax_achieving
                                                / fs.total_nbr_pax)
                                             * 100),
                                         2),
                                     0)
                                     PERCENT_TOT_PAX_ACHIEVING,
                                 NVL (
                                     ROUND (
                                         DECODE (
                                             fs.team_points,
                                             0, 0,
                                               (fs.manager_points / fs.team_points)
                                             * 100),
                                         2),
                                     0)
                                     MANAGE_OVERRIDE_PERCENTAGE,
                                team_points TOTAL_POINTS_BY_TEAM,
                                manager_points TOTAL_MGR_POINTS,
                                NVL ( ROUND (               --09/12/2014
                                     DECODE (
                                         fs.nbr_pax_achieving,
                                         0, 0,
                                         fs.manager_points / fs.nbr_pax_achieving),2),      
                                     0)
                                     manager_payout_per_achiever    
                            FROM 
                            (SELECT
                                rs.mgr_user_id AS MGR_USER_ID,
                                rs.manager_name AS manager_name,
                                SUM(NVL (rs.total_nbr_pax, 0)) total_nbr_pax,
                                SUM(NVL (rs.pax_sel_goal, 0)) pax_sel_goal,
                                SUM(NVL (rs.nbr_pax_achieving, 0)) nbr_pax_achieving,
                                SUM(NVL (rs.team_points, 0)) team_points,
                                SUM(NVL (rs.manager_points, 0)) manager_points
                            FROM    --10/29/2014 Bug 57636 ends
                            (SELECT 
                             gs.mgr_user_id AS MGR_USER_ID,
                             gs.manager_name AS manager_name,
                             NVL (gs.total_nbr_pax, 0) total_nbr_pax,
                             NVL (gs.pax_sel_goal, 0) pax_sel_goal,
                             NVL (gs.nbr_pax_achieving, 0) nbr_pax_achieving,                             
                             NVL (gs.team_points, 0) team_points,
                             NVL (gs.manager_points, 0) manager_points                             
                        FROM (  SELECT override.manager_name,
                                       mgr_user_id,
                                       SUM (rpt.total_participants) total_nbr_pax,
                                       SUM (
                                           DECODE (rpt.goal_level,
                                                   NULL, 0,
                                                   rpt.total_participants))
                                           pax_sel_goal,
                                       SUM (rpt.nbr_goal_achieved)
                                           nbr_pax_achieving,
                                       NVL (SUM (rpt.calculated_payout), 0)
                                           team_points,
                                       NVL (override.manager_points, 0)
                                           manager_points,
                                           promo.promotion_id --10/29/2014 Bug 57636
                                  FROM rpt_hierarchy rh,
                                       promo_goalquest promoGoal,
                                       promotion promo,
                                       user_node un,
                                       (  SELECT mgr_user_id,rpt.promotion_id,   --09/12/2014   
                                                    rpt.mgr_last_name
                                                 || '',''
                                                 || rpt.mgr_first_name
                                                     AS manager_name,
                                                 SUM (override_amount) manager_points
                                            FROM rpt_goal_manager_override rpt,
                                                 promotion promo
                                           WHERE     rpt.promotion_id =
                                                         promo.promotion_id
                                                 AND rpt.node_id IN (SELECT child_node_id
                                                                       FROM rpt_hierarchy_rollup
                                                                      WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       '''||p_in_parentNodeId||'''))))
                                                 AND (   ('''||p_in_promotionId||''' IS NULL)
                                                      OR (    '''||p_in_promotionId||'''
                                                                  IS NOT NULL
                                                          AND rpt.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                              get_array (
                                                                                                  '''||p_in_promotionId||''')))))
                                                 AND promo.promotion_status =
                                                         NVL ('''||p_in_promotionStatus||''',
                                                              promo.promotion_status)
                                        GROUP BY rpt.mgr_last_name,
                                                 rpt.mgr_first_name,
                                                 mgr_user_id,rpt.promotion_id) override,  --09/12/2014
                                       rpt_goal_selection_summary rpt
                                       LEFT OUTER JOIN goalquest_goallevel gl
                                           ON rpt.goal_level = gl.goallevel_id
                                 WHERE     rpt.detail_node_id = rh.node_id
                                       AND rpt.promotion_id =
                                               promoGoal.promotion_id
                                       AND promogoal.promotion_id =
                                               promo.promotion_id
                                       AND rpt.detail_node_id IN (SELECT child_node_id
                                                                    FROM rpt_hierarchy_rollup
                                                                   WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       '''||p_in_parentNodeId||'''))))
                                       AND rpt.record_type LIKE ''%node%'' --09/12/2014
                                       AND (   rpt.promotion_id IN (SELECT *
                                                                      FROM TABLE (
                                                                               CAST (
                                                                                   get_array_varchar (
                                                                                       '''||p_in_promotionId||''') AS ARRAY_VARCHAR)))
                                            OR '''||p_in_promotionId||''' IS NULL)
                                       AND promo.promotion_status =
                                               NVL ('''||p_in_promotionStatus||''',
                                                    promo.promotion_status)
                                       AND rh.node_id = un.node_id
                                       AND un.user_id = override.mgr_user_id
                                       AND promo.promotion_id = override.promotion_id   --09/12/2014
                                       AND un.role = ''own''
                                       AND un.is_primary = 1  --02/17/2016
                              GROUP BY override.manager_name,
                                       override.manager_points,
                                       override.mgr_user_id,
                                       promo.promotion_id) gs) rs --10/29/2014 Bug 57636
                    GROUP BY MGR_USER_ID,manager_name) fs --10/29/2014 Bug 57636
                    ORDER BY '|| v_sortCol ||'
      ) RS
     WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;    --08/29/2014
          
    OPEN p_out_data FOR 
   l_query;
        --getGQManagerTabResTotals
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (gs.total_nbr_pax), 0) TOTAL_PAX,
                   NVL (SUM (gs.pax_sel_goal), 0) TOTAL_PAX_GOAL_SELECTED,
                   NVL (SUM (gs.nbr_pax_achieving), 0) TOTAL_PAX_ACHIEVED,
                   NVL (
                       ROUND (
                           DECODE (
                               SUM (gs.pax_sel_goal),
                               0, 0,
                                 (  SUM (gs.nbr_pax_achieving)
                                  / SUM (gs.pax_sel_goal))
                               * 100),
                           2),
                       0)
                       PERCENT_SEL_PAX_ACHIEVING,
                   NVL (
                       ROUND (
                           DECODE (
                               SUM (gs.total_nbr_pax),
                               0, 0,
                                 (  SUM (gs.nbr_pax_achieving)
                                  / SUM (gs.total_nbr_pax))
                               * 100),
                           2),
                       0)
                       PERCENT_TOT_PAX_ACHIEVING,
                   NVL (
                       ROUND (
                           DECODE (
                               SUM (gs.team_points),
                               0, 0,
                                 (  SUM (gs.manager_points)
                                  / SUM (gs.team_points))
                               * 100),
                           2),
                       0)
                       MANAGE_OVERRIDE_PERCENTAGE,
                   NVL (SUM (gs.team_points), 0) TOTAL_POINTS_BY_TEAM,
                   NVL (SUM (gs.manager_points), 0) TOTAL_MGR_POINTS,
                   NVL (
                       ROUND (DECODE (
                           SUM (gs.nbr_pax_achieving),
                           0, 0,
                           SUM (gs.manager_points) / SUM (gs.nbr_pax_achieving)),2),
                       0)
                       manager_payout_per_achiever
              FROM (  SELECT override.manager_name,
                             SUM (rpt.total_participants) total_nbr_pax,
                             SUM (
                                 DECODE (rpt.goal_level,
                                         NULL, 0,
                                         rpt.total_participants))
                                 pax_sel_goal,
                             SUM (rpt.nbr_goal_achieved) nbr_pax_achieving,
                             NVL (SUM (rpt.calculated_payout), 0) team_points,
                             NVL (override.manager_points, 0) manager_points
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion promo,
                             user_node un,
                             (  SELECT mgr_user_id,rpt.promotion_id,  --09/12/2014
                                          rpt.mgr_last_name
                                       || ','
                                       || rpt.mgr_first_name
                                           AS manager_name,
                                       SUM (override_amount) manager_points
                                  FROM rpt_goal_manager_override rpt,
                                       promotion promo
                                 WHERE     rpt.promotion_id = promo.promotion_id
                                       AND rpt.node_id IN (SELECT child_node_id
                                                             FROM rpt_hierarchy_rollup
                                                            WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                                       AND (   (p_in_promotionId IS NULL)
                                            OR (    p_in_promotionId IS NOT NULL
                                                AND rpt.promotion_id IN (SELECT *
                                                                           FROM TABLE (
                                                                                    get_array (
                                                                                        p_in_promotionId)))))
                                       AND promo.promotion_status =
                                               NVL (p_in_promotionStatus,
                                                    promo.promotion_status)
                              GROUP BY rpt.mgr_last_name,
                                       rpt.mgr_first_name,
                                       mgr_user_id,rpt.promotion_id) override,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     rpt.detail_node_id = rh.node_id
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND promogoal.promotion_id = promo.promotion_id
                             AND rpt.detail_node_id IN (SELECT child_node_id
                                                          FROM rpt_hierarchy_rollup
                                                         WHERE node_id IN (SELECT *
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                             AND rpt.record_type LIKE '%node%'   --09/12/2014
                             AND (   rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     CAST (
                                                                         get_array_varchar (
                                                                             p_in_promotionId) AS ARRAY_VARCHAR)))
                                  OR p_in_promotionId IS NULL)
                             AND promo.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          promo.promotion_status)
                             AND rh.node_id = un.node_id
                             AND un.user_id = override.mgr_user_id
                             AND promo.promotion_id = override.promotion_id   --09/12/2014
                             AND un.role = 'own'
                             AND un.is_primary = 1  --02/17/2016
                    GROUP BY override.manager_name, override.manager_points,
                    promo.promotion_id              --02/15/2016 
                    ) gs;



        --getGQManagerResSize        
            SELECT COUNT (1) INTO p_out_size_data
              FROM (  SELECT override.manager_name,
                             SUM (rpt.total_participants) total_nbr_pax,
                             SUM (
                                 DECODE (rpt.goal_level,
                                         NULL, 0,
                                         rpt.total_participants))
                                 pax_sel_goal,
                             SUM (rpt.nbr_goal_achieved) nbr_pax_achieving,
                             NVL (SUM (rpt.calculated_payout), 0) team_points,
                             NVL (override.manager_points, 0) manager_points
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion promo,
                             user_node un,
                             (  SELECT mgr_user_id,rpt.promotion_id,  --02/15/2016
                                          rpt.mgr_last_name
                                       || ','
                                       || rpt.mgr_first_name
                                           AS manager_name,
                                       SUM (override_amount) manager_points
                                  FROM rpt_goal_manager_override rpt,
                                       promotion promo
                                 WHERE     rpt.promotion_id = promo.promotion_id
                                       AND rpt.node_id IN (SELECT child_node_id
                                                             FROM rpt_hierarchy_rollup
                                                            WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                                       AND (   (p_in_promotionId IS NULL)
                                            OR (    p_in_promotionId IS NOT NULL
                                                AND rpt.promotion_id IN (SELECT *
                                                                           FROM TABLE (
                                                                                    get_array (
                                                                                        p_in_promotionId)))))
                                       AND promo.promotion_status =
                                               NVL (p_in_promotionStatus,
                                                    promo.promotion_status)
                              GROUP BY rpt.mgr_last_name,
                                       rpt.mgr_first_name,
                                       rpt.promotion_id, --02/15/2016
                                       mgr_user_id) override,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     rpt.detail_node_id = rh.node_id
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND promogoal.promotion_id = promo.promotion_id
                             AND rpt.detail_node_id IN (SELECT child_node_id
                                                          FROM rpt_hierarchy_rollup
                                                         WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                             AND rpt.record_type LIKE '%node%'  --09/12/2014
                             AND (   rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     CAST (
                                                                         get_array_varchar (
                                                                             p_in_promotionId) AS ARRAY_VARCHAR)))
                                  OR p_in_promotionId IS NULL)
                             AND promo.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          promo.promotion_status)
                             AND rh.node_id = un.node_id
                             AND un.user_id = override.mgr_user_id
                             AND promo.promotion_id = override.promotion_id   --02/15/2016 
                             AND un.role = 'own'
                             AND un.is_primary = 1  --02/17/2016
                    GROUP BY override.manager_name, override.manager_points,
                    promo.promotion_id              --02/15/2016 
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
    END prc_getGQManagerTabRes;

    PROCEDURE prc_getMgrTotalPtsEarnedChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getMgrTotalPtsEarnedChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (  SELECT mgr_user_id AS manager_user_id,
                             rpt.mgr_last_name || ',' || rpt.mgr_first_name
                                 AS manager_name,
                             SUM (override_amount) manager_points
                        FROM rpt_goal_manager_override rpt, promotion promo
                       WHERE     rpt.promotion_id = promo.promotion_id
                             AND rpt.node_id IN (SELECT child_node_id
                                                   FROM rpt_hierarchy_rollup
                                                  WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                             AND (   (p_in_promotionId IS NULL)
                                  OR (    p_in_promotionId IS NOT NULL
                                      AND rpt.promotion_id IN (SELECT *
                                                                 FROM TABLE (
                                                                          get_array (
                                                                              p_in_promotionId)))))
                             AND promo.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          promo.promotion_status)
                    GROUP BY rpt.mgr_last_name, rpt.mgr_first_name, mgr_user_id
                    ORDER BY manager_points DESC)
             WHERE ROWNUM <= 20;

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

/******************************************************************************
  NAME:       prc_getGQManagerDetailTabRes
  Date                 Author           Description
  ----------           ---------------  ------------------------------------------------
                                        Initial..(the queries were in Java before the release 5.4. So no comments available so far)
  Suresh J               09/01/2014     Bug Fix 56149  - Fixed SQL error      
  Suresh J              09/11/2014     Bug Fix 56251 - Updated filter condition to retrive all promotion names  
  Suresh J              09/12/2014    Bug Fix 56398 - Fixed the teamsum issue                            
  Ravi Dhanekula        01/20/2016    Bug 65311 - GoalQuest Manager Achievement reports fail to open for managers/owner of multiple org units
  ******************************************************************************/

    PROCEDURE prc_getGQManagerDetailTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQManagerDetailTabRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    
    l_query  := 'SELECT *
              FROM ( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||'SELECT ROWNUM RN,
                             gs.manager_name manager_name,
                             NVL (gs.total_nbr_pax, 0) TOTAL_PAX,
                             NVL (gs.pax_sel_goal, 0) TOTAL_PAX_GOAL_SELECTED,
                             NVL (gs.nbr_pax_achieving, 0) TOTAL_PAX_ACHIEVED,
                             NVL (
                                 ROUND (
                                     DECODE (
                                         gs.pax_sel_goal,
                                         0, 0,
                                           (  gs.nbr_pax_achieving
                                            / gs.pax_sel_goal)
                                         * 100),
                                     2),
                                 0)
                                 PERCENT_SEL_PAX_ACHIEVING,
                             NVL (
                                 ROUND (
                                     DECODE (
                                         gs.total_nbr_pax,
                                         0, 0,
                                           (  gs.nbr_pax_achieving
                                            / gs.total_nbr_pax)
                                         * 100),
                                     2),
                                 0)
                                 PERCENT_TOT_PAX_ACHIEVING,
                             NVL (
                                 ROUND (
                                     DECODE (
                                         gs.team_points,
                                         0, 0,
                                           (gs.manager_points / gs.team_points)
                                         * 100),
                                     2),
                                 0)
                                 MANAGE_OVERRIDE_PERCENTAGE,
                             NVL (gs.team_points, 0) TOTAL_POINTS_BY_TEAM,
                             NVL (gs.manager_points, 0) TOTAL_MGR_POINTS,
                             NVL (
                                 ROUND (DECODE (    --09/12/2014
                                     gs.nbr_pax_achieving,
                                     0, 0,
                                     gs.manager_points / gs.nbr_pax_achieving),2),
                                 0)
                                 manager_payout_per_achiever,
                             gs.promotion_name AS promo_name
                        FROM (  SELECT override.manager_name,
                                       rpt.promotion_id,
                                       SUM (rpt.total_participants) total_nbr_pax,
                                       SUM (
                                           DECODE (rpt.goal_level,
                                                   NULL, 0,
                                                   rpt.total_participants))
                                           pax_sel_goal,
                                       SUM (rpt.nbr_goal_achieved)
                                           nbr_pax_achieving,
                                       NVL (SUM (rpt.calculated_payout), 0)
                                           team_points,
                                       NVL (override.manager_points, 0)
                                           manager_points,
                                       promo.promotion_name promotion_name
                                  FROM rpt_hierarchy rh,
                                       promo_goalquest promoGoal,
                                       promotion promo,
                                       user_node un,
                                       (  SELECT mgr_user_id,
                                                    rpt.mgr_last_name
                                                 || '',''
                                                 || rpt.mgr_first_name
                                                     AS manager_name,
                                                 rpt.promotion_id,
                                                 SUM (override_amount) manager_points
                                            FROM rpt_goal_manager_override rpt,
                                                 promotion promo
                                           WHERE     rpt.promotion_id =
                                                         promo.promotion_id
                                                 AND rpt.mgr_user_id =
                                                         '''||p_in_managerUserId||'''
                                                 AND (   ('''||p_in_promotionId||''' IS NULL)
                                                      OR (    '''||p_in_promotionId||'''
                                                                  IS NOT NULL
                                                          AND rpt.promotion_id IN (SELECT *
                                                                                     FROM TABLE (
                                                                                              get_array (
                                                                                                  '''||p_in_promotionId||''')))))
                                                 AND promo.promotion_status =
                                                         NVL ('''||p_in_promotionStatus||''',
                                                              promo.promotion_status)
                                        GROUP BY rpt.mgr_last_name,
                                                 rpt.mgr_first_name,
                                                 mgr_user_id,
                                                 rpt.promotion_id) override,
                                       rpt_goal_selection_summary rpt
                                       LEFT OUTER JOIN goalquest_goallevel gl
                                           ON rpt.goal_level = gl.goallevel_id
                                 WHERE     rpt.detail_node_id = rh.node_id
                                       AND rpt.promotion_id =
                                               promoGoal.promotion_id
                                       AND promogoal.promotion_id =
                                               promo.promotion_id
                                       AND rpt.detail_node_id IN (SELECT child_node_id
                                                                    FROM rpt_hierarchy_rollup
                                                                   WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (
                                                                       '''||p_in_parentNodeId||'''))))
                                       AND rpt.record_type LIKE ''%node%''    --09/12/2014
                                       AND (   rpt.promotion_id IN (SELECT *
                                                                      FROM TABLE (
                                                                               CAST (
                                                                                   get_array_varchar (
                                                                                       '''||p_in_promotionId||''') AS ARRAY_VARCHAR)))
                                            OR '''||p_in_promotionId||''' IS NULL)
                                       AND promo.promotion_status =
                                               NVL ('''||p_in_promotionStatus||''',
                                                    promo.promotion_status)
                                       AND rh.node_id = un.node_id
                                       AND un.user_id = override.mgr_user_id
                                       AND promo.promotion_id =       --09/12/2014
                                               override.promotion_id  --09/12/2014 
                                       AND un.role = ''own''
                              GROUP BY override.manager_name,
                                       override.manager_points,
                                       rpt.promotion_id,
                                       promo.promotion_name) gs
                    ORDER BY '|| v_sortCol ||'
      ) RS
     WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ; --09/01/2014

    OPEN p_out_data FOR 
   l_query;
    --getGQManagerDetailResSize        
            SELECT COUNT (1) INTO p_out_size_data
              FROM (  SELECT override.manager_name,
                             rpt.promotion_id,
                             SUM (rpt.total_participants) total_nbr_pax,
                             SUM (
                                 DECODE (rpt.goal_level,
                                         NULL, 0,
                                         rpt.total_participants))
                                 pax_sel_goal,
                             SUM (rpt.nbr_goal_achieved) nbr_pax_achieving,
                             NVL (SUM (rpt.calculated_payout), 0) team_points,
                             NVL (override.manager_points, 0) manager_points
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion promo,
                             user_node un,
                             (  SELECT mgr_user_id,
                                          rpt.mgr_last_name
                                       || ','
                                       || rpt.mgr_first_name
                                           AS manager_name,
                                       rpt.promotion_id,
                                       SUM (override_amount) manager_points
                                  FROM rpt_goal_manager_override rpt,
                                       promotion promo
                                 WHERE     rpt.promotion_id = promo.promotion_id
                                       AND rpt.mgr_user_id = p_in_managerUserId
                                       AND (   (p_in_promotionId IS NULL)
                                            OR (    p_in_promotionId IS NOT NULL
                                                AND rpt.promotion_id IN (SELECT *
                                                                           FROM TABLE (
                                                                                    get_array (
                                                                                        p_in_promotionId)))))
                                       AND promo.promotion_status =
                                               NVL (p_in_promotionStatus,
                                                    promo.promotion_status)
                              GROUP BY rpt.mgr_last_name,
                                       rpt.mgr_first_name,
                                       mgr_user_id,
                                       rpt.promotion_id) override,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     rpt.detail_node_id = rh.node_id
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND promogoal.promotion_id = promo.promotion_id
                             AND rpt.detail_node_id IN (SELECT child_node_id
                                                          FROM rpt_hierarchy_rollup
                                                         WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                             AND rpt.record_type LIKE '%node%'  --09/12/2014
                             AND (   rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     CAST (
                                                                         get_array_varchar (
                                                                             p_in_promotionId) AS ARRAY_VARCHAR)))
                                  OR p_in_promotionId IS NULL)
                             AND promo.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          promo.promotion_status)
                             AND rh.node_id = un.node_id
                             AND un.user_id = override.mgr_user_id
                             AND promo.promotion_id = override.promotion_id    --09/12/2014
                             AND un.role = 'own'
                    GROUP BY override.manager_name,
                             override.manager_points,
                             rpt.promotion_id) gs;



        --getGQManagerDetailTabResTotals
        OPEN p_out_totals_data FOR
            SELECT NVL (SUM (gs.total_nbr_pax), 0) TOTAL_PAX,
                   NVL (SUM (gs.pax_sel_goal), 0) TOTAL_PAX_GOAL_SELECTED,
                   NVL (SUM (gs.nbr_pax_achieving), 0) TOTAL_PAX_ACHIEVED,
                   NVL (
                       ROUND (
                           DECODE (
                               SUM (gs.pax_sel_goal),
                               0, 0,
                                 (  SUM (gs.nbr_pax_achieving)
                                  / SUM (gs.pax_sel_goal))
                               * 100),
                           2),
                       0)
                       PERCENT_SEL_PAX_ACHIEVING,
                   NVL (
                       ROUND (
                           DECODE (
                               SUM (gs.total_nbr_pax),
                               0, 0,
                                 (  SUM (gs.nbr_pax_achieving)
                                  / SUM (gs.total_nbr_pax))
                               * 100),
                           2),
                       0)
                       PERCENT_TOT_PAX_ACHIEVING,
                   NVL (
                       ROUND (
                           DECODE (
                               SUM (gs.team_points),
                               0, 0,
                                 (  SUM (gs.manager_points)
                                  / SUM (gs.team_points))
                               * 100),
                           2),
                       0)
                       MANAGE_OVERRIDE_PERCENTAGE,
                   NVL (SUM (gs.team_points), 0) TOTAL_POINTS_BY_TEAM,
                   NVL (SUM (gs.manager_points), 0) TOTAL_MGR_POINTS,
                   NVL (
                       ROUND (DECODE (
                           SUM (gs.nbr_pax_achieving),
                           0, 0,
                           SUM (gs.manager_points) / SUM (gs.nbr_pax_achieving)),2),
                       0)
                       AS manager_payout_per_achiever
              FROM (  SELECT override.manager_name,
                             rpt.promotion_id,
                             SUM (rpt.total_participants) total_nbr_pax,
                             SUM (
                                 DECODE (rpt.goal_level,
                                         NULL, 0,
                                         rpt.total_participants))
                                 pax_sel_goal,
                             SUM (rpt.nbr_goal_achieved) nbr_pax_achieving,
                             NVL (SUM (rpt.calculated_payout), 0) team_points,
                             NVL (override.manager_points, 0) manager_points
                        FROM rpt_hierarchy rh,
                             promo_goalquest promoGoal,
                             promotion promo,
                             user_node un,
                             (  SELECT mgr_user_id,
                                          rpt.mgr_last_name
                                       || ','
                                       || rpt.mgr_first_name
                                           AS manager_name,
                                       rpt.promotion_id,
                                       SUM (override_amount) manager_points
                                  FROM rpt_goal_manager_override rpt,
                                       promotion promo
                                 WHERE     rpt.promotion_id = promo.promotion_id
                                       AND rpt.mgr_user_id = p_in_managerUserId
                                       AND (   (p_in_promotionId IS NULL)
                                            OR (    p_in_promotionId IS NOT NULL
                                                AND rpt.promotion_id IN (SELECT *
                                                                           FROM TABLE (
                                                                                    get_array (
                                                                                        p_in_promotionId)))))
                                       AND promo.promotion_status =
                                               NVL (p_in_promotionStatus,
                                                    promo.promotion_status)
                              GROUP BY rpt.mgr_last_name,
                                       rpt.mgr_first_name,
                                       mgr_user_id,
                                       rpt.promotion_id) override,
                             rpt_goal_selection_summary rpt
                             LEFT OUTER JOIN goalquest_goallevel gl
                                 ON rpt.goal_level = gl.goallevel_id
                       WHERE     rpt.detail_node_id = rh.node_id
                             AND rpt.promotion_id = promoGoal.promotion_id
                             AND promogoal.promotion_id = promo.promotion_id
                             AND rpt.detail_node_id IN (SELECT child_node_id
                                                          FROM rpt_hierarchy_rollup
                                                         WHERE node_id IN (SELECT *--01/20/2016      Bug # 65311
                                                          FROM TABLE (
                                                                   get_array_varchar (p_in_parentNodeId))))
                             AND rpt.record_type LIKE '%node%'
                             AND (   rpt.promotion_id IN (SELECT *
                                                            FROM TABLE (
                                                                     CAST (
                                                                         get_array_varchar (
                                                                             p_in_promotionId) AS ARRAY_VARCHAR)))
                                  OR p_in_promotionId IS NULL)
                             AND promo.promotion_status =
                                     NVL (p_in_promotionStatus,
                                          promo.promotion_status)
                             AND rh.node_id = un.node_id
                             AND un.user_id = override.mgr_user_id   
                             AND promo.promotion_id = override.promotion_id  --09/12/2014
                             AND un.role = 'own'
                    GROUP BY override.manager_name,
                             override.manager_points,
                             rpt.promotion_id) gs;

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
    END prc_getGQManagerDetailTabRes;

    PROCEDURE prc_getGQProgramSummaryTabRes (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId         IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR,
        p_out_size_data             OUT NUMBER,
        p_out_totals_data           OUT SYS_REFCURSOR)
    IS
    /* ******************************************************************************
       Person      Date                Comments
       ---------   ----------          -----------------------------------------------------
       Swati        09/11/2014        Bug 56323 - Reports - GoalQuest Program Summary Calculation Issues    
      Suresh J     10/08/2014        Bug 57309 - Fixed Goal Not Achieved to exclue pax who are not selected Goal at all
     Suresh J     10/27/2014         Bug 57613 -- Change Filter not working for pax status  
    *******************************************************************************/
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQProgramSummaryTabRes' ;
        v_stage                   VARCHAR2 (500);
        v_sortCol      VARCHAR2(200);
        l_query VARCHAR2(32767);
    BEGIN
    
    l_query  := 'SELECT *
              FROM( ';
    
    v_sortCol := ' '|| p_in_sortColName || ' ' || p_in_sortedBy;   
   
  l_query := l_query ||'  '||' WITH pivot
                         AS (SELECT achieved,
                                    goal_achieved,
                                    SUM (goal_achieved) OVER () total_pax,
                                    base,
                                    actual_result,
                                    goal_value
                               FROM (SELECT rh.node_id AS node_id,
                                            rh.node_name node_name,
                                            NVL (achieved, 1) achieved,
                                            goal_achieved,
                                            base,
                                            actual_result,
                                            goal_value
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            ''%node%''
                                                    AND NVL (header_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   '''||p_in_parentNodeId||'''))))
                                            rh,
                                            (  SELECT r.node_id,
                                                      COUNT (
                                                          CASE
                                                              WHEN pax_goal_id
                                                                       IS NOT NULL
                                                              THEN
                                                                  NVL (achieved,
                                                                       0)
                                                              ELSE
                                                                  NULL
                                                          END)
                                                          goal_achieved,
                                                      NVL (SUM (current_value),
                                                           0)
                                                          actual_result,
                                                      NVL (
                                                          SUM (amount_to_achieve),
                                                          0)
                                                          goal_value,
                                                      NVL (SUM (base_quantity),
                                                           0)
                                                          base,
                                                      achieved
                                                 FROM rpt_goal_selection_detail rpt,
                                                      rpt_hierarchy_rollup r,
                                                      promotion p,
                                                      promo_goalquest promoGoal
                                                WHERE  rpt.level_id is not null   --10/08/2014      
                                                      AND   rpt.node_id =
                                                              r.child_node_id
                                                      AND rpt.promotion_id =
                                                              promoGoal.promotion_id(+)
                                                      AND rpt.promotion_id =
                                                              p.promotion_id(+)
                                                      AND rpt.user_status = NVL('''||p_in_participantStatus||''',rpt.user_status)     --10/27/2014
                                                      AND (   ('''||p_in_promotionId||'''
                                                                   IS NULL)
                                                           OR (    '''||p_in_promotionId||'''
                                                                       IS NOT NULL
                                                               AND P.promotion_id IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array (
                                                                                                     '''||p_in_promotionId||''')))))
                                                      AND NVL (
                                                              p.promotion_status,
                                                              '' '') =
                                                              NVL (
                                                                  '''||p_in_promotionStatus||''',
                                                                  NVL (
                                                                      p.promotion_status,
                                                                      '' ''))
                                             GROUP BY r.node_id, achieved) dtl
                                      WHERE rh.node_id = dtl.node_id(+)
                                     UNION
                                     SELECT rh.node_id AS node_id,
                                            rh.node_name || ''Team'' node_name,
                                            NVL (achieved, 1) achieved,
                                            goal_achieved,
                                            base,
                                            actual_result,
                                            goal_value
                                       FROM (SELECT DISTINCT
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM rpt_hierarchy rh,
                                                    rpt_goal_selection_summary rpt
                                              WHERE     detail_node_id =
                                                            rh.node_id
                                                    AND rpt.record_type LIKE
                                                            ''%team%''
                                                    AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                      FROM TABLE (
                                                                                               get_array_varchar (
                                                                                                   '''||p_in_parentNodeId||'''))))
                                            rh,
                                            (  SELECT rpt.node_id,
                                                      COUNT (
                                                          CASE
                                                              WHEN pax_goal_id
                                                                       IS NOT NULL
                                                              THEN
                                                                  NVL (achieved,
                                                                       0)
                                                              ELSE
                                                                  NULL
                                                          END)
                                                          goal_achieved,
                                                      NVL (SUM (current_value),
                                                           0)
                                                          actual_result,
                                                      NVL (
                                                          SUM (amount_to_achieve),
                                                          0)
                                                          goal_value,
                                                      NVL (SUM (base_quantity),
                                                           0)
                                                          base,
                                                      achieved
                                                 FROM rpt_goal_selection_detail rpt,
                                                      promotion p,
                                                      promo_goalquest promoGoal
                                                WHERE   rpt.level_id is not null   --10/08/2014
                                                      AND  rpt.node_id IN (SELECT *
                                                                            FROM TABLE (
                                                                                     get_array_varchar (
                                                                                         '''||p_in_parentNodeId||''')))
                                                      AND rpt.promotion_id =
                                                              promoGoal.promotion_id(+)
                                                      AND rpt.promotion_id =
                                                              p.promotion_id(+)
                                                      AND rpt.user_status = NVL('''||p_in_participantStatus||''',rpt.user_status)     --10/27/2014
                                                      AND (   ('''||p_in_promotionId||'''
                                                                   IS NULL)
                                                           OR (    '''||p_in_promotionId||'''
                                                                       IS NOT NULL
                                                               AND P.promotion_id IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array (
                                                                                                     '''||p_in_promotionId||''')))))
                                                      AND NVL (
                                                              p.promotion_status,
                                                              '' '') =
                                                              NVL (
                                                                  '''||p_in_promotionStatus||''',
                                                                  NVL (
                                                                      p.promotion_status,
                                                                      '' ''))
                                             GROUP BY rpt.node_id, achieved)
                                            dtl
                                      WHERE rh.node_id = dtl.node_id(+)
                                     UNION ALL
                                     SELECT 0 node_id,
                                            '' '' node_name,
                                            0 achieved,
                                            0 goal_achieved,
                                            0 base,
                                            0 actual_result,
                                            0 goal_value
                                       FROM DUAL
                                     UNION ALL
                                     SELECT 0 node_id,
                                            '' '' node_name,
                                            1 achieved,
                                            0 goal_achieved,
                                            0 base,
                                            0 actual_result,
                                            0 goal_value
                                       FROM DUAL))
                      SELECT fnc_cms_asset_code_val_extr (
                                 ''report.goalquest.summary'',
                                 ''ACHIEVED_NO_GOAL'',
                                 '''||p_in_languageCode||''')
                                 goals,
                             NVL (SUM (goal_achieved), 0) nbr_pax,
                             DECODE (
                                 NVL (total_pax, 0),
                                 0, 0,
                                 ROUND (
                                     (  (  NVL (SUM (goal_achieved), 0)
                                         / NVL (total_pax, 0))
                                      * 100),
                                     2))
                                 perc_of_pax,
                             NVL (SUM (base), 0) total_baseline_objective,
                             NVL (SUM (goal_value), 0) total_goal_value,
                             NVL (SUM (actual_result), 0) total_actual_production,
                             DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                         DECODE(NVL (SUM (base), 0),0,NVL (SUM (actual_result), 0), 
                                                ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (base), 0)) * 100)
                                                        / NVL (SUM (base), 0)),2))
                                    )-- 09/11/2014 Bug 56323
                                 perc_increase_baseline,
                             (NVL (SUM (actual_result), 0) - NVL (SUM (base), 0))
                                 unit_dol_inc_baseline,
                             DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                     DECODE(NVL (SUM (goal_value), 0),0,NVL (SUM (actual_result), 0), 
                                            ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (goal_value), 0)) * 100)
                                                    / NVL (SUM (goal_value), 0)),2))
                                    )    -- 09/11/2014 Bug 56323        
                                 perc_increase_goal,
                             (NVL (SUM (actual_result), 0) - NVL (SUM (goal_value), 0))    -- 09/11/2014 Bug 56323    
                                 unit_dol_inc_goal
                        FROM pivot
                       WHERE achieved = 0
                    GROUP BY total_pax
                    UNION
                      SELECT fnc_cms_asset_code_val_extr (
                                 ''report.goalquest.summary'',
                                 ''ACHIEVED_GOAL'',
                                 '''||p_in_languageCode||''')
                                 goals,
                             NVL (SUM (goal_achieved), 0) nbr_pax,
                             DECODE (
                                 NVL (total_pax, 0),
                                 0, 0,
                                 ROUND (
                                     (  (  NVL (SUM (goal_achieved), 0)
                                         / NVL (total_pax, 0))
                                      * 100),
                                     2))
                                 perc_of_pax,
                             NVL (SUM (base), 0) total_baseline_objective,
                             NVL (SUM (goal_value), 0) total_goal_value,
                             NVL (SUM (actual_result), 0) total_actual_production,
                             DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                         DECODE(NVL (SUM (base), 0),0,NVL (SUM (actual_result), 0), 
                                                ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (base), 0)) * 100)
                                                        / NVL (SUM (base), 0)),2))
                                    )-- 09/11/2014 Bug 56323
                                 perc_increase_baseline,
                             (NVL (SUM (actual_result), 0) - NVL (SUM (base), 0))
                                 unit_dol_inc_baseline,
                             DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                     DECODE(NVL (SUM (goal_value), 0),0,NVL (SUM (actual_result), 0), 
                                            ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (goal_value), 0)) * 100)
                                                    / NVL (SUM (goal_value), 0)),2))
                                    )    -- 09/11/2014 Bug 56323        
                                 perc_increase_goal,
                             (NVL (SUM (actual_result), 0) - NVL (SUM (goal_value), 0))    -- 09/11/2014 Bug 56323    
                                 unit_dol_inc_goal
                        FROM pivot
                       WHERE achieved = 1
                    GROUP BY total_pax
                    ORDER BY '|| v_sortCol||' )';
--     WHERE RN > ' ||p_in_rowNumStart||'  AND RN < '|| p_in_rowNumEnd ;
        
    OPEN p_out_data FOR 
   l_query;
     --getGQProgramSummaryTabResSize       
            SELECT COUNT (*) INTO p_out_size_data
              FROM (WITH pivot
                         AS (SELECT rh.node_id AS node_id,
                                    rh.node_name node_name,
                                    achieved,
                                    goal_achieved,
                                    total_pax,
                                    base,
                                    actual_result,
                                    goal_value
                               FROM (SELECT DISTINCT
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM rpt_hierarchy rh,
                                            rpt_goal_selection_summary rpt
                                      WHERE     detail_node_id = rh.node_id
                                            AND rpt.record_type LIKE '%node%'
                                            AND NVL (header_node_id, 0) IN (SELECT *
                                                                              FROM TABLE (
                                                                                       get_array_varchar (
                                                                                           p_in_parentNodeId))))
                                    rh,
                                    (  SELECT r.node_id,
                                              NVL (SUM (achieved), 0)
                                                  goal_achieved,
                                              COUNT (user_id) total_pax,
                                              NVL (SUM (current_value), 0)
                                                  actual_result,
                                              NVL (SUM (amount_to_achieve), 0)
                                                  goal_value,
                                              NVL (SUM (base_quantity), 0) base,
                                              NVL (achieved, 0) achieved
                                         FROM rpt_goal_selection_detail rpt,
                                              rpt_hierarchy_rollup r,
                                              promotion p,
                                              promo_goalquest promoGoal
                                        WHERE rpt.level_id is not null   --10/08/2014     
                                              AND rpt.node_id = r.child_node_id
                                              AND rpt.promotion_id =
                                                      promoGoal.promotion_id(+)
                                              AND rpt.promotion_id =
                                                      p.promotion_id(+)
                                              AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                                              AND (   (p_in_promotionId IS NULL)
                                                   OR (    p_in_promotionId
                                                               IS NOT NULL
                                                       AND P.promotion_id IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array (
                                                                                             p_in_promotionId)))))
                                              AND NVL (p.promotion_status, ' ') =
                                                      NVL (
                                                          p_in_promotionStatus,
                                                          NVL (
                                                              p.promotion_status,
                                                              ' '))
                                     GROUP BY r.node_id, NVL (achieved, 0)) dtl
                              WHERE rh.node_id = dtl.node_id(+)
                             UNION
                             SELECT rh.node_id AS node_id,
                                    rh.node_name || 'Team' node_name,
                                    achieved,
                                    goal_achieved,
                                    total_pax,
                                    base,
                                    actual_result,
                                    goal_value
                               FROM (SELECT DISTINCT
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM rpt_hierarchy rh,
                                            rpt_goal_selection_summary rpt
                                      WHERE     detail_node_id = rh.node_id
                                            AND rpt.record_type LIKE '%team%'
                                            AND NVL (detail_node_id, 0) IN (SELECT *
                                                                              FROM TABLE (
                                                                                       get_array_varchar (
                                                                                           p_in_parentNodeId))))
                                    rh,
                                    (  SELECT rpt.node_id,
                                              NVL (SUM (achieved), 0)
                                                  goal_achieved,
                                              COUNT (user_id) total_pax,
                                              NVL (SUM (current_value), 0)
                                                  actual_result,
                                              NVL (SUM (amount_to_achieve), 0)
                                                  goal_value,
                                              NVL (SUM (base_quantity), 0) base,
                                              NVL (achieved, 0) achieved
                                         FROM rpt_goal_selection_detail rpt,
                                              promotion p,
                                              promo_goalquest promoGoal
                                        WHERE   rpt.level_id is not null   --10/08/2014 
                                              AND rpt.node_id IN (SELECT *
                                                                    FROM TABLE (
                                                                             get_array_varchar (
                                                                                 p_in_parentNodeId)))
                                              AND rpt.promotion_id =
                                                      promoGoal.promotion_id(+)
                                              AND rpt.promotion_id =
                                                      p.promotion_id(+)
                                              AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                                              AND (   (p_in_promotionId IS NULL)
                                                   OR (    p_in_promotionId
                                                               IS NOT NULL
                                                       AND P.promotion_id IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array (
                                                                                             p_in_promotionId)))))
                                              AND NVL (p.promotion_status, ' ') =
                                                      NVL (
                                                          p_in_promotionStatus,
                                                          NVL (
                                                              p.promotion_status,
                                                              ' '))
                                     GROUP BY rpt.node_id, NVL (achieved, 0))
                                    dtl
                              WHERE rh.node_id = dtl.node_id(+))
                    SELECT 'Goals Not Achieved' goals,
                           NVL (SUM (goal_achieved), 0) nbr_pax,
                           DECODE (
                               NVL (SUM (total_pax), 0),
                               0, 0,
                               ROUND (
                                   (  (  NVL (SUM (goal_achieved), 0)
                                       / NVL (SUM (total_pax), 0))
                                    * 100),
                                   2))
                               perc_of_pax,
                           NVL (SUM (base), 0) total_baseline_objective,
                           NVL (SUM (goal_value), 0) total_goal_value,
                           NVL (SUM (actual_result), 0) total_actual_production,
                           DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                         DECODE(NVL (SUM (base), 0),0,NVL (SUM (actual_result), 0), 
                                                ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (base), 0)) * 100)
                                                        / NVL (SUM (base), 0)),2))
                                    )-- 09/11/2014 Bug 56323
                               perc_increase_baseline,
                           (NVL (SUM (actual_result), 0) - NVL (SUM (base), 0))
                               unit_dol_inc_baseline,
                           DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                     DECODE(NVL (SUM (goal_value), 0),0,NVL (SUM (actual_result), 0), 
                                            ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (goal_value), 0)) * 100)
                                                    / NVL (SUM (goal_value), 0)),2))
                                    )    -- 09/11/2014 Bug 56323        
                               perc_increase_goal,
                           (NVL (SUM (actual_result), 0) - NVL (SUM (goal_value), 0))    -- 09/11/2014 Bug 56323    
                               unit_dol_inc_goal
                      FROM pivot
                     WHERE achieved = 1
                    UNION
                    SELECT 'Goals Achieved' goals,
                           NVL (SUM (goal_achieved), 0) nbr_pax,
                           DECODE (
                               NVL (SUM (total_pax), 0),
                               0, 0,
                               ROUND (
                                   (  (  NVL (SUM (goal_achieved), 0)
                                       / NVL (SUM (total_pax), 0))
                                    * 100),
                                   2))
                               perc_of_pax,
                           NVL (SUM (base), 0) total_baseline_objective,
                           NVL (SUM (goal_value), 0) total_goal_value,
                           NVL (SUM (actual_result), 0) total_actual_production,
                           DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                         DECODE(NVL (SUM (base), 0),0,NVL (SUM (actual_result), 0), 
                                                ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (base), 0)) * 100)
                                                        / NVL (SUM (base), 0)),2))
                                    )-- 09/11/2014 Bug 56323
                               perc_increase_baseline,
                           (NVL (SUM (actual_result), 0) - NVL (SUM (base), 0))
                               unit_dol_inc_baseline,
                           DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                     DECODE(NVL (SUM (goal_value), 0),0,NVL (SUM (actual_result), 0), 
                                            ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (goal_value), 0)) * 100)
                                                    / NVL (SUM (goal_value), 0)),2))
                                    )    -- 09/11/2014 Bug 56323    
                               perc_increase_goal,
                           (NVL (SUM (actual_result), 0) - NVL (SUM (goal_value), 0))    -- 09/11/2014 Bug 56323    
                               unit_dol_inc_goal
                      FROM pivot
                     WHERE achieved = 0);

        OPEN p_out_totals_data FOR
              SELECT NVL (SUM (goal_achieved), 0) nbr_pax,
                     DECODE (
                         NVL (total_pax, 0),
                         0, 0,
                         ROUND (
                             (  (  NVL (SUM (goal_achieved), 0)
                                 / NVL (total_pax, 0))
                              * 100),
                             2))
                         perc_of_pax,
                     NVL (SUM (base), 0) total_baseline_objective,
                     NVL (SUM (goal_value), 0) total_goal_value,
                     NVL (SUM (actual_result), 0) total_actual_production,
                     DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                         DECODE(NVL (SUM (base), 0),0,NVL (SUM (actual_result), 0), 
                                                ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (base), 0)) * 100)
                                                        / NVL (SUM (base), 0)),2))
                                    )-- 09/11/2014 Bug 56323
                         perc_increase_baseline,
                     (NVL (SUM (actual_result), 0) - NVL (SUM (base), 0))
                         unit_dol_inc_baseline,
                     DECODE ( NVL(SUM (actual_result), 0),0, 0,
                                     DECODE(NVL (SUM (goal_value), 0),0,NVL (SUM (actual_result), 0), 
                                            ROUND ((((NVL (SUM (actual_result), 0)- NVL (SUM (goal_value), 0)) * 100)
                                                    / NVL (SUM (goal_value), 0)),2))
                                    )    -- 09/11/2014 Bug 56323    
                         perc_increase_goal,
                     (NVL (SUM (actual_result), 0) - NVL (SUM (goal_value), 0))    -- 09/11/2014 Bug 56323    
                         unit_dol_inc_goal
                FROM (WITH pivot
                           AS (SELECT achieved,
                                      goal_achieved,
                                      SUM (goal_achieved) OVER () total_pax,
                                      base,
                                      actual_result,
                                      goal_value
                                 FROM (SELECT rh.node_id AS node_id,
                                              rh.node_name node_name,
                                              NVL (achieved, 1) achieved,
                                              goal_achieved,
                                              base,
                                              actual_result,
                                              goal_value
                                         FROM (SELECT DISTINCT
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name
                                                 FROM rpt_hierarchy rh,
                                                      rpt_goal_selection_summary rpt
                                                WHERE     detail_node_id =
                                                              rh.node_id
                                                      AND rpt.record_type LIKE
                                                              '%node%'
                                                      AND NVL (header_node_id, 0) IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_parentNodeId))))
                                              rh,
                                              (  SELECT r.node_id,
                                                        COUNT (
                                                            CASE
                                                                WHEN pax_goal_id
                                                                         IS NOT NULL
                                                                THEN
                                                                    NVL (achieved,
                                                                         0)
                                                                ELSE
                                                                    NULL
                                                            END)
                                                            goal_achieved,
                                                        NVL (SUM (current_value),
                                                             0)
                                                            actual_result,
                                                        NVL (
                                                            SUM (amount_to_achieve),
                                                            0)
                                                            goal_value,
                                                        NVL (SUM (base_quantity),
                                                             0)
                                                            base,
                                                        achieved
                                                   FROM rpt_goal_selection_detail rpt,
                                                        rpt_hierarchy_rollup r,
                                                        promotion p,
                                                        promo_goalquest promoGoal
                                                  WHERE rpt.level_id is not null   --10/08/2014  
                                                        AND  rpt.node_id =
                                                                r.child_node_id
                                                        AND rpt.promotion_id =
                                                                promoGoal.promotion_id(+)
                                                        AND rpt.promotion_id =
                                                                p.promotion_id(+)
                                                        AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                                                        AND (   (p_in_promotionId
                                                                     IS NULL)
                                                             OR (    p_in_promotionId
                                                                         IS NOT NULL
                                                                 AND P.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array (
                                                                                                       p_in_promotionId)))))
                                                        AND NVL (
                                                                p.promotion_status,
                                                                ' ') =
                                                                NVL (
                                                                    p_in_promotionStatus,
                                                                    NVL (
                                                                        p.promotion_status,
                                                                        ' '))
                                               GROUP BY r.node_id, achieved) dtl
                                        WHERE rh.node_id = dtl.node_id(+)
                                       UNION
                                       SELECT rh.node_id AS node_id,
                                              rh.node_name || 'Team' node_name,
                                              NVL (achieved, 1) achieved,
                                              goal_achieved,
                                              base,
                                              actual_result,
                                              goal_value
                                         FROM (SELECT DISTINCT
                                                      rh.node_id AS node_id,
                                                      rh.node_name node_name
                                                 FROM rpt_hierarchy rh,
                                                      rpt_goal_selection_summary rpt
                                                WHERE     detail_node_id =
                                                              rh.node_id
                                                      AND rpt.record_type LIKE
                                                              '%team%'
                                                      AND NVL (detail_node_id, 0) IN (SELECT *
                                                                                        FROM TABLE (
                                                                                                 get_array_varchar (
                                                                                                     p_in_parentNodeId))))
                                              rh,
                                              (  SELECT rpt.node_id,
                                                        COUNT (
                                                            CASE
                                                                WHEN pax_goal_id
                                                                         IS NOT NULL
                                                                THEN
                                                                    NVL (achieved,
                                                                         0)
                                                                ELSE
                                                                    NULL
                                                            END)
                                                            goal_achieved,
                                                        NVL (SUM (current_value),
                                                             0)
                                                            actual_result,
                                                        NVL (
                                                            SUM (amount_to_achieve),
                                                            0)
                                                            goal_value,
                                                        NVL (SUM (base_quantity),
                                                             0)
                                                            base,
                                                        achieved
                                                   FROM rpt_goal_selection_detail rpt,
                                                        promotion p,
                                                        promo_goalquest promoGoal
                                                  WHERE rpt.level_id is not null   --10/08/2014  
                                                        AND  rpt.node_id IN (SELECT *
                                                                              FROM TABLE (
                                                                                       get_array_varchar (
                                                                                           p_in_parentNodeId)))
                                                        AND rpt.promotion_id =
                                                                promoGoal.promotion_id(+)
                                                        AND rpt.promotion_id =
                                                                p.promotion_id(+)
                                                        AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                                                        AND (   (p_in_promotionId
                                                                     IS NULL)
                                                             OR (    p_in_promotionId
                                                                         IS NOT NULL
                                                                 AND P.promotion_id IN (SELECT *
                                                                                          FROM TABLE (
                                                                                                   get_array (
                                                                                                       p_in_promotionId)))))
                                                        AND NVL (
                                                                p.promotion_status,
                                                                ' ') =
                                                                NVL (
                                                                    p_in_promotionStatus,
                                                                    NVL (
                                                                        p.promotion_status,
                                                                        ' '))
                                               GROUP BY rpt.node_id, achieved)
                                              dtl
                                        WHERE rh.node_id = dtl.node_id(+)))
                        SELECT 'Goals Not Achieved' goals,
                               NVL (SUM (goal_achieved), 0) goal_achieved,
                               NVL (total_pax, 0) total_pax,
                               NVL (SUM (base), 0) base,
                               NVL (SUM (goal_value), 0) goal_value,
                               NVL (SUM (actual_result), 0) actual_result
                          FROM pivot
                         WHERE achieved = 0
                      GROUP BY NVL (total_pax, 0)
                      UNION
                        SELECT 'Goals Achieved' goals,
                               NVL (SUM (goal_achieved), 0) goal_achieved,
                               NVL (total_pax, 0) total_pax,
                               NVL (SUM (base), 0) base,
                               NVL (SUM (goal_value), 0) goal_value,
                               NVL (SUM (actual_result), 0) actual_result
                          FROM pivot
                         WHERE achieved = 1
                      GROUP BY NVL (total_pax, 0))
            GROUP BY NVL (total_pax, 0);

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
    END prc_getGQProgramSummaryTabRes;


--      Suresh J     10/08/2014        Bug 57309 - Fixed Goal Not Achieved to exclue pax who are not selected Goal at all
--     Suresh J     10/27/2014         Bug 57613 -- Change Filter not working for pax status
    PROCEDURE prc_getGQGoalAchievementChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQGoalAchievementChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT COUNT (
                       CASE
                           WHEN rpt.achieved = 1 THEN rpt.user_id
                           ELSE NULL
                       END)
                       total_achieved,
                   COUNT (
                       CASE
                           WHEN rpt.achieved = 0 THEN rpt.user_id
                           ELSE NULL
                       END)
                       total_not_achieved
              FROM rpt_hierarchy_rollup rh,
                   promotion p,
                   promo_goalquest promoGoal,
                   rpt_goal_selection_detail rpt,
                   goalquest_goallevel gl
             WHERE 
--                   rpt.level_id = gl.goallevel_id(+)  --10/08/2014
                   rpt.level_id = gl.goallevel_id    --10/08/2014   
                   AND rpt.promotion_id = promoGoal.promotion_id(+)
                   AND rpt.promotion_id = p.promotion_id(+)
                   AND rpt.node_id = rh.child_node_id
                   --AND NVL (rh.node_id, 0) = NVL (p_in_parentNodeId, 0)
                   AND NVL (rh.node_id, 0) IN (SELECT * --01/15/2016, Bug # 65257
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    NVL(p_in_parentNodeId,0))))
                   AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                   AND (   (p_in_promotionId IS NULL)
                        OR (    p_in_promotionId IS NOT NULL
                            AND P.promotion_id IN (SELECT *
                                                     FROM TABLE (
                                                              get_array_varchar (
                                                                  p_in_promotionId)))))
                   AND NVL (p.promotion_status, ' ') =
                           NVL (p_in_promotionStatus,
                                NVL (p.promotion_status, ' '));

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
    END prc_getGQGoalAchievementChart;


--     Suresh J     10/08/2014        Bug 57309 - Fixed Goal Not Achieved to exclue pax who are not selected Goal at all
--     Suresh J     10/27/2014        Bug 57613 -- Change Filter not working for pax status
--     Suresh J     02/20/2015        Bug Fix 59798 -- GQ Summary Chart Translation Issue
    PROCEDURE prc_getGQIncrementalChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQIncrementalChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT *
              FROM (WITH pivot
                         AS (SELECT rh.node_id AS node_id,
                                    rh.node_name node_name,
                                    NVL (ACHIEVED, 1) ACHIEVED,
                                    base,
                                    actual_result
                               FROM (SELECT DISTINCT
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM rpt_hierarchy rh,
                                            rpt_goal_selection_summary rpt
                                      WHERE     detail_node_id = rh.node_id
                                            AND rpt.record_type LIKE '%node%'
--                                            AND NVL (header_node_id, 0) =  NVL (p_in_parentNodeId, 0)) --01/15/2016--, Bug # 65257
                                              AND NVL (header_node_id, 0) IN (SELECT *
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    NVL(p_in_parentNodeId,0)))))
                                    rh,
                                    (  SELECT r.node_id,
                                              NVL (SUM (current_value), 0)
                                                  actual_result,
                                              NVL (SUM (AMOUNT_TO_ACHIEVE), 0)
                                                  goal_value,
                                              NVL (SUM (base_quantity), 0) base,
                                              achieved
                                         FROM rpt_goal_selection_detail rpt,
                                              rpt_hierarchy_rollup r,
                                              promotion p,
                                              promo_goalquest promoGoal
                                        WHERE  rpt.level_id is not null   --10/08/2014  
                                              AND rpt.node_id = r.child_node_id
                                              AND rpt.promotion_id =
                                                      promoGoal.promotion_id(+)
                                              AND rpt.promotion_id =
                                                      p.promotion_id(+)
                                              AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                                              AND (   (p_in_promotionId IS NULL)
                                                   OR (    p_in_promotionId
                                                               IS NOT NULL
                                                       AND P.promotion_id IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array (
                                                                                             p_in_promotionId)))))
                                              AND NVL (p.promotion_status, ' ') =
                                                      NVL (
                                                          p_in_promotionStatus,
                                                          NVL (
                                                              p.promotion_status,
                                                              ' '))
                                     GROUP BY r.node_id, achieved) dtl
                              WHERE rh.node_id = dtl.node_id(+)
                             UNION
                             SELECT rh.node_id AS node_id,
                                    rh.node_name || 'Team' node_name,
                                    NVL (ACHIEVED, 1) ACHIEVED,
                                    base,
                                    actual_result
                               FROM (SELECT DISTINCT
                                            rh.node_id AS node_id,
                                            rh.node_name node_name
                                       FROM rpt_hierarchy rh,
                                            rpt_goal_selection_summary rpt
                                      WHERE     detail_node_id = rh.node_id
                                            AND rpt.record_type LIKE '%team%'
--                                            AND NVL (detail_node_id, 0) =
--                                                    NVL (p_in_parentNodeId, 0))
                                            AND NVL (detail_node_id, 0) IN (SELECT *--01/15/2016 Bug # 65257
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    NVL(p_in_parentNodeId,0)))))
                                    rh,
                                    (  SELECT rpt.node_id,
                                              NVL (SUM (current_value), 0)
                                                  actual_result,
                                              NVL (SUM (AMOUNT_TO_ACHIEVE), 0)
                                                  goal_value,
                                              NVL (SUM (base_quantity), 0) base,
                                              achieved
                                         FROM rpt_goal_selection_detail rpt,
                                              promotion p,
                                              promo_goalquest promoGoal
                                        WHERE  rpt.level_id is not null   --10/08/2014  
--                                              AND rpt.node_id =
--                                                      NVL (p_in_parentNodeId, 0)--01/15/2016 Bug # 65257
                                               AND NVL (rpt.node_id, 0) IN (SELECT *
                                                                       FROM TABLE (
                                                                                get_array_varchar (
                                                                                    NVL(p_in_parentNodeId,0))))
                                              AND rpt.promotion_id =
                                                      promoGoal.promotion_id(+)
                                              AND rpt.promotion_id =
                                                      p.promotion_id(+)
                                              AND rpt.user_status = NVL(p_in_participantStatus,rpt.user_status)     --10/27/2014
                                              AND (   (p_in_promotionId IS NULL)
                                                   OR (    p_in_promotionId
                                                               IS NOT NULL
                                                       AND P.promotion_id IN (SELECT *
                                                                                FROM TABLE (
                                                                                         get_array (
                                                                                             p_in_promotionId)))))
                                              AND NVL (p.promotion_status, ' ') =
                                                      NVL (
                                                          p_in_promotionStatus,
                                                          NVL (
                                                              p.promotion_status,
                                                              ' '))
                                     GROUP BY rpt.node_id, achieved) dtl
                              WHERE rh.node_id = dtl.node_id(+))
--                    SELECT 'Goals Not Achieved' goals,  --02/20/2015
                      SELECT fnc_cms_asset_code_val_extr( 'report.goalquest.summary','ACHIEVED_NO_GOAL', p_in_languageCode) goals,  --02/20/2015
                           NVL (SUM (BASE), 0) base,
                           NVL (SUM (actual_result), 0) actual_result
                      FROM pivot
                     WHERE achieved = 0
                    UNION
--                    SELECT 'Goals Achieved' goals,  --02/20/2015
                      SELECT fnc_cms_asset_code_val_extr( 'report.goalquest.summary','ACHIEVED_GOAL', p_in_languageCode) goals,  --02/20/2015
                           NVL (SUM (BASE), 0) base,
                           NVL (SUM (actual_result), 0) actual_result
                      FROM pivot
                     WHERE achieved = 1
                    UNION
--                     SELECT 'Totals' goals,              --02/20/2015
                     SELECT fnc_cms_asset_code_val_extr( 'report.goalquest.summary','TOTALS', p_in_languageCode) goals,     --02/20/2015

                           NVL (SUM (BASE), 0) base,
                           NVL (SUM (actual_result), 0) actual_result
                      FROM pivot);

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
    END prc_getGQIncrementalChart;

    PROCEDURE prc_getGQSelectionPctChart (
        p_in_languageCode        IN     VARCHAR,
        p_in_toDate              IN     VARCHAR,
        p_in_fromDate            IN     VARCHAR,
        p_in_managerUserId       IN     VARCHAR,
        p_in_rowNumEnd           IN     NUMBER,
        p_in_rowNumStart         IN     NUMBER,
        p_in_promotionStatus     IN     VARCHAR,
        p_in_promotionId        IN     VARCHAR,
        p_in_departments         IN     VARCHAR,
        p_in_jobPosition         IN     VARCHAR,
        p_in_participantStatus   IN     VARCHAR,
        p_in_parentNodeId        IN     VARCHAR,
        p_in_sortColName         IN     VARCHAR,
        p_in_sortedBy            IN     VARCHAR,
        p_in_nodeAndBelow        IN     VARCHAR,
        p_out_return_code           OUT NUMBER,
        p_out_data                  OUT SYS_REFCURSOR)
    IS
        c_process_name   CONSTANT execution_log.process_name%TYPE
                                      := 'prc_getGQSelectionPctChart' ;
        v_stage                   VARCHAR2 (500);
    BEGIN
        OPEN p_out_data FOR
            SELECT CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (NO_GOAL_SELECTED / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       NO_GOAL_SELE_PERC,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (LEVEL_1_SELECTED / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_1_SELE_PERC,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (LEVEL_2_SELECTED / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_2_SELE_PERC,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_3_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_3_SELE_PERC,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_4_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_4_SELE_PERC,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_5_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_5_SELE_PERC,
                   CASE
                       WHEN TOTAL_PARTICIPANTS > 0
                       THEN
                           ROUND (
                               (level_6_selected / TOTAL_PARTICIPANTS) * 100,
                               2)
                       ELSE
                           0
                   END
                       LEVEL_6_SELE_PERC
              FROM (SELECT NVL (SUM (TOTAL_PARTICIPANTS), 0) TOTAL_PARTICIPANTS,
                           NVL (SUM (NO_GOAL_SELECTED), 0) NO_GOAL_SELECTED,
                           NVL (SUM (LEVEL_1_SELECTED), 0) LEVEL_1_SELECTED,
                           NVL (SUM (LEVEL_2_SELECTED), 0) LEVEL_2_SELECTED,
                           NVL (SUM (LEVEL_3_SELECTED), 0) LEVEL_3_SELECTED,
                           NVL (SUM (LEVEL_4_SELECTED), 0) LEVEL_4_SELECTED,
                           NVL (SUM (LEVEL_5_SELECTED), 0) LEVEL_5_SELECTED,
                           NVL (SUM (LEVEL_6_SELECTED), 0) LEVEL_6_SELECTED
                      FROM (SELECT total_participant AS TOTAL_PARTICIPANTS,
                                   NVL (no_goal_selected, 0)
                                       AS NO_GOAL_SELECTED,
                                   NVL (level_1_selected, 0)
                                       AS LEVEL_1_SELECTED,
                                   NVL (level_2_selected, 0)
                                       AS LEVEL_2_SELECTED,
                                   NVL (level_3_selected, 0)
                                       AS LEVEL_3_SELECTED,
                                   NVL (level_4_selected, 0)
                                       AS LEVEL_4_SELECTED,
                                   NVL (level_5_selected, 0)
                                       AS LEVEL_5_SELECTED,
                                   NVL (level_6_selected, 0)
                                       AS LEVEL_6_SELECTED
                              FROM (WITH pivot_set
                                         AS (SELECT total_participants
                                                        AS sum_total_pax,
                                                    NVL (sequence_num, 0)
                                                        sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name node_name
                                               FROM (SELECT DISTINCT
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            rpt_goal_selection_summary rpt
                                                      WHERE     detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%node%'
                                                            AND NVL (
                                                                    header_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId))))
                                                    rh,
                                                    (SELECT total_participants,
                                                            gl.sequence_num,
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            promotion p,
                                                            promo_goalquest promoGoal,
                                                            rpt_goal_selection_summary rpt,
                                                            goalquest_goallevel gl
                                                      WHERE     rpt.goal_level =
                                                                    gl.goallevel_id(+)
                                                            AND rpt.promotion_id =
                                                                    promoGoal.promotion_id(+)
                                                            AND rpt.promotion_id =
                                                                    p.promotion_id(+)
                                                            AND detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%node%'
                                                            AND NVL (
                                                                    header_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId)))
                                                            AND rpt.pax_status =
                                                                    NVL (
                                                                        p_in_participantStatus,
                                                                        rpt.pax_status)
                                                            AND rpt.job_position =
                                                                    NVL (
                                                                        p_in_jobPosition,
                                                                        rpt.job_position)
                                                            AND (   (p_in_departments
                                                                         IS NULL)
                                                                 OR (    p_in_departments
                                                                             IS NOT NULL
                                                                     AND rpt.department IN (SELECT *
                                                                                              FROM TABLE (
                                                                                                       get_array_varchar (
                                                                                                           p_in_departments)))))
                                                            AND (   (p_in_promotionId
                                                                         IS NULL)
                                                                 OR (    p_in_promotionId
                                                                             IS NOT NULL
                                                                     AND rpt.promotion_id IN (SELECT *
                                                                                                FROM TABLE (
                                                                                                         get_array_varchar (
                                                                                                             p_in_promotionId)))))
                                                            AND NVL (
                                                                    p.promotion_status,
                                                                    ' ') =
                                                                    NVL (
                                                                        p_in_promotionStatus,
                                                                        NVL (
                                                                            p.promotion_status,
                                                                            ' ')))
                                                    rpc
                                              WHERE rh.node_id = rpc.node_id(+))
                                    SELECT t.node_name,
                                           t.node_id,
                                           (  NVL (t.no_goal_selected, 0)
                                            + NVL (t.level_1_selected, 0)
                                            + NVL (t.level_2_selected, 0)
                                            + NVL (t.level_3_selected, 0)
                                            + NVL (t.level_4_selected, 0)
                                            + NVL (t.level_5_selected, 0)
                                            + NVL (t.level_6_selected, 0))
                                               total_participant,
                                           t.no_goal_selected,
                                           t.level_1_selected,
                                           t.level_2_selected,
                                           t.level_3_selected,
                                           t.level_4_selected,
                                           t.level_5_selected,
                                           t.level_6_selected
                                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                                     FOR sequence_num
                                                     IN  (0 AS no_goal_selected,
                                                         1 AS level_1_selected,
                                                         2 AS level_2_selected,
                                                         3 AS level_3_selected,
                                                         4 AS level_4_selected,
                                                         5 AS level_5_selected,
                                                         6 AS level_6_selected)) t)
                             WHERE p_in_nodeAndBelow = 'true'
                            UNION
                            SELECT total_participant AS TOTAL_PARTICIPANTS,
                                   NVL (no_goal_selected, 0)
                                       AS NO_GOAL_SELECTED,
                                   NVL (level_1_selected, 0)
                                       AS LEVEL_1_SELECTED,
                                   NVL (level_2_selected, 0)
                                       AS LEVEL_2_SELECTED,
                                   NVL (level_3_selected, 0)
                                       AS LEVEL_3_SELECTED,
                                   NVL (level_4_selected, 0)
                                       AS LEVEL_4_SELECTED,
                                   NVL (level_5_selected, 0)
                                       AS LEVEL_5_SELECTED,
                                   NVL (level_6_selected, 0)
                                       AS LEVEL_6_SELECTED
                              FROM (WITH pivot_set
                                         AS (SELECT total_participants
                                                        AS sum_total_pax,
                                                    NVL (sequence_num, 0)
                                                        sequence_num,
                                                    rh.node_id AS node_id,
                                                    rh.node_name || ' Team'
                                                        node_name
                                               FROM (SELECT DISTINCT
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            rpt_goal_selection_summary rpt
                                                      WHERE     detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%team%'
                                                            AND NVL (
                                                                    detail_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId))))
                                                    rh,
                                                    (SELECT total_participants,
                                                            gl.sequence_num,
                                                            rh.node_id
                                                                AS node_id,
                                                            rh.node_name
                                                                node_name
                                                       FROM rpt_hierarchy rh,
                                                            promotion p,
                                                            promo_goalquest promoGoal,
                                                            rpt_goal_selection_summary rpt,
                                                            goalquest_goallevel gl
                                                      WHERE     rpt.goal_level =
                                                                    gl.goallevel_id(+)
                                                            AND rpt.promotion_id =
                                                                    promoGoal.promotion_id(+)
                                                            AND rpt.promotion_id =
                                                                    p.promotion_id(+)
                                                            AND detail_node_id =
                                                                    rh.node_id
                                                            AND rpt.record_type LIKE
                                                                    '%team%'
                                                            AND NVL (
                                                                    detail_node_id,
                                                                    0) IN (SELECT *
                                                                             FROM TABLE (
                                                                                      get_array_varchar (
                                                                                          p_in_parentNodeId)))
                                                            AND rpt.pax_status =
                                                                    NVL (
                                                                        p_in_participantStatus,
                                                                        rpt.pax_status)
                                                            AND rpt.job_position =
                                                                    NVL (
                                                                        p_in_jobPosition,
                                                                        rpt.job_position)
                                                            AND (   (p_in_departments
                                                                         IS NULL)
                                                                 OR (    p_in_departments
                                                                             IS NOT NULL
                                                                     AND rpt.department IN (SELECT *
                                                                                              FROM TABLE (
                                                                                                       get_array_varchar (
                                                                                                           p_in_departments)))))
                                                            AND (   (p_in_promotionId
                                                                         IS NULL)
                                                                 OR (    p_in_promotionId
                                                                             IS NOT NULL
                                                                     AND rpt.promotion_id IN (SELECT *
                                                                                                FROM TABLE (
                                                                                                         get_array_varchar (
                                                                                                             p_in_promotionId)))))
                                                            AND NVL (
                                                                    p.promotion_status,
                                                                    ' ') =
                                                                    NVL (
                                                                        p_in_promotionStatus,
                                                                        NVL (
                                                                            p.promotion_status,
                                                                            ' ')))
                                                    rpc
                                              WHERE rh.node_id = rpc.node_id(+))
                                    SELECT t.node_name,
                                           t.node_id,
                                           (  NVL (t.no_goal_selected, 0)
                                            + NVL (t.level_1_selected, 0)
                                            + NVL (t.level_2_selected, 0)
                                            + NVL (t.level_3_selected, 0)
                                            + NVL (t.level_4_selected, 0)
                                            + NVL (t.level_5_selected, 0)
                                            + NVL (t.level_6_selected, 0))
                                               total_participant,
                                           t.no_goal_selected,
                                           t.level_1_selected,
                                           t.level_2_selected,
                                           t.level_3_selected,
                                           t.level_4_selected,
                                           t.level_5_selected,
                                           t.level_6_selected
                                      FROM pivot_set PIVOT (SUM (sum_total_pax)
                                                     FOR sequence_num
                                                     IN  (0 AS no_goal_selected,
                                                         1 AS level_1_selected,
                                                         2 AS level_2_selected,
                                                         3 AS level_3_selected,
                                                         4 AS level_4_selected,
                                                         5 AS level_5_selected,
                                                         6 AS level_6_selected)) t)))
                   rs;

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
    END prc_getGQSelectionPctChart;
END pkg_query_goalquest;
/

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/GoalQuestReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * GoalQuestReportsService.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Sep 6, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface GoalQuestReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "goalQuestReportsService";

  // =======================================
  // GOAL QUEST PROGRESS REPORT
  // =======================================

  public Map<String, Object> getGoalQuestProgressTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestProgressDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getProgressToGoalChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestProgressResultsChart( Map<String, Object> reportParameters );

  // =======================================
  // GOAL QUEST SELECTION REPORT
  // =======================================

  public Map<String, Object> getGoalQuestSelectionTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestSelectionValidLevelNumbers( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestSelectionDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestSelectionTotalsChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestSelectionByOrgChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestSelectionPercentageChart( Map<String, Object> reportParameters );

  // =======================================
  // GOAL QUEST ACHIEVEMENT REPORT
  // =======================================
  public Map<String, Object> getGoalQuestAchievementTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestAchievementDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestAchievementPercentageAchievedChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestAchievementCountAchievedChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestAchievementResultsChart( Map<String, Object> reportParameters );

  // =======================================
  // GOAL QUEST MANAGER ACHIEVEMENT REPORT
  // =======================================

  public Map<String, Object> getGoalQuestManagerTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getManagerTotalPointsEarnedChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestManagerDetailTabularResults( Map<String, Object> reportParameters );

  // =======================================
  // GOAL QUEST EXTRACT REPORTS
  // =======================================
  public Map getGoalQuestProgressExtractResults( Map<String, Object> reportParameters );

  public Map getGoalQuestSelectionExtractResults( Map<String, Object> reportParameters );

  public Map getGoalQuestAchievementExtractResults( Map<String, Object> reportParameters );

  public Map getGoalQuestManagerAchievementExtractReport( Map<String, Object> reportParameters );

  public Map getGoalQuestProgramSummaryExtractReport( Map<String, Object> reportParameters );

  // =======================================
  // GOAL QUEST PROGRAM SUMMARY REPORT
  // =======================================

  public Map<String, Object> getGoalQuestProgramSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestGoalAchievementChart( Map<String, Object> reportParameters );

  public Map<String, Object> getGoalQuestIncrementalChart( Map<String, Object> reportParameters );

}

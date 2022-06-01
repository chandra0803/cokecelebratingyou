/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/GoalQuestReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.GoalQuestReportsDAO;
import com.biperf.core.service.reports.GoalQuestReportsService;

/**
 * GoalQuestReportsServiceImpl.
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
public class GoalQuestReportsServiceImpl implements GoalQuestReportsService
{
  private GoalQuestReportsDAO goalQuestReportsDAO;

  public void setGoalQuestReportsDAO( GoalQuestReportsDAO goalQuestReportsDAO )
  {
    this.goalQuestReportsDAO = goalQuestReportsDAO;
  }

  // =======================================
  // GOAL QUEST PROGRESS REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestProgressTabularResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestProgressTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestProgressDetailResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestProgressDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getProgressToGoalChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getProgressToGoalChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestProgressResultsChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestProgressResultsChart( reportParameters );
  }

  // =======================================
  // GOAL QUEST SELECTION REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestSelectionTabularResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionValidLevelNumbers( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionValidLevelNumbers( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionDetailResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionTotalsChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionTotalsChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionPercentageChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionPercentageChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionByOrgChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionByOrgChart( reportParameters );
  }

  // =======================================
  // GOAL QUEST ACHIEVEMENT REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestAchievementTabularResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestAchievementTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementDetailResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestAchievementDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementPercentageAchievedChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestAchievementPercentageAchievedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementCountAchievedChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestAchievementCountAchievedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementResultsChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestAchievementResultsChart( reportParameters );
  }

  // =======================================
  // GOAL QUEST MANAGER ACHIEVEMENT REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestManagerTabularResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestManagerTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getManagerTotalPointsEarnedChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getManagerTotalPointsEarnedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestManagerDetailTabularResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestManagerDetailTabularResults( reportParameters );
  }

  // =======================================
  // GOAL QUEST EXTRACT REPORTS
  // =======================================

  @Override
  public Map getGoalQuestProgressExtractResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestProgressExtractResults( reportParameters );
  }

  @Override
  public Map getGoalQuestSelectionExtractResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestSelectionExtractResults( reportParameters );
  }

  @Override
  public Map getGoalQuestAchievementExtractResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestAchievementExtractResults( reportParameters );
  }

  // =======================================
  // GOALQUEST PROGRAM SUMMARY REPORT
  // =====================================

  @Override
  public Map<String, Object> getGoalQuestProgramSummaryTabularResults( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestProgramSummaryTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestGoalAchievementChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestGoalAchievementChart( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestIncrementalChart( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestIncrementalChart( reportParameters );
  }

  @Override
  public Map getGoalQuestManagerAchievementExtractReport( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestManagerAchievementExtractReport( reportParameters );
  }

  @Override
  public Map getGoalQuestProgramSummaryExtractReport( Map<String, Object> reportParameters )
  {
    return goalQuestReportsDAO.getGoalQuestProgramSummaryExtractReport( reportParameters );
  }

}

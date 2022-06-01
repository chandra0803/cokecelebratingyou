/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/impl/ChallengePointReportsServiceImpl.java,v $
 */

package com.biperf.core.service.reports.impl;

import java.util.Map;

import com.biperf.core.dao.reports.ChallengePointReportsDAO;
import com.biperf.core.service.reports.ChallengePointReportsService;

/**
 * ChallengePointReportsServiceImpl.
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
 * <td>subraman</td>
 * <td>Jul 30, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ChallengePointReportsServiceImpl implements ChallengePointReportsService
{

  private ChallengePointReportsDAO challengePointReportsDAO;

  public void setChallengePointReportsDAO( ChallengePointReportsDAO challengePointReportsDAO )
  {
    this.challengePointReportsDAO = challengePointReportsDAO;
  }

  @Override
  public Map<String, Object> getChallengePointSelectionTabularResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointSelectionTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionValidLevelNumbers( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointSelectionValidLevelNumbers( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionDetailResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointSelectionDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionTotalsChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointSelectionTotalsChart( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionByOrgChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointSelectionByOrgChart( reportParameters );
  }

  @Override
  public Map getChallengePointSelectionExtractResults( Map<String, Object> reportParameters )
  {

    return challengePointReportsDAO.getChallengePointSelectionExtractResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointProgressTabularResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointProgressTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointProgressDetailResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointProgressDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getProgressToGoalChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getProgressToGoalChart( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointProgressResultsChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointProgressResultsChart( reportParameters );
  }

  @Override
  public Map getChallengePointProgressExtractResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointProgressExtractResults( reportParameters );
  }

  // achievement

  @Override
  public Map<String, Object> getChallengePointAchievementTabularResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointAchievementTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementDetailResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointAchievementDetailResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementPercentageAchievedChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointAchievementPercentageAchievedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementCountAchievedChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointAchievementCountAchievedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementResultsChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointAchievementResultsChart( reportParameters );
  }

  @Override
  public Map getChallengePointAchievementExtractResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointAchievementExtractResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointManagerTabularResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointManagerTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getManagerTotalPointsEarnedChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getManagerTotalPointsEarnedChart( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointManagerDetailTabularResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointManagerDetailTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointProgramSummaryTabularResults( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointProgramSummaryTabularResults( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointGoalAchievementChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointGoalAchievementChart( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointIncrementalChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointIncrementalChart( reportParameters );
  }

  @Override
  public Map getChallengePointManagerAchievementExtractReport( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointManagerAchievementExtractReport( reportParameters );
  }

  @Override
  public Map getChallengePointProgramSummaryExtractReport( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getChallengePointProgramSummaryExtractReport( reportParameters );
  }

  public Map<String, Object> getSelectionPercentageChart( Map<String, Object> reportParameters )
  {
    return challengePointReportsDAO.getSelectionPercentageChart( reportParameters );
  }

}

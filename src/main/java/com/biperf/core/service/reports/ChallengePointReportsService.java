/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/reports/ChallengePointReportsService.java,v $
 *
 */

package com.biperf.core.service.reports;

import java.util.Map;

import com.biperf.core.service.SAO;

/**
 * ChallengePointReportsService.
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
public interface ChallengePointReportsService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static final String BEAN_NAME = "challengePointReportsService";

  // =======================================
  // CHALLENGEPOINT PROGRESS REPORT
  // =======================================

  public Map<String, Object> getChallengePointProgressTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointProgressDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getProgressToGoalChart( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointProgressResultsChart( Map<String, Object> reportParameters );

  // =======================================
  // CHALLENGEPOINT SELECTION REPORT
  // =======================================

  public Map<String, Object> getChallengePointSelectionTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointSelectionValidLevelNumbers( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointSelectionDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointSelectionTotalsChart( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointSelectionByOrgChart( Map<String, Object> reportParameters );

  public Map<String, Object> getSelectionPercentageChart( Map<String, Object> reportParameters );

  // =======================================
  // CHALLENGPOINT ACHIEVEMENT REPORT
  // =======================================
  public Map<String, Object> getChallengePointAchievementTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointAchievementDetailResults( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointAchievementPercentageAchievedChart( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointAchievementCountAchievedChart( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointAchievementResultsChart( Map<String, Object> reportParameters );

  // =======================================
  // CHALLENGPOINT MANAGER ACHIEVEMENT REPORT
  // =======================================

  public Map<String, Object> getChallengePointManagerTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getManagerTotalPointsEarnedChart( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointManagerDetailTabularResults( Map<String, Object> reportParameters );

  // =======================================
  // CHALLENGEPOINT EXTRACT REPORTS
  // =======================================

  public Map getChallengePointProgressExtractResults( Map<String, Object> reportParameters );

  public Map getChallengePointSelectionExtractResults( Map<String, Object> reportParameters );

  public Map getChallengePointAchievementExtractResults( Map<String, Object> reportParameters );

  @SuppressWarnings( "rawtypes" )
  public Map getChallengePointManagerAchievementExtractReport( Map<String, Object> reportParameters );

  @SuppressWarnings( "rawtypes" )
  public Map getChallengePointProgramSummaryExtractReport( Map<String, Object> reportParameters );

  // =======================================
  // CHALLENGEPOINT PROGRAM SUMMARY REPORT
  // =======================================

  public Map<String, Object> getChallengePointProgramSummaryTabularResults( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointGoalAchievementChart( Map<String, Object> reportParameters );

  public Map<String, Object> getChallengePointIncrementalChart( Map<String, Object> reportParameters );

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/ChallengePointReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.ChallengePointReportsDAO;

/**
 * 
 * GoalQuestReportsDAOImpl.
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
public class ChallengePointReportsDAOImpl extends BaseReportsDAO implements ChallengePointReportsDAO
{

  private NamedParameterJdbcTemplate jdbcTemplate;
  private DataSource dataSource;

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  /*
   * public void setDataSource( DataSource dataSource ) { this.jdbcTemplate = new
   * NamedParameterJdbcTemplate( dataSource ); }
   */
  // ========================================
  // SELECTION REPORTS
  // ========================================
  @Override
  public Map<String, Object> getChallengePointSelectionTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "node_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "node_name";
          break;
        case 3:
          sortColName = "total_participants";
          break;
        case 4:
          sortColName = "no_goal_selected";
          break;
        case 5:
          sortColName = "level_1_selected";
          break;
        case 6:
          sortColName = "level_1_selected_percent";
          break;
        case 7:
          sortColName = "level_2_selected";
          break;
        case 8:
          sortColName = "level_2_selected_percent";
          break;
        case 9:
          sortColName = "level_3_selected";
          break;
        case 10:
          sortColName = "level_3_selected_percent";
          break;
        case 11:
          sortColName = "level_4_selected";
          break;
        case 12:
          sortColName = "level_4_selected_percent";
          break;
        case 13:
          sortColName = "level_5_selected";
          break;
        case 14:
          sortColName = "level_5_selected_percent";
          break;
        default:
          sortColName = "node_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPSelectionTabRes";
    CallPrcChallengePointSelectionReports procedure = new CallPrcChallengePointSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionValidLevelNumbers( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPSelectionValidLvlNums";
    CallPrcChallengePointSelectionReports procedure = new CallPrcChallengePointSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionDetailResults( Map<String, Object> reportParameters )
  {
    String sortColName = "pax_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "pax_name";
          break;
        case 2:
          sortColName = "node_name";
          break;
        case 3:
          sortColName = "level_number";
          break;
        case 4:
          sortColName = "promo_name";
          break;
        default:
          sortColName = "pax_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPSelectionDetailRes";
    CallPrcChallengePointSelectionReports procedure = new CallPrcChallengePointSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionTotalsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPSelectionTotalsChart";
    CallPrcChallengePointSelectionReports procedure = new CallPrcChallengePointSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getSelectionPercentageChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPSelectionPctChart";
    CallPrcChallengePointSelectionReports procedure = new CallPrcChallengePointSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointSelectionByOrgChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPSelectionByOrgChart";
    CallPrcChallengePointSelectionReports procedure = new CallPrcChallengePointSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getChallengePointSelectionExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcCPSelectionExtract procedure = new CallPrcCPSelectionExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // CHALLENGE POINT PROGRESS REPORT
  // =======================================

  @Override
  public Map<String, Object> getChallengePointProgressTabularResults( Map<String, Object> reportParameters )
  {

    String sortColName = "node_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "node_name";
          break;
        case 3:
          sortColName = "total_selected";
          break;
        case 4:
          sortColName = "nbr_threshold_reached";
          break;
        case 5:
          sortColName = "all_level_selected";
          break;
        case 6:
          sortColName = "sum_nbr_pax_25_percent";
          break;
        case 7:
          sortColName = "sum_nbr_pax_50_percent";
          break;
        case 8:
          sortColName = "sum_nbr_pax_75_percent";
          break;
        case 9:
          sortColName = "sum_nbr_pax_76_99_percent";
          break;
        case 10:
          sortColName = "sum_nbr_pax_100_percent";
          break;
        default:
          sortColName = "node_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPProgressTabRes";
    CallPrcChallengePointProgressReports procedure = new CallPrcChallengePointProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointProgressDetailResults( Map<String, Object> reportParameters )
  {
    String sortColName = "pax_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "pax_name";
          break;
        case 2:
          sortColName = "node_name";
          break;
        case 3:
          sortColName = "promotion_name";
          break;
        case 4:
          sortColName = "level_selected";
          break;
        case 5:
          sortColName = "base_quantity";
          break;
        case 6:
          sortColName = "amount_to_achieve";
          break;
        case 7:
          sortColName = "current_value";
          break;
        case 8:
          sortColName = "percent_of_challengepoint";
          break;
        default:
          sortColName = "pax_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPProgressDetailRes";
    CallPrcChallengePointProgressReports procedure = new CallPrcChallengePointProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getProgressToGoalChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPToGoalChart";
    CallPrcChallengePointProgressReports procedure = new CallPrcChallengePointProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointProgressResultsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPProgressResChart";
    CallPrcChallengePointProgressReports procedure = new CallPrcChallengePointProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getChallengePointProgressExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcChallengePointProgressReportExtract procedure = new CallPrcChallengePointProgressReportExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // CHALLENGEPOINT ACHIEVEMENT REPORT
  // =======================================

  @Override
  public Map<String, Object> getChallengePointAchievementTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "node_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "node_name";
          break;
        case 3:
          sortColName = "total_selected";
          break;
        default:
          sortColName = "node_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPAchievementTabRes";
    CallPrcChallengePointAchievementReports procedure = new CallPrcChallengePointAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementDetailResults( Map<String, Object> reportParameters )
  {
    String sortColName = "pax_name";
    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "pax_name";
          break;
        case 2:
          sortColName = "node_name";
          break;
        case 3:
          sortColName = "promo_name";
          break;
        case 4:
          sortColName = "level_number";
          break;
        case 5:
          sortColName = "base";
          break;
        case 6:
          sortColName = "goal";
          break;
        case 7:
          sortColName = "actual";
          break;
        case 8:
          sortColName = "percent_of_goal";
          break;
        case 9:
          sortColName = "is_achieved";
          break;
        case 10:
          sortColName = "points";
          break;
        default:
          sortColName = "pax_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPAchievementDetailRes";
    CallPrcChallengePointAchievementReports procedure = new CallPrcChallengePointAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementPercentageAchievedChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPPctAchievedChart";
    CallPrcChallengePointAchievementReports procedure = new CallPrcChallengePointAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementCountAchievedChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPCountAchievedChart";
    CallPrcChallengePointAchievementReports procedure = new CallPrcChallengePointAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointAchievementResultsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPAchievementResChart";
    CallPrcChallengePointAchievementReports procedure = new CallPrcChallengePointAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getChallengePointAchievementExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcCPPaxAchieveExtract procedure = new CallPrcCPPaxAchieveExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // CHALLENGEPOINT MANAGER ACHIEVEMENT REPORT
  // =======================================

  @Override
  public Map<String, Object> getChallengePointManagerTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "MANAGER_NAME";

    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "MANAGER_NAME";
          break;
        case 3:
          sortColName = "TOTAL_PAX";
          break;
        case 4:
          sortColName = "TOTAL_PAX_GOAL_SELECTED";
          break;
        case 5:
          sortColName = "TOTAL_PAX_ACHIEVED";
          break;
        case 6:
          sortColName = "PERCENT_SEL_PAX_ACHIEVING";
          break;
        case 7:
          sortColName = "PERCENT_TOT_PAX_ACHIEVING";
          break;
        case 8:
          sortColName = "MANAGE_OVERRIDE_PERCENTAGE";
          break;
        case 9:
          sortColName = "TOTAL_POINTS_BY_TEAM";
          break;
        case 10:
          sortColName = "MANAGER_PAYOUT_PER_ACHIEVER";
          break;
        case 11:
          sortColName = "TOTAL_MGR_POINTS";
          break;
        default:
          sortColName = "MANAGER_NAME";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPManagerTabRes";
    CallPrcChallengePointManagerAchievementReports procedure = new CallPrcChallengePointManagerAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getManagerTotalPointsEarnedChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getMgrTotalPtsEarnedChart";
    CallPrcChallengePointManagerAchievementReports procedure = new CallPrcChallengePointManagerAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointManagerDetailTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "MANAGER_NAME";

    if ( reportParameters.get( "sortedOn" ) != null )
    {
      int sortedOn = ( (Integer)reportParameters.get( "sortedOn" ) ).intValue();
      switch ( sortedOn )
      {
        case 1:
          sortColName = "MANAGER_NAME";
          break;
        case 2:
          sortColName = "PROMOTION_NAME";
          break;
        case 3:
          sortColName = "TOTAL_PAX";
          break;
        case 4:
          sortColName = "TOTAL_PAX_GOAL_SELECTED";
          break;
        case 5:
          sortColName = "TOTAL_PAX_ACHIEVED";
          break;
        case 6:
          sortColName = "PERCENT_SEL_PAX_ACHIEVING";
          break;
        case 7:
          sortColName = "PERCENT_TOT_PAX_ACHIEVING";
          break;
        case 8:
          sortColName = "MANAGE_OVERRIDE_PERCENTAGE";
          break;
        case 9:
          sortColName = "TOTAL_POINTS_BY_TEAM";
          break;
        case 10:
          sortColName = "TOTAL_MGR_POINTS";
          break;
        case 11:
          sortColName = "MANAGER_PAYOUT_PER_ACHIEVER";
          break;
        default:
          sortColName = "MANAGER_NAME";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPManagerDetailTabRes";
    CallPrcChallengePointManagerAchievementReports procedure = new CallPrcChallengePointManagerAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // SUMMARY REPORTS
  // =======================================
  @Override
  public Map<String, Object> getChallengePointProgramSummaryTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "GOALS";
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPProgramSummaryTabRes";
    CallPrcChallengePointSummaryReports procedure = new CallPrcChallengePointSummaryReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointGoalAchievementChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_challengepoint.prc_getCPGoalAchievementChart";
    CallPrcChallengePointSummaryReports procedure = new CallPrcChallengePointSummaryReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getChallengePointIncrementalChart( Map<String, Object> reportParameters )
  {
    String sortColName = "GOALS";
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_challengepoint.prc_getCPIncrementalChart";
    CallPrcChallengePointSummaryReports procedure = new CallPrcChallengePointSummaryReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public Map getChallengePointManagerAchievementExtractReport( Map<String, Object> reportParameters )
  {
    CallPrcChallengePointManagerAchivementExtract procedure = new CallPrcChallengePointManagerAchivementExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getChallengePointProgramSummaryExtractReport( Map<String, Object> reportParameters )
  {
    CallPrcChallengePointProgramSummaryExtract procedure = new CallPrcChallengePointProgramSummaryExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reports/hibernate/GoalQuestReportsDAOImpl.java,v $
 *
 */

package com.biperf.core.dao.reports.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.biperf.core.dao.reports.BaseReportsDAO;
import com.biperf.core.dao.reports.GoalQuestReportsDAO;

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
 * <td>drahn</td>
 * <td>Sep 6, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class GoalQuestReportsDAOImpl extends BaseReportsDAO implements GoalQuestReportsDAO
{
  @SuppressWarnings( "unused" )
  private NamedParameterJdbcTemplate jdbcTemplate;
  private DataSource dataSource;

  // =======================================
  // GOAL QUEST PROGRESS REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestProgressTabularResults( Map<String, Object> reportParameters )
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
          sortColName = "nbr_selected";
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
    String procName = "pkg_query_goalquest.prc_getGQProgressTabRes";
    CallPrcGQProgressReports procedure = new CallPrcGQProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestProgressDetailResults( Map<String, Object> reportParameters )
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
          sortColName = "level_name";
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
          sortColName = "percent_of_goal";
          break;
        default:
          sortColName = "pax_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_goalquest.prc_getGQProgressDetailRes";
    CallPrcGQProgressReports procedure = new CallPrcGQProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getProgressToGoalChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getProgressToGoalChart";
    CallPrcGQProgressReports procedure = new CallPrcGQProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestProgressResultsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQProgressResChart";
    CallPrcGQProgressReports procedure = new CallPrcGQProgressReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // GOAL QUEST SELECTION REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestSelectionTabularResults( Map<String, Object> reportParameters )
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
        case 15:
          sortColName = "level_6_selected";
          break;
        case 16:
          sortColName = "level_6_selected_percent";
          break;
        default:
          sortColName = "node_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_goalquest.prc_getGQSelectionTabRes";
    CallPrcGQSelectionReports procedure = new CallPrcGQSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionValidLevelNumbers( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQSelectionValidLvlNums";
    CallPrcGQSelectionReports procedure = new CallPrcGQSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionDetailResults( Map<String, Object> reportParameters )
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
    String procName = "pkg_query_goalquest.prc_getGQSelectionDetailRes";
    CallPrcGQSelectionReports procedure = new CallPrcGQSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionTotalsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQSelectionTotalsChart";
    CallPrcGQSelectionReports procedure = new CallPrcGQSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionByOrgChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQSelectionByOrgChart";
    CallPrcGQSelectionReports procedure = new CallPrcGQSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // GOAL QUEST ACHIEVEMENT REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestAchievementTabularResults( Map<String, Object> reportParameters )
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
        default:
          sortColName = "node_name";
          break;
      }
    }
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_goalquest.prc_getGQAchievementTabRes";
    CallPrcGQAchievementReports procedure = new CallPrcGQAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementDetailResults( Map<String, Object> reportParameters )
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
    String procName = "pkg_query_goalquest.prc_getGQAchievementDetailRes";
    CallPrcGQAchievementReports procedure = new CallPrcGQAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementPercentageAchievedChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQPctAchievedChart";
    CallPrcGQAchievementReports procedure = new CallPrcGQAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementCountAchievedChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQCountAchievedChart";
    CallPrcGQAchievementReports procedure = new CallPrcGQAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestAchievementResultsChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQAchievementResChart";
    CallPrcGQAchievementReports procedure = new CallPrcGQAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // GOAL QUEST MANAGER ACHIEVEMENT REPORT
  // =======================================

  @Override
  public Map<String, Object> getGoalQuestManagerTabularResults( Map<String, Object> reportParameters )
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
    String procName = "pkg_query_goalquest.prc_getGQManagerTabRes";
    CallPrcGQMgrAchievementReports procedure = new CallPrcGQMgrAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getManagerTotalPointsEarnedChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getMgrTotalPtsEarnedChart";
    CallPrcGQMgrAchievementReports procedure = new CallPrcGQMgrAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestManagerDetailTabularResults( Map<String, Object> reportParameters )
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
    String procName = "pkg_query_goalquest.prc_getGQManagerDetailTabRes";
    CallPrcGQMgrAchievementReports procedure = new CallPrcGQMgrAchievementReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  // =======================================
  // GOAL QUEST EXTRACT REPORTS
  // =======================================

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getGoalQuestProgressExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcGQProgressExtract procedure = new CallPrcGQProgressExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getGoalQuestSelectionExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcGQSelectionExtract procedure = new CallPrcGQSelectionExtract( dataSource );
    return procedure.executeProcedure( reportParameters );

  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map getGoalQuestAchievementExtractResults( Map<String, Object> reportParameters )
  {
    CallPrcGQPaxAchieveExtract procedure = new CallPrcGQPaxAchieveExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  /**
    * Setter: DataSource is provided by Dependency Injection.
    * 
    * @param dataSource
    */
  public void setDataSource( DataSource dataSource )
  {
    this.jdbcTemplate = new NamedParameterJdbcTemplate( dataSource );
    this.dataSource = dataSource;
  }

  // =======================================
  // GOAL QUEST PROGRAM SUMMARY REPORTS
  // =======================================
  @Override
  public Map<String, Object> getGoalQuestProgramSummaryTabularResults( Map<String, Object> reportParameters )
  {
    String sortColName = "GOALS";
    reportParameters.put( "sortColName", sortColName );
    String procName = "pkg_query_goalquest.prc_getGQProgramSummaryTabRes";
    CallPrcGQProgramSummaryReports procedure = new CallPrcGQProgramSummaryReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestGoalAchievementChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQGoalAchievementChart";
    CallPrcGQProgramSummaryReports procedure = new CallPrcGQProgramSummaryReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestIncrementalChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQIncrementalChart";
    CallPrcGQProgramSummaryReports procedure = new CallPrcGQProgramSummaryReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map<String, Object> getGoalQuestSelectionPercentageChart( Map<String, Object> reportParameters )
  {
    String procName = "pkg_query_goalquest.prc_getGQSelectionPctChart";
    CallPrcGQSelectionReports procedure = new CallPrcGQSelectionReports( dataSource, procName );
    return procedure.executeProcedure( reportParameters );

  }

  @Override
  public Map getGoalQuestManagerAchievementExtractReport( Map<String, Object> reportParameters )
  {
    CallPrcGoalQuestManagerAchievementExtract procedure = new CallPrcGoalQuestManagerAchievementExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }

  @Override
  public Map getGoalQuestProgramSummaryExtractReport( Map<String, Object> reportParameters )
  {
    CallPrcGoalQuestProgramSummaryExtract procedure = new CallPrcGoalQuestProgramSummaryExtract( dataSource );
    return procedure.executeProcedure( reportParameters );
  }
}


package com.biperf.core.dao.reports.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.goalquest.GoalQuestSummaryReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcGQProgramSummaryReports extends StoredProcedure
{
  private static final String PROGRAM_SUMMARY_TAB_RES = "pkg_query_goalquest.prc_getGQProgramSummaryTabRes";
  private static final String GOAL_ACHIEVEMENT_CHART = "pkg_query_goalquest.prc_getGQGoalAchievementChart";
  private static final String INCREMENTAL_CHART = "pkg_query_goalquest.prc_getGQIncrementalChart";

  public CallPrcGQProgramSummaryReports( DataSource ds, String storedProcName )
  {
    super( ds, storedProcName );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_managerUserId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
    switch ( storedProcName )
    {
      case PROGRAM_SUMMARY_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestProgramSummaryTabularResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new GoalQuestProgramSummaryTabularResultsTotalsMapper() ) );
        break;
      case GOAL_ACHIEVEMENT_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestGoalAchievementChartMapper() ) );
        break;
      case INCREMENTAL_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestIncrementalChartMapper() ) );
        break;
      default:
        break;
    }
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters ) throws DataAccessException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_managerUserId", reportParameters.get( "managerUserId" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    inParams.put( "p_in_nodeAndBelow", reportParameters.get( "nodeAndBelow" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  protected class GoalQuestProgramSummaryTabularResultsMapper implements ResultSetExtractor<List<GoalQuestSummaryReportValue>>
  {
    @Override
    public List<GoalQuestSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestSummaryReportValue> results = new ArrayList<GoalQuestSummaryReportValue>();
      while ( rs.next() )
      {
        GoalQuestSummaryReportValue result = new GoalQuestSummaryReportValue();
        result.setGoals( rs.getString( "goals" ) );
        result.setAchieved( rs.getLong( "nbr_pax" ) );
        result.setPercentAchieved( rs.getBigDecimal( "perc_of_pax" ) );
        result.setTotalBaseLineObjective( rs.getLong( "total_baseline_objective" ) );
        result.setTotalGoalValue( rs.getLong( "total_goal_value" ) );
        result.setTotalActualProduction( rs.getLong( "total_actual_production" ) );
        result.setPercIncreaseBaseline( rs.getLong( "perc_increase_baseline" ) );
        result.setUnitDollerIncrBaseline( rs.getLong( "unit_dol_inc_baseline" ) );
        result.setPercentIncreseGoal( rs.getLong( "perc_increase_goal" ) );
        result.setUnitDollerIncrGoal( rs.getLong( "unit_dol_inc_goal" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestProgramSummaryTabularResultsTotalsMapper implements ResultSetExtractor<GoalQuestSummaryReportValue>
  {
    @Override
    public GoalQuestSummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestSummaryReportValue result = new GoalQuestSummaryReportValue();
      if ( rs.next() )
      {
        result.setAchieved( rs.getLong( "nbr_pax" ) );
        result.setPercentAchieved( rs.getBigDecimal( "perc_of_pax" ) );
        result.setTotalBaseLineObjective( rs.getLong( "total_baseline_objective" ) );
        result.setTotalGoalValue( rs.getLong( "total_goal_value" ) );
        result.setTotalActualProduction( rs.getLong( "total_actual_production" ) );
        result.setPercIncreaseBaseline( rs.getLong( "perc_increase_baseline" ) );
        result.setUnitDollerIncrBaseline( rs.getLong( "unit_dol_inc_baseline" ) );
        result.setPercentIncreseGoal( rs.getLong( "perc_increase_goal" ) );
        result.setUnitDollerIncrGoal( rs.getLong( "unit_dol_inc_goal" ) );
      }
      return result;
    }
  }

  protected class GoalQuestGoalAchievementChartMapper implements ResultSetExtractor<GoalQuestSummaryReportValue>
  {
    @Override
    public GoalQuestSummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestSummaryReportValue result = new GoalQuestSummaryReportValue();
      if ( rs.next() )
      {
        result.setTotalAchieved( rs.getLong( "total_achieved" ) );
        result.setTotalNotAchieved( rs.getLong( "total_not_achieved" ) );
      }
      return result;
    }
  }

  protected class GoalQuestIncrementalChartMapper implements ResultSetExtractor<List<GoalQuestSummaryReportValue>>
  {
    @Override
    public List<GoalQuestSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestSummaryReportValue> results = new ArrayList<GoalQuestSummaryReportValue>();
      while ( rs.next() )
      {
        GoalQuestSummaryReportValue result = new GoalQuestSummaryReportValue();
        result.setGoals( rs.getString( "goals" ) );
        result.setTotalBaseLineObjective( rs.getLong( "base" ) );
        result.setTotalActualProduction( rs.getLong( "actual_result" ) );
        results.add( result );
      }
      return results;
    }
  }

}

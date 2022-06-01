
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

import com.biperf.core.value.challengepoint.ChallengePointSummaryReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcChallengePointSummaryReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_challengepoint.prc_getCPProgramSummaryTabRes";
  private static final String INCREMENTAL_CHART = "pkg_query_challengepoint.prc_getCPIncrementalChart";
  private static final String GOAL_ACHIEVEMENT_CHART = "pkg_query_challengepoint.prc_getCPGoalAchievementChart";

  public CallPrcChallengePointSummaryReports( DataSource ds, String storedProcName )
  {
    super( ds, storedProcName );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_managerUserId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( storedProcName )
    {
      case TABULAR_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointProgramSummaryReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointProgramSummaryReportTotalsMapper() ) );
        break;
      case INCREMENTAL_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointIncrementalChartMapper() ) );
        break;
      case GOAL_ACHIEVEMENT_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointGoalAchievementChartMapper() ) );
        break;
      default:
        break;
    }
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters ) throws DataAccessException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_managerUserId", reportParameters.get( "managerUserId" ) );
    Object nodeAndBelow = reportParameters.get( "nodeAndBelow" );
    if ( nodeAndBelow != null && ( (Boolean)nodeAndBelow ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  protected class ChallengePointProgramSummaryReportMapper implements ResultSetExtractor<List<ChallengePointSummaryReportValue>>
  {
    @Override
    public List<ChallengePointSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointSummaryReportValue> results = new ArrayList<ChallengePointSummaryReportValue>();
      while ( rs.next() )
      {
        ChallengePointSummaryReportValue result = new ChallengePointSummaryReportValue();
        result.setGoals( rs.getString( "goals" ) );
        result.setAchieved( rs.getLong( "nbr_pax" ) );
        result.setPercentAchieved( rs.getBigDecimal( "perc_of_pax" ) );
        result.setTotalBaseLineObjective( rs.getLong( "total_baseline_objective" ) );
        result.setTotalChallengeProduction( rs.getBigDecimal( "total_goal_value" ) );
        result.setTotalActualProduction( rs.getLong( "total_actual_production" ) );
        result.setPercIncreaseBaseline( rs.getBigDecimal( "perc_increase_baseline" ) );
        result.setDolIncBaseline( rs.getLong( "unit_dol_inc_baseline" ) );
        result.setPercentIncreseChallengepoint( rs.getBigDecimal( "perc_increase_goal" ) );
        result.setUnitDolIncCP( rs.getBigDecimal( "unit_dol_inc_goal" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointProgramSummaryReportTotalsMapper implements ResultSetExtractor<ChallengePointSummaryReportValue>
  {
    @Override
    public ChallengePointSummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointSummaryReportValue result = new ChallengePointSummaryReportValue();
      if ( rs.next() )
      {
        result.setAchieved( rs.getLong( "nbr_pax" ) );
        result.setPercentAchieved( rs.getBigDecimal( "perc_of_pax" ) );
        result.setTotalBaseLineObjective( rs.getLong( "total_baseline_objective" ) );
        result.setTotalChallengeProduction( rs.getBigDecimal( "total_goal_value" ) );
        result.setTotalActualProduction( rs.getLong( "total_actual_production" ) );
        result.setPercIncreaseBaseline( rs.getBigDecimal( "perc_increase_baseline" ) );
        result.setDolIncBaseline( rs.getLong( "unit_dol_inc_baseline" ) );
        result.setPercentIncreseChallengepoint( rs.getBigDecimal( "perc_increase_goal" ) );
        result.setUnitDolIncCP( rs.getBigDecimal( "unit_dol_inc_goal" ) );
      }
      return result;
    }
  }

  protected class ChallengePointGoalAchievementChartMapper implements ResultSetExtractor<ChallengePointSummaryReportValue>
  {
    @Override
    public ChallengePointSummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointSummaryReportValue result = new ChallengePointSummaryReportValue();
      if ( rs.next() )
      {

        result.setTotalAchieved( rs.getLong( "total_achieved" ) );
        result.setTotalNotAchieved( rs.getLong( "total_not_achieved" ) );
      }
      return result;
    }
  }

  protected class ChallengePointIncrementalChartMapper implements ResultSetExtractor<List<ChallengePointSummaryReportValue>>
  {
    @Override
    public List<ChallengePointSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointSummaryReportValue> results = new ArrayList<ChallengePointSummaryReportValue>();
      while ( rs.next() )
      {
        ChallengePointSummaryReportValue result = new ChallengePointSummaryReportValue();
        result.setGoals( rs.getString( "goals" ) );
        result.setTotalBaseLineObjective( rs.getLong( "base" ) );
        result.setTotalActualProduction( rs.getLong( "actual_result" ) );
        results.add( result );
      }
      return results;
    }
  }
}

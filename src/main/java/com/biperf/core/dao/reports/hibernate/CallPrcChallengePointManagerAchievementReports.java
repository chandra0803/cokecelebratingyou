
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

import com.biperf.core.value.challengepoint.ChallengePointManagerReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcChallengePointManagerAchievementReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_challengepoint.prc_getCPManagerTabRes";
  private static final String DETAIL_RESULTS = "pkg_query_challengepoint.prc_getCPManagerDetailTabRes";
  private static final String TOTAL_POINTS_EARNED_CHART = "pkg_query_challengepoint.prc_getMgrTotalPtsEarnedChart";

  public CallPrcChallengePointManagerAchievementReports( DataSource ds, String storedProcName )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointManagerReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointManagerReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointManagerDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointManagerDetailTotalsReportMapper() ) );
        break;
      case TOTAL_POINTS_EARNED_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ManagerTotalPointsEarnedChartMapper() ) );
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

  protected class ChallengePointManagerDetailReportMapper implements ResultSetExtractor<List<ChallengePointManagerReportValue>>
  {
    @Override
    public List<ChallengePointManagerReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointManagerReportValue> results = new ArrayList<ChallengePointManagerReportValue>();
      while ( rs.next() )
      {
        ChallengePointManagerReportValue result = new ChallengePointManagerReportValue();
        result.setManagerName( rs.getString( "manager_name" ) );
        result.setTotalPax( rs.getLong( "TOTAL_PAX" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "TOTAL_PAX_GOAL_SELECTED" ) );
        result.setTotalPaxAchieved( rs.getLong( "TOTAL_PAX_ACHIEVED" ) );
        result.setPercentOfSelectedPaxAchieving( rs.getBigDecimal( "PERCENT_SEL_PAX_ACHIEVING" ) );
        result.setPercentOfTotalPaxAchieving( rs.getBigDecimal( "PERCENT_TOT_PAX_ACHIEVING" ) );
        result.setPercentOfManagerOveride( rs.getBigDecimal( "MANAGE_OVERRIDE_PERCENTAGE" ) );
        result.setTotalPoints( rs.getLong( "TOTAL_POINTS_BY_TEAM" ) );
        result.setTotalManagerPoints( rs.getLong( "TOTAL_MGR_POINTS" ) );
        result.setManagerPayOutPerAchiever( rs.getBigDecimal( "manager_payout_per_achiever" ) );
        result.setPromotionName( rs.getString( "promotion_name" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointManagerDetailTotalsReportMapper implements ResultSetExtractor<ChallengePointManagerReportValue>
  {
    @Override
    public ChallengePointManagerReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointManagerReportValue result = new ChallengePointManagerReportValue();
      if ( rs.next() )
      {
        result.setTotalPax( rs.getLong( "TOTAL_PAX" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "TOTAL_PAX_GOAL_SELECTED" ) );
        result.setTotalPaxAchieved( rs.getLong( "TOTAL_PAX_ACHIEVED" ) );
        result.setPercentOfSelectedPaxAchieving( rs.getBigDecimal( "PERCENT_SEL_PAX_ACHIEVING" ) );
        result.setPercentOfTotalPaxAchieving( rs.getBigDecimal( "PERCENT_TOT_PAX_ACHIEVING" ) );
        result.setPercentOfManagerOveride( rs.getBigDecimal( "MANAGE_OVERRIDE_PERCENTAGE" ) );
        result.setTotalPoints( rs.getLong( "TOTAL_POINTS_BY_TEAM" ) );
        result.setTotalManagerPoints( rs.getLong( "TOTAL_MGR_POINTS" ) );
        result.setManagerPayOutPerAchiever( rs.getBigDecimal( "manager_payout_per_achiever" ) );
      }
      return result;
    }
  }

  protected class ChallengePointManagerReportMapper implements ResultSetExtractor<List<ChallengePointManagerReportValue>>
  {
    @Override
    public List<ChallengePointManagerReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointManagerReportValue> results = new ArrayList<ChallengePointManagerReportValue>();
      while ( rs.next() )
      {
        ChallengePointManagerReportValue result = new ChallengePointManagerReportValue();
        result.setManagerUserId( rs.getLong( "manager_id" ) );
        result.setManagerName( rs.getString( "manager_name" ) );
        result.setTotalPax( rs.getLong( "TOTAL_PAX" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "TOTAL_PAX_GOAL_SELECTED" ) );
        result.setTotalPaxAchieved( rs.getLong( "TOTAL_PAX_ACHIEVED" ) );
        result.setPercentOfSelectedPaxAchieving( rs.getBigDecimal( "PERCENT_SEL_PAX_ACHIEVING" ) );
        result.setPercentOfTotalPaxAchieving( rs.getBigDecimal( "PERCENT_TOT_PAX_ACHIEVING" ) );
        result.setPercentOfManagerOveride( rs.getBigDecimal( "MANAGE_OVERRIDE_PERCENTAGE" ) );
        result.setTotalPoints( rs.getLong( "TOTAL_POINTS_BY_TEAM" ) );
        result.setTotalManagerPoints( rs.getLong( "TOTAL_MGR_POINTS" ) );
        result.setManagerPayOutPerAchiever( rs.getBigDecimal( "manager_payout_per_achiever" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointManagerReportTotalsMapper implements ResultSetExtractor<ChallengePointManagerReportValue>
  {
    @Override
    public ChallengePointManagerReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointManagerReportValue result = new ChallengePointManagerReportValue();

      if ( rs.next() )
      {
        result.setTotalPax( rs.getLong( "TOTAL_PAX" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "TOTAL_PAX_GOAL_SELECTED" ) );
        result.setTotalPaxAchieved( rs.getLong( "TOTAL_PAX_ACHIEVED" ) );
        result.setPercentOfSelectedPaxAchieving( rs.getBigDecimal( "PERCENT_SEL_PAX_ACHIEVING" ) );
        result.setPercentOfTotalPaxAchieving( rs.getBigDecimal( "PERCENT_TOT_PAX_ACHIEVING" ) );
        result.setPercentOfManagerOveride( rs.getBigDecimal( "MANAGE_OVERRIDE_PERCENTAGE" ) );
        result.setTotalPoints( rs.getLong( "TOTAL_POINTS_BY_TEAM" ) );
        result.setTotalManagerPoints( rs.getLong( "TOTAL_MGR_POINTS" ) );
        result.setManagerPayOutPerAchiever( rs.getBigDecimal( "manager_payout_per_achiever" ) );
      }
      return result;
    }
  }

  protected class ManagerTotalPointsEarnedChartMapper implements ResultSetExtractor<List<ChallengePointManagerReportValue>>
  {
    @Override
    public List<ChallengePointManagerReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointManagerReportValue> results = new ArrayList<ChallengePointManagerReportValue>();
      while ( rs.next() )
      {
        ChallengePointManagerReportValue result = new ChallengePointManagerReportValue();
        result.setManagerName( rs.getString( "manager_name" ) );
        result.setTotalManagerPoints( rs.getLong( "manager_points" ) );
        results.add( result );
      }
      return results;
    }
  }
}

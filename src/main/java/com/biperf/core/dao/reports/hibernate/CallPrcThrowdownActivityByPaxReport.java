/**
 * 
 */

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

import com.biperf.core.value.throwdown.ThrowdownActivityByPaxReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcThrowdownActivityByPaxReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETBYPAXSUMMARY";
  private static final String PROC2 = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETBYPAXDETAIL";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETTOTALACTIVITYCHART";
  private static final String PROC4 = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETBYROUNDCHART";
  private static final String PROC5 = "PKG_QUERY_THROWDOWN_ACTIVITY.PRC_GETPOINTSEARNED";

  public CallPrcThrowdownActivityByPaxReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_roundNumber", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_userId", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.ThrowdownActivityByPaxSummaryResultsReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.ThrowdownActivityByPaxSummaryTabularResultsTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.ThrowdownActivityByPaxDetailResultsReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.ThrowdownActivityByPaxDetailTabularResultsTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.ThrowdownTotalActivityChartMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.ThrowdownActivityByRoundChartMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcThrowdownActivityByPaxReport.PointsEarnedInThrowdownChartMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_roundNumber", reportParameters.get( "roundNumber" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    inParams.put( "p_in_userId", reportParameters.get( "userId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  protected class ThrowdownActivityByPaxSummaryResultsReportMapper implements ResultSetExtractor
  {
    @Override
    public List<ThrowdownActivityByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ThrowdownActivityByPaxReportValue> reportData = new ArrayList<ThrowdownActivityByPaxReportValue>();

      while ( rs.next() )
      {
        ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
        reportValue.setUserId( rs.getLong( "user_id" ) );
        reportValue.setParticipantName( rs.getString( "participant_name" ) );
        reportValue.setLoginId( rs.getString( "login_id" ) );
        reportValue.setCountry( rs.getString( "COUNTRY" ) );
        reportValue.setParticipantStatus( rs.getString( "user_status" ) );
        reportValue.setOrgName( rs.getString( "node_name" ) );
        reportValue.setJobPosition( rs.getString( "department" ) );
        reportValue.setPromotionName( rs.getString( "promo_name" ) );
        reportValue.setDepartment( rs.getString( "job_position" ) );
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        // Rank cannot be zero - utility method extractLong return zero when null
        Long rank = rs.getLong( "rank" );
        if ( rank != null && rank.longValue() > 0 )
        {
          reportValue.setRank( rank );
        }
        reportValue.setPoints( rs.getLong( "payout" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  protected class ThrowdownActivityByPaxSummaryTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public ThrowdownActivityByPaxReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
      while ( rs.next() )
      {
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportValue.setPoints( rs.getLong( "payout" ) );
      }
      return reportValue;
    }
  }

  protected class ThrowdownActivityByPaxDetailResultsReportMapper implements ResultSetExtractor
  {
    @Override
    public List<ThrowdownActivityByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ThrowdownActivityByPaxReportValue> reportData = new ArrayList<ThrowdownActivityByPaxReportValue>();

      while ( rs.next() )
      {
        ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
        reportValue.setUserId( rs.getLong( "user_id" ) );
        reportValue.setParticipantName( rs.getString( "participant_name" ) );
        reportValue.setLoginId( rs.getString( "login_id" ) );
        reportValue.setCountry( rs.getString( "COUNTRY" ) );
        reportValue.setParticipantStatus( rs.getString( "user_status" ) );
        reportValue.setOrgName( rs.getString( "node_name" ) );
        reportValue.setDepartment( rs.getString( "department" ) );
        reportValue.setJobPosition( rs.getString( "job_position" ) );
        reportValue.setPromotionName( rs.getString( "promotion_name" ) );
        reportValue.setRoundNumber( rs.getLong( "round_number" ) );
        reportValue.setRoundStartDate( rs.getDate( "round_start_date" ) );
        reportValue.setRoundEndDate( rs.getDate( "round_end_date" ) );
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        // Rank cannot be zero - utility method extractLong return zero when null
        Long rank = rs.getLong( "rank" );
        if ( rank != null && rank.longValue() > 0 )
        {
          reportValue.setRank( rank );
        }
        reportValue.setPoints( rs.getLong( "payout" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  protected class ThrowdownActivityByPaxDetailTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public ThrowdownActivityByPaxReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
      while ( rs.next() )
      {
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportValue.setPoints( rs.getLong( "payout" ) );
      }
      return reportValue;
    }
  }

  /* CHARTS */

  private class ThrowdownTotalActivityChartMapper implements ResultSetExtractor
  {
    @Override
    public List<ThrowdownActivityByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ThrowdownActivityByPaxReportValue> reportData = new ArrayList<ThrowdownActivityByPaxReportValue>();

      while ( rs.next() )
      {
        ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
        reportValue.setParticipantName( rs.getString( "participant_name" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class ThrowdownActivityByRoundChartMapper implements ResultSetExtractor
  {
    @Override
    public List<ThrowdownActivityByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ThrowdownActivityByPaxReportValue> reportData = new ArrayList<ThrowdownActivityByPaxReportValue>();

      while ( rs.next() )
      {
        ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
        reportValue.setRoundNumber( rs.getLong( "round_number" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class PointsEarnedInThrowdownChartMapper implements ResultSetExtractor
  {
    @Override
    public List<ThrowdownActivityByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ThrowdownActivityByPaxReportValue> reportData = new ArrayList<ThrowdownActivityByPaxReportValue>();

      while ( rs.next() )
      {
        ThrowdownActivityByPaxReportValue reportValue = new ThrowdownActivityByPaxReportValue();
        reportValue.setParticipantName( rs.getString( "participant_name" ) );
        reportValue.setPoints( rs.getLong( "points" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}

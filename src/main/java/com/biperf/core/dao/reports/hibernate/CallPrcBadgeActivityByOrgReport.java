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

import com.biperf.core.value.badge.BadgeActivityByOrgReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcBadgeActivityByOrgReport extends StoredProcedure
{
  /* Tables */
  private static final String PROC1 = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETACTIVITYBYORGSUMMARY";
  private static final String PROC2 = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETACTIVITYTEAMLEVEL";
  private static final String PROC3 = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETPAXLEVEL";

  /* Charts */
  private static final String PROC4 = "PKG_QUERY_BADGE_ACTIVITY.PRC_GETBADGESEARNEDBARCHART";

  public CallPrcBadgeActivityByOrgReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_userId", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_resultsCursor", OracleTypes.CURSOR, new CallPrcBadgeActivityByOrgReport.BadgeActivityByOrgSummaryReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totalsCursor", OracleTypes.CURSOR, new CallPrcBadgeActivityByOrgReport.BadgeActivityByOrgSummaryReportTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_resultsCursor", OracleTypes.CURSOR, new CallPrcBadgeActivityByOrgReport.BadgeActivityTeamLevelTabularResultsReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totalsCursor", OracleTypes.CURSOR, new CallPrcBadgeActivityByOrgReport.BadgeActivityTeamLevelTabularResultsTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_resultsCursor", OracleTypes.CURSOR, new CallPrcBadgeActivityByOrgReport.BadgeActivityParticipantLevelTabularResultsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcBadgeActivityByOrgReport.BadgesEarnedBarChartReportMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_userId", reportParameters.get( "userId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    if ( ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /* TABLES */

  protected class BadgeActivityByOrgSummaryReportMapper implements ResultSetExtractor
  {
    @Override
    public List<BadgeActivityByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BadgeActivityByOrgReportValue> reportData = new ArrayList<BadgeActivityByOrgReportValue>();

      while ( rs.next() )
      {
        BadgeActivityByOrgReportValue reportValue = new BadgeActivityByOrgReportValue();
        reportValue.setHierarchyNodeId( rs.getLong( "NODE_ID" ) );
        reportValue.setOrgName( rs.getString( "NAME" ) );
        reportValue.setBadgesEarned( rs.getLong( "BADGES_EARNED" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  protected class BadgeActivityByOrgSummaryReportTotalsMapper implements ResultSetExtractor
  {
    @Override
    public BadgeActivityByOrgReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      BadgeActivityByOrgReportValue reportValue = new BadgeActivityByOrgReportValue();
      while ( rs.next() )
      {
        reportValue.setBadgesEarned( rs.getLong( "badges_earned" ) );
      }
      return reportValue;
    }
  }

  protected class BadgeActivityTeamLevelTabularResultsReportMapper implements ResultSetExtractor
  {
    @Override
    public List<BadgeActivityByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BadgeActivityByOrgReportValue> reportData = new ArrayList<BadgeActivityByOrgReportValue>();

      while ( rs.next() )
      {
        BadgeActivityByOrgReportValue reportValue = new BadgeActivityByOrgReportValue();
        reportValue.setParticipantName( rs.getString( "pax_name" ) );
        reportValue.setCountry( rs.getString( "pax_country" ) );
        reportValue.setBadgesEarned( rs.getLong( "badges_earned" ) );
        reportValue.setPaxId( rs.getLong( "pax_id" ) );
        reportValue.setTotalRecords( rs.getLong( "total_records" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BadgeActivityTeamLevelTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public BadgeActivityByOrgReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      BadgeActivityByOrgReportValue reportValue = new BadgeActivityByOrgReportValue();
      while ( rs.next() )
      {
        reportValue.setBadgesEarned( rs.getLong( "BADGES_EARNED" ) );
      }
      return reportValue;
    }
  }

  private class BadgeActivityParticipantLevelTabularResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<BadgeActivityByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BadgeActivityByOrgReportValue> reportData = new ArrayList<BadgeActivityByOrgReportValue>();

      while ( rs.next() )
      {
        BadgeActivityByOrgReportValue reportValue = new BadgeActivityByOrgReportValue();
        reportValue.setBadgeEarnedDate( rs.getDate( "EARNED_DATE" ) );
        reportValue.setParticipantName( rs.getString( "PARTICIPANT_NAME" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setBadgeName( rs.getString( "BADGE_NAME" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  /* CHART */

  protected class BadgesEarnedBarChartReportMapper implements ResultSetExtractor
  {
    @Override
    public List<BadgeActivityByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BadgeActivityByOrgReportValue> reportData = new ArrayList<BadgeActivityByOrgReportValue>();

      while ( rs.next() )
      {
        BadgeActivityByOrgReportValue reportValue = new BadgeActivityByOrgReportValue();
        reportValue.setOrgName( rs.getString( "NODE_NAME" ) );
        reportValue.setBadgesEarned( rs.getLong( "BADGES_EARNED" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}

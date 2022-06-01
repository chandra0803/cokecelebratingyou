
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

import com.biperf.core.value.nomination.NominationGivenByOrgReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcNominationGivenByOrgReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_NOMINATION.PRC_SUMMARY_BY_ORG";
  private static final String PROC2 = "PKG_QUERY_NOMINATION.PRC_NOMINATORS_BY_ORG";
  private static final String PROC3 = "PKG_QUERY_NOMINATION.PRC_NOMINEES_BY_ORG";
  private static final String PROC4 = "PKG_QUERY_NOMINATION.PRC_NOMINATIONS_BY_MONTH";

  public CallPrcNominationGivenByOrgReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_userId", Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationGivenByOrgReport.NominationGiverByOrgSummaryResultsMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcNominationGivenByOrgReport.NominationGiverByOrgSummaryResultsTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationGivenByOrgReport.NominationGiverNominatorsByOrgResultsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationGivenByOrgReport.NominationGiverNomineesByOrgResultsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcNominationGivenByOrgReport.NominationByMonthResultsMapper() ) );
        break;
      default:
        break;
    }
    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    Object nodeAndBelow = reportParameters.get( "nodeAndBelow" );
    if ( nodeAndBelow != null && ( (Boolean)nodeAndBelow ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_userId", reportParameters.get( "userId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class NominationGiverByOrgSummaryResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationGivenByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationGivenByOrgReportValue> reportData = new ArrayList<NominationGivenByOrgReportValue>();

      while ( rs.next() )
      {
        NominationGivenByOrgReportValue reportValue = new NominationGivenByOrgReportValue();
        reportValue.setOrgId( rs.getLong( "ORG_ID" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setEligibleNominatorsCnt( rs.getLong( "ELIGIBLE_NOMINATORS" ) );
        reportValue.setActualNominatorsCnt( rs.getLong( "ACTUAL_NOMINATORS" ) );
        reportValue.setEligbleNominatorsPct( rs.getBigDecimal( "PCT_ELIGIBLE_NOMINATORS" ) );
        reportValue.setEligibleNomineesCnt( rs.getLong( "ELIGIBLE_NOMINEES" ) );
        reportValue.setActualNomineesCnt( rs.getLong( "ACTUAL_NOMINEES" ) );
        reportValue.setEligibleNomineesPct( rs.getBigDecimal( "PCT_ELIGIBLE_NOMINEES" ) );
        reportValue.setNominationsSubmitted( rs.getLong( "NOMINATIONS_SUBMITTED" ) );
        reportValue.setNominationsReceived( rs.getLong( "NOMINATIONS_RECEIVED" ) );
        reportValue.setNominationsWonCnt( rs.getLong( "NOMINATIONS_WON" ) );
        reportValue.setPointsReceived( rs.getLong( "POINTS_RECEIVED" ) );
        reportValue.setCashReceived( rs.getLong( "CASH_RECEIVED" ) );
        reportValue.setOtherQtyReceived( rs.getLong( "OTHER_QTY_RECEIVED" ) );
        reportValue.setOtherValueReceived( rs.getLong( "OTHER_VALUE_RECEIVED" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportValue.setRecSeq( rs.getLong( "REC_SEQ" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  private class NominationGiverByOrgSummaryResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public NominationGivenByOrgReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      NominationGivenByOrgReportValue reportValue = new NominationGivenByOrgReportValue();
      while ( rs.next() )
      {
        reportValue.setEligibleNominatorsCnt( rs.getLong( "ELIGIBLE_NOMINATORS" ) );
        reportValue.setActualNominatorsCnt( rs.getLong( "ACTUAL_NOMINATORS" ) );
        reportValue.setEligbleNominatorsPct( rs.getBigDecimal( "PCT_ELIGIBLE_NOMINATORS" ) );
        reportValue.setEligibleNomineesCnt( rs.getLong( "ELIGIBLE_NOMINEES" ) );
        reportValue.setActualNomineesCnt( rs.getLong( "ACTUAL_NOMINEES" ) );
        reportValue.setEligibleNomineesPct( rs.getBigDecimal( "PCT_ELIGIBLE_NOMINEES" ) );
        reportValue.setNominationsSubmitted( rs.getLong( "NOMINATIONS_SUBMITTED" ) );
        reportValue.setNominationsReceived( rs.getLong( "NOMINATIONS_RECEIVED" ) );
        reportValue.setNominationsWonCnt( rs.getLong( "NOMINATIONS_WON" ) );
        reportValue.setPointsReceived( rs.getLong( "POINTS_RECEIVED" ) );
        reportValue.setCashReceived( rs.getLong( "CASH_RECEIVED" ) );
        reportValue.setOtherQtyReceived( rs.getLong( "OTHER_QTY_RECEIVED" ) );
        reportValue.setOtherValueReceived( rs.getLong( "OTHER_VALUE_RECEIVED" ) );
      }

      return reportValue;
    }
  }

  private class NominationGiverNominatorsByOrgResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationGivenByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationGivenByOrgReportValue> reportData = new ArrayList<NominationGivenByOrgReportValue>();

      while ( rs.next() )
      {
        NominationGivenByOrgReportValue reportValue = new NominationGivenByOrgReportValue();
        reportValue.setOrgId( rs.getLong( "ORG_ID" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setActualNominatorsCnt( rs.getLong( "ACTUAL_NOMINATORS" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }
  }

  private class NominationGiverNomineesByOrgResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationGivenByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationGivenByOrgReportValue> reportData = new ArrayList<NominationGivenByOrgReportValue>();

      while ( rs.next() )
      {
        NominationGivenByOrgReportValue reportValue = new NominationGivenByOrgReportValue();
        reportValue.setOrgId( rs.getLong( "ORG_ID" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setActualNomineesCnt( rs.getLong( "ACTUAL_NOMINEES" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }
  }

  private class NominationByMonthResultsMapper implements ResultSetExtractor
  {
    @Override
    public List<NominationGivenByOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NominationGivenByOrgReportValue> reportData = new ArrayList<NominationGivenByOrgReportValue>();

      while ( rs.next() )
      {
        NominationGivenByOrgReportValue reportValue = new NominationGivenByOrgReportValue();
        reportValue.setMonthName( rs.getString( "MONTH_NAME" ) );
        reportValue.setActualNominatorsCnt( rs.getLong( "ACTUAL_NOMINATORS" ) );
        reportValue.setActualNomineesCnt( rs.getLong( "ACTUAL_NOMINEES" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }
  }
}

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

import com.biperf.core.value.awardsreport.AwardsDetailByPaxReportValue;
import com.biperf.core.value.awardsreport.AwardsSummaryByPaxReportValue;
import com.biperf.core.value.awardsreport.OtherAwardsIssuedByPaxReportValue;
import com.biperf.core.value.awardsreport.ReceivedNotReceivedAwardsPaxReportValue;
import com.biperf.core.value.awardsreport.TotalPointsIssuedByPaxReportValue;
import com.biperf.core.value.awardsreport.TotalPointsIssuedByPeriodReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcAwardsByPax extends StoredProcedure
{
  /* Tables */
  private static final String PROC1 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSSUMMARYPAX";
  private static final String PROC2 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSDETAILPAXRES";

  /* Charts */
  private static final String PROC3 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETTOTPTSISS_PAXBARRES";
  private static final String PROC4 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETRECVNOTRCVAWD_PAXPIERES";

  public CallPrcAwardsByPax( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_jobPosition", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_participantStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_onTheSpotIncluded", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_nodeAndBelow", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_departments", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_award_type", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryIds", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_userid", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdSmryPaxRes", OracleTypes.CURSOR, new CallPrcAwardsByPax.BasicAwardsSummaryByPaxMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcAwardsByPax.BasicAwardsSummaryByPaxTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcAwardsByPax.BasicAwardsDetailByPaxMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcAwardsByPax.BasicAwardsDetailByPaxTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcAwardsByPax.BasicTotalPointsIssuedByPaxBarMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcAwardsByPax.BasicReceivedNotReceivedAwardsPaxPieMapper() ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_jobPosition", reportParameters.get( "jobPosition" ) );
    inParams.put( "p_in_participantStatus", reportParameters.get( "participantStatus" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_onTheSpotIncluded", reportParameters.get( "onTheSpotIncluded" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    if ( ( (Boolean)reportParameters.get( "nodeAndBelow" ) ).booleanValue() )
    {
      inParams.put( "p_in_nodeAndBelow", 1 );
    }
    else
    {
      inParams.put( "p_in_nodeAndBelow", 0 );
    }
    inParams.put( "p_in_departments", reportParameters.get( "department" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_award_type", reportParameters.get( "awardType" ) );
    inParams.put( "p_in_countryIds", reportParameters.get( "countryId" ) );
    inParams.put( "p_in_userid", reportParameters.get( "userId" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  /**
   * BasicTotalPointsIssuedByPaxBarMapper is an Inner class which implements the RowMapper.
   */

  private class BasicTotalPointsIssuedByPaxBarMapper implements ResultSetExtractor
  {
    @Override
    public List<TotalPointsIssuedByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<TotalPointsIssuedByPaxReportValue> reportData = new ArrayList<TotalPointsIssuedByPaxReportValue>();
      while ( rs.next() )
      {
        TotalPointsIssuedByPaxReportValue reportValue = new TotalPointsIssuedByPaxReportValue();
        reportValue.setPaxName( rs.getString( "PARTICIPANT_NAME" ) );
        reportValue.setTotalPointsCnt( rs.getLong( "MEDIA_AMOUNT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicOtherAwardsIssuedByPaxBarMapper implements ResultSetExtractor
  {
    @Override
    public List<OtherAwardsIssuedByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<OtherAwardsIssuedByPaxReportValue> reportData = new ArrayList<OtherAwardsIssuedByPaxReportValue>();

      while ( rs.next() )
      {
        OtherAwardsIssuedByPaxReportValue reportValue = new OtherAwardsIssuedByPaxReportValue();

        reportValue.setPlateauEarnedCnt( rs.getLong( "plateau_earned" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "onthespot" ) );
        reportValue.setPaxName( rs.getString( "participant_name" ) );
        reportValue.setOther( rs.getLong( "other" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicReceivedNotReceivedAwardsPaxPieMapper implements ResultSetExtractor
  {
    @Override
    public List<ReceivedNotReceivedAwardsPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReceivedNotReceivedAwardsPaxReportValue> reportData = new ArrayList<ReceivedNotReceivedAwardsPaxReportValue>();

      while ( rs.next() )
      {
        ReceivedNotReceivedAwardsPaxReportValue reportValue = new ReceivedNotReceivedAwardsPaxReportValue();

        reportValue.setReceivedPct( rs.getDouble( "RECEIVED_AWARD_PCT" ) );
        reportValue.setNotreceivedPct( rs.getDouble( "HAVE_NOTRECEIVED_AWARD_PCT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicTotalPointsIssuedByPeriodBarMapper implements ResultSetExtractor
  {
    @Override
    public List<TotalPointsIssuedByPeriodReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<TotalPointsIssuedByPeriodReportValue> reportData = new ArrayList<TotalPointsIssuedByPeriodReportValue>();

      while ( rs.next() )
      {
        TotalPointsIssuedByPeriodReportValue reportValue = new TotalPointsIssuedByPeriodReportValue();

        reportValue.setMonth( rs.getString( "MONTH_NAME" ) );
        reportValue.setTotalPointsCnt( rs.getLong( "TOTAL_POINTS_ISSUED" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicAwardsSummaryByPaxMapper implements ResultSetExtractor
  {
    @Override
    public List<AwardsSummaryByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardsSummaryByPaxReportValue> reportData = new ArrayList<AwardsSummaryByPaxReportValue>();

      while ( rs.next() )
      {
        AwardsSummaryByPaxReportValue reportValue = new AwardsSummaryByPaxReportValue();

        reportValue.setPaxName( rs.getString( "PARTICIPANT_NAME" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setCountry( rs.getString( "COUNTRY" ) );
        reportValue.setDepartment( rs.getString( "DEPARTMENT" ) );
        reportValue.setJobPosition( rs.getString( "JOB_POSITION" ) );
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "ON_THE_SPOT_DEPOSITED_COUNT" ) );
        reportValue.setUserId( rs.getLong( "USER_ID" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicAwardsSummaryByPaxTotalsMapper implements ResultSetExtractor
  {
    @Override
    public AwardsSummaryByPaxReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      AwardsSummaryByPaxReportValue reportValue = new AwardsSummaryByPaxReportValue();
      while ( rs.next() )
      {
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "ON_THE_SPOT_DEPOSITED_COUNT" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
      }
      return reportValue;
    }
  }

  private class BasicAwardsDetailByPaxMapper implements ResultSetExtractor
  {
    @Override
    public List<AwardsDetailByPaxReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardsDetailByPaxReportValue> reportData = new ArrayList<AwardsDetailByPaxReportValue>();

      while ( rs.next() )
      {
        AwardsDetailByPaxReportValue reportValue = new AwardsDetailByPaxReportValue();

        reportValue.setDate( rs.getDate( "AWARD_DATE" ) );
        reportValue.setPaxName( rs.getString( "PARTICIPANT_NAME" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setCountry( rs.getString( "COUNTRY" ) );
        reportValue.setDepartment( rs.getString( "DEPARTMENT" ) );
        reportValue.setJobPosition( rs.getString( "JOB_POSITION" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotSerial( rs.getString( "ON_THE_SPOT_SERIAL" ) );
        reportValue.setOther( rs.getString( "OTHER" ) );
        reportValue.setOtherValue( rs.getString( "OTHER_VALUE" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );
        reportValue.setLevelName( rs.getString( "LEVEL_NAME" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicAwardsDetailByPaxTotalsMapper implements ResultSetExtractor
  {
    @Override
    public AwardsDetailByPaxReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      AwardsDetailByPaxReportValue reportValue = new AwardsDetailByPaxReportValue();
      while ( rs.next() )
      {
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOther( rs.getString( "OTHER" ) );
        reportValue.setOtherValue( rs.getString( "OTHER_VALUE" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
      }
      return reportValue;
    }
  }

}

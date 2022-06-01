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

import com.biperf.core.value.awardsreport.AwardsFirstDetailReportValue;
import com.biperf.core.value.awardsreport.AwardsSecondDetailReportValue;
import com.biperf.core.value.awardsreport.AwardsSummaryReportValue;
import com.biperf.core.value.awardsreport.OtherAwardsIssuedForOrgReportValue;
import com.biperf.core.value.awardsreport.PersonsReceivingAwardsForOrgReportValue;
import com.biperf.core.value.awardsreport.ReceivedNotReceivedAwardsForOrgReportValue;
import com.biperf.core.value.awardsreport.ReceivedNotReceivedAwardsReportValue;
import com.biperf.core.value.awardsreport.TotalPointsIssuedForOrgReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcAwardsByOrg extends StoredProcedure
{
  /* Tables */
  private static final String PROC1 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSSUMMARY";
  private static final String PROC2 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSFIRSTDETAIL";
  private static final String PROC3 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETAWARDSSECONDDETAIL";

  /* Charts */
  private static final String PROC4 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETTOTPTSISS_ORGBARRES";
  private static final String PROC5 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETRCVNOTRCVAWD_ORGBARRES";
  private static final String PROC6 = "PKG_QUERY_AWARDS_RECEIVED.PRC_GETPERRCVAWD_ORGBARRES";

  public CallPrcAwardsByOrg( DataSource ds, String STORED_PROC_NAME )
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
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdSmryResult", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicAwardsSummaryMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdSmryResultTot", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicAwardsSummaryTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdfirdtlresult", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicAwardsFirstDetailMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdfirdtlResultTot", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicAwardsFirstDetailTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdsecdtlresult", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicAwardsSecondDetailMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_rs_getAwdsecdtlResultTot", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicAwardsSecondDetailTotalsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicTotalPointsIssuedForOrgBarMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicReceivedNotReceivedAwardsForOrgBarMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_result_set", OracleTypes.CURSOR, new CallPrcAwardsByOrg.BasicPersonsReceivingAwardsForOrgBarMapper() ) );
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
   * BasicAwardsSummaryMapper is an Inner class which implements the RowMapper.
   */
  private class BasicAwardsSummaryMapper implements ResultSetExtractor
  {
    @Override
    public List<AwardsSummaryReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardsSummaryReportValue> reportData = new ArrayList<AwardsSummaryReportValue>();

      while ( rs.next() )
      {
        AwardsSummaryReportValue reportValue = new AwardsSummaryReportValue();

        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setEligiblePaxCnt( rs.getLong( "ELIGIBLE_PARTICIPANTS" ) );
        reportValue.setReceivedAwardCnt( rs.getLong( "RECEIVED_AWARD" ) );
        reportValue.setReceivedAwardPct( rs.getDouble( "RECEIVED_AWARD_PCT" ) );
        reportValue.setHaveNotReceivedAwardCnt( rs.getLong( "HAVE_NOTRECEIVED_AWARD" ) );
        reportValue.setHaveNotReceivedAwardPct( rs.getDouble( "HAVE_NOTRECEIVED_AWARD_PCT" ) );
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "ON_THE_SPOT_DEPOSITED_COUNT" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setNodeId( rs.getLong( "NODE_ID" ) );
        reportValue.setCashReceived( rs.getLong( "CASH_RECEIVED" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );

        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicAwardsSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public AwardsSummaryReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      AwardsSummaryReportValue reportValue = new AwardsSummaryReportValue();
      while ( rs.next() )
      {
        reportValue.setEligiblePaxCnt( rs.getLong( "ELIGIBLE_PARTICIPANTS" ) );
        reportValue.setReceivedAwardCnt( rs.getLong( "RECEIVED_AWARD" ) );
        reportValue.setReceivedAwardPct( rs.getDouble( "RECEIVED_AWARD_PCT" ) );
        reportValue.setHaveNotReceivedAwardCnt( rs.getLong( "HAVE_NOTRECEIVED_AWARD" ) );
        reportValue.setHaveNotReceivedAwardPct( rs.getDouble( "HAVE_NOTRECEIVED_AWARD_PCT" ) );
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "ON_THE_SPOT_DEPOSITED_COUNT" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setCashReceived( rs.getLong( "CASH_RECEIVED" ) );
      }
      return reportValue;
    }
  }

  private class BasicAwardsFirstDetailMapper implements ResultSetExtractor
  {
    @Override
    public List<AwardsFirstDetailReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardsFirstDetailReportValue> reportData = new ArrayList<AwardsFirstDetailReportValue>();
      while ( rs.next() )
      {
        AwardsFirstDetailReportValue reportValue = new AwardsFirstDetailReportValue();

        reportValue.setPaxName( rs.getString( "PARTICIPANT_NAME" ) );
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setCountry( rs.getString( "COUNTRY" ) );
        reportValue.setDepartment( rs.getString( "DEPARTMENT" ) );
        reportValue.setJobPosition( rs.getString( "JOB_POSITION" ) );
        reportValue.setPromotionName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setPromotionId( rs.getLong( "PROMOTION_ID" ) );
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "ON_THE_SPOT_DEPOSITED_COUNT" ) );
        reportValue.setUserId( rs.getLong( "USER_ID" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setOtherValue( rs.getString( "OTHER_VALUE" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );

        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  private class BasicAwardsFirstDetailTotalsMapper implements ResultSetExtractor
  {
    @Override
    public AwardsFirstDetailReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      AwardsFirstDetailReportValue reportValue = new AwardsFirstDetailReportValue();
      while ( rs.next() )
      {
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "ON_THE_SPOT_DEPOSITED_COUNT" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setOtherValue( rs.getString( "OTHER_VALUE" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
      }
      return reportValue;
    }

  }

  private class BasicAwardsSecondDetailMapper implements ResultSetExtractor
  {
    @Override
    public List<AwardsSecondDetailReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<AwardsSecondDetailReportValue> reportData = new ArrayList<AwardsSecondDetailReportValue>();

      while ( rs.next() )
      {
        AwardsSecondDetailReportValue reportValue = new AwardsSecondDetailReportValue();
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
        reportValue.setPlateauLevelName( rs.getString( "LEVEL_NAME" ) );
        reportValue.setTotalRecords( rs.getLong( "TOTAL_RECORDS" ) );

        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  private class BasicAwardsSecondDetailTotalsMapper implements ResultSetExtractor
  {
    @Override
    public AwardsSecondDetailReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      AwardsSecondDetailReportValue reportValue = new AwardsSecondDetailReportValue();
      while ( rs.next() )
      {
        reportValue.setPointsReceivedCnt( rs.getLong( "POINTS_RECEIVED_COUNT" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "PLATEAU_EARNED_COUNT" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "SWEEPSTAKES_WON_COUNT" ) );
        reportValue.setOtherValue( rs.getString( "OTHER_VALUE" ) );
        reportValue.setCashReceivedCnt( rs.getLong( "CASH_RECEIVED_COUNT" ) );
      }
      return reportValue;
    }
  }

  private class BasicTotalPointsIssuedForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<TotalPointsIssuedForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<TotalPointsIssuedForOrgReportValue> reportData = new ArrayList<TotalPointsIssuedForOrgReportValue>();

      while ( rs.next() )
      {
        TotalPointsIssuedForOrgReportValue reportValue = new TotalPointsIssuedForOrgReportValue();
        reportValue.setOrgName( rs.getString( "org_name" ) );
        reportValue.setEarnedCount( rs.getLong( "earned_count" ) );
        reportData.add( reportValue );
      }
      return reportData;
    }

  }

  private class BasicOtherAwardsIssuedForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<OtherAwardsIssuedForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<OtherAwardsIssuedForOrgReportValue> reportData = new ArrayList<OtherAwardsIssuedForOrgReportValue>();

      while ( rs.next() )
      {
        OtherAwardsIssuedForOrgReportValue reportValue = new OtherAwardsIssuedForOrgReportValue();
        reportValue.setOrgName( rs.getString( "org_name" ) );
        reportValue.setPlateauEarnedCnt( rs.getLong( "plateau_earned" ) );
        reportValue.setSweepstakesWonCnt( rs.getLong( "sweepstakes_won" ) );
        reportValue.setOnTheSpotDepositedCnt( rs.getLong( "on_the_spot" ) );
        reportValue.setOther( rs.getLong( "other" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicReceivedNotReceivedAwardsPieMapper implements ResultSetExtractor
  {
    @Override
    public List<ReceivedNotReceivedAwardsReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReceivedNotReceivedAwardsReportValue> reportData = new ArrayList<ReceivedNotReceivedAwardsReportValue>();

      while ( rs.next() )
      {
        ReceivedNotReceivedAwardsReportValue reportValue = new ReceivedNotReceivedAwardsReportValue();
        reportValue.setReceivedPct( rs.getDouble( "RECEIVED_AWARD_PCT" ) );
        reportValue.setNotreceivedPct( rs.getDouble( "HAVE_NOTRECEIVED_AWARD_PCT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicReceivedNotReceivedAwardsForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<ReceivedNotReceivedAwardsForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ReceivedNotReceivedAwardsForOrgReportValue> reportData = new ArrayList<ReceivedNotReceivedAwardsForOrgReportValue>();

      while ( rs.next() )
      {
        ReceivedNotReceivedAwardsForOrgReportValue reportValue = new ReceivedNotReceivedAwardsForOrgReportValue();
        reportValue.setOrgName( rs.getString( "ORG_NAME" ) );
        reportValue.setReceivedPct( rs.getDouble( "RECEIVED_AWARD_PCT" ) );
        reportValue.setNotreceivedPct( rs.getDouble( "HAVE_NOTRECEIVED_AWARD_PCT" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

  private class BasicPersonsReceivingAwardsForOrgBarMapper implements ResultSetExtractor
  {
    @Override
    public List<PersonsReceivingAwardsForOrgReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PersonsReceivingAwardsForOrgReportValue> reportData = new ArrayList<PersonsReceivingAwardsForOrgReportValue>();

      while ( rs.next() )
      {
        PersonsReceivingAwardsForOrgReportValue reportValue = new PersonsReceivingAwardsForOrgReportValue();
        reportValue.setOrgName( rs.getString( "org" ) );
        reportValue.setPersonsCnt( rs.getLong( "person_received_award" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }

  }

}

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

import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.value.individualactivity.IndividualActivityReportValue;

import oracle.jdbc.OracleTypes;

/**
 * @author poddutur
 *
 */
public class CallPrcIndividualActivityReport extends StoredProcedure
{
  private static final String PROC1 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTABULARRESULTS";
  private static final String PROC2 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETGOALQUESTTABULARRESULTS";
  private static final String PROC3 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETONTHESPOTTABULARRESULTS";
  private static final String PROC4 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETAWARDSRECEIVED";
  private static final String PROC5 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETQUIZTABULARRESULTS";
  private static final String PROC6 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTHROWDOWNTABULARRESULTS";
  private static final String PROC7 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTHROWDOWNDETAILRESULTS";
  private static final String PROC8 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETNOMINATIONSRECEIVED";
  private static final String PROC9 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETNOMINATIONSGIVEN";
  private static final String PROC10 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETPRODUCTCLAIM";
  private static final String PROC11 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETRECOGNITIONSGIVEN";
  private static final String PROC12 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETRECOGNITIONSRECEIVED";
  private static final String PROC13 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETQUIZDETAILRESULTS";
  private static final String PROC14 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETCPTABULARRESULTS";

  /* Charts */
  private static final String PROC15 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETPOINTSRECEIVED";
  private static final String PROC16 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETPOINTSGIVENRECEIVED";
  private static final String PROC17 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETTOTALACTIVITYCHART";
  private static final String PROC18 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETMETRICSCHART";

  private static final String PROC19 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETBADGETABULARRESULTS";
  /* SSI */
  private static final String PROC20 = "PKG_QUERY_INDIVIDUAL_ACTIVITY.PRC_GETSSICONTESTS";

  public CallPrcIndividualActivityReport( DataSource ds, String STORED_PROC_NAME )
  {
    super( ds, STORED_PROC_NAME );
    // Note: Calls to declareParameter must be made in the same order as they appear in the
    // database's stored procedure parameter list.
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    if ( !STORED_PROC_NAME.equals( PROC20 ) )
    {
      declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    }
    declareParameter( new SqlParameter( "p_in_paxId", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortColName", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( STORED_PROC_NAME )
    {
      case PROC1:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.BasicIndividualActivityTabularResultsTotalsMapper() ) );
        break;
      case PROC2:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityGoalQuestMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.BasicIndividualActivityGoalQuestTabularResultsTotalsMapper() ) );
        break;
      case PROC3:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityOnTheSpotMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.BasicIndividualActivityOnTheSpotTabularResultsTotalsMapper() ) );
        break;
      case PROC4:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityAwardsReceivedReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.BasicIndividualActivityAwardsReceivedTabularResultsTotalsMapper() ) );
        break;
      case PROC5:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityQuizReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.BasicIndividualActivityQuizTabularResultsTotalsMapper() ) );
        break;
      case PROC6:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityThrowdownSummaryReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityThrowdownSummaryTotalsMapper() ) );
        break;
      case PROC7:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityThrowdownDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityThrowdownDetailTotalsMapper() ) );
        break;
      case PROC8:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityNominationsReceivedReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC9:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityNominationsGivenReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC10:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityProductClaimReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC11:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityRecognitionsGivenReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC12:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityRecognitionsReceivedReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC13:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityQuizDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case PROC14:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityChallengePointMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.BasicIndividualActivityChallengePointTabularResultsTotalsMapper() ) );
        break;
      case PROC15:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityPointsReceivedReportMapper() ) );
        break;
      case PROC16:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityPointsGivenReceivedReportMapper() ) );
        break;
      case PROC17:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityTotalActivityReportMapper() ) );
        break;
      case PROC18:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityMetricsReportMapper() ) );
        break;
      case PROC19:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityBadgeDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivityBadgeDetailTotalsMapper() ) );
        break;
      case PROC20:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new CallPrcIndividualActivityReport.IndividualActivitySSIContestReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      default:
        break;
    }

    compile();
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_paxId", reportParameters.get( "paxId" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  public Map<String, Object> executeSSIProcedure( Map<String, Object> reportParameters )
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_paxId", reportParameters.get( "paxId" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    // inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_sortColName", reportParameters.get( "sortColName" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  private class IndividualActivityReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setModuleName( rs.getString( "Activity" ) );
        reportValue.setModuleAssetCode( rs.getString( "ACTIVITY_CODE" ) );
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setPlateauEarned( rs.getLong( "Plateau_Earned" ) );
        reportValue.setSweepstakesWon( rs.getLong( "Sweepstakes_Won" ) );
        reportValue.setOtherAward( rs.getLong( "Other_Awards" ) );
        reportValue.setReceived( rs.getLong( "Received" ) );
        reportValue.setSent( rs.getLong( "Sent" ) );
        reportValue.setViewAllAwards( rs.getString( "View_All_Awards" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicIndividualActivityTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

      while ( rs.next() )
      {
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setPlateauEarned( rs.getLong( "Plateau_Earned" ) );
        reportValue.setSweepstakesWon( rs.getLong( "Sweepstakes_Won" ) );
        reportValue.setReceived( rs.getLong( "Received" ) );
        reportValue.setSent( rs.getLong( "Sent" ) );
        reportValue.setOtherAward( rs.getLong( "Other_Awards" ) );
      }
      return reportValue;
    }
  }

  private class IndividualActivityPointsReceivedReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setMonth( rs.getString( "months" ) );
        reportValue.setPoints( rs.getLong( "total_points_count" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityPointsGivenReceivedReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setUserId( rs.getLong( "USER_ID" ) );
        reportValue.setPromoName( rs.getString( "PROMOTION_NAME" ) );
        reportValue.setPointsReceived( rs.getLong( "POINTS_RECEIVED" ) );
        reportValue.setPointsGiven( rs.getLong( "POINTS_GIVEN" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityTotalActivityReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setModuleName( rs.getString( "Activity" ) );
        reportValue.setReceived( rs.getLong( "Act_count" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityMetricsReportMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

      while ( rs.next() )
      {
        reportValue.setUserName( rs.getString( "USER_NAME" ) );
        reportValue.setUserRecognitionsSent( rs.getLong( "USER_RECOGNITIONS_SENT" ) );
        reportValue.setUserRecognitionsRcvd( rs.getLong( "USER_RECOGNITIONS_RCVD" ) );
        reportValue.setOrgAvgRecognitionsSent( NumberFormatUtil.nullCheckBigDecimal( rs.getBigDecimal( "ORG_AVG_RECOGNITIONS_SENT" ) ) );
        reportValue.setOrgAvgRecognitionsRcvd( NumberFormatUtil.nullCheckBigDecimal( rs.getBigDecimal( "ORG_AVG_RECOGNITIONS_RCVD" ) ) );
        reportValue.setCompanyAvgRecognitionsSent( NumberFormatUtil.nullCheckBigDecimal( rs.getBigDecimal( "COMPANY_AVG_RECOGNITIONS_SENT" ) ) );
        reportValue.setCompanyAvgRecognitionsRcvd( NumberFormatUtil.nullCheckBigDecimal( rs.getBigDecimal( "COMPANY_AVG_RECOGNITIONS_RCVD" ) ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivityAwardsReceivedReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setDateSubmitted( rs.getDate( "date_submitted" ) );
        reportValue.setOrgName( rs.getString( "org_name" ) );
        reportValue.setPromoName( rs.getString( "promotion_name" ) );
        reportValue.setPoints( rs.getLong( "points" ) );
        reportValue.setPlateauEarned( rs.getLong( "plateau_earned" ) );
        reportValue.setSweepstakesWon( rs.getLong( "sweepstakes_won" ) );
        reportValue.setOnTheSpot( rs.getString( "onthespot" ) );
        reportValue.setRecipient( rs.getString( "recipient" ) );
        reportValue.setGiverName( rs.getString( "sender" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicIndividualActivityAwardsReceivedTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

      while ( rs.next() )
      {
        reportValue.setPoints( rs.getLong( "points" ) );
        reportValue.setPlateauEarned( rs.getLong( "plateau_earned" ) );
        reportValue.setSweepstakesWon( rs.getLong( "sweepstakes_won" ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivityNominationsReceivedReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromoName( rs.getString( "promo_name" ) );
        reportValue.setDateApproved( rs.getDate( "date_approved" ) );
        reportValue.setNominatorName( rs.getString( "nominator" ) );
        reportValue.setPoints( rs.getLong( "points" ) );
        reportValue.setSweepstakesWon( rs.getLong( "sweepstakes_won" ) );
        reportValue.setClaimId( rs.getLong( "claim_id" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityNominationsGivenReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromoName( rs.getString( "promo_name" ) );
        reportValue.setDateApproved( rs.getDate( "date_approved" ) );
        reportValue.setNomineeName( rs.getString( "nominee" ) );
        reportValue.setPoints( rs.getLong( "points" ) );
        reportValue.setSweepstakesWon( rs.getLong( "sweepstakes_won" ) );
        reportValue.setClaimId( rs.getLong( "claim_id" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityProductClaimReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setClaimNumber( rs.getString( "CLAIM_NUMBER" ) );
        reportValue.setPromoName( rs.getString( "PROMOTION" ) );
        reportValue.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        reportValue.setSoldTo( rs.getString( "SOLD_TO" ) );
        reportValue.setClaimStatus( rs.getString( "CLAIM_STATUS" ) );
        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityRecognitionsGivenReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromoName( rs.getString( "PROMO_NAME" ) );
        reportValue.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        reportValue.setReceiverName( rs.getString( "RECEIVER_NAME" ) );
        reportValue.setPointsCount( rs.getLong( "POINTS_CNT" ) );
        reportValue.setPlateauEarnedCount( rs.getLong( "PLATEAU_EARNED_CNT" ) );
        reportValue.setSweepstakesWonCount( rs.getLong( "SWEEPSTAKES_WON_CNT" ) );
        reportValue.setStatus( rs.getString( "STATUS" ) );
        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityRecognitionsReceivedReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromoName( rs.getString( "PROMO_NAME" ) );
        reportValue.setDateSubmitted( rs.getDate( "DATE_SUBMITTED" ) );
        reportValue.setGiverName( rs.getString( "GIVER_NAME" ) );
        reportValue.setPointsCount( rs.getLong( "POINTS_CNT" ) );
        reportValue.setPlateauEarnedCount( rs.getLong( "PLATEAU_EARNED_CNT" ) );
        reportValue.setSweepstakesWonCount( rs.getLong( "SWEEPSTAKES_WON_CNT" ) );
        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityThrowdownSummaryReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
        reportValue.setPromoId( rs.getLong( "promotion_id" ) );
        reportValue.setPromoName( rs.getString( "promotion_name" ) );
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportValue.setRank( rs.getLong( "RANK" ) );
        reportValue.setPoints( rs.getLong( "payout" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityThrowdownSummaryTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportValue.setPoints( rs.getLong( "payout" ) );
      }
      return reportValue;
    }
  }

  private class IndividualActivityThrowdownDetailReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
        reportValue.setPromoName( rs.getString( "promotion_name" ) );
        reportValue.setRoundNumber( rs.getLong( "round_number" ) );
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportValue.setRank( rs.getLong( "RANK" ) );
        reportValue.setPoints( rs.getLong( "payout" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityThrowdownDetailTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setWinsCnt( rs.getLong( "Wins" ) );
        reportValue.setLossCnt( rs.getLong( "Losses" ) );
        reportValue.setTiesCnt( rs.getLong( "Ties" ) );
        reportValue.setActivityCnt( rs.getBigDecimal( "activity" ) );
        reportValue.setPoints( rs.getLong( "payout" ) );
      }
      return reportValue;
    }
  }

  private class IndividualActivityQuizReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromoName( rs.getString( "PROMO_NAME" ) );
        reportValue.setQuizAttempts( rs.getLong( "QUIZ_ATTEMPTS" ) );
        reportValue.setQuizAttemptsFailed( rs.getLong( "QUIZ_FAILED" ) );
        reportValue.setQuizAttemptsPassed( rs.getLong( "QUIZ_PASSED" ) );
        reportValue.setAwardsGiven( rs.getLong( "POINTS" ) );
        reportValue.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
        reportValue.setPromoId( rs.getLong( "PROMOTION_ID" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicIndividualActivityQuizTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

      while ( rs.next() )
      {
        reportValue.setQuizAttempts( rs.getLong( "QUIZ_ATTEMPTS" ) );
        reportValue.setQuizAttemptsFailed( rs.getLong( "QUIZ_FAILED" ) );
        reportValue.setQuizAttemptsPassed( rs.getLong( "QUIZ_PASSED" ) );
        reportValue.setAwardsGiven( rs.getLong( "POINTS" ) );
        reportValue.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivityQuizDetailReportMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setQuizName( rs.getString( "PROMOTION" ) );
        reportValue.setQuizDate( rs.getDate( "QUIZ_DATE" ) );
        reportValue.setQuizResult( rs.getString( "QUIZ_RESULT" ) );
        reportValue.setAwardsGiven( rs.getLong( "POINTS" ) );
        reportValue.setSweepstakesWon( rs.getLong( "SWEEPSTAKES_WON" ) );
        reportValue.setClaimId( rs.getLong( "CLAIM_ID" ) );
        reportValue.setSubmitterId( rs.getLong( "SUBMITTER_ID" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityGoalQuestMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setOrgName( rs.getString( "Org_Name" ) );
        reportValue.setPromoName( rs.getString( "Promotion" ) );
        reportValue.setGoalLevelName( rs.getString( "Level_Selected" ) );
        reportValue.setBaseQuantity( rs.getLong( "Base" ) );
        reportValue.setAmountToAchieve( rs.getLong( "Goal" ) );
        reportValue.setActualResults( rs.getLong( "Actual_Results" ) );
        reportValue.setPercentageOfGoal( rs.getDouble( "pct_of_Goal" ) );
        reportValue.setAchieved( rs.getString( "Achieved" ) );
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setPlateauEarned( rs.getLong( "Plateau_Earned" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicIndividualActivityGoalQuestTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setPlateauEarned( rs.getLong( "Plateau_Earned" ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivityChallengePointMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setOrgName( rs.getString( "Org_Name" ) );
        reportValue.setPromoName( rs.getString( "Promotion" ) );
        reportValue.setChallengePointLevelName( rs.getString( "Level_Selected" ) );
        reportValue.setBaseQuantity( rs.getLong( "Base" ) );
        reportValue.setAmountToAchieve( rs.getLong( "ChallengePoint" ) );
        reportValue.setActualResults( rs.getLong( "Actual_Results" ) );
        reportValue.setPercentageOfChallengePoint( rs.getDouble( "pct_of_CP" ) );
        reportValue.setAchieved( rs.getString( "Achieved" ) );
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setPlateauEarned( rs.getLong( "Plateau_Earned" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicIndividualActivityChallengePointTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setPlateauEarned( rs.getLong( "Plateau_Earned" ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivityOnTheSpotMapper implements ResultSetExtractor
  {
    @Override
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setTransDate( rs.getDate( "Trans_Date" ) );
        reportValue.setRecipient( rs.getString( "Recipient" ) );
        reportValue.setOrgName( rs.getString( "Org_Name" ) );
        reportValue.setPoints( rs.getLong( "Points" ) );
        reportValue.setOnTheSpot( rs.getString( "OnTheSpot" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class BasicIndividualActivityOnTheSpotTabularResultsTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

      while ( rs.next() )
      {
        reportValue.setPoints( rs.getLong( "Points" ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivityBadgeDetailReportMapper implements ResultSetExtractor
  {
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromotionName( rs.getString( "promotion_name" ) );
        reportValue.setMediaDate( rs.getDate( "media_date" ) );
        reportValue.setBadgePoints( rs.getLong( "points" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

  private class IndividualActivityBadgeDetailTotalsMapper implements ResultSetExtractor
  {
    @Override
    public IndividualActivityReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      IndividualActivityReportValue reportValue = new IndividualActivityReportValue();
      while ( rs.next() )
      {
        reportValue.setBadgePoints( rs.getLong( "Points" ) );
      }

      return reportValue;
    }
  }

  private class IndividualActivitySSIContestReportMapper implements ResultSetExtractor
  {
    public List<IndividualActivityReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<IndividualActivityReportValue> reportData = new ArrayList<IndividualActivityReportValue>();

      while ( rs.next() )
      {
        IndividualActivityReportValue reportValue = new IndividualActivityReportValue();

        reportValue.setPromotionName( rs.getString( "SSI_CONTEST_NAME" ) );
        reportValue.setMediaDate( rs.getDate( "PAYOUT_ISSUE_DATE" ) );
        reportValue.setPoints( rs.getLong( "POINTS" ) );
        reportValue.setOther( rs.getLong( "OTHER" ) );
        reportValue.setOtherValue( rs.getString( "OTHER_VALUE" ) );
        reportValue.setContestId( rs.getLong( "SSI_CONTEST_ID" ) );
        reportValue.setIsAtnContest( rs.getString( "IS_ATN_CONTEST" ) );
        reportData.add( reportValue );
      }

      return reportData;
    }
  }

}

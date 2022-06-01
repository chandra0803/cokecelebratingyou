
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

import com.biperf.core.value.challengepoint.ChallengePointAchievementReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcChallengePointAchievementReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_challengepoint.prc_getCPAchievementTabRes";
  private static final String DETAIL_RESULTS = "pkg_query_challengepoint.prc_getCPAchievementDetailRes";
  private static final String PERCENTAGE_ACHIEVED = "pkg_query_challengepoint.prc_getCPPctAchievedChart";
  private static final String COUNT_ACHIEVED = "pkg_query_challengepoint.prc_getCPCountAchievedChart";
  private static final String RESULTS_CHART = "pkg_query_challengepoint.prc_getCPAchievementResChart";

  public CallPrcChallengePointAchievementReports( DataSource ds, String storedProcName )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointAchievementReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointAchievementReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointAchievementDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointAchievementDetailReportTotalsMapper() ) );
        break;
      case PERCENTAGE_ACHIEVED:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointAchievementPercentageAchievedReportMapper() ) );
        break;
      case COUNT_ACHIEVED:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointAchievementCountAchievedReportMapper() ) );
        break;
      case RESULTS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointAchievementResultsChartMapper() ) );
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

  protected class ChallengePointAchievementReportMapper implements ResultSetExtractor<List<ChallengePointAchievementReportValue>>
  {
    @Override
    public List<ChallengePointAchievementReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointAchievementReportValue> results = new ArrayList<ChallengePointAchievementReportValue>();
      while ( rs.next() )
      {
        ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setOrgId( rs.getLong( "NODE_ID" ) );
        result.setTotalPaxCnt( rs.getLong( "TOTAL_SELECTED" ) );
        result.setLevel1SelectedCnt( rs.getLong( "LEVEL_1_SELECTED_CNT" ) );
        result.setLevel1AchievedCnt( rs.getLong( "LEVEL_1_ACHIEVED_CNT" ) );
        result.setLevel1AchievedPercent( rs.getBigDecimal( "LEVEL_1_ACHIEVED_PERCENT" ) );
        result.setLevel1AwardCnt( rs.getLong( "LEVEL_1_AWARD_CNT" ) );
        result.setLevel2SelectedCnt( rs.getLong( "LEVEL_2_SELECTED_CNT" ) );
        result.setLevel2AchievedCnt( rs.getLong( "LEVEL_2_ACHIEVED_CNT" ) );
        result.setLevel2AchievedPercent( rs.getBigDecimal( "LEVEL_2_ACHIEVED_PERCENT" ) );
        result.setLevel2AwardCnt( rs.getLong( "LEVEL_2_AWARD_CNT" ) );
        result.setLevel3SelectedCnt( rs.getLong( "LEVEL_3_SELECTED_CNT" ) );
        result.setLevel3AchievedCnt( rs.getLong( "LEVEL_3_ACHIEVED_CNT" ) );
        result.setLevel3AchievedPercent( rs.getBigDecimal( "LEVEL_3_ACHIEVED_PERCENT" ) );
        result.setLevel3AwardCnt( rs.getLong( "LEVEL_3_AWARD_CNT" ) );
        result.setLevel4SelectedCnt( rs.getLong( "LEVEL_4_SELECTED_CNT" ) );
        result.setLevel4AchievedCnt( rs.getLong( "LEVEL_4_ACHIEVED_CNT" ) );
        result.setLevel4AchievedPercent( rs.getBigDecimal( "LEVEL_4_ACHIEVED_PERCENT" ) );
        result.setLevel4AwardCnt( rs.getLong( "LEVEL_4_AWARD_CNT" ) );
        result.setLevel5SelectedCnt( rs.getLong( "LEVEL_5_SELECTED_CNT" ) );
        result.setLevel5AchievedCnt( rs.getLong( "LEVEL_5_ACHIEVED_CNT" ) );
        result.setLevel5AchievedPercent( rs.getBigDecimal( "LEVEL_5_ACHIEVED_PERCENT" ) );
        result.setLevel5AwardCnt( rs.getLong( "LEVEL_5_AWARD_CNT" ) );
        result.setLevel6SelectedCnt( rs.getLong( "LEVEL_6_SELECTED_CNT" ) );
        result.setLevel6AchievedCnt( rs.getLong( "LEVEL_6_ACHIEVED_CNT" ) );
        result.setLevel6AchievedPercent( rs.getBigDecimal( "LEVEL_6_ACHIEVED_PERCENT" ) );
        result.setLevel6AwardCnt( rs.getLong( "LEVEL_6_AWARD_CNT" ) );
        result.setBaseQuantity( rs.getBigDecimal( "BASE_QUANTITY" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "CHALLENGEPOINT" ) );
        result.setCurrentValue( rs.getBigDecimal( "ACTUAL_RESULT" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_ACHIEVED" ) );
        // result.setIsLeaf( rs.getBoolean( "" )); //Not used
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointAchievementReportTotalsMapper implements ResultSetExtractor<ChallengePointAchievementReportValue>
  {
    @Override
    public ChallengePointAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
      if ( rs.next() )
      {
        result.setTotalPaxCnt( rs.getLong( "TOTAL_SELECTED" ) );
        result.setLevel1SelectedCnt( rs.getLong( "LEVEL_1_SELECTED_CNT" ) );
        result.setLevel1AchievedCnt( rs.getLong( "LEVEL_1_ACHIEVED_CNT" ) );
        result.setLevel1AchievedPercent( rs.getBigDecimal( "LEVEL_1_ACHIEVED_PERCENT" ) );
        result.setLevel1AwardCnt( rs.getLong( "LEVEL_1_AWARD_CNT" ) );
        result.setLevel2SelectedCnt( rs.getLong( "LEVEL_2_SELECTED_CNT" ) );
        result.setLevel2AchievedCnt( rs.getLong( "LEVEL_2_ACHIEVED_CNT" ) );
        result.setLevel2AchievedPercent( rs.getBigDecimal( "LEVEL_2_ACHIEVED_PERCENT" ) );
        result.setLevel2AwardCnt( rs.getLong( "LEVEL_2_AWARD_CNT" ) );
        result.setLevel3SelectedCnt( rs.getLong( "LEVEL_3_SELECTED_CNT" ) );
        result.setLevel3AchievedCnt( rs.getLong( "LEVEL_3_ACHIEVED_CNT" ) );
        result.setLevel3AchievedPercent( rs.getBigDecimal( "LEVEL_3_ACHIEVED_PERCENT" ) );
        result.setLevel3AwardCnt( rs.getLong( "LEVEL_3_AWARD_CNT" ) );
        result.setLevel4SelectedCnt( rs.getLong( "LEVEL_4_SELECTED_CNT" ) );
        result.setLevel4AchievedCnt( rs.getLong( "LEVEL_4_ACHIEVED_CNT" ) );
        result.setLevel4AchievedPercent( rs.getBigDecimal( "LEVEL_4_ACHIEVED_PERCENT" ) );
        result.setLevel4AwardCnt( rs.getLong( "LEVEL_4_AWARD_CNT" ) );
        result.setLevel5SelectedCnt( rs.getLong( "LEVEL_5_SELECTED_CNT" ) );
        result.setLevel5AchievedCnt( rs.getLong( "LEVEL_5_ACHIEVED_CNT" ) );
        result.setLevel5AchievedPercent( rs.getBigDecimal( "LEVEL_5_ACHIEVED_PERCENT" ) );
        result.setLevel5AwardCnt( rs.getLong( "LEVEL_5_AWARD_CNT" ) );
        result.setLevel6SelectedCnt( rs.getLong( "LEVEL_6_SELECTED_CNT" ) );
        result.setLevel6AchievedCnt( rs.getLong( "LEVEL_6_ACHIEVED_CNT" ) );
        result.setLevel6AchievedPercent( rs.getBigDecimal( "LEVEL_6_ACHIEVED_PERCENT" ) );
        result.setLevel6AwardCnt( rs.getLong( "LEVEL_6_AWARD_CNT" ) );
        result.setBaseQuantity( rs.getBigDecimal( "BASE_QUANTITY" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "CHALLENGEPOINT" ) );
        result.setCurrentValue( rs.getBigDecimal( "ACTUAL_RESULT" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_ACHIEVED" ) );
      }
      return result;
    }
  }

  protected class ChallengePointAchievementDetailReportMapper implements ResultSetExtractor<List<ChallengePointAchievementReportValue>>
  {
    @Override
    public List<ChallengePointAchievementReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointAchievementReportValue> results = new ArrayList<ChallengePointAchievementReportValue>();
      while ( rs.next() )
      {
        ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
        result.setPaxName( rs.getString( "PAX_NAME" ) );
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setPromoName( rs.getString( "PROMO_NAME" ) );
        result.setLevelNumber( rs.getString( "LEVEL_NUMBER" ) );
        result.setBaseQuantity( rs.getBigDecimal( "BASE" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "CHALLENGEPOINT" ) );
        result.setCurrentValue( rs.getBigDecimal( "ACTUAL" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_OF_CHALLENGEPOINT" ) );
        result.setIsAchieved( rs.getBoolean( "ACHIEVED" ) );
        result.setPoints( rs.getLong( "TOTAL_POINTS" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointAchievementDetailReportTotalsMapper implements ResultSetExtractor<ChallengePointAchievementReportValue>
  {
    @Override
    public ChallengePointAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
      while ( rs.next() )
      {
        result.setBaseQuantity( rs.getBigDecimal( "BASE" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "CHALLENGEPOINT" ) );
        result.setCurrentValue( rs.getBigDecimal( "ACTUAL" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_OF_CHALLENGEPOINT" ) );
        result.setPoints( rs.getLong( "TOTAL_POINTS" ) );
      }
      return result;
    }
  }

  protected class ChallengePointAchievementPercentageAchievedReportMapper implements ResultSetExtractor<ChallengePointAchievementReportValue>
  {
    @Override
    public ChallengePointAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
      while ( rs.next() )
      {
        result.setLevel1AchievedPercent( rs.getBigDecimal( "LEVEL_1_ACHIEVED_PERCENT" ) );
        result.setLevel2AchievedPercent( rs.getBigDecimal( "LEVEL_2_ACHIEVED_PERCENT" ) );
        result.setLevel3AchievedPercent( rs.getBigDecimal( "LEVEL_3_ACHIEVED_PERCENT" ) );
        result.setLevel4AchievedPercent( rs.getBigDecimal( "LEVEL_4_ACHIEVED_PERCENT" ) );
        result.setLevel5AchievedPercent( rs.getBigDecimal( "LEVEL_5_ACHIEVED_PERCENT" ) );
        result.setLevel6AchievedPercent( rs.getBigDecimal( "LEVEL_6_ACHIEVED_PERCENT" ) );
      }
      return result;
    }
  }

  protected class ChallengePointAchievementCountAchievedReportMapper implements ResultSetExtractor<ChallengePointAchievementReportValue>
  {
    @Override
    public ChallengePointAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
      while ( rs.next() )
      {
        result.setLevel1AchievedCnt( rs.getLong( "LEVEL_1_ACHIEVED_CNT" ) );
        result.setLevel2AchievedCnt( rs.getLong( "LEVEL_2_ACHIEVED_CNT" ) );
        result.setLevel3AchievedCnt( rs.getLong( "LEVEL_3_ACHIEVED_CNT" ) );
        result.setLevel4AchievedCnt( rs.getLong( "LEVEL_4_ACHIEVED_CNT" ) );
        result.setLevel5AchievedCnt( rs.getLong( "LEVEL_5_ACHIEVED_CNT" ) );
        result.setLevel6AchievedCnt( rs.getLong( "LEVEL_6_ACHIEVED_CNT" ) );
      }
      return result;
    }
  }

  protected class ChallengePointAchievementResultsChartMapper implements ResultSetExtractor<List<ChallengePointAchievementReportValue>>
  {
    @Override
    public List<ChallengePointAchievementReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointAchievementReportValue> results = new ArrayList<ChallengePointAchievementReportValue>();
      while ( rs.next() )
      {
        ChallengePointAchievementReportValue result = new ChallengePointAchievementReportValue();
        result.setPaxName( rs.getString( "pax_name" ) );
        result.setPromoName( rs.getString( "promotion_name" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setBaseQuantity( rs.getBigDecimal( "base_quantity" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "CHALLENGEPOINT" ) );
        result.setCurrentValue( rs.getBigDecimal( "ACTUAL_RESULTS" ) );
        results.add( result );
      }
      return results;
    }
  }
}

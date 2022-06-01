
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

import com.biperf.core.value.goalquest.GoalQuestAchievementReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcGQAchievementReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_goalquest.prc_getGQAchievementTabRes";
  private static final String DETAIL_RESULTS = "pkg_query_goalquest.prc_getGQAchievementDetailRes";
  private static final String PERCENT_ACHIEVED_CHART = "pkg_query_goalquest.prc_getGQPctAchievedChart";
  private static final String COUNT_ACHIEVED_CHART = "pkg_query_goalquest.prc_getGQCountAchievedChart";
  private static final String RESULTS_CHART = "pkg_query_goalquest.prc_getGQAchievementResChart";

  public CallPrcGQAchievementReports( DataSource ds, String storedProcName )
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
      case TABULAR_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestAchievementReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new GoalQuestAchievementReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestAchievementDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new GoalQuestAchievementDetailReportTotalsMapper() ) );
        break;
      case PERCENT_ACHIEVED_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestAchievementPercentageAchievedReportMapper() ) );
        break;
      case COUNT_ACHIEVED_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestAchievementCountAchievedReportMapper() ) );
        break;
      case RESULTS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestAchievementResultsChartMapper() ) );
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

  protected class GoalQuestAchievementReportMapper implements ResultSetExtractor<List<GoalQuestAchievementReportValue>>
  {
    @Override
    public List<GoalQuestAchievementReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestAchievementReportValue> results = new ArrayList<GoalQuestAchievementReportValue>();
      while ( rs.next() )
      {
        GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setOrgId( rs.getLong( "NODE_ID" ) );
        result.setTotalPaxCnt( rs.getLong( "total_participants" ) );
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
        result.setBaseQuantity( rs.getLong( "BASE_QUANTITY" ) );
        result.setAmountToAchieve( rs.getLong( "AMT_TO_ACHIEVE" ) );
        result.setCurrentValue( rs.getLong( "CURRENT_VALUE" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_ACHIEVED" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestAchievementReportTotalsMapper implements ResultSetExtractor<GoalQuestAchievementReportValue>
  {
    @Override
    public GoalQuestAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
      if ( rs.next() )
      {
        result.setTotalPaxCnt( rs.getLong( "total_participants" ) );
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
        result.setBaseQuantity( rs.getLong( "BASE_QUANTITY" ) );
        result.setAmountToAchieve( rs.getLong( "AMT_TO_ACHIEVE" ) );
        result.setCurrentValue( rs.getLong( "CURRENT_VALUE" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_ACHIEVED" ) );
      }
      return result;
    }
  }

  protected class GoalQuestAchievementDetailReportMapper implements ResultSetExtractor<List<GoalQuestAchievementReportValue>>
  {
    @Override
    public List<GoalQuestAchievementReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestAchievementReportValue> results = new ArrayList<GoalQuestAchievementReportValue>();
      while ( rs.next() )
      {
        GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
        result.setPaxName( rs.getString( "PAX_NAME" ) );
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setPromoName( rs.getString( "PROMO_NAME" ) );
        result.setLevelNumber( rs.getString( "LEVEL_NUMBER" ) );
        result.setBaseQuantity( rs.getLong( "BASE" ) );
        result.setAmountToAchieve( rs.getLong( "GOAL" ) );
        result.setCurrentValue( rs.getLong( "ACTUAL" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_OF_GOAL" ) );
        result.setIsAchieved( rs.getBoolean( "IS_ACHIEVED" ) );
        result.setPoints( rs.getLong( "POINTS" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestAchievementDetailReportTotalsMapper implements ResultSetExtractor<GoalQuestAchievementReportValue>
  {
    @Override
    public GoalQuestAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
      if ( rs.next() )
      {
        result.setBaseQuantity( rs.getLong( "BASE" ) );
        result.setAmountToAchieve( rs.getLong( "GOAL" ) );
        result.setCurrentValue( rs.getLong( "ACTUAL" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_OF_GOAL" ) );
        result.setPoints( rs.getLong( "POINTS" ) );
      }
      return result;
    }
  }

  protected class GoalQuestAchievementPercentageAchievedReportMapper implements ResultSetExtractor<GoalQuestAchievementReportValue>
  {
    @Override
    public GoalQuestAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
      if ( rs.next() )
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

  protected class GoalQuestAchievementCountAchievedReportMapper implements ResultSetExtractor<GoalQuestAchievementReportValue>
  {
    @Override
    public GoalQuestAchievementReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
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

  protected class GoalQuestAchievementResultsChartMapper implements ResultSetExtractor<List<GoalQuestAchievementReportValue>>
  {
    @Override
    public List<GoalQuestAchievementReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestAchievementReportValue> results = new ArrayList<GoalQuestAchievementReportValue>();
      while ( rs.next() )
      {
        GoalQuestAchievementReportValue result = new GoalQuestAchievementReportValue();
        result.setPaxName( rs.getString( "pax_name" ) );
        result.setPromoName( rs.getString( "promo_name" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setBaseQuantity( rs.getLong( "base_quantity" ) );
        result.setAmountToAchieve( rs.getLong( "amount_to_achieve" ) );
        result.setCurrentValue( rs.getLong( "current_value" ) );
        results.add( result );
      }
      return results;
    }
  }
}

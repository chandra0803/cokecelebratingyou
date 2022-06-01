
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

import com.biperf.core.value.goalquest.GoalQuestSelectionReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcGQSelectionReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_goalquest.prc_getGQSelectionTabRes";
  private static final String VALID_LEVEL_NUMS = "pkg_query_goalquest.prc_getGQSelectionValidLvlNums";
  private static final String DETAIL_RESULTS = "pkg_query_goalquest.prc_getGQSelectionDetailRes";
  private static final String TOTALS_CHART = "pkg_query_goalquest.prc_getGQSelectionTotalsChart";
  private static final String BY_ORG_CHART = "pkg_query_goalquest.prc_getGQSelectionByOrgChart";
  private static final String PERCENTAGE_CHART = "pkg_query_goalquest.prc_getGQSelectionPctChart";

  public CallPrcGQSelectionReports( DataSource ds, String storedProcName )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestSelectionReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new GoalQuestSelectionReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestSelectionDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case VALID_LEVEL_NUMS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestSelectionValidLevelNumbersReportMapper() ) );
        break;
      case TOTALS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestSelectionTotalsChartReportMapper() ) );
        break;
      case BY_ORG_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestSelectionByOrgChartReportMapper() ) );
        break;
      case PERCENTAGE_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestSelectionPercentageChartReportMapper() ) );
        break;
      default:
        break;
    }
    compile();
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

  protected class GoalQuestSelectionReportMapper implements ResultSetExtractor<List<GoalQuestSelectionReportValue>>
  {
    @Override
    public List<GoalQuestSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestSelectionReportValue> results = new ArrayList<GoalQuestSelectionReportValue>();
      while ( rs.next() )
      {
        GoalQuestSelectionReportValue result = new GoalQuestSelectionReportValue();
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setOrgId( rs.getLong( "NODE_ID" ) );
        result.setPaxCnt( rs.getLong( "TOTAL_PARTICIPANTS" ) );
        result.setNoGoalSelected( rs.getLong( "NO_GOAL_SELECTED" ) );
        result.setLevel1Selected( rs.getLong( "LEVEL_1_SELECTED" ) );
        result.setLevel1SelectedPercent( rs.getBigDecimal( "LEVEL_1_SELECTED_PERCENT" ) );
        result.setLevel2Selected( rs.getLong( "LEVEL_2_SELECTED" ) );
        result.setLevel2SelectedPercent( rs.getBigDecimal( "LEVEL_2_SELECTED_PERCENT" ) );
        result.setLevel3Selected( rs.getLong( "LEVEL_3_SELECTED" ) );
        result.setLevel3SelectedPercent( rs.getBigDecimal( "LEVEL_3_SELECTED_PERCENT" ) );
        result.setLevel4Selected( rs.getLong( "LEVEL_4_SELECTED" ) );
        result.setLevel4SelectedPercent( rs.getBigDecimal( "LEVEL_4_SELECTED_PERCENT" ) );
        result.setLevel5Selected( rs.getLong( "LEVEL_5_SELECTED" ) );
        result.setLevel5SelectedPercent( rs.getBigDecimal( "LEVEL_5_SELECTED_PERCENT" ) );
        result.setLevel6Selected( rs.getLong( "LEVEL_6_SELECTED" ) );
        result.setLevel6SelectedPercent( rs.getBigDecimal( "LEVEL_6_SELECTED_PERCENT" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestSelectionReportTotalsMapper implements ResultSetExtractor<GoalQuestSelectionReportValue>
  {
    @Override
    public GoalQuestSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestSelectionReportValue result = new GoalQuestSelectionReportValue();
      if ( rs.next() )
      {
        result.setPaxCnt( rs.getLong( "TOTAL_PARTICIPANTS" ) );
        result.setNoGoalSelected( rs.getLong( "NO_GOAL_SELECTED" ) );
        result.setLevel1Selected( rs.getLong( "LEVEL_1_SELECTED" ) );
        result.setLevel1SelectedPercent( rs.getBigDecimal( "LEVEL_1_SELE_PERC" ) );
        result.setLevel2Selected( rs.getLong( "LEVEL_2_SELECTED" ) );
        result.setLevel2SelectedPercent( rs.getBigDecimal( "LEVEL_2_SELE_PERC" ) );
        result.setLevel3Selected( rs.getLong( "LEVEL_3_SELECTED" ) );
        result.setLevel3SelectedPercent( rs.getBigDecimal( "LEVEL_3_SELE_PERC" ) );
        result.setLevel4Selected( rs.getLong( "LEVEL_4_SELECTED" ) );
        result.setLevel4SelectedPercent( rs.getBigDecimal( "LEVEL_4_SELE_PERC" ) );
        result.setLevel5Selected( rs.getLong( "LEVEL_5_SELECTED" ) );
        result.setLevel5SelectedPercent( rs.getBigDecimal( "LEVEL_5_SELE_PERC" ) );
        result.setLevel6Selected( rs.getLong( "LEVEL_6_SELECTED" ) );
        result.setLevel6SelectedPercent( rs.getBigDecimal( "LEVEL_6_SELE_PERC" ) );
      }
      return result;
    }
  }

  protected class GoalQuestSelectionValidLevelNumbersReportMapper implements ResultSetExtractor<List<Long>>
  {
    @Override
    public List<Long> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<Long> results = new ArrayList<Long>();
      while ( rs.next() )
      {
        results.add( rs.getLong( "LEVEL_NUMBER" ) );
      }
      return results;
    }
  }

  protected class GoalQuestSelectionDetailReportMapper implements ResultSetExtractor<List<GoalQuestSelectionReportValue>>
  {
    @Override
    public List<GoalQuestSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestSelectionReportValue> results = new ArrayList<GoalQuestSelectionReportValue>();
      while ( rs.next() )
      {
        GoalQuestSelectionReportValue result = new GoalQuestSelectionReportValue();
        result.setPaxName( rs.getString( "PAX_NAME" ) );
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setLevelName( rs.getString( "LEVEL_NUMBER" ) );
        result.setPromoName( rs.getString( "PROMO_NAME" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestSelectionTotalsChartReportMapper implements ResultSetExtractor<GoalQuestSelectionReportValue>
  {
    @Override
    public GoalQuestSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestSelectionReportValue result = new GoalQuestSelectionReportValue();
      if ( rs.next() )
      {
        result.setNoGoalSelected( rs.getLong( "NO_GOAL_SELECTED" ) );
        result.setLevel1Selected( rs.getLong( "LEVEL_1_SELECTED" ) );
        result.setLevel2Selected( rs.getLong( "LEVEL_2_SELECTED" ) );
        result.setLevel3Selected( rs.getLong( "LEVEL_3_SELECTED" ) );
        result.setLevel4Selected( rs.getLong( "LEVEL_4_SELECTED" ) );
        result.setLevel5Selected( rs.getLong( "LEVEL_5_SELECTED" ) );
        result.setLevel6Selected( rs.getLong( "LEVEL_6_SELECTED" ) );
      }
      return result;
    }
  }

  protected class GoalQuestSelectionByOrgChartReportMapper implements ResultSetExtractor<List<GoalQuestSelectionReportValue>>
  {
    @Override
    public List<GoalQuestSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestSelectionReportValue> results = new ArrayList<GoalQuestSelectionReportValue>();
      while ( rs.next() )
      {
        GoalQuestSelectionReportValue result = new GoalQuestSelectionReportValue();
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setOrgId( rs.getLong( "NODE_ID" ) );
        result.setNoGoalSelected( rs.getLong( "NO_GOAL_SELECTED" ) );
        result.setLevel1Selected( rs.getLong( "LEVEL_1_SELECTED" ) );
        result.setLevel2Selected( rs.getLong( "LEVEL_2_SELECTED" ) );
        result.setLevel3Selected( rs.getLong( "LEVEL_3_SELECTED" ) );
        result.setLevel4Selected( rs.getLong( "LEVEL_4_SELECTED" ) );
        result.setLevel5Selected( rs.getLong( "LEVEL_5_SELECTED" ) );
        result.setLevel6Selected( rs.getLong( "LEVEL_6_SELECTED" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestSelectionPercentageChartReportMapper implements ResultSetExtractor<GoalQuestSelectionReportValue>
  {
    @Override
    public GoalQuestSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestSelectionReportValue result = new GoalQuestSelectionReportValue();
      if ( rs.next() )
      {
        result.setNoGoalSelectedPercent( rs.getBigDecimal( "NO_GOAL_SELE_PERC" ) );
        result.setLevel1SelectedPercent( rs.getBigDecimal( "LEVEL_1_SELE_PERC" ) );
        result.setLevel2SelectedPercent( rs.getBigDecimal( "LEVEL_2_SELE_PERC" ) );
        result.setLevel3SelectedPercent( rs.getBigDecimal( "LEVEL_3_SELE_PERC" ) );
        result.setLevel4SelectedPercent( rs.getBigDecimal( "LEVEL_4_SELE_PERC" ) );
        result.setLevel5SelectedPercent( rs.getBigDecimal( "LEVEL_5_SELE_PERC" ) );
        result.setLevel6SelectedPercent( rs.getBigDecimal( "LEVEL_6_SELE_PERC" ) );
      }
      return result;
    }
  }
}

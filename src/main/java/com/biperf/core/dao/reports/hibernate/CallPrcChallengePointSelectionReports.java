
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

import com.biperf.core.value.challengepoint.ChallengePointSelectionReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcChallengePointSelectionReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_challengepoint.prc_getCPSelectionTabRes";
  private static final String DETAIL_RESULTS = "pkg_query_challengepoint.prc_getCPSelectionDetailRes";
  private static final String RESULTS = "pkg_query_challengepoint.prc_getCPSelectionRes";
  private static final String VALID_LEVEL_NUMS = "pkg_query_challengepoint.prc_getCPSelectionValidLvlNums";
  private static final String TOTALS_CHART = "pkg_query_challengepoint.prc_getCPSelectionTotalsChart";
  private static final String PCT_CHART = "pkg_query_challengepoint.prc_getCPSelectionPctChart";
  private static final String BY_ORG_CHART = "pkg_query_challengepoint.prc_getCPSelectionByOrgChart";

  public CallPrcChallengePointSelectionReports( DataSource ds, String storedProcName )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointSelectionReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointSelectionReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointSelectionDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        break;
      case VALID_LEVEL_NUMS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointSelectionValidLevelNumbersReportMapper() ) );
        break;
      case TOTALS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointSelectionTotalsChartReportMapper() ) );
        break;
      case PCT_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointSelectionPercentageChartReportMapper() ) );
        break;
      case BY_ORG_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointSelectionByOrgChartReportMapper() ) );
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

  protected class ChallengePointSelectionReportMapper implements ResultSetExtractor<List<ChallengePointSelectionReportValue>>
  {
    @Override
    public List<ChallengePointSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointSelectionReportValue> results = new ArrayList<ChallengePointSelectionReportValue>();
      while ( rs.next() )
      {
        ChallengePointSelectionReportValue result = new ChallengePointSelectionReportValue();
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
        result.setIsLeaf( rs.getBoolean( "IS_LEAF" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointSelectionReportTotalsMapper implements ResultSetExtractor<ChallengePointSelectionReportValue>
  {
    @Override
    public ChallengePointSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointSelectionReportValue result = new ChallengePointSelectionReportValue();
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

  protected class ChallengePointSelectionDetailReportMapper implements ResultSetExtractor<List<ChallengePointSelectionReportValue>>
  {
    @Override
    public List<ChallengePointSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointSelectionReportValue> results = new ArrayList<ChallengePointSelectionReportValue>();
      while ( rs.next() )
      {
        ChallengePointSelectionReportValue result = new ChallengePointSelectionReportValue();
        result.setPaxName( rs.getString( "PAX_NAME" ) );
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setLevelName( rs.getString( "LEVEL_NUMBER" ) );
        result.setPromoName( rs.getString( "PROMO_NAME" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointSelectionValidLevelNumbersReportMapper implements ResultSetExtractor<List<Long>>
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

  protected class ChallengePointSelectionTotalsChartReportMapper implements ResultSetExtractor<ChallengePointSelectionReportValue>
  {
    @Override
    public ChallengePointSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointSelectionReportValue result = new ChallengePointSelectionReportValue();
      if ( rs.next() )
      {
        result.setNoGoalSelected( rs.getLong( "NO_GOAL_SELECTED_CNT" ) );
        result.setLevel1Selected( rs.getLong( "LEVEL_1_SELECTED_CNT" ) );
        result.setLevel2Selected( rs.getLong( "LEVEL_2_SELECTED_CNT" ) );
        result.setLevel3Selected( rs.getLong( "LEVEL_3_SELECTED_CNT" ) );
        result.setLevel4Selected( rs.getLong( "LEVEL_4_SELECTED_CNT" ) );
        result.setLevel5Selected( rs.getLong( "LEVEL_5_SELECTED_CNT" ) );
        result.setLevel6Selected( rs.getLong( "LEVEL_6_SELECTED_CNT" ) );
      }
      return result;
    }
  }

  protected class ChallengePointSelectionPercentageChartReportMapper implements ResultSetExtractor<ChallengePointSelectionReportValue>
  {
    @Override
    public ChallengePointSelectionReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointSelectionReportValue result = new ChallengePointSelectionReportValue();
      if ( rs.next() )
      {
        result.setNoGoalSelectedPercent( rs.getBigDecimal( "NO_GOAL_SELECTED_PERCENT" ) );
        result.setLevel1SelectedPercent( rs.getBigDecimal( "LEVEL_1_SELECTED_PERCENT" ) );
        result.setLevel2SelectedPercent( rs.getBigDecimal( "LEVEL_2_SELECTED_PERCENT" ) );
        result.setLevel3SelectedPercent( rs.getBigDecimal( "LEVEL_3_SELECTED_PERCENT" ) );
        result.setLevel4SelectedPercent( rs.getBigDecimal( "LEVEL_4_SELECTED_PERCENT" ) );
        result.setLevel5SelectedPercent( rs.getBigDecimal( "LEVEL_5_SELECTED_PERCENT" ) );
        result.setLevel6SelectedPercent( rs.getBigDecimal( "LEVEL_6_SELECTED_PERCENT" ) );
      }
      return result;
    }
  }

  protected class ChallengePointSelectionByOrgChartReportMapper implements ResultSetExtractor<List<ChallengePointSelectionReportValue>>
  {
    @Override
    public List<ChallengePointSelectionReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointSelectionReportValue> results = new ArrayList<ChallengePointSelectionReportValue>();
      while ( rs.next() )
      {
        ChallengePointSelectionReportValue result = new ChallengePointSelectionReportValue();
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setOrgId( rs.getLong( "NODE_ID" ) );
        result.setPaxCnt( rs.getLong( "TOTAL_PARTICIPANTS" ) );
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
}

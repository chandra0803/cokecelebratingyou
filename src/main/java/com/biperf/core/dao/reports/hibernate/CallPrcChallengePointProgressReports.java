
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

import com.biperf.core.value.challengepoint.ChallengePointProgressReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcChallengePointProgressReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_challengepoint.prc_getCPProgressTabRes";
  private static final String DETAIL_RESULTS = "pkg_query_challengepoint.prc_getCPProgressDetailRes";
  private static final String PROGRESS_TO_GOAL_CHART = "pkg_query_challengepoint.prc_getCPToGoalChart";
  private static final String RESULTS_CHART = "pkg_query_challengepoint.prc_getCPProgressResChart";

  public CallPrcChallengePointProgressReports( DataSource ds, String storedProcName )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointProgressReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointProgressReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointProgressDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new ChallengePointProgressDetailReportTotalsMapper() ) );
        break;
      case PROGRESS_TO_GOAL_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointProgressToGoalChartMapper() ) );
        break;
      case RESULTS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new ChallengePointProgressResultsChartMapper() ) );
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

  protected class ChallengePointProgressReportMapper implements ResultSetExtractor<List<ChallengePointProgressReportValue>>
  {
    @Override
    public List<ChallengePointProgressReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointProgressReportValue> results = new ArrayList<ChallengePointProgressReportValue>();
      while ( rs.next() )
      {
        ChallengePointProgressReportValue result = new ChallengePointProgressReportValue();
        result.setTotalPax( rs.getLong( "total_selected" ) );
        result.setTotalPaxNoGoal( rs.getLong( "nbr_threshold_reached" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "all_level_selected" ) );
        result.setLevel0To25Count( rs.getLong( "sum_nbr_pax_25_percent" ) );
        result.setLevel26To50Count( rs.getLong( "sum_nbr_pax_50_percent" ) );
        result.setLevel51To75Count( rs.getLong( "sum_nbr_pax_75_percent" ) );
        result.setLevel76To99Count( rs.getLong( "sum_nbr_pax_76_99_percent" ) );
        result.setLevel100Count( rs.getLong( "sum_nbr_pax_100_percent" ) );
        result.setOrgName( rs.getString( "NODE_NAME" ) );
        result.setIsLeaf( rs.getBoolean( "IS_LEAF" ) );
        result.setParentNodeId( rs.getLong( "PARENT_NODE_ID" ) );
        result.setOrgId( rs.getLong( "DETAIL_NODE_ID" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointProgressReportTotalsMapper implements ResultSetExtractor<ChallengePointProgressReportValue>
  {
    @Override
    public ChallengePointProgressReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointProgressReportValue result = new ChallengePointProgressReportValue();
      if ( rs.next() )
      {
        result.setTotalPax( rs.getLong( "total_selected" ) );
        result.setTotalPaxNoGoal( rs.getLong( "nbr_threshold_reached" ) );
        // result.setTotalPaxGoalSelected( rs.getLong( "all_level_selected" ));
        result.setLevel0To25Count( rs.getLong( "sum_nbr_pax_25_percent" ) );
        result.setLevel26To50Count( rs.getLong( "sum_nbr_pax_50_percent" ) );
        result.setLevel51To75Count( rs.getLong( "sum_nbr_pax_75_percent" ) );
        result.setLevel76To99Count( rs.getLong( "sum_nbr_pax_76_99_percent" ) );
        result.setLevel100Count( rs.getLong( "sum_nbr_pax_100_percent" ) );
      }
      return result;
    }
  }

  protected class ChallengePointProgressDetailReportMapper implements ResultSetExtractor<List<ChallengePointProgressReportValue>>
  {
    @Override
    public List<ChallengePointProgressReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointProgressReportValue> results = new ArrayList<ChallengePointProgressReportValue>();
      while ( rs.next() )
      {
        ChallengePointProgressReportValue result = new ChallengePointProgressReportValue();
        result.setPaxName( rs.getString( "pax_name" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setPromoName( rs.getString( "promotion_name" ) );
        result.setGoalName( rs.getString( "level_selected" ) );
        result.setBaseQuantity( rs.getBigDecimal( "base_quantity" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "amount_to_achieve" ) );
        result.setCurrentValue( rs.getBigDecimal( "current_value" ) );
        result.setPercentAchieved( rs.getBigDecimal( "percent_of_challengepoint" ) );
        result.setAchieved( rs.getBoolean( "achieved" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class ChallengePointProgressDetailReportTotalsMapper implements ResultSetExtractor<ChallengePointProgressReportValue>
  {
    @Override
    public ChallengePointProgressReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointProgressReportValue result = new ChallengePointProgressReportValue();
      if ( rs.next() )
      {
        result.setBaseQuantity( rs.getBigDecimal( "BASE_QUANTITY" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "AMOUNT_ACHIEVED" ) );
        result.setCurrentValue( rs.getBigDecimal( "CURRENT_VALUE" ) );
        result.setPercentAchieved( rs.getBigDecimal( "percent_of_challengepoint" ) );
        result.setAchieved( rs.getBoolean( "ACHIEVED" ) );
      }
      return result;
    }
  }

  protected class ChallengePointProgressToGoalChartMapper implements ResultSetExtractor<ChallengePointProgressReportValue>
  {
    @Override
    public ChallengePointProgressReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      ChallengePointProgressReportValue result = new ChallengePointProgressReportValue();
      if ( rs.next() )
      {
        result.setLevel0To25Count( rs.getLong( "sum_nbr_pax_25_percent" ) );
        result.setLevel26To50Count( rs.getLong( "sum_nbr_pax_50_percent" ) );
        result.setLevel51To75Count( rs.getLong( "sum_nbr_pax_75_percent" ) );
        result.setLevel76To99Count( rs.getLong( "sum_nbr_pax_76_99_percent" ) );
        result.setLevel100Count( rs.getLong( "sum_nbr_pax_100_percent" ) );
      }
      return result;
    }
  }

  protected class ChallengePointProgressResultsChartMapper implements ResultSetExtractor<List<ChallengePointProgressReportValue>>
  {
    @Override
    public List<ChallengePointProgressReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<ChallengePointProgressReportValue> results = new ArrayList<ChallengePointProgressReportValue>();
      while ( rs.next() )
      {
        ChallengePointProgressReportValue result = new ChallengePointProgressReportValue();
        result.setPaxName( rs.getString( "pax_name" ) );
        result.setPromoName( rs.getString( "promotion_name" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setBaseQuantity( rs.getBigDecimal( "base_quantity" ) );
        result.setAmountToAchieve( rs.getBigDecimal( "amount_to_achieve" ) );
        result.setCurrentValue( rs.getBigDecimal( "current_value" ) );
      }
      return results;
    }
  }
}

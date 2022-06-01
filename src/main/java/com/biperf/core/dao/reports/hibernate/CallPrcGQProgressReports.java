
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

import com.biperf.core.value.goalquest.GoalQuestProgressReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcGQProgressReports extends StoredProcedure
{
  private static final String TABULAR_RESULTS = "pkg_query_goalquest.prc_getGQProgressTabRes";
  private static final String DETAIL_RESULTS = "pkg_query_goalquest.prc_getGQProgressDetailRes";
  private static final String TO_GOAL_CHART = "pkg_query_goalquest.prc_getProgressToGoalChart";
  private static final String RESULTS_CHART = "pkg_query_goalquest.prc_getGQProgressResChart";

  public CallPrcGQProgressReports( DataSource ds, String storedProcName )
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
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestProgressReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new GoalQuestProgressReportTotalsMapper() ) );
        break;
      case DETAIL_RESULTS:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestProgressDetailReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new GoalQuestProgressDetailReportTotalsMapper() ) );
        break;
      case TO_GOAL_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestProgressToGoalChartMapper() ) );
        break;
      case RESULTS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new GoalQuestProgressResultsChartMapper() ) );
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

  protected class GoalQuestProgressReportMapper implements ResultSetExtractor<List<GoalQuestProgressReportValue>>
  {
    @Override
    public List<GoalQuestProgressReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestProgressReportValue> results = new ArrayList<GoalQuestProgressReportValue>();
      while ( rs.next() )
      {
        GoalQuestProgressReportValue result = new GoalQuestProgressReportValue();
        result.setTotalPax( rs.getLong( "total_participants" ) );
        result.setTotalPaxNoGoal( rs.getLong( "no_goal_selected" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "nbr_selected" ) );
        result.setLevel0To25Count( rs.getLong( "sum_nbr_pax_25_percent" ) );
        result.setLevel26To50Count( rs.getLong( "sum_nbr_pax_50_percent" ) );
        result.setLevel51To75Count( rs.getLong( "sum_nbr_pax_75_percent" ) );
        result.setLevel76To99Count( rs.getLong( "sum_nbr_pax_76_99_percent" ) );
        result.setLevel100Count( rs.getLong( "sum_nbr_pax_100_percent" ) );
        result.setBaseQuantity( rs.getLong( "base" ) );
        result.setGoal( rs.getLong( "goal" ) );
        result.setAmountToAchieve( rs.getLong( "actual_result" ) );
        result.setPercentAchieved( rs.getBigDecimal( "percent_of_goal" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setParentNodeId( rs.getLong( "node_id" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestProgressReportTotalsMapper implements ResultSetExtractor<GoalQuestProgressReportValue>
  {
    @Override
    public GoalQuestProgressReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestProgressReportValue result = new GoalQuestProgressReportValue();
      if ( rs.next() )
      {
        result.setTotalPax( rs.getLong( "total_participants" ) );
        result.setTotalPaxNoGoal( rs.getLong( "no_goal_selected" ) );
        result.setTotalPaxGoalSelected( rs.getLong( "nbr_selected" ) );
        result.setLevel0To25Count( rs.getLong( "sum_nbr_pax_25_percent" ) );
        result.setLevel26To50Count( rs.getLong( "sum_nbr_pax_50_percent" ) );
        result.setLevel51To75Count( rs.getLong( "sum_nbr_pax_75_percent" ) );
        result.setLevel76To99Count( rs.getLong( "sum_nbr_pax_76_99_percent" ) );
        result.setLevel100Count( rs.getLong( "sum_nbr_pax_100_percent" ) );
        result.setBaseQuantity( rs.getLong( "base" ) );
        result.setGoal( rs.getLong( "goal" ) );
        result.setAmountToAchieve( rs.getLong( "actual_result" ) );
        result.setPercentAchieved( rs.getBigDecimal( "percent_of_goal" ) );
      }
      return result;
    }
  }

  protected class GoalQuestProgressDetailReportMapper implements ResultSetExtractor<List<GoalQuestProgressReportValue>>
  {
    @Override
    public List<GoalQuestProgressReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestProgressReportValue> results = new ArrayList<GoalQuestProgressReportValue>();
      while ( rs.next() )
      {
        GoalQuestProgressReportValue result = new GoalQuestProgressReportValue();
        result.setPaxName( rs.getString( "pax_name" ) );
        result.setPromoName( rs.getString( "promo_name" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setGoalName( rs.getString( "level_name" ) );
        result.setBaseQuantity( rs.getLong( "base_quantity" ) );
        result.setGoalAchieve( rs.getBigDecimal( "amount_to_achieve" ) );
        result.setCurrentValue( rs.getBigDecimal( "current_value" ) );
        result.setPercentAchieved( rs.getBigDecimal( "percent_of_goal" ) );
        result.setAchieved( rs.getBoolean( "achieved" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class GoalQuestProgressDetailReportTotalsMapper implements ResultSetExtractor<GoalQuestProgressReportValue>
  {
    @Override
    public GoalQuestProgressReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestProgressReportValue result = new GoalQuestProgressReportValue();
      if ( rs.next() )
      {

        result.setBaseQuantity( rs.getLong( "BASE_QUANTITY" ) );
        result.setGoalAchieve( rs.getBigDecimal( "AMOUNT_TO_ACHIEVE" ) );
        result.setCurrentValue( rs.getBigDecimal( "CURRENT_VALUE" ) );
        result.setPercentAchieved( rs.getBigDecimal( "PERCENT_ACHIEVED" ) );
        result.setAchieved( rs.getBoolean( "ACHIEVED" ) );
      }
      return result;
    }
  }

  protected class GoalQuestProgressToGoalChartMapper implements ResultSetExtractor<GoalQuestProgressReportValue>
  {
    @Override
    public GoalQuestProgressReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      GoalQuestProgressReportValue result = new GoalQuestProgressReportValue();
      if ( rs.next() )
      {

        result.setLevel0To25Count( rs.getLong( "SUM_25" ) );
        result.setLevel26To50Count( rs.getLong( "SUM_50" ) );
        result.setLevel51To75Count( rs.getLong( "SUM_75" ) );
        result.setLevel76To99Count( rs.getLong( "SUM_76_99" ) );
        result.setLevel100Count( rs.getLong( "SUM_100" ) );
      }
      return result;
    }
  }

  protected class GoalQuestProgressResultsChartMapper implements ResultSetExtractor<List<GoalQuestProgressReportValue>>
  {
    @Override
    public List<GoalQuestProgressReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<GoalQuestProgressReportValue> results = new ArrayList<GoalQuestProgressReportValue>();
      while ( rs.next() )
      {
        GoalQuestProgressReportValue result = new GoalQuestProgressReportValue();
        result.setPaxName( rs.getString( "pax_name" ) );
        result.setPromoName( rs.getString( "promo_name" ) );
        result.setOrgName( rs.getString( "node_name" ) );
        result.setBaseQuantity( rs.getLong( "base_quantity" ) );
        result.setAmountToAchieve( rs.getLong( "amount_to_achieve" ) );
        result.setCurrentValue( rs.getBigDecimal( "current_value" ) );
        results.add( result );
      }
      return results;
    }

  }
}

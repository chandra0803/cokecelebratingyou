
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

import com.biperf.core.value.budget.PointsBudgetBalanceReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcPointsBudgetBalance extends StoredProcedure
{
  private static final String BALANCE_TAB_RES = "pkg_query_point_budget_balance.prc_getBalanceTabRes";
  private static final String BALANCE_BY_PROMO_TAB_RES = "pkg_query_point_budget_balance.prc_getBalanceByPromoTabRes";
  private static final String USE_IN_POINTS_CHART = "pkg_query_point_budget_balance.prc_getUseInPointsChart";
  private static final String USE_BY_PERCENTAGE_CHART = "pkg_query_point_budget_balance.prc_getUseByPercentageChart";

  public CallPrcPointsBudgetBalance( DataSource ds, String storedProcName )
  {
    super( ds, storedProcName );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_budgetStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_budgetDistribution", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryRatio", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );

    switch ( storedProcName )
    {
      case USE_IN_POINTS_CHART:
        declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetUtilizationInPointsReportMapper() ) );
        break;
      case USE_BY_PERCENTAGE_CHART:
        declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetUtilizationByPercentageReportMapper() ) );
        break;
      case BALANCE_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new BudgetBalanceReportTotalsMapper() ) );
        break;
      case BALANCE_BY_PROMO_TAB_RES:
        declareParameter( new SqlParameter( "p_in_bud_seg_id", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceByPromotionReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new BudgetBalanceByPromotionReportTotalsMapper() ) );
        break;
      default:
        break;
    }
  }

  public Map<String, Object> executeProcedure( Map<String, Object> reportParameters ) throws DataAccessException
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_languageCode", reportParameters.get( "languageCode" ) );
    inParams.put( "p_in_rowNumEnd", reportParameters.get( "rowNumEnd" ) );
    inParams.put( "p_in_rowNumStart", reportParameters.get( "rowNumStart" ) );
    inParams.put( "p_in_parentNodeId", reportParameters.get( "parentNodeId" ) );
    inParams.put( "p_in_promotionId", reportParameters.get( "promotionId" ) );
    inParams.put( "p_in_budgetStatus", reportParameters.get( "budgetStatus" ) );
    inParams.put( "p_in_budgetDistribution", reportParameters.get( "budgetDistribution" ) );
    inParams.put( "p_in_countryRatio", reportParameters.get( "countryRatio" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortedOn" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );
    inParams.put( "p_in_bud_seg_id", reportParameters.get( "budgetSegmentId" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  protected class BudgetUtilizationInPointsReportMapper implements ResultSetExtractor<List<PointsBudgetBalanceReportValue>>
  {
    @Override
    public List<PointsBudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PointsBudgetBalanceReportValue> results = new ArrayList<PointsBudgetBalanceReportValue>();
      while ( rs.next() )
      {
        PointsBudgetBalanceReportValue result = new PointsBudgetBalanceReportValue();
        result.setBudgetMasterName( rs.getString( "budget_master_name" ) );
        result.setAwarded( rs.getLong( "awarded" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetUtilizationByPercentageReportMapper implements ResultSetExtractor<List<PointsBudgetBalanceReportValue>>
  {
    @Override
    public List<PointsBudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PointsBudgetBalanceReportValue> results = new ArrayList<PointsBudgetBalanceReportValue>();
      while ( rs.next() )
      {
        PointsBudgetBalanceReportValue result = new PointsBudgetBalanceReportValue();
        result.setBudgetMasterName( rs.getString( "budget_master_name" ) );
        result.setPointsPercentUsed( rs.getBigDecimal( "percent_used" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetBalanceReportMapper implements ResultSetExtractor<List<PointsBudgetBalanceReportValue>>
  {
    @Override
    public List<PointsBudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PointsBudgetBalanceReportValue> results = new ArrayList<PointsBudgetBalanceReportValue>();
      while ( rs.next() )
      {
        PointsBudgetBalanceReportValue result = new PointsBudgetBalanceReportValue();
        result.setPromotionId( rs.getString( "promo_id" ) );
        result.setPromotionName( rs.getString( "promo_name" ) );
        result.setPromotionDateRange( rs.getString( "promo_date" ) );// Client customizations for WIP #27192 
        result.setBudgetMasterName( rs.getString( "budget_master_name" ) );
        result.setBudgetPeriod( rs.getString( "budget_period" ) );
        result.setOriginalBudget( rs.getLong( "original_budget" ) );
        result.setBudgetAdjustments( rs.getLong( "budget_adjustments" ) );
        result.setAwarded( rs.getLong( "awarded" ) );
        result.setAvailableBalance( rs.getLong( "available_balance" ) );
        result.setBudgetSegmentId( rs.getLong( "budget_segment_id" ) );
        result.setTotalRecords( rs.getLong( "total_records" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetBalanceReportTotalsMapper implements ResultSetExtractor<PointsBudgetBalanceReportValue>
  {
    @Override
    public PointsBudgetBalanceReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      PointsBudgetBalanceReportValue result = new PointsBudgetBalanceReportValue();
      if ( rs.next() )
      {
        result.setOriginalBudget( rs.getLong( "original_budget" ) );
        result.setBudgetAdjustments( rs.getLong( "budget_adjustments" ) );
        result.setAwarded( rs.getLong( "awarded" ) );
        result.setAvailableBalance( rs.getLong( "available_balance" ) );
      }
      return result;
    }
  }

  protected class BudgetBalanceByPromotionReportMapper implements ResultSetExtractor<List<PointsBudgetBalanceReportValue>>
  {
    @Override
    public List<PointsBudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PointsBudgetBalanceReportValue> results = new ArrayList<PointsBudgetBalanceReportValue>();
      while ( rs.next() )
      {
        PointsBudgetBalanceReportValue result = new PointsBudgetBalanceReportValue();
        result.setBudgetOwnerName( rs.getString( "budget_owner_name" ) );
        result.setBudgetMasterName( rs.getString( "budget_master_name" ) );
        result.setBudgetPeriod( rs.getString( "budget_period" ) );
        result.setOriginalBudget( rs.getLong( "original_budget" ) );
        result.setBudgetAdjustments( rs.getLong( "budget_adjustments" ) );
        result.setAwarded( rs.getLong( "awarded" ) );
        result.setAvailableBalance( rs.getLong( "available_balance" ) );
        result.setTotalRecords( rs.getLong( "total_records" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetBalanceByPromotionReportTotalsMapper implements ResultSetExtractor<PointsBudgetBalanceReportValue>
  {
    @Override
    public PointsBudgetBalanceReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      PointsBudgetBalanceReportValue result = new PointsBudgetBalanceReportValue();
      while ( rs.next() )
      {
        result.setOriginalBudget( rs.getLong( "original_budget" ) );
        result.setBudgetAdjustments( rs.getLong( "budget_adjustments" ) );
        result.setAwarded( rs.getLong( "awarded" ) );
        result.setAvailableBalance( rs.getLong( "available_balance" ) );
      }
      return result;
    }
  }

}

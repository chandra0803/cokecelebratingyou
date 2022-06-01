
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

import com.biperf.core.value.budget.BudgetBalanceReportValue;

import oracle.jdbc.OracleTypes;

public class CallPrcBudgetBalance extends StoredProcedure
{
  private static final String DEPOSITED_DETAIL_TAB_RES = "pkg_query_budget_balance.prc_getDepositedDetailTabRes";
  private static final String ISSUED_DETAIL_TAB_RES = "pkg_query_budget_balance.prc_getIssuedDetailTabRes";
  private static final String ALLOCATED_DETAIL_TAB_RES = "pkg_query_budget_balance.prc_getAllocatedDetailTabRes";
  private static final String BALANCE_TAB_RES = "pkg_query_budget_balance.prc_getBalanceTabRes";
  private static final String BALANCE_BY_PROMO_TAB_RES = "pkg_query_budget_balance.prc_getBalanceByPromoTabRes";
  private static final String USE_IN_POINTS_CHART = "pkg_query_budget_balance.prc_getUseInPointsChart";
  private static final String USE_BY_PERCENTAGE_CHART = "pkg_query_budget_balance.prc_getUseByPercentageChart";

  public CallPrcBudgetBalance( DataSource ds, String storedProcName )
  {
    super( ds, storedProcName );
    declareParameter( new SqlParameter( "p_in_languageCode", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_rowNumEnd", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_rowNumStart", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_parentNodeId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_promotionStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_budgetStatus", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_budgetDistribution", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_countryRatio", Types.NUMERIC ) );
    declareParameter( new SqlParameter( "p_in_toDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_localeDatePattern", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_fromDate", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_budgetId", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedOn", Types.VARCHAR ) );
    declareParameter( new SqlParameter( "p_in_sortedBy", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( "p_out_return_code", Types.NUMERIC ) );

    switch ( storedProcName )
    {
      case DEPOSITED_DETAIL_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceDetailReportMapper() ) );
        break;
      case ISSUED_DETAIL_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceDetailReportMapper() ) );
        break;
      case ALLOCATED_DETAIL_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceDetailReportMapper() ) );
        break;
      case USE_IN_POINTS_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetUtilizationInPointsReportMapper() ) );
        break;
      case USE_BY_PERCENTAGE_CHART:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetUtilizationByPercentageReportMapper() ) );
        break;
      case BALANCE_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
        declareParameter( new SqlOutParameter( "p_out_totals_data", OracleTypes.CURSOR, new BudgetBalanceReportTotalsMapper() ) );
        break;
      case BALANCE_BY_PROMO_TAB_RES:
        declareParameter( new SqlOutParameter( "p_out_data", OracleTypes.CURSOR, new BudgetBalanceByPromotionReportMapper() ) );
        declareParameter( new SqlOutParameter( "p_out_size_data", OracleTypes.INTEGER ) );
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
    inParams.put( "p_in_promotionStatus", reportParameters.get( "promotionStatus" ) );
    inParams.put( "p_in_budgetStatus", reportParameters.get( "budgetStatus" ) );
    inParams.put( "p_in_budgetDistribution", reportParameters.get( "budgetDistribution" ) );
    inParams.put( "p_in_countryRatio", reportParameters.get( "countryRatio" ) );
    inParams.put( "p_in_toDate", reportParameters.get( "toDate" ) );
    inParams.put( "p_in_localeDatePattern", reportParameters.get( "localeDatePattern" ) );
    inParams.put( "p_in_fromDate", reportParameters.get( "fromDate" ) );
    inParams.put( "p_in_budgetId", reportParameters.get( "budgetId" ) );
    inParams.put( "p_in_sortedOn", reportParameters.get( "sortedOn" ) );
    inParams.put( "p_in_sortedBy", reportParameters.get( "sortedBy" ) );

    Map<String, Object> outParams = execute( inParams );
    return outParams;
  }

  protected class BudgetBalanceDetailReportMapper implements ResultSetExtractor<List<BudgetBalanceReportValue>>
  {
    @Override
    public List<BudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BudgetBalanceReportValue> results = new ArrayList<BudgetBalanceReportValue>();
      while ( rs.next() )
      {
        BudgetBalanceReportValue result = new BudgetBalanceReportValue();
        result.setBudgetDate( rs.getDate( "trans_date" ) );
        result.setBudgetAmount( rs.getLong( "trans_amount" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetUtilizationInPointsReportMapper implements ResultSetExtractor<List<BudgetBalanceReportValue>>
  {
    @Override
    public List<BudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BudgetBalanceReportValue> results = new ArrayList<BudgetBalanceReportValue>();
      while ( rs.next() )
      {
        BudgetBalanceReportValue result = new BudgetBalanceReportValue();
        result.setPromotionName( rs.getString( "promo_name" ) );
        result.setCurrentIssued( rs.getLong( "Current_issued" ) );
        result.setPriorPeriodIssued( rs.getLong( "Prior_Period_Issued" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetUtilizationByPercentageReportMapper implements ResultSetExtractor<List<BudgetBalanceReportValue>>
  {
    @Override
    public List<BudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BudgetBalanceReportValue> results = new ArrayList<BudgetBalanceReportValue>();
      while ( rs.next() )
      {
        BudgetBalanceReportValue result = new BudgetBalanceReportValue();
        result.setPromotionName( rs.getString( "promo_name" ) );
        result.setCurrentBudgetUtilization( rs.getBigDecimal( "current_budget_utilization" ) );
        result.setPriorPeriodUtilization( rs.getBigDecimal( "Prior_Period_Use" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetBalanceReportMapper implements ResultSetExtractor<List<BudgetBalanceReportValue>>
  {
    @Override
    public List<BudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BudgetBalanceReportValue> results = new ArrayList<BudgetBalanceReportValue>();
      while ( rs.next() )
      {
        BudgetBalanceReportValue result = new BudgetBalanceReportValue();
        result.setPromotionId( rs.getLong( "promo_id" ) );
        result.setPromotionName( rs.getString( "promo_name" ) );
        result.setPromotionDateRange( rs.getString( "promo_date" ) );
        result.setBudgetName( rs.getString( "budget_name" ) );
        result.setBudgetPeriod( rs.getString( "budget_period" ) );
        result.setCurrentDeposited( rs.getLong( "Current_deposited" ) );
        result.setCurrentNetTransfers( rs.getLong( "Current_net_transfers" ) );
        result.setCurrentTotalBudget( rs.getLong( "current_total_budget" ) );
        result.setCurrentIssued( rs.getLong( "Current_issued" ) );
        result.setCurrentBudgetRemaining( rs.getLong( "current_budget_remaining" ) );
        result.setCurrentBudgetUtilization( rs.getBigDecimal( "current_budget_utilization" ) );
        result.setPriorPeriodDeposited( rs.getLong( "Prior_Period_Deposited" ) );
        result.setPriorPeriodNetTransfers( rs.getLong( "Prior_Period_NetTransfers" ) );
        result.setPriorPeriodTotalBudget( rs.getLong( "Prior_Period_Total_Budget" ) );
        result.setPriorPeriodIssued( rs.getLong( "Prior_Period_Issued" ) );
        result.setPriorPeriodBudgetRemaining( rs.getLong( "Prior_Period_Budget_Remaining" ) );
        result.setPriorPeriodUtilization( rs.getBigDecimal( "Prior_Period_Use" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetBalanceReportTotalsMapper implements ResultSetExtractor<BudgetBalanceReportValue>
  {
    @Override
    public BudgetBalanceReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      BudgetBalanceReportValue result = new BudgetBalanceReportValue();
      if ( rs.next() )
      {
        result.setCurrentDeposited( rs.getLong( "Current_deposited" ) );
        result.setCurrentNetTransfers( rs.getLong( "Current_net_transfers" ) );
        result.setCurrentTotalBudget( rs.getLong( "current_total_budget" ) );
        result.setCurrentIssued( rs.getLong( "Current_issued" ) );
        result.setCurrentBudgetRemaining( rs.getLong( "current_budget_remaining" ) );
        result.setCurrentBudgetUtilization( rs.getBigDecimal( "current_budget_utilization" ) );
        result.setPriorPeriodDeposited( rs.getLong( "Prior_Period_Deposited" ) );
        result.setPriorPeriodNetTransfers( rs.getLong( "Prior_Period_NetTransfers" ) );
        result.setPriorPeriodTotalBudget( rs.getLong( "Prior_Period_Total_Budget" ) );
        result.setPriorPeriodIssued( rs.getLong( "Prior_Period_Issued" ) );
        result.setPriorPeriodBudgetRemaining( rs.getLong( "Prior_Period_Budget_Remaining" ) );
        result.setPriorPeriodUtilization( rs.getBigDecimal( "Prior_Period_Use" ) );
      }
      return result;
    }
  }

  protected class BudgetBalanceByPromotionReportMapper implements ResultSetExtractor<List<BudgetBalanceReportValue>>
  {
    @Override
    public List<BudgetBalanceReportValue> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<BudgetBalanceReportValue> results = new ArrayList<BudgetBalanceReportValue>();
      while ( rs.next() )
      {
        BudgetBalanceReportValue result = new BudgetBalanceReportValue();
        result.setBudgetId( rs.getLong( "budget_id" ) );
        result.setBudgetOwner( rs.getString( "budget_owner" ) );
        result.setBudgetName( rs.getString( "budget_name" ) );
        result.setBudgetDistribution( rs.getString( "budget_distribution" ) );
        result.setBudgetType( rs.getString( "budget_type" ) );
        result.setBudgetPeriod( rs.getString( "budget_period" ) );
        result.setCurrentDeposited( rs.getLong( "Current_deposited" ) );
        result.setCurrentNetTransfers( rs.getLong( "Current_net_transfers" ) );
        result.setCurrentTotalBudget( rs.getLong( "current_total_budget" ) );
        result.setCurrentIssued( rs.getLong( "Current_issued" ) );
        result.setCurrentBudgetRemaining( rs.getLong( "current_budget_remaining" ) );
        result.setCurrentBudgetUtilization( rs.getBigDecimal( "current_budget_utilization" ) );
        result.setPriorPeriodDeposited( rs.getLong( "Prior_Period_Deposited" ) );
        result.setPriorPeriodNetTransfers( rs.getLong( "Prior_Period_NetTransfers" ) );
        result.setPriorPeriodTotalBudget( rs.getLong( "Prior_Period_Total_Budget" ) );
        result.setPriorPeriodIssued( rs.getLong( "Prior_Period_Issued" ) );
        result.setPriorPeriodBudgetRemaining( rs.getLong( "Prior_Period_Budget_Remaining" ) );
        result.setPriorPeriodUtilization( rs.getBigDecimal( "Prior_Period_Use" ) );
        results.add( result );
      }
      return results;
    }
  }

  protected class BudgetBalanceByPromotionReportTotalsMapper implements ResultSetExtractor<BudgetBalanceReportValue>
  {
    @Override
    public BudgetBalanceReportValue extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      BudgetBalanceReportValue result = new BudgetBalanceReportValue();
      while ( rs.next() )
      {
        result.setCurrentDeposited( rs.getLong( "Current_deposited" ) );
        result.setCurrentNetTransfers( rs.getLong( "Current_net_transfers" ) );
        result.setCurrentTotalBudget( rs.getLong( "current_total_budget" ) );
        result.setCurrentIssued( rs.getLong( "Current_issued" ) );
        result.setCurrentBudgetRemaining( rs.getLong( "current_budget_remaining" ) );
        result.setCurrentBudgetUtilization( rs.getBigDecimal( "current_budget_utilization" ) );
        result.setPriorPeriodDeposited( rs.getLong( "Prior_Period_Deposited" ) );
        result.setPriorPeriodNetTransfers( rs.getLong( "Prior_Period_NetTransfers" ) );
        result.setPriorPeriodTotalBudget( rs.getLong( "Prior_Period_Total_Budget" ) );
        result.setPriorPeriodIssued( rs.getLong( "Prior_Period_Issued" ) );
        result.setPriorPeriodBudgetRemaining( rs.getLong( "Prior_Period_Budget_Remaining" ) );
        result.setPriorPeriodUtilization( rs.getBigDecimal( "Prior_Period_Use" ) );
      }
      return result;
    }
  }

}

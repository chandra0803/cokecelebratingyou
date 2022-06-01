
package com.biperf.core.ui.reports.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParameterInfo;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.budget.PointsBudgetBalanceReportValue;
import com.biperf.util.StringUtils;

public class PointsBudgetBalanceReportAction extends BudgetReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.BUDGET_BALANCE;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParameters.put( "countryRatio", countryMediaValue() );

    reportParametersForm.setDrilldownPromoId( null );

    if ( !isChangeFilterPromotionSelected( reportParametersForm ) )
    {
      reportParameters.put( "promotionId", null );
    }

    List<String> requiredFields = new ArrayList<String>();

    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, true ) )
    {
      return mapping.findForward( "display_budget_balance_summary" );
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      Map<String, Object> outMap = getBudgetReportsService().getPointsBudgetBalanceTabularResults( reportParameters );
      List<PointsBudgetBalanceReportValue> reportData = (List<PointsBudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows.intValue(), reportParameters ) && !"shared".equals( reportParameters.get( "budgetDistribution" ) ) )
      {
        PointsBudgetBalanceReportValue totalsRowData = (PointsBudgetBalanceReportValue)outMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportDetailType", "topLevel" );
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }
    return mapping.findForward( "display_budget_balance_summary" );
  }

  private boolean isChangeFilterPromotionSelected( ReportParametersForm reportParametersForm )
  {
    List<ReportParameterInfo> reportParameterInfoList = reportParametersForm.getReportParameterInfoList();
    boolean isSelected = false;
    for ( ReportParameterInfo reportParameterInfo : reportParameterInfoList )
    {
      if ( "promotionId".equals( reportParameterInfo.getName() ) && !StringUtils.isEmpty( reportParameterInfo.getParameterValue() ) )
      {
        isSelected = true;
      }
    }
    return isSelected;
  }

  public ActionForward displayPointsBudgetByPromotionSummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    reportParameters.put( "budgetSegmentId", reportParametersForm.getBudgetSegmentId() );
    reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    reportParameters.put( "countryRatio", countryMediaValue() );

    Map<String, Object> outMap = getBudgetReportsService().getPointsBudgetBalanceByPromotionTabularResults( reportParameters );
    List<PointsBudgetBalanceReportValue> reportData = (List<PointsBudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows.intValue(), reportParameters ) && !"shared".equals( reportParameters.get( "budgetDistribution" ) ) )
    {
      PointsBudgetBalanceReportValue totalsRowData = (PointsBudgetBalanceReportValue)outMap.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "reportDetailType", "promoLevel" );
    return mapping.findForward( "display_budget_balance_by_promotion_summary" );
  }

  public ActionForward displayUtilizationPercentageChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    if ( reportParametersForm.getDrilldownPromoId() != null )
    {
      reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    }
    reportParameters.put( "countryRatio", countryMediaValue() );

    Map<String, Object> outMap = getBudgetReportsService().getPointsBudgetUtilizationByPercentageChart( reportParameters );
    List<PointsBudgetBalanceReportValue> reportData = (List<PointsBudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_budget_balance_utilization_percentage" );
  }

  public ActionForward displayUtilizationLevelChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    if ( reportParametersForm.getDrilldownPromoId() != null )
    {
      reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );
    }
    reportParameters.put( "countryRatio", countryMediaValue() );

    Map<String, Object> outMap = getBudgetReportsService().getPointsBudgetUtilizationLevelChart( reportParameters );
    List<PointsBudgetBalanceReportValue> reportData = (List<PointsBudgetBalanceReportValue>)outMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_budget_balance_utilization_level" );
  }

  @Override
  protected Map doExtractResultsCall( Map<String, Object> reportParameters )
  {
    return getBudgetReportsService().getPointsBudgetExtractResults( reportParameters );
  }

  @Override
  protected Map doSecondaryExtractResultsCall( Map<String, Object> reportParameters )
  {
    return getBudgetReportsService().getPointsBudgetIssuanceExtractResults( reportParameters );
  }

  @Override
  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "Points" + super.getSecondExtractFileName( reportParametersForm );
  }

  @Override
  protected Map doBudgetSecondLevelExtractResultsCall( Map<String, Object> reportParameters )
  {
    return getBudgetReportsService().getPointsBudgetSecondLevelExtractResults( reportParameters );
  }

  @Override
  protected String getBudgetSecondLevelExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "Points" + super.getBudgetSecondLevelExtractFileName( reportParametersForm );
  }

  @Override
  protected Map doBudgetThirdLevelExtractResultsCall( Map<String, Object> reportParameters )
  {
    return getBudgetReportsService().getPointsBudgetExtractResults( reportParameters );
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.POINTS_BUDGET_REPORT;
  }

  protected ReportBeanMethod getSecondaryReportBeanMethod()
  {
    return ReportBeanMethod.POINTS_BUDGET_ISSUANCE_REPORT;
  }

  protected ReportBeanMethod getSecondaryLevelReportBeanMethod()
  {
    return ReportBeanMethod.POINTS_BUDGET_SECONDARY_REPORT;
  }

  protected CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }
}

/**
 * 
 */

package com.biperf.core.ui.reports.awards;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.ReceivedAwardType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.awardsreport.AwardsDetailByPaxReportValue;
import com.biperf.core.value.awardsreport.AwardsSummaryByPaxReportValue;
import com.biperf.core.value.awardsreport.ReceivedNotReceivedAwardsPaxReportValue;
import com.biperf.core.value.awardsreport.TotalPointsIssuedByPaxReportValue;

/**
 * @author poddutur
 *
 */
public class AwardsByPaxReportAction extends AwardsReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.AWARDS_BY_PAX;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParametersForm.setUserId( null );
    buildOnTheSpotReportParameter( reportParameters );

    request.setAttribute( "awardType", PromotionAwardsType.lookup( (String)reportParameters.get( "awardType" ) ).getName() );

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      if ( !isHaveNotFilterSelected( reportParameters, request ) )
      {
        Map<String, Object> output = getAwardsReportsService().getAwardsSummaryByPaxResults( reportParameters );

        List<AwardsSummaryByPaxReportValue> reportData = (List<AwardsSummaryByPaxReportValue>)output.get( SUMMARY_PAX_OUTPUT_RESULT_SET );

        request.setAttribute( "reportData", reportData );

        Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
        request.setAttribute( "maxRows", maxRows );

        if ( isLastPage( maxRows.intValue(), reportParameters ) )
        {
          AwardsSummaryByPaxReportValue totalsRowData = (AwardsSummaryByPaxReportValue)output.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalsRowData", totalsRowData );
        }
      }
    }

    String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
    request.setAttribute( "currencyCode", currencyCode );
    return mapping.findForward( "display_awards_summary_by_pax_report" );
  }

  private boolean isHaveNotFilterSelected( Map<String, Object> reportParameters, HttpServletRequest request )
  {
    boolean isHaveNotFilterSelected = false;
    if ( ReceivedAwardType.NOT_RECEIVED.equals( reportParameters.get( "receivedAwardType" ) ) )
    {
      isHaveNotFilterSelected = true;
      request.setAttribute( "displayHaveNotFilterExportMessage", isHaveNotFilterSelected );
      request.setAttribute( "hideCharts", Boolean.TRUE );
      request.setAttribute( "maxRows", 1 );
    }
    return isHaveNotFilterSelected;
  }

  public ActionForward displayAwardsDetailByPaxReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    buildOnTheSpotReportParameter( reportParameters );

    request.setAttribute( "awardType", PromotionAwardsType.lookup( (String)reportParameters.get( "awardType" ) ).getName() );

    Map<String, Object> output = getAwardsReportsService().getAwardsDetailByPaxResults( reportParameters );

    List<AwardsDetailByPaxReportValue> reportData = (List<AwardsDetailByPaxReportValue>)output.get( OUTPUT_DATA );

    request.setAttribute( "reportData", reportData );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows.intValue(), reportParameters ) )
    {
      AwardsDetailByPaxReportValue totalsRowData = (AwardsDetailByPaxReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
    String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
    request.setAttribute( "currencyCode", currencyCode );
    return mapping.findForward( "display_awards_detail_by_pax_report" );
  }

  public ActionForward displayTotalPointsIssuedByPaxBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    buildOnTheSpotReportParameter( reportParameters );
    Map<String, Object> output = getAwardsReportsService().getTotalPointsIssuedByPaxBarResults( reportParameters );
    List<TotalPointsIssuedByPaxReportValue> reportData = (List<TotalPointsIssuedByPaxReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_total_points_issued_by_pax_barchart" );
  }

  public ActionForward displayReceivedNotReceivedAwardsPaxPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    buildOnTheSpotReportParameter( reportParameters );
    Map<String, Object> output = getAwardsReportsService().getReceivedNotReceivedAwardsPaxPieResults( reportParameters );
    List<ReceivedNotReceivedAwardsPaxReportValue> reportData = (List<ReceivedNotReceivedAwardsPaxReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_received_notreceived_awards_pax_piechart" );
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return null;
  }

}

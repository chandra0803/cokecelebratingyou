
package com.biperf.core.ui.reports.plateauawards;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.PlateauAwardReportsService;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.plateauawards.PlateauAwardCodeIssuanceReportValue;

public class PlateauAwardCodeIssuanceReportAction extends PlateauAwardReportAction
{

  @Override
  protected String getReportCode()
  {
    // return Report.PLATEAU_AWARD_CODE_ISSUANCE;
    return null;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      List<PlateauAwardCodeIssuanceReportValue> reportData = getPlateauAwardReportsService().getCodeIssuanceSummaryResults( reportParameters );
      request.setAttribute( "reportData", reportData );

      int maxRows = getPlateauAwardReportsService().getCodeIssuanceSummaryResultsSize( reportParameters );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        PlateauAwardCodeIssuanceReportValue totalsRowData = getPlateauAwardReportsService().getCodeIssuanceSummaryResultsTotals( reportParameters );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    return mapping.findForward( "display_code_issuance_summary" );
  }

  public ActionForward displayAwardCodeStatusByPercentageChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    List<PlateauAwardCodeIssuanceReportValue> reportData = getPlateauAwardReportsService().getAwardCodeStatusByPercentageChartResults( reportParameters );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_award_code_status_percentage_chart" );
  }

  public ActionForward displayAwardCodeStatusByCountChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    List<PlateauAwardCodeIssuanceReportValue> reportData = getPlateauAwardReportsService().getAwardCodeStatusByCountChartResults( reportParameters );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_award_code_status_count_chart" );
  }

  protected PlateauAwardReportsService getPlateauAwardReportsService()
  {
    return (PlateauAwardReportsService)getService( PlateauAwardReportsService.BEAN_NAME );
  }
}

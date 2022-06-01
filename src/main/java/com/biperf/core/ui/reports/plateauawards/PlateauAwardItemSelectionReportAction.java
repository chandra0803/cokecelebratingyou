
package com.biperf.core.ui.reports.plateauawards;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.PlateauAwardReportsService;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.plateauawards.PlateauAwardItemSelectionReportValue;
import com.biperf.util.StringUtils;

public class PlateauAwardItemSelectionReportAction extends PlateauAwardReportAction
{

  @Override
  protected String getReportCode()
  {
    return Report.PLATEAU_AWARD_ITEM_SELECTION;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    List<String> requiredFields = Arrays.asList( new String[] {} );
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_item_selection_summary" );
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      Map<String, Object> output = getPlateauAwardReportsService().getItemSelectionSummaryResults( reportParameters );

      List<PlateauAwardItemSelectionReportValue> reportData = (List<PlateauAwardItemSelectionReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        PlateauAwardItemSelectionReportValue totalsRowData = (PlateauAwardItemSelectionReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }
    return mapping.findForward( "display_item_selection_summary" );
  }

  public ActionForward displayPlateauAwardSelectionChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getPlateauAwardReportsService().getPlateauAwardSelectionChartResults( reportParameters );
    List<PlateauAwardItemSelectionReportValue> reportData = (List<PlateauAwardItemSelectionReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_plateau_award_selection_chart" );
  }

  public ActionForward displayTopRedeemedAwardsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getPlateauAwardReportsService().getTopRedeemedAwardsChartResults( reportParameters );
    List<PlateauAwardItemSelectionReportValue> reportData = (List<PlateauAwardItemSelectionReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_top_redeemed_awards_chart" );
  }

  @Override
  protected String getExtractFileName( ReportParametersForm form )
  {
    return "Plateau" + super.getExtractFileName( form );
  }

  protected PlateauAwardReportsService getPlateauAwardReportsService()
  {
    return (PlateauAwardReportsService)getService( PlateauAwardReportsService.BEAN_NAME );
  }
}


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
import com.biperf.core.value.plateauawards.PlateauAwardLevelActivityReportValue;
import com.biperf.util.StringUtils;

public class PlateauAwardLevelActivityReportAction extends PlateauAwardReportAction
{

  @Override
  protected String getReportCode()
  {
    return Report.PLATEAU_AWARD_LEVEL_ACTIVITY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    List<String> requiredFields = Arrays.asList( new String[] {} );
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_level_activity_summary" );
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      Map<String, Object> output = getPlateauAwardReportsService().getAwardLevelActivitySummaryResults( reportParameters );

      List<PlateauAwardLevelActivityReportValue> reportData = (List<PlateauAwardLevelActivityReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        PlateauAwardLevelActivityReportValue totalsRowData = (PlateauAwardLevelActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }
    return mapping.findForward( "display_level_activity_summary" );
  }

  public ActionForward displayTeamLevelReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getPlateauAwardReportsService().getAwardLevelActivityTeamLevelResults( reportParameters );

      List<PlateauAwardLevelActivityReportValue> reportData = (List<PlateauAwardLevelActivityReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        PlateauAwardLevelActivityReportValue totalsRowData = (PlateauAwardLevelActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    return mapping.findForward( "display_level_activity_team" );
  }

  public ActionForward displayPlateauAwardActivityChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getPlateauAwardReportsService().getPlateauAwardActivityChartResults( reportParameters );
    List<PlateauAwardLevelActivityReportValue> reportData = (List<PlateauAwardLevelActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_plateau_award_activity_chart" );
  }

  public ActionForward displayPercentagePlateauAwardActivityChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getPlateauAwardReportsService().getPercentagePlateauAwardActivityChartResults( reportParameters );
    List<PlateauAwardLevelActivityReportValue> reportData = (List<PlateauAwardLevelActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_perc_plateau_award_activity_chart" );
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

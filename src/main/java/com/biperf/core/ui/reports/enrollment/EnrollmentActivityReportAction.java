
package com.biperf.core.ui.reports.enrollment;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.enrollment.EnrollmentActivityReportValue;

public class EnrollmentActivityReportAction extends EnrollmentReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.ENROLLMENT_ACTIVITY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getEnrollmentReportsService().getEnrollmentSummaryResults( reportParameters );
      List<EnrollmentActivityReportValue> reportData = (List<EnrollmentActivityReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        EnrollmentActivityReportValue totalsRowData = (EnrollmentActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    return mapping.findForward( "display_enrollment_activity_summary" );
  }

  public ActionForward displayDetailsReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> output = getEnrollmentReportsService().getEnrollmentDetailsResults( reportParameters );
    List<EnrollmentActivityReportValue> reportData = (List<EnrollmentActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    return mapping.findForward( "display_enrollment_activity_details" );
  }

  public ActionForward displayTotalEnrollmentBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getEnrollmentReportsService().getTotalEnrollmentsBarResults( reportParameters );
    List<EnrollmentActivityReportValue> reportData = (List<EnrollmentActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_enrollment_activity_barchart" );
  }

  public ActionForward displayEnrollmentPercentageBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getEnrollmentReportsService().getEnrollmentStatusPercentageBarResults( reportParameters );
    List<EnrollmentActivityReportValue> reportData = (List<EnrollmentActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    request.setAttribute( "participantStatus", reportParameters.get( "participantStatus" ) );
    return mapping.findForward( "display_enrollment_activity_status_percentage_barchart" );
  }

  public ActionForward displayActiveInactiveBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getEnrollmentReportsService().getEnrollmentActiveInactiveBarResults( reportParameters );
    List<EnrollmentActivityReportValue> reportData = (List<EnrollmentActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_enrollment_activity_status_inactive_barchart" );
  }

  public ActionForward displayPiechart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getEnrollmentReportsService().getPieResults( reportParameters );
    List<EnrollmentActivityReportValue> reportData = (List<EnrollmentActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_enrollment_activity_piechart" );
  }

}

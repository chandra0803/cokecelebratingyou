
package com.biperf.core.ui.reports.throwdown;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.ThrowdownActivityReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.throwdown.ThrowdownActivityByPaxReportValue;

public class ThrowdownActivityByPaxReportAction extends BaseReportsAction
{

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "COUNTRY",
                                                             "PARTICIPANT_STATUS",
                                                             "ORG_PATH",
                                                             "PRIMARY_ORG_UNIT",
                                                             "JOB_POSITION",
                                                             "DEPARTMENT",
                                                             "PROMOTION_NAME",
                                                             "ROUND",
                                                             "ROUND_START_DATE",
                                                             "ROUND_END_DATE",
                                                             "WIN",
                                                             "LOSS",
                                                             "TIE",
                                                             "ACTUAL_RESULTS",
                                                             "RANKING",
                                                             "POINTS_EARNED",
                                                             "PAX_CHAR1",
                                                             "PAX_CHAR2",
                                                             "PAX_CHAR3",
                                                             "PAX_CHAR4",
                                                             "PAX_CHAR5",
                                                             "PAX_CHAR6",
                                                             "PAX_CHAR7",
                                                             "PAX_CHAR8",
                                                             "PAX_CHAR9",
                                                             "PAX_CHAR10",
                                                             "PAX_CHAR11",
                                                             "PAX_CHAR12",
                                                             "PAX_CHAR13",
                                                             "PAX_CHAR14",
                                                             "PAX_CHAR15",
                                                             "PAX_CHAR16",
                                                             "PAX_CHAR17",
                                                             "PAX_CHAR18",
                                                             "PAX_CHAR19",
                                                             "PAX_CHAR20" };

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> output = getThrowdownActivityReportsService().getThrowdownActivityByPaxSummaryTabularResults( reportParameters );
      List<ThrowdownActivityByPaxReportValue> reportData = (List<ThrowdownActivityByPaxReportValue>)output.get( OUTPUT_DATA );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        ThrowdownActivityByPaxReportValue totalsRowData = (ThrowdownActivityByPaxReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_summary_report" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> output = getThrowdownActivityReportsService().getThrowdownActivityByPaxDetailTabularResults( reportParameters );
    List<ThrowdownActivityByPaxReportValue> reportData = (List<ThrowdownActivityByPaxReportValue>)output.get( OUTPUT_DATA );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      ThrowdownActivityByPaxReportValue totalsRowData = (ThrowdownActivityByPaxReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_detail_report" );
  }

  public ActionForward displayThrowdownTotalActivityChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getThrowdownActivityReportsService().getThrowdownTotalActivityChartResults( reportParameters );
    List<ThrowdownActivityByPaxReportValue> reportData = (List<ThrowdownActivityByPaxReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_total_activity_chart" );
  }

  public ActionForward displayThrowdownActivityByRoundChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getThrowdownActivityReportsService().getThrowdownActivityByRoundChartResults( reportParameters );
    List<ThrowdownActivityByPaxReportValue> reportData = (List<ThrowdownActivityByPaxReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_activity_by_round_chart" );
  }

  public ActionForward displayThrowdownPointsEarnedChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getThrowdownActivityReportsService().getPointsEarnedInThrowdownChartResults( reportParameters );
    List<ThrowdownActivityByPaxReportValue> reportData = (List<ThrowdownActivityByPaxReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_points_earned_chart" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getThrowdownActivityReportsService().getThrowdownActivityByPaxReportExtract( extractParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.THROWDOWN_ACTIVITY_BY_PAX_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportCode()
  {
    return Report.THROWDOWN_ACTIVITY_BY_PAX;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.throwdownactivity.extract";
  }

  protected ThrowdownActivityReportsService getThrowdownActivityReportsService()
  {
    return (ThrowdownActivityReportsService)getService( ThrowdownActivityReportsService.BEAN_NAME );
  }
}

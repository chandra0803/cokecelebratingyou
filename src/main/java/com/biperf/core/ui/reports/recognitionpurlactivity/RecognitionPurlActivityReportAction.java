
package com.biperf.core.ui.reports.recognitionpurlactivity;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.RecognitionPurlActivityReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.recognitionpurlactivity.RecognitionPurlActivityReportValue;

/**
 * RecognitionPurlActivityReportAction
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>kandhi</td>
 * <td>Aug 31, 2012</td>
 * <td>1.0</td>
 * <td>Intital version</td>
 * </tr>
 * </table>
 * 
 * @author kandhi
 */
public class RecognitionPurlActivityReportAction extends BaseReportsAction
{
  public static String[] EXTRACT_ALL_COLUMN_NAMES = { "PROMOTION_NAME",
                                                      "RECIPIENT_FIRST_NAME",
                                                      "RECIPIENT_MIDDLE_NAME",
                                                      "RECIPIENT_LAST_NAME",
                                                      "RECIPIENT_LOGIN_ID",
                                                      "RECIPIENT_EMAIL_ADDRESS",
                                                      "RECIPIENT_COUNTRY",
                                                      "PRIMARY_ORG_UNIT",
                                                      "MANAGER_FIRST_NAME",
                                                      "MANAGER_LAST_NAME",
                                                      "MANAGER_PRIMARY_ORG_UNIT",
                                                      "MANAGER_COUNTRY",
                                                      "AWARD_DATE",
                                                      "PURL_STATUS",
                                                      "AWARD",
                                                      "CONTRIBUTORS_INVITED",
                                                      "ACTUAL_CONTRIBUTORS",
                                                      "CONTRIBUTION_PERCENTAGE",
                                                      "CONTRIBUTIONS_POSTED",
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
  													  "PAX_CHAR20",
  													  "PAX_CHAR21",
  													  "PAX_CHAR22",
  													  "PAX_CHAR23",
  													  "PAX_CHAR24",
  													  "PAX_CHAR25",
  													  "PAX_CHAR26",
  													  "PAX_CHAR27",
  													  "PAX_CHAR28",
  													  "PAX_CHAR29",
  													  "PAX_CHAR30",
  													  "PAX_CHAR31",
  													  "PAX_CHAR32",
  													  "PAX_CHAR33",
  													  "PAX_CHAR34",
  													  "PAX_CHAR35"};


  @Override
  protected String getReportCode()
  {
    return Report.RECOGNITION_PURL_ACTIVITY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {

      Map<String, Object> output = getRecognitionPurlActivityReportsService().getSummaryTabularResults( reportParameters );

      List<RecognitionPurlActivityReportValue> reportData = (List<RecognitionPurlActivityReportValue>)output.get( OUTPUT_DATA );

      int maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        RecognitionPurlActivityReportValue totalsRowData = (RecognitionPurlActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_summary_report" );
  }

  public ActionForward displayParticipantReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> output = getRecognitionPurlActivityReportsService().getParticipantTabularResults( reportParameters );
    List<RecognitionPurlActivityReportValue> reportData = (List<RecognitionPurlActivityReportValue>)output.get( OUTPUT_DATA );

    int maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      RecognitionPurlActivityReportValue totalsRowData = (RecognitionPurlActivityReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_participant_list_report" );
  }

  public ActionForward displayPurlActivityChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );

    Map<String, Object> output = getRecognitionPurlActivityReportsService().getPurlActivityChartResults( reportParameters );

    List<RecognitionPurlActivityReportValue> reportData = (List<RecognitionPurlActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_purl_activity_chart" );
  }

  public ActionForward displayOverallContributorsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getRecognitionPurlActivityReportsService().getOverallContributorsChartResults( reportParameters );
    List<RecognitionPurlActivityReportValue> reportData = (List<RecognitionPurlActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_overall_contributors_chart" );
  }

  public ActionForward displayPurlRecipientsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getRecognitionPurlActivityReportsService().getPurlReceipientsChartResults( reportParameters );
    List<RecognitionPurlActivityReportValue> reportData = (List<RecognitionPurlActivityReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_purl_recipients_chart" );
  }

  protected RecognitionPurlActivityReportsService getRecognitionPurlActivityReportsService()
  {
    return (RecognitionPurlActivityReportsService)getService( RecognitionPurlActivityReportsService.BEAN_NAME );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.recognition.purlactivity.extract";
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParametersMap )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getRecognitionPurlActivityReportsService().getExtractResults( reportParametersMap );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.RECOGNITION_PURL_ACTIVITY_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }
}

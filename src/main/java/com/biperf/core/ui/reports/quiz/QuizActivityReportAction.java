/**
 * 
 */

package com.biperf.core.ui.reports.quiz;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.quizactivityreport.QuizActivityDetailOneReportValue;
import com.biperf.core.value.quizactivityreport.QuizActivityDetailTwoReportValue;
import com.biperf.core.value.quizactivityreport.QuizActivityForOrgReportValue;
import com.biperf.core.value.quizactivityreport.QuizActivitySummaryReportValue;
import com.biperf.core.value.quizactivityreport.QuizStatusPercentForOrgReportValue;

/**
 * @author poddutur
 *
 */
public class QuizActivityReportAction extends QuizReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.QUIZ_ACTIVITY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      filterDIYQuizzes( reportParameters );
      Map<String, Object> output = getQuizReportsService().getQuizActivitySummaryResults( reportParameters );
      List<QuizActivitySummaryReportValue> reportData = (List<QuizActivitySummaryReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      for ( QuizActivitySummaryReportValue quizReport : reportData )
      {
        if ( quizReport.getOrgName().contains( "Team" ) )
        {
          quizReport.setIsTeam( new Long( 1 ) );
        }
        else
        {
          quizReport.setIsTeam( new Long( 0 ) );
        }
      }
      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        QuizActivitySummaryReportValue totalsRowData = (QuizActivitySummaryReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }

    return mapping.findForward( "display_quiz_activity_summary_report" );
  }

  public ActionForward displayQuizActivityDetailOneReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizActivityDetailOneResults( reportParameters );
    List<QuizActivityDetailOneReportValue> reportData = (List<QuizActivityDetailOneReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      QuizActivityDetailOneReportValue totalsRowData = (QuizActivityDetailOneReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    return mapping.findForward( "display_quiz_activity_detail_one_report" );
  }

  public ActionForward displayQuizActivityDetailTwoReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizActivityDetailTwoResults( reportParameters );
    List<QuizActivityDetailTwoReportValue> reportData = (List<QuizActivityDetailTwoReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      QuizActivityDetailTwoReportValue totalsRowData = (QuizActivityDetailTwoReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
    return mapping.findForward( "display_quiz_activity_detail_two_report" );
  }

  public ActionForward displayQuizActivityForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizActivityForOrgBarResults( reportParameters );
    List<QuizActivityForOrgReportValue> reportData = (List<QuizActivityForOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_quiz_activity_for_org_report" );
  }

  public ActionForward displayQuizStatusPercentForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizStatusPercentForOrgBarResults( reportParameters );
    List<QuizStatusPercentForOrgReportValue> reportData = (List<QuizStatusPercentForOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_quiz_status_percent_for_org_report" );
  }

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "PRIMARY_ORG_UNIT",
                                                             "JOB_POSITION",
                                                             "DEPARTMENT",
                                                             "QUIZ_PROMOTION",
                                                             "QUIZ_DATE",
                                                             "QUIZ_SCORE",
                                                             "QUIZ_RESULT",
                                                             "POINTS",
                                                             // "SWEEPSTAKES_WON",
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


  private static final String[] EXTRACT_ALL_PLATEAU_ONLY_COLUMN_NAMES = { "FIRST_NAME",
                                                                          "MIDDLE_NAME",
                                                                          "LAST_NAME",
                                                                          "LOGIN_ID",
                                                                          "PRIMARY_ORG_UNIT",
                                                                          "JOB_POSITION",
                                                                          "DEPARTMENT",
                                                                          "QUIZ_PROMOTION",
                                                                          "QUIZ_DATE",
                                                                          "QUIZ_SCORE",
                                                                          "QUIZ_RESULT",
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
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "isExtract", Boolean.TRUE );
    filterDIYQuizzes( reportParameters );
    Map reportExtractData = getQuizReportsService().getQuizActivityReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, getAllColumnNames() );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.QUIZ_ACTIVITY_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return getAllColumnNames();
  }

  protected String[] getAllColumnNames()
  {
    if ( isPlateauOnlyPlatform() )
    {
      return EXTRACT_ALL_PLATEAU_ONLY_COLUMN_NAMES;
    }
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}

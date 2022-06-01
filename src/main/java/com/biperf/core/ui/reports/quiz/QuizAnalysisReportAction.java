/**
 * 
 */

package com.biperf.core.ui.reports.quiz;

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
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.quizanalysisreport.QuizAnalysisDetailOneReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAnalysisDetailTwoReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAnalysisSummaryReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAttemptStatusForOrgReportValue;
import com.biperf.core.value.quizanalysisreport.QuizAttemptsPercentForOrgReportValue;
import com.biperf.core.value.quizanalysisreport.QuizQuestionAnalysisReportValue;
import com.biperf.util.StringUtils;

/**
 * @author poddutur
 *
 */
public class QuizAnalysisReportAction extends QuizReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.QUIZ_ANALYSIS;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    reportParametersForm.setQqId( null );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    List<String> requiredFields = Arrays.asList( new String[] { FILTER_PROMOTION_ID } );
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_quiz_analysis_summary_report" );
    }
    if ( StringUtils.isEmpty( request.getParameter( CLEAR_FORM ) ) || isDashboardPage( request ) )
    {
      filterDIYQuizzes( reportParameters );
      Map<String, Object> output = getQuizReportsService().getQuizAnalysisSummaryResults( reportParameters );
      List<QuizAnalysisSummaryReportValue> reportData = (List<QuizAnalysisSummaryReportValue>)output.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        QuizAnalysisSummaryReportValue totalsRowData = (QuizAnalysisSummaryReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    else
    {
      request.setAttribute( "hideCharts", Boolean.TRUE );
    }

    return mapping.findForward( "display_quiz_analysis_summary_report" );
  }

  public ActionForward displayQuizAnalysisDetailOneReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    reportParametersForm.setQqId( null );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizAnalysisDetailOneResults( reportParameters );
    List<QuizAnalysisDetailOneReportValue> reportData = (List<QuizAnalysisDetailOneReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      QuizAnalysisDetailOneReportValue totalsRowData = (QuizAnalysisDetailOneReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    return mapping.findForward( "display_quiz_analysis_detail_one_report" );
  }

  public ActionForward displayQuizAnalysisDetailTwoReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizAnalysisDetailTwoResults( reportParameters );
    List<QuizAnalysisDetailTwoReportValue> reportData = (List<QuizAnalysisDetailTwoReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)output.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      QuizAnalysisDetailTwoReportValue totalsRowData = (QuizAnalysisDetailTwoReportValue)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    return mapping.findForward( "display_quiz_analysis_detail_two_report" );
  }

  public ActionForward displayQuizQuestionAnalysisBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizQuestionAnalysisBarResults( reportParameters );
    List<QuizQuestionAnalysisReportValue> reportData = (List<QuizQuestionAnalysisReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_quiz_question_analysis_report" );
  }

  public ActionForward displayQuizAttemptsPercentForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizAttemptsPercentForOrgBarResults( reportParameters );
    List<QuizAttemptsPercentForOrgReportValue> reportData = (List<QuizAttemptsPercentForOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_quiz_attempts_percent_for_org_report" );
  }

  public ActionForward displayQuizAttemptStatusForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    filterDIYQuizzes( reportParameters );
    Map<String, Object> output = getQuizReportsService().getQuizAttemptStatusForOrgBarResults( reportParameters );
    List<QuizAttemptStatusForOrgReportValue> reportData = (List<QuizAttemptStatusForOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_quiz_attempts_status_for_org_report" );
  }

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "QUIZ_PROMOTION",
                                                             "QUIZ_TYPE",
                                                             "QUIZ_START_DATE",
                                                             "QUIZ_END_DATE",
                                                             "QUESTIONS_IN_POOL",
                                                             "QUESTIONS_TO_ASK_PER_ATTEMPT",
                                                             "MAX_ATTEMPTS_ALLOWED_FOR_PAX",
                                                             "QUESTIONS",
                                                             "NUMBER_OF_TIMES_ASKED",
                                                             "NUMBER_OF_CORRECT_RESPONSES",
                                                             "PERCENT_OF_CORRECT_RESPONSES",
                                                             "NUMBER_OF_INCORRECT_RESPONSES",
                                                             "PERCENT_OF_INCORRECT_RESPONSES" };

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    reportParameters.put( "isExtract", Boolean.TRUE );
    filterDIYQuizzes( reportParameters );
    Map reportExtractData = getQuizReportsService().getQuizAnalysisReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.QUIZ_ANALYSIS_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }
}

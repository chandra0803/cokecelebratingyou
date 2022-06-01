
package com.biperf.core.ui.reports.challengepoint;

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
import com.biperf.core.value.challengepoint.ChallengePointSummaryReportValue;

public class ChallengePointProgramSummaryReportAction extends ChallengepointReportAction
{

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "PROMO_NAME",
                                                             "PAX_ACHIEVED_GOAL",
                                                             "PERCENT_OF_GOAL_ACHIEVED_PAX",
                                                             "TOTAL_BASElINE_ACHIEVERS",
                                                             "TOTAL_PRODUCTION_ACHIEVERS",
                                                             "PERCENT_INCRESE_ACHIEVERS",
                                                             "UNIT_DOLLAR_ACHIEVERS",
                                                             "PAX_NOT_ACHIEVED_GOAL",
                                                             "PERCENT_OF_GOAL_NOT_ACHIEVED_PAX",
                                                             "TOTAL_BASElINE_NON_ACHIEVERS",
                                                             "TOTAL_PRODUCTION_NON_ACHIEVERS",
                                                             "PERCENT_INCRESE_NON_ACHIEVERS",
                                                             "UNIT_DOLLAR_NON_ACHIEVERS" };

  @Override
  protected String getReportCode()
  {
    return Report.CHALLENGE_POINT_SUMMARY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointProgramSummaryTabularResults( reportParameters );
      List<ChallengePointSummaryReportValue> reportData = (List<ChallengePointSummaryReportValue>)resultsMap.get( "p_out_data" );
      request.setAttribute( "reportData", reportData );
      int maxRows = (int)resultsMap.get( "p_out_size_data" );
      if ( isLastPage( maxRows, reportParameters ) )
      {
        ChallengePointSummaryReportValue totalsRowData = (ChallengePointSummaryReportValue)resultsMap.get( "p_out_totals_data" );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "maxRows", maxRows );
    }
    return mapping.findForward( "display_challenge_point_program_summary" );
  }

  public ActionForward displayGoalAchievementChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointGoalAchievementChart( reportParameters );
    ChallengePointSummaryReportValue reportData = (ChallengePointSummaryReportValue)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_challenge_point_goalachievement_chart" );
  }

  public ActionForward displayChallengePointIncrementalChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointIncrementalChart( reportParameters );
    List<ChallengePointSummaryReportValue> reportData = (List<ChallengePointSummaryReportValue>)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_challenge_point_incremental_chart" );
  }

  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getChallengepointReportsService().getChallengePointProgramSummaryExtractReport( extractParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();

  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.CP_PROGRAM_SUMMARY_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}

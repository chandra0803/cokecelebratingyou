
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
import com.biperf.core.value.challengepoint.ChallengePointManagerReportValue;

public class ChallengePointManagerReportAction extends ChallengepointReportAction
{

  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "MANAGER_STATUS",
                                                             "COUNTRY",
                                                             "DEPARTMENT",
                                                             "PROMO_NAME",
                                                             "TOTAL_OF_PARTICIPANTS",
                                                             "NO_OF_PAX_SELECTED_GOAL",
                                                             "NO_OF_PAX_ACHIEVING",
                                                             "PERCENT_OF_GOAL_SELECT_PAX",
                                                             "PERCENT_OF_TOTAL_PAX_ACHIEVING",
                                                             "MANAGER_OVERRIDE",
                                                             "TOAL_EARNED_POINTS",
                                                             "MANAGER_PAYOUT_PER_ACHIEVER",
                                                             "TOTAL_MANAGER_POINTS",
                                                             "MANAGER_EARNED_PLATEAU",
                                                             "PAX_CHAR_1",
                                                             "PAX_CHAR_2",
                                                             "PAX_CHAR_3",
                                                             "PAX_CHAR_4",
                                                             "PAX_CHAR_5",
                                                             "PAX_CHAR_6",
                                                             "PAX_CHAR_7",
                                                             "PAX_CHAR_8",
                                                             "PAX_CHAR_9",
                                                             "PAX_CHAR_10",
                                                             "PAX_CHAR_11",
                                                             "PAX_CHAR_12",
                                                             "PAX_CHAR_13",
                                                             "PAX_CHAR_14",
                                                             "PAX_CHAR_15",
                                                             "PAX_CHAR_16",
                                                             "PAX_CHAR_17",
                                                             "PAX_CHAR_18",
                                                             "PAX_CHAR_19",
                                                             "PAX_CHAR_20" };

  @Override
  protected String getReportCode()
  {
    return Report.CHALLENGE_POINT_MANAGER;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointManagerTabularResults( reportParameters );
      List<ChallengePointManagerReportValue> reportData = (List<ChallengePointManagerReportValue>)resultsMap.get( "p_out_data" );
      int maxRows = (int)resultsMap.get( "p_out_size_data" );
      if ( isLastPage( maxRows, reportParameters ) )
      {
        ChallengePointManagerReportValue totalsRowData = (ChallengePointManagerReportValue)resultsMap.get( "p_out_totals_data" );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "reportData", reportData );
      request.setAttribute( "maxRows", maxRows );
    }
    return mapping.findForward( "display_challenge_point_manager_summary" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointManagerDetailTabularResults( reportParameters );
    List<ChallengePointManagerReportValue> reportData = (List<ChallengePointManagerReportValue>)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_challenge_point_manager_detail" );
  }

  public ActionForward displayTotalPointsEarnedChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getManagerTotalPointsEarnedChart( reportParameters );
    List<ChallengePointManagerReportValue> reportData = (List<ChallengePointManagerReportValue>)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_manager_points_earned_chart" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getChallengepointReportsService().getChallengePointManagerAchievementExtractReport( extractParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.CP_MANAGER_ACHIEVEMENT_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }
}


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
import com.biperf.core.value.challengepoint.ChallengePointAchievementReportValue;

public class ChallengepointAchievementReportAction extends ChallengepointReportAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "EMAIL_ADDRESS",
                                                             "COUNTRY",
                                                             "ORG_PATH",
                                                             "PARTICIPANT_STATUS",
                                                             "PRIMARY_ORG_UNIT",
                                                             "JOB_POSITION",
                                                             "DEPARTMENT",
                                                             "PHONE_NBR",
                                                             "PROMO_NAME",
                                                             "LEVEL_NAME",
                                                             "BASELINE",
                                                             "CHALLENGEPOINT",
                                                             "ACTUAL_RESULTS",
                                                             "PERCENT_OF_CHALLENGEPOINT",
                                                             "ACHIEVED",
                                                             "POINTS_EARNED",
                                                             "BASE_POINTS_EARNED",
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
    return Report.CHALLENGE_POINT_ACHIEVEMENT;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementTabularResults( reportParameters );
      List<ChallengePointAchievementReportValue> reportData = (List<ChallengePointAchievementReportValue>)resultsMap.get( "p_out_data" );
      request.setAttribute( "reportData", reportData );

      int maxRows = (int)resultsMap.get( "p_out_size_data" );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        ChallengePointAchievementReportValue totalsRowData = (ChallengePointAchievementReportValue)resultsMap.get( "p_out_totals_data" );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "maxRows", maxRows );
      request.setAttribute( "reportDetailType", "orgLevel" );
      determineGoalLevelDisplay( (ReportParametersForm)form, request );
    }
    return mapping.findForward( "display_challenge_point_achievement_summary" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementDetailResults( reportParameters );
    List<ChallengePointAchievementReportValue> reportData = (List<ChallengePointAchievementReportValue>)resultsMap.get( "p_out_data" );

    request.setAttribute( "reportData", reportData );

    int maxRows = (int)resultsMap.get( "p_out_size_data" );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      ChallengePointAchievementReportValue totalsRowData = (ChallengePointAchievementReportValue)resultsMap.get( "p_out_totals_data" );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "maxRows", maxRows );
    request.setAttribute( "reportDetailType", "paxLevel" );
    return mapping.findForward( "display_challenge_point_achievement_detail" );
  }

  public ActionForward displayAchievementPercentageChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementPercentageAchievedChart( reportParameters );
    ChallengePointAchievementReportValue reportData = (ChallengePointAchievementReportValue)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_challenge_point_achievement_percentage_chart" );
  }

  public ActionForward displayAchievementCountChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementCountAchievedChart( reportParameters );
    ChallengePointAchievementReportValue reportData = (ChallengePointAchievementReportValue)resultsMap.get( "p_out_data" );

    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_challenge_point_achievement_count_chart" );
  }

  public ActionForward displayAchievementResultsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementResultsChart( reportParameters );
    List<ChallengePointAchievementReportValue> reportData = (List<ChallengePointAchievementReportValue>)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_challenge_point_achievement_results_chart" );
  }

  public ActionForward displayAchievementPercentageByOrgChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementTabularResults( reportParameters );
    List<ChallengePointAchievementReportValue> reportData = (List<ChallengePointAchievementReportValue>)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_challenge_point_achievement_percentage_by_org_chart" );
  }

  public ActionForward displayAchievementCountByOrgChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getChallengepointReportsService().getChallengePointAchievementTabularResults( reportParameters );
    List<ChallengePointAchievementReportValue> reportData = (List<ChallengePointAchievementReportValue>)resultsMap.get( "p_out_data" );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_challenge_point_achievement_count_by_org_chart" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getChallengepointReportsService().getChallengePointAchievementExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.CP_ACHIEVEMENT_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}

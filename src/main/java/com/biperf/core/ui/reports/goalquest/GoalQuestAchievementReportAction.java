
package com.biperf.core.ui.reports.goalquest;

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
import com.biperf.core.value.goalquest.GoalQuestAchievementReportValue;

public class GoalQuestAchievementReportAction extends GoalQuestReportAction
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
                                                             "PROMO_NAME",
                                                             "LEVEL_NAME",
                                                             "BASELINE",
                                                             "GOAL",
                                                             "ACTUAL",
                                                             "PERCENT_OF_GOAL",
                                                             "ACHIEVED",
                                                             "POINTS",
                                                             "PLATEAU_EARNED",
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

  private static final String[] EXTRACT_ALL_PLATEAU_ONLY_COLUMN_NAMES = { "FIRST_NAME",
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
                                                                          "PROMO_NAME",
                                                                          "LEVEL_NAME",
                                                                          "BASELINE",
                                                                          "GOAL",
                                                                          "ACTUAL",
                                                                          "PERCENT_OF_GOAL",
                                                                          "ACHIEVED",
                                                                          "PLATEAU_EARNED",
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
    return Report.GOAL_QUEST_ACHIEVEMENT;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementTabularResults( reportParameters );
      List<GoalQuestAchievementReportValue> reportData = (List<GoalQuestAchievementReportValue>)resultsMap.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        GoalQuestAchievementReportValue totalsRowData = (GoalQuestAchievementReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "maxRows", maxRows );
      request.setAttribute( "reportDetailType", "orgLevel" );
      determineGoalLevelDisplay( (ReportParametersForm)form, request );
    }
    return mapping.findForward( "display_goal_quest_achievement_summary" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementDetailResults( reportParameters );
    List<GoalQuestAchievementReportValue> reportData = (List<GoalQuestAchievementReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      GoalQuestAchievementReportValue totalsRowData = (GoalQuestAchievementReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "maxRows", maxRows );
    request.setAttribute( "reportDetailType", "paxLevel" );
    return mapping.findForward( "display_goal_quest_achievement_detail" );
  }

  public ActionForward displayAchievementPercentageChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementPercentageAchievedChart( reportParameters );
    GoalQuestAchievementReportValue reportData = (GoalQuestAchievementReportValue)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_achievement_percentage_chart" );
  }

  public ActionForward displayAchievementCountChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementCountAchievedChart( reportParameters );
    GoalQuestAchievementReportValue reportData = (GoalQuestAchievementReportValue)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_achievement_count_chart" );
  }

  public ActionForward displayAchievementResultsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementResultsChart( reportParameters );
    List<GoalQuestAchievementReportValue> reportData = (List<GoalQuestAchievementReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_goal_quest_achievement_results_chart" );
  }

  public ActionForward displayAchievementPercentageByOrgChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementTabularResults( reportParameters );
    List<GoalQuestAchievementReportValue> reportData = (List<GoalQuestAchievementReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_achievement_percentage_by_org_chart" );
  }

  public ActionForward displayAchievementCountByOrgChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestAchievementTabularResults( reportParameters );
    List<GoalQuestAchievementReportValue> reportData = (List<GoalQuestAchievementReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_achievement_count_by_org_chart" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getGoalQuestReportsService().getGoalQuestAchievementExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, getAllColumnNames() );
    buildCSVExtractContent( contentBuf, reportExtractData );

    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.GQ_ACHIEVEMENT_REPORT;
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

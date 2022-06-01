
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
import com.biperf.core.value.goalquest.GoalQuestSummaryReportValue;

public class GoalQuestProgramSummaryReportAction extends GoalQuestReportAction
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
    return Report.GOAL_QUEST_SUMMARY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestProgramSummaryTabularResults( reportParameters );
      List<GoalQuestSummaryReportValue> reportData = (List<GoalQuestSummaryReportValue>)resultsMap.get( OUTPUT_DATA );

      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
      if ( isLastPage( maxRows, reportParameters ) )
      {
        GoalQuestSummaryReportValue totalsRowData = (GoalQuestSummaryReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "maxRows", maxRows );
    }
    return mapping.findForward( "display_goal_quest_program_summary" );
  }

  public ActionForward displayGoalAchievementChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestGoalAchievementChart( reportParameters );
    GoalQuestSummaryReportValue reportData = (GoalQuestSummaryReportValue)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_goal_quest_goalachievement_chart" );
  }

  public ActionForward displayGoalQuestIncrementalChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestIncrementalChart( reportParameters );
    List<GoalQuestSummaryReportValue> reportData = (List<GoalQuestSummaryReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_goal_quest_incremental_chart" );
  }

  protected String getExtractReportData( Map<String, Object> extractParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getGoalQuestReportsService().getGoalQuestProgramSummaryExtractReport( extractParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();

  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.GQ_PROGRAM_SUMMARY_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}

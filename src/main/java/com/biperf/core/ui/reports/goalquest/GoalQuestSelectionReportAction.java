
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
import com.biperf.core.value.goalquest.GoalQuestSelectionReportValue;

public class GoalQuestSelectionReportAction extends GoalQuestReportAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LOGIN_ID",
                                                             "EMAIL_TYPE",
                                                             "EMAIL_ADDRESS",
                                                             "COUNTRY",
                                                             "PARTICIPANT_STATUS",
                                                             "PRIMARY_ORG_UNIT",
                                                             "JOB_TITLE",
                                                             "DEPARTMENT",
                                                             "ADDRESS1",
                                                             "ADDRESS2",
                                                             "ADDRESS3",
                                                             "CITY",
                                                             "STATE",
                                                             "POSTAL_CODE",
                                                             "PHONE_TYPE",
                                                             "PHONE_NBR",
                                                             "ORG_NAME",
                                                             "PROMO_NAME",
                                                             "LEVEL_NAME",
                                                             "BASELINE",
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
    return Report.GOAL_QUEST_SELECTION;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, true ) )
    {
      Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestSelectionTabularResults( reportParameters );
      List<GoalQuestSelectionReportValue> reportData = (List<GoalQuestSelectionReportValue>)resultsMap.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
      if ( isLastPage( maxRows, reportParameters ) )
      {

        GoalQuestSelectionReportValue totalsRowData = (GoalQuestSelectionReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "maxRows", maxRows );
      request.setAttribute( "reportDetailType", "orgLevel" );
      determineGoalLevelDisplay( (ReportParametersForm)form, request );
    }
    return mapping.findForward( "display_goal_quest_selection_summary" );
  }

  public ActionForward displayDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestSelectionDetailResults( reportParameters );
    List<GoalQuestSelectionReportValue> reportData = (List<GoalQuestSelectionReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    request.setAttribute( "reportDetailType", "paxLevel" );
    return mapping.findForward( "display_goal_quest_selection_detail" );
  }

  public ActionForward displaySelectionTotalsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestSelectionTotalsChart( reportParameters );
    GoalQuestSelectionReportValue reportData = (GoalQuestSelectionReportValue)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_selection_totals_chart" );
  }

  public ActionForward displaySelectionPercentageChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestSelectionPercentageChart( reportParameters );
    GoalQuestSelectionReportValue reportData = (GoalQuestSelectionReportValue)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_selection_percentage_chart" );
  }

  public ActionForward displaySelectionByOrgChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getGoalQuestReportsService().getGoalQuestSelectionByOrgChart( reportParameters );
    List<GoalQuestSelectionReportValue> reportData = (List<GoalQuestSelectionReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    determineGoalLevelDisplay( (ReportParametersForm)form, request );
    return mapping.findForward( "display_goal_quest_selection_by_org_chart" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getGoalQuestReportsService().getGoalQuestSelectionExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.GQ_SELECTION_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}

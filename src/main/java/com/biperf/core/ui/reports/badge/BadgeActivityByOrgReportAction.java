
package com.biperf.core.ui.reports.badge;

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
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.badge.BadgeActivityByOrgReportValue;

/**
 * 
 * BadgeActivityByOrgReportAction.
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
 * <td>Nov 29, 2012</td>
 * <td>1.0</td>
 * <td>Initial version</td>
 * </tr>
 * </table>
 * 
 */
public class BadgeActivityByOrgReportAction extends BadgeActivityReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.BADGE_AVTIVITY_BY_ORG;
  }

  @Override
  protected String getExtractFileName( ReportParametersForm reportParametersForm )
  {
    return Character.toUpperCase( Report.BADGE_ACTIVITY.charAt( 0 ) ) + Report.BADGE_ACTIVITY.substring( 1 ) + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";

  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getBadgeReportsService().getBadgeActivityByOrgSummaryTabularResults( reportParameters );

      List<BadgeActivityByOrgReportValue> reportData = (List<BadgeActivityByOrgReportValue>)output.get( OUTPUT_RESULTS_CURSOR );

      Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows.intValue(), reportParameters ) )
      {
        BadgeActivityByOrgReportValue totalsRowData = (BadgeActivityByOrgReportValue)output.get( OUTPUT_TOTALS_CURSOR );
        request.setAttribute( TOTALS_ROW_DATA, totalsRowData );
      }

      for ( BadgeActivityByOrgReportValue badgeReport : reportData )
      {
        if ( badgeReport.getOrgName().contains( "Team" ) )
        {
          badgeReport.setIsTeam( new Long( 1 ) );
        }
        else
        {
          badgeReport.setIsTeam( new Long( 0 ) );
        }
      }
      request.setAttribute( MAX_ROWS, maxRows );
      request.setAttribute( REPORT_DATA, reportData );
    }
    return mapping.findForward( "display_summary_level_tabular_data" );
  }

  public ActionForward displayBadgesEarnedAtTeamLevel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> output = getBadgeReportsService().getBadgeActivityTeamLevelTabularResults( reportParameters );
    List<BadgeActivityByOrgReportValue> reportData = (List<BadgeActivityByOrgReportValue>)output.get( OUTPUT_RESULTS_CURSOR );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows.intValue(), reportParameters ) )
    {
      BadgeActivityByOrgReportValue totalsRowData = (BadgeActivityByOrgReportValue)output.get( OUTPUT_TOTALS_CURSOR );
      request.setAttribute( TOTALS_ROW_DATA, totalsRowData );
    }

    request.setAttribute( MAX_ROWS, maxRows );
    request.setAttribute( REPORT_DATA, reportData );
    return mapping.findForward( "display_team_level_tabular_data" );
  }

  public ActionForward displayBadgesEarnedAtParticipantLevel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> output = getBadgeReportsService().getBadgeActivityParticipantLevelTabularResults( reportParameters );
    List<BadgeActivityByOrgReportValue> reportData = (List<BadgeActivityByOrgReportValue>)output.get( OUTPUT_RESULTS_CURSOR );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    request.setAttribute( REPORT_DATA, reportData );
    return mapping.findForward( "display_participant_level_tabular_data" );
  }

  public ActionForward displayBadgesEarnedBarChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getBadgeReportsService().getBadgesEarnedBarChartResults( reportParameters );
    List<BadgeActivityByOrgReportValue> reportData = (List<BadgeActivityByOrgReportValue>)output.get( OUTPUT_RESULT_SET );
    request.setAttribute( REPORT_DATA, reportData );
    return mapping.findForward( "display_badges_earned_bar_chart" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getBadgeReportsService().getBadgeActivityExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.BADGES_REPORT;
  }
}

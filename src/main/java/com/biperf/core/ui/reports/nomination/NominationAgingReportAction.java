
package com.biperf.core.ui.reports.nomination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.NominationApprovalStatusType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.NominationReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.nomination.NominationAgingReportValue;

/**
 * Action class for Behaviors report
 */
public class NominationAgingReportAction extends BaseReportsAction
{

  protected static final String[] EXTRACT_DETAIL_COLUMN_NAMES = { "NOMINEE_LOGIN_ID",
                                                                  "NOMINEE_FIRST_NAME",
                                                                  "NOMINEE_MIDDLE_NAME",
                                                                  "NOMINEE_LAST_NAME",
                                                                  "TEAM_NAME",
                                                                  "NOMINEE_COUNTRY",
                                                                  "NOMINEE_PRIMARY_ORG_UNIT",
                                                                  "NOMINEE_DEPARTMENT",
                                                                  "NOMINEE_JOB_TITLE",
                                                                  "NOMINATOR_LOGIN_ID",
                                                                  "NOMINATOR_FIRST_NAME",
                                                                  "NOMINATOR_LAST_NAME",
                                                                  "NOMINATOR_COUNTRY",
                                                                  "NOMINATOR_PRIMARY_ORG_UNIT",
                                                                  "NOMINATOR_DEPARTMENT",
                                                                  "NOMINATOR_JOB_TITLE",
                                                                  "PROMOTION",
                                                                  "DATE_SUBMITTED",
                                                                  "STATUS",
                                                                  "CURRENT_APPROVAL_STEP",
                                                                  "APPROVER_1_NAME",
                                                                  "APPROVER_1_APPROVAL_DATE",
                                                                  "APPROVER_2_NAME",
                                                                  "APPROVER_2_APPROVAL_DATE",
                                                                  "APPROVER_3_NAME",
                                                                  "APPROVER_3_APPROVAL_DATE",
                                                                  "APPROVER_4_NAME",
                                                                  "APPROVER_4_APPROVAL_DATE",
                                                                  "APPROVER_5_NAME",
                                                                  "APPROVER_5_APPROVAL_DATE",
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

  public static final String[] EXTRACT_SUMMARY_COLUMN_NAMES = { "PROMOTION_NAME",
                                                                "TIME_PERIOD",
                                                                "LEVEL_1_PENDING_COUNT",
                                                                "LEVEL_1_AVERAGE_WAIT",
                                                                "LEVEL_1_APPROVED_COUNT",
                                                                "LEVEL_1_WINNER_COUNT",
                                                                "LEVEL_1_DENIED_COUNT",
                                                                "LEVEL_1_MORE_INFO_COUNT",
                                                                "LEVEL_2_PENDING_COUNT",
                                                                "LEVEL_2_AVERAGE_WAIT",
                                                                "LEVEL_2_APPROVED_COUNT",
                                                                "LEVEL_2_WINNER_COUNT",
                                                                "LEVEL_2_DENIED_COUNT",
                                                                "LEVEL_2_MORE_INFO_COUNT",
                                                                "LEVEL_3_PENDING_COUNT",
                                                                "LEVEL_3_AVERAGE_WAIT",
                                                                "LEVEL_3_APPROVED_COUNT",
                                                                "LEVEL_3_WINNER_COUNT",
                                                                "LEVEL_3_DENIED_COUNT",
                                                                "LEVEL_3_MORE_INFO_COUNT",
                                                                "LEVEL_4_PENDING_COUNT",
                                                                "LEVEL_4_AVERAGE_WAIT",
                                                                "LEVEL_4_APPROVED_COUNT",
                                                                "LEVEL_4_WINNER_COUNT",
                                                                "LEVEL_4_DENIED_COUNT",
                                                                "LEVEL_4_MORE_INFO_COUNT",
                                                                "LEVEL_5_PENDING_COUNT",
                                                                "LEVEL_5_AVERAGE_WAIT",
                                                                "LEVEL_5_APPROVED_COUNT",                                                                
                                                                "LEVEL_5_WINNER_COUNT",
                                                                "LEVEL_5_DENIED_COUNT",
                                                                "LEVEL_5_MORE_INFO_COUNT" };

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_DETAIL_COLUMN_NAMES;
  }

  @Override
  protected String getReportCode()
  {
    return Report.NOMINATION_AGING;
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.NOMINATION_AGING_DETAIL_REPORT;
  }

  @Override
  protected ReportBeanMethod getSecondaryReportBeanMethod()
  {
    return ReportBeanMethod.NOMINATION_AGING_REPORT;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.nomination.aging.extract";
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    List<String> requiredFields = new ArrayList<>();
    if ( isChangeFiltersPage( request ) && hasErrorMessages( request, reportParameters, requiredFields, false ) )
    {
      return mapping.findForward( "display_nom_aging_summary" );
    }

    Map<String, Object> output = getNominationReportsService().getNominationAgingTabularResults( reportParameters );
    List<NominationAgingReportValue> reportData = (List<NominationAgingReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows.intValue(), reportParameters ) )
    {
      List<NominationAgingReportValue> totalsRowData = (List<NominationAgingReportValue>)output.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData.get( 0 ).getLevelData() );

    }

    return mapping.findForward( "display_nom_aging_summary" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayNominationStatusChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getNominationAgingBarchartResults( reportParameters );

    // Bar chart totals as a map from the status to the number of nominations at that status
    Map<String, Long> nominationsByStatusMap = (Map<String, Long>)output.get( OUTPUT_DATA );

    // The status is the picklist asset code. Not very user-friendly. Convert to CM name.
    NominationApprovalStatusType.getList().forEach( ( statusType ) ->
    {
      Long value = nominationsByStatusMap.remove( statusType.getCode() );
      if ( value != null )
      {
        nominationsByStatusMap.put( statusType.getName(), value );
      }
    } );

    request.setAttribute( "nominationsByStatusMap", nominationsByStatusMap );

    return mapping.findForward( "display_nom_aging_barchart_report" );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();

    Map reportExtractData = getNominationReportsService().getNominationAgingReportDetailExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_DETAIL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getSecondaryExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getNominationReportsService().getNominationAgingReportSummaryExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_SUMMARY_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  @Override
  protected String getSecondExtractFileName( ReportParametersForm reportParametersForm )
  {
    return "NominationAgingSummary" + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  protected NominationReportsService getNominationReportsService()
  {
    return (NominationReportsService)getService( NominationReportsService.BEAN_NAME );
  }
}

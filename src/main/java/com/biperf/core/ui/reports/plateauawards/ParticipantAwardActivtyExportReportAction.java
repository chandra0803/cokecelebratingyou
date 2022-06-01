/**
 * 
 */

package com.biperf.core.ui.reports.plateauawards;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.ParticipantAwardActivityExportReportService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;

/**
 * @author poddutur
 *
 */
public class ParticipantAwardActivtyExportReportAction extends BaseReportsAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "LEVEL_NAME",
                                                             "REFERENCE_NUMBER",
                                                             "AWARD_CODE",
                                                             "STATUS",
                                                             "PROMOTION_NAME",
                                                             "PRIMARY_ORG_NAME",
                                                             "ITEM_VALUE",
                                                             "DATE_REDEEMED",
                                                             "DATE_ISSUED",
                                                             "EXPIRY_DATE",
                                                             "ITEM_REDEEMED",
                                                             "PRIMARY_EMAIL_ADDRESS",
                                                             "COUNTRY",
                                                             "STATE",
                                                             "ADDRESS_1",
                                                             "ADDRESS_2",
                                                             "CITY",
                                                             "POSTAL_CODE",
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
                                                             "PAX_CHAR20" };

  @Override
  protected String getReportCode()
  {
    return Report.PLATEAU_AWARD_ITEM_SELECTION;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.participant.award.activity.extract";
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    return mapping.findForward( "display_summary_report" );
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getParticipantAwardActivityExportReportService().getParticipantAwardActivityExportReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  private ParticipantAwardActivityExportReportService getParticipantAwardActivityExportReportService()
  {
    return (ParticipantAwardActivityExportReportService)getService( ParticipantAwardActivityExportReportService.BEAN_NAME );
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.PARTICIPANT_AWARD_ACTIVITY_REPORT;
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

}

/**
 * 
 */

package com.biperf.core.ui.reports.participantexport;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.ParticipantExportReportService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;

/**
 * @author poddutur
 *
 */
public class ParticipantExportReportAction extends BaseReportsAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "LOGIN_ID",
                                                             "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "SUFFIX",
                                                             "BIRTH_DATE",
                                                             "GENDER",
                                                             "STATUS",
                                                             "EMAIL_ADDRESS",
                                                             "EMAIL_ADDRESS_TYPE",
                                                             "TEXT_MESSAGE_ADDRESS",
                                                             "ADDRESS_TYPE",
                                                             "COUNTRY",
                                                             "ADDRESS_1",
                                                             "ADDRESS_2",
                                                             "ADDRESS_3",
                                                             "ADDRESS_4",
                                                             "ADDRESS_5",
                                                             "ADDRESS_6",
                                                             "CITY",
                                                             "STATE_PROVIENCE",
                                                             "POSTAL_CODE",
                                                             "PERSONAL_PHONE_NUMBER",
                                                             "BUSINESS_PHONE_NUMBER",
                                                             "CELL_PHONE_NUMBER",
                                                             "EMPLOYER",
                                                             "JOB_POSITION",
                                                             "DEPARTMENT",
                                                             "HIRE_DATE",
                                                             "TERMINATION_DATE",
                                                             "ORG_UNIT1_NAME",
                                                             "ORG_UNIT1_ROLE",
                                                             "ORG_UNIT2_NAME",
                                                             "ORG_UNIT2_ROLE",
                                                             "ORG_UNIT3_NAME",
                                                             "ORG_UNIT3_ROLE",
                                                             "ORG_UNIT4_NAME",
                                                             "ORG_UNIT4_ROLE",
                                                             "ORG_UNIT5_NAME",
                                                             "ORG_UNIT5_ROLE",
                                                             // tccc customization start WIP 30460
                                                             "ORG_UNIT6_NAME",
                                                             "ORG_UNIT6_ROLE",
                                                             "ORG_UNIT7_NAME",
                                                             "ORG_UNIT7_ROLE",
                                                             "ORG_UNIT8_NAME",
                                                             "ORG_UNIT8_ROLE",
                                                             "ORG_UNIT9_NAME",
                                                             "ORG_UNIT9_ROLE",
                                                             "ORG_UNIT10_NAME",
                                                             "ORG_UNIT10_ROLE",
                                                             "ORG_UNIT11_NAME",
                                                             "ORG_UNIT11_ROLE",
                                                             // tccc customization end WIP 30460

                                                             "CHARACTERISTIC1_NAME",
                                                             "CHARACTERISTIC1_VALUES",
                                                             "CHARACTERISTIC2_NAME",
                                                             "CHARACTERISTIC2_VALUES",
                                                             "CHARACTERISTIC3_NAME",
                                                             "CHARACTERISTIC3_VALUES",
                                                             "CHARACTERISTIC4_NAME",
                                                             "CHARACTERISTIC4_VALUES",
                                                             "CHARACTERISTIC5_NAME",
                                                             "CHARACTERISTIC5_VALUES",
                                                             "CHARACTERISTIC6_NAME",
                                                             "CHARACTERISTIC6_VALUES",
                                                             "CHARACTERISTIC7_NAME",
                                                             "CHARACTERISTIC7_VALUES",
                                                             "CHARACTERISTIC8_NAME",
                                                             "CHARACTERISTIC8_VALUES",
                                                             "CHARACTERISTIC9_NAME",
                                                             "CHARACTERISTIC9_VALUES",
                                                             "CHARACTERISTIC10_NAME",
                                                             "CHARACTERISTIC10_VALUES",
                                                             "CHARACTERISTIC11_NAME",
                                                             "CHARACTERISTIC11_VALUES",
                                                             "CHARACTERISTIC12_NAME",
                                                             "CHARACTERISTIC12_VALUES",
                                                             "CHARACTERISTIC13_NAME",
                                                             "CHARACTERISTIC13_VALUES",
                                                             "CHARACTERISTIC14_NAME",
                                                             "CHARACTERISTIC14_VALUES",
                                                             "CHARACTERISTIC15_NAME",
                                                             "CHARACTERISTIC15_VALUES",
                                                             "CHARACTERISTIC16_NAME",
                                                             "CHARACTERISTIC16_VALUES",
                                                             "CHARACTERISTIC17_NAME",
                                                             "CHARACTERISTIC17_VALUES",
                                                             "CHARACTERISTIC18_NAME",
                                                             "CHARACTERISTIC18_VALUES",
                                                             "CHARACTERISTIC19_NAME",
                                                             "CHARACTERISTIC19_VALUES",
                                                             "CHARACTERISTIC20_NAME",
                                                             "CHARACTERISTIC20_VALUES",
                                                             "CHARACTERISTIC21_NAME",
                                                             "CHARACTERISTIC21_VALUES", 
                                                             "CHARACTERISTIC22_NAME",
                                                             "CHARACTERISTIC22_VALUES", 
                                                             "CHARACTERISTIC23_NAME",
                                                             "CHARACTERISTIC23_VALUES", 
                                                             "CHARACTERISTIC24_NAME",
                                                             "CHARACTERISTIC24_VALUES", 
                                                             "CHARACTERISTIC25_NAME",
                                                             "CHARACTERISTIC25_VALUES", 
                                                             "CHARACTERISTIC26_NAME",
                                                             "CHARACTERISTIC26_VALUES", 
                                                             "CHARACTERISTIC27_NAME",
                                                             "CHARACTERISTIC27_VALUES", 
                                                             "CHARACTERISTIC28_NAME",
                                                             "CHARACTERISTIC28_VALUES", 
                                                             "CHARACTERISTIC29_NAME",
                                                             "CHARACTERISTIC29_VALUES", 
                                                             "CHARACTERISTIC30_NAME",
                                                             "CHARACTERISTIC30_VALUES",
                                                             "CHARACTERISTIC31_NAME",
                                                             "CHARACTERISTIC31_VALUES", 
                                                             "CHARACTERISTIC32_NAME",
                                                             "CHARACTERISTIC32_VALUES", 
                                                             "CHARACTERISTIC33_NAME",
                                                             "CHARACTERISTIC33_VALUES", 
                                                             "CHARACTERISTIC34_NAME",
                                                             "CHARACTERISTIC34_VALUES", 
                                                             "CHARACTERISTIC35_NAME",
                                                             "CHARACTERISTIC35_VALUES", 
                                                             "ROLE1_NAME",
                                                             "ROLE2_NAME",
                                                             "ROLE3_NAME",
                                                             "ROLE4_NAME",
                                                             "ROLE5_NAME",
                                                             "LANGUAGE",
                                                             "SSO_ID" };

  protected ParticipantExportReportService getParticipantExportReportService()
  {
    return (ParticipantExportReportService)getService( ParticipantExportReportService.BEAN_NAME );
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getReportCode()
  {
    return Report.PARTICIPANT_EXPORT;
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.participant.extract";
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    return mapping.findForward( "display_summary_report" );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map output = getParticipantExportReportService().getParticipantExportReportExtract( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, output );
    return contentBuf.toString();
  }

  @Override
  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.PARTICIPANT_REPORT;
  }
}

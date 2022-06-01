/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/reports/login/LoginReportAction.java,v $
 *
 */

package com.biperf.core.ui.reports.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.reports.LoginReportsService;
import com.biperf.core.ui.reports.BaseReportsAction;
import com.biperf.core.ui.reports.ReportBeanMethod;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.loginreport.LoginByOrganizationReportValue;
import com.biperf.core.value.loginreport.LoginByPaxReportValue;
import com.biperf.core.value.loginreport.LoginByTimeReportValue;
import com.biperf.core.value.loginreport.LoginReportValue;
import com.biperf.core.value.loginreport.ReportLogonActivityOrgPercentageValue;
import com.biperf.core.value.loginreport.ReportParticipantLogonActivityValue;

/**
 * LoginReportAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>drahn</td>
 * <td>Aug 14, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author drahn
 *
 */
public abstract class LoginReportAction extends BaseReportsAction
{
  private static final String[] EXTRACT_ALL_COLUMN_NAMES = { "FIRST_NAME",
                                                             "MIDDLE_NAME",
                                                             "LAST_NAME",
                                                             "USER_NAME",
                                                             "COUNTRY",
                                                             "ORG_NAME",
                                                             "ROLE",
                                                             "JOB_POSITION",
                                                             "DEPARTMENT",
                                                             "LAST_LOGIN",
                                                             "TOTAL_VISITS",
                                                             "MGR_FIRST_NAME",
                                                             "MGR_MIDDLE_NAME",
                                                             "MGR_LAST_NAME",
                                                             "MGR_COUNTRY",
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
     	                                                    "PAX_CHAR_20",
     	                                                    "PAX_CHAR_21",
     	                                                    "PAX_CHAR_22",
     	                                                    "PAX_CHAR_23",
     	                                                    "PAX_CHAR_24",
     	                                                    "PAX_CHAR_25",
     	                                                    "PAX_CHAR_26",
     	                                                    "PAX_CHAR_27",
     	                                                    "PAX_CHAR_28",
     	                                                    "PAX_CHAR_29",
     	                                                    "PAX_CHAR_30",
     	                                                    "PAX_CHAR_31",
     	                                                    "PAX_CHAR_32",
     	                                                    "PAX_CHAR_33",
     	                                                    "PAX_CHAR_34",
     	                                                    "PAX_CHAR_35"};


  protected LoginReportsService getLoginReportsService()
  {
    return (LoginReportsService)getService( LoginReportsService.BEAN_NAME );
  }

  @Override
  protected String getReportExtractCmAssetCode()
  {
    return "report.login.extract";
  }

  protected ReportBeanMethod getReportBeanMethod()
  {
    return ReportBeanMethod.LOGIN_REPORT;
  }

  protected String[] getColumnHeaders()
  {
    return EXTRACT_ALL_COLUMN_NAMES;
  }

  @Override
  protected String getExtractReportData( Map<String, Object> reportParameters )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = getLoginReportsService().getLoginExtractResults( reportParameters );
    buildCSVColumnNames( contentBuf, EXTRACT_ALL_COLUMN_NAMES );
    buildCSVExtractContent( contentBuf, reportExtractData );
    return contentBuf.toString();
  }

  public ActionForward displayBarResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    // Get the map of results
    Map<String, Object> resultsMap = getLoginReportsService().getMonthlyLoginBarResults( reportParameters, false );
    // Extract the resultSet list
    List<LoginReportValue> reportData = (List<LoginReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_login_bar_results" );
  }

  public ActionForward displayUniqueMonthlyResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getLoginReportsService().getMonthlyLoginBarResults( reportParameters, true );
    List<LoginReportValue> reportData = (List<LoginReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_login_bar_results" );
  }

  public ActionForward displayTopPaxLoginsByNameResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    // Get the map of results
    Map<String, Object> resultsMap = getLoginReportsService().getTopPaxLoginsByName( reportParameters );
    // Extract the resultSet list
    List<LoginByPaxReportValue> reportData = (List<LoginByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_top_login_by_pax_results" );
  }

  public ActionForward displayPercentageBarResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    // Get the map of results
    Map<String, Object> resultsMap = getLoginReportsService().getLoginPercentageBarResults( reportParameters );
    // Extract the resultSet list
    List<ReportLogonActivityOrgPercentageValue> reportData = (List<ReportLogonActivityOrgPercentageValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_login_percentage_bar_results" );
  }

  public ActionForward displayOrganizationBarResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    // Get the map of results
    Map<String, Object> resultsMap = getLoginReportsService().getOrganizationBarResults( reportParameters );
    // Extract the resultSet list
    List<LoginByOrganizationReportValue> reportData = (List<LoginByOrganizationReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_login_organization_bar_results" );
  }

  public ActionForward displayLoginStatusChartResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    // Get the map of results
    Map<String, Object> resultsMap = getLoginReportsService().getLoginStatusChartResults( reportParameters );
    // Extract the resultSet list
    List<LoginByTimeReportValue> reportData = (List<LoginByTimeReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_login_timeframe_bar_results" );
  }

  public ActionForward displayParticipantLogonActivityReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    // Get the map of results
    Map<String, Object> resultsMap = getLoginReportsService().getParticipantLogonActivityResults( reportParameters );

    // Extract the results
    Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
    List<ReportParticipantLogonActivityValue> reportData = (List<ReportParticipantLogonActivityValue>)resultsMap.get( OUTPUT_DATA );

    // Add attributes to the request
    request.setAttribute( "maxRows", maxRows );
    request.setAttribute( "reportData", reportData );
    request.setAttribute( "refreshDate", DateUtils.toDisplayTimeWithMeridiemString( DateUtils.getCurrentDate() ) );
    return mapping.findForward( "display_login_pax_detail" );
  }

  @Override
  protected void populateForm( ReportParametersForm form, HttpServletRequest request )
  {
    super.populateForm( form, request );
    form.setPaxId( UserManager.getUserId() );
  }

}


package com.biperf.core.ui.reports.login;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.loginreport.ReportLogonActivityListOfParticipantsValue;
import com.biperf.core.value.loginreport.ReportLogonActivityTopLevelValue;

public class LoginActivityByOrgReportAction extends LoginReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.LOGIN_ACTIVITY;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParametersForm.setUserId( null );

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> resultsMap = getLoginReportsService().getOrgLoginActivityTopLevelResults( reportParameters );

      Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        ReportLogonActivityTopLevelValue totalsRowData = (ReportLogonActivityTopLevelValue)resultsMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
      List<ReportLogonActivityTopLevelValue> reportData = (List<ReportLogonActivityTopLevelValue>)resultsMap.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );
      request.setAttribute( "refreshDate", DateUtils.toDisplayTimeWithMeridiemString( DateUtils.getCurrentDate() ) );
    }
    return mapping.findForward( "display_login_organization_summary" );
  }

  public ActionForward displayParticipantList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    reportParametersForm.setUserId( null );

    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    Map<String, Object> resultsMap = getLoginReportsService().getLoginListOfParticipantsResults( reportParameters, false );

    Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      Integer totalCnt = (Integer)resultsMap.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalCnt", totalCnt );
    }

    List<ReportLogonActivityListOfParticipantsValue> reportData = (List<ReportLogonActivityListOfParticipantsValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    request.setAttribute( "refreshDate", DateUtils.toDisplayTimeWithMeridiemString( DateUtils.getCurrentDate() ) );
    return mapping.findForward( "display_login_org_pax_list" );
  }
}

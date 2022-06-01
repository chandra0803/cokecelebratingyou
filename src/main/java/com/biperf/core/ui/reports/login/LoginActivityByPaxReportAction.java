
package com.biperf.core.ui.reports.login;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.LoginActivityType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.loginreport.ReportLogonActivityListOfParticipantsValue;

public class LoginActivityByPaxReportAction extends LoginReportAction
{

  @Override
  protected String getReportCode()
  {
    return Report.LOGIN_ACTIVITY_BY_PAX;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException, ParseException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    reportParametersForm.setUserId( null );
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      if ( !isHaveNotFilterSelected( reportParameters, request ) )
      {
        Map<String, Object> resultsMap = getLoginReportsService().getLoginListOfParticipantsResults( reportParameters, true );

        Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );
        request.setAttribute( "maxRows", maxRows );

        if ( isLastPage( maxRows, reportParameters ) )
        {
          Integer totalCnt = (Integer)resultsMap.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalCnt", totalCnt );
        }

        List<ReportLogonActivityListOfParticipantsValue> reportData = (List<ReportLogonActivityListOfParticipantsValue>)resultsMap.get( OUTPUT_DATA );
        request.setAttribute( "reportData", reportData );
      }
      request.setAttribute( "refreshDate", DateUtils.toDisplayTimeWithMeridiemString( DateUtils.getCurrentDate() ) );// (DateUtils.toDisplayString(DateUtils.getCurrentDate());
    }
    return mapping.findForward( "display_login_pax_summary" );
  }

  private boolean isHaveNotFilterSelected( Map<String, Object> reportParameters, HttpServletRequest request )
  {
    boolean isHaveNotFilterSelected = false;
    if ( LoginActivityType.HAVE_NOT_LOGGED_IN.equals( reportParameters.get( "loginType" ) ) )
    {
      isHaveNotFilterSelected = true;
      request.setAttribute( "displayHaveNotFilterExportMessage", isHaveNotFilterSelected );
      request.setAttribute( "hideCharts", Boolean.TRUE );
      request.setAttribute( "maxRows", 1 );
    }
    return isHaveNotFilterSelected;
  }

}

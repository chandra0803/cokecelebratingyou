
package com.biperf.core.ui.reports.nomination;

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
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.NominationGivenByOrgReportValue;

public class NominationGivenByOrgReportAction extends NominationReportAction
{

  @Override
  protected String getReportCode()
  {
    return Report.NOMINATION_GIVEN_BY_ORG;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    Integer maxRows = 0;
    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getNominationReportsService().getNominationsGivenByOrgSummaryTabularResults( reportParameters );
      List<NominationGivenByOrgReportValue> reportData = (List<NominationGivenByOrgReportValue>)output.get( OUTPUT_DATA );

      if ( reportData != null && reportData.size() > 0 )
      {
        maxRows = reportData.isEmpty() ? 0 : reportData.get( 0 ).getTotalRecords().intValue();
      }

      if ( isLastPage( maxRows, reportParameters ) )
      {
        NominationGivenByOrgReportValue totalsRowData = (NominationGivenByOrgReportValue)output.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      String currencyCode = "(" + UserManager.getUserPrimaryCountryCurrencyCode() + ")";
      request.setAttribute( "currencyCode", currencyCode );
      request.setAttribute( "maxRows", maxRows );
      request.setAttribute( "reportData", reportData );
    }
    return mapping.findForward( "display_summary_report" );
  }

  public ActionForward displayNominatorsByOrgBarChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getNominatorsByOrgChartResults( reportParameters );
    List<NominationGivenByOrgReportValue> reportData = (List<NominationGivenByOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_nominators_report" );
  }

  public ActionForward displayNomineesByOrgBarChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getNomineesByOrgChartResults( reportParameters );
    List<NominationGivenByOrgReportValue> reportData = (List<NominationGivenByOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_nominees_report" );
  }

  public ActionForward displayNominationsByMonthBarChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> output = getNominationReportsService().getNominationsByMonthChartResults( reportParameters );
    List<NominationGivenByOrgReportValue> reportData = (List<NominationGivenByOrgReportValue>)output.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_nominatons_by_month" );
  }

}

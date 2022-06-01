
package com.biperf.core.ui.reports.claim;

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
import com.biperf.core.value.claim.ClaimByOrgReportValue;
import com.biperf.core.value.claim.ClaimByPaxReportValue;

public class ClaimByOrgReportAction extends ClaimReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.CLAIM_BY_ORG;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgTabularResults( reportParameters );
      List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
      request.setAttribute( "reportData", reportData );

      Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );

      if ( isLastPage( maxRows, reportParameters ) )
      {
        ClaimByOrgReportValue totalsRowData = (ClaimByOrgReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
        request.setAttribute( "totalsRowData", totalsRowData );
      }

      request.setAttribute( "maxRows", maxRows );
      request.setAttribute( "reportDetailType", "orgLevel" );
    }
    return mapping.findForward( "display_claim_by_org_summary" );
  }

  public ActionForward displayClaimByPaxSummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByPaxTabularResults( reportParameters, false );
    List<ClaimByPaxReportValue> reportData = (List<ClaimByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      ClaimByPaxReportValue totalsRowData = (ClaimByPaxReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "maxRows", maxRows );
    request.setAttribute( "reportDetailType", "paxLevel" );
    return mapping.findForward( "display_claim_by_org_pax_summary" );
  }

  public ActionForward displayClaimListSummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    reportParameters.put( "promotionId", reportParametersForm.getDrilldownPromoId() );

    Map<String, Object> resultsMap = getClaimReportsService().getClaimByPaxClaimListTabularResults( reportParameters );

    List<ClaimByPaxReportValue> reportData = (List<ClaimByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );

    if ( isLastPage( maxRows, reportParameters ) )
    {
      ClaimByPaxReportValue totalsRowData = (ClaimByPaxReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
      request.setAttribute( "totalsRowData", totalsRowData );
    }
    reportParametersForm.setDrilldownPromoId( null );
    request.setAttribute( "maxRows", maxRows );
    request.setAttribute( "reportDetailType", "claimList" );
    return mapping.findForward( "display_claim_by_org_claim_summary" );
  }

  public ActionForward displayParticipationRateChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgParticipationRateChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_org_participation_rate" );
  }

  public ActionForward displayParticipationLevelChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgParticipationLevelChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_org_participation_level" );
  }

  public ActionForward displayMonthlyChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgMonthlyChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_org_monthly" );
  }

  public ActionForward displaySubmissionStatusChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgSubmissionStatusChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_org_submission_status" );
  }

  public ActionForward displayItemStatusChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgItemStatusChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_item_submission_status" );
  }

  public ActionForward displayTotalsChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgTotalsChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_TOTALS_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_totals" );
  }
}

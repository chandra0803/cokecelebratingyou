
package com.biperf.core.ui.reports.claim;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.SubmittedType;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.claim.ClaimByOrgReportValue;
import com.biperf.core.value.claim.ClaimByPaxReportValue;

public class ClaimByPaxReportAction extends ClaimReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.CLAIM_BY_PAX;
  }

  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      if ( !isHaveNotFilterSelected( reportParameters, request ) )
      {
        Map<String, Object> resultsMap = getClaimReportsService().getClaimByPaxTabularResults( reportParameters, true );
        List<ClaimByPaxReportValue> reportData = (List<ClaimByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
        request.setAttribute( "reportData", reportData );

        Integer maxRows = (Integer)resultsMap.get( OUTPUT_SIZE_DATA );

        if ( isLastPage( maxRows, reportParameters ) )
        {
          ClaimByPaxReportValue totalsRowData = (ClaimByPaxReportValue)resultsMap.get( OUTPUT_TOTALS_DATA );
          request.setAttribute( "totalsRowData", totalsRowData );
        }

        request.setAttribute( "maxRows", maxRows );
      }
      request.setAttribute( "reportDetailType", "summary" );
    }
    return mapping.findForward( "display_claim_by_pax_summary" );
  }

  private boolean isHaveNotFilterSelected( Map<String, Object> reportParameters, HttpServletRequest request )
  {
    boolean isHaveNotFilterSelected = false;
    if ( SubmittedType.NOT_SUBMITTED.equals( reportParameters.get( "submittedType" ) ) )
    {
      isHaveNotFilterSelected = true;
      request.setAttribute( "displayHaveNotFilterExportMessage", isHaveNotFilterSelected );
      request.setAttribute( "hideCharts", Boolean.TRUE );
      request.setAttribute( "maxRows", 1 );
    }
    return isHaveNotFilterSelected;
  }

  public ActionForward displayClaimListSummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    populateDrilldownPromoId( request, reportParameters, reportParametersForm.getDrilldownPromoId() );
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
    return mapping.findForward( "display_claim_list_summary" );
  }

  private void populateDrilldownPromoId( HttpServletRequest request, Map<String, Object> reportParameters, String drilldownPromoId )
  {
    reportParameters.put( "promotionId", drilldownPromoId );
    if ( !StringUtil.isValid( drilldownPromoId ) )
    {
      drilldownPromoId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "drillDownPromotionId" );
      reportParameters.put( "promotionId", drilldownPromoId );
    }

    request.setAttribute( "drillDownPromotionId", drilldownPromoId );
  }

  public ActionForward displayParticipationRateChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgParticipationRateChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_pax_participation_rate" );
  }

  public ActionForward displayParticipationLevelChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByOrgParticipationLevelChart( reportParameters );
    List<ClaimByOrgReportValue> reportData = (List<ClaimByOrgReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_claim_by_pax_participation_level" );
  }

  public ActionForward displayClaimsSubmittedVsAvgChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByPaxSubmittedClaims( reportParameters );
    BigDecimal averageClaims = (BigDecimal)resultsMap.get( "p_out_average_data" );
    request.setAttribute( "averageClaims", averageClaims );

    List<ClaimByPaxReportValue> reportData = (List<ClaimByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_claim_by_pax_average_claims" );
  }

  public ActionForward displayClaimsStatusChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByPaxClaimStatusChart( reportParameters );
    List<ClaimByPaxReportValue> reportData = (List<ClaimByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_claim_by_pax_claim_status" );
  }

  public ActionForward displayItemStatusChart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    Map<String, Object> resultsMap = getClaimReportsService().getClaimByPaxItemStatusChart( reportParameters );
    List<ClaimByPaxReportValue> reportData = (List<ClaimByPaxReportValue>)resultsMap.get( OUTPUT_DATA );
    request.setAttribute( "reportData", reportData );

    return mapping.findForward( "display_claim_by_pax_item_status" );
  }
}

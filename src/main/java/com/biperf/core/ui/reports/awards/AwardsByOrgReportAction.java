/**
 * 
 */

package com.biperf.core.ui.reports.awards;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.report.Report;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.reports.ReportParametersForm;
import com.biperf.core.value.awardsreport.AwardsFirstDetailReportValue;
import com.biperf.core.value.awardsreport.AwardsSecondDetailReportValue;
import com.biperf.core.value.awardsreport.AwardsSummaryReportValue;
import com.biperf.core.value.awardsreport.PersonsReceivingAwardsForOrgReportValue;
import com.biperf.core.value.awardsreport.ReceivedNotReceivedAwardsForOrgReportValue;
import com.biperf.core.value.awardsreport.TotalPointsIssuedForOrgReportValue;

/**
 * @author poddutur
 *
 */
public class AwardsByOrgReportAction extends AwardsReportAction
{
  @Override
  protected String getReportCode()
  {
    return Report.AWARDS_BY_ORG;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displaySummaryReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );
    ReportParametersForm reportParametersForm = (ReportParametersForm)form;

    // bugzilla bug#56590 fix
    reportParametersForm.setUserId( null );

    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();
    buildOnTheSpotReportParameter( reportParameters );

    request.setAttribute( "awardType", PromotionAwardsType.lookup( (String)reportParameters.get( "awardType" ) ).getName() );

    if ( !hasErrorMessages( request, reportParameters, null, false ) )
    {
      Map<String, Object> output = getAwardsReportsService().getAwardsSummaryResults( reportParameters );

      List<AwardsSummaryReportValue> reportData = (List<AwardsSummaryReportValue>)output.get( "p_out_rs_getAwdSmryResult" );

      request.setAttribute( "reportData", reportData );

      Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
      request.setAttribute( "maxRows", maxRows );

      if ( isLastPage( maxRows.intValue(), reportParameters ) )
      {
        AwardsSummaryReportValue totalsRowData = (AwardsSummaryReportValue)output.get( "p_out_rs_getAwdSmryResultTot" );
        request.setAttribute( "totalsRowData", totalsRowData );
      }
    }
    return mapping.findForward( "display_awards_summary_report" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayAwardsFirstDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    // bugzilla bug#56590 fix
    reportParametersForm.setUserId( null );
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    buildOnTheSpotReportParameter( reportParameters );

    request.setAttribute( "awardType", PromotionAwardsType.lookup( (String)reportParameters.get( "awardType" ) ).getName() );

    Map<String, Object> output = getAwardsReportsService().getAwardsFirstDetailResults( reportParameters );

    List<AwardsFirstDetailReportValue> reportData = (List<AwardsFirstDetailReportValue>)output.get( "p_out_rs_getAwdfirdtlresult" );

    request.setAttribute( "reportData", reportData );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows.intValue(), reportParameters ) )
    {
      AwardsFirstDetailReportValue totalsRowData = (AwardsFirstDetailReportValue)output.get( "p_out_rs_getAwdfirdtlResultTot" );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    return mapping.findForward( "display_awards_first_detail_report" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayAwardsSecondDetailReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    super.prepareDisplay( mapping, form, request, response );

    ReportParametersForm reportParametersForm = (ReportParametersForm)form;
    Map<String, Object> reportParameters = reportParametersForm.getReportParameters();

    buildOnTheSpotReportParameter( reportParameters );

    String promotionId = (String)reportParameters.get( "promotionId" );

    if ( promotionId != null && !promotionId.equals( "0" ) )
    {
      // navigate through the promotion ids to find any ssi promotion.
      // If it exists then set the drilldownpromoid(contest id) as promotionid. Bugfix # 69529

      String[] promotionIds = promotionId.split( "," );
      for ( String promotionItem : promotionIds )
      {
        // Is onTheSpot promotion selected
        // For on the spot the promotion id is equal to -1
        Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( promotionItem ) );

        if ( promotion != null && promotion.getPromotionType().isSelfServIncentivesPromotion() )
        {
          reportParameters.put( "promotionId", reportParameters.get( "drilldownPromoId" ) );
        }
        else if ( promotion != null && promotion.getPromotionType().isBadgePromotion() )
        {
          if ( reportParameters.get( "drilldownPromoId" ) != null )
          {
            reportParameters.put( "promotionId", reportParameters.get( "drilldownPromoId" ) );
          }
        }
      }
    }

    request.setAttribute( "awardType", PromotionAwardsType.lookup( (String)reportParameters.get( "awardType" ) ).getName() );

    Map<String, Object> output = getAwardsReportsService().getAwardsSecondDetailResults( reportParameters );

    List<AwardsSecondDetailReportValue> reportData = (List<AwardsSecondDetailReportValue>)output.get( "p_out_rs_getAwdsecdtlresult" );

    request.setAttribute( "reportData", reportData );

    Long maxRows = reportData.isEmpty() ? 0L : reportData.get( 0 ).getTotalRecords();
    request.setAttribute( "maxRows", maxRows );

    if ( isLastPage( maxRows.intValue(), reportParameters ) )
    {
      AwardsSecondDetailReportValue totalsRowData = (AwardsSecondDetailReportValue)output.get( "p_out_rs_getAwdsecdtlResultTot" );
      request.setAttribute( "totalsRowData", totalsRowData );
    }

    request.setAttribute( "drilldownPromoId", reportParametersForm.getDrilldownPromoId() );

    reportParametersForm.setDrilldownPromoId( null );

    return mapping.findForward( "display_awards_second_detail_report" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayTotalPointsIssuedForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    buildOnTheSpotReportParameter( reportParameters );
    Map<String, Object> output = getAwardsReportsService().getTotalPointsIssuedForOrgBarResults( reportParameters );
    List<TotalPointsIssuedForOrgReportValue> reportData = (List<TotalPointsIssuedForOrgReportValue>)output.get( OUTPUT_RESULT_SET );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_total_points_issued_for_org_barchart" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayReceivedNotReceivedAwardsForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    buildOnTheSpotReportParameter( reportParameters );
    Map<String, Object> output = getAwardsReportsService().getReceivedNotReceivedAwardsForOrgBarResults( reportParameters );
    List<ReceivedNotReceivedAwardsForOrgReportValue> reportData = (List<ReceivedNotReceivedAwardsForOrgReportValue>)output.get( OUTPUT_RESULT_SET );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_received_notreceived_awards_for_org_barchart" );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward displayPersonsReceivingAwardsForOrgBarchart( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    Map<String, Object> reportParameters = prepareChartDisplay( mapping, form, request, response );
    buildOnTheSpotReportParameter( reportParameters );
    Map<String, Object> output = getAwardsReportsService().getPersonsReceivingAwardsForOrgBarResults( reportParameters );
    List<PersonsReceivingAwardsForOrgReportValue> reportData = (List<PersonsReceivingAwardsForOrgReportValue>)output.get( OUTPUT_RESULT_SET );
    request.setAttribute( "reportData", reportData );
    return mapping.findForward( "display_persons_receiving_awards_for_org_barchart" );
  }

  @Override
  protected String[] getColumnHeaders()
  {
    return null;
  }

}

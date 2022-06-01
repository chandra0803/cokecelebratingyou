
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestATNSummaryAwardHistoryView;
import com.biperf.core.ui.ssi.view.SSIContestSummaryATNView;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestAwardHistoryValueBean;

/**
 * 
 *  SSIContestSummaryAwardThemNowAction.
 * 
 * @author kandhi
 * @since Feb 11, 2015
 * @version 1.0
 */
public class SSIContestSummaryAwardThemNowAction extends SSIContestBaseAction
{
  /**
   * Load the summary page boot strap for award them now contest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestSummaryAwardThemNowForm summaryForm = (SSIContestSummaryAwardThemNowForm)form;

    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, summaryForm.getContestId(), decode );

    // Get the next issuance number for this contest
    SSIContest contest = getSSIContestAwardThemNowService().getContestById( contestId );

    // Get the total participants count for this contest including all issuances
    int participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contest.getId() );

    // Get the totals value bean for the contest for all the issuances
    SSIContestAwardHistoryTotalsValueBean totalsBean = getSSIContestAwardThemNowService().getContestAwardTotals( contestId );

    SSIContestSummaryATNView pageViewBean = new SSIContestSummaryATNView( contest, getContestValueBean( contest, participantsCount ), summaryForm.getSortedOn(), summaryForm.getSortedBy() );

    String activityMeasureCurrencyCode = getActivityPrefix( contest );
    String payoutOtherCurrencyCode = getPayoutPrefix( contest );

    pageViewBean.setTotalValues( totalsBean, participantsCount, contest, activityMeasureCurrencyCode, payoutOtherCurrencyCode );
    summaryForm.setInitializationJson( toJson( pageViewBean ) );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /**
   * Load the summary of all the issuances for this contest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadIssuances( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestSummaryAwardThemNowForm summaryForm = (SSIContestSummaryAwardThemNowForm)form;
    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, summaryForm.getContestId(), decode );
    try
    {
      Map<String, Object> outParams = getSSIContestAwardThemNowService()
          .getAllIssuancesForContest( contestId, summaryForm.getPage(), SSIContestATNSummaryAwardHistoryView.RECORDS_PER_PAGE, summaryForm.getSortedOn(), summaryForm.getSortedBy() );
      List<SSIContestAwardHistoryValueBean> awardHistoryBeans = (ArrayList<SSIContestAwardHistoryValueBean>)outParams.get( "p_out_ref_cursor" );
      generateClientStateIds( request, awardHistoryBeans, contestId );
      int totalRecords = (Integer)outParams.get( "p_out_issuance_count" );
      SSIContest contest = getSSIContestAwardThemNowService().getContestById( contestId );

      String activityMeasureCurrencyCode = getActivityPrefix( contest );
      String payoutOtherCurrencyCode = getPayoutPrefix( contest );

      SSIContestATNSummaryAwardHistoryView responseView = new SSIContestATNSummaryAwardHistoryView( summaryForm.getPage(),
                                                                                                    awardHistoryBeans,
                                                                                                    totalRecords,
                                                                                                    contest,
                                                                                                    activityMeasureCurrencyCode,
                                                                                                    payoutOtherCurrencyCode );
      super.writeAsJsonToResponse( responseView, response );
    }
    catch( ServiceErrorException see )
    {
      SSIContestATNSummaryAwardHistoryView responseView = new SSIContestATNSummaryAwardHistoryView( addServiceException( see ) );
      super.writeAsJsonToResponse( responseView, response );
    }
    return null;
  }

  private void generateClientStateIds( HttpServletRequest request, List<SSIContestAwardHistoryValueBean> awardHistoryBeans, Long contestId )
  {
    for ( SSIContestAwardHistoryValueBean awardHistoryBean : awardHistoryBeans )
    {
      awardHistoryBean.setClientStateId( SSIContestUtil.getClientState( request, contestId, awardHistoryBean.getIssuanceNumber(), awardHistoryBean.getApprovalLevelActionTaken(), true ) );
    }
  }

}

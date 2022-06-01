
package com.biperf.core.ui.ssi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.ssi.view.SSIContestSummaryMasterView;
import com.biperf.core.ui.ssi.view.SSIStackRankTableViewBean;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestSummaryValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * 
 * SSIContestDetailsBaseAction
 * 
 * @author chowdhur
 * @since Jan 14, 2015
 */
public class SSIContestDetailsBaseAction extends SSIContestBaseAction
{
  private static final String TEAM = "team";
  private static final Map<String, String> contestResultsSortedOnMap = new HashMap<String, String>();

  static
  {
    // general sort columns starts
    contestResultsSortedOnMap.put( "participants", "last_name" );
    contestResultsSortedOnMap.put( "orgUnit", "org_name" );
    contestResultsSortedOnMap.put( "currentActivity", "activity_amt" );
    // general sort columns ends

    // objective sort columns starts
    contestResultsSortedOnMap.put( "objective", "objective_amount" );
    contestResultsSortedOnMap.put( "percentage", "perc_achieved" );
    contestResultsSortedOnMap.put( "objectivePayout", "objective_payout" );
    contestResultsSortedOnMap.put( "bonusPayout", "objective_bonus_payout" );
    contestResultsSortedOnMap.put( "payoutQuantity", "objective_payout" );
    contestResultsSortedOnMap.put( "payoutValue", "objective_bonus_payout" );
    contestResultsSortedOnMap.put( "payoutDescription", "OBJECTIVE_PAYOUT_DESCRIPTION" );
    contestResultsSortedOnMap.put( "totalPotentialPayout", "total_payout" );
    // objective sort columns ends

    // dtgt sort columns starts
    contestResultsSortedOnMap.put( "activityDescriptionCol1_potentialpoints", "payout_value1" );
    contestResultsSortedOnMap.put( "activityDescriptionCol1_activity", "activity_amt1" );
    contestResultsSortedOnMap.put( "activityDescriptionCol1_payoutvalue", "payout_quantity1" );

    contestResultsSortedOnMap.put( "activityDescriptionCol2_potentialpoints", "payout_value2" );
    contestResultsSortedOnMap.put( "activityDescriptionCol2_activity", "activity_amt2" );
    contestResultsSortedOnMap.put( "activityDescriptionCol2_payoutvalue", "payout_quantity2" );

    contestResultsSortedOnMap.put( "activityDescriptionCol3_potentialpoints", "payout_value3" );
    contestResultsSortedOnMap.put( "activityDescriptionCol3_activity", "activity_amt3" );
    contestResultsSortedOnMap.put( "activityDescriptionCol3_payoutvalue", "payout_quantity3" );

    contestResultsSortedOnMap.put( "activityDescriptionCol4_potentialpoints", "payout_value4" );
    contestResultsSortedOnMap.put( "activityDescriptionCol4_activity", "activity_amt4" );
    contestResultsSortedOnMap.put( "activityDescriptionCol4_payoutvalue", "payout_quantity4" );

    contestResultsSortedOnMap.put( "activityDescriptionCol5_potentialpoints", "payout_value5" );
    contestResultsSortedOnMap.put( "activityDescriptionCol5_activity", "activity_amt5" );
    contestResultsSortedOnMap.put( "activityDescriptionCol5_payoutvalue", "payout_quantity5" );

    contestResultsSortedOnMap.put( "activityDescriptionCol6_potentialpoints", "payout_value6" );
    contestResultsSortedOnMap.put( "activityDescriptionCol6_activity", "activity_amt6" );
    contestResultsSortedOnMap.put( "activityDescriptionCol6_payoutvalue", "payout_quantity6" );

    contestResultsSortedOnMap.put( "activityDescriptionCol7_potentialpoints", "payout_value7" );
    contestResultsSortedOnMap.put( "activityDescriptionCol7_activity", "activity_amt7" );
    contestResultsSortedOnMap.put( "activityDescriptionCol7_payoutvalue", "payout_quantity7" );

    contestResultsSortedOnMap.put( "activityDescriptionCol8_potentialpoints", "payout_value8" );
    contestResultsSortedOnMap.put( "activityDescriptionCol8_activity", "activity_amt8" );
    contestResultsSortedOnMap.put( "activityDescriptionCol8_payoutvalue", "payout_quantity8" );

    contestResultsSortedOnMap.put( "activityDescriptionCol9_potentialpoints", "payout_value9" );
    contestResultsSortedOnMap.put( "activityDescriptionCol9_activity", "activity_amt9" );
    contestResultsSortedOnMap.put( "activityDescriptionCol9_payoutvalue", "payout_quantity9" );

    contestResultsSortedOnMap.put( "activityDescriptionCol10_potentialpoints", "payout_value10" );
    contestResultsSortedOnMap.put( "activityDescriptionCol10_activity", "activity_amt10" );
    contestResultsSortedOnMap.put( "activityDescriptionCol10_payoutvalue", "payout_quantity10" );

    contestResultsSortedOnMap.put( "totalPointsIssued", "total_payout_value" );
    // dtgt sort columns ends

    // step it up sort columns starts
    contestResultsSortedOnMap.put( "levelCompleted", "level_completed" );
    contestResultsSortedOnMap.put( "baseline", "SIU_BASELINE_AMOUNT" );
    contestResultsSortedOnMap.put( "levelPayout", "level_payout" );
    contestResultsSortedOnMap.put( "bonusPayoutSiu", "bonus_payout" );
    contestResultsSortedOnMap.put( "totalPayoutSiu", "total_payout" );
    contestResultsSortedOnMap.put( "payoutDescriptionSiu", "PAYOUT_DESCRIPTION" );
    contestResultsSortedOnMap.put( "payoutValueLabel", "total_payout" );
    // step it up sort columns ends

  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException 
   * @throws Exception
   */
  protected ActionForward fetchContestSummaryTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    SSIContestSummaryValueBean detailValueBean = null;
    Long contestId = getContestIdFromClientState( ssiContestListForm.getId(), request );
    String sortBy = getSortBy( ssiContestListForm );
    String sortColumnName = getSortColumnName( ssiContestListForm );
    int pageNumber = getPageNumber( ssiContestListForm );
    SSIContestSummaryMasterView responseView = null;
    try
    {
      detailValueBean = getSSIContestService().getParticipantsSummaryData( contestId, ssiContestListForm.getUserId(), sortBy, sortColumnName, pageNumber, SSIContestUtil.CONTEST_DETAIL_PER_PAGE );
      detailValueBean.setContestId( contestId );
      responseView = new SSIContestSummaryMasterView( detailValueBean );
      responseView.addPaginationParams( sortBy, getRequestSortColumnName( ssiContestListForm ), pageNumber, SSIContestUtil.CONTEST_DETAIL_PER_PAGE );
      responseView.getParticipants().setRole( ssiContestListForm.getRole() );
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIContestSummaryMasterView( addServiceException( see ) );
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  protected ActionForward fetchStackRankTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), decode );
    int current = getPageNumber( ssiContestListForm );
    Long userId = ssiContestListForm.getUserId();
    boolean isTeam = isTeam( ssiContestListForm );

    Long activityId = ssiContestListForm.getActivityId();
    SSIContest contest = getSSIContestService().getContestById( contestId );
    boolean isIncludeAll = ! ( contest.getContestType().isStackRank() && contest.getStatus().isFinalizeResults() );
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    SSIContestValueBean valueBean = getContestValueBean( contest, 0 );
    SSIStackRankTableViewBean responseView = null;
    try
    {
      List<SSIContestStackRankPaxValueBean> stackRanks = getSSIContestParticipantService().getContestStackRank( contestId,
                                                                                                                userId,
                                                                                                                activityId,
                                                                                                                current,
                                                                                                                SSIContestUtil.STACK_RANK_PER_PAGE,
                                                                                                                isTeam,
                                                                                                                isIncludeAll );
      responseView = new SSIStackRankTableViewBean( stackRanks, ssiContestListForm.getRole(), precision, valueBean, contestId );
      responseView.addPaginationParams( stackRanks.size() > 0 ? stackRanks.get( 0 ).getParticipantsCount() : 0, SSIContestUtil.STACK_RANK_PER_PAGE, current );
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIStackRankTableViewBean( addServiceException( see ) );
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private boolean isTeam( SSIContestListForm form )
  {
    return SSIContest.CONTEST_ROLE_MGR.equals( form.getRole() ) && TEAM.equals( form.getFilter() );
  }

  private int getPageNumber( SSIContestListForm ssiContestListForm )
  {
    return ssiContestListForm.getPage() == 0 ? 1 : ssiContestListForm.getPage();
  }

  private String getSortBy( SSIContestListForm ssiContestListForm )
  {
    if ( StringUtil.isNullOrEmpty( ssiContestListForm.getSortedBy() ) )
    {
      return SSIContestUtil.DEFAULT_SORT_BY;
    }
    else
    {
      return ssiContestListForm.getSortedBy();
    }
  }

  private String getSortColumnName( SSIContestListForm ssiContestListForm )
  {
    if ( StringUtil.isNullOrEmpty( ssiContestListForm.getSortedOn() ) )
    {
      return "last_name";
    }
    else
    {
      return contestResultsSortedOnMap.get( ssiContestListForm.getSortedOn() );
    }
  }

  private String getRequestSortColumnName( SSIContestListForm contestListForm )
  {
    if ( StringUtil.isNullOrEmpty( contestListForm.getSortedOn() ) )
    {
      return "participants";
    }
    else
    {
      return contestListForm.getSortedOn();
    }
  }

  protected Long getContestIdFromClientState( String contestId, HttpServletRequest request ) throws InvalidClientStateException
  {
    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    return SSIContestUtil.getContestIdFromClientState( request, contestId, decode );
  }

  /**
   * Verifies whether user should see the detail. if use coming from email link or via the tile or any otehr source
   * @param userId
   * @return
   */
  protected boolean displayDetail( Long userId )
  {
    if ( userId != null && UserManager.getUserId().equals( userId ) )
    {
      return true;
    }
    return false;
  }

}

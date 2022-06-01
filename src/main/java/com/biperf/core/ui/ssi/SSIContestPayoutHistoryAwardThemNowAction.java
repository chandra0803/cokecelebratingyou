
package com.biperf.core.ui.ssi;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.SSIContestIssuanceStatusType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestApprovalLevelsView;
import com.biperf.core.ui.ssi.view.SSIContestApprovalLevelsView.SSIContestApprover;
import com.biperf.core.ui.ssi.view.SSIContestParticipantATNView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutHistoryAwardThemNowValueBean;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestSummaryAwardThemNowForm.
 * 
 * @author patelp
 * @since Feb 17, 2015
 * @version 1.0
 */

public class SSIContestPayoutHistoryAwardThemNowAction extends SSIContestCreateBaseAction
{

  private static final Integer CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL = 1;
  private static final Integer CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL = 2;
  public static final String WAITING_FOR_APPROVAL = "waiting_for_approval";
  private static final String ACTIVITY_AMOUNT = "activityAmount";
  private static final String PAYOUT_DESCRIPTION = "payoutDescription";
  private static final String PAYOUT_VALUE = "payoutValue";
  private static final String OBJECTIVE_AMOUNT = "objectiveAmount";
  private static final String OBJECTIVE_PAYOUT_DESC = "objectivePayoutDescription";
  private static final String OBJECTIVE_PAYOUT = "objectivePayout";

  /**
   * Load the summary page boot strap for award them now contest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward displayPayoutHistory( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    SSIContestPayoutHistoryAwardThemNowForm contestPayoutHistoryAwardThemNowForm = (SSIContestPayoutHistoryAwardThemNowForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    Short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    String isApproveMode = request.getParameter( "isApproveMode" );

    SSIContest ssiContest = getSSIContestAwardThemNowService().getContestByIdWithAssociations( contestId, getAssociationRequestCollection() );
    SSIContestAwardThemNow contestAwardThemNow = getSSIContestAwardThemNowService().getContestAwardThemNowByIdAndIssunace( contestId, awardIssuanceNumber );
    // Get the totals value bean for the contest for all the issuances
    SSIContestPayoutObjectivesTotalsValueBean payoutObjectivesTotalsValueBean = getSSIContestAwardThemNowService().calculatePayoutObjectivesTotals( contestId, awardIssuanceNumber );

    Map<String, Set<Participant>> contestApprovers = getSSIContestService().getSelectedContestApprovers( contestId );

    SSIContestPayoutHistoryAwardThemNowValueBean contestJson = populatePayoutHistoryAwardThemNowValueBean( request,
                                                                                                           ssiContest,
                                                                                                           contestAwardThemNow,
                                                                                                           payoutObjectivesTotalsValueBean,
                                                                                                           awardIssuanceNumber,
                                                                                                           isApproveMode,
                                                                                                           contestApprovers );

    contestPayoutHistoryAwardThemNowForm.setInitializationJson( toJson( contestJson ) );
    contestPayoutHistoryAwardThemNowForm.setId( contestJson.getId() );// temporary changes
    contestPayoutHistoryAwardThemNowForm.setBackButtonUrl( contestJson.getBackButtonUrl() );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private SSIContestPayoutHistoryAwardThemNowValueBean populatePayoutHistoryAwardThemNowValueBean( HttpServletRequest request,
                                                                                                   SSIContest ssiContest,
                                                                                                   SSIContestAwardThemNow contestAwardThemNow,
                                                                                                   SSIContestPayoutObjectivesTotalsValueBean payoutObjectivesTotalsValueBean,
                                                                                                   Short awardIssuanceNumber,
                                                                                                   String isApproveMode,
                                                                                                   Map<String, Set<Participant>> contestApprovers )
  {

    SSIContestPayoutHistoryAwardThemNowValueBean contestPayoutHistoryAwardThemNowValueBean = new SSIContestPayoutHistoryAwardThemNowValueBean();
    contestPayoutHistoryAwardThemNowValueBean.setName( ssiContest.getContestNameFromCM() );
    contestPayoutHistoryAwardThemNowValueBean.setPayoutType( ssiContest.getPayoutType().getCode() );
    if ( contestAwardThemNow.getIssuanceStatusType() != null )
    {

      if ( isFinalizeResult( contestAwardThemNow.getIssuanceStatusType().getCode() ) )
      {
        contestPayoutHistoryAwardThemNowValueBean.setStatus( SSIContestIssuanceStatusType.lookup( "approved" ).getName() );
        contestPayoutHistoryAwardThemNowValueBean.setStatusDescription( SSIContestIssuanceStatusType.lookup( "approved" ).getName() );

      }
      else
      {
        contestPayoutHistoryAwardThemNowValueBean.setStatus( contestAwardThemNow.getIssuanceStatusType().getCode() );
        contestPayoutHistoryAwardThemNowValueBean.setStatusDescription( contestAwardThemNow.getIssuanceStatusType().getName() );

      }
    }
    if ( ssiContest.getActivityMeasureType().isCurrency() )
    {
      contestPayoutHistoryAwardThemNowValueBean.setTotalActivity( payoutObjectivesTotalsValueBean.getObjectiveAmountTotal() != null
          ? NumberFormatUtil.getLocaleBasedCurrencyFormat( payoutObjectivesTotalsValueBean.getObjectiveAmountTotal(), 2 )
          : null );
    }
    else
    {
      contestPayoutHistoryAwardThemNowValueBean.setTotalActivity( NumberFormatUtil.getLocaleBasedDobleNumberFormat( payoutObjectivesTotalsValueBean.getObjectiveAmountTotal() ) );
    }

    if ( ssiContest.getPayoutType() != null && ssiContest.getPayoutType().isOther() && ssiContest.getPayoutOtherCurrencyCode() != null )
    {
      contestPayoutHistoryAwardThemNowValueBean.setTotalPayoutAmount( payoutObjectivesTotalsValueBean.getObjectivePayoutTotal() != null
          ? NumberFormatUtil.getLocaleBasedCurrencyFormat( payoutObjectivesTotalsValueBean.getObjectivePayoutTotal(), 2 )
          : null );

    }
    else
    {
      contestPayoutHistoryAwardThemNowValueBean.setTotalPayoutAmount( NumberFormatUtil.getLocaleBasedNumberFormat( payoutObjectivesTotalsValueBean.getObjectivePayoutTotal(), 0 ) );
    }

    contestPayoutHistoryAwardThemNowValueBean.setId( SSIContestUtil.getClientState( request, ssiContest.getId(), awardIssuanceNumber, contestAwardThemNow.getLevelApproved(), true ) );
    contestPayoutHistoryAwardThemNowValueBean.setSortedBy( SSIContestUtil.DEFAULT_SORT_BY );
    contestPayoutHistoryAwardThemNowValueBean.setSortedOn( SSIContestUtil.SORT_BY_LAST_NAME );
    contestPayoutHistoryAwardThemNowValueBean.setCanApprove( getCanApprovalFlag( ssiContest, contestAwardThemNow, contestApprovers ) );
    setApprovalLevels( ssiContest, contestPayoutHistoryAwardThemNowValueBean, contestAwardThemNow, contestApprovers );

    if ( "true".equalsIgnoreCase( isApproveMode ) )
    {
      contestPayoutHistoryAwardThemNowValueBean.setBackButtonUrl( "loadSSIContestIssuanceApprovalSummary.do?method=display&clientState=" + contestPayoutHistoryAwardThemNowValueBean.getId() );
    }
    else
    {
      contestPayoutHistoryAwardThemNowValueBean.setBackButtonUrl( "displayContestSummaryAwardThemNow.do?method=load&contestId=" + contestPayoutHistoryAwardThemNowValueBean.getId() );
    }
    return contestPayoutHistoryAwardThemNowValueBean;

  }

  private boolean isFinalizeResult( String status )
  {
    return "finalize_results".equalsIgnoreCase( status );
  }

  private boolean getCanApprovalFlag( SSIContest contest, SSIContestAwardThemNow contestAwardThemNow, Map<String, Set<Participant>> contestApprovers )
  {

    if ( CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL == contest.getPromotion().getContestApprovalLevels() )
    {

      if ( contestAwardThemNow.getIssuanceStatusType().isWaitingForApproval() )
      {
        if ( isUserLevel1Approver( contest, contestApprovers ) && ! ( contestAwardThemNow.getApprovedByLevel1() != null ) )
        {
          return true;
        }
        if ( isUserLevel2Approver( contest, contestApprovers ) && contestAwardThemNow.getApprovedByLevel1() != null && !UserManager.getUserId().equals( contestAwardThemNow.getApprovedByLevel1() ) )
        {
          return true;
        }
      }

    }
    else if ( CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL == contest.getPromotion().getContestApprovalLevels() )
    {
      if ( contestAwardThemNow.getIssuanceStatusType().isWaitingForApproval() )
      {
        if ( isUserLevel1Approver( contest, contestApprovers ) && contestAwardThemNow.getApprovedByLevel1() == null )
        {
          return true;
        }
      }
    }
    return false;

  }

  private boolean isUserLevel1Approver( SSIContest contest, Map<String, Set<Participant>> contestApprovers )
  {
    boolean isUserLevel1Approver = false;
    Set<Participant> level1Approvers = contestApprovers.get( "selected_contest_approver_level_1" );
    for ( Participant participant : level1Approvers )
    {
      if ( UserManager.getUserId().equals( participant.getId() ) )
      {
        isUserLevel1Approver = true;
        break;
      }
    }
    return isUserLevel1Approver;
  }

  private boolean isUserLevel2Approver( SSIContest contest, Map<String, Set<Participant>> contestApprovers )
  {
    boolean isUserLevel2Approver = false;
    Set<Participant> level1Approvers = contestApprovers.get( "selected_contest_approver_level_2" );
    for ( Participant participant : level1Approvers )
    {
      if ( UserManager.getUserId().equals( participant.getId() ) )
      {
        isUserLevel2Approver = true;
        break;
      }
    }
    return isUserLevel2Approver;
  }

  protected void setApprovalLevels( SSIContest contest,
                                    SSIContestPayoutHistoryAwardThemNowValueBean contestPayoutHistoryAwardThemNowValueBean,
                                    SSIContestAwardThemNow contestAwardThemNow,
                                    Map<String, Set<Participant>> contestApprovers )
  {
    SSIContestApprovalLevelsView approvalLevel = null;
    int contestApprovalLevels = contest.getPromotion().getContestApprovalLevels();
    if ( contestApprovalLevels > 0 )
    {
      approvalLevel = new SSIContestApprovalLevelsView();
      approvalLevel.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.LEVEL1_NAME" ) );
      if ( contestAwardThemNow.getDateApprovedLevel1() != null && contestAwardThemNow.getApprovedByLevel1() != null )
      {
        approvalLevel.setApprovedBy( getFullName( contestAwardThemNow.getApprovedByLevel1() ) );
        approvalLevel.setApproved( true );
      }
      if ( contestAwardThemNow.getIssuanceStatusType().isWaitingForApproval() && contestApprovers != null )
      {
        Set<Participant> selectedLevel1Approvers = contestApprovers.get( "selected_contest_approver_level_1" );
        getApproversList( approvalLevel, selectedLevel1Approvers );
      }

      contestPayoutHistoryAwardThemNowValueBean.getApprovalLevels().add( approvalLevel );
    }
    if ( contestApprovalLevels > 1 )
    {
      approvalLevel = new SSIContestApprovalLevelsView();
      approvalLevel.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.LEVEL2_NAME" ) );
      if ( contestAwardThemNow.getLevelApproved() > 1 )
      {
        if ( contestAwardThemNow.getDateApprovedLevel2() != null && contestAwardThemNow.getApprovedByLevel2() != null )
        {
          approvalLevel.setApprovedBy( getFullName( contestAwardThemNow.getApprovedByLevel2() ) );
          approvalLevel.setApproved( true );
        }
      }
      if ( contestAwardThemNow.getIssuanceStatusType().isWaitingForApproval() && contestApprovers != null )
      {
        Set<Participant> selectedLevel2Approvers = contestApprovers.get( "selected_contest_approver_level_2" );
        getApproversList( approvalLevel, selectedLevel2Approvers );
      }
      contestPayoutHistoryAwardThemNowValueBean.getApprovalLevels().add( approvalLevel );
    }
    contestPayoutHistoryAwardThemNowValueBean.setApprovalRequired( contestApprovalLevels > 0 ? true : false );
    contestPayoutHistoryAwardThemNowValueBean.setApproveMode( true );
  }

  /**
   * To save the current page of Contest Participants data, apply necessary validations for the data filled-in and lookup the data for the requested page; 
   * This method will be used for the following events Sort, Page Navigation on the Participant table. 
   * @param mapping
   * @param form
   * @param request
   * @param response 
   * @return - Next set of participant records to display - JSON
   * @throws Exception
   */
  public ActionForward paxNav( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutHistoryAwardThemNowForm contestPayoutHistoryAwardThemNowForm = (SSIContestPayoutHistoryAwardThemNowForm)form;
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, contestPayoutHistoryAwardThemNowForm.getId(), request.getMethod().equalsIgnoreCase( "post" ) );
    Short awardIssuanceNumber = SSIContestUtil.getIssuanceNumberFromClientState( request, contestPayoutHistoryAwardThemNowForm.getId(), request.getMethod().equalsIgnoreCase( "post" ) );
    Integer currentPage = contestPayoutHistoryAwardThemNowForm.getCurrentPage() == 0 ? 1 : contestPayoutHistoryAwardThemNowForm.getCurrentPage();
    String defaultSortedOn = StringUtil.isNullOrEmpty( contestPayoutHistoryAwardThemNowForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : contestPayoutHistoryAwardThemNowForm.getSortedOn();
    String sortedOn = getMappedSortedOn( contestPayoutHistoryAwardThemNowForm );
    String sortedBy = StringUtil.isNullOrEmpty( contestPayoutHistoryAwardThemNowForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : contestPayoutHistoryAwardThemNowForm.getSortedBy();

    List<SSIContestParticipant> participants = getSSIContestAwardThemNowService().getContestParticipants( contestId, awardIssuanceNumber, currentPage, sortedOn, sortedBy, null );
    Integer participantsCount = getSSIContestAwardThemNowService().getContestParticipantsCount( contestId, awardIssuanceNumber );
    SSIContest ssiContest = getSSIContestAwardThemNowService().getContestById( contestId );
    SSIContestParticipantATNView responseView = new SSIContestParticipantATNView( participants,
                                                                                  SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                                  participantsCount,
                                                                                  sortedBy,
                                                                                  defaultSortedOn,
                                                                                  currentPage,
                                                                                  ssiContest );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private String getMappedSortedOn( SSIContestPayoutHistoryAwardThemNowForm contestPayoutHistoryAwardThemNowForm )
  {

    String sortedOn = StringUtil.isNullOrEmpty( contestPayoutHistoryAwardThemNowForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : contestPayoutHistoryAwardThemNowForm.getSortedOn();
    return ACTIVITY_AMOUNT.equalsIgnoreCase( sortedOn )
        ? OBJECTIVE_AMOUNT
        : PAYOUT_DESCRIPTION.equalsIgnoreCase( sortedOn ) ? OBJECTIVE_PAYOUT_DESC : PAYOUT_VALUE.equalsIgnoreCase( sortedOn ) ? OBJECTIVE_PAYOUT : sortedOn;

  }

  private AssociationRequestCollection getAssociationRequestCollection()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ALL_APPROVERS ) );
    return associationRequestCollection;
  }

  private void getApproversList( SSIContestApprovalLevelsView approvalLevel, Set<Participant> selectedLevel1Approvers )
  {
    for ( Participant participant : selectedLevel1Approvers )
    {
      SSIContestApprover contestApprover = approvalLevel.new SSIContestApprover();
      contestApprover.setId( participant.getId() );
      contestApprover.setName( participant.getFirstName() + ", " + participant.getLastName() );
      approvalLevel.getApproverList().add( contestApprover );
    }
  }

}

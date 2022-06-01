/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/goalquest/GoalQuestModuleAction.java,v $
 */

package com.biperf.core.ui.goalquest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.challengepoint.ChallengepointReviewProgress;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.goalquest.GoalLevelView;
import com.biperf.core.domain.goalquest.GoalQuestPromotionCollection;
import com.biperf.core.domain.goalquest.GoalQuestReviewProgress;
import com.biperf.core.domain.goalquest.GoalsView;
import com.biperf.core.domain.goalquest.ParticipantView;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.goalquest.PromotionView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.GoalQuestValueBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biw.hc.core.service.HCServices;

public class GoalQuestModuleAction extends BaseGoalQuestAction
{
  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( GoalQuestModuleAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return getGoalQuestList( mapping, form, request, response );
    }
  }
  
  public ActionForward honeycombSSO( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String programRole = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "programRole" );
    String programId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "programId" );
    String section = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "section" );

    if ( StringUtils.isBlank( programRole ) )
    {
      throw new IllegalArgumentException( "Client state parameter 'programRole' was missing" );
    }
    if ( StringUtils.isBlank( programId ) )
    {
      throw new IllegalArgumentException( "Client state parameter 'programId' was missing" );
    }

    Map<String, String> routerParameters = new HashMap<>();
    routerParameters.put( "programid", programId );
    routerParameters.put( "programrole", programRole );
    if ( StringUtils.isNotBlank( section ) )
    {
      routerParameters.put( "section", section );
    }

    String forwardPath = getHCServices().getGoalquestSSOPath( UserManager.getUserName(), routerParameters );
    return new ActionForward( forwardPath, true );
  }
  
  public ActionForward showPromotions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    PromotionMenuBean singlePromotionMenuBean = null;
    int count = 0;

    for ( PromotionMenuBean promoMenuBean : eligiblePromotions )
    {
      if ( isValidGoalQuestPromotionForParticipant( promoMenuBean ) )
      {
        // NOTE, we need to build correctly since a user can be both a submitter AND a partner in
        // the same promotion
        if ( !promoMenuBean.isCanSubmit() && promoMenuBean.isPartner() && !isPartnerVisible( participant, promoMenuBean ) )
        {
          continue;// this is a partner, and the partner selection dates have not yet passed
        }
        singlePromotionMenuBean = promoMenuBean;
        count++;
        if ( count > 1 )
        {
          return mapping.findForward( "goalquest.promotion.list" );
        }
      }
    }
    // just in case, lets put the promotion parms in the request for dispatching
    request.setAttribute( "promotionId", singlePromotionMenuBean.getPromotion().getId() );
    return buildForward( mapping, singlePromotionMenuBean, participant );
  }

  public ActionForward getGoalQuestList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    AuthenticatedUser user = UserManager.getUser();
    GoalQuestPromotionCollection goalQuestPromotionCollections = new GoalQuestPromotionCollection();
    List<PromotionView> promotionViewList = new ArrayList<PromotionView>();

    Participant participant = getParticipantService().getParticipantById( user.getUserId() );
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    request.setAttribute( "timeZoneID", timeZoneID );
    List<PromotionMenuBean> eligiblePromotions = new ArrayList<PromotionMenuBean>( getEligiblePromotions( request ) );

    List<PromotionView> goalSelectionList = new ArrayList<PromotionView>();
    List<PromotionView> goalProgressList = new ArrayList<PromotionView>();
    List<PromotionView> goalCompletedList = new ArrayList<PromotionView>();
    List<PromotionView> sortedPromotionViewList = new ArrayList<PromotionView>();

    for ( PromotionMenuBean promoMenuBean : eligiblePromotions )
    {
      if ( isValidGoalQuestPromotionForParticipant( promoMenuBean ) )
      {
        // NOTE, we need to build correctly since a user can be both a submitter AND a partner in
        // the same promotion
        if ( !promoMenuBean.isCanSubmit() && promoMenuBean.isPartner() && !isPartnerVisible( participant, promoMenuBean ) )
        {
          continue;// this is a partner, and the partner selection dates have not yet passed
        }
        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promoMenuBean.getPromotion();
        GoalQuestValueBean valueBean = new GoalQuestValueBean();
        valueBean.setServletContext( request.getContextPath() );
        valueBean.setOwner( participant.isOwner() );
        valueBean.setManager( participant.isManager() );
        valueBean.setShowProgramRules( checkIfShowPromoRules( goalQuestPromotion, participant ) );
        valueBean.setPromotion( goalQuestPromotion );
        valueBean = buildPartnerList( promoMenuBean, participant, valueBean, request, mapping );
        promotionViewList.add( buildPromotionView( valueBean, participant, promoMenuBean ) );
      }
    }
    
    // Get goalquest promotions from honeycomb
    if ( PromotionType.GOALQUEST.equals( getModulePromotionType() ) )
    {
      List<PromotionView> honeycombPromotionViews = getGoalQuestService().getHoneycombProgramDetails( participant );
      promotionViewList.addAll( honeycombPromotionViews );
    }

    for ( Iterator<PromotionView> iter = promotionViewList.iterator(); iter.hasNext(); )
    {
      PromotionView promoView = (PromotionView)iter.next();

      if ( DateUtils.isDateBetween( new Date(), new Date(), DateUtils.toEndDate( promoView.getStartDate() ), timeZoneID ) && promoView.getAwardIssueRun().equals( "false" ) )
      {
        goalSelectionList.add( promoView );
      }
      else if ( promoView.getAwardIssueRun().equals( "true" ) )
      {
        goalCompletedList.add( promoView );
      }
      else if ( promoView.getAwardIssueRun().equals( "false" )
          && DateUtils.isDateBetween( new Date(), DateUtils.toEndDate( promoView.getStartDate() ), DateUtils.toEndDate( promoView.getFinalProcessDate() ), timeZoneID )
          || promoView.getAwardIssueRun().equals( "false" )
              && DateUtils.isDateBetween( new Date(), DateUtils.toEndDate( promoView.getFinalProcessDate() ), DateUtils.toEndDate( promoView.getPromoEndDate() ), timeZoneID ) )
      {
        goalProgressList.add( promoView );
      }
      // added for Bug 53692
      else if ( DateUtils.isDateBetween( new Date(), DateUtils.toEndDate( promoView.getPromoEndDate() ), DateUtils.toEndDate( promoView.getTileDisplayEndDate() ), timeZoneID ) )
      {
        goalCompletedList.add( promoView );
      }
      // end
    }
    Collections.sort( goalSelectionList, new GoalSelectionDateComparator() );
    sortedPromotionViewList.addAll( goalSelectionList );

    Collections.sort( goalProgressList, new GoalProgressDateComparator() );
    sortedPromotionViewList.addAll( goalProgressList );

    Collections.sort( goalCompletedList, new GoalCompletedDateComparator() );
    sortedPromotionViewList.addAll( goalCompletedList );

    goalQuestPromotionCollections.setPromotions( sortedPromotionViewList );
    super.writeAsJsonToResponse( goalQuestPromotionCollections, response );
    return null;
  }

  protected PromotionView buildPromotionView( GoalQuestValueBean valueBean, Participant participant, PromotionMenuBean promoMenuBean )
  {
    PromotionView promotionView = new PromotionView();
    promotionView.setId( valueBean.getPromotion().getId().toString() );
    promotionView.setName( valueBean.getPromotion().getName() );
    promotionView.setStartDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( valueBean.getPromotion().getGoalCollectionEndDate(), UserManager.getTimeZoneID() ) ) );
    promotionView.setStatus( buildPromotionStatus( valueBean.getPromotion() ) );
    // added for Bug 53692
    promotionView.setTileDisplayEndDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( valueBean.getPromotion().getTileDisplayEndDate(), UserManager.getTimeZoneID() ) ) );
    // end
    promotionView.setGoals( buildGoalsView( valueBean.getPromotion(), participant, valueBean, promoMenuBean ) );
    promotionView.setFinalProcessDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( valueBean.getPromotion().getFinalProcessDate(), UserManager.getTimeZoneID() ) ) );
    promotionView.setPromoStartDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( valueBean.getPromotion().getSubmissionStartDate(), UserManager.getTimeZoneID() ) ) );
    promotionView.setPromoEndDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( valueBean.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
    promotionView.setAwardIssueRun( valueBean.getPromotion().isIssueAwardsRun() ? "true" : "false" );
    if ( getUnderArmourService().uaEnabled() )
    {
      promotionView.setUa( valueBean.getPromotion().isAllowUnderArmour() );
      try
      {
        promotionView.setUaconnected( getUnderArmourService().isParticipantAuthorized( UserManager.getUserId() ) );
      }
      catch( Exception e )
      {
        log.error( e.getMessage(), e );
      }
      if ( CollectionUtils.isNotEmpty( promotionView.getGoals() ) )
      {
        GoalsView goalsView = promotionView.getGoals().get( 0 );
        Map<String, Object> params = goalsView.buildParameterMap();
        params.put( "redirectUrl", "/goalquest/selectGoal.do?" );
        promotionView.setUaAuthorizeUrl( getSystemVariableService().getPropertyByName( SystemVariableService.UA_WEBSERVICES_AUTHORIZE_URL_PREFIX ).getStringVal() + "response_type=code&client_id="
            + getSystemVariableService().getPropertyByName( SystemVariableService.UNDERARMOUR_CLIENT_ID ).getStringVal() + "&redirect_uri="
            + getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/ua/underArmourCallback.action&state="
            + ClientStateUtils.generateEncodedParamMap( params ) );
      }

    }
    return promotionView;
  }

  @SuppressWarnings( "unchecked" )
  protected GoalQuestValueBean buildPartnerList( PromotionMenuBean promoMenuBean, Participant participant, GoalQuestValueBean valueBean, HttpServletRequest request, ActionMapping mapping )
  {
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promoMenuBean.getPromotion();
    valueBean.setPaxGoal( getPaxGoal( goalQuestPromotion, participant.getId() ) );
    if ( goalQuestPromotion.isPartnersEnabled() )
    {
      AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
      ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
      ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
      ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
      goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( goalQuestPromotion.getId(), ascReqColl );
      // If partner moved out of audience List or made inactive, do not proceed to display
      // partner in GQ TILE..BugFix 20363,21163
      ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );

      if ( isPartnerVisible( participant, promoMenuBean ) )// only
                                                           // do
                                                           // this
                                                           // if
                                                           // the
                                                           // PartnerSelection
                                                           // period
                                                           // is
                                                           // over
      {
        // this is for Partners. Put this pax in there and find the owner too
        List<ParticipantPartner> partnersList = getPromotionService().getParticipantsByPromotionAndPartnerWithAssociations( goalQuestPromotion.getId(), participant.getId(), paxAscReq );
        valueBean.setPaxPartners( partnersList );
      }

      /*
       * AssociationRequestCollection associationRequestCollection = new
       * AssociationRequestCollection(); associationRequestCollection.add( new
       * PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
       * GoalQuestPromotion promotion =
       * (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( new Long(
       * goalQuestPromotion.getId() ), associationRequestCollection ); AssociationRequestCollection
       * associationReq = new AssociationRequestCollection(); associationReq.add( new
       * UserAssociationRequest( UserAssociationRequest.CHARACTERISTIC ) ); paxes =
       * getParticipantService().getAllActiveWithAssociations( associationReq ); paxes =
       * filterUsers( paxes, request, promotion, userId ); if (
       * promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) &&
       * promotion.isPartnersEnabled() && promotion.getPartnerAudiences() != null &&
       * promotion.getPartnerAudiences().size() > 0 ) { Participant pax =
       * getParticipantService().getParticipantById( participant.getId() ); paxUserName =
       * pax.getUserName(); List<Participant> selectedPartnerList = getSelectedPartnerList( paxes,
       * paxUserName, promotion ); String[] obj = null; if ( selectedPartnerList != null &&
       * selectedPartnerList.size() > 0 ) { int i = 0; obj = new String[selectedPartnerList.size()];
       * for ( Iterator<Participant> iter = selectedPartnerList.iterator(); iter.hasNext(); ) {
       * Participant selectedPartner = iter.next(); Node node = getRecipientNode( selectedPartner );
       * obj[i] = selectedPartner.getId() + ":" + node.getId() + "-" +
       * selectedPartner.getNameLFMWithComma() + " - " + node.getName() + " - " +
       * selectedPartner.getPositionTypePickList().getName() + " - " +
       * selectedPartner.getDepartmentTypePickList().getName(); i++; } }
       * request.getSession().setAttribute( "selectedPartnerResults", obj ); }
       */

    }
    return valueBean;
  }

  protected GoalLevelView buildGoalLevelView( Participant owner, GoalQuestValueBean valueBean )
  {
    PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( valueBean.getPromotion().getId(), owner.getId() );
    if ( paxGoal != null )
    {
      if ( paxGoal.getGoalLevel() != null )
      {
        GoalLevelView level = new GoalLevelView();
        level.setDescription( paxGoal.getGoalLevel().getGoalLevelDescription() );
        level.setName( paxGoal.getGoalLevel().getGoalLevelName() );
        return level;
      }
    }
    return null;
  }

  protected GoalsView buildGeneralGoalViewForPromotion( Participant owner, GoalQuestValueBean valueBean )
  {
    GoalsView goal = new GoalsView();
    goal.setCanChange( valueBean.isInGoalSelection() );
    goal.setIsAchieved( valueBean.isAwardEarned() );
    goal.setPromotionId( valueBean.getPromotion().getId() );
    goal.setServletContext( valueBean.getServletContext() );
    goal.setProgressValue( valueBean.getPercentageAchieved() != null ? valueBean.getPercentageAchieved().toString() : null );

    PaxGoal paxGoal = getPaxGoal( valueBean.getPromotion(), owner.getId() );
    if ( null != paxGoal )
    {
      if ( paxGoal.getGoalLevel() != null )
      {
        goal.setId( paxGoal.getGoalLevel().getId().toString() );
      }
      goal.setPaxGoalId( paxGoal.getId().toString() );
      buildProgress( valueBean, goal, paxGoal );
    }

    goal.setShowProgress( true );
    if ( goal.getProgressValue() != null )
    {
      if ( new BigDecimal( goal.getProgressValue() ).compareTo( new BigDecimal( 100 ) ) >= 0 )
      {
        goal.setPercentageExceeds( true );
      }
      else
      {
        goal.setPercentageExceeds( false );
      }
    }
    goal.setGoalLevel( buildGoalLevelView( owner, valueBean ) );
    return goal;
  }

  protected void buildProgress( GoalQuestValueBean valueBean, GoalsView view, PaxGoal paxGoal )
  {
    Long participantId = paxGoal.getParticipant().getId();
    Long promotionId = valueBean.getPromotion().getId();
    Map<String, Object> progressMap = null;

    if ( valueBean.getPromotion().isGoalQuestPromotion() )
    {
      progressMap = getGoalQuestService().getGoalQuestProgressByPromotionIdAndUserId( promotionId, participantId );

      @SuppressWarnings( "unchecked" )
      List<GoalQuestReviewProgress> progressList = (List<GoalQuestReviewProgress>)progressMap.get( "goalQuestProgressList" );

      if ( progressList != null && progressList.size() > 0 )
      {
        GoalQuestReviewProgress goalQuestReviewProgress = (GoalQuestReviewProgress)progressList.get( progressList.size() - 1 );
        NumberFormat formatter = NumberFormat.getIntegerInstance();
        formatter.setMaximumFractionDigits( valueBean.getPromotion().getAchievementPrecision().getPrecision() );
        formatter.setMinimumFractionDigits( valueBean.getPromotion().getAchievementPrecision().getPrecision() );
        if ( goalQuestReviewProgress.getPercentToGoal() != null )
        {
          view.setProgressValue( goalQuestReviewProgress.getPercentToGoal().toString() );
          view.setProgressDate( null != goalQuestReviewProgress.getSubmissionDate() ? goalQuestReviewProgress.getDisplaySubmissionDate() : DateUtils.toDisplayString( new Date() ) );
        }
        if ( paxGoal.getCurrentValue() != null && goalQuestReviewProgress.getAmountToAchieve() != null )
        {
          if ( paxGoal.getCurrentValue().compareTo( goalQuestReviewProgress.getAmountToAchieve() ) >= 0 )
          {
            view.setIsAchieved( true );
          }
        }
      }
    }

    else if ( valueBean.getPromotion().isChallengePointPromotion() )
    {
      try
      {
        progressMap = getChallengepointProgressService().getChallengepointProgressByPromotionIdAndUserId( promotionId, participantId );
      }
      catch( Exception e )
      {
        throw new BeaconRuntimeException( e.getMessage() );
      }
      if ( progressMap != null )
      {
        @SuppressWarnings( "unchecked" )
        List<ChallengepointReviewProgress> progressList = (List<ChallengepointReviewProgress>)progressMap.get( "challengepointProgressList" );

        if ( progressList != null && progressList.size() > 0 )
        {
          ChallengepointReviewProgress cpReviewProgress = (ChallengepointReviewProgress)progressList.get( 0 );
          NumberFormat formatter = NumberFormat.getIntegerInstance();
          formatter.setMaximumFractionDigits( valueBean.getPromotion().getAchievementPrecision().getPrecision() );
          formatter.setMinimumFractionDigits( valueBean.getPromotion().getAchievementPrecision().getPrecision() );
          if ( cpReviewProgress.getPercentToGoal() != null )
          {
            view.setProgressValue( cpReviewProgress.getPercentToGoal().toString() );
            view.setProgressDate( null != cpReviewProgress.getSubmissionDate() ? cpReviewProgress.getDisplaySubmissionDate() : DateUtils.toDisplayString( new Date() ) );
          }
          if ( paxGoal.getCurrentValue() != null && cpReviewProgress.getAmountToAchieve() != null )
          {
            if ( paxGoal.getCurrentValue().compareTo( cpReviewProgress.getAmountToAchieve() ) >= 0 )
            {
              view.setIsAchieved( true );
            }
          }
        }
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  protected List<GoalsView> buildGoalsView( GoalQuestPromotion promotion, Participant participant, GoalQuestValueBean valueBean, PromotionMenuBean promoMenuBean )
  {
    List<GoalsView> goalsList = new ArrayList<GoalsView>();
    // add owner
    if ( promoMenuBean.isCanSubmit() )// owner
    {
      GoalsView ownerGoal = buildGeneralGoalViewForPromotion( participant, valueBean );
      ownerGoal.setIsPartner( false );
      ownerGoal.setParticipant( buildParticipant( participant ) );
      goalsList.add( ownerGoal );
    }

    // this person is a partner to 1-M people in this promotion
    if ( promoMenuBean.isPartner() && isPartnerVisible( participant, promoMenuBean ) )
    {
      if ( null != valueBean.getPaxPartners() && !valueBean.getPaxPartners().isEmpty() )
      {
        for ( ParticipantPartner paxParticipant : (List<ParticipantPartner>)valueBean.getPaxPartners() )
        {
          AssociationRequestCollection associationRequests = new AssociationRequestCollection();
          associationRequests.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT ) );
          Participant pax = getParticipantService().getParticipantByIdWithAssociations( paxParticipant.getParticipant().getId(), associationRequests );
          // enter partner info
          GoalsView partnerGoal = buildGeneralGoalViewForPromotion( paxParticipant.getParticipant(), valueBean );
          partnerGoal.setIsPartner( true );
          partnerGoal.setCanChange( false );
          partnerGoal.setParticipant( buildParticipant( pax ) );
          goalsList.add( partnerGoal );
        }
      }
    }

    else if ( promoMenuBean.isViewTile() && goalsList.isEmpty() )
    {
      GoalsView participantGoal = buildGeneralGoalViewForPromotion( participant, valueBean );
      participantGoal.setParticipant( buildParticipant( participant ) );
      goalsList.add( participantGoal );
    }

    return goalsList;
  }

  protected ParticipantView buildParticipant( Participant participant )
  {
    ParticipantView paxView = new ParticipantView();
    paxView.setFirstName( participant.getFirstName() );
    paxView.setId( participant.getId().toString() );
    paxView.setLastName( participant.getLastName() );
    return paxView;
  }

  // protected boolean isPartner( Participant participant, PromotionMenuBean promoMenuBean )
  // {
  // return ( !promoMenuBean.isCanReceive() && !promoMenuBean.isCanSubmit() );
  // }

  protected boolean isPartnerVisible( Participant participant, PromotionMenuBean promoMenuBean )
  {
    GoalQuestPromotion promotion = (GoalQuestPromotion)promoMenuBean.getPromotion();
    return new Date().after( promotion.getGoalCollectionEndDate() );
  }

  // ok, we need to inspect the individual promotion - since the user could be a partner and
  // have multiple entries per this individual promotion
  // rules - IF RULES
  /*
   * 1. If the selection phase has not started but the promotion has started, forward to Rules page
   * for Owner 2. If the selection phase has passed, then forward to the detail page for owner 3. If
   * the selection phase has passed, and the user is a partner for one owner, forward to detail page
   * 4. If the selection phase has passed, and the user is a partner to 2 or more owners, forward to
   * the List page 5. if in goal selection phase and owner, forward to wizard for change/update goal
   * 6. if in goal selection phase and partner/// wait..can't happen nevermind
   */
  protected ActionForward buildForward( ActionMapping mapping, PromotionMenuBean promoMenuBean, Participant participant )
  {
    GoalQuestPromotion promotion = (GoalQuestPromotion)promoMenuBean.getPromotion();
    boolean isPartner = promoMenuBean.isPartner();
    String timeZoneId = getUserService().getUserTimeZone( participant.getId() );
    Date now = DateUtils.applyTimeZone( new Date(), timeZoneId );
    // scenario 1
    if ( now.before( DateUtils.toStartDate( promotion.getGoalCollectionStartDate() ) ) )
    {
      return mapping.findForward( "goalquest.promotion.rules" );
    }

    // scenario 2
    if ( now.after( DateUtils.toEndDate( promotion.getGoalCollectionEndDate() ) ) && !isPartner )
    {
      PaxGoal paxGoal = getPaxGoal( promotion, participant.getId() );
      if ( paxGoal != null )
      {
        ActionForward forward = new ActionForward( mapping.findForward( "goalquest.promotion.detail" ) );
        forward.setPath( forward.getPath() + "&paxGoalId=" + ( null == paxGoal ? "" : paxGoal.getId() + "" ) );
        return forward;
      }
      return mapping.findForward( "goalquest.promotion.rules" );
    }

    if ( now.after( promotion.getGoalCollectionEndDate() ) && isPartner )
    {
      PaxGoal paxGoal = null;
      ActionForward forward = null;
      ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
      List<ParticipantPartner> partnersList = getPromotionService().getParticipantsByPromotionAndPartnerWithAssociations( promotion.getId(), participant.getId(), paxAscReq );
      for ( int i = 0; i < partnersList.size(); i++ )
      {
        ParticipantPartner paxPartner = (ParticipantPartner)partnersList.get( i );
        paxGoal = getPaxGoal( promotion, paxPartner.getParticipant().getId() );
      }
      if ( partnersList.size() > 1 )
      {
        forward = new ActionForward( mapping.findForward( "goalquest.promotion.list" ) );
      }
      else
      {
        forward = new ActionForward( mapping.findForward( "goalquest.promotion.detail" ) );
        forward.setPath( forward.getPath() + "&paxGoalId=" + ( null == paxGoal ? "" : paxGoal.getId() + "" ) );
      }

      return forward;
    }

    // scenario 5
    boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
    if ( isGoalSelectableOrChangeable( promotion ) && !isPartner && !isLoginAs )
    {
      return mapping.findForward( "goalquest.promotion.wizard" );
    }

    if ( isGoalSelectableOrChangeable( promotion ) && !isPartner && isLoginAs )
    {
      return mapping.findForward( "goalquest.promotion.list" );
    }
    // ok, lets attack the most expensive scenario last
    List<ParticipantPartner> partnerList = getPromotionService().getParticipantsByPromotionAndPartnerWithAssociations( promotion.getId(), participant.getId(), null );
    if ( partnerList.size() > 1 )
    {
      return mapping.findForward( "goalquest.promotion.list" );
    }
    else
    {
      return mapping.findForward( "goalquest.promotion.detail" );
    }
  }

  private String buildPromotionStatus( GoalQuestPromotion promotion )
  {
    String timeZoneId = getUserService().getUserTimeZone( UserManager.getUserId() );

    if ( DateUtils.isDateBetween( new Date(), DateUtils.toStartDate( promotion.getTileDisplayStartDate() ), DateUtils.toEndDate( promotion.getGoalCollectionEndDate() ), timeZoneId )
        && !promotion.isIssueAwardsRun() )
    {
      return "open";
    }
    else if ( promotion.isIssueAwardsRun() )
    {
      return "ended";
    }
    else if ( DateUtils.isDateBetween( new Date(), DateUtils.toEndDate( promotion.getGoalCollectionEndDate() ), DateUtils.toEndDate( promotion.getFinalProcessDate() ), timeZoneId ) )
    {
      return "started";
    }
    else if ( !promotion.isIssueAwardsRun()
        && DateUtils.isDateBetween( new Date(), DateUtils.toEndDate( promotion.getFinalProcessDate() ), DateUtils.toEndDate( promotion.getSubmissionEndDate() ), timeZoneId ) )
    {
      return "started";
    }
    else if ( !promotion.isIssueAwardsRun() && DateUtils.isDateBetween( new Date(), DateUtils.toStartDate( promotion.getSubmissionEndDate() ), DateUtils.toEndDate( new Date() ), timeZoneId ) )
    {
      return "started";
    }
    return "ended";
  }

  private boolean isGoalSelectableOrChangeable( GoalQuestPromotion promotion )
  {
    String timeZoneId = getUserService().getUserTimeZone( UserManager.getUserId() );
    return DateUtils.isDateBetween( new Date(), DateUtils.toStartDate( promotion.getGoalCollectionStartDate() ), DateUtils.toEndDate( promotion.getGoalCollectionEndDate() ), timeZoneId );
  }

  // this needs to change for subclasses for CP and GQ
  protected boolean isValidGoalQuestPromotionForParticipant( PromotionMenuBean promoMenuBean )
  {
    Promotion promotion = promoMenuBean.getPromotion();
    // return false to all non-goalquest promotion types and all non-live ones
    if ( !promotion.isGoalQuestPromotion() )
    {
      return false;
    }
    if ( !promotion.isLive() )
    {
      return false;
    }

    return promoMenuBean.isCanReceive() || promoMenuBean.isCanSubmit() || promoMenuBean.isPartner();
  }

  private boolean checkIfShowPromoRules( Promotion promotion, com.biperf.core.domain.participant.Participant participant )
  {
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    return promotion.isWebRulesActive() && DateUtils.isDateBetween( new Date(), promotion.getWebRulesStartDate(), promotion.getWebRulesEndDate(), timeZoneID )
        && getAudienceService().isParticipantInWebRulesAudience( promotion, participant );
  }

  @SuppressWarnings( "unused" )
  private BigDecimal getPartnerEarnings( GoalQuestValueBean valueBean, GoalQuestPromotion goalQuestPromotion, Long paxPayout )
  {
    BigDecimal partnerPayout = null;
    int seqNumber = valueBean.getPaxGoal().getGoalLevel().getSequenceNumber();
    BigDecimal partnerAwdAmount = getPromotionService().getPartnerAwardAmountByPromotionAndSequenceNo( goalQuestPromotion.getId(), seqNumber );
    String partnerPayStructure = goalQuestPromotion.getPartnerPayoutStructure().getCode();
    if ( PartnerPayoutStructure.FIXED.equals( partnerPayStructure ) )
    {
      partnerPayout = partnerAwdAmount;
    }
    else if ( PartnerPayoutStructure.PERCENTAGE.equals( partnerPayStructure ) )
    {
      double baseAward = ( (GoalLevel)valueBean.getPaxGoal().getGoalLevel() ).getAward().doubleValue();
      if ( null != paxPayout )
      {
        baseAward = paxPayout.doubleValue();
      }
      int roundingMode = goalQuestPromotion.getRoundingMethod().getBigDecimalRoundingMode();
      partnerPayout = new BigDecimal( baseAward ).multiply( partnerAwdAmount ).divide( new BigDecimal( 100 ), roundingMode );
    }
    return partnerPayout;
  }

  /*
   * private List<Participant> getSelectedPartnerList( List<Participant> currentPartnerAudience,
   * String paxUserName, GoalQuestPromotion promotion ) { List<Participant> selectedPartners = new
   * ArrayList<Participant>(); String partnerCharName = null; String partnerCharValue = null; if (
   * currentPartnerAudience != null && currentPartnerAudience.size() > 0 ) { for (
   * Iterator<Participant> iter = currentPartnerAudience.iterator(); iter.hasNext(); ) { Participant
   * selectedPaxPartner = iter.next(); if ( selectedPaxPartner.getUserCharacteristics() != null &&
   * promotion.getPreSelectedPartnerChars() != null ) { Set<UserCharacteristic> userCharacteristics
   * = selectedPaxPartner.getUserCharacteristics(); Iterator<UserCharacteristic> itr =
   * userCharacteristics.iterator(); while ( itr.hasNext() ) { UserCharacteristic userChar =
   * itr.next(); partnerCharName = userChar.getUserCharacteristicType().getCharacteristicName(); if
   * ( partnerCharName.equalsIgnoreCase( promotion.getPreSelectedPartnerChars() ) ) { if (
   * userChar.getCharacteristicValue() != null ) { partnerCharValue =
   * userChar.getCharacteristicValue(); } if ( partnerCharValue.equalsIgnoreCase( paxUserName ) ) {
   * selectedPartners.add( selectedPaxPartner ); } } } } } } return selectedPartners; }
   * @SuppressWarnings( "unchecked" ) private List<Participant> filterUsers( List<Participant>
   * participants, HttpServletRequest request, Promotion promotion, Long userId ) { Long proxyUserId
   * = null; Node mainUserNode = null; // filter paxes GoalQuestPromotion gqPromo =
   * (GoalQuestPromotion)promotion; if ( gqPromo.getPartnerAudienceType() != null &&
   * gqPromo.getAwardType() != null && gqPromo.getAwardType().getCode().equals(
   * PromotionAwardsType.POINTS ) ) { AssociationRequestCollection ascRequestCollection = new
   * AssociationRequestCollection(); PromotionAssociationRequest promoAsscoiationRequest = new
   * PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES );
   * ascRequestCollection.add( promoAsscoiationRequest ); gqPromo =
   * (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( gqPromo.getId(),
   * ascRequestCollection ); } for ( Iterator<Participant> paxIter = participants.iterator();
   * paxIter.hasNext(); ) { Participant pax = paxIter.next(); if ( mustRemove( pax, userId,
   * proxyUserId, mainUserNode, gqPromo, request ) ) { paxIter.remove(); } } return participants; }
   * private boolean mustRemove( Participant pax, Long userId, Long proxyUserId, Node mainUserNode,
   * GoalQuestPromotion promotion, HttpServletRequest request ) { // remove self if ( excludeSelf(
   * request ) ) { if ( userId != null && userId.equals( pax.getId() ) ) { return true; } if (
   * proxyUserId != null && proxyUserId.equals( pax.getId() ) ) { return true; } } // 20771 inactive
   * pax wouldn't display in ajax partner search list. if ( pax.isActive() != null &&
   * !pax.isActive().booleanValue() ) { return true; } // remove if not valid for this audience if (
   * promotion != null ) { if ( !getAudienceService().isUserInPromotionPartnerAudiences( pax,
   * promotion.getPartnerAudiences() ) ) { return true; } } return false; } private Node
   * getRecipientNode( Participant recipient ) { Node selectedRecipientNode = null; // Get all nodes
   * the recipient is attached to for ( Iterator<UserNode> iter =
   * recipient.getUserNodes().iterator(); iter.hasNext(); ) { UserNode userNode = iter.next(); //
   * Pick initial active node if ( selectedRecipientNode == null &&
   * userNode.isActive().booleanValue() ) { selectedRecipientNode = userNode.getNode(); } // Compare
   * and select the first created active recipient node else if ( userNode.isActive().booleanValue()
   * && userNode.getNode().getAuditCreateInfo().getDateCreated().before(
   * selectedRecipientNode.getAuditCreateInfo().getDateCreated() ) ) { selectedRecipientNode =
   * userNode.getNode(); } } return selectedRecipientNode; }
   */

  protected boolean excludeSelf( HttpServletRequest request )
  {
    return true;
  }

  protected boolean isDateWithinTileDisplayDate( Promotion promotion )
  {
    Date now = new Date();
    return now.after( promotion.getTileDisplayStartDate() ) && now.before( promotion.getTileDisplayEndDate() );
  }

  protected boolean isNodeBasedAudience( GoalQuestPromotion promotion )
  {
    return null != promotion.getPartnerAudienceType() && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE );
  }

  protected boolean isUserCharBasedAudience( GoalQuestPromotion promotion )
  {
    return null != promotion.getPartnerAudienceType() && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.USER_CHAR );
  }

  protected boolean isSpecificAudience( GoalQuestPromotion promotion )
  {
    return null != promotion.getPartnerAudienceType() && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE );
  }
  
  protected String getModulePromotionType()
  {
    return PromotionType.GOALQUEST;
  }

  class GoalSelectionDateComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      if ( ! ( o1 instanceof PromotionView ) || ! ( o2 instanceof PromotionView ) )
      {
        throw new ClassCastException( "Object is not a PromotionView object!" );
      }

      Date goalEndDate1 = DateUtils.toDate( (String) ( (PromotionView)o1 ).getStartDate(), UserManager.getLocale() );
      Date goalEndDate2 = DateUtils.toDate( (String) ( (PromotionView)o2 ).getStartDate(), UserManager.getLocale() );
      return goalEndDate1.compareTo( goalEndDate2 );
    }
  }

  class GoalProgressDateComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      if ( ! ( o1 instanceof PromotionView ) || ! ( o2 instanceof PromotionView ) )
      {
        throw new ClassCastException( "Object is not a PromotionView object!" );
      }

      Date goalEndDate1 = DateUtils.toDate( (String) ( (PromotionView)o1 ).getPromoStartDate(), UserManager.getLocale() );
      Date goalEndDate2 = DateUtils.toDate( (String) ( (PromotionView)o2 ).getPromoStartDate(), UserManager.getLocale() );
      return goalEndDate1.compareTo( goalEndDate2 ) * -1;
    }
  }

  class GoalCompletedDateComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      if ( ! ( o1 instanceof PromotionView ) || ! ( o2 instanceof PromotionView ) )
      {
        throw new ClassCastException( "Object is not a PromotionView object!" );
      }

      Date goalEndDate1 = DateUtils.toDate( (String) ( (PromotionView)o1 ).getPromoEndDate(), UserManager.getLocale() );
      Date goalEndDate2 = DateUtils.toDate( (String) ( (PromotionView)o2 ).getPromoEndDate(), UserManager.getLocale() );
      return goalEndDate1.compareTo( goalEndDate2 ) * -1;
    }
  }

  private static AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }

  private static UnderArmourService getUnderArmourService()
  {
    return (UnderArmourService)BeanLocator.getBean( UnderArmourService.BEAN_NAME );
  }
  
  private static HCServices getHCServices()
  {
    return (HCServices)BeanLocator.getBean( HCServices.BEAN_NAME );
  }
}


package com.biperf.core.ui.goalquest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.util.StringUtils;

import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.underarmour.UnderArmourService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.goalquest.view.AwardProductView;
import com.biperf.core.ui.goalquest.view.GoalAwardView;
import com.biperf.core.ui.goalquest.view.PlateauAwardsView;
import com.biperf.core.ui.goalquest.view.PromotionAwardView;
import com.biperf.core.ui.profile.MessageView;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.GoalLevelValueBean;

public class GoalQuestWizardAction extends BaseGoalQuestAction
{
  public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = getUserId( request );
    GoalQuestWizardForm wizard = (GoalQuestWizardForm)form;
    wizard.setUserId( userId );
    // load promotion information
    buildPromotionInfo( wizard, request );
    // load any currently selected participant goals
    buildGoalInfo( wizard, request );
    // load any partner info
    buildPartnerInfo( wizard, userId );

    String activeStep = request.getParameter( "activeStep" );

    if ( org.apache.commons.lang3.StringUtils.isNoneBlank( activeStep ) )
    {
      wizard.setActiveStep( activeStep );
    }

    if ( wizard.isUaEnabled() )
    {
      Map<String, Object> params = new HashMap<String, Object>();
      params.put( "promotionId", wizard.getPromotionId() );
      params.put( "paxGoalId", wizard.getSelectedGoalId() );
      params.put( "isPartner", CollectionUtils.isNotEmpty( wizard.getPreselectedPartners() ) );
      params.put( "redirectUrl", "/goalquest/selectGoal.do?" );
      wizard.setUaOAuthUrl( getSystemVariableService().getPropertyByName( SystemVariableService.UA_WEBSERVICES_AUTHORIZE_URL_PREFIX ).getStringVal() + "response_type=code&client_id="
          + getSystemVariableService().getPropertyByName( SystemVariableService.UNDERARMOUR_CLIENT_ID ).getStringVal() + "&redirect_uri="
          + getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/ua/underArmourCallback.action&state="
          + ClientStateUtils.generateEncodedParamMap( params ) );

    }

    request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private UnderArmourService getUnderArmourService()
  {
    return (UnderArmourService)BeanLocator.getBean( UnderArmourService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward submit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forwardAction = "success_home";
    GoalQuestWizardForm wizard = (GoalQuestWizardForm)form;
    GoalQuestPromotion goalQuestPromotion = null;
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );

    if ( wizard.getPromotionId() != null )
    {
      goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( wizard.getPromotionId(), promoAssociationRequestCollection );
    }

    if ( wizard.getSelectedGoalId() != null )
    {
      GoalLevel goalLevel = (GoalLevel)getGoalLevelService().getGoalLevelById( wizard.getSelectedGoalId() );
      PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( wizard.getPromotionId(), getUserId( request ) );
      goalLevel.setPromotion( goalQuestPromotion );
      if ( paxGoal == null )
      {
        paxGoal = new PaxGoal();
        if ( goalQuestPromotion != null )
        {
          paxGoal.setGoalQuestPromotion( goalQuestPromotion );
        }
        paxGoal.setParticipant( SelectGoalUtil.getParticipant( getUserId( request ) ) );
      }
      paxGoal.setProductSetId( wizard.getSelectedProductId() );
      paxGoal.setSelectedProductName( wizard.getSelectedProductName() );
      paxGoal.setGoalLevel( goalLevel );
      List<ParticipantPartner> paxPartnerList = buildParticipantPartnerList( wizard, goalQuestPromotion, getUserId( request ) );
      paxGoal = getPaxGoalService().savePaxGoalWithPartners( goalQuestPromotion != null ? goalQuestPromotion.getId() : null, paxGoal, paxPartnerList );
      GoalLevelValueBean selectedGoalLevelValueBean = null;
      ChallengepointPaxValueBean selectedCPLevelValueBean = null;

      if ( goalLevel != null )
      {
        if ( goalQuestPromotion.isGoalQuestPromotion() )
        {
          selectedGoalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( paxGoal, goalQuestPromotion, (GoalLevel)paxGoal.getGoalLevel() );
        }
        else if ( goalQuestPromotion.isChallengePointPromotion() )
        {
          selectedCPLevelValueBean = getChallengePointService()
              .populateChallengepointPaxValueBean( paxGoal, (ChallengePointPromotion)goalQuestPromotion, paxGoal != null ? (GoalLevel)paxGoal.getGoalLevel() : null );
        }
      }

      if ( UserManager.getUser().isParticipant() )
      {
        request.getSession().setAttribute( "paxGoal", paxGoal );
        request.getSession().setAttribute( "paxPartnerList", paxPartnerList );
        if ( goalQuestPromotion.isGoalQuestPromotion() )
        {
          request.getSession().setAttribute( "goalLevelValueBean", selectedGoalLevelValueBean );
        }
        else if ( goalQuestPromotion.isChallengePointPromotion() )
        {
          request.getSession().setAttribute( "goalLevelValueBean", selectedCPLevelValueBean );
          request.getSession().setAttribute( "thresholdApplicable", isAwardThresholdApplicable( (ChallengePointPromotion)goalQuestPromotion ) );
        }
        request.getSession().setAttribute( "newGoal", Boolean.valueOf( wizard.isNew() ) );
        request.getSession().setAttribute( "goalSelectionModal", Boolean.TRUE );
      }
    }
    request.getSession().removeAttribute( "goalQuestWizardForm" );

    // new forward logic added based on defect #4012
    if ( UserManager.getUser().isParticipant() )
    {
      Long userId = UserManager.getUserId();
      List<PromotionGoalQuestSurvey> listpgqsurvey = getSurveyService().getPromotionGoalQuestSurveysByPromotionId( goalQuestPromotion.getId() );
      boolean isSurveyTaken = false;
      for ( PromotionGoalQuestSurvey pGQuestSurvey : listpgqsurvey )
      {
        ParticipantSurvey participantSurvey = getParticipantSurveyService().getParticipantSurveyByPromotionAndSurveyIdAndUserId( pGQuestSurvey.getPromotion().getId(),
                                                                                                                                 pGQuestSurvey.getSurvey().getId(),
                                                                                                                                 userId );
        if ( participantSurvey != null && participantSurvey.isCompleted() )
        {
          isSurveyTaken = true;
          break;
        }
      }

      if ( listpgqsurvey != null && !isSurveyTaken && !listpgqsurvey.isEmpty() && listpgqsurvey.size() > 0 )
      {
        request.getSession().setAttribute( "hasSurvey", Boolean.valueOf( true ) );

        forwardAction = "take_survey";
        Map clientStateParameterMap = new HashMap();
        clientStateParameterMap.put( "promotionId", goalQuestPromotion.getId() );
        clientStateParameterMap.put( "promotionCode", goalQuestPromotion.getPromotionType().getCode() );
        String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
        return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=display" } );
      }
      else
      {
        request.getSession().setAttribute( "hasSurvey", Boolean.valueOf( false ) );

        response.sendRedirect( request.getContextPath() + "/homePage.do" + RequestUtils.getHomePageFilterToken( request ) );

        // return mapping.findForward( forwardAction );
        return null;
      }
    }
    else
    {
      String queryString = null;
      String path = null;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "promotionId", new Long( wizard.getPromotionId() ) );
      clientStateParameterMap.put( "userId", new Long( wizard.getUserId() ) );

      queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      if ( goalQuestPromotion != null )
      {
        if ( goalQuestPromotion.isGoalQuestPromotion() )
        {
          path = "admin_promo_details";
        }
        else if ( goalQuestPromotion.isChallengePointPromotion() )
        {
          path = "admin_cp_promo_details";
        }
      }
      return ActionUtils.forwardWithParameters( mapping, path, new String[] { queryString } );
    }

  }

  /*
   * Instead of returning all the level, only return the products for the specified level
   */
  public ActionForward getAwardsForLevel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    GoalQuestWizardForm wizard = (GoalQuestWizardForm)form;
    buildPromotionInfo( wizard, request );

    PlateauAwardsView plateauAwards = buildPlateauAwardsView( wizard );
    PaxGoal goal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( wizard.getPromotionId(), getUserId( request ) );
    // ok, remove all the levels not associated with the selected level
    if ( goal != null )
    {
      Iterator<GoalAwardView> goalLevels = plateauAwards.getPromotions().get( 0 ).getLevels().iterator();
      while ( goalLevels.hasNext() )
      {
        GoalAwardView level = goalLevels.next();
        if ( !level.getId().equals( goal.getGoalLevel().getId().toString() ) )
        {
          goalLevels.remove();
        }
      }
    }

    super.writeAsJsonToResponse( plateauAwards, response );
    return null;
  }

  public ActionForward getPromotionAwards( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    GoalQuestWizardForm wizard = (GoalQuestWizardForm)form;
    PlateauAwardsView plateauAwards = buildPlateauAwardsView( wizard );
    super.writeAsJsonToResponse( plateauAwards, response );
    return null;
  }

  private PlateauAwardsView buildPlateauAwardsView( GoalQuestWizardForm wizard ) throws Exception
  {
    PlateauAwardsView plateauAwards = new PlateauAwardsView();
    List<PromotionAwardView> promotions = new ArrayList<PromotionAwardView>();
    PromotionAwardView promotion = new PromotionAwardView();
    promotion.setName( wizard.getPromotionName() );
    promotion.setId( wizard.getPromotionId().toString() );
    promotion.setLevels( buildGoalAwardLevels( wizard.getPromotion() ) );
    promotions.add( promotion );
    plateauAwards.setPromotions( promotions );

    return plateauAwards;
  }

  public ActionForward updateAwards( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    GoalQuestWizardForm wizard = (GoalQuestWizardForm)form;
    String awardId = request.getParameter( "awardId" );
    PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( wizard.getPromotionId(), getUserId( request ) );
    if ( null != awardId )
    {
      paxGoal.setProductSetId( awardId );
      getPaxGoalService().savePaxGoal( paxGoal );
    }

    super.writeAsJsonToResponse( new MessageView(), response );
    return null;
  }

  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return load( actionMapping, actionForm, request, response );
  }

  @SuppressWarnings( "unchecked" )
  protected void buildPromotionInfo( GoalQuestWizardForm wizard, HttpServletRequest request )
  {
    Long promotionId = buildPromotionId( request );
    wizard.setPromotionId( promotionId );
    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    ascReqColl.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( wizard.getPromotionId(), ascReqColl );
    wizard.setPromotion( goalQuestPromotion );
    if ( goalQuestPromotion.isPartnersEnabled() )
    {
      wizard.setMaxPartnersInput( goalQuestPromotion.getPartnerCount() );
    }
    wizard.setPromotionType( goalQuestPromotion.getPromotionType().getCode() );

    if ( goalQuestPromotion.isAllowUnderArmour() && getUnderArmourService().uaEnabled() )
    {
      // Trying to deactive exist UA user information from session through browser call
      wizard.setUaLogOutUrl( getSystemVariableService().getPropertyByName( SystemVariableService.UNDERARMOUR_SESSION_LOGOUT_URL_PREFIX ).getStringVal() );

      try
      {
        wizard.setUaConnected( getUnderArmourService().isParticipantAuthorized( UserManager.getUserId() ) );
      }
      catch( Exception e )
      {
        log.error( e.getMessage(), e );
      }
      wizard.setUaEnabled( true );
    }
    else
    {
      wizard.setUaEnabled( false );
      wizard.setUaConnected( false );
    }

  }

  protected void buildPartnerInfo( GoalQuestWizardForm wizard, Long userId )
  {
    if ( wizard.getPromotion().isPartnersEnabled() )
    {
      ParticipantAssociationRequest associations = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
      List<ParticipantPartner> partners = getPromotionService().getPartnersByPromotionAndParticipantWithAssociations( wizard.getPromotionId(), userId, associations );
      wizard.setPreselectedPartners( buildPartnerList( partners ) );

      if ( wizard.getPromotion().isAutoCompletePartners() && wizard.isNew() && wizard.getPreselectedPartners() != null )
      {
        wizard.setPreselectedPartners( getGoalQuestService().getGoalQuestMiniProfilesForUserAndPromotion( wizard.getPromotionId(), userId ) );
      }
    }
  }

  protected List<ParticipantSearchView> buildPartnerList( List<ParticipantPartner> partners )
  {
    List<String> partnerPaxIds = new ArrayList<String>();
    for ( ParticipantPartner partner : partners )
    {
      partnerPaxIds.add( String.valueOf( partner.getPartner().getId() ) );
    }
    if ( partnerPaxIds.isEmpty() )
    {
      return new ArrayList<ParticipantSearchView>();
    }
    String ids = StringUtils.collectionToDelimitedString( partnerPaxIds, "," );
    return getParticipantService().getParticipatForMiniProfile( ids );
  }

  protected List<ParticipantPartner> buildParticipantPartnerList( GoalQuestWizardForm wizard, GoalQuestPromotion promotion, Long userId )
  {
    List<ParticipantPartner> partners = new ArrayList<ParticipantPartner>();
    Participant me = SelectGoalUtil.getParticipant( userId );
    for ( ParticipantInfoView partner : wizard.getPartnersAsList() )
    {
      ParticipantPartner participantPartner = new ParticipantPartner();
      participantPartner.setParticipant( me );
      participantPartner.setPromotion( promotion );
      participantPartner.setPartner( SelectGoalUtil.getParticipant( partner.getId() ) );
      partners.add( participantPartner );
    }
    return partners;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  protected void buildGoalInfo( GoalQuestWizardForm wizard, HttpServletRequest request ) throws ServiceErrorException
  {
    List goalLevelBeans = null;
    List cpLevelBeans = null;
    ChallengepointPaxValueBean cpBean = null;

    PaxGoal goal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( wizard.getPromotionId(), getUserId( request ) );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );

    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( wizard.getPromotionId(), promoAssociationRequestCollection );

    if ( goalQuestPromotion != null )
    {
      goalLevelBeans = getParticipantGoalLevelBeans( goalQuestPromotion, goal );
      request.setAttribute( "goalLevelBeans", goalLevelBeans );
      request.setAttribute( "promotion", goalQuestPromotion );

      if ( goalQuestPromotion.isChallengePointPromotion() )
      {
        cpLevelBeans = getParticipantCPLevelBeans( (ChallengePointPromotion)goalQuestPromotion, goal, wizard.getUserId() );
        ChallengepointPaxValueBean paxValueBean = (ChallengepointPaxValueBean)cpLevelBeans.get( 0 );
        wizard.setChallengepointPaxValueBean( paxValueBean );
        request.setAttribute( "cpLevelBeans", cpLevelBeans );
        request.setAttribute( "thresholdApplicable", isAwardThresholdApplicable( (ChallengePointPromotion)goalQuestPromotion ) );
      }
    }
    assignSelectedGoal( goal, wizard );
  }

  private void assignSelectedGoal( PaxGoal goal, GoalQuestWizardForm wizard ) throws ServiceErrorException
  {
    if ( goal != null )// start goal pre-population
    {
      if ( goal.getGoalLevel() != null )
      {
        wizard.setSelectedGoalId( goal.getGoalLevel().getId() );
        wizard.setActiveStep( "stepGoal" );
        wizard.setNew( false );
      }
      wizard.setSelectedProductId( goal.getProductSetId() );
      String productSetId = goal.getProductSetId();
      if ( null != productSetId )
      {
        List<GoalAwardView> awards = buildGoalAwardLevels( wizard.getPromotion() );
        for ( GoalAwardView award : awards )
        {
          for ( AwardProductView product : award.getProducts() )
          {
            if ( product.getId().equals( productSetId ) )
            {
              wizard.setSelectedProductName( product.getName() );
              wizard.setSelectedProductImgUrl( product.getImg() );
              return;
            }
          }
        }
      }

      if ( wizard.isUaEnabled() && !wizard.isUaConnected() )
      {
        wizard.setActiveStep( "stepUAConnect" );
      }
    }
    else
    {
      wizard.setActiveStep( "stepOverview" );
    }
  }

  private List<GoalLevelValueBean> getParticipantGoalLevelBeans( GoalQuestPromotion promotion, PaxGoal paxGoal )
  {
    List<GoalLevelValueBean> goalLevelBeans = new ArrayList<GoalLevelValueBean>();
    if ( promotion.getGoalLevels() != null )
    {
      for ( Iterator<AbstractGoalLevel> iter = promotion.getGoalLevels().iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        paxGoal = SelectGoalUtil.getLevelSpecificGoal( paxGoal, promotion, goalLevel );
        goalLevelBeans.add( SelectGoalUtil.populateGoalLevelValueBean( paxGoal, promotion, goalLevel ) );
      }
    }
    return goalLevelBeans;
  }

  private List<ChallengepointPaxValueBean> getParticipantCPLevelBeans( ChallengePointPromotion promotion, PaxGoal paxGoal, Long userId )
  {
    List<ChallengepointPaxValueBean> goalLevelBeans = new ArrayList<ChallengepointPaxValueBean>();
    if ( promotion.getGoalLevels() != null )
    {
      for ( Iterator<AbstractGoalLevel> iter = promotion.getGoalLevels().iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        paxGoal = getChallengePointService().getLevelSpecificChallengePoint( paxGoal, promotion, goalLevel );
        try
        {
          goalLevelBeans.add( getChallengePointService().populateChallengepointPaxValueBean( paxGoal, promotion, paxGoal != null ? (GoalLevel)paxGoal.getGoalLevel() : null, userId ) );
        }
        catch( ServiceErrorException e )
        {
          log.error( e.getMessage(), e );
        }
      }
    }
    return goalLevelBeans;
  }

  /*
   * All of this rigum-roll is to allow the admins to use the same screens when selecting a level
   * for a user
   */
  protected Long getUserId( HttpServletRequest request )
  {
    String id = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( null != id )
    {
      return new Long( id );
    }
    else
    {
      GoalQuestWizardForm form = (GoalQuestWizardForm)request.getSession().getAttribute( "goalQuestWizardForm" );
      if ( form.getUserId() != null )
      {
        return form.getUserId();
      }
      else
      {
        return UserManager.getUserId();
      }
    }
  }

  public boolean isAwardThresholdApplicable( ChallengePointPromotion promotion )
  {
    return ! ( promotion != null && promotion.getAwardThresholdType() != null && promotion.getAwardThresholdType().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) );
  }
}

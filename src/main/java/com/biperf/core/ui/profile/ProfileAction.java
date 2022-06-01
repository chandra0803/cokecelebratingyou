
package com.biperf.core.ui.profile;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.ReportCategoryType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.participant.AwardBanqResponseView;
import com.biperf.core.domain.participant.DelegatorView;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantAlertView;
import com.biperf.core.domain.participant.ParticipantProfileView;
import com.biperf.core.domain.participant.SidebarModuleBean;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.managertoolkit.AlertMessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.plateauawards.PlateauAwardsService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.recognitionadvisor.RecognitionAdvisorService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.homepage.ShopAndTravelView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.EngagementPromotionData;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.SurveyPageValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * @author dudam
 * @since Dec 19, 2012
 * @version 1.0
 */
public class ProfileAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ProfileAction.class );

  private static final String EACH_LEVEL = "eachLevel";

  private static final String BUDGET_METER = "budgetTracker";
  private static final String GAMIFICATION = "gamification";
  private static final String ENGAGEMENT = "engagement";
  private static final String WORK_HAPPIER = "workHappier";
  //client customization
  private static final String BUNCH_BALL = "bunchBall";

  private static final String BUDGET_MODULE_NAME = "budgetTrackerModule";
  private static final String GAMIFICATION_MODULE_NAME = "gamificationModule";
  private static final String ENGAGEMENT_MODULE_NAME = "engagementModule";
  private static final String WORK_HAPPIER_MODULE_NAME = "workHappierPaxModule";
  //client customization
  private static final String BUNCHBALL_MODULE_NAME = "bunchBallSideBarModule";

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   * 
   * When ever page reloads this method will called to get the user info. Which is available on the top right corner.
   */
  public ActionForward fetchUserACInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ParticipantProfileView paxProfileView = new ParticipantProfileView();

    AuthenticatedUser authUser = UserManager.getUser();
    if ( authUser != null && ( !authUser.isShowTermsAndConditions() || authUser.isLaunched() ) )
    {
      paxProfileView.getParticipant().setTermsAcceptedMsg( CmsResourceBundle.getCmsBundle().getString( "profile.page.TERMS_ACCEPTED_MSG" ) );
      authUser.setRouteId( request.getHeader( "proxy-jroute" ) );

      paxProfileView.getParticipant().setFirstName( authUser.getFirstName() );
      paxProfileView.getParticipant().setId( authUser.getUserId() );
      paxProfileView.getParticipant().setLastName( authUser.getLastName() );
      if ( authUser.isParticipant() )
      {
        ProjectionCollection projections = new ProjectionCollection();
        projections.add( new ProjectionAttribute( "avatarOriginal" ) );
        projections.add( new ProjectionAttribute( "termsAcceptance" ) );
        Participant participant = getParticipantService().getParticipantByIdWithProjections( authUser.getUserId(), projections );
        paxProfileView.getParticipant().setAvatarUrl( participant.getAvatarOriginalFullPath() );
        paxProfileView.getParticipant().setGlobalParticipantSearchEnabled( getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_LARGE_AUDIENCE ).getBooleanVal() );
      }
      else
      {
        paxProfileView.getParticipant().setAvatarUrl( null );
      }

      List<Participant> delegateList = new ArrayList<Participant>();
      if ( authUser.isDelegate() )
      {
        delegateList = authUser.getOriginalAuthenticatedUser().getDelegateList();
      }
      else
      {
        delegateList = authUser.getDelegateList();
      }

      if ( delegateList != null && delegateList.size() > 0 )
      {
        List<DelegatorView> delegateViews = new ArrayList<DelegatorView>();
        boolean isTermsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
        for ( Participant proxy : delegateList )
        {
          if ( proxy.isActive() )
          {
            DelegatorView delegate = new DelegatorView();
            delegate.setId( proxy.getId() );
            delegate.setFirstName( proxy.getFirstName() );
            delegate.setLastName( proxy.getLastName() );
            boolean termsAccepted = ParticipantTermsAcceptance.ACCEPTED.equalsIgnoreCase( proxy.getTermsAcceptance().getCode() );
            // If not used TnCs, allow delegates. If using TnCs, they need to have accepted
            delegate.setAllowDelegate( isTermsAndConditionsUsed ? termsAccepted : true );
            delegateViews.add( delegate );
          }
        }
        paxProfileView.getParticipant().setDelegators( delegateViews );

      }
      if ( authUser.isDelegate() )
      {
        paxProfileView.getParticipant().setDelegate( true );
        paxProfileView.getParticipant().setDelegateFirstName( authUser.getOriginalAuthenticatedUser().getFirstName() );
        paxProfileView.getParticipant().setDelegateLastName( authUser.getOriginalAuthenticatedUser().getLastName() );
      }

      if ( authUser.isLaunched() )
      {
        paxProfileView.getParticipant().setLaunched( true );
      }

      // check for the modules names and add the data accordingly
      this.populateApplicableModules( request, paxProfileView );

    }
    super.writeAsJsonToResponse( paxProfileView, response );
    return null;
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   * 
   * When ever page reloads this method will called to get the user points. Which is available on the top right corner.
   */
  public ActionForward fetchUserPointsInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PointsView pointsView = (PointsView)request.getSession().getAttribute( "pointsView" );
    AwardBanqResponseView awardBanqResponseView = new AwardBanqResponseView();
    if ( null == pointsView )
    {
      ProfilePointsView profilePointsView = null;
      pointsView = new PointsView();
      try
      {
        SystemVariableService service = (SystemVariableService)getService( SystemVariableService.BEAN_NAME );

        boolean includeUserBalance = service.getPropertyByName( SystemVariableService.BOOLEAN_INCLUDE_BALANCE ).getBooleanVal();

        AuthenticatedUser authUser = UserManager.getUser();
        if ( authUser != null && authUser.getUserId() != null )
        {
          if ( includeUserBalance && authUser.isParticipant() && !isGiftCodeOnlyPax() )
          {
            ProjectionCollection projections = new ProjectionCollection();
            projections.add( new ProjectionAttribute( "awardBanqNumberDecrypted" ) );

            if ( !authUser.isDelegate() )
            {
              Participant pax = getParticipantService().getParticipantByIdWithProjections( authUser.getUserId(), projections );
              awardBanqResponseView = getAwardBanQService().buildAwardBanqResponseView( authUser.getUserId(), pax.getAwardBanqNumber() );
            }

            String balanceAvailable = awardBanqResponseView != null ? awardBanqResponseView.getBalanceAvailable() : null;
            Long balance = StringUtils.isNotBlank( balanceAvailable ) ? Long.valueOf( balanceAvailable ) : -1L;
            authUser.setAwardBanQBalance( balance );
            profilePointsView = new ProfilePointsView( balance );

            boolean mePlusEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.MEPLUS_ENABLED ).getBooleanVal();

            if ( mePlusEnabled )
            {
              if ( balance.equals( new Long( 0 ) ) )
              {
                if ( !getMainContentService().checkShowShopORTravel() )
                {
                  // need -1 to hide the points section for non pax and gift card only
                  profilePointsView = new ProfilePointsView( new Long( -1 ) );
                }
              }
            }
          }
          else
          {
            // need -1 to hide the points section for non pax and gift card only
            profilePointsView = new ProfilePointsView( new Long( -1 ) );
          }
          if ( profilePointsView != null )
          {
            profilePointsView.setName( WebResponseConstants.RESPONSE_TYPE_NAME_PAX );
            profilePointsView.setType( WebResponseConstants.RESPONSE_TYPE_TYPE_PAX );
            // when acting as delegate or shop link is not enabled then there is no point in
            // showing the points to the user
            ShopAndTravelView shopAndTravel = (ShopAndTravelView)request.getSession().getAttribute( "showAndTravelView" );
            if ( UserManager.getUser().isDelegate() || ( shopAndTravel != null && !shopAndTravel.isShowShop() ) )
            {
              profilePointsView.getData().setShowPoints( false );
            }

            pointsView.getMessages().add( profilePointsView );
          }
        }
        request.getSession().setAttribute( "pointsView", pointsView );
      }
      catch( Exception e )
      {
        logger.error( "Error during getting user account balance from shared services.", e );
        profilePointsView = new ProfilePointsView( new Long( -1 ) );
        profilePointsView.setName( WebResponseConstants.RESPONSE_TYPE_NAME_PAX );
        profilePointsView.setType( WebResponseConstants.RESPONSE_TYPE_TYPE_PAX );
        pointsView.getMessages().add( profilePointsView );
      }
    }
    super.writeAsJsonToResponse( pointsView, response );
    return null;
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   * 
   * When ever page reloads this method will called to get the user alerts. Which is available on the top right corner.
   */

  public ActionForward fetchUserAlrtsInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ProfileAlertsView profileAlertsView = new ProfileAlertsView();
    Alerts alerts = new Alerts();
    AuthenticatedUser authUser = UserManager.getUser();
    List<ParticipantAlert> managerAlertList = null;
    List<AlertsValueBean> raAlertList = null;
    // Get List of alerts by UserId if user is of type pax
    if ( UserManager.getUser().isParticipant() && !UserManager.getUser().isDelegate() )
    {
      List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
      managerAlertList = getAlertMessageService().getActiveAlertMessagesByUserId( authUser.getUserId() );

      // build alerts if user has eligliable promotions
      List<AlertsValueBean> promoAlertList = getAlertsList( authUser, eligiblePromotions, request );
      if ( eligiblePromotions.size() > 0 || managerAlertList.size() > 0 || NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled()
          || ( Objects.nonNull( promoAlertList ) && promoAlertList.size() > 0 ) )
      {
        List<AlertsValueBean> alertList = new ArrayList<AlertsValueBean>();
        Set<Long> contestIds = new HashSet<Long>();
        if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() && ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() )
            && !UserManager.isUserDelegateOrLaunchedAs() )
        {
          raAlertList = getRecognitionAdvisorService().checkNewEmployeeAndTeamMemberExist( authUser.getUserId() );
          alertList.addAll( raAlertList );
        }
        alertList.addAll( promoAlertList );
        if ( managerAlertList != null || raAlertList != null )
        {
          for ( ParticipantAlert paxAlert : managerAlertList )
          {
            if ( paxAlert.getAuditCreateInfo().getCreatedBy().equals( UserManager.getUserId() ) )
            {
              continue;
            }
            AlertsValueBean alertsBean = new AlertsValueBean();
            if ( paxAlert.getAlertMessage().getContestId() != null )
            {
              String ssiAlertType = paxAlert.getAlertMessage().getSsiAlertType();
              if ( SSIContest.CONTEST_ROLE_APPROVER.equals( ssiAlertType ) )
              {
                alertsBean.setActivityType( ActivityType.SSI_APPROVAL );
              }
              else if ( SSIContest.CONTEST_ROLE_CREATOR.equals( ssiAlertType ) )
              {
                alertsBean.setActivityType( ActivityType.SSI_PROGRESS_LOAD_CREATOR_ALERT );
              }
              else if ( SSIContest.CONTEST_ROLE_MGR.equals( ssiAlertType ) )
              {
                alertsBean.setActivityType( ActivityType.SSI_PROGRESS_LOAD_MGR_ALERT );
              }
              else if ( SSIContest.CONTEST_ROLE_PAX.equals( ssiAlertType ) )
              {
                alertsBean.setActivityType( ActivityType.SSI_PROGRESS_LOAD_PAX_ALERT );
              }
              else if ( SSIContest.CONTEST_ROLE_CLAIM_APPROVER.equals( ssiAlertType ) )
              {
                alertsBean.setActivityType( ActivityType.SSI_CLAIM_APPROVAL );
              }
              contestIds.add( paxAlert.getAlertMessage().getContestId() );
            }
            else
            {
              alertsBean.setActivityType( ActivityType.MANAGER_ALERT );
            }
            alertsBean.setParticipantAlert( paxAlert );
            alertList.add( alertsBean );
          }
        }

        if ( alertList.size() > 0 )
        {
          List<ParticipantAlertView> paxAlertViewList = new ArrayList<ParticipantAlertView>();
          for ( AlertsValueBean bean : alertList )
          {
            ParticipantAlertView paxAlertView = new ParticipantAlertView();
            paxAlertView.setDisplayAlert( Boolean.valueOf( request.getParameter( "showAlert" ) ) );
            Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
            if ( bean.getPromotion() != null )
            {
              clientStateParameterMap.put( "promotionId", bean.getPromotion().getId() );
            }
            if ( bean.getParticipantAlert() != null && bean.getParticipantAlert().getAlertMessage() != null && bean.getParticipantAlert().getAlertMessage().getContestId() != null )
            {
              clientStateParameterMap.put( "contestId", bean.getParticipantAlert().getAlertMessage().getContestId() );
            }
            if ( bean.getActivityType().equals( ActivityType.MANAGER_ALERT ) )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_ALERT" ) );
              paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.ALERT_FROM_MANAGER" ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + "/participantProfilePage.do#tab/AlertsAndMessages" );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.SURVEY_ALERT ) )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_ALERT" ) );
              List<SurveyPageValueBean> pendingSurveys = getPromotionService().getPendingSurveysList( UserManager.getUserId() );
              if ( pendingSurveys != null && pendingSurveys.size() > 1 )
              {
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_SURVEYS_TO_COMPLETE" ), new Object[] { pendingSurveys.size() } ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_SURVEY_LIST, clientStateParameterMap ) );
              }
              else
              {
                paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SURVEY_TO_COMPLETE" ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.TAKE_SURVEY, clientStateParameterMap ) );
              }
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.AWARD_REMINDER ) )
            {
              AuthenticatedUser user = UserManager.getUser();
              HttpSession session = request.getSession();
              Long loginUserId = (Long)session.getAttribute( "loginUserId" );
              if ( user.isLaunched() && loginUserId != null )
              {
                if ( getRoleService().getUserRoleBypassingUserIdAndRoleCode( loginUserId ) )
                {
                  paxAlertView.setPlateauRedemption( getRoleService().getUserRoleBypassingUserIdAndRoleCode( loginUserId ) );
                }
              }
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_AWARD" ) );
              if ( StringUtils.isNotEmpty( bean.getSaCelebrationId() ) )
              {
                paxAlertView
                    .setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_AWARDS_TO_REDEEM_PLATEAU" ), new Object[] { bean.getProgramName() } ) );
                paxAlertView.setCelebrationId( bean.getSaCelebrationId() );
                paxAlertView.setSaGiftCode( true );
              }
              else
              {
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_AWARDS_TO_REDEEM_PLATEAU" ),
                                                                  new Object[] { bean.getPromotion().getName() } ) );
                paxAlertView.setAlertLink( bean.getOnlineShoppingUrl() );
              }
              paxAlertView.setOpenNewWindow( true );
            }
            else if ( bean.getActivityType().equals( ActivityType.BUDGET_END ) )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEND_RECOGNITION" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_BUDGET_END" ),
                                                                new Object[] { bean.getPromotion().getName(), bean.getBudgetEndDate() } ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_SUBMIT_RECOGNITION, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.SUBMIT_PROMOTION ) || bean.getPromotion() != null && PromotionType.QUIZ.equals( bean.getPromotion().getPromotionType().getCode() ) )
            {
              paxAlertView.setOpenNewWindow( false );
              boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
              if ( PromotionType.GOALQUEST.equals( bean.getPromotion().getPromotionType().getCode() ) && !isLoginAs )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_GOAL" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_GOAL_DUE_DATE" ).replace( "\'", "''" ),
                                                                  new Object[] { bean.getPromotion().getName().toString(),
                                                                                 DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( bean.getGoalSelectionEnddate() ),
                                                                                                                                     UserManager.getTimeZoneID() ) ) } ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_GOALQUEST_SELECT_YOUR_GOAL, clientStateParameterMap ) );
              }
              else if ( PromotionType.CHALLENGE_POINT.equals( bean.getPromotion().getPromotionType().getCode() ) && !isLoginAs )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_GOAL" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_CP_DUE_DATE" ),
                                                                  new Object[] { bean.getPromotion().getName().toString(),
                                                                                 DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( bean.getCpSelectionEndDate() ),
                                                                                                                                     UserManager.getTimeZoneID() ) ) } ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_GOALQUEST_SELECT_YOUR_GOAL, clientStateParameterMap ) );
              }
              else if ( PromotionType.QUIZ.equals( bean.getPromotion().getPromotionType().getCode() ) || PromotionType.DIY_QUIZ.equals( bean.getPromotion().getPromotionType().getCode() ) )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.TAKE_QUIZ" ) );
                paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.PENDING_QUIZ" ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_QUIZ_CLAIM_SUBMISSION, clientStateParameterMap ) );
                paxAlertView.setOpenNewWindow( false );
              }
            }
            else if ( bean.getActivityType().equals( ActivityType.PURL_CONTRIBUTION_DEFAULT_INVITEE ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
            {
              boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
              if ( !isLoginAs )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INVITATION_RECOGNITION_PURL" ),
                                                                  new Object[] { bean.getPromotion().getName() } ) );

                paxAlertView.setOpenNewWindow( false );
                clientStateParameterMap.put( "isDefaultInvitee", true );
                if ( bean.getPurlContributorId() != null )
                {
                  clientStateParameterMap.put( "purlContributorId", bean.getPurlContributorId() );
                  paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_SINGLE, clientStateParameterMap ) );
                }
                else
                {
                  paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_LIST, clientStateParameterMap ) );
                }
              }
            }
            else if ( bean.getActivityType().equals( ActivityType.PURL_CONTRIBUTION_NON_DEFAULT_INVITEE ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
            {
              boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
              if ( !isLoginAs )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INTEREST_RECOGNITION_PURL" ),
                                                                  new Object[] { bean.getPromotion().getName() } ) );
                clientStateParameterMap.put( "isDefaultInvitee", false );
                paxAlertView.setOpenNewWindow( false );
                if ( bean.getPurlContributorId() != null )
                {
                  clientStateParameterMap.put( "purlContributorId", bean.getPurlContributorId() );
                  paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_SINGLE, clientStateParameterMap ) );
                }
                else
                {
                  paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_LIST, clientStateParameterMap ) );
                }
              }
            }
            else if ( bean.getActivityType().equals( ActivityType.PURL_CONTRIBUTION ) && NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
            {
              boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
              if ( !isLoginAs )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );
                paxAlertView
                    .setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INVITATION_FOR_NEW_SA" ), new Object[] { bean.getProgramName() } ) );
                if ( bean.getSaCelebrationId() != null )
                {
                  paxAlertView.setOpenNewWindow( true );
                  paxAlertView.setCelebrationId( bean.getSaCelebrationId() );
                }
                else
                {
                  paxAlertView.setOpenNewWindow( false );
                  clientStateParameterMap.put( "programId", bean.getProgramId() );
                  paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_SA_CONTRIBUTION_LIST, clientStateParameterMap ) );
                }
              }
            }
            else if ( bean.getActivityType().equals( ActivityType.PURL_INVITATION ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() ) // Not
                                                                                                                                                     // Applicable
                                                                                                                                                     // For
                                                                                                                                                     // New
                                                                                                                                                     // SA
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEND_INVITATIONS" ) );
              paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.PENDING_PURL" ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_MAINTENANCE_LIST, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.PURL_VIEW ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_PURL" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "VIEW_YOUR_RECOG_PURL" ),
                                                                new Object[] { bean.getPromotion().getName().toString() } ) );
              clientStateParameterMap.put( "purlRecipientId", bean.getPurlRecipientId() );
              clientStateParameterMap.put( "loggedinUserId", UserManager.getUserId() );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_RECIPIENT, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.NOMINATOR_REQMOREINFO_ALERT ) )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NOMINATOR_MOREINFO" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "NOMINATOR_MOREINFO_SUBJECT" ),
                                                                new Object[] { bean.getPromotion().getName().toString() } ) );
              clientStateParameterMap.put( "claimId", bean.getClaimId() );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.NOMINATOR_REQUEST_MORE_INFO, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.PENDING_APPROVALS ) )
            {
              if ( bean.getPromotion() != null && PromotionType.RECOGNITION.equals( bean.getPromotion().getPromotionType().getCode() ) )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_APPROVALS_TO_REVIEW" ),
                                                                  new Object[] { bean.getPromotion().getName().toString() } ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_RECOGNITION_APPROVAL_LIST, clientStateParameterMap ) );
              }
              else if ( bean.getPromotion() != null && PromotionType.PRODUCT_CLAIM.equals( bean.getPromotion().getPromotionType().getCode() ) )
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_APPROVALS_TO_REVIEW" ),
                                                                  new Object[] { bean.getPromotion().getName().toString() } ) );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_CLAIM_PRODUCT_APPROVE_LIST, clientStateParameterMap ) );
              }
            }
            else if ( bean.getActivityType().equals( ActivityType.PLATEAU_AWARD_REMINDER ) )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEND_REMINDER" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_AWARDS_TO_REDEEM" ),
                                                                new Object[] { bean.getNumberOfPaxWithUnclaimedAwards() } ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PLATEAU_AWARD_REMINDERS, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.AWARD_GENERATOR_MANAGER_REMINDER ) )
            {
              paxAlertView.setAlertLinkText( "View Alert" );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "QUALIFIED_TEAM_MEMBERS_THIS_MONTH" ),
                                                                new Object[] { bean.getPromotion().getName() } ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + "/participantProfilePage.do#tab/AlertsAndMessages" );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.GQ_SURVEY ) )
            {
              GoalQuestPromotion gqPromo = (GoalQuestPromotion)bean.getPromotion();
              Date surveyEndDate = new Date( DateUtils.toEndDate( gqPromo.getGoalCollectionEndDate() ).getTime()
                  + new Integer( 14 ).longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
              Date actualSurveyEndDate = new Date();
              if ( surveyEndDate.after( DateUtils.toEndDate( gqPromo.getSubmissionEndDate() ) ) )
              {
                actualSurveyEndDate = gqPromo.getSubmissionEndDate();
              }
              else
              {
                actualSurveyEndDate = surveyEndDate;
              }

              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.TAKE_SURVEY" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_TAKE_SURVEY_DUE_DATE" ),
                                                                new Object[] { bean.getPromotion().getName(),
                                                                               DateUtils.toDisplayString( DateUtils.applyTimeZone( actualSurveyEndDate, UserManager.getTimeZoneID() ) ) } ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_SURVEY, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.CELEBRATION_MANAGER_REMINDER_ALERT ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() ) // Not
                                                                                                                                                                        // Applicable
                                                                                                                                                                        // For
                                                                                                                                                                        // New
                                                                                                                                                                        // SA
            {
              if ( bean.getCelebrationManagerMessageId() != null )
              {
                CelebrationManagerMessage celebrationManagerMessage = getCelebrationService().getCelebrationManagerMessageById( bean.getCelebrationManagerMessageId() );
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.ADD_CELEBRATION_MESSAGE" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "UPCOMING_CELEBRATION" ),
                                                                  new Object[] { celebrationManagerMessage.getRecipient().getFirstName(), celebrationManagerMessage.getRecipient().getLastName() } ) );
                paxAlertView.setOpenNewWindow( false );

                clientStateParameterMap.put( "managerMessageId", bean.getCelebrationManagerMessageId() );
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_CELEBRATION_MANAGER_MESSAGE, clientStateParameterMap ) );
              }
            }
            else if ( bean.getActivityType().equals( ActivityType.FILE_DOWNLOAD ) )
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.DOWNLOAD" ) );

              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_FILE_EXTRACT" ),
                                                                new Object[] { bean.getFileName() != null ? bean.getFileName() : "", bean.getFileDownloadExpiryDate() } ) );

              clientStateParameterMap.put( "fileStoreId", bean.getFileStoreId() );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_FILE_DOWNLOAD, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.SSI_APPROVAL ) )
            {
              List<NameIdBean> contestNames = getSSIContestService().getContestNames( contestIds, UserManager.getLocale().toString() );
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
              for ( NameIdBean nameIdBean : contestNames )
              {
                if ( nameIdBean.getId().equals( bean.getParticipantAlert().getAlertMessage().getContestId() ) )
                {
                  paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_MESG" ), new Object[] { nameIdBean.getName() } ) );
                }
              }
              SSIContest contest = getSSIContestService().getContestById( bean.getParticipantAlert().getAlertMessage().getContestId() );
              if ( SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
              {
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_ISSUANCE_APPROVAL_SUMMARY, clientStateParameterMap ) );
              }
              else
              {
                paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_APPROVAL_SUMMARY, clientStateParameterMap ) );
              }
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.SSI_PROGRESS_LOAD_CREATOR_ALERT ) )
            {
              SSIContest contest = getSSIContestService().getContestById( bean.getParticipantAlert().getAlertMessage().getContestId() );
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_PROGRESS" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_CONTEST_PROGRESS" ),
                                                                new Object[] { contest.getContestNameFromCM() } ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + "/participantProfilePage.do#tab/AlertsAndMessages" );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.SSI_PROGRESS_LOAD_PAX_ALERT ) || bean.getActivityType().equals( ActivityType.SSI_PROGRESS_LOAD_MGR_ALERT ) )
            {
              SSIContest contest = getSSIContestService().getContestById( bean.getParticipantAlert().getAlertMessage().getContestId() );
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_PROGRESS" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_PROGRESS_LOAD_MESG" ),
                                                                new Object[] { contest.getContestNameFromCM() } ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + "/participantProfilePage.do#tab/AlertsAndMessages" );
              paxAlertView.setOpenNewWindow( false );

            }
            else if ( bean.getActivityType().equals( ActivityType.SSI_CLAIM_APPROVAL ) )
            {
              SSIContest contest = getSSIContestService().getContestById( bean.getParticipantAlert().getAlertMessage().getContestId() );
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
              paxAlertView
                  .setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_CLAIM_APPROVAL" ), new Object[] { contest.getContestNameFromCM() } ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_CLAIM_APPROVAL_LIST, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.POLLS_PAX_ALERT ) ) //
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.INSTANT_POLL" ) );
              paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.POLLS_PAX_ALERT" ) );
              paxAlertView.setAlertLink( "" );
              paxAlertView.setInstantPollId( bean.getInstantPollId() );
              paxAlertView.setInstantPoll( true );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.CELEBRATION_PAX_ALERT ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() ) //
            {
              clientStateParameterMap.put( "claimId", bean.getClaimId() );
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_CELEBRATION" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CELEBRATION_PAX_ALERT" ),
                                                                new Object[] { bean.getPromotion().getName().toString() } ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/celebration/celebrationPage.do", clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.CELEBRATION_PAX_ALERT ) && NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() ) //
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_CELEBRATION" ) );
              paxAlertView
                  .setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CELEBRATION_PAX_ALERT" ), new Object[] { bean.getProgramName() } ) );
              paxAlertView.setCelebrationId( bean.getSaCelebrationId() );
              paxAlertView.setOpenNewWindow( true );
            }
            else if ( bean.getActivityType().equals( ActivityType.NOMINATION_PAX_WINNER_ALERT ) ) //
            {
              clientStateParameterMap.put( "activityId", bean.getActivityId() );
              clientStateParameterMap.put( "teamId", bean.getTeamId() );
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_ALERT" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NOMINATION_PAX_WINNER_ALERT" ),
                                                                new Object[] { bean.getPromotion().getName().toString() } ) );
              paxAlertView.setAlertLink( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.NOMINATOR_MY_WINNER, clientStateParameterMap ) );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.NOMINATION_PAX_SAVED_SUBMISSIONS ) ) //
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_ALERT" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "NOMINATION_PAX_SAVED_SUBMISSIONS" ),
                                                                new Object[] { bean.getPaxNomineeInCompleteSubmissions() } ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + PageConstants.NOMINATION_PAX_SAVED_SUBMISSIONS );
              paxAlertView.setOpenNewWindow( false );
            }

            else if ( bean.getActivityType().equals( ActivityType.NOMS_APPROVER_REMAINDER_SUMMARY ) ) //
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_ALERT" ) );
              paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NOMINATION_APPROVAL_TO_REVIEW" ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + PageConstants.ALERT_NOMINATION_APPROVAL_LIST );
              paxAlertView.setOpenNewWindow( false );

            }

            else if ( bean.getActivityType().equals( ActivityType.UNVERIFIED_RECOVERY_CONTACT_ALERT ) ) //
            {
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RECOVERY_VERIFY_LINK_TEXT" ) );
              paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RECOVERY_VERIFY_TITLE" ) );
              paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + PageConstants.PARTICIPANT_SECURITY );
              paxAlertView.setOpenNewWindow( false );
            }
            else if ( bean.getActivityType().equals( ActivityType.RA_ALERT_NEW_HIRE ) && bean.isNewHireAlert() )
            {
              if ( Objects.nonNull( bean.getNhFirstName() ) && StringUtils.isNotEmpty( bean.getNhFirstName() ) )
              {
                String text = ContentReaderManager.getText( "profile.alerts.messages", "RA_ACTIONITEM_NH_LINK" );
                paxAlertView.setAlertLinkText( MessageFormat.format( text, new Object[] { bean.getNhFirstName() } ) );
                paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RA_NEW_EMP_ON_TEAM" ) );
                paxAlertView.setOpenNewWindow( false );
                paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + "/ra/" + bean.getNhUserId() + "/sendRecognition.action" );
              }
              else
              {
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RA_ACTIONITEM_OD_LINK" ) );
                paxAlertView.setAlertTitle( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RA_ACTION_ITEM_LINK" ) );
                paxAlertView.setOpenNewWindow( false );
                paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + PageConstants.RA_DETAILS_PAGE );
              }

            }
            else if ( bean.getActivityType().equals( ActivityType.RA_ALERT_OVER_DUE ) && bean.isOverDueAlert() )
            {
              Integer days = getSystemVariableService().getPropertyByName( SystemVariableService.RA_NUMBER_OF_DAYS_EMPLOYEE_REMINDER ).getIntVal();
              if ( Objects.nonNull( bean.getOdFirstName() ) && StringUtils.isNotEmpty( bean.getOdFirstName() ) )
              {

                String text = ContentReaderManager.getText( "profile.alerts.messages", "RA_ACTIONITEM_OD" );
                text = text.replaceAll( "(?<!')'", "''" );
                String text1 = ContentReaderManager.getText( "profile.alerts.messages", "RA_ACTIONITEM_NH_LINK" );
                paxAlertView.setAlertLinkText( MessageFormat.format( text1, new Object[] { bean.getOdFirstName() } ) );
                paxAlertView.setAlertTitle( MessageFormat.format( text, new Object[] { bean.getOdFirstName(), days } ) );
                paxAlertView.setOpenNewWindow( false );
                paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + "/ra/" + bean.getOdUserId() + "/sendRecognition.action" );
              }
              else
              {

                String text = ContentReaderManager.getText( "profile.alerts.messages", "RA_OD_EMP_RECOG_MORE_DAYS" );
                text = text.replaceAll( "(?<!')'", "''" );
                paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RA_VIEW_REPORT" ) );
                paxAlertView.setAlertTitle( MessageFormat.format( text, new Object[] { days } ) );
                paxAlertView.setOpenNewWindow( false );
                paxAlertView.setAlertLink( RequestUtils.getBaseURI( request ) + PageConstants.RA_DETAILS_PAGE );

              }

            } // Client customizations for WIP #43735 starts
            else if ( bean.getActivityType().equals( ActivityType.PENDING_CLAIM_AWARD ) )
            {             
              paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "coke.cash.recognition.CLAIM_AWARD" ) );
              paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "coke.cash.recognition.CLAIM_AWARD_MSG" ), new Object[] { bean.getPromotionName() } ) );              
              if(null != bean.getClaimId())
              clientStateParameterMap.put( "claimId", bean.getClaimId() );
              String url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.TCCC_CLAIM_AWARD_DISPLAY, clientStateParameterMap );
              paxAlertView.setAlertLink(url);
              paxAlertView.setOpenNewWindow( false );
            }
            // Client customization for WIP 58122
            else if ( bean.getActivityType().equals( ActivityType.PENDING_CLAIM_AWARD_NOMINATION ) )
            { 
               paxAlertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "coke.custom.nomination.REDEEM_NOMINATION_CLAIM_AWARD" ) );
               paxAlertView.setAlertTitle( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "coke.custom.nomination.CLAIM_AWARD_MSG" ), new Object[] { bean.getPromotionName() } ) );
               if(null != bean.getClaimId())
               clientStateParameterMap.put( "claimId", bean.getClaimId() );
               String url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.TCCC_CLAIM_AWARD_NOMINATION_DISPLAY, clientStateParameterMap );
               paxAlertView.setAlertLink(url);
               paxAlertView.setOpenNewWindow( false );                 
            } 
            // Client customizations for WIP #43735 ends

            if ( paxAlertView.getAlertLink() == null && paxAlertView.getAlertLinkText() == null ) // fix
                                                                                                  // for
                                                                                                  // bug
                                                                                                  // 65785
            {
              paxAlertView.setDisplayAlert( false );
            }

            paxAlertViewList.add( paxAlertView );
          }

          alerts.setAlerts( paxAlertViewList );
        }

        alerts.setNumberOfMessages( alertList.size() );

        profileAlertsView.getMessages().add( new ProfileAlertView( WebResponseConstants.RESPONSE_TYPE_NAME_PAX, WebResponseConstants.RESPONSE_TYPE_TYPE_PAX, alerts ) );
      }
    }
    super.writeAsJsonToResponse( profileAlertsView, response );

    return null;
  }

  private boolean isGiftCodeOnlyPax()
  {
    return UserManager.getUser().isParticipant() && UserManager.getUser().isGiftCodeOnlyPax();
  }

  @SuppressWarnings( "unchecked" )
  private List<AlertsValueBean> getAlertsList( AuthenticatedUser authUser, List<PromotionMenuBean> eligiblePromotions, HttpServletRequest request )
  {
    // Alerts Performance Tuning
    /*
     * List<AlertsValueBean> alertsList = null; boolean refreshFromDB = Boolean.valueOf(
     * request.getParameter( "refreshAlerts" ) ); if ( !refreshFromDB ) { alertsList =
     * (List<AlertsValueBean>)request.getSession().getAttribute( "alertsList" ); } if ( alertsList
     * != null ) { return alertsList; } else { alertsList = getPromotionService().getAlertsList(
     * authUser, authUser.getUserId(), false, eligiblePromotions ); AlertsValueBean
     * plateauReminderView = buildPlateauAwardsReminderAlerts(); if ( plateauReminderView != null )
     * { alertsList.add( plateauReminderView ); } request.getSession().setAttribute( "alertsList",
     * alertsList ); return alertsList; }
     */
    List<AlertsValueBean> alertsList = getPromotionService().getAlertsList( authUser, authUser.getUserId(), false, eligiblePromotions, false );
    AlertsValueBean plateauReminderView = buildPlateauAwardsReminderAlerts();
    if ( plateauReminderView != null )
    {
      alertsList.add( plateauReminderView );
    }
    request.getSession().setAttribute( "alertsList", alertsList );
    return alertsList;
  }

  private AlertsValueBean buildPlateauAwardsReminderAlerts()
  {
    AlertsValueBean alertsValueBean = null;
    boolean enablePlateauAwardReminder = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_AWARDS_REMINDER_ENABLED ).getBooleanVal();

    if ( enablePlateauAwardReminder )
    {
      int numberOfPaxWithUnclaimedAwards = getPlateauAwardsService().getNumberOfPaxWithUnclaimedAwardsFor( UserManager.getUserId() );

      if ( numberOfPaxWithUnclaimedAwards > 0 )
      {
        alertsValueBean = new AlertsValueBean();
        alertsValueBean.setActivityType( ActivityType.PLATEAU_AWARD_REMINDER );
        alertsValueBean.setNumberOfPaxWithUnclaimedAwards( numberOfPaxWithUnclaimedAwards );
      }
    }

    return alertsValueBean;
  }

  /**
   * This method checks the strategy logic for each module and add the app name that needs to be displayed
   * @param profilePointsView
   */
  public void populateApplicableModules( HttpServletRequest request, ParticipantProfileView participantProfileView )
  {
    // clear the data before adding the data
    participantProfileView.getParticipant().setApplicableSidebarModules( new ArrayList<SidebarModuleBean>() );

    if ( checkForBudget( request ) )
    {
      participantProfileView.getParticipant().addSidebarModule( BUDGET_MODULE_NAME, BUDGET_METER );
    }

    if ( checkForGamification( request ) )
    {
      participantProfileView.getParticipant().addSidebarModule( GAMIFICATION_MODULE_NAME, GAMIFICATION );
    }

    if ( checkForEngagement( request ) )
    {
      participantProfileView.getParticipant().addSidebarModule( ENGAGEMENT_MODULE_NAME, ENGAGEMENT );
    }

    if ( checkForWorkHappier() )
    {
      participantProfileView.getParticipant().addSidebarModule( WORK_HAPPIER_MODULE_NAME, WORK_HAPPIER );
    }
    //Client customization
    if(checkForBunchBall())
    {
      participantProfileView.getParticipant().addSidebarModule( BUNCHBALL_MODULE_NAME, BUNCH_BALL );
    }
  }
  
  //Client customization start
  private boolean checkForBunchBall()
  {
    if ( !UserManager.getUser().isParticipant() )
    {
      return false;
    }
    
    if(!getSystemVariableService().getPropertyByName( SystemVariableService.COKE_SIDEBAR_SHOW_BUNCHBALL_MODULE ).getBooleanVal())
    {
      return false;
    }
    return true;
  }
  //Client customization end

  /**
   * checks if the budget module is applicable
   * @return
   */
  private boolean checkForBudget( HttpServletRequest request )
  {
    if ( !UserManager.getUser().isParticipant() )
    {
      return false;
    }

    // check if eligible promotions is empty
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    if ( isEmpty( eligiblePromotions ) )
    {
      return false;
    }

    List<PromotionMenuBean> validPromotions = null;

    // check valid promotions
    if ( isNotEmpty( eligiblePromotions ) )
    {
      validPromotions = eligiblePromotions.stream().filter( e -> e.isCanSubmit() ).collect( Collectors.toList() );
    }

    if ( isEmpty( validPromotions ) )
    {
      return false;
    }

    if ( !getMainContentService().isParticipantHasBudgetMeter( UserManager.getUser().getUserId(), getEligiblePromotions( request ) ) )
    {
      return false;
    }
    return true;
  }

  /**
   * checks if the budget module is applicable
   * @return
   */
  private boolean checkForGamification( HttpServletRequest request )
  {
    if ( UserManager.getUser().isParticipant() && getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_BADGES ).getBooleanVal() )
    {
      List<PromotionMenuBean> badgeReceivablePromotionList = new ArrayList<PromotionMenuBean>();

      List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );

      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.isCanReceive() )
        {
          badgeReceivablePromotionList.add( promotionMenuBean );
        }
      }
      if ( getGamificationService().isUserHasActiveBadges( UserManager.getUser().getUserId(), badgeReceivablePromotionList ) > 0 )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * checks if th   e budget module is applicable
   * @return
   */
  private boolean checkForEngagement( HttpServletRequest request )
  {
    EngagementPromotionData engagementPromotionData = getEngagementService().getLiveEngagementPromotionData();
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );

    if ( engagementPromotionData != null && engagementPromotionData.getTileDisplayStartDate().compareTo( DateUtils.getCurrentDateTrimmed() ) <= 0 )
    {
      Set<Long> engEligiblePromotion = getEngagementService().getAllEligiblePromotionIds( engagementPromotionData.getPromotionId() );
      if ( CollectionUtils.isNotEmpty( eligiblePromotions ) )
      {
        for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
        {
          if ( engEligiblePromotion.contains( promotionMenuBean.getPromotion().getId() ) )
          {
            if ( promotionMenuBean.getPromotion().isAbstractRecognitionPromotion() && ( promotionMenuBean.isCanSubmit() || promotionMenuBean.isCanReceive() ) )
            {
              return true;
            }

          }
        }
      }
    }
    return false;
  }

  /**
   * checks if the budget module is applicable
   * @return
   */
  private boolean checkForWorkHappier()
  {
    boolean isworkHappierEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.WORK_HAPPIER ).getBooleanVal();

    if ( UserManager.getUser().isParticipant() && !UserManager.getUser().isDelegate() && isworkHappierEnabled )
    {
      ModuleApp workHappier = getFilterAppSetupService().getModuleByAppName( ReportCategoryType.WORKHAPPIER );

      if ( workHappier != null )
      {
        if ( getWorkHappierService().isWorkHappierAudience( workHappier ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private AlertMessageService getAlertMessageService()
  {
    return (AlertMessageService)getService( AlertMessageService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private PlateauAwardsService getPlateauAwardsService()
  {
    return (PlateauAwardsService)getService( PlateauAwardsService.BEAN_NAME );
  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  private WorkHappierService getWorkHappierService()
  {
    return (WorkHappierService)getService( WorkHappierService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }

  private RecognitionAdvisorService getRecognitionAdvisorService()
  {
    return (RecognitionAdvisorService)getService( RecognitionAdvisorService.BEAN_NAME );
  }

  private EngagementService getEngagementService()
  {
    return (EngagementService)getService( EngagementService.BEAN_NAME );
  }

}

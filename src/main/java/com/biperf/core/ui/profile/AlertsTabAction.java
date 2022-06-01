
package com.biperf.core.ui.profile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.managertoolkit.AlertMessageService;
import com.biperf.core.service.plateauawards.PlateauAwardsService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.AwardGeneratorManagerPaxBean;
import com.biperf.core.value.NominationsApprovalValueBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.SurveyPageValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * @author dudam
 * @since Dec 19, 2012
 * @version 1.0
 * 
 * In G5 profile page. Alerts & Messages Tab executes this 
 */
public class AlertsTabAction extends BaseDispatchAction
{
  private static final int GOAL_QUEST = 1;
  private static final int CHALLENGE_POINT = 2;
  private static final int QUIZ = 3;
  private static final int PURL_CONTRIBUTION = 4;
  private static final int PURL_INVITATION = 5;
  private static final int PURL_VIEW = 6;
  private static final int RECOGNITION = 7;
  private static final int NOMINATION = 8;
  private static final int PRODUCT_CLAIM = 9;
  private static final int BUDGET_END = 11;
  private static final int PURL_CONTRIBUTION_SINGLE = 12;
  private static final int FILE_DOWNLOAD = 13;
  private static final int SURVEY = 14;
  private static final int AWARD_GENERATOR_MANAGER_REMINDER = 15;
  private static final int SURVEY_ALERT_LIST = 16;
  private static final int SURVEY_TAKE = 17;
  private static final int CELEBRATION_MANAGER_REMINDER = 18;
  private static final int SSI_APPROVAL_SUMMARY = 19;
  private static final int SSI_PROGRESS_LOAD_CREATOR_ALERT = 20;
  private static final int SSI_PROGRESS_LOAD_MGR_ALERT = 21;
  private static final int SSI_PROGRESS_LOAD_PAX_ALERT = 22;
  private static final int SSI_CLAIM_APPROVAL = 23;
  private static final int NOMINATOR_MOREINFO = 24;
  private static final int UNVERIFIED_RECOVERY_METHOD = 25;
  private static final String EACH_LEVEL = "eachLevel";
  

  private static final int PURL_CONTRIBUTION_DEFAULT_INVITEE = 26;
  private static final int PURL_CONTRIBUTION_NON_DEFAULT_INVITEE = 27;
  
  // Client customization for WIP #43735 starts
  private static final int PENDING_CLAIM_AWARD = 26;
  // Client customization for WIP 58122
  private static final int PENDING_CLAIM_AWARD_NOMINATION = 27;

  // Client customization for WIP #43735 ends
  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException 
   */
  public ActionForward fetchAlrts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    // Build view object
    AlertsAndMessagesTabView view = new AlertsAndMessagesTabView();

    AuthenticatedUser authenticatedUser = UserManager.getUser();
    authenticatedUser.setRouteId( request.getHeader( "proxy-jroute" ) );

    List<AlertsValueBean> alertList = new ArrayList<AlertsValueBean>();
    List<AlertsValueBean> promoAlertList = null;

    // Get List of alerts by UserId
    if ( UserManager.getUser().isParticipant() )
    {
      List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
      promoAlertList = getAlertsList( authenticatedUser, eligiblePromotions, request );
      if ( promoAlertList != null && promoAlertList.size() > 0 )
      {
        // ADDED TO FILTER THE BELOW ALERTS AS PART OF G6 TO NOT SHOW IN MESSAGES, RIGHT NOW WE ARE
        // GETTING BLANK ROWS.
        Predicate<AlertsValueBean> filter = new Predicate<AlertsValueBean>()
        {
          @Override
          public boolean test( AlertsValueBean t )
          {
            return t.getActivityType().equals( ActivityType.POLLS_PAX_ALERT ) || t.getActivityType().equals( ActivityType.NOMINATION_PAX_WINNER_ALERT )
                || t.getActivityType().equals( ActivityType.NOMINATION_PAX_SAVED_SUBMISSIONS ) || t.getActivityType().equals( ActivityType.NOMS_APPROVER_REMAINDER_SUMMARY ) ? false : true;
          }
        };
        promoAlertList = promoAlertList.stream().filter( filter ).collect( Collectors.toList() );
        alertList.addAll( promoAlertList );
      }
    }

    // Get list of alert messages and differientiate using contestId
    List<ParticipantAlert> participantAlerts = getAlertMessageService().getActiveAlertMessagesByUserId( UserManager.getUserId() );
    Set<Long> contestIds = new HashSet<Long>();
    List<NameIdBean> contestNames = null;
    for ( ParticipantAlert participantAlert : participantAlerts )
    {

      if ( participantAlert.getAuditCreateInfo().getCreatedBy().equals( UserManager.getUserId() ) )
      {
        continue;
      }
      AlertsValueBean alertsBean = new AlertsValueBean();
      if ( participantAlert.getAlertMessage().getContestId() == null )
      {
        alertsBean.setActivityType( ActivityType.MANAGER_ALERT );
      }
      else
      {
        String ssiAlertType = participantAlert.getAlertMessage().getSsiAlertType();
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
        contestIds.add( participantAlert.getAlertMessage().getContestId() );
      }
      alertsBean.setParticipantAlert( participantAlert );
      alertList.add( alertsBean );
    }

    if ( contestIds.size() > 0 )
    {
      contestNames = getSSIContestService().getContestNames( contestIds, UserManager.getLocale().toString() );
    }
    for ( AlertsValueBean alert : alertList )
    {
      view.getAlertView().add( buildAlert( alert, request, contestNames ) );
    }

    AlertView plateauReminderView = buildPlateauAwardsReminderAlerts( request );
    if ( plateauReminderView != null )
    {
      view.getAlertView().add( plateauReminderView );
    }

    populateAlertIds( view.getAlertView() ); // need to uniquely identify alerts in JSON

    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private void populateAlertIds( List<AlertView> alertViewList )
  {
    long alertId = 0;
    for ( AlertView av : alertViewList )
    {
      av.setAlertIdNum( alertId++ );
    }
  }

  private AlertView buildAlert( AlertsValueBean alert, HttpServletRequest request, List<NameIdBean> contestNames )
  {
    AlertView alertView = new AlertView();
    if ( alert != null && alert.getActivityType() != null )
    {
      alertView.setManagerAlert( alert.getActivityType().equals( ActivityType.MANAGER_ALERT ) );

      if ( alert.getActivityType().equals( ActivityType.MANAGER_ALERT ) )
      {
        alertView.setLink( false );
        alertView.setAlertLinkUrl( null );
        alertView.setAlertLinkText( null );
        alertView.setAlertText( alert.getParticipantAlert().getAlertMessage().getMessage() );
        alertView.setAlertSubject( alert.getParticipantAlert().getAlertMessage().getSubject() );
        alertView.setDatePostedDisplay( DateUtils
            .toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils
            .getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.FILE_DOWNLOAD ) )
      {
        alertView.setLink( true );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, FILE_DOWNLOAD, null ) );
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.DOWNLOAD" ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_FILE_EXTRACT" ),
                                                         new Object[] { alert.getFileName() != null ? alert.getFileName() : "", alert.getFileDownloadExpiryDate() } ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getFileDownloadRequestedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getFileDownloadRequestedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getFileDownloadExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.AWARD_GENERATOR_MANAGER_REMINDER ) )
      {
        String alertText = buildPaxBeanText( UserManager.getUserId(), alert.getBatchId() );
        alertView.setLink( false );
        alertView.setAlertLinkUrl( null );
        alertView.setAlertLinkText( null );
        alertView.setOpenNewWindow( false );
        alertView.setAlertText( alertText );
        alertView.setAlertDismissText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.DISMISS_ALERT" ) );
        alertView.setAlertDismissUrl( buildAlertUrl( alert, request, AWARD_GENERATOR_MANAGER_REMINDER, null ) );
        alertView.setIsRemovable( true );
        alertView
            .setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "QUALIFIED_TEAM_MEMBERS_THIS_MONTH" ), new Object[] { alert.getPromotion().getName() } ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardExpiryDate() ), UserManager.getTimeZoneID() ) ) );

      }
      else if ( alert.getActivityType().equals( ActivityType.AWARD_REMINDER ) )
      {
        AuthenticatedUser user = UserManager.getUser();
        HttpSession session = request.getSession();
        Long loginUserId = (Long)session.getAttribute( "loginUserId" );

        if ( StringUtils.isNotEmpty( alert.getSaCelebrationId() ) )
        {
          if ( user.isLaunched() && loginUserId != null )
          {

            if ( getRoleService().getUserRoleBypassingUserIdAndRoleCode( loginUserId ) )
            {
              alertView.setPlateauRedemption( getRoleService().getUserRoleBypassingUserIdAndRoleCode( loginUserId ) );
            }
          }
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_AWARD" ) );
          alertView.setLink( true );
          alertView.setOpenNewWindow( true );
          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_AWARDS_TO_REDEEM_PLATEAU" ), new Object[] { alert.getProgramName() } ) );
          alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
          alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
          alertView.setCelebrationId( alert.getSaCelebrationId() );
          alertView.setSaGiftCode( true );
        }
        else
        {
          if ( user.isLaunched() && loginUserId != null )
          {

            if ( getRoleService().getUserRoleBypassingUserIdAndRoleCode( loginUserId ) )
            {
              alertView.setPlateauRedemption( getRoleService().getUserRoleBypassingUserIdAndRoleCode( loginUserId ) );
            }
          }
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_AWARD" ) );
          alertView.setLink( true );
          alertView.setOpenNewWindow( true );
          alertView.setSelectAward( true ); // customization start WIP#20160
          alertView.setAlertLinkUrl( alert.getOnlineShoppingUrl() );
          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_AWARDS_TO_REDEEM_PLATEAU" ),
                                                           new Object[] { alert.getPromotion().getName() } ) );
          alertView
              .setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardIssuedDate(), UserManager.getTimeZoneID() ), UserManager.getTimeZoneID() ) ) );
          alertView
              .setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardIssuedDate(), UserManager.getTimeZoneID() ), UserManager.getTimeZoneID() ) ) );
          if ( alert.getAwardExpiryDate() != null && !alert.getAwardExpiryDate().equals( "" ) )
          {
            alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardExpiryDate() ), UserManager.getTimeZoneID() ) ) );
            alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getAwardExpiryDate() ), UserManager.getTimeZoneID() ) ) );
          }
          else
          {
            alertView.setDueDateDisplay( "" );
          }

        }
      }
      else if ( alert.getActivityType().equals( ActivityType.BUDGET_END ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEND_RECOGNITION" ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, BUDGET_END, null ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_BUDGET_END" ),
                                                         new Object[] { alert.getPromotion().getName(), alert.getBudgetEndDate() } ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( alert.getBudgetSegmentStartDate() ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getBudgetSegmentStartDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( alert.getBudgetSegmentEndDate() ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getBudgetSegmentEndDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.SUBMIT_PROMOTION ) || alert.getActivityType().equals( ActivityType.PROGRAM_END ) )
      {
        alertView.setLink( true );
        if ( alert.getPromotion().getPromotionType().getCode().equals( PromotionType.GOALQUEST ) )
        {
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_GOAL" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, GOAL_QUEST, null ) );
          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_GOAL_DUE_DATE" ),
                                                           new Object[] { alert.getPromotion().getName().toString(),
                                                                          DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getGoalSelectionEnddate() ),
                                                                                                                              UserManager.getTimeZoneID() ) ) } ) );
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getGoalSelectionStartDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getGoalSelectionStartDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getGoalSelectionEnddate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getGoalSelectionEnddate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setOpenNewWindow( false );
        }
        if ( alert.getPromotion().getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
        {
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_GOAL" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, CHALLENGE_POINT, null ) );
          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_CP_DUE_DATE" ),
                                                           new Object[] { alert.getPromotion().getName().toString(),
                                                                          DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionEndDate() ),
                                                                                                                              UserManager.getTimeZoneID() ) ) } ) );
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionStartDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionStartDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionEndDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionEndDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setOpenNewWindow( false );
        }
        if ( alert.getPromotion().getPromotionType().getCode().equals( PromotionType.QUIZ ) || alert.getPromotion().getPromotionType().getCode().equals( PromotionType.DIY_QUIZ ) )
        {
          alertView.setLink( true );
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.TAKE_QUIZ" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, QUIZ, null ) );
          alertView.setAlertSubject( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.PENDING_QUIZ" ) );
          alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
          alertView.setDatePostedSort( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionEndDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
          alertView.setDueDateSort( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getCpSelectionEndDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setOpenNewWindow( false );
        }
      }
      else if ( alert.getActivityType().equals( ActivityType.SURVEY_ALERT ) )
      {
        alertView.setLink( true );
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.TAKE_SURVEY" ) );
        List<SurveyPageValueBean> pendingSurveys = getPromotionService().getPendingSurveysList( UserManager.getUserId() );
        if ( pendingSurveys != null )
        {
          if ( pendingSurveys.size() > 1 )
          {
            alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_SURVEYS_TO_COMPLETE" ), new Object[] { pendingSurveys.size() } ) );
            alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SURVEY_ALERT_LIST, null ) );
            // set "See Details" as start and end date if we have multiple survey(s)
            String seeDetailsMsg = CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" );
            alertView.setDatePostedDisplay( seeDetailsMsg );
            alertView.setDatePostedSort( seeDetailsMsg );
            alertView.setDueDateDisplay( seeDetailsMsg );
            alertView.setDueDateSort( seeDetailsMsg );
          }
          else
          {
            alertView.setAlertSubject( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SURVEY_TO_COMPLETE" ) );
            alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SURVEY_TAKE, null ) );
            alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getSurveySelectionStartDate() ), UserManager.getTimeZoneID() ) ) );
            alertView.setDatePostedSort( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getSurveySelectionStartDate() ), UserManager.getTimeZoneID() ) ) );
            if ( !StringUtils.isEmpty( alert.getSurveySelectionEnddate() ) )
            {
              alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getSurveySelectionEnddate() ), UserManager.getTimeZoneID() ) ) );
              alertView.setDueDateSort( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getSurveySelectionEnddate() ), UserManager.getTimeZoneID() ) ) );
            }
            else
            {
              // message is "No end date"
              String noEndDate = CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NO_END_DATE" );
              alertView.setDueDateDisplay( noEndDate );
              alertView.setDueDateSort( noEndDate );
            }
          }
        }
        alertView.setOpenNewWindow( false );
      }

      else if ( alert.getActivityType().equals( ActivityType.PURL_CONTRIBUTION_DEFAULT_INVITEE ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INVITATION_RECOGNITION_PURL" ),
                                                         new Object[] { alert.getPromotion().getName() } ) );
        alert.setDefaultInvitee( true );
        if ( StringUtils.isEmpty( alert.getPurlIssuedDate() ) )
        {
          alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PURL_CONTRIBUTION, null ) );
        }
        else
        {
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PURL_CONTRIBUTION_SINGLE, null ) );
        }
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        if ( StringUtils.isEmpty( alert.getPurlExpiryDate() ) )
        {
          alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        }
        else
        {
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        }
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.PURL_CONTRIBUTION_NON_DEFAULT_INVITEE ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INTEREST_RECOGNITION_PURL" ),
                                                         new Object[] { alert.getPromotion().getName() } ) );
      }

      else if ( alert.getActivityType().equals( ActivityType.PURL_CONTRIBUTION ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );

        if ( alert.isDefaultInvitee() )
        {
          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INVITATION_RECOGNITION_PURL" ),
                                                           new Object[] { alert.getPromotion().getName() } ) );
        }
        else
        {

          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INTEREST_RECOGNITION_PURL" ),
                                                           new Object[] { alert.getPromotion().getName() } ) );
        }


        if ( StringUtils.isEmpty( alert.getPurlIssuedDate() ) )
        {
          alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PURL_CONTRIBUTION, null ) );
        }
        else
        {
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PURL_CONTRIBUTION_SINGLE, null ) );
        }
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        if ( StringUtils.isEmpty( alert.getPurlExpiryDate() ) )
        {
          alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        }
        else
        {
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        }
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.PURL_CONTRIBUTION ) && NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.CONTRIBUTE" ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CONTRIBUTE_INVITATION_FOR_NEW_SA" ), new Object[] { alert.getProgramName() } ) );
        alertView.setLink( true );
        if ( StringUtils.isEmpty( alert.getPurlIssuedDate() ) )
        {
          alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        }
        else
        {
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        }
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        if ( alert.getSaCelebrationId() != null )
        {
          alertView.setOpenNewWindow( true );
          alertView.setCelebrationId( alert.getSaCelebrationId() );
        }
        else
        {
          alertView.setOpenNewWindow( false );
          alertView.setAlertLinkUrl( buildSAAlertUrl( alert, request ) );
        }
      }
      else if ( alert.getActivityType().equals( ActivityType.PURL_INVITATION ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEND_INVITATIONS" ) );
        alertView.setAlertSubject( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.PENDING_PURL" ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PURL_INVITATION, null ) );
        if ( StringUtils.isEmpty( alert.getPurlIssuedDate() ) )
        {
          alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        }
        else
        {
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        }
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        if ( StringUtils.isEmpty( alert.getPurlExpiryDate() ) )
        {
          alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        }
        else
        {
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        }
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.PURL_VIEW ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_PURL" ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PURL_VIEW, null ) );
        alertView
            .setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "VIEW_YOUR_RECOG_PURL" ), new Object[] { alert.getPromotion().getName().toString() } ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.CELEBRATION_PAX_ALERT ) && NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.VIEW_CELEBRATION" ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "CELEBRATION_PAX_ALERT" ), new Object[] { alert.getProgramName() } ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlIssuedDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getPurlExpiryDate() ), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setCelebrationId( alert.getSaCelebrationId() );
        alertView.setOpenNewWindow( true );
      }
      else if ( alert.getActivityType().equals( ActivityType.PENDING_APPROVALS ) )
      {
        alertView.setLink( true );

        if ( alert.getPromotionType() != null && alert.getPromotionType().equals( PromotionType.NOMINATION ) )
        {
          NominationsApprovalValueBean nominationsApprovalValueBean = alert.getNominationsApprovalValueBean();
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( nominationsApprovalValueBean.getDateSubmitted() ) );
          alertView.setDatePostedSort( DateUtils.getSortDateString( nominationsApprovalValueBean.getDateSubmitted() ) );
          alertView.setDueDateDisplay( DateUtils.toDisplayString( nominationsApprovalValueBean.getSubmissionEndDate() ) );
          alertView.setDueDateSort( DateUtils.getSortDateString( nominationsApprovalValueBean.getSubmissionEndDate() ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, NOMINATION, null ) );

          if ( nominationsApprovalValueBean.getPayoutLevelType() != null && nominationsApprovalValueBean.getPayoutLevelType().equals( EACH_LEVEL ) )
          {
            if ( !StringUtils.isEmpty( nominationsApprovalValueBean.getLevelLabel() ) )
            {
              alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS" ),
                                                               new Object[] { nominationsApprovalValueBean.getPromotionName() + " " + nominationsApprovalValueBean.getLevelLabel() } ) );
              alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_WINNERS" ) );
            }
            else
            {
              alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS_LEVEL_INDEX" ),
                                                               new Object[] { nominationsApprovalValueBean.getPromotionName(), nominationsApprovalValueBean.getApprovalLevel() } ) );
              alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_WINNERS" ) );
            }
          }
          else if ( !nominationsApprovalValueBean.isFinalLevel() )
          {
            if ( nominationsApprovalValueBean.isMultipleLevel() )
            {
              if ( !StringUtils.isEmpty( nominationsApprovalValueBean.getLevelLabel() ) )
              {
                alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS" ),
                                                                 new Object[] { nominationsApprovalValueBean.getPromotionName() + " " + nominationsApprovalValueBean.getLevelLabel() } ) );
                alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
              }
              else
              {
                alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS" ),
                                                                 new Object[] { nominationsApprovalValueBean.getPromotionName(), nominationsApprovalValueBean.getApprovalLevel() } ) );
                alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
              }
            }
            else
            {
              alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS" ),
                                                               new Object[] { nominationsApprovalValueBean.getPromotionName() } ) );
              alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
            }
          }
          else
          {
            if ( !StringUtils.isEmpty( nominationsApprovalValueBean.getLevelLabel() ) )
            {
              alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS" ),
                                                               new Object[] { nominationsApprovalValueBean.getPromotionName() + " " + nominationsApprovalValueBean.getLevelLabel() } ) );
              alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_WINNERS" ) );
            }
            else
            {
              alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS" ),
                                                               new Object[] { nominationsApprovalValueBean.getPromotionName() } ) );
              alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SELECT_WINNERS" ) );
            }
          }
        }
        else
        {
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionStartDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionStartDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
          alertView
              .setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_APPROVALS_TO_REVIEW" ), new Object[] { alert.getPromotion().getName() } ) );

          if ( alert.getPromotion().getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
          {
        	  // Client customizations for WIP #43735 starts
              // Override due date values if adih cash enabled promo
              if ( alert.getPromotion().getAdihCashOption() )
              {
                if ( alert.getPromotion().getApprovalEndDate() != null )
                {
                  alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getPromotion().getApprovalEndDate(), UserManager.getTimeZoneID() ) ) );
                  alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getPromotion().getApprovalEndDate(), UserManager.getTimeZoneID() ) ) );
                }
                else if ( alert.getPromotion().getSubmissionEndDate() != null )
                {
                  alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
                  alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
                }
                else
                {
                  alertView.setDueDateDisplay( "" );
                  alertView.setDueDateSort( "" );
                }
              }
              // Client customizations for WIP #43735 ends
            alertView.setAlertLinkUrl( buildAlertUrl( alert, request, RECOGNITION, null ) );
          }
          if ( alert.getPromotion().getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
          {
            alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PRODUCT_CLAIM, null ) );
          }
        }
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.CELEBRATION_MANAGER_REMINDER_ALERT ) && !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        if ( alert.getCelebrationManagerMessageId() != null )
        {
          CelebrationManagerMessage celebrationManagerMessage = getCelebrationService().getCelebrationManagerMessageById( alert.getCelebrationManagerMessageId() );

          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.ADD_CELEBRATION_MESSAGE" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, CELEBRATION_MANAGER_REMINDER, null ) );
          alertView.setAlertSubject(MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "UPCOMING_CELEBRATION" ),
                  new Object[] { celebrationManagerMessage.getRecipient().getFirstName(), celebrationManagerMessage.getRecipient().getLastName() } ) );
          alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( celebrationManagerMessage.getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
          alertView.setDatePostedSort( DateUtils.toDisplayString( DateUtils.applyTimeZone( celebrationManagerMessage.getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( celebrationManagerMessage.getMsgCollectExpireDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( celebrationManagerMessage.getMsgCollectExpireDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setLink( true );
          alertView.setOpenNewWindow( false );

        }
      }
      else if ( alert.getActivityType().equals( ActivityType.GQ_SURVEY ) )
      {
        Integer numberOfDays = new Integer( 14 );
        alertView.setLink( true );
        if ( alert.getPromotion().getPromotionType().getCode().equals( PromotionType.GOALQUEST ) || alert.getPromotion().getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
        {
          alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.TAKE_SURVEY" ) );
          alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SURVEY, null ) );
          GoalQuestPromotion gqPromo = (GoalQuestPromotion)alert.getPromotion();
          Date surveyEndDate = new Date( DateUtils.toEndDate( gqPromo.getGoalCollectionEndDate() ).getTime() + numberOfDays.longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
          Date actualSurveyEndDate = new Date();
          if ( surveyEndDate.after( DateUtils.toEndDate( gqPromo.getSubmissionEndDate() ) ) )
          {
            actualSurveyEndDate = gqPromo.getSubmissionEndDate();
          }
          else
          {
            actualSurveyEndDate = surveyEndDate;
          }
          alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOUR_TAKE_SURVEY_DUE_DATE" ),
                                                           new Object[] { alert.getPromotion().getName(),
                                                                          DateUtils.toDisplayString( DateUtils.applyTimeZone( actualSurveyEndDate, UserManager.getTimeZoneID() ) ) } ) );
          alertView
              .setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( ( (GoalQuestPromotion)alert.getPromotion() ).getGoalCollectionStartDate(), UserManager.getTimeZoneID() ) ) );
          alertView
              .setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( ( (GoalQuestPromotion)alert.getPromotion() ).getGoalCollectionStartDate(), UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( actualSurveyEndDate, UserManager.getTimeZoneID() ) ) );
          alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( actualSurveyEndDate, UserManager.getTimeZoneID() ) ) );
          alertView.setOpenNewWindow( false );
        }
      }
      else if ( alert.getActivityType().equals( ActivityType.SSI_APPROVAL ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.VIEW_CONTEST" ) );
        SSIContest contest = getSSIContestService().getContestById( alert.getParticipantAlert().getAlertMessage().getContestId() );
        alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_APPROVAL_MESG" ), new Object[] { contest.getContestNameFromCM() } ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SSI_APPROVAL_SUMMARY, contest ) );
        Date postedDate = contest.getDateApprovedLevel1() == null ? new Date( contest.getAuditCreateInfo().getDateCreated().getTime() ) : new Date( contest.getDateApprovedLevel1().getTime() );
        Date dueDate = contest.getDateApprovedLevel1() == null
            ? DateUtils.getDateAfterNumberOfDays( contest.getAuditCreateInfo().getDateCreated(), contest.getPromotion().getDaysToApproveOnSubmission() )
            : DateUtils.getDateAfterNumberOfDays( contest.getDateApprovedLevel1(), contest.getPromotion().getDaysToApproveOnSubmission() );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( postedDate ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( postedDate, UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( dueDate ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( dueDate, UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.SSI_PROGRESS_LOAD_CREATOR_ALERT ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.VIEW_ACTIVITY" ) );
        for ( NameIdBean bean : contestNames )
        {
          if ( bean.getId().equals( alert.getParticipantAlert().getAlertMessage().getContestId() ) )
          {
            alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_PROGRESS_LOAD_MESG" ), new Object[] { bean.getName() } ) );
          }
        }
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SSI_PROGRESS_LOAD_CREATOR_ALERT, null ) );
        alertView.setDatePostedDisplay( DateUtils
            .toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils
            .getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.SSI_PROGRESS_LOAD_MGR_ALERT ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.VIEW_ACTIVITY" ) );
        for ( NameIdBean bean : contestNames )
        {
          if ( bean.getId().equals( alert.getParticipantAlert().getAlertMessage().getContestId() ) )
          {
            alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_PROGRESS_LOAD_MESG" ), new Object[] { bean.getName() } ) );
          }
        }
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SSI_PROGRESS_LOAD_MGR_ALERT, null ) );
        alertView.setDatePostedDisplay( DateUtils
            .toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils
            .getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.SSI_PROGRESS_LOAD_PAX_ALERT ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.VIEW_ACTIVITY" ) );
        for ( NameIdBean bean : contestNames )
        {
          if ( bean.getId().equals( alert.getParticipantAlert().getAlertMessage().getContestId() ) )
          {
            alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_PROGRESS_LOAD_MESG" ), new Object[] { bean.getName() } ) );
          }
        }
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SSI_PROGRESS_LOAD_PAX_ALERT, null ) );
        alertView.setDatePostedDisplay( DateUtils
            .toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDatePostedSort( DateUtils
            .getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getAuditCreateInfo().getDateCreated(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.SSI_CLAIM_APPROVAL ) )
      {
        SSIContest contest = getSSIContestService().getContestById( alert.getParticipantAlert().getAlertMessage().getContestId() );
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.REVIEW_APPROVALS" ) );
        alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_CLAIM_APPROVAL" ), new Object[] { contest.getContestNameFromCM() } ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, SSI_CLAIM_APPROVAL, contest ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( contest.getStartDate() ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( contest.getStartDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( alert.getParticipantAlert().getAlertMessage().getExpiryDate() ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getParticipantAlert().getAlertMessage().getExpiryDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.NOMINATOR_REQMOREINFO_ALERT ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NOMINATOR_MOREINFO" ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, NOMINATOR_MOREINFO, null ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "NOMINATOR_MOREINFO_SUBJECT" ),
                                                         new Object[] { alert.getPromotion().getName().toString() } ) );

        alertView.setDatePostedDisplay( DateUtils.toDisplayString( alert.getPostedDate() ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( alert.getPostedDate() ) );
        alertView.setDueDateDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getPromotion().getSubmissionEndDate(), UserManager.getTimeZoneID() ) ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      else if ( alert.getActivityType().equals( ActivityType.UNVERIFIED_RECOVERY_CONTACT_ALERT ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RECOVERY_VERIFY_LINK_TEXT" ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, UNVERIFIED_RECOVERY_METHOD, null ) );
        alertView.setAlertSubject( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.RECOVERY_VERIFY_TITLE" ) );

        Date currentDate = new Date();
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( currentDate ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( currentDate ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      // Client customizations for WIP #43735 starts
      else if ( alert.getActivityType().equals( ActivityType.PENDING_CLAIM_AWARD ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "coke.cash.recognition.CLAIM_AWARD" ) );
        alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "coke.cash.recognition.CLAIM_AWARD_MSG" ), new Object[] { alert.getPromotionName() } ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PENDING_CLAIM_AWARD, null ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( alert.getApprovedOn() ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getApprovedOn(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setDueDateSort( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      // Client customization for WIP 58122
      else if ( alert.getActivityType().equals( ActivityType.PENDING_CLAIM_AWARD_NOMINATION ) )
      {
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "coke.custom.nomination.REDEEM_NOMINATION_CLAIM_AWARD" )  );
        alertView.setAlertSubject( MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "coke.custom.nomination.CLAIM_AWARD_MSG" ), new Object[] { alert.getPromotionName() } ) );
        alertView.setAlertLinkUrl( buildAlertUrl( alert, request, PENDING_CLAIM_AWARD_NOMINATION, null ) );
        alertView.setDatePostedDisplay( DateUtils.toDisplayString( alert.getApprovedOn() ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( alert.getApprovedOn(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setDueDateSort( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setLink( true );
        alertView.setOpenNewWindow( false );
      }
      // Client customizations for WIP #43735 ends
    }
    return alertView;
  }

  private String buildSAAlertUrl( AlertsValueBean alert, HttpServletRequest request )
  {
    String url = null;
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "programId", alert.getProgramId() );
    url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_SA_CONTRIBUTION_LIST, clientStateParameterMap );
    return url;
  }

  private String buildPaxBeanText( Long userId, Long batchId )
  {
    StringBuffer sb = new StringBuffer( "" );
    List<AwardGeneratorManagerPaxBean> paxList = getAwardGeneratorService().getPaxListByMgrAndBatchId( userId, batchId );
    if ( paxList != null && paxList.size() > 0 )
    {
      try
      {
        sb.append( "<table id=\"\" class=\"table table-bordered table-striped\"> <tbody> <thead> <tr><th>" ).append( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NAME" ) )
            .append( "</th>" ).append( "<th>" ).append( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.ANNIVERSARY_DATE" ) ).append( "</th>" ).append( "<th>" )
            .append( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.AWARD_DATE" ) ).append( "</th>" ).append( "</tr> </thead>" );
        for ( AwardGeneratorManagerPaxBean paxBean : paxList )
        {
          sb.append( " <tr><td> " ).append( paxBean.getName() ).append( " </td>" );

          sb.append( "  <td>  " ).append( DateUtils.toDisplayString( paxBean.getAnniversaryDate() ) ).append( "</td>" );
          sb.append( "   <td>" ).append( DateUtils.toDisplayString( paxBean.getAwardDate() ) ).append( "</td>" );

          sb.append( "  <td>  " )
              .append( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( DateUtils.toDisplayString( paxBean.getAnniversaryDate() ) ), UserManager.getTimeZoneID() ) ) )
              .append( "</td>" );
          sb.append( "   <td>" ).append( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( DateUtils.toDisplayString( paxBean.getAwardDate() ) ), UserManager.getTimeZoneID() ) ) )
              .append( "</td>" );

        }
        sb.append( "</tr></tbody></table>" );

      }
      catch( Exception e )
      {
        log.error( e.getMessage(), e );
      }
    }
    return sb.toString();
  }

  private String buildAlertUrl( AlertsValueBean alert, HttpServletRequest request, int code, SSIContest contest )
  {
    String url = null;
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    // File downloads don't map to promotions, so lets check first
    if ( alert.getPromotion() != null )
    {
      clientStateParameterMap.put( "promotionId", alert.getPromotion().getId() );
    }




    switch ( code )
    {
      case GOAL_QUEST:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_GOALQUEST_SELECT_YOUR_GOAL, clientStateParameterMap );
        break;
      case CHALLENGE_POINT:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_GOALQUEST_SELECT_YOUR_GOAL, clientStateParameterMap );
        break;
      case QUIZ:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_QUIZ_CLAIM_SUBMISSION, clientStateParameterMap );
        break;
      case PURL_CONTRIBUTION:

        clientStateParameterMap.put( "isDefaultInvitee", alert.isDefaultInvitee() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_LIST, clientStateParameterMap );
        break;
      case PURL_CONTRIBUTION_SINGLE:

        clientStateParameterMap.put( "purlContributorId", alert.getPurlContributorId() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_SINGLE, clientStateParameterMap );
        break;
      case PURL_INVITATION:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_MAINTENANCE_LIST, clientStateParameterMap );
        break;
      case PURL_VIEW:
        clientStateParameterMap.put( "purlRecipientId", alert.getPurlRecipientId() );
        clientStateParameterMap.put( "loggedinUserId", UserManager.getUserId() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PURL_RECIPIENT, clientStateParameterMap );
        break;
      case RECOGNITION:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_RECOGNITION_APPROVAL_LIST, clientStateParameterMap );
        break;
      case SURVEY:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_SURVEY, clientStateParameterMap );
        break;
      case NOMINATION:
        NominationsApprovalValueBean nominationsApprovalValueBean = alert.getNominationsApprovalValueBean();
        clientStateParameterMap.put( "defaultDisplayPendingApprovals", Boolean.TRUE );
        clientStateParameterMap.put( "isAllApprovablesApproved", Boolean.FALSE );
        clientStateParameterMap.put( "promotionId", nominationsApprovalValueBean.getPromotionId() );
        clientStateParameterMap.put( "approverUserId", UserManager.getUserId() );
        clientStateParameterMap.put( "levelNumber", nominationsApprovalValueBean.getApprovalLevel() );
        clientStateParameterMap.put( "claimId", nominationsApprovalValueBean.getClaimId().toString() );
        clientStateParameterMap.put( "status", nominationsApprovalValueBean.getStatus() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_NOMINATION_APPROVAL_LIST, clientStateParameterMap );
        break;
      case PRODUCT_CLAIM:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_CLAIM_PRODUCT_APPROVE_LIST, clientStateParameterMap );
        break;
      case BUDGET_END:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_SUBMIT_RECOGNITION, clientStateParameterMap );
        break;
      case FILE_DOWNLOAD:
        clientStateParameterMap.put( "fileStoreId", alert.getFileStoreId() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_FILE_DOWNLOAD, clientStateParameterMap );
        break;
      case AWARD_GENERATOR_MANAGER_REMINDER:
        clientStateParameterMap.put( "batchId", alert.getBatchId() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.AWARD_GENERATOR_MANAGER_DISMISS_REMINDER, clientStateParameterMap );
        break;
      case SURVEY_ALERT_LIST:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_SURVEY_LIST, clientStateParameterMap );
        break;
      case SURVEY_TAKE:
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.TAKE_SURVEY, clientStateParameterMap );
        break;
      case CELEBRATION_MANAGER_REMINDER:
        clientStateParameterMap.put( "managerMessageId", alert.getCelebrationManagerMessageId() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_CELEBRATION_MANAGER_MESSAGE, clientStateParameterMap );
        break;
      case SSI_APPROVAL_SUMMARY:
        clientStateParameterMap.put( "contestId", alert.getParticipantAlert().getAlertMessage().getContestId() );
        if ( SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
        {
          url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_ISSUANCE_APPROVAL_SUMMARY, clientStateParameterMap );
        }
        else
        {
          url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_APPROVAL_SUMMARY, clientStateParameterMap );
        }
        break;
      case SSI_PROGRESS_LOAD_CREATOR_ALERT:
        clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, alert.getParticipantAlert().getAlertMessage().getContestId().toString() );
        clientStateParameterMap.put( SSIContestUtil.USER_ID, alert.getParticipantAlert().getUser().getId().toString() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_CREATOR_DETAIL_URL, clientStateParameterMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );
        break;
      case SSI_PROGRESS_LOAD_MGR_ALERT:
        clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, alert.getParticipantAlert().getAlertMessage().getContestId().toString() );
        clientStateParameterMap.put( SSIContestUtil.USER_ID, alert.getParticipantAlert().getUser().getId().toString() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_MGR_DETAIL_URL, clientStateParameterMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );
        break;
      case SSI_PROGRESS_LOAD_PAX_ALERT:
        clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, alert.getParticipantAlert().getAlertMessage().getContestId().toString() );
        clientStateParameterMap.put( SSIContestUtil.USER_ID, alert.getParticipantAlert().getUser().getId().toString() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_PAX_DETAIL_URL, clientStateParameterMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );
        break;
      case SSI_CLAIM_APPROVAL:
        clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, alert.getParticipantAlert().getAlertMessage().getContestId().toString() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_CLAIM_APPROVAL_LIST, clientStateParameterMap );
        break;
      case NOMINATOR_MOREINFO:
        clientStateParameterMap.put( "claimId", alert.getClaimId() );
        url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.NOMINATOR_REQUEST_MORE_INFO, clientStateParameterMap );
        break;
        // Client customization for WIP 58122
      case PENDING_CLAIM_AWARD_NOMINATION:
          clientStateParameterMap.put( "claimId", alert.getClaimId() );
          url = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.TCCC_CLAIM_AWARD_NOMINATION_DISPLAY, clientStateParameterMap );
          break;
 

      default:
        break;
    }
    return url;
  }

  private List<AlertsValueBean> getAlertsList( AuthenticatedUser authUser, List<PromotionMenuBean> eligiblePromotions, HttpServletRequest request )
  {
    // in alerts tab - always pull latest data from DB and store in session

    List<AlertsValueBean> alertsList = getPromotionService().getAlertsList( authUser, authUser.getUserId(), false, eligiblePromotions, true );


    request.getSession().setAttribute( "alertsList", alertsList );
    return alertsList;
  }

  private AlertView buildPlateauAwardsReminderAlerts( HttpServletRequest request )
  {
    AlertView alertView = null;
    boolean enablePlateauAwardReminder = getSystemVariableService().getPropertyByName( SystemVariableService.PLATEAU_AWARDS_REMINDER_ENABLED ).getBooleanVal();

    if ( enablePlateauAwardReminder )
    {
      int numberOfPaxWithUnclaimedAwards = getPlateauAwardsService().getNumberOfPaxWithUnclaimedAwardsFor( UserManager.getUserId() );

      if ( numberOfPaxWithUnclaimedAwards > 0 )
      {
        alertView = new AlertView();
        Map<String, Long> clientStateParameterMap = new HashMap<String, Long>();
        alertView.setLink( true );
        alertView.setAlertLinkUrl( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_PLATEAU_AWARD_REMINDERS, clientStateParameterMap ) );
        alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEND_REMINDER" ) );
        alertView.setAlertSubject( MessageFormat.format( ContentReaderManager.getText( "profile.alerts.messages", "YOU_HAVE_AWARDS_TO_REDEEM" ), new Object[] { numberOfPaxWithUnclaimedAwards } ) );
        alertView.setDatePostedDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setDatePostedSort( DateUtils.getSortDateString( DateUtils.applyTimeZone( new Date(), UserManager.getTimeZoneID() ) ) );
        alertView.setDueDateDisplay( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.SEE_DETAILS" ) );
        alertView.setOpenNewWindow( false );
      }
    }

    return alertView;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private AlertMessageService getAlertMessageService()
  {
    return (AlertMessageService)getService( AlertMessageService.BEAN_NAME );
  }

  private PlateauAwardsService getPlateauAwardsService()
  {
    return (PlateauAwardsService)getService( PlateauAwardsService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private AwardGeneratorService getAwardGeneratorService()
  {
    return (AwardGeneratorService)getService( AwardGeneratorService.BEAN_NAME );
  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }
}

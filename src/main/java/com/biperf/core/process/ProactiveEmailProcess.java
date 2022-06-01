/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ProactiveEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.MatchDAO;
import com.biperf.core.dao.throwdown.RoundDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.diyquiz.DIYQuizParticipant;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionNotificationFrequencyType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionPayout;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalParticipant;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.PromotionPartnerAudience;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.mobileapp.recognition.service.BudgetEndNotification;
import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.mobileapp.recognition.service.RecognitionInactivityNotification;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetSegmentToBudgetsAssociationRequest;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.diyquiz.DIYQuizService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToUsersAssociationRequest;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionNotificationService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.GoalLevelValueBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * /** This process should be scheduled to run DAILY. It sends "proactive" Emails to participants
 * for all eligible promotions. There are 4 types of alert emails, and 1 summary email. Rules for
 * sending are as follows: 1) Promotion Program Launch Alert: Pax to receive an email when the
 * process is run on X number of days prior to the start date of all Complete Promotion(s). 2)
 * Program End Alert: Participants to receive an email when the process is run on X number of days
 * prior to the end date of all Live Promotion(s). Not applicable to promotions with no end dates.
 * 3) Inactivity Alert: Participants to receive an email when that participant has been inactive
 * (based on the promotion module, i.e. has not taken the quiz, or given a recognition, or submitted
 * a claim) X number of days prior to the day the process is run. 4) Approver Reminder: Send an
 * email reminder to approvers with pending claims after a specified period of time has past the
 * claim submission date at a frequency predefined on the promotion notification setup. 5) Goal Not
 * Selected Alert: Participants to receive an email when the process is run on X number of days
 * prior to the goal selection end date of Live GoalQuest Promotion(s). For alerts 1) to 3) - the
 * audience to receive the email is the primary audience defined on the promotion. E.g. for Quiz:
 * submitter audience; Recognition: Givers audience; Product Claim: Submitter audience. For alert 4) -
 * the audience to receive the email is the approver audience defined on the promotion who has
 * pending claims after X number of days past submission. The email is sent on the frequency at
 * which the promotion is setup to use. Each approver should get 1 email for all the overdue
 * approvals for 1 promotion. Summary Email: The person who launches/schedules the process is to
 * receive a summary email for each of the above emails, stating the total number of participants
 * who have received the alert.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tcheng</td>
 * <td>Dec 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

@SuppressWarnings( { "rawtypes", "unchecked", "unused" } )
public class ProactiveEmailProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ProactiveEmailProcess.class );

  public static final String BEAN_NAME = "proactiveEmailProcess";

  public static final String LAUNCH_MESSAGE_NAME = "Proactive Notifications Process - Program Launch";
  public static final String END_MESSAGE_NAME = "Proactive Notifications Process - Program End";
  public static final String INACTIVE_MESSAGE_NAME = "Proactive Notifications Process - Inactivity";
  public static final String APPROVER_REMINDER_MESSAGE_NAME = "Proactive Notifications Process - Approver Reminder";
  public static final String PURL_MANAGER_NONRESPONSE_MESSAGE_NAME = "Proactive Notification Process - Purl Manager Nonresponse";
  public static final String PURL_CONTRIBUTOR_NONRESPONSE_MESSAGE_NAME = "Proactive Notification Process - Purl Contributor Nonresponse";
  public static final String GOAL_NOT_SELECTED_MESSAGE = "Proactive Notifications Process - Goal Not Selected";
  public static final String BUDGET_SWEEP_MESSAGE = "Proactive Notifications Process - Budget Sweep";
  public static final String BUDGET_END_MESSAGE = "Proactive Notifications Process - Budget End";
  public static final String CHALLENGEPOINT_NOT_SELECTED_MESSAGE = "Proactive Notifications Process - Challengepoint Not Selected";
  public static final String PARTNER_SELECTED = "Proactive Notifications Process - Partner - Selected";
  public static final String GQ_CP_AWARD_REDEEM_MESSAGE_NAME = "Proactive Notifications Process - GQ/CP Award NonRedemption";
  public static final String DIY_QUIZ_START_MESSAGE = "Proactive Notifications Process - DIY Quiz Launch";
  public static final String TD_NEXT_ROUND = "Proactive Notifications Process - Throwdown next round";
  public static final String TD_PROMO_LAUNCH_PAX = "Proactive Notifications Process - Throwdown promotion launch for participant";
  public static final String TD_PROMO_LAUNCH_MANAGER = "Proactive Notifications Process - Throwdown promotion launch for manager";
  public static final String APPROVER_NOTIFICATION_MESSAGE_NAME = "Proactive Notifications Process - Approver Notification";
  public static final String APPROVER_REMINDER_END_DATE_MESSAGE_NAME = "Proactive Notifications Process - Approver Reminder - Approval End Date";

  public static final String INACTIVE_QUIZ_DEADLINE = "The deadline to complete the quiz is ";
  public static final String INACTIVE_CLAIM_DEADLINE = "The deadline to submit claims is ";
  public static final String INACTIVE_RECOGNITION_DEADLINE = "The deadline to give recognition is ";

  public static final String PRODUCT_CLAIM_APPROVAL_LINK = "/claim/approvalsProductClaimList.do";
  public static final String NOMINATION_APPROVAL_LINK = "/claim/approvalsNominationList.do";
  public static final String RECOGNITION_APPROVAL_LINK = "/claim/approvalsRecognitionList.do";

  // Services injected
  protected PromotionService promotionService;
  protected PromotionNotificationService promotionNotificationService;
  protected ClaimService claimService;
  protected ClaimGroupService claimGroupService;
  protected NodeService nodeService;
  protected PaxGoalService paxGoalService;
  protected MerchOrderService merchOrderService;
  protected ChallengePointService challengePointService;
  protected BudgetMasterService budgetMasterService;
  protected PurlService purlService;
  protected DIYQuizService diyQuizService;
  protected JournalService journalService;
  protected GoalQuestService goalQuestService;
  protected CelebrationService celebrationService;
  protected TeamService teamService;
  private CMAssetService cmAssetService;
  private ParticipantService participantService;
  private NominationPromotionService nominationPromotionService;
  private MobileNotificationService mobileNotificationService;
  private int totalPromoLaunchPaxCount = 0;
  private int totalPromoEndPaxCount = 0;
  private int totalInactivePaxCount = 0;
  private int totalApproverReminderPaxCount = 0;
  private int totalGoalNotSelectedPaxCount = 0;
  private int totalChallengepointNotSelectedCount = 0;
  private int totalBudgetSweepPaxCount = 0;
  private int totalBudgetEndPaxCount = 0;
  private int totalManagerNonResponseCount = 0;
  private int totalContributorNonResponseCount = 0;
  private int totalPartnerPaxCount = 0;
  private int totalGqCpRedemptionCount = 0;
  private int totalDIYEmailPaxCount = 0;
  private int totalThrowdownNextRoundPaxCount = 0;
  private int throwdownPromoLaunchPaxCount = 0;
  private int throwdownPromoLaunchManagerCount = 0;
  private int totalApproverNotificationCount = 0;
  private int totalApproverReminderEndDatePaxCount = 0;

  // This process does not take in any parameters -

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    // Get All Non-Expired Promotions
    ArrayList promotionList = (ArrayList)promotionService.getAllNonExpired();
    int totalCount = 0;

    // For each Non-Expired Promotion
    for ( Iterator promotionListIter = promotionList.iterator(); promotionListIter.hasNext(); )
    {
      Promotion promotion = (Promotion)promotionListIter.next();
      // Get Promotion Notifications
      ArrayList notifications = (ArrayList)promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() );

      // For each Promotion Notification
      for ( Iterator notificationsIter = notifications.iterator(); notificationsIter.hasNext(); )
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationsIter.next();
        totalCount = processProActive( promotion, promotionNotificationType, false, true );
      } // end iterating notificationsIter

      // For DIY Quiz
      processDIYQuizEmails( promotion );

    } // end iterating promotionListIter

    /* Send summary emails */
    sendSummaryMessage( totalPromoLaunchPaxCount, LAUNCH_MESSAGE_NAME );
    sendSummaryMessage( totalPromoEndPaxCount, END_MESSAGE_NAME );
    sendSummaryMessage( totalInactivePaxCount, INACTIVE_MESSAGE_NAME );
    sendSummaryMessage( totalApproverReminderPaxCount, APPROVER_REMINDER_MESSAGE_NAME );
    sendSummaryMessage( totalApproverReminderEndDatePaxCount, APPROVER_REMINDER_END_DATE_MESSAGE_NAME );
    sendSummaryMessage( totalApproverNotificationCount, APPROVER_NOTIFICATION_MESSAGE_NAME );
    sendSummaryMessage( totalPartnerPaxCount, PARTNER_SELECTED );
    sendSummaryMessage( totalDIYEmailPaxCount, DIY_QUIZ_START_MESSAGE );
    // Send 'Goal Not Selected' summary if 'Goalquest' module is installed.
    if ( isModuleInstalled( SystemVariableService.INSTALL_GOAL_QUEST ) )
    {
      sendSummaryMessage( totalGoalNotSelectedPaxCount, GOAL_NOT_SELECTED_MESSAGE );
    }
    // Send 'Budget Sweep' summary if 'Recognition' module is installed.
    if ( isModuleInstalled( SystemVariableService.INSTALL_RECOGNITION ) )
    {
      sendSummaryMessage( totalBudgetSweepPaxCount, BUDGET_SWEEP_MESSAGE );
      sendSummaryMessage( totalBudgetEndPaxCount, BUDGET_END_MESSAGE );
      sendSummaryMessage( totalManagerNonResponseCount, PURL_MANAGER_NONRESPONSE_MESSAGE_NAME );
      sendSummaryMessage( totalContributorNonResponseCount, PURL_CONTRIBUTOR_NONRESPONSE_MESSAGE_NAME );
    }
    // Only send the challengepoint not selected summary if challengepoint has been installed.
    if ( isModuleInstalled( SystemVariableService.INSTALL_CHALLENGEPOINT ) )
    {
      sendSummaryMessage( totalChallengepointNotSelectedCount, CHALLENGEPOINT_NOT_SELECTED_MESSAGE );
    }
    // Only send the throwdown next round summary if throwdown has been installed.
    if ( isModuleInstalled( SystemVariableService.INSTALL_THROWDOWN ) )
    {
      sendSummaryMessage( totalThrowdownNextRoundPaxCount, TD_NEXT_ROUND );
      sendSummaryMessage( throwdownPromoLaunchPaxCount, TD_PROMO_LAUNCH_PAX );
      sendSummaryMessage( throwdownPromoLaunchManagerCount, TD_PROMO_LAUNCH_MANAGER );
    }
    sendSummaryMessage( totalGqCpRedemptionCount, GQ_CP_AWARD_REDEEM_MESSAGE_NAME );
  } // end method onExecute()

  private void processDIYQuizEmails( Promotion promotion )
  {
    Date today = new Date();
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();

    if ( promotion.isDIYQuizPromotion() && promotion.isLive() )
    {
      // Get all active quizzes for this promotion
      List<DIYQuiz> activeQuizzes = getDiyQuizService().getQuizByPromotionAndStatus( "active", promotion.getId() );

      Message message = messageService.getMessageByCMAssetCode( MessageService.DIY_QUIZ_NOTIFY_PARTICIPANT_MESSAGE_CM_ASSET_CODE );

      for ( DIYQuiz diyQuiz : activeQuizzes )
      {
        // Check if the Quiz end date is not in Past
        if ( diyQuiz != null && diyQuiz.getEndDate() != null && !StringUtil.isEmpty( diyQuiz.getNotificationText() )
            && ( DateUtils.isSameDay( today, diyQuiz.getEndDate() ) || diyQuiz.getEndDate().after( today ) ) )
        {
          Mailing mailing = new Mailing();
          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setSender( sender );
          mailing.setDeliveryDate( new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() ) );
          mailing.setMessage( message );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );

          Set<MailingRecipient> recipients = new HashSet<MailingRecipient>();
          Set<DIYQuizParticipant> participants = diyQuiz.getParticipants();
          if ( null != participants )
          {
            for ( DIYQuizParticipant diyParticipant : participants )
            {
              // Add only the recipients that have not been notified before.
              if ( !diyParticipant.getIsNotificationSent() )
              {
                recipients.add( getDiyQuizService().getMailingRecipient( diyParticipant.getParticipant() ) );
                totalDIYEmailPaxCount++;
              }
            }
          }
          mailing.addMailingRecipients( recipients );

          User quizOwner = diyQuiz.getOwner();

          Map objectMap = new HashMap();
          // Add the personalization data to the objectMap
          // Message Library Insert Field is using promotionName for this token
          objectMap.put( "promotionName", diyQuiz.getName() );
          objectMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( diyQuiz.getStartDate(), getParticipantLocale( quizOwner ) ) );
          objectMap.put( "managerFirstName", quizOwner.getFirstName() );
          objectMap.put( "managerLastName", quizOwner.getLastName() );
          objectMap.put( "managerComments", diyQuiz.getNotificationText() );
          objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

          try
          {
            mailingService.submitMailing( mailing, objectMap );
            // Once the notification is sent, "isNotificationSent" is set to True.
            Set<DIYQuizParticipant> notifiedParticipants = new HashSet<DIYQuizParticipant>();
            if ( null != participants )
            {
              for ( DIYQuizParticipant diyParticipant : participants )
              {
                diyParticipant.setIsNotificationSent( true );
                notifiedParticipants.add( diyParticipant );
              }
              diyQuiz.getParticipants().clear();
              diyQuiz.setParticipants( notifiedParticipants );
            }
          }
          catch( Exception e )
          {
            log.error( "An exception occurred while sending DIYQuiz Email for promotion: " + promotion.getName() );
            addComment( "An exception occurred while sending DIYQuiz Email for promotion: " + promotion.getName() + ". See the log file for additional information.  " );
          }
        }
      }
    }
  }

  Locale getParticipantLocale( User user )
  {
    return LocaleUtils.getLocale( user.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : user.getLanguageType().getCode() );
  }

  protected boolean isModuleInstalled( String module )
  {
    return systemVariableService.getPropertyByName( module ).getBooleanVal();
  }

  protected Map getPaxBudgetMap( BudgetSegment budgetSegment )
  {
    Map paxBudgetMap = new HashMap();

    Set<Budget> budgets = budgetSegment.getBudgets();
    if ( null != budgets )
    {
      for ( Iterator iter = budgets.iterator(); iter.hasNext(); )
      {
        Budget paxBudget = (Budget)iter.next();
        // 1. PAX tied to this budget
        // 2. Budget should be Active and
        // 3. Budget Original Value > 0
        // 4. Budget Current Value > 0
        if ( null != paxBudget.getUser() && BudgetStatusType.ACTIVE.equals( paxBudget.getStatus().getCode() ) && paxBudget.getOriginalValue().intValue() > 0
            && paxBudget.getCurrentValue().intValue() > 0 )
        {
          paxBudgetMap.put( paxBudget.getUser().getId(), paxBudget );
        }
      }
    }

    return paxBudgetMap;
  }

  /**
   * Compose and send the Budget Sweep alert emails
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected int sendBudgetSweepMessage( Set recipients, long messageId, RecognitionPromotion promoRecognition, Map paxBudgetMap, Date budgetSweepDate )
  {
    int recipientCount = 0;

    // Add the personalization data to the objectMap
    Map objectMap = new HashMap();
    /*
     * objectMap.put( "sweepDate", com.biperf.core.utils.DateUtils.toDisplayString(
     * promoRecognition.getBudgetSweepDate() ) );
     */ // TODO have to clean up this code for PROMO_BUDGET_SWEEP
    objectMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

    // Compose the generic portion of the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    // Set the recipients on the mailing
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient mr = (MailingRecipient)iter.next();

      Budget paxBudget = (Budget)paxBudgetMap.get( mr.getUser().getId() );
      if ( null != paxBudget )
      {
        BigDecimal recipientMediaValue = userService.getBudgetMediaValueForUser( paxBudget.getUser().getId() );
        BigDecimal convertedBudgetAmt = BudgetUtils.applyMediaConversion( paxBudget.getCurrentValue(), US_MEDIA_VALUE, recipientMediaValue );
        mailing.addMailingRecipient( mr );

        // Set recipient specific data on the MailingRecipientData
        MailingRecipientData mrd1 = new MailingRecipientData();
        mrd1.setKey( "firstName" );
        mrd1.setValue( paxBudget.getUser().getFirstName() );
        mr.addMailingRecipientData( mrd1 );

        MailingRecipientData mrd2 = new MailingRecipientData();
        mrd2.setKey( "lastName" );
        mrd2.setValue( paxBudget.getUser().getLastName() );
        mr.addMailingRecipientData( mrd2 );

        MailingRecipientData mrd3 = new MailingRecipientData();
        mrd3.setKey( "budgetRemaining" );
        mrd3.setValue( String.valueOf( BudgetUtils.getBudgetDisplayValue( convertedBudgetAmt ) ) );
        mr.addMailingRecipientData( mrd3 );

        MailingRecipientData mrd4 = new MailingRecipientData();
        mrd4.setKey( "sweepDate" );
        if ( budgetSweepDate != null )
        {
          mrd4.setValue( String.valueOf( com.biperf.core.utils.DateUtils.toDisplayString( budgetSweepDate, LocaleUtils.getLocale( mr.getLocale() ) ) ) );
        }
        mr.addMailingRecipientData( mrd4 );
      }

      if ( promoRecognition.getPromoNameAssetCode() != null )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( getPromotionName( promoRecognition.getPromoNameAssetCode(), mr.getLocale() ) );
        mr.addMailingRecipientData( promotionName );
      }

      if ( promoRecognition.getAwardType() != null )
      {
        MailingRecipientData awardMedia = new MailingRecipientData();
        awardMedia.setKey( "awardMedia" );
        // awardMedia.setValue( cmAssetService.getString( "picklist.promotion.awardstype.items",
        // "NAME", CmsUtil.getLocale( mr.getLocale() ), true ) );
        ContentReader contentReader = ContentReaderManager.getContentReader();
        List promotionAwardList = new ArrayList();
        if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) ) instanceof java.util.List )
        {
          promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) );
          for ( Object content : promotionAwardList )
          {
            Content contentData = (Content)content;
            Map m = contentData.getContentDataMapList();
            Translations nameObject = (Translations)m.get( "CODE" );

            if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
            {
              Translations valueObject = (Translations)m.get( "NAME" );
              awardMedia.setValue( valueObject.getValue() );
              mr.addMailingRecipientData( awardMedia );
              break;
            }

          }
        }

      }
    }

    recipientCount = mailing.getMailingRecipients().size();
    if ( recipientCount > 0 )
    {
      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, objectMap );

        log.debug( "Number of recipients attempted for Budget Sweep Email: " + recipientCount );
        addComment( recipientCount + " users need Budget Sweep email for promotion: " + promoRecognition.getName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Budget Sweep Email for promotion: " + promoRecognition.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while sending Budget Sweep Email for promotion: " + promoRecognition.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }
    }

    return recipientCount;
  }

  /**
   * Compose and send the Budget End alert emails
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendBudgetEndMessage( Set recipients, long messageId, RecognitionPromotion promoRecognition, Map budgetMap, BudgetMaster budgetMaster )
  {
    int recipientCount = 0;

    // Add the personalization data to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

    // Compose the generic portion of the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    List<BudgetEndNotification> budgetEndNotifications = new ArrayList<BudgetEndNotification>();

    // Set the recipients on the mailing
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient mr = (MailingRecipient)iter.next();

      Budget budgetData = (Budget)budgetMap.get( mr.getUser().getId() );
      String budgetRemaining = "";
      String endDate = "";
      String promotionName = "";
      String awardMedia = "";
      if ( null != budgetData )
      {
        mailing.addMailingRecipient( mr );

        // Set recipient specific data on the MailingRecipientData
        MailingRecipientData mrd1 = new MailingRecipientData();
        mrd1.setKey( "firstName" );
        mrd1.setValue( mr.getUser().getFirstName() );
        mr.addMailingRecipientData( mrd1 );

        MailingRecipientData mrd2 = new MailingRecipientData();
        mrd2.setKey( "lastName" );
        mrd2.setValue( mr.getUser().getLastName() );
        mr.addMailingRecipientData( mrd2 );

        BigDecimal convertedBudgetValue = null;
        if ( budgetData.getBudgetSegment().getBudgetMaster().isCentralBudget() )
        {
          convertedBudgetValue = budgetData.getCurrentValue();
        }
        else
        {
          final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
          final BigDecimal RECIPIENT_MEDIA_VALUE = userService.getBudgetMediaValueForUser( mr.getUser().getId() );
          convertedBudgetValue = BudgetUtils.applyMediaConversion( budgetData.getCurrentValue(), US_MEDIA_VALUE, RECIPIENT_MEDIA_VALUE );
        }

        MailingRecipientData mrd3 = new MailingRecipientData();
        mrd3.setKey( "budgetRemaining" );
        budgetRemaining = String.valueOf( NumberFormatUtil.getUserLocaleBasedNumberFormat( BudgetUtils.getBudgetDisplayValue( convertedBudgetValue ), LocaleUtils.getLocale( mr.getLocale() ) ) );
        mrd3.setValue( budgetRemaining );
        mr.addMailingRecipientData( mrd3 );

        MailingRecipientData mrd4 = new MailingRecipientData();
        mrd4.setKey( "budgetName" );
        mrd4.setValue( budgetMaster.getBudgetName() );
        mr.addMailingRecipientData( mrd4 );

        MailingRecipientData mrd5 = new MailingRecipientData();
        mrd5.setKey( "endDate" );
        endDate = "";
        if ( promoRecognition.getSubmissionEndDate() != null && budgetData.getBudgetSegment().getEndDate() != null )
        {
          if ( promoRecognition.getSubmissionEndDate().before( budgetData.getBudgetSegment().getEndDate() ) )
          {
            endDate = com.biperf.core.utils.DateUtils.toDisplayString( promoRecognition.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
            mrd5.setValue( endDate );
          }
          else
          {
            endDate = com.biperf.core.utils.DateUtils.toDisplayString( budgetData.getBudgetSegment().getEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
            mrd5.setValue( endDate );
          }
        }
        else
        {
          endDate = com.biperf.core.utils.DateUtils.toDisplayString( budgetData.getBudgetSegment().getEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
          mrd5.setValue( endDate );
        }

        mr.addMailingRecipientData( mrd5 );

      }

      if ( promoRecognition.getPromoNameAssetCode() != null )
      {
        MailingRecipientData mailingPromotionName = new MailingRecipientData();
        mailingPromotionName.setKey( "promotionName" );
        promotionName = getPromotionName( promoRecognition.getPromoNameAssetCode(), mr.getLocale() );
        mailingPromotionName.setValue( promotionName );
        mr.addMailingRecipientData( mailingPromotionName );
      }

      if ( promoRecognition.getAwardType() != null )
      {
        MailingRecipientData mailingAwardMedia = new MailingRecipientData();
        mailingAwardMedia.setKey( "awardMedia" );
        ContentReader contentReader = ContentReaderManager.getContentReader();
        List promotionAwardList = new ArrayList();
        if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) ) instanceof java.util.List )
        {
          promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) );
          for ( Object content : promotionAwardList )
          {
            Content contentData = (Content)content;
            Map m = contentData.getContentDataMapList();
            Translations nameObject = (Translations)m.get( "CODE" );

            if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
            {
              Translations valueObject = (Translations)m.get( "NAME" );
              mailingAwardMedia.setValue( valueObject.getValue() );
              mr.addMailingRecipientData( mailingAwardMedia );
              break;
            }
          }
        }

      }

      BudgetEndNotification budgetNotification = new BudgetEndNotification( mr.getUser().getId(),
                                                                            mr.getUser().getLanguageType() != null ? mr.getUser().getLanguageType().getCode() : null,
                                                                            budgetRemaining,
                                                                            awardMedia,
                                                                            promotionName,
                                                                            endDate );
      budgetEndNotifications.add( budgetNotification );
    }

    recipientCount = mailing.getMailingRecipients().size();
    if ( recipientCount > 0 )
    {
      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, objectMap );

        log.debug( "Number of recipients attempted for Budget End Email: " + recipientCount );
        addComment( recipientCount + " users need Budget End email for promotion: " + promoRecognition.getName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Budget End Email for promotion: " + promoRecognition.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while sending Budget End Email for promotion: " + promoRecognition.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }

      // Send Budget End Push Notifications
      try
      {
        if ( budgetEndNotifications.size() > 0 )
        {
          mobileNotificationService.budgetEndNotification( budgetEndNotifications );
        }
        log.debug( "Number of recipients attempted for Budget End Mobile Notifications: " + recipientCount );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Budget End Mobile Push Notifications for promotion: " + promoRecognition.getName() + " (process invocation ID = " + getProcessInvocationId()
            + ")", e );
      }

    }

    // return recipientCount;
  }

  protected int sendGoalquestLaunchMessage( Set paxs, long messageId, GoalQuestPromotion promotion )
  {
    int mailingRecipientCount = 0;

    Map promoObjectMap = new HashMap();
    setPersonalizationData( promoObjectMap, promotion );

    List allUsers = new ArrayList();
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Participant participant = getParticipant( iter.next() );

      if ( null != participant && participant.isWelcomeEmailSent().booleanValue() )
      {
        allUsers.add( participant );
        ++mailingRecipientCount;
      }
    }

    boolean sampleEmailSent = false;
    File file = null;
    for ( Iterator iter = allUsers.iterator(); iter.hasNext(); )
    {
      try
      {
        Participant participant = (Participant)iter.next();
        if ( !isNotice() || isNotice() && !sampleEmailSent )
        {
          MailingRecipient mailingRecipient = getMailingRecipient( participant );
          if ( isNotice() && !sampleEmailSent )
          {
            mailingRecipient = getMailingRecipient( getRunByUser() );
            file = extractFile( allUsers, BEAN_NAME );
          }
          // Compose the mailing
          Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );
          Map promoObjectMapLocaleBased = new HashMap();

          setPersonalizationDataWithLocale( promoObjectMapLocaleBased, promotion, participant );
          mailingRecipient.addMailingRecipientDataFromMap( promoObjectMapLocaleBased );
          // Set the recipients on the mailing
          mailing.addMailingRecipient( mailingRecipient );
          // Add pax personalization data to the promoObjectMap
          Map paxObjectMap = new HashMap( promoObjectMap );
          setPersonalizationData( paxObjectMap, promotion, participant );
          if ( file != null )
          {
            String absPath = file.getAbsolutePath();
            String fileName = file.getName();
            mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
          }
          mailingService.submitMailing( mailing, paxObjectMap );
          sampleEmailSent = true;
        }
      }
      catch( Exception e )
      {
        String message = "An exception occurred while sending GoalQuest Welcome Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")";
        log.error( message, e );
        addComment( message );
      }
    }

    return mailingRecipientCount;
  }

  protected MailingRecipient getMailingRecipient( User user )
  {
    MailingRecipient mailingRecipient = null;
    LanguageType languageType = user.getLanguageType();

    mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setLocale( languageType != null ? languageType.getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    mailingRecipient.setUser( user );
    return mailingRecipient;
  }

  protected int sendChallengepointLaunchMessage( Set paxs, long messageId, ChallengePointPromotion promotion )
  {
    log.debug( "sending challengepoint launch message" );
    int mailingRecipientCount = 0;

    List allUsers = new ArrayList();
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      log.debug( "sending challengepoint launch message to participants" );
      Object paxTypeUnknown = iter.next();
      log.debug( "sending challengepoint launch message to participants" + paxTypeUnknown.getClass() );
      Participant participant = null;
      // paxs might be AudienceParticipant objects or Participants or FormattedValueBeans
      if ( paxTypeUnknown instanceof Participant )
      {
        participant = (Participant)paxTypeUnknown;
      }
      else if ( paxTypeUnknown instanceof AudienceParticipant )
      {
        participant = ( (AudienceParticipant)paxTypeUnknown ).getParticipant();
      }
      else if ( paxTypeUnknown instanceof FormattedValueBean )
      {
        participant = participantService.getParticipantById( ( (FormattedValueBean)paxTypeUnknown ).getId() );
      }
      if ( null != participant && participant.isWelcomeEmailSent().booleanValue() )
      {
        allUsers.add( participant );
        ++mailingRecipientCount;
      }
    }

    boolean sampleEmailSent = false;
    File file = null;
    for ( Iterator iter = allUsers.iterator(); iter.hasNext(); )
    {
      try
      {
        Participant participant = (Participant)iter.next();
        if ( !isNotice() || isNotice() && !sampleEmailSent )
        {
          MailingRecipient mailingRecipient = getMailingRecipient( participant );
          if ( isNotice() && !sampleEmailSent )
          {
            mailingRecipient = getMailingRecipient( getRunByUser() );
            file = extractFile( allUsers, BEAN_NAME );
          }
          // Compose the mailing
          Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

          Map promoObjectMap = new HashMap();
          setPersonalizationDataForCP( promoObjectMap, promotion, participant );
          mailingRecipient.addMailingRecipientDataFromMap( promoObjectMap );
          // Add pax personalization data to the promoObjectMap
          Map paxObjectMap = new HashMap();
          setPersonalizationData( paxObjectMap, promotion, participant );
          // Set the recipients on the mailing
          mailing.addMailingRecipient( mailingRecipient );
          if ( file != null )
          {
            String absPath = file.getAbsolutePath();
            String fileName = file.getName();
            mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
          }
          mailingService.submitMailing( mailing, paxObjectMap );
          sampleEmailSent = true;
        }
      }
      catch( Exception e )
      {
        String message = "An exception occurred while sending Challengepoint Welcome Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")";
        log.error( message, e );
        addComment( message );
      }
    }
    return mailingRecipientCount;
  }

  protected int sendThrowdownLaunchMessage( List<Match> matchList, long messageId, ThrowdownPromotion promotion )
  {
    log.debug( "sending throwdown launch message" );
    int mailingRecipientCount = 0;

    List<Participant> allUsers = new ArrayList<Participant>();
    for ( Iterator<Match> iter = matchList.iterator(); iter.hasNext(); )
    {
      log.debug( "sending throwdown launch message to participants" );
      Match match = (Match)iter.next();
      log.debug( "sending throwdown launch message to participants" + match.getClass() );
      Participant participant = null;
      // Getting the count of audience from the matchoutcomes.
      for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
      {
        if ( !matchTeamOutcome.getTeam().isShadowPlayer() )
        {
          allUsers.add( matchTeamOutcome.getTeam().getParticipant() );
          ++mailingRecipientCount;
        }
      }
    }

    // sending promotion launch email to promotion audiences.
    boolean sampleEmailSent = false;
    File file = null;
    for ( Iterator<Match> iter = matchList.iterator(); iter.hasNext(); )
    {
      try
      {
        Match match = (Match)iter.next();
        for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
        {
          if ( !matchTeamOutcome.getTeam().isShadowPlayer() )
          {
            Participant pax = matchTeamOutcome.getTeam().getParticipant();
            if ( !isNotice() || isNotice() && !sampleEmailSent )
            {
              MailingRecipient mailingRecipient = getMailingRecipient( pax );
              if ( isNotice() && !sampleEmailSent )
              {
                mailingRecipient = getMailingRecipient( getRunByUser() );
                file = extractFile( allUsers, BEAN_NAME );
              }
              // Compose the mailing
              Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

              Map promoObjectMap = new HashMap();
              setPersonalizationData( promoObjectMap, promotion, pax );
              setPersonalizationDataForTD( promoObjectMap, promotion, match, pax );
              setPayoutInformationForTD( promoObjectMap, promotion, match, pax );

              mailingRecipient.addMailingRecipientDataFromMap( promoObjectMap );
              // Set the recipients on the mailing
              mailing.addMailingRecipient( mailingRecipient );
              if ( file != null )
              {
                String absPath = file.getAbsolutePath();
                String fileName = file.getName();
                mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
              }
              mailingService.submitMailing( mailing, null );
              sampleEmailSent = true;
            }
          }
        }
      }
      catch( Exception e )
      {
        String message = "An exception occurred while sending Throwdown promotion launch email to eligible audiences: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")";
        log.error( message, e );
        addComment( message );
      }
    }

    return mailingRecipientCount;
  }

  protected int sendThrowdownPromoManagerLaunchMessage( List<Match> matchList, ThrowdownPromotion promotion )
  {
    log.debug( "sending throwdown launch message" );
    int mailingRecipientCount = 0;

    Participant participant = null;
    List<Participant> allUsers = new ArrayList<Participant>();

    Map<User, Set<Participant>> managers = new HashMap<User, Set<Participant>>();
    managers = participantService.getManagerForCompetitorAudience( promotion.getId() );

    if ( managers != null )
    {
      for ( Iterator<User> iter = managers.keySet().iterator(); iter.hasNext(); )
      {
        User manager = getParticipant( iter.next() );
        if ( manager == null || !manager.isActive().booleanValue() )
        {
          continue;
        }
        participant = (Participant)manager;
        allUsers.add( participant );
        ++mailingRecipientCount;
      }
    }

    boolean sampleEmailSent = false;
    File file = null;

    if ( managers != null )
    {
      for ( Iterator<User> iter = managers.keySet().iterator(); iter.hasNext(); )
      {
        User manager = getParticipant( iter.next() );
        if ( manager == null || !manager.isActive().booleanValue() || manager.isWelcomeEmailSent().booleanValue() )
        {
          continue;
        }
        try
        {
          if ( !isNotice() || isNotice() && !sampleEmailSent )
          {
            long messageId = getMessageId( MessageService.THROWDOWN_MANAGER_PROMO_LAUNCH_MESSAGE_CM_ASSET_CODE );
            Mailing mailing = composeMail( messageId, MailingType.PROMOTION );
            MailingRecipient mailingRecipient = getMailingRecipient( (Participant)manager );
            if ( isNotice() && !sampleEmailSent )
            {
              file = extractFile( allUsers, BEAN_NAME );
            }
            Map promoObjectMap = new HashMap();
            setPersonalizationData( promoObjectMap, promotion, (Participant)manager );
            mailingRecipient.addMailingRecipientDataFromMap( promoObjectMap );
            // Add pax personalization data to the promoObjectMap
            Map paxObjectMap = new HashMap();
            setPersonalizationDataForTD( paxObjectMap, promotion, null, (Participant)manager );
            // Set the recipients on the mailing
            mailing.addMailingRecipient( mailingRecipient );
            if ( file != null )
            {
              String absPath = file.getAbsolutePath();
              String fileName = file.getName();
              mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
            }
            mailingService.submitMailing( mailing, paxObjectMap );
            sampleEmailSent = true;
          }
        }
        catch( Exception e )
        {
          String message = "An exception occurred while sending Throwdown Promotion Launch email to a Manager: " + promotion.getName() + ". See the log file for additional information.  "
              + "(process invocation ID = " + getProcessInvocationId() + ")";
          log.error( message, e );
          addComment( message );
        }
      }
    }
    return mailingRecipientCount;
  }

  protected void setPersonalizationDataForTD( Map objectMap, ThrowdownPromotion promotion, Match match, User participant )
  {
    String locale = participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode();
    objectMap.put( "firstName", participant.getFirstName() );
    objectMap.put( "lastName", participant.getLastName() );
    objectMap.put( "objective", promotion.getOverviewDetailsText() );
    objectMap.put( "ofDaysPerRound", String.valueOf( promotion.getLengthOfRound() ) );
    if ( promotion.getBaseUnit() != null )
    {
      objectMap.put( "unitLabel", promotion.getBaseUnitText() );
    }
    if ( promotion.isSmackTalkAvailable() )
    {
      objectMap.put( "smackTalk", String.valueOf( Boolean.TRUE ) );
    }
    if ( match != null )
    {
      for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
      {
        if ( matchTeamOutcome.getTeam().isShadowPlayer() )
        {
          objectMap.put( "opponentName", promotion.getTeamUnavailableResolverType().getName() );
          break;
        }
        else if ( !participant.getId().equals( matchTeamOutcome.getTeam().getParticipant().getId() ) )
        {
          objectMap.put( "opponentName", matchTeamOutcome.getTeam().getParticipant().getNameFLNoComma() );
          break;
        }
      }
      if ( match.getRound() != null )
      {
        objectMap.put( "roundNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( match.getRound().getRoundNumber() ), LocaleUtils.getLocale( locale ) ) );
        objectMap.put( "roundStartDate", com.biperf.core.utils.DateUtils.toDisplayString( match.getRound().getStartDate(), LocaleUtils.getLocale( locale ) ) );
        objectMap.put( "roundEndDate", com.biperf.core.utils.DateUtils.toDisplayString( match.getRound().getEndDate(), LocaleUtils.getLocale( locale ) ) );
      }
    }
  }

  protected void setPayoutInformationForTD( Map objectMap, ThrowdownPromotion promotion, Match match, User participant )
  {
    Set<DivisionPayout> payouts = new HashSet<DivisionPayout>();
    BigDecimal minimumQualifier = null;
    Division userDiv = teamService.getDivisionForUser( promotion.getId(), participant.getId(), match.getRound().getRoundNumber() );
    for ( Division division : promotion.getDivisions() )
    {
      if ( division.equals( userDiv ) )
      {
        payouts = division.getPayouts();
        minimumQualifier = division.getMinimumQualifier();
        break;
      }
    }
    for ( DivisionPayout payout : payouts )
    {
      if ( payout.getOutcome().getCode().equals( MatchTeamOutcomeType.WIN ) )
      {
        objectMap.put( "winAwards", String.valueOf( Boolean.TRUE ) );
        objectMap.put( "winAward", String.valueOf( payout.getPoints() ) );
      }
      if ( payout.getOutcome().getCode().equals( MatchTeamOutcomeType.LOSS ) )
      {
        objectMap.put( "loseAwards", String.valueOf( Boolean.TRUE ) );
        objectMap.put( "loseAward", String.valueOf( payout.getPoints() ) );
      }
      if ( payout.getOutcome().getCode().equals( MatchTeamOutcomeType.TIE ) )
      {
        objectMap.put( "tieAwards", String.valueOf( Boolean.TRUE ) );
        objectMap.put( "tieAward", String.valueOf( payout.getPoints() ) );
      }
    }
    if ( promotion.getTeamUnavailableResolverType().getCode().equals( TeamUnavailableResolverType.MINIMUM_QUALIFIER ) )
    {
      objectMap.put( "isMinQualifier", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "minQualifier", String.valueOf( minimumQualifier ) );
    }
  }

  protected void setRoundInformationForTD( Map objectMap, ThrowdownPromotion promotion, Round round, User participant )
  {
    String locale = participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode();
    objectMap.put( "firstName", participant.getFirstName() );
    objectMap.put( "lastName", participant.getLastName() );

    objectMap.put( "roundNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( round.getRoundNumber() ), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "roundStartDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getStartDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "roundEndDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getEndDate(), LocaleUtils.getLocale( locale ) ) );
  }

  protected void setPersonalizationData( Map objectMap, ThrowdownPromotion promotion, Participant pax )
  {
    String locale = pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      objectMap.put( "programName", getPromotionName( promotion.getPromoNameAssetCode(), locale ) );
      objectMap.put( "promotionName", getPromotionName( promotion.getPromoNameAssetCode(), locale ) );
    }
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    objectMap.put( "siteLink", siteLink );
    objectMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );

    if ( isNotice() )
    {
      objectMap.put( "notice", "Notice : " );
    }
    else
    {
      objectMap.put( "notice", "" );
    }

    objectMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(), LocaleUtils.getLocale( locale ) ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotEndDate, LocaleUtils.getLocale( locale ) ) );
    }
    if ( promotion.isSmackTalkAvailable() )
    {
      objectMap.put( "smackTalk", String.valueOf( Boolean.TRUE ) );
    }
    objectMap.put( "numberOfRounds", String.valueOf( promotion.getNumberOfRounds() ) );
    objectMap.put( "ofDaysPerRound", String.valueOf( promotion.getLengthOfRound() ) );
    objectMap.put( "objective", promotion.getOverviewDetailsText() );

  }

  protected int sendThrowdownNextRoundMessage( long messageId, ThrowdownPromotion promotion, PromotionNotificationType promotionNotificationType )
  {
    log.debug( "sending throwdown next round message" );
    int mailingNextRoundRecipientCount = 0;
    Division division = promotion.getDivisions().iterator().next();
    Round currentRound = getRoundDAO().getCurrentRound( promotion.getId(), division.getId() );
    if ( currentRound != null )
    {
      int nextRoundNumber = currentRound.getRoundNumber() + 1;
      if ( nextRoundNumber <= promotion.getNumberOfRounds() )
      {
        Round nextRound = getRoundDAO().getRoundsForPromotionByDivisionAndRoundNumber( promotion.getId(), division.getId(), nextRoundNumber );
        Date nextRoundStartDate = nextRound.getStartDate();
        Date nextRoundNotificationDate = new Date( nextRoundStartDate.getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
        Date now = Calendar.getInstance().getTime();

        // Current Date should be same as notification Date
        if ( DateUtils.isSameDay( now, nextRoundNotificationDate ) )
        {
          List<Match> matchList = getMatchDAO().getMatchesByPromotionAndRoundNumber( promotion.getId(), nextRound.getRoundNumber() );
          if ( matchList != null && !matchList.isEmpty() )
          {
            for ( Iterator<Match> matchIterator = matchList.iterator(); matchIterator.hasNext(); )
            {
              try
              {
                Match match = (Match)matchIterator.next();
                for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
                {
                  if ( !matchTeamOutcome.getTeam().isShadowPlayer() )
                  {
                    Participant pax = matchTeamOutcome.getTeam().getParticipant();
                    MailingRecipient mailingRecipient = getMailingRecipient( pax );
                    // Compose the mailing
                    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

                    Map promoObjectMap = new HashMap();
                    setPersonalizationData( promoObjectMap, promotion, pax );
                    mailingRecipient.addMailingRecipientDataFromMap( promoObjectMap );
                    // Add pax personalization data to the promoObjectMap
                    Map paxObjectMap = new HashMap();
                    setPersonalizationDataForTD( paxObjectMap, promotion, match, pax );
                    // Set the recipients on the mailing
                    mailing.addMailingRecipient( mailingRecipient );
                    mailingService.submitMailing( mailing, paxObjectMap );
                    ++mailingNextRoundRecipientCount;
                  }
                }
              }
              catch( Exception e )
              {
                String message = "An exception occurred while sending Throwdown next round for promotion: " + promotion.getName() + ". See the log file for additional information.  "
                    + "(process invocation ID = " + getProcessInvocationId() + ")";
                log.error( message, e );
                addComment( message );
              }
            }

            // sending match announcement email to eligible audience managers.
            sendNextRoundMailToPaxManager( messageId, promotion, nextRound );
          }
        }
      }
    }
    else
    {
      // adding this logic to send match announcement email for eligible audience managers for first
      // round too.
      Round appropriateRound = getTeamService().getRound( promotion.getId(), division.getId(), 1 );
      Date firstRoundNotificationDate = new Date( appropriateRound.getStartDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
      Date now = Calendar.getInstance().getTime();
      if ( DateUtils.isSameDay( now, firstRoundNotificationDate ) )
      {
        sendNextRoundMailToPaxManager( messageId, promotion, appropriateRound );
      }

    }

    return mailingNextRoundRecipientCount;
  }

  protected void sendNextRoundMailToPaxManager( Long messageId, ThrowdownPromotion promotion, Round nextRound )
  {
    Map<User, Set<Participant>> managers = new HashMap<User, Set<Participant>>();
    managers = participantService.getManagerForCompetitorAudience( promotion.getId() );
    if ( managers != null )
    {
      for ( Iterator<User> iter = managers.keySet().iterator(); iter.hasNext(); )
      {
        User manager = iter.next();
        Set<Participant> participants = managers.get( manager );
        try
        {
          Mailing mailing = new Mailing();
          Message message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MANAGER_MATCH_ANNOUNCEMENT_MESSAGE_CM_ASSET_CODE );
          StringBuilder sb = new StringBuilder();
          sb.append( "<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">" );
          sb.append( "<tr><td><b>" + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.NAME" ) + "</b></td><td><b>"
              + CmsResourceBundle.getCmsBundle().getString( "participant.throwdownstats.OPPONENT" ) + "</b></td></tr>" );
          MailingRecipient mailingRecipient = getMailingRecipient( manager );
          Map promoObjectMap = new HashMap();
          Set<Long> opponentIds = new HashSet<Long>();
          for ( Iterator<Participant> iterator = participants.iterator(); iterator.hasNext(); )
          {
            Participant user = iterator.next();
            if ( opponentIds.contains( user.getId() ) )
            {
              continue;
            }
            Match match = teamService.getMatchByPromotionAndRoundNumberAndTeam( promotion.getId(), user.getId(), nextRound.getRoundNumber() );
            String opponentName = null;
            if ( match != null )
            {
              for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
              {
                if ( matchTeamOutcome.getTeam().isShadowPlayer() )
                {
                  opponentName = promotion.getTeamUnavailableResolverType().getName();
                }
                else if ( !user.getId().equals( matchTeamOutcome.getTeam().getParticipant().getId() ) )
                {
                  opponentName = matchTeamOutcome.getTeam().getParticipant().getNameFLNoComma();
                  opponentIds.add( matchTeamOutcome.getTeam().getParticipant().getId() );
                }
              }
            }
            sb.append( "<tr><td>" + user.getNameFLNoComma() + "</td><td>" + opponentName + "</td></tr>" );
          }
          sb.append( "</table>" );
          promoObjectMap.put( "teamProgressTable", sb.toString() );
          setPersonalizationData( promoObjectMap, promotion, (Participant)manager );
          mailingRecipient.addMailingRecipientDataFromMap( promoObjectMap );
          // Add pax personalization data to the promoObjectMap
          Map paxObjectMap = new HashMap();
          setRoundInformationForTD( paxObjectMap, promotion, nextRound, manager );
          // Set the recipients on the mailing
          mailing.addMailingRecipient( mailingRecipient );

          mailing.setMessage( message );
          mailing.setDeliveryDate( new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDateAsLong() ) );
          mailing.setSender( "Throwdown Promotion" );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setGuid( GuidUtils.generateGuid() );

          mailingService.submitMailing( mailing, paxObjectMap );
        }
        catch( Exception e )
        {
          String message = "An exception occurred while sending Throwdown Match announcement to Manager: " + promotion.getName() + ". See the log file for additional information.  "
              + "(process invocation ID = " + getProcessInvocationId() + ")";
          log.error( message, e );
          addComment( message );
        }
      }
    }
  }

  protected void setPersonalizationData( Map objectMap, ChallengePointPromotion promotion, User participant ) throws ServiceErrorException
  {
    objectMap.put( "firstName", participant.getFirstName() );
    objectMap.put( "lastName", participant.getLastName() );
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );
    List<ChallengepointPaxValueBean> paxBeanList = challengePointService.getParticipantChallengePointLevelBeans( promotion, paxGoal, false );
    log.debug( "Number of selection levels for promotion--->" + promotion.getId() + " are-->" + paxBeanList.size() );
    objectMap.put( "points", String.valueOf( Boolean.FALSE ) );
    objectMap.put( "isBase", String.valueOf( Boolean.FALSE ) );
    objectMap.put( "challengepoint1", String.valueOf( Boolean.FALSE ) );
    objectMap.put( "challengepoint2", String.valueOf( Boolean.FALSE ) );
    objectMap.put( "challengepoint3", String.valueOf( Boolean.FALSE ) );
    if ( promotion.getAwardType().isPointsAwardType() )
    {
      objectMap.put( "points", String.valueOf( Boolean.TRUE ) );
    }

    if ( paxBeanList != null )
    {
      ChallengepointPaxValueBean paxBaseValueBean = (ChallengepointPaxValueBean)paxBeanList.get( 0 );

      if ( paxBaseValueBean != null && paxBaseValueBean.getBaseAmount() != null )
      {
        objectMap.put( "isBase", String.valueOf( Boolean.TRUE ) );
        String baseAmount = NumberFormatUtil.getLocaleBasedBigDecimalFormat( paxBaseValueBean.getBaseAmount(),
                                                                             promotion.getAchievementPrecision().getPrecision(),
                                                                             LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                 ? UserManager.getDefaultLocale().toString()
                                                                                 : participant.getLanguageType().getCode() ) );
        objectMap.put( "base", baseAmount );
      }
      int n = 1;
      for ( Iterator iter = paxBeanList.iterator(); iter.hasNext(); )
      {
        ChallengepointPaxValueBean paxValueBean = (ChallengepointPaxValueBean)iter.next();

        // BugFix 19888.Populate Goal Level Description instead of GoalLevel name.
        String cpLevelName = paxValueBean.getGoalLevel().getGoalLevelName();
        BigDecimal cpAchieveAmt = paxValueBean.getAmountToAchieve();

        BigDecimal cpAwardAmt = paxValueBean.getGoalLevel().getAward();
        if ( cpAchieveAmt == null )
        {
          cpAchieveAmt = new BigDecimal( 0 );
        }
        if ( cpAwardAmt == null )
        {
          cpAwardAmt = new BigDecimal( 0 );
        }
        log.debug( "goal level -->" + paxValueBean.getGoalLevel().getGoalLevelName() + "achievement amount--->" + cpAchieveAmt + " awardAmount-->" + cpAwardAmt );
        if ( cpLevelName != null && !"".equals( cpLevelName ) && cpAchieveAmt != null && cpAchieveAmt.longValue() > 0 && cpAwardAmt != null && cpAwardAmt.longValue() > 0 )
        {
          objectMap.put( "challengepoint" + n, String.valueOf( Boolean.TRUE ) );
          objectMap.put( "challengepointLevel" + n, cpLevelName );
          String achieveAmt = NumberFormatUtil
              .getLocaleBasedBigDecimalFormat( cpAchieveAmt,
                                               promotion.getAchievementPrecision().getPrecision(),
                                               LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
          objectMap.put( "challengepointValue" + n, formatBasevalue( achieveAmt, promotion ) );
          String cpAwardAmount = NumberFormatUtil
              .getLocaleBasedBigDecimalFormat( cpAwardAmt,
                                               0,
                                               LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
          objectMap.put( "challengepointReward" + n, cpAwardAmount );
          n++;
        }
      }
    }
  }

  protected void setPersonalizationDataWithLocale( Map objectMap, GoalQuestPromotion promotion, User participant )
  {
    String locale = participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      objectMap.put( "programName", getPromotionName( promotion.getPromoNameAssetCode(), locale ) );
    }
    objectMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(), LocaleUtils.getLocale( locale ) ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotEndDate, LocaleUtils.getLocale( locale ) ) );
    }
  }

  protected void setPersonalizationData( Map objectMap, GoalQuestPromotion promotion, User participant )
  {
    String locale = participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode();
    objectMap.put( "firstName", participant.getFirstName() );
    // BugFix 21054,pre poluate lastName as some templates might use this values in future.
    objectMap.put( "lastName", participant.getLastName() );
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );
    String paxBase = getPaxBase( promotion, paxGoal, participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    if ( paxBase != null )
    {
      objectMap.put( "isBase", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "base", paxBase );
    }
    Collection goalLevels = promotion.getGoalLevels();
    if ( goalLevels != null )
    {
      int n = 1;
      for ( Iterator iter = goalLevels.iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        paxGoal = SelectGoalUtil.getLevelSpecificGoal( paxGoal, promotion, goalLevel );
        GoalLevelValueBean goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( paxGoal, promotion, goalLevel );
        // BugFix 19888.Populate Goal Level Description instead of GoalLevel name.
        String goalLevelName = goalLevel.getGoalLevelDescription();
        BigDecimal goalAchieveAmt = goalLevelValueBean.getCalculatedGoalAmount();
        BigDecimal goalAwardAmt = goalLevel.getAward();
        if ( goalLevelName != null && !"".equals( goalLevelName ) && goalAchieveAmt != null && goalAchieveAmt.longValue() > 0 && goalAwardAmt != null && goalAwardAmt.longValue() > 0 )
        {
          objectMap.put( "goal" + n, String.valueOf( Boolean.TRUE ) );
          objectMap.put( "goalLevel" + n, goalLevelName );
          String tempGoalAchieveAmt = goalAchieveAmt.toString();
          if ( goalAchieveAmt != null )
          {
            tempGoalAchieveAmt = NumberFormatUtil
                .getLocaleBasedBigDecimalFormat( goalAchieveAmt,
                                                 promotion.getAchievementPrecision().getPrecision(),
                                                 LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
          }
          objectMap.put( "goalValue" + n, formatBasevalue( tempGoalAchieveAmt, promotion ) );
          String tempGoalAwardAmt = goalAwardAmt.toString();
          if ( goalAchieveAmt != null )
          {
            tempGoalAwardAmt = NumberFormatUtil.getLocaleBasedNumberFormat( goalAwardAmt.longValue() );
          }
          objectMap.put( "goalReward" + n, tempGoalAwardAmt );
          n++;
        }
      }
    }
  }

  protected String getPaxBase( GoalQuestPromotion promotion, PaxGoal paxGoal, String locale )
  {
    String paxBase = null;
    int roundingMode = 0;

    if ( !Objects.isNull( promotion ) && !Objects.isNull( promotion.getRoundingMethod() ) )
    {
      roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    }

    if ( null != paxGoal )
    {
      BigDecimal baseQty = paxGoal.getBaseQuantity();
      if ( null != baseQty && baseQty.floatValue() != 0 )
      {
        String baseQuantity = NumberFormatUtil.getLocaleBasedRoundingBigDecimalFormat( baseQty,
                                                                                       promotion.getAchievementPrecision().getPrecision(),
                                                                                       roundingMode,
                                                                                       LocaleUtils.getLocale( locale ),
                                                                                       promotion.getAchievementPrecision().getPrecision() );
        paxBase = formatBasevalue( baseQuantity, promotion );
      }
    }

    return paxBase;
  }

  protected String formatBasevalue( String base, ChallengePointPromotion promotion )
  {
    StringBuffer formatBase = new StringBuffer();
    // String NBSP = "&nbsp;";

    String baseUnit = promotion.getBaseUnitText();
    if ( null != baseUnit && baseUnit.trim().length() > 0 )
    {
      String baseUnitPositionStr = promotion.getBaseUnitPosition().getCode();
      // Adding space between unit and base value
      if ( BaseUnitPosition.UNIT_BEFORE.equals( baseUnitPositionStr ) )
      {
        formatBase.append( "\\ " );
        formatBase.append( baseUnit );
        formatBase.append( base );
      }
      else if ( BaseUnitPosition.UNIT_AFTER.equals( baseUnitPositionStr ) )
      {
        formatBase.append( base );
        formatBase.append( "\\ " );
        formatBase.append( baseUnit );
      }
    }

    return formatBase.toString().length() > 0 ? formatBase.toString() : base;
  }

  protected String formatBasevalue( String base, GoalQuestPromotion promotion )
  {
    StringBuffer formatBase = new StringBuffer();
    // String NBSP = "&nbsp;";

    String baseUnit = promotion.getBaseUnitText();

    if ( null != baseUnit && baseUnit.trim().length() > 0 )
    {
      baseUnit = baseUnit.replace( "&nbsp;", " " );
      BaseUnitPosition baseUnitPosition = promotion.getBaseUnitPosition();
      String baseUnitPositionStr = baseUnitPosition != null ? baseUnitPosition.getCode() : null;

      if ( BaseUnitPosition.UNIT_BEFORE.equals( baseUnitPositionStr ) )
      {
        formatBase.append( baseUnit );
        formatBase.append( " " );
        formatBase.append( base );
      }
      else if ( BaseUnitPosition.UNIT_AFTER.equals( baseUnitPositionStr ) )
      {
        formatBase.append( base );
        formatBase.append( " " );
        formatBase.append( baseUnit );
      }
    }

    return formatBase.toString().length() > 0 ? formatBase.toString() : base;
  }

  protected void setPersonalizationData( Map objectMap, GoalQuestPromotion promotion )
  {
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    objectMap.put( "siteLink", siteLink );
    objectMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    if ( isNotice() )
    {
      objectMap.put( "notice", "Notice : " );
    }
    else
    {
      objectMap.put( "notice", "" );
    }

    if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
    {
      objectMap.put( "points", String.valueOf( Boolean.TRUE ) );
    }
  }

  protected void setPersonalizationDataForCP( Map objectMap, ChallengePointPromotion promotion, Participant pax )
  {
    String locale = pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      objectMap.put( "programName", getPromotionName( promotion.getPromoNameAssetCode(), locale ) );
    }
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    objectMap.put( "siteLink", siteLink );
    objectMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );

    if ( isNotice() )
    {
      objectMap.put( "notice", "Notice : " );
    }
    else
    {
      objectMap.put( "notice", "" );
    }

    if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
    {
      objectMap.put( "points", String.valueOf( Boolean.TRUE ) );
    }
    objectMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(), LocaleUtils.getLocale( locale ) ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotEndDate, LocaleUtils.getLocale( locale ) ) );
    }
  }

  /**
   * This method takes each promotion and sends an email to the appropriate audience based on the
   * promotion type who have been inactive in X number of days which is set up in the Promotion
   * Notifications for Program Inactivity
   * 
   * @param messageId - the message selected to be used in Message Library for this inactivity alert
   * @param promotion - the promotion for which this inactivity alert is sent for
   * @param pastDate - the number of days the pax has been inactive prior to today's date
   * @param now - today's date i.e. the process run date
   * @return int - the number of recipients for the email alert
   */
  protected int participantInactivity( long messageId, Promotion promotion, Date pastDate, Date now, boolean sendNotification )
  {
    Set paxs = null;
    if ( promotion instanceof RecognitionPromotion )
    {
      paxs = participantService.getAllPaxWhoHaveNotGivenRecognition( promotion.getId(), pastDate, now );
    }
    else if ( promotion instanceof NominationPromotion )
    {
      paxs = participantService.getAllPaxWhoHaveNotNominated( promotion.getId(), pastDate, now );
    }
    else if ( promotion instanceof ProductClaimPromotion )
    {
      paxs = participantService.getAllPaxWhoHaveNotSubmittedClaim( promotion.getId(), pastDate, now );
    }
    else if ( promotion instanceof QuizPromotion )
    {
      paxs = participantService.getAllPaxWhoHaveNotTakenQuiz( promotion.getId(), pastDate, now );
    }
    else if ( promotion instanceof SurveyPromotion )
    {
      paxs = participantService.getAllPaxWhoHaveNotTakenSurvey( promotion.getId(), pastDate, now );
    }
    // To fix the bug 20643
    if ( paxs != null && paxs.size() > 0 )
    {
      if ( sendNotification )
      {
        Set mailingRecipients = createMailingRecipients( paxs );
        // Send Program Inactivity Email to paxs
        sendInactivityMessage( mailingRecipients, messageId, promotion );
        return mailingRecipients.size();
      }
      else
      {
        return paxs.size();
      }
    }
    return 0;
  }

  protected int sendRedemptionNotification( Promotion promotion, PromotionNotificationType promoNotificationType, boolean sendNotification ) throws ServiceErrorException
  {
    String descriminator = promoNotificationType.getDescriminator();
    MerchOrderActivityQueryConstraint constraint = new MerchOrderActivityQueryConstraint();
    constraint.setPromotionId( promotion.getId() );
    constraint.setRedeemed( Boolean.FALSE );

    List unredeemedGiftCodes = new ArrayList();
    Date now = Calendar.getInstance().getTime();

    if ( descriminator != null && descriminator.equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE ) )
    {
      List checkedCodes = merchOrderService.getUnredeemedMerchOrdersAndUpdateStatus( constraint );
      now = configureDate( now );

      // now iterate though to make sure each one has hit the issuance date + number of days
      for ( int i = 0; i < checkedCodes.size(); i++ )
      {
        MerchOrder code = (MerchOrder)checkedCodes.get( i );
        String codeStatus = code.getOrderStatus();
        if ( StringUtil.isNullOrEmpty( codeStatus ) || !"expired".equals( codeStatus ) )
        {
          Date issuedDate = code.getAuditCreateInfo().getDateCreated();
          issuedDate = configureDate( issuedDate );
          long numberOfDaysSinceIssuance = ( now.getTime() - issuedDate.getTime() ) / DateUtils.MILLIS_PER_DAY;
          // TODO: change this to allow the database to perform the modulus function
          if ( numberOfDaysSinceIssuance != 0 && numberOfDaysSinceIssuance >= promoNotificationType.getNumberOfDays().longValue() )
          {
            unredeemedGiftCodes.add( code );
          }
        }
      }
    }
    else if ( descriminator != null && descriminator.equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_PROMO_END ) )
    {
      Date promoEndNotificationDate = new Date( promotion.getSubmissionEndDate().getTime() + promoNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );

      if ( promotion.isLive() && DateUtils.isSameDay( now, promoEndNotificationDate ) )
      {
        unredeemedGiftCodes = merchOrderService.getUnredeemedMerchOrdersAndUpdateStatus( constraint );
      }
    }

    if ( unredeemedGiftCodes.size() > 0 )
    {
      if ( sendNotification )
      {
        Set mailingGiftCodeRecipients = createGiftCodeMailingRecipients( unredeemedGiftCodes );
        // Send Redemption reminder
        sendRedemptionReminderMessage( mailingGiftCodeRecipients, promoNotificationType.getNotificationMessageId(), promotion );
        return mailingGiftCodeRecipients.size();
      }
      else
      {
        return unredeemedGiftCodes.size();
      }
    }
    return 0;
  }

  protected int sendGoalQuestRedemptionNotification( Promotion promotion, PromotionNotificationType promoNotificationType ) throws ServiceErrorException
  {
    String descriminator = promoNotificationType.getDescriminator();
    MerchOrderActivityQueryConstraint constraint = new MerchOrderActivityQueryConstraint();
    constraint.setGqCpPromotionId( promotion.getId() );
    constraint.setRedeemed( Boolean.FALSE );

    List unredeemedGiftCodes = new ArrayList();
    Date now = Calendar.getInstance().getTime();

    if ( descriminator != null && descriminator.equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_ISSUANCE ) )
    {
      List checkedCodes = merchOrderService.getUnredeemedMerchOrdersAndUpdateStatus( constraint );
      now = configureDate( now );

      // now iterate though to make sure each one has hit the issuance date + number of days
      for ( int i = 0; i < checkedCodes.size(); i++ )
      {
        MerchOrder code = (MerchOrder)checkedCodes.get( i );

        Date issuedDate = code.getAuditCreateInfo().getDateCreated();
        long numberOfDaysSinceIssuance = ( now.getTime() - issuedDate.getTime() ) / DateUtils.MILLIS_PER_DAY;
        // TODO: change this to allow the database to perform the modulus function

        if ( numberOfDaysSinceIssuance != 0 && 0 == numberOfDaysSinceIssuance % promoNotificationType.getNumberOfDays().longValue() )
        {
          unredeemedGiftCodes.add( code );
        }
      }
    }
    else if ( descriminator != null && descriminator.equals( PromotionEmailNotificationType.NON_REDEMP_DESCRIMINATOR_PROMO_END ) )
    {
      Date promoEndNotificationDate = new Date( promotion.getSubmissionEndDate().getTime() + promoNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );

      if ( promotion.isLive() && DateUtils.isSameDay( now, promoEndNotificationDate ) )
      {
        unredeemedGiftCodes = merchOrderService.getUnredeemedMerchOrdersAndUpdateStatus( constraint );
      }
    }
    if ( unredeemedGiftCodes.size() > 0 )
    {
      Set mailingGiftCodeRecipients = createGoalQuestGiftCodeMailingRecipients( unredeemedGiftCodes );
      // Send Redemption reminder
      sendGoalQuestRedemptionReminderMessage( mailingGiftCodeRecipients, promoNotificationType.getNotificationMessageId(), promotion );
      totalGqCpRedemptionCount += mailingGiftCodeRecipients.size();
      return mailingGiftCodeRecipients.size();
    }
    return 0;
  }

  protected Date configureDate( java.util.Date date )
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime( date );

    return configureCalendar( cal ).getTime();
  }

  protected Calendar configureCalendar( Calendar date )
  {
    date.set( Calendar.HOUR_OF_DAY, 0 );
    date.set( Calendar.MINUTE, 0 );
    date.set( Calendar.SECOND, 0 );
    date.set( Calendar.MILLISECOND, 0 );

    return date;
  }

  /**
   * This method takes each promotion and sends an email to the appropriate audience based on the
   * promotion type who have not selected a goal
   * 
   * @param messageId - the message selected to be used in Message Library for this inactivity alert
   * @param promotion - the promotion for which this inactivity alert is sent for
   * @return int - the number of recipients for the email alert
   */
  protected int goalNotSelected( long messageId, Promotion promotion )
  {
    if ( promotion.isGoalQuestPromotion() )
    {
      GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;

      Set paxs = null;
      paxs = participantService.getAllPaxWhoHaveNotSelectedGoal( promotion.getId() );
      // BugFix 17942 do not proceed further if there is no recepient to receive this mail
      if ( paxs != null && paxs.size() > 0 )
      {
        for ( Iterator paxIterator = paxs.iterator(); paxIterator.hasNext(); )
        {
          Participant pax = (Participant)paxIterator.next();
          // Bug Fix 19164. Avoid notifying Inactive Pax.
          if ( !pax.isActive().booleanValue() )
          {
            paxIterator.remove();
            continue;
          }
          // If manager override type is not manager selects team achievement then
          // manager does not need a goal

          // Removed as per the recommendation for bug 59342
          /*
           * if ( !goalQuestPromotion.getOverrideStructure().getCode().equals(
           * ManagerOverrideStructure.OVERRIDE_PERCENT ) ) { if ( pax.isOwner() ) {
           * paxIterator.remove(); } }
           */
        }
        // BugFix 17942 do not proceed further if there is no recepient to receive this mail
        if ( paxs.size() > 0 )
        {
          Set mailingRecipients = createMailingRecipients( paxs );
          // Send Goal Not Selected Email to paxs
          sendGoalNotSelectedMessage( mailingRecipients, messageId, promotion );
          return mailingRecipients.size();
        }
      }
    }
    return 0;
  }

  /**
   * This method takes each promotion and sends an email to the appropriate audience based on the
   * promotion type who have not selected a goal
   * 
   * @param messageId - the message selected to be used in Message Library for this inactivity alert
   * @param promotion - the promotion for which this inactivity alert is sent for
   * @return int - the number of recipients for the email alert
   */
  protected int challengepointNotSelected( long messageId, Promotion promotion )
  {
    if ( promotion.isChallengePointPromotion() )
    {
      ChallengePointPromotion challengepointPromotion = (ChallengePointPromotion)promotion;

      Set paxs = null;
      paxs = participantService.getAllPaxWhoHaveNotSelectedChallengepoint( promotion.getId() );
      // BugFix 17942 do not proceed further if there is no recepient to receive this mail
      if ( paxs != null && paxs.size() > 0 )
      {
        for ( Iterator paxIterator = paxs.iterator(); paxIterator.hasNext(); )
        {
          Participant pax = (Participant)paxIterator.next();
          // Bug Fix 19164. Avoid notifying Inactive Pax.
          if ( !pax.isActive().booleanValue() )
          {
            paxIterator.remove();
            continue;
          }
          // If manager override type is not manager selects team achievement then
          // manager does not need a goal
          if ( !challengepointPromotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
          {
            if ( pax.isOwner() )
            {
              paxIterator.remove();
              continue;
            }
          }
          if ( pax.isManager() && !pax.isOwner() )
          {
            paxIterator.remove();
            continue;
          }
        }
        // BugFix 17942 do not proceed further if there is no recepient to receive this mail
        if ( paxs.size() > 0 )
        {
          Set mailingRecipients = createMailingRecipients( paxs );
          // Send Goal Not Selected Email to paxs
          sendChallengepointNotSelectedMessage( mailingRecipients, messageId, promotion );
          return mailingRecipients.size();
        }
      }
    }
    return 0;
  }

  /**
   * This method takes each promotion and sends an email to the appropriate approvers based on the
   * promotion type who still have pending claims after X number of days have passed claim
   * submission, which is set up in the Promotion Notifications for Approver Reminder. Each approver
   * should get 1 email for all the overdue approvals for one promotion.
   * 
   * @param messageId - the message selected to be used in Message Library for this approver
   *          reminder
   * @param promotion - the promotion for which this approver reminder is sent for
   * @param promotionNotificationType - how frequently to remind the approver
   * @return int - the number of recipients for the email alert
   */
  protected int approverReminder( long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean individualLaunch, boolean sendNotification )
  {
    Set paxs = new HashSet();

    // Get a set of distinct approvers eligible to approve claims for this promotion
    Set approvers = this.getDistinctApproversByPromotion( promotion );

    // Iterate thru approvers
    for ( Iterator iter = approvers.iterator(); iter.hasNext(); )
    {
      Object temp = iter.next();
      Participant approver = null;

      if ( temp instanceof Participant )
      {
        approver = (Participant)temp;
      }
      else if ( temp instanceof User )
      {
        approver = participantService.getParticipantById( ( (User)temp ).getId() );
      }
      else if ( temp instanceof PromotionApprovalParticipant )
      {
        approver = ( (PromotionApprovalParticipant)temp ).getParticipant();
      }
      else if ( temp instanceof FormattedValueBean )
      {
        approver = participantService.getParticipantById( ( (FormattedValueBean)temp ).getId() );
      }

      if ( approver != null )
      {
        // If the approver has pending claims,
        if ( getPendingClaimsCount( approver.getId(), promotion.getPromotionType(), promotion.getId(), promotion.getApproverType() ) > 0 )
        {
          // If now is the time to send the reminder to the approver
          if ( isTimeToRemind( approver.getId(), promotion, promotionNotificationType, individualLaunch ) )
          {
            // then add to the set, approver should get one email per promotion
            paxs.add( approver );
          }
        }
      }
    } // end Iterate thru approvers

    // Build mailing recipients and send the approver reminder email
    if ( paxs != null && paxs.size() > 0 )
    {
      if ( sendNotification )
      {
        Set mailingRecipients = createMailingRecipients( paxs );
        // Send Approver Reminder Email to Approvers
        // TODO need to pass if the approver is getting mail as defaultApprover or normal approver.
        // Setting as false for testing purpose
        sendApproverReminderMessage( mailingRecipients, messageId, promotion, false );
        return mailingRecipients.size();
      }
      else
      {
        return paxs.size();
      }
    }

    return 0;
  }

  protected int purlManagerNonResponse( long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean sendNotification )
  {
    int purlManagerNonResponseCount = 0;
    // Get all pending purl invitation for this promotion
    List pendingPurlRecipients = purlService.getAllPendingPurlInvitations( promotion.getId() );

    if ( pendingPurlRecipients != null && pendingPurlRecipients.size() > 0 )
    {
      if ( sendNotification )
      {
        purlManagerNonResponseCount = sendPurlManagerNonResponseMessage( pendingPurlRecipients, messageId, promotion, promotionNotificationType );
      }
      else
      {
        return pendingPurlRecipients.size();
      }
    }
    return purlManagerNonResponseCount;
  }

  protected int purlContributorNonResponse( long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean sendNotification )
  {
    int purlContributorNonResponseCount = 0;
    Set contributors = new HashSet();
    Date now = new Date();
    Participant contributor = null;

    // Get all pending purl invitation for this promotion
    List pendingContributors = purlService.getAllPendingPurlContributionsProActive( promotion.getId(), new Long( promotionNotificationType.getNumberOfDays() ) );

    if ( pendingContributors != null && pendingContributors.size() > 0 )
    {
      if ( sendNotification )
      {
        purlContributorNonResponseCount = sendPurlContributorNonResponseMessage( pendingContributors, messageId, promotion, promotionNotificationType );
      }
      else
      {
        return pendingContributors.size();
      }
    }
    return purlContributorNonResponseCount;
  }

  /**
   * Returns a list of eligible approvers based on the promotion approver type.
   * 
   * @param promotion
   * @return List of eligible approvers
   */
  protected Set getDistinctApproversByPromotion( Promotion promotion )
  {
    // If approver type is 'specific_approv', approvers are on the promotion, just return the set of
    // PromotionApprovalParticipant
    ApproverType approverType = promotion.getApproverType();
    Set distinctApprovers = new HashSet();
    if ( approverType.getCode().equals( ApproverType.SPECIFIC_APPROVERS ) )
    {
      return new HashSet( promotion.getPromotionParticipantApprovers() );
    }
    else if ( approverType.getCode().equals( ApproverType.CUSTOM_APPROVERS ) )
    {
      List<Long> userIds = participantService.getAllEligibleApproversForCustomApprovalWithOpenClaims( promotion.getId() );
      for ( Long userId : userIds )
      {
        distinctApprovers.add( participantService.getParticipantById( userId ) );
      }
      return distinctApprovers;
    }

    // For other approver types, get all open claims for the promotion
    ClaimQueryConstraint claimQueryConstraint = new ClaimQueryConstraint();
    claimQueryConstraint.setOpen( Boolean.TRUE );
    claimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion.getId() } );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
    List claims = claimService.getClaimListWithAssociations( claimQueryConstraint, associationRequestCollection );

    for ( Iterator iterator = claims.iterator(); iterator.hasNext(); )
    {
      Claim claim = (Claim)iterator.next();

      try
      {
        // Get approvers by approver type on promotion
        if ( approverType.getCode().equals( ApproverType.SUBMITTERS_MANAGER ) )
        {
          distinctApprovers.addAll( getSubmittersManagers( claim ) );
        }
        else if ( approverType.getCode().equals( ApproverType.NODE_OWNER_BY_LEVEL ) || approverType.getCode().equals( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL ) )
        {
          distinctApprovers.addAll( getNodeOwnersByLevel( claim ) );
        }
        else if ( approverType.getCode().equals( ApproverType.NODE_OWNER_BY_TYPE ) || approverType.getCode().equals( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE ) )
        {
          distinctApprovers.addAll( getNodeOwnersByType( claim ) );
        }
        else if ( approverType.getCode().equals( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL ) )
        {
          distinctApprovers.addAll( getNomineeNodeOwnersByLevel( claim ) );
        }
        else if ( approverType.getCode().equals( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) )
        {
          distinctApprovers.addAll( getNomineeNodeOwnersByType( claim ) );
        }
        /*
         * else if ( approverType.getCode().equals( ApproverType.CUSTOM_APPROVERS ) ) { List<Long>
         * userIds = participantService.getAllEligibleApproversForCustomApproval( promotion.getId()
         * ); for ( Long userId : userIds ) { distinctApprovers.add( userService.getUserById( userId
         * ) ); } }
         */
      }
      catch( Exception e )
      {
        continue;
      }
    }
    return distinctApprovers;
  }

  protected Set getSubmittersManagers( Claim claim )
  {
    Set managers = new HashSet();

    managers = claim.getSubmittersNode().getNodeManagersForUser( claim.getSubmitter() );

    return managers;
  }

  protected User goUpTheHierarchyToFindANodeOwner( Node node )
  {
    User nodeOwner = null;

    for ( int i = 1; i <= 10; i++ )
    {
      node = node.getParentNode();
      if ( node.getNodeOwner() != null )
      {
        nodeOwner = node.getNodeOwner();
        break;
      }
    }
    return nodeOwner;
  }

  protected Set getNodeOwnersByLevel( Claim claim )
  {
    Set nodeOwners = new HashSet();

    // Start with submitter's node
    Node node = claim.getSubmittersNode();
    User nodeOwner = null;

    // Approval Round == 1 is the current submitter's node owner
    if ( claim.getApprovalRound().intValue() == 1 )
    {
      nodeOwner = node.getNodeOwner();
      if ( nodeOwner != null )
      {
        nodeOwners.add( nodeOwner );
      }
      else
      {
        addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + node.getName() + " does not have a Node Owner." );
        nodeOwner = goUpTheHierarchyToFindANodeOwner( node );
        if ( nodeOwner != null )
        {
          nodeOwners.add( nodeOwner );
          addComment( "Promotion: " + claim.getPromotion().getName() + ". Escalated to Node Owner " + nodeOwner.getNameLFMWithComma() + " up the hierarchy for Node: " + node.getName() );

        }
        else
        {
          addComment( "Promotion: " + claim.getPromotion().getName() + ". Cannot find a Node Owner up the hierarchy for Node: " + node.getName() );

        }
      }
    }
    else
    {
      for ( int i = 1; i < claim.getApprovalRound().intValue(); i++ )
      {
        node = node.getParentNode(); // get the node for the current approver round
      }
      nodeOwner = node.getNodeOwner();
      if ( nodeOwner != null )
      {
        nodeOwners.add( nodeOwner );
      }
      else
      {
        addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + node.getName() + " does not have a Node Owner." );
        nodeOwner = goUpTheHierarchyToFindANodeOwner( node );
        if ( nodeOwner != null )
        {
          nodeOwners.add( nodeOwner );
          addComment( "Promotion: " + claim.getPromotion().getName() + ". Escalated to Node Owner " + nodeOwner.getNameLFMWithComma() + " up the hierarchy for Node: " + node.getName() );

        }
        else
        {
          addComment( "Promotion: " + claim.getPromotion().getName() + ". Cannot find a Node Owner up the hierarchy for Node: " + node.getName() );

        }
      }
    }
    return nodeOwners;
  }

  protected Set getNomineeNodeOwnersByLevel( Claim claim )
  {
    Set nodeOwners = new HashSet();
    Set nominees = new HashSet();

    // Get nominees' node(s)
    NominationClaim nominationClaim = (NominationClaim)claim;

    nominees = nominationClaim.getClaimRecipients();

    Iterator nomineesIter = nominees.iterator();
    for ( Iterator iter2 = nomineesIter; iter2.hasNext(); )
    {
      ClaimRecipient nominee = (ClaimRecipient)iter2.next();

      Node node = nominee.getNode();
      User nodeOwner = null;

      // Approval Round == 1 is the current nominee's node owner
      if ( claim.getApprovalRound().intValue() == 1 )
      {
        nodeOwner = node.getNodeOwner();
        if ( nodeOwner != null )
        {
          nodeOwners.add( nodeOwner );
        }
        else
        {
          addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + node.getName() + " does not have a Node Owner." );
          nodeOwner = goUpTheHierarchyToFindANodeOwner( node );
          if ( nodeOwner != null )
          {
            nodeOwners.add( nodeOwner );
            addComment( "Promotion: " + claim.getPromotion().getName() + ". Escalated to Node Owner " + nodeOwner.getNameLFMWithComma() + " up the hierarchy for Node: " + node.getName() );

          }
          else
          {
            addComment( "Promotion: " + claim.getPromotion().getName() + ". Cannot find a Node Owner up the hierarchy for Node: " + node.getName() );

          }
        }
      }
      else
      {
        for ( int i = 1; i < claim.getApprovalRound().intValue(); i++ )
        {
          node = node.getParentNode(); // get the node for the current approver round
        }
        nodeOwner = node.getNodeOwner();
        if ( nodeOwner != null )
        {
          nodeOwners.add( nodeOwner );
        }
        else
        {
          addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + node.getName() + " does not have a Node Owner." );
          nodeOwner = goUpTheHierarchyToFindANodeOwner( node );
          if ( nodeOwner != null )
          {
            nodeOwners.add( nodeOwner );
            addComment( "Promotion: " + claim.getPromotion().getName() + ". Escalated to Node Owner " + nodeOwner.getNameLFMWithComma() + " up the hierarchy for Node: " + node.getName() );

          }
          else
          {
            addComment( "Promotion: " + claim.getPromotion().getName() + ". Cannot find a Node Owner up the hierarchy for Node: " + node.getName() );

          }
        }

      }
    }
    return nodeOwners;
  }

  protected Set getNodeOwnersByType( Claim claim )
  {
    Set nodeOwners = new HashSet();

    // Get submitter's node
    Node submitterNode = claim.getSubmittersNode();
    User nodeOwner = null;

    // Get all nodes whose node type equals to the node type specified on the promotion setup
    List nodes = nodeService.getNodesByNodeType( claim.getPromotion().getApprovalNodeType() );

    for ( int i = 0; i < nodes.size(); i++ )
    {
      Node approvalNodeTypeNode = (Node)nodes.get( i );

      // Get the node who is a member of the same branch in the hierarchy as the submitter's
      // node
      if ( submitterNode.isMemberOfInputNodeBranch( approvalNodeTypeNode ) || approvalNodeTypeNode.isMemberOfInputNodeBranch( submitterNode ) )
      {
        // Approval Round == 1 is the approval node type node's node owner
        if ( claim.getApprovalRound().intValue() == 1 )
        {
          nodeOwner = approvalNodeTypeNode.getNodeOwner();
          if ( nodeOwner != null )
          {
            nodeOwners.add( nodeOwner );
          }
          else
          {
            addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + approvalNodeTypeNode.getName() + " does not have a Node Owner." );
          }
        }
        else
        {
          for ( int j = 1; j < claim.getApprovalRound().intValue(); i++ )
          {
            approvalNodeTypeNode = approvalNodeTypeNode.getParentNode(); // get the node for the
                                                                         // current approver round
          }
          nodeOwner = approvalNodeTypeNode.getNodeOwner();
          if ( nodeOwner != null )
          {
            nodeOwners.add( nodeOwner );
          }
          else
          {
            addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + approvalNodeTypeNode.getName() + " does not have a Node Owner." );
          }
        }
      }
    }
    return nodeOwners;
  }

  protected Set getNomineeNodeOwnersByType( Claim claim )
  {
    Set nodeOwners = new HashSet();
    Set nominees = new HashSet();

    // Get nominees' node(s)
    NominationClaim nominationClaim = (NominationClaim)claim;

    nominees = nominationClaim.getClaimRecipients();

    Iterator nomineesIter = nominees.iterator();
    for ( Iterator iter2 = nomineesIter; iter2.hasNext(); )
    {
      ClaimRecipient nominee = (ClaimRecipient)iter2.next();

      Node nomineeNode = nominee.getNode();
      User nodeOwner = null;

      // Get all nodes whose node type equals to the node type specified on the promotion setup
      List nodes = nodeService.getNodesByNodeType( claim.getPromotion().getApprovalNodeType() );

      for ( int i = 0; i < nodes.size(); i++ )
      {
        Node approvalNodeTypeNode = (Node)nodes.get( i );

        // Get the node who is a member of the same branch in the hierarchy as the nominee's
        // node
        if ( nomineeNode.isMemberOfInputNodeBranch( approvalNodeTypeNode ) || approvalNodeTypeNode.isMemberOfInputNodeBranch( nomineeNode ) )
        {
          // Approval Round == 1 is the approval node type node's node owner
          if ( claim.getApprovalRound().intValue() == 1 )
          {
            nodeOwner = approvalNodeTypeNode.getNodeOwner();
            if ( nodeOwner != null )
            {
              nodeOwners.add( nodeOwner );
            }
            else
            {
              addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + approvalNodeTypeNode.getName() + " does not have a Node Owner." );
            }
          }
          else
          {
            for ( int j = 1; j < claim.getApprovalRound().intValue(); i++ )
            {
              approvalNodeTypeNode = approvalNodeTypeNode.getParentNode(); // get the node for
              // the current
              // approver round
            }
            nodeOwner = approvalNodeTypeNode.getNodeOwner();
            if ( nodeOwner != null )
            {
              nodeOwners.add( nodeOwner );
            }
            else
            {
              addComment( "Promotion: " + claim.getPromotion().getName() + ". Node: " + approvalNodeTypeNode.getName() + " does not have a Node Owner." );
            }
          }
        }
      }

    }

    return nodeOwners;
  }

  /**
   * Determine if now is the time to send the approver reminder.
   * 
   * @param approverUserId
   * @param promotion
   * @param promotionNotificationType
   * @return true if now is the time to send the reminder email by considering many factors,
   *         otherwise false. claim approval start date (if exists for nomination) claim approval
   *         end date(if exists for nomination), remind when X days past submission, frequency
   *         (daily or weekly i.e.which day of the week), if multiple rounds of approvals, email the
   *         current round of approver only, one email per promotion for an approver.
   */
  protected boolean isTimeToRemind( Long approverUserId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean individualLaunch )
  {
    // Go thru all claims for this approver and determine if it has been X days
    // since a pending Claim has been submitted and today is the day to send reminder
    // return true as soon as one is found.
    List approvables = new ArrayList();
    approvables = getApprovableClaims( approverUserId, promotion );
    Date claimSubmissionDate = null;
    Date today = com.biperf.core.utils.DateUtils.toStartDate( com.biperf.core.utils.DateUtils.getCurrentDate() );

    for ( Iterator iter = approvables.iterator(); iter.hasNext(); )
    {
      Approvable approvable = (Approvable)iter.next();
      Claim approvableClaim = (Claim)approvable;
      boolean isActiveClaim = true;
      if ( approvableClaim instanceof NominationClaim )
      {
        isActiveClaim = ( (NominationClaim)approvableClaim ).isActiveNomClaim();
      }
      if ( isActiveClaim )
      {
        claimSubmissionDate = com.biperf.core.utils.DateUtils.toStartDate( approvableClaim.getSubmissionDate() );

        boolean claimsOverDued = false;
        // if it has been at least X days since submission then it's overdue
        if ( com.biperf.core.utils.DateUtils.getElapsedDays( claimSubmissionDate, today ) >= promotionNotificationType.getNumberOfDays().intValue() )
        {
          claimsOverDued = true;
        }

        // Claims overdue?
        if ( claimsOverDued )
        {
          // Lastly, should approval be deferred for any promotion specific reasons
          if ( !needApproval( promotion, approvable ) )
          {
            return false;
          }
          // If so, is sent frequency set to daily
          boolean isTimeToRemind = false;
          isTimeToRemind = isEligibleforNotification( promotionNotificationType, individualLaunch );
          return isTimeToRemind;
        }
      }
    }
    return false;
  }

  /**
   * Apply promotion specific validations to check to see if approval is needed.
   * 
   * @param promotion
   * @param approvable
   * @return true if approval is needed
   */
  protected boolean needApproval( Promotion promotion, Approvable approvable )
  {

    if ( promotion.isNominationPromotion() )
    {
      // if now is not within approval period
      if ( !com.biperf.core.utils.DateUtils.isTodaysDateBetween( promotion.getApprovalStartDate(), promotion.getApprovalEndDate() ) )
      {
        return false; // no need to go on
      }
    }

    // Check to see if we need approval: if approval type = manual, then always needs approval;
    // other approval types depends on the claim: conditional nth claim, amount based, pax based
    // approvals, etc.
    return claimService.isApprovalDeferred( approvable );

  }

  /**
   * @param approverUserId
   * @param promotion
   * @return a list of open, non-expired claims approvable by the given approver
   */
  protected List getApprovableClaims( Long approverUserId, Promotion promotion )
  {
    boolean cumulativeNomination = false;
    List approvableList = new ArrayList();

    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( nominationPromotion.isCumulative() )
      {
        cumulativeNomination = true;
      }
    }

    // Nomination cumulative claims
    if ( cumulativeNomination )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( nominationPromotion.isCumulative() )
      {
        AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
        claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );

        List promotionClaimGroupsValueList = claimGroupService.getClaimGroupsForApprovalByUser( approverUserId,
                                                                                                new Long[] { promotion.getId() },
                                                                                                Boolean.TRUE, // open
                                                                                                              // claims
                                                                                                              // only
                                                                                                PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                                claimGroupAssociationRequestCollection,
                                                                                                null, // don't
                                                                                                      // need
                                                                                                      // promotionAssociationRequest
                                                                                                Boolean.FALSE, // non-expired
                                                                                                               // claims
                                                                                                               // i.e.
                                                                                                               // do
                                                                                                               // not
                                                                                                               // want
                                                                                                               // claims
                                                                                                               // that
                                                                                                               // are
                                                                                                               // open
                                                                                                               // past
                                                                                                               // approval
                                                                                                               // end
                                                                                                               // date
                                                                                                Boolean.FALSE );

        approvableList.addAll( extractTransientClaimGroups( promotionClaimGroupsValueList ) );
      }
    }

    // All other claims
    if ( !cumulativeNomination )
    {
      AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );

      List promotionClaimsValueList = claimService.getClaimsForApprovalByUser( approverUserId,
                                                                               new Long[] { promotion.getId() },
                                                                               Boolean.TRUE, // open
                                                                                             // claims
                                                                                             // only
                                                                               null, // no start
                                                                                     // submission
                                                                                     // date
                                                                               null, // no end
                                                                                     // submission
                                                                                     // date
                                                                               promotion.getPromotionType(),
                                                                               claimAssociationRequestCollection,
                                                                               null, // don't need
                                                                                     // promotionAssociationRequest
                                                                               Boolean.FALSE,
                                                                               Boolean.FALSE ); // non-expired
                                                                                                // claims
                                                                                                // i.e.
                                                                                                // do
                                                                                                // not
                                                                                                // want
      // claims that are open past approval end
      // date
      approvableList.addAll( extractApprovables( promotionClaimsValueList ) );
    }
    return approvableList;
  }

  protected static List extractApprovables( List promotionApprovableValueList )
  {
    ArrayList approvables = new ArrayList();

    for ( Iterator iter = promotionApprovableValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionApprovableValue = (PromotionApprovableValue)iter.next();
      approvables.addAll( promotionApprovableValue.getApprovables() );
    }

    return approvables;
  }

  /**
   * Group claims into ClaimGroups grouped by node and nominee
   * 
   * @param promotionClaimsValueList
   */
  protected static List extractTransientClaimGroups( List promotionClaimsValueList )
  {
    Map claimGroupByNomineeAndNodeId = new LinkedHashMap();

    List claims = extractApprovables( promotionClaimsValueList );
    for ( Iterator iter = claims.iterator(); iter.hasNext(); )
    {
      NominationClaim claim = (NominationClaim)iter.next();
      ClaimRecipient claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();
      if ( claimRecipient != null )
      {
        if ( claimRecipient.getNode() != null && claimRecipient.getRecipient() != null )
        {
          Long nomineeNodeId = claimRecipient.getNode().getId();
          Long nomineeId = claimRecipient.getRecipient().getId();
          String key = nomineeId + "-" + nomineeNodeId;

          ClaimGroup claimGroup = (ClaimGroup)claimGroupByNomineeAndNodeId.get( key );
          if ( claimGroup == null )
          {
            claimGroup = new ClaimGroup( GuidUtils.generateGuid() );
            claimGroup.setOpen( true );
            claimGroup.setNode( claimRecipient.getNode() );
            claimGroup.setParticipant( claimRecipient.getRecipient() );
            claimGroup.setPromotion( claim.getPromotion() );
            claimGroupByNomineeAndNodeId.put( key, claimGroup );
          }
          claimGroup.addClaim( claim );
        }
      }
    }
    return new ArrayList( claimGroupByNomineeAndNodeId.values() );
  }

  /**
   * @param userId
   * @param promoType
   * @return the number of pending claims waiting for approval by the given user.
   */
  protected int getPendingClaimsCount( Long userId, PromotionType promoType, Long promotionId, ApproverType approverType )
  {
    int count = 0;

    if ( promoType.isProductClaimPromotion() )
    {
      // Get the number of product claims waiting for approval.
      count = claimService.getClaimsForApprovalByUserCount( userId, promoType );
    }
    else if ( promoType.isRecognitionPromotion() )
    {
      // Get the number of recognition claims waiting for approval.
      count = claimService.getClaimsForApprovalByUserCount( userId, promoType );
    }
    else if ( promoType.isNominationPromotion() )
    {
      if ( approverType.getCode().equals( ApproverType.CUSTOM_APPROVERS ) )
      {
        // the approver list found prior to this for CUSTOM_APPROVERS includes only approvers who
        // have open claims so they should get an email
        // no need to do another query to count the claims
        count = 1;
      }
      else
      {
        // Get the number of nomination claims waiting for approval.
        count = claimService.getNominationClaimsForApprovalByUserCount( userId, promoType, promotionId );
      }
    }
    return count;
  }

  protected Set createGiftCodeMailingRecipients( List giftCodes )
  {
    Set mailingRecipients = new HashSet();
    for ( int i = 0; i < giftCodes.size(); i++ )
    {
      Set holder = new HashSet();
      MerchOrder code = (MerchOrder)giftCodes.get( i );
      if ( null != code.getParticipant() )
      {
        holder.add( code.getParticipant() );
        Set codePax = createMailingRecipients( holder );
        if ( null != codePax && !codePax.isEmpty() )
        {
          mailingRecipients.add( new MailingGiftCodeRecipient( (MailingRecipient)codePax.iterator().next(), code ) );
        }
      }
    }

    return mailingRecipients;
  }

  protected Set createGoalQuestGiftCodeMailingRecipients( List giftCodes )
  {
    Set mailingRecipients = new HashSet();
    for ( int i = 0; i < giftCodes.size(); i++ )
    {
      Set holder = new HashSet();
      MerchOrder code = (MerchOrder)giftCodes.get( i );
      if ( null != code.getParticipant() )
      {
        holder.add( code.getParticipant() );
        Set codePax = createMailingRecipients( holder );
        if ( null != codePax && !codePax.isEmpty() )
        {
          mailingRecipients.add( new MailingGoalQuestGiftCodeRecipient( (MailingRecipient)codePax.iterator().next(), code ) );
        }
      }
    }

    return mailingRecipients;
  }

  /**
   * @param paxs
   * @return Set containing MailingRecipient objects for mailing
   */
  protected Set createMailingRecipients( Set paxs )
  {
    Set mailingRecipients = new HashSet();
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Object temp = iter.next();
      Participant pax = null;
      // paxs might be AudienceParticipant objects or Participants or FormattedValueBeans
      try
      {
        pax = (Participant)temp;
      }
      catch( ClassCastException cce )
      {
        try
        {
          pax = ( (AudienceParticipant)temp ).getParticipant();
        }
        catch( ClassCastException e )
        {
          try
          {
            pax = participantService.getParticipantById( ( (FormattedValueBean)temp ).getId() );
          }
          catch( ClassCastException ex )
          {
            try
            {
              pax = participantService.getParticipantById( ( (User)temp ).getId() );
              // CLIENT_ADMIN are non pax, so it returns null
              if ( pax == null )
              {
                continue;
              }
            }
            catch( ClassCastException ucce )
            {
              // the object was not a Participant, User, FormattedValueBean or AudienceParticipant
              // so we
              // don't care about it
              continue;
            }
          }
        }
      }

      MailingRecipient mr = new MailingRecipient();
      mr.setGuid( GuidUtils.generateGuid() );

      if ( pax.getLanguageType() != null )
      {
        mr.setLocale( pax.getLanguageType().getCode() );
      }
      else
      {
        mr.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }

      mr.setUser( pax );
      // BugFix 21288..populate place holder values for site links
      Map dataMap = new HashMap();
      String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      dataMap.put( "firstName", mr.getUser().getFirstName() );
      dataMap.put( "siteLink", siteLink );
      dataMap.put( "siteURL", siteLink );
      dataMap.put( "webSite", siteLink );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      dataMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
      mr.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( mr );
    }
    return mailingRecipients;
  }

  /**
   * Compose and send the Program Launch alert emails
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendLaunchMessage( Set recipients, long messageId, Promotion promo )
  {
    // Add the personalization data to the objectMap
    Map objectMap = new HashMap();
    /*
     * objectMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString(
     * promo.getSubmissionStartDate() ) );
     */

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    // Set the recipients on the mailing
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      Object temp = iter.next();
      MailingRecipient mr = (MailingRecipient)temp;

      MailingRecipientData mrd2 = new MailingRecipientData();
      mrd2.setKey( "firstName" );
      mrd2.setValue( mr.getUser().getFirstName() );
      mrd2.setKey( "date" );
      mrd2.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionStartDate(), LocaleUtils.getLocale( mr.getLocale() ) ) );
      mr.addMailingRecipientData( mrd2 );
      MailingRecipientData mrd5 = new MailingRecipientData();
      mrd5.setKey( "numberOfDays" );
      if ( promo.getNumberOfDays() == 0 )
      {
        mrd5.setValue( "TRUE" );
      }
      else
      {
        mrd5.setValue( "FALSE" );
      }

      mr.addMailingRecipientData( mrd5 );
      mailing.addMailingRecipient( mr );

      if ( promo.getPromoNameAssetCode() != null )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mr.getLocale() ) );
        mr.addMailingRecipientData( promotionName );
      }
      if ( promo instanceof NominationPromotion )
      {
        NominationPromotion nomPromo = (NominationPromotion)promo;

        MailingRecipientData mrd1 = new MailingRecipientData();
        mrd1.setKey( "programName" );
        mrd1.setValue( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
        mr.addMailingRecipientData( mrd1 );

        MailingRecipientData mrd3 = new MailingRecipientData();
        mrd3.setKey( "behaviors" );
        mrd3.setValue( String.valueOf( nomPromo.isBehaviorActive() ) );
        mr.addMailingRecipientData( mrd3 );

        Set<PromotionBehavior> promoBehaviors = nomPromo.getPromotionBehaviors();
        String listBehaviors = "";
        for ( Iterator<PromotionBehavior> behaviorIter = promoBehaviors.iterator(); behaviorIter.hasNext(); )
        {
          PromotionBehavior promotionBehavior = behaviorIter.next();
          listBehaviors = listBehaviors + "\n";
          listBehaviors = listBehaviors + promotionBehavior.getPromotionBehaviorType().getName();
        }
        MailingRecipientData mrd4 = new MailingRecipientData();
        mrd4.setKey( "listBehaviors" );
        mrd4.setValue( listBehaviors );
        mr.addMailingRecipientData( mrd4 );
      }
    }

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Promotion Launch Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " users need Promotion Launch email for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Promotion Launch Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Promotion Launch Email for promotion: " + promo.getName() + ". See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Compose and send the Program End alert emails
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendEndMessage( Set recipients, long messageId, Promotion promo )
  {

    // Add the personalization data to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "url", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

    // Compose the generic portion of the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    // Set the recipients on the mailing
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient mr = (MailingRecipient)iter.next();
      mailing.addMailingRecipient( mr );

      String budgetDataValue = "";
      String useBudgetDataValue = "";

      if ( promo instanceof ChallengePointPromotion )
      {

        MailingRecipientData firstName = new MailingRecipientData();
        firstName.setKey( "firstName" );
        firstName.setValue( mr.getUser().getFirstName() );
        mr.addMailingRecipientData( firstName );
        MailingRecipientData lastName = new MailingRecipientData();
        lastName.setKey( "lastName" );
        lastName.setValue( mr.getUser().getLastName() );
        mr.addMailingRecipientData( lastName );
      }

      // If recognition promotion: Get Promotion Budget for the user and set budget and useBudget
      if ( promo instanceof RecognitionPromotion )
      {
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promo;
        if ( recognitionPromotion.isBudgetUsed() && getPromotionBudget( promo, mr ) > 0 )
        {
          // Set recipient specific data on the MailingRecipientData
          PromotionAwardsType awardType = null;
          awardType = PromotionAwardsType.lookup( promo.getAwardType().getCode() );
          String awardTerm;
          if ( awardType != null )
          {
            awardTerm = awardType.getName(); // e.g. Points
          }
          else
          {
            awardTerm = "budget";
          }

          budgetDataValue = "You have " + awardTerm + " remaining in your promotion budget.";
          useBudgetDataValue = "and use your remaining budget ";
        }
      }

      if ( promo instanceof NominationPromotion )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( promo.getName() );
        mr.addMailingRecipientData( promotionName );

        MailingRecipientData promotionEndDate = new MailingRecipientData();
        promotionEndDate.setKey( "promotionEndDate" );
        promotionEndDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) );
        mr.addMailingRecipientData( promotionEndDate );
      }

      MailingRecipientData date = new MailingRecipientData();
      date.setKey( "firstName" );
      date.setValue( mr.getUser().getFirstName() );
      date.setKey( "date" );
      date.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) );
      mr.addMailingRecipientData( date );

      MailingRecipientData mrd = new MailingRecipientData();
      mrd.setKey( "budget" );
      mrd.setValue( budgetDataValue );
      mr.addMailingRecipientData( mrd );

      MailingRecipientData mrd2 = new MailingRecipientData();
      mrd2.setKey( "useBudget" );
      mrd2.setValue( useBudgetDataValue );
      mr.addMailingRecipientData( mrd2 );

      if ( promo.getPromoNameAssetCode() != null )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mr.getLocale() ) );
        mr.addMailingRecipientData( promotionName );
      }
    }

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Promotion End Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " users need Promotion End email for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Promotion End Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Promotion End Email for promotion: " + promo.getName() + ". See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Returns the total available budget amount for a pax on a given promotion
   * 
   * @param promo
   * @param mr
   */
  protected double getPromotionBudget( Promotion promo, MailingRecipient mr )
  {
    double totalAvailableBudget = 0;
    List claimableNodes = promotionService.getClaimableNodes( promo.getId(), mr.getUser().getId() );
    for ( Iterator iter = claimableNodes.iterator(); iter.hasNext(); )
    {
      Node node = (Node)iter.next();
      // Get budget per user per node for the promotion
      if ( promo != null && node != null && mr.getUser().getId() != null )
      {
        Budget availableBudget = promotionService.getAvailableBudget( promo.getId(), mr.getUser().getId(), node.getId() );
        totalAvailableBudget = totalAvailableBudget + availableBudget.getCurrentValue().doubleValue();
      }
    }
    return totalAvailableBudget;

  }

  /**
   * Compose and send the Program Inactivity alerts
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendInactivityMessage( Set recipients, long messageId, Promotion promo )
  {
    // Add the personalization data to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "url", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    List<RecognitionInactivityNotification> recognitionInactivityNotifications = new ArrayList<RecognitionInactivityNotification>();

    // Set the recipients on the mailing
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      String promotionName = "";
      String deadline = "";
      Object temp = iter.next();
      MailingRecipient mr = (MailingRecipient)temp;
      objectMap.put( "firstName", mr.getUser().getFirstName() );
      if ( promo.getSubmissionEndDate() != null )
      {
        objectMap.put( "showDeadline", String.valueOf( Boolean.TRUE ) );
        if ( promo instanceof RecognitionPromotion )
        {
          deadline = com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
          objectMap.put( "deadline", deadline + "." );
        }
        else if ( promo instanceof ProductClaimPromotion )
        {
          deadline = com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
          objectMap.put( "deadline", deadline + "." );
        }
        else if ( promo instanceof QuizPromotion )
        {
          deadline = com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
          objectMap.put( "deadline", deadline );
        }
        else if ( promo instanceof SurveyPromotion )
        {
          deadline = com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) );
          objectMap.put( "deadline", deadline );
        }
      }

      if ( promo instanceof NominationPromotion )
      {
        NominationPromotion nomPromo = (NominationPromotion)promo;

        objectMap.put( "nominatorFirstName", mr.getUser().getFirstName() );
        objectMap.put( "behaviors", String.valueOf( nomPromo.isBehaviorActive() ) );
        Set<PromotionBehavior> promoBehaviors = nomPromo.getPromotionBehaviors();
        String listBehaviors = "";
        for ( PromotionBehavior promotionBehavior : promoBehaviors )
        {
          listBehaviors = listBehaviors + "<br/>";
          listBehaviors = listBehaviors + promotionBehavior.getPromotionBehaviorType().getName();
        }
        objectMap.put( "listBehaviors", listBehaviors );
        objectMap.put( "promotionName", nomPromo.getName() );
        objectMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
        // objectMap.put( "siteURL", systemVariableService.getPropertyByNameAndEnvironment(
        // SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      }

      mr.addMailingRecipientDataFromMap( objectMap );
      mailing.addMailingRecipient( mr );

      if ( promo.getPromoNameAssetCode() != null )
      {
        MailingRecipientData mailingPromotionName = new MailingRecipientData();
        mailingPromotionName.setKey( "promotionName" );
        promotionName = getPromotionName( promo.getPromoNameAssetCode(), mr.getLocale() );
        mailingPromotionName.setValue( promotionName );
        mr.addMailingRecipientData( mailingPromotionName );
      }

      RecognitionInactivityNotification recognitionInactivityNotification = new RecognitionInactivityNotification( mr.getUser()
          .getId(), mr.getUser().getLanguageType() != null ? mr.getUser().getLanguageType().getCode() : null, promotionName, deadline );
      recognitionInactivityNotifications.add( recognitionInactivityNotification );
      objectMap.remove( "deadline" );
      objectMap.remove( "nominatorFirstName" );
    }

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Promotion Inactivity Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " users need Promotion Inactivity email for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Promotion Inactivity Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Promotion Inactivity Email for promotion: " + promo.getName() + ". See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }

    // Send Inactivity Recognition Push Notifications
    try
    {
      if ( recognitionInactivityNotifications.size() > 0 )
      {
        mobileNotificationService.inactivityRecognitionNotification( recognitionInactivityNotifications );
      }
      log.debug( "Number of recipients attempted for Promotion Inactivity Mobile Push Notification: " + mailing.getMailingRecipients().size() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Promotion Inactivity Mobile Push Notification for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")",
                 e );
    }

  }

  /**
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendApproverReminderMessage( Set recipients, long messageId, Promotion promo, boolean isDefaultApprover )
  {
    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();

    /*
     * if ( promo.getApprovalEndDate() != null ) { objectMap.put( "approvalEndDate",
     * com.biperf.core.utils.DateUtils.toDisplayString( promo.getApprovalEndDate() ) ); }
     */

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    // Set the recipients on the mailing and add recipient level personalization
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient mailingRecipient = (MailingRecipient)iter.next();
      MailingRecipientData firstName = new MailingRecipientData();
      firstName.setKey( "firstName" );
      firstName.setValue( mailingRecipient.getUser().getFirstName() );
      firstName.setMailingRecipient( mailingRecipient );
      mailingRecipient.addMailingRecipientData( firstName );

      if ( promo.getApprovalEndDate() != null )
      {
        MailingRecipientData approvalEndDate = new MailingRecipientData();
        approvalEndDate.setKey( "approvalEndDate" );
        approvalEndDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promo.getApprovalEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
        approvalEndDate.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( approvalEndDate );
      }

      String url = getPendingApprovalUrl( promo );
      if ( url != null )
      {
        MailingRecipientData pendingApprovalUrl = new MailingRecipientData();
        pendingApprovalUrl.setKey( "pendingApprovalUrl" );
        pendingApprovalUrl.setValue( url );
        pendingApprovalUrl.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( pendingApprovalUrl );
      }

      if ( promo.getPromoNameAssetCode() != null )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
        mailingRecipient.addMailingRecipientData( promotionName );
      }

      if ( promo instanceof NominationPromotion )
      {
        NominationPromotion nomPromotion = (NominationPromotion)promo;
        Long pendingNominationCount = participantService.getPendingNominationCountForApprover( mailingRecipient.getUser().getId() );

        MailingRecipientData mrd1 = new MailingRecipientData();
        mrd1.setKey( "defaultApprover" );
        mrd1.setValue( isDefaultApprover ? "TRUE" : "FALSE" );
        mailingRecipient.addMailingRecipientData( mrd1 );

        MailingRecipientData mrd2 = new MailingRecipientData();
        mrd2.setKey( "pendingNominationCount" );
        mrd2.setValue( String.valueOf( pendingNominationCount ) );
        mailingRecipient.addMailingRecipientData( mrd2 );

        MailingRecipientData mrd3 = new MailingRecipientData();
        mrd3.setKey( "programName" );
        mrd3.setValue( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
        mailingRecipient.addMailingRecipientData( mrd3 );

        MailingRecipientData mrd4 = new MailingRecipientData();
        if ( pendingNominationCount == 1 )
        {
          mrd4.setKey( "oneNominationPending" );
          mrd4.setValue( "TRUE" );
        }
        else if ( pendingNominationCount > 1 )
        {
          mrd4.setKey( "multipleNominationsPending" );
          mrd4.setValue( "TRUE" );
        }
        mailingRecipient.addMailingRecipientData( mrd4 );

        MailingRecipientData mrd5 = new MailingRecipientData();
        mrd5.setKey( "siteUrl" );
        mrd5.setValue( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
        mailingRecipient.addMailingRecipientData( mrd5 );
      }

      mailing.addMailingRecipient( mailingRecipient );
    }

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Approver Reminder Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " users need Approver Reminder for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Approver Reminder Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Approver Reminder Email for promotion: " + promo.getName() + ". See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Compose and send the Approver Reminder alerts
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendChallengepointNotSelectedMessage( Set recipients, long messageId, Promotion promo )
  {
    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();

    if ( isNotice() )
    {
      objectMap.put( "notice", "Notice : " );
    }
    else
    {
      objectMap.put( "notice", "" );
    }

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );
    MailingBatch goalNotSelectedBatch = applyBatch( promo.getName() + " ChallengePoint Not Selected: " );
    mailing.setMailingBatch( goalNotSelectedBatch );
    addComment( getMailingBatchProcessComments( goalNotSelectedBatch ) );
    // Set the recipients on the mailing and add recipient level personalization
    boolean sampleSelected = false;
    boolean batchEmailEnabled = mailingService.isBatchEmailEnabled();
    List allUsers = new ArrayList();
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient mailingRecipient = (MailingRecipient)iter.next();
      if ( isNotice() )
      {
        allUsers.add( mailingRecipient.getUser() );
      }
      if ( !isNotice() || isNotice() && !sampleSelected )
      {
        MailingRecipientData firstName = new MailingRecipientData();
        firstName.setKey( "firstName" );
        firstName.setValue( mailingRecipient.getUser().getFirstName() );
        firstName.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( firstName );

        MailingRecipientData lastName = new MailingRecipientData(); // First name and lastname added
                                                                    // for goal not selected mail
        lastName.setKey( "lastName" );
        lastName.setValue( mailingRecipient.getUser().getLastName() );
        lastName.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( lastName );

        if ( promo.isChallengePointPromotion() )
        {
          MailingRecipientData mrd4 = new MailingRecipientData();
          mrd4.setKey( "challengepointSelectionEndDate" );
          mrd4.setValue( com.biperf.core.utils.DateUtils.toDisplayString( ( (ChallengePointPromotion)promo ).getGoalCollectionEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

          mailingRecipient.addMailingRecipientData( mrd4 );

        }

        if ( promo.getPromoNameAssetCode() != null )
        {
          MailingRecipientData promotionName = new MailingRecipientData();
          promotionName.setKey( "promotionName" );
          promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
          promotionName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( promotionName );
        }

        // must add this data to the DATABASE in case of batch email enabled...
        if ( batchEmailEnabled )
        {
          if ( promo.isChallengePointPromotion() )
          {
            MailingRecipientData challengepointSelectionEndDate = new MailingRecipientData();
            challengepointSelectionEndDate.setKey( "challengepointSelectionEndDate" );
            challengepointSelectionEndDate
                .setValue( com.biperf.core.utils.DateUtils.toDisplayString( ( (ChallengePointPromotion)promo ).getGoalCollectionEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
            challengepointSelectionEndDate.setMailingRecipient( mailingRecipient );
            mailingRecipient.addMailingRecipientData( challengepointSelectionEndDate );
          }
        }

        if ( isNotice() && !sampleSelected )
        {
          mailingRecipient.setUser( getRunByUser() );
        }
        mailing.addMailingRecipient( mailingRecipient );
        sampleSelected = true;
      }
    }

    // Send the e-mail message with personalization
    try
    {
      if ( isNotice() )
      {
        File file = extractFile( allUsers, BEAN_NAME );
        if ( file != null )
        {
          String absPath = file.getAbsolutePath();
          String fileName = file.getName();
          mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
        }
      }
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Challengepoint Not Selection Reminder Email: " + recipients.size() );
      addComment( recipients.size() + " users need Challengepoint not Selected Reminder for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Challengepoint Not Selected Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending  Not Selected Email for promotion: " + promo.getName() + ". See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Compose and send the Approver Reminder alerts
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendGoalNotSelectedMessage( Set recipients, long messageId, Promotion promo )
  {
    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();

    if ( isNotice() )
    {
      objectMap.put( "notice", "Notice : " );
    }
    else
    {
      objectMap.put( "notice", "" );
    }

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );
    MailingBatch goalNotSelectedBatch = applyBatch( promo.getName() + " Goal Not Selected: " );
    mailing.setMailingBatch( goalNotSelectedBatch );
    addComment( getMailingBatchProcessComments( goalNotSelectedBatch ) );
    // Set the recipients on the mailing and add recipient level personalization
    boolean sampleSelected = false;
    List allUsers = new ArrayList();
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient mailingRecipient = (MailingRecipient)iter.next();
      if ( isNotice() )
      {
        allUsers.add( mailingRecipient.getUser() );
      }
      if ( !isNotice() || isNotice() && !sampleSelected )
      {
        MailingRecipientData firstName = new MailingRecipientData();
        firstName.setKey( "firstName" );
        firstName.setValue( mailingRecipient.getUser().getFirstName() );
        firstName.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( firstName );

        MailingRecipientData lastName = new MailingRecipientData(); // First name and lastname added
                                                                    // for goal not selected mail
        lastName.setKey( "lastName" );
        lastName.setValue( mailingRecipient.getUser().getLastName() );
        lastName.setMailingRecipient( mailingRecipient );
        mailingRecipient.addMailingRecipientData( lastName );
        if ( promo.isGoalQuestPromotion() )
        {

          MailingRecipientData goalSelectionEndDate = new MailingRecipientData(); // First name and
                                                                                  // lastname added
                                                                                  // for goal not
                                                                                  // selected mail
          goalSelectionEndDate.setKey( "goalSelectionEndDate" );
          goalSelectionEndDate
              .setValue( com.biperf.core.utils.DateUtils.toDisplayString( ( (GoalQuestPromotion)promo ).getGoalCollectionEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
          goalSelectionEndDate.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( goalSelectionEndDate );

        }

        if ( promo.getPromoNameAssetCode() != null )
        {
          MailingRecipientData promotionName = new MailingRecipientData();
          promotionName.setKey( "promotionName" );
          promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
          mailingRecipient.addMailingRecipientData( promotionName );
        }

        if ( isNotice() && !sampleSelected )
        {
          mailingRecipient.setUser( getRunByUser() );
        }
        mailing.addMailingRecipient( mailingRecipient );
        sampleSelected = true;
      }
    }

    // Send the e-mail message with personalization
    try
    {
      if ( isNotice() )
      {
        File file = extractFile( allUsers, BEAN_NAME );
        if ( file != null )
        {
          String absPath = file.getAbsolutePath();
          String fileName = file.getName();
          mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
        }
      }
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Goal Not Selection Reminder Email: " + recipients.size() );
      addComment( recipients.size() + " users need Goal not Selected Reminder for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Goal Not Selected Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Goal Not Selected Email for promotion: " + promo.getName() + ". See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Compose and send the Redemption Reminder alerts
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendRedemptionReminderMessage( Set recipients, long messageId, Promotion promo )
  {
    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    // Set the recipients on the mailing and add recipient level personalization
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingGiftCodeRecipient holder = (MailingGiftCodeRecipient)iter.next();
      MerchOrder code = holder.getMerchOrder();
      MailingRecipient mailingRecipient = holder.getMailingRecipient();
      MailingRecipientData firstName = new MailingRecipientData();
      firstName.setKey( "firstName" );
      firstName.setValue( mailingRecipient.getUser().getFirstName() );
      firstName.setMailingRecipient( mailingRecipient );

      MailingRecipientData lastName = new MailingRecipientData();
      lastName.setKey( "lastName" );
      lastName.setValue( code.getParticipant().getLastName() );
      lastName.setMailingRecipient( mailingRecipient );

      MailingRecipientData merchlinqLink = new MailingRecipientData();
      merchlinqLink.setKey( "levelLink" );

      String aesEncryptionKey = systemVariableService.getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
      String aesInitVector = systemVariableService.getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
      String newGiftCodeEncrypted = null;
      try
      {
        newGiftCodeEncrypted = SecurityUtils.encryptAES( code.getFullGiftCode(), aesEncryptionKey, aesInitVector );
        log.debug( "Gift code after encryption and before sending email: " + newGiftCodeEncrypted );
      }
      catch( Exception e )
      {
        log.error( "Unable to encrypt gift code: " + code.getFullGiftCode(), e );
      }
      merchlinqLink.setValue( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/merchLevelShopping.do?method=shopOnline&gc="
          + newGiftCodeEncrypted );

      merchlinqLink.setMailingRecipient( mailingRecipient );

      MailingRecipientData level = new MailingRecipientData();
      level.setKey( "level" );
      level.setValue( CmsResourceBundle.getCmsBundle().getString( code.getPromoMerchProgramLevel().getCmAssetKey() + "." + PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY ) );
      level.setMailingRecipient( mailingRecipient );

      MailingRecipientData referenceNumber = new MailingRecipientData();
      referenceNumber.setKey( "referenceNumber" );
      referenceNumber.setValue( code.getReferenceNumber() );
      referenceNumber.setMailingRecipient( mailingRecipient );

      MailingRecipientData giftCode = new MailingRecipientData();
      giftCode.setKey( "giftCode" );
      giftCode.setValue( code.getFullGiftCode() );
      giftCode.setMailingRecipient( mailingRecipient );

      MailingRecipientData phoneNumber = new MailingRecipientData();
      phoneNumber.setKey( "phoneNumber" );
      phoneNumber.setValue( systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
      phoneNumber.setMailingRecipient( mailingRecipient );

      mailingRecipient.addMailingRecipientData( firstName );
      mailingRecipient.addMailingRecipientData( lastName );
      mailingRecipient.addMailingRecipientData( merchlinqLink );
      mailingRecipient.addMailingRecipientData( referenceNumber );
      mailingRecipient.addMailingRecipientData( giftCode );
      mailingRecipient.addMailingRecipientData( phoneNumber );
      mailingRecipient.addMailingRecipientData( level );

      if ( promo.getPromoNameAssetCode() != null )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
        mailingRecipient.addMailingRecipientData( promotionName );
      }

      mailing.addMailingRecipient( mailingRecipient );
    }

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for Non-redeemd Gift Code Reminder Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " gift codes have not been redeemed for promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Non-redeemd Gift Code Reminder Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Non-redeemd Gift Code Reminder Email for promotion: " + promo.getName() + ". See the log file for additional information.  "
          + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  /**
   * Compose and send the GoalQuest Redemption Reminder alerts
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected void sendGoalQuestRedemptionReminderMessage( Set recipients, long messageId, Promotion promo )
  {
    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();
    // objectMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString(
    // promo.getSubmissionEndDate() ) );

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    // Set the recipients on the mailing and add recipient level personalization
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingGoalQuestGiftCodeRecipient holder = (MailingGoalQuestGiftCodeRecipient)iter.next();
      MerchOrder code = holder.getMerchOrder();
      MailingRecipient mailingRecipient = holder.getMailingRecipient();
      MailingRecipientData firstName = new MailingRecipientData();
      firstName.setKey( "firstName" );
      firstName.setValue( mailingRecipient.getUser().getFirstName() );
      firstName.setMailingRecipient( mailingRecipient );

      MailingRecipientData lastName = new MailingRecipientData();
      lastName.setKey( "lastName" );
      lastName.setValue( code.getParticipant().getLastName() );
      lastName.setMailingRecipient( mailingRecipient );

      MailingRecipientData merchlinqLink = new MailingRecipientData();
      merchlinqLink.setKey( "levelLink" );
      merchlinqLink.setValue( buildMerchLinqUrlForGiftCodeRefundEmail( code.getFullGiftCode(), promo.getId() ) );

      merchlinqLink.setMailingRecipient( mailingRecipient );

      MailingRecipientData giftCode = new MailingRecipientData();
      giftCode.setKey( "giftCode" );
      giftCode.setValue( code.getFullGiftCode() );
      giftCode.setMailingRecipient( mailingRecipient );

      MailingRecipientData phoneNumber = new MailingRecipientData();
      phoneNumber.setKey( "phoneNumber" );
      phoneNumber.setValue( systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
      phoneNumber.setMailingRecipient( mailingRecipient );

      MailingRecipientData promotionEndDate = new MailingRecipientData();
      promotionEndDate.setKey( "promotionEndDate" );
      promotionEndDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
      promotionEndDate.setMailingRecipient( mailingRecipient );

      mailingRecipient.addMailingRecipientData( firstName );
      mailingRecipient.addMailingRecipientData( lastName );
      mailingRecipient.addMailingRecipientData( merchlinqLink );
      mailingRecipient.addMailingRecipientData( giftCode );
      mailingRecipient.addMailingRecipientData( phoneNumber );
      mailingRecipient.addMailingRecipientData( promotionEndDate );

      if ( promo.getPromoNameAssetCode() != null )
      {
        MailingRecipientData promotionName = new MailingRecipientData();
        promotionName.setKey( "promotionName" );
        promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
        mailingRecipient.addMailingRecipientData( promotionName );
      }

      mailing.addMailingRecipient( mailingRecipient );
    }
    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "Number of recipients attempted for GoalQuest Non-redeemd Gift Code Reminder Email: " + mailing.getMailingRecipients().size() );
      // addComment( mailing.getMailingRecipients().size() + " gift codes have not been redeemed for
      // promotion: " + promo.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending GoalQuest Non-redeemd Gift Code Reminder Email for promotion: " + promo.getName() + " (process invocation ID = " + getProcessInvocationId() + ")",
                 e );
      addComment( "An exception occurred while sending GoalQuest Non-redeemd Gift Code Reminder Email for promotion: " + promo.getName() + ". See the log file for additional information.  "
          + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }

  }

  /**
   * Compose and send the Purl Manager Nonresponse alerts
   * 
   * @param recipients
   * @param messageId
   * @param promo
   */
  protected int sendPurlManagerNonResponseMessage( List pendingPurlRecipients, long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType )
  {
    int purlManagerNonResponseCount = 0;
    Date now = new Date();
    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();

    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      objectMap.put( "points", PromotionAwardsType.POINTS );
    }
    // BugFix 18492 starts
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      objectMap.put( "pointsType", "TRUE" );
    }
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
    {
      objectMap.put( "merchandiseType", "TRUE" );
    }

    objectMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    for ( Iterator iter = pendingPurlRecipients.iterator(); iter.hasNext(); )
    {
      PurlRecipient purlRecipient = (PurlRecipient)iter.next();
      if ( purlRecipient.getAwardDate() != null )
      {
        // Manger non response Notification date : X days prior to the purl recipient award End date
        Date managerNonResponseNotificationDate = new Date( purlRecipient.getAwardDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
        // Current Date should be same as notification Date
        if ( DateUtils.isSameDay( now, managerNonResponseNotificationDate ) || now.after( managerNonResponseNotificationDate ) )
        {
          Participant nodeOwner = purlService.getNodeOwnerForPurlRecipient( purlRecipient );

          PurlContributor purlContributor = purlService.getPurlContributorByUserId( nodeOwner.getId(), purlRecipient.getId() );
          if ( null != nodeOwner )
          {
            MailingRecipient mailingRecipient = new MailingRecipient();
            mailingRecipient.setGuid( GuidUtils.generateGuid() );

            if ( nodeOwner.getLanguageType() != null )
            {
              mailingRecipient.setLocale( nodeOwner.getLanguageType().getCode() );
            }
            else
            {
              mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
            }
            mailingRecipient.setUser( nodeOwner );
            MailingRecipientData managerFirstName = new MailingRecipientData();
            managerFirstName.setKey( "managerFirstName" );
            managerFirstName.setValue( nodeOwner.getFirstName() );
            managerFirstName.setMailingRecipient( mailingRecipient );

            MailingRecipientData recipientFirstName = new MailingRecipientData();
            recipientFirstName.setKey( "recipientFirstName" );
            recipientFirstName.setValue( purlRecipient.getUser().getFirstName() );
            recipientFirstName.setMailingRecipient( mailingRecipient );

            MailingRecipientData recipientLastName = new MailingRecipientData();
            recipientLastName.setKey( "recipientLastName" );
            recipientLastName.setValue( purlRecipient.getUser().getLastName() );
            recipientLastName.setMailingRecipient( mailingRecipient );

            MailingRecipientData purlRecipientName = new MailingRecipientData();
            purlRecipientName.setKey( "purlRecipientName" );
            purlRecipientName.setValue( getPurlRecipientFullName( purlRecipient ) );
            purlRecipientName.setMailingRecipient( mailingRecipient );

            MailingRecipientData purlMaintenanceListLink = new MailingRecipientData();
            purlMaintenanceListLink.setKey( "maintenanceListLink" );

            Map clientStateParameterMap = new HashMap();
            purlMaintenanceListLink.setValue( ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                                    "/purl/purlMaintenanceList.do",
                                                                                    clientStateParameterMap ) );

            Map purlMaintainenanceMap = new HashMap();
            // QC bug #3373
            purlMaintainenanceMap.put( "purlRecipientId", purlRecipient.getId() );
            purlMaintainenanceMap.put( "purlReturnUrl", null );
            String purlMaintenanceLinkStr = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                                  "/recognitionWizard/purlInviteContributors.do?method=display",
                                                                                  purlMaintainenanceMap );
            MailingRecipientData purlMaintenanceLink = new MailingRecipientData();
            purlMaintenanceLink.setKey( "purlMaintenanceLink" );
            purlMaintenanceLink.setValue( purlMaintenanceLinkStr );
            purlMaintenanceLink.setMailingRecipient( mailingRecipient );

            Map purlContributionMap = new HashMap();
            if ( purlContributor != null )
            {

              purlContributionMap.put( "promotionId", purlContributor.getPurlRecipient().getPromotion().getId() );
              if ( purlContributor.getId() != null )
              {
                purlContributionMap.put( "purlContributorId", purlContributor.getId() );
              }
              String purlContributonLinkStr = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                                    "/purl/purlTNC.do?method=display",
                                                                                    purlContributionMap );

              MailingRecipientData purlContributonLink = new MailingRecipientData();
              purlContributonLink.setKey( "purlContributionLink" );
              purlContributonLink.setValue( purlContributonLinkStr );

              purlContributonLink.setMailingRecipient( mailingRecipient );
              mailingRecipient.addMailingRecipientData( purlContributonLink );
            }

            // Purl Invitation End Notification date : 5 days prior to the Purl award date
            Date purlInvitationEndNotificationDate = new Date( purlRecipient.getAwardDate().getTime() - 5 * DateUtils.MILLIS_PER_DAY );
            String userLanguageCode = null;
            if ( null != purlRecipient.getUser().getLanguageType() )
            {
              userLanguageCode = purlRecipient.getUser().getLanguageType().getLanguageCode();
            }
            Locale recipientLocale = userLanguageCode != null ? CmsUtil.getLocale( userLanguageCode ) : UserManager.getLocale();
            MailingRecipientData purlInvitationEndDate = new MailingRecipientData();
            purlInvitationEndDate.setKey( "purlInvitationEndDate" );
            purlInvitationEndDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( purlInvitationEndNotificationDate, recipientLocale ) );
            purlInvitationEndDate.setMailingRecipient( mailingRecipient );

            if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
            {
              for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
              {
                if ( purlRecipient.getCustomElement( i ).getDisplayValue() != null )
                {
                  MailingRecipientData formElementData = new MailingRecipientData();
                  formElementData.setKey( "formElement" + ( i + 1 ) );
                  formElementData.setValue( purlRecipient.getCustomElement( i ).getDisplayValue() );
                  formElementData.setMailingRecipient( mailingRecipient );
                  mailingRecipient.addMailingRecipientData( formElementData );
                }
              }
            }

            mailingRecipient.addMailingRecipientData( managerFirstName );
            mailingRecipient.addMailingRecipientData( recipientFirstName );
            mailingRecipient.addMailingRecipientData( recipientLastName );
            mailingRecipient.addMailingRecipientData( purlRecipientName );
            mailingRecipient.addMailingRecipientData( purlMaintenanceListLink );
            mailingRecipient.addMailingRecipientData( purlInvitationEndDate );
            mailingRecipient.addMailingRecipientData( purlMaintenanceLink );

            if ( promotion.getPromoNameAssetCode() != null )
            {
              MailingRecipientData promotionName = new MailingRecipientData();
              promotionName.setKey( "promotionName" );
              promotionName.setValue( getPromotionName( promotion.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
              mailingRecipient.addMailingRecipientData( promotionName );
            }

            mailing.addMailingRecipient( mailingRecipient );
          }
        }
      }
    }
    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      purlManagerNonResponseCount = mailing.getMailingRecipients().size();
      log.debug( "Number of recipients attempted for Purl Manager NonResponse Reminder Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " users need Purl Manager NonResponse Reminder for promotion: " + promotion.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Purl Manager NonResponse Reminder Email for promotion: " + promotion.getName() + " (process invocation ID = " + getProcessInvocationId() + ")",
                 e );
      addComment( "An exception occurred while sending Purl Manager NonResponse Reminder Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
          + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }

    return purlManagerNonResponseCount;
  }

  /**
   * Compose and send the Purl Contributor Nonresponse Reminder alerts
   * 
   * @param pendingContributors
   * @param messageId
   * @param promotion
   * @param promotionNotificationType
   */
  protected int sendPurlContributorNonResponseMessage( List pendingContributors, long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType )
  {
    int purlContributorNonResponseCount = 0;
    // Set contributors = new HashSet();
    Date now = new Date();
    User nodeOwner = null;

    // Add the mailing level personalization data to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "promotionId", promotion.getId() );

    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    for ( Iterator iter = pendingContributors.iterator(); iter.hasNext(); )
    {
      PurlContributor purlContributor = (PurlContributor)iter.next();
      PurlRecipient purlRecipient = purlContributor.getPurlRecipient();
      if ( purlRecipient.getAwardDate() != null )
      {
        // Purl Contributor non response Notification date : X days prior to the purl recipient
        // award End date
        Date contributorNonResponseNotificationDate = new Date( purlRecipient.getAwardDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
        // Current Date should be same as notification Date
        if ( DateUtils.isSameDay( now, contributorNonResponseNotificationDate ) || now.after( contributorNonResponseNotificationDate ) )
        {

          MailingRecipient mailingRecipient = new MailingRecipient();
          mailingRecipient.setGuid( GuidUtils.generateGuid() );

          if ( null != purlContributor.getUser() )
          {
            Participant contributor = participantService.getParticipantById( purlContributor.getUser().getId() );
            if ( contributor.getLanguageType() != null )
            {
              mailingRecipient.setLocale( contributor.getLanguageType().getCode() );
            }
            else
            {
              mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
            }
            mailingRecipient.setUser( contributor );
          }
          else
          {
            mailingRecipient.setPreviewEmailAddress( purlContributor.getEmailAddr() );
            mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
          }
          if ( purlContributor.getInvitedContributor() != null )
          {
            MailingRecipientData invitedContributorFirstName = new MailingRecipientData();
            invitedContributorFirstName.setKey( "invitedContributorFirstName" );
            invitedContributorFirstName.setValue( purlContributor.getInvitedContributor().getFirstName() );
            invitedContributorFirstName.setMailingRecipient( mailingRecipient );
            mailingRecipient.addMailingRecipientData( invitedContributorFirstName );

            MailingRecipientData invitedContributorLastName = new MailingRecipientData();
            invitedContributorLastName.setKey( "invitedContributorLastName" );
            invitedContributorLastName.setValue( purlContributor.getInvitedContributor().getLastName() );
            invitedContributorLastName.setMailingRecipient( mailingRecipient );
            mailingRecipient.addMailingRecipientData( invitedContributorLastName );

          }
          else
          {
            nodeOwner = purlService.getNodeOwnerForPurlRecipient( purlRecipient );
            if ( null != nodeOwner )
            {
              MailingRecipientData invitedContributorFirstName = new MailingRecipientData();
              invitedContributorFirstName.setKey( "invitedContributorFirstName" );
              invitedContributorFirstName.setValue( nodeOwner.getFirstName() );
              invitedContributorFirstName.setMailingRecipient( mailingRecipient );
              mailingRecipient.addMailingRecipientData( invitedContributorFirstName );

              MailingRecipientData invitedContributorLastName = new MailingRecipientData();
              invitedContributorLastName.setKey( "invitedContributorLastName" );
              invitedContributorLastName.setValue( nodeOwner.getLastName() );
              invitedContributorLastName.setMailingRecipient( mailingRecipient );
              mailingRecipient.addMailingRecipientData( invitedContributorLastName );
            }
          }
          MailingRecipientData defaultInvitee = new MailingRecipientData();
          defaultInvitee.setKey( "defaultInvitee" );
          defaultInvitee.setValue( purlContributor.isDefaultInvitee() ? "TRUE" : "FALSE" );
          defaultInvitee.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( defaultInvitee );

          MailingRecipientData contributorFirstName = new MailingRecipientData();
          contributorFirstName.setKey( "contributorFirstName" );
          contributorFirstName.setValue( purlContributor.getFirstName() );
          contributorFirstName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( contributorFirstName );

          clientStateParameterMap.put( "purlContributorId", purlContributor.getId() );

          MailingRecipientData contributorLastName = new MailingRecipientData();
          contributorLastName.setKey( "contributorLastName" );
          contributorLastName.setValue( purlContributor.getLastName() );
          contributorLastName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( contributorLastName );

          StringBuffer purlRecipientFullName = new StringBuffer();
          purlRecipientFullName.append( purlRecipient.getUser().getFirstName() ).append( " " ).append( purlRecipient.getUser().getLastName() ).append( " " );

          MailingRecipientData purlRecipientName = new MailingRecipientData();
          purlRecipientName.setKey( "purlRecipientName" );
          purlRecipientName.setValue( purlRecipientFullName.toString() );
          purlRecipientName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( purlRecipientName );

          MailingRecipientData recipientFirstName = new MailingRecipientData();
          recipientFirstName.setKey( "recipientFirstName" );
          recipientFirstName.setValue( purlRecipient.getUser().getFirstName() );
          recipientFirstName.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( recipientFirstName );

          if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
          {
            for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
            {
              if ( purlRecipient.getCustomElement( i ).getDisplayValue() != null )
              {
                MailingRecipientData formElementData = new MailingRecipientData();
                formElementData.setKey( "formElement" + ( i + 1 ) );
                formElementData.setValue( purlRecipient.getCustomElement( i ).getDisplayValue() );
                formElementData.setMailingRecipient( mailingRecipient );
                mailingRecipient.addMailingRecipientData( formElementData );
              }
            }
          }

          MailingRecipientData contributorCloseDate = new MailingRecipientData();
          contributorCloseDate.setKey( "contributorCloseDate" );
          contributorCloseDate.setValue( com.biperf.core.utils.DateUtils.toDisplayString( purlRecipient.getCloseDate(),
                                                                                          LocaleUtils.getLocale( purlRecipient.getUser().getLanguageType() != null
                                                                                              ? purlRecipient.getUser().getLanguageType().getCode()
                                                                                              : systemVariableService.getDefaultLanguage().getStringVal() ) ) );
          contributorCloseDate.setMailingRecipient( mailingRecipient );
          mailingRecipient.addMailingRecipientData( contributorCloseDate );

          MailingRecipientData purlContributionLink = new MailingRecipientData();
          purlContributionLink.setKey( "purlContributionLink" );
          purlContributionLink.setValue( ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                               "/purl/purlTNC.do?method=display",
                                                                               clientStateParameterMap ) );
          mailingRecipient.addMailingRecipientData( purlContributionLink );

          if ( promotion.getPromoNameAssetCode() != null )
          {
            MailingRecipientData promotionName = new MailingRecipientData();
            promotionName.setKey( "promotionName" );
            promotionName.setValue( getPromotionName( promotion.getPromoNameAssetCode(), mailingRecipient.getLocale() ) );
            mailingRecipient.addMailingRecipientData( promotionName );
          }

          mailing.addMailingRecipient( mailingRecipient );
        }
      }

      // Send Nonresponse PURL contribution push notifications
      if ( purlContributor.getUser() != null )
      {
        // Send purl notification
        String milestone = purlContributor.getPurlRecipient().getCustomFormElementInfo() + " " + purlContributor.getPurlRecipient().getPromotion().getName();
        mobileNotificationService.purlContributorInviteNotification( purlContributor.getUser().getLanguageType() != null ? purlContributor.getUser().getLanguageType().getCode() : null,
                                                                     purlContributor.getPurlRecipient().getUser().getFirstName(),
                                                                     purlContributor.getPurlRecipient().getUser().getLastName(),
                                                                     purlContributor.getPurlRecipient().getId(),
                                                                     milestone,
                                                                     purlContributor.getUser().getId(),
                                                                     true );
      }

    }
    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      purlContributorNonResponseCount = mailing.getMailingRecipients().size();
      log.debug( "Number of recipients attempted for Purl Contributor NonResponse Reminder Email: " + mailing.getMailingRecipients().size() );
      addComment( mailing.getMailingRecipients().size() + " users need Purl Contributor NonResponse Reminder for promotion: " + promotion.getName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Purl Contributor NonResponse Reminder Email for promotion: " + promotion.getName() + " (process invocation ID = " + getProcessInvocationId()
          + ")", e );
      addComment( "An exception occurred while sending Purl Contributor NonResponse Reminder Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
          + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }

    return purlContributorNonResponseCount;

  }

  private String buildMerchLinqUrlForGiftCodeRefundEmail( String giftCode, Long promotionId )
  {
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    Map params = new HashMap();
    params.put( "giftcode", giftCode );
    // CR - Convert to PerQs - START
    params.put( "promotionId", promotionId );
    // CR - Convert to PerQs - END

    return ClientStateUtils.generateEncodedLink( siteUrlPrefix, "/shopping.do?method=displayMerchLinq", params );
  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   * 
   * @param recipientCount
   * @param processType
   */
  protected void sendSummaryMessage( long recipientCount, String processType )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "processType", processType );
    objectMap.put( "count", new Long( recipientCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Message m = messageService.getMessageByCMAssetCode( MessageService.SUMMARY_CM_ASSET_CODE );
    Mailing mailing = composeMail( m.getId(), MailingType.PROMOTION );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "process: " + processType + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "number of recipients: " + recipientCount );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "process: " + processType + " has been sent to the process scheduler " + recipientUser.getFirstName() + " " + recipientUser.getLastName() + ". Total number of pax recipients: "
          + recipientCount );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + processType + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
      {
        addComment( "An exception occurred while sending " + processType + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }

    }
  }

  protected String getPurlRecipientFullName( PurlRecipient purlRecipient )
  {
    StringBuffer purlRecipientName = new StringBuffer();
    Date awardDate = null;

    purlRecipientName.append( purlRecipient.getUser().getFirstName() ).append( " " ).append( purlRecipient.getUser().getLastName() );

    return purlRecipientName.toString();
  }

  /**
   * @param promotion
   * @return the pending approval link
   */
  protected String getPendingApprovalUrl( Promotion promotion )
  {
    if ( promotion != null )
    {
      if ( promotion.isProductClaimPromotion() )
      {
        return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + PRODUCT_CLAIM_APPROVAL_LINK;
      }
      else if ( promotion.isRecognitionPromotion() )
      {
        return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + RECOGNITION_APPROVAL_LINK;
      }
      else if ( promotion.isNominationPromotion() )
      {
        return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + NOMINATION_APPROVAL_LINK;
      }
    }
    return null;
  }

  /**
   * @param pax
   * @param promotion
   * @return boolean
   */
  protected boolean isGoalSelected( Participant pax, Promotion promotion )
  {
    boolean isGoalSelected = false;
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );
    if ( paxGoal != null && paxGoal.getGoalLevel() != null )
    {
      isGoalSelected = true;
    }
    return isGoalSelected;
  }

  protected long getGoalquestPartnerEmailMessageId()
  {
    long messageId = 0;
    Message message = this.messageService.getMessageByCMAssetCode( MessageService.GOAL_QUEST_PARTNER_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE );
    if ( message != null )
    {
      messageId = message.getId().longValue();
    }
    return messageId;
  }

  protected long sendPartnerLaunchMessage( long messageId, GoalQuestPromotion promotion )
  {
    long mailingRecipientCount = 0;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    GoalQuestPromotion hydratedgqPromo = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotion.getId(), associationRequestCollection );
    Set paxs = new HashSet();
    // Retrieves a list of partner audience participants who are eligible for the given
    // promotion.
    Set partnerAudienceSet = hydratedgqPromo.getPromotionPartnerAudiences();
    Set audienceSet = new HashSet();
    if ( partnerAudienceSet != null && partnerAudienceSet.size() > 0 )
    {
      for ( Iterator iter = partnerAudienceSet.iterator(); iter.hasNext(); )
      {
        PromotionPartnerAudience promoPartnerAudience = (PromotionPartnerAudience)iter.next();
        audienceSet.add( promoPartnerAudience.getAudience() );

      }
    }

    Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    paxs.addAll( listBuilderService.searchParticipants( audienceSet, primaryHierarchyId, false, null, true ) );

    // Map promoObjectMap = new HashMap();
    // setPersonalizationData( promoObjectMap, promotion );

    List allUsers = new ArrayList();
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Participant pax = getParticipant( iter.next() );
      if ( !pax.isActive().booleanValue() )
      {
        continue;
      }
      allUsers.add( pax );
      ++mailingRecipientCount;
    }

    boolean sampleEmailSent = false;
    File file = null;
    for ( Iterator iter = allUsers.iterator(); iter.hasNext(); )
    {
      try
      {
        Participant pax = (Participant)iter.next();
        if ( !isNotice() || isNotice() && !sampleEmailSent )
        {
          // BugFix for commentt#4 of 20565.welcome email for partner should send every time when
          // this process runs.
          MailingRecipient mailingRecipient = getMailingRecipient( pax );
          if ( isNotice() && !sampleEmailSent )
          {
            file = extractFile( allUsers, BEAN_NAME );
          }
          Map paxObjectMap = new HashMap();
          setPersonalizationDataWithLocale( paxObjectMap, promotion, pax );
          mailingRecipient.addMailingRecipientDataFromMap( paxObjectMap );
          sendPartnerLaunchMessage( mailingRecipient, messageId, promotion, paxObjectMap, file );
        }
      }
      catch( Exception e )
      {
        String message = "An exception occurred while sending GoalQuest Welcome Partner Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")";
        log.error( message, e );
        if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
        {
          addComment( message );
        }
      }
    }

    return mailingRecipientCount;
  }

  protected Participant getParticipant( Object paxTypeUnknown )
  {
    Participant participant = null;
    // paxs might be AudienceParticipant objects or Participants or FormattedValueBeans
    if ( paxTypeUnknown instanceof Participant )
    {
      participant = (Participant)paxTypeUnknown;
    }
    else if ( paxTypeUnknown instanceof AudienceParticipant )
    {
      participant = ( (AudienceParticipant)paxTypeUnknown ).getParticipant();
    }
    else if ( paxTypeUnknown instanceof FormattedValueBean )
    {
      participant = participantService.getParticipantById( ( (FormattedValueBean)paxTypeUnknown ).getId() );
    }

    return participant;
  }

  protected void sendPartnerLaunchMessage( MailingRecipient recipient, long messageId, GoalQuestPromotion promo, Map promoObjectMap, File file )
  {
    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );
    // Add pax personalization data to the objectMap
    Map paxObjectMap = new HashMap( promoObjectMap );
    setPartnerPersonalizationData( paxObjectMap, promo, recipient.getUser() );
    if ( isNotice() )
    {
      recipient = getMailingRecipient( getRunByUser() );
    }
    // Set the recipients on the mailing
    mailing.addMailingRecipient( recipient );
    if ( file != null )
    {
      String absPath = file.getAbsolutePath();
      String fileName = file.getName();
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
    }
    mailingService.submitMailing( mailing, paxObjectMap );
  }

  protected void setPartnerPersonalizationData( Map objectMap, GoalQuestPromotion promotion, User participant )
  {
    String locale = participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode();
    objectMap.put( "firstName", participant.getFirstName() );
    // BugFix 21054,pre poluate lastName as some templates might use this values in future.
    objectMap.put( "lastName", participant.getLastName() );
    if ( promotion.getPromoNameAssetCode() != null )
    {
      objectMap.put( "programName", getPromotionName( promotion.getPromoNameAssetCode(), locale ) );
    }
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    objectMap.put( "siteLink", siteLink );
    objectMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    objectMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(), LocaleUtils.getLocale( locale ) ) );
    objectMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(), LocaleUtils.getLocale( locale ) ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotEndDate, LocaleUtils.getLocale( locale ) ) );
    }
  }

  public int processProActive( Promotion promotion, PromotionNotificationType promotionNotificationType, boolean individualLaunch, boolean sendNotification )
  {
    Date now = new Date();
    /*
     * int totalPromoLaunchPaxCount = 0; int totalPromoEndPaxCount = 0; int totalInactivePaxCount =
     * 0; int totalApproverReminderPaxCount = 0; int totalGoalNotSelectedPaxCount = 0; int
     * totalChallengepointNotSelectedCount = 0; int totalBudgetSweepPaxCount = 0; int
     * totalBudgetEndPaxCount = 0; int totalManagerNonResponseCount = 0; int
     * totalContributorNonResponseCount = 0;
     */
    long messageId = promotionNotificationType.getNotificationMessageId();

    // Process only when a notification has been set up on the promotion
    if ( messageId > 0 )
    {
      String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "process :" + BEAN_NAME );
      log.debug( "promotion id:" + promotion.getId() );
      log.debug( "promotion status:" + promotion.isComplete() );
      log.debug( "notification type:" + notificationTypeCode );

      /* Promotion Program Launch Alert - send to Pax X days prior to the promotion starts */

      // Notification Type : Promotion Program Launch Alert
      // Promotion Status : Complete
      if ( ( notificationTypeCode.equals( PromotionEmailNotificationType.PROGRAM_LAUNCH ) && promotion.isComplete() )
          || ( promotion.getPromotionType().getCode().equals( PromotionType.SURVEY ) && promotionNotificationType.getNumberOfDays() == 0 )
              && notificationTypeCode.equals( PromotionEmailNotificationType.PROGRAM_LAUNCH ) )
      {
        int promoLaunchPaxCount = 0;

        // Promotion Launch Notification date : X days prior to the promotion starts
        Date programLaunchNotificationDate = new Date( promotion.getSubmissionStartDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
        // Bug Fix # 23733
        // DST may throw off the result by one hour
        programLaunchNotificationDate = com.biperf.core.utils.DateUtils.round( programLaunchNotificationDate );
        promotion.setNumberOfDays( promotionNotificationType.getNumberOfDays() );
        // Send Notification if current date is the notification date
        log.debug( "promotionNotification Date-->" + programLaunchNotificationDate );
        if ( ( promotion.isComplete() && DateUtils.isSameDay( now, programLaunchNotificationDate ) )
            || ( DateUtils.isSameDay( now, programLaunchNotificationDate ) && promotion.getPromotionType().getCode().equals( "survey" ) && promotionNotificationType.getNumberOfDays() == 0 ) )
        {
          Set paxs = new HashSet();
          log.debug( "promotionLaunch Notification" );
          // Retrieves a list of primary audience participants who are eligible for the given
          // promotion.
          paxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );

          // If Goalquest Promotion, send partner welcome email also part of the Program launch
          // email
          if ( promotion.isGoalQuestPromotion() )
          {
            // Send Program Launch Email to paxss
            promoLaunchPaxCount = sendGoalquestLaunchMessage( paxs, messageId, (GoalQuestPromotion)promotion );
            // BugFix 20565..send partner welcome email also part of the Program launch email.
            // send partner Welcome email message
            long partnerMessageId = getGoalquestPartnerEmailMessageId();
            if ( ( (GoalQuestPromotion)promotion ).getPartnerAudienceType() != null && partnerMessageId > 0 )
            {
              long launchPartnerRecipientCount = sendPartnerLaunchMessage( partnerMessageId, (GoalQuestPromotion)promotion );
              promoLaunchPaxCount += launchPartnerRecipientCount;
              // Log Comment to process invocation
              String message = "Promotion: " + promotion.getName() + ". Number of Partner recipients attempted for Goalquest Partner Welcome Email : " + launchPartnerRecipientCount;
              log.debug( message );

              // PID 45107 - Survey report
              // This process generates any email only for GQ promotion with link attached to
              // all participants after the promotion is completed.

            }
          }
          else if ( promotion.isChallengePointPromotion() )
          {
            log.debug( "before sending cp launch message" );
            // Send Program Launch Email to paxs
            promoLaunchPaxCount = sendChallengepointLaunchMessage( paxs, messageId, (ChallengePointPromotion)promotion );
          }
          else if ( promotion.isThrowdownPromotion() )
          {
            log.debug( "before sending throwdown launch message" );
            // get the matches and trigger email for participant who play the match(competitor
            // audience).
            int roundNumber = 1;
            List<Match> matchList = getMatchDAO().getMatchesByPromotionAndRoundNumber( promotion.getId(), roundNumber );
            // Send Program Launch Email to paxs
            promoLaunchPaxCount = sendThrowdownLaunchMessage( matchList, messageId, (ThrowdownPromotion)promotion );
            throwdownPromoLaunchPaxCount = promoLaunchPaxCount;
            throwdownPromoLaunchManagerCount = sendThrowdownPromoManagerLaunchMessage( matchList, (ThrowdownPromotion)promotion );

          }
          else
          {
            Set mailingRecipients = createMailingRecipients( paxs );
            // Send Program Launch Email to paxs
            sendLaunchMessage( mailingRecipients, messageId, promotion );
            promoLaunchPaxCount = mailingRecipients.size();
          }
          if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
          {
            // Add Comment to process invocation
            addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Program Launch Alert: " + promoLaunchPaxCount );
          }

          log.debug( "Number of recipients attempted for Program Launch Alert: " + promoLaunchPaxCount );
        }

        totalPromoLaunchPaxCount += promoLaunchPaxCount;
        return totalPromoLaunchPaxCount;
      }

      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.PARTNER_SELECTED ) && promotion.isLive() )
      {
        int selectedPartnerCount = 0;

        if ( now.after( ( (GoalQuestPromotion)promotion ).getGoalCollectionEndDate() ) )
        {
          List<ParticipantPartner> paxs = new ArrayList<ParticipantPartner>();

          paxs.addAll( promotionService.getParticipantPartnersWhereSelectionEmailNotSentByPromotion( promotion.getId() ) );

          for ( Iterator iter1 = paxs.iterator(); iter1.hasNext(); )
          {
            ParticipantPartner participantPartner = (ParticipantPartner)iter1.next();
            PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participantPartner.getParticipant().getId() );

            if ( paxGoal != null )
            {
              paxGoalService.sendPartnerGoalSelectedEmailNotification( paxGoal, participantPartner.getPartner() );
              participantPartner.setPartnerEmailSent( true );
              selectedPartnerCount++;
              goalQuestService.saveParticipantPartnerAssoc( participantPartner );
            }
          }
          log.debug( "Number of recipients attempted for Program Launch Alert: " + selectedPartnerCount );
        }

        totalPartnerPaxCount += selectedPartnerCount;

        return totalPartnerPaxCount;
      }

      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.CP_PARTNER_GOAL_SELECTED ) && promotion.isLive() )
      {
        int selectedPartnerCount = 0;

        if ( now.after( ( (GoalQuestPromotion)promotion ).getGoalCollectionEndDate() ) )
        {
          List<ParticipantPartner> paxs = new ArrayList<ParticipantPartner>();

          paxs.addAll( promotionService.getParticipantPartnersWhereSelectionEmailNotSentByPromotion( promotion.getId() ) );

          for ( Iterator iter1 = paxs.iterator(); iter1.hasNext(); )
          {
            ParticipantPartner participantPartner = (ParticipantPartner)iter1.next();
            PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participantPartner.getParticipant().getId() );

            if ( paxGoal != null )
            {
              paxGoalService.sendCPPartnerGoalSelectedEmailNotification( paxGoal, participantPartner.getPartner() );
              participantPartner.setPartnerEmailSent( true );
              selectedPartnerCount++;
              goalQuestService.saveParticipantPartnerAssoc( participantPartner );
            }
          }
          log.debug( "Number of recipients attempted for Challengepoint Partner Selected Alert: " + selectedPartnerCount );
        }

        totalPartnerPaxCount += selectedPartnerCount;

        return totalPartnerPaxCount;
      }

      /* Promotion Program End Alert - send to Pax X days prior to the promotion ends */

      // Notification Type : Promotion Program End Alert
      // Promotion Status : Live
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.PROGRAM_END ) && promotion.isLive() )
      {
        int promoEndPaxCount = 0;

        // Promotion End Date should be specified
        if ( promotion.getSubmissionEndDate() != null )
        {
          // Promotion End Notification date : X days prior to the promotion starts
          Date programEndNotificationDate = new Date( promotion.getSubmissionEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );

          // Send Notification if current date is the notification date
          if ( DateUtils.isSameDay( now, programEndNotificationDate ) )
          {
            Set paxs = new HashSet();
            // Retrieves a list of primary participants who are eligible for the given
            // promotion.
            if ( promotion.isThrowdownPromotion() )
            {
              paxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), false ) );
            }
            else
            {
              paxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );
            }
            Set mailingRecipients = createMailingRecipients( paxs );

            // If Goalquest Promotion, remove PAX who did not select a Goal
            // Bug Fix 20517.Filter the paxes who has selected goal already!
            if ( promotion.isGoalQuestPromotion() && mailingRecipients.size() > 0 )
            {
              for ( Iterator iter = mailingRecipients.iterator(); iter.hasNext(); )
              {
                Participant participant = (Participant) ( (MailingRecipient)iter.next() ).getUser();
                if ( !participant.isActive().booleanValue() || !isGoalSelected( participant, promotion ) )
                {
                  iter.remove();
                }
              }
            }

            if ( promotion.isChallengePointPromotion() && mailingRecipients.size() > 0 )
            {
              for ( Iterator iter = mailingRecipients.iterator(); iter.hasNext(); )
              {
                Participant participant = (Participant) ( (MailingRecipient)iter.next() ).getUser();
                if ( !participant.isActive().booleanValue() || !challengePointService.isChallengepointSelected( participant, promotion ) )
                {
                  iter.remove();
                }
              }
            }
            promoEndPaxCount = mailingRecipients.size();
            // Send Program End Email to paxs
            sendEndMessage( mailingRecipients, messageId, promotion );
            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Program End Alert: " + promoEndPaxCount );
            }

            log.debug( "Number of recipients attempted for Program End Alert: " + promoEndPaxCount );
          }
        }

        totalPromoEndPaxCount += promoEndPaxCount;
        return totalPromoEndPaxCount;
      }
      /* Promotion Inactivity Alert - send to Pax X days of 'inactivity' */

      // Notification Type : Promotion Inactivity Alert
      // Promotion Status : Live
      else if ( ( notificationTypeCode.equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY )
          || notificationTypeCode.equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_RECOGNITION )
          || notificationTypeCode.equals( PromotionEmailNotificationType.PARTICIPANT_INACTIVITY_NOMINATION ) ) && promotion.isLive() )
      {
        boolean isPromotionInactivityAlert = true;
        if ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() )
        {
          if ( promotion.getBudgetMaster() != null )
          {
            for ( BudgetSegment budgetSegment : promotion.getBudgetMaster().getBudgetSegments() )
            {
              if ( budgetSegment.getEndDate() != null
                  && ( budgetSegment.getEndDate().after( com.biperf.core.utils.DateUtils.getCurrentDate() ) || budgetSegment.getEndDate().equals( com.biperf.core.utils.DateUtils.getCurrentDate() ) ) )
              {
                isPromotionInactivityAlert = true;
                break;
              }
              isPromotionInactivityAlert = false;
            }
          }
        }

        int inactivePaxCount = 0;

        // Only run on non-child promos
        boolean isChildPromotion = false;
        if ( promotion.isProductClaimPromotion() && ( (ProductClaimPromotion)promotion ).hasParent() )
        {
          isChildPromotion = true;
        }

        // Per bugfix 20643,Do not send inactivity emails when the promotion end date is passed
        // already.
        if ( !isChildPromotion && ! ( promotion.getSubmissionEndDate() != null && promotion.getSubmissionEndDate().before( now ) )
            && ( DateUtils.isSameDay( now, promotion.getSubmissionStartDate() ) || promotion.getSubmissionStartDate().before( now ) ) && promotionNotificationType != null
            && promotionNotificationType.getNumberOfDays() != null && isPromotionInactivityAlert )
        {
          Date pastDate = new Date( now.getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
          // BugFix 17561 Oracle Query between date range is giving exclusive results on end
          // date,
          // so add one more day to 'now' instance to get the paxs who has done activity on this
          // current day.
          Date nextToNow = new Date( now.getTime() + DateUtils.MILLIS_PER_DAY );
          if ( isEligibleforNotification( promotionNotificationType, individualLaunch ) )
          {
            // Send Program Inactivity Email to paxs
            inactivePaxCount = participantInactivity( messageId, promotion, pastDate, nextToNow, sendNotification );
            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Program Inactivity Alert: " + inactivePaxCount );
            }

            log.debug( "Number of recipients attempted for Program Inactivity Alert: " + inactivePaxCount );
          }
        }

        totalInactivePaxCount += inactivePaxCount;
        return totalInactivePaxCount;
      }
      /* Approver Reminder - send to Approver Pax reminding to approve claim submissions */

      // Notification Type : Approver Reminder
      // Promotion Status : Live
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.NOMINATION_APPROVER_NOTIFICATION ) )
      {
        int approverNotificationCount = 0;

        if ( DateUtils.isSameDay( new Date(), promotion.getSubmissionStartDate() ) )
        {
          approverNotificationCount = approverNotification( messageId, promotion, promotionNotificationType, individualLaunch, sendNotification );
          if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
          {
            // Add Comment to process invocation
            addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Approver Reminder Email: " + approverNotificationCount );
          }

          log.debug( "Number of recipients attempted for Approver Reminder Alert: " + approverNotificationCount );
        }
        totalApproverNotificationCount += approverNotificationCount;
        return totalApproverNotificationCount;
      }
      /* Approver Reminder - send to Approver Pax reminding to approve claim submissions */

      // Notification Type : Approver Reminder
      // Promotion Status : Live
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.APPROVER_REMINDER ) && promotion.isLive() )
      {
        log.error( "***** APPROVER_REMINDER start for promo_id:" + promotion.getId() );
        int approverReminderPaxCount = 0;
        // if the live promotion is not automatic approval types
        // and approval end date (if any) has not reached, send reminder email to approvers
        if ( !promotion.getApprovalType().isAutomaticApproved() )
        {
          if ( isEligibleforNotification( promotionNotificationType, individualLaunch ) )
          {
            // Send Approver Reminder Email to paxs
            approverReminderPaxCount = approverReminder( messageId, promotion, promotionNotificationType, individualLaunch, sendNotification );

            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Approver Reminder Email: " + approverReminderPaxCount );
            }
          }
          log.debug( "Number of recipients attempted for Approver Reminder Alert: " + approverReminderPaxCount );
        }

        totalApproverReminderPaxCount += approverReminderPaxCount;
        log.error( "***** APPROVER_REMINDER end for promo_id:" + promotion.getId() + " approverReminderPaxCount=" + approverReminderPaxCount );
        return totalApproverReminderPaxCount;
      }
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.APPROVER_REMINDER_APPROVAL_END_DATE ) && promotion.isLive() )
      {
        int approverReminderEndDatePaxCount = 0;
        // if the live promotion is not automatic approval types
        // and approval end date (if any) has not reached, send reminder email to approvers
        if ( !promotion.getApprovalType().isAutomaticApproved() && promotion.isNominationPromotion() && promotion.getApprovalEndDate() != null )
        {

          Date promoApprovalEndDate = new Date( promotion.getApprovalEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );

          if ( DateUtils.isSameDay( now, promoApprovalEndDate ) )
          {
            // Send Approver Reminder Email to paxs
            approverReminderEndDatePaxCount = approverReminderByEndDate( messageId, promotion, promotionNotificationType, individualLaunch, sendNotification );

            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Approver Reminder Email: " + approverReminderEndDatePaxCount );
            }
            log.debug( "Number of recipients attempted for Approver Reminder Alert: " + approverReminderEndDatePaxCount );
          }

        }

        totalApproverReminderEndDatePaxCount += approverReminderEndDatePaxCount;
        return totalApproverReminderEndDatePaxCount;
      }
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.APPROVER_REMINDER_TIME_PERIOD_EXPIRED ) && promotion.isLive() )
      {
        int approverReminderEndDatePaxCount = 0;
        // if the live promotion is not automatic approval types
        // and approval end date (if any) has not reached, send reminder email to approvers
        if ( !promotion.getApprovalType().isAutomaticApproved() && promotion.isNominationPromotion() )
        {
          NominationPromotion nomPromo = (NominationPromotion)promotion;
          for ( NominationPromotionTimePeriod timePeriod : nomPromo.getNominationTimePeriods() )
          {
            Date timePeriodEndDate = new Date( timePeriod.getTimePeriodEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
            timePeriodEndDate = com.biperf.core.utils.DateUtils.round( timePeriodEndDate );

            // Send Notification if current date is the notification date
            log.debug( "promotionNotification Date-->" + timePeriodEndDate );
            if ( DateUtils.isSameDay( now, timePeriodEndDate ) )
            {
              approverReminderEndDatePaxCount = approverReminderByTimePeriodEnd( messageId, promotion, promotionNotificationType, individualLaunch, sendNotification );
            }
          }
          // Send Approver Reminder Email to paxs
          if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
          {
            // Add Comment to process invocation
            addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Approver Reminder Email: " + approverReminderEndDatePaxCount );
          }

          log.debug( "Number of recipients attempted for Approver Reminder Alert: " + approverReminderEndDatePaxCount );
        }

        totalApproverReminderEndDatePaxCount += approverReminderEndDatePaxCount;
        return totalApproverReminderEndDatePaxCount;
      }
      /*
       * Purl Manager Non Response - send to Owner X days prior to the purl Recipient award date
       * ends
       */

      // Notification Type : Purl Manager NonResponse
      // Promotion Status : Live
      else if ( promotion.isRecognitionPromotion() && notificationTypeCode.equals( PromotionEmailNotificationType.PURL_MANAGER_NONRESPONSE ) && promotion.isLive() )
      {
        int managerNonResponseCount = 0;
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          if ( isEligibleforNotification( promotionNotificationType, individualLaunch ) )
          {
            managerNonResponseCount = purlManagerNonResponse( messageId, promotion, promotionNotificationType, sendNotification );

            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Manager Non Response Email: " + managerNonResponseCount );
            }

            log.debug( "Number of recipients attempted for Manager Non Response Alert: " + managerNonResponseCount );
          }
        }
        totalManagerNonResponseCount += managerNonResponseCount;
        return totalManagerNonResponseCount;
      }
      /*
       * Purl Contributor Non Response - send to Owner X days prior to the purl Recipient award date
       * ends
       */

      // Notification Type : Purl Contributor NonResponse
      // Promotion Status : Live
      else if ( promotion.isRecognitionPromotion() && notificationTypeCode.equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_NONRESPONSE ) && promotion.isLive() )
      {
        int contributorNonResponseCount = 0;
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          if ( isEligibleforNotification( promotionNotificationType, individualLaunch ) )
          {
            contributorNonResponseCount = purlContributorNonResponse( messageId, promotion, promotionNotificationType, sendNotification );
            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Purl Contributor Non Response Email: " + contributorNonResponseCount );
            }
            log.debug( "Number of recipients attempted for Purl Contributor Non Response Alert: " + contributorNonResponseCount );
          }
        }
        totalContributorNonResponseCount += contributorNonResponseCount;
        return totalContributorNonResponseCount;
      }
      /* Goal Not Selected - send to Pax reminding to select a goal */

      // Notification Type : Goal Not Selected Reminder
      // Promotion Status : Live
      // Promotion Type : Goalquest
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.GOAL_NOT_SELECTED ) && promotion.isLive() && promotion.isGoalQuestPromotion() )
      {
        int goalNotSelectedPaxCount = 0;
        GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;

        // Goal Selection End Date should be specified
        if ( goalQuestPromotion.getGoalCollectionEndDate() != null )
        {
          // Goal Not Selected Notification date : X days prior to the Goal Selection End
          Date notificationDate = new Date( goalQuestPromotion.getGoalCollectionEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );

          // Send Notification if current date is the notification date
          if ( DateUtils.isSameDay( now, notificationDate ) )
          {
            // Send Goal Not Selected Email to paxs
            goalNotSelectedPaxCount = goalNotSelected( messageId, promotion );

            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Goal Not Selected Email: " + goalNotSelectedPaxCount );
            }

            log.debug( "Number of recipients attempted for Goal Not Selected Alert: " + goalNotSelectedPaxCount );
          }
        }

        totalGoalNotSelectedPaxCount += goalNotSelectedPaxCount;
        return totalGoalNotSelectedPaxCount;
      }
      /* CP Not Selected - send to Pax reminding to select a cp */

      // Notification Type : Challengepoint Not Selected
      // Promotion Status : Live
      // Promotion Type : Challengepoint
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.CHALLENGEPOINT_NOT_SELECTED ) && promotion.isLive() && promotion.isChallengePointPromotion() )
      {
        int challengepointNotSelectedCount = 0;
        ChallengePointPromotion challengepointPromotion = (ChallengePointPromotion)promotion;
        if ( challengepointPromotion.getGoalCollectionEndDate() != null )
        {
          Date notificationDate = new Date( challengepointPromotion.getGoalCollectionEndDate().getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
          if ( DateUtils.isSameDay( now, notificationDate ) )
          {
            // Send Goal Not Selected Email to paxs
            challengepointNotSelectedCount = challengepointNotSelected( messageId, promotion );

            if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
            {
              // Add Comment to process invocation
              addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Goal Not Selected Email: " + challengepointNotSelectedCount );
            }

            log.debug( "Number of recipients attempted for Goal Not Selected Alert: " + challengepointNotSelectedCount );
          }
        }
        totalChallengepointNotSelectedCount += challengepointNotSelectedCount;
        return totalChallengepointNotSelectedCount;
      }
      /* Spotlight online non-redemption email */

      // Notification Type : Spotlight online non-redemption
      // Promotion Status : Live
      // Promotion Type : Recognition
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.NON_REDEMPTION_REMINDER ) && promotion.isLive() && promotion.isRecognitionPromotion() )
      {
        try
        {
          if ( isEligibleforNotification( promotionNotificationType, individualLaunch ) )
          {
            return sendRedemptionNotification( promotion, promotionNotificationType, sendNotification );
          }
        }
        catch( ServiceErrorException e )
        {
          log.error( e.getMessage(), e );
        }
      }
      /* GoalQuest non-redemption email */

      // Notification Type : GoalQuest non-redemption
      // Promotion Status : Live
      // Promotion Type : GoalQuest
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.GQ_NON_REDEMPTION_REMINDER ) && promotion.isLive() && promotion.isGoalQuestPromotion() )
      {
        try
        {
          return sendGoalQuestRedemptionNotification( promotion, promotionNotificationType );
        }
        catch( ServiceErrorException e )
        {
          log.error( e.getMessage(), e );
        }
      }

      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.CP_NON_REDEMPTION_REMINDER ) && promotion.isLive() && promotion.isChallengePointPromotion() )
      {
        try
        {
          return sendGoalQuestRedemptionNotification( promotion, promotionNotificationType );
        }
        catch( ServiceErrorException e )
        {
          log.error( e.getMessage(), e );
        }
      }
      /* Budget Sweep Alert - send to Owner X days prior to the Budget Sweep Date */

      // Notification Type : Budget Sweep Alert
      // Promotion Status : Live
      // Promotion Type : Recognition
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.BUDGET_SWEEP ) && promotion.isLive() && promotion.isRecognitionPromotion() )
      {
        int budgetSweepPaxCount = 0;
        RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;

        // Budget Sweep on the Promotion should be enabled
        if ( promoRecognition.isBudgetSweepEnabled() )
        {
          // Budget Sweep Notification date : X days prior to the Budget Sweep
          BudgetMaster budgetMaster = promoRecognition.getBudgetMaster();
          if ( budgetMaster.isActive() && budgetMaster.getBudgetType().isPaxBudgetType() )
          {
            Set<BudgetSegment> budgetSegments = budgetMaster.getBudgetSegments();
            for ( BudgetSegment budgetSegment : budgetSegments )
            {
              Date budgetSweepNotificationDate = null;
              Date budgetSweepDate = null;
              if ( budgetSegment.getPromotionBudgetSweeps() != null && budgetSegment.getPromotionBudgetSweeps().size() > 0 )
              {
                budgetSweepDate = budgetSegment.getPromotionBudgetSweeps().iterator().next().getBudgetSweepDate();
                budgetSweepNotificationDate = new Date( budgetSweepDate.getTime() - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
              }

              if ( budgetSweepNotificationDate != null )
              {
                // Current Date should be same as Budget Sweep Date
                if ( DateUtils.isSameDay( now, budgetSweepNotificationDate ) )
                {
                  Map paxBudgetMap = getPaxBudgetMap( budgetSegment );
                  Set paxs = new HashSet();
                  paxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );
                  Set mailingRecipients = createMailingRecipients( paxs );
                  // Send Budget Sweep Email to paxs
                  budgetSweepPaxCount = sendBudgetSweepMessage( mailingRecipients, messageId, promoRecognition, paxBudgetMap, budgetSweepDate );
                  if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
                  {
                    // Add Comment to process invocation
                    addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Budget Sweep Alert: " + budgetSweepPaxCount );
                  }

                  log.debug( "Number of recipients attempted for Budget Sweep Alert: " + budgetSweepPaxCount );
                }
                totalBudgetSweepPaxCount += budgetSweepPaxCount;
              }
            }
          }
        }

        return totalBudgetSweepPaxCount;
      }

      /* Budget End Alert - send to Owner X days prior to the Budget End Date */

      // Notification Type : Budget End Alert
      // Promotion Status : Live
      // Promotion Type : Recognition
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.BUDGET_END ) && promotion.isLive() && promotion.isRecognitionPromotion()
          && ( promotion.getSubmissionEndDate() == null || ( promotion.getSubmissionEndDate() != null && now.before( promotion.getSubmissionEndDate() ) ) ) )
      {
        int budgetEndPaxCount = 0;
        RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;

        if ( isEligibleforNotification( promotionNotificationType, individualLaunch ) )
        {

          if ( promoRecognition.getBudgetMaster() != null && promoRecognition.isBudgetUsed() )
          {
            Set allElibigiblePaxs = new HashSet();
            allElibigiblePaxs.addAll( participantService.getAllEligiblePaxForPromotion( promotion.getId(), true ) );
            Long budgetMasterId = promoRecognition.getBudgetMaster().getId();
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
            BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( budgetMasterId, associationRequestCollection );
            Map budgetMap = new HashMap();
            Set paxSet = new HashSet();
            if ( budgetMaster.isActive() )
            {
              // Business Requirement to use system time zone instead of pax time zone for improved
              // performance and since Budget Reminder
              // should be scheduled to send few days before the budget end date
              String systemTimeZoneID = claimService.getDBTimeZone();
              BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( systemTimeZoneID );

              // Load segment with budgets
              if ( currentBudgetSegment != null )
              {
                AssociationRequestCollection budgetSegmentAssociationRequestCollection = new AssociationRequestCollection();
                budgetSegmentAssociationRequestCollection.add( new BudgetSegmentToBudgetsAssociationRequest() );
                currentBudgetSegment = budgetMasterService.getBudgetSegmentById( currentBudgetSegment.getId(), budgetSegmentAssociationRequestCollection );
              }
              if ( currentBudgetSegment != null && currentBudgetSegment.getStatus() )
              {
                Set budgets = currentBudgetSegment.getBudgets();
                if ( budgets != null && budgets.size() > 0 )
                {
                  Iterator iter = budgets.iterator();
                  while ( iter.hasNext() )
                  {
                    Budget tmpBudget = (Budget)iter.next();
                    if ( tmpBudget.getBudgetSegment().getEndDate() != null )
                    {
                      // Budget End Notification date : X days prior to the Budget End
                      Date budgetEndNotificationDate = new Date( tmpBudget.getBudgetSegment().getEndDate().getTime()
                          - promotionNotificationType.getNumberOfDays().longValue() * DateUtils.MILLIS_PER_DAY );
                      // Current Date should be between budgetEndNotificationDate and budget end
                      // date
                      if ( com.biperf.core.utils.DateUtils.isTodaysDateBetween( budgetEndNotificationDate, tmpBudget.getBudgetSegment().getEndDate() ) )
                      {
                        if ( budgetMaster.isParticipantBudget() )
                        {
                          if ( null != tmpBudget.getUser() && BudgetStatusType.ACTIVE.equals( tmpBudget.getStatus().getCode() ) && tmpBudget.getOriginalValueDisplay() > 0
                              && tmpBudget.getCurrentValueDisplay() > 0 )
                          {
                            for ( Iterator paxIter = allElibigiblePaxs.iterator(); paxIter.hasNext(); )
                            {
                              Participant participant = (Participant)paxIter.next();
                              if ( tmpBudget.getUser().getId().equals( participant.getId() ) )
                              {
                                budgetMap.put( tmpBudget.getUser().getId(), tmpBudget );
                                paxSet.add( participant );
                              }
                            }

                          }
                        }
                        else if ( budgetMaster.isNodeBudget() )
                        {
                          if ( null != tmpBudget.getNode() && BudgetStatusType.ACTIVE.equals( tmpBudget.getStatus().getCode() ) && tmpBudget.getOriginalValueDisplay() > 0
                              && tmpBudget.getCurrentValueDisplay() > 0 )
                          {
                            AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
                            nodeAssociationRequestCollection.add( new NodeToUsersAssociationRequest() );
                            Node node = nodeService.getNodeWithAssociationsById( tmpBudget.getNode().getId(), nodeAssociationRequestCollection );
                            List usersInNode = node.getActiveUserList();
                            for ( Iterator userIter = usersInNode.iterator(); userIter.hasNext(); )
                            {
                              User user = (User)userIter.next();
                              for ( Iterator paxIter = allElibigiblePaxs.iterator(); paxIter.hasNext(); )
                              {
                                Participant participant = (Participant)paxIter.next();
                                Long userId = user.getId();
                                Long paxId = participant.getId();
                                if ( userId == null && paxId == null || userId != null && userId.equals( paxId ) )
                                {
                                  Long nodeId = node.getId();
                                  Long tmpBudgetNodeId = tmpBudget.getNode().getId();
                                  if ( nodeId == null && tmpBudgetNodeId == null || nodeId != null && nodeId.equals( tmpBudgetNodeId ) )
                                  {
                                    budgetMap.put( user.getId(), tmpBudget );
                                    paxSet.add( participant );
                                  }
                                }
                              }
                            }
                          }
                        }
                        else if ( budgetMaster.isCentralBudget() )
                        {
                          if ( BudgetStatusType.ACTIVE.equals( tmpBudget.getStatus().getCode() ) && tmpBudget.getOriginalValueDisplay() > 0 && tmpBudget.getCurrentValueDisplay() > 0 )
                          {
                            for ( Iterator paxIter = allElibigiblePaxs.iterator(); paxIter.hasNext(); )
                            {
                              Participant participant = (Participant)paxIter.next();

                              budgetMap.put( participant.getId(), tmpBudget );
                              paxSet.add( participant );

                            }
                          }
                        }
                      }
                    }
                  }

                  if ( !sendNotification )
                  {
                    return allElibigiblePaxs.size();
                  }
                  // Send Budget End Email to paxs
                  if ( sendNotification )
                  {
                    Set mailingRecipients = createMailingRecipients( paxSet );
                    sendBudgetEndMessage( mailingRecipients, messageId, promoRecognition, budgetMap, budgetMaster );
                    budgetEndPaxCount = mailingRecipients.size();
                    totalBudgetEndPaxCount += budgetEndPaxCount;
                    return mailingRecipients.size();
                  }

                  if ( this.getProcessInvocationId() != null && this.getProcessInvocationId() > 0 )
                  {
                    // Add Comment to process invocation
                    addComment( "Promotion: " + promotion.getName() + ". Number of recipients attempted for Budget End Alert: " + budgetEndPaxCount );
                  }

                  log.debug( "Number of recipients attempted for Budget End Alert: " + budgetEndPaxCount );
                }
              }
            }
          }
        }
        totalBudgetEndPaxCount += budgetEndPaxCount;
        return totalBudgetEndPaxCount;
      }
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.TD_NEXT_ROUND ) && ( promotion.isComplete() || promotion.isLive() ) )
      {

        log.debug( "before sending throwdown next round message" );
        // get the matches and trigger email for participant who play the match(competitor
        // audience).
        // Send Program Launch Email to paxs
        totalThrowdownNextRoundPaxCount = sendThrowdownNextRoundMessage( messageId, (ThrowdownPromotion)promotion, promotionNotificationType );
        return totalThrowdownNextRoundPaxCount;

      }
      else if ( notificationTypeCode.equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NONRESPONSE ) && promotion.isLive() && promotion.isRecognitionPromotion() )
      {
        int totalPaxCount = 0;
        RecognitionPromotion promoRecognition = (RecognitionPromotion)promotion;
        boolean includeCelebrations = promoRecognition.isIncludeCelebrations();
        String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
        if ( includeCelebrations )
        {
          List<CelebrationManagerMessage> celebrationManagerMessageList = getCelebrationService().getCelebrationManagerByPromotion( promoRecognition.getId() );
          for ( Iterator messageIter = celebrationManagerMessageList.iterator(); messageIter.hasNext(); )
          {
            CelebrationManagerMessage celebrationManagerMessage = (CelebrationManagerMessage)messageIter.next();
            // send email
            PurlRecipient purlRecipient = getPurlService().getPurlRecipientByCelebrationManagerMessageId( celebrationManagerMessage.getId() );
            int paxCount = sendCelebrationManagerReminderMessage( messageId, celebrationManagerMessage, purlRecipient, promoRecognition, sender, promotionNotificationType );
            totalPaxCount += paxCount;
          }
        }
        addComment( "Celebration Manager Reminder Email Sent for " + totalPaxCount + " recipients - for Promotion: " + promotion.getPromotionName() );
        return totalPaxCount;
      }

      log.debug( "--------------------------------------------------------------------------------" );
    } // end messageId set on the promotion
    return 0;
  }

  protected int sendCelebrationManagerReminderMessage( long messageId,
                                                       CelebrationManagerMessage celebrationManagerMessage,
                                                       PurlRecipient purlRecipient,
                                                       RecognitionPromotion promoRecognition,
                                                       String sender,
                                                       PromotionNotificationType promotionNotificationType )
  {
    int recipientCount = 0;

    Mailing mailing = buildCelebrationsManagerNotificationMailing( messageId, celebrationManagerMessage, purlRecipient, sender, promotionNotificationType );
    recipientCount = mailing != null && mailing.getMailingRecipients() != null ? mailing.getMailingRecipients().size() : 0;
    if ( recipientCount > 0 )
    {
      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, null );

        log.debug( "Number of recipients attempted for Celebration Manager Reminder Email: " + recipientCount );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Celebration Manager Reminder Email for promotion: " + promoRecognition.getName() + " (process invocation ID = " + getProcessInvocationId()
            + ")", e );
        addComment( "An exception occurred while sending Celebration Manager Reminder Email for promotion: " + promoRecognition.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }
    }

    return recipientCount;
  }

  private Mailing buildCelebrationsManagerNotificationMailing( long messageId,
                                                               CelebrationManagerMessage celebrationManagerMessage,
                                                               PurlRecipient purlRecipient,
                                                               String sender,
                                                               PromotionNotificationType promotionNotificationType )
  {
    Set mailingRecipients = buildMailingRecipientsForCelebrationManagerEmail( celebrationManagerMessage, purlRecipient, promotionNotificationType );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Message message = messageService.getMessageById( messageId );
    Mailing mailing = new Mailing();

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( sender );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Set buildMailingRecipientsForCelebrationManagerEmail( CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient, PromotionNotificationType promotionNotificationType )
  {
    Set mailingRecipients = new HashSet();

    Set recipients = new HashSet();
    String managerMessage = celebrationManagerMessage.getManagerMessage();

    Date calculatedDate = new Date( celebrationManagerMessage.getAuditCreateInfo().getDateCreated().getTime() );
    Calendar c = Calendar.getInstance();
    c.setTime( calculatedDate );
    c.add( Calendar.DATE, promotionNotificationType.getNumberOfDays() );
    calculatedDate = c.getTime();

    if ( StringUtils.isEmpty( managerMessage )
        && ( calculatedDate.before( com.biperf.core.utils.DateUtils.getCurrentDate() ) || DateUtils.isSameDay( calculatedDate, com.biperf.core.utils.DateUtils.getCurrentDate() ) ) )
    {
      User recipientManager = celebrationManagerMessage.getManager();
      if ( recipientManager != null )
      {
        recipients.add( recipientManager );
      }
    }
    String managerAboveMessage = celebrationManagerMessage.getManagerAboveMessage();
    if ( StringUtils.isEmpty( managerAboveMessage ) && calculatedDate.before( com.biperf.core.utils.DateUtils.getCurrentDate() ) )
    {
      User managerAbove = celebrationManagerMessage.getManagerAbove();
      if ( managerAbove != null )
      {
        recipients.add( managerAbove );
      }
    }

    for ( Iterator claimRecipientIter = recipients.iterator(); claimRecipientIter.hasNext(); )
    {
      User manager = (User)claimRecipientIter.next();
      MailingRecipient mailingRecipient = buildCelebrationManagerMailingRecipient( manager, celebrationManagerMessage, purlRecipient );
      mailingRecipients.add( mailingRecipient );
    }

    return mailingRecipients;
  }

  private MailingRecipient buildCelebrationManagerMailingRecipient( User sendToUser, CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( sendToUser );
    String localeCode = null;
    if ( sendToUser != null && sendToUser.getLanguageType() != null )
    {
      localeCode = sendToUser.getLanguageType().getCode();
    }
    else
    {
      localeCode = systemVariableService.getDefaultLanguage().getStringVal();
    }
    mailingRecipient.setLocale( localeCode );

    Map dataMap = new HashMap();
    Participant recipient = celebrationManagerMessage.getRecipient();
    RecognitionPromotion promotion = celebrationManagerMessage.getPromotion();

    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "recognition.detail", locale );
    String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_MANAGER_MAIL_SUBJECT" ) ) + " " + recipient.getFirstName() + " "
        + recipient.getLastName();
    dataMap.put( "subject", subjectLine );

    if ( recipient != null )
    {
      dataMap.put( "recipientFirstName", recipient.getFirstName() );
      dataMap.put( "recipientLastName", recipient.getLastName() );
    }

    if ( promotion.isIncludePurl() && purlRecipient != null )
    {
      Date awardDate = purlRecipient.getAwardDate();
      dataMap.put( "awardDate", com.biperf.core.utils.DateUtils.toDisplayString( awardDate, LocaleUtils.getLocale( localeCode ) ) );

      if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
      {
        for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
        {
          dataMap.put( "formElement" + ( i + 1 ), purlRecipient.getCustomElement( i ).getDisplayValue() );
        }
      }

      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() )
        {
          dataMap.put( "anniversaryYear", purlRecipient.getAnniversaryNumberOfYears().toString() );
        }
        else
        {
          dataMap.put( "anniversaryDay", purlRecipient.getAnniversaryNumberOfDays().toString() );
        }
      }
    }
    else
    {
      dataMap.put( "awardDate",
                   com.biperf.core.utils.DateUtils.toDisplayString( getCelebrationDeliveryDate( celebrationManagerMessage.getMsgCollectExpireDate() ), LocaleUtils.getLocale( localeCode ) ) );
      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() )
        {
          dataMap.put( "anniversaryYear", celebrationManagerMessage.getAnniversaryNumberOfYears().toString() );
        }
        else
        {
          dataMap.put( "anniversaryDay", celebrationManagerMessage.getAnniversaryNumberOfDays().toString() );
        }
      }
    }
    if ( sendToUser != null )
    {
      dataMap.put( "managerFirstName", sendToUser.getFirstName() );
      dataMap.put( "managerLastName", sendToUser.getLastName() );
    }

    if ( promotion != null )
    {
      if ( promotion.getPromoNameAssetCode() != null )
      {
        String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
        dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
      }
    }
    dataMap.put( "programWebsiteManagerMessage", getCelebrationManagerMessagePageUrl( celebrationManagerMessage.getId() ) );
    dataMap.put( "managerContributionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( celebrationManagerMessage.getMsgCollectExpireDate(), LocaleUtils.getLocale( localeCode ) ) );
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private String getCelebrationManagerMessagePageUrl( Long managerMessageId )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "managerMessageId", managerMessageId.toString() );
    String celebrationPageUrl = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                      "/celebration/managerMessageCollect.do?method=display",
                                                                      paramMap );
    return celebrationPageUrl;
  }

  private Date getCelebrationDeliveryDate( Date msgCollectExpireDate )
  {
    Date deliveryDate = msgCollectExpireDate != null ? msgCollectExpireDate : new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( deliveryDate );
    cal.add( Calendar.DATE, 1 );
    deliveryDate = cal.getTime();
    return deliveryDate;
  }

  // assuming number of days check alreday done before calling this method
  private boolean isEligibleforNotification( PromotionNotificationType promoNotification, boolean individualLaunch )
  {
    boolean isEligible = false;
    if ( individualLaunch )
    {
      return true;
    }
    PromotionNotificationFrequencyType frequency = promoNotification.getPromotionNotificationFrequencyType();
    Calendar calendar = Calendar.getInstance();

    // null check
    if ( null == frequency )
    {
      return isEligible;
    }
    else if ( frequency.isDaily() )
    {
      return true;
    }
    else if ( frequency.isWeekly() )
    {
      // Is today the day of the week to send?
      Calendar cal = new GregorianCalendar();
      int todayOfWeek = cal.get( Calendar.DAY_OF_WEEK ); // 1=Sunday, 2=Monday, ...
      if ( promoNotification.getDayOfWeekType().getCode().equals( new Integer( todayOfWeek ).toString() ) )
      {
        return true;
      }
    }
    else if ( frequency.isMonthly() )
    {
      // Is today the day of the week to send?
      Calendar cal = new GregorianCalendar();
      int todayOfMonth = cal.get( Calendar.DAY_OF_MONTH );
      if ( promoNotification.getDayOfMonth() == todayOfMonth )
      {
        return true;
      }
    }
    return isEligible;
  }

  private String getPromotionName( String promoNameAssetCode, String userLocale )
  {
    Locale locale = CmsUtil.getLocale( userLocale );
    return cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setPromotionNotificationService( PromotionNotificationService promotionNotificationService )
  {
    this.promotionNotificationService = promotionNotificationService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setClaimGroupService( ClaimGroupService claimGroupService )
  {
    this.claimGroupService = claimGroupService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return merchOrderService;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  private class MailingGiftCodeRecipient
  {
    private MerchOrder merchOrder = null;
    private MailingRecipient recipient = null;

    public MailingGiftCodeRecipient( MailingRecipient recipient, MerchOrder merchOrder )
    {
      this.merchOrder = merchOrder;
      this.recipient = recipient;
    }

    public MailingRecipient getMailingRecipient()
    {
      return this.recipient;
    }

    public MerchOrder getMerchOrder()
    {
      return this.merchOrder;
    }
  }

  private class MailingGoalQuestGiftCodeRecipient
  {
    private MerchOrder merchOrder = null;
    private MailingRecipient recipient = null;

    public MailingGoalQuestGiftCodeRecipient( MailingRecipient recipient, MerchOrder merchOrder )
    {
      this.merchOrder = merchOrder;
      this.recipient = recipient;
    }

    public MailingRecipient getMailingRecipient()
    {
      return this.recipient;
    }

    public MerchOrder getMerchOrder()
    {
      return this.merchOrder;
    }
  }

  protected long getMessageId( String messageCode )
  {
    long messageId = 0;
    Message message = messageService.getMessageByCMAssetCode( messageCode );
    if ( message != null )
    {
      messageId = message.getId().longValue();
    }
    return messageId;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  public PurlService getPurlService()
  {
    return purlService;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

  public DIYQuizService getDiyQuizService()
  {
    return diyQuizService;
  }

  public void setDiyQuizService( DIYQuizService diyQuizService )
  {
    this.diyQuizService = diyQuizService;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public GoalQuestService getGoalQuestService()
  {
    return goalQuestService;
  }

  public void setGoalQuestService( GoalQuestService goalQuestService )
  {
    this.goalQuestService = goalQuestService;
  }

  public TeamService getTeamService()
  {
    return teamService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
  }

  public CelebrationService getCelebrationService()
  {
    return celebrationService;
  }

  public void setCelebrationService( CelebrationService celebrationService )
  {
    this.celebrationService = celebrationService;
  }

  public boolean isNotice()
  {
    return false;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  private RoundDAO getRoundDAO()
  {
    return (RoundDAO)BeanLocator.getBean( RoundDAO.BEAN_NAME );
  }

  private MatchDAO getMatchDAO()
  {
    return (MatchDAO)BeanLocator.getBean( MatchDAO.BEAN_NAME );
  }

  private DivisionDAO getDivisionDAO()
  {
    return (DivisionDAO)BeanLocator.getBean( DivisionDAO.BEAN_NAME );
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setNominationPromotionService( NominationPromotionService nominationPromotionService )
  {
    this.nominationPromotionService = nominationPromotionService;
  }

  public void setMobileNotificationService( MobileNotificationService mobileNotificationService )
  {
    this.mobileNotificationService = mobileNotificationService;
  }

  protected int approverNotification( long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean individualLaunch, boolean sendNotification )
  {
    int recipientCount = 0;

    Mailing mailing = buildApproverNotificationMailing( messageId, promotion );
    recipientCount = mailing != null && mailing.getMailingRecipients() != null ? mailing.getMailingRecipients().size() : 0;
    if ( recipientCount > 0 )
    {
      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, null );

        log.debug( "Number of recipients attempted for Approver Notification Email: " + recipientCount );
        addComment( "Approver Notification Email Sent for " + recipientCount + " recipients - for Promotion: " + promotion.getName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Approver Notification Email for promotion: " + promotion.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while sending Approver Notification Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }
    }

    return recipientCount;
  }

  private Mailing buildApproverNotificationMailing( long messageId, Promotion promotion )
  {
    List<Long> paxIds = participantService.getAllEligibleApproversForCustomApproval( promotion.getId() );
    List<Participant> approvers = new ArrayList<Participant>();
    for ( Iterator<Long> iter = paxIds.iterator(); iter.hasNext(); )
    {
      Long paxId = iter.next();
      Participant participant = participantService.getParticipantById( paxId );
      approvers.add( participant );
    }
    Set mailingRecipients = buildNominationApproverNotifications( approvers, promotion.getName() );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Message message = messageService.getMessageById( messageId );
    Mailing mailing = new Mailing();

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "SYSTEM" );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  // Nomination Approver Notification
  private Set buildNominationApproverNotifications( List approvers, String promotionName )
  {
    Set approversRecipientsSet = new HashSet();

    // build approver recipient list
    Iterator approverIter = approvers.iterator();
    while ( approverIter.hasNext() )
    {
      MailingRecipient mailingRecipient = new MailingRecipient();
      User user = (User)approverIter.next();
      mailingRecipient.setUser( user );
      if ( user.getLanguageType() != null )
      {
        mailingRecipient.setLocale( user.getLanguageType().getCode() );
      }
      else
      {
        mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      Map dataMap = new HashMap();
      dataMap.put( "approverFirstName", user.getFirstName() );
      dataMap.put( "promotionName", promotionName );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );

      approversRecipientsSet.add( mailingRecipient );
    }
    return approversRecipientsSet;
  }

  protected int approverReminderByEndDate( long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean individualLaunch, boolean sendNotification )
  {
    int recipientCount = 0;

    Mailing mailing = sendApproverNotificationByEndDateMailing( messageId, promotion );
    recipientCount = mailing != null && mailing.getMailingRecipients() != null ? mailing.getMailingRecipients().size() : 0;
    if ( recipientCount > 0 )
    {
      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, null );

        log.debug( "Number of recipients attempted for Approver Notification Email: " + recipientCount );
        addComment( "Approver Notification Email Sent for " + recipientCount + " recipients - for Promotion: " + promotion.getName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Approver Notification Email for promotion: " + promotion.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while sending Approver Notification Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }
    }

    return recipientCount;
  }

  private Mailing sendApproverNotificationByEndDateMailing( long messageId, Promotion promotion )
  {
    Set approvers = this.getDistinctApproversByPromotion( promotion );
    Set mailingRecipients = buildNominationApporverReminderByEndDate( approvers, promotion, false );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Message message = messageService.getMessageById( messageId );
    Mailing mailing = new Mailing();

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "SYSTEM" );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Set buildNominationApporverReminderByEndDate( Set approvers, Promotion promotion, boolean isDefaultApprover )
  {
    Set approversRecipientsSet = new HashSet();
    NominationClaim claim = new NominationClaim();

    // build approver recipient list
    Iterator approverIter = approvers.iterator();
    while ( approverIter.hasNext() )
    {
      MailingRecipient mailingRecipient = new MailingRecipient();
      User user = (User)approverIter.next();
      mailingRecipient.setUser( user );
      if ( user.getLanguageType() != null )
      {
        mailingRecipient.setLocale( user.getLanguageType().getCode() );
      }
      else
      {
        mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      Map dataMap = new HashMap();
      dataMap.put( "approverFirstName", user.getFirstName() );
      dataMap.put( "isDefaultApprover", isDefaultApprover ? "TRUE" : "FALSE" );
      dataMap.put( "promotionName", promotion.getName() );
      dataMap.put( "approvalEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getApprovalEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

      Long pendingNominationCount = participantService.getPendingNominationCountForApprover( user.getId() );
      dataMap.put( "pendingNominationCount", pendingNominationCount.toString() );
      if ( pendingNominationCount > 1 )
      {
        dataMap.put( "multipleNominationsPending", "TRUE" );
      }
      else if ( pendingNominationCount == 1 )
      {
        dataMap.put( "oneNominationPending", "TRUE" );
      }

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );

      approversRecipientsSet.add( mailingRecipient );
    }
    return approversRecipientsSet;
  }

  protected int approverReminderByTimePeriodEnd( long messageId, Promotion promotion, PromotionNotificationType promotionNotificationType, boolean individualLaunch, boolean sendNotification )
  {
    int recipientCount = 0;

    Mailing mailing = sendApproverNotificationByTimePeriodEndMailing( messageId, promotion );
    recipientCount = mailing != null && mailing.getMailingRecipients() != null ? mailing.getMailingRecipients().size() : 0;
    if ( recipientCount > 0 )
    {
      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, null );

        log.debug( "Number of recipients attempted for Approver Notification Email: " + recipientCount );
        addComment( "Approver Notification Email Sent for " + recipientCount + " recipients - for Promotion: " + promotion.getName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending Approver Notification Email for promotion: " + promotion.getName() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while sending Approver Notification Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }
    }

    return recipientCount;
  }

  private Mailing sendApproverNotificationByTimePeriodEndMailing( long messageId, Promotion promotion )
  {
    Set approvers = this.getDistinctApproversByPromotion( promotion );
    Set mailingRecipients = buildNominationApproverReminderTimePeriodEnd( approvers, promotion, false );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Message message = messageService.getMessageById( messageId );
    Mailing mailing = new Mailing();

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "SYSTEM" );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Set buildNominationApproverReminderTimePeriodEnd( Set approvers, Promotion promotion, boolean isDefaultApprover )
  {
    Set approversRecipientsSet = new HashSet();
    NominationClaim claim = new NominationClaim();

    // build approver recipient list
    Iterator approverIter = approvers.iterator();
    while ( approverIter.hasNext() )
    {
      MailingRecipient mailingRecipient = new MailingRecipient();
      User user = (User)approverIter.next();
      mailingRecipient.setUser( user );
      if ( user.getLanguageType() != null )
      {
        mailingRecipient.setLocale( user.getLanguageType().getCode() );
      }
      else
      {
        mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }
      mailingRecipient.setGuid( GuidUtils.generateGuid() );
      NominationPromotionTimePeriod timePeriod = nominationPromotionService.getCurrentTimePeriod( promotion.getId(), getUserService().getUserTimeZone( user.getId() ), user.getId() );

      Map dataMap = new HashMap();
      dataMap.put( "approverFirstName", user.getFirstName() );
      dataMap.put( "defaultApprover", isDefaultApprover ? "TRUE" : "FALSE" );
      dataMap.put( "promotionName", promotion.getName() );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

      Long pendingNominationCount = participantService.getPendingNominationCountForApprover( user.getId() );
      dataMap.put( "pendingNominationCount", pendingNominationCount.toString() );
      if ( pendingNominationCount > 1 )
      {
        dataMap.put( "multipleNominationsPending", "TRUE" );
      }
      else if ( pendingNominationCount == 1 )
      {
        dataMap.put( "oneNominationPending", "TRUE" );
      }

      dataMap.put( "timePeriodName", timePeriod.getTimePeriodName() );
      dataMap.put( "timePeriodEndDate", com.biperf.core.utils.DateUtils.toDisplayString( timePeriod.getTimePeriodEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );

      approversRecipientsSet.add( mailingRecipient );
    }

    return approversRecipientsSet;
  }
}

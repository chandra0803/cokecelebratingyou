
package com.biperf.core.service.email;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.client.RecognizeAnyone;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.SAO;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.domain.enums.NominationEvaluationType;

/**
 * EmailServiceImpl
 * 
 *
 */
public interface MailingService extends SAO
{

  public static final String BEAN_NAME = "mailingService";

  /**
   * Submit mailing - this is the main mailing method. Mailing object has to built with
   * mailingRecipients and also have message. Additionally: MailingRecipients need to have any
   * recipient specific data that needs to be populated. ObjectMap needs to have any mailing level
   * data that needs to be populated.
   * 
   * @param mailing Mailing object - for most purposes (except email wizard mailings) it needs to
   *          contain: mailingRecipients and message
   * @param mailingLevelPersonalizationData Needs to have any mailing level data that needs to be
   *          populated. example - key = "color" | value = "blue" - the template would contain
   *          something like "The car is ${color}." and it becomes "The car is blue." * Take
   *          specific note of the token in the template v. the representation in the map!
   * @return Mailing returns mailing if successfully saved and scheduled for mailing (*** this does
   *         not mean it has been sent yet! To verify that a mailing recipient has been sent a
   *         mailing check the mailing recipient sentDate after MailingProcess has run).
   */
  public Mailing submitMailing( Mailing mailing, Map mailingLevelPersonalizationData );

  /**
   * Used to submit mailing with a runAsUser user id for Welcome Email process
   * 
   * @param mailing
   * @param mailingLevelPersonalizationData
   * @param runByUserId
   * @return Mailing
   */
  public Mailing submitMailing( Mailing mailing, Map mailingLevelPersonalizationData, Long runByUserId );

  /**
   * Resubmit mailing - used to resubmit a mailing for a user to resend it
   * 
   * @param prevMailing Previous mailing object (loaded from DB)
   * @param user User mailing is going to be resent to.
   * @param newEmailAddress if this is set then the email will be resent to the newEmailAddress;
   *                        ; otherwise the email will be resent to the same email address.
   * @param createCommLog if true a new CommLog will be created;
   * @return Mailing returns mailing if successfully saved and scheduled for mailing (*** this does
   *         not mean it has been sent yet! To verify that a mailing recipient has been sent a
   *         mailing check the mailing recipient sentDate after MailingProcess has run).
   */
  public Mailing reSubmitMailing( Mailing prevMailing, User user, String newEmailAddress, boolean createCommLog );

  /**
   * Submit message for system mailing (PM mailing)
   * 
   * @param subject
   * @param textBody Body text
   * @param htmlBody Body html
   * @return Mailing returns mailing if successfully saved and scheduled for mailing (*** this does
   *         not mean it has been sent yet! To verify that a mailing recipient has been sent a
   *         mailing check the mailing recipient sentDate after MailingProcess has run).
   */
  public Mailing submitSystemMailing( String subject, String htmlBody, String textBody );

  /**
   * Helper method to build preview mailing recipient
   * 
   * @param previewEmail preview email address
   * @return MailingRecipient mailing recipient
   */
  public MailingRecipient buildPreviewMailingRecipient( String previewEmail );

  /**
   * Helper method to build mailing recipient
   * 
   * @param user User to be attached to mailingRecipient (has e-mail addresses)
   * @param mailingRecipientData recipient specific data for mail processing
   * @return MailingRecipient
   */
  public MailingRecipient buildMailingRecipient( User user, Set mailingRecipientData );

  /**
   * Process mailing given mailing id FOR USE ONLY BY MAILING PROCESS (DO NOT USE)
   * 
   * @param mailingId
   */
  public void processMailing( Long mailingId );

  /**
   * Get mailing by id
   * 
   * @param id
   * @return Mailing
   */
  public Mailing getMailingById( Long id );

  /**
   * Save mailing
   * 
   * @param mailing
   * @return Mailing
   */
  public Mailing saveMailing( Mailing mailing );

  /**
   * Get all mailing attachment info for mailings that have been sent
   * in which contains fullFileName which can be used to delete temp files
   * previously written on the app server
   * 
   * @return List of MailingAttachmentInfo
   */
  public List getAllMailingAttachmentInfoAlreadySent();

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingDAO#getUsersWhoReceivedMessage(java.lang.Long)
   * @param messageId
   * @return List of User who have received the message
   */
  public List getUsersWhoReceivedMessage( Long messageId );

  /**
   * @param mailingId
   * @return List of User who have successfully received the message (dateSent not null on the
   *         Mailing Recipient)
   */
  public List getUsersWhoSuccessfullyReceivedMessage( Long mailingId );

  public Long getRunAsUserIdByMailingId( Long mailingId );

  /**
   * @param claim
   * @param participant
   * @param awardAmount
   * @param processStartDate
   * @param processEndDate
   * @return MailingRecipient
   */
  public MailingRecipient buildMailingRecipientForProductClaimEmail( ProductClaim claim, Participant participant, Long awardAmount, Date processStartDate, Date processEndDate, Promotion promotion );

  /**
   * 
   * @param participant
   * @param goalCalculationResult
   * @param promotion
   * @param paxPartners 
   * @param importFile 
   * @return MailingRecipient
   */
  public MailingRecipient buildMailingRecipientForGoalQuestEmail( GoalQuestPromotion promotion,
                                                                  Participant participant,
                                                                  GoalCalculationResult goalCalculationResult,
                                                                  List paxPartners,
                                                                  ImportFile importFile );

  /**
   * @param alertMessage
   * @param participant
   * @return mailingRecipient
   */
  public MailingRecipient buildMailingRecipientForManagerAlert( AlertMessage alertMessage, Participant participant, String companyName, String companyWebsite );

  /**
   * Build the Mailing list for this participant. 
   * @param Promotion
   * @param participant
   * @param ChallengepointPaxAwardValueBean
   * @param importFile 
   * @return MailingRecipient
   */
  public MailingRecipient buildMailingRecipientForChallengepointEmail( ChallengePointPromotion promotion,
                                                                       Participant participant,
                                                                       ChallengepointPaxAwardValueBean cpPaxAwardValueBean,
                                                                       ImportFile importFile );

  /**
   * Build the Mailing list for this participant. 
   * @param Promotion
   * @param participant
   * @param ChallengepointPaxAwardValueBean
   * @param importFile 
   * @return MailingRecipient
   */
  public MailingRecipient buildMailingRecipientForChallengepointEmail( ChallengePointPromotion promotion,
                                                                       Participant participant,
                                                                       ChallengepointPaxValueBean cpPaxValueBean,
                                                                       ImportFile importFile,
                                                                       List paxPartners );

  public MailingRecipient buildMailingRecipientForThrowdownEmail( ThrowdownPromotion promotion,
                                                                  Participant participant,
                                                                  Match match,
                                                                  MatchTeamOutcome currentPlayerOutcome,
                                                                  MatchTeamOutcome oppositionPlayerOutcome,
                                                                  Integer rank,
                                                                  Integer payout,
                                                                  Integer totalPaxInRanking,
                                                                  BigDecimal cumulativeProgress,
                                                                  Long rankingPayout,
                                                                  String progressEndDate,
                                                                  BigDecimal shadowScore );

  public MailingRecipient buildMailingRecipientForThrowdownEmail( ThrowdownPromotion promotion, String sb, Round round, Date progressEndDate, User manager );

  /**
   * @param claim
   * @param recipient
   * @return Mailing
   */
  public Mailing buildRecognitionMailing( RecognitionClaim claim, Participant recipient );
  
  //Coke customization start
  public Mailing buildRecognitionMailingCustom( RecognitionClaim claim, Participant recipient );

  public Mailing buildRecognitionMailingCustomOnlyPoints( RecognitionClaim claim, String award );
  // Coke customization end

  /**   
   * @param recipient
   * @return Mailing
   */
  public Mailing buildPurlManagerNotificationMailing( long messageId, PurlRecipient purlRecipient );

  /**   
   * @param recipient
   * @return Mailing
   */
  public Mailing buildPurlContributorNotificationMailing( long messageId, PurlContributor purlContributor, Long nonContributorUserId );

  /**   
   * @param recipient
   * @return Mailing
   */
  public Mailing buildPurlRecipientInvitationMailing( long messageId, PurlRecipient purlRecipient );

  /**
   * @param merchOrder
   * @return Mailing
   */
  public Mailing buildMerchOrderGiftCodeRefundMailing( MerchOrder merchOrder, String emailAddress, String message );

  /**
   * @param leaderBoard
   * @param participant
   * @param lbPax
   * @param paxCount
   * @return Mailing
   */
  public Mailing buildLeaderboardNotificationMailing( LeaderBoard leaderBoard, LeaderBoardParticipant lbPax, int paxCount, Message message, boolean createNewLB, String notifyMessage );

  /**
   * @param claim
   * @param depositedPoints 
   * @param calculatedPayout 
   * @return Mailing
   */
  public Mailing buildQuizMailing( QuizClaim claim, boolean isCalculationSuccessful, Long depositedPoints );

  // Bug # 37498 - added userId
  /**
   * @param claim
   * @return List
   */
  public List buildNominationMailing( NominationClaim claim, Long userId );

  /**
   * @param promotion
   * @param participant
   * @param goalCalculationResult
   * @param partner
   * @return HashMap
   */
  public HashMap buildMailingRecipientForPartnerGoalQuestEmail( GoalQuestPromotion promotion, Participant participant, GoalCalculationResult goalCalculationResult, Participant partner );

  /**
   * Submit mailing - this is the main mailing method. Mailing object has to built with
   * mailingRecipients and also have message. Additionally: MailingRecipients need to have any
   * recipient specific data that needs to be populated. ObjectMap needs to have any mailing level
   * data that needs to be populated. Mail is sent synchronously without scheduling. 
   * 
   * @param mailing
   * @param mailingLevelPersonalizationData
   * @return
   */
  public Mailing submitMailingWithoutScheduling( Mailing mailing, Map mailingLevelPersonalizationData );

  /**
   * Send an SMS message.
   * @param userId
   * @param destinationCountryAbbrev
   * @param destination
   * @param message
   * @return
   */
  public boolean sendSmsMessage( Long userId, String destinationCountryAbbreviation, String destination, String message );

  /**
   * This method looks at the email.batch.enable OS_PROPERTY to see if build emails
   * should be used for the following emails: 
   *    
   *    ChallengePoint: promotion launch notification, Progress updated, Goal Achieved, Goal not Achieved,
   *                    Goal not selected, intermin payout process
   *                    
   *    GoalQuest: promotion launch notification, Progress updated (Partner too), Goal Achieved(Partner too), 
   *               Goal not Achieved(Partner too), Goal Achieved no payout(Partner too), Goal not selected, 
   *               intermin payout process                
   * 
   * @param mailing
   * @return boolean if the install has bulk emails enabled
   */
  public boolean isBatchEmailEnabled();

  public MailingBatch createMailingBatch( MailingBatch mailingBatch );

  /**
   * getMailingBatch
   * @param batchId
   * @return MailingBatch
   */
  public MailingBatch getMailingBatch( Long batchId );

  /**
   * updateMailingBatch
   * @param batchId
   * @return 
   */
  public void updateMailingBatch( Long batchId );

  /**
   * getMailingsForBatchId
   * @param batchId
   * @return List of Mailing Batch
   */

  public List getMailingsForBatchId( Long batchId );

  /**
   * Check for Email Batch Enabled and Add description to Mailing Batch and create a new MailingBatch.
   * 
   * @param processName
   * 
   * @return MailingBatch
   */
  public MailingBatch applyBatch( String processName );

  public String getShortUrl( String url );

  public String getShortUrl( String url, String title );

  public Mailing buildReportExtractNotificationMailing( FileStore fileStore, Message message );

  /**
   * @param promotion
   * @param participant
   * @param cpCalculationResult
   * @param partner
   * @return HashMap
   */
  public HashMap buildMailingRecipientForPartnerCPEmail( ChallengePointPromotion promotion, Participant participant, ChallengePointCalculationResult cpCalculationResult, Participant partner );

  public String getEmailCssForUser( User user );

  public String getEmailClientLogo();

  public String getEmailBodyPhoto();

  /**
   * @param claim
   * @param recipient
   * @return Mailing
   */
  public Mailing buildRecognitionCelebrationMailing( RecognitionClaim claim, Participant recipient, boolean isPurl );

  public Mailing buildCelebrationManagersMailing( CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient );

  public Mailing buildContestProgressLoadCreatorMailing( SSIContest ssiContest,
                                                         Participant creator,
                                                         SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                         List<SSIContestProgressValueBean> contestProgressData,
                                                         List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                         SSIPromotion ssiPromotion,
                                                         String isSSIAdmin );

  public Mailing buildContestProgressLoadParticipantMailing( SSIContest ssiContest,
                                                             Participant participant,
                                                             SSIContestPaxProgressDetailValueBean paxProgressValueBean,
                                                             String badgeImageUrlSuffix,
                                                             SSIPromotion ssiPromotion );

  public Map<String, Object> setCreatorMgrDoThisGetThatData( SSIContest contest, Map<String, Object> dataMap, List<SSIContestProgressValueBean> contestProgressData, int decimalPrecision );

  public Map<String, Object> setContestProgressDoThisGetThatData( SSIContest contest, Map<String, Object> dataMap, SSIContestPaxProgressDetailValueBean paxProgressValueBean, Locale locale );

  public Mailing buildSSIContestApprovalNotification( SSIContest contest, Set<SSIContestApprover> contestApprovers, Participant creator, boolean level1Approval, Long level1ApproverId );

  public Mailing buildSSIContestApprovalStatusNotification( SSIContest contest, Participant creator, String approverName, SSIContestAwardThemNow contestAwardThemNow );

  public Mailing buildContestProgressLoadManagerMailing( SSIContest ssiContest,
                                                         Participant manager,
                                                         SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                         List<SSIContestProgressValueBean> contestProgressData,
                                                         List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                         SSIPromotion ssiPromotion );

  public Mailing buildSSIContestUpdatedAfterApprovalNotification( SSIContest contest, Participant approver, String creatorName, Date approvalDate );

  public Mailing buildContestProgressLoadCompleteCreatorMailing( SSIContest ssiContest,
                                                                 Participant creator,
                                                                 Long importFileId,
                                                                 String fileName,
                                                                 int totalRecords,
                                                                 int totalProcessedRecords,
                                                                 SSIPromotion ssiPromotion,
                                                                 String isSSIAdmin );

  public Mailing buildContestClaimApprovalNotification( SSIContest contest, Participant approver );

  public Mailing buildContestClaimApprovalUpdateStatusNotification( SSIContestPaxClaim paxClaim, String approverName, SSIContest contest, Participant submitter, String activityDescription );

  public Mailing buildContestClaimApprovalUpdateStatusNotification( SSIContestPaxClaim paxClaim,
                                                                    String approverName,
                                                                    Message message,
                                                                    SSIContest contest,
                                                                    Participant submitter,
                                                                    String activityDescription );

  public int reScheduleEmailFailures( Date startDate, Date endDate, Long runByUserId );

  /**
   * Place values into the dataMap for displaying badges.
   * @param userId Nominee user ID
   * @param includeEarned True if earned/not earned type badges should be included as well.
   */
  public void buildBadgesForNominationPromotionNotification( Map<String, String> dataMap, NominationClaim claim, Long userId, boolean includeEarned );

  public void buildNominatorRequestForMoreInfo( Approvable claim, User approver, Message message, User nominee, String moreInfo );

  public void buildNominatorNonWinnerNotification( Approvable claim, Message message, String reasonType, User nominee, String levelName );

  public Mailing buildNominationRequestMoreBudgetMailing( NominationPromotion promotion,
                                                          PromotionAwardsType awardType,
                                                          Participant claimApprover,
                                                          String claimApproverTimeZoneId,
                                                          BigDecimal amountRequested );

  public Mailing buildApproverRequestForMoreInfoReceived( Approvable claim, Participant approver, Participant nominee, Message message, String moreInfo, boolean defaultApprover );

  public Mailing buildSSIContestEditNotification( SSIContest ssiContest, String previewEmailID );

  public Mailing buildPAXOptOutOfAwardsNotification( Long mailingRecipientId, boolean isOwnerorManagerCopy, Participant participant );

  public Mailing buildPAXForgotLoginIDNotification( Long userId, PaxContactType paxContactType, String previewEmailAddress );

  /*
   * This method is called after the buildPAXForgotLoginIDNotification email goes out. This method
   * sends a notification to all the contact methods telling the user that a request was made for a
   * forgot userId email.
   */
  public Mailing buildPaxForgotLoginIDSentNotification( UserEmailAddress email, boolean sentToEmail, boolean sharedContact );

  public Mailing buildPAXForgotPasswordNotification( Long userId, PaxContactType paxContactType, String userToken );

  public Mailing buildAccountOrPasswordChangeNotification( Long userId, boolean sharedPrimaryContact, String message );

  public Mailing buildPAXActivationNotification( Long userId, Long contactId, String userToken );

  public void buildPAXActivationText( UserPhone userPhone, String userToken );

  // public Mailing buildReissuedSendPasswordMailing( User loggedinUser, User user, String password,
  // Integer daysValid );

  public String buildUserTokenURL( String userToken );

  public String buildAccountOrPasswordChangeText( User user, String message );

  // public Mailing SendWelcomeEmail( User user, boolean uniquePrimaryEmail
  // );buildReissuedPasswordMailing

  public Mailing buildRecoveryVerificationMailing( Long recipientId, PaxContactType paxContactType, String userToken );

  public Mailing buildRecoveryNotificationMailing( Long recipientId, PaxContactType paxContactType );

  public String buildRecoveryNotificationText( User user );

  public Mailing buildAccountOrPasswordChangeNotificationToTermedUser( Long mailingRecipientId, boolean sharedContact, String message );

  public Mailing buildUnLockAccountNotification( Long mailingRecipientId, boolean sharedContact, String message );

  public String buildUnLockAccountMobileNotification( String message );
  // Client customizations for WIP #42701 starts
  public Mailing buildCashRecognitionPendingApprovalMailing( Participant recipient );
  // Client customizations for WIP #42701 ends

  /* TCCC  - customization start - wip 46975 */
  public Mailing buildSendRecognizeAnyoneMailing( RecognizeAnyone recognizeAnyone );
  // Client customization for WIP 58122 
  public Set buildMailingRecipientsForWinningNominee( NominationClaim claim, NominationEvaluationType evaluationType, Long userId );
  /* TCCC  - customization end */ 
  /**   
   * @param recipient
   * @return Mailing
   */
  /* custom for wip #46293 */
  public Mailing buildPurlContributorNotificationMailing( long messageId, PurlContributor purlContributor, Long nonContributorUserId, boolean isAutoInvite );
  /* custom for wip #46293 */
  
}

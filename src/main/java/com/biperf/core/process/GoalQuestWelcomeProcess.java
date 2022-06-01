/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/GoalQuestWelcomeProcess.java,v $
 */

package com.biperf.core.process;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ChallengepointUtil;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.SelectGoalUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.GoalLevelValueBean;

@SuppressWarnings( { "unchecked", "rawtypes" } )
public class GoalQuestWelcomeProcess extends BaseGoalQuestWelcomeProcess
{
  public static final String BEAN_NAME = "goalQuestWelcomeProcess";
  private static final Log log = LogFactory.getLog( GoalQuestWelcomeProcess.class );

  // Services injected
  protected PromotionService promotionService;
  protected PaxGoalService paxGoalService;

  protected String promotionId;

  protected MailingBatch mailingBatch;

  public GoalQuestWelcomeProcess()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  @Override
  public void onExecute()
  {
    log.debug( "--------------------------------------------------------------------------------" );
    log.debug( "Running process : " + BEAN_NAME + " with PromotionID = " + promotionId );

    try
    {
      // stop if Bounce Back Email Verification is not complete
      if ( !isBounceBackVerified() )
      {
        notifyBounceBackVerifyError();
      }
      else
      {
        // Get the list of PAX goals selected for this promotion
        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
        Promotion promotion = promotionService.getPromotionByIdWithAssociations( new Long( promotionId ), arCollection );
        if ( promotion.isGoalQuestPromotion() )
        {
          Message message = getGoalquestEmailMessage();
          if ( message != null && message.getId() != null && message.getId().longValue() > 0 )
          {
            // Check for Email batch enabled and then create a batch for each promotion.
            mailingBatch = applyBatch( BEAN_NAME + " - " + promotion.getName() );
            long launchRecipientCount = sendLaunchMessage( message, (GoalQuestPromotion)promotion, null );

            // Add Comment to process invocation

            String comment = "Promotion: " + promotion.getName() + ". Number of recipients attempted for Goalquest Welcome Email : " + launchRecipientCount;
            log.debug( comment );
            addComment( comment );
            addComment( getMailingBatchProcessComments( mailingBatch ) );
          }
          else
          {
            String errorMessage = "Promotion: " + promotion.getName() + ". Goalquest Welcome Email Message not found";
            log.error( errorMessage );
            addComment( errorMessage );
          }
        }
        else
        {
          String errorMessage = "Promotion: " + promotion.getName() + ". Not a Goalquest Promotion";
          log.error( errorMessage );
          addComment( errorMessage );
        }
      }
    }
    catch( Exception e )
    {
      String errorMessage = "An exception occurred while sending GoalQuest Welcome Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")";
      log.error( errorMessage, e );
      addComment( errorMessage );
    }

    log.debug( "Completed process : " + BEAN_NAME + " with PromotionID = " + promotionId );
    log.debug( "--------------------------------------------------------------------------------" );
  }

  protected boolean isBounceBackVerified()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.BOUNCEBACK_EMAIL_VERIFIED ).getBooleanVal();
  }

  protected void notifyBounceBackVerifyError()
  {
    String errorMessage = "Bounce Back Email Verification is not complete. Cannot send GoalQuest Welcome Emails.";
    log.error( errorMessage );
    addComment( errorMessage );
    sendFailureMessage( 0, errorMessage );
  }

  /**
   * Get the Goalquest Welcome Email Message Id
   * 
   * @return
   */
  protected Message getGoalquestEmailMessage()
  {
    return messageService.getMessageByCMAssetCode( MessageService.GOAL_QUEST_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE );
  }

  /**
     * Send failure summary message
     * 
     * @param successCount
     * @param errorMessage
     * @return Mailing
     */
  protected Mailing sendFailureMessage( int successCount, String errorMessage )
  {
    User recipientUser = getRunByUser();

    String recipientFirstname = recipientUser.getFirstName();
    String recipientLastname = recipientUser.getLastName();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientFirstname );
    objectMap.put( "lastName", recipientLastname );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failureMessage", errorMessage );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.WELCOME_EMAIL_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );
    try
    {
      // Send the e-mail message with personalization
      mailing = mailingService.submitMailing( mailing, objectMap, recipientUser.getId() );
      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientFirstname + " " + recipientLastname );
      log.debug( "number of recipients successfully received Welcome Email: " + successCount );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( successCount + " users received welcome email successfully." );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while sending GoalQuest Welcome Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")";
      log.error( message, e );
      addComment( message );
    }

    return mailing;
  }

  protected long sendLaunchMessage( Message message, GoalQuestPromotion promotion, Participant testUser )
  {
    long mailingRecipientCount = 0;
    boolean sampleEmailSent = false;
    Set allUsers = new HashSet();
    List filteredUsers = new ArrayList();
    File file = null;

    if ( testUser == null )
    {
      // Primary Audience Paxs who are eligible for the given promotion.
      allUsers.addAll( participantService.getAllEligibleActivePaxForPromotion( promotion.getId(), true ) );
      for ( Iterator iter = allUsers.iterator(); iter.hasNext(); )
      {
        Participant pax = getParticipant( iter.next() );
        // Bug fix 19164 Do not proceed when the Pax is inactive
        // or check isWelcomEmailSent()
        if ( pax == null || !pax.isActive().booleanValue() || pax.isWelcomeEmailSent().booleanValue() )
        {
          continue;
        }
        filteredUsers.add( pax );
        ++mailingRecipientCount;
      }
    }
    else
    {
      // Test pax for welcome email test
      filteredUsers.add( testUser );
    }

    // get data map with promotion info in it
    Map dataMap = setPersonalizationData( promotion );
    for ( Iterator iter = filteredUsers.iterator(); iter.hasNext(); )
    {
      try
      {
        Participant pax = (Participant)iter.next();
        if ( !isNotice() || isNotice() && !sampleEmailSent )
        {
          MailingRecipient mailingRecipient = getMailingRecipient( pax );
          if ( isNotice() && !sampleEmailSent )
          {
            file = extractFile( filteredUsers, BEAN_NAME );
          }
          sendLaunchMessage( mailingRecipient, message, promotion, dataMap, file, buildTokenUrl( pax.getId() ) );
          updateUser( pax );
          sampleEmailSent = true;
        }
      }
      catch( Exception e )
      {
        String exception = "An exception occurred while sending GoalQuest Welcome Email for promotion: " + promotion.getName() + ". See the log file for additional information.  "
            + "(process invocation ID = " + getProcessInvocationId() + ")";
        log.error( exception, e );
        addComment( exception );
      }
    }
    return mailingRecipientCount;
  }

  protected Map setPersonalizationData( GoalQuestPromotion promotion )
  {
    Map dataMap = new HashMap();
    dataMap.put( "programName", promotion.getName() );
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteUrl", siteUrl );
    dataMap.put( "contactUsUrl", new StringBuffer( siteUrl ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
    {
      dataMap.put( "points", String.valueOf( Boolean.TRUE ) );
    }
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      dataMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
    }
    return dataMap;
  }

  /**
    * Send launch message to the recipient
    * 
    * @param recipient
    * @param messageId
    * @param promo
    */
  protected void sendLaunchMessage( MailingRecipient recipient, Message message, GoalQuestPromotion promo, Map promoObjectMap, File file, String userTokenUrl )
  {
    // Compose the mailing
    Mailing mailing = composeMail( message.getId(), MailingType.PROMOTION );

    // Add pax personalization data to the objectMap
    Map paxObjectMap = new HashMap( promoObjectMap );
    setPersonalizationData( paxObjectMap, promo, recipient.getUser(), userTokenUrl );

    if ( isNotice() )
    {
      recipient = getMailingRecipient( getRunByUser() );
    }
    recipient.addMailingRecipientDataFromMap( paxObjectMap );
    // Set the recipients on the mailing
    mailing.addMailingRecipient( recipient );
    mailing.setMailingBatch( mailingBatch );
    if ( file != null )
    {
      String absPath = file.getAbsolutePath();
      String fileName = file.getName();
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
    }
    mailingService.submitMailing( mailing, null );
  }

  public MailingBatch getMailingBatch()
  {
    return mailingBatch;
  }

  public void setMailingBatch( MailingBatch mailingBatch )
  {
    this.mailingBatch = mailingBatch;
  }

  protected void setPersonalizationData( Map objectMap, GoalQuestPromotion promotion, User participant, String userTokenUrl )
  {
    objectMap.put( "firstName", participant.getFirstName() );
    objectMap.put( "lastName", participant.getLastName() );
    objectMap.put( "user", participant.getUserName() );
    objectMap.put( "userTokenUrl", userTokenUrl );
    objectMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(),
                                                                                             LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                                 ? UserManager.getDefaultLocale().toString()
                                                                                                 : participant.getLanguageType().getCode() ) ) );
    objectMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(),
                                                                                           LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                               ? UserManager.getDefaultLocale().toString()
                                                                                               : participant.getLanguageType().getCode() ) ) );
    objectMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(),
                                                                                          LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                              ? UserManager.getDefaultLocale().toString()
                                                                                              : participant.getLanguageType().getCode() ) ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "promotionEndDate",
                     com.biperf.core.utils.DateUtils
                         .toDisplayString( promotEndDate,
                                           LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) ) );
    }
    objectMap.put( "notice", isNotice() ? "TRUE" : "FALSE" );

    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), participant.getId() );
    String paxBase = getPaxBase( promotion, paxGoal, participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    if ( paxBase != null )
    {
      objectMap.put( "isBase", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "base", paxBase );
    }
    boolean goalLevelsAvailable = true;
    Collection goalLevels = promotion.getGoalLevels();
    if ( goalLevels != null )
    {
      int n = 1;
      for ( Iterator iter = goalLevels.iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        paxGoal = SelectGoalUtil.getLevelSpecificGoal( paxGoal, promotion, goalLevel );
        GoalLevelValueBean goalLevelValueBean = SelectGoalUtil.populateGoalLevelValueBean( paxGoal, promotion, goalLevel );
        // BugFix 18491 Display GoalLevel Description instead of Name
        String languageCode = participant.getLanguageType().getCode();
        String cmassetcode = goalLevelValueBean.getGoalLevel().getGoalLevelcmAssetCode();
        String goalLevelName = ChallengepointUtil.getGoalLevelDescription( cmassetcode, LocaleUtils.getLocale( languageCode ) );
        BigDecimal goalAchieveAmt = goalLevelValueBean.getCalculatedGoalAmount();
        BigDecimal goalAwardAmt = goalLevel.getAward();
        if ( goalLevelName != null && !"".equals( goalLevelName ) && goalAchieveAmt != null && goalAchieveAmt.longValue() > 0 && goalAwardAmt != null && goalAwardAmt.longValue() > 0 )
        {
          objectMap.put( "goal" + n, String.valueOf( Boolean.TRUE ) );
          objectMap.put( "goalLevel" + n, goalLevelName );
          String goalAchieveAmount = goalAchieveAmt.toString();
          if ( goalAchieveAmt != null )
          {
            goalAchieveAmount = NumberFormatUtil
                .getLocaleBasedBigDecimalFormat( goalAchieveAmt,
                                                 promotion.getAchievementPrecision().getPrecision(),
                                                 LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
          }
          objectMap.put( "goalValue" + n, formatBasevalue( LocaleUtils.getLocale( languageCode ), goalAchieveAmount, promotion ) );

          String goalAwardAmount = goalAwardAmt.toString();
          if ( goalAwardAmt != null )
          {
            goalAwardAmount = NumberFormatUtil.getLocaleBasedNumberFormat( goalAwardAmt.longValue() );
          }
          objectMap.put( "goalReward" + n, goalAwardAmount );
          n++;
        }
        else
        {
          goalLevelsAvailable = false;
          break;
        }
      }
    }
    if ( goalLevelsAvailable )
    {
      objectMap.put( "showGoalLevels", "TRUE" );
    }
  }

  protected String getPaxBase( GoalQuestPromotion promotion, PaxGoal paxGoal, String locale )
  {
    String paxBase = null;
    BigDecimal baseQty = null;

    int roundingMode = 0;
    if ( promotion != null && promotion.getRoundingMethod() != null )
    {
      roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    }

    if ( null != paxGoal )
    {
      baseQty = paxGoal.getBaseQuantity();
      if ( null != baseQty && baseQty.floatValue() != 0 )
      {
        String baseQuantity = NumberFormatUtil.getLocaleBasedRoundingBigDecimalFormat( baseQty,
                                                                                       promotion.getAchievementPrecision().getPrecision(),
                                                                                       roundingMode,
                                                                                       LocaleUtils.getLocale( locale ),
                                                                                       promotion.getAchievementPrecision().getPrecision() );
        paxBase = formatBasevalue( LocaleUtils.getLocale( locale ), baseQuantity, promotion );
      }
    }

    return paxBase;
  }

  protected String formatBasevalue( Locale locale, String base, GoalQuestPromotion promotion )
  {
    StringBuffer formatBase = new StringBuffer();

    String baseUnit = ChallengepointUtil.getBaseUnitText( promotion.getBaseUnit(), locale );

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

  /**
   * Sets promotionService.
   * 
   * @param promotionService .
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * Sets the ID of the promotion whose approved journals this process will post.
   * 
   * @param promotionId the ID of the promotion whose approved journals this process will post.
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public boolean isNotice()
  {
    return false;
  }
}

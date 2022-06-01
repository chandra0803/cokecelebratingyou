/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ChallengepointWelcomeProcess.java,v $
 */

package com.biperf.core.process;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ChallengepointUtil;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxValueBean;

@SuppressWarnings( { "unchecked", "rawtypes" } )
public class ChallengepointWelcomeProcess extends BaseGoalQuestWelcomeProcess
{
  public static final String BEAN_NAME = "challengePointWelcomeProcess";
  private static final Log log = LogFactory.getLog( ChallengepointWelcomeProcess.class );

  // Services injected
  protected PromotionService promotionService;
  protected ChallengePointService challengePointService;
  protected PaxGoalService paxGoalService;
  protected String promotionId;

  protected MailingBatch mailingBatch;

  public ChallengepointWelcomeProcess()
  {
    super();
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
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
      if ( !systemVariableService.getPropertyByName( SystemVariableService.BOUNCEBACK_EMAIL_VERIFIED ).getBooleanVal() )
      {
        String errorMessage = "Bounce Back Email Verification is not complete. Cannot send Challegepoint Welcome Emails.";
        log.error( errorMessage );
        addComment( errorMessage );
        sendFailureMessage( 0, errorMessage );
      }
      else
      {
        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
        Promotion promotion = promotionService.getPromotionByIdWithAssociations( new Long( promotionId ), arCollection );
        if ( promotion.isChallengePointPromotion() )
        {
          long messageId = getChallegepointEmailMessageId();
          if ( messageId > 0 )
          {
            long launchRecipientCount = sendLaunchMessage( messageId, (ChallengePointPromotion)promotion );

            // Add Comment to process invocation
            String message = "Promotion: " + promotion.getName() + ". Number of recipients attempted for Challengepoint Welcome Email : " + launchRecipientCount;

            addComment( getMailingBatchProcessComments( mailingBatch ) );
            addComment( message );
            log.debug( message );
          }
          else
          {
            String errorMessage = "Promotion: " + promotion.getName() + ". Challengepoint Welcome Email Message not found";
            log.error( errorMessage );
            addComment( errorMessage );
          }
        }
        else
        {
          String errorMessage = "Promotion: " + promotion.getName() + ". Not a Challegepoint Promotion";
          log.error( errorMessage );
          addComment( errorMessage );
        }
      }
    }
    catch( Exception e )
    {
      String errorMessage = "An exception occurred while sending Challegepoint Welcome Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")";
      log.error( errorMessage, e );
      addComment( errorMessage );
    }

    log.debug( "Completed process : " + BEAN_NAME + " with PromotionID = " + promotionId );
    log.debug( "--------------------------------------------------------------------------------" );
  }

  /**
   * Get the Challegepoint Welcome Email Message Id
   * 
   * @return
   */
  protected long getChallegepointEmailMessageId()
  {
    long messageId = 0;
    Message message = messageService.getMessageByCMAssetCode( MessageService.CHALLENGEPOINT_WELCOME_MESSAGE_CM_ASSET_CODE );
    if ( message != null )
    {
      messageId = message.getId().longValue();
    }
    return messageId;
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
      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientFirstname + " " + recipientLastname );
      log.debug( "number of recipients successfully received Welcome Email: " + successCount );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( successCount + " users received welcome email successfully." );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while sending Challegepoint Welcome Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")";
      log.error( message, e );
      addComment( message );
    }

    return mailing;
  }

  protected long sendLaunchMessage( long messageId, ChallengePointPromotion promotion )
  {
    long mailingRecipientCount = 0;

    Set paxs = new HashSet();
    // Retrieves a list of primary audience participants who are eligible for the given
    // promotion.
    paxs.addAll( participantService.getAllEligibleActivePaxForPromotion( promotion.getId(), true ) );

    if ( paxs.size() > 0 )
    {
      // Check for Email batch enabled and then create a batch for each promotion.
      mailingBatch = applyBatch( BEAN_NAME + " - " + promotion.getName() );
    }

    Map promoObjectMap = new HashMap();
    setPersonalizationData( promoObjectMap, promotion );

    List allUsers = new ArrayList();
    for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
    {
      Participant pax = getParticipant( iter.next() );
      // Bug fix 19164 Do not proceed when the Pax is inactive
      // or check isWelcomEmailSent()
      if ( pax == null || !pax.isActive().booleanValue() || pax.isWelcomeEmailSent().booleanValue() )
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
          MailingRecipient mailingRecipient = getMailingRecipient( pax );
          if ( isNotice() && !sampleEmailSent )
          {
            file = extractFile( allUsers, BEAN_NAME );
          }

          sendLaunchMessage( mailingRecipient, messageId, promotion, promoObjectMap, file, buildTokenUrl( pax.getId() ) );
          updateUser( pax );
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

  protected void setPersonalizationData( Map objectMap, ChallengePointPromotion promotion )
  {
    objectMap.put( "programName", promotion.getName() );
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    objectMap.put( "siteUrl", siteUrl );
    objectMap.put( "contactUsUrl", new StringBuffer( siteUrl ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
    {
      objectMap.put( "points", String.valueOf( Boolean.TRUE ) );
    }
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
    }
  }

  /**
   * Send launch message to the recipient
   * 
   * @param recipient
   * @param messageId
   * @param promo
  * @throws ServiceErrorException 
   */
  protected void sendLaunchMessage( MailingRecipient recipient, long messageId, ChallengePointPromotion promo, Map promoObjectMap, File file, String userTokenUrl ) throws ServiceErrorException
  {
    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

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

  protected void setPersonalizationData( Map objectMap, ChallengePointPromotion promotion, User participant, String userTokenUrl ) throws ServiceErrorException
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
    if ( paxGoal != null )
    {
      objectMap.put( "isBase", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "base", paxBase );
    }
    List<ChallengepointPaxValueBean> paxBeanList = challengePointService.getParticipantChallengePointLevelBeans( promotion, paxGoal, false );
    if ( paxBeanList != null )
    {
      int n = 1;
      for ( Iterator iter = paxBeanList.iterator(); iter.hasNext(); )
      {
        ChallengepointPaxValueBean paxValueBean = (ChallengepointPaxValueBean)iter.next();

        // BugFix 19888,22272 Populate CP Level Description instead of CPLevel name.
        String languageCode = participant.getLanguageType().getCode();
        String cmassetcode = paxValueBean.getGoalLevel().getGoalLevelcmAssetCode();
        String cpLevelName = ChallengepointUtil.getGoalLevelDescription( cmassetcode, LocaleUtils.getLocale( languageCode ) );
        BigDecimal cpAchieveAmt = paxValueBean.getAmountToAchieve();
        BigDecimal cpAwardAmt = paxValueBean.getGoalLevel().getAward();
        if ( cpLevelName != null && !"".equals( cpLevelName ) && cpAchieveAmt != null && cpAchieveAmt.longValue() > 0 && cpAwardAmt != null && cpAwardAmt.longValue() > 0 )
        {
          objectMap.put( "challengepoint" + n, String.valueOf( Boolean.TRUE ) );
          objectMap.put( "challengepointLevel" + n, cpLevelName );
          String tempCpAchieveAmt = cpAchieveAmt.toString();
          if ( cpAchieveAmt != null )
          {
            tempCpAchieveAmt = NumberFormatUtil
                .getLocaleBasedBigDecimalFormat( cpAchieveAmt,
                                                 promotion.getAchievementPrecision().getPrecision(),
                                                 LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
          }
          objectMap.put( "challengepointValue" + n, formatBasevalue( LocaleUtils.getLocale( languageCode ), tempCpAchieveAmt, promotion ) );
          String tempCpAwardAmt = cpAwardAmt.toString();
          if ( cpAwardAmt != null )
          {
            tempCpAwardAmt = NumberFormatUtil
                .getLocaleBasedBigDecimalFormat( cpAwardAmt,
                                                 0,
                                                 LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
          }
          objectMap.put( "challengepointReward" + n, tempCpAwardAmt );
          n++;
        }
      }
    }
  }

  protected String getPaxBase( ChallengePointPromotion promotion, PaxGoal paxGoal, String locale )
  {
    String paxBase = null;
    BigDecimal baseQty = null;

    if ( null != paxGoal )
    {
      baseQty = paxGoal.getBaseQuantity();
      if ( null != baseQty && baseQty.floatValue() != 0 )
      {
        Integer precision = promotion.getAchievementPrecision().getPrecision();
        String baseQuantity = NumberFormatUtil.getLocaleBasedBigDecimalFormat( baseQty, precision, LocaleUtils.getLocale( locale ) );
        paxBase = formatBasevalue( LocaleUtils.getLocale( locale ), baseQuantity, promotion );
      }
    }

    return paxBase;
  }

  protected String formatBasevalue( Locale locale, String base, ChallengePointPromotion promotion )
  {
    StringBuffer formatBase = new StringBuffer();
    String baseUnit = ChallengepointUtil.getBaseUnitText( promotion.getBaseUnit(), locale );
    if ( null != baseUnit && baseUnit.trim().length() > 0 )
    {
      String baseUnitPositionStr = promotion.getBaseUnitPosition().getCode();

      if ( BaseUnitPosition.UNIT_BEFORE.equals( baseUnitPositionStr ) )
      {
        formatBase.append( baseUnit );
        formatBase.append( "&nbsp;" );
        formatBase.append( base );
      }
      else if ( BaseUnitPosition.UNIT_AFTER.equals( baseUnitPositionStr ) )
      {
        formatBase.append( base );
        formatBase.append( "&nbsp;" );
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

  public MailingBatch getMailingBatch()
  {
    return mailingBatch;
  }

  public void setMailingBatch( MailingBatch mailingBatch )
  {
    this.mailingBatch = mailingBatch;
  }

  public boolean isNotice()
  {
    return false;
  }

}

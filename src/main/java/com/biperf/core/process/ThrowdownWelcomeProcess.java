/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ThrowdownWelcomeProcess.java,v $
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
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionPayout;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.MatchAssociationRequest;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;

/**
 *
 */
public class ThrowdownWelcomeProcess extends BaseProcessImpl
{
  /**
   * Bean Name
   */
  public static final String BEAN_NAME = "throwdownWelcomeProcess";

  private static final Log log = LogFactory.getLog( ThrowdownWelcomeProcess.class );

  // Services injected
  protected PromotionService promotionService;
  protected ThrowdownService throwdownService;
  protected TeamService teamService;

  protected String promotionId;

  protected MailingBatch mailingBatch;
  protected MailingBatch mailingBatchForManager;
  protected PasswordResetService passwordResetService;

  public ThrowdownWelcomeProcess()
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
        AssociationRequestCollection associationRequestCollectionForPromotion = new AssociationRequestCollection();
        associationRequestCollectionForPromotion.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
        associationRequestCollectionForPromotion.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
        Promotion promotion = promotionService.getPromotionByIdWithAssociations( new Long( promotionId ), associationRequestCollectionForPromotion );
        if ( promotion.isThrowdownPromotion() )
        {

          long paxMessageId = getThrowdownEmailMessageId();
          long managerMessageId = getThrowdownManagerEmailMessageId();
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new MatchAssociationRequest( MatchAssociationRequest.MATCH_TEAM_OUTCOME ) );
          associationRequestCollection.add( new MatchAssociationRequest( MatchAssociationRequest.TEAM ) );
          associationRequestCollection.add( new MatchAssociationRequest( MatchAssociationRequest.ROUND ) );

          if ( paxMessageId > 0 )
          {
            // Check for Email batch enabled and then create a batch for each promotion.
            mailingBatch = applyBatch( BEAN_NAME + " - " + promotion.getName() );

            int roundNumber = 1;
            List<Match> matchList = throwdownService.getThrowdownMatchesByPromotionAndRoundNumber( promotion.getId(), roundNumber, associationRequestCollection );
            // Send Program Launch Email to paxs
            long launchRecipientCount = sendLaunchMessage( matchList, paxMessageId, (ThrowdownPromotion)promotion );

            // Add Comment to process invocation

            String message = "Promotion: " + promotion.getName() + ". Number of recipients attempted for Throwdown Welcome Email : " + launchRecipientCount;
            log.debug( message );
            addComment( message );
            addComment( getMailingBatchProcessComments( mailingBatch ) );
          }
          else
          {
            String errorMessage = "Promotion: " + promotion.getName() + ". Throwdown Welcome Email Message not found";
            log.error( errorMessage );
            addComment( errorMessage );
          }

          if ( managerMessageId > 0 )
          {
            // Check for Email batch enabled and then create a batch for each promotion.
            mailingBatchForManager = applyBatch( BEAN_NAME + " - " + promotion.getName() );

            int roundNumber = 1;
            List<Match> matchList = throwdownService.getThrowdownMatchesByPromotionAndRoundNumber( promotion.getId(), roundNumber, associationRequestCollection );
            // Send Program Launch Email to paxs
            long launchManagerRecipientCount = sendManagerLaunchMessage( matchList, managerMessageId, (ThrowdownPromotion)promotion );

            // Add Comment to process invocation

            String message = "Promotion: " + promotion.getName() + ". Number of recipients(Managers) attempted for Throwdown Welcome Email : " + launchManagerRecipientCount;
            log.debug( message );
            addComment( message );
            addComment( getMailingBatchProcessComments( mailingBatchForManager ) );
          }
          else
          {
            String errorMessage = "Promotion: " + promotion.getName() + ". Throwdown Welcome Email Message not found";
            log.error( errorMessage );
            addComment( errorMessage );
          }
        }
        else
        {
          String errorMessage = "Promotion: " + promotion.getName() + ". Not a Throwdown Promotion";
          log.error( errorMessage );
          addComment( errorMessage );
        }
      }
    }
    catch( Exception e )
    {
      String errorMessage = "An exception occurred while sending Throwdown Welcome Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")";
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
    String errorMessage = "Bounce Back Email Verification is not complete. Cannot send Throwdown Welcome Emails.";
    log.error( errorMessage );
    addComment( errorMessage );
    sendFailureMessage( 0, errorMessage );
  }

  /**
   * Get the Throwdown Welcome Email Message Id
   * 
   * @return
   */
  protected long getThrowdownEmailMessageId()
  {
    long messageId = 0;
    Message message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE );
    if ( message != null )
    {
      messageId = message.getId().longValue();
    }
    return messageId;
  }

  protected long getThrowdownManagerEmailMessageId()
  {
    long messageId = 0;
    Message message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MANAGER_EMAIL_WELCOME_MESSAGE_CM_ASSET_CODE );
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
      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientFirstname + " " + recipientLastname );
      log.debug( "number of recipients successfully received Welcome Email: " + successCount );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( successCount + " users received welcome email successfully." );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while sending throwdown Welcome Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")";
      log.error( message, e );
      addComment( message );
    }

    return mailing;
  }

  protected long sendLaunchMessage( List<Match> matchList, long messageId, ThrowdownPromotion promotion )
  {
    long mailingRecipientCount = 0;

    Map promoObjectMap = new HashMap();
    setPersonalizationData( promoObjectMap, promotion );
    List<Participant> allUsers = new ArrayList<Participant>();
    for ( Iterator<Match> iter = matchList.iterator(); iter.hasNext(); )
    {
      log.debug( "sending throwdown launch message to participants" );
      Match match = iter.next();
      log.debug( "sending throwdown launch message to participants" + match.getClass() );
      Participant participant = null;
      // Getting the count of audience from the matchoutcomes.
      for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
      {
        if ( !matchTeamOutcome.getTeam().isShadowPlayer() )
        {
          Participant pax = getParticipant( matchTeamOutcome.getTeam().getParticipant() );
          if ( pax == null || !pax.isActive().booleanValue() || pax.isWelcomeEmailSent().booleanValue() )
          {
            continue;
          }
          participant = pax;
          allUsers.add( participant );
          ++mailingRecipientCount;
        }
      }
    }

    boolean sampleEmailSent = false;
    File file = null;
    for ( Iterator<Match> iter = matchList.iterator(); iter.hasNext(); )
    {
      try
      {
        Match match = iter.next();
        for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
        {
          if ( !matchTeamOutcome.getTeam().isShadowPlayer() )
          {
            Participant pax = getParticipant( matchTeamOutcome.getTeam().getParticipant() );
            if ( pax == null || !pax.isActive().booleanValue() || pax.isWelcomeEmailSent().booleanValue() )
            {
              continue;
            }
            if ( !isNotice() || isNotice() && !sampleEmailSent )
            {
              MailingRecipient mailingRecipient = getMailingRecipient( pax );
              if ( isNotice() && !sampleEmailSent )
              {
                file = extractFile( allUsers, BEAN_NAME );
              }
              sendLaunchMessage( mailingRecipient, messageId, promotion, promoObjectMap, file, match, buildToken( pax.getId() ) );
              updateUser( pax );
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

  protected long sendManagerLaunchMessage( List<Match> matchList, long messageId, ThrowdownPromotion promotion )
  {
    long mailingManagerRecipientCount = 0;

    Map promoObjectMap = new HashMap();
    setPersonalizationData( promoObjectMap, promotion );
    List<Participant> allUsers = new ArrayList<Participant>();

    Map<User, Set<Participant>> managers = new HashMap<User, Set<Participant>>();
    managers = participantService.getManagerForCompetitorAudience( promotion.getId() );

    Participant participant = null;
    if ( managers != null )
    {
      for ( Iterator<User> iter = managers.keySet().iterator(); iter.hasNext(); )
      {
        User manager = getParticipant( iter.next() );
        if ( manager == null || !manager.isActive().booleanValue() || manager.isWelcomeEmailSent().booleanValue() )
        {
          continue;
        }
        participant = (Participant)manager;
        allUsers.add( participant );
        ++mailingManagerRecipientCount;
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
            MailingRecipient mailingRecipient = getMailingRecipient( manager );
            if ( isNotice() && !sampleEmailSent )
            {
              file = extractFile( allUsers, BEAN_NAME );
            }
            sendLaunchMessage( mailingRecipient, messageId, promotion, promoObjectMap, file, null, buildToken( manager.getId() ) );
            updateUser( (Participant)manager );
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
    return mailingManagerRecipientCount;
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

  protected String buildToken( Long paxId )
  {
    return getPasswordResetService().generateTokenAndSave( paxId, UserTokenType.EMAIL ).getUnencryptedTokenValue();
  }

  protected void setPersonalizationData( Map objectMap, ThrowdownPromotion promotion )
  {
    objectMap.put( "programName", promotion.getName() );
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    objectMap.put( "siteLink", siteLink );
    objectMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    objectMap.put( "numberOfRounds", String.valueOf( promotion.getNumberOfRounds() ) );
    objectMap.put( "ofDaysPerRound", String.valueOf( promotion.getLengthOfRound() ) );
    objectMap.put( "objective", promotion.getOverviewDetailsText() );
  }

  protected MailingRecipient getMailingRecipient( User user )
  {
    MailingRecipient mailingRecipient = null;

    // if ( null != participant && !participant.isWelcomeEmailSent().booleanValue() )
    {
      LanguageType languageType = user.getLanguageType();

      mailingRecipient = new MailingRecipient();
      mailingRecipient.setGuid( GuidUtils.generateGuid() );
      mailingRecipient.setLocale( languageType != null ? languageType.getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
      mailingRecipient.setUser( user );
    }

    return mailingRecipient;
  }

  /**
    * Send launch message to the recipient
    * 
    * @param recipient
    * @param messageId
    * @param promo
    */
  protected void sendLaunchMessage( MailingRecipient recipient, long messageId, ThrowdownPromotion promotion, Map promoObjectMap, File file, Match match, String passwordToken )
  {
    // Compose the mailing
    Mailing mailing = composeMail( new Long( messageId ), MailingType.PROMOTION );

    // Add pax personalization data to the objectMap
    Map paxObjectMap = new HashMap( promoObjectMap );
    setPersonalizationData( paxObjectMap, promotion, match, recipient.getUser(), passwordToken );

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

  protected void setPersonalizationData( Map objectMap, ThrowdownPromotion promotion, Match match, User participant, String passwordToken )
  {
    objectMap.put( "firstName", participant.getFirstName() );
    // BugFix 21054,pre poluate lastName as some templates might use this values in future.
    objectMap.put( "lastName", participant.getLastName() );
    objectMap.put( "user", participant.getUserName() );
    objectMap.put( "passwordToken", passwordToken );
    if ( promotion.getBaseUnit() != null )
    {
      objectMap.put( "unitLabel", promotion.getBaseUnitText() );
    }
    objectMap.put( "promotionStartDate",
                   com.biperf.core.utils.DateUtils
                       .toDisplayString( promotion.getSubmissionStartDate(),
                                         LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      objectMap.put( "promotionEndDate",
                     com.biperf.core.utils.DateUtils
                         .toDisplayString( promotEndDate,
                                           LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) ) );
    }
    if ( isNotice() )
    {
      objectMap.put( "notice", "Notice : " );
    }
    else
    {
      objectMap.put( "notice", "" );
    }

    if ( promotion.isSmackTalkAvailable() )
    {
      objectMap.put( "smackTalk", String.valueOf( Boolean.TRUE ) );
    }

    String locale = participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode();

    int roundNumber = 1;
    if ( match != null )
    {
      roundNumber = match.getRound().getRoundNumber();
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
        objectMap.put( "roundNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( roundNumber, LocaleUtils.getLocale( locale ) ) );
        objectMap.put( "roundStartDate", com.biperf.core.utils.DateUtils.toDisplayString( match.getRound().getStartDate(), LocaleUtils.getLocale( locale ) ) );
        objectMap.put( "roundEndDate", com.biperf.core.utils.DateUtils.toDisplayString( match.getRound().getEndDate(), LocaleUtils.getLocale( locale ) ) );
      }
    }
    else
    {
      List<Round> roundList = throwdownService.getThrowdownRoundsForPromotionByRoundNumber( promotion.getId(), roundNumber );
      Round round = roundList.get( 0 );
      objectMap.put( "roundNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( roundNumber ), LocaleUtils.getLocale( locale ) ) );
      objectMap.put( "roundStartDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getStartDate(), LocaleUtils.getLocale( locale ) ) );
      objectMap.put( "roundEndDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getEndDate(), LocaleUtils.getLocale( locale ) ) );

    }
    objectMap.put( "numberOfRounds", String.valueOf( promotion.getNumberOfRounds() ) );
    objectMap.put( "ofDaysPerRound", String.valueOf( promotion.getLengthOfRound() ) );
    objectMap.put( "objective", promotion.getOverviewDetailsText() );

    Set<DivisionPayout> payouts = new HashSet<DivisionPayout>();
    BigDecimal minimumQualifier = null;
    Division userDiv = teamService.getDivisionForUser( promotion.getId(), participant.getId(), roundNumber );
    for ( Division division : promotion.getDivisions() )
    {
      if ( division.equals( userDiv ) )
      {
        payouts = division.getPayouts();
        minimumQualifier = division.getMinimumQualifier();
        break;
      }
    }
    if ( !payouts.isEmpty() )
    {
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
    }
    if ( promotion.getTeamUnavailableResolverType().getCode().equals( TeamUnavailableResolverType.MINIMUM_QUALIFIER ) )
    {
      objectMap.put( "isMinQualifier", String.valueOf( Boolean.TRUE ) );
      objectMap.put( "minQualifier", String.valueOf( minimumQualifier ) );
    }

  }

  /**
   * Update user details
   */
  protected void updateUser( Participant participant )
  {
    try
    {
      User user = userService.getUserById( participant.getId() );
      user.setWelcomeEmailSent( Boolean.TRUE );
      userService.saveUser( user );
    }
    catch( Exception e )
    {
      String message = "An exception occurred while updating the user information while sending Throwdown Welcome Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")";

      log.error( message, e );
      addComment( message );
    }
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

  public boolean isNotice()
  {
    return false;
  }

  public ThrowdownService getThrowdownService()
  {
    return throwdownService;
  }

  public void setThrowdownService( ThrowdownService throwdownService )
  {
    this.throwdownService = throwdownService;
  }

  public TeamService getTeamService()
  {
    return teamService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
  }

  public PasswordResetService getPasswordResetService()
  {
    return passwordResetService;
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }

}

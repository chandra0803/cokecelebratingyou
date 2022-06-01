
package com.biperf.core.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ThrowdownProgressImportRecord;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.MatchResolution;
import com.biperf.core.service.throwdown.MatchTeamOutcomeAssociationRequest;
import com.biperf.core.service.throwdown.StackStandingService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.value.MailingBatchHolder;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class ThrowdownProgressImportProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "throwdownProgressImportProcess";
  public static final String MESSAGE_NAME = "Throwdown Progress Import Process";

  /**
   * the ID of the import file to process.
   */
  Long importFileId;

  private ImportService importService;
  private StackStandingService stackStandingService;
  private ThrowdownService throwdownService;
  private TeamService teamService;
  private PromotionService promotionService;

  private static final Log log = LogFactory.getLog( ThrowdownProgressImportProcess.class );

  @SuppressWarnings( "unchecked" )
  public void onExecute()
  {
    // do import
    doImport();

    ImportFile importFile = importService.getImportFile( new Long( importFileId ) );
    ThrowdownPromotion promotion = (ThrowdownPromotion)importFile.getPromotion();
    Integer roundNumber = importFile.getRoundNumber();
    Date progressEndDate = importFile.getProgressEndDate();
    String importFileTypeCode = importFile.getFileType().getCode();

    // do ranking
    doRanking( promotion.getId(), roundNumber );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promotion = (ThrowdownPromotion)promotionService.getPromotionByIdWithAssociations( Long.valueOf( promotion.getId() ), associationRequestCollection );
    if ( promotion.isNotificationRequired( PromotionEmailNotificationType.TD_PROGRESS_UPDATED ) )
    {
      MailingBatchHolder mailingBatchHolder = getMailingBatchHolderForProgressImportProcess();
      Round round = teamService.getRound( promotion.getId(), roundNumber );
      Set<Participant> participants = getPaxWhoHadProgress( importFileTypeCode );

      // send pax emails
      sendPaxEmails( promotion, round, participants, mailingBatchHolder, importFile );

      // send manager emails
      sendManagerEmails( promotion, round, progressEndDate, participants );
    }

    // send summary message to admin
    sendSummaryMessage();
  }

  public void setImportFileId( String importFileId )
  {
    if ( importFileId != null )
    {
      this.importFileId = new Long( importFileId );
    }
  }

  public void setImportService( ImportService importService )
  {
    this.importService = importService;
  }

  private MailingBatchHolder getMailingBatchHolderForProgressImportProcess()
  {
    Promotion promotion = importService.getImportFile( new Long( importFileId ) ).getPromotion();
    MailingBatchHolder batchHolder = new MailingBatchHolder();// fix bug #37434
    if ( getMailingService().isBatchEmailEnabled() )
    {
      batchHolder.setPaxProgressMailingBatch( applyBatch( promotion.getName() + " Import Pax Progress " ) );
    }
    return batchHolder;
  }

  private void doImport()
  {
    try
    {
      long timeBegin = System.currentTimeMillis();

      int counter = 1;

      while ( importService.importImportFile( importFileId, counter, getRunByUser() ) )
      {
        counter++;
      }

      long timeEnd = System.currentTimeMillis();
      long timeDiff = ( timeEnd - timeBegin ) / 1000;
      log.warn( "Time(in seconds) consumed to do progress load " + timeDiff );
      addComment( "Progress imported successfully." );
    }
    catch( Exception e )
    {
      addComment( "Progress import failed." );
      importService.setImportFileStatus( importFileId, ImportFileStatusType.IMPORT_FAILED );
      logErrorMessage( e );
      return;
    }
  }

  private void doRanking( Long promotionId, int roundNumber )
  {
    long timeBegin = System.currentTimeMillis();

    // recreate rankings after progress load.
    try
    {
      stackStandingService.createRankingForRound( promotionId, roundNumber );
      addComment( "Rankings creation successfull." );
    }
    catch( ServiceErrorException e )
    {
      addComment( "Rankings creation failed since it is already paid out or partially paid out." );
      log.error( e.getMessage(), e );
    }
    catch( Exception e )
    {
      addComment( "Rankings creation failed." );
      log.error( e.getMessage(), e );
    }

    long timeEnd = System.currentTimeMillis();
    long timeDiff = ( timeEnd - timeBegin ) / 1000;
    log.warn( "Time(in seconds) consumed to do ranking " + timeDiff );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private Set<Participant> getPaxWhoHadProgress( String importFileTypeCode )
  {
    long timeBegin = System.currentTimeMillis();
    Set<Participant> participants = new HashSet<Participant>();
    AssociationRequestCollection reqs = new AssociationRequestCollection();
    reqs.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    List records = importService.getAllRecords( importFileId, importFileTypeCode, new BaseAssociationRequest()
    {
      public void execute( Object domainObject )
      {
        ThrowdownProgressImportRecord record = (ThrowdownProgressImportRecord)domainObject;
        initialize( record.getImportRecordErrors() );
      }
    } );
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      ThrowdownProgressImportRecord record = (ThrowdownProgressImportRecord)iterator.next();
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }
      Participant pax = participantService.getParticipantByUserNameWithAssociations( record.getLoginId(), reqs );
      participants.add( pax );
    }
    long timeEnd = System.currentTimeMillis();
    long timeDiff = ( timeEnd - timeBegin ) / 1000;
    log.warn( "Time(in seconds) consumed to load participants who had progress " + timeDiff );
    return participants;
  }

  @SuppressWarnings( { "unchecked" } )
  private void sendPaxEmails( ThrowdownPromotion promotion, Round round, Set<Participant> participants, MailingBatchHolder mailingBatchHolder, ImportFile importFile )
  {
    long timeBegin = System.currentTimeMillis();
    try
    {
      for ( Participant user : participants )
      {
        AssociationRequestCollection arCollection = new AssociationRequestCollection();
        arCollection.add( new MatchTeamOutcomeAssociationRequest( MatchTeamOutcomeAssociationRequest.MATCH ) );
        arCollection.add( new MatchTeamOutcomeAssociationRequest( MatchTeamOutcomeAssociationRequest.MATCH_OUTCOMES ) );
        arCollection.add( new MatchTeamOutcomeAssociationRequest( MatchTeamOutcomeAssociationRequest.PROGRESS ) );
        MatchTeamOutcome outcome = teamService.getOutcomeForTeamInSpecificRound( user.getId(), round.getRoundNumber(), promotion.getId(), arCollection );
        sendPaxEmail( promotion, round, user, outcome, mailingBatchHolder, importFile );
      }
      long timeEnd = System.currentTimeMillis();
      long timeDiff = ( timeEnd - timeBegin ) / 1000;
      log.warn( "Time(in seconds) consumed to send email to participants " + timeDiff );
      addComment( "Emails to participants sent successfully." );
    }
    catch( Exception e )
    {
      addComment( "Emails to participants failed." );
      log.error( e.getMessage(), e );
      return;
    }
  }

  public void sendPaxEmail( ThrowdownPromotion promotion, Round round, Participant participant, MatchTeamOutcome outcome, MailingBatchHolder mailingBatchHolder, ImportFile importFile )
      throws ServiceErrorException
  {
    if ( participant.isActive() )
    {
      Match match = outcome.getMatch();
      match.setRound( round );
      MatchTeamOutcome currentPlayerOutcome = null;
      MatchTeamOutcome oppositionPlayerOutcome = null;
      BigDecimal shadowScore = null;
      for ( MatchTeamOutcome matchTeamOutcome : match.getTeamOutcomes() )
      {
        if ( matchTeamOutcome.getTeam().isShadowPlayer() )
        {
          oppositionPlayerOutcome = matchTeamOutcome;
        }
        else if ( participant.getId().equals( matchTeamOutcome.getTeam().getParticipant().getId() ) )
        {
          currentPlayerOutcome = matchTeamOutcome;
        }
        else
        {
          oppositionPlayerOutcome = matchTeamOutcome;
        }
      }

      MatchResolution resolution = throwdownService.resolveMatch( currentPlayerOutcome, oppositionPlayerOutcome );
      if ( oppositionPlayerOutcome.getTeam().isShadowPlayer() )
      {
        shadowScore = throwdownService.getShadowScore( round.getId(), currentPlayerOutcome.getTeam().getId() );
      }
      Message message = null;
      if ( resolution.getOutcomeResults( currentPlayerOutcome.getId() ).equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.WIN ) ) )
      {
        message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_PROGRESSUPDATED_LEAD_MESSAGE_CM_ASSET_CODE );
      }
      else if ( resolution.getOutcomeResults( currentPlayerOutcome.getId() ).equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.TIE ) ) )
      {
        message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_PROGRESSUPDATED_TIE_MESSAGE_CM_ASSET_CODE );
      }
      else if ( resolution.getOutcomeResults( currentPlayerOutcome.getId() ).equals( MatchTeamOutcomeType.lookup( MatchTeamOutcomeType.LOSS ) ) )
      {
        message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_PROGRESSUPDATED_TRAIL_MESSAGE_CM_ASSET_CODE );
      }

      if ( message != null )
      {
        StackStandingParticipant pax = stackStandingService.getHierarchyRankDetailsForPax( promotion.getId(), round.getRoundNumber(), participant.getId() );
        Integer eligiblePax = stackStandingService.getTotalUsersInHierarchyRanking( promotion.getId(), round.getRoundNumber() );
        eligiblePax = eligiblePax != null ? eligiblePax : 0;
        String progressEndDate = null;
        if ( importFile != null )
        {
          progressEndDate = DateUtils.toDisplayString( importFile.getProgressEndDate() );
        }

        MailingRecipient mailingRecipient = mailingService
            .buildMailingRecipientForThrowdownEmail( promotion,
                                                     participant,
                                                     match,
                                                     currentPlayerOutcome,
                                                     oppositionPlayerOutcome,
                                                     pax.getStanding(),
                                                     null,
                                                     eligiblePax,
                                                     pax.getStackStandingFactor(),
                                                     null,
                                                     progressEndDate,
                                                     shadowScore );

        // Create mailing object
        Mailing mailing = new Mailing();
        if ( mailingBatchHolder != null )
        {
          mailing.setMailingBatch( mailingBatchHolder.getPaxProgressMailingBatch() );
        }
        mailing.setMessage( message );
        mailing.addMailingRecipient( mailingRecipient );
        mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
        mailing.setSender( "Throwdown Promotion" );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setGuid( GuidUtils.generateGuid() );
        mailingService.submitMailing( mailing, null );
      }
    }
  }

  @SuppressWarnings( "unchecked" )
  private void sendManagerEmails( ThrowdownPromotion promotion, Round round, Date progressEndDate, Set<Participant> participants )
  {
    try
    {
      long timeBegin = System.currentTimeMillis();
      AssociationRequestCollection reqs = new AssociationRequestCollection();
      reqs.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );

      List<Long> paxsProgressLoaded = new ArrayList<Long>();
      for ( Participant pax : participants )
      {
        paxsProgressLoaded.add( pax.getId() );
      }
      List<Long> paxs = teamService.getPaxPlayingPromotionInRound( promotion.getId(), round.getRoundNumber() );
      for ( Long userId : paxs )
      {
        if ( !paxsProgressLoaded.contains( userId ) )
        {
          participants.add( participantService.getParticipantByIdWithAssociations( userId, reqs ) );
        }
      }

      Map<User, Set<Participant>> managers = participantService.getManagerForCompetitorAudience( participants );

      if ( managers != null )
      {
        for ( User user : managers.keySet() )
        {
          sendManagerEmail( promotion, round, progressEndDate, user, managers.get( user ) );
        }
      }

      long timeEnd = System.currentTimeMillis();
      long timeDiff = ( timeEnd - timeBegin ) / 1000;
      log.warn( "Time(in seconds) consumed to send email to managers " + timeDiff );
      addComment( "Emails to managers sent successfully." );
    }
    catch( Exception e )
    {
      addComment( "Emails to managers failed." );
      log.error( e.getMessage(), e );
      return;
    }
  }

  public void sendManagerEmail( ThrowdownPromotion promotion, Round round, Date progressEndDate, User manager, Set<Participant> participants )
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.THROWDOWN_MANAGER_MATCH_PROGRESS_UPDATE_MESSAGE_CM_ASSET_CODE );
    StringBuilder sb = new StringBuilder();
    String localeCode = null;
    if ( manager != null && manager.getLanguageType() != null )
    {
      localeCode = manager.getLanguageType().getCode();
    }
    else
    {
      localeCode = systemVariableService.getDefaultLanguage().getStringVal();
    }
    Locale locale = CmsUtil.getLocale( localeCode );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "participant.throwdownstats", locale );

    sb.append( "<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\">" );
    sb.append( "<tr><td><b>" + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "NAME" ) ) + "</b></td><td><b>"
        + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "ROUND" ) ) + " " + round.getRoundNumber() + " "
        + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "TOTAL" ) ) + "</b></td><td><b>"
        + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "ALL_ROUND_CUMULATIVE_TOTAL" ) ) + "</b></td><td><b>"
        + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "OVERALL_RANKING" ) ) + "</b></td></tr>" );
    for ( Participant participant : participants )
    {
      MatchTeamOutcome participantOutcome = teamService.getOutcomeForTeamInSpecificRound( participant.getId(), round.getRoundNumber(), promotion.getId() );
      participantOutcome.setPromotion( promotion );
      BigDecimal progress = participantOutcome.getCurrentValueWithPrecisionAndRounding();
      StackStandingParticipant pax = stackStandingService.getHierarchyRankDetailsForPax( promotion.getId(), round.getRoundNumber(), participant.getId() );

      sb.append( "<tr><td>" + participant.getNameFLNoComma() + "</td><td>" );
      /*
       * if ( ! ( "" ).equals( getBeforeUnitLabel( promotion ) ) ) { sb.append( getBeforeUnitLabel(
       * promotion ) ); sb.append( "&nbsp;" ); }
       */
      sb.append( progress );
      /*
       * if ( ! ( "" ).equals( getAfterUnitLabel( promotion ) ) ) { sb.append( "&nbsp;" );
       * sb.append( getAfterUnitLabel( promotion ) ); }
       */
      sb.append( "</td><td>" + pax.getStackStandingFactor() + "</td><td>" + pax.getStanding() + "</td></tr>" );
    }
    sb.append( "</table>" );

    MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForThrowdownEmail( promotion, sb.toString(), round, progressEndDate, manager );
    // Create mailing object
    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    mailing.setSender( "Throwdown Promotion" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setGuid( GuidUtils.generateGuid() );

    mailingService.submitMailing( mailing, null );
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private void sendSummaryMessage()
  {
    User recipientUser = getRunByUser();
    ImportFile importFile = importService.getImportFile( importFileId );

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "fileName", importFile.getFileName() );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    if ( importFile.getStatus().getCode().equals( ImportFileStatusType.IMPORT_FAILED ) )
    {
      objectMap.put( "importSuccess", "false" );
    }
    else
    {
      objectMap.put( "importSuccess", "true" );
    }
    // Compose the mailing
    Mailing mailing = composeMail( MessageService.THROWDOWN_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setStackStandingService( StackStandingService stackStandingService )
  {
    this.stackStandingService = stackStandingService;
  }

  public void setThrowdownService( ThrowdownService throwdownService )
  {
    this.throwdownService = throwdownService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  private String getAfterUnitLabel( ThrowdownPromotion promotion )
  {
    if ( promotion.getBaseUnit() != null && promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode().equals( BaseUnitPosition.UNIT_AFTER ) )
    {
      return promotion.getBaseUnitText();
    }
    else
    {
      return "";
    }
  }

  private String getBeforeUnitLabel( ThrowdownPromotion promotion )
  {
    if ( promotion.getBaseUnit() != null && promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode().equals( BaseUnitPosition.UNIT_BEFORE ) )
    {
      return promotion.getBaseUnitText();
    }
    else
    {
      return "";
    }
  }

}


package com.biperf.core.service.fileload.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.fileload.AwardLevelImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.purl.PurlRecipientCustomElement;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;

/**
 * AwardLevelImportStrategy.
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
 * <td>shanmuga</td>
 * <td>Feb 25, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AwardLevelImportStrategy extends ImportStrategy
{
  public static final String AWARD_LEVEL_USER_NAME = "admin.awardlevel.USER_NAME";
  public static final String AWARD_LEVEL = "admin.awardlevel.AWARD_LEVEL";
  public static final String FIRST_NAME = "admin.awardlevel.FIRST_NAME";
  public static final String LAST_NAME = "admin.awardlevel.LAST_NAME";
  public static final String COUNTRY_CODE = "admin.awardlevel.COUNTRY_CODE";
  public static final String EMAIL_ADDRESS = "admin.awardlevel.EMAIL_ADDRESS";

  public static final int STATUS_INIT = 0;
  public static final int STATUS_SUCCESS = 1;
  public static final int STATUS_PROGRAM_NOT_FOUND = 2;
  public static final int STATUS_AWARD_LEVEL_NOT_FOUND = 3;

  private static final Log logger = LogFactory.getLog( AwardLevelImportStrategy.class );

  private ParticipantService participantService;
  private SystemVariableService systemVariableService;
  private MailingService mailingService;
  private PromotionService promotionService;
  private ClaimService claimService;
  private MultimediaService multimediaService;
  private MerchLevelService merchLevelService;
  private AudienceService audienceService;
  private UserService userService;
  private PurlService purlService;
  private PurlContributorDAO purlContributorDAO;

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @param justForPaxRightNow
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the recordst to import.
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    // start out with the number of current records with errors
    int importRecordErrorCount = importFile.getImportRecordErrorCount();

    long counter = 0;
    logger.info( "processed record count: " + counter );

    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      AwardLevelImportRecord record = (AwardLevelImportRecord)iterator.next();
      boolean hasNoErrors = record.getImportRecordErrors().isEmpty();
      boolean foundError = false;

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateAwardLevelImportRecord( record, importFile.getPromotion() );

      if ( !errors.isEmpty() )
      {
        foundError = true;
        createAndAddImportRecordErrors( importFile, record, errors );
      }

      if ( foundError && hasNoErrors )
      {
        importRecordErrorCount++;
      }

    }

    importFile.setImportRecordErrorCount( importRecordErrorCount );
  }

  protected Collection validateAwardLevelImportRecord( AwardLevelImportRecord awardLevelRecord, Promotion promotion )
  {
    Collection errors = new ArrayList();

    AbstractRecognitionPromotion promo = (AbstractRecognitionPromotion)promotion;
    Set promoMerchCountries = promo.getPromoMerchCountries();
    int programAndAwardLevelValidateStatus = STATUS_INIT;

    checkRequired( awardLevelRecord.getUserName(), AWARD_LEVEL_USER_NAME, errors );
    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promo;
      if ( !recognitionPromotion.isIncludePurl() || awardLevelRecord.getAwardLevel() != null )
      {
        checkRequired( awardLevelRecord.getAwardLevel(), AWARD_LEVEL, errors );
      }

      if ( recognitionPromotion.isAwardActive() )
      {
        Participant pax = participantService.getParticipantByUserName( awardLevelRecord.getUserName() );
       // Node recipientNode = getRecipientNode( pax );
        UserNode recipientNode =userService.getPrimaryUserNode( pax.getId() );
        Node userPrimeNode=recipientNode.getNode();

        UserAddress userAddress = null;
        if ( pax != null )
        {
          userAddress = getUserService().getPrimaryUserAddress( pax.getId() );
        }
        // Participant with specified User Name must exist
        if ( pax == null )
        {
          errors.add( new ServiceError( "system.errors.UNKNOWN_USER_NAME" ) );
        }
        else
        {
          if ( !getAudienceService().isParticipantInSecondaryAudience( promotion, pax, null ) && !getAudienceService().isParticipantInPrimaryAudience( promotion, pax ) )
          {
            errors.add( new ServiceError( "system.errors.PAX_NOT_ELIGIBLE_RECEIVER" ) );
          }

          if ( recipientNode == null )
          {
            errors.add( new ServiceError( "admin.fileload.deposit.RECIPIENT_NODE_NOT_FOUND" ) );
          }
        }

        if ( recognitionPromotion.isIncludePurl() && awardLevelRecord.getAwardLevel() == null )
        {
          programAndAwardLevelValidateStatus = STATUS_SUCCESS;
        }
        else
        {
          String countryCode = null;
          if ( userAddress != null && userAddress.getAddress() != null && userAddress.getAddress().getCountry() != null )
          {
            countryCode = userAddress.getAddress().getCountry().getCountryCode();
          }
          String awardLevel = awardLevelRecord.getAwardLevel();
          programAndAwardLevelValidateStatus = validateProgramAndAwardLevel( promoMerchCountries, countryCode, awardLevel );
        }

        // If PURL recognition promotion
        if ( recognitionPromotion.isIncludePurl() )
        {
          // If award date is NULL
          if ( null == awardLevelRecord.getAwardDate() )
          {
            errors.add( new ServiceError( "admin.fileload.errors.PURL_AWARD_DATE_NULL" ) );
          }
          // If award date is NOT NULL, it should be a future date
          else
          {
            Date awardDate = DateUtils.toStartDate( awardLevelRecord.getAwardDate() );
            if ( !awardDate.after( new Date() ) )
            {
              errors.add( new ServiceError( "admin.fileload.errors.PURL_AWARD_DATE_INVALID" ) );
            }
            if ( recognitionPromotion.getSubmissionEndDate() != null )
            {
              if ( awardDate.after( recognitionPromotion.getSubmissionEndDate() ) )
              {
                errors.add( new ServiceError( "admin.fileload.errors.PURL_AWARD_DATE_BEFORE" ) );
              }
            }
          }
          Participant purlManager = getPurlService().getNodeOwnerForPurlRecipient( pax, userPrimeNode  );
          if ( null == purlManager )
          {
            errors.add( new ServiceError( "admin.fileload.errors.PURL_MANAGER_ERROR" ) );
          }
          if ( recognitionPromotion.isIncludeCelebrations() )
          {
            if ( awardLevelRecord.getAnniversaryNumberOfDaysOrYears() == null )
            {
              errors.add( new ServiceError( "admin.awardlevel.ANNIVERSARY_NUMBER_NULL" ) );
            }
          }

          // VALIDATE CUSTOM FORM ELEMENTS
          List<ClaimElement> claimElementList = buildClaimElementList( recognitionPromotion, awardLevelRecord );
          errors.addAll( claimService.validateClaimElements( claimElementList, userPrimeNode, recognitionPromotion, 0 ) );
        }

      }
      else
      {
        if ( awardLevelRecord.getAwardLevel() != null )
        {
          errors.add( new ServiceError( "system.errors.PROMOTION_AWARD_INVALID" ) );
        }
      }
    }
    if ( programAndAwardLevelValidateStatus == STATUS_INIT )
    {
      errors.add( new ServiceError( "system.errors.PROMOTION_INVALID_RECOGNITION" ) );
    }
    else if ( programAndAwardLevelValidateStatus == STATUS_PROGRAM_NOT_FOUND )
    {
      errors.add( new ServiceError( "system.errors.NO_PROGRAM_NUMBER" ) );
    }
    else if ( programAndAwardLevelValidateStatus == STATUS_AWARD_LEVEL_NOT_FOUND )
    {
      errors.add( new ServiceError( "system.errors.AWARD_LEVEL_INVALID" ) );
    }
    return errors;
  }

  private int validateProgramAndAwardLevel( Set promoMerchCountries, String countryCode, String awardLevel )
  {
    for ( Iterator promoMerchCountryIterator = promoMerchCountries.iterator(); promoMerchCountryIterator.hasNext(); )
    {
      // Find the program
      PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIterator.next();
      if ( promoMerchCountry.getCountry().getCountryCode().equalsIgnoreCase( countryCode ) )
      {
        if ( promoMerchCountry.getProgramId() == null )
        {
          return STATUS_PROGRAM_NOT_FOUND;
        }

        // Find the award level
        Collection levels = promoMerchCountry.getLevels();
        if ( levels != null )
        {
          for ( Iterator levelIter = levels.iterator(); levelIter.hasNext(); )
          {
            PromoMerchProgramLevel promoMerchProgramLevelLoop = (PromoMerchProgramLevel)levelIter.next();
            Long awardLevelLong = 0L;
            awardLevelLong = getAwardLevelNumeric( awardLevel );
            if ( awardLevelLong != null && awardLevelLong > 0L )
            {
              if ( promoMerchProgramLevelLoop != null && promoMerchProgramLevelLoop.getOrdinalPosition() == awardLevelLong )
              {
                return STATUS_SUCCESS;
              }
            }
          }
        }
      }
    }
    return STATUS_AWARD_LEVEL_NOT_FOUND;
  }

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    int errorCount = importFile.getImportRecordErrorCount();

    try
    {

      for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
      {
        AwardLevelImportRecord awardLevelRecord = (AwardLevelImportRecord)iterator.next();

        // if we find any errors then skip the record
        if ( !awardLevelRecord.getImportRecordErrors().isEmpty() )
        {
          continue;
        }

        if ( importFile.getDelayAwardDate() != null )
        {
          awardLevelRecord.setAwardDate( importFile.getDelayAwardDate() );
        }

        Participant recipient = getParticipantService().getParticipantByUserName( awardLevelRecord.getUserName() );
       // Node recipientNode = getRecipientNode( recipient );
        UserNode recipientNode =userService.getPrimaryUserNode( recipient.getId() );
        Node userPrimeNode=recipientNode.getNode();
        RecognitionPromotion promotion = (RecognitionPromotion)importFile.getPromotion();
        // If PURL recognition save PURL user info
        if ( promotion.isIncludePurl() )
        {
          PurlRecipient info = buildPurlRecipientInfo( awardLevelRecord, importFile, promotion, recipient, userPrimeNode );
          // celebration manager messages
          if ( promotion.isIncludeCelebrations() && promotion.isAllowOwnerMessage() )
          {
            CelebrationManagerMessage celebrationManagerMessage = claimService.populateAndSendCelebrationManagerMessages( promotion, null, info, recipient, null );
            info.setCelebrationManagerMessage( celebrationManagerMessage );
          }
          getPurlService().createPurlRecipient( info );
          if ( promotion.isPurlStandardMessageEnabled() )
          {
            savePurlStandardMessage( promotion, info );
          }
        }

        // otherwise submit the recognition claim
        else
        {
          boolean isValid = true;
          if ( !promotion.isSelfRecognitionEnabled() )
          {
            if ( awardLevelRecord.getUserName().equals( importFile.getSubmitter().getUserName() ) )
            {
              isValid = false;
              createAndAddImportRecordError( importFile, awardLevelRecord, new ServiceError( "awardgenerator.errors.GIVER_AND_RECEIVER_SAME" ) );
              errorCount++;
            }
          }
          Claim claim = null;
          if ( isValid )
          {
            claim = buildClaim( awardLevelRecord, importFile, promotion );
          }
          String recipientTimeZoneID = userService.getUserTimeZoneByUserCountry( recipient.getId() );
          Date recipientCurrentDate = DateUtils.applyTimeZone( new Date(), recipientTimeZoneID );
          Date recipientSendDate = null;
          Date sendDate = null;
          if ( importFile.getDelayAwardDate() != null )
          {
            sendDate = importFile.getDelayAwardDate();
          }
          else
          {
            sendDate = awardLevelRecord.getAwardDate();
          }

          if ( sendDate != null )
          {
            recipientSendDate = DateUtils.applyTimeZoneWithFirstTimeOfDay( sendDate, recipientTimeZoneID );
          }
          if ( promotion.isFileLoadEntry() && recipientSendDate != null && recipientSendDate.after( recipientCurrentDate ) )
          {
            // convert to system time zone to schedule claim
            if ( claim != null && claim.isAbstractRecognitionClaim() )
            {
              RecognitionClaim recognitionClaim = (RecognitionClaim)claim;
              String systemTimeZoneID = claimService.getDBTimeZone();
              Date deliveryDate = DateUtils.applyTimeZone( sendDate, systemTimeZoneID );
              RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
              if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isAllowOwnerMessage() )
              {
                CelebrationManagerMessage celebrationManagerMessage = getClaimService().populateAndSendCelebrationManagerMessages( recognitionPromotion, recognitionClaim, null, recipient, sendDate );
                recognitionClaim.setCelebrationManagerMessage( celebrationManagerMessage );
              }
              getClaimService().scheduleRecognition( recognitionClaim, deliveryDate );
            }
          }
          else if ( claim != null )
          {
            getClaimService().saveClaim( claim, null, null, false, true );
          }
        }

      }
      importFile.setImportRecordErrorCount( errorCount );
    }
    catch( Exception e )
    {
      logger.error( "Error during importing file", e );
      throw new BeaconRuntimeException( e );
    }

  }

  /**
   * Iterate through the promotion's custom elements and build a list of custom elements where the
   * value comes from the given award level record.  Note this will only build a list of max size 3 
   * because that is the maximum number of elements on the award level table
   */
  private static List<ClaimElement> buildClaimElementList( Promotion promotion, AwardLevelImportRecord awardLevelRecord )
  {
    List<ClaimElement> claimElementList = new ArrayList<ClaimElement>();

    Map<Integer, String> customElementMap = new HashMap<Integer, String>();
    customElementMap.put( 1, awardLevelRecord.getFormElement1() );
    customElementMap.put( 2, awardLevelRecord.getFormElement2() );
    customElementMap.put( 3, awardLevelRecord.getFormElement3() );

    int formElementNumber = 1;
    for ( ClaimFormStep claimFormStep : promotion.getClaimForm().getClaimFormSteps() )
    {
      for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
      {
        ClaimElement claimElement = new ClaimElement();
        claimElement.setClaimFormStepElement( claimFormStepElement );
        claimElement.setValue( customElementMap.get( formElementNumber ) );
        claimElementList.add( claimElement );
        formElementNumber++;
      }
    }

    return claimElementList;
  }

  private PurlRecipient buildPurlRecipientInfo( AwardLevelImportRecord awardLevelRecord, ImportFile importFile, RecognitionPromotion promotion, Participant recipient, Node recipientNode )
  {
    PurlRecipient info = new PurlRecipient();

    // Set the promotion
    info.setPromotion( promotion );

    // Set the participant
    info.setUser( recipient );
    info.setNode( recipientNode );

    // Set the submitter details
    info.setSubmitter( importFile.getSubmitter() );
    info.setSubmitterNode( importFile.getSubmitterNode() );

    // Set the invitation date, default to create date
    info.setInvitationStartDate( new Date() );
    info.setState( PurlRecipientState.lookup( PurlRecipientState.INVITATION ) );

    // Set the award date
    info.setAwardDate( awardLevelRecord.getAwardDate() );

    // Default preload contributors
    info.setShowDefaultContributors( true );

    // Set the award level
    UserAddress userAddress = getUserService().getPrimaryUserAddress( recipient.getId() );
    String countryCode = userAddress.getAddress().getCountry().getCountryCode();
    Set<PromoMerchCountry> promoMerchCountries = promotion.getPromoMerchCountries();
    Long awardLevelLong = 0L;
    awardLevelLong = getAwardLevelNumeric( awardLevelRecord.getAwardLevel() );
    PromoMerchProgramLevel awardLevel = getAwardLevel( countryCode, promoMerchCountries, awardLevelLong );
    info.setAwardLevel( awardLevel );

    if ( promotion.isIncludeCelebrations() ) // bug 62523
    {
      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() != null && promotion.getAnniversaryInYears().booleanValue() )
        {
          info.setAnniversaryNumberOfYears( awardLevelRecord.getAnniversaryNumberOfDaysOrYears() );
        }
        else
        {
          info.setAnniversaryNumberOfDays( awardLevelRecord.getAnniversaryNumberOfDaysOrYears() );
        }
      }
    }

    for ( ClaimElement claimElement : buildClaimElementList( promotion, awardLevelRecord ) )
    {
      PurlRecipientCustomElement customElement = new PurlRecipientCustomElement();
      customElement.setClaimFormStepElement( claimElement.getClaimFormStepElement() );
      customElement.setValue( claimElement.getValue() );
      info.addCustomElement( customElement );
    }

    return info;
  }

  private Claim buildClaim( AwardLevelImportRecord awardLevelRecord, ImportFile importFile, RecognitionPromotion promotion )
  {
    RecognitionClaim claim = new RecognitionClaim();

    Long cardId = importFile.getCard();
    String behavior = importFile.getBehavior();

    Card selectedCard = null;
    PromoRecognitionBehaviorType selectedBehaviour = null;
    if ( null != cardId && cardId.longValue() > -1 )
    {
      selectedCard = getMultimediaService().getCardById( cardId );
    }

    if ( null != behavior && StringUtils.isNotEmpty( behavior ) )
    {
      selectedBehaviour = PromoRecognitionBehaviorType.lookup( behavior );
    }

    String comments = importFile.getSubmitterComments();
    Participant submitter = importFile.getSubmitter();
    Node submitterNode = importFile.getSubmitterNode();
    Set<PromoMerchCountry> promoMerchCountries = promotion.getPromoMerchCountries();

    if ( StringUtils.isNotEmpty( awardLevelRecord.getComments() ) )
    {
      comments = awardLevelRecord.getComments();
    }
    else if ( comments == null || comments.equals( "" ) )
    {
      comments = " "; // This is required.
    }
    ClaimRecipient claimRecipient = new ClaimRecipient();

    Participant recipient = participantService.getParticipantByUserName( awardLevelRecord.getUserName() );
    UserAddress userAddress = getUserService().getPrimaryUserAddress( recipient.getId() );
    String countryCode = userAddress.getAddress().getCountry().getCountryCode();
    Long awardLevelLong = 0L;
    awardLevelLong = getAwardLevelNumeric( awardLevelRecord.getAwardLevel() );
    PromoMerchProgramLevel awardLevel = getAwardLevel( countryCode, promoMerchCountries, awardLevelLong );
    if ( null != awardLevel )
    {
      claimRecipient.setPromoMerchProgramLevel( awardLevel );
    }
    Node recipientNode = getRecipientNode( recipient );
    claimRecipient.setRecipient( recipient );
    claimRecipient.setNode( recipientNode );
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

    claim.addClaimRecipient( claimRecipient );
    claim.setCopyManager( importFile.isCopyManager() ); // Send an email copy to manager based on
                                                        // promotion settings
    claim.setSubmissionDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    claim.setPromotion( promotion );
    claim.setOpen( true );
    claim.setCopySender( false ); // Do not send an email copy to sender, as it would flood senders
                                  // email box
    claim.setSubmitter( submitter );
    claim.setNode( submitterNode );
    claim.setCard( selectedCard );
    claim.setBehavior( selectedBehaviour );
    claim.setSubmitterComments( comments );
    claim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( submitter ) );
    claim.setTeamId( claimService.getNextTeamId() );
    claim.setSource( RecognitionClaimSource.WEB );

    if ( promotion.isServiceAnniversary() )
    {
      if ( promotion.getAnniversaryInYears() != null && promotion.getAnniversaryInYears().booleanValue() )
      {
        claim.setAnniversaryNumberOfYears( awardLevelRecord.getAnniversaryNumberOfDaysOrYears() );
      }
      else
      {
        claim.setAnniversaryNumberOfDays( awardLevelRecord.getAnniversaryNumberOfDaysOrYears() );
      }
    }

    return claim;
  }

  private PromoMerchProgramLevel getAwardLevel( String countryCode, Set<PromoMerchCountry> promoMerchCountries, long awardLevelOrdPosition )
  {
    for ( PromoMerchCountry promoMerchCountry : promoMerchCountries )
    {
      if ( promoMerchCountry.getCountry().getCountryCode().equalsIgnoreCase( countryCode ) )
      {
        Collection<PromoMerchProgramLevel> levels = promoMerchCountry.getLevels();
        if ( levels != null )
        {
          for ( PromoMerchProgramLevel level : levels )
          {
            if ( level != null && level.getOrdinalPosition() == awardLevelOrdPosition )
            {
              return getMerchLevelService().getPromoMerchProgramLevelById( level.getId() );
            }
          }
        }
      }
    }

    return null;
  }

  private Node getRecipientNode( Participant recipient )
  {
    if ( null == recipient )
    {
      return null;
    }
    Node selectedRecipientNode = null;

    // Get all nodes the recipient is attached to
    for ( Iterator iter = recipient.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      // Pick initial active node
      if ( selectedRecipientNode == null && userNode.isActive().booleanValue() )
      {
        selectedRecipientNode = userNode.getNode();
      }
      // Compare and select the first created active recipient node
      else if ( userNode.isActive().booleanValue() && userNode.getNode().getAuditCreateInfo().getDateCreated().before( selectedRecipientNode.getAuditCreateInfo().getDateCreated() ) )
      {
        selectedRecipientNode = userNode.getNode();
      }
    }

    return selectedRecipientNode;
  }

  private Long getAwardLevelNumeric( String awardLevel )
  {
    Long awardLevelLong = 0L;
    if ( awardLevel != null )
    {
      try
      {
        awardLevelLong = Long.parseLong( awardLevel );
      }
      catch( NumberFormatException n )
      {
        awardLevelLong = 0L;
      }

    }
    return awardLevelLong;

  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public PromotionService getPromotionService()
  {
    return promotionService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public ClaimService getClaimService()
  {
    return claimService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public MultimediaService getMultimediaService()
  {
    return multimediaService;
  }

  public void setMultimediaService( MultimediaService multimediaService )
  {
    this.multimediaService = multimediaService;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

  public PurlService getPurlService()
  {
    return purlService;
  }

  private void savePurlStandardMessage( RecognitionPromotion promotion, PurlRecipient purlRecipient )
  {
    List<PurlContributorComment> comments = new LinkedList<PurlContributorComment>();
    PurlContributorComment contributorComment = new PurlContributorComment();
    contributorComment.setComments( purlRecipient.getPromotion().getPurlStandardMessage() );
    String videoUrl = createPurlStandardMessageVideoUrl( purlRecipient.getPromotion().getContentResourceCMCode() );
    if ( !StringUtil.isEmpty( videoUrl ) )
    {
      contributorComment.setVideoStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
      contributorComment.setVideoType( PurlContributorVideoType.lookup( PurlContributorVideoType.DIRECT ) );
      contributorComment.setVideoUrl( videoUrl );
    }
    String imageUrl = createPurlStandardMessageImageUrl( purlRecipient.getPromotion().getContentResourceCMCode() );
    contributorComment.setImageUrl( imageUrl );
    contributorComment.setMediaState( PurlMediaState.lookup( PurlMediaState.POSTED ) );
    contributorComment.setImageStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    contributorComment.setStatus( PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.ACTIVE ) );
    // setting default language type to admin PURL comment
    contributorComment.setCommentsLanguageType( LanguageType.lookup( systemVariableService.getDefaultLanguage().getStringVal() ) );
    comments.add( contributorComment );

    com.biperf.core.domain.purl.PurlContributor contributor = new com.biperf.core.domain.purl.PurlContributor();
    contributor.setFirstName( purlRecipient.getPromotion().getDefaultContributorName() );
    contributor.setLastName( " " );
    contributor.setPurlRecipient( purlRecipient );
    contributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );
    contributor.setAvatarUrl( purlRecipient.getPromotion().getDefaultContributorAvatar() );
    contributor.setAvatarState( PurlMediaState.lookup( PurlMediaState.STAGED ) );
    contributor.setComments( comments );
    purlContributorDAO.save( contributor );
  }

  private String createPurlStandardMessageImageUrl( String contentResourceCMCode )
  {
    List<QuizLearningDetails> purlStandardMessagePictureObjectsDetails = promotionService.getPurlStandardMessagePictureObjects( contentResourceCMCode );
    Iterator pictureObjectsItr = purlStandardMessagePictureObjectsDetails.iterator();
    String imagePicUrl = "";
    String videoUrl = "";
    String leftColumn = "";
    String uploadType = "image";
    while ( pictureObjectsItr.hasNext() )
    {
      QuizLearningDetails pictureObjectsDetail = (QuizLearningDetails)pictureObjectsItr.next();
      leftColumn = pictureObjectsDetail.getLeftColumn();
      if ( leftColumn.contains( "<p>" ) )
      {
        uploadType = "image";
        String s = "<img src=\"";
        int ix = leftColumn.indexOf( s ) + s.length();
        imagePicUrl = leftColumn.substring( ix, leftColumn.indexOf( "\"", ix + 1 ) );
      }
    }
    return imagePicUrl;
  }

  private String createPurlStandardMessageVideoUrl( String contentResourceCMCode )
  {
    String videoUrl = "";
    List<QuizLearningDetails> purlStandardMessageVideoObjectsDetails = promotionService.getPurlStandardMessagePictureObjects( contentResourceCMCode );
    if ( purlStandardMessageVideoObjectsDetails != null )
    {
      Iterator videoObjectsItr = purlStandardMessageVideoObjectsDetails.iterator();
      while ( videoObjectsItr.hasNext() )
      {
        QuizLearningDetails videoObjectsDetail = (QuizLearningDetails)videoObjectsItr.next();
        videoUrl = videoObjectsDetail.getVideoUrlMp4();
        if ( !StringUtil.isEmpty( videoUrl ) )
        {
          videoUrl = videoUrl.replace( ".mp4", "" );
          videoUrl = videoUrl.replace( ".webm", "" );
          videoUrl = videoUrl.replace( ".ogg", "" );
          videoUrl = videoUrl.replace( ".3gp", "" );
        }
      }
    }
    return videoUrl;
  }

  public void setPurlContributorDAO( PurlContributorDAO purlContributorDAO )
  {
    this.purlContributorDAO = purlContributorDAO;
  }
}

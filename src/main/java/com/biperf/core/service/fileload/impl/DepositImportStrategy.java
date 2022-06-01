/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/DepositImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.biperf.core.domain.enums.BillCodeNominationType;
import com.biperf.core.domain.enums.BillCodeRecognitionType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.fileload.DepositImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.journal.JournalBillCode;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.purl.PurlRecipientCustomElement;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PickListValueBean;

/*
 * DepositImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 13, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class DepositImportStrategy extends ImportStrategy
{
  public static final String DEPOSIT_USER_NAME = "deposit.label.USER_NAME";
  public static final String DEPOSIT_AWARD_AMOUNT = "deposit.label.AWARD_AMOUNT";
  public static final String ANNIVERSARY_YEARS = "deposit.label.ANNIVERSARY_YEARS";
  public static final String CUSTOM_VALUE = "customValue";
  public static final String DEPT_NAME = "department";
  public static final String ORG_UNIT_NAME = "orgUnitName";
  public static final String COUNTRY_CODE = "countryCode";
  public static final String LOGIN_ID = "userName";
  public static final String BILLING_CODE_NONE = "none";
  public static final String OPT_OUT_TEXT = "Opt Out Of Awards";

  private static final Log logger = LogFactory.getLog( DepositImportStrategy.class );

  private ParticipantService participantService;
  private JournalService journalService;
  private SystemVariableService systemVariableService;
  private MailingService mailingService;
  private PromotionService promotionService;
  private ClaimService claimService;
  private MultimediaService multimediaService;
  private PurlService purlService;
  private UserService userService;
  private AudienceService audienceService;
  private PurlContributorDAO purlContributorDAO;

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
    /* Remove logic added by bug 72152 */
    /*
     * boolean errorSubmitter = false; int importRecordErrorCount =
     * importFile.getImportRecordErrorCount(); long counter = 0; long importRecordTotalAmt = 0;
     * logger.info( "processed record count: " + counter ); if ( importFile.getRecognitionDeposit()
     * != null && importFile.getRecognitionDeposit().booleanValue() ) { if (
     * importFile.getSubmitter() != null ) { final BigDecimal US_MEDIA_VALUE =
     * getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES ); final
     * BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser(
     * importFile.getSubmitter().getId() ); Budget budget = null; BigDecimal
     * totalUnapprovedAwardAmount = new BigDecimal( 0 ); BigDecimal availBal = new BigDecimal( 0 );
     * importRecordTotalAmt = getPromotionService().getTotalImportPaxAwardQuantity(
     * importFile.getId() ); Promotion promotion = (Promotion)importFile.getPromotion(); if (
     * promotion.isBudgetUsed() || promotion.isCashBudgetUsed() ) { try { budget =
     * getPromotionService().getAvailableBudget( promotion.getId(),
     * importFile.getSubmitter().getId(), importFile.getSubmitterNode().getId() ); if (
     * promotion.getBudgetMaster().isCentralBudget() ) { if ( promotion.isRecognitionPromotion() &&
     * ( (RecognitionPromotion)promotion ).isIncludePurl() ) { totalUnapprovedAwardAmount =
     * getPromotionService().getTotalUnapprovedAwardQuantityPurl( promotion.getId(), null, null ); }
     * else { totalUnapprovedAwardAmount = getPromotionService().getTotalUnapprovedAwardQuantity(
     * promotion.getId(), null, null ); } } else if (
     * promotion.getBudgetMaster().isParticipantBudget() ) { if ( promotion.isRecognitionPromotion()
     * && ( (RecognitionPromotion)promotion ).isIncludePurl() ) { totalUnapprovedAwardAmount =
     * getPromotionService().getTotalUnapprovedAwardQuantityPurl( promotion.getId(),
     * importFile.getSubmitter().getId(), null ); } else { totalUnapprovedAwardAmount =
     * getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(),
     * importFile.getSubmitter().getId(), null ); } } else if (
     * promotion.getBudgetMaster().isNodeBudget() ) { if ( promotion.isRecognitionPromotion() && (
     * (RecognitionPromotion)promotion ).isIncludePurl() ) { totalUnapprovedAwardAmount =
     * getPromotionService().getTotalUnapprovedAwardQuantityPurl( promotion.getId(), null,
     * importFile.getSubmitterNode().getId() ); } else { totalUnapprovedAwardAmount =
     * getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(), null,
     * importFile.getSubmitterNode().getId() ); } } if ( budget != null ) { availBal =
     * BudgetUtils.applyMediaConversion( budget.getCurrentValue().subtract(
     * totalUnapprovedAwardAmount ), US_MEDIA_VALUE, USER_MEDIA_VALUE ); } if ( availBal.subtract(
     * new BigDecimal( importRecordTotalAmt ) ).compareTo( BigDecimal.ZERO ) < 0 ) { errorSubmitter
     * = true; } if ( errorSubmitter ) { for ( Iterator iterator = records.iterator();
     * iterator.hasNext(); ) { DepositImportRecord record = (DepositImportRecord)iterator.next(); if
     * ( !record.getImportRecordErrors().isEmpty() ) { continue; } createAndAddImportRecordError(
     * importFile, record, new ServiceError(
     * ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION,
     * importFile.getPromotion().getName() ) ); importRecordErrorCount++; }
     * importFile.setImportRecordErrorCount( importRecordErrorCount ); } } catch(
     * BeaconRuntimeException e ) { throw e; } } } } if ( !errorSubmitter ) {
     */

    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      DepositImportRecord depositRecord = (DepositImportRecord)iterator.next();

      // if we find any errors then skip the record
      if ( !depositRecord.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      if ( importFile.getDelayAwardDate() != null )
      {
        depositRecord.setAwardDate( importFile.getDelayAwardDate() );
      }

      // Check to see if the deposit type is recognition deposit
      // If it is a recognition deposit, submit a claim
      if ( importFile.getRecognitionDeposit() != null && importFile.getRecognitionDeposit().booleanValue() )
      {
        try
        {
          Participant recipient = getParticipantService().getParticipantByUserName( depositRecord.getUserName() );
          //Node recipientNode = getRecipientNode( recipient );
          UserNode recipientNode =userService.getPrimaryUserNode( recipient.getId() );
          Node userPrimeNode=recipientNode.getNode();

          RecognitionPromotion promotion = (RecognitionPromotion)importFile.getPromotion();
          // If PURL recognition save PURL user info
          if ( promotion.isIncludePurl() )
          {
            PurlRecipient info = buildPurlRecipientInfo( depositRecord, importFile, promotion, recipient, userPrimeNode );
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
            Claim claim = buildClaim( depositRecord, importFile, promotion, recipient, userPrimeNode );

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
              sendDate = depositRecord.getAwardDate();
            }
            if ( sendDate != null && importFile.getDelayAwardDate() != null )
            {
              recipientSendDate = DateUtils.applyTimeZoneWithFirstTimeOfDay( sendDate, recipientTimeZoneID );
            }
            else
            {
              recipientSendDate = sendDate;
            }
            if ( promotion.isFileLoadEntry() && recipientSendDate != null && recipientSendDate.after( recipientCurrentDate ) )
            {
              // convert to system time zone to schedule claim
              if ( claim.isRecognitionClaim() )
              {
                RecognitionClaim recognitionClaim = (RecognitionClaim)claim;
                String systemTimeZoneID = getClaimService().getDBTimeZone();
                Date deliveryDate;
                if ( importFile.getDelayAwardDate() != null )
                {
                  deliveryDate = DateUtils.applyTimeZone( sendDate, systemTimeZoneID );
                }
                else
                {
                  deliveryDate = sendDate;
                }

                RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
                if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isAllowOwnerMessage() )
                {
                  CelebrationManagerMessage celebrationManagerMessage = getClaimService().populateAndSendCelebrationManagerMessages( recognitionPromotion,
                                                                                                                                     recognitionClaim,
                                                                                                                                     null,
                                                                                                                                     recipient,
                                                                                                                                     sendDate );
                  recognitionClaim.setCelebrationManagerMessage( celebrationManagerMessage );
                }
                getClaimService().scheduleRecognition( recognitionClaim, deliveryDate );
              }
            }
            else
            {
              // bug 73458 - Added FileLoadDeposit flag so the deposit will happen immediately and
              // we can display any errors in the import_record_error table
              claim.setFileLoadDeposit( Boolean.TRUE );
              getClaimService().saveClaim( claim, null, null, false, true );
            }
          }
        }
        catch( Exception e ) // bug 73458
        {
          logger.error( "Error depositing claim for import_record_id :" + depositRecord.getId(), e );
          String depositFailedMessage = getServiceErrorText( e );
          createAndAddImportRecordError( importFile, depositRecord, new ServiceError( "admin.fileload.deposit.DEPOSIT_FAILED", null, depositFailedMessage ) );
          errorCount++;
        }
      }
      // If it is a non recognition deposit, only save journal details
      else
      {
        try // bug 73458
        {
          if ( depositRecord.getAwardAmount() == null || depositRecord.getAwardAmount().intValue() == 0 ) // Bug
                                                                                                          // 70472
          {
            createAndAddImportRecordError( importFile, depositRecord, new ServiceError( "system.errors.INVALID", "AWARD AMOUNT" ) );
            errorCount++;
          }
          else
          {
            Journal journalEntry = buildJournalEntry( depositRecord, importFile.getPromotion() );
            journalEntry = setJournalBillingCodes( journalEntry );

            // Set isFileLoadDeposit to True sothat we skip text message
            journalEntry.setFileLoadDeposit( Boolean.TRUE );
            Journal journal = getJournalService().saveAndDepositJournalEntry( journalEntry );
            // check for the deposit failure or enrollment issues if it is awardsperqs promotion
            // and
            // the
            // journal
            // status is approve. If it is then add specific ImportRecordError so that you can
            // check
            // for
            // the same while counting
            // number of enrollment failures
            if ( journal != null && PromotionAwardsType.POINTS.equals( journal.getPromotion().getAwardType().getCode() ) )
            {
              if ( JournalStatusType.lookup( JournalStatusType.APPROVE ).equals( journal.getJournalStatusType() ) )
              {
                createAndAddImportRecordError( importFile, depositRecord, new ServiceError( "admin.fileload.deposit.DEPOSIT_FAILED" ) );
                errorCount++;
              }
            }

            // send an email, for posted journal
            if ( journal != null && JournalStatusType.lookup( JournalStatusType.POST ).equals( journal.getJournalStatusType() ) )
            {
              if ( importFile.getMessage() != null ) // Fix 18956
              {
                sendEmail( depositRecord, importFile );
              }
            }
          }
        }
        catch( Exception e ) // bug 73458
        {
          logger.error( "Error depositing journal for import_record_id :" + depositRecord.getId(), e );
          String depositFailedMessage = getServiceErrorText( e );
          createAndAddImportRecordError( importFile, depositRecord, new ServiceError( "admin.fileload.deposit.DEPOSIT_FAILED", null, depositFailedMessage ) );
          errorCount++;
        }
      }
    }
    /* Remove logic added by bug 72152 */
    /*
     * } if ( errorCount > 0 && !errorSubmitter ) { importFile.setImportRecordErrorCount( errorCount
     * ); }
     */
    if ( errorCount > 0 )
    {
      importFile.setImportRecordErrorCount( errorCount );
    }
  }

  private void savePurlStandardMessage( RecognitionPromotion promotion, PurlRecipient purlRecipient )
  {
    List<PurlContributorComment> comments = new LinkedList<PurlContributorComment>();
    PurlContributorComment contributorComment = new PurlContributorComment();
    contributorComment.setComments( purlRecipient.getPromotion().getPurlStandardMessage() );
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

  private PurlRecipient buildPurlRecipientInfo( DepositImportRecord depositRecord, ImportFile importFile, RecognitionPromotion promotion, Participant recipient, Node recipientNode )
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
    info.setAwardDate( depositRecord.getAwardDate() );

    // Default preload contributors
    info.setShowDefaultContributors( true );

    // Set the award amount
    info.setAwardAmount( depositRecord.getAwardAmount() );

    if ( promotion.isIncludeCelebrations() )
    {
      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() != null && promotion.getAnniversaryInYears().booleanValue() )
        {
          info.setAnniversaryNumberOfYears( depositRecord.getAnniversaryNumberOfDaysOrYears() );
        }
        else
        {
          info.setAnniversaryNumberOfDays( depositRecord.getAnniversaryNumberOfDaysOrYears() );
        }
      }
    }

    for ( ClaimElement claimElement : buildClaimElementList( promotion, depositRecord ) )
    {
      PurlRecipientCustomElement customElement = new PurlRecipientCustomElement();
      customElement.setClaimFormStepElement( claimElement.getClaimFormStepElement() );
      customElement.setValue( claimElement.getValue() );
      info.addCustomElement( customElement );
    }

    return info;
  }

  private Claim buildClaim( DepositImportRecord depositRecord, ImportFile importFile, RecognitionPromotion promotion, Participant recipient, Node recipientNode )
  {
    RecognitionClaim claim = new RecognitionClaim();

    Long cardId = importFile.getCard();
    Card selectedCard = null;
    Long selectedCertificateId = null;
    if ( null != importFile.getCertificateId() )
    {
      selectedCertificateId = importFile.getCertificateId();
    }
    if ( null != cardId && cardId.longValue() > -1 )
    {
      selectedCard = getMultimediaService().getCardById( cardId );
    }
    String comments = importFile.getSubmitterComments();
    Participant submitter = importFile.getSubmitter();
    Node submitterNode = importFile.getSubmitterNode();

    if ( comments == null || comments.equals( "" ) )
    {
      if ( depositRecord.getComments() != null && !depositRecord.getComments().equals( "" ) )
      {
        comments = depositRecord.getComments();
      }
      else
      {
        comments = " "; // This is required.

      }
    }

    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setRecipient( recipient );
    if ( claimRecipient.getRecipient().getOptOutAwards() )
    {
      claimRecipient.setAwardQuantity( new Long( 0 ) );
    }
    else
    {
      claimRecipient.setAwardQuantity( new Long( depositRecord.getAwardAmount().longValue() ) );
    }
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
    claim.setCertificateId( selectedCertificateId );
    claim.setTeamId( claimService.getNextTeamId() );
    if ( promotion.isServiceAnniversary() )
    {
      if ( promotion.getAnniversaryInYears() != null && promotion.getAnniversaryInYears().booleanValue() )
      {
        claim.setAnniversaryNumberOfYears( depositRecord.getAnniversaryNumberOfDaysOrYears() );
      }
      else
      {
        claim.setAnniversaryNumberOfDays( depositRecord.getAnniversaryNumberOfDaysOrYears() );
      }
    }

    if ( null != importFile.getBehavior() && !importFile.getBehavior().equals( "" ) )
    {
      PromotionBehaviorType promotionBehaviorType = PromoRecognitionBehaviorType.lookup( importFile.getBehavior() );
      claim.setBehavior( promotionBehaviorType );
    }
    claim.setSubmitterComments( comments );
    claim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( submitter ) );
    claim.setSource( RecognitionClaimSource.WEB );

    return claim;
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
      // Pick primary node id
      if ( selectedRecipientNode == null && userNode.isActive().booleanValue() && userNode.getIsPrimary() )
      {
        selectedRecipientNode = userNode.getNode();
      }
    }

    return selectedRecipientNode;
  }

  private void sendEmail( DepositImportRecord depositRecord, ImportFile importFile )
  {
    Participant pax = getParticipantService().getParticipantByUserName( depositRecord.getUserName() );
    Map objectMap = new HashMap();
    // Add the personalization data to the objectMap
    objectMap.put( "mediaType", importFile.getPromotion().getAwardType().getAbbr() );
    if ( pax.getOptOutAwards() )
    {
      objectMap.put( "awardAmount", String.valueOf( new Long( 0 ) ) );
    }
    else
    {
      objectMap.put( "awardAmount", depositRecord.getAwardAmount() );
    }
    Promotion promotion = importFile.getPromotion();
    String promotionName = importFile.getPromotion().getName();
    if ( pax != null && pax.getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( promotion.getPromoNameAssetCode(), pax.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }
    objectMap.put( "programName", promotionName );
    // Message Library Insert Field is using promotionName for this token
    objectMap.put( "promotionName", importFile.getPromotion().getName() );
    objectMap.put( "lastName", pax.getLastName() );
    objectMap.put( "firstName", pax.getFirstName() );
    objectMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "websiteUrl",
                   systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                       + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_URL ).getStringVal() );
    if ( depositRecord.getAwardAmount().intValue() > 1 )
    {
      objectMap.put( "manyAwardAmount", "true" );
    }

    // Compose the mailing
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    Timestamp deliveryDate = null;
    if ( importFile.getDelayAwardDate() != null )
    {
      deliveryDate = new Timestamp( importFile.getDelayAwardDate().getTime() );
    }
    else
    {
      // Delivery Date - Assumes Now (i.e. immediate delivery)
      deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    }
    mailing.setDeliveryDate( deliveryDate );

    mailing.setMailingType( MailingType.lookup( MailingType.PROCESS_EMAIL ) );

    mailing.setMessage( importFile.getMessage() );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = systemVariableService.getDefaultLanguage().getStringVal();
    if ( pax.getLanguageType() != null )
    {
      localeCode = pax.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( pax );

    // Add mailing recipient
    mailing.addMailingRecipient( mailingRecipient );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, objectMap );
    }
    catch( Exception e )
    {
      logger.error( "Unable to submitMailing.  Caught exception: " + e.toString() );
    }
  }

  private Journal buildJournalEntry( DepositImportRecord depositRecord, Promotion promotion )
  {
    Journal journalEntry = new Journal();
    journalEntry.setGuid( GuidUtils.generateGuid() );
    Participant pax = getParticipantService().getParticipantByUserName( depositRecord.getUserName() );
    journalEntry.setParticipant( pax );
    if ( pax != null )
    {
      journalEntry.setAccountNumber( pax.getAwardBanqNumber() );

    }

    // Bug 53489 set promotion-name based on recipient Locale
    String promotionName = promotion.getPromotionName();
    if ( pax != null && pax.getLanguageType() != null )
    {
      promotionName = getPromotionService().getPromotionNameByLocale( promotion.getPromoNameAssetCode(), pax.getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? promotion.getPromotionName() : promotionName;
    }

    journalEntry.setTransactionDate( new Date() );
    journalEntry.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );

    journalEntry.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    if ( pax.getOptOutAwards() )
    {
      journalEntry.setTransactionAmount( new Long( 0 ) );
      journalEntry.setTransactionDescription( promotionName + "-" + OPT_OUT_TEXT );
    }
    else
    {
      journalEntry.setTransactionAmount( new Long( depositRecord.getAwardAmount().longValue() ) );
      journalEntry.setTransactionDescription( promotionName );
    }
    journalEntry.setComments( promotion.getName() );
    journalEntry.setPromotion( promotion );
    journalEntry.setJournalType( Journal.AWARD );
    journalEntry.setAwardPayoutType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );

    return journalEntry;
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

    boolean isAllActivePaxPromo = isAllActivePaxPromo( importFile.getPromotion() );

      // 43392 -CO - Allow deposits for inactive/terms - start
     String promoId = systemVariableService.getPropertyByName( SystemVariableService.COKE_INACTIVE_DEPOSIT_PROMO_IDS ).getStringVal();
     List<String> promoIdsList = new ArrayList<>();
     String[] promoIds = promoId != null && !promoId.equals( "" ) ? promoId.split( "," ) : null;
     if ( promoIds != null && promoIds.length > 0 )
     {
       promoIdsList.addAll( Arrays.asList( promoIds ) );
     }
     // 43392 -CO - Allow deposits for inactive/terms - end
    
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      DepositImportRecord record = (DepositImportRecord)iterator.next();
      boolean hasNoErrors = record.getImportRecordErrors().isEmpty();
      boolean foundError = false;

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateDepositImportRecord( record, importFile, isAllActivePaxPromo, promoIdsList );

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

  /**
  * Checks if the promotion uses audience of all active pax
  * @param promo
  * @return boolean
  */
  private boolean isAllActivePaxPromo( Promotion promo )
  {
    boolean allActivePaxPromo = false;
    if ( promo.isRecognitionPromotion() || promo.isNominationPromotion() )
    {
      allActivePaxPromo = getParticipantService().isAllActivePax( promo, false );
    }
    else if ( promo.isQuizPromotion() )
    {
      allActivePaxPromo = getParticipantService().isAllActivePax( promo, true );
    }
    else if ( promo.isProductClaimPromotion() && !PromotionPayoutType.TIERED.equals( ( (ProductClaimPromotion)promo ).getPayoutType().getCode() ) )
    {
      allActivePaxPromo = getParticipantService().isAllActivePax( promo, true );
    }
    return allActivePaxPromo;
  }

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

  protected Collection validateDepositImportRecord( DepositImportRecord depositRecord,
                                                    ImportFile importFile,
                                                    boolean isAllActivePaxPromo,
                                                    List<String> promoIdsList) 
  {
    Promotion promotion = importFile.getPromotion();

    Collection errors = new ArrayList();
    checkRequired( depositRecord.getUserName(), DEPOSIT_USER_NAME, errors );
    Participant pax = participantService.getParticipantByUserName( depositRecord.getUserName() );
    //Node recipientNode = getRecipientNode( pax );
    // Participant with specified User Name must exist
    if ( pax == null )
    {
      errors.add( new ServiceError( "system.errors.UNKNOWN_USER_NAME" ) );
    }
    else
    {
    	UserNode recipientNode =userService.getPrimaryUserNode( pax.getId() );
    	Node userPrimeNode=recipientNode.getNode();
      // Participant must be in Active status
       if ( pax.getStatus() == null ) 
       {
         errors.add( new ServiceError( "system.errors.PAX_NOT_ACTIVE" ) );
       }
       else if ( pax.getStatus() != null )
       {
         // 43392 -CO - Allow deposits for inactive/terms -start
         boolean inactiveEliigblePax = false;
         if ( pax.getStatus().getCode().equals( "inactive" ) )
         {
          Date currentTermDate = participantService.getCurrentParticipantEmployerTermDate( pax.getId() );
           // the user is inactive AND has a termination date
          if ( promotion.isFileLoadEntry() && promoIdsList.contains( String.valueOf( promotion.getId() ) ) && currentTermDate != null )
          {
            inactiveEliigblePax = true;
          }
          else if ( promotion.isFileLoadEntry() && promoIdsList.contains( String.valueOf( promotion.getId() ) ) 
              && currentTermDate == null && pax.isTermsAccepted() == false )
          {
            inactiveEliigblePax = false;
          }
          else
          {
            inactiveEliigblePax = false;
          }
           
          if ( !inactiveEliigblePax )
      {
        errors.add( new ServiceError( "system.errors.PAX_NOT_ACTIVE" ) );
      }
          // 43392 -CO - Allow deposits for inactive/terms end
         }
       }
      else if ( pax.getSuspensionStatus() != null
           && ( pax.getSuspensionStatus().equals( ParticipantSuspensionStatus.lookup( "susdep" ) ) || pax
               .getSuspensionStatus().equals( ParticipantSuspensionStatus.lookup( "susall" ) ) ) )
      {
        // Participant must not have Suspension Status of Suspend All or Suspend Deposits
        errors.add( new ServiceError( "system.errors.PAX_SUSPEND_DEPOSIT" ) );
      }
      else
      {
        if ( !promotion.isRecognitionPromotion() || ! ( (RecognitionPromotion)promotion ).isIncludePurl() || depositRecord.getAwardAmount().longValue() > 0 )
        {
          checkRequired( depositRecord.getAwardAmount(), DEPOSIT_AWARD_AMOUNT, errors );
        }

        if ( depositRecord.getAwardAmount() != null )
        { // don't continue if award amount is null
          if ( depositRecord.getAwardAmount().scale() > 0 )
          {
            // Constraint: An award amount must be a whole number.
            errors.add( new ServiceError( "admin.fileload.errors.AMOUNT_NOT_WHOLE_NUMBER" ) );
          }
          else
          {
            // recognition specific validations
            if ( promotion.isRecognitionPromotion() )
            {
              RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
              // make sure the award to be deposited confirm with the promotion award setup
              if ( recognitionPromotion.isAwardActive() )
              {
                if ( recognitionPromotion.isAwardAmountTypeFixed() )
                {
                   if ( recognitionPromotion.getAwardAmountFixed().intValue() != depositRecord
                       .getAwardAmount().intValue() )
                  {
                     errors
                         .add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_AWARD_AMOUNT_INVALID,
                                                 String.valueOf( recognitionPromotion
                                                     .getAwardAmountFixed().intValue() ) ) );
                  }
                }
                else
                {
                  int awardAmount = depositRecord.getAwardAmount().intValue();
                   if ( recognitionPromotion.getAwardAmountMin() != null
                       && recognitionPromotion.getAwardAmountMax() != null )
                  {
                    int minAmount = recognitionPromotion.getAwardAmountMin().intValue();
                    int maxAmount = recognitionPromotion.getAwardAmountMax().intValue();
                    if ( ! ( awardAmount >= minAmount && awardAmount <= maxAmount ) )
                    {
                       errors
                           .add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_AWARD_AMOUNT_NOT_IN_RANGE,
                                                   String.valueOf( minAmount ),
                                                   String.valueOf( maxAmount ) ) );
                    }
                  }
                }
                // If pax is in secondary audience (recognition receiver) then pax is eligible
                if ( !isAllActivePaxPromo && !audienceService.isParticipantInSecondaryAudience( promotion, pax, null ) )
                {
                  errors.add( new ServiceError( "system.errors.PAX_NOT_ELIGIBLE_RECEIVER" ) );
                }
              }
              else
              {
                if ( depositRecord.getAwardAmount().intValue() > 0 )
                {
                  errors.add( new ServiceError( "system.errors.PROMOTION_AWARD_INVALID" ) );
                }
              }

              if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isServiceAnniversary() )
              {
                checkRequired( depositRecord.getAnniversaryNumberOfDaysOrYears(), ANNIVERSARY_YEARS, errors );
              }

              if ( importFile.getRecognitionDeposit() != null && importFile.getRecognitionDeposit().booleanValue() )
              {
                if ( recipientNode == null )
                {
                  errors.add( new ServiceError( "admin.fileload.deposit.RECIPIENT_NODE_NOT_FOUND" ) );
                }
              }

              // If PURL recognition promotion
              if ( recognitionPromotion.isIncludePurl() )
              {
                // If award date is NULL
                if ( null == depositRecord.getAwardDate() )
                {
                  errors.add( new ServiceError( "admin.fileload.errors.PURL_AWARD_DATE_NULL" ) );
                }
                // If award date is NOT NULL, it should be a future date
                else
                {
                  Date awardDate = DateUtils.toStartDate( depositRecord.getAwardDate() );
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
                Participant purlManager = getPurlService().getNodeOwnerForPurlRecipient( pax, userPrimeNode );
                if ( null == purlManager )
                {
                  errors.add( new ServiceError( "admin.fileload.errors.PURL_MANAGER_ERROR" ) );
                }

                // VALIDATE CUSTOM FORM ELEMENTS
                List<ClaimElement> claimElementList = buildClaimElementList( recognitionPromotion, depositRecord );
                errors.addAll( claimService.validateClaimElements( claimElementList, userPrimeNode, recognitionPromotion, 0 ) );

                // validate form element1 value if purl with celebration enabled
                if ( recognitionPromotion.isIncludePurl() )
                {
                  if ( claimElementList != null && claimElementList.size() > 0 )
                  {
                    ClaimElement claimElement = claimElementList.get( 0 );
                    String formElementValue = claimElement.getValue();
                    if ( formElementValue != null && !formElementValue.matches( "\\d.*" ) )
                    {
                      errors.add( new ServiceError( "admin.fileload.errors.FORM_ELEMENT_INVALID_VALUE" ) );
                    }
                  }
                }

              }
            }
            // nomination specific validations
            else if ( promotion.isNominationPromotion() )
            {
              NominationPromotion nominationPromotion = (NominationPromotion)promotion;
              // make sure the award to be deposited confirm with the promotion award setup
              if ( nominationPromotion.isAwardActive() )
              {
                if ( nominationPromotion.isAwardAmountTypeFixed() )
                {
                  if ( nominationPromotion.getAwardAmountFixed().intValue() != depositRecord.getAwardAmount().intValue() )
                  {
                    errors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_AWARD_AMOUNT_INVALID, String.valueOf( nominationPromotion.getAwardAmountFixed().intValue() ) ) );
                  }
                }
                else
                {
                  int awardAmount = depositRecord.getAwardAmount().intValue();
                  if ( nominationPromotion.getAwardAmountMin() != null && nominationPromotion.getAwardAmountMax() != null )
                  {
                    int minAmount = nominationPromotion.getAwardAmountMin().intValue();
                    int maxAmount = nominationPromotion.getAwardAmountMax().intValue();
                    if ( ! ( awardAmount >= minAmount && awardAmount <= maxAmount ) )
                    {
                      errors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_AWARD_AMOUNT_NOT_IN_RANGE, String.valueOf( minAmount ), String.valueOf( maxAmount ) ) );
                    }
                  }
                }
                // If pax is in secondary audience (nomination nominee) then pax is eligible
                if ( !isAllActivePaxPromo && !audienceService.isParticipantInSecondaryAudience( promotion, pax, null ) )
                {
                  errors.add( new ServiceError( "system.errors.PAX_NOT_ELIGIBLE_RECEIVER" ) );
                }
              }
            }
            // quiz specific validations
            else if ( promotion.isQuizPromotion() )
            {
              QuizPromotion quizPromotion = (QuizPromotion)promotion;
              // make sure the award to be deposited confirm with the promotion award setup
              if ( quizPromotion.isAwardActive() )
              {
                if ( quizPromotion.getAwardAmount().intValue() != depositRecord.getAwardAmount().intValue() )
                {
                  errors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_AWARD_AMOUNT_INVALID, String.valueOf( quizPromotion.getAwardAmount().intValue() ) ) );
                }
                // make sure pax is an eligible receiver for this promotion
                // If pax is in primary audience (quiz taker) then pax is eligible
                if ( !isAllActivePaxPromo && !getPromotionService().isParticipantInAudience( pax, promotion ) )
                {
                  errors.add( new ServiceError( "system.errors.PAX_NOT_ELIGIBLE_RECEIVER" ) );
                }
              }
            }
            // product claim specific validation
            else if ( promotion.isProductClaimPromotion() )
            {
              // make sure pax is an eligible receiver for this promotion
              // If pax is in primary audience (claim submitter) then pax is eligible
              if ( !isAllActivePaxPromo && !getPromotionService().isParticipantInAudience( pax, promotion ) )
              {
                errors.add( new ServiceError( "system.errors.PAX_NOT_ELIGIBLE_RECEIVER" ) );
              }
            }
          } // amount null check
        }
      }
    }

    return errors;
  }

  /**
   * Iterate through the promotion's custom elements and build a list of custom elements where the
   * value comes from the given deposit record.  Note this will only build a list of max size 3 
   * because that is the maximum number of elements on the deposit import table
   */
  private static List<ClaimElement> buildClaimElementList( Promotion promotion, DepositImportRecord depositRecord )
  {
    List<ClaimElement> claimElementList = new ArrayList<ClaimElement>();

    Map<Integer, String> customElementMap = new HashMap<Integer, String>();
    customElementMap.put( 1, depositRecord.getFormElement1() );
    customElementMap.put( 2, depositRecord.getFormElement2() );
    customElementMap.put( 3, depositRecord.getFormElement3() );

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

  private String getBillingCodeValue( PromotionBillCode promotionBillCode, Journal journal )
  {

    String billingCodeValue = "";
    if ( promotionBillCode.getBillCode().equalsIgnoreCase( CUSTOM_VALUE ) )
    {
      billingCodeValue = promotionBillCode.getCustomValue();
    }
    else
    {
      if ( journal.getPromotion().isRecognitionPromotion() )
      {
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeRecognitionType.RECEIVER ) )
        {
          billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), journal.getPromotion().getId() );
        }
      }
      else if ( journal.getPromotion().isNominationPromotion() )
      {
        if ( promotionBillCode.getTrackBillCodeBy().equalsIgnoreCase( BillCodeNominationType.NOMINEE ) )
        {
          billingCodeValue = getBillCodeCustomValue( journal.getParticipant(), promotionBillCode.getBillCode(), journal.getPromotion().getId() );
        }
      }
    }
    if ( StringUtils.isEmpty( billingCodeValue ) )
    {
      billingCodeValue = BILLING_CODE_NONE;
    }

    if ( billingCodeValue != null && billingCodeValue.length() > 25 )
    {
      if ( logger.isWarnEnabled() )
      {
        logger.warn( "truncating billing code to 25 characters from original value of [" + billingCodeValue + "]" );
      }
      billingCodeValue = billingCodeValue.substring( 0, 24 );
    }
    return billingCodeValue;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.journal.DepositBillingCodeStrategy#setJournalBillingCodes(com.biperf.core.domain.journal.Journal)
   * @param journal
   */
  private Journal setJournalBillingCodes( Journal journal )
  {
    List<PromotionBillCode> promotionBillCodeList = journal.getPromotion().getPromotionBillCodes();
    if ( !promotionBillCodeList.isEmpty() && promotionBillCodeList.size() > 0 && journal.getPromotion().isBillCodesActive() )
    {
      Iterator<PromotionBillCode> iterator = promotionBillCodeList.iterator();

      JournalBillCode journalBillCode = null;
      if ( journal.getBillCodes().iterator().hasNext() )
      {
        journalBillCode = journal.getBillCodes().iterator().next();
      }
      else
      {
        journalBillCode = new JournalBillCode();
      }

      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode1 = iterator.next();
        if ( promotionBillCode1 != null )
        {
          journalBillCode.setBillCode1( getBillingCodeValue( promotionBillCode1, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode2 = iterator.next();
        if ( promotionBillCode2 != null )
        {
          journalBillCode.setBillCode2( getBillingCodeValue( promotionBillCode2, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode3 = iterator.next();
        if ( promotionBillCode3 != null )
        {
          journalBillCode.setBillCode3( getBillingCodeValue( promotionBillCode3, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode4 = iterator.next();
        if ( promotionBillCode4 != null )
        {
          journalBillCode.setBillCode4( getBillingCodeValue( promotionBillCode4, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode5 = iterator.next();
        if ( promotionBillCode5 != null )
        {
          journalBillCode.setBillCode5( getBillingCodeValue( promotionBillCode5, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode6 = iterator.next();
        if ( promotionBillCode6 != null )
        {
          journalBillCode.setBillCode6( getBillingCodeValue( promotionBillCode6, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode7 = iterator.next();
        if ( promotionBillCode7 != null )
        {
          journalBillCode.setBillCode7( getBillingCodeValue( promotionBillCode7, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode8 = iterator.next();
        if ( promotionBillCode8 != null )
        {
          journalBillCode.setBillCode8( getBillingCodeValue( promotionBillCode8, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode9 = iterator.next();
        if ( promotionBillCode9 != null )
        {
          journalBillCode.setBillCode9( getBillingCodeValue( promotionBillCode9, journal ) );
        }
      }
      if ( iterator.hasNext() )
      {
        PromotionBillCode promotionBillCode10 = iterator.next();
        if ( promotionBillCode10 != null )
        {
          journalBillCode.setBillCode10( getBillingCodeValue( promotionBillCode10, journal ) );
        }
      }
      journal.getBillCodes().add( journalBillCode );
    }
    return journal;
  }

  private String getBillCodeCustomValue( Participant pax, String billCodeValue, Long promotionId )
  {
    String primaryBillingCode = getBillCodeCustomValue( pax, billCodeValue );
    if ( promotionId != null && StringUtils.isEmpty( primaryBillingCode ) )
    {
      primaryBillingCode = promotionId.toString();
    }
    return primaryBillingCode;
  }

  private String getBillCodeCustomValue( Participant pax, String billCodeValue )
  {
    String customValue = null;

    if ( billCodeValue.equalsIgnoreCase( DEPT_NAME ) )
    {
      for ( Iterator nodeIter = pax.getParticipantEmployers().iterator(); nodeIter.hasNext(); )
      {
        ParticipantEmployer participantEmployer = (ParticipantEmployer)nodeIter.next();
        if ( participantEmployer.getTerminationDate() == null )
        {
          PickListValueBean pickListValueBean = userService.getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                        pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                                                                        participantEmployer.getDepartmentType() );
          customValue = pickListValueBean.getName();
          break;
        }
      }
    }
    else if ( billCodeValue.equalsIgnoreCase( COUNTRY_CODE ) )
    {
      customValue = pax.getPrimaryCountryCode();
    }
    else if ( billCodeValue.equalsIgnoreCase( LOGIN_ID ) )
    {
      customValue = pax.getUserName();
    }
    else if ( billCodeValue.equalsIgnoreCase( ORG_UNIT_NAME ) )
    {
      if ( pax.getPrimaryUserNode() != null )
      {
        customValue = pax.getPrimaryUserNode().getNode().getName();
      }
    }
    else
    {
      for ( Iterator iter = pax.getUserCharacteristics().iterator(); iter.hasNext(); )
      {
        UserCharacteristic userCharacteristic = (UserCharacteristic)iter.next();
        if ( userCharacteristic.getUserCharacteristicType().getId().equals( new Long( billCodeValue ) ) )
        {
          customValue = userCharacteristic.buildCharacteristicDisplayString();
          break;
        }
        else
        {
          customValue = " ";
        }
      }
    }
    return customValue;
  }

  private String getBillCodeValue( String name )
  {
    String result = null;
    if ( name != null )
    {
      result = name;
      if ( result != null && result.length() > 25 )
      {
        if ( logger.isWarnEnabled() )
        {
          logger.warn( "truncating billing code to 25 characters from original value of [" + result + "]" );
        }
        result = result.substring( 0, 24 );
      }
    }
    return result;
  }

  private String getServiceErrorText( Exception e ) // bug 73458
  {
    String errorText = "See log for details.";
    if ( e instanceof ServiceErrorException )
    {
      ServiceErrorException se = (ServiceErrorException)e;
      if ( se.getServiceErrorsCMText().get( 0 ) != null )
      {
        if ( se.getServiceErrorsCMText().get( 0 ).length() > 250 )
        {
          errorText = ( se.getServiceErrorsCMText().get( 0 ) ).substring( 0, 250 );
        }
        else
        {
          errorText = se.getServiceErrorsCMText().get( 0 );
        }
      }
    }
    return errorText;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void SystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
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

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public PurlContributorDAO getPurlContributorDAO()
  {
    return purlContributorDAO;
  }

  public void setPurlContributorDAO( PurlContributorDAO purlContributorDAO )
  {
    this.purlContributorDAO = purlContributorDAO;
  }

  public CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

}

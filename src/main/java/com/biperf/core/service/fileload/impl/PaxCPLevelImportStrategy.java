
package com.biperf.core.service.fileload.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.fileload.ImportFileDAO;
import com.biperf.core.dao.fileload.ImportRecordDAO;
import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.PaxCPLevelImportRecord;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.AudienceToParticipantsAssociationRequest;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxValueBean;

/**
 * PaxCPLevelImportStrategy.
 * 
 * This strategy verifies and loads participant's cp level for a Challengepoint promotion.
 * 
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
 * <td>Prabu Babu</td>
 * <td>Sep 7, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PaxCPLevelImportStrategy extends ImportStrategy
{
  private static final Log logger = LogFactory.getLog( PaxCPLevelImportStrategy.class );

  // Challengepoint error from CM
  public static final String CHALLENGEPOINT_LOGIN_ID = "admin.fileload.challengepoint.paxlevel.LOGIN_ID";
  public static final String CHALLENGEPOINT_LEVEL_NAME = "admin.fileload.errors.CP_LEVEL_NAME_NOT_DEFINED";
  public static final String CHALLENGEPOINT_DUPLICATE_LOGIN_ID = "admin.fileload.errors.DUPLICATE_LOGIN_ID";
  public static final String CHALLENGEPOINT_PROMOTION_INVALID = "admin.fileload.errors.PROMOTION_INVALID";
  public static final String CHALLENGEPOINT_LEVEL_NAME_MISMATCH = "admin.fileload.errors.CP_LEVEL_NAME_MISMATCH";
  public static final String CHALLENGEPOINT_SELECTION_DATE_ERROR = "admin.fileload.errors.CHALLENGEPOINT_SELECTION_DATE_ERROR";
  public static final String CHALLENGEPOINT_LEVEL_INVALID = "admin.fileload.errors.CP_LEVEL_NOT_VALID_FOR_MANAGER";
  public static final String UNKNOWN_LOGIN_ID = "system.errors.UNKNOWN_USER_NAME";
  private static final String CHALLENGEPOINT_PAX_NOT_IN_AUDIENCE = "admin.fileload.errors.CHALLENGEPOINT_PAX_NOT_IN_AUDIENCE";

  private PaxGoalService paxGoalService;
  private ParticipantService participantService;
  private AudienceService audienceService;
  private ImportFileDAO importFileDAO;
  private ImportRecordDAO importRecordDAO;
  private MessageService messageService;
  private MailingService mailingService;
  private PromotionService promotionService;
  private UserService userService;
  private ChallengePointService challengePointService;

  private CharacteristicDAO characteristicDAO;
  private ParticipantPartnerDAO participantPartnerDAO;

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {

    Set loginIdSet = new HashSet();
    // start out with the number of current records with errors
    int errorRecordCount = importFile.getImportRecordErrorCount();

    long counter = 0;
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      PaxCPLevelImportRecord record = (PaxCPLevelImportRecord)iterator.next();
      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateImportRecord( record, importFile.getPromotion() );

      if ( StringUtils.isNotBlank( record.getUserName() ) ) // Login ID
      {
        if ( loginIdSet.contains( record.getUserName() ) )
        {
          errors.add( new ServiceError( CHALLENGEPOINT_DUPLICATE_LOGIN_ID, record.getUserName() ) );
        }
        else
        {
          loginIdSet.add( record.getUserName() );
        }
      }

      if ( !errors.isEmpty() )
      {
        createAndAddImportRecordErrors( importFile, record, errors );
        errorRecordCount++;
      }

    }

    importFile.setImportRecordErrorCount( errorRecordCount );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @param validateNodeRelationship whether or not to validate node relationship
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean validateNodeRelationship ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );
  }

  /**
   * Validates the pax level import record - any errors will be returned in a Collection of ServerError
   * objects. I'm using ServerError objects instead of ImportRecordError objects to keep potential
   * re-use more practical (i.e. this method may partially be shared by the service layer at some
   * point)
   * 
   * @param record
   * @param promotion
   * @return Collection
   */
  protected Collection validateImportRecord( PaxCPLevelImportRecord record, Promotion promotion )
  {
    Collection errors = new ArrayList();

    // Constraint: Login ID is required
    checkRequired( record.getUserName(), CHALLENGEPOINT_LOGIN_ID, errors );

    // Constraint: cp level name is required
    checkRequired( record.getLevelName(), CHALLENGEPOINT_LEVEL_NAME, errors );

    Participant pax = null;
    // Constraint: Participant with specified User Name (Login ID) must exist
    if ( StringUtils.isNotBlank( record.getUserName() ) )
    {
      pax = participantService.getParticipantByUserName( record.getUserName() );
      if ( pax == null )
      {
        errors.add( new ServiceError( UNKNOWN_LOGIN_ID, "login id", record.getUserName() ) );
      }
      if ( pax != null )
      {
        if ( !getAudienceService().isParticipantInPrimaryAudience( promotion, pax ) && !getAudienceService().isParticipantInSecondaryAudience( promotion, pax, null ) )
        {
          errors.add( new ServiceError( CHALLENGEPOINT_PAX_NOT_IN_AUDIENCE ) );
        }
      }
    }

    // Constraint: Promotion must be a ChallengePointPromotion
    if ( !promotion.isChallengePointPromotion() )
    {
      errors.add( new ServiceError( CHALLENGEPOINT_PROMOTION_INVALID ) );
    }
    else
    {
      ChallengePointPromotion challengepointPromotion = (ChallengePointPromotion)promotion;

      // Constraint: Today must be within the CP Level selection date range on the promotion.
      // As per change request, allow the file load to range between CP Level selection start date
      // and issue awards date
      if ( challengepointPromotion.getIssueAwardsRunDate() != null )
      {
        if ( !DateUtils.isTodaysDateBetween( challengepointPromotion.getGoalCollectionStartDate(), challengepointPromotion.getIssueAwardsRunDate() ) )
        {
          errors.add( new ServiceError( CHALLENGEPOINT_SELECTION_DATE_ERROR,
                                        DateUtils.toDisplayString( challengepointPromotion.getGoalCollectionStartDate() ),
                                        DateUtils.toDisplayString( challengepointPromotion.getIssueAwardsRunDate() ) ) );
        }
      }

      // Constraint: level name must match 1 of the promotion cp level names.
      if ( StringUtils.isNotBlank( record.getLevelName() ) && challengepointPromotion.getChallengePointLevelByName( record.getLevelName().trim() ) == null )
      {
        errors.add( new ServiceError( CHALLENGEPOINT_LEVEL_NAME_MISMATCH ) );
      }
    }

    return errors;
  }

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    // start out with the number of current records with errors
    long counter = 0;
    logger.info( "processed record count: " + counter );

    boolean replaceValues = false;
    if ( importFile.getReplaceValues() != null )
    {
      replaceValues = importFile.getReplaceValues().booleanValue();
    }
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      PaxCPLevelImportRecord record = (PaxCPLevelImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      // Save Pax Level for the participant for the given promotion
      ChallengePointPromotion promotion = (ChallengePointPromotion)importFile.getPromotion();
      PaxGoal paxGoal = buildPaxGoal( record, replaceValues, promotion );
      getPaxGoalService().savePaxGoal( paxGoal );

      // Send Email Notification
      // Bug Fix 19164. Avoid notifying Inactive Pax.
      if ( promotion.isNotificationRequired( PromotionEmailNotificationType.CHALLENGEPOINT_SELECTED ) && paxGoal.getParticipant().isActive().booleanValue() )
      {
        sendCPLevelSelectedEmailNotification( paxGoal, null );
      }

      if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) && promotion.isAutoCompletePartners() && promotion.getPartnerAudienceType() != null
          && promotion.getPromotionParticipantPartners() != null )
      {
        List paxPartnerList = getPaxPartners( promotion, paxGoal );
        if ( paxPartnerList == null || paxPartnerList.size() == 0 || paxPartnerList.isEmpty() )
        {
          paxPartnerList = buildPaxPartner( promotion, paxGoal );
        }

      }

    }
  }

  public List getPaxPartners( ChallengePointPromotion cpPromo, PaxGoal paxGoal )
  {
    ParticipantAssociationRequest paxAscReq = new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT );
    List partnersList = promotionService.getPartnersByPromotionAndParticipantWithAssociations( cpPromo.getId(), paxGoal.getParticipant().getId(), paxAscReq );
    return partnersList;
  }

  protected List buildPaxPartner( ChallengePointPromotion promotion, PaxGoal paxGoal )
  {
    List partnerList = new ArrayList();
    int paxSize = promotion.getPartnerCount();
    List prePartners = new ArrayList();
    if ( promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE ) )
    {
      Set<Audience> audiences = promotion.getPartnerAudiences();
      AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
      ascReqColl.add( new AudienceToParticipantsAssociationRequest() );

      for ( Audience audience : audiences )
      {
        if ( audience instanceof CriteriaAudience )
        {
          CriteriaAudience criteriaAudience = (CriteriaAudience)getAudienceService().getAudienceById( audience.getId(), ascReqColl );
          for ( AudienceParticipant audiencePartner : (List<AudienceParticipant>)criteriaAudience.getAudienceParticipants() )
          {
            Participant pax = audiencePartner.getParticipant();
            if ( !pax.getId().equals( UserManager.getUserId() ) )
            {
              prePartners.add( pax );
            }
          }
        }
        else
        {
          PaxAudience paxAudience = (PaxAudience)getAudienceService().getAudienceById( audience.getId(), ascReqColl );
          for ( AudienceParticipant audiencePartner : (List<AudienceParticipant>)paxAudience.getAudienceParticipants() )
          {
            Participant pax = audiencePartner.getParticipant();
            if ( !pax.getId().equals( UserManager.getUserId() ) )
            {
              prePartners.add( pax );
            }
          }
        }
      }
    }
    else if ( promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE ) )
    {
      Set<UserNode> nodes = userService.getUserNodes( paxGoal.getParticipant().getId() );
      for ( UserNode userNode : nodes )
      {
        List<User> usersInNode = userService.getAllParticipantsOnNode( userNode.getNode().getId() );
        // add each user, excluding the current logged user
        for ( User user : usersInNode )
        {
          if ( !user.getId().equals( paxGoal.getParticipant().getId() ) )
          {
            prePartners.add( user );
          }
        }
      }
    }
    else if ( promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.USER_CHAR ) )
    {
      Participant pax = participantService.getParticipantById( paxGoal.getParticipant().getId() );
      List<Characteristic> characteristics = characteristicDAO.getAllCharacteristics();
      Characteristic charac = characteristicDAO.getCharacteristicById( new Long( promotion.getPreSelectedPartnerChars() ) );
      String paxCharValue = null;
      Characteristic characteristic = null;
      if ( characteristics != null )
      {
        for ( Iterator<Characteristic> iter = characteristics.iterator(); iter.hasNext(); )
        {
          characteristic = iter.next();
          if ( characteristic.getCharacteristicName().equalsIgnoreCase( charac.getCharacteristicName() ) )
          {
            Set<UserCharacteristic> userChars = pax.getUserCharacteristics();
            Iterator<UserCharacteristic> itr = userChars.iterator();
            while ( itr.hasNext() )
            {
              UserCharacteristic userChar = itr.next();
              if ( userChar.getUserCharacteristicType().getCharacteristicName().equalsIgnoreCase( charac.getCharacteristicName() ) )
              {
                paxCharValue = userChar.getCharacteristicValue();
                break;
              }
            }
            if ( paxCharValue != null )
            {
              List userCharacteristicsList = getParticipantService().getAllForCharIDAndValue( characteristic.getId(), paxCharValue );
              if ( userCharacteristicsList != null )
              {
                for ( Iterator iterator = userCharacteristicsList.iterator(); iterator.hasNext(); )
                {
                  UserCharacteristic userChar = (UserCharacteristic)iterator.next();
                  if ( !userChar.getUser().getId().equals( paxGoal.getParticipant().getId() ) )
                  {
                    prePartners.add( userChar.getUser() );
                  }
                }
              }
            }
          }
        }
      }
    }
    for ( Iterator iterator = prePartners.iterator(); iterator.hasNext(); )
    {
      if ( partnerList != null && partnerList.size() < promotion.getPartnerCount() )
      {
        Participant pax = (Participant)iterator.next();
        ParticipantPartner paxPartner = new ParticipantPartner();
        paxPartner.setParticipant( paxGoal.getParticipant() );
        paxPartner.setPromotion( promotion );
        paxPartner.setPartnerEmailSent( false );
        paxPartner.setPartner( pax );

        participantPartnerDAO.saveParticipantPartnerAssoc( paxPartner );
        partnerList.add( paxPartner );
      }
      else
      {
        break;
      }
    }

    return partnerList;
  }

  private void sendCPLevelSelectedEmailNotification( PaxGoal paxGoal, List paxPartners ) throws ServiceErrorException
  {
    ChallengePointPromotion cpPromotion = (ChallengePointPromotion)paxGoal.getGoalQuestPromotion();
    ChallengepointPaxValueBean challengepointPaxValueBean = null;
    challengepointPaxValueBean = challengePointService.populateChallengepointPaxValueBean( paxGoal, (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(), (GoalLevel)paxGoal.getGoalLevel() );
    Message message = null;
    message = getMessageService().getMessageByCMAssetCode( MessageService.CHALLENGEPOINT_SELECTED_MESSAGE_CM_ASSET_CODE );
    MailingRecipient mailingRecipient = getMailingService().buildMailingRecipientForChallengepointEmail( cpPromotion, paxGoal.getParticipant(), challengepointPaxValueBean, null, paxPartners );
    // Create mailing object
    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    mailing.addMailingRecipient( mailingRecipient );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    mailing.setSender( "ChallengePoint Promotion" );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setGuid( GuidUtils.generateGuid() );
    getMailingService().submitMailing( mailing, null );
  }

  /**
   * pass in a source and target, it will copy data over. If paxGoal exists for the pax,
   * copy over everything except for the paxGoal. This load will always overwrite the challengepointlevel.
   * 
   * @param source
   * @param replaceValues
   * @param promotion
   * @return PaxGoal
   */
  protected PaxGoal buildPaxGoal( PaxCPLevelImportRecord source, boolean replaceValues, ChallengePointPromotion promotion )
  {
    Participant pax = participantService.getParticipantByUserName( source.getUserName() );

    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );

    // This could happen - they may not have a level selected yet but the paxGoal exists for the
    // pax.
    // A PaxGoal object contains other things like base objective, current performance value, etc.
    if ( paxGoal == null )
    {
      paxGoal = new PaxGoal();
      paxGoal.setGoalQuestPromotion( promotion );
      paxGoal.setParticipant( pax );
      paxGoal.setGoalLevel( promotion.getChallengePointLevelByName( source.getLevelName() ) );
    }

    // This load will optionally overwrite existing challengepointLevel for the pax with what's on
    // the import file.
    if ( paxGoal != null && ( replaceValues || paxGoal.getGoalLevel() == null ) )
    {
      paxGoal.setGoalLevel( promotion.getChallengePointLevelByName( source.getLevelName() ) );

      // Other PaxGoal attributes are retained.
    }
    return paxGoal;
  }

  // convenience method- returns true if 'value' is not null and not empty
  protected boolean isNotNullOrEmpty( String value )
  {
    return StringUtils.isNotEmpty( value );
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public ImportFileDAO getImportFileDAO()
  {
    return importFileDAO;
  }

  public void setImportFileDAO( ImportFileDAO importFileDAO )
  {
    this.importFileDAO = importFileDAO;
  }

  public ImportRecordDAO getImportRecordDAO()
  {
    return importRecordDAO;
  }

  public void setImportRecordDAO( ImportRecordDAO importRecordDAO )
  {
    this.importRecordDAO = importRecordDAO;
  }

  public PaxGoalService getPaxGoalService()
  {
    return paxGoalService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  public CharacteristicDAO getCharacteristicDAO()
  {
    return characteristicDAO;
  }

  public void setCharacteristicDAO( CharacteristicDAO characteristicDAO )
  {
    this.characteristicDAO = characteristicDAO;
  }

  public ParticipantPartnerDAO getParticipantPartnerDAO()
  {
    return participantPartnerDAO;
  }

  public void setParticipantPartnerDAO( ParticipantPartnerDAO participantPartnerDAO )
  {
    this.participantPartnerDAO = participantPartnerDAO;
  }
}

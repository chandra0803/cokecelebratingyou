
package com.biperf.core.service.fileload.impl;

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
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.PaxBaseImportRecord;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;

public class PaxBaseImportStrategy extends ImportStrategy
{
  private static final Log logger = LogFactory.getLog( PaxBaseImportStrategy.class );

  // goalquest
  public static final String GOALQUEST_LOGIN_ID = "admin.fileload.goalquest.paxbase.LOGIN_ID";
  public static final String GOALQUEST_BASE_OBJECTIVE = "admin.fileload.errors.BASE_OBJECTIVE_NOT_DEFINED";
  public static final String GOALQUEST_DUPLICATE_LOGIN_ID = "admin.fileload.errors.DUPLICATE_LOGIN_ID";
  public static final String GOALQUEST_BASE_OBJECTIVE_INVALID = "admin.fileload.errors.AMOUNT_MORE_THAN_TWO_DECIMAL_PLACES";
  public static final String GOALQUEST_PROMOTION_INVALID = "admin.fileload.errors.PROMOTION_INVALID";
  public static final String GOALQUEST_PROMOTION_MUST_BE_PERCENT_OF_BASE = "admin.fileload.errors.MUST_BE_PERCENT_OF_BASE";
  public static final String UNKNOWN_LOGIN_ID = "system.errors.UNKNOWN_USER_NAME";
  private static final String GOALQUEST_PAX_NOT_IN_AUDIENCE = "admin.fileload.errors.GOALQUEST_PAX_NOT_IN_AUDIENCE";
  private AudienceService audienceService;
  private PaxGoalService paxGoalService;
  private ParticipantService participantService;
  private ImportFileDAO importFileDAO;
  private ImportRecordDAO importRecordDAO;

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
      PaxBaseImportRecord record = (PaxBaseImportRecord)iterator.next();
      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateImportRecord( record, importFile.getPromotion() );

      if ( StringUtils.isNotBlank( record.getUserName() ) )
      {
        if ( loginIdSet.contains( record.getUserName() ) )
        {
          errors.add( new ServiceError( GOALQUEST_DUPLICATE_LOGIN_ID, record.getUserName() ) );
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
   * Validates the pax base record - any errors will be returned in a Collection of ServerError
   * objects. I'm using ServerError objects instead of ImportRecordError objects to keep potential
   * re-use more practical (i.e. this method may partially be shared by the service layer at some
   * point)
   * 
   * @param record
   * @param promotion
   * @return Collection
   */
  protected Collection validateImportRecord( PaxBaseImportRecord record, Promotion promotion )
  {
    Collection errors = new ArrayList();

    // Constraint: Promotion must be a GoalQuestPromotion
    if ( !promotion.isGoalQuestPromotion() )
    {
      errors.add( new ServiceError( GOALQUEST_PROMOTION_INVALID ) );
    }

    // Constraint: Login ID is required
    checkRequired( record.getUserName(), GOALQUEST_LOGIN_ID, errors );

    // Constraint: Base Objective amount is required
    checkRequired( record.getBaseQuantity(), GOALQUEST_BASE_OBJECTIVE, errors );

    if ( record.getBaseQuantity() != null && ( record.getBaseQuantity().doubleValue() == 0 || record.getBaseQuantity().scale() > 4 ) )
    {
      // Constraint: A base objective amount must be up to 4 decimal places
      errors.add( new ServiceError( GOALQUEST_BASE_OBJECTIVE_INVALID ) );
    }

    if ( StringUtils.isNotBlank( record.getUserName() ) )
    {
      Participant pax = participantService.getParticipantByUserName( record.getUserName() );
      // Constraint: Participant with specified User Name (Login ID) must exist
      if ( pax == null )
      {
        errors.add( new ServiceError( UNKNOWN_LOGIN_ID, "login id", record.getUserName() ) );
      }
      if ( pax != null )
      {
        if ( !getAudienceService().isParticipantInPrimaryAudience( promotion, pax ) && !getAudienceService().isParticipantInSecondaryAudience( promotion, pax, null ) )
        {
          errors.add( new ServiceError( GOALQUEST_PAX_NOT_IN_AUDIENCE ) );
        }
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

    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      PaxBaseImportRecord record = (PaxBaseImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      // Save Pax Goal for the participant for the given promotion
      PaxGoal paxGoal = buildPaxGoal( record, (GoalQuestPromotion)importFile.getPromotion() );
      getPaxGoalService().savePaxGoal( paxGoal );

    }
  }

  /**
   * pass in a source, will copy data over. If paxGoal does not exists for the pax,
   * create a new one (goalLevel nullable). This load will always overwrite the Base Objective amount.
   * 
   * @param source
   * @param promotion
   * @return PaxGoal
   */
  protected PaxGoal buildPaxGoal( PaxBaseImportRecord source, GoalQuestPromotion promotion )
  {
    Participant pax = participantService.getParticipantByUserName( source.getUserName() );

    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );

    // This could happen - they may not have a goal selected when base objective is loaded
    if ( paxGoal == null )
    {
      paxGoal = new PaxGoal();
      paxGoal.setGoalQuestPromotion( promotion );
      paxGoal.setParticipant( pax );
    }

    // This load will always overwrite just the base objective amount
    paxGoal.setBaseQuantity( source.getBaseQuantity() );

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
}

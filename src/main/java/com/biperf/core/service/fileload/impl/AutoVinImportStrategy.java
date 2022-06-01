
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.GoalQuestPaxActivityStatus;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.ProgressTransactionType;
import com.biperf.core.domain.fileload.AutoVinImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.MailingBatchHolder;

public class AutoVinImportStrategy extends AbstractGoalQuestProgressImportStrategy
{
  private static final Log logger = LogFactory.getLog( AutoVinImportStrategy.class );

  private static final String TRANSACTION_TYPE_SALES = "S";
  private static final String TRANSACTION_TYPE_RETURN = "R";

  // goalquest
  private GoalQuestPaxActivityService goalQuestPaxActivityService;
  private ParticipantService participantService;

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    // start out with the number of current records with errors
    int errorRecordCount = importFile.getImportRecordErrorCount();
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)importFile.getPromotion();
    boolean progressLoadTypeError = !ProgressLoadType.AUTOMOTIVE.equals( goalQuestPromotion.getProgressLoadType().getCode() );
    boolean awardsIssuedError = goalQuestPromotion.isIssueAwardsRun();
    boolean loadDateError = goalQuestPromotion.getGoalCollectionEndDate().after( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) );
    boolean submissionDateError = !isValidProgressSubmissionDate( importFile.getPromotion().getId(), importFile.getProgressEndDate(), ImportFileTypeType.lookup( ImportFileTypeType.GQ_VIN_LOAD ) );

    long counter = 0;
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      AutoVinImportRecord record = (AutoVinImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = new ArrayList();

      // Constraint : Progress load type should be 'auto'
      if ( progressLoadTypeError )
      {
        errors.add( new ServiceError( "admin.fileload.errors.GQ_PROGRESS_LOAD_TYPE_AUTO_ERROR" ) );
      }
      else
      {
        // Constraint : Progress Cannot be loaded after Awards have been issued
        if ( awardsIssuedError )
        {
          errors.add( new ServiceError( "admin.fileload.errors.PROGRESS_AFTER_AWARD_ISSUE_ERROR" ) );
        }

        // Constraint : Progress cannot be loaded before Goal Collection END date
        if ( loadDateError )
        {
          errors.add( new ServiceError( "admin.fileload.errors.GOALQUEST_PROGRESS_LOAD_DATE_ERROR" ) );
        }

        // Constraint: Submission Date cannot be earlier than any of the previous
        // Goalquest VIN progress load(s) for the same promotion
        if ( submissionDateError )
        {
          errors.add( new ServiceError( "admin.fileload.errors.INVALID_PROGRESS_SUBMISSION_DATE" ) );
        }

        // If there are no promotion based errors validate pax records
        if ( errors.isEmpty() )
        {
          errors = validateAutoVinImportRecord( record, importFile.getPromotion() );
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
   * Validates the auto vin import record - any errors will be returned in a Collection of ServerError
   * objects. I'm using ServerError objects instead of ImportRecordError objects to keep potential
   * re-use more practical (i.e. this method may partially be shared by the service layer at some
   * point)
   * 
   * @param record
   * @return Collection
   */
  protected Collection validateAutoVinImportRecord( AutoVinImportRecord record, Promotion promotion )
  {
    Collection errors = new ArrayList();

    // Constraint: Login Id is required.
    checkRequired( record.getLoginId(), "admin.fileload.goalquest.progress.LOGIN_ID", errors );

    // Constraint: VIN is required.
    checkRequired( record.getVin(), "admin.fileload.goalquest.autovin.VIN_NBR", errors );

    // Constraint: Model is required.
    checkRequired( record.getModel(), "admin.fileload.goalquest.autovin.MODEL", errors );

    // Constraint: Transaction Type is required.
    checkRequired( record.getTransactionType(), "admin.fileload.goalquest.autovin.TRANS_TYPE", errors );

    // Constraint: Transaction Type must be "S" (Sales) or "R" (Return)
    if ( !isValidTransactionType( record.getTransactionType() ) )
    {
      errors.add( new ServiceError( "system.errors.UNKNOWN_TRANS_TYPE", "trans type", record.getTransactionType() ) );
    }

    if ( StringUtils.isNotBlank( record.getLoginId() ) )
    {
      Participant pax = participantService.getParticipantByUserName( record.getLoginId() );

      // Constraint: Participant with specified Login ID (User Name) must exist
      if ( pax == null )
      {
        errors.add( new ServiceError( "system.errors.UNKNOWN_USER_NAME", "login id", record.getLoginId() ) );
      }
      // Constraint: Participant should belong to Promotion Audience
      else if ( !isPaxInAudience( promotion, pax ) )
      {
        errors.add( new ServiceError( "admin.fileload.errors.GOALQUEST_PAX_NOT_IN_AUDIENCE" ) );
      }
      // Constraint: Participant should have selected a goal
      else
      {
        PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );
        if ( paxGoal == null || paxGoal.getGoalLevel() == null )
        {
          errors.add( new ServiceError( "admin.fileload.errors.GOALQUEST_PAX_GOAL_NOT_SELECTED" ) );
        }
      }
    }

    return errors;
  }

  private boolean isValidTransactionType( String transactionType )
  {
    return TRANSACTION_TYPE_SALES.equalsIgnoreCase( transactionType ) || TRANSACTION_TYPE_RETURN.equalsIgnoreCase( transactionType );
  }

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records, MailingBatchHolder mailingBatchHolder ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new BeaconRuntimeException( "Invalid call to import" );
    }

    // start out with the number of current records with errors
    long counter = 0;
    logger.info( "processed record count: " + counter );
    List emailNotificationList = new ArrayList();
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      AutoVinImportRecord record = (AutoVinImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      GoalQuestParticipantActivity goalQuestParticipantActivity = buildGoalQuestPaxActivity( record, importFile.getProgressEndDate(), (GoalQuestPromotion)importFile.getPromotion() );
      getGoalQuestPaxActivityService().saveGoalQuestPaxActivity( goalQuestParticipantActivity );
      updateNotificationList( emailNotificationList, goalQuestParticipantActivity );

    }
    sendEmailForPaxProgress( emailNotificationList, importFile, mailingBatchHolder );
  }

  private void sendEmailForPaxProgress( List emailNotificationList, ImportFile importFile, MailingBatchHolder mailingBatchHolder )
  {
    for ( Iterator notificationsIter = emailNotificationList.iterator(); notificationsIter.hasNext(); )
    {
      GoalQuestParticipantActivity goalQuestParticipantActivity = (GoalQuestParticipantActivity)notificationsIter.next();
      sendEmailForPaxProgress( goalQuestParticipantActivity, importFile, mailingBatchHolder );
    }
  }

  private void updateNotificationList( List emailNotificationList, GoalQuestParticipantActivity currentActivity )
  {
    boolean found = false;
    for ( Iterator notificationsIter = emailNotificationList.iterator(); notificationsIter.hasNext(); )
    {
      GoalQuestParticipantActivity existingActivity = (GoalQuestParticipantActivity)notificationsIter.next();
      if ( currentActivity.getGoalQuestPromotion().getId().equals( existingActivity.getGoalQuestPromotion().getId() )
          && currentActivity.getParticipant().getId().equals( existingActivity.getParticipant().getId() ) )
      {
        // if already existing do not add to the list
        found = true;
        break;
      }
    }

    // if not found add to the list
    if ( !found )
    {
      emailNotificationList.add( currentActivity );
    }

  }

  /**
   * pass in a source and target, it will copy data over
   * 
   * @param source
   * @param progressEndDate
   * @param promotion
   * @return GoalQuestParticipantActivity
   */
  protected GoalQuestParticipantActivity buildGoalQuestPaxActivity( AutoVinImportRecord source, Date progressEndDate, GoalQuestPromotion promotion )
  {
    GoalQuestParticipantActivity target = new GoalQuestParticipantActivity();
    // always incremental for auto vin load, never replace
    target.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) );
    target.setAutomotive( true );
    target.setStatus( GoalQuestPaxActivityStatus.lookup( GoalQuestPaxActivityStatus.PENDING ) );
    target.setGoalQuestPromotion( promotion );
    Participant pax = participantService.getParticipantByUserName( source.getLoginId() );
    target.setParticipant( pax );
    target.setSubmissionDate( progressEndDate );
    target.setTransactionType( ProgressTransactionType.lookup( source.getTransactionType().trim().toUpperCase() ) );
    // Add 1 if Sales, subtract 1 if Return
    if ( TRANSACTION_TYPE_SALES.equalsIgnoreCase( source.getTransactionType() ) )
    {
      target.setQuantity( new BigDecimal( 1 ) );
    }
    else if ( TRANSACTION_TYPE_RETURN.equalsIgnoreCase( source.getTransactionType() ) )
    {
      target.setQuantity( new BigDecimal( -1 ) );
    }
    target.setVin( source.getVin() );
    target.setModel( source.getModel() );
    target.setSalesDate( source.getSalesDate() );
    target.setDeliveryDate( source.getDeliveryDate() );
    target.setDealerCode( source.getDealerCode() );
    target.setDealerName( source.getDealerName() );

    return target;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public GoalQuestPaxActivityService getGoalQuestPaxActivityService()
  {
    return goalQuestPaxActivityService;
  }

  public void setGoalQuestPaxActivityService( GoalQuestPaxActivityService goalQuestPaxActivityService )
  {
    this.goalQuestPaxActivityService = goalQuestPaxActivityService;
  }

  /**
   * Fullfills the standard ImportFile process API, which doesnt support batch emails
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @throws ServiceErrorException
   */
  @Override
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    importImportFile( importFile, records, null );
  }

}

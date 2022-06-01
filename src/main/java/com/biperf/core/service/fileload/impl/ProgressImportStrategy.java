/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.GoalQuestPaxActivityStatus;
import com.biperf.core.domain.enums.GoalQuestPaxActivityType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.fileload.GoalQuestProgressImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.GoalQuestParticipantActivity;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.goalquest.GoalQuestPaxActivityService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.MailingBatchHolder;

/*
 * ProgressImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan 2,
 * 2007</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ProgressImportStrategy extends AbstractGoalQuestProgressImportStrategy
{
  private static final Log logger = LogFactory.getLog( ProgressImportStrategy.class );

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
    Set loginIdSet = new HashSet();
    // start out with the number of current records with errors
    int errorRecordCount = importFile.getImportRecordErrorCount();
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)importFile.getPromotion();
    boolean progressLoadTypeError = !ProgressLoadType.SALES.equals( goalQuestPromotion.getProgressLoadType().getCode() );
    boolean awardsIssuedError = goalQuestPromotion.isIssueAwardsRun();
    boolean loadDateError = goalQuestPromotion.getGoalCollectionEndDate().after( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) );
    boolean submissionDateError = !isValidProgressSubmissionDate( importFile.getPromotion().getId(),
                                                                  importFile.getProgressEndDate(),
                                                                  ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD ) );

    long counter = 0;
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      GoalQuestProgressImportRecord record = (GoalQuestProgressImportRecord)iterator.next();
      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = new ArrayList();

      // Constraint : Progress load type should be 'sales'
      if ( progressLoadTypeError )
      {
        errors.add( new ServiceError( "admin.fileload.errors.GQ_PROGRESS_LOAD_TYPE_SALES_ERROR" ) );
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
        // Constraint : Submission Date cannot be earlier than any of the previous
        // Goalquest progress load(s) for the same promotion
        if ( submissionDateError )
        {
          errors.add( new ServiceError( "admin.fileload.errors.INVALID_PROGRESS_SUBMISSION_DATE" ) );
        }

        // If there are no promotion based errors validate pax records
        if ( errors.isEmpty() )
        {
          errors = validateProgressImportRecord( record, importFile.getPromotion() );

          if ( StringUtils.isNotBlank( record.getLoginId() ) )
          {
            if ( loginIdSet.contains( record.getLoginId() ) )
            {
              errors.add( new ServiceError( "admin.fileload.errors.DUPLICATE_LOGIN_ID", record.getLoginId() ) );
            }
            else
            {
              loginIdSet.add( record.getLoginId() );
            }
          }
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
   * Validates the progress import record - any errors will be returned in a Collection of
   * ServerError objects. I'm using ServerError objects instead of ImportRecordError objects to keep
   * potential re-use more practical (i.e. this method may partially be shared by the service layer
   * at some point)
   * 
   * @param record
   * @return Collection
   */
  protected Collection validateProgressImportRecord( GoalQuestProgressImportRecord record, Promotion promotion )
  {
    Collection errors = new ArrayList();

    // Constraint: Login Id is required.
    checkRequired( record.getLoginId(), "admin.fileload.goalquest.progress.LOGIN_ID", errors );

    // Constraint: Total Performance is required.
    checkRequired( record.getTotalPerformanceToDate(), "admin.fileload.goalquest.progress.TOTAL_PERFORMANCE", errors );

    if ( StringUtils.isNotBlank( record.getLoginId() ) )
    {
      Participant pax = getParticipantService().getParticipantByUserName( record.getLoginId() );

      // Constraint: Participant with specified Login ID (User Name) must exist
      if ( pax == null )
      {
        errors.add( new ServiceError( "system.errors.UNKNOWN_USER_NAME" ) );
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

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the list of records to process
   * @param mailingBatchHolder the holder for the pax bulk email capability
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records, MailingBatchHolder mailingBatchHolder ) throws ServiceErrorException
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
      GoalQuestProgressImportRecord record = (GoalQuestProgressImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      GoalQuestParticipantActivity goalQuestParticipantActivity = buildGoalQuestPaxActivity( record, replaceValues, importFile.getProgressEndDate(), (GoalQuestPromotion)importFile.getPromotion() );
      getGoalQuestPaxActivityService().saveGoalQuestPaxActivity( goalQuestParticipantActivity );
      sendEmailForPaxProgress( goalQuestParticipantActivity, importFile, mailingBatchHolder );

    }
  }

  /**
   * pass in a source and target, it will copy data over
   * 
   * @param source
   * @param replaceValues
   * @param progressEndDate
   * @param promotion
   * @return GoalQuestParticipantActivity
   */
  protected GoalQuestParticipantActivity buildGoalQuestPaxActivity( GoalQuestProgressImportRecord source, boolean replaceValues, Date progressEndDate, GoalQuestPromotion promotion )
  {
    GoalQuestParticipantActivity target = new GoalQuestParticipantActivity();
    if ( replaceValues )
    {
      target.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.REPLACE ) );
    }
    else
    {
      target.setType( GoalQuestPaxActivityType.lookup( GoalQuestPaxActivityType.INCREMENTAL ) );
    }
    target.setAutomotive( false );
    target.setStatus( GoalQuestPaxActivityStatus.lookup( GoalQuestPaxActivityStatus.PENDING ) );
    target.setGoalQuestPromotion( promotion );
    Participant pax = getParticipantService().getParticipantByUserName( source.getLoginId() );
    target.setParticipant( pax );
    target.setQuantity( source.getTotalPerformanceToDate() );
    target.setSubmissionDate( progressEndDate );
    return target;
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

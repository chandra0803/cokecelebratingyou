/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/CPProgressImportStrategy.java,v $
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

import com.biperf.core.domain.challengepoint.ChallengepointProgress;
import com.biperf.core.domain.enums.ChallengepointPaxActivityType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ChallengepointProgressImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.challengepoint.ChallengepointProgressService;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.MailingBatchHolder;

/*
 * ProgressImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>meadows</td> <td>Jan 2,
 * 2007</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class CPProgressImportStrategy extends AbstractChallengepointProgressImportStrategy
{
  private static final Log logger = LogFactory.getLog( CPProgressImportStrategy.class );

  // goalquest
  public static final String CHALLENGEPOINT_LOGIN_ID = "admin.fileload.challengepoint.progress.LOGIN_ID";
  public static final String CHALLENGEPOINT_TOTAL_PERFORMANCE = "admin.fileload.challengepoint.progress.TOTAL_PERFORMANCE";
  public static final String CHALLENGEPOINT_DUPLICATE_LOGIN_ID = "admin.fileload.errors.DUPLICATE_LOGIN_ID";
  private static final String CHALLENGEPOINT_PAX_NOT_IN_AUDIENCE = "admin.fileload.errors.CHALLENGEPOINT_PAX_NOT_IN_AUDIENCE";
  private static final String CHALLENGEPOINT_PROMOTION_INVALID = "admin.fileload.errors.PROMOTION_INVALID";
  private static final String CHALLENGEPOINT_PAX_GOAL_NOT_SELECTED = "admin.fileload.errors.CHALLENGEPOINT_PAX_LEVEL_NOT_SELECTED";
  private static final String CHALLENGEPOINT_PROGRESS_LOAD_DATE_ERROR = "admin.fileload.errors.CHALLENGEPOINT_PROGRESS_LOAD_DATE_ERROR";
  private static final String PROGRESS_AFTER_AWARD_ISSUE_ERROR = "admin.fileload.errors.PROGRESS_AFTER_AWARD_ISSUE_ERROR";
  private static final String INVALID_PROGRESS_SUBMISSION_DATE = "admin.fileload.errors.INVALID_PROGRESS_SUBMISSION_DATE";

  private ChallengepointProgressService challengepointProgressService;
  private ParticipantService participantService;
  private ImportService importService;

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
    boolean validProgressSubmissionDate = isValidProgressSubmissionDate( importFile.getPromotion().getId(), importFile.getProgressEndDate() );

    long counter = 0;
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ChallengepointProgressImportRecord record = (ChallengepointProgressImportRecord)iterator.next();
      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateProgressImportRecord( record, importFile.getPromotion() );

      if ( StringUtils.isNotBlank( record.getLoginId() ) )
      {
        if ( loginIdSet.contains( record.getLoginId() ) )
        {
          errors.add( new ServiceError( CHALLENGEPOINT_DUPLICATE_LOGIN_ID, record.getLoginId() ) );
        }
        else
        {
          loginIdSet.add( record.getLoginId() );
        }
      }

      // Constraint: Submission Date cannot be earlier than any of the previous
      // Goalquest progress load(s) for the same promotion
      if ( !validProgressSubmissionDate )
      {
        errors.add( new ServiceError( INVALID_PROGRESS_SUBMISSION_DATE ) );
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
  protected Collection validateProgressImportRecord( ChallengepointProgressImportRecord record, Promotion promotion )
  {
    Collection errors = new ArrayList();

    checkRequired( record.getLoginId(), CHALLENGEPOINT_LOGIN_ID, errors );
    checkRequired( record.getTotalPerformanceToDate(), CHALLENGEPOINT_TOTAL_PERFORMANCE, errors );

    if ( StringUtils.isNotBlank( record.getLoginId() ) )
    {
      Participant pax = getParticipantService().getParticipantByUserName( record.getLoginId() );
      // Participant with specified User Name must exist
      if ( pax == null )
      {
        errors.add( new ServiceError( "system.errors.UNKNOWN_USER_NAME" ) );
      }
      else
      {
        if ( !isPaxInAudience( promotion, pax ) )
        {
          errors.add( new ServiceError( CHALLENGEPOINT_PAX_NOT_IN_AUDIENCE ) );
        }
        else
        {
          PaxGoal paxGoal = getChallengePointService().getPaxChallengePoint( promotion.getId(), pax.getId() );

          if ( paxGoal == null || paxGoal.getGoalLevel() == null )
          {
            errors.add( new ServiceError( CHALLENGEPOINT_PAX_GOAL_NOT_SELECTED ) );
          }
        }
      }
    }

    // Constraint: Promotion must be a GoalQuestPromotion
    if ( !promotion.isChallengePointPromotion() )
    {
      errors.add( new ServiceError( CHALLENGEPOINT_PROMOTION_INVALID ) );
    }
    else
    {
      ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;

      if ( challengePointPromotion.getIssueAwardsRunDate() == null )
      {
        // To fix the bug 22028
        Date todaysDate = new Date();
        if ( challengePointPromotion.getGoalCollectionEndDate().after( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) )
            || org.apache.commons.lang3.time.DateUtils.isSameDay( todaysDate, challengePointPromotion.getGoalCollectionEndDate() ) )
        {
          errors.add( new ServiceError( CHALLENGEPOINT_PROGRESS_LOAD_DATE_ERROR ) );
        }
      }
      else
      {
        errors.add( new ServiceError( PROGRESS_AFTER_AWARD_ISSUE_ERROR ) );
      }
    }

    return errors;
  }

  private boolean isValidProgressSubmissionDate( Long promotionId, Date submissionDate )
  {
    ImportFileTypeType importFileType = ImportFileTypeType.lookup( ImportFileTypeType.GQ_PROGRESS_DATA_LOAD );
    ImportFileStatusType importFileStatusType = ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED );
    Iterator iter = getImportService().getImportFiles( null, importFileType, importFileStatusType, null, null ).iterator();

    while ( iter.hasNext() )
    {
      ImportFile importFile = (ImportFile)iter.next();
      if ( promotionId.longValue() == importFile.getPromotion().getId().longValue() && submissionDate.before( importFile.getProgressEndDate() ) )
      {
        return false;
      }
    }

    return true;
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
      ChallengepointProgressImportRecord record = (ChallengepointProgressImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      ChallengepointProgress challengepointProgress = buildChallengepointProgress( record, replaceValues, importFile.getProgressEndDate(), (ChallengePointPromotion)importFile.getPromotion() );
      getChallengepointProgressService().saveChallengepointProgress( challengepointProgress );
      sendEmailForPaxProgress( challengepointProgress, importFile, mailingBatchHolder );

    }
  }

  /**
   * pass in a source and target, it will copy data over
   * 
   * @param source
   * @param replaceValues
   * @param progressEndDate
   * @param promotion
   * @return ChallengepointProgress
   */
  protected ChallengepointProgress buildChallengepointProgress( ChallengepointProgressImportRecord source, boolean replaceValues, Date progressEndDate, ChallengePointPromotion promotion )
  {
    ChallengepointProgress target = new ChallengepointProgress();
    if ( replaceValues )
    {
      target.setType( ChallengepointPaxActivityType.REPLACE );
    }
    else
    {
      target.setType( ChallengepointPaxActivityType.INCREMENTAL );
    }
    target.setChallengePointPromotion( promotion );
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

  public ChallengepointProgressService getChallengepointProgressService()
  {
    return challengepointProgressService;
  }

  public void setChallengepointProgressService( ChallengepointProgressService challengepointProgressService )
  {
    this.challengepointProgressService = challengepointProgressService;
  }

  public ImportService getImportService()
  {
    if ( null == importService )
    {
      this.setImportService( (ImportService)BeanLocator.getBean( ImportService.BEAN_NAME ) );
    }
    return importService;
  }

  public void setImportService( ImportService importService )
  {
    this.importService = importService;
  }
}

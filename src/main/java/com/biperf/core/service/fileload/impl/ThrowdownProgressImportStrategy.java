
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

import com.biperf.core.domain.enums.MatchStatusType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ThrowdownMatchProgressType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ThrowdownProgressImportRecord;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.MatchTeamProgress;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.MailingBatchHolder;

public class ThrowdownProgressImportStrategy extends ImportStrategy
{

  private static final Log logger = LogFactory.getLog( ThrowdownProgressImportStrategy.class );
  private ParticipantService participantService;
  private TeamService teamService;
  private AudienceService audienceService;

  @Override
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    importImportFile( importFile, records, null );
  }

  @Override
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
    Integer roundNumber = importFile.getRoundNumber();
    boolean replaceValues = false;
    if ( importFile.getReplaceValues() != null )
    {
      replaceValues = importFile.getReplaceValues().booleanValue();
    }
    ThrowdownPromotion promotion = (ThrowdownPromotion)importFile.getPromotion();
    List<MatchTeamProgress> teamProgress = new ArrayList<MatchTeamProgress>();
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ThrowdownProgressImportRecord record = (ThrowdownProgressImportRecord)iterator.next();

      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Participant pax = getParticipantService().getParticipantByUserName( record.getLoginId() );
      Team team = getTeamService().getTeamByUserIdForPromotion( pax.getId(), promotion.getId() );
      if ( team != null )
      {
        MatchTeamOutcome teamOutcome = getTeamService().getOutcomeForMatch( team.getId(), promotion.getId(), roundNumber );
        if ( teamOutcome != null )
        {
          MatchTeamProgress progress = buildProgress( record, replaceValues, teamOutcome );
          progress = getTeamService().saveProgress( progress );
          teamOutcome.getProgress().add( progress );
          teamProgress.add( progress );
        }
      }
    }
    HibernateSessionManager.getSession().flush();
  }

  @Override
  public void verifyImportFile( ImportFile importFile, List records )
  {
    Set loginIdSet = new HashSet();
    Set<ThrowdownProgressImportRecord> duplicateRecordsSet = new HashSet<ThrowdownProgressImportRecord>();
    Set<ThrowdownProgressImportRecord> importRecordList = new HashSet<ThrowdownProgressImportRecord>();
    // start out with the number of current records with errors
    int errorRecordCount = importFile.getImportRecordErrorCount();
    long counter = 0;
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      ThrowdownProgressImportRecord record = (ThrowdownProgressImportRecord)iterator.next();
      // guard clause - only process this record if there are no errors with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = new ArrayList();

      // If there are no promotion based errors validate pax records
      if ( errors.isEmpty() )
      {
        errors = validateProgressImportRecord( record, importFile.getPromotion(), importFile.getRoundNumber() );

        if ( StringUtils.isNotBlank( record.getLoginId() ) )
        {
          if ( loginIdSet.contains( record.getLoginId() ) )
          {
            errors.add( new ServiceError( "admin.fileload.errors.DUPLICATE_LOGIN_ID", record.getLoginId() ) );
            duplicateRecordsSet.add( record );
          }
          else
          {
            loginIdSet.add( record.getLoginId() );
            importRecordList.add( record );
          }
        }
      }

      if ( !errors.isEmpty() )
      {
        createAndAddImportRecordErrors( importFile, record, errors );
        errorRecordCount++;
      }
    }

    if ( !duplicateRecordsSet.isEmpty() )
    {
      for ( ThrowdownProgressImportRecord duprecord : duplicateRecordsSet )
      {
        Collection errors = new ArrayList();
        for ( ThrowdownProgressImportRecord record : importRecordList )
        {
          if ( record.getLoginId().contains( duprecord.getLoginId() ) )
          {
            errors.add( new ServiceError( "admin.fileload.errors.DUPLICATE_LOGIN_ID", duprecord.getLoginId() ) );
            createAndAddImportRecordErrors( importFile, record, errors );
            errorRecordCount++;
          }
        }
      }
    }

    importFile.setImportRecordErrorCount( errorRecordCount );
  }

  @Override
  public void verifyImportFile( ImportFile importFile, List records, boolean validateNodeRelationship ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );

  }

  protected Collection validateProgressImportRecord( ThrowdownProgressImportRecord record, Promotion promotion, Integer roundNumber )
  {
    Collection errors = new ArrayList();

    // Constraint: Login Id is required.
    checkRequired( record.getLoginId(), "admin.fileload.throwdown.progress.LOGIN_ID", errors );

    // Constraint: Total Performance is required.
    checkRequired( record.getTotalPerformanceToDate(), "admin.fileload.throwdown.progress.TOTAL_PERFORMANCE", errors );

    if ( StringUtils.isNotBlank( record.getLoginId() ) )
    {
      Participant pax = getParticipantService().getParticipantByUserName( record.getLoginId() );

      // Constraint: Participant with specified Login ID (User Name) must exist
      if ( pax == null )
      {
        errors.add( new ServiceError( "system.errors.UNKNOWN_USER_NAME" ) );
      }
      else if ( pax.getStatus() == null || pax.getStatus() != null && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
      {
        errors.add( new ServiceError( "admin.fileload.errors.USER_NOT_ACTIVE" ) );
      }
      // Constraint: participant should belong to Promotion competitor Audience
      else if ( teamService.getDivisionForUser( promotion.getId(), pax.getId(), roundNumber ) == null )
      {
        errors.add( new ServiceError( "admin.fileload.errors.THROWDOWN_PAX_NOT_IN_AUDIENCE" ) );
      }
      else
      {
        Match match = teamService.getMatchByPromotionAndRoundNumberAndTeam( promotion.getId(), pax.getId(), roundNumber );
        if ( match.getStatus().equals( MatchStatusType.lookup( MatchStatusType.PLAYED ) ) )
        {
          errors.add( new ServiceError( "admin.fileload.errors.THROWDOWN_MATCH_PAYOUT_COMPLETE" ) );
        }
      }
    }
    return errors;
  }

  private MatchTeamProgress buildProgress( ThrowdownProgressImportRecord source, boolean replaceValues, MatchTeamOutcome teamOutcome )
  {
    MatchTeamProgress matchTeamProgress = new MatchTeamProgress();
    if ( replaceValues )
    {
      matchTeamProgress.setProgressType( ThrowdownMatchProgressType.lookup( ThrowdownMatchProgressType.REPLACE ) );
    }
    else
    {
      matchTeamProgress.setProgressType( ThrowdownMatchProgressType.lookup( ThrowdownMatchProgressType.INCREMENTAL ) );
    }
    matchTeamProgress.setProgress( source.getTotalPerformanceToDate() );
    matchTeamProgress.setTeamOutcome( teamOutcome );
    return matchTeamProgress;
  }

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public TeamService getTeamService()
  {
    return teamService;
  }

  public void setTeamService( TeamService teamService )
  {
    this.teamService = teamService;
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

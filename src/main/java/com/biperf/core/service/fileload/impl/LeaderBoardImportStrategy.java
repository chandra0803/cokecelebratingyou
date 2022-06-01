
package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.LeaderBoardImportRecord;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.leaderboard.LeaderBoardAssociationRequest;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.util.StringUtils;

public class LeaderBoardImportStrategy extends ImportStrategy
{
  private static final Log logger = LogFactory.getLog( LeaderBoardImportStrategy.class );
  public static final String LEADERBOARDID = "leaderboard.label.LEADERBOARDID";
  public static final String ASOFDATE = "leaderboard.label.ASOFDATE";
  public static final String ACTION = "leaderboard.label.ACTION";
  private LeaderBoardService leaderBoardService;
  private ParticipantService participantService;
  private UserService userService;

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setLeaderBoardService( LeaderBoardService leaderBoardService )
  {
    this.leaderBoardService = leaderBoardService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  /**
   * Imports the specified import file
   * @param importFile
   * @param records
   * @throws ServiceErrorException
   */
  @Override
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    long counter = 0;
    logger.info( "processed record count: " + counter );

    // get leaderboard participants whose leaderboard is same as this leaderboard
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
    LeaderBoard leaderBoard = leaderBoardService.getLeaderBoardByIdWithAssociations( importFile.getLeaderboardId(), associationRequestCollection );
    
    Map<String, LeaderBoardParticipant> hashMap = new HashMap<String, LeaderBoardParticipant>();

    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    int errorCount = importFile.getImportRecordErrorCount();
    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      LeaderBoardImportRecord leaderBoardImportRecord = (LeaderBoardImportRecord)iterator.next();

      // if we find any errors then skip the record
      if ( !leaderBoardImportRecord.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      boolean hasPax = false;
      Set<LeaderBoardParticipant> newLeaderboardPax = leaderBoard.getParticipants();
      for ( LeaderBoardParticipant lbPax : leaderBoard.getParticipants() )
      {
        if ( leaderBoardImportRecord.getUserId().equals( lbPax.getUser().getId().toString() ) )
        {
          lbPax.setAsOfDate( importFile.getAsOfDate() );
          lbPax.setScore( String.valueOf( leaderBoardImportRecord.getScore() ) );
          lbPax.setActive( importFile.getActionType().equalsIgnoreCase( ImportFile.UPDATE ) ? Boolean.TRUE : Boolean.FALSE );
          hasPax = true;
          break;
        }
      }
      if ( !hasPax )
      {
        LeaderBoardParticipant leaderBoardParticipant = buildLeaderBoardParticipant( leaderBoardImportRecord, importFile );
        newLeaderboardPax.add( leaderBoardParticipant );
      }
      leaderBoard.setParticipants( newLeaderboardPax );
    }
    // copy assoation set of pax to transient list for sorting
    leaderBoard = sortLeaderBoardPaxOnSortOrder( leaderBoard );

    // calculating rank
    leaderBoard = calculateRankAndAddToPaxSet( leaderBoard );

    leaderBoardService.save( leaderBoard );

    importFile.setImportRecordErrorCount( errorCount );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param records
   *            the import records to import.
   * @param importFile
   *            the import file to import.
   */
  @Override
  public void verifyImportFile( ImportFile importFile, List records )
  {
    // start out with the number of current records with errors
    int importRecordErrorCount = importFile.getImportRecordErrorCount();
    Set<String> userIds = new HashSet<String>();
    long counter = 0;
    logger.info( "processed record count: " + counter );

    for ( Iterator iterator = records.iterator(); iterator.hasNext(); )
    {
      counter++;
      LeaderBoardImportRecord record = (LeaderBoardImportRecord)iterator.next();
      boolean hasNoErrors = record.getImportRecordErrors().isEmpty();
      boolean foundError = false;

      // guard clause - only process this record if there are no errors
      // with it
      if ( !record.getImportRecordErrors().isEmpty() )
      {
        continue;
      }

      Collection errors = validateLeaderBoardImportRecord( record, userIds, importFile.getLeaderboardId(), importFile.getActionType() );

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
   * Verifies the specified import file.
   * 
   * @param importFile
   *            the import file to import.
   * @param records
   *            the records to import.
   * @param validateNodeRelationship
   * @throws ServiceErrorException
   */
  @Override
  public void verifyImportFile( ImportFile importFile, List records, boolean fullrecordSet ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }
    verifyImportFile( importFile, records );
  }

  /**
   * Validates the given import record.
   * 
   * @param leaderboardRecord
   *            the import record to validate.
   * @return the number of import records with errors in the import file.
   */
  protected Collection validateLeaderBoardImportRecord( LeaderBoardImportRecord leaderBoardRecord, Set<String> userIds, Long leaderBoardId, String actionType )
  {
    Collection errors = new ArrayList();
    User user = null;

    // validate user
    if ( StringUtils.isEmpty( leaderBoardRecord.getUserName() ) )
    {
      errors.add( new ServiceError( "admin.fileload.errors.USER_NOT_VALID" ) );
    }
    else
    {
      user = userService.getUserByUserName( leaderBoardRecord.getUserName() );
      // get the userIds set size
      int usersInListBefore = userIds.size();
      if ( user != null )
      {
        // Add the current record userId to set and check for duplicate
        userIds.add( user.getUserName() );
        if ( userIds.size() == usersInListBefore )
        {
          errors.add( new ServiceError( "admin.fileload.errors.USER_ID_DUPLICATE" ) );
        }
      }
    }

    if ( null == user || user.getId() == null )
    {
      errors.add( new ServiceError( "admin.fileload.errors.USER_NOT_VALID" ) );
    }
    else
    {
      if ( !user.isActive() )
      {
        errors.add( new ServiceError( "admin.fileload.errors.USER_NOT_ACTIVE" ) );
      }
      else if ( actionType.equalsIgnoreCase( ImportFile.DELETE ) )
      {
        LeaderBoardParticipant leaderBoardPax = leaderBoardService.getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( leaderBoardId, user.getId() );
        if ( leaderBoardPax == null )
        {
          errors.add( new ServiceError( "admin.fileload.errors.LEADERBOARD_PAX_NOT_VALID" ) );
        }
        if ( leaderBoardRecord.getScore() != 0 )
        {
          errors.add( new ServiceError( "admin.fileload.errors.ACTIVITY_NOT_VALID" ) );
        }
      }
    }

    // check if score is -ve
    if ( leaderBoardRecord.getScore() < 0 )
    {
      errors.add( new ServiceError( "admin.fileload.errors.SCORE_IS_INVALID" ) );
    }

    return errors;
  }

  private LeaderBoardParticipant buildLeaderBoardParticipant( LeaderBoardImportRecord leaderBoardImportRecord, ImportFile importFile ) throws ServiceErrorException
  {
    LeaderBoardParticipant leaderBoardParticipant = new LeaderBoardParticipant();
    leaderBoardParticipant.setLeaderboard( leaderBoardService.getLeaderBoardById( importFile.getLeaderboardId() ) );
    leaderBoardParticipant.setUser( participantService.getParticipantById( Long.parseLong( leaderBoardImportRecord.getUserId() ) ) );
    leaderBoardParticipant.setAsOfDate( importFile.getAsOfDate() );
    leaderBoardParticipant.setScore( String.valueOf( leaderBoardImportRecord.getScore() ) );
    leaderBoardParticipant.setActive( importFile.getActionType().equalsIgnoreCase( ImportFile.UPDATE ) ? Boolean.TRUE : Boolean.FALSE );
    return leaderBoardParticipant;
  }

  private LeaderBoard sortLeaderBoardPaxOnSortOrder( LeaderBoard leaderBoard )
  {
    for ( LeaderBoardParticipant lbp : leaderBoard.getParticipants() )
    {
      if ( lbp.isActive() )
      {
        leaderBoard.getSortParticipants().add( lbp );
      }
    }
    if ( leaderBoard.getSortOrder().equals( LeaderBoardService.SORT_DESCENDING ) )
    {
      Collections.sort( leaderBoard.getSortParticipants(), DESC_COMPARATOR );
    }
    else
    {
      Collections.sort( leaderBoard.getSortParticipants(), ASCE_COMPARATOR );
    }
    return leaderBoard;
  }

  private static Comparator<LeaderBoardParticipant> ASCE_COMPARATOR = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp1.getScore() ) - Integer.parseInt( lbp2.getScore() );
    }
  };

  private static Comparator<LeaderBoardParticipant> DESC_COMPARATOR = new Comparator<LeaderBoardParticipant>()
  {
    public int compare( LeaderBoardParticipant lbp1, LeaderBoardParticipant lbp2 )
    {
      return Integer.parseInt( lbp2.getScore() ) - Integer.parseInt( lbp1.getScore() );
    }
  };

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private LeaderBoard calculateRankAndAddToPaxSet( LeaderBoard leaderBoard )
  {
    Collection<List<LeaderBoardParticipant>> c = leaderBoard.getSortParticipants().stream()
        .collect( Collectors.groupingBy( LeaderBoardParticipant::getScore, LinkedHashMap::new, Collectors.toList() ) ).values();

    int index = 1;
    for ( Iterator iterator = c.iterator(); iterator.hasNext(); )
    {
      List<LeaderBoardParticipant> lp = (List<LeaderBoardParticipant>)iterator.next();
      for ( LeaderBoardParticipant leaderBoardParticipant : lp )
      {
        leaderBoardParticipant.setParticipantRank( index );
      }
      index++;
    }
    return leaderBoard;
  }
}

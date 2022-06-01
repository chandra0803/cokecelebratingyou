
package com.biperf.core.service.leaderboard;

import java.util.List;

import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardPaxActivity;
import com.biperf.core.domain.leaderboard.LeaderBoardView;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

public interface LeaderBoardService extends SAO
{

  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "leaderBoardService";

  public static final String SORT_DESCENDING = "desc";
  public static final String LEADERBOARD_ACTIVE = "active";
  public static final String LEADERBOARD_PENDING = "pending";
  public static final String LEADERBOARD_ARCHIVED = "archived";

  // these constants are for navigation
  public static final String PAGE_DETAIL = "detail";
  public static final String PAGE_TILE = "tile";
  public static final String PAGE_ADMIN = "admin";
  public static final String TYPE_COPY = "copy";

  // LeaderBoard Service Methods Starts Here

  /**
   * Gets true if leaderboard name is available to create else false
   * 
   * @param leaderBoardName and currentLeaderBoardId
   * @return boolean
   */
  public boolean isLeaderBoardNameUnique( String leaderBoardName, Long currentLeaderBoardId );

  /**
   * Gets leaderboard
   * 
   * @param leaderBoard and rulesText
   * @return LeaderBoard
   */
  public LeaderBoard saveRulesCmText( LeaderBoard leaderBoard, String rulesText ) throws ServiceErrorException;

  /**
  * Gets the LeaderBoard
  * 
  * @param id
  * @return LeaderBoard
  */
  public LeaderBoard getLeaderBoardById( Long id ) throws ServiceErrorException;

  /**
   * @param leaderBoardId
   * @param associationRequestCollection
   * @return
   * @throws ServiceErrorException
   */
  public LeaderBoard getLeaderBoardJsonForDetail( Long leaderBoardId, AssociationRequestCollection associationRequestCollection ) throws ServiceErrorException;

  /**
   * @param leaderBoardId
   * @param associationRequestCollection
   * @return
   * @throws ServiceErrorException
   */
  public LeaderBoard getLeaderBoardJsonForTile( Long leaderBoardId ) throws ServiceErrorException;

  /**
   * @param userId
   * @return
   * @throws ServiceErrorException
   */
  public LeaderBoardView getLeaderBoardsForTile( Long userId ) throws ServiceErrorException;

  /**
   * @param userId
   * @param nameId
   * @return
   * @throws ServiceErrorException
   */
  public LeaderBoardView getLeaderBoardsForDetailUsingNameId( Long userId, String nameId ) throws ServiceErrorException;

  /**
   * @param leaderBoardId
   * @param associationRequestCollection
   * @return
   * @throws ServiceErrorException
   */
  public LeaderBoard getLeaderBoardByIdWithAssociations( Long leaderBoardId, AssociationRequestCollection associationRequestCollection ) throws ServiceErrorException;

  /**
   * Gets the list of LeaderBoard by userId.
   * 
   * @param userId
   * @return List
   */
  public List getLeaderBoardsByOwnerUserIdAndStatus( Long userId, String status ) throws ServiceErrorException;

  /**
   * Gets the list of LeaderBoard by status
   * 
   * @param status
   * @return List
   */
  public List getLeaderBoardByStatus( String status );

  /**
   * Save or Update the LeaderBoard 
   * 
   * @param leaderboard
   * @return LeaderBoard
   */
  public LeaderBoard saveLeaderBoard( LeaderBoard leaderboard ) throws ServiceErrorException;

  public LeaderBoard save( LeaderBoard leaderboard ) throws ServiceErrorException;

  // LeaderBoard Service Methods Ends Here

  // LeaderBoardParticipant Service Methods Starts Here
  /**
   * Gets the LeaderBoardParticipant by the id.
   * 
   * @param id
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant getLeaderBoardParticipantById( Long id ) throws ServiceErrorException;

  /**
   * Gets the LeaderBoardParticipants by LeaderBoardId.
   * 
   * @param leaderBoardId
   * @return List
   */
  public List getLeaderBoardParticipantsByLeaderBoardId( Long leaderBoardId, String paxStatus ) throws ServiceErrorException;

  /**
   * Save or Update LeaderBoardParticipant
   * 
   * @param leaderBoardparticipant
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant saveLeaderBoardParticipant( LeaderBoardParticipant leaderBoardParticipant ) throws ServiceErrorException;

  /**
   * Save or Update LeaderBoardParticipants list
   * 
   * @param LeaderBoardparticipant
   * @return LeaderBoardParticipant
   */
  public void saveLeaderBoardParticipantsList( List<LeaderBoardParticipant> leaderBoardParticipants ) throws ServiceErrorException;

  // LeaderBoardParticipant Service Methods Ends Here

  // LedaerBoardPaxActivity Service Methods Starts Here
  /**
   * Gets the LeaderBoardPaxActivity by the id.
   * 
   * @param id
   * @return LeaderBoardPaxActivity
   */
  public LeaderBoardPaxActivity getLeaderBoardPaxActivityById( Long id );

  /**
   * Gets list of LeaderBoardPaxActivity by UserId.
   * 
   * @param id
   * @return LeaderBoardParticipant
   * @throws ServiceErrorException
   */
  public List getLeaderBoardPaxActivityByUserId( Long userId ) throws ServiceErrorException;

  /**
   * Gets list of LeaderBoardPaxActivity by LeaderBoard Id
   * 
   * @param leaderBoardId
   * @return List
   */
  public List getLeaderBoardPaxActivityByLeaderBoardId( Long leaderBoardId ) throws ServiceErrorException;

  /**
   * Save or Update LeaderBoardPaxActivity 
   * 
   * @param leaderBoardPaxActivity
   * @return LeaderBoardPaxActivity
   */
  public LeaderBoardPaxActivity saveLeaderBoardPaxActivity( LeaderBoardPaxActivity leaderBoardPaxActivity ) throws ServiceErrorException;
  // LedaerBoardPaxActivity Service Methods Ends Here

  /**
   * 
   */
  public Integer isUserHasLiveLeaderBoard( Long userId );

  /**
   * Gets the LeaderBoardParticipant by the leaderBoardId and userId.
   * 
   * @param leaderBoardId
   * * @param userId
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( Long leaderBoardId, Long userId );

  public List<LeaderBoard> getUnexpiredLeaderBoards();
}


package com.biperf.core.dao.leaderboard;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.leaderboard.LeaderBoardPaxActivity;

public interface LeaderBoardDAO extends DAO
{

  // LeaderBoard DAO Methods Starts Here

  /**
   * Gets true if leaderboard name is available to create else false
   * 
   * @param leaderBoardName and currentLeaderBoardId
   * @return boolean
   */
  public boolean isLeaderBoardNameUnique( String leaderBoardName, Long currentLeaderBoardId );

  /**
   * Gets the LeaderBoard by using id.
   * 
   * @param id
   * @return LeaderBoard
   */
  public LeaderBoard getLeaderBoardById( Long id );

  /**
   * Gets list of LeaderBoard by using userId
   * 
   * @param userId of the owner and status
   * @return List
   */
  public List<LeaderBoard> getLeaderBoardsByOwnerUserIdAndStatus( Long userId, String status );

  /**
   * Gets list of LeaderBoard by using userId
   * 
   * @param userId of the owner
   * @return List
   */
  public List<LeaderBoard> getLeaderBoardsForTile( Long userId );

  /**
   * @param userId
   * @param nameId
   * @return
   */
  public List<LeaderBoard> getLeaderBoardsForDetailUsingNameId( Long userId, String nameId );

  /**
   * Gets list of LeaderBoard by status
   * 
   * @see com.biperf.core.dao.LeaderBoardDAO#getLeaderBoardStatus()
   * @param status
   * @return List
   */
  public List<LeaderBoard> getLeaderBoardByStatus( String status );

  /**
   * Save or Update LeaderBoard
   * 
   * @param leaderboard
   * @return LeaderBoard
   */
  public LeaderBoard saveLeaderBoard( LeaderBoard leaderboard );

  // LeaderBoard DAO Methods Ends Here

  // LeaderBoardParticipant DAO Methods Starts Here

  /**
   * Gets LeaderBoardParticipant by id.
   * 
   * @param id
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant getLeaderBoardParticipantById( Long id );

  /**
   * Gets list of LeaderBoardParticipant 
   * 
   * @param leaderBoard
   * @return LeaderBoardParticipant
   */
  public List<LeaderBoardParticipant> getLeaderBoardParticipantsByLeaderBoardId( Long leaderBoardId, String paxStatus );

  /**
   * Gets list of LeaderBoardParticipant 
   * 
   * @param leaderBoard
   * @return LeaderBoardParticipant
   */
  public List<LeaderBoardParticipant> getLeaderBoardParticipantsByLeaderBoardId( Long leaderBoardId );

  /**
   * @param participantId
   * @param leaderBoardId
   * @return leaderBoardParticipant
   */
  public LeaderBoardParticipant getLeaderBoardParticipantByParticipantIdAndLeaderBoardId( Long participantId, Long leaderBoardId );

  /**
   * Save or update the LeaderBoardParticipant.
   * 
   * @param leaderBoardParticipant
   * @return LeaderBoardParticipant
   */
  public LeaderBoardParticipant saveLeaderBoardParticipant( LeaderBoardParticipant leaderBoardParticipant );

  /**
   * Save or update the LeaderBoardParticipants list.
   * 
   * @param leaderBoardParticipant
   * @return LeaderBoardParticipant
   */
  public void saveLeaderBoardParticipantsList( List<LeaderBoardParticipant> list );

  // LeaderBoardParticipant DAO Methods Ends Here

  // LeaderBoardPaxActivity DAO Methods Starts Here
  /**
   * Gets the LeaderBoardPaxActivity by id.
   * 
   * @param id
   * @return LeaderBoardPaxActivity
   */
  public LeaderBoardPaxActivity getLeaderBoardPaxActivityById( Long id );

  /**
   * Gets list of LeaderBoardPaxActivity  by UserId.
   * 
   * @param userId
   * @return List
   */
  public List<LeaderBoardPaxActivity> getLeaderBoardPaxActivityByUserId( Long userId );

  /**
   * Gets list of LeaderBoardPaxActivity by leaderBoardId
   * 
   * @param LeaderBoard id
   * @return List
   */
  public List<LeaderBoardPaxActivity> getLeaderBoardPaxActivityByLeaderBoardId( Long leaderBoardId );

  /**
   * Save or Update LeaderBoardPaxActivity.
   * 
   * @param leaderBoardPaxActivity
   * @return LeaderBoardPaxActivity
   */
  public LeaderBoardPaxActivity saveLeaderBoardPaxActivity( LeaderBoardPaxActivity leaderBoardPaxActivity );

  // LeaderBoardPaxActivity DAO Methods Ends Here

  public Integer isUserHasLiveLeaderBoard( Long userId );

  public List<LeaderBoard> getUnexpiredLeaderBoards();
}

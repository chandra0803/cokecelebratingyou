
package com.biperf.core.service.leaderboard;

import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.service.BaseAssociationRequest;

public class LeaderBoardAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int ALL_PAX = 1;

  public LeaderBoardAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    LeaderBoard leaderBoard = (LeaderBoard)domainObject;

    switch ( hydrateLevel )
    {
      case ALL_PAX:
      {
        hydrateAllLeaderBoardPax( leaderBoard );
        break;
      }
      default:
      {
        break;
      }
    }
  }

  private void hydrateAllLeaderBoardPax( LeaderBoard leaderBoard )
  {
    initialize( leaderBoard.getParticipants() );
  }

}

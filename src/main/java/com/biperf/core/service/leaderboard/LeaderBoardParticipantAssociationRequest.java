
package com.biperf.core.service.leaderboard;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.BaseAssociationRequest;

public class LeaderBoardParticipantAssociationRequest extends BaseAssociationRequest
{
  private int hydrateLevel = 0;

  public static final int EMAIL = 5;
  public static final int EMPLOYEE = 6;
  public static final int PRIMARY_ADDRESS = 7;

  public LeaderBoardParticipantAssociationRequest( int hydrateLevel )
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
    Participant participant = (Participant)domainObject;

    switch ( hydrateLevel )
    {
      case EMAIL:
        hyderateEmail( participant );
        break;
      case EMPLOYEE:
        hyderateEmployee( participant );
        break;
      case PRIMARY_ADDRESS:
        hyderatePrimaryAddress( participant );
        break;
      default:
        break;
    }
  }

  private void hyderateEmail( Participant participant )
  {
    initialize( participant.getUserEmailAddresses() );
  }

  private void hyderateEmployee( Participant participant )
  {
    initialize( participant.getParticipantEmployers() );
  }

  private void hyderatePrimaryAddress( Participant participant )
  {
    initialize( participant.getUserAddresses() );
  }
}

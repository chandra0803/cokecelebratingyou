
package com.biperf.core.service.throwdown;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.BaseAssociationRequest;

public class SmackTalkParticipantAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;
  public static final int EMPLOYEE = 1;
  public static final int ADDRESS = 2;

  public SmackTalkParticipantAssociationRequest( int hydrateLevel )
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
      case EMPLOYEE:
        hyderateEmployee( participant );
        break;
      case ADDRESS:
        hyderateAddress( participant );
        break;
      default:
        break;
    }
  }

  private void hyderateEmployee( Participant participant )
  {
    initialize( participant.getParticipantEmployers() );
  }

  private void hyderateAddress( Participant participant )
  {
    initialize( participant.getUserAddresses() );
  }
}


package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;

/**
 * 
 * DIYQuizManagerParticipantView.
 * 
 * @author kandhi
 * @since Jul 23, 2013
 * @version 1.0
 */
public class DIYQuizManagerParticipantView
{
  private List<DIYQuizParticipantView> participants;

  public DIYQuizManagerParticipantView( Set<Participant> paxList )
  {
    if ( paxList != null )
    {
      participants = new ArrayList<DIYQuizParticipantView>( paxList.size() );
      for ( Participant participant : paxList )
      {
        participants.add( new DIYQuizParticipantView( null,
                                                      participant.getId(),
                                                      participant.getFirstName(),
                                                      participant.getLastName(),
                                                      participant.getPaxOrgName(),
                                                      participant.getPrimaryCountryCode(),
                                                      participant.getPrimaryCountryName(),
                                                      participant.getAvatarSmall(),
                                                      participant.getDepartmentType(),
                                                      participant.getPositionType() ) );
      }
    }
  }

  public List<DIYQuizParticipantView> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<DIYQuizParticipantView> participants )
  {
    this.participants = participants;
  }

}

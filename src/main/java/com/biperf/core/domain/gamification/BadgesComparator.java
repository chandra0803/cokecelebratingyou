/**
 * 
 */

package com.biperf.core.domain.gamification;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author poddutur
 *
 */
public class BadgesComparator implements Comparator, Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3751103052018435893L;

  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof ParticipantBadge ) || ! ( o2 instanceof ParticipantBadge ) )
    {
      throw new ClassCastException( "Object is not a ParticipantBadge domain object!" );
    }
    ParticipantBadge participantBadge1 = (ParticipantBadge)o1;
    ParticipantBadge participantBadge2 = (ParticipantBadge)o2;

    if ( participantBadge1 != null && participantBadge2 != null )
    {
      if ( participantBadge1.getBadgeRule() != null && participantBadge2.getBadgeRule() != null )
      {
        return participantBadge1.getBadgeRule().getBadgeNameTextFromCM().toUpperCase().compareTo( participantBadge2.getBadgeRule().getBadgeNameTextFromCM().toUpperCase() );
      }
    }
    return 0;
  }

}

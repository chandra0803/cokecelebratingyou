
package com.biperf.core.service.throwdown;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Division;

public class ParticipantAudienceConflictResult implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Participant participant = null;
  private Set<Division> divisions = new HashSet<Division>();
  private boolean wasActiveInPreviousDivision = false;
  private Division previousDivision = null;
  private Division assignedDivision = null;

  public Set<Division> getDivisions()
  {
    return divisions;
  }

  public void setDivisions( Set<Division> divisions )
  {
    this.divisions = divisions;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof ParticipantAudienceConflictResult ) )
    {
      return false;
    }

    final ParticipantAudienceConflictResult paxConflict = (ParticipantAudienceConflictResult)o;

    if ( participant != null && paxConflict.getParticipant() != null ? !participant.getUserName().equals( paxConflict.getParticipant().getUserName() ) : paxConflict.getParticipant() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getParticipant() != null ? getParticipant().hashCode() : 0;
  }

  public Division getAssignedDivision()
  {
    return assignedDivision;
  }

  public void setAssignedDivision( Division assignedDivision )
  {
    this.assignedDivision = assignedDivision;
  }

  public Division getPreviousDivision()
  {
    return previousDivision;
  }

  public void setPreviousDivision( Division previousDivision )
  {
    this.previousDivision = previousDivision;
  }

  public boolean isWasActiveInPreviousDivision()
  {
    return wasActiveInPreviousDivision;
  }

  public void setWasActiveInPreviousDivision( boolean wasActiveInPreviousDivision )
  {
    this.wasActiveInPreviousDivision = wasActiveInPreviousDivision;
  }
}

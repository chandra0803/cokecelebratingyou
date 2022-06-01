
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

public class Approver extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private ApproverCriteria approverCriteria;
  private Participant participant;

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    Approver other = (Approver)obj;
    if ( participant == null )
    {
      if ( other.participant != null )
      {
        return false;
      }
    }
    else if ( !participant.equals( other.participant ) )
    {
      return false;
    }
    return true;
  }

  public Approver deepCopy()
  {
    Approver clone = new Approver();
    clone.setApproverCriteria( this.approverCriteria );
    clone.setParticipant( this.participant );
    return clone;
  }

  public ApproverCriteria getApproverCriteria()
  {
    return approverCriteria;
  }

  public void setApproverCriteria( ApproverCriteria approverCriteria )
  {
    this.approverCriteria = approverCriteria;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

}

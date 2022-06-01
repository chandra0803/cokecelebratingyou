
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.participant.Participant;

/**
 * 
 * SSIContestApprover.
 * 
 * @author kandhi
 * @since Nov 4, 2014
 * @version 1.0
 */
public class SSIContestApprover extends BaseDomain
{
  private SSIContest contest;
  private Participant approver;
  private SSIApproverType approverType;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Participant getApprover()
  {
    return approver;
  }

  public void setApprover( Participant approver )
  {
    this.approver = approver;
  }

  public SSIApproverType getApproverType()
  {
    return approverType;
  }

  public void setApproverType( SSIApproverType approverType )
  {
    this.approverType = approverType;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( object == null )
    {
      return false;
    }

    SSIContestApprover other = (SSIContestApprover)object;
    if ( contest == null )
    {
      if ( other.contest != null )
      {
        return false;
      }
    }
    else if ( !contest.equals( other.contest ) )
    {
      return false;
    }

    if ( approver == null )
    {
      if ( other.approver != null )
      {
        return false;
      }
    }
    else if ( !approver.equals( other.approver ) )
    {
      return false;
    }

    if ( approverType == null )
    {
      if ( other.approverType != null )
      {
        return false;
      }
    }
    else if ( !approverType.equals( other.approverType ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( approver == null ? 0 : approver.hashCode() );
    result = prime * result + ( approverType == null ? 0 : approverType.hashCode() );
    return result;
  }
}


package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

public class ApproverCriteria extends BaseDomain
{

  private static final long serialVersionUID = 1L;

  private ApproverOption approverOption;
  private String approverValue;
  private Integer minVal;
  private Integer maxVal;
  private Set<Approver> approvers = new HashSet<>();

  public ApproverCriteria()
  {
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( approverValue == null ? 0 : approverValue.hashCode() );
    result = prime * result + ( approvers == null ? 0 : approvers.hashCode() );
    result = prime * result + ( maxVal == null ? 0 : maxVal.hashCode() );
    result = prime * result + ( minVal == null ? 0 : minVal.hashCode() );
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
    ApproverCriteria other = (ApproverCriteria)obj;
    if ( approverValue == null )
    {
      if ( other.approverValue != null )
      {
        return false;
      }
    }
    else if ( !approverValue.equals( other.approverValue ) )
    {
      return false;
    }
    if ( approvers == null )
    {
      if ( other.approvers != null )
      {
        return false;
      }
    }
    else if ( !approvers.equals( other.approvers ) )
    {
      return false;
    }
    if ( maxVal == null )
    {
      if ( other.maxVal != null )
      {
        return false;
      }
    }
    else if ( !maxVal.equals( other.maxVal ) )
    {
      return false;
    }
    if ( minVal == null )
    {
      if ( other.minVal != null )
      {
        return false;
      }
    }
    else if ( !minVal.equals( other.minVal ) )
    {
      return false;
    }
    return true;
  }

  public ApproverCriteria deepCopy()
  {
    ApproverCriteria clone = new ApproverCriteria();
    clone.setApproverOption( this.approverOption );
    clone.setApproverValue( this.approverValue );
    clone.setMaxVal( this.maxVal );
    clone.setMinVal( this.minVal );
    clone.setApprovers( new HashSet<>() );
    if ( getApprovers() != null && getApprovers().size() > 0 )
    {
      for ( Iterator<Approver> iter = this.getApprovers().iterator(); iter.hasNext(); )
      {
        Approver cloneApprover = iter.next();
        clone.addApprover( cloneApprover.deepCopy() );
      }
    }
    return clone;
  }

  public ApproverOption getApproverOption()
  {
    return approverOption;
  }

  public void setApproverOption( ApproverOption approverOption )
  {
    this.approverOption = approverOption;
  }

  public String getApproverValue()
  {
    return approverValue;
  }

  public void setApproverValue( String approverValue )
  {
    this.approverValue = approverValue;
  }

  public Integer getMinVal()
  {
    return minVal;
  }

  public void setMinVal( Integer minVal )
  {
    this.minVal = minVal;
  }

  public Integer getMaxVal()
  {
    return maxVal;
  }

  public void setMaxVal( Integer maxVal )
  {
    this.maxVal = maxVal;
  }

  public void addApprover( Approver approver )
  {
    approver.setApproverCriteria( this );
    this.approvers.add( approver );
  }

  public Set<Approver> getApprovers()
  {
    return approvers;
  }

  public void setApprovers( Set<Approver> approvers )
  {
    this.approvers = approvers;
  }

  public Set<Participant> getApproversAsParticipantType()
  {
    Set<Participant> paxSet = new HashSet<Participant>();
    for ( Approver a : approvers )
    {
      paxSet.add( a.getParticipant() );
    }

    return paxSet;
  }

}

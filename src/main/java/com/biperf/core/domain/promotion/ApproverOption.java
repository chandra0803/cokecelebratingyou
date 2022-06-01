
package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.CustomApproverRoutingType;
import com.biperf.core.domain.enums.CustomApproverType;

public class ApproverOption extends BaseDomain implements Comparable<ApproverOption>
{
  private static final long serialVersionUID = 1L;

  private NominationPromotion nominationPromotion;
  private Long approvalLevel;
  private CustomApproverType approverType;
  private Long characteristicId;
  private int sequenceNum;
  private CustomApproverRoutingType approverRoutingType;

  private Set<ApproverCriteria> approverCriteria = new HashSet<>();

  public ApproverOption()
  {
  }

  public ApproverOption deepCopy()
  {
    ApproverOption clone = new ApproverOption();
    clone.setApprovalLevel( this.approvalLevel );
    clone.setApproverType( this.approverType );
    clone.setNominationPromotion( this.nominationPromotion );
    clone.setCharacteristicId( this.characteristicId );
    clone.setSequenceNum( this.sequenceNum );
    clone.setApproverRoutingType( this.approverRoutingType );

    clone.setApproverCriteria( new HashSet<>() );
    // Only specific approvers are copied. Others keep the option only. (File-loaded approver info
    // is not copied.)
    if ( approverType != null && CustomApproverType.SPECIFIC_APPROVERS.equals( approverType.getCode() ) )
    {
      if ( getApproverCriteria() != null && this.getApproverCriteria().size() > 0 )
      {
        for ( Iterator<ApproverCriteria> iter = getApproverCriteria().iterator(); iter.hasNext(); )
        {
          ApproverCriteria appCriteria = iter.next();
          ApproverCriteria cloned = appCriteria.deepCopy();
          clone.addApproverCriteria( cloned );
        }
      }
    }

    return clone;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( approvalLevel == null ? 0 : approvalLevel.hashCode() );
    result = prime * result + ( approverCriteria == null ? 0 : approverCriteria.hashCode() );
    result = prime * result + ( approverType == null ? 0 : approverType.hashCode() );
    result = prime * result + ( characteristicId == null ? 0 : characteristicId.hashCode() );
    result = prime * result + ( nominationPromotion == null ? 0 : nominationPromotion.hashCode() );
    result = prime * result + ( approverRoutingType == null ? 0 : approverRoutingType.hashCode() );
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
    ApproverOption other = (ApproverOption)obj;
    if ( approvalLevel == null )
    {
      if ( other.approvalLevel != null )
      {
        return false;
      }
    }
    else if ( !approvalLevel.equals( other.approvalLevel ) )
    {
      return false;
    }
    if ( approverCriteria == null )
    {
      if ( other.approverCriteria != null )
      {
        return false;
      }
    }
    else if ( !approverCriteria.equals( other.approverCriteria ) )
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
    if ( characteristicId == null )
    {
      if ( other.characteristicId != null )
      {
        return false;
      }
    }
    else if ( !characteristicId.equals( other.characteristicId ) )
    {
      return false;
    }
    if ( nominationPromotion == null )
    {
      if ( other.nominationPromotion != null )
      {
        return false;
      }
    }
    else if ( !nominationPromotion.equals( other.nominationPromotion ) )
    {
      return false;
    }
    if ( approverRoutingType == null )
    {
      if ( other.approverRoutingType != null )
      {
        return false;
      }
    }
    else if ( !approverRoutingType.equals( other.approverRoutingType ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo( ApproverOption other )
  {
    return this.getApprovalLevel().compareTo( other.getApprovalLevel() );
  }

  public Long getApprovalLevel()
  {
    return approvalLevel;
  }

  public void setApprovalLevel( Long approvalLevel )
  {
    this.approvalLevel = approvalLevel;
  }

  public CustomApproverType getApproverType()
  {
    return approverType;
  }

  public void setApproverType( CustomApproverType approverType )
  {
    this.approverType = approverType;
  }

  public NominationPromotion getNominationPromotion()
  {
    return nominationPromotion;
  }

  public void setNominationPromotion( NominationPromotion nominationPromotion )
  {
    this.nominationPromotion = nominationPromotion;
  }

  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  public void addApproverCriteria( ApproverCriteria approverCriteria )
  {
    approverCriteria.setApproverOption( this );
    this.approverCriteria.add( approverCriteria );
  }

  public Set<ApproverCriteria> getApproverCriteria()
  {
    return approverCriteria;
  }

  public void setApproverCriteria( Set<ApproverCriteria> approverCriteria )
  {
    this.approverCriteria = approverCriteria;
  }

  public boolean isBehaviourType()
  {
    return approverType == null ? false : approverType.getCode().equalsIgnoreCase( CustomApproverType.BEHAVIOR );
  }

  public boolean isAwardType()
  {
    return approverType == null ? false : approverType.getCode().equalsIgnoreCase( CustomApproverType.AWARD );
  }

  public boolean isCharacteristicType()
  {
    return approverType == null ? false : approverType.getCode().equalsIgnoreCase( CustomApproverType.CHARACTERISTIC );
  }

  public boolean isSpecificApprovType()
  {
    return approverType == null ? false : approverType.getCode().equalsIgnoreCase( CustomApproverType.SPECIFIC_APPROVERS );
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public CustomApproverRoutingType getApproverRoutingType()
  {
    return approverRoutingType;
  }

  public void setApproverRoutingType( CustomApproverRoutingType approverRoutingType )
  {
    this.approverRoutingType = approverRoutingType;
  }

}


package com.biperf.core.domain.promotion;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

public class PostProcessJobs extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private String processBeanName;
  private String promotionType;
  private Long claimId;
  private Long journalId;
  private String triggerName;
  private String triggerGroup;
  private Set<PostProcessPayoutCalculation> payOutCalculationResult = new HashSet<PostProcessPayoutCalculation>();
  private boolean fired = false;
  private Date firedDate;
  private int retryCount;
  private Date retryCountChangeDate;

  public String getProcessBeanName()
  {
    return processBeanName;
  }

  public void setProcessBeanName( String processBeanName )
  {
    this.processBeanName = processBeanName;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getJournalId()
  {
    return journalId;
  }

  public void setJournalId( Long journalId )
  {
    this.journalId = journalId;
  }

  public String getTriggerName()
  {
    return triggerName;
  }

  public void setTriggerName( String triggerName )
  {
    this.triggerName = triggerName;
  }

  public String getTriggerGroup()
  {
    return triggerGroup;
  }

  public void setTriggerGroup( String triggerGroup )
  {
    this.triggerGroup = triggerGroup;
  }

  public Set getPayOutCalculationResult()
  {
    return payOutCalculationResult;
  }

  public void setPayOutCalculationResult( Set payOutCalculationResult )
  {
    this.payOutCalculationResult = payOutCalculationResult;
  }

  public boolean isFired()
  {
    return fired;
  }

  public void setFired( boolean fired )
  {
    this.fired = fired;
  }

  public Date getFiredDate()
  {
    return firedDate;
  }

  public void setFiredDate( Date firedDate )
  {
    this.firedDate = firedDate;
  }

  public int getRetryCount()
  {
    return retryCount;
  }

  public void setRetryCount( int retryCount )
  {
    this.retryCount = retryCount;
  }

  public Date getRetryCountChangeDate()
  {
    return retryCountChangeDate;
  }

  public void setRetryCountChangeDate( Date retryCountChangeDate )
  {
    this.retryCountChangeDate = retryCountChangeDate;
  }

  /**
   * {@inheritDoc}
   */
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
    PostProcessJobs other = (PostProcessJobs)obj;
    if ( claimId == null )
    {
      if ( other.claimId != null )
      {
        return false;
      }
    }
    else if ( !claimId.equals( other.claimId ) )
    {
      return false;
    }
    if ( fired != other.fired )
    {
      return false;
    }
    if ( firedDate == null )
    {
      if ( other.firedDate != null )
      {
        return false;
      }
    }
    else if ( !firedDate.equals( other.firedDate ) )
    {
      return false;
    }
    if ( journalId == null )
    {
      if ( other.journalId != null )
      {
        return false;
      }
    }
    else if ( !journalId.equals( other.journalId ) )
    {
      return false;
    }
    if ( processBeanName == null )
    {
      if ( other.processBeanName != null )
      {
        return false;
      }
    }
    else if ( !processBeanName.equals( other.processBeanName ) )
    {
      return false;
    }
    if ( promotionType == null )
    {
      if ( other.promotionType != null )
      {
        return false;
      }
    }
    else if ( !promotionType.equals( other.promotionType ) )
    {
      return false;
    }
    if ( retryCount != other.retryCount )
    {
      return false;
    }
    if ( retryCountChangeDate == null )
    {
      if ( other.retryCountChangeDate != null )
      {
        return false;
      }
    }
    else if ( !retryCountChangeDate.equals( other.retryCountChangeDate ) )
    {
      return false;
    }
    if ( triggerGroup == null )
    {
      if ( other.triggerGroup != null )
      {
        return false;
      }
    }
    else if ( !triggerGroup.equals( other.triggerGroup ) )
    {
      return false;
    }
    if ( triggerName == null )
    {
      if ( other.triggerName != null )
      {
        return false;
      }
    }
    else if ( !triggerName.equals( other.triggerName ) )
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( claimId == null ? 0 : claimId.hashCode() );
    result = prime * result + ( fired ? 1231 : 1237 );
    result = prime * result + ( firedDate == null ? 0 : firedDate.hashCode() );
    result = prime * result + ( journalId == null ? 0 : journalId.hashCode() );
    result = prime * result + ( processBeanName == null ? 0 : processBeanName.hashCode() );
    result = prime * result + ( promotionType == null ? 0 : promotionType.hashCode() );
    result = prime * result + retryCount;
    result = prime * result + ( retryCountChangeDate == null ? 0 : retryCountChangeDate.hashCode() );
    result = prime * result + ( triggerGroup == null ? 0 : triggerGroup.hashCode() );
    result = prime * result + ( triggerName == null ? 0 : triggerName.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "PostProcessJobs [processBeanName=" + processBeanName + ", promotionType=" + promotionType + ", claimId=" + claimId + ", journalId=" + journalId + ", triggerName=" + triggerName
        + ", triggerGroup=" + triggerGroup + ", fired=" + fired + ", firedDate=" + firedDate + ", retryCount=" + retryCount + ", retryCountChangeDate=" + retryCountChangeDate + "]";
  }

  public void addPayOutCalculationResult( PostProcessPayoutCalculation postProcessPayoutCalculation )
  {
    postProcessPayoutCalculation.setPostProcessJobs( this );
    this.payOutCalculationResult.add( postProcessPayoutCalculation );
  }

}

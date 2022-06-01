
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * SSIContestActivity.
 * 
 * @author chowdhur
 * @since Nov 12, 2014
 */
public class SSIContestActivity extends BaseDomain
{
  private SSIContest contest;
  private String description;
  private Double incrementAmount;
  private Long payoutAmount;
  private Double minQualifier;
  private Long payoutCapAmount;
  private Double goalAmount;
  private int sequenceNumber;
  private String payoutDescription;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Double getIncrementAmount()
  {
    return incrementAmount;
  }

  public void setIncrementAmount( Double incrementAmount )
  {
    this.incrementAmount = incrementAmount;
  }

  public Long getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Long payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public Double getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( Double minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public Long getPayoutCapAmount()
  {
    return payoutCapAmount;
  }

  public void setPayoutCapAmount( Long payoutCapAmount )
  {
    this.payoutCapAmount = payoutCapAmount;
  }

  public Double getGoalAmount()
  {
    return goalAmount;
  }

  public void setGoalAmount( Double goalAmount )
  {
    this.goalAmount = goalAmount;
  }

  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( description == null ? 0 : description.hashCode() );
    return result;
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
    SSIContestActivity other = (SSIContestActivity)obj;
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
    if ( description == null )
    {
      if ( other.description != null )
      {
        return false;
      }
    }
    else if ( !description.equals( other.description ) )
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "SSIContestActivity [contest=" + contest + ", description=" + description + ", incrementAmount=" + incrementAmount + ", payoutAmount=" + payoutAmount + ", minQualifier=" + minQualifier
        + ", payoutCapAmount=" + payoutCapAmount + ", goalAmount=" + goalAmount + ", sequenceNumber=" + sequenceNumber + "]";
  }

}

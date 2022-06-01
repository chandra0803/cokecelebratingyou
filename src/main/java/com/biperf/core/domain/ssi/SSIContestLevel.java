
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.gamification.BadgeRule;

/**
 * 
 * SSIContestLevel.
 * 
 * @author chowdhur
 * @since Dec 15, 2014
 */
public class SSIContestLevel extends BaseDomain
{
  private SSIContest contest;
  private BadgeRule badgeRule;
  private Double goalAmount;
  private Long payoutAmount;
  private String payoutDesc;
  private Integer sequenceNumber;

  public SSIContestLevel()
  {

  }

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }

  public Double getGoalAmount()
  {
    return goalAmount;
  }

  public void setGoalAmount( Double goalAmount )
  {
    this.goalAmount = goalAmount;
  }

  public Long getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Long payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getPayoutDesc()
  {
    return payoutDesc;
  }

  public void setPayoutDesc( String payoutDesc )
  {
    this.payoutDesc = payoutDesc;
  }

  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "SSIContestLevel [contest=" + contest + ", badgeRule=" + badgeRule + ", goalAmount=" + goalAmount + ", payoutAmount=" + payoutAmount + ", payoutDesc=" + payoutDesc + ", sequenceNumber="
        + sequenceNumber + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( badgeRule == null ? 0 : badgeRule.hashCode() );
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( goalAmount == null ? 0 : goalAmount.hashCode() );
    result = prime * result + ( payoutAmount == null ? 0 : payoutAmount.hashCode() );
    result = prime * result + ( payoutDesc == null ? 0 : payoutDesc.hashCode() );
    result = prime * result + ( sequenceNumber == null ? 0 : sequenceNumber.hashCode() );
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
    SSIContestLevel other = (SSIContestLevel)obj;
    if ( badgeRule == null )
    {
      if ( other.badgeRule != null )
      {
        return false;
      }
    }
    else if ( !badgeRule.equals( other.badgeRule ) )
    {
      return false;
    }
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
    if ( goalAmount == null )
    {
      if ( other.goalAmount != null )
      {
        return false;
      }
    }
    else if ( !goalAmount.equals( other.goalAmount ) )
    {
      return false;
    }
    if ( payoutAmount == null )
    {
      if ( other.payoutAmount != null )
      {
        return false;
      }
    }
    else if ( !payoutAmount.equals( other.payoutAmount ) )
    {
      return false;
    }
    if ( payoutDesc == null )
    {
      if ( other.payoutDesc != null )
      {
        return false;
      }
    }
    else if ( !payoutDesc.equals( other.payoutDesc ) )
    {
      return false;
    }
    if ( sequenceNumber == null )
    {
      if ( other.sequenceNumber != null )
      {
        return false;
      }
    }
    else if ( !sequenceNumber.equals( other.sequenceNumber ) )
    {
      return false;
    }
    return true;
  }

}

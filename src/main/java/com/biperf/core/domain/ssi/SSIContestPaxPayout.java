
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;

public class SSIContestPaxPayout extends BaseDomain
{
  private static final long serialVersionUID = 6654583605711292924L;

  private SSIContest contest;
  private Participant participant;
  private Double payoutAmount;
  private Journal journal;
  private Short awardIssuanceNumber;
  private Integer stackRankPosition;
  private Double activityAmount;
  private BadgeRule badgeRule;
  private String payoutDescription;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public Double getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Double payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public Journal getJournal()
  {
    return journal;
  }

  public void setJournal( Journal journal )
  {
    this.journal = journal;
  }

  public Short getAwardIssuanceNumber()
  {
    return awardIssuanceNumber;
  }

  public void setAwardIssuanceNumber( Short awardIssuanceNumber )
  {
    this.awardIssuanceNumber = awardIssuanceNumber;
  }

  public Integer getStackRankPosition()
  {
    return stackRankPosition;
  }

  public void setStackRankPosition( Integer stackRankPosition )
  {
    this.stackRankPosition = stackRankPosition;
  }

  public Double getActivityAmount()
  {
    return activityAmount;
  }

  public void setActivityAmount( Double activityAmount )
  {
    this.activityAmount = activityAmount;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
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
    result = prime * result + ( activityAmount == null ? 0 : activityAmount.hashCode() );
    result = prime * result + ( awardIssuanceNumber == null ? 0 : awardIssuanceNumber.hashCode() );
    result = prime * result + ( badgeRule == null ? 0 : badgeRule.hashCode() );
    result = prime * result + ( contest == null ? 0 : contest.hashCode() );
    result = prime * result + ( journal == null ? 0 : journal.hashCode() );
    result = prime * result + ( participant == null ? 0 : participant.hashCode() );
    result = prime * result + ( payoutAmount == null ? 0 : payoutAmount.hashCode() );
    result = prime * result + ( payoutDescription == null ? 0 : payoutDescription.hashCode() );
    result = prime * result + ( stackRankPosition == null ? 0 : stackRankPosition.hashCode() );
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
    SSIContestPaxPayout other = (SSIContestPaxPayout)obj;
    if ( activityAmount == null )
    {
      if ( other.activityAmount != null )
      {
        return false;
      }
    }
    else if ( !activityAmount.equals( other.activityAmount ) )
    {
      return false;
    }
    if ( awardIssuanceNumber == null )
    {
      if ( other.awardIssuanceNumber != null )
      {
        return false;
      }
    }
    else if ( !awardIssuanceNumber.equals( other.awardIssuanceNumber ) )
    {
      return false;
    }
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
    if ( journal == null )
    {
      if ( other.journal != null )
      {
        return false;
      }
    }
    else if ( !journal.equals( other.journal ) )
    {
      return false;
    }
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
    if ( payoutDescription == null )
    {
      if ( other.payoutDescription != null )
      {
        return false;
      }
    }
    else if ( !payoutDescription.equals( other.payoutDescription ) )
    {
      return false;
    }
    if ( stackRankPosition == null )
    {
      if ( other.stackRankPosition != null )
      {
        return false;
      }
    }
    else if ( !stackRankPosition.equals( other.stackRankPosition ) )
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
    return "SSIContestPaxPayout [contest=" + contest + ", participant=" + participant + ", payoutAmount=" + payoutAmount + ", journal=" + journal + ", awardIssuanceNumber=" + awardIssuanceNumber
        + ", stackRankPosition=" + stackRankPosition + ", activityAmount=" + activityAmount + ", badgeRule=" + badgeRule + ", payoutDescription=" + payoutDescription + "]";
  }

}

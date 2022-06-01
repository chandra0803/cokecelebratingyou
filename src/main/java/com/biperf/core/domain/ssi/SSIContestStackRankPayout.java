
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.gamification.BadgeRule;

public class SSIContestStackRankPayout extends BaseDomain
{
  private static final long serialVersionUID = 6659582605701282824L;

  private SSIContest contest;
  private Long rankPosition;
  private Long payoutAmount;
  private String payoutDescription;
  private BadgeRule badgeRule;

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Long getRankPosition()
  {
    return rankPosition;
  }

  public void setRankPosition( Long rankPosition )
  {
    this.rankPosition = rankPosition;
  }

  public Long getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Long payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( object == null || ! ( object instanceof SSIContestStackRankPayout ) )
    {
      return false;
    }

    if ( this.getId() == null || this.getRankPosition() == null )
    {
      return false;
    }
    // two Stack Rank Payouts of the same contest can not have the same rank
    SSIContestStackRankPayout other = (SSIContestStackRankPayout)object;
    return this.getId().equals( other.getId() )
        || ( contest != null && contest.getId() != null && contest.getId().equals( other.getContest().getId() ) ? rankPosition.equals( other.getRankPosition() ) : false );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().hashCode() );
    result = prime * result + ( contest == null || contest.getId() == null ? 0 : contest.getId().hashCode() );
    result = prime * result + ( rankPosition == null ? 0 : rankPosition.hashCode() );
    return result;
  }
}

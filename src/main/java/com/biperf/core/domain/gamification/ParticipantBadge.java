
package com.biperf.core.domain.gamification;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.user.User;

public class ParticipantBadge extends BaseDomain implements Comparable
{

  private static final long serialVersionUID = 1L;
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Badge badgePromotion;
  private User participant;
  private Boolean isEarned;
  private Date earnedDate;
  private Long sentCount;
  private Long receivedCount;
  private String status;
  private BadgeRule badgeRule;
  private Boolean isEarnedAllBehaviorPoints;
  private Boolean isBadgePointsEarned;
  private SSIContest contest;
  private Long claimId;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Boolean getIsBadgePointsEarned()
  {
    return isBadgePointsEarned;
  }

  public void setIsBadgePointsEarned( Boolean isBadgePointsEarned )
  {
    this.isBadgePointsEarned = isBadgePointsEarned;
  }

  public Badge getBadgePromotion()
  {
    return badgePromotion;
  }

  public void setBadgePromotion( Badge badgePromotion )
  {
    this.badgePromotion = badgePromotion;
  }

  public User getParticipant()
  {
    return participant;
  }

  public void setParticipant( User participant )
  {
    this.participant = participant;
  }

  public Boolean getIsEarned()
  {
    return isEarned;
  }

  public void setIsEarned( Boolean isEarned )
  {
    this.isEarned = isEarned;
  }

  public Date getEarnedDate()
  {
    return earnedDate;
  }

  public void setEarnedDate( Date earnedDate )
  {
    this.earnedDate = earnedDate;
  }

  public Long getSentCount()
  {
    return sentCount;
  }

  public void setSentCount( Long sentCount )
  {
    this.sentCount = sentCount;
  }

  public Long getReceivedCount()
  {
    return receivedCount;
  }

  public void setReceivedCount( Long receivedCount )
  {
    this.receivedCount = receivedCount;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------
  public String toString()
  {
    return ToStringBuilder.reflectionToString( Badge.class );
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( getId() == null ? 0 : getId().hashCode() );
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
    ParticipantBadge other = (ParticipantBadge)obj;
    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }

    return true;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof ParticipantBadge ) )
    {
      throw new ClassCastException( "A ParticipantBadge was expected." );
    }
    ParticipantBadge pBadge = (ParticipantBadge)object;
    return this.getBadgePromotion().getBadgeType().getCode().compareTo( pBadge.getBadgePromotion().getBadgeType().getCode() );
    // return this.badge.getPromotionNames( this.badge.getBadgePromotions() )

  }

  public Boolean getIsEarnedAllBehaviorPoints()
  {
    return isEarnedAllBehaviorPoints;
  }

  public void setIsEarnedAllBehaviorPoints( Boolean isEarnedAllBehaviorPoints )
  {
    this.isEarnedAllBehaviorPoints = isEarnedAllBehaviorPoints;
  }

  public SSIContest getContest()
  {
    return contest;
  }

  public void setContest( SSIContest contest )
  {
    this.contest = contest;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

}

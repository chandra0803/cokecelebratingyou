
package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class ThrowdownStackRankingParticipant implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Participant participant;
  private BigDecimal score;
  private int rank;
  private boolean payoutIssued;
  private List<BadgeInfo> badges = new ArrayList<BadgeInfo>();
  private boolean displayProgress;
  private BaseUnitPosition baseUnitPosition;
  private String baseUnit;
  private ThrowdownPromotion promotion;
  private Round round = null;
  private Long uniqueId;
  private boolean earned;
  private String icon;

  @JsonIgnore
  public Participant getParticipant()
  {
    return participant;
  }

  @JsonIgnore
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  @JsonProperty( "score" )
  public String getScore()
  {
    if ( isCurrentUser() || isDisplayProgress() )
    {
      BigDecimal progress = null != score ? score : new BigDecimal( 0 );
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  @JsonProperty( "scoreForDisplay" )
  public String getScoreForDisplay()
  {
    if ( isCurrentUser() || isDisplayProgress() )
    {
      StringBuilder sb = new StringBuilder();
      BigDecimal progress = null != score ? score : new BigDecimal( 0 );

      if ( baseUnit != null )
      {
        if ( baseUnitPosition != null && baseUnitPosition.getCode().equals( BaseUnitPosition.UNIT_BEFORE ) )
        {
          sb.append( baseUnit + " " + NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() ) );
        }
        else
        {
          sb.append( NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() ) + " " + baseUnit );
        }
      }
      else
      {
        sb.append( NumberFormatUtil.getLocaleBasedBigDecimalFormat( progress, promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() ) );
      }

      return sb.toString();
    }
    else
    {
      return "";
    }
  }

  @JsonIgnore
  public void setScore( BigDecimal score )
  {
    this.score = score;
  }

  @JsonProperty( "rank" )
  public int getRank()
  {
    return rank;
  }

  @JsonIgnore
  public void setRank( int rank )
  {
    this.rank = rank;
  }

  @JsonProperty( "firstName" )
  public String getUserFirstName()
  {
    return participant.getFirstName();
  }

  @JsonProperty( "lastName" )
  public String getUserLastName()
  {
    return participant.getLastName();
  }

  @JsonProperty( "participantId" )
  public Long getUserId()
  {
    return participant.getId();
  }

  @JsonProperty( "avatarUrl" )
  public String getAvatarUrl()
  {
    return participant.getAvatarSmallFullPath( null );
  }

  @JsonProperty( "avatarUrlSmall" )
  public String getAvatarUrlSmall()
  {
    return participant.getAvatarSmallFullPath( null );
  }

  @JsonProperty( "currentUser" )
  public boolean isCurrentUser()
  {
    return UserManager.getUserId().equals( participant.getId() );
  }

  @JsonIgnore
  public List<BadgeInfo> getBadges()
  {
    return badges;
  }

  @JsonIgnore
  public BadgeDetails getBadgeDetails()
  {
    if ( !badges.isEmpty() )
    {
      List<BadgeDetails> badgeDetails = badges.iterator().next().getBadgeDetails();
      if ( !badgeDetails.isEmpty() )
      {
        BadgeDetails details = badgeDetails.iterator().next();
        return details;
      }
    }
    return null;
  }

  @JsonIgnore
  public void setBadges( List<BadgeInfo> badges )
  {
    this.badges = badges;
  }

  @JsonIgnore
  public void setEarned( boolean earned )
  {
    this.earned = earned;
  }

  @JsonProperty( "earned" )
  public boolean isEarned()
  {
    return earned || getBadge() == null;
  }

  @JsonProperty( "badge" )
  public String getBadge()
  {
    BadgeDetails details = getBadgeDetails();
    if ( details != null )
    {
      return details.getImgLarge();
    }
    return null;
  }

  @JsonProperty( "badgeText" )
  public String getBadgeText()
  {
    BadgeDetails details = getBadgeDetails();
    if ( details != null )
    {
      return details.getBadgeName();
    }
    return null;
  }

  @JsonIgnore
  public boolean isPayoutIssued()
  {
    return payoutIssued;
  }

  @JsonIgnore
  public void setPayoutIssued( boolean payoutIssued )
  {
    this.payoutIssued = payoutIssued;
  }

  @JsonIgnore
  public boolean isDisplayProgress()
  {
    return displayProgress;
  }

  @JsonIgnore
  public void setDisplayProgress( boolean displayProgress )
  {
    this.displayProgress = displayProgress;
  }

  @JsonIgnore
  public BaseUnitPosition getBaseUnitPosition()
  {
    return baseUnitPosition;
  }

  @JsonIgnore
  public void setBaseUnitPosition( BaseUnitPosition baseUnitPosition )
  {
    this.baseUnitPosition = baseUnitPosition;
  }

  @JsonProperty( "baseUnit" )
  public String getBaseUnit()
  {
    return baseUnit;
  }

  @JsonIgnore
  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  @JsonProperty( "currentRound" )
  public int getRoundNumber()
  {
    return round.getRoundNumber();
  }

  @JsonProperty( "totalRounds" )
  public int getNumberOfRounds()
  {
    return promotion.getNumberOfRounds();
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonIgnore
  public Round getRound()
  {
    return round;
  }

  @JsonIgnore
  public void setRound( Round round )
  {
    this.round = round;
  }

  @JsonProperty( "id" )
  public Long getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId( Long uniqueId )
  {
    this.uniqueId = uniqueId;
  }

  @JsonProperty( "icon" )
  public String getIcon()
  {
    return icon;
  }

  @JsonIgnore
  public void setIcon( String icon )
  {
    this.icon = icon;
  }

}


package com.biperf.core.domain.gamification;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.BadgeCountType;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.promotion.Promotion;

public class Badge extends Promotion
{

  private static final long serialVersionUID = 1L;
  public static final String BADGE_ACTIVE = "A";
  public static final String BADGE_INACTIVE = "I";
  public static final String BADGE_UNDER_CONSTRUCTION = "C";
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private BadgeType badgeType;
  private Long displayEndDays;
  private Long tileHighlightPeriod;
  private Long notificationMessageId;
  private String status;
  private BadgeCountType badgeCountType;
  private Date displayEndDate;
  private Set<BadgePromotion> badgePromotions = new LinkedHashSet<BadgePromotion>();
  private Set<BadgeRule> badgeRules = new LinkedHashSet<BadgeRule>();
  private Set badgeParticipants = new LinkedHashSet();

  // Badge setup Admin
  private Long allBehaviorPoints;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public BadgeType getBadgeType()
  {
    return badgeType;
  }

  public void setBadgeType( BadgeType badgeType )
  {
    this.badgeType = badgeType;
  }

  public Long getDisplayEndDays()
  {
    return displayEndDays;
  }

  public void setDisplayEndDays( Long displayEndDays )
  {
    this.displayEndDays = displayEndDays;
  }

  public Long getTileHighlightPeriod()
  {
    return tileHighlightPeriod;
  }

  public void setTileHighlightPeriod( Long tileHighlightPeriod )
  {
    this.tileHighlightPeriod = tileHighlightPeriod;
  }

  public Long getNotificationMessageId()
  {
    return notificationMessageId;
  }

  public void setNotificationMessageId( Long notificationMessageId )
  {
    this.notificationMessageId = notificationMessageId;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public BadgeCountType getBadgeCountType()
  {
    return badgeCountType;
  }

  public void setBadgeCountType( BadgeCountType badgeCountType )
  {
    this.badgeCountType = badgeCountType;
  }

  public Date getDisplayEndDate()
  {
    return displayEndDate;
  }

  public void setDisplayEndDate( Date displayEndDate )
  {
    this.displayEndDate = displayEndDate;
  }

  public Set<BadgePromotion> getBadgePromotions()
  {
    return badgePromotions;
  }

  public void setBadgePromotions( Set<BadgePromotion> badgePromotions )
  {
    this.badgePromotions = badgePromotions;
  }

  public Set<BadgeRule> getBadgeRules()
  {
    return badgeRules;
  }

  public List<BadgeRule> getBadgeRulesByLevelType( BadgeLevelType levelType, List<BadgeRule> rules )
  {
    List<BadgeRule> levelBadgeRules = new ArrayList<BadgeRule>();
    for ( BadgeRule rule : rules )
    {
      if ( rule.getLevelType() != null && rule.getLevelType().equals( levelType ) )
      {
        levelBadgeRules.add( rule );
      }
    }
    return levelBadgeRules;
  }

  public void setBadgeRules( Set<BadgeRule> badgeRules )
  {
    this.badgeRules = badgeRules;
  }

  public Set getBadgeParticipants()
  {
    return badgeParticipants;
  }

  public void setBadgeParticipants( Set badgeParticipants )
  {
    this.badgeParticipants = badgeParticipants;
  }

  public void addBadgeRule( BadgeRule badgeRule )
  {
    badgeRule.setBadgePromotion( this );
    this.badgeRules.add( badgeRule );
  }

  public void addBadgePromotion( BadgePromotion badgePromotion )
  {
    badgePromotion.setBadgePromotion( this );
    this.badgePromotions.add( badgePromotion );
  }

  /**
   * @return comma separated promotion names in String
   */
  public String getDisplayPromoNames()
  {
    return getPromotionNames( getBadgePromotions() );
  }

  /*
   * Method to return the comma separated promotion names for the badge
   * @param badgePromotions
   * @return String
   */
  public String getPromotionNames( Set badgePromotions )
  {
    String promotioNames = "";
    Iterator setItr = null;
    setItr = badgePromotions.iterator();
    int count = 0;

    while ( setItr.hasNext() )
    {
      BadgePromotion badgePromotion = (BadgePromotion)setItr.next();
      if ( count == 0 || count == badgePromotions.size() )
      {
        promotioNames = promotioNames + badgePromotion.getEligiblePromotion().getName();
      }
      else
      {
        promotioNames = promotioNames + ",\n " + badgePromotion.getEligiblePromotion().getName();
      }
      count++;
    }
    return promotioNames;
  }

  public String getPromotionNamesNoLine( Set badgePromotions )
  {
    String promotioNames = "";
    Iterator setItr = null;
    setItr = badgePromotions.iterator();
    int count = 0;

    while ( setItr.hasNext() )
    {
      BadgePromotion badgePromotion = (BadgePromotion)setItr.next();
      if ( count == 0 || count == badgePromotions.size() )
      {
        promotioNames = promotioNames + badgePromotion.getEligiblePromotion().getName();
      }
      else
      {
        promotioNames = promotioNames + ", " + badgePromotion.getEligiblePromotion().getName();
      }
      count++;
    }
    return promotioNames;
  }

  /*
   * Method to return the comma separated promotion Ids for the badge
   * @param badgePromotions
   * @return String
   */
  public String getPromotionIds( Set badgePromotions )
  {
    String promotionIds = "";
    Iterator setItr = null;
    setItr = badgePromotions.iterator();
    int count = 0;

    while ( setItr.hasNext() )
    {
      BadgePromotion badgePromotion = (BadgePromotion)setItr.next();
      if ( count == 0 || count == badgePromotions.size() )
      {
        promotionIds = promotionIds + badgePromotion.getEligiblePromotion().getId();
      }
      else
      {
        promotionIds = promotionIds + "," + badgePromotion.getEligiblePromotion().getId();
      }
      count++;
    }
    return promotionIds;
  }

  public String[] getPromotionIdsAsArray( Set badgePromotions )
  {
    if ( badgePromotions != null && !badgePromotions.isEmpty() )
    {
      String[] promotionIds = new String[badgePromotions.size()];
      Iterator setItr = null;
      setItr = badgePromotions.iterator();
      int count = 0;

      while ( setItr.hasNext() )
      {
        BadgePromotion badgePromotion = (BadgePromotion)setItr.next();
        promotionIds[count] = badgePromotion.getEligiblePromotion().getId().toString();
        count++;
      }
      return promotionIds;
    }
    return null;
  }

  public Long getAllBehaviorPoints()
  {
    return allBehaviorPoints;
  }

  public void setAllBehaviorPoints( Long allBehaviorPoints )
  {
    this.allBehaviorPoints = allBehaviorPoints;
  }

  @Override
  public boolean hasParent()
  {
    return false;
  }

  @Override
  public boolean isClaimFormUsed()
  {
    return false;
  }

}

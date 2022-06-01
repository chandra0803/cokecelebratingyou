
package com.biperf.core.domain.goalquest;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;

/**
 * 
 * Javadoc for GoalBadgeRule.
 * 
 * @since Jul 2, 2013
 * @version 1.0
 */
public class GoalBadgeRule
{
  private BadgeRule badgeRule;

  private String promotionName;
  private List<String> levelNames = new ArrayList<String>();
  List<BadgeLibrary> badgeLibraryList = new ArrayList<BadgeLibrary>();
  private String badgeName;
  private String badgeDescription;
  private boolean isGoalQuest;
  private boolean isPartners;

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public String getBadgeDescription()
  {
    return badgeDescription;
  }

  public void setBadgeDescription( String badgeDescription )
  {
    this.badgeDescription = badgeDescription;
  }

  public List<BadgeLibrary> getBadgeLibraryList()
  {
    return badgeLibraryList;
  }

  public void setBadgeLibraryList( List<BadgeLibrary> badgeLibraryList )
  {
    this.badgeLibraryList = badgeLibraryList;
  }

  public List<String> getLevelNames()
  {
    return levelNames;
  }

  public void setLevelNames( List<String> levelNames )
  {
    this.levelNames = levelNames;
  }

  public boolean isPartners()
  {
    return isPartners;
  }

  public void setPartners( boolean isPartners )
  {
    this.isPartners = isPartners;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public boolean isGoalQuest()
  {
    return isGoalQuest;
  }

  public void setGoalQuest( boolean isGoalQuest )
  {
    this.isGoalQuest = isGoalQuest;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }
}

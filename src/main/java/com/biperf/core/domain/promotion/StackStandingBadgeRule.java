
package com.biperf.core.domain.promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;

public class StackStandingBadgeRule
{

  private BadgeRule badgeRule;

  private String promotionName;
  private Map<String, List<String>> nodePayouts = new HashMap<String, List<String>>();
  List<BadgeLibrary> badgeLibraryList = new ArrayList<BadgeLibrary>();
  private String badgeName;
  private String badgeDescription;
  private boolean isThrowDown;
  private boolean isStackStandingPayouts;
  // private Set<StackStandingPayoutGroup> standingPayoutGroups = new
  // HashSet<StackStandingPayoutGroup>();

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

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }

  public Map<String, List<String>> getNodePayouts()
  {
    return nodePayouts;
  }

  public void setNodePayouts( Map<String, List<String>> nodePayouts )
  {
    this.nodePayouts = nodePayouts;
  }

  public boolean isThrowDown()
  {
    return isThrowDown;
  }

  public void setThrowDown( boolean isThrowDown )
  {
    this.isThrowDown = isThrowDown;
  }

  public boolean isStackStandingPayouts()
  {
    return isStackStandingPayouts;
  }

  public void setStackStandingPayouts( boolean isStackStandingPayouts )
  {
    this.isStackStandingPayouts = isStackStandingPayouts;
  }

  /*
   * public Set<StackStandingPayoutGroup> getStandingPayoutGroups() { return standingPayoutGroups; }
   * public void setStandingPayoutGroups(Set<StackStandingPayoutGroup> standingPayoutGroups) {
   * this.standingPayoutGroups = standingPayoutGroups; }
   */

}


package com.biperf.core.domain.gamification;

import java.util.List;

public class BadgeLevelsView
{

  List<BadgePromotionLevels> levels;
  List<BadgeLibrary> badgeLibrary;

  public List<BadgePromotionLevels> getLevels()
  {
    return levels;
  }

  public void setLevels( List<BadgePromotionLevels> levels )
  {
    this.levels = levels;
  }

  public List<BadgeLibrary> getBadgeLibrary()
  {
    return badgeLibrary;
  }

  public void setBadgeLibrary( List<BadgeLibrary> badgeLibrary )
  {
    this.badgeLibrary = badgeLibrary;
  }

}

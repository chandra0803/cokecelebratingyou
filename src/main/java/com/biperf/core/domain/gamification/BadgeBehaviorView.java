
package com.biperf.core.domain.gamification;

import java.util.List;

public class BadgeBehaviorView
{

  List<BadgeBehaviorPromotion> behaviors;
  List<BadgeLibrary> badgeLibrary;

  public List<BadgeBehaviorPromotion> getBehaviors()
  {
    return behaviors;
  }

  public void setBehaviors( List<BadgeBehaviorPromotion> behaviors )
  {
    this.behaviors = behaviors;
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

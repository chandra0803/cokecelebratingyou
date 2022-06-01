
package com.biperf.core.ui.gamification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.gamification.BadgeLibrary;

@SuppressWarnings( "serial" )
public class BadgePromotionValueBean implements Serializable, Cloneable
{
  private String showFileLoadNoPromoDiv;
  List<BadgeLibrary> badgeLibraryList = new ArrayList<BadgeLibrary>();

  public List<BadgeLibrary> getBadgeLibraryList()
  {
    return badgeLibraryList;
  }

  public void setBadgeLibraryList( List<BadgeLibrary> badgeLibraryList )
  {
    this.badgeLibraryList = badgeLibraryList;
  }

  public void setShowFileLoadNoPromoDiv( String showFileLoadNoPromoDiv )
  {
    this.showFileLoadNoPromoDiv = showFileLoadNoPromoDiv;
  }

  public String getShowFileLoadNoPromoDiv()
  {
    return showFileLoadNoPromoDiv;
  }

}

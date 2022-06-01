
package com.biperf.core.value;

import com.biperf.core.domain.promotion.HomePageItem;

public class WhatsNewBean implements java.io.Serializable
{
  private HomePageItem homePageItem;
  private String browseAwardsLink;

  private WhatsNewBean()
  {
  }

  public WhatsNewBean( HomePageItem homePageItem, String browseAwardsLink )
  {
    this.homePageItem = homePageItem;
    this.browseAwardsLink = browseAwardsLink;
  }

  public String getBrowseAwardsLink()
  {
    return browseAwardsLink;
  }

  public void setBrowseAwardsLink( String browseAwardsLink )
  {
    this.browseAwardsLink = browseAwardsLink;
  }

  public HomePageItem getHomePageItem()
  {
    return homePageItem;
  }

  public void setHomePageItem( HomePageItem homePageItem )
  {
    this.homePageItem = homePageItem;
  }
}


package com.biperf.core.domain.gamification;

import java.util.List;

public class BadgeTableBean
{
  private String rangeAmountMin;
  private String rangeAmountMax;
  // private String pointBadgLibraryId;
  private String badgeName;
  private String badgeDescription;
  private long indexNumber;
  List<BadgeLibrary> badgeLibraryList;
  private String promotionName;
  private String levelName;

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getRangeAmountMin()
  {
    return rangeAmountMin;
  }

  public void setRangeAmountMin( String rangeAmountMin )
  {
    this.rangeAmountMin = rangeAmountMin;
  }

  public String getRangeAmountMax()
  {
    return rangeAmountMax;
  }

  public void setRangeAmountMax( String rangeAmountMax )
  {
    this.rangeAmountMax = rangeAmountMax;
  }

  public long getIndexNumber()
  {
    return indexNumber;
  }

  public void setIndexNumber( long indexNumber )
  {
    this.indexNumber = indexNumber;
  }

  public List<BadgeLibrary> getBadgeLibraryList()
  {
    return badgeLibraryList;
  }

  public void setBadgeLibraryList( List<BadgeLibrary> badgeLibraryList )
  {
    this.badgeLibraryList = badgeLibraryList;
  }

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

}

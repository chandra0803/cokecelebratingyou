
package com.biperf.core.value;

/**
 * 
 * EngagementBehaviorValueBean.
 * 
 * @author kandhi
 * @since Jun 25, 2014
 * @version 1.0
 */
public class EngagementBehaviorValueBean
{
  private String behavior;
  private int sentCnt;
  private int receivedCnt;
  private String badgeImageUrl;

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public int getSentCnt()
  {
    return sentCnt;
  }

  public void setSentCnt( int sentCnt )
  {
    this.sentCnt = sentCnt;
  }

  public int getReceivedCnt()
  {
    return receivedCnt;
  }

  public void setReceivedCnt( int receivedCnt )
  {
    this.receivedCnt = receivedCnt;
  }

  public String getBadgeImageUrl()
  {
    return badgeImageUrl;
  }

  public void setBadgeImageUrl( String badgeImageUrl )
  {
    this.badgeImageUrl = badgeImageUrl;
  }

}

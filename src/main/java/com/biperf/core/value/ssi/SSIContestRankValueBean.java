
package com.biperf.core.value.ssi;

/**
 * SSIContestRankValueBean.
 * 
 * @author dudam
 * @since Feb 16, 2015
 * @version 1.0
 */
public class SSIContestRankValueBean
{

  private Long id;
  private Long rank;
  private Long payoutAmount;
  private String payoutDescription;
  private Long badgeId;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getRank()
  {
    return rank;
  }

  public void setRank( Long rank )
  {
    this.rank = rank;
  }

  public Long getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Long payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

}

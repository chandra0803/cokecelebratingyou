
package com.biperf.core.value.ssi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * SSIContestStackRankValueBean.
 * 
 * @author dudam
 * @since Feb 18, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestStackRankPayoutValueBean
{

  private Long rank;
  private Long payout;
  private String payoutDescription;
  private BadgeValueBean badge;

  public SSIContestStackRankPayoutValueBean()
  {

  }

  public SSIContestStackRankPayoutValueBean( Long rank, Long payout, String payoutDescription, Long badgeId, String badgeName, String badgeUrl )
  {
    this.rank = rank;
    this.payout = payout;
    this.payoutDescription = payoutDescription;
    this.badge = new BadgeValueBean( badgeId, badgeName, badgeUrl );
  }

  public Long getRank()
  {
    return rank;
  }

  public void setRank( Long rank )
  {
    this.rank = rank;
  }

  public Long getPayout()
  {
    return payout;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public BadgeValueBean getBadge()
  {
    return badge;
  }

  public void setBadge( BadgeValueBean badge )
  {
    this.badge = badge;
  }

  public class BadgeValueBean
  {
    private Long id;
    private String type;
    private String name;
    private String img;

    public BadgeValueBean()
    {

    }

    public BadgeValueBean( Long badgeId, String badgeName, String badgeUrl )
    {
      this.id = badgeId;
      this.name = badgeName;
      this.img = badgeUrl;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getType()
    {
      return type;
    }

    public void setType( String type )
    {
      this.type = type;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public String getImg()
    {
      return img;
    }

    public void setImg( String img )
    {
      this.img = img;
    }

  }

}

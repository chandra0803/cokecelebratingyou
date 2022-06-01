/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

public class PurlNotMigratedPaxData
{
  private Long purlContributorId;
  private Long userId;
  private String avatarUrl;

  public Long getPurlContributorId()
  {
    return purlContributorId;
  }

  public void setPurlContributorId( Long purlContributorId )
  {
    this.purlContributorId = purlContributorId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

}

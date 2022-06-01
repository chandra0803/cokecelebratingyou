/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

public class PurlContributorAvatarData
{
  private Long userId;
  private String avatarUrl;

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

/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

public class MigratedAvatarData
{
  Long userId;
  String avatarOriginalUrl;
  String avatarSmallUrl;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getAvatarOriginalUrl()
  {
    return avatarOriginalUrl;
  }

  public void setAvatarOriginalUrl( String avatarOriginalUrl )
  {
    this.avatarOriginalUrl = avatarOriginalUrl;
  }

  public String getAvatarSmallUrl()
  {
    return avatarSmallUrl;
  }

  public void setAvatarSmallUrl( String avatarSmallUrl )
  {
    this.avatarSmallUrl = avatarSmallUrl;
  }

}


package com.biperf.core.value.profile.v1.uploadavatar;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class AvatarView implements Serializable
{
  private static final long serialVersionUID = 1L;

  private boolean success;

  private String avatarUrl;

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess( boolean success )
  {
    this.success = success;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  @Override
  public String toString()
  {
    return "AvatarView [avatarUrl=" + avatarUrl + "]";
  }

}

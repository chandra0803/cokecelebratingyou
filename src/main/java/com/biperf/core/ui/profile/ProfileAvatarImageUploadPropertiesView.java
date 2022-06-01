
package com.biperf.core.ui.profile;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileAvatarImageUploadPropertiesView
{
  @JsonProperty( "isSuccess" )
  private boolean isSuccess;
  private String avatarUrl;

  public boolean getIsSuccess()
  {
    return isSuccess;
  }

  public void setIsSuccess( boolean isSuccess )
  {
    this.isSuccess = isSuccess;
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

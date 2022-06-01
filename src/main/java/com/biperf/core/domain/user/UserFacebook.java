
package com.biperf.core.domain.user;

import com.biperf.core.domain.BaseDomain;

public class UserFacebook extends BaseDomain
{
  private String userId;
  private String accessToken;
  private String accessTokenEncrypted;

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getAccessToken()
  {
    return accessTokenEncrypted;
  }

  public void setAccessToken( String sessionKey )
  {
    this.accessToken = sessionKey;
    this.accessTokenEncrypted = accessToken;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof UserFacebook ) )
    {
      return false;
    }
    if ( getClass() != object.getClass() )
    {
      return false;
    }
    UserFacebook other = (UserFacebook)object;
    if ( userId == null )
    {
      if ( other.userId != null )
      {
        return false;
      }
    }
    else if ( !userId.equals( other.userId ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( userId == null ? 0 : userId.hashCode() );
    return result;
  }

}

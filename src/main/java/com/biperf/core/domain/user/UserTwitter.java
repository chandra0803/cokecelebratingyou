
package com.biperf.core.domain.user;

import com.biperf.core.domain.BaseDomain;

public class UserTwitter extends BaseDomain
{
  private String requestToken;
  private String requestTokenSecret;
  private String accessToken;
  private String accessTokenSecret;

  public String getAccessToken()
  {
    return accessToken;
  }

  public void setAccessToken( String accessToken )
  {
    this.accessToken = accessToken;
  }

  public String getAccessTokenSecret()
  {
    return accessTokenSecret;
  }

  public void setAccessTokenSecret( String accessTokenSecret )
  {
    this.accessTokenSecret = accessTokenSecret;
  }

  public String getRequestToken()
  {
    return requestToken;
  }

  public void setRequestToken( String requestToken )
  {
    this.requestToken = requestToken;
  }

  public String getRequestTokenSecret()
  {
    return requestTokenSecret;
  }

  public void setRequestTokenSecret( String requestTokenSecret )
  {
    this.requestTokenSecret = requestTokenSecret;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof UserTwitter ) )
    {
      return false;
    }
    if ( getClass() != object.getClass() )
    {
      return false;
    }
    UserTwitter other = (UserTwitter)object;
    if ( requestToken == null )
    {
      if ( other.requestToken != null )
      {
        return false;
      }
    }
    else if ( !requestToken.equals( other.requestToken ) )
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
    result = prime * result + ( requestToken == null ? 0 : requestToken.hashCode() );
    return result;
  }

}

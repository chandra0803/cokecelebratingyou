
package com.biperf.core.domain.user;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.strategy.usertoken.UserTokenType;

public abstract class UserToken extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private User user;
  private UserTokenStatusType status;
  private String token;
  private boolean allowRegenerate;
  private Date expirationDate;

  // used for the communications - but not persisted
  private transient String unencryptedTokenValue;

  public UserToken()
  {
  }

  public UserToken( User user, UserTokenStatusType status, String token, boolean allowRegenerate, Date expirationDate )
  {
    super();
    this.user = user;
    this.status = status;
    this.token = token;
    this.allowRegenerate = allowRegenerate;
    this.expirationDate = expirationDate;
  }
  
  public abstract UserTokenType getUserTokenType();

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public UserTokenStatusType getStatus()
  {
    return status;
  }

  public void setStatus( UserTokenStatusType status )
  {
    this.status = status;
  }

  public String getToken()
  {
    return token;
  }

  public void setToken( String token )
  {
    this.token = token;
  }

  public boolean isAllowRegenerate()
  {
    return allowRegenerate;
  }

  public void setAllowRegenerate( boolean allowRegenerate )
  {
    this.allowRegenerate = allowRegenerate;
  }

  public Date getExpirationDate()
  {
    return expirationDate;
  }

  public void setExpirationDate( Date expirationDate )
  {
    this.expirationDate = expirationDate;
  }

  public String getUnencryptedTokenValue()
  {
    return unencryptedTokenValue;
  }

  public void setUnencryptedTokenValue( String unencryptedTokenValue )
  {
    this.unencryptedTokenValue = unencryptedTokenValue;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    UserToken other = (UserToken)obj;
    if ( allowRegenerate != other.allowRegenerate )
    {
      return false;
    }
    if ( expirationDate == null )
    {
      if ( other.expirationDate != null )
      {
        return false;
      }
    }
    else if ( !expirationDate.equals( other.expirationDate ) )
    {
      return false;
    }
    if ( status == null )
    {
      if ( other.status != null )
      {
        return false;
      }
    }
    else if ( !status.equals( other.status ) )
    {
      return false;
    }
    if ( token == null )
    {
      if ( other.token != null )
      {
        return false;
      }
    }
    else if ( !token.equals( other.token ) )
    {
      return false;
    }
    if ( user == null )
    {
      if ( other.user != null )
      {
        return false;
      }
    }
    else if ( !user.equals( other.user ) )
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
    result = prime * result + ( allowRegenerate ? 1231 : 1237 );
    result = prime * result + ( ( expirationDate == null ) ? 0 : expirationDate.hashCode() );
    result = prime * result + ( ( status == null ) ? 0 : status.hashCode() );
    result = prime * result + ( ( token == null ) ? 0 : token.hashCode() );
    result = prime * result + ( ( user == null ) ? 0 : user.hashCode() );
    return result;
  }

  @Override
  public String toString()
  {
    return "UserToken [user=" + user + ", status=" + status + ", token=" + token + ", allowRegenerate=" + allowRegenerate + ", expirationDate=" + expirationDate + "]";
  }

}
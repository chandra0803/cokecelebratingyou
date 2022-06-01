
package com.biperf.core.domain.user;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

public class UserCookiesAcceptance extends BaseDomain
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -6738993793092578897L;
  private Long userId;
  private Date acceptanceDate;
  private Long policyVersion;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Date getAcceptanceDate()
  {
    return acceptanceDate;
  }

  public void setAcceptanceDate( Date acceptanceDate )
  {
    this.acceptanceDate = acceptanceDate;
  }

  public Long getPolicyVersion()
  {
    return policyVersion;
  }

  public void setPolicyVersion( Long policyVersion )
  {
    this.policyVersion = policyVersion;
  }

  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof UserCookiesAcceptance ) )
    {
      return false;
    }
    if ( getClass() != object.getClass() )
    {
      return false;
    }
    UserCookiesAcceptance other = (UserCookiesAcceptance)object;
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

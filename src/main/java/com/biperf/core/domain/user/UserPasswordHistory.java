
package com.biperf.core.domain.user;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;

public class UserPasswordHistory extends BaseDomain implements Cloneable
{
  private User user;
  private Date passwordSetDate;
  private String password;
  private int sequenceNumber;

  public UserPasswordHistory()
  {

  }

  public UserPasswordHistory( User user, Date passwordSetDate, String password )
  {
    this.user = user;
    this.passwordSetDate = passwordSetDate;
    this.password = password;
  }

  public User getUser()
  {
    return user;
  }

  public Date getPasswordSetDate()
  {
    return passwordSetDate;
  }

  public String getPassword()
  {
    return password;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public void setPasswordSetDate( Date passwordSetDate )
  {
    this.passwordSetDate = passwordSetDate;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof UserPasswordHistory ) )
    {
      return false;
    }

    final UserPasswordHistory other = (UserPasswordHistory)object;

    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 0;
    result = prime * result + ( this.getId() != null ? this.getId().hashCode() : 0 );
    return result;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

}


package com.biperf.core.value;

import java.io.Serializable;

public class RAValueBean implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Long userId;
  private String firstName;
  private String lastName;
  private String emailAddress;

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public int hashCode()
  {
    return getUserId() != null ? getUserId().hashCode() : 0;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof UserValueBean ) )
    {
      return false;
    }

    final RAValueBean valueBean = (RAValueBean)object;

    if ( getUserId() != null ? !getUserId().equals( valueBean.getUserId() ) : valueBean.getUserId() != null )
    {
      return false;
    }

    return true;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "RAValueBean [" );
    buf.append( "{userId=" + this.getUserId() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

}

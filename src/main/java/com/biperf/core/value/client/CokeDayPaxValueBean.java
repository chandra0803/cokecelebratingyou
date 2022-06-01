
package com.biperf.core.value.client;

import java.io.Serializable;
import java.util.Date;

/**
 * ParticipantEmployerValueBean.
 * 
 * @author dudam
 * @since Feb 12, 2018
 * @version 1.0
 */
public class CokeDayPaxValueBean implements Serializable
{
  private Long userId;
  private String FirstName;
  private String LastName;
  private String hireDate;

  public CokeDayPaxValueBean()
  {
    // empty constructor
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  
  public String getFirstName() 
  {
	return FirstName;
  }

  public void setFirstName(String firstName)
  {
	FirstName = firstName;
  }

  public String getLastName() 
  {
	return LastName;
  }

  public void setLastName(String lastName)
  {
	LastName = lastName;
  }

public String getHireDate()
  {
    return hireDate;
  }

  public void setHireDate( String hireDate )
  {
    this.hireDate = hireDate;
  }

}
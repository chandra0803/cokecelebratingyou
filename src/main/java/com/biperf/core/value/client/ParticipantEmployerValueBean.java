
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
public class ParticipantEmployerValueBean implements Serializable
{
  private Long userId;
  private Date hireDate;

  public ParticipantEmployerValueBean()
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

  public Date getHireDate()
  {
    return hireDate;
  }

  public void setHireDate( Date hireDate )
  {
    this.hireDate = hireDate;
  }

}
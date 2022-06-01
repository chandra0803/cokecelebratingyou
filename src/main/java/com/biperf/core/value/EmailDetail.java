/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

public class EmailDetail
{
  private Long userId;
  private String emailAddress;
  private String emailType;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  public String getEmailType()
  {
    return emailType;
  }

  public void setEmailType( String emailType )
  {
    this.emailType = emailType;
  }

}

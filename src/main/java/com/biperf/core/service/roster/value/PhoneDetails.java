/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.roster.value;

public class PhoneDetails
{
  private Long userId;
  private String phoneNumber;
  private String phoneExtn;
  private String type;
  private String code;

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneExtn()
  {
    return phoneExtn;
  }

  public void setPhoneExtn( String phoneExtn )
  {
    this.phoneExtn = phoneExtn;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

}

/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

public class PhoneDetail
{
  private Long userId;
  private String phoneNumber;
  private String phoneExtn;
  private String phoneType;
  private String countryPhoneCode;

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

  public String getPhoneType()
  {
    return phoneType;
  }

  public void setPhoneType( String phoneType )
  {
    this.phoneType = phoneType;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

}

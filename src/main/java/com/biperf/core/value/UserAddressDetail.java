/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

public class UserAddressDetail
{
  private Long userAddressId;
  private String addressType;

  public Long getUserAddressId()
  {
    return userAddressId;
  }

  public void setUserAddressId( Long userAddressId )
  {
    this.userAddressId = userAddressId;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public void setAddressType( String addressType )
  {
    this.addressType = addressType;
  }

}

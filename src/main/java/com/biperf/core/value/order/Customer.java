
package com.biperf.core.value.order;

public class Customer implements java.io.Serializable
{
  private String customerName;
  private String customerAddress1;
  private String customerAddress2;
  private String customerAddress3;
  private String customerCity;
  private String customerState;
  private String customerZipCode;
  private String customerCountry;
  private String customerEmail;
  private String customerPhone;

  public String getCustomerAddress1()
  {
    return customerAddress1;
  }

  public String getCustomerAddress2()
  {
    if ( customerAddress2 != null )
    {
      customerAddress2 = customerAddress2.trim();
    }
    return customerAddress2;
  }

  public String getCustomerAddress3()
  {
    if ( customerAddress3 != null )
    {
      customerAddress3 = customerAddress3.trim();
    }
    return customerAddress3;
  }

  public String getCustomerCity()
  {
    return customerCity;
  }

  public String getCustomerCountry()
  {
    return customerCountry;
  }

  public String getCustomerEmail()
  {
    return customerEmail;
  }

  public String getCustomerName()
  {
    return customerName;
  }

  public String getCustomerPhone()
  {
    return customerPhone;
  }

  public String getCustomerState()
  {
    return customerState;
  }

  public String getCustomerZipCode()
  {
    return customerZipCode;
  }

  public void setCustomerAddress1( String string )
  {
    customerAddress1 = string;
  }

  public void setCustomerAddress2( String string )
  {
    customerAddress2 = string;
  }

  public void setCustomerAddress3( String string )
  {
    customerAddress3 = string;
  }

  public void setCustomerCity( String string )
  {
    customerCity = string;
  }

  public void setCustomerCountry( String string )
  {
    customerCountry = string;
  }

  public void setCustomerEmail( String string )
  {
    customerEmail = string;
  }

  public void setCustomerName( String string )
  {
    customerName = string;
  }

  public void setCustomerPhone( String string )
  {
    customerPhone = string;
  }

  public void setCustomerState( String string )
  {
    customerState = string;
  }

  public void setCustomerZipCode( String string )
  {
    customerZipCode = string;
  }
}

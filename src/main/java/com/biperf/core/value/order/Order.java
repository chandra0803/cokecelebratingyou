
package com.biperf.core.value.order;

import com.biperf.util.StringUtils;

public class Order implements java.io.Serializable
{
  private String orderId;
  private String orderBusinessUnit;
  private String orderDate;

  public String getOrderBusinessUnit()
  {
    return orderBusinessUnit;
  }

  public String getOrderDate()
  {
    return orderDate;
  }

  public String getOrderId()
  {
    return orderId;
  }

  public void setOrderBusinessUnit( String string )
  {
    orderBusinessUnit = string;
  }

  public void setOrderDate( String date )
  {
    orderDate = date;
  }

  public void setOrderId( String string )
  {
    orderId = string;
  }

  public String toString()
  {
    return StringUtils.generateToString( this );
  }
}

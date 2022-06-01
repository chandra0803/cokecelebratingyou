
package com.biperf.core.value.order;

import java.util.ArrayList;
import java.util.List;

public class OrderLineItems implements java.io.Serializable
{
  private List lineItemList = new ArrayList();

  public OrderLineItems()
  {
  }

  public void addLineItem( LineItem lineItem )
  {
    lineItemList.add( lineItem );
  }

  public List getLineItemList()
  {
    return lineItemList;
  }
}

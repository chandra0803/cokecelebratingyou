
package com.biperf.core.value.order;

public class OrderInformation implements java.io.Serializable
{
  private Customer customer = null;
  private Order order = null;
  private OrderLineItems orderLineItems = null;

  public Customer getCustomer()
  {
    return customer;
  }

  public Order getOrder()
  {
    return order;
  }

  public OrderLineItems getOrderLineItems()
  {
    return orderLineItems;
  }

  public void setCustomer( Customer customer )
  {
    this.customer = customer;
  }

  public void setOrder( Order order )
  {
    this.order = order;
  }

  public void setOrderLineItems( OrderLineItems holder )
  {
    orderLineItems = holder;
  }
}

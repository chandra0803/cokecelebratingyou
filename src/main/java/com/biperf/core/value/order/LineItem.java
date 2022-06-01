
package com.biperf.core.value.order;

public class LineItem implements java.io.Serializable
{
  private int quantity;
  private String productId;
  private String description;
  private String shippingStatus;
  private String estimatedShippingDate;
  private String shippedDate;
  private String trackingNumber;
  private String carrierUrl;
  private String carrier;

  public String getShippedDate()
  {
    return shippedDate;
  }

  public void setShippedDate( String shippedDate )
  {
    this.shippedDate = shippedDate;
  }

  public String getDescription()
  {
    return description;
  }

  public String getProductId()
  {
    return productId;
  }

  public int getQuantity()
  {
    return quantity;
  }

  public String getShippingStatus()
  {
    return shippingStatus;
  }

  public void setDescription( String string )
  {
    description = string;
  }

  public void setProductId( String string )
  {
    productId = string;
  }

  public void setQuantity( int quant )
  {
    quantity = quant;
  }

  public void setShippingStatus( String string )
  {
    shippingStatus = string;
  }

  public String getEstimatedShippingDate()
  {
    return estimatedShippingDate;
  }

  public void setEstimatedShippingDate( String estimatedShippingDate )
  {
    this.estimatedShippingDate = estimatedShippingDate;
  }

  public String getCarrier()
  {
    return carrier;
  }

  public String getTrackingNumber()
  {
    return trackingNumber;
  }

  public String getCarrierUrl()
  {
    return carrierUrl;
  }

  public void setCarrier( String string )
  {
    carrier = string;
  }

  public void setTrackingNumber( String string )
  {
    trackingNumber = string;
  }

  public void setCarrierUrl( String string )
  {
    carrierUrl = string;
  }
}


package com.biperf.core.service.awardbanq.impl;

import java.util.ArrayList;
import java.util.List;

public class MerchLevelValueObject
{
  private int maxValue;
  private int minValue;
  private String name;
  private boolean productsReturned;
  private List<MerchLevelProductValueObject> merchLevelProduct = new ArrayList<MerchLevelProductValueObject>();

  public MerchLevelValueObject()
  {

  }

  public int getMaxValue()
  {
    return maxValue;
  }

  public void setMaxValue( int maxValue )
  {
    this.maxValue = maxValue;
  }

  public int getMinValue()
  {
    return minValue;
  }

  public void setMinValue( int minValue )
  {
    this.minValue = minValue;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isProductsReturned()
  {
    return productsReturned;
  }

  public void setProductsReturned( boolean productsReturned )
  {
    this.productsReturned = productsReturned;
  }

  public List<MerchLevelProductValueObject> getMerchLevelProduct()
  {
    return merchLevelProduct;
  }

  public void setMerchLevelProduct( List<MerchLevelProductValueObject> merchLevelProduct )
  {
    this.merchLevelProduct = merchLevelProduct;
  }

}

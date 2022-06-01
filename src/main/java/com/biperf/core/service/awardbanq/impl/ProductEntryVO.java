
package com.biperf.core.service.awardbanq.impl;

public class ProductEntryVO
{
  private String key;
  private ProductValueVO value;

  public ProductEntryVO()
  {

  }

  public ProductValueVO getValue()
  {
    return value;
  }

  public void setValue( ProductValueVO value )
  {
    this.value = value;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

}

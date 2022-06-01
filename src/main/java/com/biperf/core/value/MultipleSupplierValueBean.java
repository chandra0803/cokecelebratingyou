
package com.biperf.core.value;

import java.io.Serializable;

public class MultipleSupplierValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String imageName;
  private String title;
  private String supplierDesc;
  private String buttonDesc;
  private String supplierType;
  private String shoppingUrl;

  public MultipleSupplierValueBean()
  {

  }

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getTitle()
  {
    return title;
  }

  public void setSupplierDesc( String supplierDesc )
  {
    this.supplierDesc = supplierDesc;
  }

  public String getSupplierDesc()
  {
    return supplierDesc;
  }

  public void setButtonDesc( String buttonDesc )
  {
    this.buttonDesc = buttonDesc;
  }

  public String getButtonDesc()
  {
    return buttonDesc;
  }

  public void setSupplierType( String supplierType )
  {
    this.supplierType = supplierType;
  }

  public String getSupplierType()
  {
    return supplierType;
  }

  public void setShoppingUrl( String shoppingUrl )
  {
    this.shoppingUrl = shoppingUrl;
  }

  public String getShoppingUrl()
  {
    return shoppingUrl;
  }

}

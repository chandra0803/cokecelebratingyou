/**
 * 
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author poddutur
 *
 */
public class ProductClaimPreviewBean implements Serializable
{
  private Long productid;
  private String name;
  private String category;
  private String subcategory;
  private Long quantity;
  private ArrayList<ProductClaimCharacteristicsPreviewBean> characteristics = new ArrayList<ProductClaimCharacteristicsPreviewBean>();

  public Long getProductid()
  {
    return productid;
  }

  public void setProductid( Long productid )
  {
    this.productid = productid;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory( String category )
  {
    this.category = category;
  }

  public String getSubcategory()
  {
    return subcategory;
  }

  public void setSubcategory( String subcategory )
  {
    this.subcategory = subcategory;
  }

  public Long getQuantity()
  {
    return quantity;
  }

  public void setQuantity( Long quantity )
  {
    this.quantity = quantity;
  }

  public int getCharacteristicsCount()
  {
    if ( characteristics == null || characteristics.isEmpty() )
    {
      return 0;
    }
    return characteristics.size();
  }

  public ProductClaimCharacteristicsPreviewBean getCharacteristics( int index )
  {
    while ( index >= characteristics.size() )
    {
      characteristics.add( new ProductClaimCharacteristicsPreviewBean() );
    }
    return (ProductClaimCharacteristicsPreviewBean)characteristics.get( index );
  }

  public ArrayList<ProductClaimCharacteristicsPreviewBean> getCharacteristicsValues()
  {
    return characteristics;
  }

  public void setCharacteristicsValues( ProductClaimCharacteristicsPreviewBean characteristic )
  {
    this.characteristics.add( characteristic );
  }

}

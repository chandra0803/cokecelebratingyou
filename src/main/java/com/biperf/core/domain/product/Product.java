/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/product/Product.java,v $
 */

package com.biperf.core.domain.product;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

/**
 * Product.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Sathish</td>
 * <td>June 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class Product extends BaseDomain
{
  private String name;

  private String description;

  private String code;

  private ProductCategory productCategory;

  private Set productCharacteristics = new LinkedHashSet();

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public ProductCategory getProductCategory()
  {
    return productCategory;
  }

  public void setProductCategory( ProductCategory productCategory )
  {
    this.productCategory = productCategory;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   *
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Product ) )
    {
      return false;
    }

    final Product product = (Product)object;

    if ( product.getName() != null && !product.getName().equals( this.getName() ) )
    {
      return false;
    }
    if ( product.getProductCategory() != null && !product.getProductCategory().equals( this.getProductCategory() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   *
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getName() != null ? getName().hashCode() : 0;
    result = 29 * result + ( getProductCategory() != null ? getProductCategory().hashCode() : 0 );
    return result;
  }

  /**
   * Builds a String representation of this class. Overridden from
   *
   * @see Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "Product [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{name=" + this.getName() + "}, " );
    buf.append( "{description=" + this.getDescription() + "}, " );
    buf.append( "{code=" + this.getCode() + "}, " );
    buf.append( "]" );

    return buf.toString();
  }

  public Set getProductCharacteristicTypes()
  {
    Set productCharTypes = new LinkedHashSet();
    for ( Iterator iter = productCharacteristics.iterator(); iter.hasNext(); )
    {
      ProductCharacteristic productCharacteristic = (ProductCharacteristic)iter.next();
      productCharTypes.add( productCharacteristic.getProductCharacteristicType() );
    }

    return productCharTypes;
  }

  /**
   * @return value of productCharacteristics property
   */
  public Set getProductCharacteristics()
  {
    return productCharacteristics;
  }

  /**
   * @param productCharacteristics value for productCharacteristicTypes property
   */
  public void setProductCharacteristics( Set productCharacteristics )
  {
    this.productCharacteristics = productCharacteristics;
  }

  public void addProductCharacteristic( ProductCharacteristic productCharacteristic )
  {
    productCharacteristic.setProduct( this );
    productCharacteristics.add( productCharacteristic );
  }

  public void addProductCharacteristicByType( ProductCharacteristicType productCharacteristicType )
  {
    ProductCharacteristic productCharacteristic = new ProductCharacteristic();
    productCharacteristic.setProductCharacteristicType( productCharacteristicType );
    productCharacteristic.setProduct( this );
    productCharacteristics.add( productCharacteristic );
  }

  /**
   * Convenience method for use in claimSubmissionCreate.jsp to determine proper name to display
   *
   * @return String
   */
  public String getProductCategoryName()
  {
    String name = getProductCategory().getName();
    if ( getProductCategory().getParentProductCategory() != null )
    {
      name = getProductCategory().getParentProductCategory().getName();
    }
    return name;
  }

  /**
   * Convenience method for use in claimSubmissionCreate.jsp to determine proper name to display
   *
   * @return String
   */
  public String getProductSubCategoryName()
  {
    String name = "";
    if ( getProductCategory().getParentProductCategory() != null )
    {
      name = getProductCategory().getName();
    }
    return name;
  }

}

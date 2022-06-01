/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.product;

import com.biperf.core.domain.BaseDomain;

/**
 * ProductCharacteristic.
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
 * <td>zahler</td>
 * <td>Mar 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCharacteristic extends BaseDomain
{
  private Product product;
  private ProductCharacteristicType productCharacteristicType;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof ProductCharacteristic ) )
    {
      return false;
    }

    final ProductCharacteristic productChar = (ProductCharacteristic)object;

    if ( getProduct() != null ? !getProduct().equals( productChar.getProduct() ) : productChar.getProduct() != null )
    {
      return false;
    }
    if ( getProductCharacteristicType() != null ? !getProductCharacteristicType().equals( productChar.getProductCharacteristicType() ) : productChar.getProductCharacteristicType() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return hashcode
   */
  public int hashCode()
  {
    if ( getProduct() != null && getProductCharacteristicType() != null )
    {
      return 29 * getProduct().hashCode() + getProductCharacteristicType().hashCode();
    }
    return 0;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ProductCharacteristic [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{product=" + this.getProduct() + "}, " );
    buf.append( "{productCharacteristicType=" + this.getProductCharacteristicType() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public Product getProduct()
  {
    return product;
  }

  public void setProduct( Product product )
  {
    this.product = product;
  }

  public ProductCharacteristicType getProductCharacteristicType()
  {
    return productCharacteristicType;
  }

  public void setProductCharacteristicType( ProductCharacteristicType productCharacteristicType )
  {
    this.productCharacteristicType = productCharacteristicType;
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/claim/ClaimProductCharacteristic.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.product.ProductCharacteristicType;

/**
 * ClaimProductCharacteristic
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
 * <td>tennant</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

/**
 * This class holds a value which the user enters for a product characteristic (i.e. "BLUE" for the
 * characteristic "COLOR").
 */
public class ClaimProductCharacteristic extends BaseDomain
{

  private ProductCharacteristicType productCharacteristicType;
  private ClaimProduct claimProduct;
  private String value;

  public ProductCharacteristicType getProductCharacteristicType()
  {
    return productCharacteristicType;
  }

  public void setProductCharacteristicType( ProductCharacteristicType productCharacteristicType )
  {
    this.productCharacteristicType = productCharacteristicType;
  }

  public ClaimProduct getClaimProduct()
  {
    return this.claimProduct;
  }

  public void setClaimProduct( ClaimProduct claimProduct )
  {
    this.claimProduct = claimProduct;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    final ClaimProductCharacteristic that = (ClaimProductCharacteristic)o;

    if ( this.claimProduct != null ? !this.claimProduct.equals( that.claimProduct ) : that.claimProduct != null )
    {
      return false;
    }
    if ( productCharacteristicType != null ? !productCharacteristicType.equals( that.productCharacteristicType ) : that.productCharacteristicType != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {

    int result;

    result = productCharacteristicType != null ? productCharacteristicType.hashCode() : 0;

    result += this.getClaimProduct() != null ? this.getClaimProduct().hashCode() : 0;

    return result;
  }

}

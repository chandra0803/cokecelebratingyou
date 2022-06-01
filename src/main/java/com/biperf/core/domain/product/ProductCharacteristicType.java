/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.product;

import com.biperf.core.domain.characteristic.Characteristic;

/**
 * ProductCharacteristicType.
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
 * <td>wadzinsk</td>
 * <td>Jun 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductCharacteristicType extends Characteristic
{

  private Boolean isUnique;

  /**
   * @return value of isUnique property
   */
  public Boolean getIsUnique()
  {
    return isUnique;
  }

  /**
   * @param isUnique value for isUnique property
   */
  public void setIsUnique( Boolean isUnique )
  {
    this.isUnique = isUnique;
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof ProductCharacteristicType ) )
    {
      return false;
    }

    final ProductCharacteristicType characteristic = (ProductCharacteristicType)o;
    if ( getNameCmKey() != null ? !getNameCmKey().equals( characteristic.getNameCmKey() ) : characteristic.getNameCmKey() != null )
    {
      return false;
    }
    if ( getCmAssetCode() != null ? !getCmAssetCode().equals( characteristic.getCmAssetCode() ) : characteristic.getCmAssetCode() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Does hashCode on the Business Key (CharacteristicName) Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;

    result = this.getNameCmKey() != null ? this.getNameCmKey().hashCode() : 0;
    result += this.getCmAssetCode() != null ? this.getCmAssetCode().hashCode() * 13 : 0;

    return result;
  }

  private static final String PRODUCT_CHARACTERISTIC_ASSET_PREFIX = "characteristic_data.product.data";

  /**
   * Return the CM Asset name prefix for this Characteristic type.
   * 
   * @return String
   */
  public String getCmAssetNamePrefix()
  {
    return PRODUCT_CHARACTERISTIC_ASSET_PREFIX;
  }

  /**
   * Return the CM Asset Type name for this Characteristic type.
   * 
   * @return String
   */
  public String getCmAssetTypeName()
  {
    return "_ProductCharacteristicData";
  }
}

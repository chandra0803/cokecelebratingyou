/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/characteristic/UserCharacteristicType.java,v $
 */

package com.biperf.core.domain.characteristic;

/**
 * UserCharacteristicType.
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
 * <td>Jun 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserCharacteristicType extends Characteristic
{

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
    if ( ! ( o instanceof UserCharacteristicType ) )
    {
      return false;
    }

    final UserCharacteristicType characteristic = (UserCharacteristicType)o;
    // The business key for the characteristic table is characteristic_name, characteristic_group
    // and characteristic_subgroup. The fields characteristic_name and characteristic_group
    // cannot be null, but characteristic_subgroup can be null. So a check is done on the
    // characteristic_name and characteristic_group first, then if characteristic_subgroup
    // is not null another check is done on that field too.
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
   * Does hashCode on the Business Key ( userId and phoneType ) Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    // The business key for the characteristic table is characteristic_name, characteristic_group
    // and characteristic_subgroup. The fields characteristic_name and characteristic_group
    // cannot be null, but characteristic_subgroup can be null. So a check is done on the
    // characteristic_name and characteristic_group first, then if characteristic_subgroup
    // is not null another check is done on that field too.
    int result;

    result = this.getNameCmKey() != null ? this.getNameCmKey().hashCode() * 13 : 0;
    result += this.getCmAssetCode() != null ? this.getCmAssetCode().hashCode() : 0;

    return result;
  }

  private static final String USER_CHARACTERISTIC_ASSET_PREFIX = "characteristic_data.user.data";

  /**
   * Return the CM Asset name prefix for this Characteristic type.
   * 
   * @return String
   */
  public String getCmAssetNamePrefix()
  {
    return USER_CHARACTERISTIC_ASSET_PREFIX;
  }

  /**
   * Return the CM Asset Type name for this Characteristic type.
   * 
   * @return String
   */
  public String getCmAssetTypeName()
  {
    return "_UserCharacteristicData";
  }
}

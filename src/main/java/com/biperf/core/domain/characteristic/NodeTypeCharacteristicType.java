/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/characteristic/NodeTypeCharacteristicType.java,v $
 */

package com.biperf.core.domain.characteristic;

/**
 * NodeTypeCharacteristicType.
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
public class NodeTypeCharacteristicType extends Characteristic
{
  public static final String CHARACTERISTIC_REG_CODE_LOC_NAME = "REG_CODE_LOCATION";
  public static final String CHARACTERISTIC_NODE_TYPE_DESC = "This characteristic for node type has been automatically generated";
  public static final String CHARACTERISTIC_NODE_TYPE_MIN_VALUE = "0";
  public static final String CHARACTERISTIC_NODE_TYPE_MAX_VALUE = "999999";
  public static final Boolean CHARACTERISTIC_NODE_TYPE_REQUIRED = new Boolean( false );
  public static final String NODE_CHARACTERISTIC_VERSION = "0";

  private Long domainId; // nodeType.nodeTypeId

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
    if ( ! ( o instanceof NodeTypeCharacteristicType ) )
    {
      return false;
    }

    final NodeTypeCharacteristicType characteristic = (NodeTypeCharacteristicType)o;
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
    Long thisSubgroup = getDomainId();
    Long objectSubgroup = characteristic.getDomainId();
    if ( thisSubgroup == null )
    {
      thisSubgroup = new Long( 0 );
    }
    if ( objectSubgroup == null )
    {
      objectSubgroup = new Long( 0 );
    }

    if ( !thisSubgroup.equals( objectSubgroup ) )
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

    result = this.getNameCmKey() != null ? this.getNameCmKey().hashCode() : 0;
    result += this.getCmAssetCode() != null ? this.getCmAssetCode().hashCode() * 13 : 0;
    result += this.getDomainId() != null ? this.getDomainId().hashCode() * 23 : 0;

    return result;
  }

  public Long getDomainId()
  {
    return domainId;
  }

  public void setDomainId( Long domainId )
  {
    this.domainId = domainId;
  }

  private static final String NODE_TYPE_CHARACTERISTIC_ASSET_PREFIX = "characteristic_data.nodetype.data";

  /**
   * Return the CM Asset name prefix for this Characteristic type.
   * 
   * @return String
   */
  public String getCmAssetNamePrefix()
  {
    return NODE_TYPE_CHARACTERISTIC_ASSET_PREFIX;
  }

  /**
   * Return the CM Asset Type name for this Characteristic type.
   * 
   * @return String
   */
  public String getCmAssetTypeName()
  {
    return "_NodeTypeCharacteristicData";
  }
}

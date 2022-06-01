/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/AddressType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The AddressType is a concrete instance of a PickListItem which wraps a type save enum object of a
 * PickList from content manager.
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
 * <td>robinsa</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AddressType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.addresstype";

  public static final String BUSINESS_TYPE = "bus";
  public static final String SHIPPING_TYPE = "shp";
  public static final String HOME_TYPE = "hom";
  public static final String OTHER_TYPE = "oth";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected AddressType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of AddressType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( AddressType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return AddressType
   */
  public static AddressType lookup( String code )
  {
    return (AddressType)getPickListFactory().getPickListItem( AddressType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return AddressType
   */
  // public static AddressType getDefaultItem()
  // {
  // return (AddressType)getPickListFactory().getDefaultPickListItem( AddressType.class );
  // }
  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}

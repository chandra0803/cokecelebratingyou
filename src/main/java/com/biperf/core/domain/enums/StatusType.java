/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/StatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * Status type picklist.
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
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class StatusType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.statustype";

  public static final String ACTIVE_CODE = "1";
  public static final String INACTIVE_CODE = "0";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected StatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( StatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static StatusType lookup( String code )
  {
    return (StatusType)getPickListFactory().getPickListItem( StatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static StatusType getDefaultItem()
  // {
  // return (StatusType)getPickListFactory().getDefaultPickListItem( StatusType.class );
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

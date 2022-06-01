/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dudam</td>
 * <td>September 17, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class SortOrderType extends PickListItem
{

  private static final long serialVersionUID = 1L;
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.sortorder";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SortOrderType()
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
    return getPickListFactory().getPickList( SortOrderType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SortOrderType lookup( String code )
  {
    return (SortOrderType)getPickListFactory().getPickListItem( SortOrderType.class, code );
  }

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

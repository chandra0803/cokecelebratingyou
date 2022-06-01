/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

public class ProcessListType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.process.list.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ProcessListType()
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
    return getPickListFactory().getPickList( ProcessListType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ProcessListType lookup( String code )
  {
    return (ProcessListType)getPickListFactory().getPickListItem( ProcessListType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ProcessListType getDefaultItem()
  // {
  // return (ProcessListType)getPickListFactory().getDefaultPickListItem(
  // PromotionPayoutType.class );
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

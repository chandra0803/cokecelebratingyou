
package com.biperf.core.domain.enums;

import java.util.List;

public class BadgeReceivedType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.badgereceived.type";

  public static final String RECEIVED = "have";
  public static final String NOT_RECEIVED = "haveNot";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BadgeReceivedType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of BadgeReceivedType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( BadgeReceivedType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return BadgeReceivedType
   */
  public static BadgeReceivedType lookup( String code )
  {
    return (BadgeReceivedType)getPickListFactory().getPickListItem( BadgeReceivedType.class, code );
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

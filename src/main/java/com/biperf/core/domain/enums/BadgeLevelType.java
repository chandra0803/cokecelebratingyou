
package com.biperf.core.domain.enums;

import java.util.List;

public class BadgeLevelType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.badgeleveltype";
  public static final String STACK_STAND = "stackstand";
  public static final String OVERALL = "overall";
  public static final String UNDEFEATED = "undefeated";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BadgeLevelType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of Badge Level Type
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( BadgeLevelType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return BadgeLevelType
   */
  public static BadgeLevelType lookup( String code )
  {
    return (BadgeLevelType)getPickListFactory().getPickListItem( BadgeLevelType.class, code );
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

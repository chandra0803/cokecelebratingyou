
package com.biperf.core.domain.enums;

import java.util.List;

public class PreliminaryProactiveEmailType extends ModuleAwarePickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.preliminaryEmail";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PreliminaryProactiveEmailType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PreliminaryProactiveEmailType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PreliminaryProactiveEmailType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PreliminaryProactiveEmailType
   */
  public static PreliminaryProactiveEmailType lookup( String code )
  {
    return (PreliminaryProactiveEmailType)getPickListFactory().getPickListItem( PreliminaryProactiveEmailType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PreliminaryProactiveEmailType
   */
  public static PreliminaryProactiveEmailType getDefaultItem()
  {
    return (PreliminaryProactiveEmailType)getPickListFactory().getDefaultPickListItem( PreliminaryProactiveEmailType.class );
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

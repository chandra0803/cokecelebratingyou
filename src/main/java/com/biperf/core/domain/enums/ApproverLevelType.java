package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * ApproverLevelType.
 * 
 * @author reddyl
 * @since 16 Apr, 2019
 */
public class ApproverLevelType extends PickListItem
{


  /**
   * Level type codes
   */
  public static final String LEVEL1 = "level1";
  public static final String LEVEL2 = "level2";
  public static final String LEVEL3 = "level3";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.coke.noms.level.select.options";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  public ApproverLevelType()
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
    return getPickListFactory().getPickList( ApproverLevelType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ApproverLevelType lookup( String code )
  {
    return (ApproverLevelType)getPickListFactory()
        .getPickListItem( ApproverLevelType.class, code );
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

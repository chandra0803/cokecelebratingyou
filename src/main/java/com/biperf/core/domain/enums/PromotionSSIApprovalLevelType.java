
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * PromotionSSIApprovalLevelType.
 * 
 * @author chowdhur
 * @since Nov 5, 2014
 */
public class PromotionSSIApprovalLevelType extends PickListItem
{

  /**
   * Level type codes
   */
  public static final String LEVEL1 = "level1";
  public static final String LEVEL2 = "level2";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.contestapproval.level";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  public PromotionSSIApprovalLevelType()
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
    return getPickListFactory().getPickList( PromotionSSIApprovalLevelType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionSSIApprovalLevelType lookup( String code )
  {
    return (PromotionSSIApprovalLevelType)getPickListFactory().getPickListItem( PromotionSSIApprovalLevelType.class, code );
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

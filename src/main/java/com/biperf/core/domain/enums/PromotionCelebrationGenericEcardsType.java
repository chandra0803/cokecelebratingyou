
package com.biperf.core.domain.enums;

import java.util.List;

public class PromotionCelebrationGenericEcardsType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promo.celebrations.ecards.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionCelebrationGenericEcardsType()
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
    return getPickListFactory().getPickList( PromotionCelebrationGenericEcardsType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionCelebrationGenericEcardsType lookup( String code )
  {
    return (PromotionCelebrationGenericEcardsType)getPickListFactory().getPickListItem( PromotionCelebrationGenericEcardsType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PromotionCelebrationGenericEcardsType getDefaultItem()
  {
    return (PromotionCelebrationGenericEcardsType)getPickListFactory().getDefaultPickListItem( PromotionCelebrationGenericEcardsType.class );
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

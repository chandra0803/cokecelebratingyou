
package com.biperf.core.domain.enums;

import java.util.List;

public class PromotionCelebrationsVideoType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promo.celebrations.video.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionCelebrationsVideoType()
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
    return getPickListFactory().getPickList( PromotionCelebrationsVideoType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionCelebrationsVideoType lookup( String code )
  {
    return (PromotionCelebrationsVideoType)getPickListFactory().getPickListItem( PromotionCelebrationsVideoType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PromotionCelebrationsVideoType getDefaultItem()
  {
    return (PromotionCelebrationsVideoType)getPickListFactory().getDefaultPickListItem( PromotionCelebrationsVideoType.class );
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


package com.biperf.core.domain.enums;

import java.util.List;

public class PromotionCelebrationsImageType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promo.celebrations.image.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromotionCelebrationsImageType()
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
    return getPickListFactory().getPickList( PromotionCelebrationsImageType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromotionCelebrationsImageType lookup( String code )
  {
    return (PromotionCelebrationsImageType)getPickListFactory().getPickListItem( PromotionCelebrationsImageType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PromotionCelebrationsImageType getDefaultItem()
  {
    return (PromotionCelebrationsImageType)getPickListFactory().getDefaultPickListItem( PromotionCelebrationsImageType.class );
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


package com.biperf.core.domain.enums;

import java.util.List;

public class PurlPromotionMediaValue extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.promotion.media.value";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlPromotionMediaValue()
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
    return getPickListFactory().getPickList( PurlPromotionMediaValue.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlPromotionMediaValue lookup( String code )
  {
    return (PurlPromotionMediaValue)getPickListFactory().getPickListItem( PurlPromotionMediaValue.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlPromotionMediaValue getDefaultItem()
  {
    return (PurlPromotionMediaValue)getPickListFactory().getDefaultPickListItem( PurlPromotionMediaValue.class );
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

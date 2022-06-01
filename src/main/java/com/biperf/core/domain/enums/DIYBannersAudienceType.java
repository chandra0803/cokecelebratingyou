
package com.biperf.core.domain.enums;

import java.util.List;

public class DIYBannersAudienceType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.diy.banners.audiencetype";

  public static final String DIY_BANNER_AUDIENCE = "diybannersaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DIYBannersAudienceType()
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
    return getPickListFactory().getPickList( DIYBannersAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static DIYBannersAudienceType lookup( String code )
  {
    return (DIYBannersAudienceType)getPickListFactory().getPickListItem( DIYBannersAudienceType.class, code );
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

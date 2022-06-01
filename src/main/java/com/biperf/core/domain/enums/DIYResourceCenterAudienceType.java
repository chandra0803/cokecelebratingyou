
package com.biperf.core.domain.enums;

import java.util.List;

public class DIYResourceCenterAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.diy.resource.center.audiencetype";

  public static final String DIY_RESOURCE_CENTER_AUDIENCE = "diyresourcecenteraudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DIYResourceCenterAudienceType()
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
    return getPickListFactory().getPickList( DIYResourceCenterAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static DIYResourceCenterAudienceType lookup( String code )
  {
    return (DIYResourceCenterAudienceType)getPickListFactory().getPickListItem( DIYResourceCenterAudienceType.class, code );
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

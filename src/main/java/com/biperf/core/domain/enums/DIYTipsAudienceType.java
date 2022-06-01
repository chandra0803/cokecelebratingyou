
package com.biperf.core.domain.enums;

import java.util.List;

public class DIYTipsAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.diy.tips.audiencetype";

  public static final String DIY_TIPS_AUDIENCE = "diytipsaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DIYTipsAudienceType()
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
    return getPickListFactory().getPickList( DIYTipsAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static DIYTipsAudienceType lookup( String code )
  {
    return (DIYTipsAudienceType)getPickListFactory().getPickListItem( DIYTipsAudienceType.class, code );
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

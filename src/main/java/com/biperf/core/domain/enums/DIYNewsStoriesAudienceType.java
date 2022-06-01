
package com.biperf.core.domain.enums;

import java.util.List;

public class DIYNewsStoriesAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.diy.news.stories.audiencetype";

  public static final String DIY_NEWS_STORIES_AUDIENCE = "diynewsstoriesaudience";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DIYNewsStoriesAudienceType()
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
    return getPickListFactory().getPickList( DIYNewsStoriesAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static DIYNewsStoriesAudienceType lookup( String code )
  {
    return (DIYNewsStoriesAudienceType)getPickListFactory().getPickListItem( DIYNewsStoriesAudienceType.class, code );
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

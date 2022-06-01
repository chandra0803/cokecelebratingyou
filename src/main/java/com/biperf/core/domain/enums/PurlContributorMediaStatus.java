
package com.biperf.core.domain.enums;

import java.util.List;

public class PurlContributorMediaStatus extends PickListItem
{
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.contributor.media.status";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlContributorMediaStatus()
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
    return getPickListFactory().getPickList( PurlContributorMediaStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlContributorMediaStatus lookup( String code )
  {
    return (PurlContributorMediaStatus)getPickListFactory().getPickListItem( PurlContributorMediaStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlContributorMediaStatus getDefaultItem()
  {
    return (PurlContributorMediaStatus)getPickListFactory().getDefaultPickListItem( PurlContributorMediaStatus.class );
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

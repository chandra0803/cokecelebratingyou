
package com.biperf.core.domain.enums;

import java.util.List;

public class PurlContributorCommentStatus extends PickListItem
{
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.contributor.comment.status";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlContributorCommentStatus()
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
    return getPickListFactory().getPickList( PurlContributorCommentStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlContributorCommentStatus lookup( String code )
  {
    return (PurlContributorCommentStatus)getPickListFactory().getPickListItem( PurlContributorCommentStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlContributorCommentStatus getDefaultItem()
  {
    return (PurlContributorCommentStatus)getPickListFactory().getDefaultPickListItem( PurlContributorCommentStatus.class );
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

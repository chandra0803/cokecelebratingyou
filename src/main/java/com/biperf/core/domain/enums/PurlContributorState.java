
package com.biperf.core.domain.enums;

import java.util.List;

public class PurlContributorState extends PickListItem
{
  public static final String INVITATION = "invitation";
  public static final String CONTRIBUTION = "contribution";
  public static final String COMPLETE = "complete";
  public static final String EXPIRED = "expired";
  public static final String ARCHIVED = "archived";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.contributor.state";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlContributorState()
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
    return getPickListFactory().getPickList( PurlContributorState.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlContributorState lookup( String code )
  {
    return (PurlContributorState)getPickListFactory().getPickListItem( PurlContributorState.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlContributorState getDefaultItem()
  {
    return (PurlContributorState)getPickListFactory().getDefaultPickListItem( PurlContributorState.class );
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

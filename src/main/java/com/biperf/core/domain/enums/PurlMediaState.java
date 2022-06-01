
package com.biperf.core.domain.enums;

import java.util.List;

public class PurlMediaState extends PickListItem
{
  public static final String STAGED = "staged";
  public static final String PROCESSING_TO_POST = "process_to_post";
  public static final String POSTED = "posted";
  public static final String DELETED = "deleted";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.media.state";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlMediaState()
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
    return getPickListFactory().getPickList( PurlMediaState.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlMediaState lookup( String code )
  {
    return (PurlMediaState)getPickListFactory().getPickListItem( PurlMediaState.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlMediaState getDefaultItem()
  {
    return (PurlMediaState)getPickListFactory().getDefaultPickListItem( PurlMediaState.class );
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

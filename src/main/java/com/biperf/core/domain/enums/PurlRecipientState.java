
package com.biperf.core.domain.enums;

import java.util.List;

public class PurlRecipientState extends PickListItem
{
  public static final String INVITATION = "invitation";
  public static final String CONTRIBUTION = "contribution";
  public static final String RECOGNITION = "recognition";
  public static final String COMPLETE = "complete";
  public static final String EXPIRED = "expired";
  public static final String ARCHIVED = "archived";

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.purl.recipient.state";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PurlRecipientState()
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
    return getPickListFactory().getPickList( PurlRecipientState.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PurlRecipientState lookup( String code )
  {
    return (PurlRecipientState)getPickListFactory().getPickListItem( PurlRecipientState.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static PurlRecipientState getDefaultItem()
  {
    return (PurlRecipientState)getPickListFactory().getDefaultPickListItem( PurlRecipientState.class );
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

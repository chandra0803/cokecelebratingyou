
package com.biperf.core.domain.enums;

import java.util.List;

public class ReceivedAwardType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.receivedaward.type";

  public static final String RECEIVED = "received";
  public static final String NOT_RECEIVED = "notReceived";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReceivedAwardType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ReceivedAwardType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReceivedAwardType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ReceivedAwardType
   */
  public static ReceivedAwardType lookup( String code )
  {
    return (ReceivedAwardType)getPickListFactory().getPickListItem( ReceivedAwardType.class, code );
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

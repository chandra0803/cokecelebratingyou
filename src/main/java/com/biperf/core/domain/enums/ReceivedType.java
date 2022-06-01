
package com.biperf.core.domain.enums;

import java.util.List;

public class ReceivedType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.received.type";

  public static final String RECEIVED = "have";
  public static final String NOT_RECEIVED = "haveNot";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReceivedType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ReceivedType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReceivedType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ReceivedType
   */
  public static ReceivedType lookup( String code )
  {
    return (ReceivedType)getPickListFactory().getPickListItem( ReceivedType.class, code );
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

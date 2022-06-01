
package com.biperf.core.domain.enums;

import java.util.List;

public class UserTokenStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.usertoken.status";

  public static final String EXPIRED = "expired";
  public static final String USED = "used";
  public static final String ISSUED = "issued";

  /**
   * This constructor is protected - only the PickListFactory class creates
   * these instances.
   */
  protected UserTokenStatusType()
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
    return getPickListFactory().getPickList( UserTokenStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static UserTokenStatusType lookup( String code )
  {
    return (UserTokenStatusType)getPickListFactory().getPickListItem( UserTokenStatusType.class, code );
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

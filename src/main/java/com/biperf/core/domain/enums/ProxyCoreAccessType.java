
package com.biperf.core.domain.enums;

import java.util.List;

public class ProxyCoreAccessType extends PickListItem
{

  private static final String PICKLIST_ASSET = "picklist.proxy.core.access";

  public static final String ROSTER_MGMT = "roster_mgmt";
  public static final String SEND_ALERT = "send_alert";
  public static final String BUDGET_TRANSFER = "budget_transfer";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ProxyCoreAccessType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ProxyCoreAccessType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ProxyCoreAccessType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ProxyCoreAccessType
   */
  public static ProxyCoreAccessType lookup( String code )
  {
    return (ProxyCoreAccessType)getPickListFactory().getPickListItem( ProxyCoreAccessType.class, code );
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

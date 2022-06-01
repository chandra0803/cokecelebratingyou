
package com.biperf.core.domain.enums;

import java.util.List;

public class SmackTalkTabType extends PickListItem
{

  private static final long serialVersionUID = 2970489641825165668L;

  /**
     * Asset name used in Content Manager
     */
  private static final String PICKLIST_ASSET = "picklist.smacktalk.tab";

  public static final String GLOBAL_TAB = "global";
  public static final String TEAM_TAB = "team";
  public static final String ME_TAB = "mine";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SmackTalkTabType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of AddressType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SmackTalkTabType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return AddressType
   */
  public static SmackTalkTabType lookup( String code )
  {
    return (SmackTalkTabType)getPickListFactory().getPickListItem( SmackTalkTabType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return AddressType
   */
  // public static AddressType getDefaultItem()
  // {
  // return (AddressType)getPickListFactory().getDefaultPickListItem( AddressType.class );
  // }
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

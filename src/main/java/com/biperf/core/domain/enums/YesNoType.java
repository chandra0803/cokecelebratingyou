
package com.biperf.core.domain.enums;

import java.util.List;

public class YesNoType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.yesno";

  public static final String YES = "yes";
  public static final String NO = "no";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected YesNoType()
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
    return getPickListFactory().getPickList( YesNoType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static YesNoType lookup( String code )
  {
    return (YesNoType)getPickListFactory().getPickListItem( YesNoType.class, code );
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

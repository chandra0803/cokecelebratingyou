
package com.biperf.core.domain.enums;

import java.util.List;

public class GivenType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.given.type";

  public static final String HAVE = "have";
  public static final String HAVE_NOT = "haveNot";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected GivenType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of GivenType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( GivenType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return GivenType
   */
  public static GivenType lookup( String code )
  {
    return (GivenType)getPickListFactory().getPickListItem( GivenType.class, code );
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

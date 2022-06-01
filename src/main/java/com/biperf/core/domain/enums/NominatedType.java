
package com.biperf.core.domain.enums;

import java.util.List;

public class NominatedType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.nominated.type";

  public static final String NOMINATED = "have";
  public static final String NOT_NOMINATED = "haveNot";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected NominatedType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of NominatedType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( NominatedType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return NominatedType
   */
  public static NominatedType lookup( String code )
  {
    return (NominatedType)getPickListFactory().getPickListItem( NominatedType.class, code );
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


package com.biperf.core.domain.enums;

import java.util.List;

public class SelectedType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.selected.type";

  public static final String HAVE = "select";
  public static final String HAVE_NOT = "not_select";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SelectedType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SelectedType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SelectedType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SelectedType
   */
  public static SelectedType lookup( String code )
  {
    return (SelectedType)getPickListFactory().getPickListItem( SelectedType.class, code );
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

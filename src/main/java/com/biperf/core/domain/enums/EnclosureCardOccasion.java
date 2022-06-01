
package com.biperf.core.domain.enums;

import java.util.List;

public class EnclosureCardOccasion extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.enclosurecardoccasion";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected EnclosureCardOccasion()
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
    return getPickListFactory().getPickList( EnclosureCardOccasion.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static EnclosureCardOccasion lookup( String code )
  {
    return (EnclosureCardOccasion)getPickListFactory().getPickListItem( EnclosureCardOccasion.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static EnclosureCardOccasion getDefaultItem()
  {
    return (EnclosureCardOccasion)getPickListFactory().getDefaultPickListItem( EnclosureCardOccasion.class );
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

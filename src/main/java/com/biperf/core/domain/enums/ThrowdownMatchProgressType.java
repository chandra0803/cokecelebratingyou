
package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class ThrowdownMatchProgressType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.matchprogress.type";

  public static final String INCREMENTAL = "increment";
  public static final String REPLACE = "replace";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ThrowdownMatchProgressType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MatchTeamOutcomeType
   */
  @SuppressWarnings( "unchecked" )
  public static List<ThrowdownMatchProgressType> getList()
  {
    return getPickListFactory().getPickList( ThrowdownMatchProgressType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static ThrowdownMatchProgressType lookup( String code )
  {
    return (ThrowdownMatchProgressType)getPickListFactory().getPickListItem( ThrowdownMatchProgressType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static ThrowdownMatchProgressType getDefaultItem()
  {
    return (ThrowdownMatchProgressType)getPickListFactory().getDefaultPickListItem( ThrowdownMatchProgressType.class );
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

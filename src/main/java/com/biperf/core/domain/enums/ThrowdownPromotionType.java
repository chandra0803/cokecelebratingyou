
package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class ThrowdownPromotionType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.promotion.type";

  public static final String HEAD_2_HEAD = "h2h";
  // public static final String BRACKET = "bracket";
  // public static final String BOTH = "both";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ThrowdownPromotionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MatchTeamOutcomeType
   */
  @SuppressWarnings( "unchecked" )
  public static List<ThrowdownPromotionType> getList()
  {
    return getPickListFactory().getPickList( ThrowdownPromotionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static ThrowdownPromotionType lookup( String code )
  {
    return (ThrowdownPromotionType)getPickListFactory().getPickListItem( ThrowdownPromotionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static ThrowdownPromotionType getDefaultItem()
  {
    return (ThrowdownPromotionType)getPickListFactory().getDefaultPickListItem( ThrowdownPromotionType.class );
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


package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class MatchStatusType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.matchstatus.type";

  public static final String PLAYED = "played";
  public static final String NOT_PLAYED = "not_played";
  // public static final String IN_PROGRESS = "in_progress";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MatchStatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MatchTeamOutcomeType
   */
  @SuppressWarnings( "unchecked" )
  public static List<MatchStatusType> getList()
  {
    return getPickListFactory().getPickList( MatchStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static MatchStatusType lookup( String code )
  {
    return (MatchStatusType)getPickListFactory().getPickListItem( MatchStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static MatchStatusType getDefaultItem()
  {
    return (MatchStatusType)getPickListFactory().getDefaultPickListItem( MatchStatusType.class );
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

  public boolean isPlayed()
  {
    return PLAYED.equals( getCode() );
  }

  public boolean isNotPlayed()
  {
    return NOT_PLAYED.equals( getCode() );
  }
}

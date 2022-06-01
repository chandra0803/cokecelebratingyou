
package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class MatchTeamOutcomeType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.matchteamoutcome.type";

  public static final String NONE = "none";
  public static final String WIN = "win";
  public static final String LOSS = "loss";
  public static final String TIE = "tie";
  public static final String BYE = "bye";
  public static final String FORFEIT = "forfeit";
  public static final String DISQUALIFIED = "disqualified";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MatchTeamOutcomeType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MatchTeamOutcomeType
   */
  @SuppressWarnings( "unchecked" )
  public static List<MatchTeamOutcomeType> getList()
  {
    return getPickListFactory().getPickList( MatchTeamOutcomeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static MatchTeamOutcomeType lookup( String code )
  {
    return (MatchTeamOutcomeType)getPickListFactory().getPickListItem( MatchTeamOutcomeType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static MatchTeamOutcomeType getDefaultItem()
  {
    return (MatchTeamOutcomeType)getPickListFactory().getDefaultPickListItem( MatchTeamOutcomeType.class );
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

  public boolean isWin()
  {
    return WIN.equals( getCode() );
  }

  public boolean isLoss()
  {
    return LOSS.equals( getCode() );
  }

  public boolean isNone()
  {
    return NONE.equals( getCode() );
  }

  public boolean isBye()
  {
    return BYE.equals( getCode() );
  }
}

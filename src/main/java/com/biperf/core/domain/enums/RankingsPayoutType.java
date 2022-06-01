
package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class RankingsPayoutType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.rankingspayout.type";

  public static final String RANKINGS_AND_PAYOUT = "rankings_and_payout";
  public static final String RANKINGS_ONLY = "rankings_only";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected RankingsPayoutType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MatchTeamOutcomeType
   */
  @SuppressWarnings( "unchecked" )
  public static List<RankingsPayoutType> getList()
  {
    return getPickListFactory().getPickList( RankingsPayoutType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static RankingsPayoutType lookup( String code )
  {
    return (RankingsPayoutType)getPickListFactory().getPickListItem( RankingsPayoutType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static RankingsPayoutType getDefaultItem()
  {
    return (RankingsPayoutType)getPickListFactory().getDefaultPickListItem( RankingsPayoutType.class );
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

  public boolean isRankingOnly()
  {
    return RANKINGS_ONLY.equals( getCode() );
  }

  public boolean isPayoutIncluded()
  {
    return RANKINGS_AND_PAYOUT.equals( getCode() );
  }
}

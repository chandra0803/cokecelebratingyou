
package com.biperf.core.domain.enums;

import java.util.List;

public class ChallengepointProductionCountType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.cpproductioncounttype";

  public static final String CP_ACHIEVED = "cp_achieved";
  public static final String CP_NOT_ACHIEVED_SALES_OVER_BASELINE = "cp_not_achieved_sales_over_baseline";
  public static final String SUBTOTAL = "subtotal";
  public static final String CP_NOT_ACHIEVED_SALES_UNDER_BASELINE = "cp_not_achieved_sales_under_baseline";
  public static final String TOTAL = "total";
  public static final String DID_NOT_SELECT_CP = "did_not_select_cp";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ChallengepointProductionCountType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ChallengepointProductionCountType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ChallengepointProductionCountType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ChallengepointProductionCountType
   */
  public static ChallengepointProductionCountType lookup( String code )
  {
    return (ChallengepointProductionCountType)getPickListFactory().getPickListItem( ChallengepointProductionCountType.class, code );
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

  public boolean isChallengepointAchieved()
  {
    return CP_ACHIEVED.equals( getCode() );
  }

  public boolean isChallengepointNotAchievedSalesOverBaseline()
  {
    return CP_NOT_ACHIEVED_SALES_OVER_BASELINE.equals( getCode() );
  }

  public boolean isChallengepointNotAchievedSalesUnderBaseline()
  {
    return CP_NOT_ACHIEVED_SALES_UNDER_BASELINE.equals( getCode() );
  }

  public boolean isDidNotSelectChallengepoint()
  {
    return DID_NOT_SELECT_CP.equals( getCode() );
  }

  public boolean isSubTotal()
  {
    return SUBTOTAL.equals( getCode() );
  }

  public boolean isTotal()
  {
    return TOTAL.equals( getCode() );
  }

}

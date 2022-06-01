
package com.biperf.core.domain.enums;

import java.util.List;

public class CustomApproverRoutingType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.customapproverrouting";

  public static final String BYNOMINEE = "by_nominee";
  public static final String BYNOMINATOR = "by_nominator";

  // public static final List<String> eligibleForViewApproverModal = Arrays.asList( CHARACTERISTIC ,
  // BEHAVIOR , AWARD );

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  public CustomApproverRoutingType()
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
    return getPickListFactory().getPickList( CustomApproverRoutingType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CustomApproverRoutingType lookup( String code )
  {
    return (CustomApproverRoutingType)getPickListFactory().getPickListItem( CustomApproverRoutingType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static CustomApproverType getDefaultItem()
  // {
  // return (CustomApproverType)getPickListFactory().getDefaultPickListItem(
  // CustomApproverType.class
  // );
  // }
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

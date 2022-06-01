
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * This picklist is for the approval level search filter options on the nomination aging report. 
 * 
 * @author corneliu
 * @since Jun 2, 2016
 */
@SuppressWarnings( "serial" )
public class ApprovalLevelType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.nomination.approval.status";

  public static final String LEVEL_1 = "1";
  public static final String LEVEL_2 = "2";
  public static final String LEVEL_3 = "3";
  public static final String LEVEL_4 = "4";
  public static final String LEVEL_5 = "5";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ApprovalLevelType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public static List<ApprovalLevelType> getList()
  {
    return getPickListFactory().getPickList( ApprovalLevelType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ApprovalLevelType lookup( String code )
  {
    return (ApprovalLevelType)getPickListFactory().getPickListItem( ApprovalLevelType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static ApprovalLevelType getDefaultItem()
  {
    return (ApprovalLevelType)getPickListFactory().getDefaultPickListItem( ApprovalLevelType.class );
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

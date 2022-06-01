
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * This picklist is for the status search filter options on the nomination aging report. 
 * The picklist actually used to keep track of a nomination's status is {@link ApprovalStatusType}
 * 
 * @author corneliu
 * @since Jun 2, 2016
 */
@SuppressWarnings( "serial" )
public class NominationApprovalStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.nomination.approval.status";

  public static final String PENDING = "pend";
  public static final String WINNER = "winner";
  public static final String NON_WINNER = "non_winner";
  public static final String EXPIRED = "expired";
  public static final String APPROVED = "approved";
  public static final String MORE_INFO = "more_info";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected NominationApprovalStatusType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public static List<NominationApprovalStatusType> getList()
  {
    return getPickListFactory().getPickList( NominationApprovalStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static NominationApprovalStatusType lookup( String code )
  {
    return (NominationApprovalStatusType)getPickListFactory().getPickListItem( NominationApprovalStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static NominationApprovalStatusType getDefaultItem()
  {
    return (NominationApprovalStatusType)getPickListFactory().getDefaultPickListItem( NominationApprovalStatusType.class );
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

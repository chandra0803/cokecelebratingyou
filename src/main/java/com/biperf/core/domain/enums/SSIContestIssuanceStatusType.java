
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * SSIContestIssuanceStatusType.
 * 
 * @author kandhi
 * @since Feb 6, 2015
 * @version 1.0
 */
public class SSIContestIssuanceStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.atnissuancestaus.type";

  public static final String IN_PROGRESS = "inProgress";
  public static final String CANCELLED = "cancelled";
  public static final String COMPLETED = "completed";
  public static final String WAITING_FOR_APPROVAL = "waiting_for_approval";
  public static final String APPROVED = "approved";
  public static final String DENIED = "denied";
  public static final String FINALIZE_RESULTS = "finalize_results";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIContestIssuanceStatusType()
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
    return getPickListFactory().getPickList( SSIContestActivitySubmissionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIContestIssuanceStatusType lookup( String code )
  {
    return (SSIContestIssuanceStatusType)getPickListFactory().getPickListItem( SSIContestIssuanceStatusType.class, code );
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

  /**
   * 
   * @return true if this object represents the "pending" status; false otherwise.
   */
  public boolean isDenied()
  {
    return getCode().equalsIgnoreCase( DENIED );
  }

  /**
   * 
   * @return true if this object represents the "pending" status; false otherwise.
   */
  public boolean isApproved()
  {
    return getCode().equalsIgnoreCase( APPROVED );
  }

  /**
   * 
   * @return true if this object represents the "isWaitingForApproval" status; false otherwise.
   */
  public boolean isWaitingForApproval()
  {
    return getCode().equalsIgnoreCase( WAITING_FOR_APPROVAL );
  }

}

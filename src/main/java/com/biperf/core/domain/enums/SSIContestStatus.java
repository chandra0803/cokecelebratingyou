
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * SSIContestStatus.
 * 
 * @author simhadri
 * @since Nov 13, 2014
 *
 */
public class SSIContestStatus extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.contest.status";

  public static final String DRAFT = "draft";
  public static final String WAITING_FOR_APPROVAL = "waiting_for_approval";
  public static final String PENDING = "pending";
  public static final String LIVE = "live";
  public static final String FINALIZE_RESULTS = "finalize_results";
  public static final String CLOSED = "closed";
  public static final String DENIED = "denied";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIContestStatus()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SSIContestStatus
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SSIContestStatus.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SSIContestStatus
   */
  public static SSIContestStatus lookup( String code )
  {
    return (SSIContestStatus)getPickListFactory().getPickListItem( SSIContestStatus.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return SSIContestStatus
   */
  public static SSIContestStatus getDefaultItem()
  {
    return (SSIContestStatus)getPickListFactory().getDefaultPickListItem( SSIContestStatus.class );
  }

  /**
   * 
   * @return true if this object represents the "live" status; false otherwise.
   */
  public boolean isLive()
  {
    return getCode().equalsIgnoreCase( LIVE );
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
   * @return true if this object represents the "draft" status; false otherwise.
   */
  public boolean isDraft()
  {
    return getCode().equalsIgnoreCase( DRAFT );
  }

  /**
   * 
   * @return true if this object represents the "pending" status; false otherwise.
   */
  public boolean isPending()
  {
    return getCode().equalsIgnoreCase( PENDING );
  }

  /**
   * 
   * @return true if this object represents the "waiting for approval" status; false otherwise.
   */
  public boolean isWaitingForApproval()
  {
    return getCode().equalsIgnoreCase( WAITING_FOR_APPROVAL );
  }

  /**
   * 
   * @return true if this object represents the "denied" status; false otherwise.
   */
  public boolean isDenied()
  {
    return getCode().equalsIgnoreCase( DENIED );
  }

  /**
   * 
   * @return true if this object represents the "finalize_results" status; false otherwise.
   */
  public boolean isFinalizeResults()
  {
    return getCode().equalsIgnoreCase( FINALIZE_RESULTS );
  }

  /**
   * 
   * @return true if this object represents the "closed" status; false otherwise.
   */
  public boolean isClosed()
  {
    return getCode().equalsIgnoreCase( CLOSED );
  }

}

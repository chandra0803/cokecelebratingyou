
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * @author dudam
 * @since Nov 4, 2014
 * @version 1.0
 */
public class SSIContestActivitySubmissionType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.submission";

  public static final String CSV_CODE = "csv";
  public static final String CLAIM_CODE = "claim";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIContestActivitySubmissionType()
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
  public static SSIContestActivitySubmissionType lookup( String code )
  {
    return (SSIContestActivitySubmissionType)getPickListFactory().getPickListItem( SSIContestActivitySubmissionType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "csv"; false
   *         otherwise.
   */
  public boolean isCsv()
  {
    return getCode().equalsIgnoreCase( CSV_CODE );
  }

  /**
   * 
   * @return true if this object represents the "claim"; false
   *         otherwise.
   */
  public boolean isClaim()
  {
    return getCode().equalsIgnoreCase( CLAIM_CODE );
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

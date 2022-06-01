
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * @author dudam
 * @since Nov 4, 2014
 * @version 1.0
 */
public class SSIPayoutType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.payout";

  public static final String POINTS_CODE = "points";
  public static final String MERCHANDISE_CODE = "merchandise";
  public static final String OTHER_CODE = "other";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIPayoutType()
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
    return getPickListFactory().getPickList( SSIPayoutType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIPayoutType lookup( String code )
  {
    return (SSIPayoutType)getPickListFactory().getPickListItem( SSIPayoutType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "points"; false
   *         otherwise.
   */
  public boolean isPoints()
  {
    return getCode().equalsIgnoreCase( POINTS_CODE );
  }

  /**
   * 
   * @return true if this object represents the "merchandise"; false
   *         otherwise.
   */
  public boolean isMerchandise()
  {
    return getCode().equalsIgnoreCase( MERCHANDISE_CODE );
  }

  /**
   * 
   * @return true if this object represents the "other"; false
   *         otherwise.
   */
  public boolean isOther()
  {
    return getCode().equalsIgnoreCase( OTHER_CODE );
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

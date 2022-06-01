
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * 
 * SSIBillPayoutCodeType.
 * 
 * @author kandhi
 * @since Dec 9, 2014
 * @version 1.0
 */
public class SSIBillPayoutCodeType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.ssi.billpayoutcodetype";

  public static final String PARTICIPANT = "participant";
  public static final String CREATOR = "creator";
  public static final String OTHER = "other";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SSIBillPayoutCodeType()
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
    return getPickListFactory().getPickList( SSIBillPayoutCodeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SSIBillPayoutCodeType lookup( String code )
  {
    return (SSIBillPayoutCodeType)getPickListFactory().getPickListItem( SSIBillPayoutCodeType.class, code );
  }

  /**
   * 
   * @return true if this object represents the "participant"; false
   *         otherwise.
   */
  public boolean isParticipant()
  {
    return getCode().equalsIgnoreCase( PARTICIPANT );
  }

  /**
   * 
   * @return true if this object represents the "creator"; false
   *         otherwise.
   */
  public boolean isCreator()
  {
    return getCode().equalsIgnoreCase( CREATOR );
  }

  /**
   * 
   * @return true if this object represents the "participant"; false
   *         otherwise.
   */
  public boolean isOther()
  {
    return getCode().equalsIgnoreCase( OTHER );
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

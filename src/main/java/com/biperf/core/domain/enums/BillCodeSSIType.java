
package com.biperf.core.domain.enums;

import java.util.List;

public class BillCodeSSIType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */

  private static final String PICKLIST_ASSET = "picklist.bill.code.ssi";
  public static final String DEFAULT_ITEM_CODE = "ssibillcodes";

  public static final String CREATOR = "creator";
  public static final String PARTICIPANT = "participant";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BillCodeSSIType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of BillCodeSSIType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( BillCodeSSIType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return BillCodeSSIType
   */
  public static BillCodeSSIType lookup( String code )
  {
    return (BillCodeSSIType)getPickListFactory().getPickListItem( BillCodeSSIType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return BillCodeSSIType
   */
  public static BillCodeSSIType getDefaultItem()
  {
    return (BillCodeSSIType)getPickListFactory().getDefaultPickListItem( BillCodeSSIType.class );
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

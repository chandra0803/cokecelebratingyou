
package com.biperf.core.domain.enums;

import java.util.List;

public class BillCodeRecognitionType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */

  private static final String PICKLIST_ASSET = "picklist.bill.code";
  public static final String DEFAULT_ITEM_CODE = "bill";

  public static final String GIVER = "giver";
  public static final String RECEIVER = "receiver";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BillCodeRecognitionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of BillCodeRecognitionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( BillCodeRecognitionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return BillCodeRecognitionType
   */
  public static BillCodeRecognitionType lookup( String code )
  {
    return (BillCodeRecognitionType)getPickListFactory().getPickListItem( BillCodeRecognitionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return BillCodeRecognitionType
   */
  public static BillCodeRecognitionType getDefaultItem()
  {
    return (BillCodeRecognitionType)getPickListFactory().getDefaultPickListItem( BillCodeRecognitionType.class );
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

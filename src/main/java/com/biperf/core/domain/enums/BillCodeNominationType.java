
package com.biperf.core.domain.enums;

import java.util.List;

public class BillCodeNominationType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */

  private static final String PICKLIST_ASSET = "picklist.bill.code.nomination";
  public static final String DEFAULT_ITEM_CODE = "nomination";

  public static final String NOMINATOR = "nominator";
  public static final String NOMINEE = "nominee";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected BillCodeNominationType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of BillCodeNominationType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( BillCodeNominationType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return BillCodeNominationType
   */
  public static BillCodeNominationType lookup( String code )
  {
    return (BillCodeNominationType)getPickListFactory().getPickListItem( BillCodeNominationType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return BillCodeNominationType
   */
  public static BillCodeNominationType getDefaultItem()
  {
    return (BillCodeNominationType)getPickListFactory().getDefaultPickListItem( BillCodeNominationType.class );
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

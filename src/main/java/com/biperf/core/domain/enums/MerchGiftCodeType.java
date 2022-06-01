/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * MerchGiftCodeType.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>ernste</td>
 * <td>August 7, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class MerchGiftCodeType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.merch.giftcode.type";

  public static final String PRODUCT = "product";
  public static final String LEVEL = "level";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MerchGiftCodeType()
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
    return getPickListFactory().getPickList( MerchGiftCodeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MerchGiftCodeType lookup( String code )
  {
    return (MerchGiftCodeType)getPickListFactory().getPickListItem( MerchGiftCodeType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return MerchGiftCodeType
   */
  public static MerchGiftCodeType getDefaultItem()
  {
    return (MerchGiftCodeType)getPickListFactory().getDefaultPickListItem( MerchGiftCodeType.class );
  }

  public boolean isProduct()
  {
    return getCode().equalsIgnoreCase( PRODUCT );
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

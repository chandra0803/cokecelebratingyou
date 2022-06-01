/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PromotionType
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
 * <td>gaddam</td>
 * <td>Jun 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TransactionHistoryType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.transactionhistorytype";

  public static final String NOMINATION = "nomination";
  public static final String PRODUCT_CLAIM = "product_claim";
  public static final String RECOGNITION = "recognition";
  public static final String QUIZ = "quiz";
  public static String GOALQUEST = "goalquest";
  /**
   * for transaction history discretionary promotion type
   */
  public static final String DISCRETIONARY = "discretionary";
  /**
   * for transaction history reversals
   */
  public static final String REVERSAL = "reversal";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TransactionHistoryType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PromotionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( TransactionHistoryType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static TransactionHistoryType lookup( String code )
  {
    return (TransactionHistoryType)getPickListFactory().getPickListItem( TransactionHistoryType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  // public static PromotionType getDefaultItem()
  // {
  // return (PromotionType)getPickListFactory().getDefaultPickListItem( PromotionType.class );
  // }
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

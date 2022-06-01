/*
 * $Source$
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ProgressTransactionType.
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
 * <td>meadows</td>
 * <td>March 23, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProgressTransactionType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.progresstransactiontype";

  public static final String SALE = "S";
  public static final String RETURN = "R";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ProgressTransactionType()
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
    return getPickListFactory().getPickList( ProgressTransactionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ProgressTransactionType lookup( String code )
  {
    return (ProgressTransactionType)getPickListFactory().getPickListItem( ProgressTransactionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return ProgressLoadType
   */
  public static ProgressTransactionType getDefaultItem()
  {
    return (ProgressTransactionType)getPickListFactory().getDefaultPickListItem( ProgressTransactionType.class );
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

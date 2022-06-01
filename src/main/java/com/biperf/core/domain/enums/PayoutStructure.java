/*
 * $Source$
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PayoutStructure.
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
 * <td>Dec 6, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutStructure extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.payoutstructure";

  public static final String FIXED = "fixed";
  public static final String RATE = "rate";
  public static final String BOTH = "both";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PayoutStructure()
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
    return getPickListFactory().getPickList( PayoutStructure.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PayoutStructure lookup( String code )
  {
    return (PayoutStructure)getPickListFactory().getPickListItem( PayoutStructure.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static PayoutStructure getDefaultItem()
  {
    return (PayoutStructure)getPickListFactory().getDefaultPickListItem( PayoutStructure.class );
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

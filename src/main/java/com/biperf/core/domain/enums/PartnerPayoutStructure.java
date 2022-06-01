/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PartnerPayoutStructure.java,v $
 * (c) 2008 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PartnerPayoutStructure.
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
 * <td>reddy</td>
 * <td>Feb 25, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PartnerPayoutStructure extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.partnerpayoutstructure";

  public static final String FIXED = "fixed";
  public static final String PERCENTAGE = "percent";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PartnerPayoutStructure()
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
    return getPickListFactory().getPickList( PartnerPayoutStructure.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PartnerPayoutStructure lookup( String code )
  {
    return (PartnerPayoutStructure)getPickListFactory().getPickListItem( PartnerPayoutStructure.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static PartnerPayoutStructure getDefaultItem()
  {
    return (PartnerPayoutStructure)getPickListFactory().getDefaultPickListItem( PartnerPayoutStructure.class );
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
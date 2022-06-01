/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/PromoMgrPayoutFreqType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The PromoMgrPayoutFreqType is a concrete instance of a PickListItem which wrappes a type save
 * enum object of a PickList from content manager.
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
 * <td>sedey</td>
 * <td>July 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoMgrPayoutFreqType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promo.mgr.payout.freq.type";

  // Picklist codes
  public static final String MONTHLY = "monthly";
  public static final String QUARTERLY = "quarterly";
  public static final String ANNUALLY = "annually";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PromoMgrPayoutFreqType()
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
    return getPickListFactory().getPickList( PromoMgrPayoutFreqType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static PromoMgrPayoutFreqType lookup( String code )
  {
    return (PromoMgrPayoutFreqType)getPickListFactory().getPickListItem( PromoMgrPayoutFreqType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromoMgrPayoutFreqType getDefaultItem()
  // {
  // return (PromoMgrPayoutFreqType)getPickListFactory().getDefaultPickListItem(
  // PromoMgrPayoutFreqType.class );
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

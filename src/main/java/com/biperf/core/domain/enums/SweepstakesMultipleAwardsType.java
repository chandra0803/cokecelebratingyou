/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/SweepstakesMultipleAwardsType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * SweepstakesMultipleAwardsType.
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
 * <td>jenniget</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesMultipleAwardsType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.sweepstakesmultipleawardstype";

  // Codes
  public static final String PERIOD_CODE = "period";
  public static final String MULTIPLE_CODE = "multiple";
  public static final String DRAWING_CODE = "drawing";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SweepstakesMultipleAwardsType()
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
    return getPickListFactory().getPickList( SweepstakesMultipleAwardsType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SweepstakesMultipleAwardsType lookup( String code )
  {
    return (SweepstakesMultipleAwardsType)getPickListFactory().getPickListItem( SweepstakesMultipleAwardsType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionAudienceType getDefaultItem()
  // {
  // return (PromotionAudienceType)getPickListFactory().getDefaultPickListItem(
  // PromotionAudienceType.class );
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

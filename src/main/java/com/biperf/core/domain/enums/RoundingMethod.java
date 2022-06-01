/*
 * $Source$
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.enums;

import java.math.BigDecimal;
import java.util.List;

/**
 * RoundingMethod.
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
public class RoundingMethod extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.goalquest.roundingmethod";

  public static final String STANDARD = "standard";
  public static final String UP = "up";
  public static final String DOWN = "down";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected RoundingMethod()
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
    return getPickListFactory().getPickList( RoundingMethod.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static RoundingMethod lookup( String code )
  {
    return (RoundingMethod)getPickListFactory().getPickListItem( RoundingMethod.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PayoutStructure
   */
  public static RoundingMethod getDefaultItem()
  {
    return (RoundingMethod)getPickListFactory().getDefaultPickListItem( RoundingMethod.class );
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

  public int getBigDecimalRoundingMode()
  {
    if ( getCode() != null && getCode().equals( STANDARD ) )
    {
      return BigDecimal.ROUND_HALF_UP;
    }
    if ( getCode() != null && getCode().equals( UP ) )
    {
      return BigDecimal.ROUND_UP;
    }
    return BigDecimal.ROUND_DOWN;
  }
}

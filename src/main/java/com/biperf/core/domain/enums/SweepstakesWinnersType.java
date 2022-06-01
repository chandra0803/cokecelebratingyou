/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/SweepstakesWinnersType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * SweepstakesWinnersType.
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
public class SweepstakesWinnersType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.sweepstakeswinnerstype";

  // Codes
  public static final String SPECIFIC_CODE = "specific";
  public static final String PERCENTAGE_CODE = "percentage";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SweepstakesWinnersType()
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
    return getPickListFactory().getPickList( SweepstakesWinnersType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SweepstakesWinnersType lookup( String code )
  {
    return (SweepstakesWinnersType)getPickListFactory().getPickListItem( SweepstakesWinnersType.class, code );
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

  public boolean isSpecific()
  {
    return getCode().equalsIgnoreCase( SPECIFIC_CODE );
  }

  public boolean isPercentage()
  {
    return getCode().equalsIgnoreCase( PERCENTAGE_CODE );
  }
}

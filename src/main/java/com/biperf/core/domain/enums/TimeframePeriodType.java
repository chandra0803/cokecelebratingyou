/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/TimeframePeriodType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The TimeframePeriodType is a concrete instance of a PickListItem which wraps a type save enum
 * object of a PickList from content manager.
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
 * <td>robinsa</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TimeframePeriodType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.timeframe.period.type";

  public static final String WEEKLEY = "weekly";
  public static final String MONTHLY = "monthly";
  public static final String QUARTERLY = "quarterly";
  public static final String ANNUALLY = "annually";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TimeframePeriodType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of TimeframePeriodType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( TimeframePeriodType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return TimeframePeriodType
   */
  public static TimeframePeriodType lookup( String code )
  {
    return (TimeframePeriodType)getPickListFactory().getPickListItem( TimeframePeriodType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return TimeframePeriodType
   */
  public static TimeframePeriodType getDefaultItem()
  {
    return (TimeframePeriodType)getPickListFactory().getDefaultPickListItem( TimeframePeriodType.class );
  }

  /**
   * Overridden from
   * 
   * @see PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isWeekly()
  {
    return WEEKLEY.equals( getCode() );
  }

  public boolean isMonthly()
  {
    return MONTHLY.equals( getCode() );
  }

  public boolean isQuarterly()
  {
    return QUARTERLY.equals( getCode() );
  }

  public boolean isAnnually()
  {
    return ANNUALLY.equals( getCode() );
  }
}

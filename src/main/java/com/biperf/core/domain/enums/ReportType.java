/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ReportType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ReportType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Oct 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ReportType extends PickListItem
{

  public static final String PICKLIST_ASSET = "picklist.reporttype";

  public static final String SUMMARY = "summary";
  public static final String DETAILS = "details";
  public static final String BAR_CHART = "barchart";
  public static final String PIE_CHART = "piechart";
  public static final String TREND = "trend";
  public static final String FUNNEL = "funnel";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of ReportType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReportType.class, new PickListItemSortOrderComparator() );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ReportType
   */
  public static ReportType lookup( String code )
  {
    return (ReportType)getPickListFactory().getPickListItem( ReportType.class, code );
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

  public boolean isSummary()
  {
    return this.getCode().equals( SUMMARY );
  }

  public boolean isDetails()
  {
    return this.getCode().equals( DETAILS );
  }

  public boolean isBarChart()
  {
    return this.getCode().equals( BAR_CHART );
  }

  public boolean isPieChart()
  {
    return this.getCode().equals( PIE_CHART );
  }

  public boolean isTrend()
  {
    return this.getCode().equals( TREND );
  }

  public boolean isFunnel()
  {
    return this.getCode().equals( FUNNEL );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ReportChartType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ReportChartType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sedey</td>
 * <td>Aug 1, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author sedey
 *
 */
public class ReportChartType extends PickListItem
{
  public static String BAR_2D = "Bar2D";
  public static String COLUMN_3D = "Column3D";
  public static String MS_BAR_3D = "MSBar3D";
  public static String MS_COLUMN_3D = "MSColumn3D";
  public static String MS_LINE_2D = "MSLine";
  public static String LINE_2D = "Line";
  public static String PIE_2D = "Pie2D";
  public static String PIE_3D = "Pie3D";
  public static String STACKED_COLUMN_3D = "StackedColumn3D";
  public static String STACKED_COLUMN_2D = "StackedColumn2D";
  public static String STACKED_BAR_2D = "StackedBar2D";
  public static String STACKED_BAR_3D = "StackedBar3D";
  public static String BUBBLE = "Bubble";
  public static String SCATTER = "Scatter";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.reportcharttype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportChartType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogReasonType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( ReportChartType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogReasonType
   */
  public static ReportChartType lookup( String code )
  {
    return (ReportChartType)getPickListFactory().getPickListItem( ReportChartType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogReasonType
   */
  public static ReportChartType getDefaultItem()
  {
    return (ReportChartType)getPickListFactory().getDefaultPickListItem( ReportChartType.class );
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

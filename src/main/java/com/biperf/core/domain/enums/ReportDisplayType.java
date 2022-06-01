
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ReportDisplayType.
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
 * <td>robinsra</td>
 * <td>Nov 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ReportDisplayType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.report.display.type";

  public static final String COUNT = "count";
  public static final String PERCENT = "percent";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportDisplayType()
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
    return getPickListFactory().getPickList( ReportDisplayType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ReportDisplayType lookup( String code )
  {
    return (ReportDisplayType)getPickListFactory().getPickListItem( ReportDisplayType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ReportDisplayType getDefaultItem()
  // {
  // return (ReportDisplayType)getPickListFactory().getDefaultPickListItem( ReportDisplayType.class
  // );
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

  public boolean isCount()
  {
    return COUNT.equals( getCode() );
  }

  public boolean isPercent()
  {
    return PERCENT.equals( getCode() );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ReportParameterType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ReportParameterType <p/> <b>Change History:</b><br>
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
public class ReportParameterType extends PickListItem
{
  public static String TEXT = "text";
  public static String RADIO = "radio";
  public static String CHECKBOX = "checkbox";
  public static String DATE_PICKER = "datePicker";
  public static String SELECT_PICKLIST = "selectPicklist";
  public static String SELECT_QUERY = "selectQuery";
  public static String MULTI_SELECT_PICKLIST = "multiSelectPick";
  public static String MULTI_SELECT_QUERY = "multiSelectQuery";
  public static String HIDDEN = "hidden";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.reportparametertype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ReportParameterType()
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
    return getPickListFactory().getPickList( ReportParameterType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogReasonType
   */
  public static ReportParameterType lookup( String code )
  {
    return (ReportParameterType)getPickListFactory().getPickListItem( ReportParameterType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogReasonType
   */
  public static ReportParameterType getDefaultItem()
  {
    return (ReportParameterType)getPickListFactory().getDefaultPickListItem( ReportParameterType.class );
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

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/ProcessParameterInputFormatType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * A concrete instance of a PickListItem which wrappes a type save enum object of a PickList from
 * content manager.
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
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessParameterInputFormatType extends PickListItem
{

  public static final String TEXT_FIELD = "text_field";
  public static final String CHECK_BOXES = "check_boxes";
  public static final String DROP_DOWN = "drop_down";
  public static final String DATE = "date";
  public static final String RADIO = "radio";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.process.parameter.input.format.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ProcessParameterInputFormatType()
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
    return getPickListFactory().getPickList( ProcessParameterInputFormatType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ProcessParameterInputFormatType lookup( String code )
  {
    return (ProcessParameterInputFormatType)getPickListFactory().getPickListItem( ProcessParameterInputFormatType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static PromotionPayoutType getDefaultItem()
  // {
  // return (PromotionPayoutType)getPickListFactory().getDefaultPickListItem(
  // PromotionPayoutType.class );
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

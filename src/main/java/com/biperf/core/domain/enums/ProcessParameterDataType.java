/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
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
public class ProcessParameterDataType extends PickListItem
{

  public static final String NUMBER = "number";
  public static final String String = "string";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.process.parameter.data.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ProcessParameterDataType()
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
    return getPickListFactory().getPickList( ProcessParameterDataType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ProcessParameterDataType lookup( String code )
  {
    return (ProcessParameterDataType)getPickListFactory().getPickListItem( ProcessParameterDataType.class, code );
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

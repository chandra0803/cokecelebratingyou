
package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/NumberFieldInputFormatType.java,v $
                                     */

import java.util.List;

/**
 * This is a concrete instance of a PickListItem which wrappes a type safe enum object of a PickList
 * from content manager.
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
 * <td>tennant</td>
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NumberFieldInputFormatType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.numberfieldinputformattype";

  public static final String WHOLE = "whole";
  public static final String DECIMALS = "decimals";
  public static final String MONEY = "money";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected NumberFieldInputFormatType()
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
    return getPickListFactory().getPickList( NumberFieldInputFormatType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static NumberFieldInputFormatType lookup( String code )
  {
    return (NumberFieldInputFormatType)getPickListFactory().getPickListItem( NumberFieldInputFormatType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static NumberFieldInputFormatType getDefaultItem()
  // {
  // return (NumberFieldInputFormatType)getPickListFactory().getDefaultPickListItem(
  // NumberFieldInputFormatType.class );
  // }
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
}


package com.biperf.core.domain.enums;/*
                                     * (c) 2005 BI, Inc.  All rights reserved.
                                     * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/SingleSelectionInputControlType.java,v $
                                     */

import java.util.List;

/**
 * The com.biperf.core.domain.enums.SingleSelectionInputControlType is a concrete instance of a
 * PickListItem which wrappes a type safe enum object of a PickList from content manager.
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
public class SingleSelectionInputControlType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.singleselectioninputcontroltype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SingleSelectionInputControlType()
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
    return getPickListFactory().getPickList( SingleSelectionInputControlType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static SingleSelectionInputControlType lookup( String code )
  {
    return (SingleSelectionInputControlType)getPickListFactory().getPickListItem( SingleSelectionInputControlType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static SingleSelectionInputControlType getDefaultItem()
  // {
  // return (SingleSelectionInputControlType)getPickListFactory().getDefaultPickListItem(
  // SingleSelectionInputControlType.class );
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

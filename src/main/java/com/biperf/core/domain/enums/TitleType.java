/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/TitleType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The TitleType is a concrete instance of a PickListItem which wrappes a type save enum object of a
 * PickList from content manager.
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
 * <td>dunne</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TitleType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.titletype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TitleType()
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
    return getPickListFactory().getPickList( TitleType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static TitleType lookup( String code )
  {
    return (TitleType)getPickListFactory().getPickListItem( TitleType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static TitleType getDefaultItem()
  // {
  // return (TitleType)getPickListFactory().getDefaultPickListItem( TitleType.class );
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
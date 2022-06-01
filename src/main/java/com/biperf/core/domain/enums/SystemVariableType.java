/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/SystemVariableType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The SystenVariableType is a concrete instance of a PickListItem which wrappes a type save enum
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
 * <td>Jason</td>
 * <td>Apr 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariableType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.system.variable.type";

  public static final String STRING = "5";
  public static final String BOOLEAN = "1";
  public static final String INTEGER = "2";
  public static final String DATE = "7";
  public static final String DOUBLE = "4";
  public static final String LONG = "3";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected SystemVariableType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of SystemVariableType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( SystemVariableType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return SystemVariableType
   */
  public static SystemVariableType lookup( String code )
  {
    return (SystemVariableType)getPickListFactory().getPickListItem( SystemVariableType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return SystemVariableType
   */
  // public static SystemVariableType getDefaultItem()
  // {
  // return (SystemVariableType)getPickListFactory().getDefaultPickListItem(
  // SystemVariableType.class );
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

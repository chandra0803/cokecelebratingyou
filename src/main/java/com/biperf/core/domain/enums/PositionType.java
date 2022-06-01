/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/PositionType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * PositionType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class PositionType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.positiontype";

  public static final String COORDINATOR = "coord";
  public static final String DIRECTOR = "dir";
  public static final String EMPLOYEE = "emp";
  public static final String MANAGER = "mgr";
  public static final String PRESIDENT = "pres";
  public static final String SUPERVISOR = "superv";
  public static final String TRAINEE = "train";
  public static final String VICE_PRESIDENT = "vpres";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected PositionType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of PositionType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( PositionType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PositionType
   */
  public static PositionType lookup( String code )
  {
    return (PositionType)getPickListFactory().getPickListItem( PositionType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return PositionType
   */
  // public static PositionType getDefaultItem()
  // {
  // return (PositionType)getPickListFactory().getDefaultPickListItem( PositionType.class );
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

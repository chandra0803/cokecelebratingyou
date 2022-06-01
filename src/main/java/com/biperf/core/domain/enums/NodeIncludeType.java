/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/NodeIncludeType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * NodeIncludeType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>wadzinsk</td>
 * <td>June 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class NodeIncludeType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.nodeselectiontype";

  public static final String NODE_AND_CHILDREN = "nac";
  public static final String NODE_ONLY = "no";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected NodeIncludeType()
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
    return getPickListFactory().getPickList( NodeIncludeType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static NodeIncludeType lookup( String code )
  {
    return (NodeIncludeType)getPickListFactory().getPickListItem( NodeIncludeType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static NodeIncludeType getDefaultItem()
  // {
  // return (NodeIncludeType)getPickListFactory().getDefaultPickListItem( NodeIncludeType.class );
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

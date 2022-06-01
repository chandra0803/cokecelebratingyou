/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/HierarchyRoleType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * HierarchyType <p/> <b>Change History:</b><br>
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
public class HierarchyRoleType extends PickListItem
{
  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.hierarchyrole.type";

  public static final String OWNER = "own";
  public static final String MEMBER = "mbr";
  public static final String MANAGER = "mgr";

  /**
   * This constructor is public - but only the PickListFactory and tests should create these
   * instances.
   */
  protected HierarchyRoleType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of HierarchyType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( HierarchyRoleType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return HierarchyType
   */
  public static HierarchyRoleType lookup( String code )
  {
    return (HierarchyRoleType)getPickListFactory().getPickListItem( HierarchyRoleType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return HierarchyType
   */
  public static HierarchyRoleType getDefaultItem()
  {
    return (HierarchyRoleType)getPickListFactory().getDefaultPickListItem( HierarchyRoleType.class );
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

  /**
   * @return boolean isOwner
   */
  public boolean isOwner()
  {
    return this.getCode().equals( HierarchyRoleType.OWNER );
  }

  public boolean isManager()
  {
    return this.getCode().equals( HierarchyRoleType.MANAGER );
  }

  public boolean isMember()
  {
    return this.getCode().equals( HierarchyRoleType.MEMBER );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/DepartmentType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * DepartmentType <p/> <b>Change History:</b><br>
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
public class DepartmentType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.department.type";

  public static final String MARKETING = "mkt";
  public static final String SERVICE = "svc";
  public static final String SALES = "sales";
  public static final String OFFICE = "offc";
  public static final String ENGINEERING = "eng";
  public static final String HUMAN_RESOURCES = "hr";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected DepartmentType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of DepartmentType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( DepartmentType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return DepartmentType
   */
  public static DepartmentType lookup( String code )
  {
    return (DepartmentType)getPickListFactory().getPickListItem( DepartmentType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return DepartmentType
   */
  // public static DepartmentType getDefaultItem()
  // {
  // return (DepartmentType)getPickListFactory().getDefaultPickListItem( DepartmentType.class );
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

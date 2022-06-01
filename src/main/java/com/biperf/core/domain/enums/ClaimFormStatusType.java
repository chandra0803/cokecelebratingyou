/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ClaimFormStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * ClaimFormStatusType is a concrete instance of a PickListItem which wrapes a type save enum object
 * of a PickList from content manager.
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
 * <td>robinsra</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStatusType extends PickListItem
{

  public static final String UNDER_CONSTRUCTION = "undrconstr";
  public static final String TEMPLATE = "tmpl";
  public static final String COMPLETED = "compl";
  public static final String ASSIGNED = "assgn";
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.claimformstatustype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ClaimFormStatusType()
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
    return getPickListFactory().getPickList( ClaimFormStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ClaimFormStatusType lookup( String code )
  {
    return (ClaimFormStatusType)getPickListFactory().getPickListItem( ClaimFormStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ClaimFormStatusType getDefaultItem()
  // {
  // return (ClaimFormStatusType)getPickListFactory().getDefaultPickListItem(
  // ClaimFormStatusType.class );
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

  /**
   * returns true if this type is "Under Construction"
   * 
   * @return boolean
   */
  public boolean isUnderConstruction()
  {
    return UNDER_CONSTRUCTION.equals( getCode() );
  }

  /**
   * returns true if this type is "Completed"
   * 
   * @return boolean
   */
  public boolean isCompleted()
  {
    return COMPLETED.equals( getCode() );
  }

  public boolean isAssigned()
  {
    return ASSIGNED.equals( getCode() );
  }

  public boolean isTemplate()
  {
    return TEMPLATE.equals( getCode() );
  }
} // end ClaimFormStatusType

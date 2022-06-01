/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CalculatorStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CalculatorStatusType is a concrete instance of a PickListItem which wrapes a type save enum object
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
 * <td>sedey</td>
 * <td>May 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorStatusType extends PickListItem
{

  public static final String UNDER_CONSTRUCTION = "undrconstr";
  public static final String COMPLETED = "compl";
  public static final String ASSIGNED = "assgn";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.calculatorstatustype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CalculatorStatusType()
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
    return getPickListFactory().getPickList( CalculatorStatusType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CalculatorStatusType lookup( String code )
  {
    return (CalculatorStatusType)getPickListFactory().getPickListItem( CalculatorStatusType.class, code );
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
   * @return boolean
   */
  public boolean isUnderConstruction()
  {
    return UNDER_CONSTRUCTION.equals( getCode() );
  }

  /**
   * returns true if this type is "Completed"
   * @return boolean
   */
  public boolean isCompleted()
  {
    return COMPLETED.equals( getCode() );
  }

  /**
   * returns true if this type is "Assigned"
   * @return boolean
   */
  public boolean isAssigned()
  {
    return ASSIGNED.equals( getCode() );
  }

} // end ClaimFormStatusType

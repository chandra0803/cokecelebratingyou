/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CalculatorAwardType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CalculatorAwardType is a concrete instance of a PickListItem which wrapes a type save enum object
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
 * <td>babu</td>
 * <td>May 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorAwardType extends PickListItem
{

  public static final String FIXED_AWARD = "fixed";
  public static final String RANGE_AWARD = "range";
  public static final String MERCHANDISE_LEVEL_AWARD = "merchlevel";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.calculatorawardtype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CalculatorAwardType()
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
    return getPickListFactory().getPickList( CalculatorAwardType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static CalculatorAwardType lookup( String code )
  {
    return (CalculatorAwardType)getPickListFactory().getPickListItem( CalculatorAwardType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static CalculatorAwardType getDefaultItem()
  {
    return (CalculatorAwardType)getPickListFactory().getDefaultPickListItem( CalculatorAwardType.class );
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
   * returns true if this type is "Fixed"
   * @return boolean
   */
  public boolean isFixedAward()
  {
    return FIXED_AWARD.equals( getCode() );
  }

  /**
   * returns true if this type is "Range"
   * @return boolean
   */
  public boolean isRangeAward()
  {
    return RANGE_AWARD.equals( getCode() );
  }

  /**
   * returns true if this type is "Merch Level"
   * @return boolean
   */
  public boolean isMerchLevelAward()
  {
    return MERCHANDISE_LEVEL_AWARD.equals( getCode() );
  }

} // end CalculatorAwardType

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/*
 * StackRankFactorType <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 2, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankFactorType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset codes
   */
  public static final String NUMERIC_CLAIM_ELEMENT = "numericClaimElement";
  public static final String QUANTITY_SOLD = "quantitySold";

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.stackrankfactortype";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all stack rank factor types.
   * 
   * @return a list of all stack rank factor types, as a <code>List</code> of
   *         <code>StackRankFactorType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( StackRankFactorType.class );
  }

  /**
   * Returns the specified <code>StackRankFactorType</code> object; returns null if the given code
   * is null or invalid.
   * 
   * @param code a stack rank factor type code.
   * @return the specified <code>StackRankFactorType</code> object, or returns null if the given
   *         code is null or invalid.
   */
  public static StackRankFactorType lookup( String code )
  {
    return (StackRankFactorType)getPickListFactory().getPickListItem( StackRankFactorType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the PickListFactory class should create
   * instances of this class.
   */
  StackRankFactorType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}

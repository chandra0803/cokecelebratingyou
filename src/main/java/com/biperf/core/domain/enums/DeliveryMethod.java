/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/DeliveryMethod.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/*
 * DeliveryMethod <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Dec 7, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class DeliveryMethod extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset codes
   */
  public static final String FAST_SHIP = "fastShip";
  public static final String STANDARD = "standard";
  public static final String ECONOMY = "economy";

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.deliverymethod";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all delivery methods.
   * 
   * @return a list of all delivery methods, as a <code>List</code> of <code>DeliveryMethod</code>
   *         objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( DeliveryMethod.class );
  }

  /**
   * Returns the specified <code>DeliveryMethod</code> object; returns null if the given code is
   * null or invalid.
   * 
   * @param code a delivery method code.
   * @return the specified <code>DeliveryMethod</code> object, or returns null if the given code
   *         is null or invalid.
   */
  public static DeliveryMethod lookup( String code )
  {
    return (DeliveryMethod)getPickListFactory().getPickListItem( DeliveryMethod.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the PickListFactory class should create
   * instances of this class.
   */
  DeliveryMethod()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}

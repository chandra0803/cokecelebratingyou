/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/*
 * StackRankApprovalType <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 6, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankApprovalType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset codes
   */
  public static final String AUTOMATIC_APPROVAL = "automaticApproval";
  public static final String MANUAL_APPROVAL = "manualApproval";

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.stackrankapprovaltype";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all stack rank approval types.
   * 
   * @return a list of all stack rank approval types, as a <code>List</code> of
   *         <code>StackRankApprovalType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( StackRankApprovalType.class );
  }

  /**
   * Returns the specified <code>StackRankApprovalType</code> object; returns null if the given
   * code is null or invalid.
   * 
   * @param code a stack rank approval type code.
   * @return the specified <code>StackRankApprovalType</code> object, or returns null if the given
   *         code is null or invalid.
   */
  public static StackRankApprovalType lookup( String code )
  {
    return (StackRankApprovalType)getPickListFactory().getPickListItem( StackRankApprovalType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the {@link PickListFactory} class should
   * create instances of this class.
   */
  StackRankApprovalType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

/*
 * StackRankState <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 6, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankState extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset codes
   */
  public static final String BEFORE_CREATE_STACK_RANK_LISTS = "before_creating_stackrank_lists";
  public static final String CREATING_STACK_RANK_LISTS = "creating_stackrank_lists";
  public static final String WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED = "waiting_for_stackrank_lists_to_be_approved";
  public static final String STACK_RANK_LISTS_APPROVED = "stackrank_lists_approved";

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.stackrankstate";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all stack rank states.
   * 
   * @return a list of all stack rank states, as a <code>List</code> of
   *         <code>StackRankState</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( StackRankState.class );
  }

  /**
   * Returns the specified <code>StackRankState</code> object; returns null if the given code is
   * null or invalid.
   * 
   * @param code a stack rank state code.
   * @return the specified <code>StackRankState</code> object, or null if the given code is null
   *         or invalid.
   */
  public static StackRankState lookup( String code )
  {
    return (StackRankState)getPickListFactory().getPickListItem( StackRankState.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has protected access because only the PickListFactory class should create
   * instances of this class.
   */
  protected StackRankState()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  /**
   * Returns true if this StackRankState object represents the state "before creating stack rank
   * lists;" returns false otherwise.
   * 
   * @return true if this StackRankState object represents the state "before creating stack rank
   *         lists;" return false otherwise.
   */
  public boolean isBeforeCreatingStackRankLists()
  {
    return getCode().equalsIgnoreCase( BEFORE_CREATE_STACK_RANK_LISTS );
  }

  /**
   * Returns true if this StackRankState object represents the state "waiting for stack rank lists
   * to be approved;" returns false otherwise.
   * 
   * @return true if this StackRankState object represents the state "waiting for stack rank lists
   *         to be approved;" return false otherwise.
   */
  public boolean isWaitingForStackRankListsToBeApproved()
  {
    return getCode().equalsIgnoreCase( WAITING_FOR_STACK_RANK_LISTS_TO_BE_APPROVED );
  }
}

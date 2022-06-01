/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.service.AssociationRequestCollection;

/*
 * StackRankDAO <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 7, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public interface StackRankDAO extends DAO
{
  /**
   * The key to an object that implements this interface in the Spring application context.
   */
  static final String BEAN_NAME = "stackRankDAO";

  /**
   * Creates the stack rank lists for the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank whoses stack rank lists this method will create.
   */
  void createStackRankLists( Long stackRankId );

  /**
   * Deletes the given stack rank.
   * 
   * @param stackRank the stack rank to delete.
   */
  void deleteStackRank( StackRank stackRank );

  /**
   * Returns the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank to get.
   * @return the specified stack rank.
   */
  StackRank getStackRank( Long stackRankId );

  /**
   * Returns the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank to get.
   * @param associationRequest
   * @return the specified stack rank.
   */
  StackRank getStackRank( Long stackRankId, AssociationRequestCollection associationRequest );

  /**
   * Returns a List of stack rank objects based on a queryConstraint and hydrates anything in the
   * associationRequest
   * 
   * @param queryConstraint the queryConstraint object
   * @param associationRequest
   * @return List
   */
  List getStackRankList( StackRankQueryConstraint queryConstraint, AssociationRequestCollection associationRequest );

  /**
   * Saves the given stack rank.
   * 
   * @param stackRank the stack rank to save.
   * @return the saved version of the stack rank.
   */
  StackRank saveStackRank( StackRank stackRank );

  /**
   * Get the stack rank which has the given state that was created last i.e. max(date_created) by a
   * promotion id.
   * 
   * @param promotionId
   * @param state
   * @param associationRequest
   * @return the timestamp of the date created of the stack rank
   */
  public StackRank getLatestStackRankByPromotionId( Long promotionId, String state, AssociationRequestCollection associationRequest );

  /**
   * Returns a count of stack rank product claim promotions that meet the specified criteria. Any
   * parameter can be left null so that the query is not constrained by that parameter.
   * 
   * @param queryConstraint
   * @return int the stack rank promotion list count with a specified state(#StackRankState)
   */
  public int getStackRankListCount( StackRankQueryConstraint queryConstraint );
}

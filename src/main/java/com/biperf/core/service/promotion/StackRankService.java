/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion;

import java.util.List;
import java.util.Set;

import com.biperf.core.dao.promotion.hibernate.StackRankQueryConstraint;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/*
 * StackRankService <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 7, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public interface StackRankService extends SAO
{
  /**
   * The key to an object that implements this interface in the Spring application context.
   */
  public static final String BEAN_NAME = "stackRankService";

  /**
   * Approves the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank that this method will approve.
   */
  void approveStackRank( Long stackRankId );

  /**
   * Creates the stack rank lists for the specified stack rank.
   * 
   * @param stackRankId the ID of the stack rank whoses stack rank lists this method will create.
   */
  void createStackRankLists( Long stackRankId );

  /**
   * Deletes the specified stack rank.
   * 
   * @param id the ID of the stack rank to delete.
   */
  void deleteStackRank( Long id );

  /**
   * Returns the specified stack rank.
   * 
   * @param id the ID of the stack rank to get.
   * @return the specified stack rank.
   */
  StackRank getStackRank( Long id );

  /**
   * Returns the specified stack rank.
   * 
   * @param id the ID of the stack rank to get.
   * @return the specified stack rank.
   */
  StackRank getStackRank( Long id, AssociationRequestCollection associationRequest );

  /**
   * Saves the given stack rank.
   * 
   * @param stackRank the stack rank to save.
   * @return the saved version of the stack rank.
   */
  StackRank saveStackRank( StackRank stackRank );

  /**
   * Returns a list of stack rank objects
   * 
   * @param queryConstraint the stack rank to save.
   * @return a list of stack rank objects
   */
  List getStackRankList( StackRankQueryConstraint queryConstraint );

  /**
   * Returns a list of stack rank objects
   * 
   * @param queryConstraint the stack rank to save.
   * @param associationRequest
   * @return a list of stack rank objects
   */
  List getStackRankList( StackRankQueryConstraint queryConstraint, AssociationRequestCollection associationRequest );

  /**
   * Get all of the NodeType objects for a given StackRank.
   * 
   * @param stackRankId
   * @return List of NodeTypes
   */
  public Set getNodeTypesByStackRankId( Long stackRankId );

  /**
   * Get all of the Node objects for a StackRank and NodeType.
   * 
   * @param stackRankId
   * @param nodeTypeId
   * @return List of Nodes
   */
  public List getNodesByStackRankIdAndNodeTypeId( Long stackRankId, Long nodeTypeId );

  /**
   * Returns a count of stack rank product claim promotions that meet the specified criteria. Any
   * parameter can be left null so that the query is not constrained by that parameter.
   * 
   * @param queryConstraint
   * @return int the stack rank promotion list count with a specified state(#StackRankState)
   */
  public int getStackRankListCount( StackRankQueryConstraint queryConstraint );

  /**
   * Get the stack rank which has the given state that was created last i.e. max(date_created) by a
   * promotion id.
   * 
   * @param promotionId
   * @param state
   * @param associationRequest
   * @return StackRank
   */
  public StackRank getLatestStackRankByPromotionId( Long promotionId, String state, AssociationRequestCollection associationRequest );

  /**
   * @param userId
   * @return List of Promotions where stack rank is viewable for the given user.
   */
  public List getStackRankPromotionsForHomepage( Long userId );

}

/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.promotion.hibernate.StackRankNodeQueryConstraint;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * StackRankNodeService.
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
 * <td>gaddam</td>
 * <td>Mar 8, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface StackRankNodeService extends SAO
{
  /**
   * The key to an object that implements this interface in the Spring application context.
   */
  static final String BEAN_NAME = "stackRankNodeService";

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankNodeId the ID of the stack rank node to retrieve.
   * @return the specified stack rank node.
   */
  StackRankNode getStackRankNode( Long stackRankNodeId );

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankNodeId the ID of the stack rank node to retrieve.
   * @param associationRequests initializes stack rank node associations.
   * @return the specified stack rank node.
   */
  StackRankNode getStackRankNode( Long stackRankNodeId, AssociationRequestCollection associationRequests );

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId the ID of the stack rank whose stack rank node is returned.
   * @param nodeId the ID of the node whose stack rank node is returned.
   * @return the specified stack rank node.
   */
  StackRankNode getStackRankNode( Long stackRankId, Long nodeId );

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId the ID of the stack rank whose stack rank node is returned.
   * @param nodeId the ID of the node whose stack rank node is returned.
   * @param associationRequests initializes stack rank node associations.
   * @return the specified stack rank node.
   */
  StackRankNode getStackRankNode( Long stackRankId, Long nodeId, AssociationRequestCollection associationRequests );

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId the ID of the stack rank whose stack rank node is returned.
   * @param nodeId the ID of the node whose stack rank node is returned.
   * @param userId a stack rank node is returned only if this user is associated with the given
   *          node.
   * @param associationRequests initializes stack rank node associations.
   * @return the specified stack rank node.
   */
  StackRankNode getStackRankNode( Long stackRankId, Long nodeId, Long userId, AssociationRequestCollection associationRequests );

  /**
   * Returns the stack rank nodes that satisfy the query constraint.
   * 
   * @param queryConstraint specifies the stack rank nodes to return.
   * @return the stack rank nodes that satisfy the query constraint, as a <code>List</code> of
   *         {@link StackRankNode} objects.
   */
  List getStackRankNodes( StackRankNodeQueryConstraint queryConstraint );

  /**
   * @return a Map<key:StackRankNode, value:List>, where the value is a List of
   *         VisibleStackRankNodeQualifierEnum's for which the user qualifies as visible.
   */
  Map getVisibleStackRankNodes( Long userId, Long[] optionalStackRankIdsIncluded, Long[] optionalNodeTypeIdsIncluded );
}

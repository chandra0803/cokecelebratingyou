/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.promotion.StackRankNodeDAO;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * StackRankNodeDAOImpl.
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
 */
public class StackRankNodeDAOImpl extends BaseDAO implements StackRankNodeDAO
{
  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankNodeId the ID of the stack rank node to retrieve.
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankNodeId )
  {
    return (StackRankNode)getSession().get( StackRankNode.class, stackRankNodeId );
  }

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId
   * @param nodeId
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankId, Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.selectStackRankNodeByStackRankIdAndNodeId" );

    query.setLong( "stackRankId", stackRankId.longValue() );
    query.setLong( "nodeId", nodeId.longValue() );

    return (StackRankNode)query.uniqueResult();
  }

  /**
   * Returns the specified stack rank node.
   * 
   * @param stackRankId the ID of the stack rank whose stack rank node is returned.
   * @param nodeId the ID of the node whose stack rank node is returned.
   * @param userId a stack rank node is returned only if this user is associated with the given
   *          node.
   * @return the specified stack rank node.
   */
  public StackRankNode getStackRankNode( Long stackRankId, Long nodeId, Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.selectStackRankNodeByStackRankIdNodeIdAndUserId" );

    query.setLong( "stackRankId", stackRankId.longValue() );
    query.setLong( "nodeId", nodeId.longValue() );
    query.setLong( "userId", userId.longValue() );

    return (StackRankNode)query.uniqueResult();
  }

  /**
   * Returns the stack rank nodes that satisfy the query constraint.
   * 
   * @param queryConstraint specifies the stack rank nodes to return.
   * @return the stack rank nodes that satisfy the query constraint, as a <code>List</code> of
   *         {@link StackRankNode} objects.
   */
  public List getStackRankNodes( StackRankNodeQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectList( queryConstraint );
  }
}

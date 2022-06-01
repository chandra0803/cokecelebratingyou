/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.utils.HibernateSessionManager;

/*
 * StackRankNodeQueryConstraint <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 20, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 */

public class StackRankNodeQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * Limits the stack rank nodes fetched to only those for these stack ranks.
   */
  private Long[] stackRankIdsIncluded;

  /**
   * Limits the stack rank nodes fetched to only those that contain a ranking for this user.
   */
  private Long userId;

  /**
   * Only include stack rank nodes whose stackRank matche any of these states.
   */
  private StackRankState[] stackRankStatesIncluded;

  /**
   * Limits the stack rank nodes fetched to only those for the specified promotionId.
   */
  private Long promotionId;

  /**
   * include stackRankNodes whose associated node type in the node matches any of these
   * nodeTypeIdsIncluded
   */
  private Long[] nodeTypeIdsIncluded;

  /**
   * include stackRankNodes where the  manager or owner of the stack rank node is 
   * managerOrOwnerUserIdOfSelectedNode.
   */
  private Long managerOrOwnerUserIdOfSelectedNode;

  /**
   * include stackRankNodes where the manager or owner of the stack rank node or any descendant 
   * node is 
   * managerOrOwnerUserIdOfSelectedNodeAndBelow.
   */
  private Long managerOrOwnerUserIdOfSelectedNodeAndBelow;

  /**
   * Exclude stackRankNodes whose associated promotion does not matching any of the types
   */
  private PromotionStatusType[] promotionStatusTypesExcluded;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "stackRankNode" );

    if ( stackRankIdsIncluded != null )
    {
      criteria.add( Restrictions.in( "stackRankNode.stackRank.id", stackRankIdsIncluded ) );
    }

    if ( userId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRankParticipants", "stackRankParticipant" );
      criteria.add( Restrictions.eq( "stackRankParticipant.participant.id", userId ) );
    }

    if ( stackRankStatesIncluded != null && stackRankStatesIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRank", "stackRank" );
      criteria.add( Restrictions.in( "stackRank.state", stackRankStatesIncluded ) );
    }

    if ( promotionStatusTypesExcluded != null && promotionStatusTypesExcluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRank", "stackRank" );
      createAliasIfNotAlreadyCreated( criteria, "stackRank.promotion", "promotion" );
      criteria.add( Restrictions.not( Restrictions.in( "promotion.promotionStatus", promotionStatusTypesExcluded ) ) );
    }

    if ( promotionId != null )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRank", "stackRank" );
      criteria.add( Restrictions.eq( "stackRank.promotion.id", promotionId ) );
    }

    if ( nodeTypeIdsIncluded != null && nodeTypeIdsIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.node", "node" );

      createAliasIfNotAlreadyCreated( criteria, "node.nodeType", "nodeType" );

      criteria.add( Restrictions.in( "nodeType.id", nodeTypeIdsIncluded ) );
    }

    if ( managerOrOwnerUserIdOfSelectedNode != null && managerOrOwnerUserIdOfSelectedNode.longValue() != 0 )
    {
      Criterion criterion = Restrictions.sqlRestriction(
                                                         " {alias}.node_id IN  " + "     (   SELECT node_id   " + "                FROM user_node  " + "               WHERE user_id = ?  "
                                                             + "                 AND role in ('" + HierarchyRoleType.MANAGER + "','" + HierarchyRoleType.OWNER + "') )",
                                                         managerOrOwnerUserIdOfSelectedNode,
                                                         StandardBasicTypes.LONG );
      criteria.add( criterion );
    }

    if ( managerOrOwnerUserIdOfSelectedNodeAndBelow != null && managerOrOwnerUserIdOfSelectedNodeAndBelow.longValue() != 0 )
    {
      Criterion criterion = Restrictions.sqlRestriction(
                                                         " {alias}.node_id IN  " + "  (" + "    SELECT un.node_id FROM USER_NODE un" + "      WHERE un.user_id = 5598   AND un.ROLE IN ('"
                                                             + HierarchyRoleType.MANAGER + "','" + HierarchyRoleType.OWNER + "')" + "      AND un.node_id IN (      SELECT node_id FROM NODE"
                                                             + "       START WITH NODE.node_id = {alias}.node_id" + "       CONNECT BY PRIOR NODE.node_id = NODE.parent_node_id" + "       )    )",
                                                         managerOrOwnerUserIdOfSelectedNodeAndBelow,
                                                         StandardBasicTypes.LONG );
      criteria.add( criterion );
    }

    return criteria;
  }

  public Class getResultClass()
  {
    return StackRankNode.class;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getManagerOrOwnerUserIdOfSelectedNode()
  {
    return managerOrOwnerUserIdOfSelectedNode;
  }

  public void setManagerOrOwnerUserIdOfSelectedNode( Long managerOrOwnerUserIdOfSelectedNode )
  {
    this.managerOrOwnerUserIdOfSelectedNode = managerOrOwnerUserIdOfSelectedNode;
  }

  public Long getManagerOrOwnerUserIdOfSelectedNodeAndBelow()
  {
    return managerOrOwnerUserIdOfSelectedNodeAndBelow;
  }

  public void setManagerOrOwnerUserIdOfSelectedNodeAndBelow( Long managerOrOwnerUserIdOfSelectedNodeAndBelow )
  {
    this.managerOrOwnerUserIdOfSelectedNodeAndBelow = managerOrOwnerUserIdOfSelectedNodeAndBelow;
  }

  public void setNodeTypeIdsIncluded( Long[] nodeTypeIdsIncluded )
  {
    this.nodeTypeIdsIncluded = nodeTypeIdsIncluded;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public StackRankState[] getStackRankStatesIncluded()
  {
    return stackRankStatesIncluded;
  }

  public void setStackRankStatesIncluded( StackRankState[] stackRankStatesIncluded )
  {
    this.stackRankStatesIncluded = stackRankStatesIncluded;
  }

  public PromotionStatusType[] getPromotionStatusTypesExcluded()
  {
    return promotionStatusTypesExcluded;
  }

  public void setPromotionStatusTypesExcluded( PromotionStatusType[] promotionStatusTypesExcluded )
  {
    this.promotionStatusTypesExcluded = promotionStatusTypesExcluded;
  }

  public Long[] getStackRankIdsIncluded()
  {
    return stackRankIdsIncluded;
  }

  public void setStackRankIdsIncluded( Long[] stackRankIdsIncluded )
  {
    this.stackRankIdsIncluded = stackRankIdsIncluded;
  }
}

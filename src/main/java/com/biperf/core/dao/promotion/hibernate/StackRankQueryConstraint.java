/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.utils.HibernateSessionManager;

/**
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
 * <td>sedey</td>
 * <td>March 8, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class StackRankQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * Only include stackRanks matching any of these states
   */
  private StackRankState[] stackRankStatesIncluded;

  /**
   * Only include stackRanks for these promotions
   */
  private Long[] promotionIdsIncluded;

  /**
   * include stackRanks whose node id matches any of these nodeIdsIncluded
   */
  private Long[] nodeIdsIncluded;

  /**
   * include stackRanks whose userId matches any of these userIdsIncluded.
   */
  private Long[] userIdsIncluded;

  /**
   * include stackRanks for which the userIdForNodeAndBelow is a manager for one
   * of the stackRank's participants.
   */
  private Long userIdForNodeAndBelow;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "stackRank" );

    if ( stackRankStatesIncluded != null && stackRankStatesIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "stackRank.state", stackRankStatesIncluded ) );
    }

    if ( promotionIdsIncluded != null && promotionIdsIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "stackRank.promotion.id", promotionIdsIncluded ) );
    }

    if ( userIdsIncluded != null && userIdsIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRank.stackRankNodes", "stackRankNode" );

      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRankParticipants", "stackRankParticipant" );

      Criterion inRestriction = Restrictions.in( "stackRankParticipant.participant.id", userIdsIncluded );

      criteria.add( inRestriction );
    }

    if ( nodeIdsIncluded != null && nodeIdsIncluded.length > 0 )
    {
      Criteria criteriaWithStackRankNode = createAliasIfNotAlreadyCreated( criteria, "stackRank.stackRankNodes", "stackRankNode" );

      Criterion inRestriction = Restrictions.in( "stackRankNode.node.id", nodeIdsIncluded );

      criteriaWithStackRankNode.add( inRestriction );
    }

    if ( userIdForNodeAndBelow != null && userIdForNodeAndBelow.longValue() != 0 )
    {
      Criterion criterion = Restrictions.sqlRestriction( "{alias}.stack_rank_id in " + "   (SELECT stack_rank_id " + "      FROM stack_rank_node  " + "     WHERE node_id IN  "
          + "     (   (SELECT node_id  " + "            FROM node  " + "           WHERE is_deleted = 0  " + "           START WITH node_id IN  " + "             (SELECT node_id   "
          + "                FROM user_node  " + "               WHERE user_id = ?  " + "                 AND role in ('" + HierarchyRoleType.MANAGER + "','" + HierarchyRoleType.OWNER + "') )  "
          + "           CONNECT BY prior node.node_id = node.parent_node_id ) ) )", userIdForNodeAndBelow, StandardBasicTypes.LONG );
      criteria.add( criterion );
    }

    return criteria;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Return the result object type - Should be overridden by subclasses.
   * 
   * @return Class
   */
  public Class getResultClass()
  {
    return StackRank.class;
  }

  /**
   * @return value of stackRankStatesIncluded property
   */
  public StackRankState[] getStackRankStatesIncluded()
  {
    return stackRankStatesIncluded;
  }

  /**
   * @param stackRankStatesIncluded value for stackRankStatesIncluded property
   */
  public void setStackRankStatesIncluded( StackRankState[] stackRankStatesIncluded )
  {
    this.stackRankStatesIncluded = stackRankStatesIncluded;
  }

  public Long[] getPromotionsIncluded()
  {
    return promotionIdsIncluded;
  }

  public void setPromotionIdsIncluded( Long[] promotionIdsIncluded )
  {
    this.promotionIdsIncluded = promotionIdsIncluded;
  }

  public Long[] getNodeIdsIncluded()
  {
    return nodeIdsIncluded;
  }

  public void setNodeIdsIncluded( Long[] nodeIdsIncluded )
  {
    this.nodeIdsIncluded = nodeIdsIncluded;
  }

  public Long[] getUserIdsIncluded()
  {
    return userIdsIncluded;
  }

  public void setUserIdsIncluded( Long[] userIdsIncluded )
  {
    this.userIdsIncluded = userIdsIncluded;
  }

  public Long getUserIdForNodeAndBelow()
  {
    return userIdForNodeAndBelow;
  }

  public void setUserIdForNodeAndBelow( Long userIdForNodeAndBelow )
  {
    this.userIdForNodeAndBelow = userIdForNodeAndBelow;
  }

}

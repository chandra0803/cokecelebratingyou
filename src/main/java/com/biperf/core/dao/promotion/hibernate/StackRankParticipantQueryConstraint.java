/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * StackRankParticipantQueryConstraint
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
 * <td>Tammy Cheng</td>
 * <td>Mar 14, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class StackRankParticipantQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * include stackRankParticipants whose associated promotionId matches any of these
   * promotionIdsIncluded.
   */
  private Long[] promotionIdsIncluded;

  /**
   * include stackRankParticipants whose associated stackRank is in a state matches any of these
   * stackRankStatesIncluded.
   */
  private StackRankState[] stackRankStatesIncluded;

  /**
   * include stackRankParticipants whose associated stackRankId matches any of these
   * stackRankIdsIncluded
   */
  private Long[] stackRankIdsIncluded;

  /**
   * include stackRankParticipants whose node id matches any of these nodeIdsIncluded
   */
  private Long[] nodeIdsIncluded;

  /**
   * include stackRankParticipants whose rank is in between the range specified by rankFromIncluded
   * and rankToIncluded
   */
  private int rankFromIncluded;
  private int rankToIncluded;

  /**
   * include stackRankParticipants whose userId matches any of these userIdsIncluded.
   */
  private Long[] userIdsIncluded;

  /**
   * exclude participants without stack rank - eliminate participants who did not submit a claim
   * but are associated in the node. Can determine this by a 0 in the stack rank factor column
   */
  private boolean excludeParticipantsWithoutStackRank;
  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.QueryConstraint#buildCriteria()
   * @return Criteria
   */
  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "stackRankParticipant" );

    // Constrain by promotion ids
    if ( promotionIdsIncluded != null && promotionIdsIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankParticipant.stackRankNode", "stackRankNode" );
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRank", "stackRank" );
      Criterion inRestriction = Restrictions.in( "stackRank.promotion.id", promotionIdsIncluded );

      criteria.add( inRestriction );

    }

    // Constrain by stack rank states
    if ( stackRankStatesIncluded != null && stackRankStatesIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankParticipant.stackRankNode", "stackRankNode" );
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRank", "stackRank" );
      Criterion inRestriction = Restrictions.in( "stackRank.state", stackRankStatesIncluded );

      criteria.add( inRestriction );
    }

    // Constrain by stack rank ids
    if ( stackRankIdsIncluded != null && stackRankIdsIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankParticipant.stackRankNode", "stackRankNode" );
      createAliasIfNotAlreadyCreated( criteria, "stackRankNode.stackRank", "stackRank" );
      Criterion inRestriction = Restrictions.in( "stackRank.id", stackRankIdsIncluded );

      criteria.add( inRestriction );
    }

    // Constrain by node ids (Node Id of the Node the Stack Rank Node contains, not the stack rank
    // node id
    if ( nodeIdsIncluded != null && nodeIdsIncluded.length > 0 )
    {
      createAliasIfNotAlreadyCreated( criteria, "stackRankParticipant.stackRankNode", "stackRankNode" );
      Criterion inRestriction = Restrictions.in( "stackRankNode.node.id", nodeIdsIncluded );

      criteria.add( inRestriction );
    }

    // Constrain by a range of ranks, e.g. to specify the top 10s, use rankFromIncluded=1 and
    // rankToIncluded=10
    if ( rankFromIncluded > 0 && rankToIncluded > 0 )
    {
      criteria.add( Restrictions.between( "stackRankParticipant.rank", new Integer( rankFromIncluded ), new Integer( rankToIncluded ) ) );
    }

    // Constrain by user id of a participant
    if ( userIdsIncluded != null && userIdsIncluded.length > 0 )
    {
      criteria.add( Restrictions.in( "stackRankParticipant.participant.id", userIdsIncluded ) );
    }

    // Constrain by a stack rank factor > 0 - eliminates those pax without
    if ( excludeParticipantsWithoutStackRank )
    {
      criteria.add( Restrictions.gt( "stackRankParticipant.stackRankFactor", new Integer( 0 ) ) );
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
    return StackRankParticipant.class;
  }

  public Long[] getPromotionIdsIncluded()
  {
    return promotionIdsIncluded;
  }

  public void setPromotionIdsIncluded( Long[] promotionIdsIncluded )
  {
    this.promotionIdsIncluded = promotionIdsIncluded;
  }

  public Long[] getUserIdsIncluded()
  {
    return userIdsIncluded;
  }

  public void setUserIdsIncluded( Long[] userIdsIncluded )
  {
    this.userIdsIncluded = userIdsIncluded;
  }

  public int getRankFromIncluded()
  {
    return rankFromIncluded;
  }

  public void setRankFromIncluded( int rankFromIncluded )
  {
    this.rankFromIncluded = rankFromIncluded;
  }

  public int getRankToIncluded()
  {
    return rankToIncluded;
  }

  public void setRankToIncluded( int rankToIncluded )
  {
    this.rankToIncluded = rankToIncluded;
  }

  public Long[] getNodeIdsIncluded()
  {
    return nodeIdsIncluded;
  }

  public void setNodeIdsIncluded( Long[] nodeIdsIncluded )
  {
    this.nodeIdsIncluded = nodeIdsIncluded;
  }

  public Long[] getStackRankIdsIncluded()
  {
    return stackRankIdsIncluded;
  }

  public void setStackRankIdsIncluded( Long[] stackRankIdsIncluded )
  {
    this.stackRankIdsIncluded = stackRankIdsIncluded;
  }

  public StackRankState[] getStackRankStatesIncluded()
  {
    return stackRankStatesIncluded;
  }

  public void setStackRankStatesIncluded( StackRankState[] stackRankStatesIncluded )
  {
    this.stackRankStatesIncluded = stackRankStatesIncluded;
  }

  public boolean isExcludeParticipantsWithoutStackRank()
  {
    return excludeParticipantsWithoutStackRank;
  }

  public void setExcludeParticipantsWithoutStackRank( boolean excludeParticipantsWithoutStackRank )
  {
    this.excludeParticipantsWithoutStackRank = excludeParticipantsWithoutStackRank;
  }

}

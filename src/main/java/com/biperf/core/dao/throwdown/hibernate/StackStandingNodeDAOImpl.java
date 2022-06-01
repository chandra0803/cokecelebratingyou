
package com.biperf.core.dao.throwdown.hibernate;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.throwdown.StackStandingNodeDAO;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class StackStandingNodeDAOImpl extends BaseDAO implements StackStandingNodeDAO
{

  @Override
  public StackStandingNode getStackStandingNode( Long stackStandingNodeId )
  {
    return (StackStandingNode)getSession().get( StackStandingNode.class, stackStandingNodeId );
  }

  @Override
  public StackStandingNode getStackStandingByNode( Long stackStandingId, Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.throwdown.getStackStandingByNode" );
    query.setLong( "stackStandingId", stackStandingId.longValue() );
    query.setLong( "nodeId", nodeId.longValue() );
    return (StackStandingNode)query.uniqueResult();
  }

  @Override
  public StackStandingNode getStackStandingByHierarchy( Long stackStandingId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.throwdown.getStackStandingByHierarchy" );
    query.setLong( "stackStandingId", stackStandingId.longValue() );
    return (StackStandingNode)query.uniqueResult();
  }

  @Override
  public StackStandingNode saveStackStandingNode( StackStandingNode stackStandingNode )
  {
    return (StackStandingNode)HibernateUtil.saveOrUpdateOrShallowMerge( stackStandingNode );
  }

}

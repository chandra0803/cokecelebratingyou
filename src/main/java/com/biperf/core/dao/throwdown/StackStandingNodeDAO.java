
package com.biperf.core.dao.throwdown;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.StackStandingNode;

public interface StackStandingNodeDAO extends DAO
{
  public static final String BEAN_NAME = "stackStandingNodeDAO";

  public StackStandingNode getStackStandingNode( Long stackStandingNodeId );

  public StackStandingNode getStackStandingByNode( Long stackStandingId, Long nodeId );

  public StackStandingNode getStackStandingByHierarchy( Long stackStandingId );

  public StackStandingNode saveStackStandingNode( StackStandingNode stackStandingNode );

}

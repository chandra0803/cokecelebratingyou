
package com.biperf.core.service.throwdown.impl;

import com.biperf.core.dao.throwdown.StackStandingNodeDAO;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.service.throwdown.StackStandingNodeService;

public class StackStandingNodeServiceImpl implements StackStandingNodeService
{
  private StackStandingNodeDAO stackStandingNodeDAO;

  @Override
  public StackStandingNode getStackStandingNode( Long stackStandingNodeId )
  {
    return stackStandingNodeDAO.getStackStandingNode( stackStandingNodeId );
  }

  @Override
  public StackStandingNode getStackStandingByNode( Long stackStandingId, Long nodeId )
  {
    return stackStandingNodeDAO.getStackStandingByNode( stackStandingId, nodeId );
  }

  @Override
  public StackStandingNode getStackStandingByHierarchy( Long stackStandingId )
  {
    return stackStandingNodeDAO.getStackStandingByHierarchy( stackStandingId );
  }

  @Override
  public StackStandingNode saveStackStandingNode( StackStandingNode stackStandingNode )
  {
    return stackStandingNodeDAO.saveStackStandingNode( stackStandingNode );
  }

  public void setStackStandingNodeDAO( StackStandingNodeDAO stackStandingNodeDAO )
  {
    this.stackStandingNodeDAO = stackStandingNodeDAO;
  }

}

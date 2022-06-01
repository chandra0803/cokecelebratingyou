
package com.biperf.core.service.throwdown;

import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.service.SAO;

public interface StackStandingNodeService extends SAO
{
  public static String BEAN_NAME = "stackStandingNodeService";

  public StackStandingNode getStackStandingNode( Long stackStandingNodeId );

  public StackStandingNode getStackStandingByNode( Long stackStandingId, Long nodeId );

  public StackStandingNode getStackStandingByHierarchy( Long stackStandingId );

  public StackStandingNode saveStackStandingNode( StackStandingNode stackStandingNode );

}

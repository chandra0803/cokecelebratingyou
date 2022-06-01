
package com.biperf.core.domain.engagement;

import com.biperf.core.domain.BaseDomain;

public class EngagementEligManager extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private Long nodeUsersCnt;

  public Long getNodeUsersCnt()
  {
    return nodeUsersCnt;
  }

  public void setNodeUsersCnt( Long nodeUsersCnt )
  {
    this.nodeUsersCnt = nodeUsersCnt;
  }

  @Override
  public boolean equals( Object object )
  {
    return false;
  }

  @Override
  public int hashCode()
  {
    return 0;
  }

}

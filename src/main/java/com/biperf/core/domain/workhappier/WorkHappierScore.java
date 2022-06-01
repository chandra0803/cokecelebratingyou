
package com.biperf.core.domain.workhappier;

import com.biperf.core.domain.BaseDomain;

public class WorkHappierScore extends BaseDomain
{
  private WorkHappier workHappier;
  private Long userId;
  private Long nodeId;
  private Long score;

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof WorkHappierScore ) )
    {
      return false;
    }

    final WorkHappierScore other = (WorkHappierScore)object;

    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 0;
    result = prime * result + ( this.getId() != null ? this.getId().hashCode() : 0 );
    result = prime * result + ( this.getScore() != null ? this.getScore().hashCode() : 0 );
    return result;
  }

  public WorkHappier getWorkHappier()
  {
    return workHappier;
  }

  public void setWorkHappier( WorkHappier workHappier )
  {
    this.workHappier = workHappier;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getScore()
  {
    return score;
  }

  public void setScore( Long score )
  {
    this.score = score;
  }

}

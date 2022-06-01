
package com.biperf.core.domain.workhappier;

import com.biperf.core.domain.BaseDomain;

public class WorkHappierFeedback extends BaseDomain
{

  private Long nodeId;
  private String comments;

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof WorkHappierFeedback ) )
    {
      return false;
    }

    final WorkHappierFeedback other = (WorkHappierFeedback)object;

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
    return result;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

}

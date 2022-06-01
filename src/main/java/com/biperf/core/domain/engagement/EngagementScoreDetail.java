
package com.biperf.core.domain.engagement;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;

public class EngagementScoreDetail extends BaseDomain
{

  private static final long serialVersionUID = 1L;

  private Long score;
  private boolean average;
  private Long receivedCount;
  private Long sentCount;
  private Long connectedToCount;
  private Long connectedFromCount;
  private Long loginActivityCount;
  private boolean recognitionRececeivedRecently;
  private Integer transMonth;
  private Integer transYear;
  private User user;
  private Node node;

  public Long getScore()
  {
    return score;
  }

  public void setScore( Long score )
  {
    this.score = score;
  }

  public boolean isAverage()
  {
    return average;
  }

  public void setAverage( boolean average )
  {
    this.average = average;
  }

  public Long getReceivedCount()
  {
    return receivedCount;
  }

  public void setReceivedCount( Long receivedCount )
  {
    this.receivedCount = receivedCount;
  }

  public Long getSentCount()
  {
    return sentCount;
  }

  public void setSentCount( Long sentCount )
  {
    this.sentCount = sentCount;
  }

  public Long getConnectedToCount()
  {
    return connectedToCount;
  }

  public void setConnectedToCount( Long connectedToCount )
  {
    this.connectedToCount = connectedToCount;
  }

  public Long getConnectedFromCount()
  {
    return connectedFromCount;
  }

  public void setConnectedFromCount( Long connectedFromCount )
  {
    this.connectedFromCount = connectedFromCount;
  }

  public Long getLoginActivityCount()
  {
    return loginActivityCount;
  }

  public void setLoginActivityCount( Long loginActivityCount )
  {
    this.loginActivityCount = loginActivityCount;
  }

  public boolean isRecognitionRececeivedRecently()
  {
    return recognitionRececeivedRecently;
  }

  public void setRecognitionRececeivedRecently( boolean recognitionRececeivedRecently )
  {
    this.recognitionRececeivedRecently = recognitionRececeivedRecently;
  }

  public Integer getTransMonth()
  {
    return transMonth;
  }

  public void setTransMonth( Integer transMonth )
  {
    this.transMonth = transMonth;
  }

  public Integer getTransYear()
  {
    return transYear;
  }

  public void setTransYear( Integer transYear )
  {
    this.transYear = transYear;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser( User user )
  {
    this.user = user;
  }

  public Node getNode()
  {
    return node;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    EngagementScoreDetail other = (EngagementScoreDetail)obj;
    if ( user == null )
    {
      if ( other.user != null )
      {
        return false;
      }
    }
    else if ( !user.equals( other.user ) )
    {
      return false;
    }
    if ( node == null )
    {
      if ( other.node != null )
      {
        return false;
      }
    }
    else if ( !node.equals( other.node ) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( user == null ? 0 : user.hashCode() );
    result = prime * result + ( node == null ? 0 : node.hashCode() );
    return result;
  }

}

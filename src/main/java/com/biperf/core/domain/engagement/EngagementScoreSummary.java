
package com.biperf.core.domain.engagement;

import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

public class EngagementScoreSummary extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private String recordType;
  private Long node;
  private Long parentNode;
  private Integer transMonth;
  private Integer transYear;
  private Long score;
  private Long totalParticipantCount;
  private Long receivedCount;
  private Long sentCount;
  private Long connectedToCount;
  private Long connectedFromCount;
  private Long loginActivityCount;
  private Set<EngagementBehaviorSummary> engagementBehaviorSummaries = new HashSet<EngagementBehaviorSummary>();

  public String getRecordType()
  {
    return recordType;
  }

  public void setRecordType( String recordType )
  {
    this.recordType = recordType;
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

  public Long getScore()
  {
    return score;
  }

  public void setScore( Long score )
  {
    this.score = score;
  }

  public Long getTotalParticipantCount()
  {
    return totalParticipantCount;
  }

  public void setTotalParticipantCount( Long totalParticipantCount )
  {
    this.totalParticipantCount = totalParticipantCount;
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

  public Long getNode()
  {
    return node;
  }

  public void setNode( Long node )
  {
    this.node = node;
  }

  public Long getParentNode()
  {
    return parentNode;
  }

  public void setParentNode( Long parentNode )
  {
    this.parentNode = parentNode;
  }

  public Set<EngagementBehaviorSummary> getEngagementBehaviorSummaries()
  {
    return engagementBehaviorSummaries;
  }

  public void setEngagementBehaviorSummaries( Set<EngagementBehaviorSummary> engagementBehaviorSummaries )
  {
    this.engagementBehaviorSummaries = engagementBehaviorSummaries;
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
    EngagementScoreSummary other = (EngagementScoreSummary)obj;
    if ( recordType == null )
    {
      if ( other.recordType != null )
      {
        return false;
      }
    }
    else if ( !recordType.equals( other.recordType ) )
    {
      return false;
    }
    if ( transMonth == null )
    {
      if ( other.transMonth != null )
      {
        return false;
      }
    }
    else if ( !transMonth.equals( other.transMonth ) )
    {
      return false;
    }
    if ( transYear == null )
    {
      if ( other.transYear != null )
      {
        return false;
      }
    }
    else if ( !transYear.equals( other.transYear ) )
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
    if ( parentNode == null )
    {
      if ( other.parentNode != null )
      {
        return false;
      }
    }
    else if ( !parentNode.equals( other.parentNode ) )
    {
      return false;
    }
    if ( engagementBehaviorSummaries == null )
    {
      if ( other.engagementBehaviorSummaries != null )
      {
        return false;
      }
    }
    else if ( !engagementBehaviorSummaries.equals( other.engagementBehaviorSummaries ) )
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
    result = prime * result + ( recordType == null ? 0 : recordType.hashCode() );
    result = prime * result + ( transMonth == null ? 0 : transMonth.hashCode() );
    result = prime * result + ( transYear == null ? 0 : transYear.hashCode() );
    result = prime * result + ( node == null ? 0 : node.hashCode() );
    result = prime * result + ( parentNode == null ? 0 : parentNode.hashCode() );
    result = prime * result + ( engagementBehaviorSummaries == null ? 0 : engagementBehaviorSummaries.hashCode() );
    return result;
  }

}

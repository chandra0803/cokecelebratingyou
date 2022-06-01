
package com.biperf.core.domain.engagement;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;

public class EngagementBehaviorSummary extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private Long receivedCount;
  private Long sentCount;
  private String behavior;
  private Promotion promotion;
  private EngagementScoreSummary engagementScoreSummary;

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

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public EngagementScoreSummary getEngagementScoreSummary()
  {
    return engagementScoreSummary;
  }

  public void setEngagementScoreSummary( EngagementScoreSummary engagementScoreSummary )
  {
    this.engagementScoreSummary = engagementScoreSummary;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
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
    EngagementBehaviorSummary other = (EngagementBehaviorSummary)obj;
    if ( engagementScoreSummary == null )
    {
      if ( other.engagementScoreSummary != null )
      {
        return false;
      }
    }
    else if ( !engagementScoreSummary.equals( other.engagementScoreSummary ) )
    {
      return false;
    }
    if ( behavior == null )
    {
      if ( other.behavior != null )
      {
        return false;
      }
    }
    else if ( !behavior.equals( other.behavior ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
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
    result = prime * result + ( engagementScoreSummary == null ? 0 : engagementScoreSummary.hashCode() );
    result = prime * result + ( behavior == null ? 0 : behavior.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    return result;
  }

}

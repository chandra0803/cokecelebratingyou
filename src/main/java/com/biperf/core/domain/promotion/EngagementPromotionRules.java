
package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

public class EngagementPromotionRules extends BaseDomain implements Cloneable
{

  private static final long serialVersionUID = 1L;

  private Promotion promotion;
  private Long receivedTarget;
  private Long sentTarget;
  private Long connectedTarget;
  private Long connectedFromTarget;
  private Long loginActivityTarget;
  private Long receivedIntTarget;
  private Long sentIntTarget;
  private Long connectedIntTarget;
  private Long connectedFromIntTarget;
  private Long loginActivityIntTarget;
  private Long receivedAdvTarget;
  private Long sentAdvTarget;
  private Long connectedAdvTarget;
  private Long connectedFromAdvTarget;
  private Long loginActivityAdvTarget;
  private Long receivedWeight;
  private Long sentWeight;
  private Long connectedWeight;
  private Long connectedFromWeight;
  private Long loginActivityWeight;

  private Set<EngagementPromotionRulesAudience> engagementPromotionRulesAudiences = new HashSet<EngagementPromotionRulesAudience>();

  public Object clone() throws CloneNotSupportedException
  {
    EngagementPromotionRules clonedEngagementPromotionRules = (EngagementPromotionRules)super.clone();
    clonedEngagementPromotionRules.setPromotion( null );
    clonedEngagementPromotionRules.resetBaseDomain();

    clonedEngagementPromotionRules.setEngagementPromotionRulesAudiences( new LinkedHashSet<EngagementPromotionRulesAudience>() );
    for ( Iterator<EngagementPromotionRulesAudience> engagementPromotionRulesAudiencesIter = this.engagementPromotionRulesAudiences.iterator(); engagementPromotionRulesAudiencesIter.hasNext(); )
    {
      EngagementPromotionRulesAudience engagementPromotionRulesAudience = (EngagementPromotionRulesAudience)engagementPromotionRulesAudiencesIter.next();
      clonedEngagementPromotionRules.addEngagementPromotionRulesAudiences( (EngagementPromotionRulesAudience)engagementPromotionRulesAudience.clone() );
    }

    return clonedEngagementPromotionRules;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Long getReceivedTarget()
  {
    return receivedTarget;
  }

  public void setReceivedTarget( Long receivedTarget )
  {
    this.receivedTarget = receivedTarget;
  }

  public Long getSentTarget()
  {
    return sentTarget;
  }

  public void setSentTarget( Long sentTarget )
  {
    this.sentTarget = sentTarget;
  }

  public Long getConnectedTarget()
  {
    return connectedTarget;
  }

  public void setConnectedTarget( Long connectedTarget )
  {
    this.connectedTarget = connectedTarget;
  }

  public Long getConnectedFromTarget()
  {
    return connectedFromTarget;
  }

  public void setConnectedFromTarget( Long connectedFromTarget )
  {
    this.connectedFromTarget = connectedFromTarget;
  }

  public Long getLoginActivityTarget()
  {
    return loginActivityTarget;
  }

  public void setLoginActivityTarget( Long loginActivityTarget )
  {
    this.loginActivityTarget = loginActivityTarget;
  }

  public Long getReceivedIntTarget()
  {
    return receivedIntTarget;
  }

  public void setReceivedIntTarget( Long receivedIntTarget )
  {
    this.receivedIntTarget = receivedIntTarget;
  }

  public Long getSentIntTarget()
  {
    return sentIntTarget;
  }

  public void setSentIntTarget( Long sentIntTarget )
  {
    this.sentIntTarget = sentIntTarget;
  }

  public Long getConnectedIntTarget()
  {
    return connectedIntTarget;
  }

  public void setConnectedIntTarget( Long connectedIntTarget )
  {
    this.connectedIntTarget = connectedIntTarget;
  }

  public Long getConnectedFromIntTarget()
  {
    return connectedFromIntTarget;
  }

  public void setConnectedFromIntTarget( Long connectedFromIntTarget )
  {
    this.connectedFromIntTarget = connectedFromIntTarget;
  }

  public Long getLoginActivityIntTarget()
  {
    return loginActivityIntTarget;
  }

  public void setLoginActivityIntTarget( Long loginActivityIntTarget )
  {
    this.loginActivityIntTarget = loginActivityIntTarget;
  }

  public Long getReceivedAdvTarget()
  {
    return receivedAdvTarget;
  }

  public void setReceivedAdvTarget( Long receivedAdvTarget )
  {
    this.receivedAdvTarget = receivedAdvTarget;
  }

  public Long getSentAdvTarget()
  {
    return sentAdvTarget;
  }

  public void setSentAdvTarget( Long sentAdvTarget )
  {
    this.sentAdvTarget = sentAdvTarget;
  }

  public Long getConnectedAdvTarget()
  {
    return connectedAdvTarget;
  }

  public void setConnectedAdvTarget( Long connectedAdvTarget )
  {
    this.connectedAdvTarget = connectedAdvTarget;
  }

  public Long getConnectedFromAdvTarget()
  {
    return connectedFromAdvTarget;
  }

  public void setConnectedFromAdvTarget( Long connectedFromAdvTarget )
  {
    this.connectedFromAdvTarget = connectedFromAdvTarget;
  }

  public Long getLoginActivityAdvTarget()
  {
    return loginActivityAdvTarget;
  }

  public void setLoginActivityAdvTarget( Long loginActivityAdvTarget )
  {
    this.loginActivityAdvTarget = loginActivityAdvTarget;
  }

  public Long getReceivedWeight()
  {
    return receivedWeight;
  }

  public void setReceivedWeight( Long receivedWeight )
  {
    this.receivedWeight = receivedWeight;
  }

  public Long getSentWeight()
  {
    return sentWeight;
  }

  public void setSentWeight( Long sentWeight )
  {
    this.sentWeight = sentWeight;
  }

  public Long getConnectedWeight()
  {
    return connectedWeight;
  }

  public void setConnectedWeight( Long connectedWeight )
  {
    this.connectedWeight = connectedWeight;
  }

  public Long getConnectedFromWeight()
  {
    return connectedFromWeight;
  }

  public void setConnectedFromWeight( Long connectedFromWeight )
  {
    this.connectedFromWeight = connectedFromWeight;
  }

  public Long getLoginActivityWeight()
  {
    return loginActivityWeight;
  }

  public void setLoginActivityWeight( Long loginActivityWeight )
  {
    this.loginActivityWeight = loginActivityWeight;
  }

  public Set<EngagementPromotionRulesAudience> getEngagementPromotionRulesAudiences()
  {
    return engagementPromotionRulesAudiences;
  }

  public void setEngagementPromotionRulesAudiences( Set<EngagementPromotionRulesAudience> engagementPromotionRulesAudiences )
  {
    this.engagementPromotionRulesAudiences = engagementPromotionRulesAudiences;
  }

  public void addEngagementPromotionRulesAudiences( EngagementPromotionRulesAudience engagementPromotionRulesAudience )
  {
    engagementPromotionRulesAudience.setEnagementPromotionRules( this );
    this.engagementPromotionRulesAudiences.add( engagementPromotionRulesAudience );
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof EngagementPromotionRules ) )
    {
      return false;
    }

    final EngagementPromotionRules engagementPromotionRules = (EngagementPromotionRules)object;

    if ( getPromotion() != null ? !getPromotion().equals( engagementPromotionRules.getPromotion() ) : engagementPromotionRules.getPromotion() != null )
    {
      return false;
    }

    if ( getEngagementPromotionRulesAudiences() != null
        ? !getEngagementPromotionRulesAudiences().equals( engagementPromotionRules.getEngagementPromotionRulesAudiences() )
        : engagementPromotionRules.getEngagementPromotionRulesAudiences() != null )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getPromotion() != null ? getPromotion().hashCode() : 0;
    result = 31 * result + ( getEngagementPromotionRulesAudiences() != null ? getEngagementPromotionRulesAudiences().hashCode() : 0 );

    return result;
  }

}

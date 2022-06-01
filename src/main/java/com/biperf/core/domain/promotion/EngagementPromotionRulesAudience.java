
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Audience;

public class EngagementPromotionRulesAudience extends BaseDomain implements Cloneable
{
  private static final long serialVersionUID = 1L;

  private Audience audience;
  private EngagementPromotionRules enagementPromotionRules;
  private Promotion eligiblePromotion;
  private SecondaryAudienceType audienceType;

  public Audience getAudience()
  {
    return audience;
  }

  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  public EngagementPromotionRules getEnagementPromotionRules()
  {
    return enagementPromotionRules;
  }

  public void setEnagementPromotionRules( EngagementPromotionRules enagementPromotionRules )
  {
    this.enagementPromotionRules = enagementPromotionRules;
  }

  public Promotion getEligiblePromotion()
  {
    return eligiblePromotion;
  }

  public void setEligiblePromotion( Promotion eligiblePromotion )
  {
    this.eligiblePromotion = eligiblePromotion;
  }

  public SecondaryAudienceType getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( SecondaryAudienceType audienceType )
  {
    this.audienceType = audienceType;
  }

  public Object clone() throws CloneNotSupportedException
  {
    EngagementPromotionRulesAudience engagementPromotionRulesAudience = (EngagementPromotionRulesAudience)super.clone();
    engagementPromotionRulesAudience.resetBaseDomain();
    return engagementPromotionRulesAudience;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( super.getId() == null ? 0 : super.getId().hashCode() );
    result = prime * result + ( getAudience() == null ? 0 : getAudience().hashCode() );
    result = prime * result + ( getEligiblePromotion() == null ? 0 : getEligiblePromotion().hashCode() );
    result = prime * result + ( getAudienceType() == null ? 0 : getAudienceType().hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    final EngagementPromotionRulesAudience engagementPromotionRulesAudience = (EngagementPromotionRulesAudience)object;

    if ( getId() != null ? !getId().equals( engagementPromotionRulesAudience.getId() ) : engagementPromotionRulesAudience.getId() != null )
    {
      return false;
    }
    if ( getAudience() != null ? !getAudience().equals( engagementPromotionRulesAudience.getAudience() ) : engagementPromotionRulesAudience.getAudience() != null )
    {
      return false;
    }
    if ( getEligiblePromotion() != null ? !getEligiblePromotion().equals( engagementPromotionRulesAudience.getEligiblePromotion() ) : engagementPromotionRulesAudience.getEligiblePromotion() != null )
    {
      return false;
    }
    if ( getAudienceType() != null ? !getAudienceType().equals( engagementPromotionRulesAudience.getAudienceType() ) : engagementPromotionRulesAudience.getAudienceType() != null )
    {
      return false;
    }
    return true;

  }
}

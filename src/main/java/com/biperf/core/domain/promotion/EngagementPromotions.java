
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

public class EngagementPromotions extends BaseDomain implements Cloneable
{

  private static final long serialVersionUID = 1L;

  private Promotion promotion;
  private Promotion eligiblePromotion;

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Promotion getEligiblePromotion()
  {
    return eligiblePromotion;
  }

  public void setEligiblePromotion( Promotion eligiblePromotion )
  {
    this.eligiblePromotion = eligiblePromotion;
  }

  public Object clone() throws CloneNotSupportedException
  {
    EngagementPromotions engagementPromotions = (EngagementPromotions)super.clone();
    engagementPromotions.setPromotion( null );
    engagementPromotions.resetBaseDomain();
    return engagementPromotions;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof EngagementPromotions ) )
    {
      return false;
    }

    final EngagementPromotions engagementPromotions = (EngagementPromotions)object;

    if ( getPromotion() != null ? !getPromotion().equals( engagementPromotions.getPromotion() ) : engagementPromotions.getPromotion() != null )
    {
      return false;
    }
    if ( getEligiblePromotion() != null ? !getEligiblePromotion().equals( engagementPromotions.getEligiblePromotion() ) : engagementPromotions.getEligiblePromotion() != null )
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
    result = 29 * result + ( getEligiblePromotion() != null ? getEligiblePromotion().hashCode() : 0 );
    return result;
  }

}

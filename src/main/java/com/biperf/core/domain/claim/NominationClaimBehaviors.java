
package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.PromotionBehaviorType;

public class NominationClaimBehaviors extends BaseDomain implements Cloneable
{
  private NominationClaim nominationClaim;
  private PromotionBehaviorType behavior;

  public NominationClaimBehaviors()
  {

  }

  public NominationClaimBehaviors( PromotionBehaviorType behavior )
  {
    this.behavior = behavior;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof NominationClaimBehaviors ) )
    {
      return false;
    }

    final NominationClaimBehaviors other = (NominationClaimBehaviors)object;

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

    if ( getBehavior() == null )
    {
      if ( other.getBehavior() != null )
      {
        return false;
      }
    }
    else if ( !getBehavior().equals( other.getBehavior() ) )
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
    result = prime * result + ( this.getBehavior() != null ? this.getBehavior().hashCode() : 0 );
    return result;
  }

  public NominationClaim getNominationClaim()
  {
    return nominationClaim;
  }

  public void setNominationClaim( NominationClaim nominationClaim )
  {
    this.nominationClaim = nominationClaim;
  }

  public PromotionBehaviorType getBehavior()
  {
    return behavior;
  }

  public void setBehavior( PromotionBehaviorType behavior )
  {
    this.behavior = behavior;
  }
}

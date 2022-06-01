
package com.biperf.core.value;

import java.util.Date;

import com.biperf.core.domain.promotion.ThrowdownPromotion;

public class PromotionRoundValue
{
  private ThrowdownPromotion promotion;
  private int roundNumber;
  private Date roundStartDate;
  private Date roundEndDate;

  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  public int getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( int roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public Date getRoundStartDate()
  {
    return roundStartDate;
  }

  public void setRoundStartDate( Date roundStartDate )
  {
    this.roundStartDate = roundStartDate;
  }

  public Date getRoundEndDate()
  {
    return roundEndDate;
  }

  public void setRoundEndDate( Date roundEndDate )
  {
    this.roundEndDate = roundEndDate;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof PromotionRoundValue ) )
    {
      return false;
    }

    final PromotionRoundValue promotionRoundValue = (PromotionRoundValue)object;

    if ( getPromotion() != null ? !getPromotion().equals( promotionRoundValue.getPromotion() ) : promotionRoundValue.getPromotion() != null )
    {
      return false;
    }

    if ( getRoundNumber() != promotionRoundValue.getRoundNumber() )
    {
      return false;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    int result;
    result = getPromotion() != null ? getPromotion().hashCode() : 0;
    result = 31 * result + getRoundNumber();

    return result;
  }

}

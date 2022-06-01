
package com.biperf.core.service.promotion.impl;

import java.util.Map;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.promotion.WinnersListStrategyFactory;
import com.biperf.core.strategy.WinnersListStrategy;

/**
 * WinnersListStrategyFactoryImpl
 * 
 *
 */
public class WinnersListStrategyFactoryImpl implements WinnersListStrategyFactory
{
  private Map entries;

  /**
   * Returns a winner strategy given a promotion Overridden from
   * 
   * @see com.biperf.core.service.promotion.WinnersListStrategyFactory#getWinnersListStrategy(com.biperf.core.domain.promotion.Promotion)
   * @param promotion
   * @return WinnersListStrategy
   */
  public WinnersListStrategy getWinnersListStrategy( Promotion promotion )
  {
    WinnersListStrategy winnersListStrategy = null;

    if ( promotion.isNominationPromotion() )
    {
      winnersListStrategy = (WinnersListStrategy)entries.get( "recognition" );
    }
    else if ( promotion.isProductClaimPromotion() )
    {
      winnersListStrategy = (WinnersListStrategy)entries.get( "product_claim" );
    }
    else if ( promotion.isQuizPromotion() )
    {
      winnersListStrategy = (WinnersListStrategy)entries.get( "quiz" );
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      winnersListStrategy = (WinnersListStrategy)entries.get( "recognition" );
    }
    else if ( promotion.isSurveyPromotion() )
    {
      winnersListStrategy = (WinnersListStrategy)entries.get( "survey" );
    }

    else if ( promotion.isBadgePromotion() )
    {
      winnersListStrategy = (WinnersListStrategy)entries.get( "badge" );
    }

    return winnersListStrategy;
  }

  /**
   * @return value of entries property
   */
  public Map getEntries()
  {
    return entries;
  }

  /**
   * @param entries value for entries property
   */
  public void setEntries( Map entries )
  {
    this.entries = entries;
  }

}

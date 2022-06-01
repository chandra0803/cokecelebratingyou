
package com.biperf.core.service.promotion;

import java.util.Map;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.strategy.WinnersListStrategy;

public interface WinnersListStrategyFactory
{
  public WinnersListStrategy getWinnersListStrategy( Promotion promotion );

  /**
   * @return value of entries property
   */
  public Map getEntries();

  /**
   * @param entries value for entries property
   */
  public void setEntries( Map entries );

}

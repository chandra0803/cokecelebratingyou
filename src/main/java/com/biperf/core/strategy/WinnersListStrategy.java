
package com.biperf.core.strategy;

import java.util.List;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;

public interface WinnersListStrategy
{

  /**
   * Creates a list of winners for a specified promotion for the given sweepstake. Only participants
   * who were/are qualified during the specified timeframe. The promoSweepstake must contain a begin
   * and end date.
   * 
   * @param promotion
   * @param promoSweepstake
   * @param giverWinnerReplacementTotal
   * @param receiverWinnerReplacementTotal
   * @return List
   */
  public List buildWinnersList( Promotion promotion, PromotionSweepstake promoSweepstake, int giverWinnerReplacementTotal, int receiverWinnerReplacementTotal );

}

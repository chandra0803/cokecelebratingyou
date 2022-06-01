
package com.biperf.core.domain.promotion;

import com.biperf.core.service.throwdown.StackRankingCalculationResult;
import com.biperf.core.service.throwdown.ThrowdownRoundCalculationResult;

public class ThrowdownAwardRunCalculationView
{

  private ThrowdownRoundCalculationResult throwdownRoundCalculationResult = null;
  private StackRankingCalculationResult stackRankingCalculationResult = null;
  private String promotionUpperCaseName;

  public ThrowdownRoundCalculationResult getThrowdownRoundCalculationResult()
  {
    return throwdownRoundCalculationResult;
  }

  public void setThrowdownRoundCalculationResult( ThrowdownRoundCalculationResult throwdownRoundCalculationResult )
  {
    this.throwdownRoundCalculationResult = throwdownRoundCalculationResult;
  }

  public StackRankingCalculationResult getStackRankingCalculationResult()
  {
    return stackRankingCalculationResult;
  }

  public void setStackRankingCalculationResult( StackRankingCalculationResult stackRankingCalculationResult )
  {
    this.stackRankingCalculationResult = stackRankingCalculationResult;
  }

  public void setPromotionUpperCaseName( String promotionUpperCaseName )
  {
    this.promotionUpperCaseName = promotionUpperCaseName;
  }

  public String getPromotionUpperCaseName()
  {
    return promotionUpperCaseName;
  }

}

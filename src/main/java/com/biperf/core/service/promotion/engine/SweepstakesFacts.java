/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;

/**
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>Oct 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepstakesFacts implements PayoutCalculationFacts
{
  PromotionSweepstakeWinner promotionSweepstakeWinner;

  public SweepstakesFacts()
  {
    super();
  }

  public SweepstakesFacts( PromotionSweepstakeWinner promotionSweepstakeWinner )
  {
    this.promotionSweepstakeWinner = promotionSweepstakeWinner;
  }

  /**
   * @return value of promotionSweepstakeWinner property
   */
  public PromotionSweepstakeWinner getPromotionSweepstakeWinner()
  {
    return promotionSweepstakeWinner;
  }

  /**
   * @param promotionSweepstakeWinner value for promotionSweepstakeWinner property
   */
  public void setPromotionSweepstakeWinner( PromotionSweepstakeWinner promotionSweepstakeWinner )
  {
    this.promotionSweepstakeWinner = promotionSweepstakeWinner;
  }

}

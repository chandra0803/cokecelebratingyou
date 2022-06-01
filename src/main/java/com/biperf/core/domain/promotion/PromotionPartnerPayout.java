/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/promotion/PromotionPartnerPayout.java,v $
 */

package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

/**
 * PromotionPartnerPayout.
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
 * <td>reddy</td>
 * <td>Feb 26, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PromotionPartnerPayout extends AbstractGoalLevel
{
  private BigDecimal partnerAwardAmount;

  public BigDecimal getPartnerAwardAmount()
  {
    return partnerAwardAmount;
  }

  public void setPartnerAwardAmount( BigDecimal partnerAwardAmount )
  {
    this.partnerAwardAmount = partnerAwardAmount;
  }

  public boolean equals( Object object )
  {
    if ( ! ( object instanceof PromotionPartnerPayout ) )
    {
      return false;
    }
    PromotionPartnerPayout otherPromoPartnerPayout = (PromotionPartnerPayout)object;
    return super.equals( otherPromoPartnerPayout );
  }

  public boolean isManagerOverrideGoalLevel()
  {
    return false;
  }

  public boolean isPromotionPartnerPayout()
  {
    return true;
  }

}

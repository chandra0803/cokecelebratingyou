/**
 * 
 */

package com.biperf.core.value;

import java.util.Date;

public class EngagementPromotionData
{
  private Long promotionId;
  private Date tileDisplayStartDate;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Date getTileDisplayStartDate()
  {
    return tileDisplayStartDate;
  }

  public void setTileDisplayStartDate( Date tileDisplayStartDate )
  {
    this.tileDisplayStartDate = tileDisplayStartDate;
  }

}

/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

/**
 * 
 * @author poddutur
 * @since Apr 1, 2016
 */
public class NominationInProgressModuleBean
{
  private Long promotionId;
  private Long claimId;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }
}

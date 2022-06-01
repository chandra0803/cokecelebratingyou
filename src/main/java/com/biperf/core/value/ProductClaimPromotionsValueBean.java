/**
 * 
 */

package com.biperf.core.value;

import java.util.Date;

/**
 * @author poddutur
 *
 */
public class ProductClaimPromotionsValueBean
{
  private Long promotionId;
  private String promotionName;
  private Date promotionStartDate;
  private Date promotionEndDate;
  private Long claimId;
  private int numberSubmitted;
  private int numberOfApprovals;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getPromotionStartDate()
  {
    return promotionStartDate;
  }

  public void setPromotionStartDate( Date promotionStartDate )
  {
    this.promotionStartDate = promotionStartDate;
  }

  public Date getPromotionEndDate()
  {
    return promotionEndDate;
  }

  public void setPromotionEndDate( Date promotionEndDate )
  {
    this.promotionEndDate = promotionEndDate;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public int getNumberSubmitted()
  {
    return numberSubmitted;
  }

  public void setNumberSubmitted( int numberSubmitted )
  {
    this.numberSubmitted = numberSubmitted;
  }

  public int getNumberOfApprovals()
  {
    return numberOfApprovals;
  }

  public void setNumberOfApprovals( int numberOfApprovals )
  {
    this.numberOfApprovals = numberOfApprovals;
  }

}

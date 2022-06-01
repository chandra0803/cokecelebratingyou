
package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;

public class PostProcessPayoutCalculation extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private PostProcessJobs postProcessJobs;
  private Long journalId;
  private Long promotionPayoutGroupId;
  private Long minimumQualifierStatusId;
  private Long payoutCalculationAuditId;
  private Long promoMerchProgramLevelId;
  private Long calculatedPayout;
  private BigDecimal calculatedCashPayout;

  public Long getJournalId()
  {
    return journalId;
  }

  public void setJournalId( Long journalId )
  {
    this.journalId = journalId;
  }

  public Long getPromotionPayoutGroupId()
  {
    return promotionPayoutGroupId;
  }

  public void setPromotionPayoutGroupId( Long promotionPayoutGroupId )
  {
    this.promotionPayoutGroupId = promotionPayoutGroupId;
  }

  public Long getMinimumQualifierStatusId()
  {
    return minimumQualifierStatusId;
  }

  public void setMinimumQualifierStatusId( Long minimumQualifierStatusId )
  {
    this.minimumQualifierStatusId = minimumQualifierStatusId;
  }

  public Long getPayoutCalculationAuditId()
  {
    return payoutCalculationAuditId;
  }

  public void setPayoutCalculationAuditId( Long payoutCalculationAuditId )
  {
    this.payoutCalculationAuditId = payoutCalculationAuditId;
  }

  public Long getPromoMerchProgramLevelId()
  {
    return promoMerchProgramLevelId;
  }

  public void setPromoMerchProgramLevelId( Long promoMerchProgramLevelId )
  {
    this.promoMerchProgramLevelId = promoMerchProgramLevelId;
  }

  public Long getCalculatedPayout()
  {
    return calculatedPayout;
  }

  public void setCalculatedPayout( Long calculatedPayout )
  {
    this.calculatedPayout = calculatedPayout;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    PostProcessPayoutCalculation other = (PostProcessPayoutCalculation)obj;
    if ( calculatedCashPayout == null )
    {
      if ( other.calculatedCashPayout != null )
      {
        return false;
      }
    }
    else if ( !calculatedCashPayout.equals( other.calculatedCashPayout ) )
    {
      return false;
    }
    if ( calculatedPayout == null )
    {
      if ( other.calculatedPayout != null )
      {
        return false;
      }
    }
    else if ( !calculatedPayout.equals( other.calculatedPayout ) )
    {
      return false;
    }
    if ( journalId == null )
    {
      if ( other.journalId != null )
      {
        return false;
      }
    }
    else if ( !journalId.equals( other.journalId ) )
    {
      return false;
    }
    if ( minimumQualifierStatusId == null )
    {
      if ( other.minimumQualifierStatusId != null )
      {
        return false;
      }
    }
    else if ( !minimumQualifierStatusId.equals( other.minimumQualifierStatusId ) )
    {
      return false;
    }
    if ( payoutCalculationAuditId == null )
    {
      if ( other.payoutCalculationAuditId != null )
      {
        return false;
      }
    }
    else if ( !payoutCalculationAuditId.equals( other.payoutCalculationAuditId ) )
    {
      return false;
    }
    if ( postProcessJobs == null )
    {
      if ( other.postProcessJobs != null )
      {
        return false;
      }
    }
    else if ( !postProcessJobs.equals( other.postProcessJobs ) )
    {
      return false;
    }
    if ( promoMerchProgramLevelId == null )
    {
      if ( other.promoMerchProgramLevelId != null )
      {
        return false;
      }
    }
    else if ( !promoMerchProgramLevelId.equals( other.promoMerchProgramLevelId ) )
    {
      return false;
    }
    if ( promotionPayoutGroupId == null )
    {
      if ( other.promotionPayoutGroupId != null )
      {
        return false;
      }
    }
    else if ( !promotionPayoutGroupId.equals( other.promotionPayoutGroupId ) )
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( calculatedCashPayout == null ? 0 : calculatedCashPayout.hashCode() );
    result = prime * result + ( calculatedPayout == null ? 0 : calculatedPayout.hashCode() );
    result = prime * result + ( journalId == null ? 0 : journalId.hashCode() );
    result = prime * result + ( minimumQualifierStatusId == null ? 0 : minimumQualifierStatusId.hashCode() );
    result = prime * result + ( payoutCalculationAuditId == null ? 0 : payoutCalculationAuditId.hashCode() );
    result = prime * result + ( postProcessJobs == null ? 0 : postProcessJobs.hashCode() );
    result = prime * result + ( promoMerchProgramLevelId == null ? 0 : promoMerchProgramLevelId.hashCode() );
    result = prime * result + ( promotionPayoutGroupId == null ? 0 : promotionPayoutGroupId.hashCode() );
    return result;
  }

  public BigDecimal getCalculatedCashPayout()
  {
    return calculatedCashPayout;
  }

  public void setCalculatedCashPayout( BigDecimal calculatedCashPayout )
  {
    this.calculatedCashPayout = calculatedCashPayout;
  }

  public PostProcessJobs getPostProcessJobs()
  {
    return postProcessJobs;
  }

  public void setPostProcessJobs( PostProcessJobs postProcessJobs )
  {
    this.postProcessJobs = postProcessJobs;
  }

  @Override
  public String toString()
  {
    return "PostProcessPayoutCalculation [postProcessJobs=" + postProcessJobs + ", journalId=" + journalId + ", promotionPayoutGroupId=" + promotionPayoutGroupId + ", minimumQualifierStatusId="
        + minimumQualifierStatusId + ", payoutCalculationAuditId=" + payoutCalculationAuditId + ", promoMerchProgramLevelId=" + promoMerchProgramLevelId + ", calculatedPayout=" + calculatedPayout
        + ", calculatedCashPayout=" + calculatedCashPayout + "]";
  }

}

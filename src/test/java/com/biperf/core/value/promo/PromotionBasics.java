
package com.biperf.core.value.promo;

import java.util.Date;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;

public class PromotionBasics
{

  private String promoName;
  private ClaimForm claimForm;
  private Date submissionStartDate;
  private Date submissionEndDate;
  private PromotionAwardsType awardType;
  private PromotionType promoType;
  private PromotionStatusType statusType;
  private ApprovalConditionalAmmountOperatorType approvalConditionalAmountOperator;
  private String promoNameAssetCode = "testAssetCode";
  private NominationAwardGroupType awardGroupType;
  private NominationEvaluationType evaluationType;
  private boolean selfNomination;

  private String shortName;

  public String getPromoName()
  {
    return promoName;
  }

  public void setPromoName( String pomoName )
  {
    this.promoName = pomoName;
  }

  public ClaimForm getClaimForm()
  {
    return claimForm;
  }

  public void setClaimForm( ClaimForm claimForm )
  {
    this.claimForm = claimForm;
  }

  public Date getSubmissionStartDate()
  {
    return submissionStartDate;
  }

  public void setSubmissionStartDate( Date submissionStartDate )
  {
    this.submissionStartDate = submissionStartDate;
  }

  public Date getSubmissionEndDate()
  {
    return submissionEndDate;
  }

  public void setSubmissionEndDate( Date submissionEndDate )
  {
    this.submissionEndDate = submissionEndDate;
  }

  public PromotionAwardsType getAwardType()
  {
    return awardType;
  }

  public void setAwardType( PromotionAwardsType awardType )
  {
    this.awardType = awardType;
  }

  public PromotionType getPromoType()
  {
    return promoType;
  }

  public void setPromoType( PromotionType promoType )
  {
    this.promoType = promoType;
  }

  public PromotionStatusType getStatusType()
  {
    return statusType;
  }

  public void setStatusType( PromotionStatusType statusType )
  {
    this.statusType = statusType;
  }

  public ApprovalConditionalAmmountOperatorType getApprovalConditionalAmountOperator()
  {
    return approvalConditionalAmountOperator;
  }

  public void setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType approvalConditionalAmountOperator )
  {
    this.approvalConditionalAmountOperator = approvalConditionalAmountOperator;
  }

  public String getPromoNameAssetCode()
  {
    return promoNameAssetCode;
  }

  public void setPromoNameAssetCode( String promoNameAssetCode )
  {
    this.promoNameAssetCode = promoNameAssetCode;
  }

  public NominationAwardGroupType getAwardGroupType()
  {
    return awardGroupType;
  }

  public void setAwardGroupType( NominationAwardGroupType awardGroupType )
  {
    this.awardGroupType = awardGroupType;
  }

  public NominationEvaluationType getEvaluationType()
  {
    return evaluationType;
  }

  public void setEvaluationType( NominationEvaluationType evaluationType )
  {
    this.evaluationType = evaluationType;
  }

  public boolean isSelfNomination()
  {
    return selfNomination;
  }

  public void setSelfNomination( boolean selfNomination )
  {
    this.selfNomination = selfNomination;
  }

  public String getShortName()
  {
    return shortName;
  }

  public void setShortName( String shortName )
  {
    this.shortName = shortName;
  }

}

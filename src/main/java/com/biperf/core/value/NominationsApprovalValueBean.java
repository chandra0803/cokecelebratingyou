/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.Date;

/**
 * 
 * @author poddutur
 * @since Mar 1, 2016
 */
public class NominationsApprovalValueBean
{
  private Long claimId;
  private Long promotionId;
  private String promotionName;
  private Date dateSubmitted;
  private int approvalLevel;
  private int totalRecords;
  private String levelLabel;
  private String payoutLevelType;
  private Date submissionStartDate;
  private Date submissionEndDate;
  private boolean multipleLevel;
  private boolean finalLevel;
  private int totalPromoRecords;
  private String status;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

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

  public int getApprovalLevel()
  {
    return approvalLevel;
  }

  public void setApprovalLevel( int approvalLevel )
  {
    this.approvalLevel = approvalLevel;
  }

  public int getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( int totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String getLevelLabel()
  {
    return levelLabel;
  }

  public void setLevelLabel( String levelLabel )
  {
    this.levelLabel = levelLabel;
  }

  public String getPayoutLevelType()
  {
    return payoutLevelType;
  }

  public void setPayoutLevelType( String payoutLevelType )
  {
    this.payoutLevelType = payoutLevelType;
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

  public boolean isMultipleLevel()
  {
    return multipleLevel;
  }

  public void setMultipleLevel( boolean multipleLevel )
  {
    this.multipleLevel = multipleLevel;
  }

  public boolean isFinalLevel()
  {
    return finalLevel;
  }

  public void setFinalLevel( boolean finalLevel )
  {
    this.finalLevel = finalLevel;
  }

  public int getTotalPromoRecords()
  {
    return totalPromoRecords;
  }

  public void setTotalPromoRecords( int totalPromoRecords )
  {
    this.totalPromoRecords = totalPromoRecords;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }
}

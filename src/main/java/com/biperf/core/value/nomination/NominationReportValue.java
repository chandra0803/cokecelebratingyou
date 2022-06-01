
package com.biperf.core.value.nomination;

import java.util.Date;

public class NominationReportValue
{
  private Long giverUserId;
  private Long recvrUserId;
  private String nominator;
  private String giverCountryName;
  private String giverNodeName;
  private Long submittedCnt;
  private Long nominatedCnt;
  private Long pointsIssued;
  private Long pointsReceived;
  private Long cashIssued;
  private Long cashReceived;
  private Long otherQytIssued;
  private Long otherQytReceived;
  private Long otherAmtIssued;
  private Long otherAmtReceived;
  private String promotionId;

  private Long totalRecords;
  private Long recseq;

  private Long claimId;
  private Date dateSubmitted;
  private String teamName;
  private String nominee;
  private String recvrCountryName;
  private String recvrNodeName;
  private String promotionName;
  private String claimApprovalStatus;
  private Long pointsAwardAmt;
  private Long cashAwardAmt;
  private String otherAwardDesc;
  private Long otherAwardAmt;

  public NominationReportValue()
  {
    super();
  }

  public Long getGiverUserId()
  {
    return giverUserId;
  }

  public void setGiverUserId( Long giverUserId )
  {
    this.giverUserId = giverUserId;
  }

  public String getNominator()
  {
    return nominator;
  }

  public void setNominator( String nominator )
  {
    this.nominator = nominator;
  }

  public String getGiverCountryName()
  {
    return giverCountryName;
  }

  public void setGiverCountryName( String giverCountryName )
  {
    this.giverCountryName = giverCountryName;
  }

  public String getGiverNodeName()
  {
    return giverNodeName;
  }

  public void setGiverNodeName( String giverNodeName )
  {
    this.giverNodeName = giverNodeName;
  }

  public Long getSubmittedCnt()
  {
    return submittedCnt;
  }

  public void setSubmittedCnt( Long submittedCnt )
  {
    this.submittedCnt = submittedCnt;
  }

  public Long getPointsIssued()
  {
    return pointsIssued;
  }

  public void setPointsIssued( Long pointsIssued )
  {
    this.pointsIssued = pointsIssued;
  }

  public Long getCashIssued()
  {
    return cashIssued;
  }

  public void setCashIssued( Long cashIssued )
  {
    this.cashIssued = cashIssued;
  }

  public Long getOtherQytIssued()
  {
    return otherQytIssued;
  }

  public void setOtherQytIssued( Long otherQytIssued )
  {
    this.otherQytIssued = otherQytIssued;
  }

  public Long getOtherAmtIssued()
  {
    return otherAmtIssued;
  }

  public void setOtherAmtIssued( Long otherAmtIssued )
  {
    this.otherAmtIssued = otherAmtIssued;
  }

  public Long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public Long getRecseq()
  {
    return recseq;
  }

  public void setRecseq( Long recseq )
  {
    this.recseq = recseq;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getNominee()
  {
    return nominee;
  }

  public void setNominee( String nominee )
  {
    this.nominee = nominee;
  }

  public String getRecvrCountryName()
  {
    return recvrCountryName;
  }

  public void setRecvrCountryName( String recvrCountryName )
  {
    this.recvrCountryName = recvrCountryName;
  }

  public String getRecvrNodeName()
  {
    return recvrNodeName;
  }

  public void setRecvrNodeName( String recvrNodeName )
  {
    this.recvrNodeName = recvrNodeName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getClaimApprovalStatus()
  {
    return claimApprovalStatus;
  }

  public void setClaimApprovalStatus( String claimApprovalStatus )
  {
    this.claimApprovalStatus = claimApprovalStatus;
  }

  public Long getPointsAwardAmt()
  {
    return pointsAwardAmt;
  }

  public void setPointsAwardAmt( Long pointsAwardAmt )
  {
    this.pointsAwardAmt = pointsAwardAmt;
  }

  public Long getCashAwardAmt()
  {
    return cashAwardAmt;
  }

  public void setCashAwardAmt( Long cashAwardAmt )
  {
    this.cashAwardAmt = cashAwardAmt;
  }

  public String getOtherAwardDesc()
  {
    return otherAwardDesc;
  }

  public void setOtherAwardDesc( String otherAwardDesc )
  {
    this.otherAwardDesc = otherAwardDesc;
  }

  public Long getOtherAwardAmt()
  {
    return otherAwardAmt;
  }

  public void setOtherAwardAmt( Long otherAwardAmt )
  {
    this.otherAwardAmt = otherAwardAmt;
  }

  public Long getRecvrUserId()
  {
    return recvrUserId;
  }

  public void setRecvrUserId( Long recvrUserId )
  {
    this.recvrUserId = recvrUserId;
  }

  public Long getPointsReceived()
  {
    return pointsReceived;
  }

  public void setPointsReceived( Long pointsReceived )
  {
    this.pointsReceived = pointsReceived;
  }

  public Long getCashReceived()
  {
    return cashReceived;
  }

  public void setCashReceived( Long cashReceived )
  {
    this.cashReceived = cashReceived;
  }

  public Long getOtherQytReceived()
  {
    return otherQytReceived;
  }

  public void setOtherQytReceived( Long otherQytReceived )
  {
    this.otherQytReceived = otherQytReceived;
  }

  public Long getOtherAmtReceived()
  {
    return otherAmtReceived;
  }

  public void setOtherAmtReceived( Long otherAmtReceived )
  {
    this.otherAmtReceived = otherAmtReceived;
  }

  public Long getNominatedCnt()
  {
    return nominatedCnt;
  }

  public void setNominatedCnt( Long nominatedCnt )
  {
    this.nominatedCnt = nominatedCnt;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

}

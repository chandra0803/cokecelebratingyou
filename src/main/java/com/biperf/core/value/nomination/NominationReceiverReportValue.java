
package com.biperf.core.value.nomination;

import java.math.BigDecimal;
import java.util.Date;

public class NominationReceiverReportValue
{
  // TABULAR DATA
  private Date dateSubmitted;
  private String receiverName;
  private String receiverCountry;
  private String receiverOrgName;
  private String giverName;
  private String giverCountry;
  private String giverOrgName;
  private String promotionName;
  private String approvalStatus;
  private String approverName;
  private Date approvalDate;
  private Long points;
  private Long sweepstakesWon;
  private Long badgesEarned;
  private Long eligibleNomineesCnt;
  private Long actualNomineesCnt;
  private BigDecimal eligibleNomineesPct;
  private Long totalNominationsRcvdCnt;
  private Long receiverId;

  // CHARTS
  private String monthName;
  private Long nominationCount;
  private String orgName;
  private String promoName;
  private Long claimId;

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public String getReceiverName()
  {
    return receiverName;
  }

  public void setReceiverName( String receiverName )
  {
    this.receiverName = receiverName;
  }

  public String getReceiverCountry()
  {
    return receiverCountry;
  }

  public void setReceiverCountry( String receiverCountry )
  {
    this.receiverCountry = receiverCountry;
  }

  public String getReceiverOrgName()
  {
    return receiverOrgName;
  }

  public void setReceiverOrgName( String receiverOrgName )
  {
    this.receiverOrgName = receiverOrgName;
  }

  public String getGiverName()
  {
    return giverName;
  }

  public void setGiverName( String giverName )
  {
    this.giverName = giverName;
  }

  public String getGiverCountry()
  {
    return giverCountry;
  }

  public void setGiverCountry( String giverCountry )
  {
    this.giverCountry = giverCountry;
  }

  public String getGiverOrgName()
  {
    return giverOrgName;
  }

  public void setGiverOrgName( String giverOrgName )
  {
    this.giverOrgName = giverOrgName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public void setApprovalStatus( String approvalStatus )
  {
    this.approvalStatus = approvalStatus;
  }

  public String getApprovalStatus()
  {
    return approvalStatus;
  }

  public String getApproverName()
  {
    return approverName;
  }

  public void setApproverName( String approverName )
  {
    this.approverName = approverName;
  }

  public Date getApprovalDate()
  {
    return approvalDate;
  }

  public void setApprovalDate( Date approvalDate )
  {
    this.approvalDate = approvalDate;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public Long getSweepstakesWon()
  {
    return sweepstakesWon;
  }

  public void setSweepstakesWon( Long sweepstakesWon )
  {
    this.sweepstakesWon = sweepstakesWon;
  }

  public Long getBadgesEarned()
  {
    return badgesEarned;
  }

  public void setBadgesEarned( Long badgesEarned )
  {
    this.badgesEarned = badgesEarned;
  }

  public String getMonthName()
  {
    return monthName;
  }

  public void setMonthName( String monthName )
  {
    this.monthName = monthName;
  }

  public Long getNominationCount()
  {
    return nominationCount;
  }

  public void setNominationCount( Long nominationCount )
  {
    this.nominationCount = nominationCount;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getPromoName()
  {
    return promoName;
  }

  public void setPromoName( String promoName )
  {
    this.promoName = promoName;
  }

  public Long getEligibleNomineesCnt()
  {
    return eligibleNomineesCnt;
  }

  public void setEligibleNomineesCnt( Long eligibleNomineesCnt )
  {
    this.eligibleNomineesCnt = eligibleNomineesCnt;
  }

  public Long getActualNomineesCnt()
  {
    return actualNomineesCnt;
  }

  public void setActualNomineesCnt( Long actualNomineesCnt )
  {
    this.actualNomineesCnt = actualNomineesCnt;
  }

  public BigDecimal getEligibleNomineesPct()
  {
    return eligibleNomineesPct;
  }

  public void setEligibleNomineesPct( BigDecimal eligibleNomineesPct )
  {
    this.eligibleNomineesPct = eligibleNomineesPct;
  }

  public Long getTotalNominationsRcvdCnt()
  {
    return totalNominationsRcvdCnt;
  }

  public void setTotalNominationsRcvdCnt( Long totalNominationsRcvdCnt )
  {
    this.totalNominationsRcvdCnt = totalNominationsRcvdCnt;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getReceiverId()
  {
    return receiverId;
  }

  public void setReceiverId( Long receiverId )
  {
    this.receiverId = receiverId;
  }

}

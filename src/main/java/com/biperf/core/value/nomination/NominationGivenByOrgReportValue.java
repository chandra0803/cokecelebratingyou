
package com.biperf.core.value.nomination;

import java.math.BigDecimal;

public class NominationGivenByOrgReportValue
{
  private Long orgId;

  private Long hierarchyNodeId;
  private String orgName;
  private Long eligibleNominatorsCnt;
  private Long actualNominatorsCnt;
  private BigDecimal eligbleNominatorsPct;
  private Long nominationsGivenCnt;
  private Long nominationsWonCnt;
  private Long points;
  private Long cash;
  private Long other;
  private Long pointsReceived;
  private Long cashReceived;
  private Long otherQtyReceived;
  private Long otherValueReceived;
  private Long sweepstakesWon;
  private Long eligibleNomineesCnt;
  private Long actualNomineesCnt;
  private BigDecimal eligibleNomineesPct;
  private Long nominationsReceived;
  private Long nominationsSubmitted;
  private Long nomineesSubmitted;

  private Long totalRecords;
  private Long recSeq;

  private String monthName;

  public String getMonthName()
  {
    return monthName;
  }

  public void setMonthName( String monthName )
  {
    this.monthName = monthName;
  }

  public Long getOrgId()
  {
    return orgId;
  }

  public void setOrgId( Long orgId )
  {
    this.orgId = orgId;
  }

  public Long getRecSeq()
  {
    return recSeq;
  }

  public void setRecSeq( Long recSeq )
  {
    this.recSeq = recSeq;
  }

  public Long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

  public String getHierarchyNodeId()
  {
    return hierarchyNodeId.toString();
  }

  public void setHierarchyNodeId( Long hierarchyNodeId )
  {
    this.hierarchyNodeId = hierarchyNodeId;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getEligibleNominatorsCnt()
  {
    return eligibleNominatorsCnt;
  }

  public void setEligibleNominatorsCnt( Long eligibleNominatorsCnt )
  {
    this.eligibleNominatorsCnt = eligibleNominatorsCnt;
  }

  public Long getActualNominatorsCnt()
  {
    return actualNominatorsCnt;
  }

  public void setActualNominatorsCnt( Long actualNominatorsCnt )
  {
    this.actualNominatorsCnt = actualNominatorsCnt;
  }

  public BigDecimal getEligbleNominatorsPct()
  {
    return eligbleNominatorsPct;
  }

  public void setEligbleNominatorsPct( BigDecimal eligbleNominatorsPct )
  {
    this.eligbleNominatorsPct = eligbleNominatorsPct;
  }

  public Long getNominationsGivenCnt()
  {
    return nominationsGivenCnt;
  }

  public void setNominationsGivenCnt( Long nominationsGivenCnt )
  {
    this.nominationsGivenCnt = nominationsGivenCnt;
  }

  public Long getNominationsWonCnt()
  {
    return nominationsWonCnt;
  }

  public void setNominationsWonCnt( Long nominationsWonCnt )
  {
    this.nominationsWonCnt = nominationsWonCnt;
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

  public Long getCash()
  {
    return cash;
  }

  public void setCash( Long cash )
  {
    this.cash = cash;
  }

  public Long getOther()
  {
    return other;
  }

  public void setOther( Long other )
  {
    this.other = other;
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

  public Long getOtherQtyReceived()
  {
    return otherQtyReceived;
  }

  public void setOtherQtyReceived( Long otherQtyReceived )
  {
    this.otherQtyReceived = otherQtyReceived;
  }

  public Long getOtherValueReceived()
  {
    return otherValueReceived;
  }

  public void setOtherValueReceived( Long otherValueReceived )
  {
    this.otherValueReceived = otherValueReceived;
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

  public Long getNominationsReceived()
  {
    return nominationsReceived;
  }

  public void setNominationsReceived( Long nominationsReceived )
  {
    this.nominationsReceived = nominationsReceived;
  }

  public Long getNomineesSubmitted()
  {
    return nomineesSubmitted;
  }

  public void setNomineesSubmitted( Long nomineesSubmitted )
  {
    this.nomineesSubmitted = nomineesSubmitted;
  }

  public Long getNominationsSubmitted()
  {
    return nominationsSubmitted;
  }

  public void setNominationsSubmitted( Long nominationsSubmitted )
  {
    this.nominationsSubmitted = nominationsSubmitted;
  }

}

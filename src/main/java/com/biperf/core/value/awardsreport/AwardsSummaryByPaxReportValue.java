/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class AwardsSummaryByPaxReportValue
{
  private String paxName;
  private String orgName;
  private String country;
  private String department;
  private String jobPosition;
  private Long pointsReceivedCnt;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;
  private Long onTheSpotDepositedCnt;
  private Long badgesEarnedCnt;
  private Long userId;
  private Long other;
  private Long cashReceivedCnt;

  private Long totalRecords;

  public Long getUserId()
  {
    return userId;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public AwardsSummaryByPaxReportValue()
  {
    super();
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public Long getPointsReceivedCnt()
  {
    return pointsReceivedCnt;
  }

  public void setPointsReceivedCnt( Long pointsReceivedCnt )
  {
    this.pointsReceivedCnt = pointsReceivedCnt;
  }

  public Long getPlateauEarnedCnt()
  {
    return plateauEarnedCnt;
  }

  public void setPlateauEarnedCnt( Long plateauEarnedCnt )
  {
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public Long getSweepstakesWonCnt()
  {
    return sweepstakesWonCnt;
  }

  public void setSweepstakesWonCnt( Long sweepstakesWonCnt )
  {
    this.sweepstakesWonCnt = sweepstakesWonCnt;
  }

  public Long getOnTheSpotDepositedCnt()
  {
    return onTheSpotDepositedCnt;
  }

  public void setOnTheSpotDepositedCnt( Long onTheSpotDepositedCnt )
  {
    this.onTheSpotDepositedCnt = onTheSpotDepositedCnt;
  }

  public Long getBadgesEarnedCnt()
  {
    return badgesEarnedCnt;
  }

  public void setBadgesEarnedCnt( Long badgesEarnedCnt )
  {
    this.badgesEarnedCnt = badgesEarnedCnt;
  }

  public Long getOther()
  {
    return other;
  }

  public void setOther( Long other )
  {
    this.other = other;
  }

  public Long getCashReceivedCnt()
  {
    return cashReceivedCnt;
  }

  public void setCashReceivedCnt( Long cashReceivedCnt )
  {
    this.cashReceivedCnt = cashReceivedCnt;
  }

  public Long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

}

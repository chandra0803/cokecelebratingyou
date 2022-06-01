/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class OtherAwardsIssuedForOrgReportValue
{
  private String awardsName;
  private String orgName;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;
  private Long onTheSpotDepositedCnt;
  private Long badgesEarnedCnt;
  private Long other;

  public OtherAwardsIssuedForOrgReportValue()
  {
    super();
  }

  public String getAwardsName()
  {
    return awardsName;
  }

  public void setAwardsName( String awardsName )
  {
    this.awardsName = awardsName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
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

}

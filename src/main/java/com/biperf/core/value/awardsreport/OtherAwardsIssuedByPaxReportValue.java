/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class OtherAwardsIssuedByPaxReportValue
{
  private String awardsName;
  private String paxName;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;
  private Long onTheSpotDepositedCnt;
  private Long badgesEarnedCnt;
  private Long other;

  public OtherAwardsIssuedByPaxReportValue()
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

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
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

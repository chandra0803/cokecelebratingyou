/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class TotalPointsIssuedByPaxReportValue
{
  private String paxName;
  private Long totalPointsCnt;

  public TotalPointsIssuedByPaxReportValue()
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

  public Long getTotalPointsCnt()
  {
    return totalPointsCnt;
  }

  public void setTotalPointsCnt( Long totalPointsCnt )
  {
    this.totalPointsCnt = totalPointsCnt;
  }

}

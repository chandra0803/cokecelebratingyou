/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class TotalPointsIssuedByPeriodReportValue
{
  private String month;
  private Long totalPointsCnt;

  public TotalPointsIssuedByPeriodReportValue()
  {
    super();
  }

  public String getMonth()
  {
    return month;
  }

  public void setMonth( String month )
  {
    this.month = month;
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

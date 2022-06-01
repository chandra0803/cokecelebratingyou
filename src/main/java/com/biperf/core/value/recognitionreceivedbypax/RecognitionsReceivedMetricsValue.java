/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionsReceivedMetricsValue
{
  private Long topCnt;
  private Long totalCnt;
  double overallOrgAvgCnt;

  public RecognitionsReceivedMetricsValue()
  {
    super();
  }

  public RecognitionsReceivedMetricsValue( Long topCnt, Long totalCnt, double overallOrgAvgCnt )
  {
    super();
    this.topCnt = topCnt;
    this.totalCnt = totalCnt;
    this.overallOrgAvgCnt = overallOrgAvgCnt;
  }

  public Long getTopCnt()
  {
    return topCnt;
  }

  public void setTopCnt( Long topCnt )
  {
    this.topCnt = topCnt;
  }

  public Long getTotalCnt()
  {
    return totalCnt;
  }

  public void setTotalCnt( Long totalCnt )
  {
    this.totalCnt = totalCnt;
  }

  public double getOverallOrgAvgCnt()
  {
    return overallOrgAvgCnt;
  }

  public void setOverallOrgAvgCnt( double overallOrgAvgCnt )
  {
    this.overallOrgAvgCnt = overallOrgAvgCnt;
  }

}

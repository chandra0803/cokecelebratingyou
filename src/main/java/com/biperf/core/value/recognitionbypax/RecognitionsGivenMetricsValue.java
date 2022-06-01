/**
 * 
 */

package com.biperf.core.value.recognitionbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionsGivenMetricsValue
{
  private Long topCnt;
  private Long totalCnt;
  private double overallOrgAvgCnt;

  public RecognitionsGivenMetricsValue()
  {
    super();
  }

  public RecognitionsGivenMetricsValue( Long topCnt, Long totalCnt, double overallOrgAvgCnt )
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

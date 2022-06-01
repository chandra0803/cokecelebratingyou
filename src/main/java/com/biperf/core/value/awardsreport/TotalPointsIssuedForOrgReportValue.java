/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class TotalPointsIssuedForOrgReportValue
{
  private String orgName;
  private Long earnedCount;

  public TotalPointsIssuedForOrgReportValue()
  {
    super();
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getEarnedCount()
  {
    return earnedCount;
  }

  public void setEarnedCount( Long earnedCount )
  {
    this.earnedCount = earnedCount;
  }

}

/**
 * 
 */

package com.biperf.core.value.quizactivityreport;

/**
 * @author poddutur
 *
 */
public class QuizStatusPercentForOrgReportValue
{
  private String orgName;
  private Long nodeId;
  private double failedPct;
  private double passedPct;

  public QuizStatusPercentForOrgReportValue()
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

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public double getFailedPct()
  {
    return failedPct;
  }

  public void setFailedPct( double failedPct )
  {
    this.failedPct = failedPct;
  }

  public double getPassedPct()
  {
    return passedPct;
  }

  public void setPassedPct( double passedPct )
  {
    this.passedPct = passedPct;
  }

}

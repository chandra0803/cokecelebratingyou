/**
 * 
 */

package com.biperf.core.value.quizactivityreport;

/**
 * @author poddutur
 *
 */
public class QuizActivityForOrgReportValue
{
  private String orgName;
  private Long nodeId;
  private Long attemptsFailed;
  private Long attemptsPassed;

  public QuizActivityForOrgReportValue()
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

  public Long getAttemptsFailed()
  {
    return attemptsFailed;
  }

  public void setAttemptsFailed( Long attemptsFailed )
  {
    this.attemptsFailed = attemptsFailed;
  }

  public Long getAttemptsPassed()
  {
    return attemptsPassed;
  }

  public void setAttemptsPassed( Long attemptsPassed )
  {
    this.attemptsPassed = attemptsPassed;
  }

}


package com.biperf.core.value;

/**
 * 
 * EngagementTeamsValueBean.
 * 
 * @author kandhi
 * @since Jun 11, 2014
 * @version 1.0
 */
public class EngagementTeamsValueBean
{
  private int companyGoal;
  private Long nodeId;
  private String nodeName;
  private Long parentNodeId;
  private int score;
  private int receivedCnt;
  private int sentCnt;
  private Integer connectedToCnt;
  private Integer connectedFromCnt;
  private int loginActivityCnt;
  private int receivedTarget;
  private int sentTarget;
  private Integer connectedToTarget;
  private Integer connectedFromTarget;
  private int loginActivityTarget;
  private String managerName;

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getParentNodeId()
  {
    return parentNodeId;
  }

  public void setParentNodeId( Long parentNodeId )
  {
    this.parentNodeId = parentNodeId;
  }

  public int getScore()
  {
    return score;
  }

  public void setScore( int score )
  {
    this.score = score;
  }

  public int getCompanyGoal()
  {
    return companyGoal;
  }

  public void setCompanyGoal( int companyGoal )
  {
    this.companyGoal = companyGoal;
  }

  public int getReceivedCnt()
  {
    return receivedCnt;
  }

  public void setReceivedCnt( int receivedCnt )
  {
    this.receivedCnt = receivedCnt;
  }

  public int getSentCnt()
  {
    return sentCnt;
  }

  public void setSentCnt( int sentCnt )
  {
    this.sentCnt = sentCnt;
  }

  public int getLoginActivityCnt()
  {
    return loginActivityCnt;
  }

  public void setLoginActivityCnt( int loginActivityCnt )
  {
    this.loginActivityCnt = loginActivityCnt;
  }

  public int getReceivedTarget()
  {
    return receivedTarget;
  }

  public void setReceivedTarget( int receivedTarget )
  {
    this.receivedTarget = receivedTarget;
  }

  public int getSentTarget()
  {
    return sentTarget;
  }

  public void setSentTarget( int sentTarget )
  {
    this.sentTarget = sentTarget;
  }

  public int getLoginActivityTarget()
  {
    return loginActivityTarget;
  }

  public void setLoginActivityTarget( int loginActivityTarget )
  {
    this.loginActivityTarget = loginActivityTarget;
  }

  public String getManagerName()
  {
    return managerName;
  }

  public void setManagerName( String managerName )
  {
    this.managerName = managerName;
  }

  public Integer getConnectedToCnt()
  {
    return connectedToCnt;
  }

  public void setConnectedToCnt( Integer connectedToCnt )
  {
    this.connectedToCnt = connectedToCnt;
  }

  public Integer getConnectedFromCnt()
  {
    return connectedFromCnt;
  }

  public void setConnectedFromCnt( Integer connectedFromCnt )
  {
    this.connectedFromCnt = connectedFromCnt;
  }

  public Integer getConnectedToTarget()
  {
    return connectedToTarget;
  }

  public void setConnectedToTarget( Integer connectedToTarget )
  {
    this.connectedToTarget = connectedToTarget;
  }

  public Integer getConnectedFromTarget()
  {
    return connectedFromTarget;
  }

  public void setConnectedFromTarget( Integer connectedFromTarget )
  {
    this.connectedFromTarget = connectedFromTarget;
  }

}

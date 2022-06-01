
package com.biperf.core.value;

/**
 * 
 * EngagementTeamMembersValueBean.
 * 
 * @author kandhi
 * @since Jun 11, 2014
 * @version 1.0
 */
public class EngagementTeamMembersValueBean
{
  private int companyGoal;
  private Long nodeId;
  private String nodeName;
  private Long userId;
  private String firstName;
  private String lastName;
  private int score;
  private int receivedCnt;
  private int sentCnt;
  private int connectedToCnt;
  private int connectedFromCnt;
  private int loginActivityCnt;
  private int receivedTarget;
  private int sentTarget;
  private int connectedToTarget;
  private int connectedFromTarget;
  private int loginActivityTarget;
  private boolean isRecognizedRecent;
  private String avatarUrl;

  public int getCompanyGoal()
  {
    return companyGoal;
  }

  public void setCompanyGoal( int companyGoal )
  {
    this.companyGoal = companyGoal;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public boolean isRecognizedRecent()
  {
    return isRecognizedRecent;
  }

  public void setRecognizedRecent( boolean isRecognizedRecent )
  {
    this.isRecognizedRecent = isRecognizedRecent;
  }

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

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public int getScore()
  {
    return score;
  }

  public void setScore( int score )
  {
    this.score = score;
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

  public int getConnectedToCnt()
  {
    return connectedToCnt;
  }

  public void setConnectedToCnt( int connectedToCnt )
  {
    this.connectedToCnt = connectedToCnt;
  }

  public int getConnectedFromCnt()
  {
    return connectedFromCnt;
  }

  public void setConnectedFromCnt( int connectedFromCnt )
  {
    this.connectedFromCnt = connectedFromCnt;
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

  public int getConnectedToTarget()
  {
    return connectedToTarget;
  }

  public void setConnectedToTarget( int connectedToTarget )
  {
    this.connectedToTarget = connectedToTarget;
  }

  public int getConnectedFromTarget()
  {
    return connectedFromTarget;
  }

  public void setConnectedFromTarget( int connectedFromTarget )
  {
    this.connectedFromTarget = connectedFromTarget;
  }

  public int getLoginActivityTarget()
  {
    return loginActivityTarget;
  }

  public void setLoginActivityTarget( int loginActivityTarget )
  {
    this.loginActivityTarget = loginActivityTarget;
  }

}

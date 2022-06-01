
package com.biperf.core.value;

import java.sql.Timestamp;

/**
 * 
 * EngagementSummaryValueBean.
 * 
 * @author kandhi
 * @since Jun 3, 2014
 * @version 1.0
 */
public class EngagementSummaryValueBean
{
  private boolean scoreActive;
  private int score;
  private int companyGoal;
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
  private int scoreAchievedCnt;
  private int receivedAchievedCnt;
  private int sentAchievedCnt;
  private int connectedToAchievedCnt;
  private int connectedFromAchievedCnt;
  private int loginAchievedCnt;
  private int totalParticipantCount;
  private Timestamp asofDate;
  private Long selectedBenchmarks;
  private int totalTeamsAvailable;
  private int totalMembersAvailable;
  private boolean displayTarget;
  private int connectedToPaxCnt;
  private int connectedFromPaxCnt;

  private String date;
  private String time;
  private String timeZoneId;

  public int getTotalTeamsAvailable()
  {
    return totalTeamsAvailable;
  }

  public void setTotalTeamsAvailable( int totalTeamsAvailable )
  {
    this.totalTeamsAvailable = totalTeamsAvailable;
  }

  public int getTotalMembersAvailable()
  {
    return totalMembersAvailable;
  }

  public void setTotalMembersAvailable( int totalMembersAvailable )
  {
    this.totalMembersAvailable = totalMembersAvailable;
  }

  public boolean isScoreActive()
  {
    return scoreActive;
  }

  public void setScoreActive( boolean scoreActive )
  {
    this.scoreActive = scoreActive;
  }

  public int getScoreAchievedCnt()
  {
    return scoreAchievedCnt;
  }

  public void setScoreAchievedCnt( int scoreAchievedCnt )
  {
    this.scoreAchievedCnt = scoreAchievedCnt;
  }

  public int getReceivedAchievedCnt()
  {
    return receivedAchievedCnt;
  }

  public void setReceivedAchievedCnt( int receivedAchievedCnt )
  {
    this.receivedAchievedCnt = receivedAchievedCnt;
  }

  public int getSentAchievedCnt()
  {
    return sentAchievedCnt;
  }

  public void setSentAchievedCnt( int sentAchievedCnt )
  {
    this.sentAchievedCnt = sentAchievedCnt;
  }

  public int getConnectedToAchievedCnt()
  {
    return connectedToAchievedCnt;
  }

  public void setConnectedToAchievedCnt( int connectedToAchievedCnt )
  {
    this.connectedToAchievedCnt = connectedToAchievedCnt;
  }

  public int getConnectedFromAchievedCnt()
  {
    return connectedFromAchievedCnt;
  }

  public void setConnectedFromAchievedCnt( int connectedFromAchievedCnt )
  {
    this.connectedFromAchievedCnt = connectedFromAchievedCnt;
  }

  public int getLoginAchievedCnt()
  {
    return loginAchievedCnt;
  }

  public void setLoginAchievedCnt( int loginAchievedCnt )
  {
    this.loginAchievedCnt = loginAchievedCnt;
  }

  public int getTotalParticipantCount()
  {
    return totalParticipantCount;
  }

  public void setTotalParticipantCount( int totalParticipantCount )
  {
    this.totalParticipantCount = totalParticipantCount;
  }

  public int getCompanyGoal()
  {
    return companyGoal;
  }

  public void setCompanyGoal( int companyGoal )
  {
    this.companyGoal = companyGoal;
  }

  public Timestamp getAsofDate()
  {
    return asofDate;
  }

  public void setAsofDate( Timestamp asofDate )
  {
    this.asofDate = asofDate;
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

  public Long getSelectedBenchmarks()
  {
    return selectedBenchmarks;
  }

  public void setSelectedBenchmarks( Long selectedBenchmarks )
  {
    this.selectedBenchmarks = selectedBenchmarks;
  }

  public boolean isDisplayTarget()
  {
    return displayTarget;
  }

  public void setDisplayTarget( boolean displayTarget )
  {
    this.displayTarget = displayTarget;
  }

  public int getConnectedToPaxCnt()
  {
    return connectedToPaxCnt;
  }

  public void setConnectedToPaxCnt( int connectedToPaxCnt )
  {
    this.connectedToPaxCnt = connectedToPaxCnt;
  }

  public int getConnectedFromPaxCnt()
  {
    return connectedFromPaxCnt;
  }

  public void setConnectedFromPaxCnt( int connectedFromPaxCnt )
  {
    this.connectedFromPaxCnt = connectedFromPaxCnt;
  }

  // Fix for 59693
  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public String getTime()
  {
    return time;
  }

  public void setTime( String time )
  {
    this.time = time;
  }

  public String getTimeZoneId()
  {
    return timeZoneId;
  }

  public void setTimeZoneId( String timeZoneId )
  {
    this.timeZoneId = timeZoneId;
  }

}

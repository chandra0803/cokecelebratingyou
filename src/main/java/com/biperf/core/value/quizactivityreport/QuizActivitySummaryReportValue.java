/**
 * 
 */

package com.biperf.core.value.quizactivityreport;

/**
 * @author poddutur
 *
 */
public class QuizActivitySummaryReportValue
{
  private String orgName;
  private Long eligibleQuizzesCnt;
  private Long quizAttemptsCnt;
  private Long quizAttemptsInProgressCnt;
  private Long attemptsFailedCnt;
  private Long attemptsPassedCnt;
  private double eligiblePassedPct;
  private double eligibleFailedPct;
  private Long pointsCnt;
  private Long sweepstakesWonCnt;
  private Long badgesEarnedCnt;
  private Long nodeId;
  private Long isTeam;

  public QuizActivitySummaryReportValue()
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

  public Long getEligibleQuizzesCnt()
  {
    return eligibleQuizzesCnt;
  }

  public void setEligibleQuizzesCnt( Long eligibleQuizzesCnt )
  {
    this.eligibleQuizzesCnt = eligibleQuizzesCnt;
  }

  public Long getQuizAttemptsCnt()
  {
    return quizAttemptsCnt;
  }

  public void setQuizAttemptsCnt( Long quizAttemptsCnt )
  {
    this.quizAttemptsCnt = quizAttemptsCnt;
  }

  public Long getQuizAttemptsInProgressCnt()
  {
    return quizAttemptsInProgressCnt;
  }

  public void setQuizAttemptsInProgressCnt( Long quizAttemptsInProgressCnt )
  {
    this.quizAttemptsInProgressCnt = quizAttemptsInProgressCnt;
  }

  public Long getAttemptsFailedCnt()
  {
    return attemptsFailedCnt;
  }

  public void setAttemptsFailedCnt( Long attemptsFailedCnt )
  {
    this.attemptsFailedCnt = attemptsFailedCnt;
  }

  public Long getAttemptsPassedCnt()
  {
    return attemptsPassedCnt;
  }

  public void setAttemptsPassedCnt( Long attemptsPassedCnt )
  {
    this.attemptsPassedCnt = attemptsPassedCnt;
  }

  public double getEligiblePassedPct()
  {
    return eligiblePassedPct;
  }

  public void setEligiblePassedPct( double eligiblePassedPct )
  {
    this.eligiblePassedPct = eligiblePassedPct;
  }

  public double getEligibleFailedPct()
  {
    return eligibleFailedPct;
  }

  public void setEligibleFailedPct( double eligibleFailedPct )
  {
    this.eligibleFailedPct = eligibleFailedPct;
  }

  public Long getPointsCnt()
  {
    return pointsCnt;
  }

  public void setPointsCnt( Long pointsCnt )
  {
    this.pointsCnt = pointsCnt;
  }

  public Long getSweepstakesWonCnt()
  {
    return sweepstakesWonCnt;
  }

  public void setSweepstakesWonCnt( Long sweepstakesWonCnt )
  {
    this.sweepstakesWonCnt = sweepstakesWonCnt;
  }

  public Long getBadgesEarnedCnt()
  {
    return badgesEarnedCnt;
  }

  public void setBadgesEarnedCnt( Long badgesEarnedCnt )
  {
    this.badgesEarnedCnt = badgesEarnedCnt;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getIsTeam()
  {
    return isTeam;
  }

  public void setIsTeam( Long isTeam )
  {
    this.isTeam = isTeam;
  }

}

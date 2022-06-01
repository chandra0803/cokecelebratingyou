/**
 * 
 */

package com.biperf.core.value.quizactivityreport;

/**
 * @author poddutur
 *
 */
public class QuizActivityDetailOneReportValue
{
  private String paxName;
  private String country;
  private Long eligibleQuizzesCnt;
  private Long quizAttemptsCnt;
  private Long quizAttemptsInProgressCnt;
  private Long attemptsFailedCnt;
  private Long attemptsPassedCnt;
  private Long pointsCnt;
  private Long sweepstakesWonCnt;
  private Long badgesEarnedCnt;
  private Long participantId;
  private Long isLeaf;

  public QuizActivityDetailOneReportValue()
  {
    super();
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
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

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

  public Long getIsLeaf()
  {
    return isLeaf;
  }

  public void setIsLeaf( Long isLeaf )
  {
    this.isLeaf = isLeaf;
  }

}

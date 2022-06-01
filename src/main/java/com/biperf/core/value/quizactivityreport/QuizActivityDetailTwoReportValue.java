/**
 * 
 */

package com.biperf.core.value.quizactivityreport;

import java.util.Date;

/**
 * @author poddutur
 *
 */
public class QuizActivityDetailTwoReportValue
{
  private Date quizDate;
  private String completedByName;
  private String orgName;
  private String promotionName;
  private String scorePassing;
  private String quizResult;
  private Long pointsCnt;
  private Long sweepstakesWonCnt;
  private Long badgesEarnedCnt;
  private Long certificate;
  private Long claimId;
  private Long participantId;

  public QuizActivityDetailTwoReportValue()
  {
    super();
  }

  public Date getQuizDate()
  {
    return quizDate;
  }

  public void setQuizDate( Date quizDate )
  {
    this.quizDate = quizDate;
  }

  public String getCompletedByName()
  {
    return completedByName;
  }

  public void setCompletedByName( String completedByName )
  {
    this.completedByName = completedByName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getScorePassing()
  {
    return scorePassing;
  }

  public void setScorePassing( String scorePassing )
  {
    this.scorePassing = scorePassing;
  }

  public String getQuizResult()
  {
    return quizResult;
  }

  public void setQuizResult( String quizResult )
  {
    this.quizResult = quizResult;
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

  public Long getCertificate()
  {
    return certificate;
  }

  public void setCertificate( Long certificate )
  {
    this.certificate = certificate;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getParticipantId()
  {
    return participantId;
  }

  public void setParticipantId( Long participantId )
  {
    this.participantId = participantId;
  }

}

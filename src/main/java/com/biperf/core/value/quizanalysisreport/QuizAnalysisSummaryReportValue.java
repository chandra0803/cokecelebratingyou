/**
 * 
 */

package com.biperf.core.value.quizanalysisreport;

/**
 * @author poddutur
 *
 */
public class QuizAnalysisSummaryReportValue
{
  private Long quizId;
  String quizName;
  String quizType;
  private Long questionsInPoolCnt;
  private Long quesToAskPerAttemptCnt;
  private Long reqToPassCnt;
  private Long quizAttemptsCnt;
  private double quizAttemptsPct;
  private Long passedAttemptsCnt;
  private double passedAttemptsPct;
  private Long failedAttemptsCnt;
  private double failedAttemptsPct;
  private Long incompleteAttemptsCnt;
  private double incompleteAttemptsPct;
  private String maxAttemptsAllowedPerPaxCnt;

  public QuizAnalysisSummaryReportValue()
  {
    super();
  }

  public Long getQuizId()
  {
    return quizId;
  }

  public void setQuizId( Long quizId )
  {
    this.quizId = quizId;
  }

  public String getQuizName()
  {
    return quizName;
  }

  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  public String getQuizType()
  {
    return quizType;
  }

  public void setQuizType( String quizType )
  {
    this.quizType = quizType;
  }

  public Long getQuestionsInPoolCnt()
  {
    return questionsInPoolCnt;
  }

  public void setQuestionsInPoolCnt( Long questionsInPoolCnt )
  {
    this.questionsInPoolCnt = questionsInPoolCnt;
  }

  public Long getQuesToAskPerAttemptCnt()
  {
    return quesToAskPerAttemptCnt;
  }

  public void setQuesToAskPerAttemptCnt( Long quesToAskPerAttemptCnt )
  {
    this.quesToAskPerAttemptCnt = quesToAskPerAttemptCnt;
  }

  public Long getReqToPassCnt()
  {
    return reqToPassCnt;
  }

  public void setReqToPassCnt( Long reqToPassCnt )
  {
    this.reqToPassCnt = reqToPassCnt;
  }

  public Long getQuizAttemptsCnt()
  {
    return quizAttemptsCnt;
  }

  public void setQuizAttemptsCnt( Long quizAttemptsCnt )
  {
    this.quizAttemptsCnt = quizAttemptsCnt;
  }

  public double getQuizAttemptsPct()
  {
    return quizAttemptsPct;
  }

  public void setQuizAttemptsPct( double quizAttemptsPct )
  {
    this.quizAttemptsPct = quizAttemptsPct;
  }

  public Long getPassedAttemptsCnt()
  {
    return passedAttemptsCnt;
  }

  public void setPassedAttemptsCnt( Long passedAttemptsCnt )
  {
    this.passedAttemptsCnt = passedAttemptsCnt;
  }

  public double getPassedAttemptsPct()
  {
    return passedAttemptsPct;
  }

  public void setPassedAttemptsPct( double passedAttemptsPct )
  {
    this.passedAttemptsPct = passedAttemptsPct;
  }

  public Long getFailedAttemptsCnt()
  {
    return failedAttemptsCnt;
  }

  public void setFailedAttemptsCnt( Long failedAttemptsCnt )
  {
    this.failedAttemptsCnt = failedAttemptsCnt;
  }

  public double getFailedAttemptsPct()
  {
    return failedAttemptsPct;
  }

  public void setFailedAttemptsPct( double failedAttemptsPct )
  {
    this.failedAttemptsPct = failedAttemptsPct;
  }

  public Long getIncompleteAttemptsCnt()
  {
    return incompleteAttemptsCnt;
  }

  public void setIncompleteAttemptsCnt( Long incompleteAttemptsCnt )
  {
    this.incompleteAttemptsCnt = incompleteAttemptsCnt;
  }

  public double getIncompleteAttemptsPct()
  {
    return incompleteAttemptsPct;
  }

  public void setIncompleteAttemptsPct( double incompleteAttemptsPct )
  {
    this.incompleteAttemptsPct = incompleteAttemptsPct;
  }

  public String getMaxAttemptsAllowedPerPaxCnt()
  {
    return maxAttemptsAllowedPerPaxCnt;
  }

  public void setMaxAttemptsAllowedPerPaxCnt( String maxAttemptsAllowedPerPaxCnt )
  {
    this.maxAttemptsAllowedPerPaxCnt = maxAttemptsAllowedPerPaxCnt;
  }

}

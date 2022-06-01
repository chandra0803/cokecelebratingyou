/**
 * 
 */

package com.biperf.core.value.quizanalysisreport;

/**
 * @author poddutur
 *
 */
public class QuizAttemptStatusForOrgReportValue
{
  private String quizName;
  private Long passedAttemptsCnt;
  private Long failedAttemptsCnt;
  private Long incompleteAttemptsCnt;

  public QuizAttemptStatusForOrgReportValue()
  {
    super();
  }

  public String getQuizName()
  {
    return quizName;
  }

  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  public Long getPassedAttemptsCnt()
  {
    return passedAttemptsCnt;
  }

  public void setPassedAttemptsCnt( Long passedAttemptsCnt )
  {
    this.passedAttemptsCnt = passedAttemptsCnt;
  }

  public Long getFailedAttemptsCnt()
  {
    return failedAttemptsCnt;
  }

  public void setFailedAttemptsCnt( Long failedAttemptsCnt )
  {
    this.failedAttemptsCnt = failedAttemptsCnt;
  }

  public Long getIncompleteAttemptsCnt()
  {
    return incompleteAttemptsCnt;
  }

  public void setIncompleteAttemptsCnt( Long incompleteAttemptsCnt )
  {
    this.incompleteAttemptsCnt = incompleteAttemptsCnt;
  }

}

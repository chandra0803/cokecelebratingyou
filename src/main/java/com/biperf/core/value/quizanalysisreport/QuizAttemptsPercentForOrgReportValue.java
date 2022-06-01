/**
 * 
 */

package com.biperf.core.value.quizanalysisreport;

/**
 * @author poddutur
 *
 */
public class QuizAttemptsPercentForOrgReportValue
{
  private String quizName;
  private double passedAttemptsPct;
  private double failedAttemptsPct;
  private double incompleteAttemptsPct;

  public QuizAttemptsPercentForOrgReportValue()
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

  public double getPassedAttemptsPct()
  {
    return passedAttemptsPct;
  }

  public void setPassedAttemptsPct( double passedAttemptsPct )
  {
    this.passedAttemptsPct = passedAttemptsPct;
  }

  public double getFailedAttemptsPct()
  {
    return failedAttemptsPct;
  }

  public void setFailedAttemptsPct( double failedAttemptsPct )
  {
    this.failedAttemptsPct = failedAttemptsPct;
  }

  public double getIncompleteAttemptsPct()
  {
    return incompleteAttemptsPct;
  }

  public void setIncompleteAttemptsPct( double incompleteAttemptsPct )
  {
    this.incompleteAttemptsPct = incompleteAttemptsPct;
  }

}

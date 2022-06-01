/**
 * 
 */

package com.biperf.core.value.quizanalysisreport;

/**
 * @author poddutur
 *
 */
public class QuizQuestionAnalysisReportValue
{
  private String question;
  private double correctResponsesPct;
  private double incorrectResponsesPct;

  public QuizQuestionAnalysisReportValue()
  {
    super();
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

  public double getCorrectResponsesPct()
  {
    return correctResponsesPct;
  }

  public void setCorrectResponsesPct( double correctResponsesPct )
  {
    this.correctResponsesPct = correctResponsesPct;
  }

  public double getIncorrectResponsesPct()
  {
    return incorrectResponsesPct;
  }

  public void setIncorrectResponsesPct( double incorrectResponsesPct )
  {
    this.incorrectResponsesPct = incorrectResponsesPct;
  }

}

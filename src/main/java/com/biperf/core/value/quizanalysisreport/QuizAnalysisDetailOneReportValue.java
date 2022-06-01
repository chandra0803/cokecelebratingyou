/**
 * 
 */

package com.biperf.core.value.quizanalysisreport;

/**
 * @author poddutur
 *
 */
public class QuizAnalysisDetailOneReportValue
{
  private Long qqId;
  private String question;
  private Long nbrOfTimesAsked;
  private Long nbrCorrectResponses;
  private double correctResponsesPct;
  private Long nbrIncorrectResponses;
  private double incorrectResponsesPct;

  public QuizAnalysisDetailOneReportValue()
  {
    super();
  }

  public Long getQqId()
  {
    return qqId;
  }

  public void setQqId( Long qqId )
  {
    this.qqId = qqId;
  }

  public String getQuestion()
  {
    return question;
  }

  public void setQuestion( String question )
  {
    this.question = question;
  }

  public Long getNbrOfTimesAsked()
  {
    return nbrOfTimesAsked;
  }

  public void setNbrOfTimesAsked( Long nbrOfTimesAsked )
  {
    this.nbrOfTimesAsked = nbrOfTimesAsked;
  }

  public Long getNbrCorrectResponses()
  {
    return nbrCorrectResponses;
  }

  public void setNbrCorrectResponses( Long nbrCorrectResponses )
  {
    this.nbrCorrectResponses = nbrCorrectResponses;
  }

  public double getCorrectResponsesPct()
  {
    return correctResponsesPct;
  }

  public void setCorrectResponsesPct( double correctResponsesPct )
  {
    this.correctResponsesPct = correctResponsesPct;
  }

  public Long getNbrIncorrectResponses()
  {
    return nbrIncorrectResponses;
  }

  public void setNbrIncorrectResponses( Long nbrIncorrectResponses )
  {
    this.nbrIncorrectResponses = nbrIncorrectResponses;
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

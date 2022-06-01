/**
 * 
 */

package com.biperf.core.value.quizanalysisreport;

/**
 * @author poddutur
 *
 */
public class QuizAnalysisDetailTwoReportValue
{
  private String response;
  private String rightResponse;
  private Long timesSelectedCnt;
  private double timesSelectedPct;

  public QuizAnalysisDetailTwoReportValue()
  {
    super();
  }

  public String getResponse()
  {
    return response;
  }

  public void setResponse( String response )
  {
    this.response = response;
  }

  public String getRightResponse()
  {
    return rightResponse;
  }

  public void setRightResponse( String rightResponse )
  {
    this.rightResponse = rightResponse;
  }

  public Long getTimesSelectedCnt()
  {
    return timesSelectedCnt;
  }

  public void setTimesSelectedCnt( Long timesSelectedCnt )
  {
    this.timesSelectedCnt = timesSelectedCnt;
  }

  public double getTimesSelectedPct()
  {
    return timesSelectedPct;
  }

  public void setTimesSelectedPct( double timesSelectedPct )
  {
    this.timesSelectedPct = timesSelectedPct;
  }

}

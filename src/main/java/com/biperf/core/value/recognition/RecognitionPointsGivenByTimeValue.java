/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsGivenByTimeValue
{

  private Long recognitionPointCnt;
  private String monthName;

  public RecognitionPointsGivenByTimeValue()
  {
    super();
  }

  public RecognitionPointsGivenByTimeValue( Long recognitionPointCnt, String monthName )
  {
    super();
    this.recognitionPointCnt = recognitionPointCnt;
    this.monthName = monthName;
  }

  public double getRecognitionPointCnt()
  {
    return recognitionPointCnt;
  }

  public void setRecognitionPointCnt( Long recognitionPointCnt )
  {
    this.recognitionPointCnt = recognitionPointCnt;
  }

  public String getMonthName()
  {
    return monthName;
  }

  public void setMonthName( String monthName )
  {
    this.monthName = monthName;
  }

}

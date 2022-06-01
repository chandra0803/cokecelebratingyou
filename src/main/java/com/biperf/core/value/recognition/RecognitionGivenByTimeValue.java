/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionGivenByTimeValue
{
  private Long recognitionCnt;
  private String monthName;

  public RecognitionGivenByTimeValue()
  {
    super();
  }

  public RecognitionGivenByTimeValue( Long recognitionCnt, String monthName )
  {
    super();
    this.recognitionCnt = recognitionCnt;
    this.monthName = monthName;
  }

  public Long getRecognitionCnt()
  {
    return recognitionCnt;
  }

  public void setRecognitionCnt( Long recognitionCnt )
  {
    this.recognitionCnt = recognitionCnt;
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

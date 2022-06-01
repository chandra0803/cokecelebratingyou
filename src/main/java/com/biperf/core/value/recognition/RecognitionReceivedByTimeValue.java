/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedByTimeValue
{
  private Long recognitionCnt;
  private String monthName;

  public RecognitionReceivedByTimeValue()
  {
    super();
  }

  public RecognitionReceivedByTimeValue( Long recognitionCnt, String monthName )
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

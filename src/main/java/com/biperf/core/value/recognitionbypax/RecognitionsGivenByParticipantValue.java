/**
 * 
 */

package com.biperf.core.value.recognitionbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionsGivenByParticipantValue
{
  private String paxName;
  private Long recognitionCnt;

  public RecognitionsGivenByParticipantValue()
  {
    super();
  }

  public RecognitionsGivenByParticipantValue( String paxName, Long recognitionCnt )
  {
    super();
    this.paxName = paxName;
    this.recognitionCnt = recognitionCnt;
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public double getRecognitionCnt()
  {
    return recognitionCnt;
  }

  public void setRecognitionCnt( Long recognitionCnt )
  {
    this.recognitionCnt = recognitionCnt;
  }

}

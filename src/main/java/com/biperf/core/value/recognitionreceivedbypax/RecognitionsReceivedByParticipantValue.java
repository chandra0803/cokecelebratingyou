/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionsReceivedByParticipantValue
{
  private String paxName;
  private Long recognitionCnt;

  public RecognitionsReceivedByParticipantValue()
  {
    super();
  }

  public RecognitionsReceivedByParticipantValue( String paxName, Long recognitionCnt )
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

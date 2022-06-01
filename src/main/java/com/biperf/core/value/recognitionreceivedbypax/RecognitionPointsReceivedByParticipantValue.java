/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsReceivedByParticipantValue
{
  private String paxName;
  private Long recognitionPointsCnt;

  public RecognitionPointsReceivedByParticipantValue()
  {
    super();
  }

  public RecognitionPointsReceivedByParticipantValue( String paxName, Long recognitionPointsCnt )
  {
    super();
    this.paxName = paxName;
    this.recognitionPointsCnt = recognitionPointsCnt;
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public double getRecognitionPointsCnt()
  {
    return recognitionPointsCnt;
  }

  public void setRecognitionPointsCnt( Long recognitionPointsCnt )
  {
    this.recognitionPointsCnt = recognitionPointsCnt;
  }

}

/**
 * 
 */

package com.biperf.core.value.recognitionbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsGivenByParticipantValue
{

  private String paxName;
  private Long recognitionPointsCnt;

  public RecognitionPointsGivenByParticipantValue()
  {
    super();
  }

  public RecognitionPointsGivenByParticipantValue( String paxName, Long recognitionPointsCnt )
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

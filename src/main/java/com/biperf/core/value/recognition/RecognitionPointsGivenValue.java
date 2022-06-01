/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsGivenValue
{

  private String nodeName;
  private Long recognitionPointCnt;

  public RecognitionPointsGivenValue()
  {
    super();
  }

  public RecognitionPointsGivenValue( String nodeName, Long recognitionPointCnt )
  {
    super();
    this.nodeName = nodeName;
    this.recognitionPointCnt = recognitionPointCnt;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public double getRecognitionPointCnt()
  {
    return recognitionPointCnt;
  }

  public void setRecognitionPointCnt( Long recognitionPointCnt )
  {
    this.recognitionPointCnt = recognitionPointCnt;
  }

}

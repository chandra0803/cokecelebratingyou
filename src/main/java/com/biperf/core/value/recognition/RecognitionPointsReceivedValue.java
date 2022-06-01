/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsReceivedValue
{
  private String nodeName;
  private Long recognitionPointCnt;

  public RecognitionPointsReceivedValue()
  {
    super();
  }

  public RecognitionPointsReceivedValue( String nodeName, Long recognitionPointCnt )
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

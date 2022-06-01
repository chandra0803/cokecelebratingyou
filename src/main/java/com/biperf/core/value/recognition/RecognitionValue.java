/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionValue
{

  private String nodeName;
  private Long recognitionCnt;

  public RecognitionValue()
  {
    super();
  }

  public RecognitionValue( String nodeName, Long recognitionCnt )
  {
    super();
    this.nodeName = nodeName;
    this.recognitionCnt = recognitionCnt;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
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

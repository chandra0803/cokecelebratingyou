/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedValue
{
  private String nodeName;
  private Long recognitionCnt;

  public RecognitionReceivedValue()
  {
    super();
  }

  public RecognitionReceivedValue( String nodeName, Long recognitionCnt )
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

  public Long getRecognitionCnt()
  {
    return recognitionCnt;
  }

  public void setRecognitionCnt( Long recognitionCnt )
  {
    this.recognitionCnt = recognitionCnt;
  }
}

/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedParticipationRateBarValue
{
  private Long haveReceivedCnt;
  private Long haveNotReceivedCnt;
  private String nodeName;

  public RecognitionReceivedParticipationRateBarValue()
  {
    super();
  }

  public RecognitionReceivedParticipationRateBarValue( Long haveReceivedCnt, Long haveNotReceivedCnt, String nodeName )
  {
    super();
    this.haveReceivedCnt = haveReceivedCnt;
    this.haveNotReceivedCnt = haveNotReceivedCnt;
    this.nodeName = nodeName;
  }

  public Long getHaveReceivedCnt()
  {
    return haveReceivedCnt;
  }

  public void setHaveReceivedCnt( Long haveReceivedCnt )
  {
    this.haveReceivedCnt = haveReceivedCnt;
  }

  public Long getHaveNotReceivedCnt()
  {
    return haveNotReceivedCnt;
  }

  public void setHaveNotReceivedCnt( Long haveNotReceivedCnt )
  {
    this.haveNotReceivedCnt = haveNotReceivedCnt;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

}

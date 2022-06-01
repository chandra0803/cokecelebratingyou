/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionGivenParticipationRateBarValue
{

  private Long haveGivenCnt;
  private Long haveNotGivenCnt;
  private String nodeName;

  public RecognitionGivenParticipationRateBarValue()
  {
    super();
  }

  public RecognitionGivenParticipationRateBarValue( Long haveGivenCnt, Long haveNotGivenCnt, String nodeName )
  {
    super();
    this.haveGivenCnt = haveGivenCnt;
    this.haveNotGivenCnt = haveNotGivenCnt;
    this.nodeName = nodeName;
  }

  public Long getHaveGivenCnt()
  {
    return haveGivenCnt;
  }

  public void setHaveGivenCnt( Long haveGivenCnt )
  {
    this.haveGivenCnt = haveGivenCnt;
  }

  public Long getHaveNotGivenCnt()
  {
    return haveNotGivenCnt;
  }

  public void setHaveNotGivenCnt( Long haveNotGivenCnt )
  {
    this.haveNotGivenCnt = haveNotGivenCnt;
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

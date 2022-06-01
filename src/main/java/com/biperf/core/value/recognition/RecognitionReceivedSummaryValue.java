/**
 * 
 */

package com.biperf.core.value.recognition;

public class RecognitionReceivedSummaryValue
{
  private String nodeName;
  private Long nodeId;
  private Long eligibleCnt;
  private Long actualCnt;
  private double eligiblePct;
  private Long totalRecognition;
  private Long points;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;
  private Integer totalRecords;

  public RecognitionReceivedSummaryValue()
  {
    super();
  }

  public RecognitionReceivedSummaryValue( String nodeName,
                                          Long nodeId,
                                          Long eligibleCnt,
                                          Long actualCnt,
                                          double eligiblePct,
                                          Long totalRecognition,
                                          Long points,
                                          Long plateauEarnedCnt,
                                          Long sweepstakesWonCnt,
                                          Integer totalRecords )
  {
    super();
    this.nodeName = nodeName;
    this.nodeId = nodeId;
    this.eligibleCnt = eligibleCnt;
    this.actualCnt = actualCnt;
    this.eligiblePct = eligiblePct;
    this.totalRecognition = totalRecognition;
    this.points = points;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.sweepstakesWonCnt = sweepstakesWonCnt;
    this.setTotalRecords( totalRecords );
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getEligibleCnt()
  {
    return eligibleCnt;
  }

  public void setEligibleCnt( Long eligibleCnt )
  {
    this.eligibleCnt = eligibleCnt;
  }

  public Long getActualCnt()
  {
    return actualCnt;
  }

  public void setActualCnt( Long actualCnt )
  {
    this.actualCnt = actualCnt;
  }

  public double getEligiblePct()
  {
    return eligiblePct;
  }

  public void setEligiblePct( double eligiblePct )
  {
    this.eligiblePct = eligiblePct;
  }

  public Long getTotalRecognition()
  {
    return totalRecognition;
  }

  public void setTotalRecognition( Long totalRecognition )
  {
    this.totalRecognition = totalRecognition;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public Long getPlateauEarnedCnt()
  {
    return plateauEarnedCnt;
  }

  public void setPlateauEarnedCnt( Long plateauEarnedCnt )
  {
    this.plateauEarnedCnt = plateauEarnedCnt;
  }

  public Long getSweepstakesWonCnt()
  {
    return sweepstakesWonCnt;
  }

  public void setSweepstakesWonCnt( Long sweepstakesWonCnt )
  {
    this.sweepstakesWonCnt = sweepstakesWonCnt;
  }

  public Integer getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Integer totalRecords )
  {
    this.totalRecords = totalRecords;
  }

}

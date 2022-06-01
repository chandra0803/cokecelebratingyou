/**
 * 
 */

package com.biperf.core.value.recognition;

public class RecognitionGivenDetailValue
{

  private Long giverUserId;
  private String giverName;
  private Long totalRecognition;
  private Long recognitionPoints;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;
  private Integer totalRecords;

  public RecognitionGivenDetailValue()
  {
    super();
  }

  public RecognitionGivenDetailValue( Long giverUserId, String giverName, Long totalRecognition, Long recognitionPoints, Long plateauEarnedCnt, Long sweepstakesWonCnt, Integer totalRecords )
  {
    super();
    this.giverUserId = giverUserId;
    this.giverName = giverName;
    this.totalRecognition = totalRecognition;
    this.recognitionPoints = recognitionPoints;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.sweepstakesWonCnt = sweepstakesWonCnt;
    this.totalRecords = totalRecords;
  }

  public Long getGiverUserId()
  {
    return giverUserId;
  }

  public void setGiverUserId( Long giverUserId )
  {
    this.giverUserId = giverUserId;
  }

  public String getGiverName()
  {
    return giverName;
  }

  public void setGiverName( String giverName )
  {
    this.giverName = giverName;
  }

  public Long getTotalRecognition()
  {
    return totalRecognition;
  }

  public void setTotalRecognition( Long totalRecognition )
  {
    this.totalRecognition = totalRecognition;
  }

  public Long getRecognitionPoints()
  {
    return recognitionPoints;
  }

  public void setRecognitionPoints( Long recognitionPoints )
  {
    this.recognitionPoints = recognitionPoints;
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

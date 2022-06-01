
package com.biperf.core.value.recognition;

public class RecognitionReceivedDetailTotalsValue
{

  private Long totalRecognition;
  private Long recognitionPoints;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;

  public RecognitionReceivedDetailTotalsValue()
  {
    super();
  }

  public RecognitionReceivedDetailTotalsValue( Long totalRecognition, Long recognitionPoints, Long plateauEarnedCnt, Long sweepstakesWonCnt )
  {
    super();
    this.totalRecognition = totalRecognition;
    this.recognitionPoints = recognitionPoints;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.sweepstakesWonCnt = sweepstakesWonCnt;
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

}

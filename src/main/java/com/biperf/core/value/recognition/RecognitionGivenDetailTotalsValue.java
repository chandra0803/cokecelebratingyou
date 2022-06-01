
package com.biperf.core.value.recognition;

public class RecognitionGivenDetailTotalsValue
{

  private Long totalRecognitions;
  private Long totalRecognitionPoints;
  private Long totalPlateauEarnedCnt;
  private Long totalSweepstakesWonCnt;

  public RecognitionGivenDetailTotalsValue()
  {
    super();
  }

  public RecognitionGivenDetailTotalsValue( Long totalRecognitions, Long totalRecognitionPoints, Long totalPlateauEarnedCnt, Long totalSweepstakesWonCnt )
  {
    super();
    this.totalRecognitions = totalRecognitions;
    this.totalRecognitionPoints = totalRecognitionPoints;
    this.totalPlateauEarnedCnt = totalPlateauEarnedCnt;
    this.totalSweepstakesWonCnt = totalSweepstakesWonCnt;
  }

  public Long getTotalRecognitions()
  {
    return totalRecognitions;
  }

  public void setTotalRecognitions( Long totalRecognitions )
  {
    this.totalRecognitions = totalRecognitions;
  }

  public Long getTotalRecognitionPoints()
  {
    return totalRecognitionPoints;
  }

  public void setTotalRecognitionPoints( Long totalRecognitionPoints )
  {
    this.totalRecognitionPoints = totalRecognitionPoints;
  }

  public Long getTotalPlateauEarnedCnt()
  {
    return totalPlateauEarnedCnt;
  }

  public void setTotalPlateauEarnedCnt( Long totalPlateauEarnedCnt )
  {
    this.totalPlateauEarnedCnt = totalPlateauEarnedCnt;
  }

  public Long getTotalSweepstakesWonCnt()
  {
    return totalSweepstakesWonCnt;
  }

  public void setTotalSweepstakesWonCnt( Long totalSweepstakesWonCnt )
  {
    this.totalSweepstakesWonCnt = totalSweepstakesWonCnt;
  }

}

/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionReceivedDetailValue
{
  private Long receiverUserId;
  private String receiverName;
  private Long totalRecognition;
  private Long recognitionPoints;
  private Long plateauEarnedCnt;
  private Long sweepstakesWonCnt;
  private Integer totalRecords;

  public RecognitionReceivedDetailValue()
  {
    super();
  }

  public RecognitionReceivedDetailValue( Long receiverUserId, String receiverName, Long totalRecognition, Long recognitionPoints, Long plateauEarnedCnt, Long sweepstakesWonCnt, Integer totalRecords )
  {
    super();
    this.setReceiverUserId( receiverUserId );
    this.receiverName = receiverName;
    this.totalRecognition = totalRecognition;
    this.recognitionPoints = recognitionPoints;
    this.plateauEarnedCnt = plateauEarnedCnt;
    this.sweepstakesWonCnt = sweepstakesWonCnt;
    this.setTotalRecords( totalRecords );
  }

  public Long getReceiverUserId()
  {
    return receiverUserId;
  }

  public void setReceiverUserId( Long receiverUserId )
  {
    this.receiverUserId = receiverUserId;
  }

  public String getReceiverName()
  {
    return receiverName;
  }

  public void setReceiverName( String receiverName )
  {
    this.receiverName = receiverName;
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

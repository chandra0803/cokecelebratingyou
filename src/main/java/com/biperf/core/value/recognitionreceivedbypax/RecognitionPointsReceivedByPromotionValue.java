/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsReceivedByPromotionValue
{
  private String promotionName;
  private Long recognitionPointsCnt;

  public RecognitionPointsReceivedByPromotionValue()
  {
    super();
  }

  public RecognitionPointsReceivedByPromotionValue( String promotionName, Long recognitionPointsCnt )
  {
    super();
    this.promotionName = promotionName;
    this.recognitionPointsCnt = recognitionPointsCnt;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public double getRecognitionPointsCnt()
  {
    return recognitionPointsCnt;
  }

  public void setRecognitionPointsCnt( Long recognitionPointsCnt )
  {
    this.recognitionPointsCnt = recognitionPointsCnt;
  }

}

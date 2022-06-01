/**
 * 
 */

package com.biperf.core.value.recognitionbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionPointsGivenByPromotionValue
{
  private String promotionName;
  private Long recognitionPointsCnt;

  public RecognitionPointsGivenByPromotionValue()
  {
    super();
  }

  public RecognitionPointsGivenByPromotionValue( String promotionName, Long recognitionPointsCnt )
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

/**
 * 
 */

package com.biperf.core.value.recognitionreceivedbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionsReceivedByPromotionValue
{
  private String promotionName;
  private Long recognitionCnt;

  public RecognitionsReceivedByPromotionValue()
  {
    super();
  }

  public RecognitionsReceivedByPromotionValue( String promotionName, Long recognitionCnt )
  {
    super();
    this.promotionName = promotionName;
    this.recognitionCnt = recognitionCnt;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public double getRecognitionCnt()
  {
    return recognitionCnt;
  }

  public void setRecognitionCnt( Long recognitionCnt )
  {
    this.recognitionCnt = recognitionCnt;
  }

}

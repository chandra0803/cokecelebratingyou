
package com.biperf.core.value;

import java.io.Serializable;

public class EngagementBenchmarkValueBean implements Serializable
{

  private static final long serialVersionUID = 1L;

  private String recognitionSentWeight;
  private String recognitionReceivedWeight;
  private String uniqueRecognitionSentWeight;
  private String uniqueRecognitionReceivedWeight;
  private String loginActivityWeight;
  private String recognitionSentTarget;
  private String recognitionReceivedTarget;
  private String uniqueRecognitionSentTarget;
  private String uniqueRecognitionReceivedTarget;
  private String loginActivityTarget;

  private String[] selectedAudiences;
  private String[] notSelectedAudiences;
  private Long promotionId;
  private long audienceType;
  private int index;
  private Long benchmarkId;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public long getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( long audienceType )
  {
    this.audienceType = audienceType;
  }

  public String getRecognitionSentWeight()
  {
    return recognitionSentWeight;
  }

  public void setRecognitionSentWeight( String recognitionSentWeight )
  {
    this.recognitionSentWeight = recognitionSentWeight;
  }

  public String getRecognitionReceivedWeight()
  {
    return recognitionReceivedWeight;
  }

  public void setRecognitionReceivedWeight( String recognitionReceivedWeight )
  {
    this.recognitionReceivedWeight = recognitionReceivedWeight;
  }

  public String getUniqueRecognitionSentWeight()
  {
    return uniqueRecognitionSentWeight;
  }

  public void setUniqueRecognitionSentWeight( String uniqueRecognitionSentWeight )
  {
    this.uniqueRecognitionSentWeight = uniqueRecognitionSentWeight;
  }

  public String getUniqueRecognitionReceivedWeight()
  {
    return uniqueRecognitionReceivedWeight;
  }

  public void setUniqueRecognitionReceivedWeight( String uniqueRecognitionReceivedWeight )
  {
    this.uniqueRecognitionReceivedWeight = uniqueRecognitionReceivedWeight;
  }

  public String getLoginActivityWeight()
  {
    return loginActivityWeight;
  }

  public void setLoginActivityWeight( String loginActivityWeight )
  {
    this.loginActivityWeight = loginActivityWeight;
  }

  public String getRecognitionSentTarget()
  {
    return recognitionSentTarget;
  }

  public void setRecognitionSentTarget( String recognitionSentTarget )
  {
    this.recognitionSentTarget = recognitionSentTarget;
  }

  public String getRecognitionReceivedTarget()
  {
    return recognitionReceivedTarget;
  }

  public void setRecognitionReceivedTarget( String recognitionReceivedTarget )
  {
    this.recognitionReceivedTarget = recognitionReceivedTarget;
  }

  public String getUniqueRecognitionSentTarget()
  {
    return uniqueRecognitionSentTarget;
  }

  public void setUniqueRecognitionSentTarget( String uniqueRecognitionSentTarget )
  {
    this.uniqueRecognitionSentTarget = uniqueRecognitionSentTarget;
  }

  public String getUniqueRecognitionReceivedTarget()
  {
    return uniqueRecognitionReceivedTarget;
  }

  public void setUniqueRecognitionReceivedTarget( String uniqueRecognitionReceivedTarget )
  {
    this.uniqueRecognitionReceivedTarget = uniqueRecognitionReceivedTarget;
  }

  public String getLoginActivityTarget()
  {
    return loginActivityTarget;
  }

  public void setLoginActivityTarget( String loginActivityTarget )
  {
    this.loginActivityTarget = loginActivityTarget;
  }

  public String[] getSelectedAudiences()
  {
    return selectedAudiences;
  }

  public void setSelectedAudiences( String[] selectedAudiences )
  {
    this.selectedAudiences = selectedAudiences;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex( int index )
  {
    this.index = index;
  }

  public Long getBenchmarkId()
  {
    return benchmarkId;
  }

  public void setBenchmarkId( Long benchmarkId )
  {
    this.benchmarkId = benchmarkId;
  }

  public String[] getNotSelectedAudiences()
  {
    return notSelectedAudiences;
  }

  public void setNotSelectedAudiences( String[] notSelectedAudiences )
  {
    this.notSelectedAudiences = notSelectedAudiences;
  }

}

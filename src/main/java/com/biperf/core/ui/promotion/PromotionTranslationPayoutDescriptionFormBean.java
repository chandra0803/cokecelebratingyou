/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion;

/**
 * 
 * @author kancherl
 * @since July 19, 2016
 */
public class PromotionTranslationPayoutDescriptionFormBean
{
  private Long levelIndex;
  private String payoutDescription;

  public Long getLevelIndex()
  {
    return levelIndex;
  }

  public void setLevelIndex( Long levelIndex )
  {
    this.levelIndex = levelIndex;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

}

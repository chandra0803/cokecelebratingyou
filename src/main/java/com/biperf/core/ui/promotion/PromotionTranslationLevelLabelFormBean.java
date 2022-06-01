
package com.biperf.core.ui.promotion;

import java.io.Serializable;

public class PromotionTranslationLevelLabelFormBean implements Serializable
{
  private Long levelIndex;
  private String levelLabel;

  public String getLevelLabel()
  {
    return levelLabel;
  }

  public void setLevelLabel( String levelLabel )
  {
    this.levelLabel = levelLabel;
  }

  public Long getLevelIndex()
  {
    return levelIndex;
  }

  public void setLevelIndex( Long levelIndex )
  {
    this.levelIndex = levelIndex;
  }
}

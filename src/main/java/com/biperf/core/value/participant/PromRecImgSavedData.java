/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.participant;

public class PromRecImgSavedData
{
  private Long promotionId;
  private String defContAvatarUrl;
  private String defCeleAvatarUrl;

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getDefContAvatarUrl()
  {
    return defContAvatarUrl;
  }

  public void setDefContAvatarUrl( String defContAvatarUrl )
  {
    this.defContAvatarUrl = defContAvatarUrl;
  }

  public String getDefCeleAvatarUrl()
  {
    return defCeleAvatarUrl;
  }

  public void setDefCeleAvatarUrl( String defCeleAvatarUrl )
  {
    this.defCeleAvatarUrl = defCeleAvatarUrl;
  }
}

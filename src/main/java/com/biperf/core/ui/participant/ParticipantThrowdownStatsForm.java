/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/Attic/ParticipantThrowdownStatsForm.java,v $
 *
 */

package com.biperf.core.ui.participant;

import java.util.List;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.core.value.ThrowdownPlayerStatsBean;

public class ParticipantThrowdownStatsForm extends BaseForm
{
  private static final long serialVersionUID = 1L;

  private long userId;
  private String promotionId;
  private List<PromotionPaxValue> promotions;
  private ThrowdownPlayerStatsBean playerStats;

  public long getUserId()
  {
    return userId;
  }

  public void setUserId( long userId )
  {
    this.userId = userId;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public ThrowdownPlayerStatsBean getPlayerStats()
  {
    return playerStats;
  }

  public void setPlayerStats( ThrowdownPlayerStatsBean playerStats )
  {
    this.playerStats = playerStats;
  }

  public List<PromotionPaxValue> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( List<PromotionPaxValue> promotions )
  {
    this.promotions = promotions;
  }

}

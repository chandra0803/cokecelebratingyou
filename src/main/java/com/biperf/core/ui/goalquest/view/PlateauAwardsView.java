
package com.biperf.core.ui.goalquest.view;

import java.util.ArrayList;
import java.util.List;

public class PlateauAwardsView
{
  private List<PromotionAwardView> promotions = new ArrayList<PromotionAwardView>();

  public List<PromotionAwardView> getPromotions()
  {
    return promotions;
  }

  public void setPromotions( List<PromotionAwardView> promotions )
  {
    this.promotions = promotions;
  }
}

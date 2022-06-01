
package com.biperf.core.value.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.value.RecognitionBean.BehaviorBean;

@SuppressWarnings( "serial" )
public class PromotionCacheBean implements Serializable
{

  private Promotion promotion = null;
  private List<BehaviorBean> behaviors = new ArrayList<BehaviorBean>();

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public List<BehaviorBean> getBehaviors()
  {
    return behaviors;
  }

  public void setBehaviors( List<BehaviorBean> behaviors )
  {
    this.behaviors = behaviors;
  }

}

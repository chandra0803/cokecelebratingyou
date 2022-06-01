/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/challengepoint/ChallengepointModuleAction.java,v $
 */

package com.biperf.core.ui.challengepoint;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.ui.goalquest.GoalQuestModuleAction;
import com.biperf.core.value.PromotionMenuBean;

public class ChallengepointModuleAction extends GoalQuestModuleAction
{
  @Override
  protected boolean isValidGoalQuestPromotionForParticipant( PromotionMenuBean promoMenuBean )
  {
    Promotion promotion = promoMenuBean.getPromotion();
    // return false to all non-challengepoint promotion types and all non-live ones
    if ( !promotion.isChallengePointPromotion() )
    {
      return false;
    }
    if ( !promotion.isLive() )
    {
      return false;
    }
    if ( promoMenuBean.isViewTile() )
    {
      return true;
    }

    return promoMenuBean.isCanReceive() || promoMenuBean.isCanSubmit() || promoMenuBean.isPartner();
  }
  
  @Override
  protected String getModulePromotionType()
  {
    return PromotionType.CHALLENGE_POINT;
  }
}

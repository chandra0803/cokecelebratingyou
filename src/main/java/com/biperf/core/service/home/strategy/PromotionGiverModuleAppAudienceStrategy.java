
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.value.PromotionMenuBean;

public class PromotionGiverModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      if ( promotionMenuBean.getPromotion().isAbstractRecognitionPromotion() && promotionMenuBean.isCanSubmit() )
      {
        return true;
      }
    }

    return false;
  }
}

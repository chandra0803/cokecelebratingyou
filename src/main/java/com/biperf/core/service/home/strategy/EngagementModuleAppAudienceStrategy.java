
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.value.EngagementPromotionData;
import com.biperf.core.value.PromotionMenuBean;

/**
 * 
 * EngagementModuleAppAudienceStrategy.
 * 
 * @author kandhi
 * @since Jun 6, 2014
 * @version 1.0
 */
public class EngagementModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean isUserInAudience = false;
    EngagementPromotionData engagementPromotionData = getEngagementService().getLiveEngagementPromotionData();
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );

    if ( engagementPromotionData != null && engagementPromotionData.getTileDisplayStartDate().compareTo( DateUtils.getCurrentDateTrimmed() ) <= 0 )
    {
      Set<Long> engEligiblePromotion = getEngagementService().getAllEligiblePromotionIds( engagementPromotionData.getPromotionId() );
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( engEligiblePromotion.contains( promotionMenuBean.getPromotion().getId() ) )
        {
          if ( promotionMenuBean.getPromotion().isAbstractRecognitionPromotion() && ( promotionMenuBean.isCanSubmit() || promotionMenuBean.isCanReceive() ) )
          {
            return true;
          }
        }
      }
    }

    return isUserInAudience;
  }
}

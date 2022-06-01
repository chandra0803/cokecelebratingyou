
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.value.PromotionMenuBean;

public class BudgetTrackerModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !participant.isParticipant() )
    {
      return false;
    }

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( parameterMap );
    return getMainContentService().isParticipantHasBudgetMeter( participant.getId(), eligiblePromotions );
  }
}

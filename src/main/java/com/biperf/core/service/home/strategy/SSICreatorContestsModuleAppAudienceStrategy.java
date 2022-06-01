
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.ssi.SSIPromotionService;

public class SSICreatorContestsModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  private SSIPromotionService ssiPromotionService;

  public SSIPromotionService getSsiPromotionService()
  {
    return ssiPromotionService;
  }

  public void setSsiPromotionService( SSIPromotionService ssiPromotionService )
  {
    this.ssiPromotionService = ssiPromotionService;
  }

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    return getSsiPromotionService().isPaxInContestCreatorAudience( participant.getId() );
  }

}

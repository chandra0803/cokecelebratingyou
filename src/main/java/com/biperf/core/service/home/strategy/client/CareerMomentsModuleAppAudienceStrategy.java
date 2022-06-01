package com.biperf.core.service.home.strategy.client;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.home.strategy.AbstractModuleAppAudienceStrategy;
import com.biperf.core.service.home.strategy.ModuleAppAudienceStrategy;
import com.biperf.core.service.home.strategy.StandardModuleAppAudienceStrategy;
import com.biperf.core.utils.UserManager;

public class CareerMomentsModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !UserManager.getUser().isParticipant() )
    {
      return false;
    }
    return super.isUserInAudience( participant, filter, parameterMap );
  }

}

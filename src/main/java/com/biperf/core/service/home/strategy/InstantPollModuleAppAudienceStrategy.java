
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.utils.UserManager;

public class InstantPollModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private InstantPollService instantPollService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    List<Long> intantPollIds = instantPollService.getEligibleInstantPollIds( UserManager.getUserId() );
    if ( !intantPollIds.isEmpty() )
    {
      return true;
    }
    return false;
  }

  public void setInstantPollService( InstantPollService instantPollService )
  {
    this.instantPollService = instantPollService;
  }
}


package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;

public class StandardModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    ModuleApp module = filter.getModuleApp();
    // if no audience, skip it
    if ( module.getAudienceType().isNoneAudienceType() )
    {
      return false;
    }
    // if audience is all, go nuts.
    if ( module.getAudienceType().isAllActivePaxType() )
    {
      return true;
    }
    // check specific audiences for user's membership
    if ( module.getAudiences() != null )
    {
      for ( Audience audience : module.getAudiences() )
      {
        if ( getAudienceService().isParticipantInAudience( participant.getId(), audience ) )
        {
          return true;// in a specified audience, don't need to iterate more
        }
      }
    }
    return false;
  }
}

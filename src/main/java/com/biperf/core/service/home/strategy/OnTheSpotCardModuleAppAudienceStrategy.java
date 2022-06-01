
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

public class OnTheSpotCardModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // if not a pax return false
    if ( !UserManager.getUser().isParticipant() || UserManager.getUser().isSSIAdmin() )
    {
      return false;
    }
    else
    {
      // check the system variable
      if ( !getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal() )
      {
        return false;
      }
      // check for user in onTheSpotCard audience list
      return super.isUserInAudience( participant, filter, parameterMap );
    }
  }
}

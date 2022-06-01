
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;

public class ManagerToolkitModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // only show to participants
    if ( !participant.isParticipant() )
    {
      return false;
    }
    else
    {
      Map<String, Object> options = getFilterAppSetupService().getToolkitOptions( participant, false );
      return (Boolean)options.get( "showToolkit" );
    }
  }
}


package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

public class CommunicationsModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( UserManager.getUser().isParticipant() )
    {
      if ( super.isUserInAudience( participant, filter, parameterMap ) )
      {
        boolean displayManagerSendAlert = getSystemVariableService().getPropertyByName( SystemVariableService.MANAGER_SEND_ALERT ).getBooleanVal();

        boolean isUserInDIYAudience = false;
        String[] audienceRoleCode = { "DIY_BANNER_ADMIN", "DIY_NEWS_ADMIN", "DIY_RESOURCE_ADMIN", "DIY_TIPS_ADMIN" };
        for ( int i = 0; i < audienceRoleCode.length; i++ )
        {
          String roleCode = null;
          roleCode = audienceRoleCode[i];
          // check user exist in any type of diy communication audience
          isUserInDIYAudience = getAudienceService().isUserInDIYCommAudience( participant.getId(), roleCode );
          if ( isUserInDIYAudience )
          {
            break;
          }
        }

        if ( !isUserInDIYAudience && ( participant.isManager() || participant.isOwner() ) && displayManagerSendAlert )
        {
          isUserInDIYAudience = true;
        }

        return isUserInDIYAudience;
      }
      else
      {
        return false;
      }
    }
    else
    {
      return false;
    }
  }
}

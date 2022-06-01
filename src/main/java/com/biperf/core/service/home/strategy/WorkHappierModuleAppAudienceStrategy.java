/**
 * 
 */

package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.system.SystemVariableService;

/**
 * @author poddutur
 *
 */
public class WorkHappierModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean isworkHappierEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.WORK_HAPPIER ).getBooleanVal();

    if ( participant.isParticipant() && super.isUserInAudience( participant, filter, parameterMap ) && isworkHappierEnabled )
    {
      return true;
    }
    return false;
  }

}

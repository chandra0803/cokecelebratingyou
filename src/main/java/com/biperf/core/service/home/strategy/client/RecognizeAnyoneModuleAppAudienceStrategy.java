/**
 * 
 */

package com.biperf.core.service.home.strategy.client;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.home.strategy.ModuleAppAudienceStrategy;
import com.biperf.core.service.home.strategy.StandardModuleAppAudienceStrategy;

/**
 * @author panners
 *
 */
public class RecognizeAnyoneModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( participant.isParticipant())// && super.isUserInAudience( participant, filter, parameterMap ))
    {
      return true;
    }
    return false;
  }

}

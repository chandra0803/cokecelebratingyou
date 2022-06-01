
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;

public class PublicRecognitionModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // if not a pax return false;
    if ( participant.isParticipant() )
    {
      if ( super.isUserInAudience( participant, filter, parameterMap ) )
      {
        boolean displayTile = getPromotionService().displayPublicRecognitionTile();

        if ( displayTile )
        {
          return true;
        }
      }
    }

    return false;
  }
}

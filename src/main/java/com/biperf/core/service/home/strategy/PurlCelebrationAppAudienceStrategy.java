/**
 * 
 */

package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

/**
 * @author poddutur
 *
 */
public class PurlCelebrationAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    // if not a pax return false;
    if ( participant.isParticipant() )
    {
      if ( super.isUserInAudience( participant, filter, parameterMap ) )
      {
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          return true;
        }
        boolean displayTile = getPromotionService().displayPurlCelebrationTile();

        if ( displayTile )
        {
          return true;
        }
      }
    }

    return false;
  }

}

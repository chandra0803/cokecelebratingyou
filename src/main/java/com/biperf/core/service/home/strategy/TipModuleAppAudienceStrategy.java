
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.objectpartners.cms.domain.Content;

public class TipModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean displayFilter = false;
    boolean displayDiyTips = false;

    if ( super.isUserInAudience( participant, filter, parameterMap ) )
    {
      List tips = getMainContentService().getDailyTips();
      if ( tips != null )
      {
        displayFilter = !tips.isEmpty();
      }
    }

    List<Content> diyTips = getParticipantDIYCommunicationsService().getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_TIPS, DIYCommunications.DIY_TIPS_SECTION_CODE );

    if ( diyTips != null && diyTips.size() > 0 )
    {
      displayDiyTips = !diyTips.isEmpty();
    }

    if ( displayFilter || displayDiyTips )
    {
      return true;
    }

    return false;
  }
}

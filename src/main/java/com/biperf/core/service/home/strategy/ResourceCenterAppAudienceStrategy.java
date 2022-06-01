
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class ResourceCenterAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean displayFilter = false;
    boolean displayDiyResourceCenter = false;

    if ( super.isUserInAudience( participant, filter, parameterMap ) )
    {

      ContentReader contentReader = ContentReaderManager.getContentReader();
      List<Object> resourceCenter = (List<Object>)contentReader.getContent( "home.resourceCenter" );

      displayFilter = ! ( null == resourceCenter || resourceCenter.size() == 0 );
    }

    List<Content> diyResourceCenter = getParticipantDIYCommunicationsService().getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_RESOURCE_CENTER,
                                                                                                     DIYCommunications.DIY_RESOURCE_SECTION_CODE );
    if ( diyResourceCenter != null && diyResourceCenter.size() > 0 )
    {
      displayDiyResourceCenter = !diyResourceCenter.isEmpty();
    }

    if ( displayFilter || displayDiyResourceCenter )
    {
      return true;
    }

    return false;
  }
}

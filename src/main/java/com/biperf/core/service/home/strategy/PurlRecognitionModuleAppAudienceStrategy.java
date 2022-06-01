
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;

public class PurlRecognitionModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private PurlService purlService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    boolean isUserInAudience = false;

    // New Service Anniversary Celebration Module Tile Enabling.
    if ( participant.isParticipant() && getSystemVariableService().getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal()
        && !getSystemVariableService().getPropertyByName( SystemVariableService.NEW_SERVICE_ANNIVERSARY ).getBooleanVal() )
    {
      if ( participant.isOwner() && getPurlInvitationsCountForManager( participant.getId() ) > 0 )
      {
        isUserInAudience = true;
      }
    }

    return isUserInAudience;
  }

  private int getPurlInvitationsCountForManager( Long userId )
  {
    int count = 0;
    // TODO replace it with sql query to find out if there are any invitations for the manager
    // current logic is expensive
    List<PurlRecipient> invitations = purlService.getAllPurlInvitationsForManager( userId, null, null );
    if ( invitations != null )
    {
      count = invitations.size();
    }
    return count;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }
}

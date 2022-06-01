
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.ssi.SSIContestParticipantService;

public class SSICreatorModuleAppAudienceStrategy extends StandardModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  private SSIContestParticipantService ssiContestParticipantService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    return ssiContestParticipantService.isPaxContestCreatorSuperViewer( participant.getId() );
  }

  public void setSsiContestParticipantService( SSIContestParticipantService ssiContestParticipantService )
  {
    this.ssiContestParticipantService = ssiContestParticipantService;
  }

}

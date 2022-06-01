
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.claim.NominationClaimService;

public class InProgressNominationModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{

  NominationClaimService nominationClaimService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {

    return nominationClaimService.inProgressNominationClaimExist( participant.getId() );

  }

  public void setNominationClaimService( NominationClaimService nominationClaimService )
  {
    this.nominationClaimService = nominationClaimService;
  }

}

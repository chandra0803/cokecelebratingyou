
package com.biperf.core.service.home.strategy;

import java.util.Map;

import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.claim.NominationClaimService;

public class NominationsApprovalModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private NominationClaimService nominationClaimService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {

    if ( !participant.isParticipant() )
    {
      return false;
    }
    return getNominationClaimService().getNominationApprovalsByClaimCount( participant.getId() ) > 0;
  }

  public NominationClaimService getNominationClaimService()
  {
    return nominationClaimService;
  }

  public void setNominationClaimService( NominationClaimService nominationClaimService )
  {
    this.nominationClaimService = nominationClaimService;
  }
}

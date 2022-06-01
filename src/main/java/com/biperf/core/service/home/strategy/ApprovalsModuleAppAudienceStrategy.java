
package com.biperf.core.service.home.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.value.ssi.SSIContestListValueBean;

public class ApprovalsModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy implements ModuleAppAudienceStrategy
{
  private ClaimService claimService;
  private SSIContestService ssiContestService;
  private SSIContestPaxClaimService ssiContestPaxClaimService;

  @Override
  public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String, Object> parameterMap )
  {
    if ( !participant.isParticipant() )
    {
      return false;
    }
    List<SSIContestListValueBean> waitingForApprovalContests = ssiContestService.getContestWaitingForApprovalByUserId( participant.getId() );
    List<SSIContestListValueBean> waitingForApprovalClaims = ssiContestPaxClaimService.getPaxClaimsWaitingForApprovalByApproverId( participant.getId() );

    return claimService.getClaimsForApprovalByUserCount( participant.getId(), PromotionType.lookup( PromotionType.RECOGNITION ) ) > 0
        || claimService.getUserInClaimsApprovalAudienceCount( participant.getId(), PromotionType.lookup( PromotionType.NOMINATION ) ) > 0
        || getClaimGroupService().getUserInClaimGroupsApprovalAudienceCount( participant.getId(),
                                                                             PromotionType.lookup( PromotionType.NOMINATION ) ) > 0
        || getClaimGroupService().getClaimGroupsForApprovalByUserCount( participant.getId(),
                                                                        PromotionType.lookup( PromotionType.NOMINATION ) ) > 0
        || claimService.getUserInClaimsApprovalAudienceCount( participant.getId(),
                                                              PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) > 0
        || waitingForApprovalContests != null && waitingForApprovalContests.size() > 0 || waitingForApprovalClaims != null && waitingForApprovalClaims.size() > 0
        || claimService.pastApprovalExist( participant.getId() );
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public SSIContestPaxClaimService getSsiContestPaxClaimService()
  {
    return ssiContestPaxClaimService;
  }

  public void setSsiContestPaxClaimService( SSIContestPaxClaimService ssiContestPaxClaimService )
  {
    this.ssiContestPaxClaimService = ssiContestPaxClaimService;
  }

}

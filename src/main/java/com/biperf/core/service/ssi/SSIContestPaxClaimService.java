
package com.biperf.core.service.ssi;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.SSIFileUpload;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestPaginationValueBean;
import com.biperf.core.value.ssi.SSIContestPaxClaimCountValueBean;

/**
 * 
 * SSIContestPaxClaimService.
 * 
 * @author kancherl
 * @since May 20, 2015
 * @version 1.0
 */

public interface SSIContestPaxClaimService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "ssiContestPaxClaimService";

  public SSIContestPaxClaim getPaxClaimById( Long paxClaimId );

  public SSIContestPaxClaim getPaxClaimByIdWithAssociations( Long paxClaimId, AssociationRequestCollection associationRequestCollection );

  public SSIFileUpload uploadClaimDocument( SSIFileUpload data ) throws ServiceErrorException;

  public SSIContestPaxClaim savePaxClaim( SSIContestPaxClaim paxClaim ) throws ServiceErrorException;

  public void approvePaxClaim( Long paxClaimId, Long approverId ) throws ServiceErrorException;

  public Date approveAllPaxClaims( Long contestId, Long approverId ) throws ServiceErrorException;

  public void approveAllPaxClaimsAndNotify( Long contestId, Long approverId ) throws ServiceErrorException;

  public void denyPaxClaim( Long paxClaimId, Long approverId, String deniedReason ) throws ServiceErrorException;

  public List<SSIContestPaxClaim> getPaxClaimsByContestId( Long contestId );

  public List<SSIContestPaxClaim> getPaxClaimsBySubmitterId( Long contestId, Long submitterId, SSIContestPaginationValueBean paginationParams );

  public int getPaxClaimsCountBySubmitterId( Long contestId, Long submitterId );

  public Double getClaimsActivityAmount( Long contestId, Long submitterId );

  public Map<String, Object> getPaxClaimsByContestIdAndStatus( Long contestId, String claimStatus, int pageNumber, int recordsPerPage, String sortedOn, String SortedBy ) throws ServiceErrorException;

  public SSIContestPaxClaimCountValueBean getPaxClaimsCountByApproverId( Long contestId );

  public SSIContestPaxClaim getPaxClaimByClaimNumber( String claimNumber );

  public SSIContestPaxClaim getPaxClaimByClaimNumberWithAssociations( String claimNumber, AssociationRequestCollection associationRequestCollection );

  public boolean isClaimNumberExists( String claimNumber );

  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndStatus( Long contestId, List<SSIClaimStatus> claimStatuses );

  public List<SSIContestPaxClaim> getPaxClaimsByApproverId( Long approverId );

  public List<SSIContestListValueBean> getPaxClaimsWaitingForApprovalByApproverId( Long approverId );

  public List<SSIContestListValueBean> getPaxClaimsViewAllByApproverId( Long approverId );

  void updateAllPaxClaimsAndStackRank( Long contestId, Long approverId ) throws ServiceErrorException;

  public int updateContestClaims( Long contestId ) throws ServiceErrorException;

  public List<SSIContestPaxClaim> getPaxClaimsByContestIdAndApproveDenyDate( Long contestId, Date approveDenyDate );

  public void notifyClaimSubmitterAndExpireClaimApprovalAlert( Long paxClaimId, Long approverId );

  public Set<Long> getPaxClaimsForApprovalByContestId( List<ParticipantAlert> alertMessageList );
}

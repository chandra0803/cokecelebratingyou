
package com.biperf.core.service.claim;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.CumulativeInfoTableDataValueBean;
import com.biperf.core.value.NominationInProgressModuleBean;
import com.biperf.core.value.NominationsApprovalPageDataValueBean;
import com.biperf.core.value.NominationsApprovalPageTableValueBean;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;

public interface NominationClaimService extends SAO
{
  public final String BEAN_NAME = "nominationClaimService";

  public boolean inProgressNominationClaimExist( Long submitterId );

  public int getInProgressNominationClaimsCount( Long submitterId );

  public Map<String, Object> getNominationClaimsInProgress( Map<String, Object> parameters );

  public int getNominationClaimsSubmittedCountByPeriod( Long timePeriodId, Long submitterId );

  public void saveNominationClaim( Claim claim );

  public RecognitionClaimSubmissionResponse stepNominee( RecognitionClaimSubmission submission, Long groupId ) throws ServiceErrorException;

  public NominationClaim buildNomClaimBasics( NominationPromotion promotion, RecognitionClaimSubmission submission );

  /**
   * Save information from behavior step
   */
  public void stepBehavior( RecognitionClaimSubmission submission, boolean isEditMode );

  /**
   * Save information from ecard step
   */
  public void stepEcard( RecognitionClaimSubmission submission );

  public void stepWhy( RecognitionClaimSubmission submission, boolean isDraft ) throws ServiceErrorException;

  public RecognitionClaimSubmissionResponse submitClaim( RecognitionClaimSubmission submission, NominationClaimStatusType statusType, String individualOrTeam, int stepNumber )
      throws ServiceErrorException;

  public int getNominationClaimsSubmittedCount( Long timePeriodId, Long submitterId, Long recipientId );

  public PendingNominationsApprovalMainValueBean getPendingNominationClaimsForApprovalByUser( Map<String, Object> parameters );

  public Set<ProductClaimParticipant> buildTeamMembers( RecognitionClaimSubmission submission );

  /**
   * For every recipient of the nomination, change their approval status to non-winner.
   * If claimGroupId is provided, claimId is ignored - the claims in the group will be used.
   * If only claimId is provided, then only the recipients of that claim will be marked non-winner.
   */
  public void reverseWinnerStatus( Long claimGroupId, Long claimId );

  public NominationInProgressModuleBean getInProgressNominationClaimAndPromotionId( Long userId );

  public NominationsApprovalPageDataValueBean getNominationsApprovalPageData( Map<String, Object> parameters );

  public NominationsApprovalPageTableValueBean getNominationsApprovalPageTableData( Map<String, Object> parameters );

  void addWhyAttachmentUrlReference( Long claimId, String fileUrl, String fileName );

  public void removeWhyAttachment( Long claimId ) throws ServiceErrorException;

  public Map<String, Object> getEligibleNominationPromotionsForApprover( Long userId );

  public String getAwardTypeForCurrentLevel( Long id );

  public Map<String, Object> getNominationPastWinnersList( Map<String, Object> reportParameters );

  public Map<String, Object> getEligiblePastWinnersPromotions( Long userId );

  public Map<String, Object> getNominationPastWinnersDetail( Map<String, Object> parameters );

  public Map<String, Object> getNominationWinnerModalDetails();

  public Map<String, Object> getNominationMyWinnersList( Map<String, Object> parameters );

  public Map<String, Object> getNominationsApprovalPageExtractCsvData( Map<String, Object> parameters );

  public CumulativeInfoTableDataValueBean getCumulativeApprovalNominatorTableData( Map<String, Object> parameters );

  public int getNominationApprovalsByClaimCount( Long userId );

  public RecognitionClaimSubmissionResponse stepNominating( RecognitionClaimSubmission submission, Long groupId ) throws ServiceErrorException;

  int getNominationApprovalsByClaimCountForSideBar( Long userId );

  public List<NameIdBean> getTeamMembersByTeamName( Long teamId );

  public boolean isCardMapped( Long cardId );

}


package com.biperf.core.dao.claim;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.value.CumulativeInfoTableDataValueBean;
import com.biperf.core.value.NominationInProgressModuleBean;
import com.biperf.core.value.NominationsApprovalPageDataValueBean;
import com.biperf.core.value.NominationsApprovalPageTableValueBean;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;

public interface NominationClaimDAO extends DAO
{
  public static final String BEAN_NAME = "nominationClaimDAO";

  public Map<String, Object> getNominationClaimsInProgress( Map<String, Object> parameters );

  public int getClaimsSubmittedCountByPeriod( Long timePeriodId, Long submitterId );

  public int getClaimsSubmittedCountByPeriodAndNominee( Long timePeriodId, Long submitterId, Long recipientId );

  public PendingNominationsApprovalMainValueBean getPendingNominationClaimsForApprovalByUser( Map<String, Object> parameters );

  public NominationInProgressModuleBean getInProgressNominationClaimAndPromotionId( Long userId );

  public int inProgressCount( Long submitterId );

  public NominationsApprovalPageDataValueBean getNominationsApprovalPageData( Map<String, Object> parameters );

  public NominationsApprovalPageTableValueBean getNominationsApprovalPageTableData( Map<String, Object> parameters );

  public Map<String, Object> getEligibleNominationPromotionsForApprover( Long userId );

  public Map<String, Object> getNominationPastWinnersList( Map<String, Object> parameters );

  public Map<String, Object> getEligiblePastWinnersPromotions( Long userId );

  public Map<String, Object> getNominationPastWinnersDetail( Map<String, Object> parameters );

  public Map<String, Object> getNominationWinnerModalDetails();

  public Map<String, Object> getNominationMyWinnersList( Map<String, Object> parameters );

  public Map<String, Object> getNominationsApprovalPageExtractCsvData( Map<String, Object> parameters );

  public CumulativeInfoTableDataValueBean getCumulativeApprovalNominatorTableData( Map<String, Object> parameters );

  public int getNominationApprovalsByClaimCount( Long userId );

  int getNominationApprovalsByClaimCountForSideBar( Long userId );

  public List<NameIdBean> getTeamMembersByTeamName( Long teamId );

  public boolean isCardMapped( Long cardId );

}

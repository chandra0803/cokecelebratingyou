
package com.biperf.core.service.ssi;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestContentValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;

/**
 * 
 * SSIContestAwardThemNowService.
 * 
 * @author kandhi
 * @since Feb 5, 2015
 * @version 1.0
 */
public interface SSIContestAwardThemNowService extends SSIContestService
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "ssiContestAwardThemNowService";

  public int getContestParticipantsCount( Long contestId, Short awardIssuanceNumber );

  public SSIContest updateContestAwardIssuanceNumber( Long contestId );

  public List<SSIContestParticipant> getContestParticipants( Long contestId,
                                                             Short awardIssuanceNumber,
                                                             Integer pageNumber,
                                                             String sortedOn,
                                                             String sortedBy,
                                                             AssociationRequestCollection associationRequestCollection );

  public List<SSIContestParticipant> getContestParticipants( Long contestId, Short awardIssuanceNumber, Integer pageNumber, Integer pageSize );

  public SSIContestAwardThemNow saveContest( SSIContest contest,
                                             SSIContestContentValueBean valueBean,
                                             List<UpdateAssociationRequest> updateAssociationRequests,
                                             Long badgeRuleId,
                                             short issuanceNumber )
      throws ServiceErrorException;

  public void saveContestParticipants( Long contestId, Long[] participantIds, Short awardNumber );

  public void deleteContestParticipant( Long contestId, Long long1, Short awardIssuanceNumber );

  public SSIContest saveContest( SSIContest detachedContest, SSIContestContentValueBean valueBean, List<UpdateAssociationRequest> updateAssociationRequests, Long badgeRuleId )
      throws ServiceErrorException;

  public void deleteContestIssuance( Long long1, Short awardIssuanceNumberFromClientState ) throws ServiceErrorException;

  public void savePayoutAwardThemNow( SSIContestAwardThemNow contestAwardThemNow, List<SSIContestParticipantValueBean> detachedParticipants );

  public SSIContestAwardThemNow getContestAwardThemNowByIdAndIssunace( Long contestId, short issuanceNumber );

  public SSIContestAwardHistoryTotalsValueBean getContestAwardTotals( Long contestId );

  public Map<String, Object> getAllIssuancesForContest( Long contestId, int pageNumber, int recordsPerpage, String sortedOn, String sortedBy ) throws ServiceErrorException;

  public void updateAwardThemNowContestStatus( Long contestId, short issuanceNumber ) throws ServiceErrorException;

  public SSIContestAwardThemNow saveAwardThemNowContest( short awardIssuanceNumber, SSIContest attachedContest );

  public void updateContestParticipant( List<SSIContestParticipantValueBean> detachedParticipants, short issuanceNumber );

  public List<SSIContestParticipant> getAttachedParticipants( List<Long> paxIds, short issuanceNumber );

  public List<SSIContestParticipant> saveParticipantsAndFetchNextPageResults( SSIContest detachedContest,
                                                                              short awardIssuanceNumber,
                                                                              List<SSIContestParticipantValueBean> detachedParticipants,
                                                                              String sortedBy,
                                                                              String sortedOn,
                                                                              int pageNumber )
      throws ServiceErrorException;

  public void updateSameValueForAllPax( Long contestId, short issuanceNumber, String key, SSIContestParticipantValueBean participant );

  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId, Short issuanceNumber ) throws ServiceErrorException;

  public SSIContestAwardThemNow approveContest( Long contestId, Long userId, int levelApproved, Short awardIssuanceNumber ) throws ServiceErrorException;

  public void denyContest( Long contestId, Long userId, int approvalLevel, String denialReson, Short awardIssuanceNumber ) throws ServiceErrorException;

}

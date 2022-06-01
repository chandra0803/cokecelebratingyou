
package com.biperf.core.dao.ssi;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;

/**
 * 
 * SSIContestAwardThemNowDAO.
 * 
 * @author kandhi
 * @since Feb 5, 2015
 * @version 1.0
 */
public interface SSIContestAwardThemNowDAO extends SSIContestDAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "ssiContestAwardThemNowDAO";

  public int getContestParticipantsCount( Long contestId, Short issuanceNumber );

  public void saveContestParticipants( Long contestId, Long[] participantIds, Short awardIssuanceNumber );

  public Short getMaxAwardIssuanceNumber( Long contestId );

  public void deleteContestParticipant( Long contestId, Long paxId, Short awardIssuanceNumber );

  public SSIContestAwardThemNow saveContestAtn( SSIContestAwardThemNow contestAtn );

  public List<SSIContestParticipant> getContestParticipants( Long contestId, Short awardIssuanceNumber, Integer pageNumber, int recordsPerPage, String sortedOn, String sortedBy );

  public void updateAwardThemNowContestStatus( final Long contestId, final Short awardIssuanceNumber, final String status );

  public void updateContestWithAwardIssuanceNumber( Long contestId, Short awardIssuanceNumber );

  void updateAwadThemNowMessage( String message, Long contestId, short issuanceNumber );

  public SSIContestAwardThemNow getContestAwardThemNowByIdAndIssunace( Long contestId, short issuanceNumber );

  public SSIContestAwardHistoryTotalsValueBean getContestAwardTotals( Long contestId );

  public Map<String, Object> getAllIssuancesForContest( Long contestId, int pageNumber, int recordsPerpage, String sortedOn, String sortedBy ) throws ServiceErrorException;

  public List<SSIContestParticipant> getContestParticipants( List<Long> paxIds, short issuanceNumber );

  void updateSameValueForAllPax( Long contestId, short issuanceNumber, String key, SSIContestParticipantValueBean participant );

  SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId, Short issuanceNumber ) throws ServiceErrorException;

  public SSIContestAwardThemNow getContestAwardThemNow( Long contestId, Short awardIssuanceNumber );

  public List<Long> getExistingContestParticipantIds( Long[] participantIds, Long contestId, Short awardIssuanceNumber );

}

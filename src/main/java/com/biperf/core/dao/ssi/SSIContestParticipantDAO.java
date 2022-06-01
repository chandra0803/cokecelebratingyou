
package com.biperf.core.dao.ssi;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestParticipantProgress;
import com.biperf.core.domain.ssi.SSIContestParticipantStackRank;
import com.biperf.core.domain.ssi.SSIContestPaxPayout;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;

public interface SSIContestParticipantDAO extends DAO
{
  public static final String BEAN_NAME = "ssiContestParticipantDAO";

  public SSIContestParticipant getContestParticipantById( Long contestParticipantId );

  public SSIContestParticipantProgress getContestParticipantProgressById( Long contestParticipantProgressId );

  public SSIContestParticipantProgress saveContestParticipantProgress( SSIContestParticipantProgress contestParticipantProgress );

  public Double getParticipantActivityAmoumt( Long contestId, Long participantId );

  public Map<String, Object> getContestParticipantProgressDetail( Long contestId, Long participantId ) throws Exception;

  public List<SSIContestListValueBean> getParticipantLiveContestsValueBean( Long participantId );

  public List<SSIContest> getParticipantLiveContests( Long participantId );

  public List<SSIContestListValueBean> getParticipantArchivedContests( Long participantId );

  public int getContestParticipantsCount( Long contestId );

  public List<SSIContestParticipant> getContestParticipants( Long contestId, Integer pageNumber, Integer pageSize, String sortedOn, String sortedBy );

  public int getContestManagersCount( Long contestId );

  public int getContestSuperViewersCount( Long contestId );

  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, Integer pageSize, String sortedOn, String sortedBy );

  public List<SSIContestSuperViewer> getContestSuperviewers( Long contestId, Integer pageNumber, Integer pageSize, String sortedOn, String sortedBy );

  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId ) throws Exception;

  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId, Short isuanceNumber ) throws Exception;

  public SSIContestParticipantStackRank getContestParticipantStackRankByContestIdAndPaxId( Long contestId, Long participantId );

  public List<SSIContestParticipantStackRank> getAllContestParticipantsStackRankByContestId( Long contestId, int pageNumber, int pageSize, String sortedOn, String sortedBy );

  public SSIContestParticipantStackRank saveContestParticipantsStackRank( SSIContestParticipantStackRank contestParticipantStackRank );

  public List<SSIContestParticipantProgress> getContestParticipantsProgresses( Long contestId, List<Long> paxIds );

  public List<SSIContestParticipantProgressValueBean> getContestParticipantsProgresses( Long contestId,
                                                                                        int currentPage,
                                                                                        int resultsPerPage,
                                                                                        String sortedOn,
                                                                                        String sortedBy,
                                                                                        boolean isDoThisGetThat );

  public Participant getParticipant( Long paxIds );

  public boolean isPaxContestCreator( Long participantId );

  public boolean isPaxInContestPaxAudience( Long participantId );

  public boolean isPaxInContestManagerAudience( Long participantId );

  public List<SSIContestStackRankPaxValueBean> getContestStackRank( Long contestId, Long userId, Long activityId, int currentPage, int resultsPerPage, boolean isTeam, boolean isIncludeAll )
      throws Exception;

  public List<Long> getContestProgressLoadParticipantIdsByImportFileId( Long contestId, Long importFileId );

  public SSIContestParticipant getSSIContestParticipantByPaxId( Long contestId, Long userId );

  public SSIContestPaxPayout saveContestPaxPayout( SSIContestPaxPayout contestPaxPayout );

  public boolean isPaxContestSuperViewer( Long participantId );

}

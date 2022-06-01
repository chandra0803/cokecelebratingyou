
package com.biperf.core.service.ssi;

import java.util.Date;
import java.util.List;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ssi.SSIConetstParticipantActivityValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;

/**
 * 
 * SSIContestParticipantService.
 * 
 * @author kandhi
 * @since Dec 2, 2014
 * @version 1.0
 */
public interface SSIContestParticipantService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "ssiContestParticipantService";

  public SSIContestParticipant getContestParticipantById( Long contestParticipantId );

  public Double getParticipantActivityAmoumt( Long contestId, Long participantId );

  public SSIContestPaxProgressDetailValueBean getContestParticipantProgress( Long contestId, Long participantId ) throws ServiceErrorException;

  /**This is used in contest detail page to pull all the live contests basic info
   * @param participantId
   * @return list of SSIContestListValueBean
   */
  public List<SSIContestListValueBean> getParticipantLiveContestsValueBean( Long participantId );

  /**This is used in tile pages to pull all the live contests
   * @param participantId
   * @return list of SSIContest
   */
  public List<SSIContest> getParticipantLiveContests( Long participantId );

  public List<SSIContestListValueBean> getParticipantArchivedContests( Long participantId );

  public int getContestParticipantsCount( Long contestId );

  public List<SSIContestParticipant> getContestParticipants( Long contestId, Integer pageNumber, String sortedOn, String sortedBy );

  public List<SSIContestParticipant> getContestParticipantsWithAssociations( Long contestId,
                                                                             Integer pageNumber,
                                                                             String sortColumnName,
                                                                             String sortedBy,
                                                                             AssociationRequestCollection associationRequestCollection );

  public int getContestManagersCount( Long contestId );

  public int getContestSuperViewersCount( Long contestId );

  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy, AssociationRequestCollection associationRequestCollection );

  public List<SSIContestSuperViewer> getContestSuperviewers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy, AssociationRequestCollection associationRequestCollection );

  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId ) throws ServiceErrorException;

  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId, Short issuanceNumber ) throws ServiceErrorException;

  public void saveContestParticipantProgress( Long contestId, Date activityDate, List<SSIConetstParticipantActivityValueBean> participantActivities );

  public List<SSIContestParticipantProgressValueBean> getContestParticipantsProgresses( Long contestId,
                                                                                        int currentPage,
                                                                                        int resultsPerPage,
                                                                                        String sortedOn,
                                                                                        String sortedBy,
                                                                                        boolean isDoThisGetThat );

  public void updateSameValueForAllPax( Long contestId, String key, SSIContestParticipantValueBean participant );

  public boolean isPaxContestCreatorSuperViewer( Long participantId );

  public boolean isPaxInContestPaxAudience( Long participantId );

  public boolean isPaxInContestManagerAudience( Long participantId );

  public List<SSIContestStackRankPaxValueBean> getContestStackRank( Long contestId, Long userId, Long activityId, int currentPage, int resultsPerPage, boolean isTeam, boolean isIncludeAll )
      throws ServiceErrorException;

  /**
   * This method used by contest creator to see the progress of the contest
   * @param contestId
   * @return SSIContestProgressValueBean
   */
  public List<SSIContestProgressValueBean> getContestProgress( Long contestId, Long userId ) throws ServiceErrorException;

  public SSIContestProgressValueBean getContestProgressForTile( Long contestId ) throws ServiceErrorException;

  public List<Long> getContestProgressLoadParticipantIdsByImportFileId( Long contestId, Long importFileId ) throws ServiceErrorException;

  public SSIContestParticipant getSSIContestParticipantByPaxId( Long contestId, Long userId );

  public boolean isSuperViewer( SSIContest contest, Long userId );

}

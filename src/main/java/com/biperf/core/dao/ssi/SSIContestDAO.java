
package com.biperf.core.dao.ssi;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxPayout;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestBillCodeBean;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestSummaryValueBean;
import com.biperf.core.value.ssi.SSIContestTranslationsCountValueBean;

/**
 * 
 * SSIContestDAO.
 * 
 * @author kandhi
 * @since Oct 22, 2014
 * @version 1.0
 */
public interface SSIContestDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "ssiContestDAO";

  public SSIContest getContestById( Long contestId );

  public SSIContest saveContest( SSIContest contest );

  public void deleteContest( SSIContest contest );

  public SSIContest getContestByIdWithAssociations( Long contestId, AssociationRequestCollection associationRequestCollection );

  public SSIContestParticipant getContestParticipantByContestIdAndPaxId( Long contestId, Long participantId );

  public List<SSIContestApprover> getContestApproversByIdAndApproverType( Long contestId, SSIApproverType ssiApproverType );

  public List<SSIContestParticipant> getAllContestParticipantsByContestId( Long contestId );

  public List<SSIContestManager> getAllContestManagersByContestId( Long contestId );

  public boolean isContestNameUnique( String contestName, Long currentContestId, String locale );

  public void saveContestParticipants( List<SSIContestParticipant> ssiContestParticipants );

  public void saveContestParticipants( final Long contestId, final Long[] participantIds );

  public void deleteContestParticipant( Long contestId, Long participantId );

  public void saveContestManagers( Long contestId, Long[] managerIds );

  public void saveContestSuperViewers( Long contestId, Long[] superViewerIds );

  public void deleteContestManager( Long contestId, Long managerId );

  public void deleteContestSuperViewer( Long contestId, Long superViewerId );

  public List<String> getAllContestNames();

  public List<SSIContestTranslationsCountValueBean> getContestTranslationsCount( String assetCode );

  public Map<String, Object> getSSIContestManagerList( Long ssiContestId, String locale, String sortedOn, String sortedBy ) throws ServiceErrorException;

  public List<SSIContestNameValueBean> getTranslatedContestNames( String assetCode, String key );

  public List<SSIContestDescriptionValueBean> getTranslatedContestDescriptions( String assetCode, String key );

  public List<SSIContestMessageValueBean> getTranslatedContestMessages( String assetCode, String key );

  public List<SSIContestListValueBean> getManagerLiveContests( Long managerId );

  public List<SSIContestListValueBean> getManagerArchivedContests( Long managerId );

  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId ) throws ServiceErrorException;

  public Long calculatePayoutDoThisGetThatTotals( Long contestId );

  public List<SSIContestParticipant> getContestParticipants( List<Long> paxIds );

  public List<SSIContestListValueBean> getContestListByCreatorSuperViewer( Long creatorId );

  public SSIContestActivity getContestActivityById( Long contestActivityId );

  public List<SSIContestActivity> getContestActivitiesByContestId( Long contestId );

  public List<SSIContestListValueBean> getArchivedContestListByCreator( Long creatorId );

  public List<SSIContestListValueBean> getDeniedContestListByCreator( Long creatorId );

  public List<SSIContest> getCreatorLiveContests( Long creatorId );

  public List<SSIContestListValueBean> getAwardThemNowContestSuperViewer( Long userId );

  public void updatePayout( boolean resetObjectiveAmount, boolean resetObjectivePayoutDescription, boolean resetBonusFields, boolean resetActivityDescription, boolean resetPayout, Long contestId );

  public SSIContestLevel getContestLevelById( Long contestLevelId );

  public List<SSIContestLevel> getContestLevelsByContestId( Long contestId );

  public Long copyContest( Long sourceContestId, String destinationContestName, String locale ) throws ServiceErrorException;

  public boolean checkUserBelongToContestApproversGroup( Long contestId, Long userId );

  public List<Long> getExistingContestParticipantIds( Long[] paxIds, Long contestId );

  public int getNextSequenceNum( Long contestId );

  public void deleteContestActivity( Long contestId, Long contestActivityId, Long userId ) throws ServiceErrorException;

  public SSIContestActivity saveContestActivity( SSIContestActivity ssiContestActivity );

  public List<Long> getExistingContestManagerIds( Long[] managerIds, Long contestId );

  public List<Long> getExistingContestSuperViewerIds( Long[] superViewerIds, Long contestId );

  public List<SSIContestListValueBean> getContestsWithTodayTileStartDate();

  public List<SSIContestListValueBean> getContestWaitingForApprovalByUserId( Long userId );

  public List<SSIContest> getAllContestsByStatus( List<SSIContestStatus> contestStatuses );

  public List<NameIdBean> getContestNames( Set<Long> contestIds, String locale );

  public Map<String, Object> downloadContestData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public Map<String, Object> downloadContestPayoutData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public Map<String, Object> extractContestClaimData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public SSIContestLevel saveContestLevel( SSIContestLevel ssiContestLevel );

  public void deleteContestLevel( Long contestLevelId, Long contestId, Long userId ) throws ServiceErrorException;

  public void deleteStackRankPayout( Long contestId, Long stackRankPayoutId );

  public SSIContestStackRankPayout saveStackRankPayout( SSIContestStackRankPayout ssiContestStackRankPayout );

  public SSIContestStackRankPayout getStackRankPayoutById( Long stackRankPayoutId );

  public List<SSIContestStackRankPayout> getStackRankPayoutsByContestId( Long contestId );

  public SSIContestSummaryValueBean getParticipantsSummaryData( Long contestId, Long userId, String sortBy, String sortColumnName, int pageNumber, int perPage ) throws ServiceErrorException;

  public SSIContestPayoutsValueBean getContestPayouts( Long contestId, Long contestActivityId, String sortColumnName, String sortBy, Integer pageNumber, Integer perPage ) throws ServiceErrorException;

  public SSIContestPayoutsValueBean saveContestPaxPayouts( Long contestId,
                                                           String userIds,
                                                           String payoutAmounts,
                                                           String payoutDesc,
                                                           String sortColumnName,
                                                           String sortBy,
                                                           Integer pageNumber,
                                                           Integer perPage )
      throws ServiceErrorException;

  public int getNextLevelSequenceNum( Long contestId );

  public void resetAllParticipantsBaseLineAmount( Long contestId ) throws SQLException;

  public SSIContestBaseLineTotalsValueBean calculateBaseLineTotalsForStepItUp( Long contestId );

  public Map<String, Object> getContestProgress( Long contestId, Long userId ) throws ServiceErrorException;

  public Map<String, Object> getContestProgressForTile( Long contestId ) throws ServiceErrorException;

  public void updateSameValueForAllPax( Long contestId, String key, SSIContestParticipantValueBean participant );

  public void updateContestGoalPercentage( Long contestId ) throws ServiceErrorException;

  public void updateContestStackRank( Long contestId ) throws ServiceErrorException;

  public Map<String, Double> getContestActivityTotals( Long contestId );

  public List<DepositProcessBean> getContestUserJournalList( Long contestId, Short awardIssuanceNumber );

  public List<SSIContest> getAllContestsToLaunch( SSIContestStatus contestStatus );

  public Integer getOpenContestCount( Long promotionId, String contestType );

  public SSIContestPayoutStackRankTotalsValueBean getStackRankTotals( Long contestId );

  public Map<String, Object> downloadContestCreatorExtractData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, Integer pageSize, String sortColumnName, String sortOrder );

  public List<SSIContestSuperViewer> getContestSuperViewers( Long contestId, Integer pageNumber, Integer pageSize, String sortColumnName, String sortOrder );

  public boolean approveContestPayouts( SSIContest ssiContest, Long userId, Short awardIssuanceNumber, String csvUserIds, String csvPayoutAmounts ) throws ServiceErrorException;

  public Long getHighestLevelPayout( Long contestId );

  public int getBadgeCountInSsiContest( Long promotionId, Long badgeRuleId );

  public Map<String, Object> getSSIContestUserManagerList( Long ssiContestId, Long paxId ) throws ServiceErrorException;

  public SSIContestClaimField getContestClaimFieldById( Long contestClaimFieldId );

  public List<SSIContestClaimField> getContestClaimFieldsByContestId( Long contestId );

  public int launchNonATNContestArchivalProcess( Date archivalDate ) throws SQLException;

  public int launchATNContestArchivalProcessWithoutApprover( Date archivalDate ) throws SQLException;

  public int launchATNContestArchivalProcessWithApprover( Long ssiContestId ) throws SQLException;

  public List<SSIContest> getExpiredAwardThemNowContest( Date archivalDate );

  public int getWaitingforApprovalAwardThemNowIssuancesCount( Long ssiContestId );

  public Map<String, Object> getContestSRPayoutList( Long contestId ) throws ServiceErrorException;

  public SSIContestPaxPayout getPaxPayout( Long contestId, Long userId );

  public int getRequireContentApproval( Long ssiContestId );

  public List<SSIContest> getContestSearchByAdmin( List<String> assetCodes, List<Long> userIDs, String ssiContestStatus, java.util.Date startDate, java.util.Date endDate );

  public void saveAdminAction( SSIAdminContestActions ssiAdminContestActions );

  public SSIAdminContestActions getAdminActionByEditCreator( Long contestID );

  public Map getContsetPaxManagerSVExtract( Map<String, Object> reportParameters, String ssiType );

  public List<NameableBean> getSSIContestList( String contestType );

  public Map<String, Object> verifyImportFile( Long importFileId, String loadType, Long contestId, String contestType );

  public List<SSIContest> getSuperViewerLiveContests( Long creatorId );

  public List<SSIContestListValueBean> getDeniedContestListBySuperViewer( Long creatorId );

  public List<SSIContestBillCodeBean> getContestBillCodesByContestId( Long contestId );

  public List<SSIContestBillCodeBean> getBillCodesByPromoId( Long promoId );

  public List<SSIContestListValueBean> getArchivedContestListBySuperViewer( Long creatorId );

  public Map getContsetErrorExtract( Map<String, Object> reportParameters );

  public boolean fetchContestCreatorCount( Long userId );

  void updateContestParticipants( SSIContestParticipant contestParticipant );

  void updateContestManagers( SSIContestManager contestManager );

  public List<SSIContestParticipant> getSSIContestATNParticipants( Long contestId, Short awardIssuanceNumber );

  public List<SSIContestParticipant> getSSIContestParticipants( Long contestId );

  public boolean isParticipantInSsiContest( Long contestId, Long userId );

}

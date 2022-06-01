
package com.biperf.core.service.ssi;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.SSIApproverType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.participant.Participant;
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
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestBillCodeBean;
import com.biperf.core.value.ssi.SSIContestContentValueBean;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestDocumentValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestRankValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestSummaryValueBean;
import com.biperf.core.value.ssi.SSIContestTranslationsCountValueBean;

/**
 * @author dudam
 * @since Nov 5, 2014
 * @version 1.0
 */
public interface SSIContestService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "ssiContestService";

  public SSIContest getContestById( Long contestId );

  public SSIContestParticipant getContestParticipantByContestIdAndPaxId( Long contestId, Long participantId );

  public SSIContest saveContest( SSIContest contest, SSIContestContentValueBean valueBean, List<UpdateAssociationRequest> updateAssociationRequests ) throws ServiceErrorException;

  public SSIContest saveContest( SSIContest detachedContest, List<UpdateAssociationRequest> updateAssociationRequests ) throws ServiceErrorException;

  public void sendContestUpdateNotificationToApprovers( Long contestId );

  public SSIContest getContestByIdWithAssociations( Long contestId, AssociationRequestCollection associationRequestCollection );

  public List<SSIContestApprover> getContestApproversByIdAndApproverType( Long contestId, SSIApproverType ssiApproverType );

  public List<SSIContestParticipant> getAllContestParticipantsByContestId( Long contestId );

  public List<SSIContestManager> getAllContestManagersByContestId( Long contestId );

  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy );

  public List<SSIContestSuperViewer> getContestSuperViewers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy );

  public boolean isContestNameUnique( String contestName, Long currentContestId, String locale );

  public void saveContestParticipants( Long contestId, Long[] participantIds );

  public void deleteContestParticipant( Long contestId, Long participantId );

  public void saveContestManagers( Long contestId, Long[] managerIds ) throws ServiceErrorException;

  public void saveContestSuperViewers( Long contestId, Long[] superViewerIds ) throws ServiceErrorException;

  public void deleteContestManager( Long contestId, Long managerId );

  public void deleteContestSuperViewer( Long contestId, Long superViewerId );

  public Map<String, Set<Participant>> getSelectedContestApprovers( Long contestId );

  public List<SSIContestTranslationsCountValueBean> getContestTranslationsCount( String assetCode );

  public List<SSIContestNameValueBean> getTranslatedContestNames( String assetCode, String key );

  public List<SSIContestDescriptionValueBean> getTranslatedContestDescriptions( String assetCode, String key );

  public List<SSIContestMessageValueBean> getTranslatedContestMessages( String assetCode, String key );

  public Map<String, Object> getContestManagersForSelectedPax( Long contestId, String locale, String sortedOn, String sortedBy ) throws ServiceErrorException;

  public List<SSIContestDocumentValueBean> getTranslatedContestDocuments( String assetCode );

  public List<SSIContestListValueBean> getManagerArchivedContests( Long managerId );

  public List<SSIContestListValueBean> getManagerLiveContests( Long managerId );

  public SSIContestPayoutObjectivesTotalsValueBean calculatePayoutObjectivesTotals( Long contestId ) throws ServiceErrorException;

  public Long calculatePayoutDoThisGetThatTotals( Long contestId );

  public SSIContest savePayoutObjectives( SSIContest detachSSIContest, Long badgeId, List<SSIContestParticipantValueBean> participants ) throws ServiceErrorException;

  public List<SSIContestParticipant> saveContestAndFetchNextPageResults( SSIContest ssiContest,
                                                                         Long badgeId,
                                                                         List<SSIContestParticipantValueBean> participants,
                                                                         String sortedBy,
                                                                         String sortedOn,
                                                                         int pageNumber )
      throws ServiceErrorException;

  public List<SSIContestParticipant> saveContestAndFetchNextPageResults( SSIContest ssiContest, List<SSIContestParticipantValueBean> participants, String sortedBy, String sortedOn, int pageNumber )
      throws ServiceErrorException;

  public List<SSIContestParticipant> updatePayout( SSIContest detachSSIContest, Long contestId );

  public List<SSIContestListValueBean> getContestListByCreator( Long creatorId );

  public SSIContestActivity getContestActivityById( Long contestActivityId );

  public List<SSIContestActivity> getContestActivitiesByContestId( Long contestId );

  public List<SSIContestListValueBean> getArchivedContestListByCreator( Long creatorId );

  public List<SSIContestListValueBean> getDeniedContestListByCreator( Long creatorId );

  public List<SSIContest> getCreatorLiveContests( Long creatorId );

  public void deleteContest( Long contestId ) throws ServiceErrorException;

  public SSIContestLevel getContestLevelById( Long contestLevelId );

  public List<SSIContestLevel> getContestLevelsByContestId( Long contestId );

  public Long copyContest( Long sourceContestId, String destinationContestName, String locale ) throws ServiceErrorException;

  public void approveContest( Long contestId, Long userId, Integer approvalLevel ) throws ServiceErrorException;

  public void denyContest( Long contestId, Long userId, Integer approvalLevel, String denialReason ) throws ServiceErrorException;

  public SSIContest savePayoutDoThisGetThat( SSIContest detachedContest ) throws ServiceErrorException;

  public SSIContestActivity saveContestActivity( Long contestId, SSIContestActivity ssiContestActivity );

  public void deleteContestActivity( Long contestId, Long contestActivityId, Long userId ) throws ServiceErrorException;

  public SSIContestActivity updateContestActivity( SSIContestActivity ssiContestActivity );

  public List<SSIContestListValueBean> getContestsWithTodayTileStartDate();

  public void updateContestStatus( Long contestId ) throws ServiceErrorException;

  public List<SSIContest> getAllContestsByStatus( List<SSIContestStatus> contestStatuses );

  /**
   * Gives the total number of contest waiting for approvals based on userid
   * 
   * @param userId
   * @return List of SSIContestListValueBean
   */
  public List<SSIContestListValueBean> getContestWaitingForApprovalByUserId( Long userId );

  public List<SSIContestListValueBean> getApprovalsForListPageByUserId( Long userId );

  public List<NameIdBean> getContestNames( Set<Long> contestIds, String locale );

  public Map<String, Object> downloadContestData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public Map<String, Object> downloadContestPayoutData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public Map<String, Object> extractContestClaimData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public SSIContestLevel saveContestLevel( Long contestId, SSIContestLevel ssiContestLevel, Long badgeRuleId );

  public SSIContestLevel updateContestLevel( SSIContestLevel ssiContestLevel, Long badgeRuleId );

  public SSIContestSummaryValueBean getParticipantsSummaryData( Long contestId, Long userId, String sortBy, String sortColumnName, int pageNumber, int perPage ) throws ServiceErrorException;

  public List<SSIContestParticipant> resetAllParticipantsBaseLineAmount( String individualBaselineType, Long contestId ) throws ServiceErrorException;

  public SSIContestBaseLineTotalsValueBean calculateBaseLineTotalsForStepItUp( Long contestId );

  public List<SSIContestParticipant> updateLevelPayout( SSIContest detachSSIContest, Long contestId );

  public SSIContest savePayoutStepItUp( SSIContest detachedContest, List<SSIContestParticipantValueBean> detachedParticipants ) throws ServiceErrorException;

  public SSIContest savePayoutStackRank( SSIContest detachedContest, List<SSIContestRankValueBean> detachedRanks, List<UpdateAssociationRequest> updateAssociation ) throws ServiceErrorException;

  public SSIContestPayoutStackRankTotalsValueBean getStackRankTotals( Long contestId );

  public SSIContestPayoutsValueBean getContestPayouts( Long contestId, Long contestActivityId, String sortColumnName, String sortBy, Integer rowNumStart, Integer rowNumEnd )
      throws ServiceErrorException;

  public SSIContestPayoutsValueBean saveContestPaxPayouts( Long contestId,
                                                           String userIds,
                                                           String payoutAmounts,
                                                           String payoutDesc,
                                                           String sortColumnName,
                                                           String sortBy,
                                                           Integer rowNumStart,
                                                           Integer rowNumEnd )
      throws ServiceErrorException;

  public void updateContestStackRank( Long contestId ) throws ServiceErrorException;

  public boolean approveContestPayouts( Long ssiContestId, Short awardIssuanceNumber, String csvUserIds, String csvPayoutAmounts ) throws ServiceErrorException;

  public List<SSIContest> getAllContestsToLaunch( SSIContestStatus contestStatus );

  public SSIContest saveContest( SSIContest contest );

  public void setUploadInProgress( Long contestId, boolean isUploadInProgress, String saveAndSendProgressUpdate );

  public void resetUpdateInProgressFlag( Long contestId, boolean isUpdateInProgress );

  public Map<String, Integer> getOpenContestCount( Long promotionId, List<String> contestTypes );

  public void updateContestParticipant( List<SSIContestParticipantValueBean> detachedParticipants );

  public Map<String, Double> getContestActivityTotals( Long contestId );

  public Map<String, Object> downloadContestCreatorExtractData( Map<String, Object> inParameters ) throws ServiceErrorException;

  public void deleteContestLevel( Long contestid, Long contestLevelId, Long userId ) throws ServiceErrorException;

  public List<SSIContestStackRankPayout> getStackRankPayoutsByContestId( Long contestId );

  public Long getHighestLevelPayout( Long contestId );

  public List<SSIContestManager> getContestManagersWithAssociations( Long contestId, Integer currentPage, String sortedOn, String sortedBy, AssociationRequestCollection associationRequestCollection );

  public List<SSIContestSuperViewer> getContestSuperViewersWithAssociations( Long contestId,
                                                                             Integer currentPage,
                                                                             String sortedOn,
                                                                             String sortedBy,
                                                                             AssociationRequestCollection associationRequestCollection );

  public List<DepositProcessBean> getContestUserJournalList( Long ssiContestId, Short awarIssuanceNumber );

  public void launchContestPayoutsDepositProcess( Long contestId, Short awardIssuanceNumber ) throws ServiceErrorException;

  public int getBadgeCountInSsiContest( Long promotionId, Long badgeRuleId );

  public void finalizeContest( Long contestId );

  public Map<String, Object> getContestUserManagersForSelectedPax( Long contestId, Long paxId ) throws ServiceErrorException;

  public SSIContestClaimField getContestClaimFieldById( Long contestClaimFieldId );

  public List<SSIContestClaimField> getContestClaimFieldsByContestId( Long contestId );

  public int launchContestArchivalProcess( SSIPromotion ssiPromotion ) throws ServiceErrorException;

  public List<SSIContestStackRankPayoutValueBean> getContestSRPayoutList( Long contestId ) throws ServiceErrorException;

  public SSIContestPaxPayout getPaxPayout( Long contestId, Long userId );

  public int getRequireContentApprovalProcess( Long contentId );

  public List<SSIContest> getContestSearchByAdmin( List<String> assetCodes, List<Long> creatorIDs, String ssiContestStatus, Date startDate, Date endDate );

  public void saveAdminAction( Long contestID, Long userID, String action, String desc );

  public int getWaitingforApprovalAwardThemNowIssuancesCount( Long ssiID );

  public SSIAdminContestActions getAdminActionByEditCreator( Long contestID );

  public Map getContsetPaxManagerSVExtract( Map<String, Object> reportParameters, String ssiType );

  public List<NameableBean> getSSIContestList( String contestType );

  public Map<String, Object> verifyImportFile( Long importFileId, Long contestId, String contestType );

  public Map<String, Object> importImportFile( Long importFileId, Long contestId, String contestType );

  List<SSIContestBillCodeBean> getContestBillCodesByContestId( Long contestId );

  List<SSIContestBillCodeBean> getBillCodesByPromoId( Long promoId );

  public Map getContsetErrorExtract( Map<String, Object> reportParameters );

  public boolean isContestCreator( Long userId );

  void updateContestParticipants( SSIContestParticipant contestParticipant );

  void updateContestManagers( SSIContestManager contestManager );
  
  public List<SSIContestParticipant> getSSIContestATNParticipants( Long ssiContestId, Short awarIssuanceNumber );

  public List<SSIContestParticipant> getSSIContestParticipants( Long ssiContestId );

}

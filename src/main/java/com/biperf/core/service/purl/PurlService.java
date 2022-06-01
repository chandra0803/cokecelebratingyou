
package com.biperf.core.service.purl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.purl.PurlCelebration;
import com.biperf.core.domain.purl.PurlCelebrationSet;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlContributorMedia;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.value.PurlAwardValue;
import com.biperf.core.value.PurlContributorInviteValue;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.PurlMediaUploadValue;
import com.biperf.core.value.contributor.ContributionsList;
import com.biperf.core.value.contributor.Contributor;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PurlContributorAvatarData;
import com.biperf.core.value.participant.PurlContributorImageData;
import com.biperf.core.value.participant.PurlNotMigratedPaxData;
import com.biperf.core.value.recognition.PurlRecipientValue;

public interface PurlService extends SAO
{
  public static final String BEAN_NAME = "purlService";

  public static final String PURL_RECIPIENT_URL_DELIMITER = "|";

  public static final String PURL_RECIPIENT_URL_NAME = ".";

  public List<Long> getPurlManagerNodes( Long userId );

  public PurlRecipient getPurlRecipientById( Long id );

  public PurlRecipient getPurlRecipientById( Long id, AssociationRequestCollection reqCollection );

  public PurlRecipient savePurlRecipient( PurlRecipient info );

  public PurlRecipient createPurlRecipient( PurlRecipient info ) throws ServiceErrorException;

  public void deletePurlRecipient( PurlRecipient info );

  public PurlContributor getPurlContributorById( Long id );

  public PurlContributor getPurlContributorById( Long id, AssociationRequestCollection associationRequests );

  public PurlContributor getPurlContributorByUserId( Long userId, Long purlRecipientId );

  public PurlContributor getPurlContributorByEmailAddr( String emailAddr, Long purlRecipientId );

  public PurlContributor savePurlContributor( PurlContributor contributor );

  public void deletePurlContributor( PurlContributor contributor );

  public void deletePurlContributor( Long purlContributorId );

  public List<PurlRecipient> getAllPurlInvitationsForManager( Long userId, Long promotionId );

  public List<PurlRecipient> getAllPurlInvitationsForManager( Long userId, Long promotionId, AssociationRequestCollection collectionReq );

  public List<PurlRecipient> getAllPendingPurlInvitations( Long promotionId );

  public List<PurlRecipient> getAllPendingPurlInvitationsForManager( Long userId, Long promotionId );

  public List<PurlRecipient> getAllPendingPurlInvitationsForManager( Long userId, Long promotionId, List<Long> purlManagerNodes );

  public List<PurlRecipient> getAllCurrentPurlInvitationsForManager( Long userId, Long promotionId );

  public List<PurlRecipient> getAllCurrentPurlInvitationsForManager( Long userId, Long promotionId, List<Long> purlManagerNodes );

  public List<PurlContributor> getAllPurlContributions( Long userId, Long promotionId, boolean isDefaultInvitee );

  public List<PurlContributor> getAllPurlContributions( Long userId, Long promotionId, boolean isDefaultInvitee, AssociationRequestCollection collectionReq );

  public List<PurlContributor> getAllPendingPurlContributions( Long promotionId );

  public List<PurlContributor> getAllPendingPurlContributions( Long userId, Long promotionId );

  public List<PurlContributor> getAllPendingPurlContributions( Long userId, Long promotionId, boolean excludeSelfManagerContributions );

  public List<PurlContributor> getAllCurrentPurlContributions( Long userId, Long promotionId, boolean excludeSelfManagerContributions );

  public List<PurlContributorMedia> getPhotoUploads( Long purlRecipientId );

  public List<PurlContributorMedia> getVideoUploads( Long purlRecipientId );

  public List<PurlContributorComment> getComments( Long purlRecipientId );

  public List<PurlContributorComment> getComments( Long purlRecipientId, boolean orderDescending, int rowNumStart, int rowNumEnd );

  public int getPhotoUploadCount( Long purlRecipientId );

  public int getVideoUploadCount( Long purlRecipientId );

  public int getCommentCount( Long purlRecipientId );

  public int getPurlCountributorCount( Long promotionId );

  public PurlContributorMedia getPurlContributorMediaById( Long id );

  public PurlContributorComment getPurlContributorCommentById( Long id );

  public PurlContributorMedia savePurlContributorMedia( PurlContributorMedia media );

  public PurlContributorComment savePurlContributorComment( PurlContributorComment comment );

  public List<PurlRecipient> getAllCompletedPurlRecipients( Long userId, Long promotionId );

  public List<PurlRecipient> getAllPendingPurlRecipients( Long userId, Long promotionId );

  public List<PurlRecipient> getAllCurrentPurlRecipients( Long userId, Long promotionId );

  public List<PurlRecipient> getAllNonExpiredPurlRecipients( Long userId, Long promotionId );

  public String getGiftcodeForRecipient( Long purlRecipientId );

  public List<PurlContributorInviteValue> sendContributorInvitationByContributor( List<PurlContributorInviteValue> contributorInvites, Long purlContributorId );
  
  public List<PurlContributorInviteValue> sendContributorInvitationByManager( Long purlRecipientId, List<PurlContributorInviteValue> inviteList, boolean saveForLater );

  public PurlContributorInviteValue sendContributorInvitationBySubmitter( Long submitterUserId, PurlRecipient purlRecipient, PurlContributorInviteValue invite ) throws ServiceErrorException;

  public PurlContributorComment postCommentForContributor( Long purlContributorId, PurlContributorComment comment ) throws PurlContributorCommentValidationException, ServiceErrorException;

  public PurlContributor updatePurlContributorName( PurlContributor contributor );

  public List<PurlMediaUploadValue> postMediaForContributor( Long purlContributorId, List<PurlMediaUploadValue> mediaUploads );

  public PurlMediaUploadValue postMediaForContributor( Long purlContributorId, PurlMediaUploadValue mediaUpload );

  public PurlFileUploadValue uploadAvatarForContributor( PurlFileUploadValue data, boolean isFromProcess ) throws ServiceErrorException;

  public void postAvatarForContributor( Long purlContributorId, String avatarUrl );

  public boolean deleteAvatarForContributor( Long purlContributorId );

  public PurlFileUploadValue uploadPhotoForContributor( PurlFileUploadValue data ) throws ServiceErrorException;

  public PurlFileUploadValue uploadVideoForContributor( PurlFileUploadValue data ) throws ServiceErrorException;

  public boolean moveFileToWebdav( String mediaUrl );

  public void moveFileToWebdavAsynchronously( String mediaUrl );

  public void sendThankyouToContributors( Long purlRecipientId, String subject, String comments ) throws ServiceErrorException;

  public boolean postPurlUrlOnTwitter( Long userId, Long purlRecipientId );

  public boolean postPurlUrlOnFacebook( Long userId, Long purlRecipientId );

  public List<PurlRecipient> getAllPurlRecipientsForAwardIssuance();

  public PurlAwardValue processAward( Long purlRecipientId ) throws ServiceErrorException;

  public List<PurlRecipient> getAllPurlRecipientsToExpire();

  public boolean processExpirePurl( Long purlRecipientId ) throws ServiceErrorException;

  public List<PurlRecipient> getAllPurlRecipientsToArchive();

  public boolean processArchivePurl( Long purlRecipientId ) throws ServiceErrorException;

  public String createPurlRecipientUrl( PurlRecipient purlRecipient );

  public String getFacebookFeedDialogUrlForPurlPost( PurlRecipient purlRecipient, boolean isMobile );

  /**
   * Search for PURL recipient associated with the given claim id, and return the view url of that recipients PURL page.
   * Will return NULL if no PURL is assocaited with the given claim id.
   * 
   * @param claimId
   * @return String
   */
  public String createPurlRecipientUrlFromClaimId( Long claimId );

  public Participant getNodeOwnerForPurlRecipient( Long purlRecipientId );

  public Participant getNodeOwnerForPurlRecipient( PurlRecipient purlRecipient );

  public Participant getNodeOwnerForPurlRecipient( User purlRecipientUser, Node purlRecipientNode );

  public String createPostPurlToTwitterUrl( PurlRecipient purlRecipient );

  public String createPostPurlToLinkedInUrl( HttpServletRequest request, PurlRecipient purlRecipient );

  public String createPostPurlToChatterUrl( HttpServletRequest request, PurlRecipient purlRecipient );

  public boolean isValidForContribution( Long purlContributorId );

  public List<Promotion> getEligiblePurlPromotionsForInvitation( Long userId );

  public List<Promotion> getEligiblePurlPromotionsForInvitation( Long userId, List<Long> purlManagerNodes );

  public List<Promotion> getEligiblePurlPromotionsForContributor( Long userId );

  public List<Promotion> getEligiblePurlPromotionsForRecipient( Long userId );

  public boolean deleteMediaFromAppDataDir( String filePath );

  public boolean deleteMediaFromWebdav( String filePath );

  public void deleteContributorsNotInvited( Long purlRecipientId, List<PurlContributorInviteValue> inviteList );

  public List<Participant> getPreSelectedContributors( Long recipientUserId, Long recipientNodeId, AssociationRequestCollection associationRequests );

  public List<Participant> getPreSelectedContributors( Long purlRecipientId );

  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId );

  public PurlContributor getContributorStepElementById( Long elementId );

  public boolean isValidForInvitation( Long purlRecipientId );

  public PurlRecipient getPurlRecipientByClaimId( Long claimId );

  public PurlRecipient getPurlRecipientByClaimIdWithAssociations( Long claimId, AssociationRequestCollection associationRequests );

  public List<TranslatedPurlContributorComment> getTranslatedContributorCommentsFor( Long purlRecipientId );

  public boolean validVideoFileData( PurlFileUploadValue data );

  public PurlFileUploadValue uploadVideoForStandardMessage( PurlFileUploadValue data ) throws ServiceErrorException;

  public boolean validFileData( PurlFileUploadValue data );

  public List<PurlCelebrationSet> getUpcomingPurlRecipients( String tabType, int pageSize, String sortedBy, String sortedOn, int startIndex, String lastName, String tile, String listValue )
      throws ServiceErrorException;

  public List<PurlCelebrationSet> getAwardedPurlRecipients( String tabType, int pageSize, String sortedBy, String sortedOn, int startIndex, String lastName, String tile, String listValue )
      throws ServiceErrorException;

  public PurlRecipient getPurlRecipientByCelebrationManagerMessageId( Long id );

  public List<PurlContributor> getPurlContributors( Long managerId, Long purlRecipientId );

  public PurlContributor getPurlContributorByPromotionIdandUserId( Long userId, Long purlRecipientId, Long promotionId );

  public int getPurlRecipientsCountForAutoInvite( Long promotionId, Long purlRecipientId );

  public int getUpcomingPurlRecipientsCount();

  public List<PurlCelebration> getUpcomingPurlRecipients( int pageSize, String sortedBy, String sortedOn, int startIndex ) throws ServiceErrorException;

  public List<ContributionsList> contributorCommentsProcess( List<PurlContributorComment> comments, String finalImagePrefixUrl );

  public Contributor updateParticipant( Participant participant );

  public String getPurlVideoUrl( String ownCardName );

  public List<PurlRecipientValue> getUpComingCelebrationList();

  public int getMaxCommentLength();

  public List<PurlRecipient> getPurlRecipientByUserId( Long userId );

  public List<PurlContributor> getAllPurlContributions( Long purlRecipientId, AssociationRequestCollection collectionReq );

  public List<PaxAvatarData> getNotMigratedPurlContributorAvatarData();

  public void removeAvatarForContributor( Long purlContributorId );

  public List<PurlContributor> getAllPendingPurlContributionsProActive( Long promotionId, Long numberOfDays );

  public void updateMigratedPurlContributorAvatar( Long userId, String avatarUrl );

  public List<PurlContributorImageData> getNotMigratedPurlContributorImgPaxData();

  public void updateMigratedPurlContributorImage( Long purlContributorCommentId, String imageUrl, String imageUrlThumb );

  public int getPurlAwardDate( Long userId, Long purlRecipientId );

  public int getCelebrationAwardDate( Long userId, Long claimId );

  public List<Long> getAllInternalPurlContributorUser();

  public List<PurlContributorAvatarData> getNotSyncPurlContrbUserAvatarData( List<Long> userIds );

  public void updateMigratedPurlContrPaxAvatar( Long userId, String avatarUrl );

  public List<Long> getAllPurlUsersAvatarMigrated( List<Long> userIds );

  public List<PurlNotMigratedPaxData> getNotMigratedPurlUserAvatar( List<Long> userIds );

  public List<Long> getAllPurlContrbUsersToCopyTheUrl();

  public List<PurlContributor> getAllPendingPurlContributionsForDefaultInvitee( Long userId, Long promotionId, boolean excludeSelfManagerContributions );

  public List<PurlContributor> getAllPendingPurlContributionsForNonDefaultInvitee( Long userId, Long promotionId, boolean excludeSelfManagerContributions );
  
  public List getCustomSortOfUpcomingCelebration(  Long charaterticsDivisionId , Long userId , int pageNumber ,int pageSize ); //Customization for the WIP#51332
  
  public List<Participant> getUsersByCharacteristicIdAndValue( Long charId, String charValue );
//custom for wip # 46293
  public void sendPurlContributionEmail( PurlRecipient purlRecipient, PurlContributor purlContributor, Long nonContributorUserId, boolean isAutoInvite);
  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId, Long numberOfDays );
  public void unsubscribeExternalUser( String emailAddress );

  public List<Participant> getNodeMemberForPurlMgrRecipient( Long purlRecipientId );
  
}

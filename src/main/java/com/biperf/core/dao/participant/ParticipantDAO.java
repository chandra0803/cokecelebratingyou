
package com.biperf.core.dao.participant;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.value.client.ClientPublicRecognitionDeptBean;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PaxIndexData;

/**
 * @author crosenquest Apr 27, 2005
 */
public interface ParticipantDAO extends DAO
{
  public static final String BEAN_NAME = "participantDAO";

  /**
   * Saves the participant information to the database.
   * 
   * @param participant
   * @return Participant
   */
  public Participant saveParticipant( Participant participant );

  /**
   * Get all participants.
   * 
   * @return a list of all participants as a <code>List</code> of {@link Participant} objects.
   */
  public List getAll();

  /**
   * Get all active participants.
   * 
   * @return a list of all active participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  public List<Participant> getAllActive();

  public List<Long> getAllActivePaxIds();

  public List<Long> getAllPaxIds();

  /**
   * Get all active participants  and Welcome email not sent .
   * 
   * @return a list of all active participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  public List getAllActiveAndWelcomeEmailNotSent();

  /**
   * Get the participant record from the database using the Id.
   * 
   * @param id
   * @return Participant
   */
  public Participant getParticipantById( Long id );

  /**
   * Get the runtime proxy of participant
   * 
   * @param id
   * @return Participant
   */
  public Participant getParticipantProxyById( Long id );

  /**
   * Get the participant record from the database using the userName.
   * 
   * @param userName
   * @return Participant
   */
  public Participant getParticipantByUserName( String userName );

  /**
   * Get the participant record from the database using the ssn.
   * 
   * @param ssn
   * @return Participant
   */
  public Participant getParticipantBySSN( String ssn );

  /**
   * Get the participant (hydrated for participant overview) from the database using the Id.
   * 
   * @param id
   * @return Participant - Fully hydrated
   * NOTE: Only use this if you need fully hydrated Participant object. 
   * Otherwise use getParticipantByIdWithAssociations
   */
  public Participant getParticipantOverviewById( Long id );

  /**
   * Search the database for the user with the search criteria params.
   * 
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria );

  /**
   * Search the database for the user with the search criteria params.
   * 
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria, boolean isCriteriaStartingWith );

  /**
   * Search the database for the search criteria params.
   * 
   * @param searchCriteria
   * @return List<String>
   */
  public List<String> searchCriteriaAutoComplete( ParticipantSearchCriteria searchCriteria );

  /**
   * Search the database for the search criteria params.
   * 
   * @param searchCriteria
   * @return List
   */
  public List<Participant> searchParticipants( ParticipantSearchCriteria searchCriteria );

  /**
   * Search the database for Address related information from result Participants
   * 
   * @param ids
   * @return
   */
  public List<Participant> populateCountriesForUsers( Long[] ids );

  /**
   * Get all active participants NOT enrolled in the awardBanq system.
   * 
   * @return a list of all active participants NOT enrolled in awardBanq system as a
   *         <code>List</code> of participant ids.
   */
  public List getAllActivePaxNotEnrolledInBanqSystem();

  /**
   * Get all active participants with the specified participantPreferenceCommunicationsType.
   * @param participantPreferenceCommunicationsType
   * @param associationRequestCollection
   * @return a {@link Participant} list.
   */
  public List getAllActivePaxWithCommunicationPreferenceInCampaign( String campaignNumber,
                                                                    ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType,
                                                                    AssociationRequestCollection associationRequestCollection,
                                                                    int pageNumber,
                                                                    int pageSize );

  /**
   * Calls the stored procedure to verify participant file
   * 
   * @param importFileId
   * @param loadType
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, String loadType, Long userId, Long hierarchyId );

  /**
   * @param lastId
   * @param batchSize
   * @return List
   */
  public List getAllParticipantsInAwardbanqSystem( Long lastId, int batchSize );

  /** get most recent participant_employer record and return the termination_date
   * @param userId
   * @return Date
   */
  public Date getCurrentParticipantEmployerTermDate( Long userId );

  /**
   * Get Node owner for a given node.
   * 
   * @return {@link User} object.
   */
  public User getNodeOwner( Long nodeId );

  /**
   * @param nodeId
   * @param roleType
   * @return
   */
  public Set getNodeManager( Long nodeId, String roleType );

  /**
   * @param nodeId
   * @param participantStatus
   * @param excludeUserId
   * @return
   */
  public List rosterSearch( Long nodeId, String participantStatus, Long excludeUserId, int sortField, boolean sortAscending, int pageNumber, int pageSize );

  /**
   * @param nodeId
   * @param participantStatus
   * @param excludeUserId
   * @return
   */
  public Long rosterSearchParticipantCount( Long nodeId, String participantStatus, Long excludeUserId );

  /**
   * @return
   */
  public Map markAsPlateauAwardsOnly();

  /**
   * @param userId
   * @return
   */
  public List<ParticipantSearchView> getParticipatForMiniProfile( Long userId );

  /**
   * @param participantIds
   * @return
   */
  public List<ParticipantSearchView> getParticipatForMiniProfile( String participantIds );

  /**
   * 
   * @param userId
   * @return NodePaxCountValueBean
   * NodePaxCountValueBean contains pax count of the user node and below node pax count 
   */
  public List getNodePaxCount( Long userId );

  /**
   * @param criteria
   * @return long pax count
   */
  public Long getPaxCountBasedOnCriteria( ParticipantSearchCriteria criteria );

  /**
   * Get Participant by SSO Id.
   * 
   * @param ssoId
   * @return Participant
   */
  public List<Participant> getUserBySSOId( String ssoId );

  public List getAllForCharIDAndValue( Long id, String charName );

  public Participant getParticipantByIdWithProjections( Long participantId, ProjectionCollection collection );

  public Participant getParticipantByUserNameWithProjections( String userName, ProjectionCollection collection );

  public List<Participant> getParticipantsByIdWithProjections( List<Long> participantIds, ProjectionCollection collection );

  public boolean isParticipantFollowed( Long userId, Long loggedinUserId );

  public String getLNameFNameByPaxId( Long participantId );

  public Date getActiveHireDate( Long userId );

  public List<Participant> getAllParticipantsByAudienceIds( List<Long> audienceIdList );

  public Map getParticipatForMiniProfile( String promotionId, String userId, String userLocale );

  public Long getPaxCountBasedOnEmailCriteria( ParticipantSearchCriteria paxCriteria );

  public List<Long> getAllActivePaxIdsInCampaignForEstatements( String campaignNumber, String sendOnlyPaxWithPoints, Long startingUserId );

  public Long getPendingNominationCountForApprover( Long approverId );

  public List<Long> getAllEligibleApproversForCustomApproval( Long promotionId );

  public List<PaxIndexData> getParticipantIndexData( List<Long> userIds );

  List<Long> getFollowersByUserId( Long userId );

  List<Participant> getParticipantsIAmFollowing( Long participantId );

  ParticipantFollowers getById( Long participantId, Long followerId ) throws ServiceErrorException;

  long getFollowersCountByUserId( Long userId );

  void addParticipantFollowee( ParticipantFollowers addParticiapantFollowee ) throws ServiceErrorException;

  List<ParticipantFollowers> getParticipantsWhoAreFollowingMe( Long currentUserId );

  void removeParticipantFollowee( ParticipantFollowers removeParticiapantFollowee ) throws ServiceErrorException;

  public List<Long> getAllParticipantIDsByLastName( String creatorLastName );

  public String getLNameFNameByPaxIdWithComma( Long contestOwnerId );

  List<Long> findPaxIdsWhoDisabledPublicProfile( List<Long> forPaxIds );

  public void saveTNCHistory( UserTNCHistory userTNCHistory );

  public boolean isPaxOptedOutOfAwards( Long paxId );

  public List<PaxContactType> getValidUserContactMethodsByEmail( String email );

  /** Get participant data that will be sent to honeycomb for the account sync process */
  public Map<String, Object> getGToHoneycombSyncPaxData( List<Long> userIds );

  public List<PaxContactType> getValidUniqueUserContactMethodsByUserId( Long userId );

  public List<PaxContactType> getValidUserContactMethodsByUserId( Long userId );

  public boolean isParticipantRecoveryContactsAvailable( Long paxId );

  public List<PaxContactType> getValidUniqueUserContactMethodsByEmailOrPhone( String email );

  public List<PaxContactType> getValidUserContactMethodsByPhone( String phoneNumber );

  public List<PaxContactType> getAdditionalContactMethodsByEmailOrPhone( String email );

  public List<PaxContactType> getContactsAutocompleteEmail( String initialQuery, String searchQuery );

  public List<PaxContactType> getContactsAutocompletePhone( String initialQuery, String searchQuery );

  public String getHeroModuleAppAudienceTypeByUserId( Long userId );

  public List<UserNode> getAllManagerAndOwner();

  public Map inActivateBiwUsers( Long runByuserId );

  public List<PaxAvatarData> getNotMigratedPaxAvatarData();

  public void updateMigratedPaxAvatarData( Long userId, String avatarOriginal, String avatarSmall );

  public List<Long> getAllEligibleApproversForCustomApprovalWithOpenClaims( Long promotionId );

  public List<PaxAvatarData> getUpdatedRosterUserIdPaxAvatarData();
  /* coke customization start */
  public boolean isOptedOut( Long userId );
  
  public List<String> getByPositionTypeForAutoComplete( String startsWith );
  
  public List<String> getByDepartmentTypeForAutoComplete( String startsWith );
  /* coke customization end */ 
  // Client customization for WIP #26597 starts
  public String getCharacteristicValueByUserIdAndCharacteristicDescription( Long userId, String characteristicDescription );
  // Client customization for WIP #26597 ends

  // Client customization for wip #26532 starts
  public boolean isAllowePurlOutsideInvites( Long participantId );
  // Client customization for wip #26532 ends
  public List<Participant> getUsersByCharacteristicIdAndValue( Long charId, String charValue );
  
  public List<Participant> getNodeMemberForPurlMgrRecipient( Long purlRecipientId );
  
  public List<ClientPublicRecognitionDeptBean> getAllActiveDepartmentsForPublicRecognition();
}

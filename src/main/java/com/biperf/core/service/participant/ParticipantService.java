/*
 * (c) 2005 BI, Inc. All rights reserved. $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/ParticipantService.java,v $
 */

package com.biperf.core.service.participant;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserFacebook;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.ui.user.UserForm;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.ParticipantUpdateProcessSummaryBean;
import com.biperf.core.value.PurlContributorValueBean;
import com.biperf.core.value.client.ClientPublicRecognitionDeptBean;
import com.biperf.core.value.hc.AccountSyncRequest;
import com.biperf.core.value.hc.ParticipantSyncResponse;
import com.biperf.core.value.participant.ParticipantGroupList;

/**
 * ParticipantService.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>crosenquest</td>
 * <td>May 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface ParticipantService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "participantService";

  /**
   * Saves the association between Participant and Employer.
   * 
   * @param participantId
   * @param participantEmployer
   * @return Participant
   */
  public Participant saveParticipantEmployer( Long participantId, ParticipantEmployer participantEmployer );

  /**
   * 
   * 
   * @param participantId
   * @param participantEmployer
   * @param userForm
   * @return Participant
   */
  public Participant saveParticipantEmployer( Long participantId, ParticipantEmployer participantEmployer, UserForm userForm );

  /**
   * Saves the association between Participant and Employer and sets the termination date of the
   * previously current employment record.
   * 
   * @param participantId
   * @param participantEmployer
   * @param previousTerminationDate
   * @return Participant
   */
  public Participant saveParticipantEmployer( Long participantId, ParticipantEmployer participantEmployer, Date previousTerminationDate );

  /**
   * Saves a participant to the database through the DAO.
   * 
   * @param participant
   * @return Participant
   * @throws ServiceErrorException
   */
  public Participant saveParticipant( Participant participant ) throws ServiceErrorException;

  public Participant updatePaxAuthorizeDate( Participant participant ) throws ServiceErrorException;

  /**
   * Create the participant to the database through the DAO. This is passed a Participant Object
   * with dependent objects also attached. (address, email etc) Some dependent object may need to be
   * looked up (node, employer, characteristci) by the id. The impl will be in charge of how the
   * save/insert should happen.
   * 
   * @param participant
   * @return Participant
   * @throws ServiceErrorException
   */
  public Participant createFullParticipant( Participant participant ) throws ServiceErrorException;

  /**
   * Retrieve all participants.
   * 
   * @return a list of all participants as a <code>List</code> of {@link Participant} objects.
   */
  public List getAll();

  /**
   * Retrieve all ACTIVE participants.
   * 
   * @return a list of all ACTIVE participants as a <code>List</code> of {@link Participant}
   *         objects.
   */
  public List getAllActive();

  public List<Long> getAllActivePaxIds();

  public List<Long> getAllPaxIds();

  /**
   * Update all active participants and set allow estatements value
   * 
   * @return List of ?
   * @param allowEstatements
   */
  public List updateAllActiveAllowEstatements( boolean allowEstatements );

  /**
   * Retrieve all ACTIVE participants with associations.
   * 
   * @return a list of all ACTIVE participants as a <code>List</code> of {@link Participant}
   *         objects.
   * @param reqCollection
   */
  public List<Participant> getAllActiveWithAssociations( AssociationRequestCollection reqCollection );

  /**
   * Retrieve a participant from the database through the DAO using the Id of the participant.
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
   * 
   * @param participantId
   * @return List of ParticipantSearchView   value bean
   */
  public List<ParticipantSearchView> getParticipatForMiniProfile( Long participantId );

  /**
   * 
   * @param array of participantIds
   * @return List of ParticipantSearchView   value bean
   */
  public List<ParticipantSearchView> getParticipatForMiniProfile( String participantIds );

  /**
   * Retrieve a participant (with associations) from the database through the DAO using the Id of
   * the participant.
   * 
   * @param id
   * @param reqCollection
   * @return Participant
   */
  public Participant getParticipantByIdWithAssociations( Long id, AssociationRequestCollection reqCollection );

  /**
   * Retrieve a participant from the database through the DAO using the userName. 
   * @param userName
   * @return Participant
   */
  public Participant getParticipantByUserName( String userName );

  public Participant getParticipantByUserNameWithAssociations( String userName, AssociationRequestCollection associationRequests );

  /**
   * Retrieve a participant from the database through the DAO using the userName. 
   * @param ssn
   * @return Participant
   */
  public Participant getParticipantBySSN( String ssn );

  /**
   * Retrieve a participant hydrated for Participant Overview from the database through the DAO
   * using the Id of the participant.
   * 
   * @param id
   * @return Participant -Fully Hydrated
   * NOTE: Only use this if you need fully hydrated Participant object. 
   * Otherwise use getParticipantByIdWithAssociations
   */
  public Participant getParticipantOverviewById( Long id );

  /**
   * Search the database through the DAO for the user using the search criteria params.
   * 
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria );

  /**
   * Search the database through the DAO for the user using the search criteria params.
   * 
   * @param searchCriteria
   * @return List
   */
  public List searchParticipant( ParticipantSearchCriteria searchCriteria, boolean isCriteriaStartingWith );

  /**
   * Search the database through the DAO for the user using the search criteria params.
   * 
   * @param searchCriteria
   * @return List of Strings
   */
  public List<String> searchCriteriaAutoComplete( ParticipantSearchCriteria searchCriteria );

  /**
   * Search the database through the DAO for the user using the search criteria params.
   * 
   * @param searchCriteria
   * @return List of Participants
   */
  public List<Participant> searchParticipants( ParticipantSearchCriteria searchCriteria );

  /**
   * 
   * Search the database through DAO for Country Address using filtered Participants 
   * 
   * @param ids
   * @return
   */
  public List<Map<Long, CountryValueBean>> populateCountriesForUsers( Long[] ids );

  /**
   * @param criteria
   * @return long pax count
   */
  public Long getPaxCountBasedOnCriteria( ParticipantSearchCriteria criteria );

  /**
   * Get a list of participants through the DAO using the search criteria params. Overridden from
   * 
   * @param searchCriteria
   * @param associationRequests 
   * @return List
   */
  public List searchParticipantWithAssociations( ParticipantSearchCriteria searchCriteria, boolean isCriteriaStartingWith, AssociationRequestCollection associationRequests );

  /**
   * Return the current ParticipantEmployment object. This is the record with either a null, or the
   * latest termination date.
   * 
   * @param id
   * @return ParticipantEmployer
   */
  public ParticipantEmployer getCurrentParticipantEmployer( Long id );

  public ParticipantEmployer getCurrentParticipantEmployer( Participant participant );

  /**
   * Updates the participant preferences on the participant (id) passed in.
   * 
   * @param participantId
   * @param contactMethodTypes
   * @param activeTextMessages
   * @param contactMethods
   * @param language
   * @param emailAddress
   * @param textMessageAddress
   */
  // Client customizations for wip #26532 starts
  public void updateParticipantPreferences( Long participantId,
                                            String[] contactMethodTypes,
                                            String[] activeTextMessages,
                                            String[] contactMethods,
                                            String language,
                                            UserEmailAddress emailAddress,
                                            UserEmailAddress textMessageAddress,
                                            boolean allowPublicRecognition,
                                            boolean allowPublicInformation,
                                            boolean allowPublicBirthDate,
                                            boolean allowPublicHireDate,
                                            boolean allowSharePurlToOutsiders,
                                            boolean allowPurlContributionsToSeeOthers );
  // Client customizations for wip #26532 ends

  /**
   * Get all active participants with the specified participantPreferenceCommunicationsType.
   * 
   * @param participantPreferenceCommunicationsType
   * @param associationRequestCollection
   * @return a {@link Participant} list.
   */
  public List getAllActivePaxWithCommunicationPreferenceInCampaign( String campaignNumber,
                                                                    ParticipantPreferenceCommunicationsType participantPreferenceCommunicationsType,
                                                                    AssociationRequestCollection associationRequestCollectionint,
                                                                    int pageNumber,
                                                                    int pageSize );

  /**
   * Retrieves a list of participants whose centraxId and awardBanqNumber are null
   * 
   * @return participants
   */
  public List getAllParticipantsNotInBanqSystem();

  /**
   * Retrieves a list of participants who are eligible for the given promotion. The boolean flag
   * tells us whether to return paxs from primary audience (giver/submitter) or secondary audience
   * (reciever/team member)
   * 
   * @param promoId
   * @param primaryAudience
   * @return participants
   */
  public Set getAllEligiblePaxForPromotion( Long promoId, boolean primaryAudience );

  /**
   * Retrieves a list of participants who are eligible and Active and Welcome email not sent for the given promotion. The boolean flag
   * tells us whether to return paxs from primary audience (giver/submitter) or secondary audience
   * (reciever/team member)
   * 
   * @param promoId
   * @param primaryAudience
   * @return participants
   */
  public Set getAllEligibleActivePaxForPromotion( Long promoId, boolean primaryAudience );

  /**
   * Retrieve a list of all participants who are eligible fo the given promotion and in
   * the specified node.
   * @param promotionId
   * @param nodeId
   * @return Set
   */
  public Set getAllPaxesInPromotionAndNode( Long promotionId, Long nodeId );

  /**
   * Retrieves a list of participants who have given recognition for the given promotion during the
   * time period provided
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveGivenRecognition( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have NOT given recognition for the given promotion during
   * the time period provided
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveNotGivenRecognition( Long promoId, Date start, Date end );

  public long getUserIdWhoHaveNotGivenAnyClaim( Long promoId, Long paxId, Date start, Date end );

  /**
   * Retrieves a list of participants who have received recognition for the given promotion during
   * the time period provided
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveReceivedRecognition( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have NOT received recognition for the given promotion
   * during the time period provided
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveNotReceivedRecognition( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have used budget for the given promotion during the time
   * period provided. The boolean flag tells us if we should limit participants to those who have
   * used ALL budget.
   * 
   * @param promoId
   * @param start
   * @param end
   * @param usedAll
   * @return participants
   */
  public Set getAllPaxWhoHaveUsedBudget( Long promoId, Date start, Date end, boolean usedAll );

  /**
   * Retrieves a list of participants who have NOT used budget for the given promotion during the
   * time period provided. The boolean flag tells us if we should limit participants to those who
   * have NOT used ALL budget.
   * 
   * @param promoId
   * @param start
   * @param end
   * @param usedAll
   * @return participants
   */
  public Set getAllPaxWhoHaveNotUsedBudget( Long promoId, Date start, Date end, boolean usedAll );

  /**
   * Retrieves a list of participants who have passed the quiz for the given promotion during the
   * time period provided.
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHavePassedQuiz( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have NOT passed the quiz for the given promotion during
   * the time period provided.
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveNotPassedQuiz( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have taken the quiz for the given promotion during the
   * time period provided.
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveTakenQuiz( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have NOT taken the quiz for the given promotion during the
   * time period provided.
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveNotTakenQuiz( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have submitted a claim for the given promotion during the
   * time period provided.
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveSubmittedClaim( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have NOT submitted a claim for the given promotion during
   * the time period provided.
   * 
   * @param promoId
   * @param start
   * @param end
   * @return participants
   */
  public Set getAllPaxWhoHaveNotSubmittedClaim( Long promoId, Date start, Date end );

  /**
   * Retrieves a list of participants who have NOT selected a goal for the given promotion
   * 
   * @param promoId
   * @return participants
   */
  public Set getAllPaxWhoHaveNotSelectedGoal( Long promoId );

  /**
   * Retrieves a list of participants who have NOT selected a challengepoint for the given promotion
   * 
   * @param promoId
   * @return participants
   */
  public Set getAllPaxWhoHaveNotSelectedChallengepoint( Long promoId );

  /**
   * Retrieves a list of participant users for the given set of nodes.  If includeChildNodes is set then the 
   * child nodes will also be searched.
   *  
   * @param nodes 
   * @param includeChildNodes
   * @return Set
   */
  public Set getPaxInNodes( Collection nodes, boolean includeChildNodes );

  /**
   * Verifies the specified hierarchy import file a page at a time.
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, Long userId, Long hierarchyId );

  /**
   * Verifies the specified hierarchy import file a page at a time.
   * 
   * @param importFileId
   * @param userId
   * @return Map result set
   */
  public Map importImportFile( Long importFileId, Long userId );

  // to fix the bug 20668
  /**
   * @param promo
   * @param checkPrimary
   * @return boolean value
   */
  public boolean isAllActivePax( Promotion promo, boolean checkPrimary );

  /**
   * @param lastId
   * @param batchSize
   * @param summary
   * @return Long
   */
  public Long updateParticipantInAwardbanqByBatch( Long lastId, int batchSize, ParticipantUpdateProcessSummaryBean summary ) throws ServiceErrorException; // bug
                                                                                                                                                           // fix:37649

  /**
   * @param userId
   * @param activeTextMessages
   */
  public void updateTextMessagePreferences( Long userId, String[] activeTextMessages );

  /** 
   * get most recent participant_employer record and return the termination_date
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
   * @param userId
   * @param facebook
   */
  public void updateFacebookInfo( Long userId, UserFacebook facebook );

  /**
   * @param nodeId
   * @param participantStatus
   * @param excludeUserId
   * @return
   */
  public List rosterSearch( Long nodeId, String participantStatus, Long excludeUserId, int sortField, boolean sortAscending, int pageNumber, int pageSize );

  /**
   * @param participant
   * @throws ServiceErrorException
   */
  public void rosterUpdateParticipant( Participant participant ) throws ServiceErrorException;

  /**
   * @return
   */
  public Map markAsPlateauAwardsOnly();

  /** 
   * @param userId
   * @return
   */
  public List getNodePaxCount( Long userId );

  /**
   * Get Participant by SSO ID.
   * 
   * @param ssoId
   * @return Participant
   */
  public List<Participant> getUserBySSOId( String ssoId );

  public List getAllForCharIDAndValue( Long id, String charName );

  public Map<User, Set<Participant>> getManagerForCompetitorAudience( Long promoId );

  public Map<User, Set<Participant>> getManagerForCompetitorAudience( Set<Participant> paxs );

  public Participant getParticipantByIdWithProjections( Long participantId, ProjectionCollection collection );

  public Participant getParticipantByUserNameWithProjections( String userName, ProjectionCollection collection );

  public List<Participant> getParticipantsByIdWithProjections( List<Long> participantIds, ProjectionCollection collection );

  public Set getAllPaxWhoHaveNotTakenSurvey( Long promoId, Date start, Date end );

  public boolean isParticipantFollowed( Long userId, Long loggedinUserId );

  /* public List<Participant> getNodeAndBelowForBudget( Long userId, Long nodeId ); */

  public List<Participant> getAllPaxWhoHaveGivenOrReceivedRecognition( Long userId );

  public String getLNameFNameByPaxId( Long participantId );

  public Date getActiveHireDate( Long userId );

  public List<Participant> getAllPreSelectedContributors( Long recipientId );

  public List<PurlContributorValueBean> getAllExistingContributors( Long userId );

  public List<Participant> getAllParticipantsByAudienceIds( List<Long> audienceIdList );

  public Map getParticipatForMiniProfile( String promotionId, String userId, String userLocale );

  public Long getPaxCountBasedOnEmailCriteria( ParticipantSearchCriteria paxCriteria );

  public Integer getPaxCountBasedOnNodes( List<Long> nodesId, Boolean isIncludeChild );

  public ParticipantGroupList getGroupList( Long userId );

  public ParticipantSearchListView getParticipantGroupList( Long groupId, Long promotionId );

  public ParticipantSearchListView getParticipant( Set<ClaimRecipient> recipients, Long promotionId );

  public ParticipantSearchListView getTeamParticipants( Set<ProductClaimParticipant> claimParticipants, Long promotionId );

  public List<Long> getAllActivePaxIdsInCampaignForEstatements( String campaignNumber, String sendOnlyPaxWithPoints, Long startingUserId );

  public Long getPendingNominationCountForApprover( Long approverId );

  public Set getAllPaxWhoHaveNotNominated( Long promoId, Date start, Date end );

  public List<Long> getAllEligibleApproversForCustomApproval( Long promotionId );

  public String getParticipantCurrencyCode( Long paxId );

  public void removeParticipantFollowee( ParticipantFollowers removeParticiapantFollower ) throws ServiceErrorException;

  public void addParticipantFollowee( ParticipantFollowers addParticiapantFollower ) throws ServiceErrorException;

  public ParticipantFollowers getById( Long participantId, Long followerId ) throws ServiceErrorException;

  public List<Long> getFollowersByUserId( Long userId );

  String getParticipantPublicUrl( Long paxId );

  public List<Long> getAllParticipantIDsByLastName( String creatorLastName );

  public String getLNameFNameByPaxIdWithComma( Long contestOwnerId );

  List<Long> findPaxIdsWhoDisabledPublicProfile( List<Long> forPaxIds );

  public Participant saveParticipantWithTNCHistory( Participant participant, UserTNCHistory userTNCHistory ) throws ServiceErrorException;

  public void saveTNCHistory( UserTNCHistory userTNCHistory );

  public boolean isPaxOptedOutOfAwards( Long paxId );

  public List<PaxContactType> getValidUserContactMethodsByEmail( String email );

  /** 
   * Get participant data that will be sent to honeycomb for the account sync process.
   * @return List, element for each participant. Each participant is a map of table name to attribute map, which is column name to value
   */
  public AccountSyncRequest getGToHoneycombSyncPaxData( List<Long> userIds );

  /**
   * Save the participant data for each successfully synced participant after honeycomb account sync
   */
  public void saveAccountSyncResponse( ParticipantSyncResponse syncResponse );

  public List<PaxContactType> getValidUserContactMethodsByUserId( Long userId );

  public List<PaxContactType> getValidUniqueUserContactMethodsByUserId( Long userId );

  public List<PaxContactType> getValidUniqueUserContactMethodsByEmailOrPhone( String emailOrPhone );

  public List<PaxContactType> getValidUserContactMethodsByPhone( String phoneNumber );

  public List<PaxContactType> getAdditionalContactMethodsByEmailOrPhone( String emailOrPhone );

  public boolean isParticipantRecoveryContactsAvailable( Long paxId );

  public List<PaxContactType> getContactsAutocomplete( String initialQuery, String searchQuery );

  public String getHeroModuleAudienceTypeByUserId( Long userId );

  public List<AlertsValueBean> getUnverifiedRecoveryAlerts( Participant participant );

  /**
   * Send a notification mailing about changed recovery contacts.  
   * If only email was changed, send both phone fields as the current value. (It didn't change, so they're the same.)
   * Notification is triggered when:  
   * <li> Existing contact was updated </li>
   * <li> A second method was added </li>
   * <li> A contact was removed </li>
   */
  public void sendRecoveryChangeNotification( Long userId, String previousPhoneCountryCode, String previousPhoneNbr, String newPhoneNbr, String previousEmailAddr, String newEmailAddr );

  public List<UserNode> getAllManagerAndOwner();

  public Map inActivateBiwUsers( Long runByuserId );

  public List<Long> getAllEligibleApproversForCustomApprovalWithOpenClaims( Long promotionId );
  /* coke customization start */
  public boolean isOptedOut( Long userId );

  public Participant saveParticipantUser( Participant participant );
  
  public List<String> getByPositionTypeForAutoComplete( String startsWith );
  
  public List<String> getByDepartmentTypeForAutoComplete( String startsWith );
  /* coke customization end */

  // Client customization for wip #26532 starts
  public boolean isAllowePurlOutsideInvites( Long participantId );
  // Client customization for wip #26532 ends  
  //client customization start - wip 52159
  public List<Participant> getUsersByCharacteristicIdAndValue(Long charId, String charValue );
  //client customization end - wip 52159
  
  public List<Participant> getNodeMemberForPurlMgrRecipient( Long purlRecipientId );
  
  public List<ClientPublicRecognitionDeptBean> getAllActiveDepartmentsForPublicRecognition();
}

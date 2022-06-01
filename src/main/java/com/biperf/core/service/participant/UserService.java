/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/UserService.java,v $
 */

package com.biperf.core.service.participant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.value.EmailDetail;
import com.biperf.core.value.GeneratedUserIdBean;
import com.biperf.core.value.PhoneDetail;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.RAValueBean;
import com.biperf.core.value.UserAddressDetail;
import com.biperf.core.value.UserDivisionValueBean;

/**
 * UserService.
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
 * <td>sharma</td>
 * <td>Apr 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
/**
 * UserService.
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
 * <td>reddy</td>
 * <td>Aug 1, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public interface UserService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "userService";

  /**
   * Updates the information on the userNode param for the user associated to the userId param.
   *
   * @param userId
   * @param nodeId
   * @param role
   * @return UserNode
   */
  public UserNode updateUserNodeRole( Long userId, Long nodeId, HierarchyRoleType role );

  /**
   * Updates the role for the user attached to the userId param.
   *
   * @param userId
   * @param roleList
   */
  public void updateUserRoles( Long userId, List roleList );

  /**
   * Retrieves a list of Nodes which aren't currently assigned to the user which is represented by
   * the userId param.
   *
   * @param userId
   * @return List
   */
  public List getUnassignedNodes( Long userId );

  /**
   * Removes the relationship between the user associated to the userId param and the nodes
   * associated to the String[] of nodeIds.
   *
   * @param userId
   * @param nodeIds
   */
  public void removeUserNodes( Long userId, String[] nodeIds ) throws ServiceErrorException;

  /**
   * Get the nodes assigned to the user.
   *
   * @param userId
   * @return List
   */
  public List getAssignedNodes( Long userId );

  /**
   * Saves the user to the database.
   *
   * @param user
   * @return User
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public User saveUser( User user ) throws UniqueConstraintViolationException, ServiceErrorException;

  /**
   * Gets the user by the id.
   *
   * @param id
   * @return User
   */
  public User getUserById( Long id );

  /**
   * @param id
   * @param associationRequestCollection
   * @return User
   */
  public User getUserByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets the user by the username
   *
   * @param userName
   * @return User
   */
  public User getUserByUserName( String userName );

  /**
   * Gets the user by the username with associations
   *
   * @param userName
   * @param associationRequestCollection
   * @return User
   */
  public User getUserByUserNameWithAssociations( String userName, AssociationRequestCollection associationRequestCollection );

  /**
   * Return a List of all users.
   *
   * @return List of Users
   */
  public List getAllUsers();

  /**
   * Gets a list of users on the node associated to the nodeId param.
   *
   * @param nodeId
   * @return List
   */
  public List getAllUsersOnNode( Long nodeId );

  /**
   * Gets a list of all of the users on a node having the specified hierarchyRoleType.
   *
   * @param nodeId
   * @param hierarchyRoleType
   * @param associationRequestCollection
   * @return List of all of the users on a specific node having the specified hierarchyRoleType.
   */
  public List getAllUsersOnNodeHavingRole( Long nodeId, HierarchyRoleType hierarchyRoleType, AssociationRequestCollection associationRequestCollection );

  /**
   * Return a List of all users that are not participants.
   *
   * @return List of Users
   */
  public List getAllUsersNonParticipant();

  /**
   * Return a list of users which match the search criteria.
   *
   * @param firstName
   * @param lastName
   * @param userName
   * @return List
   */
  public List searchUser( String firstName, String lastName, String userName );

  /**
   * Set the UserDAO through IoC
   *
   * @param userDAO
   */
  public void setUserDAO( UserDAO userDAO );

  /**
   * Return a set of roles available to the user with the id param.
   *
   * @param userId
   * @return Set
   */
  public Set getAvailableRolesByUserId( Long userId );

  /**
   * Return a List of acls available to the user with the id param.
   *
   * @param userId
   * @return Set
   */
  public List getAvailableAclsByUserId( Long userId );

  /**
   * Update the user characteristic
   *
   * @param userId
   * @param userCharacteristic
   */
  public void updateUserCharacteristic( Long userId, UserCharacteristic userCharacteristic );

  /**
   * Update the a list of user characteristics
   *
   * @param userId
   * @param userCharacteristics
   */
  public void updateUserCharacteristics( Long userId, List userCharacteristics );

  /**
   * Call bank services update for updated user characteristics.
   * Bank service call is only made to update bank characteristics, not all user characteristics.
   */
  public void updateBankCharacteristics( Long userId, List<UserCharacteristic> userCharacteristics );

  /**
   * Insert the user characteristic
   *
   * @param userCharacteristic
   * @param userId
   * @return UserCharacteristic
   */
  public UserCharacteristic addUserCharacteristic( Long userId, UserCharacteristic userCharacteristic );

  /**
   * Add a new UserEmailAddress for the user
   *
   * @param userId
   * @param userEmailAddress
   * @return UserEmailAddress
   * @throws ServiceErrorException
   */
  public UserEmailAddress addUserEmailAddress( Long userId, UserEmailAddress userEmailAddress ) throws ServiceErrorException;

  /**
   * Get the primary email address for a user
   *
   * @param userId
   * @return Set
   */
  public UserEmailAddress getPrimaryUserEmailAddress( Long userId );

  /**
   * Get the text message address for a user
   *
   * @param userId
   * @return UserEmailAddress
   */
  public UserEmailAddress getTextMessageAddress( Long userId );

  /**
   * Get the primary email address for a user
   *
   * @param userId
   * @param emailAddressType
   * @throws ServiceErrorException
   */
  public void setPrimaryUserEmailAddress( Long userId, EmailAddressType emailAddressType ) throws ServiceErrorException;

  /**
   * Get the email address list for a user by user id
   *
   * @param userId
   * @return Set
   */
  public Set getUserEmailAddresses( Long userId );

  /**
   * Update the user email address
   *
   * @param userId
   * @param userEmailAddress
   * @throws ServiceErrorException
   */
  public void updateUserEmailAddress( Long userId, UserEmailAddress userEmailAddress ) throws ServiceErrorException;

  /**
   * Delete the user's email address by email type
   *
   * @param userId
   * @param emailAddressType
   * @throws ServiceErrorException
   */
  public void deleteUserEmailAddressbyType( Long userId, EmailAddressType emailAddressType ) throws ServiceErrorException;

  /**
   * Get the phone list for a user by user id
   *
   * @param userId
   * @return Set
   */
  public Set getUserPhones( Long userId );

  /**
   * Get the phone list for a user by user id
   *
   * @param userId
   * @param phoneType
   * @return UserPhone
   */
  public UserPhone getUserPhone( Long userId, String phoneType );

  /**
   * Get the Primary UserPhone
   *
   * @param userId
   * @return UserPhone primary
   */
  public UserPhone getPrimaryUserPhone( Long userId );

  /**
   * Insert the user phone
   *
   * @param userPhone
   * @param userId
   * @return UserPhone
   * @throws ServiceErrorException
   */
  public UserPhone addUserPhone( Long userId, UserPhone userPhone ) throws ServiceErrorException;

  /**
   * Update the user phone
   *
   * @param userPhone
   * @param userId
   * @throws ServiceErrorException
   */
  public void updateUserPhone( Long userId, UserPhone userPhone ) throws ServiceErrorException;

  /**
   * Delete the user's phone by type
   *
   * @param userId
   * @param phoneType
   */
  public void deleteUserPhone( Long userId, String phoneType );

  /**
   * Sets the primary flag to true on the specified phone type
   *
   * @param userId
   * @param phoneType
   * @throws ServiceErrorException
   */
  public void updatePrimaryPhone( Long userId, String phoneType ) throws ServiceErrorException;

  /**
   * Sets the primary flag to true on the specified phone type
   *
   * @param userId
   * @param nodeId
   * @throws ServiceErrorException
   */
  public void updatePrimaryNode( Long userId, Long nodeId ) throws ServiceErrorException;

  /**
   * Get the available phone types by user id
   *
   * @param userId
   * @return List
   */
  public List getAvailablePhoneTypes( Long userId );

  /**
   * Adds a User Address for a given user
   *
   * @param userId
   * @param userAddress
   * @return UserAddress
   * @throws ServiceErrorException
   */
  public UserAddress addUserAddress( Long userId, UserAddress userAddress ) throws ServiceErrorException;

  /**
   * Deletes a User Address for a given userId and addressType
   *
   * @param userId
   * @param addressType
   * @throws ServiceErrorException
   */
  public void deleteUserAddress( Long userId, String addressType ) throws ServiceErrorException;

  /**
   * gets a user address based on userId and addressType
   *
   * @param userId
   * @param addressType
   * @return UserAddress
   */
  public UserAddress getUserAddress( Long userId, String addressType );

  /**
   * Get User Addresses for a specific user
   *
   * @param userId
   * @return Set of UserAddresses
   */
  public Set getUserAddresses( Long userId );

  /**
   * Gets the Primary User Address
   *
   * @param userId
   * @return UserAddress
   */
  public UserAddress getPrimaryUserAddress( Long userId );

  /**
   * Gets the Primary User Address Country
   *
   * @param userId
   * @return Country
   */
  public Country getPrimaryUserAddressCountry( Long userId );

  /**
   * Gets the Primary User Node
   *
   * @param userId
   * @return UserNode
   */
  public UserNode getPrimaryUserNode( Long userId );

  /**
   * Updates the Primary Address to the address with the given address type
   *
   * @param userId
   * @param addressTypeofNewPrimary
   * @throws ServiceErrorException
   */
  public void updatePrimaryAddress( Long userId, String addressTypeofNewPrimary ) throws ServiceErrorException;

  /**
   * Updates a specific User Address
   *
   * @param userId
   * @param userAddress
   * @throws ServiceErrorException
   */
  public void updateUserAddress( Long userId, UserAddress userAddress ) throws ServiceErrorException;

  /**
   * Returns a list of Address Types that are not already in use.
   *
   * @param userId
   * @return List of address types
   */
  public List getAvailableAddressTypes( Long userId );

  /**
   * Get the characteristics list for a user by user id
   *
   * @param userId
   * @return Set
   */
  public Set getUserCharacteristics( Long userId );

  /**
   * Adds a node to the user creating a UserNode
   *
   * @param userId
   * @param userNode
   */
  public void addUserNode( Long userId, UserNode userNode );

  /**
   * Get all the UserNodes for a User.
   *
   * @param userId
   * @return Set of UserNodes for a User
   */
  public Set<UserNode> getUserNodes( Long userId );

  /**
   * Get the userNode identified by the userId and nodeId params.
   *
   * @param userId
   * @param nodeId
   * @return UserNode
   */
  public UserNode getUserNodeByUserIdAndNodeId( Long userId, Long nodeId );

  /**
   * Builds a set of UserNodes given a node id and a list of user ids.
   *
   * @param nodeId
   * @param userIds
   * @return Set of <code>com.biperf.core.domain.user.UserNode</code>
   */
  public Set buildUserNodeSet( Long nodeId, Set userIds );

  /**
   * Generate the password.
   *
   * @return String generated password
   */
  public String generatePassword();

  /**
   * Updates all UserNodes when moving a user from one node to another.
   *
   * @param oldNodeId
   * @param newNodeId
   */
  public void updateUserNodeChangeNode( Long oldNodeId, Long newNodeId );

  /**
   * Updates the list of acls assigned to the user.
   *
   * @param userId
   * @param userAclList
   */
  public void updateUserAcls( Long userId, List userAclList );

  /**
   * Get a list of all acls in the application.
   *
   * @return List
   */
  public List getAllAcls();

  /**
   * Saves the userAcl.
   *
   * @param userAcl
   */
  public void updateUserAcls( UserAcl userAcl );

  /**
   * Removes the list of userAcls from the user.
   *
   * @param userId
   * @param userAclsToRemove
   */
  public void removeUserAcls( Long userId, List userAclsToRemove );

  /**
   * @return List of user value beans
   */
  public List getAllUsersForWelcomeMail();

  public List<RAValueBean> getAllUsersForRAWelcomeMail();

  /**
   * @return PasswordPolicyStrategy
   */
  public PasswordPolicyStrategy getPasswordPolicyStrategy();

  /**
   * @param passwordPolicy
   */
  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicy );

  /**
   * @params userId,status
   */
  public void updateUserNodeStatus( Long userId, Boolean status );

  public List getOwnerNodes( Long userId );

  public List getOwnerNodeIds( Long userId );

  public List getNodeMembersForOwnerNodes( Long userId );

  /**
   * Gets a list of all of the participants on a node.
   *
   * @param nodeId
   * @return List of all of the participants on a specific node
   */
  public List<User> getAllParticipantsOnNode( Long nodeId );

  public void saveLoginInfo( long userId );

  public UserNode getUserNode( long userId, long nodeId );

  public User getUserByEmailAddr( String emailAddr );

  public String getUserTimeZone( Long id );

  public String getUserTimeZoneByUserCountry( Long userId );

  public String getCountryProgramIdByUserId( Long userId );

  public BigDecimal getBudgetMediaValueForUser( Long userId );

  public List<User> getAllUsersBasedOnCharacteristics( Long characteristicId );

  public GeneratedUserIdBean generateUniqueUserIdBean( String firstName, String lastName );

  public String generateUniqueUserId( String firstName, String lastName );

  public boolean isUserNameInUse( String userName );

  public LanguageType getPreferredLanguageFor( Long userId );

  public LanguageType getPreferredLanguageFor( User user );

  public void deleteLoginTokenFor( Long userId );

  BigDecimal getBudgetMediaValueByUserId( final Long userId );

  public List<Long> getAssignedNodesIdList( Long userId );

  String getCountryProgramId( Long userId );

  public Set<Long> getUserIdsByEmailOrPhone( String emailOrPhone );

  public UserEmailAddress getUserEmailAddressById( Long contactId );

  public UserPhone getUserPhoneById( Long contactId );

  // public User reissueSendPassword( User user, User loggedInUser, SecurityPwdRequest
  // securityPwdRequest ) throws UniqueConstraintViolationException, ServiceErrorException;

  public int getPrimaryEmailCountByEmailAddress( String email );

  public boolean isUniqueEmail( String email );

  public boolean isUniquePhoneNumber( String phoneNumber );

  public List<StrongMailUser> getAllStrongMailUsers();

  public void updateStrongMailUserByUserName( StrongMailUser user );

  public void lockUserAccount( Long userId ) throws Exception;

  public String sendRecoveryVerificationMessage( Long userId, ContactType contactType, String emailOrPhone ) throws ServiceErrorException;

  public boolean verifyRecoveryCode( Long userId, String token, ContactType contactType ) throws ServiceErrorException;

  // Facility for the termed user to do the account activation, to redeem his/her points.
  // Anything going wrong these methods has been used to delete the contacts from system.

  public void deleteUserEmailAddress( long userId );

  public void deleteUserPhones( long userId );

  public void deleteEmailAddressForUser( Long userId, Long useremailAddressId );

  public void deletePhoneNumberForUser( Long userId, Long userphoneNumberId );

  public void deleteAddressForUser( Long userId, Long userAddressId );

  public UserAddress getUserAddressById( Long id );

  public User updateUser( User userToSave ) throws UniqueConstraintViolationException, ServiceErrorException;

  public UserEmailAddress updateEmailAddressForUser( Long userId, UserEmailAddress userEmailAddress ) throws ServiceErrorException;

  public UserPhone updatePhoneNumberForUser( Long userId, UserPhone userPhone ) throws ServiceErrorException;

  public UserAddress updateAddressForUser( Long userId, UserAddress userAddress ) throws ServiceErrorException;

  public void deleteCharacteristicForUser( Long userId, Long charctersticId );

  public UserCharacteristic getUserCharacteristicById( Long userId, Long charactersticId );

  public UserCharacteristic updateCharacteristicForUser( Long userId, UserCharacteristic userCharacteristic );

  public List<UserCharacteristic> getUserCharacteristicsByUserId( Long userId );

  public void deleteUser( User userToSave ) throws UniqueConstraintViolationException;

  public User getUserByPrimaryEmailAddr( String emailAddr );

  public void resetUserChatracteristic( Long userId, Long charcteristicId );

  public boolean IsUserAccountLocked( Long userId ) throws Exception;

  public List<PickListValueBean> getPickListValuesFromCM( String assetCode, String locale );

  public PickListValueBean getPickListValueFromCMView( String assetCode, String locale, String code );

  public boolean IsUserinOrgUnitorBelow( Long loggedinUserId, Long searchedUserId );

  public Long getUserIdByRosterUserId( UUID rosterUserId );

  public UUID getRosterUserIdByUserId( Long userId );

  public List<EmailDetail> getEmailDetailsByUserId( Long userId );

  public List<PhoneDetail> getPhoneDetailsByUserId( Long userId );

  public List<UserAddressDetail> getUserAddressDetailsByUserId( Long userId );

  public Long getUserPhoneIdByRosterPhoneId( UUID rosterPhoneId );

  public UUID getRosterPhoneIdByUserPhoneId( Long userPhoneId );

  public Long getEmailAddressIdByRosterEmailId( UUID rosterEmailId );

  public UUID getRosterEmailIdByEmailAddressId( Long emailAddressId );

  public Long getUserAddressIdByRosterAddressId( UUID rosterAddressId );

  public UUID getRosterAddressIdByUserAddressId( Long userAddressId );

  public User getUserByRosterUserId( UUID rosterUserId );

  public UserEmailAddress getUserEmailAddressByRosterEmailId( UUID rosterEmailId );

  public UserPhone getUserPhoneByrosterPhoneId( UUID rosterPhoneId );

  public UserAddress getUserAddressByRosterAddressId( UUID rosterAddressId );

  /*WIP 20160 customization start */
  public Long getUserIdByName(String userName);
  /*WIP 20160 customization end */

  // Client customizations for WIP #42701 starts
  public String getUserCurrencyCharValue( Long userId );
  
  public String getUserDivisionKeyCharValue( Long userId );
  
  public String getUserJobGradeCharValue( Long userId );
  // Client customizations for WIP #42701 ends
 
  // Client customizations for WIP #42683 starts
  public String getUserCountryCharValue( Long userId );

  public String getUserServiceCharValue( Long userId );
  // Client customizations for WIP #42683 ends
  public boolean isUserEmailAddressExists( String emailAddress );
  
  public List<UserDivisionValueBean> getUserDivisionKey(Long sUserId);
} // end UserService

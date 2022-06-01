/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/UserDAO.java,v $
 */

package com.biperf.core.dao.participant;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserLoginInfo;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.value.EmailDetail;
import com.biperf.core.value.PhoneDetail;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.RAValueBean;
import com.biperf.core.value.UserAddressDetail;
import com.biperf.core.value.UserDivisionValueBean;
import com.biperf.core.value.UserValueBean;

/**
 * Interface for the UserDAO functionality.
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
 * <td>tennant</td>
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface UserDAO extends DAO
{

  public static final String BEAN_NAME = "userDAO";

  /**
   * Returns a list of nodes not assigned to the user associated to the ID param.
   * 
   * @param userId
   * @return List
   */
  public List getUnassignedNodes( Long userId );

  /**
   * 
   * @param userId
   * @return List
   */
  public List getAssignedNodeIds( Long userId );

  /**
   * Returns a list of nodes assigned to the user associated to the Id param.
   * 
   * @param userId
   * @return List
   */
  public List getAssignedNodes( Long userId );

  public Country getPrimaryUserAddressCountry( Long userId );

  /**
   * Gets the user from the database by the id.
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
   * Validates the password
   * 
   * @param userName
   * @param password
   * @return boolean isPasswordValid
   */
  /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
  // public boolean isPasswordValid( String userName, String password );
  public boolean isPasswordValid( User user, String password );

  // public boolean isLoginTokenValid( String userName, String loginToken );
  public boolean isLoginTokenValid( User user, String loginToken );

  /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

  /**
   * Saves the user to the database.
   * 
   * @param user
   * @return User
   */
  public User saveUser( User user );

  /**
   * Returns the User based on userName
   * 
   * @param userName
   * @return User
   */
  public User getUserByUserName( String userName );

  /**
   * Returns the User based on userName and associations
   * 
   * @param userName
   * @return User
   */
  public User getUserByUserNameWithAssociations( String userName, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns all users in a List
   * 
   * @return List
   */
  public List getAll();

  /**
   * Returns list of all users that are not participants
   * 
   * @return List
   */
  public List getAllUsersNonParticipant();

  /**
   * Returns a list of users which are found with the search criteria.
   * 
   * @param firstName
   * @param lastName
   * @param userName
   * @return List
   */
  public List searchUser( String firstName, String lastName, String userName );

  /**
   * Returns a set of available roles for a given user.
   * 
   * @param userId
   * @return Set
   */
  public Set getAvailableRoles( Long userId );

  /**
   * Returns a list of available acls for the user record associated to the userId param.
   * 
   * @param userId
   * @return List
   */
  public List getAvailableAcls( Long userId );

  /**
   * Update the UserCharacteristic record. Implementations should take into account the Object tree
   * and update reference to this child in the parent. Hibernate's session.merge will take care of
   * this. TODO Possible move this to a UserCharacteristicDAO???
   * 
   * @param userCharacteristic
   * @return UserCharacteristic
   */
  public UserCharacteristic updateUserCharacteristic( UserCharacteristic userCharacteristic );

  /**
   * Update the UserCharacteristic records. Implementations should take into account the Object tree
   * and update reference to this child in the parent. Hibernate's session.merge will take care of
   * this. TODO Possible move this to a UserCharacteristicDAO???
   * 
   * @param userCharacteristicList
   */
  public void updateUserCharacteristics( List userCharacteristicList );

  /**
   * UserEmailAddress update
   * 
   * @param userEmailAddress
   */
  public void updateUserEmailAddress( UserEmailAddress userEmailAddress );

  /**
   * Update the UserPhone record. Implementations should take into account the Object tree and
   * update reference to this child in the parent. Hibernate's session.merge will take care of this.
   * TODO Possible move this to a UserPhoneDAO???
   * 
   * @param userPhone
   * @return UserPhone
   */
  public UserPhone updateUserPhone( UserPhone userPhone );

  /**
   * Update the UserAddress record. Implementations should take into account the Object tree and
   * update reference to this child in the parent. Hibernate's session.merge will take care of this.
   * TODO Possible move this to a UserAddressDAO???
   * 
   * @param userAddress
   * @return UserAddress
   */
  public UserAddress updateUserAddress( UserAddress userAddress );

  /**
   * Get all the users for whom a welcome email should be sent
   * 
   * @return List of user value beans
   */
  public List getAllUsersForWelcomeMail();

  public List<RAValueBean> getAllUsersForRAWelcomeMail();

  /**
   * Gets a list of all of the users on a node.
   * 
   * @param nodeId
   * @return List of all of the users on a specific node
   */
  public List getAllUsersOnNode( Long nodeId );

  /**
   * Gets a list of all of the users on a node having the specified hierarchyRoleType.
   * 
   * @param nodeId
   * @param hierarchyRoleType
   * @param associationRequestCollection
   * @return List of all of the users on a specific node
   */
  public List getAllUsersOnNodeHavingRole( Long nodeId, HierarchyRoleType hierarchyRoleType, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a list of all of the participants on a node.
   * 
   * @param nodeId
   * @return List of all of the participants on a specific node
   */
  public List getAllParticipantsOnNode( Long nodeId );

  public void saveLoginInfo( UserLoginInfo userLoginInfo );

  public UserNode getUserNode( long userId, long nodeId );

  public User getUserByEmailAddr( String emailAddr );

  public UserValueBean getUserDetailsForWelcomeMail( Long userId );

  public String getUserTimeZone( Long userId );

  public String getCountryProgramIdByUserId( Long userId );

  public List<User> getAllUsersBasedOnCharacteristics( Long characteristicId );

  public String getUserTimeZoneForAdmin();

  public void deleteLoginTokenFor( Long userId );

  public List getAssignedNodesIdList( Long userId );

  public int getAllLoginActivityCount( Long userId );

  String getCountryProgramId( Long userId );

  // public boolean isReissuedSendPasswordExpired( Long userId );

  public Set<Long> getUserIdsByEmail( String emailAddress );

  /** Only looks for phone numbers that an SMS message can be sent to */
  public Set<Long> getUserIdsByContactPhone( String phoneNumber );

  public UserEmailAddress getUserEmailAddressById( Long id );

  public UserPhone getUserPhoneById( Long contactId );

  boolean updateRecoveryEmail( Long userId, String emailAddress );

  boolean updateRecoveryPhone( Long userId, String phoneNumber, String countryPhoneCode );

  public int getPrimaryEmailCountByEmailAddress( String email );

  public boolean isUniqueEmail( String email );

  public boolean isUniquePhoneNumber( String phoneNumber );

  public List<StrongMailUser> getAllStrongMailUsers();

  public void updateStrongMailUserByUserName( StrongMailUser user );

  // Facility for the termed user to do the account activation, to redeem his/her points.
  // Anything going wrong these methods has been used to delete the contacts from system.

  public void deleteUserEmailAddress( long userId );

  public void deleteUserPhones( long userId );

  public void deleteEmailAddressForUser( Long userId, Long useremailAddressId );

  public void deletePhoneNumberForUser( Long userId, Long userphoneNumberId );

  public void deleteAddressForUser( Long userId, Long userAddressId );

  public UserAddress getUserAddressById( Long id );

  public UserEmailAddress updateEmailAddressForUser( UserEmailAddress userEmailAddress );

  public void deleteAttributeForUser( Long userId, Long charctersticId );

  public UserCharacteristic getUserCharacteristicById( Long userId, Long charactersticId );

  public List<UserCharacteristic> getUserCharacteristicsByUserId( Long userId );

  // Introducing new new method since saveUser has many references
  public User save( User user );

  public User getUserByPrimaryEmailAddr( String emailAddr );

  public void resetUserChatracteristic( Long userId, Long charcteristicId );

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
  // Client customizations for WIP #42701 starts
  public String getUserCurrencyCharValue( Long userId );
  
  public String getUserDivisionKeyCharValue( Long userId );
  
  public String getUserJobGradeCharValue( Long userId );
  // Client customizations for WIP #42701 ends
  
  // Client customizations for WIP #42683 starts
  public String getUserCountryCharValue( Long userId );

  public String getUserServiceCharValue( Long userId );
  // Client customizations for WIP #42683 ends
 
  /*WIP 20160  customization start */
  public Long getUserIdByName(String userName);
  /*WIP  20160 customization end */
  public boolean isUserEmailAddressExists( String emailAddress );
  
  public List<UserDivisionValueBean> getUserDivisionKey(Long sUserId);
}

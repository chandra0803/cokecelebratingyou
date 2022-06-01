/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/participant/impl/UserServiceImpl.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.UserTokenDAO;
import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.Acl;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserLoginInfo;
import com.biperf.core.domain.user.UserNameComparator;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.domain.user.UserRole;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.biperf.core.value.EmailDetail;
import com.biperf.core.value.GeneratedUserIdBean;
import com.biperf.core.value.PhoneDetail;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.RAValueBean;
import com.biperf.core.value.UserAddressDetail;
import com.biperf.core.value.UserDivisionValueBean;
import com.biperf.util.StringUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * UserServiceImpl.
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
 *
 */
public class UserServiceImpl implements UserService
{

  private static final org.apache.commons.logging.Log logger = LogFactory.getLog( UserServiceImpl.class );
  /** UserDAO * */
  private UserDAO userDAO;

  /** CharacteristicDAO * */
  private CharacteristicDAO characteristicDAO;

  /** NodeDAO * */
  private NodeDAO nodeDAO;

  /** AclDAO */
  private AclDAO aclDAO;

  /** RoleDAO */
  private RoleDAO roleDAO;

  /** ParticipantDAO * */
  private ParticipantDAO participantDAO;

  /** Awardbanq * */
  private AwardBanQServiceFactory awardBanQServiceFactory;

  /** PasswordPolicyStrategy * */
  private PasswordPolicyStrategy passwordPolicyStrategy = null;

  private SystemVariableService systemVariableService;

  private CountryDAO countryDAO;

  private UserCharacteristicService userCharacteristicService;

  private UserTokenDAO userTokenDAO;

  public void setUserTokenDAO( UserTokenDAO userTokenDAO )
  {
    this.userTokenDAO = userTokenDAO;
  }

  /**
   * Set the RoleDAO through IoC
   *
   * @param roleDAO
   */
  public void setRoleDAO( RoleDAO roleDAO )
  {
    this.roleDAO = roleDAO;
  }

  /**
   * Set the AclDAO through IoC
   *
   * @param aclDAO
   */
  public void setAclDAO( AclDAO aclDAO )
  {
    this.aclDAO = aclDAO;
  }

  /**
   * Set the UserDAO through IoC
   *
   * @param userDAO
   */
  @Override
  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  /**
   * Setter for injecting the CharacteristicDAO
   *
   * @param characteristicDAO
   */
  public void setCharacteristicDAO( CharacteristicDAO characteristicDAO )
  {
    this.characteristicDAO = characteristicDAO;
  }

  /**
   * Setter for injecting the NodeDAO
   *
   * @param nodeDAO
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  public ParticipantDAO getParticipantDAO()
  {
    return participantDAO;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  /**
   * @return PasswordPolicyStrategy
   */
  @Override
  public PasswordPolicyStrategy getPasswordPolicyStrategy()
  {
    return passwordPolicyStrategy;
  }

  /**
   * @param passwordPolicy
   */
  @Override
  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicy )
  {
    this.passwordPolicyStrategy = passwordPolicy;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserById(java.lang.Long)
   * @param id
   * @return User
   */
  @Override
  public User getUserById( Long id )
  {
    return this.userDAO.getUserById( id );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserById(java.lang.Long)
   * @param id
   * @return User
   */
  @Override
  public UserEmailAddress getUserEmailAddressById( Long contactId )
  {
    return this.userDAO.getUserEmailAddressById( contactId );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserById(java.lang.Long)
   * @param id
   * @return User
   */
  @Override
  public UserPhone getUserPhoneById( Long contactId )
  {
    return this.userDAO.getUserPhoneById( contactId );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return User
   */
  @Override
  public User getUserByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.userDAO.getUserByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * Gets the user by the username Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserByUserName(java.lang.String)
   * @param userName
   * @return User
   */
  @Override
  public User getUserByUserName( String userName )
  {
    return this.userDAO.getUserByUserName( userName );
  }

  /**
   * Gets the user by the username Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserByUserName(java.lang.String)
   * @param userName
   * @return User
   */
  @Override
  public User getUserByUserNameWithAssociations( String userName, AssociationRequestCollection associationRequestCollection )
  {
    return this.userDAO.getUserByUserNameWithAssociations( userName, associationRequestCollection );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#saveUser(com.biperf.core.domain.user.User)
   * @param userToSave
   * @return User
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  @Override
  public User saveUser( User userToSave ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    boolean updateAwardbanName = false;

    // TODO This could become a strategy if there are other unique constraints needed

    // Check to see if the user already exists in the database.
    User dbUser = userDAO.getUserByUserName( userToSave.getUserName() );
    if ( dbUser != null )
    {
      // if we found a record in the database with the given username, and our userToSave ID is
      // null,
      // we are trying to insert a duplicate record.
      if ( userToSave.getId() == null )
      {
        throw new UniqueConstraintViolationException();
      }

      // if we found a record in the database with the given username, but the ids are not equal,
      // we are trying to update to a username that already exists so throw a
      // UniqueConstraintViolationException
      if ( dbUser.getId().compareTo( userToSave.getId() ) != 0 )
      {
        throw new UniqueConstraintViolationException();
      }
    }

    // if inserting, need to process child objects after save.
    if ( userToSave.getId() == null )
    {
      // get the children and hold onto them.
      Set addresses = userToSave.getUserAddresses();
      Set phones = userToSave.getUserPhones();
      Set emailAddresses = userToSave.getUserEmailAddresses();
      Set nodes = userToSave.getUserNodes();
      Set userRoles = userToSave.getUserRoles();

      // clear out children for clean insert of user
      userToSave.setUserAddresses( new LinkedHashSet() );
      userToSave.setUserPhones( new LinkedHashSet() );
      userToSave.setUserEmailAddresses( new LinkedHashSet() );
      userToSave.setUserNodes( new LinkedHashSet() );

      userToSave = this.userDAO.saveUser( userToSave );

      for ( Iterator userRolesIter = userRoles.iterator(); userRolesIter.hasNext(); )
      {
        UserRole userRole = (UserRole)userRolesIter.next();
        userToSave.addRole( userRole.getRole() );
      }

      Iterator addressIterator = addresses.iterator();
      while ( addressIterator.hasNext() )
      {
        userToSave.addUserAddress( (UserAddress)addressIterator.next() );
      }

      Iterator phoneIterator = phones.iterator();
      while ( phoneIterator.hasNext() )
      {
        userToSave.addUserPhone( (UserPhone)phoneIterator.next() );
      }

      Iterator emailIterator = emailAddresses.iterator();
      while ( emailIterator.hasNext() )
      {
        userToSave.addUserEmailAddress( (UserEmailAddress)emailIterator.next() );
      }

      Iterator nodeIterator = nodes.iterator();
      while ( nodeIterator.hasNext() )
      {
        UserNode userNode = (UserNode)nodeIterator.next();
        Node node = nodeDAO.getNodeByNameAndHierarchy( userNode.getNode().getName(), userNode.getNode().getHierarchy() );
        if ( node == null )
        {
          List serviceErrors = new ArrayList();
          serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.NODE_BY_NAME_NOT_FOUND, userNode.getNode().getName() ) );
          throw new ServiceErrorExceptionWithRollback( serviceErrors );
        }
        userNode.setNode( node );
        userToSave.addUserNode( userNode );
      }
    }
    else
    {
      // user already in db - see if need to update awardbanq because of a name change
      if ( userToSave instanceof Participant )
      {
        updateAwardbanName = awardbanQNameCheck( userToSave, userToSave.getId() );
      }
    }

    // ------------------
    // Save the User
    // ------------------
    dbUser = this.userDAO.saveUser( userToSave );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanName )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( dbUser.getId() );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }

    // If the user is inactive, then the user's node association's should also be inactive.
    Boolean isActive = dbUser.isActive();
    if ( isActive != null )
    {
      for ( Iterator iter = dbUser.getUserNodes().iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        userNode.setActive( isActive );
      }
    }

    return dbUser;
  }

  /**
   * awardbanQNameCheck
   *
   * @param checkUser
   * @return true - if need to update awardbanQ name false - if do not need to update awardbanQ name
   */
  private boolean awardbanQNameCheck( User checkUser, Long userId )
  {

    // If the name changed
    // and the logged in user is a Participant
    // and the pax has an awardbanq number,
    // then see if the name has changed at all -
    // if it has mark that we need to update the Awardbanq name also
    boolean updateAwardbanQName = false;

    Participant participant = participantDAO.getParticipantOverviewById( userId );
    if ( participant != null )
    {
      if ( participant.getAwardBanqNumber() != null && !participant.getAwardBanqNumber().equals( "" ) )
      {
        if ( !nullCheckAndTrim( participant.getFirstName() ).equals( nullCheckAndTrim( checkUser.getFirstName() ) ) )
        {
          // first names are different
          updateAwardbanQName = true;
        }
        if ( !nullCheckAndTrim( participant.getMiddleName() ).equals( nullCheckAndTrim( checkUser.getMiddleName() ) ) )
        {
          // middle names are different
          updateAwardbanQName = true;
        }
        if ( !nullCheckAndTrim( participant.getLastName() ).equals( nullCheckAndTrim( checkUser.getLastName() ) ) )
        {
          // last names are different
          updateAwardbanQName = true;
        }
      }
    }

    return updateAwardbanQName;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAllUsers()
   * @return List of Users
   */
  @Override
  public List getAllUsers()
  {
    return userDAO.getAll();
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAllUsersNonParticipant()
   * @return List of users that are not participants
   */
  @Override
  public List getAllUsersNonParticipant()
  {
    return userDAO.getAllUsersNonParticipant();
  }

  /**
   * Use the search criteria to find users which similarities. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#searchUser(java.lang.String,
   *      java.lang.String, java.lang.String)
   * @param firstName
   * @param lastName
   * @param userName
   * @return List
   */
  @Override
  public List searchUser( String firstName, String lastName, String userName )
  {
    return userDAO.searchUser( firstName, lastName, userName );
  }

  /**
   * Get a set of available roles for the user with the Id param. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAvailableRolesByUserId(java.lang.Long)
   * @param userId
   * @return Set
   */
  @Override
  public Set getAvailableRolesByUserId( Long userId )
  {
    return userDAO.getAvailableRoles( userId );
  }

  /**
   * Gets a list of available acls for the user with the Id param. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAvailableAclsByUserId(java.lang.Long)
   * @param userId
   * @return List
   */
  @Override
  public List getAvailableAclsByUserId( Long userId )
  {
    return userDAO.getAvailableAcls( userId );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#addUserCharacteristic(java.lang.Long,
   *      com.biperf.core.domain.user.UserCharacteristic)
   * @param userId
   * @param userCharacteristic
   * @return UserCharacteristic
   */
  @Override
  public UserCharacteristic addUserCharacteristic( Long userId, UserCharacteristic userCharacteristic )
  {
    User user = userDAO.getUserById( userId );

    userCharacteristic.setUser( user );
    user.getUserCharacteristics().add( userCharacteristic );

    user = userDAO.saveUser( user );

    return userCharacteristic;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserCharacteristic(java.lang.Long,
   *      com.biperf.core.domain.user.UserCharacteristic)
   * @param userId
   * @param userCharacteristic
   */
  @Override
  public void updateUserCharacteristic( Long userId, UserCharacteristic userCharacteristic )
  {
    User user = userDAO.getUserById( userId );

    userCharacteristic.setUser( user );

    // TODO Possibly call remove and add on the set. Currently, hibernate complains.
    // Call this for now. This is doing a session.merge to update the reference with
    // the value from this userPhone object.
    userDAO.updateUserCharacteristic( userCharacteristic );

  } // updateUserCharacteristic

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserCharacteristics(java.lang.Long,
   *      List)
   * @param userId
   * @param userCharacteristics
   */
  @Override
  public void updateUserCharacteristics( Long userId, List userCharacteristics )
  {
    User user = userDAO.getUserById( userId );

    for ( int i = 0; i < userCharacteristics.size(); i++ )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)userCharacteristics.get( i );
      UserCharacteristicType characteristic = (UserCharacteristicType)characteristicDAO.getCharacteristicById( userCharacteristic.getUserCharacteristicType().getId() );

      userCharacteristic.setUser( user );
      userCharacteristic.setUserCharacteristicType( characteristic );
    }

    // TODO Possibly call remove and add on the set. Currently, hibernate complains.
    // Call this for now. This is doing a session.merge to update the reference with
    // the value from this userPhone object.
    userDAO.updateUserCharacteristics( userCharacteristics );
  }

  @Override
  public void updateBankCharacteristics( Long userId, List<UserCharacteristic> userCharacteristics )
  {
    List<String> bankCharacteristicNames = userCharacteristicService.getBankEnrollmentCharacteristicNames();
    boolean bankUpdateNeeded = false;

    for ( int i = 0; i < userCharacteristics.size(); i++ )
    {
      UserCharacteristic userCharacteristic = userCharacteristics.get( i );
      UserCharacteristicType characteristic = (UserCharacteristicType)characteristicDAO.getCharacteristicById( userCharacteristic.getUserCharacteristicType().getId() );

      // If characteristic is a bank characteristic, flag bank update needed
      if ( bankCharacteristicNames.contains( characteristic.getCharacteristicName() ) )
      {
        bankUpdateNeeded = true;
      }
    }

    // Call bank update if flagged for update
    if ( bankUpdateNeeded )
    {
      Participant participant = participantDAO.getParticipantOverviewById( userId );
      if ( participant != null && participant.getAwardBanqNumber() != null && !participant.getAwardBanqNumber().equals( "" ) )
      {
        try
        {
          awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( participant );
        }
        catch( Exception e )
        {
          logger.error( " Exception" + e );
        }
      }
    }
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#deleteUserEmailAddressbyType(java.lang.Long,
   *      com.biperf.core.domain.enums.EmailAddressType)
   * @param userId
   * @param emailAddressType
   */
  @Override
  public void deleteUserEmailAddressbyType( Long userId, EmailAddressType emailAddressType ) throws ServiceErrorException
  {
    User user = userDAO.getUserById( userId );
    for ( Iterator iter = user.getUserEmailAddresses().iterator(); iter.hasNext(); )
    {
      UserEmailAddress userEmailAddress = (UserEmailAddress)iter.next();
      if ( userEmailAddress.getEmailType().equals( emailAddressType ) )
      {
        if ( !userEmailAddress.isPrimary() )
        {
          iter.remove();
        }
        else
        {
          List serviceErrors = new ArrayList();
          serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.PRIMARY_EMAIL_DELETE_ERR ) );
          throw new ServiceErrorExceptionWithRollback( serviceErrors );
        }
      }
    }
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserEmailAddresses(java.lang.Long)
   * @param userId
   * @return Set
   */
  @Override
  public Set getUserEmailAddresses( Long userId )
  {
    Set userEmailAddressSet = userDAO.getUserById( userId ).getUserEmailAddresses();
    Hibernate.initialize( userEmailAddressSet );
    return userEmailAddressSet;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserEmailAddress(java.lang.Long,
   *      com.biperf.core.domain.user.UserEmailAddress)
   * @param userId
   * @param userEmailAddress
   * @throws ServiceErrorException
   */
  @Override
  public void updateUserEmailAddress( Long userId, UserEmailAddress userEmailAddress ) throws ServiceErrorException
  {
    boolean updateAwardbanQEmail = false;
    User user = userDAO.getUserById( userId );
    userEmailAddress.setUser( user );

    // Check if need to update awardbanq with the email Address
    Boolean isPrimaryEmail = userEmailAddress.getIsPrimary();
    if ( isPrimaryEmail != null )
    {
      if ( userEmailAddress.getIsPrimary().booleanValue() )
      {
        if ( user instanceof Participant )
        {
          updateAwardbanQEmail = awardbanQEmailCheck( userEmailAddress, userId );
        }
      }
    }

    // ------------------
    // update user email
    // ------------------
    userDAO.updateUserEmailAddress( userEmailAddress );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQEmail )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
  }

  /**
   * awardbanQEmailCheck
   *
   * @param checkEmailAddress
   * @return true - if need to update awardbanQ address false - if do not need to update awardbanQ
   *         address
   */
  private boolean awardbanQEmailCheck( UserEmailAddress checkEmailAddress, Long userId )
  {

    // If the email changed is the Primary email
    // and the logged in user is a Participant
    // and the pax has an awardbanq number,
    // then see if the primary email has changed at all -
    // if it has mark that we need to update the Awardbanq email also
    boolean updateAwardbanQEmail = false;

    Participant participant = participantDAO.getParticipantOverviewById( userId );
    if ( participant != null )
    {
      if ( participant.getAwardBanqNumber() != null && !participant.getAwardBanqNumber().equals( "" ) )
      {

        UserEmailAddress currentUserEmail = participant.getPrimaryEmailAddress();
        if ( currentUserEmail != null )
        {
          if ( !nullCheckAndTrim( currentUserEmail.getEmailAddr() ).equals( nullCheckAndTrim( checkEmailAddress.getEmailAddr() ) ) )
          {
            // emails are different
            updateAwardbanQEmail = true;
          }
        }
        else
        {
          // There is not a primary email setup yet, so need to update awardbanQ
          updateAwardbanQEmail = true;
        }
      }
    }

    return updateAwardbanQEmail;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#addUserEmailAddress(java.lang.Long,
   *      com.biperf.core.domain.user.UserEmailAddress)
   * @param userId
   * @param userEmailAddress
   * @return UserEmailAddress
   * @throws ServiceErrorException
   */
  @Override
  public UserEmailAddress addUserEmailAddress( Long userId, UserEmailAddress userEmailAddress ) throws ServiceErrorException
  {
    boolean updateAwardbanQEmail = false;
    User user = userDAO.getUserById( userId );

    // If this is the first email address for this user,
    // force this to be the Primary email address,
    // All other email addresses should be added as Not Primary
    // Per BA's, No exceptions thrown if need to switch primary/non primary
    if ( user.getUserEmailAddresses().size() <= 0 && !userEmailAddress.getEmailType().equals( EmailAddressType.lookup( EmailAddressType.RECOVERY ) ) )
    {
      userEmailAddress.setIsPrimary( Boolean.TRUE );

      // check if will need to update awardbanq email
      if ( user instanceof Participant )
      {
        updateAwardbanQEmail = awardbanQEmailCheck( userEmailAddress, userId );
      }
    }
    else
    {
      userEmailAddress.setIsPrimary( Boolean.FALSE );
    }

    // ------------
    // add email
    // ------------
    user.getUserEmailAddresses().add( userEmailAddress );
    userEmailAddress.setUser( user );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQEmail )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
    return userEmailAddress;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getPrimaryUserEmailAddress(java.lang.Long)
   * @param userId
   * @return UserEmailAddress
   */
  @Override
  public UserEmailAddress getPrimaryUserEmailAddress( Long userId )
  {
    User user = userDAO.getUserById( userId );
    if ( null != user && null !=  user.getUserEmailAddresses() )
    {
    for ( Iterator iter = user.getUserEmailAddresses().iterator(); iter.hasNext(); )
    {
      UserEmailAddress userEmailAddress = (UserEmailAddress)iter.next();
      Boolean isPrimary = userEmailAddress.getIsPrimary();
      if ( isPrimary != null && isPrimary.equals( Boolean.TRUE ) )
      {
        return userEmailAddress;
      }
    }
  }
    return null;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getTextMessageAddress(java.lang.Long)
   * @param userId
   * @return UserEmailAddress
   */
  @Override
  public UserEmailAddress getTextMessageAddress( Long userId )
  {
    User user = userDAO.getUserById( userId );
    for ( Iterator iter = user.getUserEmailAddresses().iterator(); iter.hasNext(); )
    {
      UserEmailAddress address = (UserEmailAddress)iter.next();
      if ( address.getEmailType() != null && address.getEmailType().equals( EmailAddressType.lookup( EmailAddressType.SMS ) ) )
      {
        return address;
      }
    }
    return null;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#setPrimaryUserEmailAddress(java.lang.Long,
   *      com.biperf.core.domain.enums.EmailAddressType)
   * @param userId
   * @param emailAddressType
   * @throws ServiceErrorException
   */
  @Override
  public void setPrimaryUserEmailAddress( Long userId, EmailAddressType emailAddressType ) throws ServiceErrorException
  {
    boolean updateAwardbanQEmail = false;
    User user = userDAO.getUserById( userId );
    for ( Iterator iter = user.getUserEmailAddresses().iterator(); iter.hasNext(); )
    {
      UserEmailAddress userEmailAddress = (UserEmailAddress)iter.next();
      if ( userEmailAddress.getEmailType().equals( emailAddressType ) )
      {
        // check if will need to update awardbanq email
        if ( user instanceof Participant )
        {
          updateAwardbanQEmail = awardbanQEmailCheck( userEmailAddress, userId );
        }

        // -------------------------
        // Update email to primary
        // --------------------------
        userEmailAddress.setIsPrimary( Boolean.TRUE );

      }
      else
      {
        userEmailAddress.setIsPrimary( Boolean.FALSE );
      }
    } // end for loop

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQEmail )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#addUserPhone(java.lang.Long,
   *      com.biperf.core.domain.user.UserPhone)
   * @param userId
   * @param userPhone
   * @return UserPhone
   * @throws ServiceErrorException
   */
  @Override
  public UserPhone addUserPhone( Long userId, UserPhone userPhone ) throws ServiceErrorException
  {
    boolean updateAwardbanQPhone = false;
    User user = userDAO.getUserById( userId );

    userPhone.setUser( user );

    // If this is the first phone for this user,
    // force this to be the Primary address,
    // All other addresses should be added as Not Primary
    // Per BA's, No exceptions thrown if need to switch primary/non primary
    if ( user.getUserPhones().size() <= 0 && !userPhone.getPhoneType().equals( PhoneType.lookup( PhoneType.RECOVERY ) ) )
    {
      userPhone.setIsPrimary( Boolean.TRUE );

      // check if will need to update awardbanq phone
      if ( user instanceof Participant )
      {
        updateAwardbanQPhone = awardbanQPhoneCheck( userPhone, userId );
      }
    }
    else
    {
      userPhone.setIsPrimary( Boolean.FALSE );
    }

    // -----------------
    // add the phone
    // -----------------
    user.getUserPhones().add( userPhone );

    user = userDAO.saveUser( user );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQPhone )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }

    return userPhone;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#deleteUserPhone(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param phoneType
   */
  @Override
  public void deleteUserPhone( Long userId, String phoneType )
  {
    User user = userDAO.getUserById( userId );

    Iterator iter = user.getUserPhones().iterator();

    while ( iter.hasNext() )
    {
      if ( ( (UserPhone)iter.next() ).getPhoneType().getCode().equals( phoneType ) )
      {
        iter.remove();
      }
    }
    userDAO.saveUser( user );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserPhone(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param phoneType
   * @return UserPhone
   */
  @Override
  public UserPhone getUserPhone( Long userId, String phoneType )
  {
    User user = userDAO.getUserById( userId );

    UserPhone foundUserPhone = null;

    Iterator iter = user.getUserPhones().iterator();

    while ( iter.hasNext() )
    {
      UserPhone userPhone = (UserPhone)iter.next();
      if ( userPhone.getPhoneType().getCode().equals( phoneType ) )
      {
        foundUserPhone = userPhone;
      }
    }
    return foundUserPhone;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getPrimaryUserPhone(java.lang.Long)
   * @param userId
   * @return UserPhone primary
   */
  @Override
  public UserPhone getPrimaryUserPhone( Long userId )
  {
    User user = userDAO.getUserById( userId );

    Set userPhones = user.getUserPhones();

    Hibernate.initialize( userPhones );

    return user.getPrimaryPhone();
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserPhones(java.lang.Long)
   * @param userId
   * @return Set of UserPhones
   */
  @Override
  public Set getUserPhones( Long userId )
  {
    User user = userDAO.getUserById( userId );

    Set userPhones = user.getUserPhones();

    Hibernate.initialize( userPhones );

    return userPhones;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updatePrimaryPhone(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param phoneType
   * @throws ServiceErrorException
   */
  @Override
  public void updatePrimaryPhone( Long userId, String phoneType ) throws ServiceErrorException
  {
    boolean updateAwardbanQPhone = false;
    User user = userDAO.getUserById( userId );

    Iterator iter = user.getUserPhones().iterator();

    while ( iter.hasNext() )
    {
      UserPhone savedUserPhone = (UserPhone)iter.next();
      if ( savedUserPhone.getPhoneType().getCode().equals( phoneType ) )
      {
        // check if will need to update awardbanq phone
        if ( user instanceof Participant )
        {
          updateAwardbanQPhone = awardbanQPhoneCheck( savedUserPhone, userId );
        }

        // -------------------------
        // Update phone to primary
        // --------------------------
        savedUserPhone.setIsPrimary( Boolean.valueOf( true ) );

      }
      else
      {
        savedUserPhone.setIsPrimary( Boolean.valueOf( false ) );
      }
    } // while

    userDAO.saveUser( user );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQPhone )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
  }

  @Override
  public void updatePrimaryNode( Long userId, Long nodeId ) throws ServiceErrorException
  {
    User user = userDAO.getUserById( userId );

    Iterator iter = user.getUserNodes().iterator();

    while ( iter.hasNext() )
    {
      UserNode savedUserNode = (UserNode)iter.next();
      // -------------------------
      // Update user node to primary
      // --------------------------
      if ( savedUserNode.getNode().getId().equals( nodeId ) )
      {
        savedUserNode.setIsPrimary( Boolean.valueOf( true ) );
      }
      else if ( savedUserNode.getIsPrimary().equals( Boolean.TRUE ) )
      {
        savedUserNode.setIsPrimary( Boolean.valueOf( false ) );
      }
    } // while

    userDAO.saveUser( user );

  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserPhone(java.lang.Long,
   *      com.biperf.core.domain.user.UserPhone)
   * @param userId
   * @param userPhone
   * @throws ServiceErrorException
   */
  @Override
  public void updateUserPhone( Long userId, UserPhone userPhone ) throws ServiceErrorException
  {
    boolean updateAwardbanQPhone = false;
    User user = userDAO.getUserById( userId );

    userPhone.setUser( user );

    // Check if need to update awardbanq with the phone number
    Boolean isPrimaryPhone = userPhone.getIsPrimary();
    if ( isPrimaryPhone != null )
    {
      if ( userPhone.getIsPrimary().booleanValue() )
      {
        if ( user instanceof Participant )
        {
          updateAwardbanQPhone = awardbanQPhoneCheck( userPhone, userId );
        }
      }
    }

    // TODO Possibly call remove and add on the set. Currently, hibernate complains.
    // Call this for now. This is doing a session.merge to update the reference with
    // the value from this userPhone object.
    userDAO.updateUserPhone( userPhone );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQPhone )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }

  } // updateUserPhone

  /**
   * awardbanQPhoneCheck
   *
   * @param checkUserPhone
   * @return true - if need to update awardbanQ phone false - if do not need to update awardbanQ
   *         phone
   */
  private boolean awardbanQPhoneCheck( UserPhone checkUserPhone, Long userId )
  {

    // If the phone changed is the Primary phone
    // and the logged in user is a Participant
    // and the pax has an awardbanq number,
    // then see if the primary phone has changed at all -
    // if it has mark that we need to update the Awardbanq phone also
    boolean updateAwardbanQPhone = false;

    Participant participant = participantDAO.getParticipantOverviewById( userId );
    if ( participant != null )
    {
      if ( participant.getAwardBanqNumber() != null && !participant.getAwardBanqNumber().equals( "" ) )
      {

        UserPhone currentUserPhone = participant.getPrimaryPhone();
        if ( currentUserPhone != null )
        {
          if ( !nullCheckAndTrim( currentUserPhone.getPhoneNbr() ).equals( nullCheckAndTrim( checkUserPhone.getPhoneNbr() ) ) )
          {
            // phones are different
            updateAwardbanQPhone = true;
          }
        }
        else
        {
          // There is not a primary phone setup yet, so need to update awardbanQ
          updateAwardbanQPhone = true;
        }

      }
    }

    return updateAwardbanQPhone;
  }

  /**
   * Constructs a list of the currently used UserPhone types and call removeAll on the phoneTypes
   * list. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAvailablePhoneTypes(java.lang.Long)
   * @param userId
   * @return List of phoneTypes
   */
  @Override
  public List getAvailablePhoneTypes( Long userId )
  {
    User user = userDAO.getUserById( userId );

    List userPhoneTypes = new ArrayList();

    Iterator iter = user.getUserPhones().iterator();
    while ( iter.hasNext() )
    {
      userPhoneTypes.add( ( (UserPhone)iter.next() ).getPhoneType() );
    } // while

    List phoneTypes = new ArrayList( PhoneType.getList() );

    phoneTypes.removeAll( userPhoneTypes );

    return phoneTypes;
  }

  /**
   * Adds a User Address for a given user Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#addUserAddress(java.lang.Long,
   *      com.biperf.core.domain.user.UserAddress)
   * @param userId
   * @param userAddress
   * @return UserAddress
   * @throws ServiceErrorException
   */
  @Override
  public UserAddress addUserAddress( Long userId, UserAddress userAddress ) throws ServiceErrorException
  {
    boolean updateAwardbanQAddress = false;
    User user = userDAO.getUserById( userId );

    userAddress.setUser( user );

    // If this is the first address for this user,
    // force this to be the Primary address,
    // All other addresses should be added as Not Primary
    // Per BA's, No exceptions thrown if need to switch primary/non primary
    if ( user.getUserAddresses().size() <= 0 )
    {
      userAddress.setIsPrimary( Boolean.TRUE );

      // check if need to update awardbanq
      if ( user instanceof Participant )
      {
        updateAwardbanQAddress = awardbanQAddressCheck( userAddress, user.getId() );
      }
    }
    else
    {
      userAddress.setIsPrimary( Boolean.FALSE );
    }

    // -----------------------
    // Add the user address
    // -----------------------
    user.getUserAddresses().add( userAddress );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQAddress )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
    return userAddress;
  } // end addUserAddress

  /**
   * Deletes a User Address for a given userId and addressType Overridden from Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#deleteUserAddress(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param addressType
   * @throws ServiceErrorException
   */
  @Override
  public void deleteUserAddress( Long userId, String addressType ) throws ServiceErrorException
  {

    List errors = new ArrayList();
    User user = userDAO.getUserById( userId );

    Iterator iter = user.getUserAddresses().iterator();
    while ( iter.hasNext() )
    {
      UserAddress userAddress = (UserAddress)iter.next();
      if ( userAddress.getAddressType().getCode().equals( addressType ) )
      {
        if ( userAddress.getIsPrimary().booleanValue() )
        {
          // Cannot Remove a Primary Address
          errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_DELETE_PRIMARY ) );
        }
        else
        {
          iter.remove();
        }
      }
    }

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorExceptionWithRollback( errors );
    }

  } // end deleteUserAddress

  /**
   * Gets a user address based on userId and addressType Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserAddress(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param addressType
   * @return UserAddress
   */
  @Override
  public UserAddress getUserAddress( Long userId, String addressType )
  {
    User user = userDAO.getUserById( userId );

    UserAddress foundUserAddress = null;

    Iterator iter = user.getUserAddresses().iterator();

    while ( iter.hasNext() )
    {
      UserAddress userAddress = (UserAddress)iter.next();
      if ( userAddress.getAddressType().getCode().equals( addressType ) )
      {
        foundUserAddress = userAddress;
      }
    }
    return foundUserAddress;
  } // end getUserAddress

  /**
   * Get User Addresses for a specific user Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserAddresses(java.lang.Long)
   * @param userId
   * @return Set of UserAddresses
   */
  @Override
  public Set getUserAddresses( Long userId )
  {
    User user = userDAO.getUserById( userId );

    Set userAddresses = user.getUserAddresses();

    Hibernate.initialize( userAddresses );

    return userAddresses;
  } // end getUserAddreses

  @Override
  public Country getPrimaryUserAddressCountry( Long userId )
  {
    return userDAO.getPrimaryUserAddressCountry( userId );
  }

  /**
   * Gets the Primary User Address Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getPrimaryUserAddress(java.lang.Long)
   * @param userId
   * @return UserAddress
   */
  @Override
  public UserAddress getPrimaryUserAddress( Long userId )
  {
    User user = userDAO.getUserById( userId );
    if( null != user ){
    Set userAddresses = user.getUserAddresses();

    Hibernate.initialize( userAddresses );

    return user.getPrimaryAddress();
    }else{
    	return null;
    }
  } // end getPrimaryUserAddress

  /**
   * Updates the Primary Address to the address with the given address type Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updatePrimaryAddress(java.lang.Long,
   *      java.lang.String)
   * @param userId
   * @param addressTypeofNewPrimary
   * @throws ServiceErrorException
   */
  @Override
  public void updatePrimaryAddress( Long userId, String addressTypeofNewPrimary ) throws ServiceErrorException
  {
    List errors = new ArrayList();
    User user = userDAO.getUserById( userId );
    boolean updateAwardbanQAddress = false;
    // -----------------------------------------------
    // Get the country of the Current Primary Address
    // ------------------------------------------------
    String currentPrimaryCountry = "";
    UserAddress currentPrimaryAddress = getPrimaryUserAddress( userId );
    if ( currentPrimaryAddress != null )
    {
      currentPrimaryCountry = currentPrimaryAddress.getAddress().getCountry().getCountryCode();
    }

    // ----------------------------------------------------------------
    // Loop through the addresses to find the one with an address type
    // that matches the address type selected to be the new primary
    // Set the others to false. Also check if will need to update awardbanq addr
    // ----------------------------------------------------------------
    Iterator iter = user.getUserAddresses().iterator();
    while ( iter.hasNext() )
    {
      UserAddress savedUserAddress = (UserAddress)iter.next();
      if ( savedUserAddress.getAddressType().getCode().equals( addressTypeofNewPrimary ) )
      {
        // Verify the country of the new primary address matches the
        // country of the current primary address
        if ( currentPrimaryCountry.equals( "" ) || currentPrimaryCountry.equals( savedUserAddress.getAddress().getCountry().getCountryCode() ) )
        {
          // check if will need to update awardbanq addr
          if ( user instanceof Participant )
          {
            updateAwardbanQAddress = awardbanQAddressCheck( savedUserAddress, userId );
          }

          // -------------------------------
          // update this address to primary
          // --------------------------------
          savedUserAddress.setIsPrimary( Boolean.TRUE );
        }
        else
        {
          // Cannot change primary address to one of a different country than the original primary
          errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_CHANGE_PRIMARY_COUNTRY ) );
          savedUserAddress.setIsPrimary( Boolean.FALSE );
          currentPrimaryAddress.setIsPrimary( Boolean.TRUE );
          break;
        }
      }
      else
      {
        savedUserAddress.setIsPrimary( Boolean.FALSE );
      }
    } // while

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorExceptionWithRollback( errors );
    }

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQAddress )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }

  } // end updatePrimaryAddress

  /**
   * Updates a specific User Address Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserAddress(java.lang.Long,
   *      com.biperf.core.domain.user.UserAddress)
   * @param userId
   * @param userAddress
   * @throws ServiceErrorException
   */
  @Override
  public void updateUserAddress( Long userId, UserAddress userAddress ) throws ServiceErrorException
  {
    String primaryAddrType = "";
    String primaryCountryType = "";
    boolean updateAwardbanQAddress = false;
    List errors = new ArrayList();

    User user = userDAO.getUserById( userId );

    // -----------------------------------------------
    // Get the Current Primary Address for the user
    // ------------------------------------------------
    UserAddress currentPrimaryAddress = getPrimaryUserAddress( userId );

    if ( currentPrimaryAddress == null )
    {
      // There is not a primary address for this user.
      // set this address to primary
      userAddress.setIsPrimary( Boolean.TRUE );
    }
    else
    {
      primaryAddrType = currentPrimaryAddress.getAddressType().getCode();
      primaryCountryType = currentPrimaryAddress.getAddress().getCountry().getCountryCode();

      if ( userAddress.getAddressType().getCode().equals( primaryAddrType ) )
      {
        // The Address being updated is the current Primary Address.
        // See if they are attempting to change the primary value to false
        if ( !userAddress.getIsPrimary().booleanValue() )
        {
          // Cannot change the current primary to Not primary here.
          // Instead need to set a different user address as primary
          // via the updatePrimaryAddress method
          // (which will then in turn will update the existing primary to not primary)
          errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_UPDATE_PRIMARY_TO_NONPRIMARY ) );
        }

        // Cannot change Country of current primary
        if ( !primaryCountryType.equals( userAddress.getAddress().getCountry().getCountryCode() ) )
        {
          errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_CHANGE_PRIMARY_COUNTRY ) );
        }
      }
      else
      {
        // The Address being updated is Not the current Primary Address,
        // and trying to change it to be primary
        if ( userAddress.getIsPrimary().booleanValue() )
        {
          // Verify the country of the new primary address matches the
          // country of the current primary address
          if ( !primaryCountryType.equals( "" ) && !primaryCountryType.equals( userAddress.getAddress().getCountry().getCountryCode() ) )
          {
            // Cannot change primary address to one of a different country than the original primary
            errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_CHANGE_PRIMARY_COUNTRY ) );
          }
        }
      }

    }

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorExceptionWithRollback( errors );
    }

    // --------------------------
    // AwardbanQ address check
    // --------------------------
    if ( userAddress.isPrimary() )
    {
      if ( user instanceof Participant )
      {
        updateAwardbanQAddress = awardbanQAddressCheck( userAddress, userId );
      }
    }

    // ------------------------
    // Update the User Address
    // -------------------------
    userAddress.setUser( user );
    // TODO Possibly call remove and add on the set. Currently, hibernate complains.
    // Call this for now. This is doing a session.merge to update the reference with
    // the value from this userAddress object.
    userDAO.updateUserAddress( userAddress );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQAddress )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }

  } // updateUserAddress

  /**
   * awardbanQAddressCheck
   *
   * @param checkUserAddress
   * @return true - if need to update awardbanQ address false - if do not need to update awardbanQ
   *         address
   */
  private boolean awardbanQAddressCheck( UserAddress checkUserAddress, Long userId )
  {

    // If the address changed is the Primary address
    // and the logged in user is a Participant
    // and the pax has an awardbanq number,
    // then see if the primary address has changed at all -
    // if it has mark that we need to update the Awardbanq address also
    boolean updateAwardbanQAddress = false;

    Participant participant = participantDAO.getParticipantOverviewById( userId );
    if ( participant != null )
    {
      if ( participant.getAwardBanqNumber() != null && !participant.getAwardBanqNumber().equals( "" ) )
      {

        UserAddress currentUserAddress = participant.getPrimaryAddress();
        if ( currentUserAddress != null )
        {
          Address currentAddress = currentUserAddress.getAddress();
          if ( currentAddress != null )
          {
            // compare the addresses
            updateAwardbanQAddress = isAddressChanged( currentAddress, checkUserAddress.getAddress() );
          }
        }
        else
        {
          // There is not a primary address setup yet, so need to update awardbanQ
          updateAwardbanQAddress = true;
        }
      }
    }

    return updateAwardbanQAddress;
  }

  /**
   * Compares the 2 input addresses
   *
   * @param origPrimaryAddress
   * @param checkUserAddress
   * @return true - if the addresses are different false - if the addresses are the same
   */
  private boolean isAddressChanged( Address origPrimaryAddress, Address checkUserAddress )
  {
    boolean addressChanged = false;
    if ( !nullCheckAndTrim( origPrimaryAddress.getAddr1() ).equals( nullCheckAndTrim( checkUserAddress.getAddr1() ) ) )
    {
      addressChanged = true;
    }
    if ( !nullCheckAndTrim( origPrimaryAddress.getAddr2() ).equals( nullCheckAndTrim( checkUserAddress.getAddr2() ) ) )
    {
      addressChanged = true;
    }
    if ( !nullCheckAndTrim( origPrimaryAddress.getAddr3() ).equals( nullCheckAndTrim( checkUserAddress.getAddr3() ) ) )
    {
      addressChanged = true;
    }
    if ( !nullCheckAndTrim( origPrimaryAddress.getCity() ).equals( nullCheckAndTrim( checkUserAddress.getCity() ) ) )
    {
      addressChanged = true;
    }

    if ( origPrimaryAddress.getStateType() == null && checkUserAddress.getStateType() != null || origPrimaryAddress.getStateType() != null && checkUserAddress.getStateType() == null )
    {
      addressChanged = true;
    }

    if ( origPrimaryAddress.getStateType() != null && checkUserAddress.getStateType() != null )
    {
      if ( !nullCheckAndTrim( origPrimaryAddress.getStateType().getCode() ).equals( nullCheckAndTrim( checkUserAddress.getStateType().getCode() ) ) )
      {
        addressChanged = true;
      }
    }

    if ( !nullCheckAndTrim( origPrimaryAddress.getPostalCode() ).equals( nullCheckAndTrim( checkUserAddress.getPostalCode() ) ) )
    {
      addressChanged = true;
    }
    return addressChanged;
  }

  /**
   * nullCheckAndTrim
   *
   * @param checkString
   * @return If string is null, will make it an empty string, then it will trim the string
   */
  private String nullCheckAndTrim( String checkString )
  {
    if ( checkString == null )
    {
      checkString = "";
    }
    else
    {
      checkString = checkString.trim();
    }
    return checkString;
  }

  /**
   * Returns a list of Address Types that are not already in use. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAvailableAddressTypes(java.lang.Long)
   * @param userId
   * @return List of address types
   */
  @Override
  public List getAvailableAddressTypes( Long userId )
  {
    User user = userDAO.getUserById( userId );

    List userAddressTypes = new ArrayList();

    // user Address types already in use
    Iterator iter = user.getUserAddresses().iterator();
    while ( iter.hasNext() )
    {
      userAddressTypes.add( ( (UserAddress)iter.next() ).getAddressType() );
    }

    // List of all address types
    List addressTypes = new ArrayList( AddressType.getList() );

    // Remove from the list, all of the Address types that are already in use
    addressTypes.removeAll( userAddressTypes );

    return addressTypes;
  } // getAvailableAddressTypes

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserPhones(java.lang.Long)
   * @param userId
   * @return Set of UserPhones
   */
  @Override
  public Set getUserCharacteristics( Long userId )
  {
    User user = userDAO.getUserById( userId );

    Set userChars = user.getUserCharacteristics();

    Hibernate.initialize( userChars );

    return userChars;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#addUserNode(java.lang.Long,
   *      com.biperf.core.domain.user.UserNode)
   * @param userId
   * @param userNode
   */
  @Override
  public void addUserNode( Long userId, UserNode userNode )
  {
    Node node = nodeDAO.getNodeById( userNode.getNode().getId() );
    User user = userDAO.getUserById( userId );

    userNode.setUser( user );
    userNode.setNode( node );

    user.addUserNode( userNode );
  }

  /**
   * Gets the Primary User Node Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getPrimaryUserNode(java.lang.Long)
   * @param userId
   * @return UserNode
   */
  @Override
  public UserNode getPrimaryUserNode( Long userId )
  {
    User user = userDAO.getUserById( userId );

    Set userNodes = user.getUserNodes();

    Hibernate.initialize( userNodes );

    return user.getPrimaryUserNode();
  } // end getPrimaryUserAddress

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getUserNodes(java.lang.Long)
   * @param userId
   * @return Set of UserNodes
   */
  @Override
  public Set<UserNode> getUserNodes( Long userId )
  {
    return userDAO.getUserById( userId ).getUserNodes();

  }

  /**
   * Get the list of Nodes assigned to the user.
   *
   * @param userId
   * @return List
   */
  @Override
  public List getAssignedNodes( Long userId )
  {
    return this.userDAO.getAssignedNodes( userId );
  }

  /**
   * Removes the nodes associated to the user.
   *
   * @param userId
   * @param nodeIds
   */
  @Override
  public void removeUserNodes( Long userId, String[] nodeIds ) throws ServiceErrorException
  {
    // Get the user.
    User user = this.userDAO.getUserById( userId );

    for ( int i = 0; i <= nodeIds.length - 1; i++ )
    {
      if ( !StringUtils.isEmpty( nodeIds[i] ) )
      {
        UserNode userNodeToRemove = user.getUserNodeByNodeId( new Long( nodeIds[i] ) );
        if ( !userNodeToRemove.getIsPrimary() )
        {
          user.removeUserNode( userNodeToRemove );
        }
        else
        {
          List serviceErrors = new ArrayList();
          serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.PRIMARY_USER_NODE_DELETE_ERR ) );
          throw new ServiceErrorExceptionWithRollback( serviceErrors );
        }

      }
    }
  }

  /**
   * Fetches a list of nodes which aren't currently assigned to the user identified by the userId
   * param.
   *
   * @param userId
   * @return List
   */
  @Override
  public List getUnassignedNodes( Long userId )
  {
    return userDAO.getUnassignedNodes( userId );
  }

  /**
   * Get the userNode for the userId and nodeId params.
   *
   * @param userId
   * @param nodeId
   * @return UserNode
   */
  @Override
  public UserNode getUserNodeByUserIdAndNodeId( Long userId, Long nodeId )
  {
    Set userNodes = this.userDAO.getUserById( userId ).getUserNodes();

    Iterator userNodeIterator = userNodes.iterator();

    UserNode userNode = null;

    while ( userNodeIterator.hasNext() )
    {
      userNode = (UserNode)userNodeIterator.next();

      if ( userNode.getNode().getId().longValue() == nodeId.longValue() )
      {
        break;
      }

    }

    return userNode;
  }

  /**
   * Update the userNode param for the user associated to the userId param.
   *
   * @param userId
   * @param nodeId
   * @param role
   * @return UserNode
   */
  @Override
  public UserNode updateUserNodeRole( Long userId, Long nodeId, HierarchyRoleType role )
  {
    UserNode userNodeToUpdate = getUserNode( userId, nodeId );
    userNodeToUpdate.setHierarchyRoleType( role );
    return userNodeToUpdate;
  }

  /**
   * Update the userRole praram fUpdate the userNode param for the user associated to the userId
   * param.
   *
   * @param userId
   * @param userRole
   */
  public void updateUserRole( Long userId, UserRole userRole )
  {
    User user = this.userDAO.getUserById( userId );
    Role role = this.roleDAO.getRoleById( userRole.getRole().getId() );

    userRole.setUser( user );
    userRole.setRole( role );

    user.addUserRole( userRole );

  }

  /**
   * Builds a set of UserNodes given a node id and a list of user ids.
   *
   * @param nodeId
   * @param userIds
   * @return Set of <code>com.biperf.core.domain.user.UserNode</code>
   */
  @Override
  public Set buildUserNodeSet( Long nodeId, Set userIds )
  {
    Set userNodes = Collections.EMPTY_SET;
    Node node = this.nodeDAO.getNodeById( nodeId );

    Iterator iter = userIds.iterator();
    while ( iter.hasNext() )
    {
      User user = userDAO.getUserById( new Long( (String)iter.next() ) );
      userNodes.add( new UserNode( user, node ) );
    }

    return userNodes;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#generatePassword()
   * @return String generated Password
   */
  @Override
  public String generatePassword()
  {
    return getPasswordPolicyStrategy().generatePassword();
  }

  /**
   * This method will move all users from one node to another node. It will also take into account
   * all attributes on the UserNode and resolve conflicts before assigning to the new Node.
   *
   * @see com.biperf.core.service.participant.UserService#updateUserNodeChangeNode(java.lang.Long,java.lang.Long)
   * @param oldNodeId
   * @param newNodeId
   */
  @Override
  public void updateUserNodeChangeNode( Long oldNodeId, Long newNodeId )
  {
    // Get all userIds for the oldNode
    List users = getAllUsersOnNode( oldNodeId );

    // Get the node all users are to be moved to
    Node newNode = nodeDAO.getNodeById( newNodeId );

    // Loop Users getting each UserNode
    Iterator iterator = users.iterator();
    while ( iterator.hasNext() )
    {
      User user = (User)iterator.next();
      moveUserToNode( oldNodeId, newNode, user );
    }
  }

  /**
   * Gets a list of users on the node associated to the nodeId param.
   *
   * @param nodeId
   * @return List
   */
  @Override
  public List getAllUsersOnNode( Long nodeId )
  {
    return userDAO.getAllUsersOnNode( nodeId );
  }

  /**
   * Gets a list of all of the participants on a node.
   *
   * @param nodeId
   * @return List of all of the participants on a specific node
   */
  @Override
  public List<User> getAllParticipantsOnNode( Long nodeId )
  {
    return userDAO.getAllParticipantsOnNode( nodeId );
  }

  @Override
  public Set<Long> getUserIdsByEmailOrPhone( String emailOrPhone )
  {
    if ( emailOrPhone.indexOf( "@" ) > 0 )
    {
      return userDAO.getUserIdsByEmail( emailOrPhone );
    }
    else
    {
      return userDAO.getUserIdsByContactPhone( emailOrPhone );
    }
  }

  /**
   * Gets a list of all of the users on a node having the specified hierarchyRoleType.
   *
   * @param nodeId
   * @param hierarchyRoleType
   * @param associationRequestCollection
   * @return List of all of the users on a specific node having the specified hierarchyRoleType.
   */
  @Override
  public List getAllUsersOnNodeHavingRole( Long nodeId, HierarchyRoleType hierarchyRoleType, AssociationRequestCollection associationRequestCollection )
  {
    return userDAO.getAllUsersOnNodeHavingRole( nodeId, hierarchyRoleType, associationRequestCollection );
  }

  /**
   * Given a <code>User</code> and a <code>Node</code> change the <code>UserNode</code> on the
   * User to move the user to a new node.
   *
   * @param oldNodeId
   * @param newNode
   * @param user
   */
  public void moveUserToNode( Long oldNodeId, Node newNode, User user )
  {
    UserNode oldUserNode = user.getUserNodeByNodeId( oldNodeId );

    UserNode newUserNode = new UserNode();
    newUserNode.setNode( newNode );
    newUserNode.setActive( oldUserNode.isActive() );
    newUserNode.setIsPrimary( oldUserNode.getIsPrimary() );
    // set the HierarchyRoleType if the current one is owner and there is an owner on the new Node
    // make the role manager.
    if ( oldUserNode.getHierarchyRoleType().isOwner() && newNode.hasOwner() )
    {
      newUserNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );
    }
    else
    {
      newUserNode.setHierarchyRoleType( oldUserNode.getHierarchyRoleType() );
    }

    user.removeUserNode( oldUserNode );
    user.addUserNode( newUserNode );
  }

  /**
   * Update the userRoles in the list for the user attached to the userId param. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserRoles(java.lang.Long,
   *      java.util.List)
   * @param userId
   * @param userRoleList
   */
  @Override
  public void updateUserRoles( Long userId, List userRoleList )
  {

    User user = this.userDAO.getUserById( userId );

    // get the list of existing userRoles
    Set existingUserRoles = user.getUserRoles();
    List removeRoles = new ArrayList();

    // Remove the userRoles which aren't in the list of new userRoles
    for ( Iterator existingUserRolesIter = existingUserRoles.iterator(); existingUserRolesIter.hasNext(); )
    {
      UserRole userRole = (UserRole)existingUserRolesIter.next();

      if ( !userRoleList.contains( userRole ) )
      {
        removeRoles.add( userRole );
      }
    }

    if ( removeRoles.size() > 0 )
    {
      user.getUserRoles().removeAll( removeRoles );
    }

    for ( Iterator userRoleListIter = userRoleList.iterator(); userRoleListIter.hasNext(); )
    {

      UserRole userRole = (UserRole)userRoleListIter.next();

      Role role = this.roleDAO.getRoleById( userRole.getRole().getId() );
      userRole.setUser( user );
      userRole.setRole( role );

      user.getUserRoles().remove( userRole );

      user.addUserRole( userRole );

    }

  }

  /**
   * Update the acls assigned to the user. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserAcls(java.lang.Long,
   *      java.util.List)
   * @param userId
   * @param userAclList
   */
  @Override
  public void updateUserAcls( Long userId, List userAclList )
  {

    User user = this.userDAO.getUserById( userId );

    // get the list of existing userAcls
    Set existingUserAcls = user.getUserAcls();
    List removeAcls = new ArrayList();

    // Remove the userAcls which aren't in the list of new userAcls
    for ( Iterator existingUserAclsIter = existingUserAcls.iterator(); existingUserAclsIter.hasNext(); )
    {
      UserAcl userAcl = (UserAcl)existingUserAclsIter.next();

      if ( !userAclList.contains( userAcl ) )
      {
        removeAcls.add( userAcl );
      }
    }

    if ( removeAcls.size() > 0 )
    {
      user.getUserAcls().removeAll( removeAcls );
    }

    for ( Iterator userAclListIter = userAclList.iterator(); userAclListIter.hasNext(); )
    {

      UserAcl userAcl = (UserAcl)userAclListIter.next();

      Acl acl = this.aclDAO.getAclById( userAcl.getAcl().getId() );
      userAcl.setUser( user );
      userAcl.setAcl( acl );

      user.getUserAcls().remove( userAcl );

      user.addUserAcl( userAcl );

    }

  }

  /**
   * Gets a list of all acls. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#getAllAcls()
   * @return List
   */
  @Override
  public List getAllAcls()
  {
    return this.aclDAO.getAll();
  }

  /**
   * Update the user with the userAcl param. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserAcls(com.biperf.core.domain.user.UserAcl)
   * @param userAcl
   */
  @Override
  public void updateUserAcls( UserAcl userAcl )
  {

    User user = this.userDAO.getUserById( userAcl.getUser().getId() );
    userAcl.setUser( user );

    Acl acl = this.aclDAO.getAclById( userAcl.getAcl().getId() );
    userAcl.setAcl( acl );

    user.getUserAcls().remove( userAcl );
    user.addUserAcl( userAcl );

  }

  /**
   * Removes the userAcls from the list from the user. Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#removeUserAcls(java.lang.Long,
   *      java.util.List)
   * @param userId
   * @param userAclsToRemove
   */
  @Override
  public void removeUserAcls( Long userId, List userAclsToRemove )
  {

    // Get the user.
    User user = this.userDAO.getUserById( userId );

    UserAcl userAcl;

    for ( Iterator userAclIter = userAclsToRemove.iterator(); userAclIter.hasNext(); )
    {

      userAcl = (UserAcl)userAclIter.next();

      Acl acl = this.aclDAO.getAclById( userAcl.getAcl().getId() );
      UserAcl newUserAcl = new UserAcl( user, acl );
      newUserAcl.setTarget( userAcl.getTarget() );
      newUserAcl.setPermission( userAcl.getPermission() );
      newUserAcl.setGuid( userAcl.getGuid() );

      if ( user.getUserAcls().contains( newUserAcl ) )
      {
        user.getUserAcls().remove( newUserAcl );
      }

    }

  }

  /**
   * @return List of user value beans
   */
  @Override
  public List getAllUsersForWelcomeMail()
  {
    return userDAO.getAllUsersForWelcomeMail();
  }

  /**
   * @return List of user value beans
   */
  @Override
  public List<RAValueBean> getAllUsersForRAWelcomeMail()
  {
    return userDAO.getAllUsersForRAWelcomeMail();
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserNodeStatus(java.lang.Long,
   *      com.biperf.core.domain.enums.ParticipantStatus)
   * @param userId
   * @param status
   */
  @Override
  public void updateUserNodeStatus( Long userId, Boolean status )
  {
    User user = this.userDAO.getUserById( userId );

    Set userNodeSet = user.getUserNodes();
    for ( Iterator iter = userNodeSet.iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      userNode.setActive( status );

    }

  }

  @Override
  public List getOwnerNodes( Long userId )
  {
    return getOwnerNodes( userId, false );
  }

  @Override
  public List getOwnerNodeIds( Long userId )
  {
    return getOwnerNodes( userId, true );
  }

  private List getOwnerNodes( Long userId, boolean nodeIdOnly )
  {
    // Get the user.
    User user = this.userDAO.getUserById( userId );

    // Get the nodes, user is assigned to
    List nodes = this.getAssignedNodes( userId );

    // Find the nodes for which user is an owner
    List ownerNodeList = new ArrayList();
    for ( int i = 0; i < nodes.size(); i++ )
    {
      Node node = (Node)nodes.get( i );
      if ( user.isOwnerOf( node ) )
      {
        if ( nodeIdOnly )
        {
          ownerNodeList.add( node.getId() );
        }
        else
        {
          ownerNodeList.add( node );
        }
      }
    }

    return ownerNodeList;
  }

  @Override
  public List getNodeMembersForOwnerNodes( Long userId )
  {
    Set userList = new HashSet();

    // Get the user.
    User user = this.userDAO.getUserById( userId );

    // Get the nodes, user is assigned to
    List nodes = this.getAssignedNodes( userId );

    // Find members for the nodes for which user is an owner
    for ( int i = 0; i < nodes.size(); i++ )
    {
      Node node = (Node)nodes.get( i );
      if ( user.isOwnerOf( node ) )
      {
        userList.addAll( this.getAllParticipantsOnNode( node.getId() ) );
      }
    }

    List sortedUserList = new ArrayList();
    sortedUserList.addAll( userList );

    Collections.sort( sortedUserList, new UserNameComparator() );

    return sortedUserList;
  }

  @Override
  public String getUserTimeZone( Long id )
  {
    return UserManager.getTimeZoneID();
  }

  @Override
  public String getUserTimeZoneByUserCountry( Long userId )
  {
    return userDAO.getUserTimeZone( userId );
  }

  @Override
  public void saveLoginInfo( long userId )
  {
    Timestamp timestamp = new Timestamp( System.currentTimeMillis() );
    UserLoginInfo userLoginInfo = new UserLoginInfo();
    userLoginInfo.setUserId( userId );
    userLoginInfo.setUserLoggedInTime( timestamp );
    userDAO.saveLoginInfo( userLoginInfo );
  }

  @Override
  public UserNode getUserNode( long userId, long nodeId )
  {
    return userDAO.getUserNode( userId, nodeId );
  }

  @Override
  public User getUserByEmailAddr( String emailAddr )
  {
    return this.userDAO.getUserByEmailAddr( emailAddr );
  }

  @Override
  public String getCountryProgramIdByUserId( Long userId )
  {
    return this.userDAO.getCountryProgramIdByUserId( userId );
  }

  @Override
  public String getCountryProgramId( Long userId )
  {
    return this.userDAO.getCountryProgramId( userId );
  }

  @Override
  public BigDecimal getBudgetMediaValueForUser( Long userId )
  {
    BigDecimal budgetMediaValue = null;

    Country country = getPrimaryUserAddressCountry( userId );
    if ( country != null )
    {
      budgetMediaValue = country.getBudgetMediaValue();
    }

    return budgetMediaValue;
  }

  @Override
  public List<User> getAllUsersBasedOnCharacteristics( Long characteristicId )
  {
    return userDAO.getAllUsersBasedOnCharacteristics( characteristicId );
  }

  @Override
  public GeneratedUserIdBean generateUniqueUserIdBean( String firstName, String lastName )
  {
    String userName = generateUniqueUserId( firstName, lastName );
    GeneratedUserIdBean bean = new GeneratedUserIdBean();
    bean.setUserName( userName );
    return bean;
  }

  @Override
  public String generateUniqueUserId( String firstName, String lastName )
  {
    User user = null;
    String userName = null;
    int low = 1000;
    int high = 9999;
    Random r = new Random();
    do
    {
      int randomValue = r.nextInt( high - low ) + low;
      userName = firstName.toLowerCase().charAt( 0 ) + lastName.toLowerCase() + randomValue;
      userName = StringUtil.filterAlphaNumeric( userName );
    }
    while ( isUserNameInUse( userName ) );
    return userName;
  }

  @Override
  public boolean isUserNameInUse( String userName )
  {
    return getUserByUserName( userName ) != null;
  }

  @Override
  public List<Long> getAssignedNodesIdList( Long userId )
  {
    return userDAO.getAssignedNodesIdList( userId );
  }

  @Override
  public LanguageType getPreferredLanguageFor( Long userId )
  {
    User user = this.getUserById( userId );
    return getPreferredLanguageFor( user );
  }

  @Override
  public LanguageType getPreferredLanguageFor( User user )
  {
    LanguageType language = null;
    if ( user != null )
    {
      language = user.getLanguageType();

      if ( language == null )
      {
        language = LanguageType.lookup( systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() );
      }
    }

    return language;
  }

  @Override
  public void deleteLoginTokenFor( Long userId )
  {
    userDAO.deleteLoginTokenFor( userId );
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  @Override
  public BigDecimal getBudgetMediaValueByUserId( final Long userId )
  {
    return countryDAO.getBudgetMediaValueByUserId( userId );
  }

  public void setCountryDAO( CountryDAO countryDAO )
  {
    this.countryDAO = countryDAO;
  }

  public void setUserCharacteristicService( UserCharacteristicService userCharacteristicService )
  {
    this.userCharacteristicService = userCharacteristicService;
  }

  @Override
  public int getPrimaryEmailCountByEmailAddress( String email )
  {
    return this.userDAO.getPrimaryEmailCountByEmailAddress( email );
  }

  @Override
  public boolean isUniqueEmail( String email )
  {
    return this.userDAO.isUniqueEmail( email );
  }

  @Override
  public boolean isUniquePhoneNumber( String phoneNumber )
  {
    return this.userDAO.isUniquePhoneNumber( phoneNumber );
  }

  @Override
  public List<StrongMailUser> getAllStrongMailUsers()
  {
    return this.userDAO.getAllStrongMailUsers();
  }

  @Override
  public void updateStrongMailUserByUserName( StrongMailUser user )
  {
    this.userDAO.updateStrongMailUserByUserName( user );
  }

  @Override
  public void lockUserAccount( Long userId ) throws Exception
  {
    User user = getUserById( userId );
    user.setAccountLocked( true );
    user.setForcePasswordChange( true );

    // Changing the password forces the user to go through reset password flow once unlocked
    String newPassword = RandomStringUtils.randomAscii( 30 );
    newPassword = new SHA256Hash().encryptDefault( newPassword );
    user.setPassword( newPassword );
    // This will ensure that the child objects are re-attached to the active session
    // even when few sub objects comes from different session
    userDAO.save( user );
  }

  @Override
  public String sendRecoveryVerificationMessage( Long userId, ContactType contactType, String emailOrPhone ) throws ServiceErrorException
  {
    // The email address or phone number
    String contactValue = "";
    User user = getUserById( userId );

    if ( ContactType.EMAIL.equals( contactType ) )
    {
      UserEmailAddress recoveryEmail = user.getEmailAddressByType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );

      // Must save a new value before verifying it
      if ( !recoveryEmail.getEmailAddr().equals( emailOrPhone ) )
      {
        throw new ServiceErrorException( "profile.preference.tab.VERIFY_BEFORE_SAVE_ERROR" );
      }

      UserToken userToken = getPasswordResetService().generateTokenAndSave( userId, UserTokenType.EMAIL_VERIFICATION );
      Mailing recoveryVerificationMailing = getMailingService().buildRecoveryVerificationMailing( userId, buildEmailPaxContactType( recoveryEmail ), userToken.getUnencryptedTokenValue() );
      getMailingService().submitMailing( recoveryVerificationMailing, null, userId );
      contactValue = recoveryEmail.getEmailAddr();
    }
    else if ( ContactType.PHONE.equals( contactType ) )
    {
      UserPhone recoveryPhone = user.getPhoneByType( PhoneType.lookup( PhoneType.RECOVERY ) );

      // Must save a new value before verifying it
      if ( !recoveryPhone.getPhoneNbr().equals( emailOrPhone ) )
      {
        throw new ServiceErrorException( "profile.preference.tab.VERIFY_BEFORE_SAVE_ERROR" );
      }

      String message = CmsResourceBundle.getCmsBundle().getString( MessageService.FORGOT_PASSWORD, "TEXT_MSG" );
      UserToken userToken = getPasswordResetService().generateTokenAndSave( userId, UserTokenType.PHONE_VERIFICATION );
      String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
      String formatedMesg = message.replace( "${userToken}", userToken.getUnencryptedTokenValue() );
      formatedMesg = formatedMesg.replace( "${programName}", programName );
      getMailingService().sendSmsMessage( userId, recoveryPhone.getCountryPhoneCode(), recoveryPhone.getPhoneNbr(), formatedMesg );
      contactValue = recoveryPhone.getPhoneNbr();
    }

    return contactValue;
  }

  @Override
  public boolean verifyRecoveryCode( Long userId, String token, ContactType contactType ) throws ServiceErrorException
  {
    User user = getUserById( userId );

    // The email verification token is generated as all lower case. Lowercase the user input, as
    // well.
    token = token.toLowerCase().trim();

    UserToken userToken = userTokenDAO.getTokenById( new SHA256Hash().encryptDefault( token ) );
    if ( userToken == null || !UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ).equals( userToken.getStatus() ) || !userId.equals( userToken.getUser().getId() ) )
    {
      return false;
    }

    if ( ContactType.EMAIL.equals( contactType ) && !UserTokenType.EMAIL_VERIFICATION.equals( userToken.getUserTokenType() ) )
    {
      return false;
    }
    else if ( ContactType.PHONE.equals( contactType ) && !UserTokenType.PHONE_VERIFICATION.equals( userToken.getUserTokenType() ) )
    {
      return false;
    }

    useToken( userToken );

    if ( ContactType.EMAIL.equals( contactType ) )
    {
      UserEmailAddress recoveryEmail = user.getEmailAddressByType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
      recoveryEmail.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.VERIFIED ) );
      userDAO.saveUser( user );
    }
    else if ( ContactType.PHONE.equals( contactType ) )
    {
      UserPhone recoveryPhone = user.getPhoneByType( PhoneType.lookup( PhoneType.RECOVERY ) );
      recoveryPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.VERIFIED ) );
      userDAO.saveUser( user );
    }

    return true;
  }

  private void useToken( UserToken token )
  {
    // update the Token to used
    token.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.USED ) );
    userTokenDAO.saveUserToken( token );
  }

  private PaxContactType buildEmailPaxContactType( UserEmailAddress emailAddress )
  {
    PaxContactType paxContactType = new PaxContactType();
    paxContactType.setContactType( ContactType.EMAIL );
    paxContactType.setValue( StringUtil.maskEmailAddress( emailAddress.getEmailAddr() ) );
    paxContactType.setContactId( emailAddress.getId() );
    paxContactType.setUnique( isUniqueEmail( emailAddress.getEmailAddr() ) );
    return paxContactType;
  }

  private PaxContactType buildPhonePaxContactType( UserPhone phone )
  {
    PaxContactType paxContactType = new PaxContactType();
    paxContactType.setContactType( ContactType.PHONE );
    paxContactType.setValue( StringUtil.maskPhoneNumber( phone.getPhoneNbr() ) );
    paxContactType.setContactId( phone.getId() );
    paxContactType.setUnique( isUniquePhoneNumber( phone.getPhoneNbr() ) );
    return paxContactType;
  }

  public MailingService getMailingService()
  {
    return (MailingService)BeanLocator.getBean( MailingService.class );
  }

  public PasswordResetService getPasswordResetService()
  {
    return (PasswordResetService)BeanLocator.getBean( PasswordResetService.class );
  }

  @Override
  public void deleteUserEmailAddress( long userId )
  {
    userDAO.deleteUserEmailAddress( userId );

  }

  @Override
  public void deleteUserPhones( long userId )
  {
    userDAO.deleteUserPhones( userId );

  }

  @Override
  public void deleteEmailAddressForUser( Long userId, Long userEmailAddressId )
  {
    userDAO.deleteEmailAddressForUser( userId, userEmailAddressId );
  }

  @Override
  public void deletePhoneNumberForUser( Long userId, Long userPhoneNumberId )
  {
    userDAO.deletePhoneNumberForUser( userId, userPhoneNumberId );
  }

  @Override
  public void deleteAddressForUser( Long userId, Long userAddressId )
  {
    userDAO.deleteAddressForUser( userId, userAddressId );
  }

  @Override
  public UserAddress getUserAddressById( Long id )
  {
    return this.userDAO.getUserAddressById( id );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateEmailAddressForPerson(java.lang.Long,
   *      com.biperf.core.domain.user.userEmailAddress)
   * @param userId
   * @param userEmailAddress
   * @throws ServiceErrorException
   */
  @Override
  public UserEmailAddress updateEmailAddressForUser( Long userId, UserEmailAddress userEmailAddress ) throws ServiceErrorException
  {
    boolean updateAwardbanQEmail = false;
    User user = userDAO.getUserById( userId );
    userEmailAddress.setUser( user );

    // Check if need to update awardbanq with the email Address
    Boolean isPrimaryEmail = userEmailAddress.getIsPrimary();
    if ( isPrimaryEmail != null )
    {
      if ( userEmailAddress.getIsPrimary().booleanValue() )
      {
        if ( user instanceof Participant )
        {
          updateAwardbanQEmail = awardbanQEmailCheck( userEmailAddress, userId );
        }
      }
    }

    // ------------------
    // update user email
    // ------------------
    UserEmailAddress personEmailAddress = userDAO.updateEmailAddressForUser( userEmailAddress );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQEmail )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
    return personEmailAddress;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.participant.UserService#updateUserPhone(java.lang.Long,
   *      com.biperf.core.domain.user.UserPhone)
   * @param userId
   * @param userPhone
   * @throws ServiceErrorException
   */
  @Override
  public UserPhone updatePhoneNumberForUser( Long userId, UserPhone userPhone ) throws ServiceErrorException
  {
    UserPhone updateUserPhone = null;
    boolean updateAwardbanQPhone = false;
    User user = userDAO.getUserById( userId );

    userPhone.setUser( user );

    // Check if need to update awardbanq with the phone number
    Boolean isPrimaryPhone = userPhone.getIsPrimary();
    if ( isPrimaryPhone != null )
    {
      if ( userPhone.getIsPrimary().booleanValue() )
      {
        if ( user instanceof Participant )
        {
          updateAwardbanQPhone = awardbanQPhoneCheck( userPhone, userId );
        }
      }
    }

    // TODO Possibly call remove and add on the set. Currently, hibernate complains.
    // Call this for now. This is doing a session.merge to update the reference with
    // the value from this userPhone object.
    updateUserPhone = userDAO.updateUserPhone( userPhone );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQPhone )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
    return updateUserPhone;
  } // updateUserPhone

  @Override
  public UserAddress updateAddressForUser( Long userId, UserAddress userAddress ) throws ServiceErrorException
  {
    String primaryAddrType = "";
    String primaryCountryType = "";
    boolean updateAwardbanQAddress = false;
    List errors = new ArrayList();
    UserAddress updateAddressForPerson = null;

    User user = userDAO.getUserById( userId );

    // -----------------------------------------------
    // Get the Current Primary Address for the user
    // ------------------------------------------------
    UserAddress currentPrimaryAddress = getPrimaryUserAddress( userId );

    if ( currentPrimaryAddress == null )
    {
      // There is not a primary address for this user.
      // set this address to primary
      userAddress.setIsPrimary( Boolean.TRUE );
    }
    else
    {
      primaryAddrType = currentPrimaryAddress.getAddressType().getCode();
      primaryCountryType = currentPrimaryAddress.getAddress().getCountry().getCountryCode();

      if ( userAddress.getAddressType().getCode().equals( primaryAddrType ) )
      {
        // The Address being updated is the current Primary Address.
        // See if they are attempting to change the primary value to false
        if ( !userAddress.getIsPrimary().booleanValue() )
        {
          // Cannot change the current primary to Not primary here.
          // Instead need to set a different user address as primary
          // via the updatePrimaryAddress method
          // (which will then in turn will update the existing primary to not primary)
          errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_UPDATE_PRIMARY_TO_NONPRIMARY ) );
        }

        // Cannot change Country of current primary
        if ( !primaryCountryType.equals( userAddress.getAddress().getCountry().getCountryCode() ) )
        {
          errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_CHANGE_PRIMARY_COUNTRY ) );
        }
      }
      else
      {
        // The Address being updated is Not the current Primary Address,
        // and trying to change it to be primary
        if ( userAddress.getIsPrimary().booleanValue() )
        {
          // Verify the country of the new primary address matches the
          // country of the current primary address
          if ( !primaryCountryType.equals( "" ) && !primaryCountryType.equals( userAddress.getAddress().getCountry().getCountryCode() ) )
          {
            // Cannot change primary address to one of a different country than the original primary
            errors.add( new ServiceError( ServiceErrorMessageKeys.PARTICIPANT_ADDRESS_CHANGE_PRIMARY_COUNTRY ) );
          }
        }
      }

    }

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorExceptionWithRollback( errors );
    }

    // --------------------------
    // AwardbanQ address check
    // --------------------------
    if ( userAddress.isPrimary() )
    {
      if ( user instanceof Participant )
      {
        updateAwardbanQAddress = awardbanQAddressCheck( userAddress, userId );
      }
    }

    // ------------------------
    // Update the User Address
    // -------------------------
    userAddress.setUser( user );
    // TODO Possibly call remove and add on the set. Currently, hibernate complains.
    // Call this for now. This is doing a session.merge to update the reference with
    // the value from this userAddress object.
    updateAddressForPerson = userDAO.updateUserAddress( userAddress );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanQAddress )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( userId );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }
    return updateAddressForPerson;
  } // updateUserAddress

  @Override
  public User updateUser( User userToSave ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    boolean updateAwardbanName = false;

    // TODO This could become a strategy if there are other unique constraints needed

    // Check to see if the user already exists in the database.
    User dbUser = userDAO.getUserByUserName( userToSave.getUserName() );
    if ( dbUser != null )
    {
      // if we found a record in the database with the given username, and our userToSave ID is
      // null,
      // we are trying to insert a duplicate record.
      if ( userToSave.getId() == null )
      {
        throw new UniqueConstraintViolationException();
      }

      // if we found a record in the database with the given username, but the ids are not equal,
      // we are trying to update to a username that already exists so throw a
      // UniqueConstraintViolationException
      if ( dbUser.getId().compareTo( userToSave.getId() ) != 0 )
      {
        throw new UniqueConstraintViolationException();
      }
    }

    // if inserting, need to process child objects after save.
    if ( userToSave.getId() == null )
    {
      // get the children and hold onto them.
      Set addresses = userToSave.getUserAddresses();
      Set phones = userToSave.getUserPhones();
      Set emailAddresses = userToSave.getUserEmailAddresses();
      Set nodes = userToSave.getUserNodes();
      Set userRoles = userToSave.getUserRoles();

      // clear out children for clean insert of user
      userToSave.setUserAddresses( new LinkedHashSet() );
      userToSave.setUserPhones( new LinkedHashSet() );
      userToSave.setUserEmailAddresses( new LinkedHashSet() );
      userToSave.setUserNodes( new LinkedHashSet() );

      userToSave = this.userDAO.saveUser( userToSave );

      for ( Iterator userRolesIter = userRoles.iterator(); userRolesIter.hasNext(); )
      {
        UserRole userRole = (UserRole)userRolesIter.next();
        userToSave.addRole( userRole.getRole() );
      }

      Iterator addressIterator = addresses.iterator();
      while ( addressIterator.hasNext() )
      {
        userToSave.addUserAddress( (UserAddress)addressIterator.next() );
      }

      Iterator phoneIterator = phones.iterator();
      while ( phoneIterator.hasNext() )
      {
        userToSave.addUserPhone( (UserPhone)phoneIterator.next() );
      }

      Iterator emailIterator = emailAddresses.iterator();
      while ( emailIterator.hasNext() )
      {
        userToSave.addUserEmailAddress( (UserEmailAddress)emailIterator.next() );
      }

      Iterator nodeIterator = nodes.iterator();
      while ( nodeIterator.hasNext() )
      {
        UserNode userNode = (UserNode)nodeIterator.next();
        Node node = nodeDAO.getNodeByNameAndHierarchy( userNode.getNode().getName(), userNode.getNode().getHierarchy() );
        if ( node == null )
        {
          List serviceErrors = new ArrayList();
          serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.NODE_BY_NAME_NOT_FOUND, userNode.getNode().getName() ) );
          throw new ServiceErrorExceptionWithRollback( serviceErrors );
        }
        userNode.setNode( node );
        userToSave.addUserNode( userNode );
      }
    }
    else
    {
      // user already in db - see if need to update awardbanq because of a name change
      if ( userToSave instanceof Participant )
      {
        updateAwardbanName = awardbanQNameCheck( userToSave, userToSave.getId() );
      }
    }

    // ------------------
    // Save the User
    // ------------------
    dbUser = this.userDAO.saveUser( userToSave );

    // --------------------------------
    // Update AwardbanQ if necessary
    // --------------------------------
    if ( updateAwardbanName )
    {
      Participant updatedParticipant = participantDAO.getParticipantOverviewById( dbUser.getId() );
      try
      {
        awardBanQServiceFactory.getAwardBanQService().updateParticipantInAwardBanQWebService( updatedParticipant );
      }
      catch( JsonGenerationException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( JsonMappingException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( UniformInterfaceException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( ClientHandlerException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( IOException e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
      catch( Exception e )
      {
        logger.error( e );
        throw new ServiceErrorException( e.getMessage(), e );
      }
    }

    // If the user is inactive, then the user's node association's should also be inactive.
    Boolean isActive = dbUser.isActive();
    if ( isActive != null )
    {
      for ( Iterator iter = dbUser.getUserNodes().iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        userNode.setActive( isActive );
      }
    }

    return dbUser;
  }

  @Override
  public void deleteCharacteristicForUser( Long userId, Long charcteristicId )
  {
    userDAO.deleteAttributeForUser( userId, charcteristicId );
  }

  @Override
  public UserCharacteristic getUserCharacteristicById( Long userId, Long charactersticId )
  {
    return userDAO.getUserCharacteristicById( userId, charactersticId );
  }

  @Override
  public UserCharacteristic updateCharacteristicForUser( Long userId, UserCharacteristic userCharacteristic )
  {
    User user = userDAO.getUserById( userId );

    userCharacteristic.setUser( user );
    return userDAO.updateUserCharacteristic( userCharacteristic );
  }

  @Override
  public List<UserCharacteristic> getUserCharacteristicsByUserId( Long userId )
  {
    return userDAO.getUserCharacteristicsByUserId( userId );
  }

  @Override
  public void deleteUser( User userToSave ) throws UniqueConstraintViolationException
  {
    // Check to see if the user already exists in the database.
    User dbUser = userDAO.getUserByUserName( userToSave.getUserName() );
    if ( dbUser != null )
    {
      if ( userToSave.getId() == null )
      {
        throw new UniqueConstraintViolationException();
      }
      if ( dbUser.getId().compareTo( userToSave.getId() ) != 0 )
      {
        throw new UniqueConstraintViolationException();
      }
    }
    userToSave.getUserEmailAddresses().clear();
    userToSave.getUserPhones().clear();
    userToSave.getUserAddresses().clear();
    userToSave.setUserEmailAddresses( null );
    userToSave.setUserAddresses( null );
    userToSave.setUserPhones( null );
    userDAO.saveUser( userToSave );
  }

  @Override
  public User getUserByPrimaryEmailAddr( String emailAddr )
  {
    return userDAO.getUserByPrimaryEmailAddr( emailAddr );
  }

  @Override
  public boolean IsUserAccountLocked( Long userId ) throws Exception
  {
    boolean isAccLocked = false;
    User user = userDAO.getUserById( userId );
    if ( Objects.nonNull( user ) )
    {
      isAccLocked = user.isAccountLocked();

    }
    return isAccLocked;
  }

  @Override
  public void resetUserChatracteristic( Long userId, Long charcteristicId )
  {
    userDAO.resetUserChatracteristic( userId, charcteristicId );
  }

  public List<PickListValueBean> getPickListValuesFromCM( String assetCode, String locale )
  {
    return userDAO.getPickListValuesFromCM( assetCode, locale );
  }

  @Override
  public PickListValueBean getPickListValueFromCMView( String assetCode, String locale, String code )
  {
    return userDAO.getPickListValueFromCMView( assetCode, locale, code );
  }

  public boolean IsUserinOrgUnitorBelow( Long loggedinUserId, Long searchedUserId )
  {
    return userDAO.IsUserinOrgUnitorBelow( loggedinUserId, searchedUserId );
  }

  @Override
  public Long getUserIdByRosterUserId( UUID rosterUserId )
  {
    return userDAO.getUserIdByRosterUserId( rosterUserId );
  }

  @Override
  public UUID getRosterUserIdByUserId( Long userId )
  {
    return userDAO.getRosterUserIdByUserId( userId );
  }

  @Override
  public List<EmailDetail> getEmailDetailsByUserId( Long userId )
  {
    return userDAO.getEmailDetailsByUserId( userId );
  }

  @Override
  public List<PhoneDetail> getPhoneDetailsByUserId( Long userId )
  {
    return userDAO.getPhoneDetailsByUserId( userId );
  }

  @Override
  public List<UserAddressDetail> getUserAddressDetailsByUserId( Long userId )
  {
    return userDAO.getUserAddressDetailsByUserId( userId );
  }

  @Override
  public Long getUserPhoneIdByRosterPhoneId( UUID rosterPhoneId )
  {
    return userDAO.getUserPhoneIdByRosterPhoneId( rosterPhoneId );
  }

  @Override
  public UUID getRosterPhoneIdByUserPhoneId( Long userPhoneId )
  {
    return userDAO.getRosterPhoneIdByUserPhoneId( userPhoneId );
  }

  @Override
  public Long getEmailAddressIdByRosterEmailId( UUID rosterEmailId )
  {
    return userDAO.getEmailAddressIdByRosterEmailId( rosterEmailId );
  }


  /*WIP 20160 customization start */
  public Long getUserIdByName(String userName)
  {
	  return userDAO.getUserIdByName(userName);
  }
  /*WIP 20160 customization end */

  @Override
  public UUID getRosterEmailIdByEmailAddressId( Long emailAddressId )
  {
    return userDAO.getRosterEmailIdByEmailAddressId( emailAddressId );
  }

  @Override
  public Long getUserAddressIdByRosterAddressId( UUID rosterAddressId )
  {
    return userDAO.getUserAddressIdByRosterAddressId( rosterAddressId );
  }

  @Override
  public UUID getRosterAddressIdByUserAddressId( Long userAddressId )
  {
    return userDAO.getRosterAddressIdByUserAddressId( userAddressId );
  }

  @Override
  public User getUserByRosterUserId( UUID rosterUserId )
  {
    return this.userDAO.getUserByRosterUserId( rosterUserId );
  }

  @Override
  public UserEmailAddress getUserEmailAddressByRosterEmailId( UUID rosterEmailId )
  {
    return userDAO.getUserEmailAddressByRosterEmailId( rosterEmailId );
  }

  @Override
  public UserPhone getUserPhoneByrosterPhoneId( UUID rosterPhoneId )
  {
    return userDAO.getUserPhoneByrosterPhoneId( rosterPhoneId );
  }

  @Override
  public UserAddress getUserAddressByRosterAddressId( UUID rosterAddressId )
  {
    return userDAO.getUserAddressByRosterAddressId( rosterAddressId );
  }

  // Client customizations for WIP #42701 starts
  public String getUserCurrencyCharValue( Long userId )
  {
    return this.userDAO.getUserCurrencyCharValue( userId );
  }
  
  public String getUserDivisionKeyCharValue( Long userId )
  {
    return this.userDAO.getUserDivisionKeyCharValue( userId );
  }
  
  public String getUserJobGradeCharValue( Long userId )
  {
    return this.userDAO.getUserJobGradeCharValue( userId );
  }
  // Client customizations for WIP #42701 ends
  
  // Client customizations for WIP #42683 starts
  public String getUserCountryCharValue( Long userId )
  {
    return this.userDAO.getUserCountryCharValue( userId );
  }

  public String getUserServiceCharValue( Long userId )
  {
    return this.userDAO.getUserServiceCharValue( userId );
  }
  // Client customizations for WIP #42683 ends
  /* Customization for WIP 32479 starts here */
  @Override
  public boolean isUserEmailAddressExists( String emailAddress )
  {
    return this.userDAO.isUserEmailAddressExists( emailAddress );
  }
  /* Customization for WIP 32479 ends here */
  public List<UserDivisionValueBean> getUserDivisionKey(Long sUserId)
  {
	 return this.userDAO.getUserDivisionKey( sUserId );
  } 
}
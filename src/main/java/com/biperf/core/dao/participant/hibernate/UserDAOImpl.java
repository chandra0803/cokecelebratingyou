/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/hibernate/UserDAOImpl.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.recognitionadvisor.hibernate.CallPrcRecognitionAdvisorWelcomeEmail;
import com.biperf.core.dao.reports.hibernate.BaseReportsResultTransformer;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserLoginInfo;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.crypto.MD5Hash;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.biperf.core.utils.hibernate.HibernateUtil;
import com.biperf.core.value.EmailDetail;
import com.biperf.core.value.PhoneDetail;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.RAValueBean;
import com.biperf.core.value.UserAddressDetail;
import com.biperf.core.value.UserDivisionValueBean;
import com.biperf.core.value.UserValueBean;

/**
 * UserDAOImpl used for persisting and retreiving User domain objects.
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
public class UserDAOImpl extends BaseDAO implements UserDAO
{

  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";
  public static final String P_OUT_DATA = "p_out_user_data";

  private static final Log log = LogFactory.getLog( UserDAOImpl.class );
  private static final String DELETE_VALUE = "delete_option";

  private DataSource dataSource;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  /**
   * Get the user by their Id. Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getUserById(java.lang.Long)
   * @param id
   * @return User
   */
  @Override
  public User getUserById( Long id )
  {
    User user = (User)getSession().get( User.class, id );
    return user;
  }

  /**
   * Get the user by their Id. Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getUserById(java.lang.Long)
   * @param id
   * @return User
   */
  @Override
  public UserEmailAddress getUserEmailAddressById( Long id )
  {
    return (UserEmailAddress)getSession().get( UserEmailAddress.class, id );
  }

  /**
   * Get the user by their Id. Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getUserById(java.lang.Long)
   * @param id
   * @return User
   */
  @Override
  public UserPhone getUserPhoneById( Long id )
  {
    return (UserPhone)getSession().get( UserPhone.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getUserByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return User
   */
  @Override
  public User getUserByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    User user = (User)getSession().get( User.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( user );
    }

    return user;
  }

  @Override
  public User getUserByUserNameWithAssociations( String username, AssociationRequestCollection associationRequestCollection )
  {
    User user = getUserByUserName( username );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( user );
    }

    return user;
  }

  /**
   * 
   * @see com.biperf.core.dao.participant.UserDAO#saveLoginInfo(java.lang.Long)
   * @param id
   * @return void
   */
  @Override
  public void saveLoginInfo( UserLoginInfo userLoginInfo )
  {
    getSession().save( userLoginInfo );
  }

  /**
   * Validates the password
   * 
   * @param userName
   * @param password
   * @return boolean isPasswordValid
   */
  /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
  /* Changing method parameter from userName to User object */
  // public boolean isPasswordValid( String userName, String password )
  @Override
  public boolean isPasswordValid( User user, String password )
  /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
  {
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // String upperUserName = userName.toUpperCase();
    String upperUserName = user.getUserName().toUpperCase();
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    if ( StringUtils.isEmpty( user.getPassword() ) )
    {
      return false;
    }
    Query validatePwdQuery = getSession().getNamedQuery( "com.biperf.core.domain.user.ValidatePassword" );

    validatePwdQuery.setString( "userName", upperUserName );
    if ( user.getPassword().startsWith( "{MD5}" ) )
    {
      validatePwdQuery.setString( "password", new MD5Hash().encryptDefault( password ) );
    }
    else
    {
      validatePwdQuery.setString( "password", new SHA256Hash().encryptDefault( password ) );
    }
    String dbUserName = (String)validatePwdQuery.uniqueResult();
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    if ( upperUserName.equalsIgnoreCase( dbUserName ) )
    {
      return true;
    }
    return false;
  }

  /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
  /* Changing method parameter from userName to User object */
  // public boolean isLoginTokenValid( String userName, String loginToken )
  @Override
  public boolean isLoginTokenValid( User user, String loginToken )
  {
    // if(userName == null || loginToken == null)
    if ( user == null || loginToken == null )
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    {
      return false;
    }
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // String upperUserName = userName.toUpperCase();
    String upperUserName = user.getUserName().toUpperCase();
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    boolean loginTokenValid = false;

    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    /*
     * Will keep this below code few months until all the users have been updated with SHA256
     * encryption of their passwords. After all the users are converted, we can remove/edit MD5 hash
     * related code
     */
    /*
     * String dbUserName = (String)session .getNamedQuery(
     * "com.biperf.core.domain.user.ValidateLoginToken" ).setString( "userName", upperUserName )
     * .setString( "loginToken", new MD5Hash().encryptDefault( loginToken ) ).uniqueResult();
     */
    Query validateTokenQuery = getSession().getNamedQuery( "com.biperf.core.domain.user.ValidateLoginToken" );

    validateTokenQuery.setString( "userName", upperUserName );
    if ( user.getLoginToken() != null && user.getLoginToken().startsWith( "{MD5}" ) )
    {
      validateTokenQuery.setString( "loginToken", new MD5Hash().encryptDefault( loginToken ) );
    }
    else
    {
      validateTokenQuery.setString( "loginToken", new SHA256Hash().encryptDefault( loginToken ) );
    }
    String dbUserName = (String)validateTokenQuery.uniqueResult();
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    if ( upperUserName.equalsIgnoreCase( dbUserName ) )
    {
      loginTokenValid = true;
    }

    return loginTokenValid;
  }

  /**
   * Saves the user information to the database.
   * 
   * @param user
   * @return User
   */
  @Override
  public User saveUser( User user )
  {
    return (User)HibernateUtil.saveOrUpdateOrShallowMerge( user );
  }

  @Override
  public Country getPrimaryUserAddressCountry( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.country.getPrimaryAddressCountrForUser" );
    query.setLong( "userId", userId );
    return (Country)query.uniqueResult();
  }

  /**
   * Get the User by their Username.
   * 
   * @param userName
   * @return User
   */
  @Override
  public User getUserByUserName( String userName )
  {
    String upperUserName = userName.toUpperCase();
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.UserLookupByUserName" );
    query.setString( "userName", upperUserName );
    User user = (User)query.uniqueResult();
    return user;
  }

  /**
   * Gets a list of all roles. Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getAll()
   * @return List
   */
  @Override
  public List getAll()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.AllUsers" );
    return query.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getAllUsersNonParticipant()
   * @return List
   */
  @Override
  public List getAllUsersNonParticipant()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.AllUsersNonParticipant" );
    return query.list();
  }

  /**
   * Search for users with the param criteria. Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#searchUser(java.lang.String, java.lang.String,
   *      java.lang.String)
   * @param firstName
   * @param lastName
   * @param userName
   * @return List
   */
  @Override
  public List searchUser( String firstName, String lastName, String userName )
  {

    Criteria searchCriteria = getSession().createCriteria( User.class );
    searchCriteria.add( Restrictions.ne( "userType", UserType.lookup( UserType.PARTICIPANT ) ) );
    searchCriteria.add( Restrictions.ne( "active", new Boolean( false ) ) );

    if ( null != firstName && !"".equals( firstName ) )
    {
      searchCriteria.add( Restrictions.ilike( "firstName", firstName, MatchMode.ANYWHERE ) );
    }

    if ( null != lastName && !"".equals( lastName ) )
    {
      searchCriteria.add( Restrictions.ilike( "lastName", lastName, MatchMode.ANYWHERE ) );
    }

    if ( null != userName && !"".equals( userName ) )
    {
      searchCriteria.add( Restrictions.ilike( "userName", userName, MatchMode.ANYWHERE ) );
    }

    searchCriteria.addOrder( Order.asc( "userName" ) );

    return searchCriteria.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Set<Long> getUserIdsByEmail( String emailAddress )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.UserIdsByEmail" );
    query.setString( "emailAddress", emailAddress );
    return new HashSet<Long>( query.list() );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Set<Long> getUserIdsByContactPhone( String phoneNumber )
  {
    // Strip leading zeros, they could be part of the country code
    phoneNumber = phoneNumber.replaceFirst( "^0+", "" );

    CallPrcGetUserContactByPhone phoneProcedure = new CallPrcGetUserContactByPhone( dataSource );
    Map<String, Object> phoneProcedureResults = phoneProcedure.executeProcedureAll( phoneNumber );
    List<PaxContactType> contactTypes = (List<PaxContactType>)phoneProcedureResults.get( "p_out_result_set" );
    return contactTypes.stream().map( PaxContactType::getUserId ).collect( Collectors.toSet() );
  }

  @Override
  public int getPrimaryEmailCountByEmailAddress( String email )
  {
    Criteria criteria = getSession().createCriteria( UserEmailAddress.class );
    criteria.add( Restrictions.eq( "emailAddr", email ).ignoreCase() );
    criteria.add( Restrictions.eq( "isPrimary", true ) );
    criteria.setProjection( Projections.rowCount() );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  /**
   * Gets a Set of all available roles for a given user. Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getAvailableRoles(Long userId)
   * @param userId
   * @return Set
   */
  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public Set getAvailableRoles( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.AvailableRoles" );
    query.setLong( "userId", userId.longValue() );

    return new LinkedHashSet( query.list() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#updateUserCharacteristic(com.biperf.core.domain.user.UserCharacteristic)
   * @param userCharacteristic
   */
  @Override
  public UserCharacteristic updateUserCharacteristic( UserCharacteristic userCharacteristic )
  {
    if ( userCharacteristic.getId() != null && userCharacteristic.getCharacteristicValue().equals( DELETE_VALUE ) )
    {
      getSession().delete( userCharacteristic );
    }
    else if ( !userCharacteristic.getCharacteristicValue().equals( DELETE_VALUE ) )
    {
      getSession().merge( userCharacteristic );
    }
    return userCharacteristic;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#updateUserCharacteristics(List)
   * @param userCharacteristics
   */
  @Override
  public void updateUserCharacteristics( List userCharacteristics )
  {
    for ( int i = 0; i < userCharacteristics.size(); i++ )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)userCharacteristics.get( i );
      if ( userCharacteristic.getId() != null && userCharacteristic.getCharacteristicValue().equals( DELETE_VALUE ) )
      {
        getSession().delete( userCharacteristic );
      }
      else if ( !userCharacteristic.getCharacteristicValue().equals( DELETE_VALUE ) )
      {
        getSession().merge( userCharacteristic );
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#updateUserEmailAddress(com.biperf.core.domain.user.UserEmailAddress)
   * @param userEmailAddress
   */
  @Override
  public void updateUserEmailAddress( UserEmailAddress userEmailAddress )
  {
    getSession().merge( userEmailAddress );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#updateUserPhone(com.biperf.core.domain.user.UserPhone)
   * @param userPhone
   * @return UserPhone
   */
  @Override
  public UserPhone updateUserPhone( UserPhone userPhone )
  {
    getSession().merge( userPhone );
    return userPhone;
  }

  /**
   * Update the UserAddress record. Implementations should take into account the Object tree and
   * update reference to this child in the parent. Hibernate's getSession().merge will take care of this.
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#updateUserAddress(com.biperf.core.domain.user.UserAddress)
   * @param userAddress
   * @return UserAddress
   */
  @Override
  public UserAddress updateUserAddress( UserAddress userAddress )
  {
    getSession().merge( userAddress );
    return userAddress;
  }

  /**
   * Returns a list of available acls for the user record associated to the userId param.
   * 
   * @param userId
   * @return List
   */
  @Override
  public List getAvailableAcls( Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAvailableAcls" );
    query.setLong( "userId", userId.longValue() );

    return query.list();
  }

  /**
   * 
   * Overridden from @see com.biperf.core.dao.participant.UserDAO#getAssignedNodeIds(java.lang.Long)
   * @param userId
   * @return List
   */
  @Override
  public List getAssignedNodeIds( Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAssignedNodeIds" );
    query.setLong( "userId", userId.longValue() );

    return query.list();
  }

  /**
   * Returns a list of nodes assigned to the user associated to the Id param.
   * 
   * @param userId
   * @return List
   */
  @Override
  public List getAssignedNodes( Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAssignedNodes" );
    query.setLong( "userId", userId.longValue() );

    return query.list();
  }

  /**
   * Fetch a list of nodes not assigned to the user associated to the Id param.
   * 
   * @param userId
   * @return List
   */
  @Override
  public List getUnassignedNodes( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetUnassignedNodes" );
    query.setLong( "userId", userId.longValue() );

    return query.list();
  }

  /**
   * @return List of user value beans
   */
  @Override
  public List getAllUsersForWelcomeMail()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAllUsersForWelcomeEmail" );

    query.setResultTransformer( new UserValueBeanRowMapper() );

    return query.list();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<RAValueBean> getAllUsersForRAWelcomeMail()
  {

    List<RAValueBean> raValueBeanList = null;
    @SuppressWarnings( "rawtypes" )
    Map results = new HashMap();
    CallPrcRecognitionAdvisorWelcomeEmail proc = new CallPrcRecognitionAdvisorWelcomeEmail( dataSource );
    results = proc.execute();
    raValueBeanList = validateOutput( raValueBeanList, results );
    return raValueBeanList;
  }

  private class UserValueBeanRowMapper extends BaseReportsResultTransformer
  {
    @Override
    public UserValueBean transformTuple( Object[] tuple, String[] aliases )
    {
      UserValueBean valueBean = new UserValueBean();
      valueBean.setId( extractLong( tuple[0] ) );
      valueBean.setFirstName( extractString( tuple[1] ) );
      valueBean.setLastName( extractString( tuple[2] ) );
      valueBean.setMiddleName( extractString( tuple[3] ) );
      valueBean.setEmailAddress( extractString( tuple[4] ) );
      valueBean.setUserName( extractString( tuple[5] ) );
      valueBean.setDuplicateEmail( extractBoolean( tuple[6] ) );
      return valueBean;
    }

  }

  private class RAValueBeanRowMapper extends BaseReportsResultTransformer
  {
    @Override
    public RAValueBean transformTuple( Object[] tuple, String[] aliases )
    {
      RAValueBean valueBean = new RAValueBean();
      valueBean.setUserId( extractLong( tuple[0] ) );
      valueBean.setFirstName( extractString( tuple[1] ) );
      valueBean.setLastName( extractString( tuple[2] ) );

      valueBean.setEmailAddress( extractString( tuple[3] ) );

      return valueBean;
    }

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.UserDAO#getAllUsersOnNode(java.lang.Long)
   * @param nodeId
   * @return List of Users on a Node
   */
  @Override
  public List getAllUsersOnNode( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAllUsersOnNode" );

    query.setLong( "nodeId", nodeId.longValue() );

    return query.list();
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
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAllUsersOnNodeHavingRole" );

    query.setLong( "nodeId", nodeId.longValue() );
    query.setString( "hierarchyRoleTypeCode", hierarchyRoleType.getCode() );
    List results = query.list();
    associationRequestCollection.process( results );
    return results;
  }

  /**
   * Gets a list of all of the participants on a node.
   * 
   * @param nodeId
   * @return List of all of the participants on a specific node
   */
  @Override
  @SuppressWarnings( "unchecked" )
  public List<User> getAllParticipantsOnNode( Long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAllParticipantsOnNode" );

    query.setLong( "nodeId", nodeId.longValue() );

    return query.list();
  }

  @Override
  public UserNode getUserNode( long userId, long nodeId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserNode" );
    query.setLong( "nodeId", nodeId );
    query.setLong( "userId", userId );
    return (UserNode)query.uniqueResult();
  }

  @Override
  public User getUserByEmailAddr( String emailAddr )
  {
    Criteria criteria = getSession().createCriteria( User.class );
    criteria.add( Restrictions.eq( "userType", UserType.lookup( UserType.PARTICIPANT ) ) );
    criteria.createAlias( "userEmailAddresses", "userEmailAddresses" );
    criteria.add( Restrictions.eq( "userEmailAddresses.emailAddr", emailAddr ) );
    return (User)criteria.uniqueResult();
  }

  @Override
  public UserValueBean getUserDetailsForWelcomeMail( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetUserDetailsForWelcomeMail" );
    query.setLong( "userId", userId.longValue() );
    query.setResultTransformer( new UserValueBeanRowMapper() );
    return (UserValueBean)query.uniqueResult();
  }

  @Override
  public String getUserTimeZone( Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetUserTimeZoneid" );
    query.setParameter( "userId", userId );
    return (String)query.uniqueResult();
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public String getCountryProgramIdByUserId( Long userId )
  {
    Query query = getSession().createSQLQuery( getQueryForCountryProgramId() );
    query.setParameter( "userId", userId );
    List<String> results = query.list();
    if ( results != null && results.size() == 1 )
    {
      return results.get( 0 );
    }
    else if ( results != null && results.size() > 1 )
    {
      // Should be unique result. If more than 1 throw exception
      throw new BeaconRuntimeException( "Should not have more than one program_id for the user" );
    }
    return null;
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public String getCountryProgramId( Long userId )
  {
    Query query = getSession().createSQLQuery( getCountryProgramIdQuery() );
    query.setParameter( "userId", userId );
    List<String> results = query.list();
    if ( results != null && results.size() == 1 )
    {
      return results.get( 0 );
    }
    else if ( results != null && results.size() > 1 )
    {
      // Should be unique result. If more than 1 throw exception
      throw new BeaconRuntimeException( "Should not have more than one program_id for the user" );
    }
    return null;
  }
//Client customizations for WIP #42701 starts
  
 public String getUserCurrencyCharValue( Long userId )
 {
   Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserCurrencyCharValue" );
   query.setParameter( "userId", userId );
   String currency = (String)query.uniqueResult();
   if ( StringUtil.isNullOrEmpty( currency ) )
     return "USD";
   else
     return currency;
 }
 
 public String getUserDivisionKeyCharValue( Long userId )
 {
   Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserDivisionKeyCharValue" );
   query.setParameter( "userId", userId );
   String divisionKey = (String)query.uniqueResult();
   return divisionKey;
 }
 
 public String getUserJobGradeCharValue( Long userId )
 {
   Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserJobGradeCharValue" );
   query.setParameter( "userId", userId );
   String jobGrade = (String)query.uniqueResult();
   return jobGrade;
 }
 // Client customizations for WIP #42701 ends

 // Client customizations for WIP #42683 starts
 public String getUserCountryCharValue( Long userId )
 {
   Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserCountryCharValue" );
   query.setParameter( "userId", userId );
   String country = (String)query.uniqueResult();
   return country;
 }

 public String getUserServiceCharValue( Long userId )
 {
   Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserServiceCharValue" );
   query.setParameter( "userId", userId );
   String service = (String)query.uniqueResult();
   return service;
 }
 // Client customizations for WIP #42683 ends
  // program id should be retrieved only for internal suppliers only
  private static String getQueryForCountryProgramId()
  {
    StringBuilder sql = new StringBuilder();
    sql.append( " SELECT DISTINCT c.program_id " );
    sql.append( " FROM country_suppliers cs, " );
    sql.append( "   supplier s, " );
    sql.append( "   user_address ua, " );
    sql.append( "   country c " );
    sql.append( " WHERE ua.country_id=c.country_id " );
    sql.append( " AND c.country_id   =cs.country_id " );
    sql.append( " AND ua.user_id     = :userId " );
    sql.append( " AND cs.supplier_id =s.supplier_id " );
    sql.append( " AND s.supplier_type='internal' " );
    sql.append( " AND ua.is_primary  = 1 " );
    return sql.toString();
  }

  private static String getCountryProgramIdQuery()
  {
    StringBuilder sql = new StringBuilder();
    sql.append( " SELECT DISTINCT c.program_id " );
    sql.append( " FROM country_suppliers cs, " );
    sql.append( "   supplier s, " );
    sql.append( "   user_address ua, " );
    sql.append( "   country c " );
    sql.append( " WHERE ua.country_id=c.country_id " );
    sql.append( " AND c.country_id   =cs.country_id " );
    sql.append( " AND ua.user_id     = :userId " );
    sql.append( " AND cs.supplier_id =s.supplier_id " );
    sql.append( " AND ua.is_primary  = 1 " );
    return sql.toString();
  }

  @Override
  @SuppressWarnings( "unchecked" )
  public List<User> getAllUsersBasedOnCharacteristics( Long characteristicId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAllUsersBasedOnCharacteristics" );

    query.setLong( "characteristicId", characteristicId.longValue() );

    return query.list();
  }

  @Override
  public String getUserTimeZoneForAdmin()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetUserTimeZoneidForAdmin" );
    return (String)query.uniqueResult();
  }

  @Override
  public void deleteLoginTokenFor( Long userId )
  {
    User user = getUserById( userId );
    user.setLoginToken( null );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<Long> getAssignedNodesIdList( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.GetAssignedNodesIdList" );
    query.setLong( "userId", userId.longValue() );
    return query.list();
  }

  @Override
  public int getAllLoginActivityCount( Long userId )
  {
    Criteria criteria = getSession().createCriteria( UserLoginInfo.class );
    criteria.setProjection( Projections.rowCount() );
    criteria.add( Restrictions.eq( "userId", userId ) );
    return ( (Long)criteria.uniqueResult() ).intValue();
  }

  @Override
  public boolean isUniqueEmail( String email )
  {
    Criteria criteria = getSession().createCriteria( UserEmailAddress.class );
    criteria.setProjection( Projections.distinct( Projections.property( "user.id" ) ) );
    criteria.add( Restrictions.eq( "emailAddr", email ).ignoreCase() );
    return criteria.list().size() == 1 ? true : false;
  }

  @Override
  public boolean updateRecoveryEmail( Long userId, String emailAddress )
  {
    Criteria criteria = getSession().createCriteria( UserEmailAddress.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "emailType", EmailAddressType.lookup( EmailAddressType.RECOVERY ) ) );

    UserEmailAddress emailAddr = (UserEmailAddress)criteria.uniqueResult();

    if ( emailAddr != null )
    {
      if ( !emailAddr.getEmailAddr().equals( emailAddress ) )
      {
        emailAddr.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      }
      emailAddr.setEmailAddr( emailAddress );
      getSession().update( emailAddr );
      return true;
    }

    return false;
  }

  @Override
  public boolean updateRecoveryPhone( Long userId, String phoneNumber, String countryPhoneCode )
  {
    Criteria criteria = getSession().createCriteria( UserPhone.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "phoneType", EmailAddressType.lookup( EmailAddressType.RECOVERY ) ) );

    UserPhone userPhone = (UserPhone)criteria.uniqueResult();
    if ( userPhone != null )
    {
      if ( !userPhone.getPhoneNbr().equals( phoneNumber ) )
      {
        userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
      }
      userPhone.setCountryPhoneCode( countryPhoneCode );
      userPhone.setPhoneNbr( phoneNumber );
      getSession().update( userPhone );
      return true;
    }
    return false;
  }

  @Override
  public boolean isUniquePhoneNumber( String phoneNumber )
  {
    Criteria criteria = getSession().createCriteria( UserPhone.class );
    criteria.setProjection( Projections.distinct( Projections.property( "user.id" ) ) );
    criteria.add( Restrictions.eq( "phoneNbr", phoneNumber ).ignoreCase() );
    return criteria.list().size() == 1 ? true : false;
  }

  @Override
  public List<StrongMailUser> getAllStrongMailUsers()
  {
    Criteria criteria = getSession().createCriteria( StrongMailUser.class );
    return criteria.list();
  }

  @Override
  public void updateStrongMailUserByUserName( StrongMailUser user )
  {
    getSession().saveOrUpdate( user );
    getSession().flush();
  }

  @SuppressWarnings( "unchecked" )
  private List<RAValueBean> validateOutput( List<RAValueBean> raValueBeanList, @SuppressWarnings( "rawtypes" ) Map results )
  {
    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
    }
    else
    {
      raValueBeanList = (List<RAValueBean>)results.get( "p_out_user_data" );
    }
    return raValueBeanList;
  }

  @Override
  public void deleteUserEmailAddress( long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.UserEmailAddress.deleteuseremailaddress" );
    query.setParameter( "userId", userId );
    query.executeUpdate();
    getSession().flush();

  }

  @Override
  public void deleteUserPhones( long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.UserPhone.deleteuserphones" );
    query.setParameter( "userId", userId );
    query.executeUpdate();
    getSession().flush();

  }

  @Override
  public void deleteEmailAddressForUser( Long userId, Long userEmailAddressId )
  {
    Criteria criteria = getSession().createCriteria( UserEmailAddress.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "id", userEmailAddressId ) );
    UserEmailAddress userEmailAddress = (UserEmailAddress)criteria.uniqueResult();
    getSession().delete( userEmailAddress );
    getSession().flush();

  }

  @Override
  public void deletePhoneNumberForUser( Long userId, Long userPhoneNumberId )
  {
    Criteria criteria = getSession().createCriteria( UserPhone.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "id", userPhoneNumberId ) );
    UserPhone userPhone = (UserPhone)criteria.uniqueResult();
    getSession().delete( userPhone );
    getSession().flush();

  }

  @Override
  public void deleteAddressForUser( Long userId, Long userAddressId )
  {
    Criteria criteria = getSession().createCriteria( UserAddress.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "id", userAddressId ) );
    UserAddress userEmailAddress = (UserAddress)criteria.uniqueResult();
    getSession().delete( userEmailAddress );
    getSession().flush();

  }

  @Override
  public void deleteAttributeForUser( Long userId, Long charcteristicId )
  {
    Criteria criteria = getSession().createCriteria( UserCharacteristic.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "userCharacteristicType.id", charcteristicId ) );
    UserCharacteristic userCharacteristic = (UserCharacteristic)criteria.uniqueResult();
    getSession().delete( userCharacteristic );
    getSession().flush();

  }

  @Override
  public UserAddress getUserAddressById( Long id )
  {
    return (UserAddress)getSession().get( UserAddress.class, id );
  }

  @Override
  public UserEmailAddress updateEmailAddressForUser( UserEmailAddress userEmailAddress )
  {
    getSession().merge( userEmailAddress );
    return userEmailAddress;
  }

  @Override
  public UserCharacteristic getUserCharacteristicById( Long userId, Long charactersticId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( UserCharacteristic.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "userCharacteristicType.id", charactersticId ) );
    return (UserCharacteristic)criteria.uniqueResult();

  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<UserCharacteristic> getUserCharacteristicsByUserId( Long userId )
  {
    Criteria userCharacteristiccriteria = HibernateSessionManager.getSession().createCriteria( UserCharacteristic.class );
    userCharacteristiccriteria.add( Restrictions.eq( "user.id", userId ) );
    return userCharacteristiccriteria.list();

  }

  @Override
  public User save( User user )
  {
    user = (User)HibernateUtil.saveOrUpdateOrShallowMerge( user );
    getSession().flush();
    getSession().refresh( user );
    return user;
  }

  @Override
  public User getUserByPrimaryEmailAddr( String emailAddr )
  {
    Criteria criteria = getSession().createCriteria( User.class );
    criteria.createAlias( "userEmailAddresses", "userEmailAddresses" );
    criteria.add( Restrictions.eq( "userEmailAddresses.emailAddr", emailAddr ).ignoreCase() );
    criteria.add( Restrictions.eq( "userEmailAddresses.isPrimary", true ) );
    return criteria.list().size() > 1 ? null : (User)criteria.uniqueResult();
  }

  @Override
  public void resetUserChatracteristic( Long userId, Long charcteristicId )
  {
    Criteria criteria = getSession().createCriteria( UserCharacteristic.class );
    criteria.add( Restrictions.eq( "user.id", userId ) );
    criteria.add( Restrictions.eq( "id", charcteristicId ) );
    UserCharacteristic userCharacteristic = (UserCharacteristic)criteria.uniqueResult();
    getSession().delete( userCharacteristic );
    getSession().flush();

  }

  @Override
  public List<PickListValueBean> getPickListValuesFromCM( String assetCode, String locale )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.audience.getPickListValuesForBean" );
    query.setParameter( "assetCode", assetCode );
    query.setParameter( "locale", locale );
    query.setResultTransformer( Transformers.aliasToBean( PickListValueBean.class ) );
    return query.list();
  }

  @Override
  public PickListValueBean getPickListValueFromCMView( String assetCode, String locale, String code )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getPickListValueFromCMView" );
    query.setParameter( "assetCode", assetCode );
    query.setParameter( "locale", locale );
    query.setParameter( "code", code );
    query.setResultTransformer( Transformers.aliasToBean( PickListValueBean.class ) );
    return (PickListValueBean)query.uniqueResult();
  }

  public boolean IsUserinOrgUnitorBelow( Long loggedinUserId, Long searchedUserId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.IsUserinOrgUnitorBelow" );
    query.setParameter( "p_in_logged_in_user_id", loggedinUserId );
    query.setParameter( "p_in_searched_user_id", searchedUserId );
    return BooleanUtils.toBoolean( (Integer)query.uniqueResult() );
  }

  @Override
  public Long getUserIdByRosterUserId( UUID rosterUserId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserIdByRosterUserId" );
    query.setParameter( "rosterUserId", rosterUserId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterUserIdByUserId( Long userId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getRosterUserIdByUserId" );
    query.setParameter( "userId", userId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public List<EmailDetail> getEmailDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getEmailDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( EmailDetail.class ) );
    return (List<EmailDetail>)query.list();
  }

  @Override
  public List<PhoneDetail> getPhoneDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getPhoneDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( PhoneDetail.class ) );
    return (List<PhoneDetail>)query.list();
  }

  @Override
  public List<UserAddressDetail> getUserAddressDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserAddressDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( UserAddressDetail.class ) );
    return (List<UserAddressDetail>)query.list();
  }

  @Override
  public Long getUserPhoneIdByRosterPhoneId( UUID rosterPhoneId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserPhoneIdByRosterPhoneId" );
    query.setParameter( "rosterPhoneId", rosterPhoneId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterPhoneIdByUserPhoneId( Long userPhoneId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getRosterPhoneIdByUserPhoneId" );
    query.setParameter( "userPhoneId", userPhoneId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public Long getEmailAddressIdByRosterEmailId( UUID rosterEmailId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getEmailAddressIdByRosterEmailId" );
    query.setParameter( "rosterEmailId", rosterEmailId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterEmailIdByEmailAddressId( Long emailAddressId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getRosterEmailIdByEmailAddressId" );
    query.setParameter( "emailAddressId", emailAddressId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public Long getUserAddressIdByRosterAddressId( UUID rosterAddressId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getUserAddressIdByRosterAddressId" );
    query.setParameter( "rosterAddressId", rosterAddressId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterAddressIdByUserAddressId( Long userAddressId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getRosterAddressIdByUserAddressId" );
    query.setParameter( "userAddressId", userAddressId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public User getUserByRosterUserId( UUID rosterUserId )
  {
    Criteria searchCriteria = getSession().createCriteria( User.class );
    searchCriteria.add( Restrictions.eq( "rosterUserId", rosterUserId ) );
    return (User)searchCriteria.uniqueResult();
  }

  @Override
  public UserEmailAddress getUserEmailAddressByRosterEmailId( UUID rosterEmailId )
  {
    Criteria searchCriteria = getSession().createCriteria( UserEmailAddress.class );
    searchCriteria.add( Restrictions.eq( "rosterEmailId", rosterEmailId ) );
    return (UserEmailAddress)searchCriteria.uniqueResult();
  }

  @Override
  public UserPhone getUserPhoneByrosterPhoneId( UUID rosterPhoneId )
  {
    Criteria searchCriteria = getSession().createCriteria( UserPhone.class );
    searchCriteria.add( Restrictions.eq( "rosterPhoneId", rosterPhoneId ) );
    return (UserPhone)searchCriteria.uniqueResult();
  }

  @Override
  public UserAddress getUserAddressByRosterAddressId( UUID rosterAddressId )
  {
    Criteria searchCriteria = getSession().createCriteria( UserAddress.class );
    searchCriteria.add( Restrictions.eq( "rosterAddressId", rosterAddressId ) );
    return (UserAddress)searchCriteria.uniqueResult();
  }

  /*WIP 20160 customization start */
  public Long getUserIdByName(String userName)
  {
	Session session = HibernateSessionManager.getSession();
	Query query = session
	        .getNamedQuery( "com.biperf.core.domain.user.GetUserIdByUserName" );
	query.setString("userName", userName);
	return (Long)query.uniqueResult();
  }
  /*WIP 20160 customization end */
  /* Customization for WIP 32479 starts here */

  @Override
  public boolean isUserEmailAddressExists( String emailAddress )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.UserEmailAddress.isUserEmailAddressExists" );
    query.setString( "emailAddress", emailAddress );

    return (Integer)query.uniqueResult() > 0;
  }
  /* Customization for WIP 32479 ends here */
  

  public List<UserDivisionValueBean> getUserDivisionKey(Long sUserId)
  {
	Session session = HibernateSessionManager.getSession();
	Query query = session
	        .getNamedQuery( "com.biperf.core.domain.user.getUserDivisionKey" );
	query.setLong("userId", sUserId);
	query.setResultTransformer( new UserDivisionResultTransformer() );
	return query.list();
  }
  
  @SuppressWarnings("serial")
	private class UserDivisionResultTransformer extends BaseResultTransformer {

		@Override
		public UserDivisionValueBean transformTuple(Object[] tuple, String[] aliases) {
			UserDivisionValueBean bean = new UserDivisionValueBean();
			bean.setUserId(tuple[0] == null ? null : ((Long) tuple[0]));// USER_ID
			bean.setDivision(tuple[1] == null ? null : ((String) tuple[1])); // DIVISION_ID
			bean.setCharacteristicId(tuple[2] == null ? null : ((Long) tuple[2])); // CHARACTERISTIC
																					// ID
			return bean;
		}
	}
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/user/User.java,v $
 */

package com.biperf.core.domain.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.GenderType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.ParticipantEnrollmentSource;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.SuffixType;
import com.biperf.core.domain.enums.TitleType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.utils.BeanLocator;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * User domain object which represents ALL users within the Beacon application.
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
 * <td>Apr 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class User extends BaseDomain
{
  private String userName;
  private UserType userType;
  private TitleType titleType;
  private String firstName;
  private String middleName;
  private String lastName;
  private SuffixType suffixType;
  private String ssn;
  private String ssnDecrypted;
  private Date birthDate;
  private GenderType genderType;
  private String password;
  private String loginToken;
  private Boolean active;
  private boolean profileSetupDone;
  private Boolean welcomeEmailSent = new Boolean( false );
  private Boolean raWelcomeEmailSent = new Boolean( false );
  private Boolean acceptedCmsTerms = new Boolean( false );
  private Long masterUserId;
  private SecretQuestionType secretQuestionType;
  private String secretAnswer;
  private String secretAnswerDecrypted;
  private Integer loginFailuresCount;
  private boolean forcePasswordChange;
  private Date lastResetDate;
  private LanguageType languageType;
  private ParticipantEnrollmentSource enrollmentSource;
  private Date enrollmentDate;
  private boolean accountLocked;
  private Date lockTimeoutExpireDate;
  public Set userRoles = new LinkedHashSet();
  public Set userAcls = new LinkedHashSet();
  private Set userAddresses = new LinkedHashSet();
  private Set<UserCharacteristic> userCharacteristics = new LinkedHashSet<UserCharacteristic>();
  private Set<UserEmailAddress> userEmailAddresses = new LinkedHashSet<UserEmailAddress>();
  private Set<UserPhone> userPhones = new LinkedHashSet<UserPhone>();
  private Set<UserNode> userNodes = new LinkedHashSet<UserNode>();
  private Set<UserPasswordHistory> userPasswords = new HashSet<UserPasswordHistory>();
  private boolean acceptTermsOnTextMessages;
  private Boolean oneTimePassword = new Boolean( false );
  private Date oneTimePasswordDate;
  private UUID rosterUserId;
//#28250 customization start
  private String svcVideoURL;
  
  public UserEmailAddress getTextMessageAddress()
  {
    if ( userEmailAddresses != null )
    {
      for ( Iterator iter = userEmailAddresses.iterator(); iter.hasNext(); )
      {
        UserEmailAddress address = (UserEmailAddress)iter.next();
        if ( address.getEmailType().equals( EmailAddressType.lookup( EmailAddressType.SMS ) ) )
        {
          return address;
        }
      }
    }
    // If not found, then return null
    return null;
  }

  /**
   * Assign an Acl to this User.
   * 
   * @param acl
   */
  public void addAcl( Acl acl )
  {
    UserAcl userAcl = new UserAcl();
    userAcl.setAcl( acl );
    userAcl.setUser( this );
    this.userAcls.add( userAcl );
  }

  /**
   * Adds a list of acls to the UserAcls.
   * 
   * @param aclList
   */
  public void replaceAcls( List aclList )
  {
    this.userAcls = new LinkedHashSet();
    Iterator aclListIter = aclList.iterator();
    while ( aclListIter.hasNext() )
    {
      addAcl( (Acl)aclListIter.next() );
    }
  }

  /**
   * Get the Acls assigned to this user.
   * 
   * @return Set
   */
  public Set getUserAcls()
  {
    return this.userAcls;
  }

  /**
   * Sets the Acls onto this user.
   * 
   * @param userAcls
   */
  public void setUserAcls( Set userAcls )
  {
    this.userAcls = userAcls;
  }

  /**
   * Assign a role to this User.
   * 
   * @param role
   */
  public void addRole( Role role )
  {
    UserRole userRole = new UserRole();
    userRole.setRole( role );
    userRole.setUser( this );
    this.userRoles.add( userRole );
  }

  /**
   * Assign a Node to this User.
   * 
   * @param node
   * @param isActive
   * @param hierarchyRoleType
   */
  public void addNode( Node node, Boolean isActive, HierarchyRoleType hierarchyRoleType, boolean isPrimary )
  {
    UserNode userNode = new UserNode( this, node );
    userNode.setActive( isActive );
    userNode.setHierarchyRoleType( hierarchyRoleType );
    userNode.setIsPrimary( isPrimary );
    this.userNodes.add( userNode );
  }

  public boolean isForcePasswordChange()
  {
    return forcePasswordChange;
  }

  public void setForcePasswordChange( boolean forcePasswordChange )
  {
    this.forcePasswordChange = forcePasswordChange;
  }

  /**
   * Get the roles assigned to this user.
   * 
   * @return Set
   */
  public Set getUserRoles()
  {
    return this.userRoles;
  }

  /**
   * Sets the roles onto this user.
   * 
   * @param userRoles
   */
  public void setUserRoles( Set userRoles )
  {
    this.userRoles = userRoles;
  }

  /**
   * Get the firstName.
   * 
   * @return String
   */
  public String getFirstName()
  {
    return firstName;
  }

  /**
   * Set the firstName.
   * 
   * @param firstName
   */
  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  /**
   * Get the lastName.
   * 
   * @return String
   */
  public String getLastName()
  {
    return lastName;
  }

  /**
   * Set the lastName.
   * 
   * @param lastName
   */
  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getPossessiveSuffix()
  {
    String lastLetter = lastName.substring( lastName.length() - 1 );
    return "s".equals( lastLetter ) ? "'" : "'s";
  }

  /**
   * Get the password.
   * 
   * @return String
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Set the password.
   * 
   * @param password
   */
  public void setPassword( String password )
  {
    this.password = password;
  }

  public String getLoginToken()
  {
    return loginToken;
  }

  public void setLoginToken( String loginToken )
  {
    this.loginToken = loginToken;
  }

  /**
   * Get the secretAnswer.
   * 
   * @return String
   */
  public String getSecretAnswer()
  {
    return secretAnswerDecrypted;
  }

  //
  // This is added to get rid of warning related to not using the hidden secret answer
  //

  /* package */String getHiddenSecretAnswer()
  {
    return secretAnswer;
  }

  /**
   * Set the secretAnswer.
   * 
   * @param secretAnswer
   */
  public void setSecretAnswer( String secretAnswer )
  {
    this.secretAnswer = secretAnswer;
    this.secretAnswerDecrypted = secretAnswer;
  }

  /**
   * get Secret Question Type
   * 
   * @return SecretQuestionType
   */
  public SecretQuestionType getSecretQuestionType()
  {
    return secretQuestionType;
  }

  /**
   * set Secret Question Type
   * 
   * @param secretQuestionType
   */
  public void setSecretQuestionType( SecretQuestionType secretQuestionType )
  {
    this.secretQuestionType = secretQuestionType;
  }

  /**
   * Get the userName.
   * 
   * @return String
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * Set the userName.
   * 
   * @param userName
   */
  public void setUserName( String userName )
  {
    this.userName = userName.toUpperCase();
  }

  /**
   * @return value of loginFailuresCount property
   */
  public Integer getLoginFailuresCount()
  {
    return loginFailuresCount;
  }

  /**
   * @param loginFailuresCount value for loginFailuresCount property
   */
  public void setLoginFailuresCount( Integer loginFailuresCount )
  {
    this.loginFailuresCount = loginFailuresCount;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  @Override
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "User [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "{userName=" ).append( this.getUserName() ).append( "}, " );
    buf.append( "{lastName=" ).append( this.getLastName() ).append( "}, " );
    buf.append( "{firstName=" ).append( this.getFirstName() ).append( "}, " );
    buf.append( "{middleName=" ).append( this.getMiddleName() ).append( "}, " );
    buf.append( "{title=" ).append( this.getTitleType() ).append( "}, " );
    buf.append( "{suffix=" ).append( this.getSuffixType() ).append( "}, " );
    buf.append( "{ssn=" ).append( this.getSsn() ).append( "}, " );
    buf.append( "{birthDate=" ).append( this.getBirthDate() ).append( "}, " );
    buf.append( "{gender=" ).append( this.getGenderType() ).append( "}, " );
    buf.append( "{masterUserId=" ).append( this.getMasterUserId() ).append( "}, " );
    buf.append( "{password=" ).append( this.getPassword() ).append( "}, " );
    buf.append( "{isActive=" ).append( this.getActive() ).append( "}, " );
    buf.append( "{isProfileSetupDone=" ).append( this.getProfileSetupDone() ).append( "}, " );
    buf.append( "{isWelcomeEmailSent=" ).append( this.isWelcomeEmailSent() ).append( "}, " );
    buf.append( "{raWelcomeEmailSent=" ).append( this.getRaWelcomeEmailSent() ).append( "}, " );
    buf.append( "{roles=" ).append( this.getUserRoles() ).append( "}" );
    buf.append( "{secretQuestionType=" ).append( this.getSecretQuestionType() ).append( "}" );
    buf.append( "{secretAnswer=" ).append( this.getSecretAnswer() ).append( "}" );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  @Override
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof User ) )
    {
      return false;
    }

    final User user = (User)o;

    if ( getUserName() != null ? !getUserName().equals( user.getUserName() ) : user.getUserName() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  @Override
  public int hashCode()
  {
    return getUserName() != null ? getUserName().hashCode() : 0;
  }

  /**
   * Get the Last Reset date in millis.
   * 
   * @return long Last Date the password was reset in Millis
   */
  public Date getLastResetDate()
  {
    return lastResetDate;
  }

  /**
   * set the last reset date in millis
   * 
   * @param lastResetDate
   */
  public void setLastResetDate( Date lastResetDate )
  {
    this.lastResetDate = lastResetDate;
  }

  @JsonIgnore
  public Boolean getActive()
  {
    return active;
  }

  public boolean getProfileSetupDone()
  {
    return profileSetupDone;
  }

  public void setProfileSetupDone( boolean profileSetupDone )
  {
    this.profileSetupDone = profileSetupDone;
  }

  /**
   * isActive returns the Active flag
   * 
   * @return ative
   */
  public Boolean isActive()
  {
    return active;
  }

  /**
   * isProfileSetupDone returns the profileSetupDone flag
   * 
   * @return profileSetupDone
   */
  public boolean isProfileSetupDone()
  {
    return profileSetupDone;
  }

  public void setActive( Boolean active )
  {
    this.active = active;
  }

  /**
   * Returns true if the user is active, false if the user is inactive, and null is the active
   * status is not set. Subclasses override this method if they use a mechanism other than the field
   * User.active to track their active status.
   * 
   * @return true if the user is active, false if the user is inactive, and null is the active
   *         status is not set.
   */
  public Boolean isActiveClassAware()
  {
    return isActive();
  }

  @JsonIgnore
  public Boolean getWelcomeEmailSent()
  {
    return welcomeEmailSent;
  }

  public Boolean isWelcomeEmailSent()
  {
    return welcomeEmailSent;
  }

  public void setWelcomeEmailSent( Boolean welcomeEmailSent )
  {
    this.welcomeEmailSent = welcomeEmailSent;
  }

  @JsonIgnore
  public Boolean getAcceptedCmsTerms()
  {
    return acceptedCmsTerms;
  }

  public Boolean isAcceptedCmsTerms()
  {
    return acceptedCmsTerms;
  }

  public void setAcceptedCmsTerms( Boolean acceptedCmsTerms )
  {
    this.acceptedCmsTerms = acceptedCmsTerms;
  }

  /**
   * @return value of userEmailAddresses property
   */
  public Set<UserEmailAddress> getUserEmailAddresses()
  {
    return userEmailAddresses;
  }

  /**
   * @param userEmailAddresses value for userEmailAddresses property
   */
  public void setUserEmailAddresses( Set<UserEmailAddress> userEmailAddresses )
  {
    this.userEmailAddresses = userEmailAddresses;
  }

  /**
   * Add a UserEmailAddress to userEmailAddresses
   * 
   * @param userEmailAddress
   */
  public void addUserEmailAddress( UserEmailAddress userEmailAddress )
  {
    userEmailAddress.setUser( this );
    this.userEmailAddresses.add( userEmailAddress );
  }

  /**
   * Add a UserPhone to userPhones
   * 
   * @param userPhone
   */
  public void addUserPhone( UserPhone userPhone )
  {
    userPhone.setUser( this );
    this.userPhones.add( userPhone );
  }

  /**
   * Add a UserAddress to userAddresses
   * 
   * @param userAddress
   */
  public void addUserAddress( UserAddress userAddress )
  {
    userAddress.setUser( this );
    this.userAddresses.add( userAddress );
  }

  /**
   * Add a UserCharacteristic to userCharacteristics
   * 
   * @param userCharacteristic
   */
  public void addUserCharacteristic( UserCharacteristic userCharacteristic )
  {
    userCharacteristic.setUser( this );
    this.userCharacteristics.add( userCharacteristic );
  }

  public Set<UserPhone> getUserPhones()
  {
    return userPhones;
  }

  public void setUserPhones( Set<UserPhone> userPhones )
  {
    this.userPhones = userPhones;
  }

  /**
   * @return value of userAddresses property
   */
  public Set getUserAddresses()
  {
    return userAddresses;
  }

  /**
   * @param userAddresses value for userAddresses property
   */
  public void setUserAddresses( Set userAddresses )
  {
    this.userAddresses = userAddresses;
  }

  /**
   * Gets the primary UserAddress record
   * 
   * @return UserAddress
   */

  public UserAddress getPrimaryAddress()
  {
    UserAddress primaryUserAddress = null;
    if ( userAddresses != null )
    {
      Iterator addrIter = userAddresses.iterator();
      while ( addrIter.hasNext() )
      {
        UserAddress savedUserAddress = (UserAddress)addrIter.next();
        if ( savedUserAddress.isPrimary() )
        {
          primaryUserAddress = savedUserAddress;
        }
      } // while
    } // if userAddresses != null

    return primaryUserAddress;
  } // end getPrimaryAddress

  public String getPrimaryCountryName()
  {
    String countryName = "";
    UserAddress address = getPrimaryAddress();
    if ( null != address )
    {
      countryName = address.getAddress().getCountry().getI18nCountryName();
    }
    return countryName;
  }

  public String getPrimaryCountryCode()
  {
    String countryCode = "";
    UserAddress address = getPrimaryAddress();
    if ( null != address )
    {
      countryCode = address.getAddress().getCountry().getCountryCode();
    }
    return countryCode;
  }

  /**
   * Gets the primary UserPhone record
   * 
   * @return UserPhone
   */
  public UserPhone getPrimaryPhone()
  {
    UserPhone primaryUserPhone = null;
    if ( userPhones != null )
    {
      Iterator phoneIter = userPhones.iterator();
      while ( phoneIter.hasNext() )
      {
        UserPhone savedUserPhone = (UserPhone)phoneIter.next();
        if ( savedUserPhone.isPrimary() )
        {
          primaryUserPhone = savedUserPhone;
        }
      } // while
    } // if userPhones != null

    return primaryUserPhone;
  } // getPrimaryPhone

  /**
   * Gets the primary UserEmailAddress record
   * 
   * @return UserEmailAddress
   */
  public UserEmailAddress getPrimaryEmailAddress()
  {
    UserEmailAddress primaryUserEmailAddress = null;
    if ( userEmailAddresses != null )
    {
      Iterator emailIter = userEmailAddresses.iterator();
      while ( emailIter.hasNext() )
      {
        UserEmailAddress savedUserEmailAddress = (UserEmailAddress)emailIter.next();
        if ( savedUserEmailAddress.isPrimary() )
        {
          primaryUserEmailAddress = savedUserEmailAddress;
        }
      } // while
    } // if userEmailAddresses != null

    return primaryUserEmailAddress;
  } // getPrimaryPhone

  /**
   * Gets the primary UserNode record
   * 
   * @return UserNode
   */
  public UserNode getPrimaryUserNode()
  {
    UserNode userNode = null;
    if ( userNodes != null )
    {
      Iterator nodeIter = userNodes.iterator();
      while ( nodeIter.hasNext() )
      {
        UserNode savedUserNode = (UserNode)nodeIter.next();
        if ( savedUserNode.getIsPrimary() )
        {
          userNode = savedUserNode;
        }
      } // while
    } // if userNodes != null

    return userNode;
  } // end getPrimaryUserNode

  /**
   * @return value of userCharacteristics property
   */
  public Set<UserCharacteristic> getUserCharacteristics()
  {
    return userCharacteristics;
  }

  /**
   * @param userCharacteristics value for userCharacteristics property
   */
  public void setUserCharacteristics( Set<UserCharacteristic> userCharacteristics )
  {
    this.userCharacteristics = userCharacteristics;
  }

  /**
   * Get a set of UserNode
   * 
   * @return Set of UserNodes
   */
  public Set<UserNode> getUserNodes()
  {
    return userNodes;
  }

  /**
   * Get a list of active UserNodes' nodes
   * 
   */
  public List<Node> getUserNodesAsNodes()
  {
    List<Node> nodes = new ArrayList<Node>();

    for ( Iterator<UserNode> iter = userNodes.iterator(); iter.hasNext(); )
    {
      UserNode userNode = iter.next();
      if ( ( userNode.isActive() == null || userNode.isActive().booleanValue() ) && !userNode.getNode().isDeleted() )
      {
        nodes.add( userNode.getNode() );
      }
    }

    return nodes;
  }

  public long getUserNodesCount()
  {
    return userNodes.size();
  }

  /**
   * Return the user's userNodes where the node is within the primary hierarchy.
   */
  public Set<UserNode> getUserNodesWithinPrimaryHierarchy()
  {
    // TODO: We want to avoid calling the service layer from a domain object.
    // However, having this method is convienient, espically when called from a jsp context.
    // As an alternative source for the primary hierarchy, the Primary Hierarchy could
    // be something we cache as a resource and access via the ResourceManager rather than accessed
    // via the HierarchyService
    Hierarchy primaryHierarchy = ( (HierarchyService)BeanLocator.getBean( HierarchyService.BEAN_NAME ) ).getPrimaryHierarchy();

    Set<UserNode> userNodesWithinHierarchy = new LinkedHashSet<UserNode>();

    for ( Iterator iter = userNodes.iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getNode().getHierarchy().equals( primaryHierarchy ) )
      {
        userNodesWithinHierarchy.add( userNode );
      }
    }

    return userNodesWithinHierarchy;
  }

  /**
   * Get a list of UserNodes' nodes where the node is within the primary hierarchy.
   * 
   */
  public List<Node> getUserNodesWithinPrimaryHierarchyAsNodes()
  {
    List<Node> nodes = new ArrayList<Node>();

    for ( Iterator iter = getUserNodesWithinPrimaryHierarchy().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      nodes.add( userNode.getNode() );
    }

    return nodes;
  }

  public long getUserNodesWithinPrimaryHierarchyCount()
  {
    return getUserNodesWithinPrimaryHierarchy().size();
  }

  /**
   * Add a node to UserNodes
   * 
   * @param userNode
   */
  public void addUserNode( UserNode userNode )
  {
    userNode.setUser( this );
    this.userNodes.add( userNode );
  }

  public void removeUserNode( UserNode userNode )
  {
    userNode.getNode().getUserNodes().remove( userNode );
    userNodes.remove( userNode );
  }

  /**
   * Setter for UserNodes
   * 
   * @param userNodes
   */
  public void setUserNodes( Set<UserNode> userNodes )
  {
    this.userNodes = userNodes;
  }

  public Date getBirthDate()
  {
    return birthDate;
  }

  public void setBirthDate( Date birthDate )
  {
    this.birthDate = birthDate;
  }

  public String getMiddleName()
  {
    return middleName;
  }

  public void setMiddleName( String middleName )
  {
    this.middleName = middleName;
  }

  public String getSsn()
  {
    return ssnDecrypted;
  }

  public void setSsn( String ssn )
  {
    this.ssn = ssn;
    this.ssnDecrypted = ssn;
  }

  //
  // This is added to get rid of warning related to not using the hidden secret answer
  //

  /* package */String getHiddenSsn()
  {
    return ssn;
  }

  public GenderType getGenderType()
  {
    return genderType;
  }

  public void setGenderType( GenderType genderType )
  {
    this.genderType = genderType;
  }

  public SuffixType getSuffixType()
  {
    return suffixType;
  }

  public void setSuffixType( SuffixType suffixType )
  {
    this.suffixType = suffixType;
  }

  public TitleType getTitleType()
  {
    return titleType;
  }

  public void setTitleType( TitleType titleType )
  {
    this.titleType = titleType;
  }

  public LanguageType getLanguageType()
  {
    return languageType;
  }

  public void setLanguageType( LanguageType languageType )
  {
    this.languageType = languageType;
  }

  /**
   * Get a UserNode given a nodeId to search by.
   * 
   * @param nodeId
   * @return <code>UserNode</code>
   */
  public UserNode getUserNodeByNodeId( Long nodeId )
  {
    UserNode userNode = null;
    UserNode foundUserNode = null;
    Iterator userNodeIterator = userNodes.iterator();

    while ( userNodeIterator.hasNext() )
    {
      userNode = (UserNode)userNodeIterator.next();
      if ( userNode.getNode().getId().equals( nodeId ) )
      {
        foundUserNode = userNode;
        break;
      }

    }
    return foundUserNode;
  }

  public UserType getUserType()
  {
    return userType;
  }

  public void setUserType( UserType userType )
  {
    this.userType = userType;
  }

  public Date getEnrollmentDate()
  {
    return enrollmentDate;
  }

  public void setEnrollmentDate( Date enrollmentDate )
  {
    this.enrollmentDate = enrollmentDate;
  }

  public ParticipantEnrollmentSource getEnrollmentSource()
  {
    return enrollmentSource;
  }

  public void setEnrollmentSource( ParticipantEnrollmentSource enrollmentSource )
  {
    this.enrollmentSource = enrollmentSource;
  }

  public Long getMasterUserId()
  {
    return masterUserId;
  }

  public void setMasterUserId( Long masterUserId )
  {
    this.masterUserId = masterUserId;
  }

  public boolean isAcceptTermsOnTextMessages()
  {
    return acceptTermsOnTextMessages;
  }

  public void setAcceptTermsOnTextMessages( boolean acceptTermsOnTextMessages )
  {
    this.acceptTermsOnTextMessages = acceptTermsOnTextMessages;
  }

  /**
   * Returns true if this user is a participant; returns false otherwise.
   * 
   * @return true if this user is a participant; false otherwise.
   */
  public boolean isParticipant()
  {
    return getUserType().isParticipant();
  }

  /**
   * Returns true if this user_type = 'bi'; returns false otherwise.
   * 
   * @return true if this user_type = 'bi'; returns false otherwise.
   */
  public boolean isAdmin()
  {
    return getUserType().isAdmin();
  }

  /**
   * Returns true if this user is the manager of the specified node; returns false otherwise.
   * 
   * @param node a node.
   * @return true if this user is the manager of the specified node; false otherwise.
   */
  public boolean isManagerOf( Node node )
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        HierarchyRoleType roleType = userNode.getHierarchyRoleType();
        if ( userNode.getNode().equals( node ) && roleType.isManager() )
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns true if this user is a manager. This means that they are an Owner or a Manager of ANY
   * node
   * 
   * @return boolean isManager
   */
  public boolean isManager()
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        HierarchyRoleType roleType = userNode.getHierarchyRoleType();
        if ( roleType != null && ( roleType.isOwner() || roleType.isManager() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns true if this user is an Owner of any node
   * 
   * @return boolean isOwner
   */
  public boolean isOwner()
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        HierarchyRoleType roleType = userNode.getHierarchyRoleType();
        if ( roleType != null && roleType.isOwner() )
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns true if this user is a member of the specified node; returns false otherwise.
   * 
   * @param node a node.
   * @return true if this user is a member of the specified node; false otherwise.
   */
  public boolean isMemberOf( Node node )
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        HierarchyRoleType roleType = userNode.getHierarchyRoleType();
        if ( null != userNode && userNode.getNode().equals( node ) && roleType.isMember() )
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns true if this user is the owner of the specified node; returns false otherwise.
   * 
   * @param node a node.
   * @return true if this user is the owner of the specified node; false otherwise.
   */
  public boolean isOwnerOf( Node node )
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        HierarchyRoleType roleType = userNode.getHierarchyRoleType();
        if ( userNode.getNode().equals( node ) && roleType.isOwner() )
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns true if this user is in the specified node; returns false otherwise.
   * 
   * @param node a node.
   * @return true if this user is in the specified node; false otherwise.
   */
  public boolean isInNode( Node node )
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        if ( userNode.getNode().equals( node ) )
        {
          return true;
        }
      } // while
    } // if

    return false;
  }

  /**
   * Returns true if this user is in the specified nodeType; returns false otherwise.
   * 
   * @param nodeType a nodeType.
   * @return true if this user is in the specified node; false otherwise.
   */
  public boolean isInNodeType( NodeType nodeType )
  {
    if ( userNodes != null )
    {
      Iterator iter = userNodes.iterator();
      while ( iter.hasNext() )
      {
        UserNode userNode = (UserNode)iter.next();
        if ( userNode.getNode().getNodeType().equals( nodeType ) )
        {
          return true;
        }
      } // while
    } // if

    return false;
  }

  /**
   * If this user has a fax number, then this method returns it; otherwise, this method returns
   * null.
   * 
   * @return this user's fax number, if the user has a fax number; return null otherwise.
   */
  public UserPhone getFaxNumber()
  {
    UserPhone faxNumber = null;

    Iterator iter = userPhones.iterator();
    while ( iter.hasNext() )
    {
      UserPhone userPhone = (UserPhone)iter.next();
      if ( userPhone.getPhoneType().getCode().equals( PhoneType.FAX ) )
      {
        // Return the first fax number found.
        faxNumber = userPhone;
        break;
      }
    }

    return faxNumber;
  }

  /**
   * Adds a userRole directly to the collection.
   * 
   * @param userRole
   */
  public void addUserRole( UserRole userRole )
  {
    this.userRoles.add( userRole );
  }

  /**
   * Adds a userAcl directly to the collection.
   * 
   * @param userAcl
   */
  public void addUserAcl( UserAcl userAcl )
  {
    userAcl.setUser( this );
    this.userAcls.add( userAcl );
  }

  public String getNameLFMNoComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( lastName != null )
    {
      fullName.append( lastName ).append( " " );
    }
    if ( firstName != null )
    {
      fullName.append( firstName );
    }
    if ( middleName != null )
    {
      fullName.append( " " ).append( middleName ).append( " " );
    }

    return fullName.toString();
  }

  public String getNameLFMWithComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( lastName != null )
    {
      fullName.append( lastName ).append( ", " );
    }
    if ( firstName != null )
    {
      fullName.append( firstName );
    }
    if ( middleName != null )
    {
      fullName.append( " " ).append( middleName ).append( " " );
    }

    return fullName.toString();
  }

  public String getNameFLMWithComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( firstName != null )
    {
      fullName.append( firstName ).append( ", " );
    }
    if ( lastName != null )
    {
      fullName.append( lastName );
    }
    if ( middleName != null )
    {
      fullName.append( " " ).append( middleName ).append( " " );
    }
    return fullName.toString();
  }

  public String getNameFLNoComma()
  {
    StringBuffer fullName = new StringBuffer();
    if ( firstName != null )
    {
      fullName.append( firstName ).append( " " );
    }
    if ( lastName != null )
    {
      fullName.append( lastName );
    }

    return fullName.toString();
  }

  public List getActiveOwnerNodes()
  {
    List matchingNodes = new ArrayList();
    for ( Iterator iter = getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getHierarchyRoleType().isActive() && userNode.getHierarchyRoleType().isOwner() )
      {
        matchingNodes.add( userNode.getNode() );
      }
    }
    return matchingNodes;
  }

  @SuppressWarnings( "unchecked" )
  public List getActiveManagerNodes()
  {
    List matchingNodes = new ArrayList();
    for ( Iterator iter = getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getHierarchyRoleType().isActive() && ( userNode.getHierarchyRoleType().isOwner() || userNode.getHierarchyRoleType().isManager() ) )
      {
        matchingNodes.add( userNode.getNode() );
      }
    }
    Collections.sort( matchingNodes, new Comparator<Node>()
    {
      @Override
      public int compare( Node o1, Node o2 )
      {
        return o1.getName().compareTo( o2.getName() );
      }
    } );
    return matchingNodes;
  }

  public List getActiveOwnerNodeIds()
  {
    List matchingNodes = new ArrayList();
    for ( Iterator iter = getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getHierarchyRoleType().isActive() && userNode.getHierarchyRoleType().isOwner() )
      {
        matchingNodes.add( userNode.getNode().getId() );
      }
    }
    return matchingNodes;
  }

  /**
   * Get all of this user's node where this user's role for the node matches hierarchyRoleType.
   * Assumes usernodes are hydrated.
   * 
   * @param hierarchyRoleType
   * @return List
   */
  public List<Node> getNodes( HierarchyRoleType hierarchyRoleType )
  {
    List<Node> matchingNodes = new ArrayList<Node>();

    for ( Iterator<UserNode> iter = getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = iter.next();
      if ( userNode.getHierarchyRoleType().equals( hierarchyRoleType ) )
      {
        matchingNodes.add( userNode.getNode() );
      }
    }

    return matchingNodes;
  }

  public List getNodes( HierarchyRoleType[] hierarchyRoleTypes )
  {
    List matchingNodes = new ArrayList();

    if ( null != hierarchyRoleTypes )
    {
      for ( int i = 0; i < hierarchyRoleTypes.length; ++i )
      {
        matchingNodes.add( getNodes( hierarchyRoleTypes[i] ) );
      }
    }

    return matchingNodes;
  }

  public boolean isAccountLocked()
  {
    return accountLocked;
  }

  public void setAccountLocked( boolean accountLocked )
  {
    this.accountLocked = accountLocked;
  }

  public Date getLockTimeoutExpireDate()
  {
    return lockTimeoutExpireDate;
  }

  public void setLockTimeoutExpireDate( Date lockTimeoutExpireDate )
  {
    this.lockTimeoutExpireDate = lockTimeoutExpireDate;
  }

  /**
   * Returns the roles assigned to this user.
   *
   * @return the roles assigned to this user, as a <code>List</code> of {@link Role} objects.
   */
  public List getRoles()
  {
    List roles = new ArrayList();

    for ( Iterator iter = userRoles.iterator(); iter.hasNext(); )
    {
      UserRole userRole = (UserRole)iter.next();
      roles.add( userRole.getRole() );
    }

    return roles;
  }

  public Set<UserPasswordHistory> getUserPasswords()
  {
    return userPasswords;
  }

  public void setUserPasswords( Set<UserPasswordHistory> userPasswords )
  {
    this.userPasswords = userPasswords;
  }

  public void addUserPassword( UserPasswordHistory userPasswordHistory )
  {
    userPasswordHistory.setUser( this );
    this.userPasswords.add( userPasswordHistory );
  }

  public Boolean getOneTimePassword()
  {
    return oneTimePassword;
  }

  public Boolean isOneTimePassword()
  {
    return oneTimePassword;
  }

  public Date getOneTimePasswordDate()
  {
    return oneTimePasswordDate;
  }

  public void setOneTimePassword( Boolean oneTimePassword )
  {
    this.oneTimePassword = oneTimePassword;
  }

  public void setOneTimePasswordDate( Date oneTimePasswordDate )
  {
    this.oneTimePasswordDate = oneTimePasswordDate;
  }

  public Boolean getRaWelcomeEmailSent()
  {
    return raWelcomeEmailSent;
  }

  public void setRaWelcomeEmailSent( Boolean raWelcomeEmailSent )
  {
    this.raWelcomeEmailSent = raWelcomeEmailSent;
  }

  public UUID getRosterUserId()
  {
    return rosterUserId;
  }

  public void setRosterUserId( UUID rosterUserId )
  {
    this.rosterUserId = rosterUserId;
  }

  /**
 * @return the svcVideoURL
 */
public String getSvcVideoURL() {
	return svcVideoURL;
}

/**
 * @param svcVideoURL the svcVideoURL to set
 */
public void setSvcVideoURL(String svcVideoURL) {
	this.svcVideoURL = svcVideoURL;
}

/**
   * Gets the UserEmailAddress record by email type
   * 
   * @return UserEmailAddress
   */
  public UserEmailAddress getEmailAddressByType( EmailAddressType emailType )
  {
    Predicate<UserEmailAddress> predicate = c -> c.getEmailType().getCode().equals( emailType.getCode() );
    UserEmailAddress userEmailAddress = userEmailAddresses.stream().filter( predicate ).findFirst().orElse( null );
    return userEmailAddress;
  }

  /**
   * Gets the UserPhone record by phone type
   * 
   * @return UserPhone
   */
  public UserPhone getPhoneByType( PhoneType phoneType )
  {
    Predicate<UserPhone> predicate = c -> c.getPhoneType().getCode().equals( phoneType.getCode() );
    UserPhone userPhone = userPhones.stream().filter( predicate ).findFirst().orElse( null );
    return userPhone;
  }

  public boolean isActivationComplete()
  {
    return null != this.getPassword();
  }
}

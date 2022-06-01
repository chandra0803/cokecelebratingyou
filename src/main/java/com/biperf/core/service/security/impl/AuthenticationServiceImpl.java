/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/security/impl/AuthenticationServiceImpl.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.acegi.authentication.dao.UserCredential;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.CountrySupplier;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserRole;
import com.biperf.core.domain.user.UserTypeEnum;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.CmsAuthorizationUtil;
import com.biperf.core.security.acl.AclEntry;
import com.biperf.core.security.acl.GrantedAuthorityAclEntryImpl;
import com.biperf.core.security.credentials.ClientSeamlessLogonCredentials;
import com.biperf.core.security.credentials.LoginAsCredentials;
import com.biperf.core.security.credentials.QuestionAnswerCredentials;
import com.biperf.core.security.credentials.StandardLoginIdSeamlessLogonCredentials;
import com.biperf.core.security.credentials.StandardSSOIdSeamlessLogonCredentials;
import com.biperf.core.security.exception.AccountHardLockoutException;
import com.biperf.core.security.exception.AccountLockoutException;
import com.biperf.core.security.exception.PaxLockoutException;
import com.biperf.core.security.exception.WarningAlertFailedLoginException;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.LoginToken;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AccountExpirationStrategy;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.UserLockoutStrategy;
import com.biperf.core.ui.user.LockoutInfo;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

/**
 * AuthenticationServiceImpl.
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
 * <td>kumars</td>
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthenticationServiceImpl implements AuthenticationService
{
  private static final Log log = LogFactory.getLog( AuthenticationServiceImpl.class );
  private int lockoutWarningThreshold;
  private UserDAO userDAO = null;
  private UserLockoutStrategy userLockoutStrategy = null;
  private AccountExpirationStrategy accountExpirationStrategy = null;
  private PasswordPolicyStrategy passwordPolicyStrategy = null;
  private AudienceService audienceService = null;
  private ParticipantService participantService = null;
  private UserService userService = null;
  private SystemVariableService systemVariableService = null;
  private AwardBanQServiceFactory awardBanQServiceFactory = null;
  private ProxyService proxyService = null;
  private ShoppingService shoppingService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthenticationService#authenticate(java.lang.String,
   *      java.lang.Object) Authenticate the given username and password. Returns a populated User
   *      object if authentication information is valid. Otherwise, throws certain Authenication
   *      Exceptions
   * @param username
   * @param credentials
   * @return AuthenticatedUser
   * @throws LoginException
   */
  public AuthenticatedUser authenticate( String username, Object credentials ) throws LoginException
  {
    User user = null;

    boolean isSuccessfulAuthentication = false;

    try
    {
      user = userDAO.getUserByUserName( username );

      if ( user == null )
      {
        throw new FailedLoginException( "Username not found." );
      }
      if ( isUserLockedOut( user ) )
      {
        throw buildAccountLockoutException( user );
      }
      if ( !isCredentialsValid( user, credentials ) )
      {
        // check to determine how many attempts are left. The count is updated in the
        // finally block so we need to assume 1 less here as the count is updated
        // after this code is executed
        int attemptsAllowed = userLockoutStrategy.getAllowableFailedLoginAttempts();
        if ( attemptsAllowed > -1 ) // only throw this exception is there's a max attempt allowed
        {
          int actualFailedAttempts = ( null != user.getLoginFailuresCount() ) ? user.getLoginFailuresCount() + 1 : 1;
          int remaining = attemptsAllowed - actualFailedAttempts;
          if ( remaining <= lockoutWarningThreshold && remaining > 0 )
          {
            throw new WarningAlertFailedLoginException( remaining, "Credentials invalid, at most 2 attempts left" );
          }
          else if ( remaining == 0 )
          {
            throw buildAccountLockoutException( user );
          }
        }
        throw new FailedLoginException( "Credentials invalid." );
      }
      if ( accountExpirationStrategy.isAccountExpired( user ) )
      {
        throw new AccountExpiredException( "Account has Expired." );
      }
      if ( isPaxInactiveNonUSAndNoCataLogURL( user ) )
      {
        throw new NonActiveNonUSNoCataLogURLException( "" );
      }
      if ( isPaxInactiveWithNoAwardBanQPoints( user ) )
      {
        throw new PaxLockoutException();
      }
      isSuccessfulAuthentication = true;

    }
    catch( LoginException ex )
    {
      isSuccessfulAuthentication = false;
      throw ex;
    }
    finally
    {
      userLockoutStrategy.handleAuthentication( user, isSuccessfulAuthentication );
      if ( user != null )
      {
        userDAO.saveUser( user );
      }
    }

    return buildAuthenticatedUser( user, credentials );
  }

  /** Check which type of account lock - hard or soft - to provide the correct message */
  private AccountLockoutException buildAccountLockoutException( User user )
  {
    LockoutInfo lockoutInfo = getUserLockOutInfo( user );
    if ( lockoutInfo.isHardLocked() )
    {
      return new AccountHardLockoutException();
    }
    return new AccountLockoutException();
  }

  public AuthenticatedUser authenticate( LoginToken token, HttpServletRequest request )
  {
    AuthenticatedUser authUser = null;
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // if(userDAO.isLoginTokenValid(token.getUsername(), token.getToken()))
    User user = userDAO.getUserByUserName( token.getUsername() );
    if ( userDAO.isLoginTokenValid( user, token.getToken() ) )
    {
      // User user = userDAO.getUserByUserName(token.getUsername());
      if ( !StringUtils.isEmpty( user.getLoginToken() ) && user.getLoginToken().startsWith( "{MD5}" ) )
      {
        user.setLoginToken( token.getToken() );
        userDAO.saveUser( user );
      }
      /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      if ( user.getActive() )
      {
        authUser = loginAs( token.getUsername() );
        request.getSession().setAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT, SecurityContextHolder.getContext() );
      }

      // Update the login count since technically they are logging in again.
      userService.saveLoginInfo( user.getId() );
    }

    return authUser;
  }

  public AuthenticatedUser loginAs( String userName )
  {
    User user = userDAO.getUserByUserName( userName );
    UserCredential userCredentials = new UserCredential( user.getId().longValue(), user.getUserName(), user.getPassword() );

    AuthenticatedUser newAuthenticatedUser = buildAuthenticatedUser( user, userCredentials );

    Object principal = newAuthenticatedUser;
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( principal, userCredentials, newAuthenticatedUser.getAuthorities() );

    authentication.setDetails( newAuthenticatedUser );
    // With new ACEGI upgrade, the constructor already sets authenticated to true..
    // authentication.setAuthenticated( true );

    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication( authentication );

    UserManager.setUser( newAuthenticatedUser );

    return newAuthenticatedUser;
  }

  /**
   * Validate the credentials.
   * 
   * @param user
   * @param credentials
   * @return boolean credentialsValid
   */
  private boolean isCredentialsValid( User user, Object credentials )
  {
    // TODO Possibly use a Strategy so this can be more configurable when/if other forms of
    // authentication are to be used.

    boolean credentialsValid = false;

    // If the credentials are a String, assume username/password authentication
    if ( credentials instanceof String )
    {
      /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      /*
       * Will keep this below code few months until all the users have been updated with SHA256
       * encryption of their passwords. After all the users are converted, we can remove/edit MD5
       * hash related code
       */
      // credentialsValid = userDAO.isPasswordValid( user.getUserName(), credentials.toString() );
      credentialsValid = userDAO.isPasswordValid( user, credentials.toString() );

      if ( credentialsValid )
      {

        if ( user.getPassword().startsWith( "{MD5}" ) )
        {
          user.setPassword( credentials.toString() );
          userDAO.saveUser( user );
        }
      }
    }
    else if ( credentials instanceof QuestionAnswerCredentials )
    {
      QuestionAnswerCredentials qaCredentials = (QuestionAnswerCredentials)credentials;
      if ( user.getSecretAnswer() == null || !user.getSecretAnswer().equalsIgnoreCase( qaCredentials.getSecretAnswer() ) || user.getSecretQuestionType().getCode() == null
          || !user.getSecretQuestionType().getCode().equalsIgnoreCase( qaCredentials.getSecretQuestion() ) )
      {
        credentialsValid = false;
      }
      else
      {
        credentialsValid = true;
      }
    }
    else if ( credentials instanceof LoginAsCredentials )
    {
      // Automatically mark as valid. These credentials are only used when we are an admin who has
      // done
      // a "switch user" to act as another user/pax.
      credentialsValid = true;
    }

    else if ( credentials instanceof ClientSeamlessLogonCredentials )
    {
      // These credentials are used by client and get validated in isValid() method
      credentialsValid = ( (ClientSeamlessLogonCredentials)credentials ).isValid();
    }
    else if ( credentials instanceof StandardLoginIdSeamlessLogonCredentials )
    {
      // These credentials are used by Standard Login ID SSO and get validated in isValid() method
      credentialsValid = ( (StandardLoginIdSeamlessLogonCredentials)credentials ).isValid();
    }
    else if ( credentials instanceof StandardSSOIdSeamlessLogonCredentials )
    {
      // These credentials are used by Standard SSO ID SSO and get validated in isValid() method
      credentialsValid = ( (StandardSSOIdSeamlessLogonCredentials)credentials ).isValid();
    }

    return credentialsValid;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthenticationService#isUserLockedOut(com.biperf.core.domain.user.User)
   * @param user
   * @return boolean isUserLockedOut
   */
  public boolean isUserLockedOut( User user )
  {
    return userLockoutStrategy.isUserLockedout( user );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthenticationService#isUserLockedOut(com.biperf.core.domain.user.User)
   * @param user
   * @return boolean isUserLockedOut
   */
  public LockoutInfo getUserLockOutInfo( User user )
  {
    return userLockoutStrategy.getUserLockOutInfo( user );
  }

  /**
   * This method is used by the Pax Close Out requirement If the participant status is Inactive, and
   * he/she has zero awardBanQ balance then he/she is to be locked out of the site and an error
   * message displayed at login If the client uses Terms & Conditions, the pax is defaulted to be
   * Inactive when the pax is fileloaded or created. In that case, the pax will have zero balance
   * but we can't shut him out of the site since he has not even been to it yet.
   * 
   * @param user
   * @return boolean isPaxInactiveWithNoAwardBanQPoints
   */
  public boolean isPaxInactiveWithNoAwardBanQPoints( User user )
  {
    if ( user instanceof Participant )
    {
      Participant pax = (Participant)user;

      if ( isPaxRealInactive( pax ) )
      {
        // Get awardBanQ Balance for the Pax
        Long balance = awardBanQServiceFactory.getAwardBanQService().getAccountBalanceForParticipantId( pax.getId() );
        // BugFix 18326 Balance could be Null also
        if ( balance == null || balance.longValue() <= 0 )
        {
          return true;
        }
      }
    }
    else
    {
      if ( !user.getActive() )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isPaxInactive( Participant pax )
  {
    if ( pax.getStatus() != null && pax.getStatus().getCode() != null && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  public boolean isPaxRealInactive( Participant pax )
  {
    if ( this.isTermsAndConditionsUsed() )
    {
      if ( isPaxInactive( pax ) && pax.getTerminationDate() != null )
      {
        return true;
      }
    }
    else
    {
      if ( isPaxInactive( pax ) && pax.getTermsAcceptance() != null && pax.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.NOTACCEPTED ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * This method is used by the Pax Close Out requirement If the participant status is Inactive, and
   * he/she has zero awardBanQ balance then he/she is to be locked out of the site and an error
   * message displayed at login If the client uses Terms & Conditions, the pax is defaulted to be
   * Inactive when the pax is fileloaded or created. In that case, the pax will have zero balance
   * but we can't shut him out of the site since he has not even been to it yet.
   * 
   * @param user
   * @return boolean isPaxInactiveNonUSAndNoCataLogURL
   */
  private boolean isPaxInactiveNonUSAndNoCataLogURL( User user )
  {
    if ( user instanceof Participant )
    {
      Participant pax = (Participant)user;

      if ( pax.getStatus() != null && pax.getStatus().getCode() != null && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
      {
        UserAddress userAddress = getPrimaryUserAddress( user );
        if ( userAddress == null )
        {
          return true;
        }
        if ( !userAddress.getAddress().getCountry().getCountryCode().equals( "us" ) )
        {
          // ----- CataLoge URL for INACTIVE participants who are non-USA-----
          if ( userAddress.getAddress().getCountry().getPrimarySupplier().getCatalogUrl() == null || userAddress.getAddress().getCountry().getPrimarySupplier().getCatalogUrl().equals( "" ) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 
   * @param userId
   * @return UserAddress getPrimaryUserAddress
   */
  private UserAddress getPrimaryUserAddress( User user )
  {
    Set userAddresses = user.getUserAddresses();
    for ( Iterator iterator = userAddresses.iterator(); iterator.hasNext(); )
    {
      UserAddress userAddress = (UserAddress)iterator.next();
      if ( userAddress.isPrimary() )
      {
        return userAddress;
      }
    }
    return null;
  }

  /**
   * Build the AuthenticatedUser object.
   * 
   * @param user
   * @param credentials
   * @return AuthenticatedUser
   */
  public AuthenticatedUser buildAuthenticatedUser( User user, Object credentials )
  {
    AuthenticatedUser authenticatedUser = new AuthenticatedUser();
    authenticatedUser.setUserId( user.getId() );
    authenticatedUser.setUsername( user.getUserName() );
    authenticatedUser.setPassword( user.getPassword() );
    authenticatedUser.setAccountNonExpired( true );
    authenticatedUser.setCredentialsNonExpired( true );
    authenticatedUser.setAccountNonLocked( true );
    authenticatedUser.setEnabled( true );
    authenticatedUser.setFirstName( user.getFirstName() );
    authenticatedUser.setMiddleName( user.getMiddleName() );
    authenticatedUser.setLastName( user.getLastName() );
    authenticatedUser.setProfileSetup( user.isProfileSetupDone() );
    UserNode userNode = user.getPrimaryUserNode();
    authenticatedUser.setPrimaryNodeId( null != userNode ? userNode.getNode().getId() : null );

    if ( user.isParticipant() )
    {
      authenticatedUser.setTimeZoneId( userDAO.getUserTimeZone( user.getId() ) );
    }
    else
    {
      authenticatedUser.setTimeZoneId( userDAO.getUserTimeZoneForAdmin() );
    }

    if ( credentials instanceof ClientSeamlessLogonCredentials || credentials instanceof StandardLoginIdSeamlessLogonCredentials || credentials instanceof StandardSSOIdSeamlessLogonCredentials )
    {
      authenticatedUser.setFromSSO( true );
    }
    else
    {
      authenticatedUser.setFromSSO( false );
    }

    // delegate Logic, check if user is a valid delegate
    // List<Proxy> delegates = new ArrayList<Proxy>();
    List<Participant> delegates = new ArrayList<Participant>();

    delegates = proxyService.getUsersByProxyUserId( user.getId() );
    if ( null != delegates && delegates.size() > 0 )
    {
      // authenticatedUser.setDelegate( true );
      authenticatedUser.setDelegateList( delegates );
    }

    if ( user.getLanguageType() != null )
    {
      // bug fix : 35473
      String languageCode = user.getLanguageType().getCode();
      int index = languageCode.indexOf( '_' );
      if ( index == -1 )
      {
        authenticatedUser.setLocale( new Locale( languageCode ) );
      }
      else
      {
        authenticatedUser.setLocale( new Locale( languageCode.substring( 0, index ), languageCode.substring( index + 1 ) ) );
      }
      // End of bug fix:35473
    }

    // Participant
    if ( user instanceof Participant )
    {
      Participant pax = (Participant)user;

      authenticatedUser.setHoneycombUserId( pax.getHoneycombUserId() );
      authenticatedUser.setRosterUserId( pax.getRosterUserId() );

      authenticatedUser.setGiftCodeOnlyPax( pax.isGiftCodeOnly() );
      if ( pax.getAwardBanqNumber() == null || pax.getAwardBanqNumber().trim().equals( "" ) )
      {
        authenticatedUser.setHasAwardbanqNbrAtLogin( false );
      }
      else
      {
        authenticatedUser.setHasAwardbanqNbrAtLogin( true );
      }

      // commented out to decouple shared services for home page
      // Set the pax's awardBanQ balance
      // authenticatedUser.setAwardBanQBalance( awardBanQServiceFactory.getAwardBanQService()
      // .getAccountBalanceForParticipantId( pax.getId() ) );

      // Set the pax's status
      if ( pax.getStatus() != null )
      {
        authenticatedUser.setPaxStatus( pax.getStatus().getCode() );
      }

      /*
       * start - to improve login speed ParticipantEmployer paxEmployer =
       * participantService.getCurrentParticipantEmployer( user .getId() ); if ( paxEmployer != null
       * ) { // Set the pax's termination date authenticatedUser.setPaxTerminationDate(
       * paxEmployer.getTerminationDate() ); }
       */

      Date currentTermDate = participantService.getCurrentParticipantEmployerTermDate( user.getId() );

      if ( currentTermDate != null )
      {
        authenticatedUser.setPaxTerminationDate( currentTermDate );
      }
      /* end - to improve login speed */

      // Set the pax's T&Cs acceptance details if T&Cs are used (based on sysvar)
      if ( isTermsAndConditionsUsed() )
      {
        if ( pax.getTermsAcceptance() != null )
        {
          authenticatedUser.setPaxTermsAcceptance( pax.getTermsAcceptance().getCode() );
        }
        authenticatedUser.setPaxTermsAcceptedDate( pax.getTermsAcceptedDate() );
        authenticatedUser.setUserIDAcceptedTerms( pax.getUserIDAcceptedTerms() );
      } // else these fields are null on the authenticatedUser

      authenticatedUser.setOptOutOfProgram( pax.getOptOutOfProgram() );
      authenticatedUser.setOptOutOfAward( pax.getOptOutAwards() );
      authenticatedUser.setRecoveryMethodsCollected( participantService.isParticipantRecoveryContactsAvailable( user.getId() ) );
    }

    // TODO: determine if we need to force admins to apply recovery methods
    if ( !user.isParticipant() )
    {
      authenticatedUser.setRecoveryMethodsCollected( true );
      // Changes related to WIP #61691 starts
      authenticatedUser.setRosterUserId( user.getRosterUserId() );
      // Changes related to WIP #61691 ends
    }

    Set audienceNames = new HashSet();
    if ( user instanceof Participant )
    {
      authenticatedUser.setUserType( UserTypeEnum.PARTICIPANT );

      audienceNames.add( CmsAuthorizationUtil.DEFAULT_AUDIENCE_NAME );
      audienceNames.add( CmsAuthorizationUtil.PARTICIPANT_AUDIENCE_NAME );
      Participant pax = (Participant)user;
      for ( Iterator iterator = audienceService.getAllParticipantAudiences( pax ).iterator(); iterator.hasNext(); )
      {
        Audience audience = (Audience)iterator.next();
        audienceNames.add( audience.getName() );
      }
    }
    else
    {
      authenticatedUser.setUserType( UserTypeEnum.USER );

      audienceNames.add( CmsAuthorizationUtil.DEFAULT_AUDIENCE_NAME );
      audienceNames.add( CmsAuthorizationUtil.USER_AUDIENCE_NAME );
    }

    authenticatedUser.setAudienceNames( audienceNames );

    // if password has expired, or if the record is flagged to force a password change,
    // allow successful authentication, but flag the authenticated user object
    if ( passwordPolicyStrategy.isPasswordExpired( user ) || credentials instanceof QuestionAnswerCredentials || user.isForcePasswordChange() || secretDetailsIncomplete( user ) )
    {
      log.debug( "setting credentials to expired" );
      authenticatedUser.setCredentialsNonExpired( false );
    }

    // Check the user type and add roles based on that.
    Set grantedAuthorities = new HashSet();

    GrantedAuthority grantedAuthorityDefault = buildGrantedAuthority( AuthorizationService.ROLE_CODE_AUTHENTICATED );
    grantedAuthorities.add( grantedAuthorityDefault );

    SimpleGrantedAuthority loginAsGrantedAuthority = buildGrantedAuthority( AuthorizationService.ROLE_CODE_LOGIN_AS );
    if ( credentials instanceof LoginAsCredentials )
    {
      GrantedAuthority grantedAuthority = loginAsGrantedAuthority;
      grantedAuthorities.add( grantedAuthority );
    }

    if ( user instanceof Participant )
    {
      GrantedAuthority grantedAuthority = buildGrantedAuthority( AuthorizationService.ROLE_CODE_PAX );
      grantedAuthorities.add( grantedAuthority );
    }
    else
    {
      GrantedAuthority grantedAuthority = buildGrantedAuthority( AuthorizationService.ROLE_CODE_USER );
      grantedAuthorities.add( grantedAuthority );

      if ( user.getUserType() != null )
      {
        if ( UserType.BI.equals( user.getUserType().getCode() ) )
        {
          grantedAuthority = buildGrantedAuthority( AuthorizationService.ROLE_CODE_BI_USER );
          grantedAuthorities.add( grantedAuthority );
        }
      }
    }

    if ( user.isManager() )
    {
      GrantedAuthority grantedAuthority = buildGrantedAuthority( AuthorizationService.ROLE_MANAGER );
      grantedAuthorities.add( grantedAuthority );
    }

    // Changes for Bugzilla bug #55298
    authenticatedUser.setManager( user.isManager() );
    authenticatedUser.setOwner( user.isOwner() );

    // Adding roles
    Set roles = user.getUserRoles();
    Iterator roleIterator = roles.iterator();
    while ( roleIterator.hasNext() )
    {
      UserRole userRole = (UserRole)roleIterator.next();
      grantedAuthorities.add( buildGrantedAuthority( userRole.getRole().getCode() ) );
    }

    // Adding acls
    Iterator aclIterator = user.getUserAcls().iterator();
    while ( aclIterator.hasNext() )
    {
      UserAcl userAcl = (UserAcl)aclIterator.next();
      grantedAuthorities.add( new GrantedAuthorityAclEntryImpl( "ACL_" + userAcl.getAcl().getCode(), (AclEntry)userAcl.createAclEntry() ) );
    }

    // Add CM roles based on these roles
    CmsAuthorizationUtil.addCmsRolesToGrantedAuthority( grantedAuthorities );

    // Convert roles to array and save on user
    // List<GrantedAuthority> grantedAuth = (List<GrantedAuthority>)grantedAuthorities;

    List<GrantedAuthority> grantedAuth = new ArrayList<GrantedAuthority>( grantedAuthorities );

    authenticatedUser.setAuthorities( grantedAuth );
    return authenticatedUser;
  }

  /**
   * Check Sysvar
   * 
   * @return true if this client program requires Paxs to accept Terms & Conditions otherwise return
   *         false if T&Cs not required
   */
  public boolean isTermsAndConditionsUsed()
  {
    boolean isTermsAndConditionsUsed = systemVariableService.getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
    return isTermsAndConditionsUsed;
  }

  /**
   * Return true if the user has a null question type or a blank answer.
   */

  private boolean secretDetailsIncomplete( User user )
  {
    return user.getSecretQuestionType() == null || StringUtils.isEmpty( user.getSecretAnswer() );
  }

  /**
   * Utility method to create GrantedAuthorities
   * 
   * @param roleCode
   * @return GrantedAuthorityImpl with ROLE_ and roleCode or null if roleCode is invalid
   */
  private SimpleGrantedAuthority buildGrantedAuthority( String roleCode )
  {
    if ( roleCode == null || roleCode.trim().equals( "" ) )
    {
      return null;
    }
    return new SimpleGrantedAuthority( "ROLE_" + roleCode );
  }

  public String getShoppingUrlForInactiveUser( String inactivatedUserUrl, String multipleSupplierUrl, Long userId )
  {
    String externalSupplierUrl = null;
    String shoppingType = ShoppingService.NONE;
    boolean multiplieSupplier = false;

    // if country has more than one supplier or one supplier and has display travel award true then
    // takes to multiple supplier awards page.
    UserAddress userAddress = userService.getPrimaryUserAddress( userId );
    if ( userAddress != null )
    {
      Address address = userAddress.getAddress();
      if ( address != null )
      {
        if ( address.getCountry().getCountrySuppliers() != null && address.getCountry().getCountrySuppliers().size() > 1
            || address.getCountry().getCountrySuppliers() != null && address.getCountry().getCountrySuppliers().size() == 1 && address.getCountry().getDisplayTravelAward() )
        {
          multiplieSupplier = true;
        }
        else
        {
          shoppingType = shoppingService.checkShoppingType( userId );

          if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
          {
            String catalogUrl = "";
            if ( address.getCountry() != null && address.getCountry().getCountrySuppliers() != null )
            {
              Set suppliers = address.getCountry().getCountrySuppliers();
              Iterator iterator = suppliers.iterator();
              while ( iterator.hasNext() )
              {
                CountrySupplier countrySupplier = (CountrySupplier)iterator.next();
                if ( countrySupplier != null && countrySupplier.getSupplier() != null )
                {
                  catalogUrl = countrySupplier.getSupplier().getCatalogUrl();
                }
              }
            }
            externalSupplierUrl = catalogUrl;
          }
        }
      }
    }

    if ( multiplieSupplier )
    {
      return multipleSupplierUrl;
    }
    else if ( shoppingType.equals( ShoppingService.INTERNAL ) )
    {
      return inactivatedUserUrl;
    }
    else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
    {
      return externalSupplierUrl;
    }
    return inactivatedUserUrl;
  }

  /**
   * Set the UserDAO through IoC
   * 
   * @param userDAO
   */
  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  /**
   * @param userLockoutStrategy
   */
  public void setUserLockoutStrategy( UserLockoutStrategy userLockoutStrategy )
  {
    this.userLockoutStrategy = userLockoutStrategy;
  }

  /**
   * @param accountExpirationStrategy value for accountExpirationStrategy property
   */
  public void setAccountExpirationStrategy( AccountExpirationStrategy accountExpirationStrategy )
  {
    this.accountExpirationStrategy = accountExpirationStrategy;
  }

  /**
   * @param passwordPolicyStrategy value for passwordPolicyStrategy property
   */
  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicyStrategy )
  {
    this.passwordPolicyStrategy = passwordPolicyStrategy;
  }

  /**
   * @param systemVariableService value for systemVariableService property
   */
  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setProxyService( ProxyService proxyService )
  {
    this.proxyService = proxyService;
  }

  public static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setShoppingService( ShoppingService shoppingService )
  {
    this.shoppingService = shoppingService;
  }

  public void setLockoutWarningThreshold( int lockoutWarningThreshold )
  {
    this.lockoutWarningThreshold = lockoutWarningThreshold;
  }

}

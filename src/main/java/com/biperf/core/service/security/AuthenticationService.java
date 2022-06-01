/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/security/AuthenticationService.java,v $
 */

package com.biperf.core.service.security;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.SAO;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.AccountExpirationStrategy;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.UserLockoutStrategy;
import com.biperf.core.ui.user.LockoutInfo;

/**
 * AuthenticationService.
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
 * <td>zahler</td>
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface AuthenticationService extends SAO
{
  /** Bean name for use by Service Lookup */
  public static final String BEAN_NAME = "authenticationService";
  public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

  /**
   * @param username
   * @param credentials
   * @return User
   * @throws LoginException
   */
  public AuthenticatedUser authenticate( String username, Object credentials ) throws LoginException;

  public AuthenticatedUser loginAs( String userName );

  public AuthenticatedUser authenticate( LoginToken token, HttpServletRequest request );

  /**
   * Build the AuthenticatedUser object.
   * 
   * @param user
   * @param credentials
   * @return AuthenticatedUser
   */
  public AuthenticatedUser buildAuthenticatedUser( User user, Object credentials );

  /**
   * Checks to see if User is locked out of the system.
   * 
   * @param user
   * @return boolean isUserLockedOut
   */
  public boolean isUserLockedOut( User user );
  
  public LockoutInfo getUserLockOutInfo( User user );

  /**
   * Check Sysvar
   * 
   * @return true if this client program requires Paxs to accept Terms & Conditions otherwise return
   *         false if T&Cs not required
   */
  public boolean isTermsAndConditionsUsed();

  /**
   * 
   * @return true if this client program requires has T&C set to true and pax is inactive with T&C status set to accepted 
   * @return true if this client program requires has T&C set to false and pax is inactive with T&C status set to notaccepted 
   * otherwise returns false
   */
  public boolean isPaxRealInactive( Participant pax );

  /**
   * Set the UserDAO through IoC
   * 
   * @param userDAO
   */
  public void setUserDAO( UserDAO userDAO );

  /**
   * @param userLockoutStrategy
   */
  public void setUserLockoutStrategy( UserLockoutStrategy userLockoutStrategy );

  /**
   * @param accountExpirationStrategy value for accountExpirationStrategy property
   */
  public void setAccountExpirationStrategy( AccountExpirationStrategy accountExpirationStrategy );

  /**
   * @param passwordPolicyStrategy value for passwordPolicyStrategy property
   */
  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicyStrategy );

  /**
   * Set the audienceService through IoC.
   * 
   * @param audienceService
   */
  public void setAudienceService( AudienceService audienceService );

  /**
   * Set the participantService through IoC.
   * 
   * @param participantService
   */
  public void setParticipantService( ParticipantService participantService );

  /**
   * Set the systemVariableService through IoC.
   * 
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService );

  /**
   * Set the awardBanQServiceFactory through IoC.
   * 
   * @param awardBanQServiceFactory
   */
  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory );

  /**
   * Set the proxyService through IoC.
   * 
   * @param proxyService
   */
  public void setProxyService( ProxyService proxyService );

  public String getShoppingUrlForInactiveUser( String inactivatedUserUrl, String multipleSupplierUrl, Long userId );

  public boolean isPaxInactiveWithNoAwardBanQPoints( User user );
}

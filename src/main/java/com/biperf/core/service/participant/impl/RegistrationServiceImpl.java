
package com.biperf.core.service.participant.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.participant.RegistrationService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.RegistrationStrategy;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.UserValueBean;

public class RegistrationServiceImpl implements RegistrationService
{
  public static final String BI_ADMIN = "BI-ADMIN";

  private RegistrationStrategy registrationStrategy = null;
  private AuthenticationService authenticationService = null;
  private UserService userService = null;
  private WelcomeEmailService welcomeEmailService = null;
  private SystemVariableService systemVariableService = null;
  private PasswordPolicyStrategy passwordPolicyStrategy = null;

  /**
   * Ensures the registration code entered is valid.
   * 
   * @param registrationCode
   * @throws ServiceErrorException
   */
  public void validateRegistrationCode( String registrationCode ) throws ServiceErrorException
  {
    List errors = new ArrayList();

    errors = getRegistrationStrategy().getRegistrationCodeValidationErrors( registrationCode );

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorException( errors );
    }
  }

  /**
   * Ensures the password entered is valid.
   * 
   * @param newPassword
   * @throws ServiceErrorException
   */
  public void validatePassword( String newPassword ) throws ServiceErrorException
  {
    List errors = new ArrayList();

    errors = getPasswordPolicyStrategy().getPasswordValidationErrors( "", newPassword ); // no old
                                                                                         // password

    if ( !errors.isEmpty() )
    {
      throw new ServiceErrorException( errors );
    }
  }

  /**
   * Enrolls a participant, sends welcome email and logs that person in.
   * 
   * @param participant
   * @param promotionId
   * @param audienceId
   * @throws ServiceErrorException
   */
  public void enroll( Participant participant ) throws ServiceErrorException
  {
    // Set "BI-ADMIN" as the current user on UserManager in order to insert to database.
    setAdminAsCurrentUser();

    // Persist Participant to database.
    List enrollErrors = getRegistrationStrategy().enrollParticipant( participant );

    // FLUSH: The flush was needed after hibernate 3.2.3 upgrade not
    // doing flush before fetch.
    // HibernateSessionManager.getSession().flush() ;

    if ( !enrollErrors.isEmpty() )
    {
      throw new ServiceErrorException( enrollErrors );
    }

    // Email address is an optional field on the Registration form.
    // Per use case if the pax does not give an email address then
    // no welcome email will be sent.
    if ( participant.getPrimaryEmailAddress() != null && participant.getPrimaryEmailAddress().getEmailAddr() != null )
    {
      sendWelcomeEmail( participant, false );
    }

    // Login the newly enrolled participant.
    loginAs( participant.getUserName() );
  }

  /**
   * Enrolls a RosterMgmtparticipant, sends welcome email and logs that person in.
   * 
   * @param participant
   * @throws ServiceErrorException
   */

  public void sendWelcomeEamiltoRosterMgmtPax( Participant participant ) throws ServiceErrorException
  {
    sendWelcomeEmail( participant, participant.isForcePasswordChange() );
  }

  /**
   * HibernateAuditInterceptor onSave() won't allow insert made to database unless there is an
   * authenticated user on ThreadLocal.
   */
  private void setAdminAsCurrentUser()
  {
    User user = userService.getUserByUserName( BI_ADMIN );

    // Since our registration is accessed by ROLE_ANONYMOUS the UserManager
    // will not have a user object and any inserts will fail because the audit interceptor
    // wont be able to get a value for createdBy. So we need to create an authenticated user
    // but we don't have a pax yet so we are using bi-admin as the user.
    // Then so the audit interceptor can get the value for created by set the
    // authenticated user in the UserManager.
    if ( UserManager.getUser() == null )
    {
      AuthenticatedUser authUser = new AuthenticatedUser();
      authUser.setUserId( user.getId() );
      UserManager.setUser( authUser );
    }
  }

  /**
   * Login the newly enrolled participant.
   * 
   * @param userName
   */
  private void loginAs( String userName )
  {
    getAuthenticationService().loginAs( userName );
  }

  /**
   * Sends an welcome email to the participant.
   * 
   * @param participant
   */
  private void sendWelcomeEmail( Participant participant, boolean forcePasswordChange )
  {
    UserValueBean userValueBean = new UserValueBean();
    userValueBean.setId( participant.getId() );
    userValueBean.setLastName( participant.getLastName() );
    userValueBean.setFirstName( participant.getFirstName() );
    userValueBean.setMiddleName( participant.getMiddleName() );
    userValueBean.setEmailAddress( participant.getPrimaryEmailAddress().getEmailAddr() );
    userValueBean.setUserName( participant.getUserName() );

    try
    {
      // System Variable to control how to send the Welcome email(s) to pax
      User user = getUserService().getUserById( UserManager.getUserId() );
      // Send one Welcome Email to paxs with both the Login ID and Password in one mailing
      welcomeEmailService.sendWelcomeEmail( userValueBean, participant.getPassword(), user, false, null );

    }
    catch( Exception e )
    {
      // do nothing here. MailingService will handle.
      // continue on so that the pax can be logged in.
    }

  }

  /**
   * This method will return a map of promotion id, audience id and node id for the specified
   * registration code.
   * 
   * @param code
   * @return Map
   */
  public Map getRegistrationInfoByRegistrationCode( String code )
  {
    return getRegistrationStrategy().getRegistrationInfoByRegistrationCode( code );
  }

  public RegistrationStrategy getRegistrationStrategy()
  {
    return registrationStrategy;
  }

  public void setRegistrationStrategy( RegistrationStrategy registrationStrategy )
  {
    this.registrationStrategy = registrationStrategy;
  }

  public AuthenticationService getAuthenticationService()
  {
    return authenticationService;
  }

  public void setAuthenticationService( AuthenticationService authenticationService )
  {
    this.authenticationService = authenticationService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public WelcomeEmailService getWelcomeEmailService()
  {
    return welcomeEmailService;
  }

  public void setWelcomeEmailService( WelcomeEmailService welcomeEmailService )
  {
    this.welcomeEmailService = welcomeEmailService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public PasswordPolicyStrategy getPasswordPolicyStrategy()
  {
    return passwordPolicyStrategy;
  }

  public void setPasswordPolicyStrategy( PasswordPolicyStrategy passwordPolicyStrategy )
  {
    this.passwordPolicyStrategy = passwordPolicyStrategy;
  }

}

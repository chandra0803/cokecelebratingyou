/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/participant/impl/ProfileServiceImplTest.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.util.ArrayList;

import org.jmock.Mock;

import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.UserPasswordHistoryDAO;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/**
 * ProfileServiceImplTest.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProfileServiceImplTest extends BaseServiceTest
{

  private ProfileServiceImpl profileService = new ProfileServiceImpl();
  private Mock mockAuthService = null;
  private Mock mockUserDAO = null;
  private Mock mockParticipantDAO = null;
  private Mock mockPasswordPolicyStrategy = null;
  private Mock mockSystemVariableService = null;
  private Mock mockUserPasswordHistoryDAO = null;

  public void setUp() throws Exception
  {
    super.setUp();

    mockAuthService = new Mock( AuthenticationService.class );
    mockUserDAO = new Mock( UserDAO.class );
    mockParticipantDAO = new Mock( ParticipantDAO.class );
    mockPasswordPolicyStrategy = new Mock( PasswordPolicyStrategy.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );
    mockUserPasswordHistoryDAO = new Mock( UserPasswordHistoryDAO.class );

    profileService.setAuthenticationService( (AuthenticationService)mockAuthService.proxy() );
    profileService.setUserDAO( (UserDAO)mockUserDAO.proxy() );
    profileService.setParticipantDAO( (ParticipantDAO)mockParticipantDAO.proxy() );
    profileService.setPasswordPolicyStrategy( (PasswordPolicyStrategy)mockPasswordPolicyStrategy.proxy() );
    profileService.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
    profileService.setUserPasswordHistoryDAO( (UserPasswordHistoryDAO)mockUserPasswordHistoryDAO.proxy() );
  }

  public void testSetTermsAndConditionsToAccept() throws ServiceErrorException
  {
    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( new Long( 1 ) );
    UserManager.setUser( authUser );
    Long userId = UserManager.getUserId();
    Participant pax = new Participant();
    pax.setId( userId );
    pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.ACCEPTED ) );
    pax.setTermsAcceptedDate( DateUtils.getCurrentDate() );
    pax.setUserIDAcceptedTerms( userId.toString() );
    pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.ACTIVE ) );
    pax.setStatusChangeDate( DateUtils.getCurrentDate() );

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( userId ) ).will( returnValue( pax ) );

    mockParticipantDAO.expects( once() ).method( "saveParticipant" ).with( same( pax ) );

    profileService.setTermsAndConditions( userId, true );

    mockAuthService.verify();
    mockUserDAO.verify();
    assertNotNull( "terms accept date should not be null.", pax.getTermsAcceptedDate() );
    assertNotNull( "status change date should not be null.", pax.getStatusChangeDate() );
    assertEquals( "Terms and Conditions Acceptance should be accepted.", "accepted", pax.getTermsAcceptance().getCode() );
    assertEquals( "Terms and Conditions Pax status should be active.", "active", pax.getStatus().getCode() );
    assertEquals( "Terms and Conditions User Id accepted should be equal.", userId.toString(), pax.getUserIDAcceptedTerms() );
  }

  public void testSetTermsAndConditionsToDecline() throws ServiceErrorException
  {
    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( new Long( 1 ) );
    UserManager.setUser( authUser );
    Long userId = UserManager.getUserId();
    Participant pax = new Participant();
    pax.setId( userId );
    pax.setTermsAcceptance( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.NOTACCEPTED ) );
    pax.setStatus( ParticipantStatus.lookup( ParticipantStatus.INACTIVE ) );
    pax.setStatusChangeDate( DateUtils.getCurrentDate() );

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( userId ) ).will( returnValue( pax ) );

    mockParticipantDAO.expects( once() ).method( "saveParticipant" ).with( same( pax ) );

    profileService.setTermsAndConditions( userId, false );

    mockAuthService.verify();
    mockUserDAO.verify();
    assertNull( "terms accept date should be null.", pax.getTermsAcceptedDate() );
    assertNotNull( "status change date should not be null.", pax.getStatusChangeDate() );
    assertEquals( "Terms and Conditions Acceptance should be not accepted.", "notaccepted", pax.getTermsAcceptance().getCode() );
    assertEquals( "Terms and Conditions Pax status should be inactive.", "inactive", pax.getStatus().getCode() );
    assertNull( "Terms and Conditions User Id accepted should be null.", pax.getUserIDAcceptedTerms() );
  }

  public void testChangePassword() throws ServiceErrorException
  {

    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( new Long( 1 ) );
    UserManager.setUser( authUser );

    Long userId = UserManager.getUserId();
    User user = new User();
    user.setId( userId );
    user.setPassword( "oldPassword" );
    user.setLastResetDate( null );
    user.setForcePasswordChange( true );
    String newPassword = "newPass";
    String secretQuestion = "city";
    String secretAnswer = "EdenPrairie";
    user.setSecretQuestionType( SecretQuestionType.lookup( secretQuestion ) );
    user.setSecretAnswer( secretAnswer );
    // user.setReissuedPasswordExpireDate( null );
    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( userId ) ).will( returnValue( user ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    mockPasswordPolicyStrategy.expects( once() ).method( "getPasswordValidationErrors" ).with( same( user.getPassword() ), same( newPassword ) ).will( returnValue( new ArrayList<>() ) );

    final PropertySetItem passwordHistoryCheckCount = new PropertySetItem();
    passwordHistoryCheckCount.setIntVal( 5 );
    passwordHistoryCheckCount.setKey( SystemVariableService.USER_PASSWORD_HISTORY_CHECK_COUNT );
    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.USER_PASSWORD_HISTORY_CHECK_COUNT ) ).will( returnValue( passwordHistoryCheckCount ) );

    mockUserPasswordHistoryDAO.expects( once() ).method( "getUserPasswords" ).with( same( userId ) ).will( returnValue( null ) );

    profileService.changePassword( userId, newPassword, secretQuestion, secretAnswer, false );

    mockAuthService.verify();
    mockUserDAO.verify();
    mockSystemVariableService.verify();
    mockUserPasswordHistoryDAO.verify();
    assertNotNull( user.getLastResetDate() );
    assertFalse( user.isForcePasswordChange() );
  }

  public void testChangePasswordWithAccRecovery() throws ServiceErrorException
  {

    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( new Long( 1 ) );
    UserManager.setUser( authUser );

    Long userId = UserManager.getUserId();
    User user = new User();
    user.setId( userId );
    user.setPassword( "oldPassword" );
    user.setLastResetDate( null );
    user.setForcePasswordChange( true );
    String newPassword = "newPass";
    String oldPassword = "oldPass";
    // String secretQuestion = "city";
    // String secretAnswer = "EdenPrairie";
    String emailAddress = "s@gmail.com";
    String phoneNumber = "9791047656";
    String countryPhoneCode = "in";
    UserPhone userPhone = new UserPhone();
    userPhone.setCountryPhoneCode( countryPhoneCode );
    userPhone.setIsPrimary( Boolean.FALSE );
    userPhone.setPhoneNbr( phoneNumber );
    userPhone.setPhoneType( PhoneType.lookup( PhoneType.RECOVERY ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    user.addUserPhone( userPhone );
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( emailAddress );
    userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( Boolean.FALSE );
    user.addUserEmailAddress( userEmailAddress );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( userId ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "updateRecoveryPhone" ).with( eq( userId ), eq( phoneNumber ), eq( countryPhoneCode ) ).will( returnValue( false ) );
    mockUserDAO.expects( once() ).method( "updateRecoveryEmail" ).with( eq( userId ), eq( emailAddress ) ).will( returnValue( false ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    mockPasswordPolicyStrategy.expects( once() ).method( "isValidPassword" ).with( same( user.getPassword() ), same( oldPassword ) ).will( returnValue( new ArrayList<>() ) );
    mockPasswordPolicyStrategy.expects( once() ).method( "getPasswordValidationErrors" ).with( same( user.getPassword() ), same( newPassword ) ).will( returnValue( new ArrayList<>() ) );

    final PropertySetItem passwordHistoryCheckCount = new PropertySetItem();
    passwordHistoryCheckCount.setIntVal( 5 );
    passwordHistoryCheckCount.setKey( SystemVariableService.USER_PASSWORD_HISTORY_CHECK_COUNT );
    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( same( SystemVariableService.USER_PASSWORD_HISTORY_CHECK_COUNT ) ).will( returnValue( passwordHistoryCheckCount ) );

    mockUserPasswordHistoryDAO.expects( once() ).method( "getUserPasswords" ).with( same( userId ) ).will( returnValue( null ) );

    profileService.changePassword( userId, newPassword, oldPassword, emailAddress, phoneNumber, countryPhoneCode, true );
    mockAuthService.verify();
    mockUserDAO.verify();
    mockSystemVariableService.verify();
    mockUserPasswordHistoryDAO.verify();
    assertNotNull( user.getLastResetDate() );
    assertFalse( user.isForcePasswordChange() );
  }

  public void testSaveAccRecoveryInfo() throws ServiceErrorException
  {

    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( new Long( 1 ) );
    UserManager.setUser( authUser );

    Long userId = UserManager.getUserId();
    User user = new User();
    user.setId( userId );
    String emailAddress = "s@gmail.com";
    String phoneNumber = "9791047656";
    String countryPhoneCode = "in";
    UserPhone userPhone = new UserPhone();
    userPhone.setCountryPhoneCode( countryPhoneCode );
    userPhone.setIsPrimary( Boolean.FALSE );
    userPhone.setPhoneNbr( phoneNumber );
    userPhone.setPhoneType( PhoneType.lookup( PhoneType.RECOVERY ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    user.addUserPhone( userPhone );
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( emailAddress );
    userEmailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setIsPrimary( Boolean.FALSE );
    user.addUserEmailAddress( userEmailAddress );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( userId ) ).will( returnValue( user ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( same( user ) );

    profileService.saveAccRecoveryInfo( userId, emailAddress, phoneNumber, countryPhoneCode );
    mockAuthService.verify();
    mockUserDAO.verify();
    assertEquals( "User ID", userId, user.getId() );
  }

}

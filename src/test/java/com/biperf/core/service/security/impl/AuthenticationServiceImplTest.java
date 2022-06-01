/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/security/impl/AuthenticationServiceImplTest.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.FailedLoginException;

import org.jmock.Mock;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.credentials.QuestionAnswerCredentials;
import com.biperf.core.security.exception.AccountLockoutException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.strategy.AccountExpirationStrategy;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.UserLockoutStrategy;
import com.biperf.core.ui.user.LockoutInfo;

/**
 * AuthenticationServiceImplTest.
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
 * <td>robinsra</td>
 * <td>Apr 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthenticationServiceImplTest extends BaseServiceTest
{
  private AuthenticationService authenticationService = new AuthenticationServiceImpl();
  private Mock mockUserDAO = null;
  private Mock mockUserLockoutStrategy = null;
  private Mock mockAccountExpirationStrategy = null;
  private Mock mockPasswordPolicyStrategy = null;
  private Mock mockProxyService = null;

  private SecretQuestionType mockSecretQuestionType = new SecretQuestionType()
  {
    public String getCode()
    {
      return "PET";
    }
  };

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockUserDAO = new Mock( UserDAO.class );
    mockUserLockoutStrategy = new Mock( UserLockoutStrategy.class );
    mockAccountExpirationStrategy = new Mock( AccountExpirationStrategy.class );
    mockPasswordPolicyStrategy = new Mock( PasswordPolicyStrategy.class );
    mockProxyService = new Mock( ProxyService.class );

    authenticationService.setUserDAO( (UserDAO)mockUserDAO.proxy() );
    authenticationService.setUserLockoutStrategy( (UserLockoutStrategy)mockUserLockoutStrategy.proxy() );
    authenticationService.setAccountExpirationStrategy( (AccountExpirationStrategy)mockAccountExpirationStrategy.proxy() );
    authenticationService.setPasswordPolicyStrategy( (PasswordPolicyStrategy)mockPasswordPolicyStrategy.proxy() );
    authenticationService.setProxyService( (ProxyService)mockProxyService.proxy() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.BaseServiceTest#tearDown()
   * @throws Exception
   */
  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  /**
   * testAuthenticateByNameQuestAnswer
   * 
   * @throws Exception
   */
  public void testAuthenticateByUserNameQuestionAnswer() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en" ) );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );

    List<Participant> delegates = new ArrayList<Participant>();
    // user.setStatusType( mockTrueStatusType );
    user.setSecretAnswer( "Smith" );
    user.setSecretQuestionType( mockSecretQuestionType );

    String country = "en_US";

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "getUserTimeZone" ).with( eq( user.getId() ) ).will( returnValue( country ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( eq( user ) ).will( returnValue( user ) );
    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" ).with( eq( user ), eq( true ) );
    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( eq( user ) ).will( returnValue( false ) );
    mockAccountExpirationStrategy.expects( once() ).method( "isAccountExpired" ).with( eq( user ) ).will( returnValue( false ) );
    mockPasswordPolicyStrategy.expects( once() ).method( "isPasswordExpired" ).with( eq( user ) ).will( returnValue( false ) );

    mockProxyService.expects( once() ).method( "getUsersByProxyUserId" ).with( eq( user.getId() ) ).will( returnValue( delegates ) );

    authenticationService.authenticate( user.getUserName(), new QuestionAnswerCredentials( user.getSecretQuestionType().getCode(), user.getSecretAnswer() ) );

    mockUserDAO.verify();

  }

  /**
   * testAuthenticateByNamePassword
   * 
   * @throws Exception
   */
  public void testAuthenticateByUserNamePassword() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en" ) );
    // user.setStatusType( mockTrueStatusType );
    user.setPassword( "password" );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );

    List<Participant> delegates = new ArrayList<Participant>();

    String country = "en_US";

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "isPasswordValid" ).will( returnValue( true ) );

    mockUserDAO.expects( once() ).method( "getUserTimeZone" ).with( eq( user.getId() ) ).will( returnValue( country ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( eq( user ) ).will( returnValue( user ) );
    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" );
    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( eq( user ) ).will( returnValue( false ) );
    mockAccountExpirationStrategy.expects( once() ).method( "isAccountExpired" ).with( eq( user ) ).will( returnValue( false ) );
    mockPasswordPolicyStrategy.expects( once() ).method( "isPasswordExpired" ).with( eq( user ) ).will( returnValue( false ) );

    mockProxyService.expects( once() ).method( "getUsersByProxyUserId" ).with( eq( user.getId() ) ).will( returnValue( delegates ) );

    authenticationService.authenticate( user.getUserName(), user.getPassword() );

    mockUserDAO.verify();

  }

  /**
   * testAuthenticateByUserNamePassword with invalid Username
   * 
   * @throws Exception
   */
  public void testAuthenticateUserNameInvalid() throws Exception
  {
    String badUserName = "badUserName";
    String password = "password";

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( badUserName ) ).will( returnValue( null ) );
    // mockUserCache.expects( once() ).method( "getUserFromCache" ).with( eq( badUserName ) )
    // .will( returnValue( null ) );
    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" ).with( eq( null ), eq( false ) );

    try
    {
      authenticationService.authenticate( badUserName, password );

      fail( "Should have thrown FailedLoginException" );
    }
    catch( FailedLoginException e )
    {
      return;
    }
  }

  /**
   * testAuthenticateByUserNamePassword with invalid password
   * 
   * @throws Exception
   */
  public void testAuthenticatePasswordInvalid() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );
    user.setActive( Boolean.TRUE );

    String badPassword = "badPassword";

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "isPasswordValid" ).will( returnValue( false ) );

    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" ).with( eq( user ), eq( false ) );
    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( eq( user ) ).will( returnValue( false ) );
    mockUserLockoutStrategy.expects( once() ).method( "getAllowableFailedLoginAttempts" ).will( returnValue( 5 ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( eq( user ) ).will( returnValue( user ) );

    try
    {
      authenticationService.authenticate( user.getUserName(), badPassword );

      fail( "Should have thrown FailedLoginException" );
    }
    catch( FailedLoginException e )
    {
      return;
    }
  }

  /**
   * testAuthenticateByUserNamePassword with invalid password
   * 
   * @throws Exception
   */
  public void testAuthenticateAccountExpired() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );
    user.setActive( Boolean.TRUE );

    String password = "password";
    user.setPassword( password );

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "isPasswordValid" ).will( returnValue( true ) );
    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( eq( user ) ).will( returnValue( false ) );

    mockAccountExpirationStrategy.expects( once() ).method( "isAccountExpired" ).with( eq( user ) ).will( returnValue( true ) );

    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" ).with( eq( user ), eq( false ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( eq( user ) ).will( returnValue( user ) );

    try
    {
      authenticationService.authenticate( user.getUserName(), password );

      fail( "Should have thrown AccountExpiredException" );
    }
    catch( AccountExpiredException e )
    {
      return;
    }
  }

  /**
   * testAuthenticateByUserNamePassword with invalid password
   * 
   * @throws Exception
   */
  public void testAuthenticateLockout() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );
    user.setActive( Boolean.TRUE );

    String password = "password";

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );
    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( eq( user ) ).will( returnValue( true ) );
    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" ).with( eq( user ), eq( false ) );
    mockUserLockoutStrategy.expects( once() ).method( "getUserLockOutInfo" ).with( eq( user ) ).will( returnValue( new LockoutInfo() ) );
    mockUserDAO.expects( once() ).method( "saveUser" ).with( eq( user ) ).will( returnValue( user ) );

    try
    {
      authenticationService.authenticate( user.getUserName(), password );

      fail( "Should have thrown AccountLockoutException" );
    }
    catch( AccountLockoutException e )
    {
      return;
    }
  }

  /**
   * testAuthenticateByNamePassword
   * 
   * @throws Exception
   */
  public void testAuthenticatePasswordExpired() throws Exception
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "unittest" );
    user.setActive( Boolean.TRUE );
    user.setLanguageType( LanguageType.lookup( "en" ) );
    // user.setStatusType( mockTrueStatusType );
    user.setPassword( "password" );
    user.setUserType( UserType.lookup( UserType.PARTICIPANT ) );

    List<Participant> delegates = new ArrayList<Participant>();

    String country = "en_US";

    mockUserDAO.expects( once() ).method( "getUserByUserName" ).with( eq( user.getUserName() ) ).will( returnValue( user ) );
    mockUserDAO.expects( once() ).method( "isPasswordValid" ).will( returnValue( true ) );

    mockUserDAO.expects( once() ).method( "getUserTimeZone" ).with( eq( user.getId() ) ).will( returnValue( country ) );

    mockUserDAO.expects( once() ).method( "saveUser" ).with( eq( user ) ).will( returnValue( user ) );
    mockUserLockoutStrategy.expects( once() ).method( "handleAuthentication" );
    mockUserLockoutStrategy.expects( once() ).method( "isUserLockedout" ).with( eq( user ) ).will( returnValue( false ) );
    mockAccountExpirationStrategy.expects( once() ).method( "isAccountExpired" ).with( eq( user ) ).will( returnValue( false ) );
    mockPasswordPolicyStrategy.expects( once() ).method( "isPasswordExpired" ).with( eq( user ) ).will( returnValue( true ) );

    mockProxyService.expects( once() ).method( "getUsersByProxyUserId" ).with( eq( user.getId() ) ).will( returnValue( delegates ) );

    AuthenticatedUser authenticatedUser = authenticationService.authenticate( user.getUserName(), user.getPassword() );

    assertEquals( "Password should be expired", false, authenticatedUser.isCredentialsNonExpired() );

  }
}

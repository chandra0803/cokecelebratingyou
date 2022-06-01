
package com.biperf.core.dao.participant.hibernate;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.UserTokenDAO;
import com.biperf.core.domain.enums.UserTokenStatusType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.EmailUserToken;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.crypto.SHA256Hash;

public class UserTokenDAOImplTest extends BaseDAOTest
{

  private static UserTokenDAO underTest;

  static
  {
    underTest = (UserTokenDAO)ApplicationContextFactory.getApplicationContext().getBean( "userTokenDAO" );
  }

  public void testSaveUserToken()
  {
    UserToken userToken = buildUserToken();
    userToken.setUser( buildAndSaveParticipant() );
    underTest.saveUserToken( userToken );
    flushAndClearSession();

    assertTrue( "Token should not be empty", userToken.getToken() != null );
    assertTrue( userToken.getId() != null );
  }

  public void testValidateToken()
  {
    User existingUser = getUserDao().getUserByUserName( buildAndSaveParticipant().getUserName() );

    UserToken userToken = buildUserToken();
    userToken.setUser( existingUser );
    underTest.saveUserToken( userToken );
    flushAndClearSession();
    boolean validateToken = underTest.validateToken( existingUser.getId(), userToken.getToken() );

    assertTrue( "Token should not be empty", userToken.getToken() != null );
    assertTrue( userToken.getId() != null );
    assertTrue( validateToken );
  }

  public void testValidateClearTextToken()
  {
    User existingUser = getUserDao().getUserByUserName( buildAndSaveParticipant().getUserName() );
    String tokenClearText = "A123B123";
    UserToken userToken = buildUserToken();
    userToken.setUser( existingUser );
    userToken.setToken( new SHA256Hash().encryptDefault( tokenClearText ) );
    underTest.saveUserToken( userToken );
    flushAndClearSession();
    boolean validateToken = underTest.validateClearTextToken( existingUser.getId(), tokenClearText );

    assertTrue( "Token should not be empty", userToken.getToken() != null );
    assertTrue( userToken.getId() != null );
    assertTrue( validateToken );
  }

  public void testgetTokenById()
  {
    User existingUser = getUserDao().getUserByUserName( buildAndSaveParticipant().getUserName() );

    UserToken expectedUaserToken = buildUserToken();
    expectedUaserToken.setUser( existingUser );
    underTest.saveUserToken( expectedUaserToken );

    flushAndClearSession();

    UserToken actualToken = underTest.getTokenById( expectedUaserToken.getToken() );

    assertEquals( expectedUaserToken.getToken(), actualToken.getToken() );
  }

  private UserDAO getUserDao()
  {
    return (UserDAO)BeanLocator.getBean( UserDAO.class );
  }

  public static UserToken buildUserToken()
  {
    UserToken userToken = new EmailUserToken();
    userToken.setStatus( UserTokenStatusType.lookup( UserTokenStatusType.ISSUED ) );
    userToken.setToken( new SHA256Hash().encryptDefault( "A123B123" ) );
    userToken.setExpirationDate( Date.from( Instant.now().plus( 1L, ChronoUnit.DAYS ) ) );
    return userToken;
  }

  private Participant buildAndSaveParticipant()
  {
    return ParticipantDAOImplTest.buildAndSaveParticipant( buildUniqueString() );
  }

}

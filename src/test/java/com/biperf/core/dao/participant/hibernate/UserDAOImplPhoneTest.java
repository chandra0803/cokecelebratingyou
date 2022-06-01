/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/participant/hibernate/UserDAOImplPhoneTest.java,v $
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * UserDAOImplPhoneTest.
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
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserDAOImplPhoneTest extends BaseDAOTest
{
  /**
   * Returns user dao
   * 
   * @return UserDAO
   */
  protected UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Test Adding a UserPhone
   */
  public void testAddUserPhone()
  {
    // Create new user
    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setFirstName( "TestFIRSTNAME" );
    user.setLastName( "TestLASTNAME" );
    user.setPassword( "testPASSWORD" );
    user.setUserName( "testUSERNAME" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );
    user.setLastResetDate( new Date() );

    // Save the user
    User actualUser = getUserDAO().saveUser( user );

    // Create User Phone
    UserPhone userPhone = new UserPhone();
    userPhone.setPhoneType( PhoneType.lookup( "hom" ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhone.setPhoneNbr( "9119119111" );
    userPhone.setPhoneExt( "911" );

    // Add Phone
    actualUser.addUserPhone( userPhone );

    // Save the user with the phone added
    User finalUser = getUserDAO().saveUser( actualUser );

    HibernateSessionManager.getSession().flush();

    // make sure one user phone record exists
    assertTrue( finalUser.getUserPhones().size() == 1 );

    // make sure the correct record is there
    assertTrue( finalUser.getUserPhones().contains( userPhone ) );
  }

  /**
   * Test Adding a UserPhone
   */
  public void testUpdateUserPhone()
  {
    // Create new user
    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setFirstName( "TestFIRSTNAME" );
    user.setLastName( "TestLASTNAME" );
    user.setPassword( "testPASSWORD" );
    user.setUserName( "testUSERNAME" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );
    user.setLastResetDate( new Date() );

    // Create User Phone
    UserPhone userPhone = new UserPhone();
    userPhone.setPhoneType( PhoneType.lookup( "hom" ) );
    userPhone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userPhone.setPhoneNbr( "9119119111" );
    userPhone.setPhoneExt( "911" );
    userPhone.setUser( user );

    user.getUserPhones().add( userPhone );

    // Save the user with the phone added
    User insertedUser = getUserDAO().saveUser( user );

    HibernateSessionManager.getSession().flush();

    String updatedPhoneNumber = "2222222222";
    // Create updated User Phone
    UserPhone userPhoneUpdated = new UserPhone();
    userPhoneUpdated.setId( userPhone.getId() );
    userPhoneUpdated.setAuditCreateInfo( userPhone.getAuditCreateInfo() );
    userPhoneUpdated.setAuditUpdateInfo( userPhone.getAuditUpdateInfo() );
    userPhoneUpdated.setVersion( userPhone.getVersion() );
    userPhoneUpdated.setIsPrimary( userPhone.getIsPrimary() );
    userPhoneUpdated.setPhoneType( userPhone.getPhoneType() );
    userPhoneUpdated.setVerificationStatus( userPhone.getVerificationStatus() );
    userPhoneUpdated.setPhoneNbr( updatedPhoneNumber );
    userPhoneUpdated.setPhoneExt( "111" );
    userPhoneUpdated.setUser( insertedUser );

    getUserDAO().updateUserPhone( userPhoneUpdated );

    HibernateSessionManager.getSession().flush();

    User finalUser = getUserDAO().getUserById( insertedUser.getId() );

    Set finalUserPhones = finalUser.getUserPhones();

    String phoneNumberFound = "";

    Iterator iter = finalUserPhones.iterator();
    while ( iter.hasNext() )
    {
      UserPhone savedUserPhone = (UserPhone)iter.next();
      if ( savedUserPhone.getPhoneType().equals( userPhone.getPhoneType() ) )
      {
        phoneNumberFound = savedUserPhone.getPhoneNbr();
      }
    }

    assertEquals( phoneNumberFound, updatedPhoneNumber );
  }
}

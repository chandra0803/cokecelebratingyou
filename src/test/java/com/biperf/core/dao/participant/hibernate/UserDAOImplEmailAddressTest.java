/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/participant/hibernate/UserDAOImplEmailAddressTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * UserDAOImplEmailAddressTest.
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
 * <td>May 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserDAOImplEmailAddressTest extends BaseDAOTest
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
   * Test Adding a UserEmailAddress
   */
  public void testAddUserEmailAddress()
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

    // Create User EmailAddress
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "unitTest@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "BUS" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setUser( actualUser );

    // Add EmailAddr
    actualUser.getUserEmailAddresses().add( userEmailAddress );

    // Save the user with the phone added
    User finalUser = getUserDAO().saveUser( actualUser );

    HibernateSessionManager.getSession().saveOrUpdate( userEmailAddress );

    assertTrue( finalUser.getUserEmailAddresses().size() == 1 );
    assertTrue( finalUser.getUserEmailAddresses().contains( userEmailAddress ) );
  }

  /**
   * Test Adding a UserPhone
   */
  public void testUpdateUserEmailAddress()
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
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "unitTest@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "BUS" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setUser( user );

    user.getUserEmailAddresses().add( userEmailAddress );

    // Save the user with the email addr added
    User actualUser = getUserDAO().saveUser( user );

    HibernateSessionManager.getSession().flush();

    String updatedEmailAddr = "unitTestUpdated@test.com";

    // Create updated User Phone
    UserEmailAddress updatedUserEmailAddress = new UserEmailAddress();
    updatedUserEmailAddress.setId( userEmailAddress.getId() );
    updatedUserEmailAddress.setAuditCreateInfo( userEmailAddress.getAuditCreateInfo() );
    updatedUserEmailAddress.setAuditUpdateInfo( userEmailAddress.getAuditUpdateInfo() );
    updatedUserEmailAddress.setVersion( userEmailAddress.getVersion() );
    updatedUserEmailAddress.setIsPrimary( userEmailAddress.getIsPrimary() );
    updatedUserEmailAddress.setEmailAddr( updatedEmailAddr );
    updatedUserEmailAddress.setEmailType( userEmailAddress.getEmailType() );
    updatedUserEmailAddress.setVerificationStatus( userEmailAddress.getVerificationStatus() );
    updatedUserEmailAddress.setUser( actualUser );

    getUserDAO().updateUserEmailAddress( updatedUserEmailAddress );

    HibernateSessionManager.getSession().flush();

    User finalUser = getUserDAO().getUserById( actualUser.getId() );

    Set finalUserEmailAddresses = finalUser.getUserEmailAddresses();

    String emailAddrFound = "";

    Iterator iter = finalUserEmailAddresses.iterator();
    while ( iter.hasNext() )
    {
      UserEmailAddress savedUserEmailAddress = (UserEmailAddress)iter.next();
      if ( savedUserEmailAddress.getEmailType().equals( userEmailAddress.getEmailType() ) )
      {
        emailAddrFound = savedUserEmailAddress.getEmailAddr();
      }
    }

    assertEquals( emailAddrFound, updatedEmailAddr );
  }
}

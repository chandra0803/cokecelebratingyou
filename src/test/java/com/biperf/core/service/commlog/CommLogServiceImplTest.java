/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/commlog/CommLogServiceImplTest.java,v $
 */

package com.biperf.core.service.commlog;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.commlog.CommLogDAO;
import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.commlog.impl.CommLogServiceImpl;

/**
 * CommLogServiceImplTest.
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
 * <td>jenniget</td>
 * <td>Nov 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogServiceImplTest extends BaseServiceTest
{

  private static int uniqueStringCounter = 1000;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public CommLogServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * commLogServiceImpl
   */
  private CommLogServiceImpl commLogService = new CommLogServiceImpl();

  /**
   * mocks
   */
  private Mock mockCommLogDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @throws Exception
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockCommLogDAO = new Mock( CommLogDAO.class );
    commLogService.setCommLogDAO( (CommLogDAO)mockCommLogDAO.proxy() );
  }

  /**
   * Test getting CommLogs by userId.
   */
  public void testGetCommLogsForUser()
  {
    List commLogList = new ArrayList();
    commLogList.add( new CommLog() );
    commLogList.add( new CommLog() );
    User user = buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
    mockCommLogDAO.expects( once() ).method( "getCommLogsByUser" ).with( same( user.getId() ) ).will( returnValue( commLogList ) );

    List returnedCommLogList = commLogService.getCommLogsForUser( user.getId(), new AssociationRequestCollection() );
    assertTrue( returnedCommLogList.size() == 2 );
    mockCommLogDAO.verify();
  } // end testGetCommLogsForUser

  /**
   * Test getting CommLogs by Id.
   */
  public void testGetCommLogById()
  {
    CommLog log = new CommLog();
    User user = buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "unitTest@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "BUS" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setUser( user );
    user.getUserEmailAddresses().add( userEmailAddress );
    log.setUser( user );
    log.setId( new Long( 1 ) );
    mockCommLogDAO.expects( once() ).method( "getCommLogById" ).with( same( log.getId() ) ).will( returnValue( log ) );

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
    CommLog returnedLog = commLogService.getCommLogById( log.getId(), requestCollection );
    assertEquals( returnedLog, log );
    mockCommLogDAO.verify();
  } // end testGetCommLogById

  /**
   * Test getting CommLogs assigned to a given userId.
   */
  public void testGetCommLogsAssignedToUser()
  {
    User user = buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
    UserEmailAddress userEmailAddress = new UserEmailAddress();
    userEmailAddress.setEmailAddr( "unitTest@test.com" );
    userEmailAddress.setEmailType( EmailAddressType.lookup( "BUS" ) );
    userEmailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    userEmailAddress.setUser( user );
    user.getUserEmailAddresses().add( userEmailAddress );

    List commLogList = new ArrayList();
    CommLog log1 = new CommLog();
    log1.setId( new Long( 1 ) );
    log1.setUser( user );

    CommLog log2 = new CommLog();
    log2.setId( new Long( 2 ) );
    log2.setUser( user );
    commLogList.add( log1 );
    commLogList.add( log2 );

    mockCommLogDAO.expects( once() ).method( "getCommLogsAssignedToUser" ).with( same( user.getId() ) ).will( returnValue( commLogList ) );
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );

    List returnedCommLogList = commLogService.getCommLogsAssignedToUser( user.getId(), requestCollection );
    assertTrue( returnedCommLogList.size() == 2 );
    mockCommLogDAO.verify();
  } // end testGetCommLogsAssignedToUser

  /**
   * Test saving a CommLog.
   */
  public void testSaveCommLog()
  {
    CommLog log = new CommLog();
    log.setCommLogReasonType( CommLogReasonType.getDefaultItem() );
    log.setCommLogCategoryType( CommLogCategoryType.getDefaultItem() );
    log.setCommLogSourceType( CommLogSourceType.getDefaultItem() );
    log.setCommLogStatusType( CommLogStatusType.getDefaultItem() );
    log.setCommLogUrgencyType( CommLogUrgencyType.getDefaultItem() );

    mockCommLogDAO.expects( once() ).method( "saveCommLog" ).with( same( log ) ).will( returnValue( log ) );

    CommLog returnedLog = commLogService.saveCommLog( log );
    assertEquals( returnedLog, log );
    mockCommLogDAO.verify();
  } // end testSaveCommLog

  /**
   * Test getting the open CommLogs assigned to user.
   */
  public void testGetOpenCommLogsAssignedToUser()
  {
    List commLogs = new ArrayList();
    User user = buildUser( new Long( 13 ), "testUser" + getUniqueString() );
    Long userId = new Long( 13 );
    commLogs.add( buildStaticCommLog( user, CommLogUrgencyType.lookup( CommLogUrgencyType.ESCALATED_COLD_CODE ) ) );
    commLogs.add( buildStaticCommLog( user, CommLogUrgencyType.lookup( CommLogUrgencyType.NORMAL_CODE ) ) );
    mockCommLogDAO.expects( once() ).method( "getOpenCommLogsAssignedToUser" ).with( same( userId ) ).will( returnValue( commLogs ) );

    List returnedCommLogs = commLogService.getOpenCommLogsAssignedToUser( userId, null );

    assertEquals( commLogs, returnedCommLogs );
  }

  /**
   * Test getting the escalated CommLogs assigned to user.
   */
  public void testGetEscalatedCommLogsAssignedToUser()
  {
    List commLogs = new ArrayList();
    User user = buildUser( new Long( 13 ), "testUser" + getUniqueString() );
    Long userId = new Long( 13 );
    CommLog commLog1 = buildStaticCommLog( user, CommLogUrgencyType.lookup( CommLogUrgencyType.ESCALATED_COLD_CODE ) );
    CommLog commLog2 = buildStaticCommLog( user, CommLogUrgencyType.lookup( CommLogUrgencyType.NORMAL_CODE ) );
    commLogs.add( commLog1 );
    commLogs.add( commLog2 );
    mockCommLogDAO.expects( once() ).method( "getOpenCommLogsAssignedToUser" ).with( same( userId ) ).will( returnValue( new ArrayList( commLogs ) ) );

    List returnedCommLogs = commLogService.getEscalatedCommLogsAssignedToUser( userId, null );

    assertNotSame( commLogs, returnedCommLogs );
    assertTrue( returnedCommLogs.contains( commLog1 ) );
    assertFalse( returnedCommLogs.contains( commLog2 ) );
  }

  public static User buildUser( Long userId, String userName )
  {
    User user = new User();
    user.setId( userId );
    user.setUserName( userName );
    return user;
  }

  public static CommLog buildStaticCommLog( User user, CommLogUrgencyType urgencyType )
  {
    CommLog commLog = new CommLog();
    commLog.setCommLogStatusType( CommLogStatusType.lookup( CommLogStatusType.OPEN_CODE ) );
    commLog.setCommLogUrgencyType( urgencyType );
    commLog.setUser( user );
    return commLog;
  }

  /**
   * Builds a static user with the params.
   * 
   * @param userName
   * @param firstName
   * @param lastName
   * @return User
   */
  public static User buildStaticUser( String userName, String firstName, String lastName )
  {

    User user = new User();
    user.setUserType( UserType.lookup( UserType.BI ) );
    user.setUserName( userName );
    user.setFirstName( firstName );
    user.setLastName( lastName );
    user.setPassword( "testPASSWORD" );
    user.setActive( Boolean.TRUE );
    user.setWelcomeEmailSent( Boolean.FALSE );

    return user;
  }

  /**
   * Builds a unique String for testing.
   * 
   * @return String
   */
  public static String buildUniqueString()
  {
    return "TEST" + ( System.currentTimeMillis() % 3432423 ) + "." + uniqueStringCounter++;
  }

} // end CommLogServiceImplTest

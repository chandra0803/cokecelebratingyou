/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/participant/impl/UserNodeHistoryServiceImplTest.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;

import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.UserNodeHistoryDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNodeHistory;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.UserNodeHistoryAssociationRequest;

/**
 * UserNodeHistoryServiceImplTest.
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
 * <td>Dec 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNodeHistoryServiceImplTest extends BaseServiceTest
{
  private UserNodeHistoryDAO userNodeHistoryDAOMock;
  private static int uniqueStringCounter = 1000;

  private UserNodeHistoryServiceImpl classUnderTest = new UserNodeHistoryServiceImpl();

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    userNodeHistoryDAOMock = EasyMock.createMock( UserNodeHistoryDAO.class );
    classUnderTest.setUserNodeHistoryDAO( userNodeHistoryDAOMock );
  }

  public void testGetAllUserNodeHistoryByUser()
  {
    List userNodeHistoryList = new ArrayList();

    User user = buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
    user.setId( new Long( 1 ) );
    Node node = NodeDAOImplTest.buildUniqueNode( getUniqueString() );

    userNodeHistoryList.add( buildUserNodeHistoryRecord( user, node ) );

    EasyMock.expect( userNodeHistoryDAOMock.getAllUserNodeHistoryByUser( user.getId() ) ).andReturn( userNodeHistoryList );
    EasyMock.replay( userNodeHistoryDAOMock );
    assertTrue( classUnderTest.getAllUserNodeHistoryByUser( new Long( 1 ) ).size() == 1 );
    EasyMock.verify( userNodeHistoryDAOMock );
  }

  public void testGetAllUserNodeHistoryByUserId()
  {
    List userNodeHistoryList = new ArrayList();

    User user = buildStaticUser( "testUsername" + buildUniqueString(), "testFIRSTNAME", "testLASTNAME" );
    user.setId( new Long( 1 ) );
    Node node = NodeDAOImplTest.buildUniqueNode( getUniqueString() );

    userNodeHistoryList.add( buildUserNodeHistoryRecord( user, node ) );

    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new UserNodeHistoryAssociationRequest( UserNodeHistoryAssociationRequest.ALL ) );

    EasyMock.expect( userNodeHistoryDAOMock.getAllUserNodeHistoryByUser( user.getId() ) ).andReturn( userNodeHistoryList );
    EasyMock.replay( userNodeHistoryDAOMock );
    assertTrue( classUnderTest.getAllUserNodeHistoryByUserId( new Long( 1 ), requestCollection ).size() == 1 );
    EasyMock.verify( userNodeHistoryDAOMock );

  }

  public static UserNodeHistory buildUserNodeHistoryRecord( User user, Node node )
  {
    UserNodeHistory userNodeHistory = new UserNodeHistory();

    userNodeHistory.setNode( node );
    userNodeHistory.setUser( user );
    userNodeHistory.setActive( Boolean.TRUE );
    userNodeHistory.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    return userNodeHistory;
  }

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

  public static String buildUniqueString()
  {
    return "TEST" + ( System.currentTimeMillis() % 3432423 ) + "." + uniqueStringCounter++;
  }

}

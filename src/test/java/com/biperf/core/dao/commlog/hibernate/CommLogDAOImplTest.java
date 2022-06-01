/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/commlog/hibernate/CommLogDAOImplTest.java,v $
 *
 */

package com.biperf.core.dao.commlog.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.commlog.CommLogDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.mailing.MailingDAO;
import com.biperf.core.dao.mailing.hibernate.MailingDAOImplTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.participant.hibernate.UserDAOImplTest;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.commlog.CommLogComment;
import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * CommLogDAOImplTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLogDAOImplTest extends BaseDAOTest
{
  /**
   * Test method getOpenCommLogsAssignedToUser().
   */
  public void testGetOpenCommLogsAssignedToUser()
  {
    CommLogDAO commLogDAO = getCommLogDAO();
    User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );

    MailingDAO mailingDAO = getMailingDAO();
    Mailing mailing1 = MailingDAOImplTest.buildMailing( "Mailing1", "Text 1" );
    mailingDAO.saveMailing( mailing1 );
    Mailing mailing2 = MailingDAOImplTest.buildMailing( "Mailing2", "Text 2" );
    mailingDAO.saveMailing( mailing2 );

    CommLog commLog1 = buildStaticCommLog( user, user );
    commLog1.setMailing( mailing1 );
    CommLog commLog2 = buildStaticCommLog( user, user );
    commLog2.setMailing( mailing2 );

    commLog1.setCommLogStatusType( CommLogStatusType.lookup( CommLogStatusType.CLOSED_CODE ) );
    commLog2.setCommLogStatusType( CommLogStatusType.lookup( CommLogStatusType.OPEN_CODE ) );
    commLogDAO.saveCommLog( commLog1 );
    commLogDAO.saveCommLog( commLog2 );

    flushAndClearSession();

    List openCommLogsByUser = commLogDAO.getOpenCommLogsAssignedToUser( user.getId() );
    assertNotNull( openCommLogsByUser );
    assertTrue( openCommLogsByUser.size() == 1 );
    assertTrue( openCommLogsByUser.contains( commLog2 ) );
    assertFalse( openCommLogsByUser.contains( commLog1 ) );

    Iterator iter = openCommLogsByUser.iterator();
    while ( iter.hasNext() )
    {
      CommLog commLog = (CommLog)iter.next();
      assertTrue( commLog.getMailing() != null );
    }
  }

  /**
   * Test method getCommLogByUser().
   */
  public void testGetCommLogsByUser()
  {
    CommLogDAO commLogDAO = getCommLogDAO();
    User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );

    CommLog commLog1 = buildStaticCommLog( user, user );
    CommLog commLog2 = buildStaticCommLog( user, user );
    commLogDAO.saveCommLog( commLog1 );
    commLogDAO.saveCommLog( commLog2 );

    flushAndClearSession();

    List commLogsByUser = commLogDAO.getCommLogsByUser( user.getId() );
    assertNotNull( commLogsByUser );
    assertTrue( commLogsByUser.size() == 2 );
    assertTrue( commLogsByUser.contains( commLog1 ) );
    assertTrue( commLogsByUser.contains( commLog2 ) );

  }

  /**
   * Test method getCommLogByClaimItem().
   */
  public void testGetCommLogsByClaimItem()
  {
    CommLogDAO commLogDAO = getCommLogDAO();
    RecognitionClaim claim = ClaimDAOImplTest.buildRecognitionClaim( "comm" + String.valueOf( System.currentTimeMillis() % 5503032 ) );
    // Create the claim recipient.
    Participant recipient1 = ParticipantDAOImplTest.buildAndSaveParticipant( "recipient1-" + String.valueOf( System.currentTimeMillis() % 5503032 ) );

    Set userNodes1 = recipient1.getUserNodes();
    Node recipient1Node1 = null;
    Iterator iter1 = userNodes1.iterator();
    if ( iter1.hasNext() )
    {
      recipient1Node1 = ( (UserNode)iter1.next() ).getNode();
    }

    ClaimRecipient claimRecipient1 = new ClaimRecipient();
    claimRecipient1.setRecipient( recipient1 );
    claimRecipient1.setNode( recipient1Node1 );
    claimRecipient1.setAwardQuantity( new Long( 5 ) );
    claimRecipient1.setRecipient( null );
    claim.addClaimRecipient( claimRecipient1 );
    getClaimDAO().saveClaim( claim );

    CommLog commLog1 = buildStaticCommLog( claimRecipient1.getRecipient(), claimRecipient1.getRecipient() );
    CommLog commLog2 = buildStaticCommLog( claimRecipient1.getRecipient(), claimRecipient1.getRecipient() );
    MailingDAO mailingDAO = getMailingDAO();
    Mailing mailing1 = MailingDAOImplTest.buildMailing( "Mailing1", "Text 1" );
    Mailing mailing2 = MailingDAOImplTest.buildMailing( "Mailing2", "Text 2" );
    commLog1.setMailing( mailing1 );
    commLog2.setMailing( mailing2 );
    ( (MailingRecipient) ( mailing1.getMailingRecipients().iterator().next() ) ).setClaimRecipientId( claimRecipient1.getId() );
    ( (MailingRecipient) ( mailing2.getMailingRecipients().iterator().next() ) ).setClaimRecipientId( claimRecipient1.getId() );
    ( (MailingRecipient) ( mailing1.getMailingRecipients().iterator().next() ) ).setUser( null );
    ( (MailingRecipient) ( mailing2.getMailingRecipients().iterator().next() ) ).setUser( null );
    mailingDAO.saveMailing( mailing1 );
    mailingDAO.saveMailing( mailing2 );
    commLogDAO.saveCommLog( commLog1 );
    commLogDAO.saveCommLog( commLog2 );

    flushAndClearSession();
  }

  /**
   * Test method getCommLogsAssignedToUser().
   */
  public void testGetCommLogsAssignedToUser()
  {
    CommLogDAO commLogDAO = getCommLogDAO();
    User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );

    CommLog commLog1 = buildStaticCommLog( user, user );
    CommLog commLog2 = buildStaticCommLog( user, user );
    commLogDAO.saveCommLog( commLog1 );
    commLogDAO.saveCommLog( commLog2 );

    flushAndClearSession();

    List commLogsByUser = commLogDAO.getCommLogsAssignedToUser( user.getId() );
    assertNotNull( commLogsByUser );
    assertTrue( commLogsByUser.size() == 2 );
    assertTrue( commLogsByUser.contains( commLog1 ) );
    assertTrue( commLogsByUser.contains( commLog2 ) );
  }

  /**
   * Test method saveCommLog().
   */
  public void testSaveCommLogAndGetById()
  {
    CommLog commLog = buildStaticCommLog();
    CommLogDAO commLogDAO = getCommLogDAO();
    commLog.addComment( buildStaticCommLogComment() );
    commLog.addComment( buildStaticCommLogComment() );
    commLog = commLogDAO.saveCommLog( commLog );

    flushAndClearSession();

    CommLog persistedCommLog = commLogDAO.getCommLogById( commLog.getId() );

    assertDomainObjectEquals( commLog, persistedCommLog );
  }

  private static CommLogDAO getCommLogDAO()
  {
    return (CommLogDAO)ApplicationContextFactory.getApplicationContext().getBean( "commLogDAO" );
  }

  private static MailingDAO getMailingDAO()
  {
    return (MailingDAO)ApplicationContextFactory.getApplicationContext().getBean( "mailingDAO" );
  }

  private static UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  private static ClaimDAO getClaimDAO()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( "claimDAO" );
  }

  public static CommLog buildStaticCommLog()
  {
    User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );
    return buildStaticCommLog( user, user );
  }

  public static CommLog buildStaticCommLog( User user, User assignedToUser )
  {
    CommLog commLog = new CommLog();
    commLog.setUser( user );
    commLog.setAssignedByUser( user );
    commLog.setAssignedToUser( assignedToUser );
    commLog.setCommLogCategoryType( CommLogCategoryType.lookup( "testCategory" ) );
    commLog.setCommLogReasonType( CommLogReasonType.lookup( "testReason" ) );
    commLog.setCommLogSourceType( CommLogSourceType.lookup( "testSource" ) );
    commLog.setCommLogStatusType( CommLogStatusType.lookup( "testStatus" ) );
    commLog.setCommLogUrgencyType( CommLogUrgencyType.lookup( CommLogUrgencyType.NORMAL_CODE ) );
    commLog.setMessageType( CommLog.MESSAGE_TYPE_EMAIL );
    return commLog;
  }

  public static CommLogComment buildStaticCommLogComment()
  {
    CommLogComment comment = new CommLogComment();
    comment.setComments( "Test Comment : " + buildUniqueString() );
    User user = UserDAOImplTest.buildStaticUser();
    getUserDAO().saveUser( user );
    comment.setCommentUser( user );
    return comment;
  }

}
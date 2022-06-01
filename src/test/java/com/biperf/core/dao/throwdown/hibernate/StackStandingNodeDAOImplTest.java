
package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.StackStandingDAO;
import com.biperf.core.dao.throwdown.StackStandingNodeDAO;
import com.biperf.core.dao.throwdown.StackStandingParticipantDAO;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.ApplicationContextFactory;

public class StackStandingNodeDAOImplTest extends BaseDAOTest
{

  public static StackStandingNode buildStackStandingNode()
  {
    StackStandingNode stackStandingNode = new StackStandingNode();
    Node newNode = NodeDAOImplTest.buildUniqueNode( buildUniqueString() );
    newNode = getNodeDAO().saveNode( newNode );
    flushAndClearSession();
    StackStanding stackStanding = new StackStanding();
    ThrowdownPromotion tdPromo1 = new ThrowdownPromotion();
    tdPromo1 = PromotionDAOImplTest.buildThrowdownPromotion( buildUniqueString() );
    getPromotionDAO().save( tdPromo1 );
    flushAndClearSession();

    stackStanding.setPromotion( tdPromo1 );
    stackStanding.setPayoutsIssued( false );
    stackStanding.setRoundNumber( 1 );
    stackStanding.setGuid( buildUniqueString() );
    getStackStandingDAO().save( stackStanding );
    flushAndClearSession();

    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    getParticipantDAO().saveParticipant( participant );
    flushAndClearSession();

    StackStandingParticipant stackStandingParticipant = new StackStandingParticipant();
    stackStandingParticipant.setParticipant( participant );
    stackStandingParticipant.setStackStandingNode( stackStandingNode );
    stackStandingParticipant.setStackStandingFactor( new BigDecimal( 55 ) );
    stackStandingNode.addStackStandingParticipant( stackStandingParticipant );
    stackStandingNode.setNode( newNode );
    stackStandingNode.setStackStanding( stackStanding );
    getStackStandingNodeDAO().saveStackStandingNode( stackStandingNode );

    getStackStandingParticipantDAO().saveStackStandingParticipant( stackStandingParticipant );
    flushAndClearSession();

    return stackStandingNode;
  }

  public void testSave()
  {
    StackStandingNode stackStandingNode = buildStackStandingNode();
    getStackStandingNodeDAO().saveStackStandingNode( stackStandingNode );
    StackStandingNode expected = getStackStandingNodeDAO().getStackStandingNode( stackStandingNode.getId() );
    assertEquals( "Actual StackStandingNode is equal to what was expected", expected, stackStandingNode );
  }

  public void testGetStackStandingNode()
  {
    StackStandingNode stackStandingNode = buildStackStandingNode();
    getStackStandingNodeDAO().saveStackStandingNode( stackStandingNode );
    StackStanding stackStanding = stackStandingNode.getStackStanding();
    Node node = stackStandingNode.getNode();
    StackStandingNode expected = getStackStandingNodeDAO().getStackStandingByNode( stackStanding.getId(), node.getId() );
    assertSame( "Actual StackStandingNode is equal to what was expected", stackStandingNode, expected );
  }

  /**
   * Get the StackStandingNodeDAO.
   * 
   * @return StackStandingNodeDAO
   */
  private static StackStandingNodeDAO getStackStandingNodeDAO()
  {
    return (StackStandingNodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "stackStandingNodeDAO" );
  }

  /**
   * Get the PromotionDAO.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }

  /**
   * Get the StackStandingDAO.
   * 
   * @return StackStandingDAO
   */
  private static StackStandingDAO getStackStandingDAO()
  {
    return (StackStandingDAO)ApplicationContextFactory.getApplicationContext().getBean( "stackStandingDAO" );
  }

  /**
   * Get the NodeDAO.
   * 
   * @return NodeDAO
   */
  private static NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

  /**
   * Get the StackStandingDAO.
   * 
   * @return StackStandingDAO
   */
  private static StackStandingParticipantDAO getStackStandingParticipantDAO()
  {
    return (StackStandingParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "stackStandingParticipantDAO" );
  }

  /**
   * Get the ParticipantDAO.
   * 
   * @return ParticipantDAO
   */
  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "participantDAO" );
  }

}

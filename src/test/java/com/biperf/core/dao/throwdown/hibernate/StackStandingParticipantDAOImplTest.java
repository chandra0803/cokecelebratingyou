
package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.throwdown.StackStandingParticipantDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.utils.ApplicationContextFactory;

public class StackStandingParticipantDAOImplTest extends BaseDAOTest
{

  public static StackStandingParticipant buildStckStandingParticipantWithAllDetails()
  {
    StackStandingParticipant stackStandingParticipant = new StackStandingParticipant();
    StackStandingNode stackStandingNode = StackStandingNodeDAOImplTest.buildStackStandingNode();
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( buildUniqueString() );
    getParticipantDAO().saveParticipant( participant );
    stackStandingParticipant.setStackStandingNode( stackStandingNode );
    stackStandingParticipant.setParticipant( participant );
    stackStandingParticipant.setPayout( new Long( 55 ) );
    stackStandingParticipant.setPayoutsIssued( false );
    stackStandingParticipant.setStackStandingFactor( new BigDecimal( 5 ) );
    stackStandingParticipant.setStanding( 5 );
    stackStandingParticipant.setTied( false );
    getStackStandingParticipantDAO().saveStackStandingParticipant( stackStandingParticipant );
    return stackStandingParticipant;
  }

  public void testGetStackStandingParticipant()
  {
    StackStandingParticipant actualStackStandingParticipant = buildStckStandingParticipantWithAllDetails();
    StackStandingParticipant expectedStackStandingParticipant = getStackStandingParticipantDAO().getStackStandingParticipant( actualStackStandingParticipant.getId() );
    assertEquals( "Actual StackStandingParticipant is equal to what was expected", actualStackStandingParticipant, expectedStackStandingParticipant );
  }

  public void testGetStackStandingParticipantForSpecificUser()
  {
    StackStandingParticipant actualStackStandingParticipant = buildStckStandingParticipantWithAllDetails();
    Participant participant = actualStackStandingParticipant.getParticipant();
    StackStandingNode stackStandingNode = actualStackStandingParticipant.getStackStandingNode();

    StackStandingParticipant expectedStackStandingParticipant = getStackStandingParticipantDAO().getStackStandingParticipant( stackStandingNode.getId(), participant.getId() );
    assertEquals( "Actual StackStandingParticipant is equal to what was expected", actualStackStandingParticipant, expectedStackStandingParticipant );
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

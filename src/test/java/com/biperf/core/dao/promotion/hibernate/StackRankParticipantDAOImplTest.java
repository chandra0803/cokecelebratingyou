/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.StackRankDAO;
import com.biperf.core.dao.promotion.StackRankNodeDAO;
import com.biperf.core.dao.promotion.StackRankParticipantDAO;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.GuidUtils;

public class StackRankParticipantDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private static final Log log = LogFactory.getLog( StackRankParticipantDAOImplTest.class );
  private static final StackRankParticipantDAO stackRankParticipantDao = getStackRankParticipantDao();
  private static final StackRankNodeDAO stackRankNodeDao = getStackRankNodeDao();
  private static final StackRankDAO stackRankDao = getStackRankDao();
  private static final PromotionDAO promotionDao = getPromotionDao();
  private static final ParticipantDAO participantDao = getParticipantDao();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests getting a list of stack rank participants based on promotion.
   */
  public void testGetStackRankParticipantList()
  {
    StackRank stackRank = buildStackRank( getUniqueString() );

    // Insert the stack rank and its association
    StackRank savedStackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( savedStackRank.getId() );

    Set stackRankNodes = stackRank.getStackRankNodes();
    for ( Iterator iter = stackRankNodes.iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      for ( Iterator iter2 = stackRankNode.getStackRankParticipants().iterator(); iter2.hasNext(); )
      {
        StackRankParticipant stackRankParticipant = (StackRankParticipant)iter2.next();
        stackRankParticipantDao.saveStackRankParticipant( stackRankParticipant );
      }
    }

    flushAndClearSession();

    // Select the stack rank participants based on a promotion and stack rank state
    StackRankParticipantQueryConstraint queryConstraint = new StackRankParticipantQueryConstraint();

    queryConstraint.setPromotionIdsIncluded( new Long[] { savedStackRank.getPromotion().getId() } );
    queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) } );

    List stackRankPaxList = stackRankParticipantDao.getStackRankParticipantList( queryConstraint );

    log.debug( "By Promotion Id and Stack Rank State - stackRankPaxList: " + stackRankPaxList );

    assertTrue( stackRankPaxList.size() > 0 );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a new {@link StackRankParticipant} object.
   * 
   * @param uniqueString a unique string.
   * @param rank a rank the participant holds.
   * @return a new {@link StackRankParticipant} object.
   */
  public static StackRankParticipant buildStackRankParticipant( String uniqueString, int rank )
  {
    StackRankParticipant stackRankParticipant = new StackRankParticipant();

    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    participant = participantDao.saveParticipant( participant );
    stackRankParticipant.setParticipant( participant );

    stackRankParticipant.setStackRankFactor( 10 );
    stackRankParticipant.setRank( rank );
    stackRankParticipant.setTied( false );
    stackRankParticipant.setPayout( new Long( 2 ) );

    return stackRankParticipant;
  }

  /**
   * Returns a new {@link StackRank} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link StackRank} object.
   */
  public static StackRank buildStackRank( String uniqueString )
  {
    StackRank stackRank = new StackRank();

    stackRank.setGuid( GuidUtils.generateGuid() );
    stackRank.setState( StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) );
    stackRank.setStartDate( new Date() );
    stackRank.setEndDate( new Date() );
    stackRank.setCalculatePayout( false );

    // Add stack rank nodes to the stack rank.
    stackRank.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "1" ) );
    stackRank.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "2" ) );

    // Attach the stack rank to a promotion.
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString );
    promotion = (ProductClaimPromotion)promotionDao.save( promotion );
    stackRank.setPromotion( promotion );

    return stackRank;
  }

  /**
   * Returns a reference to the stack rank participant DAO.
   * 
   * @return a reference to the stack rank participant DAO.
   */
  private static StackRankParticipantDAO getStackRankParticipantDao()
  {
    return (StackRankParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( StackRankParticipantDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the promotion DAO.
   * 
   * @return a reference to the promotion DAO.
   */
  private static PromotionDAO getPromotionDao()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the stack rank DAO.
   * 
   * @return a reference to the stack rank DAO.
   */
  private static StackRankDAO getStackRankDao()
  {
    return (StackRankDAO)ApplicationContextFactory.getApplicationContext().getBean( StackRankDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the stack rank node DAO.
   * 
   * @return a reference to the stack rank node DAO.
   */
  private static StackRankNodeDAO getStackRankNodeDao()
  {
    return (StackRankNodeDAO)ApplicationContextFactory.getApplicationContext().getBean( StackRankNodeDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the participant DAO.
   * 
   * @return a reference to the participant DAO.
   */
  private static ParticipantDAO getParticipantDao()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( ParticipantDAO.BEAN_NAME );
  }

}

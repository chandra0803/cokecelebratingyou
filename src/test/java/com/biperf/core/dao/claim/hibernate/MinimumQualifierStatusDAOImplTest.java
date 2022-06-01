/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Map;

import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * ClaimDAOImplTest.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MinimumQualifierStatusDAOImplTest extends BaseDAOTest
{

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  NodeDAO nodeDao = getNodeDao();
  ParticipantDAO participantDao = getParticipantDao();
  PromotionDAO promotionDao = getPromotionDao();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests saving and getting a status by the ID.
   */
  public void testSaveUpdateAndGetById()
  {

    // Build a unique string to be used during testing.
    String uniqueString = String.valueOf( System.currentTimeMillis() % 5503032 );

    // Create a promotion.
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString + "promo" );

    PromotionPayoutGroup promotionPayoutGroup = PromotionPayoutDAOImplTest.buildPromotionPayoutGroup( false );
    promotion.addPromotionPayoutGroup( promotionPayoutGroup );

    promotionDao.save( promotion );

    // Create a claim submitter.
    Participant submitter = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    participantDao.saveParticipant( submitter );

    // Test saving a status.
    MinimumQualifierStatus minimumQualifierStatus = new MinimumQualifierStatus();
    minimumQualifierStatus.setPromotionPayoutGroup( promotionPayoutGroup );
    minimumQualifierStatus.setSubmitter( submitter );

    getMinimumQualifierStatusDAO().save( minimumQualifierStatus );

    flushAndClearSession();

    assertNotNull( "Id is null.", minimumQualifierStatus.getId() );

    // Reload
    MinimumQualifierStatus savedStatus = getMinimumQualifierStatusDAO().getMinimumQualifierStatus( minimumQualifierStatus.getId() );

    assertEquals( minimumQualifierStatus, savedStatus );

    // update
    int expectedNewCompletedQuantity = savedStatus.getCompletedQuantity() + 1;
    savedStatus.setCompletedQuantity( expectedNewCompletedQuantity );

    getMinimumQualifierStatusDAO().save( savedStatus );

    flushAndClearSession();

    savedStatus = getMinimumQualifierStatusDAO().getMinimumQualifierStatus( minimumQualifierStatus.getId() );

    assertEquals( expectedNewCompletedQuantity, savedStatus.getCompletedQuantity() );

    flushAndClearSession();

    // Load by query
    Map minQualifierStatusByPromoPayoutGroup = getMinimumQualifierStatusDAO().getMinQualifierStatusByPromoPayoutGroup( submitter.getId(), promotion.getId() );
    assertEquals( 1, minQualifierStatusByPromoPayoutGroup.size() );
    assertEquals( minimumQualifierStatus, minQualifierStatusByPromoPayoutGroup.get( promotionPayoutGroup ) );
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the node DAO.
   * 
   * @return a reference to the node DAO.
   */
  private static NodeDAO getNodeDao()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( NodeDAO.BEAN_NAME );
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

  /**
   * Returns a reference to the promotion DAO.
   * 
   * @return a reference to the promotion DAO.
   */
  private static PromotionDAO getPromotionDao()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  private static MinimumQualifierStatusDAO getMinimumQualifierStatusDAO()
  {
    return (MinimumQualifierStatusDAO)ApplicationContextFactory.getApplicationContext().getBean( MinimumQualifierStatusDAO.BEAN_NAME );
  }

}
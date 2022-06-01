/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/claim/hibernate/JournalClaimQueryConstraintTest.java,v $
 */

package com.biperf.core.dao.claim.hibernate;

import java.util.Date;
import java.util.List;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.utils.ApplicationContextFactory;

public class JournalClaimQueryConstraintTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  private static final Date START_DATE = new Date();
  private static final Date END_DATE = new Date();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  public void testJournalClaimQueryConstraint()
  {
    // Setup loggers.
    // Logger.getLogger("org.hibernate.engine.query.HQLQueryPlan").setLevel(
    // Level.toLevel("DEBUG"));
    // Logger.getLogger("org.hibernate.engine.QueryParameters").setLevel( Level.toLevel("DEBUG"));
    // Logger.getLogger("org.hibernate.SQL").setLevel( Level.toLevel("DEBUG"));

    // Build and save domain objects.
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    getUserDao().saveUser( participant );

    Promotion promotion = PromotionDAOImplTest.buildNominationPromotion( getUniqueString() );
    getPromotionDao().save( promotion );

    // Build the query constraint.
    JournalClaimQueryConstraint queryConstraint = new JournalClaimQueryConstraint( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ), null );

    queryConstraint.setJournalStatusType( JournalStatusType.POST );
    queryConstraint.setRecipientId( participant.getId() );
    queryConstraint.setPromotionId( promotion.getId() );
    queryConstraint.setApprovalStartDate( START_DATE );
    queryConstraint.setApprovalEndDate( END_DATE );

    // Build the association request collection.
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    // Get the claim list.
    List claimList = getClaimDao().getClaimList( queryConstraint, associationRequestCollection );

    assertNotNull( claimList );
    assertTrue( claimList.size() == 0 );
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the claim DAO.
   *
   * @return a reference to the claim DAO.
   */
  private static ClaimDAO getClaimDao()
  {
    return (ClaimDAO)ApplicationContextFactory.getApplicationContext().getBean( ClaimDAO.BEAN_NAME );
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
   * Returns a reference to the user DAO.
   *
   * @return a reference to the user DAO.
   */
  private static UserDAO getUserDao()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( UserDAO.BEAN_NAME );
  }
}

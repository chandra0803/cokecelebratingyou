/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/audit/hibernate/PayoutCalculationAuditDAOImplTest.java,v $
 */

package com.biperf.core.dao.audit.hibernate;

import java.util.List;

import com.biperf.core.dao.audit.PayoutCalculationAuditDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.ClaimDAOImplTest;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.journal.hibernate.JournalDAOImplTest;
import com.biperf.core.dao.promotion.PromotionPayoutDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.audit.SalesPayoutCalculationAudit;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.utils.ApplicationContextFactory;

/*
 * PayoutCalculationAuditDAOImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 18, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class PayoutCalculationAuditDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Builds a {@link SalesPayoutCalculationAudit} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link SalesPayoutCalculationAudit} object.
   */
  public static SalesPayoutCalculationAudit buildSalesPayoutCalculationAudit( String uniqueString )
  {
    // Create the claim.
    Claim claim = ClaimDAOImplTest.buildProductClaim( uniqueString );
    claim = getClaimDao().saveClaim( claim );

    // Create the promotion payout group.
    PromotionPayoutGroup promotionPayoutGroup = PromotionPayoutDAOImplTest.buildPromotionPayoutGroup();
    Promotion promotion = claim.getPromotion();
    if ( promotion.isProductClaimPromotion() )
    {
      ( (ProductClaimPromotion)promotion ).addPromotionPayoutGroup( promotionPayoutGroup );
      promotionPayoutGroup = getPromotionPayoutDao().saveGroup( promotionPayoutGroup );
    }

    // Get the participant.
    Participant participant = claim.getSubmitter();

    // Create the journal entry.
    Journal journal = JournalDAOImplTest.buildAndSaveJournal( uniqueString );
    journal = getJournalDao().saveJournalEntry( journal );

    // Create the payout calculation audit record.
    SalesPayoutCalculationAudit auditRecord = new SalesPayoutCalculationAudit();

    auditRecord.setClaim( claim );
    auditRecord.setPromotionPayoutGroup( promotionPayoutGroup );
    auditRecord.setParticipant( participant );
    auditRecord.setJournal( journal );
    auditRecord.setReasonType( PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.SALES_SUCCESS ) );
    auditRecord.setReasonText( "Promotion payout calculation succeeded." );

    return auditRecord;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests saving a {@link SalesPayoutCalculationAudit} object and then loading it by ID.
   */
  public void testSaveAndGetById()
  {
    PayoutCalculationAuditDAO payoutCalculationAuditDao = getPayoutCalculationAuditDao();

    PayoutCalculationAudit expectedAudit = buildSalesPayoutCalculationAudit( getUniqueString() );
    payoutCalculationAuditDao.save( expectedAudit );
    flushAndClearSession();

    PayoutCalculationAudit actualAudit = payoutCalculationAuditDao.getPayoutCalculationAuditById( expectedAudit.getId() );
    assertTrue( expectedAudit.equals( actualAudit ) );
  }

  /**
   * Tests saving a {@link SalesPayoutCalculationAudit} object and then loading it by ID.
   */
  public void testGetPayoutCalculationAuditsByClaimIdAndParticipantId()
  {
    PayoutCalculationAuditDAO payoutCalculationAuditDao = getPayoutCalculationAuditDao();
    String claimAndParticipantId = new Long( "1" ).toString();

    SalesPayoutCalculationAudit salesPayoutCalculationAudit = buildSalesPayoutCalculationAudit( claimAndParticipantId );
    payoutCalculationAuditDao.save( salesPayoutCalculationAudit );

    flushAndClearSession();

    Long claimId = salesPayoutCalculationAudit.getClaim().getId();
    Long participantId = salesPayoutCalculationAudit.getParticipant().getId();

    List payoutCalculationAuditRecords = payoutCalculationAuditDao.getPayoutCalculationAuditsByClaimIdAndParticipantId( claimId, participantId );

    assertTrue( payoutCalculationAuditRecords.size() >= 1 );
  }

  /**
   * Tests loading all {@link SalesPayoutCalculationAudit} objects.
   */
  public void testGetAll()
  {
    PayoutCalculationAuditDAO payoutCalculationAuditDao = getPayoutCalculationAuditDao();

    PayoutCalculationAudit auditRecord1 = buildSalesPayoutCalculationAudit( getUniqueString() );
    payoutCalculationAuditDao.save( auditRecord1 );

    PayoutCalculationAudit auditRecord2 = buildSalesPayoutCalculationAudit( getUniqueString() );
    payoutCalculationAuditDao.save( auditRecord2 );

    flushAndClearSession();

    List payoutCalculationAuditRecords = payoutCalculationAuditDao.getAll();
    assertTrue( payoutCalculationAuditRecords.size() >= 2 );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
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
   * Returns a reference to the journal DAO.
   * 
   * @return a reference to the journal DAO.
   */
  private static JournalDAO getJournalDao()
  {
    return (JournalDAO)ApplicationContextFactory.getApplicationContext().getBean( JournalDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the payout calculation audit DAO.
   * 
   * @return a reference to the payout calculation audit DAO.
   */
  private static PayoutCalculationAuditDAO getPayoutCalculationAuditDao()
  {
    return (PayoutCalculationAuditDAO)ApplicationContextFactory.getApplicationContext().getBean( PayoutCalculationAuditDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the promotion payout DAO.
   * 
   * @return a reference to the promotion payout DAO.
   */
  private static PromotionPayoutDAO getPromotionPayoutDao()
  {
    return (PromotionPayoutDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionPayoutDAO.BEAN_NAME );
  }

}

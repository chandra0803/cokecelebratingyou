/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/hibernate/StackStandingDAOImplTest.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.StackStandingDAO;
import com.biperf.core.dao.throwdown.StackStandingNodeDAO;
import com.biperf.core.dao.throwdown.StackStandingParticipantDAO;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.ApplicationContextFactory;

public class StackStandingDAOImplTest extends BaseDAOTest
{

  public static StackStanding buildStackStanding( String promotionName )
  {
    StackStanding stackStanding = new StackStanding();
    stackStanding.setActive( true );
    stackStanding.setGuid( buildUniqueString() );
    stackStanding.setPayoutsIssued( false );
    stackStanding.setRoundNumber( 1 );
    ThrowdownPromotion promotion = new ThrowdownPromotion();
    promotion = PromotionDAOImplTest.buildThrowdownPromotion( promotionName );
    getPromotionDAO().save( promotion );
    stackStanding.setPromotion( promotion );
    return stackStanding;
  }

  public void testSaveStackStanding()
  {
    StackStanding actualStackStanding = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( actualStackStanding );
    StackStanding expectedStackStanding = getStackStandingDAO().get( actualStackStanding.getId() );
    assertEquals( "Actual StackStanding is same as Expected StackStanding", expectedStackStanding, actualStackStanding );
  }

  public void testDelete()
  {
    /*
     * StackStanding stackStanding = buildStackStanding( buildUniqueString() );
     * getStackStandingDAO().save( stackStanding ); StackStanding actualStackStanding =
     * getStackStandingDAO().get( stackStanding.getId() ); getStackStandingDAO().delete(
     * stackStanding ); StackStanding expectedStackStanding = getStackStandingDAO().get(
     * stackStanding.getId() ); assertTrue( "StackStanding deleted successfully",
     * expectedStackStanding == null ); assertNotSame(
     * "Second Confirmation that StackStanding is deleted", expectedStackStanding,
     * actualStackStanding );
     */
  }

  public void testGetAll()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding );
    StackStanding stackStanding1 = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding1 );
    StackStanding stackStanding2 = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding2 );
    List<StackStanding> stackStandingList = getStackStandingDAO().getAll();
    assertTrue( "StackStanding Should Be fetch all", stackStandingList.size() >= 3 );
  }

  public void testGetRoundRankings()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding );
    StackStanding stackStanding1 = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding1 );
    StackStanding stackStanding2 = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding2 );
    List<StackStanding> roundRankingsList = getStackStandingDAO().getRankings();
    assertTrue( "StackStanding Should Be fetch all three", roundRankingsList.size() >= 3 );

  }

  public void testGetRoundRankingsForPromotion()
  {
    StackStanding actualStackStanding = buildStackStanding( buildUniqueString() );
    StackStanding savedStackStanding = getStackStandingDAO().save( actualStackStanding );
    List<StackStanding> roundRankingsList = getStackStandingDAO().getRankingsForPromotion( savedStackStanding.getPromotion().getId() );
    assertTrue( "StackStanding Should Be fetch all ", roundRankingsList.size() > 0 );
  }

  public void testGetRoundRankingForPromotionAndRound()
  {
    StackStanding actualStackStanding = buildStackStanding( buildUniqueString() );
    StackStanding savedStackStanding = getStackStandingDAO().save( actualStackStanding );
    StackStanding round1 = getStackStandingDAO().getRankingForPromotionAndRound( savedStackStanding.getPromotion().getId(), 1 );
    StackStanding actualStackStanding1 = buildStackStanding( buildUniqueString() );
    actualStackStanding1.setRoundNumber( 2 );
    StackStanding savedStackStanding1 = getStackStandingDAO().save( actualStackStanding1 );
    StackStanding round2 = getStackStandingDAO().getRankingForPromotionAndRound( savedStackStanding1.getPromotion().getId(), 2 );
    assertNotSame( "StackStanding is not same for round1 and round2", round2, round1 );
  }

  public void testGetApprovedRoundRankings()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding );
    StackStanding stackStanding1 = buildStackStanding( buildUniqueString() );
    stackStanding1.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding1 );
    StackStanding stackStanding2 = buildStackStanding( buildUniqueString() );
    stackStanding2.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding2 );
    List<StackStanding> approvedRankings = getStackStandingDAO().getApprovedRankings();
    List<StackStanding> allStandings = getStackStandingDAO().getAll();
    assertTrue( "Approved round rankings  Should Be fetch ", approvedRankings.size() == 2 );
    assertTrue( "StackStanding Should Be fetch all", allStandings.size() >= 3 );
  }

  public void testGetApprovedRoundRankingsForPromotion()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    getStackStandingDAO().save( stackStanding );
    StackStanding stackStanding1 = buildStackStanding( buildUniqueString() );
    stackStanding1.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding1 );
    StackStanding stackStanding2 = buildStackStanding( buildUniqueString() );
    stackStanding2.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding2 );
    List<StackStanding> approvedRankings = getStackStandingDAO().getApprovedRankingsForPromotion( stackStanding2.getPromotion().getId() );
    List<StackStanding> allStandings = getStackStandingDAO().getAll();
    assertTrue( "Approved round rankings is not Empty for promo ", approvedRankings.size() > 0 );
    assertTrue( "StackStanding Should Be fetch all", allStandings.size() >= 3 );
  }

  public void testGetApprovedRoundRankingForPromotionAndRound()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    StackStanding unApprovedRankings = getStackStandingDAO().save( stackStanding );
    StackStanding stackStanding1 = buildStackStanding( buildUniqueString() );
    stackStanding1.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding1 );
    StackStanding stackStanding2 = buildStackStanding( buildUniqueString() );
    stackStanding2.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding2 );
    StackStanding approvedRankings = getStackStandingDAO().getApprovedRankingForPromotionAndRound( stackStanding2.getPromotion().getId(), stackStanding.getRoundNumber() );

    assertNotSame( "Approved and unApproved rankings are not same ", approvedRankings, unApprovedRankings );
  }

  public void testGetUnapprovedRoundRankings()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    stackStanding.setPayoutsIssued( false );
    getStackStandingDAO().save( stackStanding );
    List<StackStanding> unApprovedRankings = getStackStandingDAO().getUnapprovedRankings();
    assertTrue( "unApprovedRankings are not empty", unApprovedRankings.size() > 0 );
  }

  public void testGetUnapprovedRoundRankingsForPromotion()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    stackStanding.setPayoutsIssued( false );
    StackStanding actualStackStanding = getStackStandingDAO().save( stackStanding );
    List<StackStanding> unApprovedRankings = getStackStandingDAO().getUnapprovedRankingsForPromotion( stackStanding.getPromotion().getId() );
    StackStanding expectedStackStanding = unApprovedRankings.get( 0 );
    assertEquals( "Actual result is equal to what was expected", actualStackStanding, expectedStackStanding );
  }

  public void testGetUnapprovedRoundRankingForPromotionAndRound()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    stackStanding.setPayoutsIssued( false );
    StackStanding actualStackStanding = getStackStandingDAO().save( stackStanding );
    StackStanding expectedStackStanding = getStackStandingDAO().getUnapprovedRankingForPromotionAndRound( stackStanding.getPromotion().getId(), stackStanding.getRoundNumber() );
    assertEquals( "Actual result is equal to what was expected", actualStackStanding, expectedStackStanding );
  }

  public void testIsAnyPaxPaidOutForRanking()
  {
    StackStanding stackStanding = buildStackStanding( buildUniqueString() );
    stackStanding.setPayoutsIssued( true );
    getStackStandingDAO().save( stackStanding );
    boolean paidForRanking = getStackStandingDAO().isAnyPaxPaidOutForRanking( stackStanding.getId() );
    assertFalse( "Payout Issued", paidForRanking );
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
   * Get the PromotionDAO.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }

  /**
   * Get the StackStandingParticipantDAO.
   * 
   * @return StackStandingParticipantDAO
   */
  private static StackStandingParticipantDAO getStackStandingParticipantDAO()
  {
    return (StackStandingParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "stackStandingParticipantDAO" );
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
}

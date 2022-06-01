/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/strategy/impl/ProductClaimWinnersListStrategyTest.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.SweepstakesClaimEligibilityType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.service.claim.ClaimService;

import junit.framework.TestCase;

/*
 * ProductClaimWinnersListStrategyTest.
 * 
 *
 */

public class ProductClaimWinnersListStrategyTest extends TestCase
{

  private ProductClaimWinnersListStrategy prodWinnersListStrategy = new ProductClaimWinnersListStrategy();

  private MockPickListFactory mockFactory = new MockPickListFactory();
  private ClaimService claimServiceMock;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    claimServiceMock = EasyMock.createMock( ClaimService.class );
    prodWinnersListStrategy.setClaimService( claimServiceMock );
  }

  /**
   * Tests Submitter only winner list
   */
  public void testBuildWinnersListSubmittersOnly()
  {

    // SweepstakesMultipleAwardsType
    // PERIOD_CODE = "period";
    // MULTIPLE_CODE = "multiple";
    // DRAWING_CODE = "drawing";
    // PromotionSweepstakesWinnersTypes
    // SPECIFIC_CODE = "specific";
    // PERCENTAGE_CODE = "percentage";

    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Participant part3 = buildParticipant( 3 );
    Participant part4 = buildParticipant( 4 );

    // TEST 1 - TEST SUBMITTERS ONLY and ALL_CLOSED_CLAIMS
    Promotion promotion = buildProductClaimPromotion( 1,
                                                      SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE,
                                                      SweepstakesMultipleAwardsType.PERIOD_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS,
                                                      2,
                                                      0 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    int giverWinnerReplacementTotal = 0;
    int receiverWinnerReplacementTotal = 0;
    // build claims
    List<Claim> claims = new LinkedList<>();

    claims.add( buildClaim( 1, part1 ) );
    claims.add( buildClaim( 2, part2 ) );
    claims.add( buildClaim( 3, part3 ) );
    claims.add( buildClaim( 4, part4 ) );

    EasyMock.expect( claimServiceMock.getProductClaimClaimsList( promotion.getId(), SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS, promotionSweepstake ) ).andReturn( claims );

    EasyMock.replay( claimServiceMock );

    List actualWinners = prodWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );
    TestCase.assertEquals( 2, actualWinners.size() );

    EasyMock.reset( claimServiceMock );

  }

  /**
   * Tests Team Members only winner list
   */
  public void testBuildWinnersListTeamMembersOnly()
  {

    // TEST 2 - TEST TeamMembers ONLY
    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Promotion promotion = buildProductClaimPromotion( 2,
                                                      SweepstakesWinnerEligibilityType.TEAM_MEMBERS_ONLY_CODE,
                                                      SweepstakesMultipleAwardsType.PERIOD_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS,
                                                      0,
                                                      3 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();

    int giverWinnerReplacementTotal = 0;
    int receiverWinnerReplacementTotal = 0;

    ProductClaimParticipant prodParticipant6 = buildProductClaimParticipant( 6 );
    ProductClaimParticipant prodParticipant7 = buildProductClaimParticipant( 7 );
    ProductClaimParticipant prodParticipant8 = buildProductClaimParticipant( 8 );
    ProductClaimParticipant prodParticipant9 = buildProductClaimParticipant( 9 );
    Set<ProductClaimParticipant> claimParticipants1 = new LinkedHashSet<>();
    Set<ProductClaimParticipant> claimParticipants2 = new LinkedHashSet<>();
    claimParticipants1.add( prodParticipant6 );
    claimParticipants1.add( prodParticipant7 );
    claimParticipants2.add( prodParticipant8 );
    claimParticipants2.add( prodParticipant9 );
    // build claims

    List<Claim> claims = new LinkedList<>();
    claims.add( buildClaimWithParticpants( 1, part1, claimParticipants1 ) );
    claims.add( buildClaimWithParticpants( 2, part2, claimParticipants2 ) );

    EasyMock.expect( claimServiceMock.getProductClaimClaimsList( promotion.getId(), SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS, promotionSweepstake ) ).andReturn( claims );

    EasyMock.replay( claimServiceMock );

    List actualWinners = prodWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 3, actualWinners.size() );

    EasyMock.reset( claimServiceMock );

  }

  /**
   * Tests Submitter and team member winner list
   */
  public void testBuildWinnersListSubmittersAndTeamMembersCombined()
  {

    // TEST 3 - TEST GIVERS AND RECEIVERS COMBINED

    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Participant part3 = buildParticipant( 3 );
    Participant part4 = buildParticipant( 4 );

    Promotion promotion = buildProductClaimPromotion( 3,
                                                      SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_COMBINED_CODE,
                                                      SweepstakesMultipleAwardsType.PERIOD_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS,
                                                      5,
                                                      0 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    int giverWinnerReplacementTotal = 0;
    int receiverWinnerReplacementTotal = 0;

    ProductClaimParticipant prodParticipant6 = buildProductClaimParticipant( 6 );
    ProductClaimParticipant prodParticipant7 = buildProductClaimParticipant( 7 );
    ProductClaimParticipant prodParticipant8 = buildProductClaimParticipant( 8 );
    ProductClaimParticipant prodParticipant9 = buildProductClaimParticipant( 9 );
    Set<ProductClaimParticipant> claimParticipants1 = new LinkedHashSet<>();
    Set<ProductClaimParticipant> claimParticipants2 = new LinkedHashSet<>();
    claimParticipants1.add( prodParticipant6 );
    claimParticipants1.add( prodParticipant7 );
    claimParticipants2.add( prodParticipant8 );
    claimParticipants2.add( prodParticipant9 );
    // build claims
    List<Claim> claims = new LinkedList<>();
    claims.add( buildClaimWithParticpants( 1, part1, claimParticipants1 ) );
    claims.add( buildClaimWithParticpants( 2, part2, claimParticipants2 ) );
    claims.add( buildClaim( 3, part3 ) );
    claims.add( buildClaim( 4, part4 ) );

    EasyMock.expect( claimServiceMock.getProductClaimClaimsList( promotion.getId(), SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS, promotionSweepstake ) ).andReturn( claims );

    EasyMock.replay( claimServiceMock );

    List actualWinners = prodWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 5, actualWinners.size() );

    EasyMock.reset( claimServiceMock );

  }

  /**
   * Tests Submitter and Teammembers separate winner list
   */
  public void testBuildWinnersListSubmittersAndTeamMembersSeparate()
  {
    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Participant part3 = buildParticipant( 3 );
    Participant part4 = buildParticipant( 4 );
    // TEST 4 - TEST GIVERS AND RECEIVERS SEPERATE
    Promotion promotion = buildProductClaimPromotion( 4,
                                                      SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE,
                                                      SweepstakesMultipleAwardsType.PERIOD_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS,
                                                      2,
                                                      3 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    int giverWinnerReplacementTotal = 0;
    int receiverWinnerReplacementTotal = 0;

    ProductClaimParticipant prodParticipant6 = buildProductClaimParticipant( 6 );
    ProductClaimParticipant prodParticipant7 = buildProductClaimParticipant( 7 );
    ProductClaimParticipant prodParticipant8 = buildProductClaimParticipant( 8 );
    ProductClaimParticipant prodParticipant9 = buildProductClaimParticipant( 9 );
    Set<ProductClaimParticipant> claimParticipants1 = new LinkedHashSet<>();
    Set<ProductClaimParticipant> claimParticipants2 = new LinkedHashSet<>();
    claimParticipants1.add( prodParticipant6 );
    claimParticipants1.add( prodParticipant7 );
    claimParticipants2.add( prodParticipant8 );
    claimParticipants2.add( prodParticipant9 );
    // build claims
    List<Claim> claims = new LinkedList<>();
    claims.add( buildClaimWithParticpants( 1, part1, claimParticipants1 ) );
    claims.add( buildClaimWithParticpants( 2, part2, claimParticipants2 ) );
    claims.add( buildClaim( 3, part3 ) );
    claims.add( buildClaim( 4, part4 ) );

    EasyMock.expect( claimServiceMock.getProductClaimClaimsList( promotion.getId(), SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS, promotionSweepstake ) ).andReturn( claims );
    EasyMock.replay( claimServiceMock );

    List actualWinners = prodWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 5, actualWinners.size() );

    EasyMock.reset( claimServiceMock );
  }

  /**
   * Tests Submitter and Teammembers combined percenatge winner list
   */
  public void testBuildWinnersListSubmittersAndTeamMembersCombinedPercentage()
  {
    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Participant part3 = buildParticipant( 3 );
    Participant part4 = buildParticipant( 4 );
    // TEST 5 - TEST PERCENTAGE
    Promotion promotion = buildProductClaimPromotion( 1,
                                                      SweepstakesWinnerEligibilityType.SUBMITTERS_AND_TEAM_MEMBERS_SEPARATE_CODE,
                                                      SweepstakesMultipleAwardsType.PERIOD_CODE,
                                                      SweepstakesWinnersType.PERCENTAGE_CODE,
                                                      SweepstakesWinnersType.PERCENTAGE_CODE,
                                                      SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS,
                                                      50,
                                                      25 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    int giverWinnerReplacementTotal = 0;
    int receiverWinnerReplacementTotal = 0;

    ProductClaimParticipant prodParticipant6 = buildProductClaimParticipant( 6 );
    ProductClaimParticipant prodParticipant7 = buildProductClaimParticipant( 7 );
    ProductClaimParticipant prodParticipant8 = buildProductClaimParticipant( 8 );
    ProductClaimParticipant prodParticipant9 = buildProductClaimParticipant( 9 );
    Set<ProductClaimParticipant> claimParticipants1 = new LinkedHashSet<>();
    Set<ProductClaimParticipant> claimParticipants2 = new LinkedHashSet<>();
    claimParticipants1.add( prodParticipant6 );
    claimParticipants1.add( prodParticipant7 );
    claimParticipants2.add( prodParticipant8 );
    claimParticipants2.add( prodParticipant9 );
    // build claims
    List<Claim> claims = new LinkedList<>();
    claims.add( buildClaimWithParticpants( 1, part1, claimParticipants1 ) );
    claims.add( buildClaimWithParticpants( 2, part2, claimParticipants2 ) );
    claims.add( buildClaim( 3, part3 ) );
    claims.add( buildClaim( 4, part4 ) );
    // build activities

    EasyMock.expect( claimServiceMock.getProductClaimClaimsList( promotion.getId(), SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS, promotionSweepstake ) ).andReturn( claims );
    EasyMock.replay( claimServiceMock );

    List actualWinners = prodWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    assertEquals( 3, actualWinners.size() );

    EasyMock.reset( claimServiceMock );
  }

  /**
   * Tests Submitter winner list testmultiple awards for same
   */
  public void testBuildWinnersListSubmittersMultiple()
  {
    // build a set of participants for use
    Participant part1 = buildParticipant( 1 );
    Participant part2 = buildParticipant( 2 );
    Participant part3 = buildParticipant( 3 );
    // TEST - GIVER, MULTIPLE, GIVER SPECIFIC, RECEIVER SPECIFIC
    // TEST MULTIPLE
    Promotion promotion = buildProductClaimPromotion( 6,
                                                      SweepstakesWinnerEligibilityType.SUBMITTERS_ONLY_CODE,
                                                      SweepstakesMultipleAwardsType.MULTIPLE_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesWinnersType.SPECIFIC_CODE,
                                                      SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS,
                                                      19,
                                                      0 );

    PromotionSweepstake promotionSweepstake = buildPromotionSweepstake();
    // if these values are more
    int giverWinnerReplacementTotal = 19;
    int receiverWinnerReplacementTotal = 0;

    ProductClaimParticipant prodParticipant6 = buildProductClaimParticipant( 6 );
    ProductClaimParticipant prodParticipant7 = buildProductClaimParticipant( 7 );
    ProductClaimParticipant prodParticipant8 = buildProductClaimParticipant( 8 );
    ProductClaimParticipant prodParticipant9 = buildProductClaimParticipant( 9 );

    Set<ProductClaimParticipant> claimParticipants1 = new LinkedHashSet<>();
    Set<ProductClaimParticipant> claimParticipants2 = new LinkedHashSet<>();
    claimParticipants1.add( prodParticipant6 );
    claimParticipants1.add( prodParticipant7 );
    claimParticipants2.add( prodParticipant8 );
    claimParticipants2.add( prodParticipant9 );
    // build claims
    List<Claim> claims = new LinkedList<>();
    claims.add( buildClaimWithParticpants( 1, part1, claimParticipants1 ) );
    claims.add( buildClaimWithParticpants( 2, part2, claimParticipants2 ) );
    claims.add( buildClaim( 3, part1 ) );
    claims.add( buildClaim( 4, part2 ) );
    claims.add( buildClaim( 5, part3 ) );

    EasyMock.expect( claimServiceMock.getProductClaimClaimsList( promotion.getId(), SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS, promotionSweepstake ) ).andReturn( claims );

    EasyMock.replay( claimServiceMock );

    List actualWinners = prodWinnersListStrategy.buildWinnersList( promotion, promotionSweepstake, giverWinnerReplacementTotal, receiverWinnerReplacementTotal );

    // check size
    assertEquals( 19, actualWinners.size() );
    // check that winners have been used more than once
    HashMap map = new HashMap();
    Iterator iter = actualWinners.iterator();
    boolean multipleEntriesFound = false;
    while ( iter.hasNext() )
    {
      PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)iter.next();
      if ( map.containsKey( winner.getParticipant().getId() ) )
      {
        multipleEntriesFound = true;
        break;
      }
      map.put( winner.getParticipant().getId(), winner );

    }

    assertTrue( "Multiple entries expected to be found but were not", multipleEntriesFound );
    EasyMock.reset( claimServiceMock );

  }

  /**
   * @param id
   * @param sweepstakeEligibilityTypeCode
   * @param sweepstakesMultipleAwardTypeCode
   * @param sweepstakeSubmittersTypeCode
   * @param sweepstakeTeammembersTypeCode
   * @param sweepstakeSubmittersWinners
   * @param sweepstakeTeammembersWinners
   * @return ProductClaimPromotion
   */

  private ProductClaimPromotion buildProductClaimPromotion( long id,
                                                            String sweepstakeEligibilityTypeCode,
                                                            String sweepstakesMultipleAwardTypeCode,
                                                            String sweepstakeSubmittersTypeCode,
                                                            String sweepstakeTeammembersTypeCode,
                                                            String sweepstakesClaimEligibilityType,
                                                            int sweepstakeSubmittersWinners,
                                                            int sweepstakeTeammembersWinners )
  {

    ProductClaimPromotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( id ) );

    promotion.setSweepstakesClaimEligibilityType( (SweepstakesClaimEligibilityType)mockFactory.getPickListItem( SweepstakesClaimEligibilityType.class, sweepstakesClaimEligibilityType ) );

    promotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)mockFactory.getPickListItem( SweepstakesWinnerEligibilityType.class, sweepstakeEligibilityTypeCode ) );

    promotion.setSweepstakesMultipleAwardType( (SweepstakesMultipleAwardsType)mockFactory.getPickListItem( SweepstakesMultipleAwardsType.class, sweepstakesMultipleAwardTypeCode ) );

    promotion.setSweepstakesPrimaryBasisType( (SweepstakesWinnersType)mockFactory.getPickListItem( SweepstakesWinnersType.class, sweepstakeSubmittersTypeCode ) );

    promotion.setSweepstakesSecondaryBasisType( (SweepstakesWinnersType)mockFactory.getPickListItem( SweepstakesWinnersType.class, sweepstakeTeammembersTypeCode ) );

    promotion.setSweepstakesPrimaryWinners( new Integer( sweepstakeSubmittersWinners ) );
    promotion.setSweepstakesSecondaryWinners( new Integer( sweepstakeTeammembersWinners ) );

    return promotion;
  }

  /**
   * Builds a PromotionSweepstake from scratch.
   * 
   * @return PromotionSweepstake
   */
  private PromotionSweepstake buildPromotionSweepstake()
  {
    PromotionSweepstake promotionSweepstake = new PromotionSweepstake();
    promotionSweepstake.setStartDate( new Date() );
    promotionSweepstake.setEndDate( new Date() );
    promotionSweepstake.setProcessed( false );

    return promotionSweepstake;
  }

  private Claim buildClaim( long id, Participant participant )
  {
    ProductClaim claim = new ProductClaim();
    claim.setId( new Long( id ) );
    claim.setSubmissionDate( new Date() );
    claim.setSubmitter( participant );

    return claim;
  }

  private Claim buildClaimWithParticpants( long id, Participant participant, Set claimParticipants )
  {
    ProductClaim claim = new ProductClaim();
    claim.setId( new Long( id ) );
    claim.setSubmissionDate( new Date() );
    claim.setSubmitter( participant );
    claim.setClaimParticipants( claimParticipants );

    return claim;
  }

  private Participant buildParticipant( long id )
  {
    Participant participant = new Participant();
    participant.setId( new Long( id ) );
    participant.setFirstName( String.valueOf( id ) );
    participant.setLastName( String.valueOf( id ) );
    participant.setUserName( String.valueOf( id ) );
    participant.setStatus( (ParticipantStatus)mockFactory.getPickListItem( ParticipantStatus.class, ParticipantStatus.ACTIVE ) );
    return participant;
  }

  private ProductClaimParticipant buildProductClaimParticipant( long id )
  {
    ProductClaimParticipant productClaimParticipant = new ProductClaimParticipant();
    productClaimParticipant.setParticipant( buildParticipant( id ) );
    return productClaimParticipant;
  }

}

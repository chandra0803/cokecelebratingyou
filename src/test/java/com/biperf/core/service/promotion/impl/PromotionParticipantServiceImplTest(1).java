/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/promotion/impl/PromotionParticipantServiceImplTest.java,v $
 */

package com.biperf.core.service.promotion.impl;

import org.easymock.EasyMock;

import com.biperf.core.dao.promotion.PromotionParticipantDAO;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.promotion.PromotionTeamPosition;
import com.biperf.core.domain.promotion.PromotionWebRulesAudience;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.impl.AudienceServiceImplTest;

/**
 * PromotionParticipantServiceImplTest.
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
 * <td>Aug 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionParticipantServiceImplTest extends BaseServiceTest
{

  private PromotionParticipantServiceImpl promotionParticipantServiceImplUnderTest;
  private PromotionParticipantDAO promotionParticipantDAOMock;

  /**
   * Sets this test up with the appropriate DAOs and services. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  public void setUp() throws Exception
  {

    // Prepares the pickListFactory
    super.setUp();

    promotionParticipantDAOMock = EasyMock.createMock( PromotionParticipantDAO.class );

    promotionParticipantServiceImplUnderTest = new PromotionParticipantServiceImpl();
    promotionParticipantServiceImplUnderTest.setPromotionParticipantDAO( promotionParticipantDAOMock );
  }

  /**
   * Tests saving, getting and deleting the PromotionTeamPostion.
   */
  public void testSaveGetByIdAndDeletePromotionTeamPosition()
  {

    PromotionTeamPosition expectedPromotionTeamPosition = buildPromotionTeamPosition();

    EasyMock.expect( promotionParticipantDAOMock.savePromotionTeamPosition( expectedPromotionTeamPosition ) ).andReturn( expectedPromotionTeamPosition );
    EasyMock.replay( promotionParticipantDAOMock );

    PromotionTeamPosition actualPromotionTeamPosition = promotionParticipantServiceImplUnderTest.savePromotionTeamPosition( expectedPromotionTeamPosition );

    EasyMock.verify( promotionParticipantDAOMock );
    assertEquals( "Actual PromotionTeamPosition isn't equals to expected", expectedPromotionTeamPosition, actualPromotionTeamPosition );

    // reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test GetById */
    // Prepare the mockDAO for a getById call
    EasyMock.expect( promotionParticipantDAOMock.getPromotionTeamPositionById( expectedPromotionTeamPosition.getId() ) ).andReturn( expectedPromotionTeamPosition );
    EasyMock.replay( promotionParticipantDAOMock );

    // Get the audience through the service
    PromotionTeamPosition actualPromotionTeamPositionFromGetById = promotionParticipantServiceImplUnderTest.getPromotionTeamPositionById( expectedPromotionTeamPosition.getId() );

    // Verify the DAO was called
    EasyMock.verify( promotionParticipantDAOMock );

    // Assert the actual promotionTeamAudience was the one which was expected.
    assertEquals( "Actual promotionTeamAudience isn't equals to expected from getById", expectedPromotionTeamPosition, actualPromotionTeamPositionFromGetById );

    // Reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test Delete */
    // The getById method on the DAO will be called first
    EasyMock.expect( promotionParticipantDAOMock.getPromotionTeamPositionById( expectedPromotionTeamPosition.getId() ) ).andReturn( expectedPromotionTeamPosition );

    // The delete method on the DAO will be called
    promotionParticipantDAOMock.deletePromotionTeamPosition( expectedPromotionTeamPosition );
    EasyMock.expectLastCall().once();

    // Prepare the DAO for the calls
    EasyMock.replay( promotionParticipantDAOMock );

    // Call delete through the service (which in turn will call the DAO methods in their expected
    // order)
    promotionParticipantServiceImplUnderTest.deletePromotionTeamPosition( expectedPromotionTeamPosition.getId() );

    // Verify the DAO was called correctly
    EasyMock.verify( promotionParticipantDAOMock );

  }

  /**
   * Test saving, getting and deleting the PromotionWebRulesAudience.
   */
  public void testSaveGetByIdAndDeletePromotionWebRulesAudience()
  {

    PromotionWebRulesAudience expectedPromotionWebRulesAudience = buildPromotionWebRulesAudience();

    EasyMock.expect( promotionParticipantDAOMock.savePromotionAudience( expectedPromotionWebRulesAudience ) ).andReturn( expectedPromotionWebRulesAudience );
    EasyMock.replay( promotionParticipantDAOMock );

    PromotionWebRulesAudience actualPromotionWebRulesAudience = (PromotionWebRulesAudience)promotionParticipantServiceImplUnderTest.savePromotionAudience( expectedPromotionWebRulesAudience );

    EasyMock.verify( promotionParticipantDAOMock );
    assertEquals( "Actual PromotionWebRulesAudience isn't equals to expected", expectedPromotionWebRulesAudience, actualPromotionWebRulesAudience );

    // reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test GetById */
    // Prepare the mockDAO for a getById call
    EasyMock.expect( promotionParticipantDAOMock.getPromotionAudienceById( expectedPromotionWebRulesAudience.getId() ) ).andReturn( expectedPromotionWebRulesAudience );
    EasyMock.replay( promotionParticipantDAOMock );

    // Get the audience through the service
    PromotionWebRulesAudience actualPromotionWebRulesAudienceFromGetById = (PromotionWebRulesAudience)promotionParticipantServiceImplUnderTest
        .getPromotionAudienceById( expectedPromotionWebRulesAudience.getId() );

    // Verify the DAO was called
    EasyMock.verify( promotionParticipantDAOMock );

    // Assert the actual promotionTeamAudience was the one which was expected.
    assertEquals( "Actual promotionTeamAudience isn't equals to expected from getById", expectedPromotionWebRulesAudience, actualPromotionWebRulesAudienceFromGetById );

    // Reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test Delete */
    // The getById method on the DAO will be called first
    EasyMock.expect( promotionParticipantDAOMock.getPromotionAudienceById( expectedPromotionWebRulesAudience.getId() ) ).andReturn( expectedPromotionWebRulesAudience );

    // The delete method on the DAO will be called
    promotionParticipantDAOMock.deletePromotionAudience( expectedPromotionWebRulesAudience );
    EasyMock.expectLastCall().once();

    // Prepare the DAO for the calls
    EasyMock.replay( promotionParticipantDAOMock );

    // Call delete through the service (which in turn will call the DAO methods in their expected
    // order)
    promotionParticipantServiceImplUnderTest.deletePromotionAudience( expectedPromotionWebRulesAudience.getId() );

    // Verify the DAO was called correctly
    EasyMock.verify( promotionParticipantDAOMock );

  }

  /**
   * Test saving, getting and deleting the PromotionPrimaryAudience.
   */
  public void testSaveGetByIdAndDeletePromotionSubmitterAudience()
  {

    PromotionPrimaryAudience expectedPromotionSubmitterAudience = buildPromotionSubmitterAudience();

    EasyMock.expect( promotionParticipantDAOMock.savePromotionAudience( expectedPromotionSubmitterAudience ) ).andReturn( expectedPromotionSubmitterAudience );
    EasyMock.replay( promotionParticipantDAOMock );

    PromotionPrimaryAudience actualPromotionSubmitterAudience = (PromotionPrimaryAudience)promotionParticipantServiceImplUnderTest.savePromotionAudience( expectedPromotionSubmitterAudience );

    EasyMock.verify( promotionParticipantDAOMock );
    assertEquals( "Actual promotionSubmitterAudience isn't equals to expected", expectedPromotionSubmitterAudience, actualPromotionSubmitterAudience );

    // reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test GetById */
    // Prepare the mockDAO for a getById call
    EasyMock.expect( promotionParticipantDAOMock.getPromotionAudienceById( expectedPromotionSubmitterAudience.getId() ) ).andReturn( expectedPromotionSubmitterAudience );
    EasyMock.replay( promotionParticipantDAOMock );

    // Get the audience through the service
    PromotionPrimaryAudience actualPromotionSubmitterAudienceFromGetById = (PromotionPrimaryAudience)promotionParticipantServiceImplUnderTest
        .getPromotionAudienceById( expectedPromotionSubmitterAudience.getId() );

    // Verify the DAO was called
    EasyMock.verify( promotionParticipantDAOMock );

    // Assert the actual promotionSubmitterAudience was the one which was expected.
    assertEquals( "Actual promotionSubmitterAudience isn't equals to expected from getById", expectedPromotionSubmitterAudience, actualPromotionSubmitterAudienceFromGetById );

    // Reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test Delete */
    // The getById method on the DAO will be called first
    EasyMock.expect( promotionParticipantDAOMock.getPromotionAudienceById( expectedPromotionSubmitterAudience.getId() ) ).andReturn( expectedPromotionSubmitterAudience );

    // The delete method on the DAO will be called
    promotionParticipantDAOMock.deletePromotionAudience( expectedPromotionSubmitterAudience );
    EasyMock.expectLastCall().once();

    // Prepare the DAO for the calls
    EasyMock.replay( promotionParticipantDAOMock );

    // Call delete through the service (which in turn will call the DAO methods in their expected
    // order)
    promotionParticipantServiceImplUnderTest.deletePromotionAudience( expectedPromotionSubmitterAudience.getId() );

    // Verify the DAO was called correctly
    EasyMock.verify( promotionParticipantDAOMock );

  }

  /**
   * Test saving, getting and deleting the promotion teamAudience.
   */
  public void testSaveGetByIdAndDeletePromotionTeamAudience()
  {

    /* Test Save */
    // Build the promotionTeamAudience
    PromotionSecondaryAudience expectedPromotionTeamAudience = buildPromotionTeamAudience();

    // Prepare the mock DAO for a save call.
    EasyMock.expect( promotionParticipantDAOMock.savePromotionAudience( expectedPromotionTeamAudience ) ).andReturn( expectedPromotionTeamAudience );
    EasyMock.replay( promotionParticipantDAOMock );

    // Call the service (which in turn calls the mockDAO)
    PromotionSecondaryAudience actualPromotionTeamAudienceFromSave = (PromotionSecondaryAudience)promotionParticipantServiceImplUnderTest.savePromotionAudience( expectedPromotionTeamAudience );

    // Verify the mockDAO was called from the service as expected
    EasyMock.verify( promotionParticipantDAOMock );

    // Assert the return value from the service is what was expected.
    assertEquals( "Actual promotionTeamAudience isn't equals to expected from save", expectedPromotionTeamAudience, actualPromotionTeamAudienceFromSave );

    // reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test GetById */
    // Prepare the mockDAO for a getById call
    EasyMock.expect( promotionParticipantDAOMock.getPromotionAudienceById( expectedPromotionTeamAudience.getId() ) ).andReturn( expectedPromotionTeamAudience );
    EasyMock.replay( promotionParticipantDAOMock );

    // Get the audience through the service
    PromotionSecondaryAudience actualPromotionTeamAudienceFromGetById = (PromotionSecondaryAudience)promotionParticipantServiceImplUnderTest
        .getPromotionAudienceById( expectedPromotionTeamAudience.getId() );

    // Verify the DAO was called
    EasyMock.verify( promotionParticipantDAOMock );

    // Assert the actual promotionTeamAudience was the one which was expected.
    assertEquals( "Actual promotionTeamAudience isn't equals to expected from getById", expectedPromotionTeamAudience, actualPromotionTeamAudienceFromGetById );

    // Reset the MockDAO
    EasyMock.reset( promotionParticipantDAOMock );

    /* Test Delete */
    // The getById method on the DAO will be called first
    EasyMock.expect( promotionParticipantDAOMock.getPromotionAudienceById( expectedPromotionTeamAudience.getId() ) ).andReturn( expectedPromotionTeamAudience );

    // The delete method on the DAO will be called
    promotionParticipantDAOMock.deletePromotionAudience( expectedPromotionTeamAudience );
    EasyMock.expectLastCall().once();

    // Prepare the DAO for the calls
    EasyMock.replay( promotionParticipantDAOMock );

    // Call delete through the service (which in turn will call the DAO methods in their expected
    // order)
    promotionParticipantServiceImplUnderTest.deletePromotionAudience( expectedPromotionTeamAudience.getId() );

    // Verify the DAO was called correctly
    EasyMock.verify( promotionParticipantDAOMock );

  }

  /**
   * Builds and returns a promotionTeamAudience.
   * 
   * @return PromotionSecondaryAudience
   */
  private PromotionSecondaryAudience buildPromotionTeamAudience()
  {
    Promotion promotion = PromotionServiceImplTest.buildProductClaimPromotion( "adsase" );
    PaxAudience paxAudience = AudienceServiceImplTest.buildStaticPaxAudience();

    PromotionSecondaryAudience promotionTeamAudience = new PromotionSecondaryAudience();
    promotionTeamAudience.setId( new Long( 2312 ) );
    promotionTeamAudience.setAudience( paxAudience );
    promotionTeamAudience.setPromotion( promotion );

    return promotionTeamAudience;
  }

  /**
   * Builds and returns a promotionSubmitterAudience.
   * 
   * @return PromotionPrimaryAudience
   */
  public static PromotionPrimaryAudience buildPromotionSubmitterAudience()
  {

    Promotion promotion = PromotionServiceImplTest.buildProductClaimPromotion( "adsase" );
    PaxAudience paxAudience = AudienceServiceImplTest.buildStaticPaxAudience();

    PromotionPrimaryAudience promotionSubmitterAudience = new PromotionPrimaryAudience();
    promotionSubmitterAudience.setId( new Long( 2312 ) );
    promotionSubmitterAudience.setAudience( paxAudience );
    promotionSubmitterAudience.setPromotion( promotion );

    return promotionSubmitterAudience;
  }

  /**
   * Builds and returns a promotionWebRulesAudience.
   * 
   * @return PromotionWebRulesAudience
   */
  public static PromotionWebRulesAudience buildPromotionWebRulesAudience()
  {

    Promotion promotion = PromotionServiceImplTest.buildProductClaimPromotion( "adsase" );
    PaxAudience paxAudience = AudienceServiceImplTest.buildStaticPaxAudience();

    PromotionWebRulesAudience promotionWebRulesAudience = new PromotionWebRulesAudience();
    promotionWebRulesAudience.setId( new Long( 3123 ) );
    promotionWebRulesAudience.setAudience( paxAudience );
    promotionWebRulesAudience.setPromotion( promotion );

    return promotionWebRulesAudience;
  }

  /**
   * Builds and returns a promotionTeamPosition.
   * 
   * @return PromotionTeamPosition
   */
  private PromotionTeamPosition buildPromotionTeamPosition()
  {

    Promotion promotion = PromotionServiceImplTest.buildProductClaimPromotion( "adsase" );

    PromotionTeamPosition promotionTeamPosition = new PromotionTeamPosition();
    promotionTeamPosition.setPromotion( promotion );
    promotionTeamPosition.setRequired( true );

    return promotionTeamPosition;

  }

}

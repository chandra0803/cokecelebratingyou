/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/promotion/impl/PromotionNotificationServiceImplTest.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionNotificationDAO;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;

/**
 * PromotionNotificationServiceImplTest.
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
 * <td>sedey</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionNotificationServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PromotionNotificationServiceImplTest( String test )
  {
    super( test );
  }

  /** promotionNotificationServiceImplementation */
  private PromotionNotificationServiceImpl promotionNotificationService = new PromotionNotificationServiceImpl();

  /** mocks */
  private Mock mockPromotionDAO = null;
  private Mock mockPromotionNotificationDAO = null;
  private Mock mockClaimFormDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    mockPromotionDAO = new Mock( PromotionDAO.class );
    mockPromotionNotificationDAO = new Mock( PromotionNotificationDAO.class );
    mockClaimFormDAO = new Mock( ClaimFormDAO.class );

    promotionNotificationService.setPromotionDAO( (PromotionDAO)mockPromotionDAO.proxy() );
    promotionNotificationService.setPromotionNotificationDAO( (PromotionNotificationDAO)mockPromotionNotificationDAO.proxy() );
    promotionNotificationService.setClaimFormDAO( (ClaimFormDAO)mockClaimFormDAO.proxy() );
  }

  /**
   * Test getting the Promotion Type PromotionNotification by id.
   */
  public void testGetPromotionTypeNotificationById()
  {
    // Create a test promotion
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    // Get the test PromotionTypeNotification.
    PromotionNotificationType promoNotificationType = new PromotionNotificationType();
    promoNotificationType.setId( new Long( 1 ) );
    promoNotificationType.setPromotion( promotion );
    // PromotionNotificationDAO expected to call getPromotionEmailNotificationTypeById
    // once with the PromotionNotificationId which will return the PromotionNotification
    // expected
    mockPromotionNotificationDAO.expects( once() ).method( "getPromotionEmailNotificationTypeById" ).with( same( promoNotificationType.getId() ) ).will( returnValue( promoNotificationType ) );

    promotionNotificationService.getPromotionNotificationTypeById( promoNotificationType.getId() );

    mockPromotionNotificationDAO.verify();
  }

  /**
   * Test getting the ClaimForm Type PromotionNotification by id.
   */
  public void testGetClaimFormTypeNotificationById()
  {
    // Create a test promotion
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    // Get the test PromotionTypeNotification.
    ClaimFormNotificationType claimFormNotificationType = new ClaimFormNotificationType();
    claimFormNotificationType.setId( new Long( 1 ) );
    claimFormNotificationType.setPromotion( promotion );
    // PromotionNotificationDAO expected to call getPromotionEmailNotificationTypeById
    // once with the PromotionNotificationId which will return the PromotionNotification
    // expected
    mockPromotionNotificationDAO.expects( once() ).method( "getClaimFormEmailNotificationTypeById" ).with( same( claimFormNotificationType.getId() ) ).will( returnValue( claimFormNotificationType ) );

    promotionNotificationService.getClaimFormNotificationTypeById( claimFormNotificationType.getId() );

    mockPromotionNotificationDAO.verify();
  }

  /**
   * Test Saving the PromotionNotification through the service.
   */
  public void testSavePromotionNotification()
  {
    // Create a test promotion
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    // Create the test PromotionTypeNotification.
    PromotionNotificationType promoNotificationType = new PromotionNotificationType();
    promoNotificationType.setPromotion( promotion );

    // Create the saved PromotionTypeNotification.
    PromotionNotificationType savedPromoNotificationType = new PromotionNotificationType();
    savedPromoNotificationType.setId( new Long( 1 ) );
    savedPromoNotificationType.setPromotion( promotion );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );

    mockPromotionNotificationDAO.expects( once() ).method( "save" ).with( same( promoNotificationType ) ).will( returnValue( savedPromoNotificationType ) );

    promotionNotificationService.savePromotionNotification( promotion.getId(), promoNotificationType );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockPromotionNotificationDAO.verify();
  }

  /**
   * Test Saving the PromotionNotification through the service.
   */
  public void testSavePromotionNotifications()
  {
    // Create a test promotion
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setName( "new promo" );

    ClaimFormStepEmailNotification claimFormStepEmailNotification = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotification.setId( new Long( 1 ) );

    // Create a new test PromotionTypeNotification.
    PromotionNotificationType promoNotificationType = new PromotionNotificationType();
    promoNotificationType.setPromotion( promotion );

    // Create another new test PromotionTypeNotification.
    PromotionNotificationType promoNotificationType2 = new PromotionNotificationType();
    promoNotificationType2.setPromotion( promotion );

    // Create a new test ClaimFormTypeNotification
    ClaimFormNotificationType claimFormNotificationType = new ClaimFormNotificationType();
    claimFormNotificationType.setPromotion( promotion );
    claimFormNotificationType.setClaimFormStepEmailNotification( claimFormStepEmailNotification );

    // Create an existing PromotionTypeNotification
    PromotionNotificationType existingPromoNotificationType = new PromotionNotificationType();
    existingPromoNotificationType.setId( new Long( 1 ) );
    existingPromoNotificationType.setPromotion( promotion );

    // Create an existing ClaimFormTypeNotification
    ClaimFormNotificationType existingClaimFormNotificationType = new ClaimFormNotificationType();
    existingClaimFormNotificationType.setId( new Long( 1 ) );
    existingClaimFormNotificationType.setPromotion( promotion );
    existingClaimFormNotificationType.setClaimFormStepEmailNotification( claimFormStepEmailNotification );

    List promotionNotifications = new ArrayList();
    promotionNotifications.add( promoNotificationType );
    promotionNotifications.add( promoNotificationType2 );
    promotionNotifications.add( claimFormNotificationType );
    promotionNotifications.add( existingPromoNotificationType );
    promotionNotifications.add( existingClaimFormNotificationType );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );

    // First save call
    mockPromotionNotificationDAO.expects( once() ).method( "save" ).with( same( promoNotificationType ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );

    // Second save call
    mockPromotionNotificationDAO.expects( once() ).method( "save" ).with( same( promoNotificationType2 ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );

    mockClaimFormDAO.expects( once() ).method( "getClaimFormStepEmailNotificationById" ).with( same( claimFormStepEmailNotification.getId() ) ).will( returnValue( claimFormStepEmailNotification ) );

    // Third save call
    mockPromotionNotificationDAO.expects( once() ).method( "save" ).with( same( claimFormNotificationType ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );

    mockPromotionNotificationDAO.expects( once() ).method( "getPromotionEmailNotificationTypeById" ).with( same( existingPromoNotificationType.getId() ) )
        .will( returnValue( existingPromoNotificationType ) );

    // Fourth save call (update)
    mockPromotionNotificationDAO.expects( once() ).method( "save" ).with( same( existingPromoNotificationType ) );

    mockPromotionDAO.expects( once() ).method( "getPromotionById" ).with( same( promotion.getId() ) ).will( returnValue( promotion ) );

    mockPromotionNotificationDAO.expects( once() ).method( "getClaimFormEmailNotificationTypeById" ).with( same( existingClaimFormNotificationType.getId() ) )
        .will( returnValue( existingClaimFormNotificationType ) );

    // Fifth save call (update)
    mockPromotionNotificationDAO.expects( once() ).method( "save" ).with( same( existingClaimFormNotificationType ) );

    promotionNotificationService.savePromotionNotifications( promotion.getId(), promotionNotifications );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockPromotionNotificationDAO.verify();
  }

  /**
   * Test get all PromotionTypeNotifications for a promotion;
   */
  public void testGetAllPromotionNotificationTypesByPromotionId()
  {
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );

    List promotionTypeNotifications = new ArrayList();
    promotionTypeNotifications.add( new PromotionNotificationType() );

    mockPromotionNotificationDAO.expects( once() ).method( "getAllPromotionEmailNotificationsByPromotionId" ).with( same( promotion.getId() ) ).will( returnValue( promotionTypeNotifications ) );

    List returnedNotifications = promotionNotificationService.getPromotionTypeNotificationsByPromotionId( promotion.getId() );
    assertTrue( returnedNotifications.size() > 0 );
  }

  /**
   * Test get all ClaimFormTypeNotifications for a promotion;
   */
  public void testGetAllClaimFormNotificationTypesByPromotionId()
  {
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );

    List promotionTypeNotifications = new ArrayList();
    promotionTypeNotifications.add( new ClaimFormNotificationType() );

    mockPromotionNotificationDAO.expects( once() ).method( "getAllClaimFormStepEmailNotificationsByPromotionId" ).with( same( promotion.getId() ) ).will( returnValue( promotionTypeNotifications ) );

    List returnedNotifications = promotionNotificationService.getClaimFormTypeNotificationsByPromotionId( promotion.getId() );
    assertTrue( returnedNotifications.size() > 0 );
  }
}

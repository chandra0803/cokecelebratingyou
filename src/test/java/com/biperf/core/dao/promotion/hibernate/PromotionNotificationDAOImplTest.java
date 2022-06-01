/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/promotion/hibernate/PromotionNotificationDAOImplTest.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.List;

import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.PromotionNotificationDAO;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;

/**
 * PromotionNotificationDAOImplTest.
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
 * <td>Aug 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class PromotionNotificationDAOImplTest extends BaseDAOTest
{
  private ClaimFormDAO claimFormDAO;
  private PromotionDAO promotionDAO;
  private PromotionNotificationDAO promotionNotificationDAO;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  public void setUp() throws Exception
  {
    super.setUp();
    claimFormDAO = getClaimFormDAO();
    promotionDAO = getPromotionDAO();
    promotionNotificationDAO = getPromotionNotificationDAO();
  }

  /**
   * Uses the ApplicationContextFactory to look up the ClaimFormDAO implementation.
   * 
   * @return ProductDAO
   */
  private static ClaimFormDAO getClaimFormDAO()
  {
    return (ClaimFormDAO)getDAO( "claimFormDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return ProductDAO
   */
  private PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( "promotionDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionNotificaionDAO implementation.
   * 
   * @return ProductDAO
   */
  private static PromotionNotificationDAO getPromotionNotificationDAO()
  {
    return (PromotionNotificationDAO)getDAO( "promotionNotificationDAO" );
  }

  /**
   * Test saving, getting and updating promotionNotifications.
   */
  public void testSaveGetPromotionNotificationById()
  {
    // create and save a promotion
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    // Create a promotion notification
    PromotionNotificationType expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setNotificationMessageId( 1 );
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.PROGRAM_LAUNCH ) );
    expectedPromoNotification.setNumberOfDays( new Integer( 5 ) );

    // Create a ClaimForm/ClaimFormStep/ClaimFormStepEmailNotification object
    ClaimForm claimForm = getClaimFormDomainObject();
    ClaimFormStep claimFormStep = getClaimFormStepDomainObject();
    ClaimFormStepEmailNotification claimFormNotification = getClaimFormStepEmailNotificationDomainObject( ClaimFormStepEmailNotificationType.CLAIM_APPROVED );
    claimFormStep.addClaimFormStepEmailNotification( claimFormNotification );
    claimForm.addClaimFormStep( claimFormStep );
    claimFormDAO.saveClaimForm( claimForm );

    // Create a claim notification
    ClaimFormNotificationType expectedClaimNotification = new ClaimFormNotificationType();
    expectedClaimNotification.setNotificationMessageId( 2 );
    expectedClaimNotification.setClaimFormStepEmailNotification( claimFormNotification );
    expectedClaimNotification.setClaimFormStepEmailNotificationType( claimFormNotification.getClaimFormStepEmailNotificationType() );

    // Add the notifications to the list of PromotionNotifications on the promotion
    promotion.addPromotionNotification( expectedPromoNotification );
    promotion.addPromotionNotification( expectedClaimNotification );

    // Save each notification
    promotionNotificationDAO.save( expectedPromoNotification );
    promotionNotificationDAO.save( expectedClaimNotification );

    // Clear the session
    flushAndClearSession();

    // Get the notifications by Id
    PromotionNotificationType actualPromoNotification = (PromotionNotificationType)promotionNotificationDAO.getPromotionEmailNotificationTypeById( expectedPromoNotification.getId() );
    ClaimFormNotificationType actualClaimNotification = (ClaimFormNotificationType)promotionNotificationDAO.getClaimFormEmailNotificationTypeById( expectedClaimNotification.getId() );

    // Check to see if the actual objects are equal to the expected ones
    assertEquals( "Expected promotionPromoNotification wasn't equal to actual", expectedPromoNotification, actualPromoNotification );
    assertEquals( "Expected promotionClaimNotification wasn't equal to actual", expectedClaimNotification, actualClaimNotification );

    // Update the actual objects
    actualPromoNotification.setNotificationMessageId( 1 );
    actualClaimNotification.setNotificationMessageId( 1 );

    // Save the updated notifications
    promotionNotificationDAO.save( actualPromoNotification );
    promotionNotificationDAO.save( actualClaimNotification );

    flushAndClearSession();

    // Get the notifications by Id
    PromotionNotificationType updatedPromoNotification = (PromotionNotificationType)promotionNotificationDAO.getPromotionEmailNotificationTypeById( expectedPromoNotification.getId() );
    ClaimFormNotificationType updatedClaimNotification = (ClaimFormNotificationType)promotionNotificationDAO.getClaimFormEmailNotificationTypeById( expectedClaimNotification.getId() );

    // Check to see if the updated objects are equal to the actual ones
    assertEquals( "Updated promotionPromoNotification wasn't equal to actual", actualPromoNotification, updatedPromoNotification );
    assertEquals( "Updated promotionClaimNotification wasn't equal to actual", actualClaimNotification, updatedClaimNotification );
  }

  /**
   * Test getting a list of promotion promotionNotification objects assigned to a Promtion.
   */
  public void testGetAllPromotionEmailNotificationsByPromotionId()
  {
    // create and save a promotion
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    // Create a promotion notification
    PromotionNotificationType expectedPromoNotification = new PromotionNotificationType();
    expectedPromoNotification.setNotificationMessageId( 2 );
    expectedPromoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.PROGRAM_LAUNCH ) );
    expectedPromoNotification.setNumberOfDays( new Integer( 5 ) );

    // Create another promotion notification
    PromotionNotificationType expectedPromoNotification2 = new PromotionNotificationType();
    expectedPromoNotification2.setNotificationMessageId( 2 );
    expectedPromoNotification2.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.PROGRAM_END ) );
    expectedPromoNotification2.setNumberOfDays( new Integer( 1 ) );

    // Add the notifications to the list of PromotionNotifications on the promotion
    promotion.addPromotionNotification( expectedPromoNotification );
    promotion.addPromotionNotification( expectedPromoNotification2 );

    promotionNotificationDAO.save( expectedPromoNotification );
    promotionNotificationDAO.save( expectedPromoNotification2 );

    // Clear the session
    flushAndClearSession();

    List actualNotificationList = promotionNotificationDAO.getAllPromotionEmailNotificationsByPromotionId( promotion.getId() );

    assertEquals( "expected PromoNotificationList wasn't equal to actual", actualNotificationList.size(), promotion.getPromotionNotifications().size() );
  }

  /**
   * Test getting a list of claim promotionNotification objects assigned to a Promtion.
   */
  public void testGetAllClaimFormEmailNotificationsByPromotionId()
  {
    // create and save a promotion
    Promotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "Test1000" );
    promotionDAO.save( promotion );

    ClaimFormStepEmailNotification claimFormNotification = getClaimFormStepEmailNotificationDomainObject( ClaimFormStepEmailNotificationType.CLAIM_APPROVED );
    ClaimFormStepEmailNotification claimFormNotification2 = getClaimFormStepEmailNotificationDomainObject( ClaimFormStepEmailNotificationType.CLAIM_SUBMITTED );

    // Create a ClaimForm/ClaimFormStep/ClaimFormStepEmailNotification object
    ClaimForm claimForm = getClaimFormDomainObject();
    ClaimFormStep claimFormStep = getClaimFormStepDomainObject();
    claimFormStep.addClaimFormStepEmailNotification( claimFormNotification );
    claimFormStep.addClaimFormStepEmailNotification( claimFormNotification2 );
    claimForm.addClaimFormStep( claimFormStep );
    claimFormDAO.saveClaimForm( claimForm );

    // Create a claim notification
    ClaimFormNotificationType expectedClaimNotification = new ClaimFormNotificationType();
    expectedClaimNotification.setNotificationMessageId( 2 );
    expectedClaimNotification.setClaimFormStepEmailNotification( claimFormNotification );
    expectedClaimNotification.setClaimFormStepEmailNotificationType( claimFormNotification.getClaimFormStepEmailNotificationType() );

    // Create another claim notification
    ClaimFormNotificationType expectedClaimNotification2 = new ClaimFormNotificationType();
    expectedClaimNotification2.setNotificationMessageId( 2 );
    expectedClaimNotification2.setClaimFormStepEmailNotification( claimFormNotification2 );
    expectedClaimNotification2.setClaimFormStepEmailNotificationType( claimFormNotification2.getClaimFormStepEmailNotificationType() );

    // Add the notifications to the list of PromotionNotifications on the promotion
    promotion.addPromotionNotification( expectedClaimNotification );
    promotion.addPromotionNotification( expectedClaimNotification2 );

    promotionNotificationDAO.save( expectedClaimNotification );
    promotionNotificationDAO.save( expectedClaimNotification2 );

    // Clear the session
    flushAndClearSession();

    List actualNotificationList = promotionNotificationDAO.getAllClaimFormStepEmailNotificationsByPromotionId( promotion.getId() );

    assertEquals( "expected ClaimNotificationList wasn't equal to actual", actualNotificationList.size(), promotion.getPromotionNotifications().size() );
  }

  /**
   * @return ClaimForm Random Domain object
   */
  private static ClaimForm getClaimFormDomainObject()
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    ClaimForm claimForm = new ClaimForm();
    claimForm.setName( "A Test Form" + uniqueName );
    claimForm.setCmAssetCode( "claimform.asset" + uniqueName );
    claimForm.setDescription( "Description of the test claim form" + uniqueName );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( "clm" ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ) );

    return claimForm;
  }

  /**
   * @return ClaimFormStep Random Domain object
   */
  private static ClaimFormStep getClaimFormStepDomainObject()
  {
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setCmKeyFragment( String.valueOf( Math.random() % 29930291 ) );
    claimFormStep.setSalesRequired( true );

    return claimFormStep;
  }

  /**
   * @return ClaimFormStepEmailNotification Random Domain object
   */
  private static ClaimFormStepEmailNotification getClaimFormStepEmailNotificationDomainObject( String emailNotificationCode )
  {
    ClaimFormStepEmailNotification claimFormStepEmailNotification = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotification.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.lookup( emailNotificationCode ) );
    return claimFormStepEmailNotification;
  }

  // Create a promotion notification
  public static PromotionNotificationType getPromotionNotificationType( String uniqueString )
  {

    PromotionNotificationType promoNotification = new PromotionNotificationType();
    promoNotification.setNotificationMessageId( 1 );
    promoNotification.setPromotionEmailNotificationType( PromotionEmailNotificationType.lookup( PromotionEmailNotificationType.PROGRAM_LAUNCH ) );
    promoNotification.setNumberOfDays( new Integer( 5 ) );

    return promoNotification;

  }

  // Create a ClaimFormNotificationType
  public static ClaimFormNotificationType getClaimFormNotificationType( String uniqueString )
  {

    ClaimFormNotificationType claimFormNotificationType = new ClaimFormNotificationType();
    claimFormNotificationType.setNotificationMessageId( 1 );
    claimFormNotificationType.setClaimFormStepEmailNotification( getClaimFormStepEmailNotificationDomainObject( ClaimFormStepEmailNotificationType.CLAIM_APPROVED ) );
    claimFormNotificationType.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.lookup( ClaimFormStepEmailNotificationType.CLAIM_APPROVED ) );

    return claimFormNotificationType;

  }

  public static ClaimFormNotificationType getSavedClaimFormNotificationType()
  {

    // Create a ClaimForm/ClaimFormStep/ClaimFormStepEmailNotification object
    ClaimForm claimForm = getClaimFormDomainObject();
    ClaimFormStep claimFormStep = getClaimFormStepDomainObject();
    ClaimFormStepEmailNotification claimFormNotification = getClaimFormStepEmailNotificationDomainObject( ClaimFormStepEmailNotificationType.CLAIM_APPROVED );
    claimFormStep.addClaimFormStepEmailNotification( claimFormNotification );
    claimForm.addClaimFormStep( claimFormStep );
    getClaimFormDAO().saveClaimForm( claimForm );

    // Create a claim notification
    ClaimFormNotificationType expectedClaimNotification = new ClaimFormNotificationType();
    expectedClaimNotification.setNotificationMessageId( 2 );
    expectedClaimNotification.setClaimFormStepEmailNotification( claimFormNotification );
    expectedClaimNotification.setClaimFormStepEmailNotificationType( claimFormNotification.getClaimFormStepEmailNotificationType() );

    flushAndClearSession();

    return expectedClaimNotification;

  }

}

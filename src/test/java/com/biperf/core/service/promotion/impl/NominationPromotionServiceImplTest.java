
package com.biperf.core.service.promotion.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jmock.Mock;

import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.nomination.NominationSubmitDataBehaviorValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;

public class NominationPromotionServiceImplTest extends BaseServiceTest
{
  private NominationPromotionServiceImpl nominationPromotionServiceImpl;

  private Mock mockSystemVariableService = null;
  private Mock mockGamificationService = null;
  private Mock mockPromotionDao = null;

  public void setUp() throws Exception
  {
    super.setUp();

    nominationPromotionServiceImpl = new NominationPromotionServiceImpl();

    mockSystemVariableService = new Mock( SystemVariableService.class );
    mockGamificationService = new Mock( GamificationService.class );
    mockPromotionDao = new Mock( PromotionDAO.class );

    nominationPromotionServiceImpl.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
    nominationPromotionServiceImpl.setGamificationService( (GamificationService)mockGamificationService.proxy() );
    nominationPromotionServiceImpl.setPromotionDAO( (PromotionDAO)mockPromotionDao.proxy() );
  }

  public void testGetSubmissionWizardTabs()
  {
    // Passing it null should give empty list
    assertTrue( nominationPromotionServiceImpl.getSubmissionWizardTabs( null ).isEmpty() );
  }

  public void testGetECards()
  {
    // Build up what the DAO will end up returning (the expected return value from the DAO)
    List<NominationSubmitDataECardValueBean> expectedCards = new ArrayList<>();

    NominationSubmitDataECardValueBean card1 = new NominationSubmitDataECardValueBean();
    card1.setId( 9383L );
    card1.setSmallImage( "small.img" );
    card1.setLargeImage( "large.img" );
    card1.setLocale( "ab_CD" );
    card1.setTranslatable( true );
    expectedCards.add( card1 );

    mockPromotionDao.expects( once() ).method( "getECards" ).will( returnValue( expectedCards ) );

    // Also going to tell the system variable service to give us a fake locale
    PropertySetItem expectedVar = new PropertySetItem();
    expectedVar.setStringVal( "g5noms" );
    mockSystemVariableService.expects( once() ).method( "getPropertyByNameAndEnvironment" ).will( returnValue( expectedVar ) );

    // Make service call
    NominationPromotion nomPromo = new NominationPromotion();
    nomPromo.setId( 1234L );
    List<NominationSubmitDataECardValueBean> actualCards = nominationPromotionServiceImpl.getECards( nomPromo, "ab_CD" );

    // Can't just make sure list is equal. The service should modify the image name.
    assertNotNull( actualCards );
    assertEquals( actualCards.size(), 1 );
    assertTrue( "ECard image not made locale sensitive", actualCards.get( 0 ).getSmallImage().contains( "ab_CD" ) );
    assertTrue( "ECard image not made environment aware", actualCards.get( 0 ).getSmallImage().contains( "g5noms" ) );

    mockPromotionDao.verify();
    mockSystemVariableService.verify();
  }

  public void testGetSubmitBehaviors()
  {
    // Expected final result
    List<NominationSubmitDataBehaviorValueBean> expectedBehaviors = new ArrayList<>();
    NominationSubmitDataBehaviorValueBean behavior = new NominationSubmitDataBehaviorValueBean();
    behavior.setId( PromoNominationBehaviorType.ABOVE_BEYOND_CODE );
    behavior.setName( PromoNominationBehaviorType.lookup( PromoNominationBehaviorType.ABOVE_BEYOND_CODE ).getName() );
    behavior.setImage( "g5noms/small_badge.img" );
    expectedBehaviors.add( behavior );

    // Expected site URL prefix
    PropertySetItem expectedVar = new PropertySetItem();
    expectedVar.setStringVal( "g5noms" );
    mockSystemVariableService.expects( once() ).method( "getPropertyByNameAndEnvironment" ).will( returnValue( expectedVar ) );

    // Expected list of behavior types
    List<String> expectedBehaviorTypes = new ArrayList<>();
    expectedBehaviorTypes.add( PromoNominationBehaviorType.ABOVE_BEYOND_CODE );
    mockPromotionDao.expects( once() ).method( "getBehaviorTypes" ).will( returnValue( expectedBehaviorTypes ) );

    // Expected badge rule
    BadgeRule expectedBadgeRule = new BadgeRule();
    expectedBadgeRule.setBadgeLibraryCMKey( "fake_cm_key" );
    mockGamificationService.expects( once() ).method( "getBadgeRuleByBehaviorName" ).will( returnValue( expectedBadgeRule ) );

    // Expected badge library
    List<BadgeLibrary> expectedBadgeLibrary = new ArrayList<>();
    BadgeLibrary badgeLib = new BadgeLibrary();
    badgeLib.setEarnedImageSmall( "/small_badge.img" );
    expectedBadgeLibrary.add( badgeLib );
    mockGamificationService.expects( once() ).method( "getEarnedNotEarnedImageList" ).will( returnValue( expectedBadgeLibrary ) );

    // Make service call (We can give it a fake promo ID - our expectations above short circuit it,
    // anyways.)
    List<NominationSubmitDataBehaviorValueBean> actualBehaviors = nominationPromotionServiceImpl.getBehaviors( 1L, Locale.US, Locale.US );

    assertEquals( actualBehaviors.size(), expectedBehaviors.size() );
    for ( int i = 0; i < actualBehaviors.size(); ++i )
    {
      assertEquals( actualBehaviors.get( i ).getId(), expectedBehaviors.get( i ).getId() );
      assertEquals( actualBehaviors.get( i ).getName(), expectedBehaviors.get( i ).getName() );
      assertEquals( actualBehaviors.get( i ).getImage(), expectedBehaviors.get( i ).getImage() );
    }

    mockSystemVariableService.verify();
    mockPromotionDao.verify();
    mockGamificationService.verify();
  }

}

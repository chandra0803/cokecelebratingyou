
package com.biperf.core.service.gamification.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.jmock.Mock;

import com.biperf.core.dao.gamification.GamificationDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.BadgeCountType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TimeZoneId;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeBehaviorPromotion;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.impl.ParticipantServiceImpl;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.PromotionServiceImpl;
import com.biperf.core.service.promotion.impl.PromotionServiceImplTest;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

public class GamificationServiceImplTest extends BaseServiceTest
{
  private GamificationServiceImpl gamificationServicempl = new GamificationServiceImpl()
  {
    protected Locale getUserLocale()
    {
      return Locale.US;
    }
  };
  private PromotionServiceImpl promotionServiceImpl = new PromotionServiceImpl();
  private ParticipantServiceImpl participantServiceImpl = new ParticipantServiceImpl();
  private Mock mockPromotionDAO = null;
  private Mock mockParticipantDAO = null;
  private Mock mockParticipantService = null;
  private Mock mockPromotionService = null;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public GamificationServiceImplTest( String test )
  {
    super( test );
  }

  /** mockLeaderBoardDAO */
  private Mock mockGamificationDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockGamificationDAO = new Mock( GamificationDAO.class );
    mockPromotionDAO = new Mock( PromotionDAO.class );
    mockParticipantDAO = new Mock( ParticipantDAO.class );
    gamificationServicempl.setGamificationDAO( (GamificationDAO)mockGamificationDAO.proxy() );
    promotionServiceImpl.setPromotionDAO( (PromotionDAO)mockPromotionDAO.proxy() );
    participantServiceImpl.setParticipantDAO( (ParticipantDAO)mockParticipantDAO.proxy() );
    mockParticipantService = new Mock( ParticipantService.class );
    mockPromotionService = new Mock( PromotionService.class );
    gamificationServicempl.setParticipantService( (ParticipantService)mockParticipantService.proxy() );
    gamificationServicempl.setPromotionService( (PromotionService)mockPromotionService.proxy() );
  }

  /**
   * Test savebadge
   */
  public void testSaveBadge() throws ServiceErrorException
  {
    // Get the test Badge
    Badge badge = new Badge();
    badge.setBadgeType( BadgeType.lookup( BadgeType.FILELOAD ) );
    badge.setName( "testName1" );
    badge.setDisplayEndDays( new Long( 12 ) );
    badge.setStatus( Badge.BADGE_ACTIVE );
    badge.setNotificationMessageId( new Long( -1 ) );
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    badge.setAuditCreateInfo( auditCreateInfo );
    badge.setVersion( new Long( 1 ) );

    mockGamificationDAO.expects( once() ).method( "saveBadge" ).with( same( badge ) ).will( returnValue( badge ) );

    Badge badgeReturned = this.gamificationServicempl.saveBadge( badge );

    assertEquals( "Actual saved badget wasn't equal to what was expected", badge, badgeReturned );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeById
   */
  public void testGetBadgeById() throws ServiceErrorException
  {
    Badge badge = new Badge();
    badge.setId( new Long( 250 ) );

    mockGamificationDAO.expects( once() ).method( "getBadgeById" ).with( same( badge.getId() ) ).will( returnValue( badge ) );

    Badge actualBadge = gamificationServicempl.getBadgeById( badge.getId() );

    assertEquals( "Actual badge does not match to what was expected", badge, actualBadge );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeByStatus
   */
  public void testGetBadgeByStatus() throws ServiceErrorException
  {
    Badge badge = new Badge();
    badge.setStatus( Badge.BADGE_ACTIVE );

    List<Badge> expectedBadgeList = new ArrayList<Badge>();
    Badge badge1 = new Badge();
    badge1.setId( new Long( 250 ) );

    Badge badge2 = new Badge();
    badge1.setId( new Long( 251 ) );
    expectedBadgeList.add( badge1 );
    expectedBadgeList.add( badge2 );

    mockGamificationDAO.expects( once() ).method( "getBadgeByStatus" ).will( returnValue( expectedBadgeList ) );

    List<Badge> actualList = gamificationServicempl.getBadgeByStatus( badge.getStatus() );

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedBadgeList ) );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeById
   */
  public void testGetBehaviorForSelectedPromotions() throws ServiceErrorException
  {
    String promotionIds = "250,290";

    List behaviorNames = new ArrayList();
    behaviorNames.add( "innovation" );
    behaviorNames.add( "greatidea" );
    behaviorNames.add( "abovebeyond" );

    mockGamificationDAO.expects( once() ).method( "getBehaviorForSelectedPromotions" ).will( returnValue( behaviorNames ) );

    List actualBehaviors = gamificationServicempl.getBehaviorForSelectedPromotions( promotionIds, null, PromotionType.NOMINATION );

    assertEquals( "Actual behaviors does not match to what was expected", behaviorNames.size(), actualBehaviors.size() );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeBehavior promotions
   */
  public void testGetBadgeBehaviorPromotions() throws ServiceErrorException
  {
    String promotionIds = "290";

    List promotionNamesList = new ArrayList();
    List<BadgeBehaviorPromotion> expectedBadgeBehList = new ArrayList<BadgeBehaviorPromotion>();

    BadgeBehaviorPromotion badgbeh1 = new BadgeBehaviorPromotion();
    badgbeh1.setBehaviorName( "greatidea" );
    badgbeh1.setPromotionNames( "fazil_promo2" );

    BadgeBehaviorPromotion badgbeh2 = new BadgeBehaviorPromotion();
    badgbeh2.setBehaviorName( "abovebeyond" );
    badgbeh2.setPromotionNames( "fazil_promo2" );

    List<String> promoNameList = new ArrayList<String>();
    promoNameList.add( "fazil_promo2" );

    expectedBadgeBehList.add( badgbeh1 );
    expectedBadgeBehList.add( badgbeh2 );

    mockGamificationDAO.expects( once() ).method( "getPromotionsNameForBehavior" ).will( returnValue( promoNameList ) );
    mockGamificationDAO.expects( once() ).method( "getPromotionsNameForBehavior" ).will( returnValue( promoNameList ) );

    List<BadgeBehaviorPromotion> actualList = gamificationServicempl.getBadgeBehaviorPromotions( promotionIds, expectedBadgeBehList );

    String actaulPromoNames = actualList.get( 0 ).getPromotionNames();
    String expectedPromoNames = expectedBadgeBehList.get( 0 ).getPromotionNames();

    assertTrue( "Actual set didn't contain expected set for getBadgeBehaviorPromotions.", actaulPromoNames.equalsIgnoreCase( expectedPromoNames ) );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeById
   */
  /*
   * public void testGetLevelsForSelectedPromotions() throws ServiceErrorException { String
   * promotionIds="290"; List<BadgePromotionLevels> expectedLevelsList=new ArrayList();
   * BadgePromotionLevels bp1=new BadgePromotionLevels(); bp1.setCountryCode( "0" );
   * bp1.setCountryId( 0 ); bp1.setProgramId("0"); bp1.setLevelId( 270 ); bp1.setLevelName(
   * "Bronze-goal1" ); BadgePromotionLevels bp2=new BadgePromotionLevels(); bp2.setCountryCode( "0"
   * ); bp2.setCountryId( 0 ); bp2.setProgramId("0"); bp2.setLevelId( 272 ); bp2.setLevelName(
   * "test123" ); BadgePromotionLevels bp3=new BadgePromotionLevels(); bp2.setCountryCode( "0" );
   * bp2.setCountryId( 0 ); bp2.setProgramId("0"); bp2.setLevelId( 273 ); bp2.setLevelName(
   * "Silver-goal2" ); expectedLevelsList.add(bp1); expectedLevelsList.add(bp2);
   * expectedLevelsList.add(bp3); mockGamificationDAO.expects( once() ).method(
   * "getLevelsByPromotions" ).will( returnValue( expectedLevelsList ) ); List<BadgePromotionLevels>
   * actualLevelsList = gamificationServicempl.getPromotionLevels( promotionIds );
   * assertEquals("Actual levels does not match to what was expected",expectedLevelsList,
   * actualLevelsList); mockGamificationDAO.verify(); }
   */

  /**
   * Test savebadgerule
   */
  public void testSaveBadgeRule() throws ServiceErrorException
  {
    List<BadgeRule> badgeRuleList = new ArrayList<BadgeRule>();
    Badge badge = new Badge();
    badge.setId( new Long( 250 ) );
    BadgeRule badgeRule = new BadgeRule();
    badgeRule.setBadgePromotion( badge );
    badgeRule.setBadgeLibraryCMKey( "badge 1" );
    badgeRule.setBadgeDescription( "testing desc" );
    badgeRule.setBadgeName( "test badge name" );
    // badge.setBadgeCountType(BadgeCountType.lookup( "given" ));
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    badgeRule.setAuditCreateInfo( auditCreateInfo );
    badgeRule.setVersion( new Long( 1 ) );
    badgeRuleList.add( badgeRule );
    mockGamificationDAO.expects( once() ).method( "saveBadgeRule" ).with( same( badgeRule ) ).will( returnValue( badgeRule ) );

    BadgeRule badgeRuleReturned = this.gamificationServicempl.saveBadgeRules( badgeRuleList );

    assertEquals( "Actual saved badge rule wasn't equal to what was expected", badgeRule, badgeRuleReturned );

    mockGamificationDAO.verify();

  }

  /**
   * Test savebadge promotion
   */
  public void testSaveBadgePromotion() throws ServiceErrorException
  {
    Badge badge = new Badge();
    badge.setId( new Long( 250 ) );
    BadgePromotion badgePromo = new BadgePromotion();
    badgePromo.setBadgePromotion( badge );
    RecognitionPromotion recPromo = new RecognitionPromotion();
    recPromo.setId( new Long( 290 ) );
    badgePromo.setEligiblePromotion( recPromo );

    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    badgePromo.setAuditCreateInfo( auditCreateInfo );
    badgePromo.setVersion( new Long( 1 ) );
    mockGamificationDAO.expects( once() ).method( "saveBadgePromotion" ).with( same( badgePromo ) ).will( returnValue( badgePromo ) );

    BadgePromotion badgePromoReturned = this.gamificationServicempl.saveBadgePromotion( badgePromo );

    assertEquals( "Actual saved badge promotion wasn't equal to what was expected", badgePromo, badgePromoReturned );

    mockGamificationDAO.verify();

  }

  /**
   * Test isBadgeExists
   */
  public void testisBadgeExists() throws ServiceErrorException
  {
    String badgeName = "fazil1";
    String badgeId = "5066";
    String badgeExists = "Y";

    mockGamificationDAO.expects( once() ).method( "isBadgeNameExists" ).will( returnValue( badgeExists ) );

    String actualBadgeExists = gamificationServicempl.isBadgeNameExists( badgeName, badgeId );

    assertTrue( "Actual badge does not match to what was expected", badgeExists.equals( actualBadgeExists ) );

    mockGamificationDAO.verify();

  }

  public void testIsUserHasActiveBadges()
  {
    List<ParticipantBadge> expectedPartcipantBadgeList = new ArrayList<ParticipantBadge>();
    Badge b = new Badge();
    b.setId( new Long( 273 ) );
    Badge b1 = new Badge();
    b1.setId( new Long( 270 ) );
    ParticipantBadge pBadge = new ParticipantBadge();
    User user = new User();
    user.setId( new Long( 5585 ) );
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );
    ParticipantBadge pBadge1 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( true );

    ParticipantBadge pBadge2 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b );
    pBadge2.setIsEarned( false );

    expectedPartcipantBadgeList.add( pBadge );
    expectedPartcipantBadgeList.add( pBadge1 );
    expectedPartcipantBadgeList.add( pBadge2 );
    List<Long> expectedPartcipantBadgeIdsList = new ArrayList<Long>();
    expectedPartcipantBadgeIdsList.add( b.getId() );
    expectedPartcipantBadgeIdsList.add( b1.getId() );
    
    mockGamificationDAO.expects( once() ).method( "getBadgeEligiblePromotionIds" ).will( returnValue( expectedPartcipantBadgeIdsList ) );
    mockGamificationDAO.expects( once() ).method( "isUserHasActiveBadges" ).will( returnValue( 0 ) );
    assertTrue( gamificationServicempl.isUserHasActiveBadges( 5585L, new ArrayList() ) == 0 );
  }

  /**
   * Test getBadgeByParticipant id
   */
  public void testGetBadgeByParticipant() throws ServiceErrorException
  {
    Long partcipantId = new Long( 5585 );
    Badge b = new Badge();
    b.setId( new Long( 273 ) );
    Badge b1 = new Badge();
    b1.setId( new Long( 270 ) );

    List<ParticipantBadge> expectedPartcipantBadgeList = new ArrayList<ParticipantBadge>();
    ParticipantBadge pBadge = new ParticipantBadge();
    User user = new User();
    user.setId( new Long( 5585 ) );
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );
    ParticipantBadge pBadge1 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( true );

    ParticipantBadge pBadge2 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b );
    pBadge2.setIsEarned( false );

    expectedPartcipantBadgeList.add( pBadge );
    expectedPartcipantBadgeList.add( pBadge1 );
    expectedPartcipantBadgeList.add( pBadge2 );

    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantId" ).will( returnValue( expectedPartcipantBadgeList ) );

    List<ParticipantBadge> actualList = gamificationServicempl.getBadgeByParticipantId( partcipantId );

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedPartcipantBadgeList ) );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeByParticipant id
   */
  public void testGetBadgeByParticipantSorted() throws ServiceErrorException
  {
    Long partcipantId = new Long( 5585 );
    Badge b = new Badge();
    b.setId( new Long( 274 ) );
    Badge b1 = new Badge();
    b1.setId( new Long( 273 ) );
    b1.setBadgeType( BadgeType.lookup( "progress" ) );
    b1.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    Badge b2 = new Badge();
    b2.setId( new Long( 276 ) );

    Badge b3 = new Badge();
    b3.setId( new Long( 270 ) );
    Badge b4 = new Badge();
    b4.setId( new Long( 255 ) );

    BadgeRule br1 = new BadgeRule();
    br1.setBadgePromotion( b3 );
    br1.setId( 250L );
    br1.setBehaviorName( "abovebeyond" );

    BadgeRule br2 = new BadgeRule();
    br1.setBadgePromotion( b4 );
    br1.setId( 252L );
    br1.setBehaviorName( "innovation" );

    List<BadgeRule> expectedBadgeRules = new ArrayList<BadgeRule>();
    expectedBadgeRules.add( br1 );
    expectedBadgeRules.add( br2 );

    BadgeRule br3 = new BadgeRule();
    br3.setBadgePromotion( b1 );
    br3.setId( 277L );
    br3.setMaximumQualifier( 3L );

    BadgeRule br4 = new BadgeRule();
    br4.setBadgePromotion( b1 );
    br4.setId( 278L );
    br4.setMaximumQualifier( 5L );

    List<BadgeRule> badgeRulesProgressNotStarted = new ArrayList<BadgeRule>();
    badgeRulesProgressNotStarted.add( br3 );
    badgeRulesProgressNotStarted.add( br4 );

    List<ParticipantBadge> expectedPartcipantBadgeEarnedList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> expectedPartcipantBadgeEarnedListOld = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> expectedPartcipantBadgeProgressList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> expectedPartcipantBadgeBehaviorList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> expectedPartcipantBadgeBehaviorListNotEarned = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressNotStartedBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> expectedPartcipantBadgeMaster = new ArrayList<ParticipantBadge>();

    // Earned Badge
    ParticipantBadge pBadge = new ParticipantBadge();
    User user = new User();
    user.setId( new Long( 5585 ) );
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );

    ParticipantBadge pBadgeOld = new ParticipantBadge();
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );

    // End of Earned Badge

    // Progress >0 badges
    ParticipantBadge pBadge1 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( false );

    ParticipantBadge pBadge2 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b2 );
    pBadge2.setIsEarned( false );
    // end of progress>0

    // behavior badges
    ParticipantBadge pBadge3 = new ParticipantBadge();
    pBadge3.setParticipant( user );
    pBadge3.setBadgePromotion( b3 );
    pBadge3.setIsEarned( true );

    ParticipantBadge pBadge4 = new ParticipantBadge();
    pBadge4.setParticipant( user );
    pBadge4.setBadgePromotion( b4 );
    pBadge4.setIsEarned( true );

    // behavior earned badges end
    ParticipantBadge pBadge5 = new ParticipantBadge();
    pBadge3.setParticipant( user );
    pBadge3.setBadgePromotion( b3 );
    pBadge3.setIsEarned( false );

    ParticipantBadge pBadge6 = new ParticipantBadge();
    pBadge4.setParticipant( user );
    pBadge4.setBadgePromotion( b4 );
    pBadge4.setIsEarned( false );

    // behavior not earned badges

    // end of behavior badges

    // Progress =0 badges
    ParticipantBadge pBadge10 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( false );

    ParticipantBadge pBadge11 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b1 );
    pBadge2.setIsEarned( false );
    // end of progress=0

    expectedPartcipantBadgeEarnedList.add( pBadge );
    expectedPartcipantBadgeEarnedListOld.add( pBadgeOld );
    expectedPartcipantBadgeProgressList.add( pBadge1 );
    expectedPartcipantBadgeProgressList.add( pBadge2 );

    expectedPartcipantBadgeBehaviorList.add( pBadge3 );
    expectedPartcipantBadgeBehaviorList.add( pBadge4 );

    expectedPartcipantBadgeBehaviorListNotEarned.add( pBadge5 );
    expectedPartcipantBadgeBehaviorListNotEarned.add( pBadge6 );

    progressNotStartedBadgeList.add( pBadge10 );
    progressNotStartedBadgeList.add( pBadge11 );

    expectedPartcipantBadgeMaster.addAll( expectedPartcipantBadgeEarnedList );
    expectedPartcipantBadgeMaster.addAll( expectedPartcipantBadgeProgressList );
    expectedPartcipantBadgeMaster.addAll( expectedPartcipantBadgeBehaviorList );
    expectedPartcipantBadgeMaster.addAll( expectedPartcipantBadgeBehaviorListNotEarned );
    expectedPartcipantBadgeMaster.addAll( expectedPartcipantBadgeEarnedListOld );
    expectedPartcipantBadgeMaster.addAll( progressNotStartedBadgeList );

    Participant partcipant = new Participant();
    partcipant.setId( 5585L );
    // this.participantServiceImpl.saveParticipant( partcipant );
    mockParticipantService.expects( atLeastOnce() ).method( "getParticipantById" );

    // mockParticipantService.expects( once() ).method( "getParticipantById" ).with( same(
    // partcipant ) );

    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantEarnedHighLight" ).will( returnValue( expectedPartcipantBadgeEarnedList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantProgress" ).will( returnValue( expectedPartcipantBadgeProgressList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantBehavior" ).will( returnValue( expectedPartcipantBadgeBehaviorList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantBehaviorNotEarned" ).will( returnValue( expectedBadgeRules ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantEarnedOld" ).will( returnValue( expectedPartcipantBadgeEarnedListOld ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantProgressNotStarted" ).will( returnValue( badgeRulesProgressNotStarted ) );
    List<PromotionMenuBean> eligiblePromotions = new ArrayList();
    PromotionMenuBean promoMenu = new PromotionMenuBean();
    RecognitionPromotion promo = new RecognitionPromotion();
    promo.setId( 252L );
    promo.setBehaviorActive( true );
    Promotion promotion = (Promotion)promo;

    promotion.setPromotionType( PromotionType.lookup( "picklist.promotiontype.recognition" ) );

    promoMenu.setPromotion( promotion );
    eligiblePromotions.add( promoMenu );

    List<ParticipantBadge> actualList = gamificationServicempl.getBadgeByParticipantSorted( eligiblePromotions, partcipantId );

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedPartcipantBadgeMaster ) );

    mockGamificationDAO.verify();

  }

  /**
   * Test getBadgeByParticipant id
   */
  public void testGetBadgeByParticipantProfileSorted() throws ServiceErrorException
  {
    Long partcipantId = new Long( 5585 );
    Badge b = new Badge();
    b.setId( new Long( 274 ) );
    Badge b1 = new Badge();
    b1.setId( new Long( 261 ) );
    b1.setBadgeType( BadgeType.lookup( "progress" ) );
    b1.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    Badge b2 = new Badge();
    b2.setId( new Long( 276 ) );

    Badge b3 = new Badge();
    b3.setId( new Long( 260 ) );
    Badge b4 = new Badge();
    b4.setId( new Long( 276 ) );

    Badge b5 = new Badge();
    b3.setId( new Long( 280 ) );
    Badge b6 = new Badge();
    b4.setId( new Long( 281 ) );

    Badge historyBadge1 = new Badge();
    b4.setId( new Long( 250 ) );

    Badge historyBadge2 = new Badge();
    b4.setId( new Long( 251 ) );

    BadgeRule br1 = new BadgeRule();
    br1.setBadgePromotion( b3 );
    br1.setId( 276L );
    br1.setBehaviorName( "abovebeyond" );

    BadgeRule br2 = new BadgeRule();
    br1.setBadgePromotion( b4 );
    br1.setId( 275L );
    br1.setBehaviorName( "innovation" );

    List<BadgeRule> expectedBadgeRules = new ArrayList<BadgeRule>();
    expectedBadgeRules.add( br1 );
    expectedBadgeRules.add( br2 );

    BadgeRule br3 = new BadgeRule();
    br3.setBadgePromotion( b1 );
    br3.setId( 277L );
    br3.setMaximumQualifier( 3L );

    BadgeRule br4 = new BadgeRule();
    br4.setBadgePromotion( b1 );
    br4.setId( 278L );
    br4.setMaximumQualifier( 5L );

    List<BadgeRule> badgeRulesProgressNotStarted = new ArrayList<BadgeRule>();
    badgeRulesProgressNotStarted.add( br3 );
    badgeRulesProgressNotStarted.add( br4 );

    List<ParticipantBadge> earnedBadgeProgressList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> behaviorBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> earnedfileLoadList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> historyBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> masterPartcipantList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> behaviorNotEarnedBadgeList = new ArrayList<ParticipantBadge>();
    List<ParticipantBadge> progressNotStartedBadgeList = new ArrayList<ParticipantBadge>();

    // Earned Badge
    ParticipantBadge pBadge = new ParticipantBadge();
    User user = new User();
    user.setId( new Long( 5585 ) );
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );

    ParticipantBadge pBadgeOld = new ParticipantBadge();
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );

    // End of Earned Badge

    // Progress >0 badges
    ParticipantBadge pBadge1 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( false );

    ParticipantBadge pBadge2 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b2 );
    pBadge2.setIsEarned( false );
    // end of progress>0

    // Progress =0 badges
    ParticipantBadge pBadge10 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( false );

    ParticipantBadge pBadge11 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b1 );
    pBadge2.setIsEarned( false );
    // end of progress=0

    // behavior badges
    ParticipantBadge pBadge3 = new ParticipantBadge();
    pBadge3.setParticipant( user );
    pBadge3.setBadgePromotion( b3 );
    pBadge3.setIsEarned( true );

    ParticipantBadge pBadge4 = new ParticipantBadge();
    pBadge4.setParticipant( user );
    pBadge4.setBadgePromotion( b4 );
    pBadge4.setIsEarned( true );

    // behavior earned badges end
    ParticipantBadge pBadge5 = new ParticipantBadge();
    pBadge3.setParticipant( user );
    pBadge3.setBadgePromotion( b3 );
    pBadge3.setIsEarned( false );

    ParticipantBadge pBadge6 = new ParticipantBadge();
    pBadge4.setParticipant( user );
    pBadge4.setBadgePromotion( b4 );
    pBadge4.setIsEarned( false );

    // behavior earned badges end
    ParticipantBadge pBadge7 = new ParticipantBadge();
    pBadge7.setParticipant( user );
    pBadge7.setBadgePromotion( b5 );
    pBadge7.setIsEarned( true );

    ParticipantBadge pBadge8 = new ParticipantBadge();
    pBadge8.setParticipant( user );
    pBadge8.setBadgePromotion( b6 );
    pBadge8.setIsEarned( true );

    // behavior not earned badges

    ParticipantBadge hisBadge1 = new ParticipantBadge();
    hisBadge1.setParticipant( user );
    hisBadge1.setBadgePromotion( historyBadge1 );
    hisBadge1.setIsEarned( true );

    ParticipantBadge hisBadge2 = new ParticipantBadge();
    hisBadge2.setParticipant( user );
    hisBadge2.setBadgePromotion( historyBadge2 );
    hisBadge2.setIsEarned( true );

    // end of behavior badges

    earnedBadgeProgressList.add( pBadge );

    progressBadgeList.add( pBadge1 );
    progressBadgeList.add( pBadge2 );

    progressNotStartedBadgeList.add( pBadge10 );
    progressNotStartedBadgeList.add( pBadge11 );

    behaviorBadgeList.add( pBadge3 );
    behaviorBadgeList.add( pBadge4 );

    behaviorNotEarnedBadgeList.add( pBadge5 );
    behaviorNotEarnedBadgeList.add( pBadge6 );

    earnedfileLoadList.add( pBadge7 );
    earnedfileLoadList.add( pBadge8 );

    historyBadgeList.add( hisBadge1 );
    historyBadgeList.add( hisBadge2 );

    masterPartcipantList.addAll( earnedBadgeProgressList );
    masterPartcipantList.addAll( progressBadgeList );
    masterPartcipantList.addAll( progressNotStartedBadgeList );
    masterPartcipantList.addAll( behaviorBadgeList );
    masterPartcipantList.addAll( behaviorNotEarnedBadgeList );
    masterPartcipantList.addAll( earnedfileLoadList );
    masterPartcipantList.addAll( historyBadgeList );
    Participant partcipant = new Participant();
    partcipant.setId( 5585L );
    // mockParticipantService.expects( atLeastOnce() ).method( "getParticipantById" )
    mockParticipantService.expects( atLeastOnce() ).method( "getParticipantById" );
    // .with( same( partcipant ) );

    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantEarnedProgress" ).will( returnValue( earnedBadgeProgressList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantNotEarnedProgress" ).will( returnValue( progressBadgeList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantProgressNotStarted" ).will( returnValue( badgeRulesProgressNotStarted ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantBehavior" ).will( returnValue( behaviorBadgeList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantBehaviorNotEarned" ).will( returnValue( expectedBadgeRules ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeByParticipantEarnedFileLoad" ).will( returnValue( earnedfileLoadList ) );
    mockGamificationDAO.expects( once() ).method( "getBadgeParticipantHistory" ).will( returnValue( historyBadgeList ) );
    List<PromotionMenuBean> eligiblePromotions = new ArrayList();
    PromotionMenuBean promoMenu = new PromotionMenuBean();
    RecognitionPromotion promo = new RecognitionPromotion();
    promo.setId( 252L );
    promo.setBehaviorActive( true );
    Promotion promotion = (Promotion)promo;

    promotion.setPromotionType( PromotionType.lookup( "picklist.promotiontype.recognition" ) );

    promoMenu.setPromotion( promotion );
    eligiblePromotions.add( promoMenu );

    List<ParticipantBadge> actualList = gamificationServicempl.getBadgeByParticipantProfileSorted( eligiblePromotions, partcipantId, 0 );

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( masterPartcipantList ) );

    mockGamificationDAO.verify();

  }

  public void testGetLiveCompletedPromotionList() throws ServiceErrorException
  {
    List promotionList = getPromotionList();

    mockPromotionService.expects( once() ).method( "getPromotionListWithAssociations" ).will( returnValue( promotionList ) );

    // mockPromotionDAO.expects( once() ).method( "getPromotionListWithAssociations" ).will(
    // returnValue( promotionList ) );

    List actualList = gamificationServicempl.getLiveCompletedPromotionList();
    assertTrue( "Actual promotion list don't contain expected set for getLiveCompletedPromotionList.", actualList.size() == promotionList.size() );

    mockGamificationDAO.verify();

  }

  /**
   * Test populate Partcipants for progress badge
   */
  public void testPopulatePartcipantBadgeProgress() throws ServiceErrorException
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( 5584L );

    Participant receiver = new Participant();
    receiver.setId( 5585L );

    List<Badge> badges = new ArrayList<Badge>();
    Badge b = new Badge();
    b = buildBadge( "progress", "progress1" );
    b.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    b = buildBadgeRules( b, "progress" );
    Badge b1 = new Badge();
    b1 = buildBadge( "progress", "progress2" );
    b1.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    b1 = buildBadgeRules( b1, "progress" );
    badges.add( b );
    badges.add( b1 );

    String approvalTypeCode = ApprovalType.AUTOMATIC_IMMEDIATE;

    Claim inputClaim = buildTestClaim( userToReturn, approvalTypeCode );

    ParticipantBadge expectedParticpantSender = new ParticipantBadge();

    expectedParticpantSender.setParticipant( userToReturn );

    ParticipantBadge expectedParticpantReceiver = new ParticipantBadge();

    expectedParticpantReceiver.setParticipant( receiver );

    ParticipantBadge participantProgress = null;

    mockGamificationDAO.expects( once() ).method( "getBadgeByBadgeIdAndPromotionId" ).will( returnValue( badges ) );
    // mockGamificationDAO.expects( once() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    mockGamificationDAO.expects( atLeastOnce() ).method( "getParticipantBadgeByBadgeRule" ).will( returnValue( participantProgress ) );

    // expectedParticpantSender=partcipantImplTest.buildStaticParticipant();

    /*
     * mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).with( same(
     * expectedParticpantSender ) ) .will( returnValue( expectedParticpantSender ) );
     * mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).with( same(
     * expectedParticpantReceiver ) ) .will( returnValue( expectedParticpantReceiver ) );
     */

    mockGamificationDAO.expects( atLeastOnce() ).method( "saveParticipantBadge" ).will( returnValue( expectedParticpantSender ) );
    // mockGamificationDAO.expects( once() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    // mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).will( returnValue(
    // expectedParticpantReceiver ) );

    // mockGamificationDAO.expects( atLeastOnce() ).method( "saveParticipantBadge" );
    // mockGamificationDAO.expects( atLeastOnce() ).method( "saveParticipantBadge" );
    mockGamificationDAO.expects( atLeastOnce() ).method( "getSessionLockForParticipantBadge" );
    mockGamificationDAO.expects( atLeastOnce() ).method( "canCreateJournal" ).will( returnValue( 0 ) );

    List<ParticipantBadge> actualParticpant1 = this.gamificationServicempl.populateBadgePartcipant( inputClaim );

    assertTrue( "Populate partcipant badge not equal to what was expected", expectedParticpantReceiver.getId() == actualParticpant1.get( 0 ).getId() );

    mockGamificationDAO.verify();

    /*
     * mockGamificationDAO.expects( once() ).method( "getBadgeByPromotion" ).will( returnValue(
     * badges ) ); mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).with(
     * same( expectedParticpant ) ) .will( returnValue( expectedParticpant ) ); ParticipantBadge
     * actualParticpant2= this.gamificationServicempl.populateBadgePartcipant( inputClaim );
     * assertEquals(
     * "Populate partcipant badge not equal to what was expected",expectedParticpant.getId(),
     * actualParticpant2.getId() ); mockGamificationDAO.verify();
     */

  }

  /**
   * Test populate Partcipants for progress badge
   */
  public void testPopulatePartcipantBadgeEarned() throws ServiceErrorException
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( 5584L );

    Participant receiver = new Participant();
    receiver.setId( 5585L );

    List<Badge> badges = new ArrayList<Badge>();
    Badge b = new Badge();
    b = buildBadge( "earned", "earned1" );
    b = buildBadgeRules( b, "earned" );
    Badge b1 = new Badge();
    b1 = buildBadge( "earned", "earned2" );
    b1 = buildBadgeRules( b1, "progress" );
    badges.add( b );
    // badges.add(b1);

    String approvalTypeCode = ApprovalType.AUTOMATIC_IMMEDIATE;

    Claim inputClaim = buildTestClaim( userToReturn, approvalTypeCode );

    ParticipantBadge expectedParticpantSender = new ParticipantBadge();

    expectedParticpantSender.setParticipant( userToReturn );

    ParticipantBadge expectedParticpantReceiver = new ParticipantBadge();

    expectedParticpantReceiver.setParticipant( receiver );

    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( new Long( 1 ) );
    authUser.setTimeZoneId( TimeZoneId.IST );
    authUser.setPrimaryCountryCode( Country.UNITED_STATES );
    UserManager.setUser( authUser );

    ParticipantBadge participantProgress = null;

    mockGamificationDAO.expects( once() ).method( "getBadgeByBadgeIdAndPromotionId" ).will( returnValue( badges ) );
    // mockGamificationDAO.expects( once() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    // mockGamificationDAO.expects( atLeastOnce() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    mockGamificationDAO.expects( atLeastOnce() ).method( "getParticipantBadgeByBadgeLib" ).will( returnValue( participantProgress ) );

    // expectedParticpantSender=partcipantImplTest.buildStaticParticipant();

    /*
     * mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).with( same(
     * expectedParticpantSender ) ) .will( returnValue( expectedParticpantSender ) );
     * mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).with( same(
     * expectedParticpantReceiver ) ) .will( returnValue( expectedParticpantReceiver ) );
     */

    mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).will( returnValue( expectedParticpantReceiver ) );
    // mockGamificationDAO.expects( once() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    // mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).will( returnValue(
    // expectedParticpantReceiver ) );

    // mockGamificationDAO.expects( atLeastOnce() ).method( "saveParticipantBadge" );
    // mockGamificationDAO.expects( atLeastOnce() ).method( "saveParticipantBadge" );

    List<ParticipantBadge> actualParticpant1 = this.gamificationServicempl.populateBadgePartcipant( inputClaim );

    assertTrue( "Populate partcipant badge not equal to what was expected", expectedParticpantReceiver.getId() == actualParticpant1.get( 0 ).getId() );

    mockGamificationDAO.verify();

    /*
     * mockGamificationDAO.expects( once() ).method( "getBadgeByPromotion" ).will( returnValue(
     * badges ) ); mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).with(
     * same( expectedParticpant ) ) .will( returnValue( expectedParticpant ) ); ParticipantBadge
     * actualParticpant2= this.gamificationServicempl.populateBadgePartcipant( inputClaim );
     * assertEquals(
     * "Populate partcipant badge not equal to what was expected",expectedParticpant.getId(),
     * actualParticpant2.getId() ); mockGamificationDAO.verify();
     */

  }

  /**
   * Test populate Partcipants for progress badge
   */
  public void testPopulatePartcipantBadgeBehavior() throws ServiceErrorException
  {
    Participant userToReturn = new Participant();
    userToReturn.setId( 5584L );

    Participant receiver = new Participant();
    receiver.setId( 5585L );

    List<Badge> badges = new ArrayList<Badge>();
    Badge b = new Badge();
    b = buildBadge( "behavior", "behavior1 test" );
    b.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    b = buildBadgeRules( b, "behavior" );
    Badge b1 = new Badge();
    b1 = buildBadge( "behavior", "behavior2 test" );
    b1.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    b1 = buildBadgeRules( b1, "behavior" );
    badges.add( b );
    badges.add( b1 );

    String approvalTypeCode = ApprovalType.AUTOMATIC_IMMEDIATE;

    Claim inputClaim = buildTestClaim( userToReturn, approvalTypeCode );

    ParticipantBadge expectedParticpantSender = new ParticipantBadge();

    expectedParticpantSender.setParticipant( userToReturn );

    ParticipantBadge expectedParticpantReceiver = new ParticipantBadge();

    expectedParticpantReceiver.setParticipant( receiver );

    ParticipantBadge participantProgress = null;

    mockGamificationDAO.expects( once() ).method( "getBadgeByBadgeIdAndPromotionId" ).will( returnValue( badges ) );
    // mockGamificationDAO.expects( once() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    // mockGamificationDAO.expects( atLeastOnce() ).method( "getParticipantBadgeByBadgeRule" ).will(
    // returnValue( participantProgress ) );
    mockGamificationDAO.expects( atLeastOnce() ).method( "getParticipantBadgeByBadgeLib" ).will( returnValue( participantProgress ) );

    mockGamificationDAO.expects( atLeastOnce() ).method( "saveParticipantBadge" ).will( returnValue( expectedParticpantReceiver ) );
    // mockGamificationDAO.expects( once() ).method( "saveParticipantBadge" ).will( returnValue(
    // expectedParticpantReceiver ) );
    mockGamificationDAO.expects( atLeastOnce() ).method( "canCreateJournal" ).will( returnValue( 0 ) );
    mockGamificationDAO.expects( atLeastOnce() ).method( "getBehaviorEarnedCount" ).will( returnValue( 0 ) );

    List<ParticipantBadge> actualParticpant1 = this.gamificationServicempl.populateBadgePartcipant( inputClaim );

    assertTrue( "Populate partcipant badge not equal to what was expected", expectedParticpantReceiver.getId() == actualParticpant1.get( 0 ).getId() );

    mockGamificationDAO.verify();

  }

  private RecognitionClaim buildTestClaim( Participant userToReturn, String approvalTypeCode )
  {
    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setId( new Long( 253 ) );
    promotion.setApprovalType( ApprovalType.lookup( approvalTypeCode ) );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setSubmissionStartDate( new Date( new Date().getTime() - ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotion.setSubmissionEndDate( new Date( new Date().getTime() + ( DateUtils.MILLIS_PER_DAY * 2 ) ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setAwardActive( true );
    promotion.setAwardAmountFixed( 100L );
    ClaimForm claimForm = new ClaimForm();
    claimForm.setCmAssetCode( "123456" );
    promotion.setClaimForm( claimForm );
    RecognitionClaim claim = new RecognitionClaim();
    claim.setSubmissionDate( new Date() );
    claim.setPromotion( promotion );
    claim.setSubmitter( userToReturn );

    claim.setBehavior( PromoRecognitionBehaviorType.getDefaultItem().lookup( "innovation" ) );

    Set claimRecognitions = new LinkedHashSet();
    ClaimRecipient claimRecognition = new ClaimRecipient();
    Participant recipient = new Participant();
    recipient.setId( 5585L );
    recipient.setFirstName( "George Michael" );
    recipient.setLastName( "Bluth" );
    recipient.setDepartmentType( "Banana Stand" );
    recipient.setPositionType( "Sales Clerk" );
    claimRecognition.setRecipient( recipient );
    claimRecognition.setClaim( claim );
    claimRecognition.setAwardQuantity( new Long( 10 ) );
    claimRecognition.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );
    claimRecognition.setId( new Long( 1 ) );
    claimRecognitions.add( claimRecognition );
    /*
     * Product product1 = ProductDAOImplTest .buildStaticProductDomainObject( "product12",
     * ProductDAOImplTest .getProductCategoryDomainObject( "cat12" ) ); ClaimProduct claimProduct1 =
     * new ClaimProduct( product1, 1 ); claimProduct1.setApprovalStatusType(
     * ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) ); claimProduct1.setQuantity( 5 );
     * claim.addClaimProduct( claimProduct1 );
     */
    claim.setClaimRecipients( claimRecognitions );

    return claim;
  }

  private Badge buildBadge( String type, String name ) throws ServiceErrorException
  {
    Badge badge = new Badge();
    badge.setBadgeType( BadgeType.lookup( type ) );
    badge.setName( name );
    badge.setDisplayEndDays( new Long( 12 ) );
    badge.setStatus( Badge.BADGE_ACTIVE );
    badge.setNotificationMessageId( new Long( -1 ) );
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    badge.setAuditCreateInfo( auditCreateInfo );
    badge.setVersion( new Long( 1 ) );

    mockGamificationDAO.expects( once() ).method( "saveBadge" ).with( same( badge ) ).will( returnValue( badge ) );

    Badge badgeReturned = this.gamificationServicempl.saveBadge( badge );
    return badgeReturned;

  }

  private Badge buildBadgeRules( Badge badge, String badgeType ) throws ServiceErrorException
  {
    List<BadgeRule> badgeRuleList = new ArrayList<BadgeRule>();

    BadgeRule badgeRule = new BadgeRule();
    badgeRule.setBadgePromotion( badge );
    badgeRule.setBadgeLibraryCMKey( "badge1" );
    badgeRule.setBadgeDescription( "testing desc" );
    badgeRule.setBadgeName( "test badge name" );

    BadgeRule badgeRule1 = new BadgeRule();
    badgeRule1.setBadgePromotion( badge );
    badgeRule1.setBadgeLibraryCMKey( "badge2" );
    badgeRule1.setBadgeDescription( "testing desc2" );
    badgeRule1.setBadgeName( "test badge name2" );

    if ( badgeType.equalsIgnoreCase( "progress" ) )
    {
      badgeRule.setMaximumQualifier( 5L );
      badgeRule1.setMaximumQualifier( 8L );

    }
    else if ( badgeType.equalsIgnoreCase( "behavior" ) )
    {
      badgeRule.setBehaviorName( "innovation" );
      badgeRule1.setBehaviorName( "abovebeyond" );

    }
    else if ( badgeType.equalsIgnoreCase( "earned" ) )
    {
      badgeRule.setMinimumQualifier( 10L );
      badgeRule.setMaximumQualifier( 25L );
      badgeRule.setLevelName( "bronze" );
      badgeRule1.setMinimumQualifier( 26L );
      badgeRule1.setMaximumQualifier( 50L );
      badgeRule1.setLevelName( "silver" );

    }
    // badge.setBadgeCountType(BadgeCountType.lookup( "given" ));
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    badgeRule.setAuditCreateInfo( auditCreateInfo );
    badgeRule.setVersion( new Long( 1 ) );
    badgeRule1.setAuditCreateInfo( auditCreateInfo );
    badgeRule1.setVersion( new Long( 1 ) );
    badgeRuleList.add( badgeRule );
    // badgeRuleList.add( badgeRule1 );
    mockGamificationDAO.expects( once() ).method( "saveBadgeRule" ).with( same( badgeRule ) ).will( returnValue( badgeRule ) );

    // mockGamificationDAO.expects( once() ).method( "saveBadgeRule" ).with( same( badgeRule1 ) )
    // .will( returnValue( badgeRule1 ) );

    BadgeRule badgeRuleReturned = this.gamificationServicempl.saveBadgeRules( badgeRuleList );
    Set badgeRuleSet = new HashSet( badgeRuleList );
    badge.setBadgeRules( badgeRuleSet );
    return badge;
  }

  /**
   * Test getBadgeByParticipant id
   */
  public void testGetBadgesForRecognitionConfirmation() throws ServiceErrorException
  {
    Long partcipantId = new Long( 5585 );
    Badge b = new Badge();
    b.setId( new Long( 274 ) );
    Badge b1 = new Badge();
    b1.setId( new Long( 273 ) );
    b1.setBadgeType( BadgeType.lookup( "progress" ) );
    b1.setBadgeCountType( BadgeCountType.lookup( "total" ) );
    Badge b2 = new Badge();
    b2.setId( new Long( 276 ) );

    Badge b3 = new Badge();
    b3.setId( new Long( 270 ) );
    Badge b4 = new Badge();
    b4.setId( new Long( 255 ) );

    BadgeRule br3 = new BadgeRule();
    br3.setBadgePromotion( b1 );
    br3.setId( 277L );
    br3.setMaximumQualifier( 3L );

    BadgeRule br4 = new BadgeRule();
    br4.setBadgePromotion( b1 );
    br4.setId( 278L );
    br4.setMaximumQualifier( 5L );

    List<ParticipantBadge> expectedPartcipantBadgeList = new ArrayList<ParticipantBadge>();

    // Earned Badge
    ParticipantBadge pBadge = new ParticipantBadge();
    User user = new User();
    user.setId( new Long( 5585 ) );
    pBadge.setParticipant( user );
    pBadge.setBadgePromotion( b );
    pBadge.setIsEarned( true );

    // End of Earned Badge

    // Progress >0 badges
    ParticipantBadge pBadge1 = new ParticipantBadge();
    pBadge1.setParticipant( user );
    pBadge1.setBadgePromotion( b1 );
    pBadge1.setIsEarned( false );
    pBadge1.setSentCount( 1L );

    ParticipantBadge pBadge2 = new ParticipantBadge();
    pBadge2.setParticipant( user );
    pBadge2.setBadgePromotion( b2 );
    pBadge2.setIsEarned( false );
    pBadge2.setSentCount( 2L );
    // end of progress>0

    expectedPartcipantBadgeList.add( pBadge );
    expectedPartcipantBadgeList.add( pBadge1 );
    expectedPartcipantBadgeList.add( pBadge2 );

    Participant partcipant = new Participant();
    partcipant.setId( 5585L );
    // this.participantServiceImpl.saveParticipant( partcipant );

    // mockParticipantService.expects( once() ).method( "getParticipantById" ).with( same(
    // partcipant ) );

    mockGamificationDAO.expects( once() ).method( "getBadgesForRecognitionConfirmationScreen" ).will( returnValue( expectedPartcipantBadgeList ) );

    List<PromotionMenuBean> eligiblePromotions = new ArrayList();
    PromotionMenuBean promoMenu = new PromotionMenuBean();
    RecognitionPromotion promo = new RecognitionPromotion();
    promo.setId( 252L );
    promo.setBehaviorActive( true );
    Promotion promotion = (Promotion)promo;

    promotion.setPromotionType( PromotionType.lookup( "picklist.promotiontype.recognition" ) );

    promoMenu.setPromotion( promotion );
    eligiblePromotions.add( promoMenu );

    List<ParticipantBadge> actualList = gamificationServicempl.getBadgesForRecognitionConfirmationScreen( promo.getId(), partcipant.getId() );

    assertTrue( "Actual set didn't contain expected set for getAll.", actualList.containsAll( expectedPartcipantBadgeList ) );

    mockGamificationDAO.verify();

  }

  /**
   * Build a list of promotion domain objects for testing.
   * 
   * @return List
   */
  private List getPromotionList()
  {
    List promotionList = new ArrayList();

    Promotion promotion1 = PromotionServiceImplTest.buildProductClaimPromotion( "100" );
    promotion1.setId( new Long( 1 ) );

    Promotion promotion2 = PromotionServiceImplTest.buildProductClaimPromotion( "101" );
    promotion2.setId( new Long( 2 ) );

    Promotion promotion3 = PromotionServiceImplTest.buildProductClaimPromotion( "102" );
    promotion3.setId( new Long( 3 ) );

    promotionList.add( promotion1 );
    promotionList.add( promotion2 );
    promotionList.add( promotion3 );

    return promotionList;

  }

  /**
   * creates a promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static ProductClaimPromotion buildProductClaimPromotion( String suffix )
  {
    return PromotionDAOImplTest.buildProductClaimPromotion( suffix );
  }

  /**
   * Creates a recognitionPromotion.
   * 
   * @return RecognitionPromotion
   */
  public static RecognitionPromotion buildRecognitionPromotion()
  {

    String uniqueString = getUniqueString();

    RecognitionPromotion recognitionPromotion = PromotionDAOImplTest.buildRecognitionPromotion( "RECOGNITIONPROMOTION_" + uniqueString );

    return recognitionPromotion;
  }

  /**
   * Creates a recognition promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static RecognitionPromotion buildRecognitionPromotion( String suffix )
  {

    return PromotionDAOImplTest.buildRecognitionPromotion( suffix );

  }

  /**
   * Creates a quiz promotion domain object
   * 
   * @param suffix
   * @return Promotion
   */
  public static QuizPromotion buildQuizPromotion( String suffix )
  {

    return PromotionDAOImplTest.buildQuizPromotion( suffix );

  }

}


package com.biperf.core.service.ssi.impl;

import org.easymock.EasyMock;
import org.jmock.Mock;

import com.biperf.cache.Cache;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.promotion.PromotionSecondaryAudience;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.impl.AudienceServiceImpl;
import com.biperf.core.value.PaxPromoEligibilityValueBean;

public class SSIPromotionServiceImplTest extends BaseServiceTest
{

  /** mock service and dao */
  private AudienceService audienceService = new AudienceServiceImpl();
  private AudienceServiceImpl classUnderTest = new AudienceServiceImpl();
  public Participant testPax;

  private Mock mockOSCache = null;
  private Mock audienceMockDAO;
  private Mock cmAssetMockService;

  public static final String TEST_FIRST_NAME_PREFIX = "fIRstn";
  public static final String TEST_LAST_NAME_PREFIX = "lASTN";

  private NodeService nodeServiceMock;
  private ParticipantService participantServiceMock;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {

    super.setUp();

    audienceMockDAO = new Mock( AudienceDAO.class );
    ( (AudienceServiceImpl)audienceService ).setAudienceDAO( (AudienceDAO)audienceMockDAO.proxy() );
    classUnderTest.setAudienceDAO( (AudienceDAO)audienceMockDAO.proxy() );

    cmAssetMockService = new Mock( CMAssetService.class );
    ( (AudienceServiceImpl)audienceService ).setCmAssetService( (CMAssetService)cmAssetMockService.proxy() );

    nodeServiceMock = EasyMock.createMock( NodeService.class );
    classUnderTest.setNodeService( nodeServiceMock );

    participantServiceMock = EasyMock.createMock( ParticipantService.class );
    classUnderTest.setParticipantService( participantServiceMock );
    ( (AudienceServiceImpl)audienceService ).setParticipantService( participantServiceMock );

    mockOSCache = new Mock( Cache.class );
    ( (AudienceServiceImpl)audienceService ).setCriteriaAudienceCacheForMock( (Cache)mockOSCache.proxy() );
    ( (AudienceServiceImpl)audienceService ).setPaxPromoEligibilityCacheForMock( (Cache)mockOSCache.proxy() );

    ( (AudienceServiceImpl)classUnderTest ).setCriteriaAudienceCacheForMock( (Cache)mockOSCache.proxy() );
    ( (AudienceServiceImpl)classUnderTest ).setPaxPromoEligibilityCacheForMock( (Cache)mockOSCache.proxy() );

    testPax = ParticipantDAOImplTest.buildUniqueParticipant( "testPax1" );
  }

  public void testIsPaxInContestCreatorAudienceAllActivePax()
  {
    SSIPromotion promotion = PromotionDAOImplTest.buildSSIPromotion( "test1000" );
    promotion.setId( new Long( 1 ) );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    PromotionPrimaryAudience promotionSubmitterAudience = new PromotionPrimaryAudience();
    PaxAudience paxAudience = new PaxAudience();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    paxAudience.addParticipant( testPax );
    promotionSubmitterAudience.setAudience( paxAudience );
    promotion.addPromotionPrimaryAudience( promotionSubmitterAudience );

    mockOSCache.expects( atLeastOnce() ).method( "get" ).with( same( testPax.getId() ) );
    mockOSCache.expects( atLeastOnce() ).method( "put" ).with( same( testPax.getId() ), isA( PaxPromoEligibilityValueBean.class ) );
    assertTrue( audienceService.isParticipantInPrimaryAudience( promotion, testPax ) );
  }

  public void testIsPaxInContestCreatorAudienceSpecificPax()
  {
    SSIPromotion promotion = PromotionDAOImplTest.buildSSIPromotion( "test1000" );
    promotion.setId( new Long( 1 ) );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.SPECIFY_AUDIENCE_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    PromotionPrimaryAudience promotionPrimaryAudience = new PromotionPrimaryAudience();
    PaxAudience paxAudience = new PaxAudience();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    paxAudience.addParticipant( testPax );
    promotionPrimaryAudience.setAudience( paxAudience );
    promotion.addPromotionPrimaryAudience( promotionPrimaryAudience );

    mockOSCache.expects( atLeastOnce() ).method( "get" ).with( same( testPax.getId() ) );
    mockOSCache.expects( atLeastOnce() ).method( "put" ).with( same( testPax.getId() ), isA( PaxPromoEligibilityValueBean.class ) );
    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );
    assertTrue( audienceService.isParticipantInPrimaryAudience( promotion, testPax ) );
  }

  public void testIsPaxInContestParticipantAudienceAllActivePax()
  {
    SSIPromotion promotion = PromotionDAOImplTest.buildSSIPromotion( "test1000" );
    promotion.setId( new Long( 1 ) );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ALL_ACTIVE_PAX_CODE ) );

    PromotionSecondaryAudience promotionSecondaryAudience = new PromotionSecondaryAudience();
    PaxAudience paxAudience = new PaxAudience();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    paxAudience.addParticipant( testPax );
    promotionSecondaryAudience.setAudience( paxAudience );
    promotion.addPromotionSecondaryAudience( promotionSecondaryAudience );

    mockOSCache.expects( atLeastOnce() ).method( "get" ).with( same( testPax.getId() ) );
    mockOSCache.expects( atLeastOnce() ).method( "put" ).with( same( testPax.getId() ), isA( PaxPromoEligibilityValueBean.class ) );
    assertTrue( audienceService.isParticipantInSecondaryAudience( promotion, testPax ) );
  }

  public void testIsPaxInContestParticipantAudienceSpecificPax()
  {
    SSIPromotion promotion = PromotionDAOImplTest.buildSSIPromotion( "test1000" );
    promotion.setId( new Long( 1 ) );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) );

    PromotionSecondaryAudience promotionSecondaryAudience = new PromotionSecondaryAudience();
    PaxAudience paxAudience = new PaxAudience();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    paxAudience.addParticipant( testPax );
    promotionSecondaryAudience.setAudience( paxAudience );
    promotion.addPromotionSecondaryAudience( promotionSecondaryAudience );

    mockOSCache.expects( atLeastOnce() ).method( "get" ).with( same( testPax.getId() ) );
    mockOSCache.expects( atLeastOnce() ).method( "put" ).with( same( testPax.getId() ), isA( PaxPromoEligibilityValueBean.class ) );
    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );
    assertTrue( audienceService.isParticipantInSecondaryAudience( promotion, testPax ) );
  }

}

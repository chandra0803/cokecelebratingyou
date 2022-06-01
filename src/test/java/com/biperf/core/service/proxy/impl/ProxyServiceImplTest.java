/**
 * 
 */

package com.biperf.core.service.proxy.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.claim.hibernate.ClaimFormDAOImplTest;
import com.biperf.core.dao.proxy.ProxyDAO;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesMultipleAwardsType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.domain.proxy.ProxyModulePromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.utils.DateUtils;

/**
 * ProxyServiceImplTest.
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
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyServiceImplTest extends BaseServiceTest
{
  private ProxyServiceImpl proxyServiceImpl = new ProxyServiceImpl();

  private Mock mockProxyDAO = null;

  private ProxyServiceImpl classUnderTest;

  private ProxyDAO proxyDAOMock;

  private AssociationRequest mockAssociationRequest = new AssociationRequest()
  {
    public void execute( Object domainObject )
    {
    }
  };

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ProxyServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    mockProxyDAO = new Mock( ProxyDAO.class );

    proxyServiceImpl.setProxyDAO( (ProxyDAO)mockProxyDAO.proxy() );

    classUnderTest = new ProxyServiceImpl();
    classUnderTest.setProxyDAO( proxyDAOMock );
  }

  /**
   * Test to save a promotion.
   */
  public void testSave()
  {
    // create a proxy object
    Proxy expectedProxy = buildProxy();

    expectedProxy.setId( new Long( 1 ) );

    // ProxyDAO expected to call save once with the Proxy
    // which will return the Proxy expected
    mockProxyDAO.expects( once() ).method( "save" ).with( same( expectedProxy ) ).will( returnValue( expectedProxy ) );

    // make the service call
    try
    {
      Proxy actualProxy = proxyServiceImpl.saveProxy( expectedProxy );

      assertEquals( "Actual proxy didn't match with what is expected", expectedProxy, actualProxy );

      mockProxyDAO.verify();
    }
    catch( ServiceErrorException se )
    {
      // shouldn't happen.
    }

  }

  /**
   * Tests getting the proxy from the service through the DAO.
   */
  public void testGetById()
  {
    Proxy expectedProxy = buildProxy();
    expectedProxy.setId( new Long( 1 ) );

    mockProxyDAO.expects( once() ).method( "getProxyById" ).with( same( expectedProxy.getId() ) ).will( returnValue( expectedProxy ) );

    Proxy actualProxy = proxyServiceImpl.getProxyById( expectedProxy.getId() );

    assertEquals( "Actual Proxy does not match to what was expected", expectedProxy, actualProxy );

    mockProxyDAO.verify();
  }

  /**
   * Tests getting the proxy with associations from the service through the DAO.
   */
  public void testGetByIdWithAssociations()
  {
    Proxy expectedProxy = buildProxy();
    expectedProxy.setId( new Long( 1 ) );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( mockAssociationRequest );

    mockProxyDAO.expects( once() ).method( "getProxyByIdWithAssociations" ).with( same( expectedProxy.getId() ), eq( associationRequestCollection ) ).will( returnValue( expectedProxy ) );

    Proxy actualProxy = proxyServiceImpl.getProxyByIdWithAssociations( expectedProxy.getId(), associationRequestCollection );

    assertEquals( "Actual Proxy does not match to what was expected", expectedProxy, actualProxy );

    mockProxyDAO.verify();
  }

  /**
   * Tests getting proxies by user id from the service through the DAO.
   */
  public void testGetProxiesByUserId()
  {
    List proxyList = new ArrayList();

    Proxy expectedProxy = buildProxy();
    expectedProxy.setId( new Long( 1 ) );

    proxyList.add( expectedProxy );

    // Mock object for the getAll call.
    mockProxyDAO.expects( once() ).method( "getProxiesByUserId" ).with( same( expectedProxy.getUser().getId() ) ).will( returnValue( proxyList ) );

    List returnedUsers = proxyServiceImpl.getProxiesByUserId( expectedProxy.getUser().getId() );
    assertTrue( returnedUsers.size() > 0 );
  }

  /**
   * Tests deleting the hierachy from the database through the service.
   * 
   * @throws Exception
   */
  public void testDelete() throws Exception
  {
    Proxy expectedProxy = buildProxy();
    expectedProxy.setId( new Long( 1 ) );

    mockProxyDAO.expects( once() ).method( "getProxyById" ).with( same( expectedProxy.getId() ) ).will( returnValue( expectedProxy ) );

    mockProxyDAO.expects( once() ).method( "delete" ).with( same( expectedProxy ) );

    proxyServiceImpl.deleteProxy( expectedProxy.getId() );

    mockProxyDAO.verify();
  }

  /**
   * Tests deleting the hierachy from the database through the service.
   * 
   * @throws Exception
   */
  public void testDeleteList() throws Exception
  {
    List proxyIdList = new ArrayList();

    Proxy expectedProxy1 = buildProxy();
    Proxy expectedProxy2 = buildProxy();

    expectedProxy1.setId( new Long( 1 ) );
    expectedProxy2.setId( new Long( 2 ) );

    proxyIdList.add( expectedProxy1.getId() );
    proxyIdList.add( expectedProxy2.getId() );

    mockProxyDAO.expects( once() ).method( "getProxyById" ).with( same( expectedProxy1.getId() ) ).will( returnValue( expectedProxy1 ) );
    mockProxyDAO.expects( once() ).method( "getProxyById" ).with( same( expectedProxy2.getId() ) ).will( returnValue( expectedProxy2 ) );

    mockProxyDAO.expects( once() ).method( "delete" ).with( same( expectedProxy1 ) );
    mockProxyDAO.expects( once() ).method( "delete" ).with( same( expectedProxy2 ) );

    proxyServiceImpl.deleteProxies( proxyIdList );

    mockProxyDAO.verify();
  }

  /**
   * Test get all users for a proxyUser
   */
  public void testGetAllUsersForAProxy()
  {
    List users = new ArrayList();
    users.add( new User() );

    Proxy expectedProxy = buildProxy();
    expectedProxy.setId( new Long( 1 ) );

    // Mock object for the getAll call.
    mockProxyDAO.expects( once() ).method( "getUsersByProxyUserId" ).with( same( expectedProxy.getProxyUser().getId() ) ).will( returnValue( users ) );

    List returnedUsers = proxyServiceImpl.getUsersByProxyUserId( expectedProxy.getProxyUser().getId() );
    assertTrue( returnedUsers.size() > 0 );
  }

  /**
   * Tests getting the proxy by user and proxy id from the service through the DAO.
   */
  public void testGetByUserIdAndProxyId()
  {
    Proxy expectedProxy = buildProxy();
    expectedProxy.setId( new Long( 1 ) );

    mockProxyDAO.expects( once() ).method( "getProxyByUserAndProxyUserWithAssociations" ).with( same( expectedProxy.getUser().getId() ), same( expectedProxy.getProxyUser().getId() ), same( null ) )
        .will( returnValue( expectedProxy ) );

    Proxy actualProxy = proxyServiceImpl.getProxyByUserAndProxyUser( expectedProxy.getUser().getId(), expectedProxy.getProxyUser().getId() );

    assertEquals( "Actual Proxy does not match to what was expected", expectedProxy, actualProxy );

    mockProxyDAO.verify();
  }

  public void testPromotionsAllowedForDelgate()
  {
    List<Promotion> promoList = new ArrayList<Promotion>();
    List<Promotion> actualPromoList = new ArrayList<Promotion>();
    List<Promotion> expectedPromoList = new ArrayList<Promotion>();
    Promotion promo = buildRecognitionPromotion( 2525L );
    promoList.add( promo );
    Promotion promo1 = buildRecognitionPromotion( 2526L );
    promoList.add( promo1 );
    Promotion promo2 = buildRecognitionPromotion( 2527L );
    promoList.add( promo2 );

    expectedPromoList.add( promo );
    expectedPromoList.add( promo1 );

    Proxy expectedProxy = buildProxyWithModules();
    expectedProxy.setId( new Long( 1 ) );
    actualPromoList = proxyServiceImpl.getPromotionsAllowedForDelegateEZ( promoList, expectedProxy );
    assertEquals( "Actual Promotion size does not match to what was expected", expectedPromoList.size(), actualPromoList.size() );

  }

  /**
   * creates a proxy domain object
   * 
   * @return Proxy
   */
  public static Proxy buildProxy()
  {
    Proxy proxy = new Proxy();
    Participant user = new Participant();
    Participant proxyUser = new Participant();

    user.setId( new Long( 1 ) );
    user.setFirstName( "Test" );

    proxyUser.setId( new Long( 2 ) );
    proxyUser.setFirstName( "Proxy" );

    proxy.setUser( user );
    proxy.setProxyUser( proxyUser );

    return proxy;
  }

  public static Proxy buildProxyWithModules()
  {
    Proxy proxy = new Proxy();
    Participant user = new Participant();
    Participant proxyUser = new Participant();

    user.setId( new Long( 1 ) );
    user.setFirstName( "Test" );

    proxyUser.setId( new Long( 2 ) );
    proxyUser.setFirstName( "Proxy" );

    proxy.setUser( user );
    proxy.setProxyUser( proxyUser );
    proxy.setAllModules( false );

    ProxyModule proxyModule = new ProxyModule();
    proxyModule.setId( new Long( 1 ) );
    proxyModule.setAllPromotions( false );
    proxyModule.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    proxyModule.setProxy( proxy );

    ProxyModulePromotion proxyPromos = new ProxyModulePromotion();
    proxyPromos.setId( new Long( 1 ) );
    proxyPromos.setPromotion( buildRecognitionPromotion( 2526L ) );
    proxyPromos.setProxyModule( proxyModule );

    proxyModule.addProxyModulePromotion( proxyPromos );

    ProxyModulePromotion proxyPromos1 = new ProxyModulePromotion();
    proxyPromos1.setId( new Long( 2 ) );
    proxyPromos1.setPromotion( buildRecognitionPromotion( 2525L ) );
    proxyPromos1.setProxyModule( proxyModule );

    proxyModule.addProxyModulePromotion( proxyPromos1 );

    proxy.addProxyModule( proxyModule );

    return proxy;
  }

  public static RecognitionPromotion buildRecognitionPromotion( Long promoId )
  {
    String uniqueString = getUniqueString();
    RecognitionPromotion recognitionPromotion = buildRecognitionPromotion( "RECOGNITIONPROMOTION_" + uniqueString, promoId );
    return recognitionPromotion;
  }

  public static RecognitionPromotion buildRecognitionPromotion( String suffix, Long promoId )
  {
    RecognitionPromotion promotion = new RecognitionPromotion();

    promotion.setId( promoId );
    // Required fields
    promotion.setName( "testPromotion" + suffix );

    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setApprovalConditionalAmountOperator( ApprovalConditionalAmmountOperatorType.lookup( ApprovalConditionalAmmountOperatorType.EQ ) );
    promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
    promotion.setApproverType( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
    promotion.setPromoNameAssetCode( "testAssetCode" );
    ClaimForm claimForm = ClaimFormDAOImplTest.buildClaimFormDomainObjectWithStepsAndElements();

    promotion.setClaimForm( claimForm );
    promotion.setSubmissionStartDate( new Date() );
    promotion.setSubmissionEndDate( new Date() );

    promotion.setWebRulesStartDate( new Date() );
    promotion.setWebRulesEndDate( new Date() );
    promotion.setWebRulesActive( true );
    promotion.setApprovalStartDate( DateUtils.toStartDate( new Date() ) );

    // recognition-specific setters for required fields
    // promotion.setIssuanceType( PromotionIssuanceType.lookup( PromotionIssuanceType.ONLINE ) );
    promotion.setOnlineEntry( true );
    promotion.setFileLoadEntry( false );

    promotion.setIncludePurl( false );

    promotion.setIncludeCertificate( false );
    promotion.setCopyRecipientManager( false );
    promotion.setAwardActive( true );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setAwardAmountFixed( new Long( 13 ) );
    promotion.setFileloadBudgetAmount( false );
    promotion.setSweepstakesActive( true );
    promotion.setSweepstakesWinnerEligibilityType( SweepstakesWinnerEligibilityType.lookup( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) );
    promotion.setSweepstakesMultipleAwardType( SweepstakesMultipleAwardsType.lookup( SweepstakesMultipleAwardsType.MULTIPLE_CODE ) );
    promotion.setBehaviorActive( false );
    promotion.setCardActive( true );
    promotion.setCardClientSetupDone( false );

    promotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.INNOVATION_CODE ) );
    promotion.addPromotionBehavior( PromoRecognitionBehaviorType.lookup( PromoRecognitionBehaviorType.GREAT_IDEA_CODE ) );

    PromotionSweepstake sweepstake = new PromotionSweepstake();
    sweepstake.setStartDate( new Date() );
    sweepstake.setEndDate( new Date() );
    sweepstake.setProcessed( false );
    promotion.addPromotionSweepstake( sweepstake );
    return promotion;
  }

}

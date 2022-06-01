/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/participant/impl/AudienceServiceImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service.participant.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.jmock.Mock;

import com.biperf.cache.Cache;
import com.biperf.core.dao.employer.hibernate.EmployerDAOImplTest;
import com.biperf.core.dao.hierarchy.hibernate.HierarchyDAOImplTest;
import com.biperf.core.dao.hierarchy.hibernate.NodeTypeDAOImplTest;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.hibernate.AudienceDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SecondaryAudienceType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionAudience;
import com.biperf.core.domain.promotion.PromotionPrimaryAudience;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.value.PaxPromoEligibilityValueBean;

/**
 * AudienceServiceImplTest.
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
 * <td>sharma</td>
 * <td>Jun 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceServiceImplTest extends BaseServiceTest
{
  private static final int TEST_NODE_ID3 = 3;

  private static final int TEST_NODE_CHARACTERISTIC_TYPE_ID2 = 10012;

  private static final int TEST_NODE_CHARACTERISTIC_TYPE_ID1 = 10011;

  private static final Long TEST_EMPLOYER_ID_2 = new Long( 200 );

  private static final Long TEST_EMPLOYER_ID_1 = new Long( 100 );

  private static final int TEST_USER_CHARACTERISTIC_ID2 = 12;

  private static final int TEST_USER_CHARACTERISTIC_ID1 = 11;

  private static final Log log = LogFactory.getLog( AudienceServiceImplTest.class );

  private AudienceService audienceService = new AudienceServiceImpl();
  private AudienceServiceImpl classUnderTest = new AudienceServiceImpl();

  private NodeService nodeServiceMock;

  private ParticipantService participantServiceMock;

  private Mock audienceMockDAO;
  private Mock cmAssetMockService;

  private Mock mockOSCache = null;

  public Participant testPax;
  public Participant otherPax;

  public PaxAudience paxAudience1WithTestPax;

  public PaxAudience paxAudience2WithTestPax;

  public PaxAudience paxAudienceWithTestPaxAndOtherPax;

  public PaxAudience paxAudienceWithJustOtherPax;

  public Set promotionAudiencesPaxAudienceOnly = new LinkedHashSet();

  public static final String TEST_FIRST_NAME = "FirstName";
  public static final String TEST_FIRST_NAME_PREFIX = "fIRstn";
  public static final String TEST_LAST_NAME = "LastName";
  public static final String TEST_LAST_NAME_PREFIX = "lASTN";
  public static final String TEST_NODE_NAME = "Level3Node1";
  public static final String TEST_NODE_NAME_PREFIX = "lEvel3";
  public static final String TEST_LEVEL_2_NODE_NAME_PREFIX = "lEvel2";

  private AssociationRequest mockAssociationRequest = new AssociationRequest()
  {
    public void execute( Object domainObject )
    {
    }
  };

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

    otherPax = ParticipantDAOImplTest.buildUniqueParticipant( "otherPax1" );

    paxAudience1WithTestPax = (PaxAudience)AudienceDAOImplTest.getPaxAudience( testPax, "1" );

    paxAudience2WithTestPax = (PaxAudience)AudienceDAOImplTest.getPaxAudience( testPax, "2" );

    paxAudienceWithTestPaxAndOtherPax = (PaxAudience)AudienceDAOImplTest.getPaxAudience( testPax, "3" );
    paxAudienceWithTestPaxAndOtherPax.addParticipant( otherPax );

    paxAudienceWithJustOtherPax = (PaxAudience)AudienceDAOImplTest.getPaxAudience( otherPax, "4" );

    promotionAudiencesPaxAudienceOnly = new LinkedHashSet();

    promotionAudiencesPaxAudienceOnly.add( buildPromotionAudience( paxAudience1WithTestPax ) );
    promotionAudiencesPaxAudienceOnly.add( buildPromotionAudience( paxAudience2WithTestPax ) );
    promotionAudiencesPaxAudienceOnly.add( buildPromotionAudience( paxAudienceWithTestPaxAndOtherPax ) );
    promotionAudiencesPaxAudienceOnly.add( buildPromotionAudience( paxAudienceWithJustOtherPax ) );

    populateParticipantWithCriteriaProperties( testPax );
  }

  public void testGetAudienceById()
  {
    Audience audience = new CriteriaAudience();
    audience.setId( new Long( 1 ) );
    audienceMockDAO.expects( once() ).method( "getAudienceById" ).with( same( audience.getId() ) ).will( returnValue( audience ) );
    audienceService.getAudienceById( audience.getId(), null );
    audienceMockDAO.verify();
  }

  public void testGetAudienceByName()
  {
    Audience audience = new CriteriaAudience();
    audience.setId( new Long( 9 ) );
    audience.setName( "testAudience" );
    audienceMockDAO.expects( once() ).method( "getAudienceByName" ).with( same( audience.getName() ) ).will( returnValue( audience ) );
    audienceService.getAudienceByName( audience.getName() );
    audienceMockDAO.verify();
  }

  // TODO
  /*
   * public void testSaveNewAudience() throws ServiceErrorException { Audience audience = new
   * CriteriaAudience(); audience.setId( null ); audience.setName( "testAudience" );
   * audienceMockDAO.expects( once() ).method( "save" ).with( same( audience ) );
   * cmAssetMockService.expects( once() ).method( "createCmsAudience" ).with( same( audience
   * .getName() ) ); mockOSCache.expects( once() ).method( "get" ).with( same( "criteriaAudiences" )
   * ); audienceService.save( audience ); audienceMockDAO.verify(); } public void
   * testUpdateAudience() throws ServiceErrorException { Audience audience = new CriteriaAudience();
   * audience.setId( new Long( 1 ) ); audience.setName( "testAudience" ); audienceMockDAO.expects(
   * once() ).method( "save" ).with( same( audience ) ); audienceMockDAO.expects( once() ).method(
   * "getAudienceById" ).with( same( audience.getId() ) ) .will( returnValue( audience ) ); //
   * cmAssetMockService.expects( once() ).method( "updateCmsAudience" ).with( same( audience //
   * .getName() ), // same( audience // .getName() ) ); mockOSCache.expects( once() ).method(
   * "remove" ).with( same( "criteriaAudiences" ) ); mockOSCache.expects( once() ).method( "clear"
   * ); audienceService.save( audience ); audienceMockDAO.verify(); } public void testDelete() {
   * Audience audience = new CriteriaAudience(); Audience audience1 = new CriteriaAudience();
   * audience.setId( new Long( 1 ) ); audienceMockDAO.expects( once() ).method( "getAudienceById"
   * ).with( same( audience.getId() ) ) .will( returnValue( audience ) ); audienceMockDAO.expects(
   * once() ).method( "delete" ).with( same( audience ) ); mockOSCache.expects( once() ).method(
   * "remove" ).with( same( "criteriaAudiences" ) ); mockOSCache.expects( once() ).method( "clear"
   * ); audienceService.delete( audience.getId() ); audienceMockDAO.verify(); }
   */
  /*
   * public void testIsUserInPromotionAudiencesPaxAudiencesOnly() { assertTrue(
   * audienceService.isUserInPromotionAudiences( testPax, promotionAudiencesPaxAudienceOnly ) ); }
   */
  public void testIsUserInTeamMemberAudienceSameAsSubmittersAudience()
  {
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( "test1000" );
    promotion.setId( new Long( 1 ) );
    promotion.setTeamUsed( true );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.SAME_AS_PRIMARY_CODE ) );

    PromotionPrimaryAudience promotionSubmitterAudience = new PromotionPrimaryAudience();
    PaxAudience paxAudience = new PaxAudience();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    paxAudience.addParticipant( testPax );
    promotionSubmitterAudience.setAudience( paxAudience );
    promotion.addPromotionPrimaryAudience( promotionSubmitterAudience );

    /*
     * PaxPromoEligibilityValueBean paxPromoEligibilityValueBean = new
     * PaxPromoEligibilityValueBean(); PaxPromoEligibilityValueBean.PromotionEligibility
     * promotionEligibility = paxPromoEligibilityValueBean.new PromotionEligibility();
     * promotionEligibility.setPromotionId( promotion.getId() );
     * promotionEligibility.setInSecondaryAudience( Boolean.TRUE );
     * promotionEligibility.setInPublicRecognitionAudience( Boolean.FALSE );
     * promotionEligibility.setInWebRulesAudience( Boolean.TRUE );
     * paxPromoEligibilityValueBean.setInSecondaryAudience( testPax.getId(), promotion.getId(),
     * Boolean.TRUE ); paxPromoEligibilityValueBean.setInPublicRecognigionAudience(testPax.getId(),
     * promotion.getId(), Boolean.FALSE); paxPromoEligibilityValueBean.setInWebRulesAudience(
     * testPax.getId(), promotion.getId(), Boolean.TRUE );
     */

    mockOSCache.expects( atLeastOnce() ).method( "get" ).with( same( testPax.getId() ) );
    mockOSCache.expects( atLeastOnce() ).method( "put" ).with( same( testPax.getId() ), isA( PaxPromoEligibilityValueBean.class ) );

    assertTrue( audienceService.isParticipantInSecondaryAudience( promotion, testPax, null ) );
  }

  public void testIsUserInTeamMemberAudienceFromSubmittersNode()
  {
    ProductClaimPromotion promotion = new ProductClaimPromotion();
    promotion.setTeamUsed( true );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setPrimaryAudienceType( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) );
    promotion.setSecondaryAudienceType( SecondaryAudienceType.lookup( SecondaryAudienceType.ACTIVE_PAX_PRIMARY_NODE_CODE ) );
    promotion.setId( new Long( 1 ) );

    Node node = new Node();
    node.setId( new Long( 1 ) );

    UserNode userNode = new UserNode( testPax, node );
    testPax.addUserNode( userNode );
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database

    List userList = new ArrayList();
    userList.add( testPax );

    mockOSCache.expects( once() ).method( "get" ).with( same( testPax.getId() ) );
    mockOSCache.expects( once() ).method( "put" ).with( same( testPax.getId() ), isA( PaxPromoEligibilityValueBean.class ) );
    boolean result = classUnderTest.isParticipantInSecondaryAudience( promotion, testPax, node );

    assertTrue( result );
  }

  /*
   * public void testIsUserInTeamMemberAudienceSpecificTeamMemberAudience() { ProductClaimPromotion
   * promotion = new ProductClaimPromotion(); promotion.setTeamUsed( true );
   * promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
   * promotion.setSecondaryAudienceType( SecondaryAudienceType .lookup(
   * SecondaryAudienceType.SPECIFY_AUDIENCE_CODE ) ); PromotionSecondaryAudience
   * promotionTeamAudience = new PromotionSecondaryAudience(); PaxAudience paxAudience = new
   * PaxAudience(); testPax.setId(new Long("32140"));//Manually entered testPax Id from database
   * paxAudience.addParticipant( testPax ); paxAudience.setId(new Long("1"));//Manually entered
   * paxAudience Id from database promotionTeamAudience.setAudience( paxAudience );
   * promotion.addPromotionSecondaryAudience( promotionTeamAudience ); audienceMockDAO.expects(
   * once() ).method( "checkPaxAudiencesByAudienceIdParticipantId" ).with( same( paxAudience.getId()
   * ),same( testPax.getId() ) ).will(returnValue(new ArrayList())); assertTrue(
   * audienceService.isParticipantInSecondaryAudience( promotion, testPax, null ) ); }
   */
  public void testIsUserInPromotionAudiencesCriteriaAudienceNameSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setFirstName( TEST_FIRST_NAME_PREFIX );
    audienceCriteria.setLastName( TEST_LAST_NAME_PREFIX );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );
    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNameFailure()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setFirstName( "wrongfirst" );
    audienceCriteria.setLastName( "wronglast" );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 0 ) ) );

    assertFalse( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudiencePaxCharsSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = new AudienceCriteriaCharacteristic();

    for ( Iterator iter = testPax.getUserCharacteristics().iterator(); iter.hasNext(); )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)iter.next();
      if ( userCharacteristic.getId().longValue() == TEST_USER_CHARACTERISTIC_ID2 )
      {
        // use second char as test characteristic
        audienceCriteriaCharacteristic.setCharacteristic( userCharacteristic.getUserCharacteristicType() );
        audienceCriteriaCharacteristic.setCharacteristicValue( userCharacteristic.getCharacteristicValue() );
        audienceCriteriaCharacteristic.setId( new Long( 100000 ) );
      }

    }
    assertNotNull( audienceCriteriaCharacteristic.getId() );
    audienceCriteria.setCharacteristicCriterias( Collections.singleton( audienceCriteriaCharacteristic ) );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );

    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudiencePaxCharsFailure()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = new AudienceCriteriaCharacteristic();

    for ( Iterator iter = testPax.getUserCharacteristics().iterator(); iter.hasNext(); )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)iter.next();
      if ( userCharacteristic.getId().longValue() == TEST_USER_CHARACTERISTIC_ID2 )
      {
        // use second char as test characteristic
        audienceCriteriaCharacteristic.setCharacteristic( userCharacteristic.getUserCharacteristicType() );
        audienceCriteriaCharacteristic.setCharacteristicValue( "wrongvalue" );
        audienceCriteriaCharacteristic.setId( new Long( 100000 ) );
      }

    }
    assertNotNull( audienceCriteriaCharacteristic.getId() );
    audienceCriteria.setCharacteristicCriterias( Collections.singleton( audienceCriteriaCharacteristic ) );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 0 ) ) );

    assertFalse( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeCharsSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = new AudienceCriteriaCharacteristic();

    for ( Iterator iter = testPax.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getNode().getId().longValue() == TEST_NODE_ID3 )
      {
        for ( Iterator iterator = userNode.getNode().getNodeCharacteristics().iterator(); iterator.hasNext(); )
        {
          NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)iterator.next();
          if ( nodeCharacteristic.getNodeTypeCharacteristicType().getId().longValue() == TEST_NODE_CHARACTERISTIC_TYPE_ID2 )
          {
            // use second char as test characteristic
            audienceCriteriaCharacteristic.setCharacteristic( nodeCharacteristic.getNodeTypeCharacteristicType() );
            audienceCriteriaCharacteristic.setCharacteristicValue( nodeCharacteristic.getCharacteristicValue() );
            audienceCriteriaCharacteristic.setId( new Long( 1000000 ) );
            break;
          }

        }
      }
    }
    assertNotNull( audienceCriteriaCharacteristic.getId() );
    audienceCriteria.setCharacteristicCriterias( Collections.singleton( audienceCriteriaCharacteristic ) );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );
    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeCharsFailure()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = new AudienceCriteriaCharacteristic();

    for ( Iterator iter = testPax.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( userNode.getNode().getId().longValue() == TEST_NODE_ID3 )
      {
        for ( Iterator iterator = userNode.getNode().getNodeCharacteristics().iterator(); iterator.hasNext(); )
        {
          NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)iterator.next();
          if ( nodeCharacteristic.getNodeTypeCharacteristicType().getId().longValue() == TEST_NODE_CHARACTERISTIC_TYPE_ID2 )
          {
            // use second char as test characteristic
            audienceCriteriaCharacteristic.setCharacteristic( nodeCharacteristic.getNodeTypeCharacteristicType() );
            audienceCriteriaCharacteristic.setCharacteristicValue( "wrong value" );
            audienceCriteriaCharacteristic.setId( new Long( 1000000 ) );
            break;
          }

        }
      }
    }
    assertNotNull( audienceCriteriaCharacteristic.getId() );
    audienceCriteria.setCharacteristicCriterias( Collections.singleton( audienceCriteriaCharacteristic ) );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );
    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 0 ) ) );

    assertFalse( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceEmployerInfoSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();

    for ( Iterator iter = testPax.getParticipantEmployers().iterator(); iter.hasNext(); )
    {
      ParticipantEmployer participantEmployer = (ParticipantEmployer)iter.next();
      if ( participantEmployer.getEmployer() != null && participantEmployer.getEmployer().getId().equals( TEST_EMPLOYER_ID_2 ) )
      {
        // use second emp value
        audienceCriteria.setDepartmentType( participantEmployer.getDepartmentType() );
        audienceCriteria.setEmployerId( participantEmployer.getEmployer().getId() );
        audienceCriteria.setPositionType( participantEmployer.getPositionType() );
      }
    }
    assertNotNull( audienceCriteria.getEmployerId() );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceEmployerInfoFailure()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();

    for ( Iterator iter = testPax.getParticipantEmployers().iterator(); iter.hasNext(); )
    {
      ParticipantEmployer participantEmployer = (ParticipantEmployer)iter.next();
      if ( participantEmployer.getEmployer() != null && participantEmployer.getEmployer().getId().equals( TEST_EMPLOYER_ID_2 ) )
      {
        // use second emp value
        audienceCriteria.setEmployerId( new Long( -1 ) );
      }

    }
    assertNotNull( audienceCriteria.getEmployerId() );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 0 ) ) );

    assertFalse( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeIdSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();

    UserNode userNode = testPax.getUserNodeByNodeId( new Long( 3 ) );

    // nodeId no children
    audienceCriteria.setNodeId( userNode.getNode().getId() );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( audienceService.isUserInPromotionAudiences( testPax, promotionAudiences ) );
  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeIdWithChildrenSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    UserNode userNode = testPax.getUserNodeByNodeId( new Long( 3 ) );

    // nodeId with children - at each point in hierarchy
    audienceCriteria.setChildNodesIncluded( true );

    // Get top level node (test node is at third level)
    audienceCriteria.setNodeId( new Long( 1 ) );
    Node level1Node1 = userNode.getNode().getParentNode().getParentNode();

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );
    EasyMock.reset( participantServiceMock );

    // Get middle level node (test node is at third level)
    audienceCriteria.setNodeId( new Long( 2 ) );
    Node level2Node1 = userNode.getNode().getParentNode();

    promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );
    EasyMock.reset( nodeServiceMock );
    EasyMock.reset( participantServiceMock );

    // Get same node
    audienceCriteria.setNodeId( new Long( 3 ) );
    Node level3Node1 = userNode.getNode();

    promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( eq( null ), eq( testPax.getId() ) ).will( returnValue( 1 ) );

    EasyMock.replay( participantServiceMock );
    assertTrue( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );

  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeIdWithChildrenFailure()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    UserNode userNode = testPax.getUserNodeByNodeId( new Long( 3 ) );

    // nodeId with children - at each point in hierarchy
    audienceCriteria.setChildNodesIncluded( true );

    // Make 4th level node (test node is at third level)

    Node level3Node1 = userNode.getNode();
    Node level4Node1 = buildNode( level3Node1.getNodeType(), level3Node1.getHierarchy(), 4, "level4node1", level3Node1 );
    audienceCriteria.setNodeId( new Long( 4 ) );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    EasyMock.replay( nodeServiceMock );
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 0 ) ) );

    assertFalse( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );
    EasyMock.verify( nodeServiceMock );
    EasyMock.reset( nodeServiceMock );

  }

  public void testFilterNodesByCriteriaAudiences()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    UserNode userNode = testPax.getUserNodeByNodeId( new Long( 3 ) );

    // nodeId with children - at each point in hierarchy
    audienceCriteria.setChildNodesIncluded( false );

    // Make 4th level node (test node is at third level)

    Node level3Node1 = userNode.getNode();
    Node level4Node1 = buildNode( level3Node1.getNodeType(), level3Node1.getHierarchy(), 4, "level4node1", level3Node1 );
    audienceCriteria.setNodeName( level3Node1.getName() );

    CriteriaAudience criteriaAudience = new CriteriaAudience( audienceCriteria );
    List nodeList = new ArrayList();
    nodeList.add( level3Node1 );
    nodeList.add( level4Node1 );
    Set audienceSet = new HashSet();
    audienceSet.add( criteriaAudience );

    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( mockAssociationRequest );
    EasyMock.expect( nodeServiceMock.getNodeWithAssociationsById( EasyMock.eq( new Long( 3 ) ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( level3Node1 );
    EasyMock.expect( nodeServiceMock.getNodeWithAssociationsById( EasyMock.eq( new Long( 4 ) ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( level4Node1 );
    EasyMock.replay( nodeServiceMock );

    List filteredList = classUnderTest.filterNodesByAudiences( nodeList, audienceSet );
    EasyMock.verify( nodeServiceMock );
    assertEquals( 1, filteredList.size() );
    assertTrue( filteredList.contains( level3Node1 ) );

  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeNameWithChildrenSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    UserNode userNode = testPax.getUserNodeByNodeId( new Long( 3 ) );

    // nodeId with children - at each point in hierarchy
    audienceCriteria.setChildNodesIncluded( true );

    // Get top level node (test node is at third level)
    Node level1Node1 = userNode.getNode().getParentNode().getParentNode();
    audienceCriteria.setNodeName( level1Node1.getName() );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    log.warn( "Current design doesn't allow for top level node by name criteria because path gets set to '/'. " + "TODO: change design to support top level node name criteria" );
    // assertTrue(classUnderTest.isUserInPromotionAudiences(testPax, promotionAudiences ));

    // 2nd level node prefix
    audienceCriteria.setNodeName( TEST_LEVEL_2_NODE_NAME_PREFIX );

    promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );
    EasyMock.reset( participantServiceMock );

    // Get same node (use prefix)
    audienceCriteria.setNodeName( TEST_NODE_NAME_PREFIX );

    promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );

  }

  public void testIsUserInPromotionAudiencesCriteriaAudienceNodeNameSuccess()
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();

    // nodeId with no children - at each point in hierarchy
    audienceCriteria.setChildNodesIncluded( false );

    // 2nd level node prefix (test node is at third level)
    audienceCriteria.setNodeName( TEST_LEVEL_2_NODE_NAME_PREFIX );

    Set promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 0 ) ) );
    assertFalse( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );
    EasyMock.reset( participantServiceMock );

    // Get same node (use prefix)
    audienceCriteria.setNodeName( TEST_NODE_NAME_PREFIX );

    promotionAudiences = Collections.singleton( new PromotionPrimaryAudience( new CriteriaAudience( audienceCriteria ), null ) );
    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( null ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    assertTrue( classUnderTest.isUserInPromotionAudiences( testPax, promotionAudiences ) );

  }

  public void testGetAllParticipantAudiences()
  {
    CriteriaAudience audience = new CriteriaAudience();
    audience.setId( new Long( 1 ) );
    audience.setName( "CriteraAudience" + System.currentTimeMillis() );

    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setFirstName( "critF" );
    audienceCriteria.setLastName( "critL" );

    Set characteristicCriterias = new HashSet();

    AudienceCriteriaCharacteristic audCriteriaCharacteristic = new AudienceCriteriaCharacteristic();
    UserCharacteristic characteristic = new UserCharacteristic();
    characteristic.setId( new Long( 1 ) );
    characteristic.setCharacteristicValue( "long neck" );
    audCriteriaCharacteristic.setId( new Long( 1 ) );
    audCriteriaCharacteristic.setCharacteristicValue( "long neck" );
    characteristicCriterias.add( audCriteriaCharacteristic );
    audienceCriteria.setCharacteristicCriterias( characteristicCriterias );

    audience.addAudienceCriteria( audienceCriteria );

    audienceCriteria.setDepartmentType( DepartmentType.MARKETING );

    ParticipantEmployer participantEmployer = new ParticipantEmployer();
    Employer employer = new Employer();
    employer.setId( new Long( 1 ) );
    employer.setName( "Bob Loblaw Attorney-at-Law" );

    participantEmployer.setEmployer( employer );

    List<CriteriaAudience> audiences = new ArrayList<CriteriaAudience>();
    audiences.add( audience );

    ConcurrentHashMap<Long, CriteriaAudience> allCriteriaAudiences = new ConcurrentHashMap<Long, CriteriaAudience>();
    for ( CriteriaAudience criteriaAudience : audiences )
    {
      allCriteriaAudiences.put( criteriaAudience.getId(), criteriaAudience );
    }

    audienceMockDAO.expects( once() ).method( "getAllPaxAudiencesByParticipantId" ).with( same( testPax.getId() ) ).will( returnValue( new ArrayList() ) );

    audienceMockDAO.expects( once() ).method( "checkAudiencesByAudienceIdParticipantId" ).with( same( audience.getId() ), same( testPax.getId() ) ).will( returnValue( new Integer( 1 ) ) );

    mockOSCache.expects( once() ).method( "get" ).with( same( "criteriaAudiences" ) ).will( returnValue( allCriteriaAudiences ) );

    testPax.setFirstName( "critF-First" );
    testPax.setLastName( "critL-Last" );
    testPax.addUserCharacteristic( characteristic );
    testPax.addParticipantEmployer( participantEmployer );

    EasyMock.expect( participantServiceMock.getParticipantById( null ) ).andReturn( testPax );
    EasyMock.replay( participantServiceMock );

    Set allAudiences = audienceService.getAllParticipantAudiences( testPax );
    assertTrue( "Expected list of allAudiences to contain CriteriaAudience", allAudiences.contains( audience ) );
  }

  public static Object buildPromotionAudience( Audience audience )
  {
    PromotionAudience promotionAudience = new PromotionPrimaryAudience();
    promotionAudience.setAudience( audience );

    return promotionAudience;
  }

  /**
   * Builds and returns a CriteriaAudience.
   * 
   * @return CriteriaAudience
   */
  public static CriteriaAudience buildStaticCriteriaAudience()
  {

    Audience audience = new CriteriaAudience();
    audience.setId( new Long( System.currentTimeMillis() ) );
    return (CriteriaAudience)audience;

  }

  /**
   * Builds and returns a PaxAudience.
   * 
   * @return PaxAudience
   */
  public static PaxAudience buildStaticPaxAudience()
  {

    Audience audience = new PaxAudience();
    audience.setId( new Long( System.currentTimeMillis() ) );
    return (PaxAudience)audience;

  }

  public void testCheckPaxAudiencesByAudienceIdParticipantId()
  {
    PaxAudience paxAudience = new PaxAudience();
    paxAudience.addParticipant( testPax );
    testPax.setId( new Long( "32140" ) );// Manually entered testPax Id from database
    paxAudience.setId( new Long( "1" ) );// Manually entered paxAudience Id from database
    audienceMockDAO.expects( once() ).method( "checkPaxAudiencesByAudienceIdParticipantId" ).with( same( paxAudience.getId() ), same( testPax.getId() ) ).will( returnValue( new ArrayList() ) );
    audienceService.checkPaxAudiencesByAudienceIdParticipantId( testPax, paxAudience );
    audienceMockDAO.verify();
  }

  private static void populateParticipantWithCriteriaProperties( Participant participant )
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 29930291 );

    // fn ln
    participant.setFirstName( TEST_FIRST_NAME );
    participant.setLastName( TEST_LAST_NAME );

    // //pax characteristics - create 2
    UserCharacteristic userCharacteristic1 = buildUserCharacteristicAndType( participant, "test.asset.name1", "testCharacteristic1", "value1", new Long( 1001 ), new Long( 2001 ) );
    UserCharacteristic userCharacteristic2 = buildUserCharacteristicAndType( participant, "test.asset.name2", "testCharacteristic2", "value2", new Long( 1002 ), new Long( 2002 ) );
    userCharacteristic1.setId( new Long( TEST_USER_CHARACTERISTIC_ID1 ) );
    userCharacteristic2.setId( new Long( TEST_USER_CHARACTERISTIC_ID2 ) );
    participant.addUserCharacteristic( userCharacteristic1 );
    participant.addUserCharacteristic( userCharacteristic2 );

    // //Employer info - create two
    ParticipantEmployer participantEmployer1 = EmployerDAOImplTest.buildParticipantEmployer( EmployerDAOImplTest.buildEmployer( "employer1", null ) );
    participantEmployer1.getEmployer().setId( TEST_EMPLOYER_ID_1 );
    participantEmployer1.setDepartmentType( null );
    participantEmployer1.setPositionType( null );

    ParticipantEmployer participantEmployer2 = EmployerDAOImplTest.buildParticipantEmployer( EmployerDAOImplTest.buildEmployer( "employer2", null ) );
    participantEmployer2.getEmployer().setId( TEST_EMPLOYER_ID_2 ); // need id for comparison

    participant.addParticipantEmployer( participantEmployer1 );
    participant.addParticipantEmployer( participantEmployer2 );

    // //Node info
    NodeType nodeType = NodeTypeDAOImplTest.buildNodeType( uniqueString );
    nodeType.setId( new Long( 1 ) );
    Hierarchy hierarchy = HierarchyDAOImplTest.buildHierarchy( uniqueString );

    NodeTypeCharacteristicType nodeTypeCharacteristicType1 = buildNodeTypeCharacteristicType( "test.asset.name1", "testCharacteristic1", new Long( TEST_NODE_CHARACTERISTIC_TYPE_ID1 ) );

    NodeTypeCharacteristicType nodeTypeCharacteristicType2 = buildNodeTypeCharacteristicType( "test.asset.name2", "testCharacteristic2", new Long( TEST_NODE_CHARACTERISTIC_TYPE_ID2 ) );

    Node level1node1 = buildNode( nodeType, hierarchy, 1, "level1node1", null );

    Node level2node1 = buildNode( nodeType, hierarchy, 2, "level2node1", level1node1 );
    NodeCharacteristic nodeCharacteristic1Type1 = buildNodeCharacteristic( level2node1, nodeTypeCharacteristicType1, "otherNodeValue", new Long( 2012 ) );
    level2node1.addNodeCharacteristic( nodeCharacteristic1Type1 );

    Node level3node1 = buildNode( nodeType, hierarchy, TEST_NODE_ID3, TEST_NODE_NAME, level2node1 );
    NodeCharacteristic nodeCharacteristic2Type1 = buildNodeCharacteristic( level3node1, nodeTypeCharacteristicType1, "nodeValue1", new Long( 20012 ) );
    NodeCharacteristic nodeCharacteristic2Type2 = buildNodeCharacteristic( level3node1, nodeTypeCharacteristicType2, "nodeValue2", new Long( 20022 ) );
    level3node1.addNodeCharacteristic( nodeCharacteristic2Type1 );
    level3node1.addNodeCharacteristic( nodeCharacteristic2Type2 );

    UserNode userNode1 = new UserNode( participant, level3node1 );
    userNode1.setHierarchyRoleType( HierarchyRoleType.getDefaultItem() );
    UserNode userNode2 = new UserNode( participant, buildNode( nodeType, hierarchy, 4, "otherNode", null ) );
    userNode2.setHierarchyRoleType( HierarchyRoleType.getDefaultItem() );

    participant.addUserNode( userNode1 );
    participant.addUserNode( userNode2 );
  }

  private static Node buildNode( NodeType nodeType, Hierarchy hierarchy, int nodeId, String nodeName, Node parentNode )
  {
    Node node = new Node();
    node.setId( new Long( nodeId ) );
    node.setName( nodeName );
    node.setParentNode( parentNode );
    node.setNodeType( nodeType );
    node.setHierarchy( hierarchy );
    node.setPath( node.calculatePath() );
    return node;
  }

  private static UserCharacteristic buildUserCharacteristicAndType( Participant participant,
                                                                    String assetCode,
                                                                    String characteristicTypeName,
                                                                    String characteristicValue,
                                                                    Long userCharacteristicTypeId,
                                                                    Long userCharacteristicId )
  {
    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setId( userCharacteristicTypeId );
    characteristic.setCharacteristicName( characteristicTypeName );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( assetCode );

    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setId( userCharacteristicId );
    userCharacteristic.setCharacteristicValue( characteristicValue );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( participant );
    return userCharacteristic;
  }

  private static NodeTypeCharacteristicType buildNodeTypeCharacteristicType( String assetCode, String characteristicTypeName, Long nodeTypeCharacteristicTypeId )
  {
    NodeTypeCharacteristicType characteristic = new NodeTypeCharacteristicType();
    characteristic.setId( nodeTypeCharacteristicTypeId );
    characteristic.setCharacteristicName( characteristicTypeName );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( "code" ) );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( assetCode );

    return characteristic;
  }

  private static NodeCharacteristic buildNodeCharacteristic( Node node, NodeTypeCharacteristicType characteristic, String characteristicValue, Long nodeCharacteristicId )
  {

    NodeCharacteristic nodeCharacteristic = new NodeCharacteristic();
    nodeCharacteristic.setId( nodeCharacteristicId );
    nodeCharacteristic.setCharacteristicValue( characteristicValue );
    nodeCharacteristic.setNodeTypeCharacteristicType( characteristic );
    nodeCharacteristic.setNode( node );
    return nodeCharacteristic;
  }
}

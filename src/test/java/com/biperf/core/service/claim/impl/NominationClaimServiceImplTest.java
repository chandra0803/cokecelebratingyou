
package com.biperf.core.service.claim.impl;

import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.NominationClaimDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.strategy.RandomNumberStrategy;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean;

public class NominationClaimServiceImplTest extends BaseServiceTest
{
  private NominationClaimServiceImpl classUnderTest = new NominationClaimServiceImpl();

  ClaimService claimService = createNiceMock( ClaimService.class );
  NominationClaimDAO nominationClaimDAO = createNiceMock( NominationClaimDAO.class );
  ClaimDAO claimDAO = createNiceMock( ClaimDAO.class );
  PromotionService promotionService = createNiceMock( PromotionService.class );
  ParticipantService participantService = createNiceMock( ParticipantService.class );
  NodeDAO nodeDAO = createNiceMock( NodeDAO.class );
  UserService userService = createNiceMock( UserService.class );
  CalculatorService calculatorService = createNiceMock( CalculatorService.class );
  RandomNumberStrategy randomNumberStrategy = createNiceMock( RandomNumberStrategy.class );
  NominationPromotionService nomPromoService = createNiceMock( NominationPromotionService.class );

  public void setUp() throws Exception
  {
    super.setUp();

    classUnderTest.setCalculatorService( calculatorService );
    classUnderTest.setClaimDAO( claimDAO );
    classUnderTest.setClaimService( claimService );
    classUnderTest.setNodeDAO( nodeDAO );
    classUnderTest.setNominationClaimDAO( nominationClaimDAO );
    classUnderTest.setParticipantService( participantService );
    classUnderTest.setPromotionService( promotionService );
    classUnderTest.setUserService( userService );
    classUnderTest.setRandomNumberStrategy( randomNumberStrategy );

  }

  public void testStepNominee_newNominationWithTeam() throws ServiceErrorException
  {
    NominationPromotion nomPromotion = getNomPromotion( 1L );

    expect( promotionService.getPromotionById( anyLong() ) ).andReturn( nomPromotion ).times( 1 );

    // expect( claimService.getClaimById( anyLong() ) ).andReturn( getNomClaim( 1l ) ).times( 1 );

    expect( participantService.getParticipantById( anyLong() ) ).andReturn( getParticipant( 1L ) ).anyTimes();
    expect( randomNumberStrategy.getRandomizedClaimNumber() ).andReturn( 1 + "" ).times( 1 );
    expect( applicationContext.getBean( NominationPromotionService.BEAN_NAME ) ).andReturn( nomPromoService ).times( 1 );

    expect( nomPromoService.getCurrentTimePeriod( anyLong(), (String)anyObject(), anyLong() ) ).andReturn( getTimePeriod( 1L ) ).times( 1 );

    expect( claimDAO.getNextTeamId() ).andReturn( 1L ).times( 1 );

    // expect( nodeDAO.getNodeById( anyLong() ) ).andReturn( getNode( 1l ) ).times( 1 );
    expect( claimDAO.saveClaim( (NominationClaim)anyObject() ) ).andReturn( getNomClaim( 1L ) ).times( 1 );

    RecognitionClaimSubmission claimSubmission = getClaimSubmission();
    NominationSubmitDataPromotionValueBean nomPromoVO = getNomPromoVO( 1L );
    nomPromoVO.setTeamName( "team" );
    nomPromoVO.setNomineeType( "team" );
    claimSubmission.setNomSubmitDataPromotionValueBean( nomPromoVO );
    claimSubmission.setParticipants( Arrays.asList( getNominee( 1L ) ) );
    claimSubmission.setNodeId( 1L );

    replay( promotionService, participantService, randomNumberStrategy, applicationContext, nomPromoService, nodeDAO, claimDAO, claimService );

    classUnderTest.stepNominee( claimSubmission, 1L );

    EasyMock.verify( promotionService, participantService, randomNumberStrategy, applicationContext, nomPromoService, nodeDAO, claimDAO, claimService );
  }

  private NominationPromotionTimePeriod getTimePeriod( long l )
  {
    NominationPromotionTimePeriod p = new NominationPromotionTimePeriod();
    p.setId( l );
    return p;
  }

  public static NominationPromotion getNomPromotion( Long id )
  {
    NominationPromotion p = new NominationPromotion();
    p.setId( id );
    return p;
  }

  public static Participant getParticipant( Long id )
  {
    Participant p = new Participant();
    p.setId( id );

    final Address addr = new Address();
    final Country country = new Country();
    country.setCurrencyCode( "USD" );
    addr.setCountry( country );

    final UserAddress ua = new UserAddress();
    ua.setAddress( addr );
    ua.setIsPrimary( Boolean.TRUE );

    final Set<UserAddress> addrs = new HashSet<>();
    addrs.add( ua );
    p.setUserAddresses( addrs );

    return p;
  }

  public static NominationClaim getNomClaim( Long id )
  {
    NominationClaim claim = new NominationClaim();
    claim.setId( id );
    return claim;
  }

  public static UserNode getUserNode( Long id )
  {
    UserNode userNode = new UserNode();
    userNode.setId( id );
    userNode.setNode( getNode( id ) );
    return userNode;
  }

  public static Node getNode( Long id )
  {
    Node node = new Node();
    node.setId( id );
    return node;
  }

  public static ClaimRecipient getClaimRecipient()
  {
    ClaimRecipient r = new ClaimRecipient();
    return r;
  }

  public static RecognitionClaimSubmission getClaimSubmission()
  {
    Long userId = UserManager.getUserId();
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( RecognitionClaimSource.UNKNOWN, userId, 1L, 1L );
    return submission;
  }

  public static NominationSubmitDataPromotionValueBean getNomPromoVO( Long promoId )
  {
    NominationSubmitDataPromotionValueBean bean = new NominationSubmitDataPromotionValueBean();
    bean.setId( promoId );
    bean.setIndividualOrTeam( NominationAwardGroupType.TEAM );
    return bean;
  }

  public static NominationsParticipantDataValueBean.ParticipantValueBean getNominee( Long id )
  {
    NominationsParticipantDataValueBean.ParticipantValueBean pax = new NominationsParticipantDataValueBean.ParticipantValueBean();
    pax.setId( id );
    return pax;
  }

  public static NominationsParticipantDataValueBean.NodeValueBean getNodeVO( int id )
  {
    NominationsParticipantDataValueBean.NodeValueBean node = new NominationsParticipantDataValueBean.NodeValueBean();
    node.setId( id );
    node.setName( "nodeName" );
    return node;
  }

}

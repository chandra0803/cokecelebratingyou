
package com.biperf.core.service.throwdown.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.StackStandingDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.throwdown.StackStandingNodeService;
import com.biperf.core.service.throwdown.StackStandingParticipantService;

public class StackStandingServiceImplTest extends BaseServiceTest
{
  StackStandingServiceImpl stackStandingServiceImpl = new StackStandingServiceImpl();

  public StackStandingServiceImplTest( String test )
  {
    super( test );
  }

  // ** mockStackStandingDAO *//*
  private Mock mockStackStandingDAO = null;
  private Mock mockStackStandingNodeService = null;
  private Mock mockStackStandingParticipantService = null;
  private Mock mockPromotionService = null;
  private Mock mockNodeService = null;
  private Mock mockUserService = null;
  private Mock mockNodeTypeService = null;

  protected void setUp() throws Exception
  {
    super.setUp();
    mockStackStandingDAO = new Mock( StackStandingDAO.class );
    mockStackStandingNodeService = new Mock( StackStandingNodeService.class );
    mockStackStandingParticipantService = new Mock( StackStandingParticipantService.class );
    mockPromotionService = new Mock( PromotionService.class );
    mockNodeService = new Mock( NodeService.class );
    mockUserService = new Mock( UserService.class );
    mockNodeTypeService = new Mock( NodeTypeService.class );
    stackStandingServiceImpl.setStackStandingDAO( (StackStandingDAO)mockStackStandingDAO.proxy() );
    stackStandingServiceImpl.setStackStandingNodeService( (StackStandingNodeService)mockStackStandingNodeService.proxy() );
    stackStandingServiceImpl.setStackStandingParticipantService( (StackStandingParticipantService)mockStackStandingParticipantService.proxy() );
    stackStandingServiceImpl.setPromotionService( (PromotionService)mockPromotionService.proxy() );
    stackStandingServiceImpl.setNodeService( (NodeService)mockNodeService.proxy() );
    // stackStandingServiceImpl.setUserService( (UserService )mockUserService.proxy());
    stackStandingServiceImpl.setNodeTypeService( (NodeTypeService)mockNodeTypeService.proxy() );
  }

  public static StackStanding buildStackStanding()
  {
    StackStanding stackStand = new StackStanding();
    // building a promotion
    ThrowdownPromotion tdPromo = new ThrowdownPromotion();
    tdPromo = PromotionDAOImplTest.buildThrowdownPromotion( getUniqueString() );
    stackStand.setPromotion( tdPromo );
    stackStand.setRoundNumber( 1 );
    stackStand.setGuid( "34d746a5:142faa6a8b6:-8000" );
    stackStand.setPayoutsIssued( true );
    stackStand.setActive( true );

    StackStandingNode stackNode = new StackStandingNode();
    Node node = new Node();
    NodeType nodeType = new NodeType();
    nodeType.setId( new Long( 1 ) );
    nodeType.setName( "test" );
    nodeType.setNameCmKey( "NODE_TYPE_NAME" );
    nodeType.setCmAssetCode( "hierarchy.hierarchy " );
    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    nodeType.setAuditCreateInfo( auditCreateInfo );
    nodeType.setVersion( new Long( 1 ) );
    node.setNodeType( nodeType );
    node.setName( "test" );
    node.setPath( "//USA/West/Test" );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setId( new Long( 1 ) );
    hierarchy.setCmAssetCode( "test" );
    hierarchy.setNameCmKey( "test" );
    hierarchy.setActive( true );
    hierarchy.setPrimary( true );
    hierarchy.setDeleted( false );
    hierarchy.setNodeTypeRequired( false );
    hierarchy.setAuditCreateInfo( auditCreateInfo );
    hierarchy.setVersion( new Long( 1 ) );
    node.setHierarchy( hierarchy );
    node.setDeleted( false );
    node.setAuditCreateInfo( auditCreateInfo );
    node.setVersion( new Long( 1 ) );
    stackNode.setNode( node );

    StackStandingParticipant stackStandingParticipant = new StackStandingParticipant();
    stackStandingParticipant.setId( new Long( 1 ) );
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( getUniqueString() );
    stackStandingParticipant.setParticipant( pax );
    stackStandingParticipant.setStackStandingFactor( BigDecimal.valueOf( 10.456 ) );
    stackStandingParticipant.setTied( false );
    stackStandingParticipant.setStanding( 1 );
    stackStandingParticipant.setPayoutsIssued( false );
    stackStandingParticipant.setStackStandingNode( stackNode );
    stackStandingParticipant.setStanding( 1 );

    stackNode.addStackStandingParticipant( stackStandingParticipant );
    stackStand.addStackStandingNode( stackNode );

    return stackStand;

  }

  public void testSave()
  {
    StackStanding stackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "save" ).with( same( stackStanding ) ).will( returnValue( stackStanding ) );
    StackStanding expectedStackStanding = this.stackStandingServiceImpl.save( stackStanding );
    assertEquals( "Actual saved stackstandis equal to what was expected", stackStanding, expectedStackStanding );
    mockStackStandingDAO.verify();
  }

  public void testGetAll()
  {
    List<StackStanding> expectedStackStandingList = new ArrayList<StackStanding>();
    StackStanding stackStanding = buildStackStanding();
    StackStanding stackStanding1 = buildStackStanding();
    StackStanding stackStanding2 = buildStackStanding();

    expectedStackStandingList.add( stackStanding );
    expectedStackStandingList.add( stackStanding1 );
    expectedStackStandingList.add( stackStanding2 );

    mockStackStandingDAO.expects( once() ).method( "getAll" ).will( returnValue( expectedStackStandingList ) );
    List<StackStanding> actualList = stackStandingServiceImpl.getAll();

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedStackStandingList ) );
    mockStackStandingDAO.verify();

  }

  public void testGetRankings()
  {
    List<StackStanding> actualStackStandingList = new ArrayList<StackStanding>();
    StackStanding stackStanding = buildStackStanding();
    StackStanding stackStanding1 = buildStackStanding();
    StackStanding stackStanding2 = buildStackStanding();

    actualStackStandingList.add( stackStanding );
    actualStackStandingList.add( stackStanding1 );
    actualStackStandingList.add( stackStanding2 );

    mockStackStandingDAO.expects( once() ).method( "getRankings" ).will( returnValue( actualStackStandingList ) );
    List<StackStanding> expectedList = stackStandingServiceImpl.getRankings();

    assertTrue( "Actual set contains expected set for getAll.", expectedList.containsAll( actualStackStandingList ) );
    mockStackStandingDAO.verify();

  }

  public void testGetRankingsForPromotion()
  {
    StackStanding stackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "save" ).with( same( stackStanding ) ).will( returnValue( stackStanding ) );
    StackStanding savedStackStanding = this.stackStandingServiceImpl.save( stackStanding );
    List<StackStanding> actualStackStandingList = new ArrayList<StackStanding>();
    actualStackStandingList.add( stackStanding );
    mockStackStandingDAO.expects( once() ).method( "getRankingsForPromotion" ).will( returnValue( actualStackStandingList ) );
    List<StackStanding> expectedList = stackStandingServiceImpl.getRankingsForPromotion( savedStackStanding.getPromotion().getId() );
    assertTrue( "StackStanding Should Be fetch all ", expectedList.size() > 0 );
  }

  public void testGetRankingForPromotionAndRound()
  {
    StackStanding actualStackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "save" ).with( same( actualStackStanding ) ).will( returnValue( actualStackStanding ) );
    StackStanding savedStackStanding = this.stackStandingServiceImpl.save( actualStackStanding );
    mockStackStandingDAO.expects( once() ).method( "getRankingForPromotionAndRound" ).will( returnValue( savedStackStanding ) );
    StackStanding expectedStackStanding = stackStandingServiceImpl.getRankingForPromotionAndRound( savedStackStanding.getPromotion().getId(), savedStackStanding.getRoundNumber() );
    assertEquals( "Actual saved stackstand is equal to what was expected", actualStackStanding, expectedStackStanding );
    mockStackStandingDAO.verify();
  }

  public void testGetApprovedRankings()
  {
    List<StackStanding> actualStackStandingList = new ArrayList<StackStanding>();
    StackStanding stackStanding = buildStackStanding();
    StackStanding stackStanding1 = buildStackStanding();
    StackStanding stackStanding2 = buildStackStanding();

    actualStackStandingList.add( stackStanding );
    actualStackStandingList.add( stackStanding1 );
    actualStackStandingList.add( stackStanding2 );

    mockStackStandingDAO.expects( once() ).method( "getApprovedRankings" ).will( returnValue( actualStackStandingList ) );
    List<StackStanding> expectedList = stackStandingServiceImpl.getApprovedRankings();

    assertTrue( "Actual set contains expected set for getAll.", expectedList.containsAll( actualStackStandingList ) );
    mockStackStandingDAO.verify();

  }

  public void testGetApprovedRankingForPromotionAndRound()
  {
    StackStanding actualStackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "save" ).with( same( actualStackStanding ) ).will( returnValue( actualStackStanding ) );
    StackStanding savedStackStanding = this.stackStandingServiceImpl.save( actualStackStanding );
    mockStackStandingDAO.expects( once() ).method( "getApprovedRankingForPromotionAndRound" ).will( returnValue( savedStackStanding ) );
    StackStanding expectedStackStanding = stackStandingServiceImpl.getApprovedRankingForPromotionAndRound( savedStackStanding.getPromotion().getId(), savedStackStanding.getRoundNumber() );
    assertEquals( "Actual saved stackstand is equal to what was expected", actualStackStanding, expectedStackStanding );
    mockStackStandingDAO.verify();
  }

  public void testGetUnapprovedRankings()
  {
    List<StackStanding> actualStackStandingList = new ArrayList<StackStanding>();
    StackStanding stackStanding = buildStackStanding();
    actualStackStandingList.add( stackStanding );
    mockStackStandingDAO.expects( once() ).method( "getUnapprovedRankings" ).will( returnValue( actualStackStandingList ) );
    List<StackStanding> expectedStackStandingList = this.stackStandingServiceImpl.getUnapprovedRankings();
    assertTrue( "Actual set contains expected set", expectedStackStandingList.containsAll( actualStackStandingList ) );
    mockStackStandingDAO.verify();
  }

  public void testGetUnapprovedRankingsForPromotion()
  {
    List<StackStanding> actualStackStandingList = new ArrayList<StackStanding>();
    StackStanding stackStanding = buildStackStanding();
    actualStackStandingList.add( stackStanding );
    mockStackStandingDAO.expects( once() ).method( "getUnapprovedRankingsForPromotion" ).will( returnValue( actualStackStandingList ) );
    List<StackStanding> expectedStackStandingList = this.stackStandingServiceImpl.getUnapprovedRankingsForPromotion( stackStanding.getPromotion().getId() );
    assertTrue( "Actual set contains expected set", expectedStackStandingList.containsAll( actualStackStandingList ) );
    mockStackStandingDAO.verify();
  }

  public void testGetUnapprovedRankingForPromotionAndRound()
  {
    StackStanding actualStackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "save" ).with( same( actualStackStanding ) ).will( returnValue( actualStackStanding ) );
    StackStanding savedStackStanding = this.stackStandingServiceImpl.save( actualStackStanding );
    mockStackStandingDAO.expects( once() ).method( "getUnapprovedRankingForPromotionAndRound" ).will( returnValue( savedStackStanding ) );
    StackStanding expectedStackStanding = stackStandingServiceImpl.getUnapprovedRankingForPromotionAndRound( savedStackStanding.getPromotion().getId(), savedStackStanding.getRoundNumber() );
    assertEquals( "Actual saved stackstand is equal to what was expected", actualStackStanding, expectedStackStanding );
    mockStackStandingDAO.verify();
  }

  public void testIsAnyPaxPaidOutForRanking()
  {
    StackStanding actualStackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "save" ).with( same( actualStackStanding ) ).will( returnValue( actualStackStanding ) );
    StackStanding savedStackStanding = stackStandingServiceImpl.save( actualStackStanding );
    mockStackStandingDAO.expects( once() ).method( "isAnyPaxPaidOutForRanking" ).will( returnValue( savedStackStanding.isPayoutsIssued() && savedStackStanding.isActive() ) );
    boolean isTaxPaidOut = stackStandingServiceImpl.isAnyPaxPaidOutForRanking( savedStackStanding.getId() );
    assertTrue( "Tax paid out for ranking ", isTaxPaidOut );
    assertEquals( true, isTaxPaidOut );
    mockStackStandingDAO.verify();
  }

  public void testGetNodeRankForUser()
  {
    StackStanding actualStackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "getNodeRankForUser" )
        .will( returnValue( actualStackStanding.getStackStandingNodes().iterator().next().getStackStandingParticipants().iterator().next().getStanding() ) );
    int rank = stackStandingServiceImpl.getNodeRankForUser( actualStackStanding.getPromotion().getId(),
                                                            actualStackStanding.getRoundNumber(),
                                                            actualStackStanding.getStackStandingNodes().iterator().next().getStackStandingParticipants().iterator().next().getParticipant().getId(),
                                                            actualStackStanding.getStackStandingNodes().iterator().next().getId() );
    assertTrue( "The rank is one ", rank == 1 );
  }

  public void testGetHierarchyRankForUser()
  {
    StackStanding actualStackStanding = buildStackStanding();
    mockStackStandingDAO.expects( once() ).method( "getHierarchyRankForUser" )
        .will( returnValue( actualStackStanding.getStackStandingNodes().iterator().next().getStackStandingParticipants().iterator().next().getStanding() ) );
    int rank = stackStandingServiceImpl.getHierarchyRankForUser( actualStackStanding.getPromotion()
        .getId(), actualStackStanding.getRoundNumber(), actualStackStanding.getStackStandingNodes().iterator().next().getStackStandingParticipants().iterator().next().getParticipant().getId() );
    assertFalse( "The rank is one ", ! ( rank == 1 ) );
  }

}

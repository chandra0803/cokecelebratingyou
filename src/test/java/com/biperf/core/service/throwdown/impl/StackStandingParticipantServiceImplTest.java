
package com.biperf.core.service.throwdown.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.jmock.Mock;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.StackStandingParticipantDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackStanding;
import com.biperf.core.domain.promotion.StackStandingNode;
import com.biperf.core.domain.promotion.StackStandingParticipant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.BaseServiceTest;

public class StackStandingParticipantServiceImplTest extends BaseServiceTest
{
  StackStandingParticipantServiceImpl stackStandingParticipantServiceImpl = new StackStandingParticipantServiceImpl();

  public StackStandingParticipantServiceImplTest( String test )
  {
    super( test );
  }

  // ** mockStackStandingParticipantDAO *//*
  private Mock mockStackStandingParticipantDAO = null;

  protected void setUp() throws Exception
  {
    super.setUp();
    mockStackStandingParticipantDAO = new Mock( StackStandingParticipantDAO.class );
    stackStandingParticipantServiceImpl.setStackStandingParticipantDAO( (StackStandingParticipantDAO)mockStackStandingParticipantDAO.proxy() );
  }

  public static StackStandingParticipant buildStackStandingParticipant()
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
    stackStandingParticipant.setAuditCreateInfo( auditCreateInfo );
    stackStandingParticipant.setVersion( new Long( 1 ) );

    return stackStandingParticipant;
  }

  public void testSaveStackStandingParticipant()
  {
    StackStandingParticipant stackStandingParticipant = buildStackStandingParticipant();
    mockStackStandingParticipantDAO.expects( once() ).method( "saveStackStandingParticipant" ).with( same( stackStandingParticipant ) ).will( returnValue( stackStandingParticipant ) );
    StackStandingParticipant expectedStackStandingParticipant = this.stackStandingParticipantServiceImpl.saveStackStandingParticipant( stackStandingParticipant );
    assertEquals( "Actual saved stackstandparticipant is equal to what was expected", stackStandingParticipant, expectedStackStandingParticipant );
    mockStackStandingParticipantDAO.verify();
  }

  public void testGetStackStandingParticipant()
  {
    StackStandingParticipant stackStandingParticipant = buildStackStandingParticipant();
    mockStackStandingParticipantDAO.expects( once() ).method( "getStackStandingParticipant" ).with( same( stackStandingParticipant.getId() ) ).will( returnValue( stackStandingParticipant ) );
    StackStandingParticipant expectedStackStandingParticipant = this.stackStandingParticipantServiceImpl.getStackStandingParticipant( stackStandingParticipant.getId() );
    assertEquals( "Actual StackStandingParticipant is equals to what was expected.", stackStandingParticipant, expectedStackStandingParticipant );
  }

}


package com.biperf.core.service.promotion.impl;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.promotion.StackRankParticipantDAO;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.StackRankParticipant;
import com.biperf.core.service.participant.impl.ParticipantServiceImplTest;
import com.biperf.core.utils.MockContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

public class StackRankParticipantServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public StackRankParticipantServiceImplTest( String test )
  {
    super( test );
  }

  private StackRankParticipantServiceImpl classUnderTest = new StackRankParticipantServiceImpl();

  private Mock mockStackRankParticipantDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {

    // Prepares the pickListFactory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }
    mockStackRankParticipantDAO = new Mock( StackRankParticipantDAO.class );
    classUnderTest.setStackRankParticipantDAO( (StackRankParticipantDAO)mockStackRankParticipantDAO.proxy() );
  }

  /**
   * Test getting the stack rank participant by the ID.
   */
  public void testGetStackRankParticipantById()
  {

    StackRankParticipant expectedParticipant = buildStackRankParticipant( "Joseph", "Campbell", "055567704" );

    mockStackRankParticipantDAO.expects( once() ).method( "getStackRankParticipant" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );

    StackRankParticipant actualParticipant = this.classUnderTest.getStackRankParticipant( expectedParticipant.getId() );

    assertEquals( "Actual Stack Rank Participant wasn't equals to what was expected", expectedParticipant, actualParticipant );

  }

  /**
   * Build a static StackRankParticipant for testing.
   * 
   * @return StackRankParticipant
   */
  private static StackRankParticipant buildStackRankParticipant( String fName, String lName, String awardBanqNbr )
  {
    StackRankParticipant stackRankPax = new StackRankParticipant();

    Participant pax = ParticipantServiceImplTest.buildStaticParticipant();
    pax.setFirstName( fName );
    pax.setLastName( lName );
    pax.setAwardBanqNumber( awardBanqNbr );
    stackRankPax.setParticipant( pax );

    stackRankPax.setPayout( new Long( 10 ) );
    stackRankPax.setRank( 1 );
    stackRankPax.setStackRankFactor( 5 );
    stackRankPax.setTied( false );

    return stackRankPax;
  }

  /**
   * Build a static StackRankNode for testing.
   * 
   * @return StackRankNode
   */
  /*
   * private static StackRankNode buildStackRankNode( Long parentNodeId, Long thisNodeId, StackRank
   * stackRank ) { StackRankNode stackRankNode = new StackRankNode(); stackRankNode.setNode(
   * buildNode( parentNodeId, thisNodeId ) ); stackRankNode.setStackRank( stackRank ); Set
   * aSetOfStackRankPaxs = new HashSet(); StackRankParticipant pax1 = buildStackRankParticipant(
   * "Zoe", "Pinto", "055567704" ); pax1.setStackRankNode( stackRankNode ); aSetOfStackRankPaxs.add(
   * pax1 ); StackRankParticipant pax2 = buildStackRankParticipant( "Sarah", "Pinto", "055567704" );
   * pax2.setStackRankNode( stackRankNode ); aSetOfStackRankPaxs.add( pax2 ); StackRankParticipant
   * pax3 = buildStackRankParticipant( "Greg", "Pinto", "055567704" ); pax3.setStackRankNode(
   * stackRankNode ); aSetOfStackRankPaxs.add( pax3 ); stackRankNode.setStackRankParticipants(
   * aSetOfStackRankPaxs ); return stackRankNode; }
   */
  /**
   * Build a static Node for testing.
   * 
   * @return Node
   */
  /*
   * private static Node buildNode( Long parentNodeId, Long thisNodeId ) { Node parentNode = new
   * Node(); parentNode.setId( parentNodeId ); parentNode.setPath( "/" ); Node node = new Node();
   * node.setId( thisNodeId ); node.setName( "testNAME" ); node.setDescription( "testDESCRIPTION" );
   * node.setParentNode( parentNode ); Hierarchy hierarchy = NodeServiceImplTest.buildHierarchy();
   * node.setHierarchy( hierarchy ); NodeType nodeType = new NodeType(); nodeType.setId( new Long( 1
   * ) ); node.setNodeType( nodeType ); return node; }
   */
  /**
   * Build a static Stack Rank for testing.
   * 
   * @return Stack Rank
   */
  /*
   * private static StackRank buildStackRank() { StackRank stackRank = new StackRank();
   * stackRank.setGuid( GuidUtils.generateGuid() ); stackRank.setPromotion(
   * PromotionServiceImplTest.buildProductClaimPromotion( "1" ) ); Set aSetOfStackRankNodes = new
   * HashSet(); aSetOfStackRankNodes.add( buildStackRankNode( new Long( 100 ), new Long( 1 ),
   * stackRank ) ); aSetOfStackRankNodes.add( buildStackRankNode( new Long( 200 ), new Long( 2 ),
   * stackRank ) ); aSetOfStackRankNodes.add( buildStackRankNode( new Long( 300 ), new Long( 3 ),
   * stackRank ) ); stackRank.setStackRankNodes( aSetOfStackRankNodes ); stackRank.setStartDate(
   * DateUtils.toDate( "01/01/2006" ) ); stackRank.setState( mockStackRankStatus );
   * stackRank.setEndDate( DateUtils.toDate( "12/31/2006" ) ); stackRank.setCalculatePayout( true );
   * return stackRank; }
   */
}

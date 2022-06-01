
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.jmock.Mock;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.fileload.BudgetImportRecord;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;

/**
 * BudgetImportStrategyTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th></th>
 * </tr>
 * <tr>
 * <td>Travis Jenniges</td>
 * <td>Sep 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetImportStrategyTest extends BaseServiceTest
{
  private UserService userServiceMock;
  private ParticipantService paxServiceMock;
  private NodeService nodeServiceMock;
  private Mock promotionServiceMock;
  private BudgetMasterService budgetServiceMock;

  private BudgetImportStrategy importStrategyUnderTest;

  public void setUp() throws Exception
  {
    super.setUp();
    importStrategyUnderTest = new BudgetImportStrategy();

    userServiceMock = EasyMock.createMock( UserService.class );
    importStrategyUnderTest.setUserService( userServiceMock );

    paxServiceMock = EasyMock.createMock( ParticipantService.class );
    importStrategyUnderTest.setPaxService( paxServiceMock );

    nodeServiceMock = EasyMock.createMock( NodeService.class );
    importStrategyUnderTest.setNodeService( nodeServiceMock );

    promotionServiceMock = new Mock( PromotionService.class );
    importStrategyUnderTest.setPromotionService( (PromotionService)promotionServiceMock.proxy() );

    budgetServiceMock = EasyMock.createMock( BudgetMasterService.class );
    importStrategyUnderTest.setBudgetService( budgetServiceMock );
  }

  public void testVerifyCleanUserBudgetImportFile() throws ServiceErrorException
  {
    ImportFile file = buildCleanUserBudgetImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    Participant pax = new Participant();
    Set<Participant> paxs = new HashSet<>( 1 );
    paxs.add( pax );
    for ( Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext(); )
    {
      // paxServiceControl.expectAndReturn( paxServiceMock.getAllEligiblePaxForPromotion( file
      // .getPromotion().getId(), true ), paxs );
      BudgetImportRecord record = (BudgetImportRecord)iter.next();
      if ( record.getUserId() != null )
      {
        EasyMock.expect( paxServiceMock.getParticipantById( record.getUserId() ) ).andReturn( new Participant() );
      }
      EasyMock.expect( userServiceMock.getUserById( record.getUserId() ) ).andReturn( new User() );
      // promotionServiceMock.expects( once() ).method( "isParticipantMemberOfPromotionAudience"
      // ).with( new IsAnything(), new IsAnything(), new IsAnything(), new IsAnything() ).will(
      // returnValue( true ) );
    }
    // userServiceControl.expectAndReturn( userServiceMock.getUserById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[0]).getUserId() ),
    // new User() );
    // userServiceControl.expectAndReturn( userServiceMock.getUserById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[1]).getUserId() ),
    // new User() );
    final Map<String, Object> result = new HashMap<>();
    result.put( "p_out_total_error_rec", new BigDecimal( 0 ) );
    result.put( "p_out_returncode", new BigDecimal( 0 ) );
    EasyMock.expect( budgetServiceMock.verifyImportFile( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( result );
    EasyMock.replay( budgetServiceMock );

    EasyMock.replay( userServiceMock );
    EasyMock.replay( paxServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getBudgetImportRecords() ) );

    // paxServiceControl.verify();
    // userServiceControl.verify();

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  // NOT READY TO CALL THIS YET AS LINK BETWEEN PROMOTION AND BUDGETMASTER DOES NOT EXIST YET
  //
  // public void testImportCleanUserBudgetImportFile()
  // {
  // ImportFile file = buildCleanUserBudgetImportFile();
  // file.setStatus( (ImportFileStatusType)MockPickListFactory
  // .getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFIED ) );
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  //
  // try
  // {
  // for(Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext();){
  // BudgetImportRecord record = (BudgetImportRecord)iter.next();
  // User user = new User();
  // user.setId(new Long(1));
  // userServiceControl.expectAndReturn(userServiceMock.getUserByUserName(record.getUserName()),
  // user);
  // Budget budget = new Budget();
  // budget.setStatus((BudgetStatusType)MockPickListFactory
  // .getMockPickListItem( BudgetStatusType.class, "active") );
  // budget.setUser(user);
  // budgetServiceControl.expectAndReturn(budgetServiceMock.addUserBudget(new Long(1), new Long(1),
  // budget), budget);
  // }
  // userServiceControl.replay();
  // budgetServiceControl.replay();
  // importStrategyUnderTest.importImportFile( file );
  // userServiceControl.verify();
  // budgetServiceControl.verify();
  // }
  // catch( ServiceErrorException e )
  // {
  // fail( "" + e );
  // }
  //
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  // }

  public void testVerifyCleanNodeBudgetImportFile() throws ServiceErrorException
  {
    ImportFile file = buildCleanNodeBudgetImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    Participant pax = new Participant();
    Set paxs = new HashSet( 1 );
    paxs.add( pax );

    for ( Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext(); )
    {
      // paxServiceControl.expectAndReturn( paxServiceMock.getAllEligiblePaxForPromotion( file
      // .getPromotion().getId(), true ), paxs );

      BudgetImportRecord record = (BudgetImportRecord)iter.next();

      Node node = new Node();
      node.setId( record.getNodeId() );
      node.setName( record.getNodeId().toString() );
      UserNode userNode = new UserNode( pax, node );
      userNode.setIsPrimary( Boolean.TRUE );
      pax.addUserNode( userNode );
      // promotionServiceMock.expects( once() ).method( "isPromotionClaimableByNode" ).with( new
      // IsAnything(), same(node) ).will( returnValue( true ) );

      EasyMock.expect( nodeServiceMock.getNodeById( record.getNodeId() ) ).andReturn( node );
    }
    // nodeServiceControl.expectAndReturn( nodeServiceMock.getNodeById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[0]).getNodeId()),
    // new User() );
    // nodeServiceControl.expectAndReturn( nodeServiceMock.getNodeById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[1]).getNodeId() ),
    // new User() );

    final Map<String, Object> result = new HashMap<>();
    result.put( "p_out_total_error_rec", new BigDecimal( 0 ) );
    result.put( "p_out_returncode", new BigDecimal( 0 ) );
    EasyMock.expect( budgetServiceMock.verifyImportFile( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( result );
    EasyMock.replay( budgetServiceMock );

    // paxServiceControl.replay();
    EasyMock.replay( nodeServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getBudgetImportRecords() ) );
    // paxServiceControl.verify();
    // nodeServiceControl.verify();

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  // NOT READY TO CALL THIS YET AS LINK BETWEEN PROMOTION AND BUDGETMASTER DOES NOT EXIST YET
  //
  // public void testImportCleanNodeBudgetImportFile()
  // {
  // ImportFile file = buildCleanNodeBudgetImportFile();
  // file.setStatus( (ImportFileStatusType)MockPickListFactory
  // .getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFIED ) );
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  //
  // try
  // {
  // for(Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext();){
  // BudgetImportRecord record = (BudgetImportRecord)iter.next();
  // Node node = new Node();
  // node.setId(record.getNodeId());
  // nodeServiceControl.expectAndReturn(nodeServiceMock.getNodeById(record.getNodeId()), node);
  // Budget budget = new Budget();
  // budget.setStatus((BudgetStatusType)MockPickListFactory
  // .getMockPickListItem( BudgetStatusType.class, "active") );
  // budget.setNode(node);
  // budgetServiceControl.expectAndReturn(budgetServiceMock.addNodeBudget(new Long(1),
  // record.getNodeId(), budget), budget);
  // }
  // nodeServiceControl.replay();
  // budgetServiceControl.replay();
  // importStrategyUnderTest.importImportFile( file );
  // nodeServiceControl.verify();
  // budgetServiceControl.verify();
  // }
  // catch( ServiceErrorException e )
  // {
  // fail( "" + e );
  // }
  //
  // assertTrue( file.getImportRecordErrors().isEmpty() );
  // }

  public void testVerifyDirtyPaxImportFile() throws ServiceErrorException
  {
    ImportFile file = buildBadPaxBudgetImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    Participant pax = new Participant();
    Set paxs = new HashSet( 1 );
    paxs.add( pax );
    for ( Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext(); )
    {
      // paxServiceControl.expectAndReturn( paxServiceMock.getAllEligiblePaxForPromotion( file
      // .getPromotion().getId(), true ), paxs );
      BudgetImportRecord record = (BudgetImportRecord)iter.next();

      if ( record.getUserId() != null )
      {
        if ( record.getUserId().longValue() == 6 )
        {
          EasyMock.expect( userServiceMock.getUserById( record.getUserId() ) ).andReturn( new User() );
          EasyMock.expect( paxServiceMock.getParticipantById( record.getUserId() ) ).andReturn( new Participant() );
        }
        else if ( record.getUserId().longValue() == 123 )
        {
          EasyMock.expect( userServiceMock.getUserById( record.getUserId() ) ).andReturn( null );
        }
      }
    }
    // userServiceControl.expectAndReturn( userServiceMock.getUserById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[0]).getUserId() ),
    // new User() );
    // userServiceControl.expectAndReturn( userServiceMock.getUserById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[1]).getUserId() ),
    // new User() );
    // userServiceControl.expectAndReturn( userServiceMock.getUserById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[2]).getUserId() ),
    // new User() );
    EasyMock.replay( userServiceMock );
    EasyMock.replay( paxServiceMock );
    // promotionServiceMock.expects( once() ).method( "isParticipantMemberOfPromotionAudience"
    // ).with( new IsAnything(), new IsAnything(), new IsAnything(), new IsAnything() ).will(
    // returnValue( true ) );

    final int ERROR_RECORD_COUNT = 3;
    final Map<String, Object> result = new HashMap<>();
    result.put( "p_out_total_error_rec", new BigDecimal( ERROR_RECORD_COUNT ) );
    result.put( "p_out_returncode", new BigDecimal( 0 ) );
    EasyMock.expect( budgetServiceMock.verifyImportFile( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( result );
    EasyMock.replay( budgetServiceMock );

    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getBudgetImportRecords() ) );
    // userServiceControl.verify();
    // paxServiceControl.verify();

    // assertFalse( file.getImportRecordErrors().isEmpty() );
    assertEquals( ERROR_RECORD_COUNT, file.getImportRecordErrorCount() );
  }

  public void testVerifyDirtyNodeImportFile() throws ServiceErrorException
  {
    ImportFile file = buildBadNodeBudgetImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    Participant pax = new Participant();
    Set paxs = new HashSet( 1 );
    paxs.add( pax );
    for ( Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext(); )
    {
      // paxServiceControl.expectAndReturn( paxServiceMock.getAllEligiblePaxForPromotion( file
      // .getPromotion().getId(), true ), paxs );
      BudgetImportRecord record = (BudgetImportRecord)iter.next();
      if ( record.getNodeId() != null )
      {
        if ( record.getNodeId().longValue() == 1000 )
        {
          EasyMock.expect( nodeServiceMock.getNodeById( record.getNodeId() ) ).andReturn( new Node() );
        }
        else if ( record.getNodeId().longValue() == 1001 )
        {
          EasyMock.expect( nodeServiceMock.getNodeById( record.getNodeId() ) ).andReturn( null );
        }

      }
    }
    // nodeServiceControl.expectAndReturn( nodeServiceMock.getNodeById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[0]).getNodeId()),
    // new User() );
    // nodeServiceControl.expectAndReturn( nodeServiceMock.getNodeById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[1]).getNodeId() ),
    // new User() );
    // nodeServiceControl.expectAndReturn( nodeServiceMock.getNodeById(
    // ((BudgetImportRecord)file.getImportRecords().toArray()[2]).getNodeId() ),
    // new User() );

    // paxServiceControl.replay();
    EasyMock.replay( nodeServiceMock );
    // promotionServiceMock.expects( once() ).method( "isPromotionClaimableByNode" ).with( new
    // IsAnything(), new IsAnything() ).will( returnValue( true ) );

    final int ERROR_RECORD_COUNT = 3;
    final Map<String, Object> result = new HashMap<>();
    result.put( "p_out_total_error_rec", new BigDecimal( ERROR_RECORD_COUNT ) );
    result.put( "p_out_returncode", new BigDecimal( 0 ) );
    EasyMock.expect( budgetServiceMock.verifyImportFile( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( result );
    EasyMock.replay( budgetServiceMock );

    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getBudgetImportRecords() ) );
    // paxServiceControl.verify();
    // nodeServiceControl.verify();

    // assertFalse( file.getImportRecordErrors().isEmpty() );
    assertEquals( ERROR_RECORD_COUNT, file.getImportRecordErrorCount() );
  }

  public static ImportFile buildCleanUserBudgetImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.BUDGET ) );
    Set records = new HashSet( 2 );
    file.setBudgetImportRecords( records );

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setBudgetMaster( new BudgetMaster() );
    promotion.getBudgetMaster().setId( new Long( 1 ) );
    promotion.getBudgetMaster().setBudgetType( (BudgetType)MockPickListFactory.getMockPickListItem( BudgetType.class, BudgetType.PAX_BUDGET_TYPE ) );
    promotion.getBudgetMaster().setAwardType( (BudgetMasterAwardType)MockPickListFactory.getMockPickListItem( BudgetMasterAwardType.class, BudgetMasterAwardType.POINTS ) );

    file.setPromotion( promotion );

    BudgetImportRecord record = new BudgetImportRecord();
    record.setActionType( "add" );
    record.setBudgetAmount( new BigDecimal( 10 ) );
    record.setUserId( new Long( 6 ) );
    records.add( record );

    /*
     * BudgetImportRecord record2 = new BudgetImportRecord(); record2.setActionType( "add" );
     * record2.setBudgetAmount( new BigDecimal( 10 ) ); record2.setUserId( new Long( 2 ) );
     * records.add( record2 );
     */

    return file;
  }

  public static ImportFile buildCleanNodeBudgetImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.BUDGET ) );
    Set records = new HashSet( 2 );
    file.setBudgetImportRecords( records );

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setBudgetMaster( new BudgetMaster() );
    promotion.getBudgetMaster().setId( new Long( 1 ) );
    promotion.getBudgetMaster().setBudgetType( (BudgetType)MockPickListFactory.getMockPickListItem( BudgetType.class, BudgetType.NODE_BUDGET_TYPE ) );
    promotion.getBudgetMaster().setAwardType( (BudgetMasterAwardType)MockPickListFactory.getMockPickListItem( BudgetMasterAwardType.class, BudgetMasterAwardType.POINTS ) );

    file.setPromotion( promotion );

    BudgetImportRecord record1 = new BudgetImportRecord();
    record1.setActionType( "add" );
    record1.setBudgetAmount( new BigDecimal( 10 ) );
    record1.setNodeId( new Long( 5001 ) );
    records.add( record1 );

    /*
     * BudgetImportRecord record3 = new BudgetImportRecord(); record3.setActionType( "add" );
     * record3.setBudgetAmount( new BigDecimal( 10 ) ); record3.setNodeId( new Long( 5005 ) );
     * records.add( record3 );
     */
    return file;
  }

  public static ImportFile buildBadNodeBudgetImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.BUDGET ) );

    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetType( (BudgetType)MockPickListFactory.getMockPickListItem( BudgetType.class, BudgetType.NODE_BUDGET_TYPE ) );
    budgetMaster.setAwardType( (BudgetMasterAwardType)MockPickListFactory.getMockPickListItem( BudgetMasterAwardType.class, BudgetMasterAwardType.POINTS ) );

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setBudgetMaster( budgetMaster );

    file.setPromotion( promotion );

    Set records = new HashSet( 3 );
    file.setBudgetImportRecords( records );

    BudgetImportRecord record = new BudgetImportRecord();
    record.setActionType( "add" );
    record.setBudgetAmount( null ); // budget amount is required
    record.setNodeId( new Long( 1000 ) );
    records.add( record );

    BudgetImportRecord record1 = new BudgetImportRecord();
    record1.setActionType( "add" );
    record1.setBudgetAmount( new BigDecimal( 10 ) );
    record1.setNodeId( null ); // node ID is required for a node budget
    records.add( record1 );

    BudgetImportRecord record2 = new BudgetImportRecord();
    record2.setActionType( "add" );
    record2.setBudgetAmount( new BigDecimal( 10 ) );
    record2.setNodeId( new Long( 1001 ) ); // node must exist with given node ID
    records.add( record2 );

    return file;
  }

  public static ImportFile buildBadPaxBudgetImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.BUDGET ) );

    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetType( (BudgetType)MockPickListFactory.getMockPickListItem( BudgetType.class, BudgetType.PAX_BUDGET_TYPE ) );
    budgetMaster.setAwardType( (BudgetMasterAwardType)MockPickListFactory.getMockPickListItem( BudgetMasterAwardType.class, BudgetMasterAwardType.POINTS ) );

    RecognitionPromotion promotion = new RecognitionPromotion();
    promotion.setPromotionType( PromotionType.lookup( PromotionType.RECOGNITION ) );
    promotion.setBudgetMaster( budgetMaster );

    file.setPromotion( promotion );

    Set records = new HashSet( 3 );
    file.setBudgetImportRecords( records );

    BudgetImportRecord record = new BudgetImportRecord();
    record.setActionType( "add" );
    record.setBudgetAmount( null ); // budget amount is required
    record.setUserId( new Long( 6 ) );
    records.add( record );

    BudgetImportRecord record1 = new BudgetImportRecord();
    record1.setActionType( "add" );
    record1.setBudgetAmount( new BigDecimal( 10 ) );
    record1.setUserId( null ); // username is required for participant budget
    records.add( record1 );

    BudgetImportRecord record2 = new BudgetImportRecord();
    record2.setActionType( "add" );
    record2.setBudgetAmount( new BigDecimal( 10 ) );
    record2.setUserId( new Long( 123 ) ); // user must exist with given user ID for participant
    // budget
    records.add( record2 );

    return file;
  }

  public static void addErrors( ImportFile file )
  {
    Set errors = new HashSet( 1 );
    errors.add( new ImportRecordError() );
    for ( Iterator iter = file.getBudgetImportRecords().iterator(); iter.hasNext(); )
    {
      BudgetImportRecord record = (BudgetImportRecord)iter.next();
      record.setImportRecordErrors( errors );
    }
  }
}
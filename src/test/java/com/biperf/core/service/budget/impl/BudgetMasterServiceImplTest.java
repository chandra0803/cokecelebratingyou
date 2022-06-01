/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/budget/impl/BudgetMasterServiceImplTest.java,v $
 */

package com.biperf.core.service.budget.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.cms.CMAssetService;

/**
 * BudgetMasterServiceImplTest.
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
 * <td>May 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public BudgetMasterServiceImplTest( String test )
  {
    super( test );
  }

  /** budgetMasterServiceImplementation */
  private BudgetMasterServiceImpl budgetMasterService = new BudgetMasterServiceImpl();

  /** mocks */
  private Mock mockBudgetMasterDAO = null;
  private Mock mockUserDAO = null;
  private Mock mockNodeDAO = null;
  private Mock mockCmAssetService = null;

  /**
   * Mock representation of a budgetMaster budget type.
   */
  private static final BudgetType mockBudgetTypePax = new BudgetType()
  {
    public String getCode()
    {
      return BudgetType.PAX_BUDGET_TYPE;
    }
  };

  /**
   * Mock representation of a budgetMaster award type.
   */
  private static final BudgetMasterAwardType mockAwardTypePoints = new BudgetMasterAwardType()
  {
    public String getCode()
    {
      return BudgetMasterAwardType.POINTS;
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
    mockBudgetMasterDAO = new Mock( BudgetMasterDAO.class );
    mockUserDAO = new Mock( UserDAO.class );
    mockNodeDAO = new Mock( NodeDAO.class );
    mockCmAssetService = new Mock( CMAssetService.class );

    budgetMasterService.setBudgetMasterDAO( (BudgetMasterDAO)mockBudgetMasterDAO.proxy() );
    budgetMasterService.setUserDAO( (UserDAO)mockUserDAO.proxy() );
    budgetMasterService.setNodeDAO( (NodeDAO)mockNodeDAO.proxy() );
    budgetMasterService.setCmAssetService( (CMAssetService)mockCmAssetService.proxy() );
  }

  /**
   * Test getting the BudgetMaster by id.
   */
  public void testGetBudgetMasterById()
  {
    // Get the test BudgetMaster.
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setId( new Long( 1 ) );
    budgetMaster.setBudgetName( "ServiceTestBudgetName" );
    budgetMaster.setMultiPromotion( true );

    // BudgetMasterDAO expected to call getBudgetMasterById once with the BudgetMasterId which will
    // return the BudgetMaster expected
    mockBudgetMasterDAO.expects( once() ).method( "getBudgetMasterById" ).with( same( budgetMaster.getId() ) ).will( returnValue( budgetMaster ) );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    budgetMasterService.getBudgetMasterById( budgetMaster.getId(), associationRequestCollection );

    mockBudgetMasterDAO.verify();
  }

  /**
   * Test updating the BudgetMaster through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveBudgetMasterUpdate()
      // throws UniqueConstraintViolationException,
      throws ServiceErrorException
  {

    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setId( new Long( 1 ) );
    budgetMaster.setBudgetName( "ServiceTestBudgetName" );
    budgetMaster.setMultiPromotion( true );

    BudgetMaster dbBudgetMaster = new BudgetMaster();
    dbBudgetMaster.setId( new Long( 1 ) );
    dbBudgetMaster.setBudgetName( "ServiceTestBudgetName" );
    dbBudgetMaster.setMultiPromotion( true );

    // update the user
    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetMaster" ).with( same( budgetMaster ) );
    // mockBudgetMasterDAO.expects( once() ).method( "getBudgetMasterByName" ).with( same(
    // budgetMaster.getBudgetName() ) )
    // .will( returnValue( dbBudgetMaster ) );
    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // test the budgetMasterService.saveBudgetMaster
    budgetMasterService.saveBudgetMaster( budgetMaster );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockBudgetMasterDAO.verify();
  }

  /**
   * Test adding the BudgetMaster through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveBudgetMasterInsert()
      // throws UniqueConstraintViolationException,
      throws ServiceErrorException
  {
    // Create the test BudgetMaster.
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetName( "ServiceTestBudgetName" );
    budgetMaster.setMultiPromotion( true );

    BudgetMaster savedBudgetMaster = new BudgetMaster();
    savedBudgetMaster.setId( new Long( 1 ) );
    savedBudgetMaster.setBudgetName( "ServiceTestBudgetName" );
    savedBudgetMaster.setMultiPromotion( true );

    // insert a new budgetMaster
    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetMaster" ).with( same( budgetMaster ) ).will( returnValue( savedBudgetMaster ) );

    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetMaster" ).with( same( savedBudgetMaster ) ).will( returnValue( savedBudgetMaster ) );

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );
    mockCmAssetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( "test.asset.name" + System.currentTimeMillis() ) );
    // test the BudgetMasterService.saveBudgetMaster
    budgetMasterService.saveBudgetMaster( budgetMaster );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    mockBudgetMasterDAO.verify();
  }

  // COMMENTED OUT - ASSOCIATED SOURCE IS COMMENTED
  /**
   * Test adding duplicate budgetMaster through the service.
   */
  // public void testSaveBudgetMasterInsertConstraintViolation()
  // {
  // todo jjd reimplement once cm2 is fully integrated
  // Create the test BudgetMaster.
  // BudgetMaster budgetMaster = new BudgetMaster();
  // budgetMaster.setBudgetName( "ServiceTestBudgetName" );
  // budgetMaster.setMultiPromotion( true );
  //
  // BudgetMaster dbBudgetMaster = new BudgetMaster();
  // dbBudgetMaster.setId( new Long( 1 ) );
  // dbBudgetMaster.setBudgetName( "ServiceTestBudgetName" );
  // dbBudgetMaster.setMultiPromotion( true );
  //
  // // insert a new budgetMaster
  // mockBudgetMasterDAO.expects( once() ).method( "saveBudgetMaster" ).with( same( budgetMaster )
  // )
  // .will( returnValue( budgetMaster ) );
  //
  // mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );
  //
  // try
  // {
  // // test the BudgetMasterService.saveBudgetMaster
  // budgetMasterService.saveBudgetMaster( budgetMaster );
  // }
  // catch( UniqueConstraintViolationException e )
  // {
  // return;
  // }
  //
  // fail( "Should have thrown UniqueConstraintViolationException" );
  // }

  /**
   * Test adding duplicate budget through the service.
   */
  // public void testSaveBudgetMasterUpdateConstraintViolation()
  // {
  // todo jjd reimplement once cm2 is fully integrated
  // Create the test BudgetMaster.
  // BudgetMaster budgetMaster = new BudgetMaster();
  // budgetMaster.setId( new Long( 1 ) );
  // budgetMaster.setBudgetName( "ServiceTestBudgetName" );
  // budgetMaster.setMultiPromotion( true );
  //
  // BudgetMaster dbBudgetMaster = new BudgetMaster();
  // dbBudgetMaster.setId( new Long( 2 ) );
  // dbBudgetMaster.setBudgetName( "ServiceTestBudgetName" );
  // dbBudgetMaster.setMultiPromotion( true );
  //
  // // insert a new budgetMaster
  //
  // try
  // {
  // // test the BudgetMasterService.saveBudgetMaster
  // budgetMasterService.saveBudgetMaster( budgetMaster );
  // }
  // catch( UniqueConstraintViolationException e )
  // {
  // return;
  // }
  //
  // fail( "Should have thrown UniqueConstraintViolationException" );
  // }

  /**
   * Test deleting the BudgetMaster through the service.
   * 
   * @throws ServiceErrorException
   */
  public void testDeleteBudgetMaster() throws ServiceErrorException
  {
    // Create the test BudgetMaster.
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setId( new Long( 1 ) );
    budgetMaster.setBudgetName( "ServiceTestBudgetName" );
    budgetMaster.setMultiPromotion( true );
    budgetMaster.setBudgetType( mockBudgetTypePax );
    budgetMaster.setAwardType( mockAwardTypePoints );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetMasterById" ).with( same( budgetMaster.getId() ) ).will( returnValue( budgetMaster ) );

    mockBudgetMasterDAO.expects( once() ).method( "deleteBudgetMaster" ).with( same( budgetMaster ) );

    mockCmAssetService.expects( once() ).method( "createOrUpdateAsset" );

    // test the BudgetMasterService.deleteBudgetMaster
    budgetMasterService.deleteBudgetMaster( budgetMaster.getId() );

    // Verify the mockDAO deletes.
    mockBudgetMasterDAO.verify();
  }

  /**
   * Test deleting the BudgetMaster with budgets through the service. This should error out and a
   * ServiceErrorException should be caught.
   */
  public void testDeleteBudgetMasterWithBudgetSegment()
  {
    // Create the test BudgetMaster.
    BudgetMaster budgetMaster = new BudgetMaster()
    {
      @Override
      public String getBudgetMasterName()
      {
        return "Name";
      }
    };
    budgetMaster.setId( new Long( 1 ) );
    budgetMaster.setBudgetName( "ServiceTestBudgetName" );
    budgetMaster.setMultiPromotion( true );
    budgetMaster.setBudgetType( mockBudgetTypePax );
    budgetMaster.setAwardType( mockAwardTypePoints );

    User user = new User();
    user.setId( new Long( 1 ) );

    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setUser( user );

    BudgetSegment segment = createBudgetSegmentForTest();

    segment.addBudget( budget );
    budgetMaster.addBudgetSegment( segment );
    mockBudgetMasterDAO.expects( once() ).method( "getBudgetMasterById" ).with( same( budgetMaster.getId() ) ).will( returnValue( budgetMaster ) );
    mockBudgetMasterDAO.expects( once() ).method( "getActiveBudgetCountByBudgetMaster" ).with( same( budgetMaster.getId() ) ).will( returnValue( 1 ) );

    try
    {
      // test the BudgetMasterService.deleteBudgetMaster
      budgetMasterService.deleteBudgetMaster( budgetMaster.getId() );
    }
    catch( ServiceErrorException e )
    {
      return;
    }

    fail( "Should have thrown ServiceErrorException" );

  }

  /**
   * Test get all BudgetMasters returns at least 1 BudgetMaster
   */
  public void testGetAllBudgetMasters()
  {
    List budgetMasters = new ArrayList();
    budgetMasters.add( new BudgetMaster() );
    mockBudgetMasterDAO.expects( once() ).method( "getAll" ).will( returnValue( budgetMasters ) );

    List returnedBudgetMasters = budgetMasterService.getAll();
    assertTrue( returnedBudgetMasters.size() > 0 );
  }

  /**
   * Test get all Active BudgetMasters returns at least 1 BudgetMaster
   */
  public void testGetAllActiveBudgetMasters()
  {
    List budgetMasters = new ArrayList();
    budgetMasters.add( new BudgetMaster() );
    mockBudgetMasterDAO.expects( once() ).method( "getAllActive" ).will( returnValue( budgetMasters ) );

    List returnedBudgetMasters = budgetMasterService.getAllActive();
    assertTrue( returnedBudgetMasters.size() > 0 );
  }

  /**
   * Test get all inactive BudgetMasters returns at least 1 BudgetMaster
   */
  public void testGetAllInactiveBudgetMasters()
  {
    List budgetMasters = new ArrayList();
    budgetMasters.add( new BudgetMaster() );
    mockBudgetMasterDAO.expects( once() ).method( "getAllInactive" ).will( returnValue( budgetMasters ) );

    List returnedBudgetMasters = budgetMasterService.getAllInactive();
    assertTrue( returnedBudgetMasters.size() > 0 );
  }

  /**
   * Test adding a user budget
   * 
   * @throws ServiceErrorException
   */
  public void testAddUserBudget() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );

    BudgetSegment segment = createBudgetSegmentForTest();
    segment.setId( 2L );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetSegmentById" ).with( same( segment.getId() ) ).will( returnValue( segment ) );

    mockUserDAO.expects( once() ).method( "getUserById" ).with( same( user.getId() ) ).will( returnValue( user ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForUserbyBudgetSegmentId" ).with( same( segment.getId() ), same( user.getId() ) );

    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetSegment" ).with( same( segment ) );

    budgetMasterService.addUserBudget( segment.getId(), user.getId(), budget );

    mockUserDAO.verify();
    mockBudgetMasterDAO.verify();
    assertTrue( segment.getBudgets().contains( budget ) );

  }

  /**
   * Test adding a node budget
   * 
   * @throws ServiceErrorException
   */
  public void testAddNodeBudget() throws ServiceErrorException
  {
    Node node = new Node();
    node.setId( new Long( 1 ) );
    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );

    BudgetSegment budgetSegment = createBudgetSegmentForTest();

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetSegmentById" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetSegment ) );

    mockNodeDAO.expects( once() ).method( "getNodeById" ).with( same( node.getId() ) ).will( returnValue( node ) );

    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetSegment" ).with( same( budgetSegment ) );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForNodeByBudgetSegmentId" ).with( same( budgetSegment.getId() ), same( node.getId() ) );

    budgetMasterService.addNodeBudget( budgetSegment.getId(), node.getId(), budget );

    mockUserDAO.verify();
    mockBudgetMasterDAO.verify();
    mockNodeDAO.verify();
    assertTrue( budgetSegment.getBudgets().size() == 1 );
    assertTrue( budgetSegment.getBudgets().contains( budget ) );
  }

  /**
   * Test get all of the budget records
   */
  public void testGetBudgets()
  {
    User user = new User();
    user.setId( new Long( 1 ) );

    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setUser( user );

    Node node = new Node();
    node.setId( new Long( 1 ) );

    Budget budget2 = new Budget();
    budget2.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget2.setCurrentValue( BigDecimal.valueOf( 100 ) );
    budget2.setNode( node );

    BudgetSegment budgetSegment = createBudgetSegmentForTest();

    budgetSegment.addBudget( budget );
    budgetSegment.addBudget( budget2 );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetSegmentById" ).with( same( budgetSegment.getId() ) ).will( returnValue( budgetSegment ) );

    Set budgets = budgetMasterService.getBudgets( budgetSegment.getId() );
    assertTrue( budgets.size() == 2 );
  }

  /**
   * Test get a budget by id
   */
  public void testGetBudget()
  {
    User user = new User();
    user.setId( new Long( 1 ) );

    Budget budget = new Budget();
    budget.setId( new Long( 1 ) );
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setUser( user );

    Node node = new Node();
    node.setId( new Long( 1 ) );

    Budget budget2 = new Budget();
    budget.setId( new Long( 2 ) );
    budget2.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget2.setCurrentValue( BigDecimal.valueOf( 100 ) );
    budget2.setNode( node );

    BudgetSegment budgetSegment = createBudgetSegmentForTest();

    budgetSegment.addBudget( budget );
    budgetSegment.addBudget( budget2 );

    mockBudgetMasterDAO.expects( once() ).method( "getBudget" ).with( same( budgetSegment.getId() ), same( budget.getId() ) ).will( returnValue( budget ) );

    Budget returnedBudget = budgetMasterService.getBudget( budgetSegment.getId(), budget.getId() );

    assertEquals( returnedBudget, budget );
    mockBudgetMasterDAO.verify();
  }

  /**
   * Test update user budget
   */
  public void testUpdateUserBudget() throws ServiceErrorException
  {
    User user = new User();
    user.setId( new Long( 1 ) );
    user.setUserName( "UNITTEST" );

    Budget budget = new Budget();
    budget.setUser( user );
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );

    Budget updatedBudget = new Budget();
    updatedBudget.setUser( user );
    updatedBudget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    updatedBudget.setCurrentValue( BigDecimal.valueOf( 100 ) );

    BudgetSegment budgetSegment = createBudgetSegmentForTest();
    budgetSegment.addBudget( budget );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForUserbyBudgetSegmentId" ).with( same( budgetSegment.getId() ), same( user.getId() ) ).will( returnValue( budget ) );

    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetSegment" ).with( same( budgetSegment ) ).will( returnValue( budgetSegment ) );

    budgetMasterService.updateUserBudget( budgetSegment, user.getId(), updatedBudget );

    assertEquals( updatedBudget.getOriginalValue(), budget.getOriginalValue() );

  } // testupdateUserBudget

  /**
   * Test update node budget
   */
  public void testUpdateNodeBudget() throws ServiceErrorException
  {
    Node node = new Node();
    node.setId( new Long( 1 ) );

    Budget budget = new Budget();
    budget.setNode( node );
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );

    Budget updatedBudget = new Budget();
    updatedBudget.setNode( node );
    updatedBudget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    updatedBudget.setCurrentValue( BigDecimal.valueOf( 100 ) );

    BudgetSegment budgetSegment = createBudgetSegmentForTest();
    budgetSegment.addBudget( budget );

    mockBudgetMasterDAO.expects( once() ).method( "getBudgetForNodeByBudgetSegmentId" ).with( same( budgetSegment.getId() ), same( node.getId() ) ).will( returnValue( budget ) );

    mockBudgetMasterDAO.expects( once() ).method( "saveBudgetSegment" ).with( same( budgetSegment ) ).will( returnValue( budgetSegment ) );

    budgetMasterService.updateNodeBudget( budgetSegment, node.getId(), updatedBudget );

    assertEquals( updatedBudget.getOriginalValue(), budget.getOriginalValue() );

  } // testupdateUserPhone

  private BudgetSegment createBudgetSegmentForTest()
  {
    BudgetSegment budgetSegment = new BudgetSegment();
    budgetSegment.setName( "segment1" );
    budgetSegment.setStartDate( new Date() );
    budgetSegment.setEndDate( new Date() );
    budgetSegment.setId( 2L );

    return budgetSegment;
  }
}

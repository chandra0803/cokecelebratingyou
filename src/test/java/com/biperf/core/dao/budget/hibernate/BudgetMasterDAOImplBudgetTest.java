/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/budget/hibernate/BudgetMasterDAOImplBudgetTest.java,v $
 */

package com.biperf.core.dao.budget.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.budget.BudgetQueryConstraint;
import com.biperf.core.dao.budget.limits.TextCharacteristicConstraintLimits;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * BudgetMastrDAOImplBudgetTest.
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
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterDAOImplBudgetTest extends BaseDAOTest
{

  private Long createdUserBudgetMasterId;
  private Long createdNodeBudgetMasterId;
  private Long createdUserCharacteristicId;
  private Long createdNodeCharacteristicId;
  private Long createdUserBudgetSegmentId;
  private Long createdNodeBudgetSegmentId;

  private BudgetMasterDAO budgetMasterDAO;
  private BudgetMaster testBudgetMaster;

  /**
   * Returns BudgetMasterDAO from the beanFactory.
   * 
   * @return BudgetMasterDAO
   */
  protected BudgetMasterDAO getBudgetMasterDAO()
  {
    return (BudgetMasterDAO)ApplicationContextFactory.getApplicationContext().getBean( "budgetMasterDAO" );
  }

  /**
   * Returns UserDAO from the beanFactory.
   * 
   * @return UserDAO
   */
  protected UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Returns NodeDAO from the beanFactory.
   * 
   * @return NodeDAO
   */
  private NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

  /**
   * Returns characteristic dao
   * 
   * @return CharacteristicDAO
   */
  protected CharacteristicDAO getUserCharacteristicDAO()
  {
    return (CharacteristicDAO)ApplicationContextFactory.getApplicationContext().getBean( "userCharacteristicDAO" );
  }

  /**
   * Returns characteristic dao
   * 
   * @return CharacteristicDAO
   */
  protected CharacteristicDAO getNodeCharacteristicDAO()
  {
    return (CharacteristicDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeTypeCharacteristicDAO" );
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    budgetMasterDAO = getBudgetMasterDAO();
  }

  public void testAddBudget()
  {
    BudgetMaster budgetMaster = createTestBudgetMaster();

    BudgetSegment budgetSegment = new BudgetSegment();
    budgetSegment.setName( "Segment1" );
    budgetSegment.setStatus( true );
    budgetSegment.setStartDate( new Date() );
    budgetSegment.setEndDate( new Date() );
    budgetSegment.setCmAssetCode( "unittest.cm.asset.code" );

    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

    budgetSegment.addBudget( budget );
    budgetMaster.addBudgetSegment( budgetSegment );

    BudgetMaster actualBudgetMaster = budgetMasterDAO.saveBudgetMaster( budgetMaster );
    HibernateSessionManager.getSession().flush();

    BudgetSegment updatedSeg = budgetMasterDAO.getBudgetSegmentById( budgetSegment.getId() );
    Iterator<Budget> iter = updatedSeg.getBudgets().iterator();
    while ( iter.hasNext() )
    {
      Budget currentBudget = iter.next();
      if ( currentBudget.equals( budget ) )
      {
        return;
      }
    }
    fail();
    // assertTrue( updatedSeg.getBudgets().contains( budget ) );
  }

  public void testUpdateBudget()
  {
    BudgetMaster budgetMaster = createTestBudgetMaster();

    BudgetSegment budgetSegment = new BudgetSegment();
    budgetSegment.setName( "Segment1" );
    budgetSegment.setStatus( true );
    budgetSegment.setStartDate( new Date() );
    budgetSegment.setEndDate( new Date() );
    budgetSegment.setCmAssetCode( "unittest.cm.asset.code" );

    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setStatus( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

    budgetSegment.addBudget( budget );
    budgetMaster.addBudgetSegment( budgetSegment );

    BudgetMaster actualBudgetMaster = budgetMasterDAO.saveBudgetMaster( budgetMaster );
    HibernateSessionManager.getSession().flush();

    Set<BudgetSegment> budgetSegments = actualBudgetMaster.getBudgetSegments();
    Iterator<BudgetSegment> budgetSegmentsIter = budgetSegments.iterator();

    BigDecimal valueFound = null;
    while ( budgetSegmentsIter.hasNext() )
    {
      BudgetSegment currentSeg = budgetSegmentsIter.next();
      Set<Budget> budgets = currentSeg.getBudgets();
      Iterator<Budget> budgetsIter = budgets.iterator();
      while ( budgetsIter.hasNext() )
      {
        Budget currentBudget = budgetsIter.next();
        if ( currentBudget.getId().equals( budget.getId() ) )
        {
          valueFound = currentBudget.getCurrentValue();
          break;
        }
      }
    }
    assertEquals( valueFound, budget.getCurrentValue() );

    budget.setCurrentValue( new BigDecimal( 300 ) );
    budgetMasterDAO.updateBudget( budget );
    Budget finalBudget = budgetMasterDAO.getBudgetbyId( budget.getId() );
    HibernateSessionManager.getSession().flush();
    assertEquals( finalBudget.getCurrentValue(), budget.getCurrentValue() );
  }

  public void testGetBudgetListWithUser()
  {
    createTestUserBudgets();
    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();
    BudgetQueryConstraint budgetQueryConstraint = new BudgetQueryConstraint();
    budgetQueryConstraint.setBudgetSegmentId( createdUserBudgetSegmentId );
    budgetQueryConstraint.setUserBasedConstraints( true );

    List budgetList = budgetMasterDAO.getBudgetList( budgetQueryConstraint );
    assertEquals( 3, budgetList.size() );

    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setCharacteristicId( createdUserCharacteristicId );
    textCharacteristicConstrantLimits.setTextValue( "VALUE" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_STARTS_WITH );

    Map constraintMap = new HashMap();
    constraintMap.put( textCharacteristicConstrantLimits.getCharacteristicId(), textCharacteristicConstrantLimits );
    budgetQueryConstraint.setCharacteristicConstraintLimits( constraintMap );
    budgetList = budgetMasterDAO.getBudgetList( budgetQueryConstraint );
    assertEquals( 1, budgetList.size() );
  }

  public void testGetBudgetListWithNode()
  {
    Long nodeTypeId = createTestNodeBudgets();
    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();
    BudgetQueryConstraint budgetQueryConstraint = new BudgetQueryConstraint();
    budgetQueryConstraint.setBudgetSegmentId( createdNodeBudgetSegmentId );
    budgetQueryConstraint.setUserBasedConstraints( false );
    List<Long> nodeTypeIds = new ArrayList<Long>();
    nodeTypeIds.add( nodeTypeId );
    budgetQueryConstraint.setNodeTypeIds( nodeTypeIds );
    List budgetList = budgetMasterDAO.getBudgetList( budgetQueryConstraint );
    assertEquals( 2, budgetList.size() );
    TextCharacteristicConstraintLimits textCharacteristicConstrantLimits = new TextCharacteristicConstraintLimits();
    textCharacteristicConstrantLimits.setCharacteristicId( createdNodeCharacteristicId );
    textCharacteristicConstrantLimits.setTextValue( "VALUE" );
    textCharacteristicConstrantLimits.setType( TextCharacteristicConstraintLimits.TYPE_STARTS_WITH );
    textCharacteristicConstrantLimits.setNodeTypeId( nodeTypeId );
    Map<Long, TextCharacteristicConstraintLimits> constraintMap = new HashMap<Long, TextCharacteristicConstraintLimits>();
    constraintMap.put( textCharacteristicConstrantLimits.getCharacteristicId(), textCharacteristicConstrantLimits );
    budgetQueryConstraint.setCharacteristicConstraintLimits( constraintMap );
    budgetList = budgetMasterDAO.getBudgetList( budgetQueryConstraint );
    assertEquals( 1, budgetList.size() );
  }

  public void testGetAllNotActive()
  {
    createTestUserBudgets();
    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();
    List budgetList = budgetMasterDAO.getAllBudgetsNotActive( createdUserBudgetSegmentId.longValue() );
    assertEquals( 1, budgetList.size() );
  }

  private BudgetMaster createTestBudgetMaster()
  {
    BudgetMaster testBudgetMaster = new BudgetMaster();
    testBudgetMaster.setBudgetName( "Test Budget Master" );
    testBudgetMaster.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    testBudgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    testBudgetMaster.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    testBudgetMaster.setNameCmKey( "New Asset Key" );
    testBudgetMaster.setCmAssetCode( "New Asset Name" );
    testBudgetMaster.setMultiPromotion( false );
    testBudgetMaster.setStartDate( new Date() );
    return testBudgetMaster;
  }

  private void createTestUserBudgets()
  {
    // Create new user
    User user1 = new User();
    user1.setUserType( UserType.lookup( UserType.BI ) );
    user1.setFirstName( "TestFIRSTNAME" );
    user1.setLastName( "TestLASTNAME" );
    user1.setPassword( "testPASSWORD" );
    user1.setUserName( "testUSERNAME1" );
    user1.setActive( Boolean.TRUE );
    user1.setWelcomeEmailSent( Boolean.FALSE );
    user1.setLastResetDate( new Date() );

    // Create a Characteristic
    CharacteristicDAO characteristicDAO = getUserCharacteristicDAO();

    UserCharacteristicType characteristic = new UserCharacteristicType();
    characteristic.setDescription( "description" );
    characteristic.setCharacteristicName( "XChar" );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    String uniqueName = String.valueOf( Math.random() % 29930293 );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
    characteristic.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );

    Characteristic actualCharacteristic = characteristicDAO.saveCharacteristic( characteristic );
    createdUserCharacteristicId = actualCharacteristic.getId();

    // Create User Characteristic
    UserCharacteristic userCharacteristic = new UserCharacteristic();
    userCharacteristic.setCharacteristicValue( "VALUE1" );
    userCharacteristic.setUserCharacteristicType( characteristic );
    userCharacteristic.setUser( user1 );

    user1.getUserCharacteristics().add( userCharacteristic );

    User actualUser1 = getUserDAO().saveUser( user1 );

    // Create new user
    User user2 = new User();
    user2.setUserType( UserType.lookup( UserType.BI ) );
    user2.setFirstName( "TestFIRSTNAME" );
    user2.setLastName( "TestLASTNAME" );
    user2.setPassword( "testPASSWORD" );
    user2.setUserName( "testUSERNAME2" );
    user2.setActive( Boolean.TRUE );
    user2.setWelcomeEmailSent( Boolean.FALSE );
    user2.setLastResetDate( new Date() );

    // Create User Characteristic
    UserCharacteristic userCharacteristic2 = new UserCharacteristic();
    userCharacteristic2.setCharacteristicValue( "BADVALUE" );
    userCharacteristic2.setUserCharacteristicType( characteristic );
    userCharacteristic2.setUser( user2 );

    user2.getUserCharacteristics().add( userCharacteristic2 );

    User actualUser2 = getUserDAO().saveUser( user2 );

    // Create new user
    User user3 = new User();
    user3.setUserType( UserType.lookup( UserType.BI ) );
    user3.setFirstName( "TestFIRSTNAME" );
    user3.setLastName( "TestLASTNAME" );
    user3.setPassword( "testPASSWORD" );
    user3.setUserName( "testUSERNAME3" );
    user3.setActive( Boolean.TRUE );
    user3.setWelcomeEmailSent( Boolean.FALSE );
    user3.setLastResetDate( new Date() );

    User actualUser3 = getUserDAO().saveUser( user3 );

    // Create new Budget Master
    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();

    BudgetMaster expectedBudgetMaster = new BudgetMaster();
    expectedBudgetMaster.setBudgetName( "Test Budget Master1" );
    expectedBudgetMaster.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    expectedBudgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster.setNameCmKey( "New Asset Keyx" );
    expectedBudgetMaster.setCmAssetCode( "New Asset Namex" );
    expectedBudgetMaster.setMultiPromotion( false );
    expectedBudgetMaster.setStartDate( new Date() );

    BudgetStatusType activeStatus = BudgetStatusType.lookup( BudgetStatusType.ACTIVE );
    BudgetStatusType closedStatus = BudgetStatusType.lookup( BudgetStatusType.CLOSED );
    BudgetActionType actionType = BudgetActionType.lookup( BudgetActionType.DEPOSIT );
    // Create a (user) Budget Record
    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setUser( actualUser1 );
    budget.setStatus( activeStatus );
    budget.setActionType( actionType );

    // Create a (user) Budget Record
    Budget budget2 = new Budget();
    budget2.setOriginalValue( BigDecimal.valueOf( 900 ) );
    budget2.setCurrentValue( BigDecimal.valueOf( 800 ) );
    budget2.setUser( actualUser2 );
    budget2.setStatus( activeStatus );
    budget2.setActionType( actionType );

    // Create a (user) Budget Record
    Budget budget3 = new Budget();
    budget3.setOriginalValue( BigDecimal.valueOf( 900 ) );
    budget3.setCurrentValue( BigDecimal.valueOf( 800 ) );
    budget3.setStatus( BudgetStatusType.lookup( BudgetStatusType.SUSPENDED ) );
    budget3.setUser( actualUser3 );
    budget3.setStatus( closedStatus );
    budget3.setActionType( actionType );

    BudgetSegment testBudgetSegment = new BudgetSegment();
    testBudgetSegment.setName( "Segment1" );
    testBudgetSegment.setStatus( true );
    testBudgetSegment.setStartDate( new Date() );
    testBudgetSegment.setEndDate( new Date() );
    testBudgetSegment.setCmAssetCode( "unittest.cm.asset.code" );

    testBudgetSegment.addBudget( budget );
    testBudgetSegment.addBudget( budget2 );
    testBudgetSegment.addBudget( budget3 );

    expectedBudgetMaster.addBudgetSegment( testBudgetSegment );
    BudgetMaster actualBudgetMaster = budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster );

    flushAndClearSession();
    BudgetSegment actualBudgetSegment = budgetMasterDAO.saveBudgetSegment( testBudgetSegment );
    HibernateSessionManager.getSession().flush();
    createdUserBudgetMasterId = actualBudgetMaster.getId();
    createdUserBudgetSegmentId = actualBudgetSegment.getId();
  }

  private Long createTestNodeBudgets()
  {

    String uniqueNodeName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeDAO nodeDAO = getNodeDAO();

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "Test NodeHierarchy1" + uniqueNodeName );
    hierarchy.setDescription( "description goes here" );
    hierarchy.setPrimary( false );
    hierarchy.setActive( true );
    hierarchy.setCmAssetCode( "CM name ASSET" );
    hierarchy.setNameCmKey( "CM name KEY" );

    NodeType nodeType = new NodeType();
    nodeType.setName( "testNodeType" + uniqueNodeName );
    nodeType.setCmAssetCode( "test.asset" );
    nodeType.setNameCmKey( "testkey" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueNodeName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType );
    parentNode.setHierarchy( hierarchy );

    nodeDAO.saveNode( parentNode );
    // Create a Characteristic

    NodeTypeCharacteristicType nodeTypeCharacteristicType = new NodeTypeCharacteristicType();
    nodeTypeCharacteristicType.setDescription( "description" );
    nodeTypeCharacteristicType.setCharacteristicName( "XChar2" );
    nodeTypeCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    nodeTypeCharacteristicType.setMaxSize( new Long( 5 ) );
    nodeTypeCharacteristicType.setPlName( "new picklist" );
    String uniqueName2 = String.valueOf( Math.random() % 29930293 );
    nodeTypeCharacteristicType.setNameCmKey( "asset_key" );
    nodeTypeCharacteristicType.setCmAssetCode( "test.asset.name" + uniqueName2 );
    nodeTypeCharacteristicType.setIsRequired( Boolean.valueOf( true ) );
    nodeTypeCharacteristicType.setActive( true );
    nodeTypeCharacteristicType.setDomainId( new Long( 1 ) );
    nodeTypeCharacteristicType.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );

    CharacteristicDAO characteristicDAO = getNodeCharacteristicDAO();
    Characteristic actualNodeCharacteristic = characteristicDAO.saveCharacteristic( nodeTypeCharacteristicType );
    createdNodeCharacteristicId = actualNodeCharacteristic.getId();
    Node node = new Node();
    node.setName( "testNAME" + uniqueNodeName );
    node.setDescription( "testDESCRIPTION" );
    node.setParentNode( parentNode );
    node.setPath( "testPATH" );
    node.setNodeType( nodeType );
    node.setHierarchy( hierarchy );
    // Create Node Characteristic
    NodeCharacteristic nodeCharacteristic = new NodeCharacteristic();
    nodeCharacteristic.setCharacteristicValue( "VALUE1" );
    nodeCharacteristic.setNodeTypeCharacteristicType( nodeTypeCharacteristicType );
    nodeCharacteristic.setNode( node );
    node.getNodeCharacteristics().add( nodeCharacteristic );

    Node actualNode1 = nodeDAO.saveNode( node );

    Node node2 = new Node();
    node2.setName( "testNAME2" + uniqueNodeName );
    node2.setDescription( "testDESCRIPTION" );
    node2.setParentNode( parentNode );
    node2.setPath( "testPATH" );
    node2.setNodeType( nodeType );
    node2.setHierarchy( hierarchy );
    // Create Node Characteristic
    NodeCharacteristic nodeCharacteristic2 = new NodeCharacteristic();
    nodeCharacteristic2.setCharacteristicValue( "BADVALUE" );
    nodeCharacteristic2.setNodeTypeCharacteristicType( nodeTypeCharacteristicType );
    nodeCharacteristic2.setNode( node2 );
    node2.getNodeCharacteristics().add( nodeCharacteristic2 );

    Node actualNode2 = nodeDAO.saveNode( node2 );

    // Create new Budget Master
    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();

    BudgetMaster expectedBudgetMaster = new BudgetMaster();
    expectedBudgetMaster.setBudgetName( "Test Budget Master2" );
    expectedBudgetMaster.setBudgetType( BudgetType.lookup( BudgetType.NODE_BUDGET_TYPE ) );
    expectedBudgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster.setNameCmKey( "New Asset Keyx2" );
    expectedBudgetMaster.setCmAssetCode( "New Asset Namex2" );
    expectedBudgetMaster.setMultiPromotion( false );
    expectedBudgetMaster.setStartDate( new Date() );

    BudgetStatusType activeStatus = BudgetStatusType.lookup( BudgetStatusType.ACTIVE );
    // Create a (user) Budget Record
    Budget budget = new Budget();
    budget.setOriginalValue( BigDecimal.valueOf( 500 ) );
    budget.setCurrentValue( BigDecimal.valueOf( 200 ) );
    budget.setNode( actualNode1 );
    budget.setStatus( activeStatus );
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

    // Create a (user) Budget Record
    Budget budget2 = new Budget();
    budget2.setOriginalValue( BigDecimal.valueOf( 900 ) );
    budget2.setCurrentValue( BigDecimal.valueOf( 800 ) );
    budget2.setNode( actualNode2 );
    budget2.setStatus( activeStatus );
    budget2.setActionType( BudgetActionType.lookup( BudgetActionType.DEPOSIT ) );

    BudgetSegment testBudgetSegment = new BudgetSegment();
    testBudgetSegment.setName( "Segment1" );
    testBudgetSegment.setStatus( true );
    testBudgetSegment.setStartDate( new Date() );
    testBudgetSegment.setEndDate( new Date() );
    testBudgetSegment.setCmAssetCode( "unittest.cm.asset.code" );

    testBudgetSegment.addBudget( budget );
    testBudgetSegment.addBudget( budget2 );

    // Save the Budget Master
    expectedBudgetMaster.addBudgetSegment( testBudgetSegment );
    BudgetMaster actualBudgetMaster = budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster );
    flushAndClearSession();
    
    BudgetSegment actualBudgetSegment = budgetMasterDAO.saveBudgetSegment( testBudgetSegment );
    flushAndClearSession();
    
    createdNodeBudgetSegmentId = actualBudgetSegment.getId();
    createdNodeBudgetMasterId = actualBudgetMaster.getId();
    return actualNode1.getNodeType().getId();
  }

}

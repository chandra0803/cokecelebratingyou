/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/budget/hibernate/BudgetMasterDAOImplTest.java,v $
 */

package com.biperf.core.dao.budget.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.DateUtils;

/**
 * BudgetMasterDAOImplTest.
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
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetMasterDAOImplTest extends BaseDAOTest
{

  /**
   * Tests saving or updating the BudgetMaster. This needs to fetch the budgetMaster by Id so it is
   * also testing BudgetMasterDAO.getBudgetMasterById(Long id).
   */
  public void testSaveGetDeleteBudgetMasterById()
  {

    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();

    BudgetMaster expectedBudgetMaster = new BudgetMaster();
    expectedBudgetMaster.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    expectedBudgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster.setMultiPromotion( false );
    expectedBudgetMaster.setStartDate( new Date() );

    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster );

    flushAndClearSession();

    BudgetMaster actualBudgetMaster = budgetMasterDAO.getBudgetMasterById( expectedBudgetMaster.getId() );

    assertEquals( "BudgetMaster not equals", expectedBudgetMaster, actualBudgetMaster );

    // Update the BudgetMaster
    expectedBudgetMaster.setBudgetName( "testUpdatedName" );

    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster );

    flushAndClearSession();

    // Updated BudgetMaster from the database
    actualBudgetMaster = budgetMasterDAO.getBudgetMasterById( expectedBudgetMaster.getId() );

    assertEquals( "Updated BudgetMaster not equals", expectedBudgetMaster, actualBudgetMaster );

    budgetMasterDAO.deleteBudgetMaster( actualBudgetMaster );

    flushAndClearSession();

    BudgetMaster checkBudgetMaster = budgetMasterDAO.getBudgetMasterById( expectedBudgetMaster.getId() );

    if ( checkBudgetMaster != null )
    {
      assertEquals( "BudgetMaster Item still there after delete", expectedBudgetMaster, checkBudgetMaster );
    }
  }

  /**
   * Tests saving and getting all the budgetMaster records saved.
   */
  public void testGetAll()
  {

    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();

    int count = 0;

    count = budgetMasterDAO.getAll().size();

    List expectedBudgetMasters = new ArrayList();

    BudgetMaster expectedBudgetMaster1 = new BudgetMaster();
    expectedBudgetMaster1.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster1.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    expectedBudgetMaster1.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster1.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster1.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster1.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster1.setMultiPromotion( false );
    expectedBudgetMaster1.setStartDate( new Date() );
    expectedBudgetMasters.add( expectedBudgetMaster1 );
    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster1 );

    BudgetMaster expectedBudgetMaster2 = new BudgetMaster();
    expectedBudgetMaster2.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster2.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    expectedBudgetMaster2.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster2.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster2.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster2.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster2.setMultiPromotion( false );
    expectedBudgetMaster2.setStartDate( new Date() );
    expectedBudgetMasters.add( expectedBudgetMaster2 );
    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster2 );

    flushAndClearSession();

    List actualBudgetMasters = budgetMasterDAO.getAll();

    assertEquals( "List of budgetMasters aren't the same size.", expectedBudgetMasters.size() + count, actualBudgetMasters.size() );

  }

  /**
   * Tests saving and getting all active budgetMaster records saved.
   */
  public void testGetAllActive()
  {

    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();

    int count = 0;

    count = budgetMasterDAO.getAllActive().size();

    List expectedBudgetMasters = new ArrayList();

    BudgetMaster expectedBudgetMaster1 = new BudgetMaster();
    expectedBudgetMaster1.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster1.setBudgetType( BudgetType.lookup( BudgetType.NODE_BUDGET_TYPE ) );
    expectedBudgetMaster1.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster1.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster1.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster1.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster1.setMultiPromotion( false );
    expectedBudgetMaster1.setActive( true );
    expectedBudgetMaster1.setStartDate( new Date() );
    expectedBudgetMasters.add( expectedBudgetMaster1 );
    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster1 );

    BudgetMaster expectedBudgetMaster2 = new BudgetMaster();
    expectedBudgetMaster2.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster2.setBudgetType( BudgetType.lookup( BudgetType.NODE_BUDGET_TYPE ) );
    expectedBudgetMaster2.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster2.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster2.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster2.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster2.setMultiPromotion( false );
    expectedBudgetMaster2.setActive( true );
    expectedBudgetMaster2.setStartDate( new Date() );
    expectedBudgetMasters.add( expectedBudgetMaster2 );
    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster2 );

    flushAndClearSession();

    List actualBudgetMasters = budgetMasterDAO.getAllActive();

    assertEquals( "List of budgetMasters aren't the same size.", expectedBudgetMasters.size() + count, actualBudgetMasters.size() );

  }

  /**
   * Tests saving and getting all inactive budgetMaster records saved.
   */
  public void testGetAllInactive()
  {

    BudgetMasterDAO budgetMasterDAO = getBudgetMasterDAO();

    int count = 0;

    count = budgetMasterDAO.getAllInactive().size();

    List expectedBudgetMasters = new ArrayList();

    BudgetMaster expectedBudgetMaster1 = new BudgetMaster();
    expectedBudgetMaster1.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster1.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    expectedBudgetMaster1.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster1.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster1.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster1.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster1.setMultiPromotion( false );
    expectedBudgetMaster1.setActive( false );
    expectedBudgetMaster1.setStartDate( new Date() );
    expectedBudgetMasters.add( expectedBudgetMaster1 );
    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster1 );

    BudgetMaster expectedBudgetMaster2 = new BudgetMaster();
    expectedBudgetMaster2.setBudgetName( "Test Budget Master" );
    expectedBudgetMaster2.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    expectedBudgetMaster2.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    expectedBudgetMaster2.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    expectedBudgetMaster2.setNameCmKey( "New Asset Key" );
    expectedBudgetMaster2.setCmAssetCode( "New Asset Name" );
    expectedBudgetMaster2.setMultiPromotion( false );
    expectedBudgetMaster2.setActive( false );
    expectedBudgetMaster2.setStartDate( new Date() );
    expectedBudgetMasters.add( expectedBudgetMaster2 );
    budgetMasterDAO.saveBudgetMaster( expectedBudgetMaster2 );

    flushAndClearSession();

    List actualBudgetMasters = budgetMasterDAO.getAllInactive();

    assertEquals( "List of budgetMasters aren't the same size.", expectedBudgetMasters.size() + count, actualBudgetMasters.size() );

  }

  public static BudgetSegment buildBudgetSegment()
  {
    BudgetSegment budgetSegment = new BudgetSegment();
    budgetSegment.setName( "Default Segment" );
    Date currentDate = new Date();
    budgetSegment.setStartDate( currentDate );
    budgetSegment.setEndDate( DateUtils.getNextDay( currentDate ) );
    budgetSegment.setStatus( Boolean.TRUE );
    budgetSegment.setCmAssetCode( "unittest.cm.asset.code" );

    return budgetSegment;
  }

  public static BudgetMaster buildAndSaveBudgetMaster( String budgetName, BudgetSegment budgetSegment, Participant softCapApprover )
  {
    BudgetMaster budgetMaster = buildBudgetMaster( budgetName, budgetSegment );

    BudgetMaster savedBudgetMaster = getBudgetMasterDAO().saveBudgetMaster( budgetMaster );

    return savedBudgetMaster;
  }

  public static BudgetMaster buildBudgetMaster( String budgetName, BudgetSegment budgetSegment )
  {
    BudgetMaster budgetMaster = new BudgetMaster();
    budgetMaster.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    budgetMaster.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    budgetMaster.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    budgetMaster.setFinalPayoutRule( BudgetFinalPayoutRule.lookup( BudgetFinalPayoutRule.FULL_PAYOUT ) );
    budgetMaster.setNameCmKey( "New Asset Key" );
    budgetMaster.setCmAssetCode( "New Asset Name" );
    budgetMaster.setMultiPromotion( false );
    budgetMaster.setStartDate( new Date() );
    budgetMaster.addBudgetSegment( budgetSegment );
    return budgetMaster;
  }

  /**
   * Get the BudgetMasterDAO.
   * 
   * @return BudgetMasterDAO
   */
  private static BudgetMasterDAO getBudgetMasterDAO()
  {
    return (BudgetMasterDAO)ApplicationContextFactory.getApplicationContext().getBean( "budgetMasterDAO" );
  }
}

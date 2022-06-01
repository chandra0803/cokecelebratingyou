/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/budget/hibernate/BudgetMasterDAOImplBudgetSegmentTest.java,v $
 */

package com.biperf.core.dao.budget.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.BudgetOverrideableType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.DateUtils;
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
public class BudgetMasterDAOImplBudgetSegmentTest extends BaseDAOTest
{
  private BudgetMasterDAO budgetMasterDAO;
  private BudgetMaster testBudgetMaster, basicBudgetMaster;
  private Long budgetMasterId;
  private Calendar calendar;

  /**
   * Returns BudgetMasterDAO from the beanFactory.
   * 
   * @return BudgetMasterDAO
   */
  protected BudgetMasterDAO getBudgetMasterDAO()
  {
    return (BudgetMasterDAO)ApplicationContextFactory.getApplicationContext().getBean( "budgetMasterDAO" );
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    budgetMasterDAO = getBudgetMasterDAO();
    testBudgetMaster = createTestBudgetMaster();
    testBudgetMaster = budgetMasterDAO.saveBudgetMaster( testBudgetMaster );
    HibernateSessionManager.getSession().flush();
    budgetMasterId = testBudgetMaster.getId();
    calendar = Calendar.getInstance();
  }

  private BudgetMaster createTestBudgetMaster()
  {
    BudgetMaster ret = new BudgetMaster();
    ret.setBudgetName( "Test Budget Master" );
    ret.setBudgetType( BudgetType.lookup( BudgetType.PAX_BUDGET_TYPE ) );
    ret.setAwardType( BudgetMasterAwardType.lookup( BudgetMasterAwardType.POINTS ) );
    ret.setOverrideableType( BudgetOverrideableType.lookup( BudgetOverrideableType.HARD_OVERRIDE ) );
    ret.setNameCmKey( "New Asset Key" );
    ret.setCmAssetCode( "New Asset Name" );
    ret.setMultiPromotion( false );
    ret.setStartDate( new Date() );

    return ret;
  }

  /**
   * Test adding a BudgetSegment to a Bug
   */
  public void testAddBudgetSegment()
  {
    // Initialize a BudgetSegment to add
    BudgetSegment segment = new BudgetSegment();
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setStartDate( new Date() );
    segment.setEndDate( new Date() );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    // Add BudgetSegment to the BudgetMaster
    testBudgetMaster.addBudgetSegment( segment );

    // Save the masterBudget with the added budget segment
    BudgetMaster finalBudgetMaster = budgetMasterDAO.saveBudgetMaster( testBudgetMaster );

    HibernateSessionManager.getSession().flush();

    assertTrue( finalBudgetMaster.getBudgetSegments().size() == 1 );
    assertTrue( finalBudgetMaster.getBudgetSegments().contains( segment ) );
  }

  public void testSaveBudgetSegment()
  {
    BudgetSegment segment = new BudgetSegment();
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setStartDate( new Date() );
    segment.setEndDate( new Date() );
    segment.setBudgetMaster( testBudgetMaster );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    budgetMasterDAO.saveBudgetSegment( segment );
    HibernateSessionManager.getSession().flush();
    BudgetSegment compareSegment = budgetMasterDAO.getBudgetSegmentById( segment.getId() );
    assertTrue( segment.equals( compareSegment ) );
  }

  public void testUpdateBudgetSegment()
  {
    BudgetSegment segment = new BudgetSegment();
    calendar.set( 2014, Calendar.APRIL, 1 );
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setStartDate( calendar.getTime() );
    segment.setEndDate( calendar.getTime() );
    segment.setBudgetMaster( testBudgetMaster );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    budgetMasterDAO.saveBudgetSegment( segment );
    flushAndClearSession();

    Set<BudgetSegment> segments = budgetMasterDAO.getBudgetMasterById( testBudgetMaster.getId() ).getBudgetSegments();

    assertEquals( segments.size(), 1 );

    BudgetSegment updatedSegment = budgetMasterDAO.getBudgetSegmentById( segment.getId() );
    calendar.set( 2014, Calendar.APRIL, 2 );
    updatedSegment.setEndDate( calendar.getTime() );
    updatedSegment.setCmAssetCode( "unittest.cm.asset.code2" );

    budgetMasterDAO.saveBudgetSegment( updatedSegment );
    flushAndClearSession();
    segments = budgetMasterDAO.getBudgetMasterById( testBudgetMaster.getId() ).getBudgetSegments();

    assertEquals( segments.size(), 1 );
    BudgetSegment finalseg = budgetMasterDAO.getBudgetSegmentById( segment.getId() );

    Date compare1, compare2;
    compare1 = truncDateMs( finalseg.getEndDate() );
    compare2 = truncDateMs( updatedSegment.getEndDate() );

    assertEquals( compare1, compare2 );
  }

  public void testGetBudgetSegmentById()
  {
    // Ids are set automatically, so we don't set one ourselves
    BudgetSegment segment = new BudgetSegment();
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    calendar.set( 2014, Calendar.APRIL, 1 );
    segment.setStartDate( calendar.getTime() );
    segment.setEndDate( calendar.getTime() );

    testBudgetMaster.addBudgetSegment( segment );

    BudgetSegment finalSegment = budgetMasterDAO.saveBudgetSegment( segment );
    HibernateSessionManager.getSession().flush();

    // Get the id the database assigned our segment
    Long id = finalSegment.getId();

    // Attempt to get our segment by id
    BudgetSegment compareSeg = budgetMasterDAO.getBudgetSegmentById( id );

    assertTrue( segment.equals( compareSeg ) );
  }

  public void testGetBudgetSegmentsByBudgetMasterId()
  {
    Long budgetMasterId = testBudgetMaster.getId();

    BudgetSegment segment = new BudgetSegment();
    calendar.set( 2014, Calendar.APRIL, 1 );
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setStartDate( calendar.getTime() );
    segment.setEndDate( calendar.getTime() );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    BudgetSegment segment2 = new BudgetSegment();
    calendar.set( 2014, Calendar.APRIL, 2 );
    segment2.setName( "Segment2" );
    segment2.setStatus( true );
    segment2.setStartDate( calendar.getTime() );
    segment2.setEndDate( calendar.getTime() );
    segment2.setCmAssetCode( "unittest.cm.asset.code" );

    testBudgetMaster.addBudgetSegment( segment );
    testBudgetMaster.addBudgetSegment( segment2 );

    budgetMasterDAO.saveBudgetMaster( testBudgetMaster );
    HibernateSessionManager.getSession().flush();

    List<BudgetSegment> gotSegs = budgetMasterDAO.getBudgetSegmentsByBudgetMasterId( budgetMasterId );
    assertEquals( gotSegs.size(), 2 );
    assertTrue( gotSegs.contains( segment ) );
    assertTrue( gotSegs.contains( segment2 ) );
  }

  public void testGetBudgetSegmentsToTransferByBudgetMasterId()
  {
    BudgetSegment segment = new BudgetSegment();
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setStartDate( DateUtils.getLastDayOfCurrentMonth() );
    segment.setEndDate( DateUtils.getLastDayOfCurrentMonth() );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    calendar.set( 2014, Calendar.APRIL, 2 );
    BudgetSegment segment2 = new BudgetSegment();
    segment2.setName( "Segment2" );
    segment2.setStatus( true );
    segment2.setStartDate( calendar.getTime() );
    segment2.setEndDate( calendar.getTime() );
    segment2.setCmAssetCode( "unittest.cm.asset.code" );

    // Need to allow realloacation for the query used in getBudgetSegmentsToTransferByBudgetId
    // We'll only allow one to be sure it properly ignores the other
    segment.setAllowBudgetReallocation( true );

    testBudgetMaster.addBudgetSegment( segment );
    testBudgetMaster.addBudgetSegment( segment2 );

    budgetMasterDAO.saveBudgetMaster( testBudgetMaster );
    HibernateSessionManager.getSession().flush();

    List<BudgetSegment> gotSegs = budgetMasterDAO.getBudgetSegmentsToTransferByBudgetMasterId( budgetMasterId );
    assertEquals( gotSegs.size(), 1 );
    assertTrue( gotSegs.contains( segment ) );
  }

  /**
   * Creates three budgetSegment instances, two with the same name and one with a unique name,
   * and stores them. It then asserts that one is unique, but the others are not.
   */
  public void testIsBudgetSegmentNameUnique()
  {
    BudgetSegment segment = new BudgetSegment();
    calendar.set( 2014, Calendar.APRIL, 1 );
    segment.setName( "Segment1" );
    segment.setStatus( true );
    segment.setStartDate( calendar.getTime() );
    segment.setEndDate( calendar.getTime() );
    segment.setCmAssetCode( "unittest.cm.asset.code" );

    BudgetSegment segment2 = new BudgetSegment();
    calendar.set( 2014, Calendar.APRIL, 2 );
    segment2.setName( "Segment2" );
    segment2.setStatus( true );
    segment2.setStartDate( calendar.getTime() );
    segment2.setEndDate( calendar.getTime() );
    segment2.setCmAssetCode( "unittest.cm.asset.code" );

    BudgetSegment segment3 = new BudgetSegment();
    calendar.set( 2014, Calendar.APRIL, 3 );
    segment3.setName( "Segment1" );
    segment3.setStatus( true );
    segment3.setStartDate( calendar.getTime() );
    segment3.setEndDate( calendar.getTime() );
    segment3.setCmAssetCode( "unittest.cm.asset.code" );

    testBudgetMaster.addBudgetSegment( segment );
    testBudgetMaster.addBudgetSegment( segment2 );
    testBudgetMaster.addBudgetSegment( segment3 );

    budgetMasterDAO.saveBudgetSegment( segment );
    budgetMasterDAO.saveBudgetSegment( segment2 );
    budgetMasterDAO.saveBudgetSegment( segment3 );
    HibernateSessionManager.getSession().flush();

    assertFalse( budgetMasterDAO.isBudgetSegmentNameUnique( budgetMasterId, segment.getName(), segment.getId() ) );
    assertFalse( budgetMasterDAO.isBudgetSegmentNameUnique( budgetMasterId, segment3.getName(), segment3.getId() ) );
    assertTrue( budgetMasterDAO.isBudgetSegmentNameUnique( budgetMasterId, segment2.getName(), segment2.getId() ) );
  }

  /**
   * A method for truncating date objects. Specifically, it sets the milliseconds
   * to zero to allow comparison with dates stored in the databse.
   * 
   * @param date The date to be truncated
   * @return A date with the same value, but with the milliseconds truncated off.
   */
  private Date truncDateMs( Date date )
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime( date );
    cal.set( Calendar.MILLISECOND, 0 );
    return cal.getTime();
  }
}

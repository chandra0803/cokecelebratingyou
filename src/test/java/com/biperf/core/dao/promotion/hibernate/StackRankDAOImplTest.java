/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.promotion.hibernate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.StackRankDAO;
import com.biperf.core.domain.enums.StackRankState;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;

/*
 * StackRankDAOImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 7, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final PromotionDAO promotionDao = getPromotionDao();
  private static final StackRankDAO stackRankDao = getStackRankDao();

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests deleting a stack rank.
   */
  public void testDeleteStackRank()
  {
    // Insert the stack rank into the database.
    StackRank stackRank1 = buildStackRank( getUniqueString() );
    stackRank1 = stackRankDao.saveStackRank( stackRank1 );
    assertNotNull( stackRank1.getId() );
    flushAndClearSession();

    // Verify that the stack rank was inserted into the database.
    StackRank stackRank2 = stackRankDao.getStackRank( stackRank1.getId() );
    compareStackRank( stackRank1, stackRank2 );

    // Delete the stack rank from the database.
    Long stackRankId = stackRank2.getId();
    stackRankDao.deleteStackRank( stackRank2 );
    flushAndClearSession();

    // Verify that the stack rank was deleted from the database.
    StackRank stackRank3 = stackRankDao.getStackRank( stackRankId );
    assertNull( stackRank3 );
  }

  /**
   * Tests getting a list of stack ranks based on promotion and state.
   */
  public void testGetStackRankList()
  {
    StackRank stackRank = buildStackRank( getUniqueString() );
    StackRank stackRank2 = buildStackRank( getUniqueString() );

    // Insert the stack rank.
    StackRank savedStackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( savedStackRank.getId() );

    StackRank savedStackRank2 = stackRankDao.saveStackRank( stackRank2 );
    assertNotNull( savedStackRank2.getId() );

    flushAndClearSession();

    // Select the stack rank.
    StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
    queryConstraint.setPromotionIdsIncluded( new Long[] { savedStackRank.getPromotion().getId() } );

    List stackRankList = stackRankDao.getStackRankList( queryConstraint, null );

    assertTrue( stackRankList.size() > 0 );
    flushAndClearSession();

    // Test the userId and nodeId Query constraint. Nothing has to be returned, just
    // making sure it works.
    StackRankQueryConstraint userAndNodeQueryConstraint = new StackRankQueryConstraint();
    userAndNodeQueryConstraint.setUserIdsIncluded( new Long[] { new Long( 1 ) } );
    userAndNodeQueryConstraint.setNodeIdsIncluded( new Long[] { new Long( 1 ) } );

    stackRankDao.getStackRankList( userAndNodeQueryConstraint, null );

  }

  /**
   * Tests getting a list of stack ranks based on promotion and state.
   */
  public void testGetStackRankListWithQueryConstraint()
  {
    StackRank stackRank = buildStackRank( getUniqueString() );
    StackRank stackRank2 = buildStackRank( getUniqueString() );

    // Insert the stack rank.
    stackRankDao.saveStackRank( stackRank );
    stackRankDao.saveStackRank( stackRank2 );

    flushAndClearSession();

    // Test the userId and nodeId Query constraint. Nothing has to be returned, just
    // making sure it works.
    StackRankQueryConstraint userAndNodeQueryConstraint = new StackRankQueryConstraint();
    userAndNodeQueryConstraint.setUserIdForNodeAndBelow( new Long( 100 ) );

    List stackRankList = stackRankDao.getStackRankList( userAndNodeQueryConstraint, null );

    assertNotNull( stackRankList );

  }

  /**
   * Tests inserting, selecting, and updating a StackRank object.
   */
  public void testSaveUpdateAndGetById()
  {
    StackRank stackRank = buildStackRank( getUniqueString() );

    // Insert the stack rank.
    StackRank savedStackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( savedStackRank.getId() );
    flushAndClearSession();

    // Select the stack rank.
    StackRank retrievedStackRank = stackRankDao.getStackRank( savedStackRank.getId() );
    compareStackRank( savedStackRank, retrievedStackRank );

    // Update the stack rank.
    retrievedStackRank.setState( StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) );
    retrievedStackRank.setStartDate( new Date() );
    retrievedStackRank.setEndDate( new Date() );
    retrievedStackRank.setCalculatePayout( true );
    savedStackRank = stackRankDao.saveStackRank( retrievedStackRank );
    flushAndClearSession();

    // Select the updated stack rank.
    retrievedStackRank = stackRankDao.getStackRank( savedStackRank.getId() );
    compareStackRank( savedStackRank, retrievedStackRank );
  }

  /**
   * Test getLatestStackRankByPromotionId.
   */
  public void testGetLatestStackRankByPromotionId()
  {
    ArrayList stackRanks = new ArrayList();
    stackRanks = buildMultipleStackRanks( getUniqueString() );

    // Insert the first stack rank.
    StackRank savedStackRank1 = stackRankDao.saveStackRank( (StackRank)stackRanks.get( 0 ) );
    assertNotNull( savedStackRank1.getId() );
    flushAndClearSession();

    // Insert the second stack rank.
    StackRank savedStackRank2 = stackRankDao.saveStackRank( (StackRank)stackRanks.get( 1 ) );
    assertNotNull( savedStackRank2.getId() );
    flushAndClearSession();

    StackRank latestStackRank = stackRankDao.getLatestStackRankByPromotionId( savedStackRank2.getPromotion().getId(), StackRankState.STACK_RANK_LISTS_APPROVED, null );
    Timestamp savedStackRank2CreatedDate = savedStackRank2.getAuditCreateInfo().getDateCreated();
    Timestamp latestCreatedDate = latestStackRank.getAuditCreateInfo().getDateCreated();

    String latestDate = DateUtils.toDisplayTimeString( new Date( latestCreatedDate.getTime() ) );
    String saved2Date = DateUtils.toDisplayTimeString( new Date( savedStackRank2CreatedDate.getTime() ) );

    // Stack Rank2 is the latest Stack Rank for a specified promotion
    assertEquals( "Stack Rank Date Created should equal.", latestDate, saved2Date );

  }

  /**
   * Tests getting a list of stack ranks based on promotion and state.
   */
  public void testGetStackRankListCount()
  {
    StackRank stackRank = buildStackRank( getUniqueString() );

    // Insert the stack rank.
    StackRank savedStackRank = stackRankDao.saveStackRank( stackRank );
    assertNotNull( savedStackRank.getId() );

    flushAndClearSession();

    StackRankQueryConstraint queryConstraint = new StackRankQueryConstraint();
    queryConstraint.setPromotionIdsIncluded( new Long[] { savedStackRank.getPromotion().getId() } );
    queryConstraint.setStackRankStatesIncluded( new StackRankState[] { StackRankState.lookup( StackRankState.BEFORE_CREATE_STACK_RANK_LISTS ) } );

    int count = stackRankDao.getStackRankListCount( queryConstraint );

    assertTrue( count > 0 );
  }

  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a new {@link StackRank} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link StackRank} object.
   */
  public static StackRank buildStackRank( String uniqueString )
  {
    StackRank stackRank = new StackRank();

    stackRank.setGuid( GuidUtils.generateGuid() );
    stackRank.setState( StackRankState.lookup( StackRankState.BEFORE_CREATE_STACK_RANK_LISTS ) );
    stackRank.setStartDate( new Date() );
    stackRank.setEndDate( new Date() );
    stackRank.setCalculatePayout( false );

    // Add stack rank nodes to the stack rank.
    stackRank.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "1" ) );
    stackRank.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "2" ) );

    // Attach the stack rank to a promotion.
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString );
    promotion = (ProductClaimPromotion)promotionDao.save( promotion );
    stackRank.setPromotion( promotion );

    return stackRank;
  }

  /**
   * Returns a new {@link StackRank} object.
   * 
   * @param uniqueString a unique string.
   * @return a new {@link StackRank} object.
   */
  public static ArrayList buildMultipleStackRanks( String uniqueString )
  {
    ArrayList aListOfStackRanksWithTheSamePromotion = new ArrayList();

    // ***************** Build stackRank1
    StackRank stackRank = new StackRank();

    stackRank.setGuid( GuidUtils.generateGuid() );
    stackRank.setState( StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) );
    stackRank.setStartDate( new Date() );
    stackRank.setEndDate( new Date() );
    stackRank.setCalculatePayout( false );

    // Add stack rank nodes to the stack rank.
    stackRank.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "1" ) );
    stackRank.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "2" ) );

    // Attach the stack rank to a promotion.
    ProductClaimPromotion promotion = PromotionDAOImplTest.buildProductClaimPromotion( uniqueString );
    promotion = (ProductClaimPromotion)promotionDao.save( promotion );
    stackRank.setPromotion( promotion );

    aListOfStackRanksWithTheSamePromotion.add( stackRank );

    // ******************* Build stackRank2
    StackRank stackRank2 = new StackRank();

    stackRank2.setGuid( GuidUtils.generateGuid() );
    stackRank2.setState( StackRankState.lookup( StackRankState.STACK_RANK_LISTS_APPROVED ) );
    stackRank2.setStartDate( new Date() );
    stackRank2.setEndDate( new Date() );
    stackRank2.setCalculatePayout( false );

    // Add stack rank nodes to the stack rank.
    stackRank2.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "3" ) );
    stackRank2.addStackRankNode( StackRankNodeDAOImplTest.buildStackRankNode( uniqueString + "4" ) );

    // Attach the stack rank to a promotion.
    promotion = (ProductClaimPromotion)promotionDao.save( promotion );
    stackRank2.setPromotion( promotion );

    aListOfStackRanksWithTheSamePromotion.add( stackRank2 );

    return aListOfStackRanksWithTheSamePromotion;
  }

  // ---------------------------------------------------------------------------
  // Compare Methods
  // ---------------------------------------------------------------------------

  private void compareStackRank( StackRank stackRank1, StackRank stackRank2 )
  {
    assertEquals( stackRank1.getGuid(), stackRank2.getGuid() );
    assertEquals( stackRank1.getState(), stackRank2.getState() );
    assertEquals( stackRank1.getStartDate(), stackRank2.getStartDate() );
    assertEquals( stackRank1.getEndDate(), stackRank2.getEndDate() );
    assertTrue( stackRank1.isCalculatePayout() == stackRank2.isCalculatePayout() );
  }

  // ---------------------------------------------------------------------------
  // DAO Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the promotion DAO.
   * 
   * @return a reference to the promotion DAO.
   */
  private static PromotionDAO getPromotionDao()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( PromotionDAO.BEAN_NAME );
  }

  /**
   * Returns a reference to the stack rank DAO.
   * 
   * @return a reference to the stack rank DAO.
   */
  private static StackRankDAO getStackRankDao()
  {
    return (StackRankDAO)ApplicationContextFactory.getApplicationContext().getBean( StackRankDAO.BEAN_NAME );
  }
}

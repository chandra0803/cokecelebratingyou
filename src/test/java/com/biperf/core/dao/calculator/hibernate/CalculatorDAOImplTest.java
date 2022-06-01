/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/calculator/hibernate/CalculatorDAOImplTest.java,v $
 */

package com.biperf.core.dao.calculator.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.calculator.CalculatorDAO;
import com.biperf.core.dao.calculator.CalculatorQueryConstraint;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.domain.enums.StatusType;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * CalculatorDAOImplTest.
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
 * <td>May 23, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CalculatorDAOImplTest extends BaseDAOTest
{
  /**
   * Tests getting a list of calculators.
   */
  public void testGetCalculators()
  {
    // create a list of saved calculators
    List expectedCalculatorList = new ArrayList();

    expectedCalculatorList.add( getSavedCalculator( "Calc1" + buildUniqueString() ) );
    expectedCalculatorList.add( getSavedCalculator( "Calc2" + buildUniqueString() ) );
    expectedCalculatorList.add( getSavedCalculator( "Calc3" + buildUniqueString() ) );
    expectedCalculatorList.add( getSavedCalculator( "Calc4" + buildUniqueString() ) );
    expectedCalculatorList.add( getSavedCalculator( "Calc5" + buildUniqueString() ) );

    // get a list of all calculators from the database
    CalculatorQueryConstraint queryConstraint = new CalculatorQueryConstraint();
    List actualCalculatorList = this.getCalculatorDAO().getCalculatorList( queryConstraint );

    // assert the created list is contained within the list of ones fetched
    assertTrue( "List of calculators wasn't in the actual list", actualCalculatorList.containsAll( expectedCalculatorList ) );
  }

  /**
   * Tests saving, gettingById and deleting a calculator.
   */
  public void testSaveGetByIdDeleteCalculator()
  {
    CalculatorDAO calculatorDAO = getCalculatorDAO();

    String uniqueString = getUniqueString();

    Calculator expectedCalc = getSavedCalculator( uniqueString );

    Calculator saveCalc = calculatorDAO.saveCalculator( expectedCalc );

    assertEquals( "Actual Saved Calc wasn't equal to what was expected.", expectedCalc, saveCalc );
    flushAndClearSession();

    Calculator getCalculatorById = calculatorDAO.getCalculatorByIdWithAssociations( saveCalc.getId(), null );

    assertEquals( "Actual getCalcById Calculator wasn't equals to what was expected.", expectedCalc, getCalculatorById );

    calculatorDAO.deleteCalculator( getCalculatorById );
  }

  /**
   * Tests getting a calculatorCriterion by calculatorId.
   */
  public void testGetCalculatorCriterionById()
  {

    Calculator savedCalc = getSavedCalculator( getUniqueString() );

    CalculatorCriterion calcCriterionFromSavedCalc = (CalculatorCriterion)savedCalc.getCalculatorCriterion().get( 0 );

    CalculatorCriterion getCalcCriterionById = getCalculatorDAO().getCalculatorCriterionByIdWithAssociations( calcCriterionFromSavedCalc.getId(), null );

    assertEquals( "Calculator Criterion weren't equal as expected.", calcCriterionFromSavedCalc, getCalcCriterionById );
  }

  /**
   * Test getting a CalculatorCriterionRating by id.
   */
  public void testGetCalculatorCriterionRatingById()
  {
    Calculator savedCalc = getSavedCalculator( getUniqueString() );
    flushAndClearSession();

    CalculatorCriterionRating expectedRating = (CalculatorCriterionRating) ( (CalculatorCriterion)savedCalc.getCalculatorCriterion().get( 0 ) ).getCriterionRatings().get( 0 );

    CalculatorCriterionRating actualRating = getCalculatorDAO().getCalculatorCriterionRatingById( expectedRating.getId() );

    assertEquals( "Actual rating wasn't equal to expected.", expectedRating, actualRating );
  }

  /**
   * Tests getting a calculatorPayout by calculatorId.
   */
  public void testGetCalculatorPayoutById()
  {
    Calculator savedCalc = getSavedCalculator( getUniqueString() );
    flushAndClearSession();

    Iterator calcPayoutIter = savedCalc.getCalculatorPayouts().iterator();

    CalculatorPayout calcPayoutFromSavedCalc = (CalculatorPayout)calcPayoutIter.next();

    CalculatorPayout getCalcPayoutById = getCalculatorDAO().getCalculatorPayoutById( calcPayoutFromSavedCalc.getId() );

    assertEquals( "Calculator Payout weren't equal as expected.", calcPayoutFromSavedCalc, getCalcPayoutById );
  }

  /**
   * Build a calculator with the uniqueString param and saves it for testing.
   * 
   * @param uniqueString
   * @return Calculator
   */
  private Calculator getSavedCalculator( String uniqueString )
  {
    return getCalculatorDAO().saveCalculator( buildCalculator( uniqueString ) );
  }

  /**
   * Builds a static calculator for testing.
   * 
   * @param uniqueString
   * @return calculator
   */
  public static Calculator buildCalculator( String uniqueString )
  {
    Calculator calculator = new Calculator();

    calculator.setName( uniqueString );
    calculator.setDescription( uniqueString + " - DESCRIPTION" );
    calculator.setCalculatorStatusType( CalculatorStatusType.lookup( CalculatorStatusType.COMPLETED ) );
    calculator.setWeightedScore( true );
    calculator.setDisplayWeights( true );
    calculator.setWeightCMAssetName( "weightCMAssetName." + uniqueString );
    calculator.setDisplayScores( true );
    calculator.setScoreCMAssetName( "scoreCMAssetName." + uniqueString );
    calculator.setCalculatorAwardType( CalculatorAwardType.lookup( CalculatorAwardType.FIXED_AWARD ) );

    calculator.addCalculatorCriterion( buildCalculatorCriterion( "1-Criterion-" + uniqueString ) );
    calculator.addCalculatorCriterion( buildCalculatorCriterion( "2-Criterion-" + uniqueString ) );
    calculator.addCalculatorCriterion( buildCalculatorCriterion( "3-Criterion-" + uniqueString ) );

    calculator.addCalculatorPayout( buildCalculatorPayout( 1, 3, 5, 10 ) );
    calculator.addCalculatorPayout( buildCalculatorPayout( 4, 6, 11, 20 ) );
    calculator.addCalculatorPayout( buildCalculatorPayout( 7, 10, 21, 30 ) );

    return calculator;
  }

  /**
   * Builds a static calculatorCriterion for testing.
   * 
   * @param uniqueString
   * @return CalculatorCriterion
   */
  public static CalculatorCriterion buildCalculatorCriterion( String uniqueString )
  {
    CalculatorCriterion calculatorCriterion = new CalculatorCriterion();

    calculatorCriterion.setCmAssetName( uniqueString + "-CMASSETNAME" );
    calculatorCriterion.setSequenceNum( 1 );
    calculatorCriterion.setCriterionStatus( StatusType.lookup( StatusType.ACTIVE_CODE ) );
    calculatorCriterion.setWeightValue( 2 );

    calculatorCriterion.addCriterionRating( buildCalculatorCriterionRating( "RATING1-" + uniqueString ) );
    calculatorCriterion.addCriterionRating( buildCalculatorCriterionRating( "RATING2-" + uniqueString ) );
    calculatorCriterion.addCriterionRating( buildCalculatorCriterionRating( "RATING3-" + uniqueString ) );

    return calculatorCriterion;
  }

  /**
   * Builds a static calculatorCriterionRating for testing.
   * 
   * @param uniqueString
   * @return calculatorCriterionRating
   */
  public static CalculatorCriterionRating buildCalculatorCriterionRating( String uniqueString )
  {
    CalculatorCriterionRating calcCriterionRating = new CalculatorCriterionRating();
    calcCriterionRating.setCmAssetName( uniqueString + "-CMASSETNAMEANSWER" );
    calcCriterionRating.setRatingValue( 1 );

    return calcCriterionRating;
  }

  /**
   * Builds a static calculatorPayout for testing.
   * 
   * @param lowScore
   * @param highScore
   * @param lowAward
   * @param highAward
   * @return calculatorPayout
   */
  public static CalculatorPayout buildCalculatorPayout( int lowScore, int highScore, int lowAward, int highAward )
  {
    CalculatorPayout calcPayout = new CalculatorPayout();
    calcPayout.setLowScore( lowScore );
    calcPayout.setHighScore( highScore );
    calcPayout.setLowAward( lowAward );
    calcPayout.setHighAward( highAward );

    return calcPayout;
  }

  /**
   * Get the CalculatorDAO.
   * 
   * @return CalculatorDAO
   */
  private CalculatorDAO getCalculatorDAO()
  {
    return (CalculatorDAO)ApplicationContextFactory.getApplicationContext().getBean( "calculatorDAO" );
  }

}

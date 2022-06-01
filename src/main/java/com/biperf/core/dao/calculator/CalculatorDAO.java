/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/calculator/CalculatorDAO.java,v $
 */

package com.biperf.core.dao.calculator;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.service.AssociationRequestCollection;

/**
 * CalculatorDAO
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
 */
public interface CalculatorDAO extends DAO
{
  /** ApplicationContext beanName */
  public static final String BEAN_NAME = "calculatorDAO";

  /**
   * Get a Calculator from the database with proper associations.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Calculator
   */
  public Calculator getCalculatorByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Gets a calclator from the database by name.
   * 
   * @param name
   * @return Calculator
   */
  public Calculator getCalculatorByName( String name );

  /**
   * Saves a calculator.
   * 
   * @param calculator
   * @return Calculator
   */
  public Calculator saveCalculator( Calculator calculator );

  /**
   * Delete the Calculator.
   * 
   * @param calculator
   */
  public void deleteCalculator( Calculator calculator );

  /**
   * Get Calculators from the database.
   * 
   * @param calcQueryConstraint
   * @return List
   */
  public List getCalculatorList( CalculatorQueryConstraint calcQueryConstraint );

  /**
   * Get Calculators from the database with proper associations.
   * 
   * @param calcQueryConstraint
   * @param associationRequestCollection
   * @return List
   */
  public List getCalculatorListWithAssociations( CalculatorQueryConstraint calcQueryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Get a CalculatorCriterion from the database with proper associations.
   * 
   * @param id
   * @param associationRequestCollection
   * @return CalculatorCriterion
   */
  public CalculatorCriterion getCalculatorCriterionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Get the CalculatorCriterionRating by the id param.
   * 
   * @param calculatorCriterionRatingId
   * @return CalculatorCriterionRating
   */
  public CalculatorCriterionRating getCalculatorCriterionRatingById( Long calculatorCriterionRatingId );

  /**
   * Get a CalculatorPayout from the database.
   * 
   * @param id
   * @return CalculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutById( Long id );

  /**
   * Get a CalculatorPayout from the database.
   * 
   * @param calculatorId
   * @param score
   * 
   */
  public CalculatorPayout getCalculatorPayoutByScore( Long calculatorId, int score );

}

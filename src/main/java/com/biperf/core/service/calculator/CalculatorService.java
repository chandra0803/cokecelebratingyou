/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/calculator/CalculatorService.java,v $
 */

package com.biperf.core.service.calculator;

import java.util.List;

import com.biperf.core.dao.calculator.CalculatorQueryConstraint;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;

/**
 * CalculatorService
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
 * <td>May 24, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface CalculatorService extends SAO
{
  /**
   * BEAN_NAME is for applicationContext.xml reference
   */
  public static String BEAN_NAME = "calculatorService";

  /**
   * Get the calculator from the database by the id.
   * 
   * @param id
   * @return Calculator
   */
  public Calculator getCalculatorById( Long id );

  /**
   * Retrieves a calculator from the database with proper associations.
   * 
   * @param id
   * @param associationRequestCollection
   * @return Calculator
   */
  public Calculator getCalculatorByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Retrieves a list of calculators from the database.
   * 
   * @param queryConstraint
   * @return List a list of Calculators
   */
  public List getCalculatorList( CalculatorQueryConstraint queryConstraint );

  /**
   * Retrieves a list of calculators from the database with proper associations.
   * 
   * @param queryConstraint
   * @param associationRequestCollection
   * @return List a list of Calculators
   */
  public List getCalculatorListWithAssociations( CalculatorQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Copies a Calculator
   * 
   * @param calculatorIdToCopy
   * @param newCalculatorName
   * @return Calculator (The copied Calculator)
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   */
  public Calculator copyCalculator( Long calculatorIdToCopy, String newCalculatorName ) throws UniqueConstraintViolationException, ServiceErrorException;

  /**
   * Saves the calculator to the database.
   * 
   * @param calculator
   * @return Calculator
   * @throws ServiceErrorException
   */
  public Calculator saveCalculator( Calculator calculator ) throws ServiceErrorException;

  /**
   * Deletes a calculator if it is under_construction or complete. If the calculator is assigned it cannot be
   * deleted. Overridden from
   * 
   * @param calculatorId
   * @throws ServiceErrorException
   */
  public void deleteCalculator( Long calculatorId ) throws ServiceErrorException;

  /**
   * Deletes a list of calculators.
   * 
   * @param calculatorIdList - List of calculator.id
   * @throws ServiceErrorException
   */
  public void deleteCalculators( List calculatorIdList ) throws ServiceErrorException;

  /**
   * Get the calculator criterion by id w/ associations (calculatorCriterionRatings)
   * 
   * @param id
   * @param associationRequestCollection
   * @return CalculatorCriterion
   */
  public CalculatorCriterion getCalculatorCriterionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  /**
   * Save or update calculatorCriterion.
   * 
   * @param calculatorId
   * @param managedCalculatorCriterion
   * @param criterionText
   * @return CalculatorCriterion
   * @throws ServiceErrorException
   */
  public CalculatorCriterion saveCalculatorCriterion( Long calculatorId, CalculatorCriterion managedCalculatorCriterion, String criterionText ) throws ServiceErrorException;

  /**
   * Move the specified calculatorCriterion to the newIndex and shift all other CalculatorCriterion down.
   * 
   * @param calculatorId
   * @param criterionId
   * @param newIndex
   */
  public void reorderCriterion( Long calculatorId, Long criterionId, int newIndex );

  /**
   * Delete a list of calculatorCriterionIds for this form.
   * 
   * @param calculatorId
   * @param criterionIds
   */
  public void deleteCalculatorCriterion( Long calculatorId, List criterionIds ) throws ServiceErrorException;

  /**
   * Get the calculator criterion rating by calculatorCriterionRatingid.
   * 
   * @param id
   * @return CalculatorCriterionRating
   */
  public CalculatorCriterionRating getCriterionRatingById( Long id );

  /**
   * Save or update calculatorCriterionRating.
   * 
   * @param calculatorCriterionId
   * @param managedCriterionRating
   * @param ratingText
   * @return CalculatorCriterionRating
   * @throws ServiceErrorException
   */
  public CalculatorCriterionRating saveCriterionRating( Long calculatorCriterionId, CalculatorCriterionRating managedCriterionRating, String ratingText ) throws ServiceErrorException;

  /**
   * Move the specified rating to the newIndex and shift all other ratings down.
   * 
   * @param criterionId
   * @param ratingId
   * @param newIndex
   */
  public void reorderRating( Long criterionId, Long ratingId, int newIndex );

  /**
   * Delete the calculator criterion associated to the calculatorCriterionIds param.
   * 
   * @param criterionId
   * @param criterionRatingIds
   */
  public void deleteCriterionRating( Long criterionId, List criterionRatingIds );

  /**
   * Get the calculator payout by calculatorPayoutid.
   * 
   * @param id
   * @return CalculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutById( Long id );

  /**
   * Get the calculator payout by calculator score.
   * 
   * @param calculatorId
   * @param score
   * @return CalculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutByScore( Long calculatorId, int score );

  /**
   * Save or update calculatorPayout.
   * 
   * @param calculatorId
   * @param managedPayout
   * @return CalculatorPayout
   * @throws ServiceErrorException
   */
  public CalculatorPayout saveCalculatorPayout( Long calculatorId, CalculatorPayout managedPayout ) throws ServiceErrorException;

  /**
   * Delete the calculatorPayouts associated to the calculatorPayoutIds param.
   * 
   * @param calculatorId
   * @param calculatorPayoutIds
   */
  public void deleteCalculatorPayout( Long calculatorId, List calculatorPayoutIds ) throws ServiceErrorException;

  /**
   * Updates the status of a calculator
   * 
   * @param calculatorId
   */
  public void updateCalculatorStatus( Long calculatorId );

  /**
   * Calculate the weighted score for the given criteria and criteria rating. The
   * promotion ID is used to look up the correct Calculator. Nomination promotions will assume 
   * the calculator is on the final level. The criteriaId is the 
   * criteria in question, and the criteriaRating is the rating as selected by the user.
   * Return the rating score, which takes weighting into account.
   * @param promotionId
   * @param criteriaId
   * @param criteriaRating
   * @return 
   */
  public int getCalculatorRatingScore( Long promotionId, Long criteriaId, int criteriaRating );

  /**
   * See above.  Uses given calculator rather than looking it up via the promotion.
   */
  public int getCalculatorRatingScore( Calculator calculator, Long criteriaId, int criteriaRating );
}

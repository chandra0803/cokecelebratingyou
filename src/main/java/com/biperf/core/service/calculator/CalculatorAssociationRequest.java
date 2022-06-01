/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/calculator/CalculatorAssociationRequest.java,v $
 */

package com.biperf.core.service.calculator;

import java.util.Iterator;

import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * CalculatorAssociationRequest.
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
public class CalculatorAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Hyrate Level: CalculatorCriterion
   */
  public static final int CALCULATOR_CRITERION = 2;

  /**
   * Hyrate Level: CalculatorCriterionRating
   */
  public static final int CALCULATOR_CRITERION_RATING = 3;

  /**
   * Hyrate Level: CalculatorPayout
   */
  public static final int CALCULATOR_PAYOUT = 4;

  /**
   * Hyrate Level: Promotion
   */

  public static final int CALCULATOR_PROMOTIONS = 5;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public CalculatorAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Calculator calculator = (Calculator)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateCalculatorCriterion( calculator );
        hydrateCalculatorCriterionRating( calculator );
        hydrateCalculatorPayout( calculator );
        hydrateCalculatorPromotions( calculator );
        break;

      case CALCULATOR_CRITERION:
        hydrateCalculatorCriterion( calculator );
        break;

      case CALCULATOR_CRITERION_RATING:
        hydrateCalculatorCriterion( calculator );
        hydrateCalculatorCriterionRating( calculator );
        break;

      case CALCULATOR_PAYOUT:
        hydrateCalculatorPayout( calculator );
        break;

      case CALCULATOR_PROMOTIONS:
        hydrateCalculatorPromotions( calculator );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Loads the Calculator Criterion list attached on this calculator
   * 
   * @param calculator
   */
  private void hydrateCalculatorCriterion( Calculator calculator )
  {
    initialize( calculator.getCalculatorCriterion() );
  }

  /**
   * Loads the Calculator Payout list attached on this calculator
   * 
   * @param calculator
   */
  private void hydrateCalculatorPayout( Calculator calculator )
  {
    initialize( calculator.getCalculatorPayouts() );
  }

  /**
   * Hydrate the Calculator Criterion Rating list on each Calculator Criterion in the list attached to this calculator.
   * 
   * @param calculator
   */
  private void hydrateCalculatorCriterionRating( Calculator calculator )
  {
    if ( calculator.getCalculatorCriterion() != null )
    {
      for ( Iterator iter = calculator.getCalculatorCriterion().iterator(); iter.hasNext(); )
      {
        CalculatorCriterion criterion = (CalculatorCriterion)iter.next();
        if ( criterion != null )
        {
          initialize( criterion.getCriterionRatings() );
        }
      }
    }
  }

  /**
   * Loads the Calculator Promotions list attached on this calculator
   * 
   * @param calculator
   */
  private void hydrateCalculatorPromotions( Calculator calculator )
  {
    initialize( calculator.getPromotions() );
  }

}

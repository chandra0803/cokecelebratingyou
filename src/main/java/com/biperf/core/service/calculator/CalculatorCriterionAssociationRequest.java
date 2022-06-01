/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/calculator/CalculatorCriterionAssociationRequest.java,v $
 */

package com.biperf.core.service.calculator;

import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * CalculatorCriterionAssociationRequest.
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
public class CalculatorCriterionAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  /**
   * Hyrate Level: ALL
   */
  public static final int ALL = 1;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public CalculatorCriterionAssociationRequest( int hydrateLevel )
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
    CalculatorCriterion criterion = (CalculatorCriterion)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateCriterionRatings( criterion );
        hydrateCriterionCalculatorCriterion( criterion );
        break;

      default:
        break;
    } // switch
  }

  /**
   * Hydrate the CalculatorCriterionRating list on each criterion
   * 
   * @param criterion
   */
  private void hydrateCriterionRatings( CalculatorCriterion criterion )
  {
    initialize( criterion.getCriterionRatings() );
  }

  /**
   * Hydrate the CalculatorCriterion list on each calculator object on a criterion
   * 
   * @param criterion
   */
  private void hydrateCriterionCalculatorCriterion( CalculatorCriterion criterion )
  {
    initialize( criterion.getCalculator().getCalculatorCriterion() );
  }
}

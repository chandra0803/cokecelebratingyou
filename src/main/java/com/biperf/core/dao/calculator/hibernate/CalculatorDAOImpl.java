/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/calculator/hibernate/CalculatorDAOImpl.java,v $
 */

package com.biperf.core.dao.calculator.hibernate;

import java.util.List;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.calculator.CalculatorDAO;
import com.biperf.core.dao.calculator.CalculatorQueryConstraint;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * CalculatorDAOImpl
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
public class CalculatorDAOImpl extends BaseDAO implements CalculatorDAO
{

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorByIdWithAssociations(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return calculator
   */
  public Calculator getCalculatorByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Calculator calculator = (Calculator)getSession().get( Calculator.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( calculator );
    }
    return calculator;
  }

  /**
   * Get calculator by name. Overridden from
   * 
   * @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorByName(java.lang.String)
   * @param name
   * @return Calculator
   */
  public Calculator getCalculatorByName( String name )
  {
    return (Calculator)getSession().getNamedQuery( "com.biperf.core.domain.calculator.CalculatorByName" ).setString( "name", name.toUpperCase() ).uniqueResult();
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#saveCalculator(com.biperf.core.domain.calculator.Calculator)
   * @param calculator
   * @return calculator
   */
  public Calculator saveCalculator( Calculator calculator )
  {
    return (Calculator)HibernateUtil.saveOrUpdateOrShallowMerge( calculator );
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#deleteCalculator(com.biperf.core.domain.calculator.Calculator)
   * @param calculator
   */
  public void deleteCalculator( Calculator calculator )
  {
    getSession().delete( calculator );
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorList(com.biperf.core.dao.calculator.CalculatorQueryConstraint)
   * @param calcQueryConstraint
   * @return List
   */
  public List getCalculatorList( CalculatorQueryConstraint calcQueryConstraint )
  {
    return HibernateUtil.getObjectList( calcQueryConstraint );
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorListWithAssociations(com.biperf.core.dao.calculator.CalculatorQueryConstraint, com.biperf.core.service.AssociationRequestCollection)
   * @param calcQueryConstraint
   * @param associationRequestCollection 
   * @return List
   */
  public List getCalculatorListWithAssociations( CalculatorQueryConstraint calcQueryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    List calculatorList = HibernateUtil.getObjectList( calcQueryConstraint );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( calculatorList );
    }
    return calculatorList;
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorCriterionByIdWithAssociations(java.lang.Long, com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return calculatorCriterion
   */
  public CalculatorCriterion getCalculatorCriterionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    CalculatorCriterion calculatorCriterion = (CalculatorCriterion)getSession().get( CalculatorCriterion.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( calculatorCriterion );
    }
    return calculatorCriterion;
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorCriterionRatingById(java.lang.Long)
   * @param calculatorCriterionRatingId
   * @return CalculatorCriterionRating
   */
  public CalculatorCriterionRating getCalculatorCriterionRatingById( Long calculatorCriterionRatingId )
  {
    return (CalculatorCriterionRating)getSession().get( CalculatorCriterionRating.class, calculatorCriterionRatingId );
  }

  /**
   * Overridden from @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorPayoutById(java.lang.Long)
   * @param id
   * @return calculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutById( Long id )
  {
    CalculatorPayout calculatorPayout = (CalculatorPayout)getSession().get( CalculatorPayout.class, id );

    return calculatorPayout;
  }

  /**
   * Get calculator payout by score. Overridden from
   * 
   * @see com.biperf.core.dao.calculator.CalculatorDAO#getCalculatorPayoutByScore(java.lang.Long, int)
   * @param calculatorId
   * @param score
   * @return CalculatorPayout
   */
  public CalculatorPayout getCalculatorPayoutByScore( Long calculatorId, int score )
  {
    return (CalculatorPayout)getSession().getNamedQuery( "com.biperf.core.domain.calculator.CalculatorPayoutByScore" ).setLong( "calculatorId", calculatorId.longValue() ).setInteger( "score", score )
        .uniqueResult();
  }

}

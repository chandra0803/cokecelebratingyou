/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/calculator/CalculatorQueryConstraint.java,v $
 */

package com.biperf.core.dao.calculator;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.enums.CalculatorAwardType;
import com.biperf.core.domain.enums.CalculatorStatusType;
import com.biperf.core.utils.HibernateSessionManager;

public class CalculatorQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private CalculatorStatusType[] calculatorStatusTypeIncluded;
  private CalculatorAwardType[] calculatorAwardTypeIncluded;
  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "calculator" );

    if ( calculatorStatusTypeIncluded != null )
    {
      // criteria.add( Restrictions.eq( "calculator.calculatorStatusType",
      // CalculatorStatusType.lookup(status)) );
      criteria.add( Restrictions.in( "calculator.calculatorStatusType", calculatorStatusTypeIncluded ) );
    }

    if ( calculatorAwardTypeIncluded != null )
    {
      criteria.add( Restrictions.in( "calculator.calculatorAwardType", calculatorAwardTypeIncluded ) );
    }

    return criteria;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Return the class of the objects selected by this query constraint.
   *
   * @return the class of the objects selected by this query constraint.
   */
  public Class getResultClass()
  {
    return Calculator.class;
  }

  public CalculatorStatusType[] getCalculatorStatusTypeIncluded()
  {
    return calculatorStatusTypeIncluded;
  }

  public void setCalculatorStatusTypeIncluded( CalculatorStatusType[] calculatorStatusTypeIncluded )
  {
    this.calculatorStatusTypeIncluded = calculatorStatusTypeIncluded;
  }

  public CalculatorAwardType[] getCalculatorAwardTypeIncluded()
  {
    return calculatorAwardTypeIncluded;
  }

  public void setCalculatorAwardTypeIncluded( CalculatorAwardType[] calculatorAwardTypeIncluded )
  {
    this.calculatorAwardTypeIncluded = calculatorAwardTypeIncluded;
  }
}

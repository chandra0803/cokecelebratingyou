/*
 * (c) 2014 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/Attic/SSIPromotionQueryConstraint.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.ssi.SSIPromotion;

public class SSIPromotionQueryConstraint extends PromotionQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If null, include promotions with and without budgets; if true, include only promotions with
   * budgets; if false, include only promotions without budgets.
   */
  private Boolean hasBudgets;

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    if ( hasBudgets != null )
    {
      if ( hasBudgets.booleanValue() )
      {
        // only promotions with budgets
        criteria.add( Restrictions.isNotNull( "promotion.budgetMaster" ) );
      }
      else
      {
        // only promotions without budgets
        criteria.add( Restrictions.isNull( "promotion.budgetMaster" ) );
      }
    }

    return criteria;
  }

  public Class getResultClass()
  {
    return SSIPromotion.class;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Boolean getHasBudgets()
  {
    return hasBudgets;
  }

  public void setHasBudgets( Boolean hasBudgets )
  {
    this.hasBudgets = hasBudgets;
  }
}

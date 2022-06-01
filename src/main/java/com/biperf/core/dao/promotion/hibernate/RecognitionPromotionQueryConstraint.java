/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/promotion/hibernate/RecognitionPromotionQueryConstraint.java,v $
 */

package com.biperf.core.dao.promotion.hibernate;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.utils.DateUtils;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RecognitionPromotionQueryConstraint extends PromotionQueryConstraint
{
  /**
   * if null, include promotions with and without budgets; if true, include only promotions with
   * budgets; if false, include only promotions without budgets.
   */
  private Boolean hasBudgets;

  private Boolean budgetSweepEnabled;
  private Boolean budgetSweepRun;
  private Boolean includePurl;
  private Boolean status;

  /**
   * Return the result object type - Should be overridden by subclasses.
   */
  public Class getResultClass()
  {
    return RecognitionPromotion.class;
  }

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
    if ( budgetSweepEnabled != null )
    {
      criteria.add( Restrictions.eq( "promotion.budgetSweepEnabled", budgetSweepEnabled ) );
    }
    if ( includePurl != null )
    {
      criteria.add( Restrictions.eq( "promotion.includePurl", includePurl ) );
    }
    if ( budgetSweepRun != null )
    {
      criteria.createAlias( "promotion.promotionBudgetSweeps", "budgetSweep" ).add( Restrictions.eq( "budgetSweep.budgetSweepRun", budgetSweepRun ) );
      if ( budgetSweepRun.booleanValue() == Boolean.FALSE )
      {
        Date currentDateEndOfDay = DateUtils.toEndDate( new Date() );
        criteria.add( Restrictions.le( "budgetSweep.budgetSweepDate", currentDateEndOfDay ) );
      }
      criteria.add( Restrictions.eq( "budgetSweep.status", status ) );
    }

    return criteria;
  }

  /**
   * @return value of hasBudgets property
   */
  public Boolean getHasBudgets()
  {
    return hasBudgets;
  }

  /**
   * @param hasBudgets value for hasBudgets property
   */
  public void setHasBudgets( Boolean hasBudgets )
  {
    this.hasBudgets = hasBudgets;
  }

  public Boolean getBudgetSweepEnabled()
  {
    return budgetSweepEnabled;
  }

  public void setBudgetSweepEnabled( Boolean budgetSweepEnabled )
  {
    this.budgetSweepEnabled = budgetSweepEnabled;
  }

  public Boolean getBudgetSweepRun()
  {
    return budgetSweepRun;
  }

  public void setBudgetSweepRun( Boolean budgetSweepRun )
  {
    this.budgetSweepRun = budgetSweepRun;
  }

  public Boolean getIncludePurl()
  {
    return includePurl;
  }

  public void setIncludePurl( Boolean includePurl )
  {
    this.includePurl = includePurl;
  }

  public Boolean getStatus()
  {
    return status;
  }

  public void setStatus( Boolean status )
  {
    this.status = status;
  }

}

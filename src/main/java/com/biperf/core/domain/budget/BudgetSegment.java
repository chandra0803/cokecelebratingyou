/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/budget/BudgetSegment.java,v $
 */

package com.biperf.core.domain.budget;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.BudgetReallocationEligType;
import com.biperf.core.utils.DateUtils;

public class BudgetSegment extends BaseDomain
{

  private static final long serialVersionUID = 3256626154904436249L;
  public static final String BUDGET_PERIOD_NAME_DATA_ASSET_PREFIX = "budget_period_name_data.budgetperiodname.";
  public static final String BUDGET_PERIOD_NAME_KEY = "BUDGET_PERIOD_NAME";
  public static final String BUDGET_PERIOD_DATA_SECTION_CODE = "budget_period_name_data";
  public static final String BUDGET_PERIOD_NAME_SUFFIX = " Budget Period Name";
  public static final String BUDGET_PERIOD_ASSET_TYPE_NAME = "_BudgetPeriodNameData";

  private BudgetMaster budgetMaster;
  private Boolean status;
  private Date startDate;
  private Date endDate;
  private String name;
  private String cmAssetCode;
  private String nameCmKey;
  private boolean allowBudgetReallocation;
  private BudgetReallocationEligType budgetReallocationEligType;
  private Set<Budget> budgets = new LinkedHashSet<Budget>();
  private Set<PromotionBudgetSweep> promotionBudgetSweeps = new LinkedHashSet<PromotionBudgetSweep>();

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof BudgetSegment ) )
    {
      return false;
    }
    final BudgetSegment budgetSegment = (BudgetSegment)o;

    if ( getBudgetMaster() != null ? !getBudgetMaster().equals( budgetSegment.getBudgetMaster() ) : budgetSegment.getBudgetMaster() != null )
    {
      return false;
    }
    if ( getName() != null ? !getName().equals( budgetSegment.getName() ) : budgetSegment.getName() != null )
    {
      return false;
    }
    if ( getStartDate() != null ? !getStartDate().equals( budgetSegment.getStartDate() ) : budgetSegment.getStartDate() != null )
    {
      return false;
    }
    if ( getEndDate() != null ? !getEndDate().equals( budgetSegment.getEndDate() ) : budgetSegment.getEndDate() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "BudgetSegment [budgetMaster=" + budgetMaster + ", status=" + status + ", startDate=" + startDate + ", endDate=" + endDate + ", name=" + name + ", allowBudgetReallocation="
        + allowBudgetReallocation + ", budgetReallocationEligType=" + budgetReallocationEligType + "]";
  }

  public int hashCode()
  {
    int result;
    result = getBudgetMaster() != null ? getBudgetMaster().hashCode() : 0;
    result = 29 * result + ( getStartDate() != null ? getStartDate().hashCode() : 0 );
    result = 29 * result + ( getEndDate() != null ? getEndDate().hashCode() : 0 );
    return result;
  }

  public BudgetMaster getBudgetMaster()
  {
    return budgetMaster;
  }

  public void setBudgetMaster( BudgetMaster budgetMaster )
  {
    this.budgetMaster = budgetMaster;
  }

  public Boolean getStatus()
  {
    return status;
  }

  public void setStatus( Boolean status )
  {
    this.status = status;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public String getDisplayStartDate()
  {
    return DateUtils.toDisplayString( this.getStartDate() );
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public String getDisplayEndDate()
  {
    return DateUtils.toDisplayString( this.getEndDate() );
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public String getDisplaySegmentName()
  {
    return this.getName() + " - " + this.getDisplayStartDate() + " - " + getDisplayEndDate();
  }

  public String getPaxDisplaySegmentName()
  {
    return this.getDisplayStartDate() + " - " + getDisplayEndDate();
  }

  public boolean isAllowBudgetReallocation()
  {
    return allowBudgetReallocation;
  }

  public void setAllowBudgetReallocation( boolean allowBudgetReallocation )
  {
    this.allowBudgetReallocation = allowBudgetReallocation;
  }

  public BudgetReallocationEligType getBudgetReallocationEligType()
  {
    return budgetReallocationEligType;
  }

  public void setBudgetReallocationEligType( BudgetReallocationEligType budgetReallocationEligType )
  {
    this.budgetReallocationEligType = budgetReallocationEligType;
  }

  /**
   * Add a Budget to budgets
   * 
   * @param budget
   */
  public void addBudget( Budget budget )
  {
    budget.setBudgetSegment( this );
    this.budgets.add( budget );
  }

  public Set<Budget> getBudgets()
  {
    return budgets;
  }

  public void setBudgets( Set<Budget> budgets )
  {
    this.budgets = budgets;
  }

  public void addPromotionBudgetSweep( PromotionBudgetSweep promotionBudgetSweep )
  {
    promotionBudgetSweep.setBudgetSegment( this );
    this.promotionBudgetSweeps.add( promotionBudgetSweep );
  }

  public void setPromotionBudgetSweeps( Set<PromotionBudgetSweep> promotionBudgetSweeps )
  {
    this.promotionBudgetSweeps = promotionBudgetSweeps;
  }

  public Set<PromotionBudgetSweep> getPromotionBudgetSweeps()
  {
    return promotionBudgetSweeps;
  }

  public boolean isExpired( Date date )
  {
    Date effectiveStartDate = getStartDate();
    Date effectiveEndDate = getEndDate() == null ? null : new Date( getEndDate().getTime() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59000 );
    if ( date == null )
    {
      date = new Date();
    }

    if ( effectiveStartDate == null )
    {
      if ( effectiveEndDate != null && date.after( effectiveEndDate ) )
      {
        return true;
      }
      return false;
    }

    if ( effectiveEndDate != null && ( date.before( effectiveStartDate ) || date.after( effectiveEndDate ) ) )
    {
      return true;
    }
    return false;
  }

  public boolean isExpired()
  {
    return isExpired( null );
  }

  public boolean isContainingDeletableBudgets()
  {
    boolean hasOne = false;
    Iterator iter = budgets.iterator();
    while ( !hasOne && iter.hasNext() )
    {
      Budget budget = (Budget)iter.next();
      hasOne = budget.isDeletable();
    }
    return hasOne;
  }

  public boolean isCurrentOrFuture()
  {
    Date effectiveEndDate = getEndDate() == null ? null : new Date( getEndDate().getTime() + 23 * 60 * 60 * 1000 + 59 * 60 * 1000 + 59000 );
    Date date = new Date();

    if ( effectiveEndDate != null && date.before( effectiveEndDate ) || effectiveEndDate == null )
    {
      return true;
    }
    return false;
  }

}

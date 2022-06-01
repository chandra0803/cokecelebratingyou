
package com.biperf.core.domain.budget;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.RecognitionPromotion;

public class PromotionBudgetSweep extends BaseDomain
{

  /** serialVersionUID */
  private static final long serialVersionUID = 3905523717891630136L;

  /** recognition promotion */
  private RecognitionPromotion recognitionPromotion;

  /** budgetSegment */
  private BudgetSegment budgetSegment;

  private Date budgetSweepDate;
  private boolean budgetSweepRun;
  private Date budgetSweepRunDate;
  private boolean status;

  public void setRecognitionPromotion( RecognitionPromotion recognitionPromotion )
  {
    this.recognitionPromotion = recognitionPromotion;
  }

  public RecognitionPromotion getRecognitionPromotion()
  {
    return recognitionPromotion;
  }

  public BudgetSegment getBudgetSegment()
  {
    return budgetSegment;
  }

  public void setBudgetSegment( BudgetSegment budgetSegment )
  {
    this.budgetSegment = budgetSegment;
  }

  public Date getBudgetSweepDate()
  {
    return budgetSweepDate;
  }

  public boolean isBudgetSweepRun()
  {
    return budgetSweepRun;
  }

  public Date getBudgetSweepRunDate()
  {
    return budgetSweepRunDate;
  }

  public void setBudgetSweepDate( Date budgetSweepDate )
  {
    this.budgetSweepDate = budgetSweepDate;
  }

  public void setBudgetSweepRun( boolean budgetSweepRun )
  {
    this.budgetSweepRun = budgetSweepRun;
  }

  public void setBudgetSweepRunDate( Date budgetSweepRunDate )
  {
    this.budgetSweepRunDate = budgetSweepRunDate;
  }

  public boolean isStatus()
  {
    return status;
  }

  public void setStatus( boolean status )
  {
    this.status = status;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    return ToStringBuilder.reflectionToString( PromotionBudgetSweep.class );
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof PromotionBudgetSweep ) )
    {
      return false;
    }

    final PromotionBudgetSweep promoBudgetSweep = (PromotionBudgetSweep)o;

    if ( getBudgetSegment() != null ? !getBudgetSegment().equals( promoBudgetSweep.getBudgetSegment() ) : promoBudgetSweep.getBudgetSegment() != null )
    {
      return false;
    }
    if ( getRecognitionPromotion() != null ? !getRecognitionPromotion().equals( promoBudgetSweep.getRecognitionPromotion() ) : promoBudgetSweep.getRecognitionPromotion() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getBudgetSegment() != null ? getBudgetSegment().hashCode() : 0;
    result = 29 * result + ( getRecognitionPromotion() != null ? getRecognitionPromotion().hashCode() : 0 );

    return result;
  }
}


package com.biperf.core.domain.budget;

import java.util.Comparator;

import com.biperf.core.value.BudgetSegmentValueBean;

public class BudgetSegmentValueBeanComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof BudgetSegmentValueBean ) || ! ( o2 instanceof BudgetSegmentValueBean ) )
    {
      throw new ClassCastException( "Object is not a budgetSegment domain object!" );
    }
    BudgetSegmentValueBean segment1 = (BudgetSegmentValueBean)o1;
    BudgetSegmentValueBean segment2 = (BudgetSegmentValueBean)o2;

    if ( segment1 != null && segment2 != null )
    {
      return new Long( segment1.getStartDate().getTime() ).compareTo( new Long( segment2.getStartDate().getTime() ) );
    }
    return 0;
  }
}

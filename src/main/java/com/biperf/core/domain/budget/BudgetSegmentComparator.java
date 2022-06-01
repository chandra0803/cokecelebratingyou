
package com.biperf.core.domain.budget;

import java.util.Comparator;

public class BudgetSegmentComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof BudgetSegment ) || ! ( o2 instanceof BudgetSegment ) )
    {
      throw new ClassCastException( "Object is not a budgetSegment domain object!" );
    }
    BudgetSegment segment1 = (BudgetSegment)o1;
    BudgetSegment segment2 = (BudgetSegment)o2;

    if ( segment1 != null && segment2 != null )
    {
      return new Long( segment1.getStartDate().getTime() ).compareTo( new Long( segment2.getStartDate().getTime() ) );
    }
    return 0;
  }
}

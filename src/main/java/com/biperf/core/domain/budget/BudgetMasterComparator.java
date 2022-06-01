
package com.biperf.core.domain.budget;

import java.util.Comparator;

public class BudgetMasterComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof BudgetMaster ) || ! ( o2 instanceof BudgetMaster ) )
    {
      throw new ClassCastException( "Object is not a budgetMaster domain object!" );
    }
    BudgetMaster master1 = (BudgetMaster)o1;
    BudgetMaster master2 = (BudgetMaster)o2;

    if ( master1 != null && master2 != null )
    {
      return master1.getBudgetName().toLowerCase().compareTo( master2.getBudgetName().toLowerCase() );
    }
    return 0;
  }
}

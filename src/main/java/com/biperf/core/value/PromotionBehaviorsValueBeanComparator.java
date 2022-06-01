
package com.biperf.core.value;

import java.util.Comparator;

public class PromotionBehaviorsValueBeanComparator implements Comparator<PromotionBehaviorsValueBean>
{
  public int compare( PromotionBehaviorsValueBean o1, PromotionBehaviorsValueBean o2 )
  {

    if ( o1.getPromoNominationBehaviorTypeName().equals( o2.getPromoNominationBehaviorTypeName() ) )
    {
      return 0;
    }
    if ( o1.getPromoNominationBehaviorTypeName() == null )
    {
      return -1;
    }
    if ( o2.getPromoNominationBehaviorTypeName() == null )
    {
      return 1;
    }
    return o1.getPromoNominationBehaviorTypeName().toUpperCase().compareTo( o2.getPromoNominationBehaviorTypeName().toUpperCase() );
  }
}

/**
* 
*/

package com.biperf.core.ui.promotion;

import java.util.Comparator;

/**
 * @author poddutur
 *
 */
public class PromotionAwardsFormBeanComparator implements Comparator
{

  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof PromotionAwardsFormBean ) || ! ( o2 instanceof PromotionAwardsFormBean ) )
    {
      throw new ClassCastException( "Object is not a PromotionAwardsFormBean domain object!" );
    }
    PromotionAwardsFormBean bean1 = (PromotionAwardsFormBean)o1;
    PromotionAwardsFormBean bean2 = (PromotionAwardsFormBean)o2;

    return ( (Comparable)bean1.getBudgetMasterName().toLowerCase() ).compareTo( bean2.getBudgetMasterName().toLowerCase() );
  }
}

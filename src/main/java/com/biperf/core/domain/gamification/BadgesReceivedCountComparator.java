/**
 * 
 */

package com.biperf.core.domain.gamification;

import java.io.Serializable;
import java.util.Comparator;

import com.biperf.core.value.EngagementBehaviorValueBean;

/**
 * @author alagappa
 *
 */
public class BadgesReceivedCountComparator implements Comparator, Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 3751103052018435893L;

  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof EngagementBehaviorValueBean ) || ! ( o2 instanceof EngagementBehaviorValueBean ) )
    {
      throw new ClassCastException( "Object is not a EngagementBehaviorValueBean object!" );
    }
    EngagementBehaviorValueBean bean1 = (EngagementBehaviorValueBean)o1;
    EngagementBehaviorValueBean bean2 = (EngagementBehaviorValueBean)o2;

    if ( bean1 != null && bean2 != null )
    {
      Integer bean1RecCnt = new Integer( bean1.getReceivedCnt() );
      Integer bean2RecCnt = new Integer( bean2.getReceivedCnt() );

      int compare1 = ( (Comparable)bean2RecCnt ).compareTo( bean1RecCnt );
      if ( compare1 != 0 )
      {
        return compare1;
      }

      int compare2 = bean1.getBehavior().compareTo( bean2.getBehavior() );
      if ( compare2 != 0 )
      {
        return compare2;
      }
    }
    return 0;
  }

}

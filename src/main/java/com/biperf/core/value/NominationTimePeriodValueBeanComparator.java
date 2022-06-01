
package com.biperf.core.value;

import java.util.Comparator;

public class NominationTimePeriodValueBeanComparator implements Comparator
{

  public int compare( Object o1, Object o2 )
  {
    NominationTimePeriodValueBean timePeriod1 = (NominationTimePeriodValueBean)o1;
    NominationTimePeriodValueBean timePeriod2 = (NominationTimePeriodValueBean)o2;

    if ( timePeriod1 != null && timePeriod2 != null )
    {
      return new Long( timePeriod1.getStartDate().getTime() ).compareTo( new Long( timePeriod2.getStartDate().getTime() ) );
    }
    return 0;
  }

}

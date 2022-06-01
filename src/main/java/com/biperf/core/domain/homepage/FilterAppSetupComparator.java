
package com.biperf.core.domain.homepage;

import java.util.Comparator;

public class FilterAppSetupComparator implements Comparator<FilterAppSetup>
{
  /*
   * Sort order is in this priority based on: -- filter type: sort order (ie, which filter to
   * display first -- Priority (ie, 1 or 2) -- orderNumber
   */
  @Override
  public int compare( FilterAppSetup filter1, FilterAppSetup filter2 )
  {
    int ret = 0;
    // type
    ret = new Integer( filter1.getFilterSetupType().getSortOrder() ).compareTo( new Integer( filter2.getFilterSetupType().getSortOrder() ) );
    if ( ret != 0 )
    {
      return ret;
    }
    // priority
    ret = new Integer( filter1.getPriority() ).compareTo( new Integer( filter2.getPriority() ) );
    if ( ret != 0 )
    {
      return ret;
    }
    // orderNumber
    return new Integer( filter1.getOrderNumber() ).compareTo( new Integer( filter2.getOrderNumber() ) );
  }
}

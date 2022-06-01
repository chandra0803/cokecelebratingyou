package com.biperf.core.value.client;

import java.util.Comparator;



public class TcccLevelPayoutValueBeanComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof TcccLevelPayoutValueBean ) || ! ( o2 instanceof TcccLevelPayoutValueBean ) )
    {
      throw new ClassCastException( "Object is not a TcccLevelPayout domain object!" );
    }
    TcccLevelPayoutValueBean segment1 = (TcccLevelPayoutValueBean)o1;
    TcccLevelPayoutValueBean segment2 = (TcccLevelPayoutValueBean)o2;
    
    if(segment1 != null && segment2 != null)
    {
        return (new Long(segment1.getLevelPayoutId())).compareTo(new Long(segment2.getLevelPayoutId()));
    }
    return 0;
  }
}
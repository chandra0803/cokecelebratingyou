
package com.biperf.core.service.awardbanq;

import java.util.Comparator;

import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;

@SuppressWarnings( "rawtypes" )
public class MerchLevelValueObjectComparator implements Comparator
{
  public int compare( Object arg1, Object arg2 )
  {
    Long maxValue1 = new Long( ( (MerchLevelValueObject)arg1 ).getMaxValue() );
    Long maxValue2 = new Long( ( (MerchLevelValueObject)arg2 ).getMaxValue() );

    return maxValue1.compareTo( maxValue2 );
  }
}

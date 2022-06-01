
package com.biperf.core.service.diycommunications.impl;

import java.util.Comparator;
import java.util.Date;

import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;

public class NewsDateComparator implements Comparator<Content>
{
  public int compare( Content o1, Content o2 )
  {
    Date startDate1 = DateUtils.toDate( (String)o1.getContentDataMap().get( "START_DATE" ), UserManager.getDefaultLocale() );
    Date startDate2 = DateUtils.toDate( (String)o2.getContentDataMap().get( "START_DATE" ), UserManager.getDefaultLocale() );

    if ( startDate1 != null && startDate2 != null ) // both have dates, standard compare
    {
      return startDate2.compareTo( startDate1 );
    }
    if ( startDate1 == null && startDate2 == null ) // both dates are null, return "even"
    {
      return 0;
    }
    if ( startDate1 == null )
    {
      return 1;
    }
    return -1; // startDate2 is not null, default value
  }
}

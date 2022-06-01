
package com.biperf.core.service.diycommunications.impl;

import java.util.Comparator;

import com.objectpartners.cms.domain.Content;

public class NewsSortOrderComparator implements Comparator<Content>
{
  public int compare( Content o1, Content o2 )
  {
    return ( (String)o1.getContentDataMap().get( "SORT_ORDER" ) ).compareTo( (String)o2.getContentDataMap().get( "SORT_ORDER" ) );
  }
}

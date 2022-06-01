
package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.ProxyCoreAccessType;

public class UserProxyUtils
{
  public static String toCoreAccess( String[] list )
  {
    StringBuilder buffer = new StringBuilder();
    if ( null != list && list.length > 0 )
    {
      boolean first = true;
      for ( String item : list )
      {
        if ( first )
        {
          first = false;
        }
        else
        {
          buffer.append( "|" );
        }
        buffer.append( item );
      }
    }
    return buffer.toString();
  }

  public static String[] toCoreAccessList( String value )
  {
    if ( !StringUtils.isEmpty( value ) )
    {
      return value.split( "\\|" );
    }
    return null;
  }

  public static List<ProxyCoreAccessType> toCoreAccessPickList( String value )
  {
    List<ProxyCoreAccessType> list = new ArrayList<ProxyCoreAccessType>();
    String[] items = toCoreAccessList( value );
    if ( null != items && items.length > 0 )
    {
      for ( String item : items )
      {
        list.add( ProxyCoreAccessType.lookup( item ) );
      }
    }
    return list;
  }

}

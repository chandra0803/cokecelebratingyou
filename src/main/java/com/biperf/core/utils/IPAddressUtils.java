
package com.biperf.core.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;

public class IPAddressUtils
{
  private static final Log log = LogFactory.getLog( IPAddressUtils.class );

  // Private constructor
  private IPAddressUtils()
  {
  }

  public static boolean isAccessRestricted( final String sysVarKey )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( sysVarKey );
    return null != property && property.getBooleanVal();
  }

  public static boolean isAllowAccess( final String sysVarKey, final String userIP )
  {
    final String[] validIPList = getValidIPList( sysVarKey );
    if ( null != validIPList )
    {
      for ( String validIP : validIPList )
      {
        if ( log.isDebugEnabled() )
        {
          log.debug( "Checking if user IP " + userIP + " is allowed by " + validIP );
        }

        if ( !StringUtil.isEmpty( userIP ) && userIP.matches( validIP ) )
        {
          if ( log.isDebugEnabled() )
          {
            log.debug( "Access allowed for user IP " + userIP );
          }
          return true;
        }
      }
    }

    if ( log.isDebugEnabled() )
    {
      log.debug( "Access denied for user IP " + userIP );
    }
    return false;
  }

  private static String[] getValidIPList( final String sysVarKey )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( sysVarKey );
    if ( null != property && null != property.getStringVal() )
    {
      return property.getStringVal().split( "," );
    }
    return null;
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}

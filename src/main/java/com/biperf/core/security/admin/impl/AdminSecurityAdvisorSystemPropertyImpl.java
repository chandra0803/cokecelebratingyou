
package com.biperf.core.security.admin.impl;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.security.admin.AbstractAdminSecurityAdvisor;
import com.biperf.core.security.admin.AdminSecurityAdvisor;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.StringUtil;

public class AdminSecurityAdvisorSystemPropertyImpl extends AbstractAdminSecurityAdvisor implements AdminSecurityAdvisor
{
  private static final Log log = LogFactory.getLog( AdminSecurityAdvisorSystemPropertyImpl.class );

  private SystemVariableService systemVariableService = null;

  public boolean isValidAdminIp( ServletRequest servletRequest )
  {
    HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;

    if ( isAllowAccess( getRemoteAddress( httpRequest ) ) )
    {
      return true;
    }

    return false;
  }

  private boolean isAllowAccess( String userIP )
  {
    final String[] validIPList = getValidIPList();
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

  private String[] getValidIPList()
  {
    PropertySetItem property = systemVariableService.getPropertyByName( SystemVariableService.ADMIN_IP_RESTRICTIONS );
    if ( null != property && null != property.getStringVal() )
    {
      return property.getStringVal().split( "," );
    }
    return new String[] { "0:0:0:0:0:0:0:1" };
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

}

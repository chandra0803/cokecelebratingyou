
package com.biperf.core.security.admin;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractAdminSecurityAdvisor implements AdminSecurityAdvisor
{
  public String getRemoteAddress( HttpServletRequest request )
  {
    String remoteAddress = request.getHeader( "X-FORWARDED-FOR" );

    if ( remoteAddress == null )
    {
      remoteAddress = request.getRemoteAddr();
    }
    if ( remoteAddress.indexOf( "," ) >= 0 )
    {
      remoteAddress = remoteAddress.substring( 0, remoteAddress.indexOf( "," ) );
    }

    return remoteAddress;
  }
}

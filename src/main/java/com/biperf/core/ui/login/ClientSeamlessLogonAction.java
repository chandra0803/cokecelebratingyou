
package com.biperf.core.ui.login;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.IPAddressUtils;

public class ClientSeamlessLogonAction extends SeamlessLogonAction
{

  private static final Log logger = LogFactory.getLog( ClientSeamlessLogonAction.class );

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    return super.unspecified( mapping, actionForm, request, response );
  }

  @Override
  protected Object buildCredentials( HttpServletRequest request, Map map, String ssoUniqueId )
  {
    return super.buildCredentials( request, map, ssoUniqueId );
  }

  @Override
  public ActionForward encryptParmsTest( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    if ( !IPAddressUtils.isAllowAccess( SystemVariableService.ADMIN_IP_RESTRICTIONS, getRemoteAddress( request ) ) )
    {
      logger.error( "IP Address: " + getRemoteAddress( request ) + " is not a valid ip addresses" );
      return mapping.findForward( "access_denied" );
    }
    return super.encryptParmsTest( mapping, actionForm, request, response );
  }

  // --------------------------
  // Testing Methods
  // --------------------------
  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward test( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    ActionMessages errors = new ActionMessages();

    errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "Not Implemented" ) );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return mapping.findForward( "test" );
  }

  private String getRemoteAddress( HttpServletRequest httpServletRequest )
  {
    String remoteAddress = httpServletRequest.getHeader( "X-FORWARDED-FOR" );
    if ( remoteAddress == null )
    {
      remoteAddress = httpServletRequest.getRemoteAddr();
    }
    if ( remoteAddress.indexOf( "," ) >= 0 )
    {
      remoteAddress = remoteAddress.substring( 0, remoteAddress.indexOf( "," ) );
    }
    logger.debug( "Remote address:" + remoteAddress + ", Request url:" + httpServletRequest.getRequestURL() );
    return remoteAddress;
  }

}

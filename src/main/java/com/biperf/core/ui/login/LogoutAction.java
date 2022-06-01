/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/login/LogoutAction.java,v $
 */

package com.biperf.core.ui.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.UserManager;

/**
 * LogoutAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>jdunne</td>
 * <td>Mar 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LogoutAction extends BaseDispatchAction
{
  /**
   * Method execute
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    AuthenticatedUser authenticatedUser = UserManager.getUser();

    if ( authenticatedUser == null )
    {
      // timed out
      if ( getSystemVariableService().getPropertyByName( SystemVariableService.LOGOUT_TIMEOUT_LIKE_SSO ).getBooleanVal() )
      {
        return mapping.findForward( "successSso" );
      }
      else
      {
        return mapping.findForward( "success" );
      }
    }

    boolean fromSSO = authenticatedUser.isFromSSO();

    ActionForward lForward = null;
    if ( fromSSO )
    {
      lForward = mapping.findForward( "successSso" );
    }
    else
    {
      // go back to the login screen
      lForward = mapping.findForward( "success" );
    }
    // we are setting this in webSecurity Filter
    request.getSession().removeAttribute( "userObj" );

    request.getSession().invalidate();

    return lForward;
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}

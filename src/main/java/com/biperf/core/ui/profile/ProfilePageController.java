/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/profile/ProfilePageController.java,v $
 */

package com.biperf.core.ui.profile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * ChangePasswordController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ProfilePageController extends BaseController
{
  /**
   * Prepares the form and request for the userProfile page.
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_BADGES ).getBooleanVal() )
    {
      request.setAttribute( "showBadge", true );
    }
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.PROFILE_FOLLOWLIST_SHOW ).getBooleanVal() )
    {
      request.setAttribute( "showFollowList", true );
    }

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_DELEGATE ).getBooleanVal() )
    {
      request.setAttribute( "showDelegate", true );
    }
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}

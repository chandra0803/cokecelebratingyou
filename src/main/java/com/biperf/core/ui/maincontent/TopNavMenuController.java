/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/maincontent/TopNavMenuController.java,v $
 */

package com.biperf.core.ui.maincontent;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.constants.ViewAttributeNames;
import com.biperf.core.utils.UserManager;

/**
 * TopNavMenuController.
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
 * <td>sharma</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TopNavMenuController extends BaseController
{
  /**
   * The controller gets the top navigation menu data from MainContentService Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    MainContentService service = (MainContentService)getService( MainContentService.BEAN_NAME );
    AuthenticatedUser authenticatedUser = UserManager.getUser();
    authenticatedUser.setRouteId( request.getHeader( "proxy-jroute" ) );
    List eligiblePromoList = (List)request.getSession().getAttribute( "eligiblePromotions" );

    // use original menus
    request.setAttribute( ViewAttributeNames.TOP_NAV_MENUS, service.getUserMenus( authenticatedUser, eligiblePromoList ) );

  }
}

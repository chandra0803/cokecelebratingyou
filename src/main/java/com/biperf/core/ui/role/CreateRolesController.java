/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/role/CreateRolesController.java,v $
 */

package com.biperf.core.ui.role;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.security.RoleService;
import com.biperf.core.ui.BaseController;

/**
 * Controller class to manage requests calls to role.create.widget tile.
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
 * <td>crosenquest</td>
 * <td>Apr 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CreateRolesController extends BaseController
{

  /**
   * Performs fetching the Role list from the service to display as objects which will allow update.
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

    // Get the roleList and put it in the request for processing
    Set roles = getRoleService().getAll();
    request.setAttribute( "allRoles", roles );

  }

  /**
   * Get the RoleService from the beanLocator.
   * 
   * @return RoleService
   */
  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserAddressListController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;

/**
 * UserAddressListController.
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
 * <td>robinsra</td>
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAddressListController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    UserAddressListForm userAddressListForm = (UserAddressListForm)request.getAttribute( "userAddressListForm" );
    Long userId = new Long( userAddressListForm.getUserId() );

    Set userAddresses = getUserService().getUserAddresses( userId );

    request.setAttribute( "userAddressList", userAddresses );

    // for displaying name
    if ( userAddressListForm.getUserId() != null && !"".equals( userAddressListForm.getUserId() ) )
    {
      request.setAttribute( "displayNameUserId", userAddressListForm.getUserId() );
    }
  }

  /**
   * Gets a User Service
   * 
   * @return User Service
   * @throws Exception
   */
  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  } // end getUserService

} // end class UserAddressListController

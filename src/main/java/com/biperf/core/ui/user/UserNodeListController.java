/*
 * (c) 2005 BI, Inc. All rights reserved. $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserNodeListController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * UserNodeListController.
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
 * <td>May 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNodeListController extends BaseController
{

  /**
   * Manages getting a list of nodes both available and assigned from the user.
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long userId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        userId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        userId = new Long( (String)clientStateMap.get( "userId" ) );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( userId != null && userId.longValue() != 0 )
    {

      UserService userService = getUserService();

      request.setAttribute( "userAssignedNodeList", userService.getAssignedNodes( userId ) );
      request.setAttribute( "userNodeList", userService.getUserById( userId ).getUserNodes() );
      request.setAttribute( "unassignedNodeList", userService.getUnassignedNodes( userId ) );
      request.setAttribute( "user", userService.getUserById( userId ) );
    }

    request.setAttribute( "hierarchyTypeValues", HierarchyRoleType.getList() );

    // for displaying name
    if ( userId != null )
    {
      String requestUserId = String.valueOf( userId.longValue() );
      request.setAttribute( "displayNameUserId", requestUserId );
    }
  }

  /**
   * Gets the UserService through BeanLocation.
   * 
   * @return UserService
   * @throws Exception
   */
  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}

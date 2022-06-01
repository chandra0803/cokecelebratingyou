/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.security.hibernate.RoleQueryConstraint;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class UserRoleController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Assign Roles page.
   *
   * @param componentContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    RequestWrapper requestWrapper = new RequestWrapper( request );

    String userId = null;
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
        userId = (String)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "userId" );
        userId = id.toString();
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Get the user.
    User user = getUserService().getUserById( new Long( userId ) );

    // Load the form.
    UserRoleForm form = requestWrapper.getUserRoleForm();
    form.load( user );
    form.setMethod( "save" );

    // Get the roles for this type of user.
    UserWrapper userWrapper = new UserWrapper( user );
    List assignedRoles = userWrapper.getAssignedRoles();
    List unassignedRoles = userWrapper.getUnassignedRoles();

    // Setup the request.
    request.setAttribute( "user", user );
    request.setAttribute( "assignedRoles", assignedRoles );
    request.setAttribute( "unassignedRoles", unassignedRoles );
    request.setAttribute( "roleCount", new Integer( assignedRoles.size() + unassignedRoles.size() ) );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the user service.
   *
   * @return a reference to the user service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adds behavior needed by the <code>UserRoleController</code> class to a
   * {@link HttpServletRequest} object.
   */
  private static class RequestWrapper extends HttpServletRequestWrapper
  {
    /**
     * Constructs a <code>RequestWrapper</code> object.
     *
     * @param request  the <code>HttpServletRequest</code> to be wrapped.
     */
    RequestWrapper( HttpServletRequest request )
    {
      super( request );
    }

    /**
     * Returns the ID of the user to whom roles will be assigned.
     *
     * @return the ID of the user to whom roles will be assigned.
     */
    public Long getUserId()
    {
      Long userId = null;

      String userIdString = getParameter( "userId" );
      if ( userIdString != null && userIdString.length() > 0 )
      {
        userId = new Long( userIdString );
      }

      return userId;
    }

    /**
     * Returns the user role form associated with this HTTP request. If none
     * exists, this method creates one.
     *
     * @return the user role form associated with this HTTP request.
     */
    UserRoleForm getUserRoleForm()
    {
      UserRoleForm userRoleForm = (UserRoleForm)getAttribute( "userRoleForm" );
      if ( userRoleForm == null )
      {
        userRoleForm = new UserRoleForm();
      }

      return userRoleForm;
    }
  }

  /**
   * Adds behavior needed by the <code>UserRoleController</code> class to a
   * {@link User} object.
   */
  private static class UserWrapper
  {
    /**
     * The {@link User} object wrapped by this class.
     */
    private User user;

    /**
     * Constructs a <code>UserWrapper</code> object.
     *
     * @param user  the {@link User} object to be wrapped.
     */
    UserWrapper( User user )
    {
      this.user = user;
    }

    /**
     * Returns the roles assigned to the user.
     *
     * @return the roles assigned to the user, as a <code>List</code> of
     *         {@link Role} objects ordered by role name.
     */
    private List getAssignedRoles()
    {
      List assignedRoles = user.getRoles();

      Collections.sort( assignedRoles, new Comparator()
      {
        public int compare( Object o1, Object o2 )
        {
          Role r1 = (Role)o1;
          Role r2 = (Role)o2;

          return r1.getName().compareTo( r2.getName() );
        }
      } );

      return assignedRoles;
    }

    /**
     * Returns the roles not assigned to the user.
     *
     * @return the roles assigned to the user, as a <code>List</code>
     *         of {@link Role} objects ordered by role name.
     */
    private List getUnassignedRoles()
    {
      // Get the roles available to the user.
      RoleQueryConstraint queryConstraint = new RoleQueryConstraint();
      queryConstraint.setActive( Boolean.TRUE );
      queryConstraint.setUserTypesIncluded( new String[] { user.getUserType().getCode() } );
      List allRoles = getRoleService().getRoleList( queryConstraint );

      // Get the roles assigned to the user.
      List assignedRoles = user.getRoles();

      // Get the roles not assigned to the user.
      List unassignedRoles = new ArrayList( allRoles );
      unassignedRoles.removeAll( assignedRoles );

      // Sort the unassigned roles by role name.
      Collections.sort( unassignedRoles, new Comparator()
      {
        public int compare( Object o1, Object o2 )
        {
          Role r1 = (Role)o1;
          Role r2 = (Role)o2;

          return r1.getName().compareTo( r2.getName() );
        }
      } );

      return unassignedRoles;
    }

    /**
     * Returns the role service.
     *
     * @return a reference to the role service.
     */
    private RoleService getRoleService()
    {
      return (RoleService)getService( RoleService.BEAN_NAME );
    }
  }
}

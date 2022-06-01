/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/role/RoleAction.java,v $
 */

package com.biperf.core.ui.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.hibernate.exception.ConstraintViolationException;

import com.biperf.core.domain.user.Role;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * Action to manage CUR on UserRoles.
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
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RoleAction extends BaseDispatchAction
{

  /**
   * Prepare the display for updating a role.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    RoleForm roleForm = (RoleForm)form;
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    ActionMessages errors = new ActionMessages();

    String roleId = null;
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
        roleId = (String)clientStateMap.get( "roleId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "roleId" );
        roleId = id.toString();
      }

      if ( roleId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "roleId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Role roleToUpdate = getRoleService().getRoleById( new Long( roleId ) );
    request.setAttribute( "isRoleBiwOnly", getRoleService().isRoleBiwOnly( roleToUpdate ) );

    roleForm.load( roleToUpdate );
    roleForm.setMethod( "save" );
    roleForm.setUpdate( true );

    request.setAttribute( RoleForm.FORM_NAME, roleForm );

    ActionForward forward = mapping.findForward( forwardTo );

    // get the actionForward to display the create pages.
    return forward;
  }

  /**
   * Update the Role with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      forwardTo = ActionConstants.CANCEL_FORWARD;
    }
    else
    {

      RoleService roleService = getRoleService();
      Role role = ( (RoleForm)form ).toDomainObject();

      try
      {
        roleService.saveRole( role );
      }
      catch( ConstraintViolationException cve )
      {
        log.warn( "User tried to save a role with an already existing name: " + cve );
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "admin.role.UNIQUE_CONSTRAINT" ) );

      }
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      request.setAttribute( RoleForm.FORM_NAME, new RoleForm() );
    }

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Forward to displayCreate pages.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.CREATE_FORWARD;

    RoleForm roleForm = (RoleForm)form;
    roleForm.load( new Role() );
    roleForm.setMethod( "save" );
    roleForm.setUpdate( false );

    request.setAttribute( "roleForm", roleForm );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Forward to displaySearch pages.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Create method manages creating a Role.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_CREATE;

    Role role = ( (RoleForm)form ).toDomainObject();
    role.setVersion( null );

    getRoleService().saveRole( role );

    request.setAttribute( RoleForm.FORM_NAME, new RoleForm() );

    return mapping.findForward( forwardTo );
  }

  /**
   * Search for a list of roles with the given search criteria.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    List roleList = new ArrayList();

    String forwardTo = ActionConstants.SUCCESS_SEARCH;

    RoleForm roleForm = (RoleForm)form;

    Boolean activityStatus = determineSearchActitivyStatus( roleForm );

    // determine what activity status to search for
    roleList = getRoleService().searchRole( roleForm.getHelpText(), activityStatus );

    request.setAttribute( "roleList", roleList );

    request.setAttribute( RoleForm.FORM_NAME, new RoleForm() );

    return mapping.findForward( forwardTo );

  }

  /**
   * Will determine the boolean value for the search parameter activity status.
   * 
   * @param roleForm
   * @return Boolean
   */
  private Boolean determineSearchActitivyStatus( RoleForm roleForm )
  {

    if ( roleForm.getActivityStatus().equals( "1" ) )
    {
      return Boolean.valueOf( true );
    }
    else if ( roleForm.getActivityStatus().equals( "0" ) )
    {
      return Boolean.valueOf( false );
    }

    return null;

  }

  /**
   * Get a userRoleService for managing actions to userRoles.
   * 
   * @return UserRoleService
   */
  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }

}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserRoleAction.java,v $
 */

package com.biperf.core.ui.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserRole;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * Action class for UserRole CRU operations.
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
 * <td>sedey</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserRoleAction extends BaseDispatchAction
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Assigns the selected roles to the specified user.
   * 
   * @param mapping the action mapping used to select this instance.
   * @param form the ActionForm bean for this request.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @return an <code>ActionForward</code> instance describing where and how control should be
   *         forwarded, or null if the response has already been completed.
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    UserRoleForm userRoleForm = (UserRoleForm)form;
    User user = userRoleForm.toDomainObject();

    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    if ( !user.isParticipant() )
    {
      if ( user.getUserType().getCode().equals( UserType.BI ) )
      {
        forwardTo = "success_bi_user";
      }
      else
      {
        forwardTo = "success_client_user";
      }
    }

    if ( isCancelled( request ) )
    {

      forwardTo = ActionConstants.CANCEL_FORWARD;
      if ( !user.isParticipant() )
      {
        if ( user.getUserType().getCode().equals( UserType.BI ) )
        {
          forwardTo = "cancel_bi_user";
        }
        else
        {
          forwardTo = "cancel_client_user";
        }
      }
    }
    else
    {
      if ( userRoleForm.getAssignedRoles() == null )
      {
        userRoleForm.setAssignedRoles( new String[] {} );
      }

      List userRoleList = new ArrayList();
      for ( int i = 0; i < userRoleForm.getAssignedRoles().length; i++ )
      {
        Role newRole = getRoleService().getRoleById( new Long( userRoleForm.getAssignedRoles()[i] ) );
        userRoleList.add( new UserRole( user, newRole ) );
      }

      getUserService().updateUserRoles( userRoleForm.getUserId(), userRoleList );
    }

    ActionForward forward = mapping.findForward( forwardTo );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", user.getId() );
    String returnUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), forward.getPath(), clientStateParameterMap );
    response.sendRedirect( returnUrl );
    return null;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the user service.
   * 
   * @return a reference to the user service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /**
   * Returns a reference to the role service.
   * 
   * @return a reference to the role service.
   */
  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }
}

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserSecurityAction.java,v $
 */

package com.biperf.core.ui.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.user.Acl;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.reports.DashboardReportsService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * UserSecurityAction.
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
 * <td>Sep 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserSecurityAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( UserSecurityAction.class );

  /**
   * Prepares the display for the userAcls and userRoles.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DETAILS_FORWARD;
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
        userId = ( (Long)clientStateMap.get( "userId" ) ).toString();
      }
      if ( userId != null )
      {
        UserSecurityForm userSecurityForm = (UserSecurityForm)form;
        userSecurityForm.setUserId( userId );
        userSecurityForm.setMethod( "update" );

        User user = getUserById( new Long( userId ) );
        request.setAttribute( "user", user );

        userSecurityForm.setUserAclListSize( user.getUserAcls().size() );
        userSecurityForm.setUserAclList( user.getUserAcls() );
      }
      else
      {
        logger.error( "userId was not part of client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    // decide to show/hide clear previous dashboard access button
    if ( getDashboardReportsService().dashboardExistsForUser( Long.parseLong( userId ) ) )
    {
      request.setAttribute( "showClearDashboard", Boolean.TRUE );
    }
    else
    {
      request.setAttribute( "showClearDashboard", Boolean.FALSE );
    }
    return mapping.findForward( forwardTo );
  }

  public ActionForward clearDashboard( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String userId = getUserId( request );
    getDashboardReportsService().deleteUserDashboard( Long.parseLong( userId ) );
    Map parameterMap = new HashMap();
    parameterMap.put( "userId", userId );
    String redirectUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/participant/displayUserSecurity.do?method=display", parameterMap );
    response.sendRedirect( redirectUrl );
    return null;
  }

  public String getUserId( HttpServletRequest request )
  {
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
        userId = ( (Long)clientStateMap.get( "userId" ) ).toString();
      }
    }
    catch( Exception e )
    {
      //
    }
    return userId;
  }

  /**
   * Prepares the display for the userAcls and userRoles.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.DETAILS_FORWARD;

    UserSecurityForm userSecurityForm = (UserSecurityForm)form;

    List userAclsToRemove = buildRemovedUserAcls( userSecurityForm );

    getUserService().removeUserAcls( new Long( userSecurityForm.getUserId() ), userAclsToRemove );

    User user = getUserById( new Long( userSecurityForm.getUserId() ) );
    if ( user != null )
    {
      userSecurityForm.setUserAclList( user.getUserAcls() );
      userSecurityForm.setUserAclListSize( user.getUserAcls().size() );
    }
    request.setAttribute( "user", user );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;

  }

  /**
   * Builds a list of UserAcls to be removed from the user.
   * 
   * @param userSecurityForm
   * @return List
   */
  private List buildRemovedUserAcls( UserSecurityForm userSecurityForm )
  {

    List userAclsToRemove = new ArrayList();

    if ( userSecurityForm.getRemoveUserAcls() != null )
    {

      // build a list of userAcls to be removed from the user.
      List userAclsInfo = ArrayUtil.stringArrayToList( userSecurityForm.getRemoveUserAcls() );

      UserAcl userAcl;

      // parse the userAcl information to build a UserAcl domain.
      for ( Iterator userAclsInfoIter = userAclsInfo.iterator(); userAclsInfoIter.hasNext(); )
      {

        String userAclInfoInString = (String)userAclsInfoIter.next();

        List userAclInformation = ArrayUtil.convertStringTokenizerToList( new StringTokenizer( userAclInfoInString, "||" ) );

        // add the userAcl domain to the list to return.
        Acl acl = new Acl();
        acl.setId( new Long( (String)userAclInformation.get( 0 ) ) );

        userAcl = new UserAcl();
        userAcl.setAcl( acl );
        userAcl.setTarget( (String)userAclInformation.get( 1 ) );
        userAcl.setPermission( (String)userAclInformation.get( 2 ) );
        userAcl.setGuid( (String)userAclInformation.get( 3 ) );

        userAclsToRemove.add( userAcl );

      }
    }

    return userAclsToRemove;

  }

  /**
   * Get the userService from the BeanFactory.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private DashboardReportsService getDashboardReportsService()
  {
    return (DashboardReportsService)getService( DashboardReportsService.BEAN_NAME );
  }

  /**
   * Gets the user by Id through the service.
   * 
   * @param userId
   * @return User
   */
  private User getUserById( Long userId )
  {
    return getUserService().getUserById( userId );
  }

}

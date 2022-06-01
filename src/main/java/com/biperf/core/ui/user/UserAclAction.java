/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserAclAction.java,v $
 */

package com.biperf.core.ui.user;

// import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.user.Acl;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAcl;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AclService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.util.StringUtils;

/**
 * UserAclAction.
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
 * <td>Apr 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAclAction extends BaseDispatchAction
{

  /**
   * Prepares the view for assigning acls to users.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreateUserAcl( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    UserAclForm userAclForm = (UserAclForm)form;

    String forwardTo = ActionConstants.CREATE_FORWARD;
    userAclForm.setMethod( "save" );

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
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    User user = getUserService().getUserById( new Long( userId ) );

    if ( userId != null && !"".equals( userId ) )
    {
      request.setAttribute( "displayNameUserId", userId );
    }

    Acl selectedAcl = null;
    Long aclId = null;
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
      String aclIdString = (String)clientStateMap.get( "aclId" );
      // BugFix 19166 check for Null
      if ( !StringUtils.isEmpty( aclIdString ) )
      {
        aclId = new Long( aclIdString );
      }
      if ( aclId != null )
      {
        selectedAcl = getAclService().getAclById( aclId );
        request.setAttribute( "selectedAcl", selectedAcl );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this is optional
    }

    UserAcl newUserAcl = new UserAcl();
    newUserAcl.setGuid( GuidUtils.generateGuid() );
    newUserAcl.setUser( user );
    newUserAcl.setAcl( selectedAcl );

    userAclForm.load( newUserAcl );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Prepares the view for updating already existing userAcls.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdateUserAcl( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    UserAclForm userAclForm = (UserAclForm)form;

    String forwardTo = ActionConstants.CREATE_FORWARD;
    userAclForm.setMethod( "save" );
    userAclForm.setUpdate( true );

    Long userId = null;
    String guid = null;
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
      guid = (String)clientStateMap.get( "guid" );
      try
      {
        userId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        new Long( (String)clientStateMap.get( "userId" ) );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    User user = getUserService().getUserById( userId );

    List allAcls = getUserService().getAllAcls();

    request.setAttribute( "allAcls", allAcls );
    UserAcl userAcl = getUserAclByUserIdAndGuid( user, guid );
    request.setAttribute( "selectedAcl", userAcl.getAcl() );

    request.setAttribute( "user", user );

    userAclForm.load( userAcl );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Get the acl from the userAcls by the GUID.
   * 
   * @param user
   * @param guid
   * @return UserAcl
   */
  private UserAcl getUserAclByUserIdAndGuid( User user, String guid )
  {

    for ( Iterator userAclIter = user.getUserAcls().iterator(); userAclIter.hasNext(); )
    {
      UserAcl userAcl = (UserAcl)userAclIter.next();
      if ( userAcl.getGuid().equals( guid ) )
      {
        return userAcl;
      }
    }

    return null;
  }

  /**
   * Save the user defined UserAcls.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    UserAclForm userAclForm = (UserAclForm)form;
    UserAcl userAcl = userAclForm.toDomainObject();
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    if ( isCancelled( request ) )
    {
      forwardTo = ActionConstants.CANCEL_FORWARD;
    }
    else
    {
      getUserService().updateUserAcls( userAcl );
    }

    ActionForward forward = mapping.findForward( forwardTo );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userAclForm.getUserId() );
    String returnUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), forward.getPath(), clientStateParameterMap );
    response.sendRedirect( returnUrl );
    return null;
  }

  /**
   * Save the user defined UserAcls.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    UserAclForm userAclForm = (UserAclForm)form;
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ActionForward forward = mapping.findForward( forwardTo );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userAclForm.getUserId() );
    String returnUrl = ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), forward.getPath(), clientStateParameterMap );
    response.sendRedirect( returnUrl );
    return null;
  }

  /**
   * Builds a list of assigned acls from the existing userAcls.
   * 
   * @param user
   * @return List
   */
  /*
   * private List getAssignedAcls( User user ) { List assignedAcls = new ArrayList(); UserAcl
   * userAcl; for (Iterator userAclsIter = user.getUserAcls().iterator(); userAclsIter.hasNext();) {
   * userAcl = (UserAcl)userAclsIter.next(); assignedAcls.add(userAcl.getAcl()); } return
   * assignedAcls; }
   */

  /**
   * Get the userService from the BeanFactory.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  /**
   * Get the aclService from the BeanFactory.
   * 
   * @return AclService
   */
  private AclService getAclService()
  {
    return (AclService)getService( AclService.BEAN_NAME );
  }

}

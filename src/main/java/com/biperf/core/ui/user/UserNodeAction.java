/*
 * (c) 2005 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/UserNodeAction.java,v $
 */

package com.biperf.core.ui.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.FormattedValueBean;

/**
 * UserNodeAction.
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
 * <td>Adam</td>
 * <td>May 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserNodeAction extends BaseDispatchAction
{

  private static final Log logger = LogFactory.getLog( UserNodeAction.class );

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    UserNodeForm userNodeForm = (UserNodeForm)actionForm;
    if ( userNodeForm.getReturnActionMapping() != null && !userNodeForm.getReturnActionMapping().equals( "" ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "nodeId", userNodeForm.getNodeId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, userNodeForm.getReturnActionMapping(), new String[] { queryString } );
    }

    return displayList( actionMapping, actionForm, request, response );
  }

  /**
   * Manages the display of the list of Nodes (for editing) associate to a participant.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DETAILS_FORWARD;

    UserNodeForm userNodeForm = (UserNodeForm)form;

    // Put the user id onto the form
    String userId = "";
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
      userId = (String)clientStateMap.get( "userId" );
      if ( userId == null )
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    userNodeForm.setUserId( userId );

    // Find the Primary user node for the given user
    Long longUserId = new Long( userId );

    UserNode userNode = getUserService().getPrimaryUserNode( longUserId );

    if ( userNode != null )
    {
      userNodeForm.setNodeId( userNode.getNode().getId().toString() );
    }

    request.setAttribute( UserNodeForm.FORM_NAME, userNodeForm );

    request.setAttribute( "userId", userNodeForm.getUserId() );

    User user = getUserById( new Long( userNodeForm.getUserId() ) );
    request.setAttribute( "user", user );

    ActionForward forward = mapping.findForward( forwardTo );

    return forward;
  }

  /**
   * Gets the user from the userService by the userId param.
   * 
   * @param userId
   * @return User
   */
  private User getUserById( Long userId )
  {
    return getUserService().getUserById( userId );
  }

  /**
   * Update UserNode with the data provided through the UserNodeForm.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeUserNodes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    String forwardTo = ActionConstants.SUCCESS_UPDATE;

    UserNodeForm userNodeForm = (UserNodeForm)form;

    if ( isTokenValid( request, true ) )
    {
      if ( userNodeForm.getNodeIds().length > 0 )
      {
        int count = getUserService().getAssignedNodes( new Long( userNodeForm.getUserId() ) ).size();
        if ( count > userNodeForm.getNodeIds().length )
        {
          try
          {
            getUserService().removeUserNodes( Long.valueOf( userNodeForm.getUserId() ), userNodeForm.getNodeIds() );
            getAudienceService().rematchParticipantForAllCriteriaAudiences( Long.valueOf( userNodeForm.getUserId() ) );
          }
          catch( ServiceErrorException e )
          {
            log.debug( e );
            List serviceErrors = e.getServiceErrors();
            ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
          }
        }
        else
        {
          errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( "node.errors.USER_MORE_THAN_ONE_NODE" ) );
        }
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_UPDATE;
    }

    ActionForward forward = mapping.findForward( forwardTo );

    request.setAttribute( "userId", userNodeForm.getUserId() );
    request.setAttribute( "user", getUserById( new Long( userNodeForm.getUserId() ) ) );

    return forward;

  }

  /**
   * Prepares and forwards to the view which updates the role on the user node.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdateUserNodeRole( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    UserNodeForm userNodeForm = (UserNodeForm)form;

    Long userId = null;
    Long nodeId = null;
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
        nodeId = (Long)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        String s = (String)clientStateMap.get( "nodeId" );
        nodeId = new Long( s );
      }
      try
      {
        userId = (Long)clientStateMap.get( "userId" );
      }
      catch( ClassCastException cce )
      {
        String temp = (String)clientStateMap.get( "userId" );
        userId = new Long( temp );
      }

      if ( userId == null || nodeId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId and nodeId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( forwardTo );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Get the userNode identified by the userId and the nodeId
    UserNode userNode = getUserService().getUserNode( userId, nodeId );

    userNodeForm.setMethod( "updateUserNodeRole" );
    userNodeForm.setUserId( String.valueOf( userId.longValue() ) );
    if ( userNode.getHierarchyRoleType() != null )
    {
      userNodeForm.setRole( userNode.getHierarchyRoleType().getCode() );
    }

    request.setAttribute( UserNodeForm.FORM_NAME, userNodeForm );
    request.setAttribute( "userNode", userNode );
    request.setAttribute( "hierarchyTypeValues", HierarchyRoleType.getList() );
    request.setAttribute( "user", getUserById( new Long( userNodeForm.getUserId() ) ) );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Prepares and forwards to the view which creates the role on the user node.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreateUserNodeRole( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    UserNodeForm userNodeForm = (UserNodeForm)form;
    ActionMessages errors = new ActionMessages();
    Long nodeId = null;
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
      userId = (Long)clientStateMap.get( "userId" );
      if ( userId == null || userId.longValue() == 0 )
      {
        List participants = (List)request.getSession().getAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );
        if ( participants != null && !participants.isEmpty() )
        {
          Iterator participantIter = participants.iterator();
          if ( participantIter.hasNext() )
          {
            FormattedValueBean participantBean = (FormattedValueBean)participantIter.next();
            userId = participantBean.getId();
          }
        }
        else
        {
          // if there are no participants in the list, return to the master screen.
          request.getSession().removeAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );
          Map clientStateParameterMap = new HashMap();
          clientStateParameterMap.put( "nodeId", userNodeForm.getNodeId() );
          String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
          return ActionUtils.forwardWithParameters( mapping, userNodeForm.getReturnActionMapping(), new String[] { queryString } );
        }
        request.getSession().removeAttribute( ListBuilderAction.SESSION_RESULT_PARTICIPANT_ID_LIST );
      }
      try
      {
        nodeId = (Long)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        String id = (String)clientStateMap.get( "nodeId" );
        if ( id != null && !id.equals( "" ) )
        {
          nodeId = new Long( id );
        }
      }
      if ( nodeId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "userId and nodeId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( forwardTo );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Get the userNode identified by the userId and the nodeId
    UserNode userNode = getUserService().getUserNode( userId, nodeId );

    userNodeForm.setMethod( "addUserNode" );
    // set the useid to form this is added to fix bug 10757
    userNodeForm.setUserId( userId.toString() );
    request.setAttribute( UserNodeForm.FORM_NAME, userNodeForm );
    request.setAttribute( "userNode", userNode );
    request.setAttribute( "hierarchyTypeValues", HierarchyRoleType.getList() );
    request.setAttribute( "user", getUserById( new Long( userNodeForm.getUserId() ) ) );
    // get the actionForward to display the udpate pages.

    return mapping.findForward( forwardTo );
  }

  /**
   * Updates the role on the user node.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward updateUserNodeRole( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();
    UserNodeForm userNodeForm = (UserNodeForm)form;

    UserNode userNode = null;
    try
    {
      userNode = getUserService().updateUserNodeRole( new Long( userNodeForm.getUserId() ), new Long( userNodeForm.getNodeId() ), HierarchyRoleType.lookup( userNodeForm.getRole() ) );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( new Long( userNodeForm.getUserId() ) );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_UPDATE;
    }

    request.setAttribute( "userNode", userNode );
    userNodeForm.setMethod( "removeUserNodes" );

    request.setAttribute( UserNodeForm.FORM_NAME, userNodeForm );
    request.setAttribute( "userId", userNodeForm.getUserId() );

    if ( userNodeForm.getReturnActionMapping() != null && !userNodeForm.getReturnActionMapping().equals( "" ) )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "nodeId", userNodeForm.getNodeId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, userNodeForm.getReturnActionMapping(), new String[] { queryString } );
    }
    // get the actionForward to display the udpate pages.
    ActionForward forward = mapping.findForward( forwardTo );

    request.setAttribute( "user", getUserById( new Long( userNodeForm.getUserId() ) ) );
    request.setAttribute( "hierarchyTypeValues", HierarchyRoleType.getList() );

    return forward;

  }

  /**
   * Manages the display of the list of Nodes which will potentially be added to the participant.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayListToAdd( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.CREATE_FORWARD;

    UserNodeForm userNodeForm = (UserNodeForm)form;

    Long userId = Long.valueOf( userNodeForm.getUserId() );

    userNodeForm.setMethod( "addUserNode" );
    userNodeForm.setUserId( String.valueOf( userId.longValue() ) );

    request.setAttribute( UserNodeForm.FORM_NAME, userNodeForm );
    request.setAttribute( "userId", userNodeForm.getUserId() );

    request.setAttribute( "user", getUserById( new Long( userNodeForm.getUserId() ) ) );
    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Creates a user node by adding the selected nodes to the user and saving the user.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addUserNode( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_CREATE;
    ActionMessages errors = new ActionMessages();

    UserNodeForm userNodeForm = (UserNodeForm)form;

    Long userId = Long.valueOf( userNodeForm.getUserId() );

    UserNode userNode = new UserNode();
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( userNodeForm.getRole() ) );

    Node node = new Node();
    node.setId( Long.valueOf( userNodeForm.getNodeId() ) );

    userNode.setNode( node );

    Set userNodes = getUserService().getUserNodes( userId );

    // If this is first node added for user then set this node as primary
    if ( userNodes != null && userNodes.size() > 0 )
    {
      userNode.setIsPrimary( false );
    }
    else
    {
      userNode.setIsPrimary( true );
    }

    try
    {
      getUserService().addUserNode( Long.valueOf( userNodeForm.getUserId() ), userNode );
      getAudienceService().rematchParticipantForAllCriteriaAudiences( Long.valueOf( userNodeForm.getUserId() ) );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_CREATE;
    }

    userNodeForm.setMethod( "addUserNodes" );
    userNodeForm.setUserId( String.valueOf( userId.longValue() ) );

    UserNode userPrimaryNode = getUserService().getPrimaryUserNode( userId.longValue() );

    if ( userPrimaryNode != null )
    {
      userNodeForm.setNodeId( userPrimaryNode.getNode().getId().toString() );
    }

    request.setAttribute( UserNodeForm.FORM_NAME, userNodeForm );
    request.setAttribute( "userNodeList", getUserService().getAssignedNodes( Long.valueOf( userNodeForm.getUserId() ) ) );

    // get the actionForward to display the udpate pages.
    ActionForward forward = mapping.findForward( forwardTo );

    // if we want to foward based on returnActionMapping
    // which is provided by the caller, then do following.
    if ( userNodeForm.getReturnActionMapping() != null )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "nodeId", userNodeForm.getNodeId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, userNodeForm.getReturnActionMapping(), new String[] { queryString } );
    }
    request.setAttribute( "user", getUserById( new Long( userNodeForm.getUserId() ) ) );
    return forward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward changePrimary( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    UserNodeForm userNodeForm = (UserNodeForm)form;
    Long userId = Long.valueOf( userNodeForm.getUserId() );
    Long nodeId = Long.valueOf( userNodeForm.getNodeId() );

    try
    {
      getUserService().updatePrimaryNode( userId, nodeId );

    }
    catch( ServiceErrorException se )
    {
      logger.debug( se.toString() );
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", userNodeForm.getUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString } );
  }

  /**
   * Get the nodeService from the beanFactory locator.
   * 
   * @return NodeService
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
}

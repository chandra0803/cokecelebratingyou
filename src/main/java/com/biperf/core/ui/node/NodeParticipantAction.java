/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/node/NodeParticipantAction.java,v $
 */

package com.biperf.core.ui.node;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * NodeParticipantAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class NodeParticipantAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( NodeParticipantAction.class );
  private static final String SESSION_PAX_NODE_REASSIGN_FORM = "sessionPaxNodeReassignForm";

  public ActionForward remove( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    // TODO good refactoring opportunity here with remove - similar functionality in other actions
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_DELETE;

    NodeParticipantListForm form = (NodeParticipantListForm)actionForm;
    String[] deletedIds = form.getDelete();
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );
    try
    {
      getNodeService().removeParticipantsFromNode( new Long( form.getNodeId() ), list );
      getAudienceService().rematchNodeForAllCriteriaAudiences( new Long( form.getNodeId() ), null, null );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_DELETE;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "nodeId", new Long( form.getNodeId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString } );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdateReassignParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    NodeParticipantReassignForm participantNodeReassignForm = (NodeParticipantReassignForm)form;

    ActionMessages errors = new ActionMessages();
    String nodeId = null;
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
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException e )
      {
        nodeId = clientStateMap.get( "nodeId" ).toString();
      }
      if ( nodeId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "nodeId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    participantNodeReassignForm.setOldNodeId( nodeId );

    participantNodeReassignForm.setMethod( "updateReassignParticipants" );

    // get the actionForward to display the create pages.
    String forwardTo = ActionConstants.UPDATE_FORWARD;

    return mapping.findForward( forwardTo );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward updateReassignParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "updateReassignParticipants";

    NodeParticipantReassignForm nodeForm = (NodeParticipantReassignForm)form;

    if ( isCancelled( request ) )
    {
      request.setAttribute( "nodeId", new Long( nodeForm.getOldNodeId() ) );

      nodeForm.setNewNodeId( "" );
      nodeForm.setNewNodeName( "" );
      // EARLY EXIT
      logger.info( "<<< " + METHOD_NAME + " cancelled." );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    Node oldNode = getNodeService().getNodeById( new Long( nodeForm.getOldNodeId() ) );
    Node newNode = getNodeService().getNodeByNameAndHierarchy( nodeForm.getNewNodeName(), oldNode.getHierarchy() );

    // TODO : Should we also do validate if source and destination node for being same ?
    if ( newNode == null )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "node.errors.NAME_NOT_FOUND", nodeForm.getNewNodeName() ) );
    }
    else
    {
      try
      {
        getUserService().updateUserNodeChangeNode( oldNode.getId(), newNode.getId() );
        getAudienceService().rematchNodeForAllCriteriaAudiences( oldNode.getId(), newNode.getId(), null );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }

    ActionForward forward = null;

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    else
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "nodeId", newNode.getId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString } );
    }

    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    NodeParticipantReassignForm participantNodeReassignForm = (NodeParticipantReassignForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PAX_NODE_REASSIGN_FORM, participantNodeReassignForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + NodeSearchAction.RETURN_ACTION_URL_PARAM
        + "=/hierarchy/nodeParticipantReassignDisplay.do?method=returnNodeLookup" );

    return mapping.findForward( ActionConstants.FAIL_UPDATE );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    NodeParticipantReassignForm participantNodeReassignForm = (NodeParticipantReassignForm)form;

    // Get the form back out of the Session to redisplay.
    NodeParticipantReassignForm sessionParticipantNodeReassignForm = (NodeParticipantReassignForm)request.getSession().getAttribute( SESSION_PAX_NODE_REASSIGN_FORM );

    if ( sessionParticipantNodeReassignForm != null )
    {
      try
      {
        BeanUtils.copyProperties( participantNodeReassignForm, sessionParticipantNodeReassignForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    String nodeId = "";
    String nodeName = "";
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
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        Long nodeIdLong = (Long)clientStateMap.get( "nodeId" );
        nodeId = nodeIdLong.toString();
      }
      nodeName = (String)clientStateMap.get( "nodeName" );
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this is an optional parameter
    }

    participantNodeReassignForm.setNewNodeId( nodeId == null ? "" : nodeId );
    participantNodeReassignForm.setNewNodeName( nodeName == null ? "" : nodeName );
    participantNodeReassignForm.setMethod( "updateReassignParticipants" );
    // clean up the session
    request.getSession().removeAttribute( SESSION_PAX_NODE_REASSIGN_FORM );

    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  public ActionForward search( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}

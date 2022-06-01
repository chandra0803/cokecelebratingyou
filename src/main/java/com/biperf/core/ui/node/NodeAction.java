/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/node/NodeAction.java,v $
 */

package com.biperf.core.ui.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * NodeAction.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeAction extends BaseDispatchAction
{
  /**
   * Update with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionMessages errors = new ActionMessages();

    String forwardTo = ActionConstants.SUCCESS_UPDATE;

    NodeForm nodeForm = (NodeForm)form;

    if ( isCancelled( request ) )
    {

      forwardTo = ActionConstants.CANCEL_FORWARD;
      request.setAttribute( "nodeId", new Long( nodeForm.getId() ) );

    }
    else
    {

      if ( isTokenValid( request, true ) )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );
        Node node = getNodeService().getNodeWithAssociationsById( new Long( nodeForm.getId() ), associationRequestCollection );
        Long existingNodeTypeId = node.getNodeType() != null ? node.getNodeType().getId() : null;
        try
        {
          getNodeService().saveNode( nodeForm.toDomainObject( node ), nodeForm.getParentNodeIdAsLong(), true );
          if ( existingNodeTypeId != null && !existingNodeTypeId.equals( nodeForm.getNodeTypeIdAsLong() ) )
          {
            for ( Iterator nodeCharIter = node.getNodeCharacteristics().iterator(); nodeCharIter.hasNext(); )
            {
              NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)nodeCharIter.next();
              getNodeService().deleteNodeCharacteristics( nodeCharacteristic );
            }
          }
          getAudienceService().rematchNodeForAllCriteriaAudiences( node.getId(), null, null );
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
        errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      }

      if ( errors.size() > 0 )
      {
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_UPDATE;
      }
      else
      {
        request.setAttribute( "nodeId", Long.valueOf( nodeForm.getId() ) );
      }

    }

    return mapping.findForward( forwardTo );
  }

  /**
   * Delete the selected node.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_DELETE;
    NodeForm nodeForm = (NodeForm)form;
    ActionMessages errors = new ActionMessages();
    if ( isCancelled( request ) )
    {

      forwardTo = ActionConstants.CANCEL_FORWARD;
      request.setAttribute( "nodeId", new Long( nodeForm.getId() ) );

    }
    else
    {

      if ( isTokenValid( request, true ) )
      {

        NodeService nodeService = getNodeService();

        // Get the id for the deletedNode
        Long deletedNodeId = new Long( nodeForm.getId() );
        Node node = nodeService.getNodeById( deletedNodeId );

        // Get the id for the nodeToHoldChildrenFromDeletedNode
        Long nodeToHoldChildrenId = new Long( nodeForm.getChildrenToNodeId() );

        // Get the id for the nodeToHoldUserFromDeletedNode
        Long nodeToHoldUsersId = new Long( nodeForm.getPaxToNodeId() );

        try
        {
          nodeService.updateNodeAndDelete( deletedNodeId, nodeToHoldChildrenId, nodeToHoldUsersId );
          getAudienceService().rematchNodeForAllCriteriaAudiences( deletedNodeId, nodeToHoldUsersId, nodeToHoldChildrenId );
        }
        catch( ServiceErrorException e )
        {
          log.debug( e );
          List serviceErrors = e.getServiceErrors();
          ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        }

        request.setAttribute( "id", node.getHierarchy().getId() );
        if ( !errors.isEmpty() )
        {
          saveErrors( request, errors );
          forwardTo = ActionConstants.FAIL_DELETE;
        }
        else
        {
          forwardTo = ActionConstants.SUCCESS_DELETE;
        }
      }
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for deleting a node.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayDelete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.DELETE_FORWARD;
    ActionMessages errors = new ActionMessages();
    NodeForm nodeForm = (NodeForm)form;
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
      catch( ClassCastException cce )
      {
        Long nodeIdLong = (Long)clientStateMap.get( "nodeId" );
        nodeId = nodeIdLong.toString();
      }
      if ( nodeId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "nodeId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_DELETE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    NodeService nodeService = getNodeService();

    AssociationRequestCollection nodeChildrenCollection = new AssociationRequestCollection();
    // nodeChildrenCollection.add( new NodeToUserNodesAssociationRequest() );
    nodeChildrenCollection.add( new NodeToNodeCharacteristicAssociationRequest() );

    Node nodeToDelete = nodeService.getNodeWithAssociationsById( new Long( nodeId ), nodeChildrenCollection );
    Long nbrUserNodes = nodeService.getNumberOfUserNodesById( nodeToDelete.getId() );

    List childrenNodes = nodeService.getChildrenNodes( new Long( nodeId ) );

    // Check to see if the node has children OR participants
    if ( nbrUserNodes.intValue() > 0 || childrenNodes.size() > 0 )
    { // YES? Forward to move children/participants to another node within the same hierarchy

      nodeForm.setId( String.valueOf( nodeToDelete.getId() ) );
      nodeForm.setMethod( "delete" );
      request.setAttribute( "nodeToDelete", nodeToDelete );
      request.setAttribute( "childrenNodes", childrenNodes );
      forwardTo = ActionConstants.DELETE_FORWARD;
    }
    else
    { // NO? Logically delete

      // Put the hierarchyId in the request
      request.setAttribute( "id", nodeToDelete.getHierarchy().getId() );

      nodeService.deleteNode( nodeToDelete );
      nodeForm.setMethod( "search" );
      forwardTo = ActionConstants.SUCCESS_DELETE;
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * Prepare the display for updating.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    ActionMessages errors = new ActionMessages();
    NodeForm nodeForm = (NodeForm)form;
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
    Node nodeToUpdate = getNodeService().getNodeById( new Long( nodeId ) );

    nodeForm.load( nodeToUpdate );
    nodeForm.setMethod( "update" );

    request.setAttribute( NodeForm.FORM_NAME, nodeForm );
    request.setAttribute( "excludeNode", nodeToUpdate );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Forward to the displaySearch page.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    NodeForm nodeForm = (NodeForm)form;
    nodeForm.setName( "" );
    nodeForm.setMethod( "search" );

    request.setAttribute( NodeForm.FORM_NAME, nodeForm );

    // get the actionForward to display the create pages.
    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Search with the form provided criteria.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_SEARCH;

    ActionMessages errors = new ActionMessages();

    List nodeList = new ArrayList();

    if ( isTokenValid( request, true ) )
    {
      NodeForm nodeForm = (NodeForm)form;

      nodeList = getNodeService().searchNode( nodeForm.getName(), nodeForm.getDescription(), nodeForm.getDeleted() );

    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_SEARCH;
    }
    else
    {
      request.setAttribute( "nodeSearchResults", nodeList );
    }

    // get the actionForward.
    return mapping.findForward( forwardTo );
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

    // Get the nodeForm and set the appropriate method.
    NodeForm nodeForm = new NodeForm();
    nodeForm.setMethod( "create" );
    String nodeTypeId = RequestUtils.getOptionalParamString( request, "nodeTypeId" );
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

      String parentNodeId = (String)clientStateMap.get( "parentNodeId" );
      if ( nodeTypeId == null )
      {
        nodeTypeId = (String)clientStateMap.get( "nodeTypeId" );
      }
      String hierarchyId = (String)clientStateMap.get( "hierarchyId" );
      if ( parentNodeId == null || parentNodeId.equals( "" ) )
      {
        parentNodeId = "0";
      }
      if ( hierarchyId != null && nodeTypeId != null )
      {
        nodeForm.setParentNodeId( parentNodeId );
        nodeForm.setNodeTypeId( nodeTypeId );
        nodeForm.setHierarchyId( hierarchyId );
      }
      else
      {
        ActionMessages errors = new ActionMessages();
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "hierarchyId and nodeTypeId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    request.setAttribute( NodeForm.FORM_NAME, nodeForm );

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Create the node from the user's form submission.
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

    ActionMessages errors = new ActionMessages();

    NodeForm nodeForm = (NodeForm)form;

    if ( isCancelled( request ) )
    {

      request.setAttribute( "nodeId", new Long( nodeForm.getParentNodeId() ) );
      forwardTo = ActionConstants.CANCEL_FORWARD;

    }
    else
    {

      if ( isTokenValid( request, true ) )
      {
        Node node = nodeForm.toDomainObject( new Node() );

        node.setVersion( null );
        getNodeService().saveNode( node, nodeForm.getParentNodeIdAsLong(), true );

        request.setAttribute( "nodeId", node.getId() );

      }
      else
      {
        errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      }

      if ( errors.size() > 0 )
      {
        saveErrors( request, errors );
        forwardTo = ActionConstants.FAIL_CREATE;
      }

    }

    // get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Get the nodeService from the beanFactory locator.
   * 
   * @return NodeService
   */
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}

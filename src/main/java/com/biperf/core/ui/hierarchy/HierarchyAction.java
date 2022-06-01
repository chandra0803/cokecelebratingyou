/*
 * $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/hierarchy/HierarchyAction.java,v $
 * (c) 2005 BI, Inc. All rights reserved.
 */

package com.biperf.core.ui.hierarchy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.HierarchyNodeType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.node.NodeSearchAction;
import com.biperf.core.ui.utils.HierarchyTreeUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.jenkov.prizetags.tree.impl.Tree;
import com.jenkov.prizetags.tree.impl.TreeNode;
import com.jenkov.prizetags.tree.itf.ITree;
import com.jenkov.prizetags.tree.itf.ITreeNode;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * HierarchyAction.
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
 * <td>kumars</td>
 * <td>Apr 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyAction extends BaseDispatchAction
{
  private static final String SESSION_HIERARCHY_KEY = "SESSION_HIERARCHY_KEY";

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displayList( actionMapping, actionForm, request, response );
  }

  /**
   * displays the hierarchy list
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayList( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * displays the hierarchy details
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String hierarchyId;
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
      if ( RequestUtils.containsAttribute( request, "id" ) )
      {
        hierarchyId = RequestUtils.getRequiredAttributeLong( request, "id" ).toString();
      }
      else
      {
        try
        {
          hierarchyId = (String)clientStateMap.get( "id" );
        }
        catch( ClassCastException cce )
        {
          Long id = (Long)clientStateMap.get( "id" );
          hierarchyId = id.toString();
        }
      }

      if ( hierarchyId != null && hierarchyId.length() > 0 )
      {
        Hierarchy hierarchy = getHierarchyService().getById( new Long( hierarchyId ) );

        request.setAttribute( "hierarchy", hierarchy );

        // get all the nodes for the hierarchy
        List nodeList = getNodeService().getNodesAsHierarchy( hierarchy.getId() );
        Node primaryNode = HierarchyTreeUtils.buildNodeHierarchy( nodeList );
        if ( primaryNode != null )
        {
          hierarchy.addNode( primaryNode );
        }

        // check if the tree hierarchy should be removed from the session...
        long nodeId = RequestUtils.getOptionalParamLong( request, "expand" );
        if ( nodeId == 0 )
        {
          nodeId = RequestUtils.getOptionalParamLong( request, "collapse" );
        }
        if ( nodeId == 0 )
        {
          request.getSession().removeAttribute( "tree.hierarchy" );
        }

        if ( request.getSession().getAttribute( "tree.hierarchy" ) == null )
        {
          if ( hierarchy.getNodes() != null )
          {
            ITree tree = new Tree();

            Iterator nodeIter = hierarchy.getNodes().iterator();
            while ( nodeIter.hasNext() )
            {
              Node node = (Node)nodeIter.next();

              // (node id , node name , node type)
              ITreeNode root = new TreeNode( String.valueOf( node.getId() ), node.getName(), "root" );
              root.setToolTip( ContentReaderManager.getText( node.getNodeType().getCmAssetCode(), node.getNodeType().getNameCmKey() ) );

              HierarchyTreeUtils.addChildren( root, node );
              tree.setRoot( root );
            }
            request.getSession().setAttribute( "tree.hierarchy", tree );
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Creates the hierarchy.
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {

    String forwardTo = ActionConstants.SUCCESS_CREATE;

    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    HierarchyForm hierarchyForm = (HierarchyForm)actionForm;

    if ( isTokenValid( request, true ) )
    {
      Hierarchy hierarchy = hierarchyForm.toDomainObject( (ArrayList)request.getSession().getAttribute( "nodeTypes" ), getNodeTypeService().getDefaultNodeType() );

      try
      {
        getHierarchyService().save( hierarchy );
        // Clean up the session by removing the nodeTypes list.
        request.getSession().removeAttribute( "nodeTypes" );
      }
      catch( NonUniqueDataServiceErrorException e )
      {
        errors.add( ActionConstants.ERROR_SERVICE_EXCEPTION, new ActionMessage( "hierarchy.errors.DUPLICATE_HIERARCHY_NAME" ) );
      }
      catch( ServiceErrorException e )
      {
        errors.add( ActionConstants.ERROR_SERVICE_EXCEPTION, new ActionMessage( "hierarchy.errors.SAVE_ERROR" ) );
      }

    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      hierarchyForm.setMethod( "create" );
    }

    // get the actionForward to display the create pages.
    return actionMapping.findForward( forwardTo );
  }

  /**
   * prepare to add a new hierarchy
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isCancelled( request ) )
    {
      request.getSession().removeAttribute( "nodeTypes" );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    // Nothing to prepare for create...

    // get the actionForward to display the create pages.
    return actionMapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * prepare the hierarchy for an update
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    if ( isCancelled( request ) )
    {
      request.getSession().removeAttribute( "nodeTypes" );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    String hierarchyId = null;
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
        hierarchyId = (String)clientStateMap.get( "id" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "id" );
        hierarchyId = id.toString();
      }
      if ( hierarchyId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "hierarchyId as part of clientState" ) );
        saveErrors( request, errors );
        return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
      }
      Hierarchy hierarchy = getHierarchyService().getById( new Long( hierarchyId ) );

      HierarchyForm hierarchyForm = (HierarchyForm)actionForm;

      hierarchyForm.load( hierarchy );

      // If there are HierarchyNodeTypes for this hierarchy, place the NodeTypes in a list
      // and store that list in the session.
      if ( hierarchy.getHierarchyNodeTypes() != null )
      {
        List nodeTypes = new ArrayList();
        Iterator it = hierarchy.getHierarchyNodeTypes().iterator();
        while ( it.hasNext() )
        {
          HierarchyNodeType hierarchyNodeType = (HierarchyNodeType)it.next();
          NodeType nodeType = hierarchyNodeType.getNodeType();
          nodeTypes.add( nodeType );
        }
        request.getSession().setAttribute( "nodeTypes", nodeTypes );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // get the actionForward to display the update page
    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * remove a hierarchy
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    HierarchyListForm form = (HierarchyListForm)actionForm;

    String[] deleteList = form.getDelete();

    if ( deleteList != null )
    {
      HierarchyService hierarchyService = getHierarchyService();
      for ( int i = 0; i < deleteList.length; i++ )
      {
        String deleteCode = deleteList[i];
        Long hierarchyId = new Long( deleteCode );
        List nodeList = getNodeService().getNodesByHierarchy( hierarchyId );

        // check if there are any nodes for the hierarchy
        if ( nodeList.size() == 0 )
        {
          try
          {
            hierarchyService.delete( getHierarchyService().getById( hierarchyId ) );
          }
          catch( ServiceErrorException e )
          {
            errors.add( ActionConstants.ERROR_SERVICE_EXCEPTION, new ActionMessage( "hierarchy.errors.SAVE_ERROR" ) );
          }
        }
        else
        {
          // report an error to the user
          Hierarchy hierarchy = hierarchyService.getById( hierarchyId );

          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( "hierarchy.errors.HAS_NODE_ATTACHED", CmsResourceBundle.getCmsBundle().getString( hierarchy.getCmAssetCode(), hierarchy.getNameCmKey() ) ) );

        }
      }
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    // get the actionForward to display the create pages.
    return actionMapping.findForward( forwardTo );
  }

  /**
   * add a node type to the hierarchy
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward addNodeType( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isCancelled( request ) )
    {
      request.getSession().removeAttribute( "nodeTypes" );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );

    HierarchyForm form = (HierarchyForm)actionForm;

    // The list of assigned nodeTypes is maintained in the session, get the list and
    // add any selected availableNodeTypes to the list of assigned nodeTypes.
    List nodeTypeList = (ArrayList)request.getSession().getAttribute( "nodeTypes" );
    if ( nodeTypeList == null )
    {
      nodeTypeList = new ArrayList();
    }

    if ( form.getAvailableNodeTypes() != null )
    {
      for ( int i = 0; i < form.getAvailableNodeTypes().length; i++ )
      {
        NodeType newNodeType = getNodeTypeService().getNodeTypeById( new Long( form.getAvailableNodeTypes()[i] ) );
        nodeTypeList.add( newNodeType );
      }
    }

    // Put the list of nodeTypes back in the session.
    request.getSession().setAttribute( "nodeTypes", nodeTypeList );

    return actionForward;
  }

  /**
   * remove a node type from the hierarchy
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward removeNodeType( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    if ( isCancelled( request ) )
    {
      request.getSession().removeAttribute( "nodeTypes" );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();

    HierarchyForm form = (HierarchyForm)actionForm;

    // The list of assigned nodeTypes is maintained in the session, get the list and
    // remove any selected assignedNodeTypes from the list of assigned nodeTypes.
    List nodeTypeList = (ArrayList)request.getSession().getAttribute( "nodeTypes" );
    if ( nodeTypeList == null )
    {
      nodeTypeList = new ArrayList();
    }

    if ( form.getAddedNodeTypes() != null )
    {
      for ( int i = 0; i < form.getAddedNodeTypes().length; i++ )
      {
        NodeType removedNodeType = getNodeTypeService().getNodeTypeById( new Long( form.getAddedNodeTypes()[i] ) );
        List nodeList = getNodeService().getNodesByNodeTypeAndHierarchy( removedNodeType, form.getId() );

        // check if there are any nodes for the node type
        if ( nodeList.size() == 0 )
        {
          nodeTypeList.remove( removedNodeType );
        }
        else
        {
          // report an error to the user
          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( "node_type.errors.HAS_NODE_ATTACHED", CmsResourceBundle.getCmsBundle().getString( removedNodeType.getCmAssetCode(), removedNodeType.getNameCmKey() ) ) );
        }
      }
    }

    // Put the list of nodeTypes back in the session.
    request.getSession().setAttribute( "nodeTypes", nodeTypeList );

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_FORWARD;
    }

    return actionMapping.findForward( forwardTo );
  }

  /**
   * Get the NodeTypeService from the beanFactory locator.
   * 
   * @return NodeTypeService
   */
  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  /**
   * Get the NodeService from the beanFactory locator.
   * 
   * @return NodeService
   */
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  /**
   * Get the HierarchyService from the beanFactory locator.
   * 
   * @return HierarchyService
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  public ActionForward prepareNodeLookup( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
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
      String hierarchyId = null;
      try
      {
        hierarchyId = (String)clientStateMap.get( "hierarchyId" );
      }
      catch( ClassCastException cce )
      {
        hierarchyId = ( (Long)clientStateMap.get( "hierarchyId" ) ).toString();
      }
      if ( hierarchyId != null )
      {
        request.getSession().setAttribute( SESSION_HIERARCHY_KEY, hierarchyId );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this an optional parameters
    }

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + NodeSearchAction.RETURN_ACTION_URL_PARAM + "=/hierarchy/hierarchyDisplay.do?method=returnNodeLookup" );

    return null;
  }

  public ActionForward returnNodeLookup( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String hierarchyId = (String)request.getSession().getAttribute( SESSION_HIERARCHY_KEY );

    // clean up the session
    request.getSession().removeAttribute( SESSION_HIERARCHY_KEY );

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
      String nodeId = null;
      try
      {
        nodeId = (String)clientStateMap.get( "nodeId" );
      }
      catch( ClassCastException cce )
      {
        nodeId = ( (Long)clientStateMap.get( "nodeId" ) ).toString();
      }

      if ( nodeId != null )
      {
        Map paramMapExpand = new HashMap();
        paramMapExpand.put( "hierarchyId", hierarchyId );
        response.sendRedirect( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/hierarchy/viewNode.do", paramMapExpand ) + "&nodeId=" + nodeId );
      }
      else
      {
        Map paramMapExpand = new HashMap();
        paramMapExpand.put( "id", hierarchyId );
        response.sendRedirect( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/hierarchy/hierarchyDisplay.do?method=display", paramMapExpand ) );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this an optional parameters
    }

    return null;
  }

}

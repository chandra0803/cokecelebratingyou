/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/node/NodeViewController.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToHierarchyAssociationRequest;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.hierarchy.NodeToNodeTypeAssociationRequest;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * NodeViewController.
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
 * <td>sharma</td>
 * <td>May 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeViewController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( NodeViewController.class );

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    // get the node details
    String nodeId = null;
    if ( RequestUtils.containsAttribute( request, "nodeId" ) )
    {
      nodeId = RequestUtils.getRequiredAttributeLong( request, "nodeId" ).toString();
    }
    else if ( RequestUtils.containsParam( request, "nodeId" ) )
    {
      nodeId = RequestUtils.getRequiredParamString( request, "nodeId" ).toString();
    }
    if ( nodeId == null )
    {
      try
      {
        String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
        if ( clientState != null && clientState.length() > 0 )
        {
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
        }
        if ( nodeId == null )
        {
          LOG.error( "promotionId not found in client state" );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }
    if ( nodeId != null )
    {
      AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
      nodeAssociationRequestCollection.add( new NodeToNodeTypeAssociationRequest() );
      nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );
      nodeAssociationRequestCollection.add( new NodeToHierarchyAssociationRequest() );
      // nodeAssociationRequestCollection.add( new NodeToUserNodesAssociationRequest() );
      // nodeAssociationRequestCollection.add( new NodeToUsersAssociationRequest() );
      Node node = getNodeService().getNodeWithAssociationsById( new Long( nodeId ), nodeAssociationRequestCollection );

      request.setAttribute( "node", node );

      // get the participant count for this node
      Long nodePaxCount = getNodeService().getNumberOfUserNodesById( node.getId() );
      request.setAttribute( "nodePaxCount", nodePaxCount );

      // get the child node(s) of this node
      AssociationRequestCollection childNodeAssociationRequestCollection = new AssociationRequestCollection();
      childNodeAssociationRequestCollection.add( new NodeToNodeTypeAssociationRequest() );
      List childNodeList = getNodeService().getChildNodesWithAssociationsByParent( new Long( nodeId ), childNodeAssociationRequestCollection );
      request.setAttribute( "childNodeList", childNodeList );

      // Count the number of child nodes this node contains.
      int childNodeCount = 0;
      if ( childNodeList != null )
      {
        childNodeCount = childNodeList.size();
      }
      request.setAttribute( "childNodeCount", new Integer( childNodeCount ) );

      // Get the node type list.
      request.setAttribute( "nodeTypeList", new ArrayList( node.getHierarchy().getHierarchyNodeTypes() ) );
    }
  }

  /**
   * Get the NodeService from the bean factory.
   * 
   * @return NodeService
   * @throws Exception
   */
  private NodeService getNodeService() throws Exception
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }
}

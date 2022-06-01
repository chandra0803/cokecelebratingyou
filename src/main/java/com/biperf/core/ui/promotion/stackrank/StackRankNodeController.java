/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.promotion.StackRank;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.StackRankNodeAssociationRequest;
import com.biperf.core.service.promotion.StackRankNodeService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

/*
 * StackRankNodeController <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 21, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankNodeController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Stack Rank Node page, which is accessed from the "full list" link on the
   * Stack Rank widget on the home page.
   * 
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    StackRankNodeForm form = (StackRankNodeForm)request.getAttribute( "stackRankNodeForm" );
    Long stackRankId = form.getStackRankId();

    // Get the node.
    Node node = getNodeService().getNodeByNameAndHierarchy( form.getNameOfNode(), getHierarchyService().getPrimaryHierarchy() );
    NodeType nodeType = node.getNodeType();

    // Get the current user.
    Long userId = UserManager.getUserId();

    Map visibleStackRankNodes = getStackRankNodeService().getVisibleStackRankNodes( userId, new Long[] { stackRankId }, new Long[] { nodeType.getId() } );

    StackRankNode stackRankNode = getStackRankNode( node, userId, visibleStackRankNodes );

    // Get the stack rank.
    StackRank stackRank = stackRankNode.getStackRank();
    List nodeList = buildNodeList( nodeType, visibleStackRankNodes );

    request.setAttribute( "stackRank", stackRank );

    request.setAttribute( "node", node );

    request.setAttribute( "stackRankNode", stackRankNode );

    request.setAttribute( "nodeList", nodeList );
  }

  private List buildNodeList( NodeType nodeType, Map visibleStackRankNodes )
  {
    List nodeList = new ArrayList();

    for ( Iterator iter = visibleStackRankNodes.keySet().iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      if ( stackRankNode.getNode().getNodeType().equals( nodeType ) )
      {
        nodeList.add( stackRankNode.getNode() );
      }
    }

    return nodeList;
  }

  private StackRankNode getStackRankNode( Node node, Long userId, Map visibleStackRankNodes )
  {
    StackRankNode requestedStackRankNode = null;

    for ( Iterator iter = visibleStackRankNodes.keySet().iterator(); iter.hasNext(); )
    {
      StackRankNode stackRankNode = (StackRankNode)iter.next();
      if ( stackRankNode.getNode().equals( node ) )
      {
        requestedStackRankNode = stackRankNode;
        break;
      }
    }
    if ( requestedStackRankNode == null )
    {
      throw new BeaconRuntimeException( "Possible hack attempt, userid (" + userId + ") trying to access " + "stack rank node listing for node (" + node.getName() + ") not visible to them" );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new StackRankNodeAssociationRequest( StackRankNodeAssociationRequest.ALL_HYDRATION_LEVEL ) );

    // get version hydrated with stack rank pax
    requestedStackRankNode = getStackRankNodeService().getStackRankNode( requestedStackRankNode.getId(), associationRequestCollection );
    return requestedStackRankNode;
  }

  // ---------------------------------------------------------------------------
  // Service Getter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the Hierarchy service.
   * 
   * @return a reference to the Hierarchy service.
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)BeanLocator.getBean( HierarchyService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Node service.
   * 
   * @return a reference to the Node service.
   */
  private NodeService getNodeService()
  {
    return (NodeService)BeanLocator.getBean( NodeService.BEAN_NAME );
  }

  /**
   * Returns a reference to the Stack Rank Node service.
   * 
   * @return a reference to the Stack Rank Node service.
   */
  private StackRankNodeService getStackRankNodeService()
  {
    return (StackRankNodeService)BeanLocator.getBean( StackRankNodeService.BEAN_NAME );
  }

}

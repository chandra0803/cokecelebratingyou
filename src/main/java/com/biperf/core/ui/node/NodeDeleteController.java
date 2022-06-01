/*
 File: NodeDeleteController.java
 (c) 2005 BI, Inc.  All rights reserved.
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      May 26, 2005   1.0      Created
 
 */

package com.biperf.core.ui.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeNameComparator;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * @author crosenquest
 */
public class NodeDeleteController extends BaseController
{

  /**
   * Will build a list of available nodes to which the children nodes and the participants will be
   * moved to when deleted. Overridden from
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

    Node nodeToDelete = (Node)RequestUtils.getAttribute( request, "nodeToDelete" );
    List childrenNodes = (List)RequestUtils.getAttribute( request, "childrenNodes" );
    NodeService nodeService = getNodeService();

    List nodesInTheSameHierarchy = nodeService.getNodesByHierarchy( nodeToDelete.getHierarchy().getId() );

    // Get the list of nodes which are available as parent nodes for the children of the deleted
    // node.
    List availableNodesForChildren = new ArrayList();
    availableNodesForChildren.addAll( nodesInTheSameHierarchy );
    availableNodesForChildren.remove( nodeToDelete );
    availableNodesForChildren.removeAll( childrenNodes );
    Collections.sort( availableNodesForChildren, new NodeNameComparator() );
    request.setAttribute( "availableNodesForChildren", availableNodesForChildren );

    // Get the list of nodes which are available as parent nodes for the participants of the deleted
    // node.
    List availableNodesForParticipants = new ArrayList();
    availableNodesForParticipants.addAll( nodesInTheSameHierarchy );
    availableNodesForParticipants.remove( nodeToDelete );
    Collections.sort( availableNodesForParticipants, new NodeNameComparator() );
    request.setAttribute( "availableNodesForParticipants", availableNodesForParticipants );

  }

  /**
   * Gets the node service through the bean look-up.
   * 
   * @return NodeService
   * @throws Exception
   */
  private NodeService getNodeService() throws Exception
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

}

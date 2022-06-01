/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/node/NodeParticipantReassignController.java,v $
 */

package com.biperf.core.ui.node;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToHierarchyAssociationRequest;
import com.biperf.core.ui.BaseController;

/**
 * NodeParticipantReassignController.
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
 * <td>zahler</td>
 * <td>May 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeParticipantReassignController extends BaseController
{

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
    NodeParticipantReassignForm participantNodeReassignForm = (NodeParticipantReassignForm)request.getAttribute( "nodeParticipantReassignForm" );

    // get the node details
    Long nodeId = new Long( participantNodeReassignForm.getOldNodeId() );

    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToHierarchyAssociationRequest() );
    Node node = getNodeService().getNodeWithAssociationsById( nodeId, nodeAssociationRequestCollection );
    request.setAttribute( "node", node );

    // get the participant count for this node
    // Long nodePaxCount = new Long( 10 ); // stubbed out fo now, till pax-node associatio is worked
    // // out
    // request.setAttribute( "nodePaxCount", nodePaxCount );
    //
    // // get all characteristics of this node.
    // // For now, stubbed it out till this list becomes available
    // List nodeCharacteristicList = new ArrayList();
    //
    // NodeTypeCharacteristicType characteristic = new NodeTypeCharacteristicType();
    // characteristic.setCharacteristicName( "Area Size" );
    // NodeCharacteristic nodeCharacteristic = new NodeCharacteristic();
    // nodeCharacteristic.setNodeTypeCharacteristicType( characteristic );
    // nodeCharacteristic.setCharacteristicValue( "25,000" );
    // nodeCharacteristicList.add( nodeCharacteristic );
    //
    // characteristic = new NodeTypeCharacteristicType();
    // characteristic.setCharacteristicName( "Channel Type" );
    // nodeCharacteristic = new NodeCharacteristic();
    // nodeCharacteristic.setNodeTypeCharacteristicType( characteristic );
    // nodeCharacteristic.setCharacteristicValue( "Retail" );
    // nodeCharacteristicList.add( nodeCharacteristic );
    //
    // request.setAttribute( "nodeCharacteristicList", nodeCharacteristicList );

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

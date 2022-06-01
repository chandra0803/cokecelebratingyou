/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/node/NodeListController.java,v $
 */

package com.biperf.core.ui.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * NodeListController.
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
public class NodeListController extends BaseController
{
  /**
   * Execute method will load a list of Nodes and pass them to the jsp. If this controller can find
   * the "excludeNode" attribute in the request, it will load a list of nodes excluding that node.
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    NodeForm nodeForm = (NodeForm)request.getAttribute( NodeForm.FORM_NAME );

    List nodeList = new ArrayList();

    if ( RequestUtils.containsAttribute( request, "excludeNode" ) )
    {
      Node node = (Node)RequestUtils.getRequiredAttribute( request, "excludeNode" );
      nodeList = getNodeService().getAllExcludingNode( node );
      request.setAttribute( "node", node );
    }
    else
    {
      nodeList = getNodeService().getAll();
    }

    request.setAttribute( "nodeList", nodeList );
    String parentNodeId;
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
        parentNodeId = (String)clientStateMap.get( "parentNodeId" );
      }
      catch( ClassCastException cce )
      {
        parentNodeId = clientStateMap.get( "parentNodeId" ).toString();
      }
      if ( parentNodeId == null )
      {
        parentNodeId = nodeForm.getParentNodeId();
      }
      if ( parentNodeId != null )
      {
        Node parentNode = getNodeService().getNodeById( new Long( parentNodeId ) );
        request.setAttribute( "parentNode", parentNode );
      }

      // Get the nodeTypeId and hierarchyId and manage the request
      String nodeTypeId = null;
      String hierarchyId = null;

      // get nodeTypeId
      try
      {
        nodeTypeId = (String)clientStateMap.get( "nodeTypeId" );
      }
      catch( ClassCastException e )
      {
        nodeTypeId = clientStateMap.get( "nodeTypeId" ).toString();
      }
      if ( nodeTypeId == null )
      {
        nodeTypeId = nodeForm.getNodeTypeId();
      }
      if ( nodeTypeId != null )
      {
        NodeType nodeType = getNodeTypeService().getNodeTypeById( new Long( nodeTypeId ) );
        request.setAttribute( "nodeType", nodeType );
      }
      else
      {
        throw new IllegalArgumentException( "request parameter clientState nodeTypeId was missing" );
      }

      // get hierarchyId
      try
      {
        hierarchyId = (String)clientStateMap.get( "hierarchyId" );
      }
      catch( ClassCastException e )
      {
        hierarchyId = clientStateMap.get( "hierarchyId" ).toString();
      }
      if ( hierarchyId == null )
      {
        hierarchyId = nodeForm.getHierarchyId();
      }
      if ( hierarchyId != null )
      {
        Hierarchy hierarchy = getHierarchyService().getById( new Long( hierarchyId ) );
        request.setAttribute( "hierarchy", hierarchy );
      }
      else
      {
        throw new IllegalArgumentException( "request parameter clientState hierarchyId was missing" );
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Set nodeCharacteristics = new LinkedHashSet();
    if ( nodeForm.getId() != null && !nodeForm.getId().equals( "" ) )
    {
      AssociationRequestCollection nodeChildrenCollection = new AssociationRequestCollection();
      nodeChildrenCollection.add( new NodeToNodeCharacteristicAssociationRequest() );

      Node nodeToUpdate = getNodeService().getNodeWithAssociationsById( new Long( nodeForm.getId() ), nodeChildrenCollection );
      if ( nodeToUpdate != null )
      {
        nodeCharacteristics = nodeToUpdate.getActiveNodeCharacteristics();
      }
    }

    List nodeTypeCharacteristicTypes = getNodeTypeCharacteristicService().getAllNodeTypeCharacteristicTypesByNodeTypeId( new Long( nodeForm.getNodeTypeId() ) );

    List characteristicList = CharacteristicUtils.getNodeCharacteristicValueList( nodeCharacteristics, nodeTypeCharacteristicTypes );

    if ( nodeForm.getNodeTypeCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, nodeForm.getNodeTypeCharacteristicValueList() );
    }
    nodeForm.setNodeTypeCharacteristicValueList( characteristicList );

    // If there is a Dynamic (Dyna) Pick List for any of the Characteristics, that characteristic
    // will have a value for plName. Each of these dyna pick lists needs to be set
    // in the request. This needs to be done for the userCharacteristics Set, and
    // the availableCharacteristics Set.
    Iterator currentIt = nodeCharacteristics.iterator();
    while ( currentIt.hasNext() )
    {
      NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)currentIt.next();
      if ( nodeCharacteristic.getNodeTypeCharacteristicType().getPlName() != null && !nodeCharacteristic.getNodeTypeCharacteristicType().getPlName().equals( "" ) )
      {
        request.setAttribute( nodeCharacteristic.getNodeTypeCharacteristicType().getPlName(), DynaPickListType.getList( nodeCharacteristic.getNodeTypeCharacteristicType().getPlName() ) );
      }
    }

    // This is a little different than the above Iteration because this iteration is
    // dealing with Characteristic Objects rather than UserCharacteristic Objects
    Iterator availableIt = nodeTypeCharacteristicTypes.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) );
      }
    }

    request.setAttribute( NodeForm.FORM_NAME, nodeForm );

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

  /**
   * Get the HierarchyService from the beanFactory.
   * 
   * @return HierarchyService
   * @throws Exception
   */
  private HierarchyService getHierarchyService() throws Exception
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  /**
   * Get the nodeTypeService from the beanLocator.
   * 
   * @return NodeTypeService
   * @throws Exception
   */
  private NodeTypeService getNodeTypeService() throws Exception
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  /**
   * @return NodeTypeCharacteristicService
   * @throws Exception
   */
  private NodeTypeCharacteristicService getNodeTypeCharacteristicService() throws Exception
  {
    return (NodeTypeCharacteristicService)getService( NodeTypeCharacteristicService.BEAN_NAME );
  }
}

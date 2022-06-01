/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/node/NodeParticipantListController.java,v $
 */

package com.biperf.core.ui.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToUsersAssociationRequest;
import com.biperf.core.ui.BaseController;

/**
 * NodeParticipantListController.
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
 * <td>tennant</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeParticipantListController extends BaseController
{
  private static final Log log = LogFactory.getLog( NodeParticipantListController.class );

  /**
   * Tiles controller for the Node Participant List page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    log.debug( "NodeParticipantListController.execute" );
    NodeParticipantListForm form = (NodeParticipantListForm)request.getAttribute( "nodeParticipantListForm" );
    Long nodeId = new Long( form.getNodeId() );
    String lastName = form.getLastName();
    List users = new ArrayList();

    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();

    nodeAssociationRequestCollection.add( new NodeToUsersAssociationRequest() );
    Node node = getNodeService().getNodeWithAssociationsById( nodeId, nodeAssociationRequestCollection );

    String searchString = "(?i).*" + lastName + ".*";
    List activeUserList = node.getNodeUsersList();
    Map nodeParticpantRoles = new HashMap();
    for ( Iterator iterator = activeUserList.iterator(); iterator.hasNext(); )
    {
      User user = (User)iterator.next();
      if ( lastName != null )
      {
        if ( user.getLastName().matches( searchString ) )
        {
          users.add( user );
        }
      }
      else
      {
        users.add( user );
      }
      HierarchyRoleType hierarchyRoleType = user.getUserNodeByNodeId( node.getId() ).getHierarchyRoleType();
      nodeParticpantRoles.put( user.getId(), hierarchyRoleType );
    }

    Collections.sort( users, new Comparator()
    {
      public int compare( Object o1, Object o2 )
      {
        User user1 = (User)o1;
        User user2 = (User)o2;

        return user1.getNameLFMWithComma().compareTo( user2.getNameLFMWithComma() );
      }
    } );

    request.setAttribute( "nodeParticipantList", users );
    request.setAttribute( "nodeParticpantRoles", nodeParticpantRoles );
    request.setAttribute( "node", node );
    request.setAttribute( "hierarchy", node.getHierarchy() );
  }

  private NodeService getNodeService() throws Exception
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }
}

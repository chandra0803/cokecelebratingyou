/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/hierarchy/HierarchyController.java,v $
 */

package com.biperf.core.ui.hierarchy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.ui.BaseController;

/**
 * HierarchyController.
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
 * <td>May 03, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyController extends BaseController
{

  private static final Log logger = LogFactory.getLog( HierarchyController.class );

  /**
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
    List<NodeType> allNodeTypes = getNodeTypeService().getAll();

    // The list of assigned node types is created and maintained in the session by the
    // HierarchyAction.class.
    List assignedNodeTypes = (ArrayList)request.getSession().getAttribute( "nodeTypes" );

    // If there are assignedNodeTypes, remove them from the list of allNodeTypes.
    if ( assignedNodeTypes != null )
    {
      allNodeTypes.removeAll( assignedNodeTypes );
    }

    List filteredNodeTypes = allNodeTypes;
    for ( NodeType nodeType : allNodeTypes )
    {
      if ( nodeType.isDefaultNodeType() )
      {
        filteredNodeTypes.remove( nodeType );
        break;
      }
    }

    // Put the list of allNodeTypes into the request.
    request.setAttribute( "allNodeTypes", filteredNodeTypes );

    logger.debug( "allNodeTypes: " + request.getAttribute( "allNodeTypes" ) );

    // Count the number of hierarchy node types associated with the hierarchy.
    int hierarchyNodeTypeCount = 0;
    Hierarchy hierarchy = (Hierarchy)request.getAttribute( "hierarchy" );
    if ( hierarchy != null )
    {
      hierarchyNodeTypeCount = hierarchy.getHierarchyNodeTypes().size();
    }
    request.setAttribute( "hierarchyNodeTypeCount", new Integer( hierarchyNodeTypeCount ) );
  }

  private NodeTypeService getNodeTypeService() throws Exception
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

}

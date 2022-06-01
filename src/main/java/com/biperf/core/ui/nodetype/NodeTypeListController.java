/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/nodetype/NodeTypeListController.java,v $
 */

package com.biperf.core.ui.nodetype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.ui.BaseController;

/**
 * NodeTypeListController.
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
 * <td>Apr 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeTypeListController extends BaseController
{
  /**
   * Tiles controller for the Node Type List page
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
    request.setAttribute( "nodeTypeList", getNodeTypeService().getAll() );

    List allCharList = getNodeTypeCharacteristicService().getAllCharacteristics();
    Map charMap = new HashMap();
    for ( Iterator iter = allCharList.iterator(); iter.hasNext(); )
    {
      NodeTypeCharacteristicType characteristic = (NodeTypeCharacteristicType)iter.next();
      Long nodeTypeId = characteristic.getDomainId();
      List charList = (List)charMap.get( nodeTypeId );
      if ( charList == null )
      {
        charList = new ArrayList();
        charMap.put( nodeTypeId, charList );
      }
      charList.add( characteristic );
    }

    request.setAttribute( "characteristicMap", charMap );
  }

  private NodeTypeService getNodeTypeService() throws Exception
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  /**
   * @return NodeTypeCharacteristicService
   */
  private NodeTypeCharacteristicService getNodeTypeCharacteristicService()
  {
    return (NodeTypeCharacteristicService)getService( NodeTypeCharacteristicService.BEAN_NAME );
  }
}

/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/node/NodeSearchAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.node;

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
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * NodeSearchAction.
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
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeSearchAction extends BaseDispatchAction
{
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return displaySearch( actionMapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // default
    return displaySearch( mapping, form, request, response );

  }

  /**
   * Displays the NodeLookup Search Page.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearch( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    NodeSearchForm nodeSearchForm = (NodeSearchForm)form;

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
      nodeId = (String)clientStateMap.get( "nodeId" );
      Iterator entries = clientStateMap.entrySet().iterator();
      while ( entries.hasNext() )
      {
        Map.Entry entry = (Map.Entry)entries.next();
        nodeSearchForm.setParams( (String)entry.getKey(), entry.getValue() );
      }
    }
    catch( IllegalArgumentException e )
    {
      // do nothing since this isn't a required parameter
    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this isn't a required parameter
    }
    nodeSearchForm.setNodeId( nodeId );
    nodeSearchForm.setNameOfNode( RequestUtils.getOptionalParamString( request, "nodeName" ) );
    nodeSearchForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Displays the NodeLookup Search Page. Selects the primary hiearchy and disables the rest of the
   * hierarchy drop down.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displaySearchWithinPrimary( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    NodeSearchForm nodeSearchForm = (NodeSearchForm)form;
    nodeSearchForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );
    nodeSearchForm.setNodeSearchType( RequestUtils.getOptionalParamString( request, "nodeSearchType" ) );

    // get the primary hierarchy and set its id to the form so it will be selected on the display
    HierarchyService hierarchyService = (HierarchyService)getService( HierarchyService.BEAN_NAME );
    Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
    nodeSearchForm.setHierarchyId( primaryHierarchy.getId().longValue() );

    // let the JSP know to disable the select list of hiearchies
    nodeSearchForm.setHierarchyListDisabled( true );

    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  /**
   * Perform a Node lookup based on HierarchyId, PartialNodeName.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    NodeSearchForm nodeSearchForm = (NodeSearchForm)form;

    long hierarchyId = nodeSearchForm.getHierarchyId();
    String partialNodeName = nodeSearchForm.getNameOfNode();
    long nodeType = nodeSearchForm.getNodeTypeId();

    ActionMessages errors = new ActionMessages();

    try
    {
      List nodeList = getNodeService().searchNode( new Long( hierarchyId ), partialNodeName, new Long( nodeType ) );
      request.setAttribute( "nodeList", nodeList );
    }
    catch( Exception e )
    {
      errors.add( "errorMessage", new ActionMessage( "node.errors.SEARCH_FAILED" ) );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
    }

    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
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

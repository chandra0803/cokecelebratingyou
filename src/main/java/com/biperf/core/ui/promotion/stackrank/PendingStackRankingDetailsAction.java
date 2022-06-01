/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion.stackrank;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.node.NodeSearchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * PendingStackRankingDetailsAction.
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
 * <td>gaddam</td>
 * <td>Mar 10, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PendingStackRankingDetailsAction extends BaseDispatchAction
{
  /** Log */
  private static final Log LOG = LogFactory.getLog( PendingStackRankingDetailsAction.class );

  private static final String SESSION_PENDING_STACKRANK_FORM = "sessionPendingStackRankForm";

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PendingStackRankingDetailsForm pendingStackRankForm = (PendingStackRankingDetailsForm)form;

    pendingStackRankForm.setMethod( "display" );
    String promotionId = "";
    String stackRankId = "";
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
        promotionId = (String)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        promotionId = ( (Long)clientStateMap.get( "promotionId" ) ).toString();
      }
      try
      {
        stackRankId = (String)clientStateMap.get( "stackRankId" );
      }
      catch( ClassCastException cce )
      {
        stackRankId = ( (Long)clientStateMap.get( "stackRankId" ) ).toString();
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as these are optional parameters
    }
    pendingStackRankForm.setPromotionId( promotionId == null ? "" : promotionId );
    pendingStackRankForm.setStackRankId( stackRankId == null ? "" : stackRankId );

    pendingStackRankForm.setNodeType( RequestUtils.containsParam( request, "nodeType" ) ? RequestUtils.getRequiredParamString( request, "nodeType" ) : "" );

    // get the actionForward to display the detail page.
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showDetails( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // see whether the user has typed a node name, if yes then get the nodeId
    PendingStackRankingDetailsForm pendingStackRankForm = (PendingStackRankingDetailsForm)form;
    if ( pendingStackRankForm.getNodeName().length() > 0 )
    {
      Node node = null;

      // means, user has typed a node name, he didn't look up the node
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      node = getNodeService().getNodeByNameAndHierarchy( pendingStackRankForm.getNodeName(), primaryHierarchy );

      // if the node is not found then exit
      if ( node == null )
      {
        ActionMessages errors = new ActionMessages();
        // Create a valid error message for not retrieving a node
        errors.add( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, new ActionMessage( "node.errors.SEARCH_FAILED" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }

      pendingStackRankForm.setNodeId( node.getId().toString() );

    }
    // get the actionForward to display the detail page.
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward prepareNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    PendingStackRankingDetailsForm stackRankForm = (PendingStackRankingDetailsForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_PENDING_STACKRANK_FORM, stackRankForm );

    response.sendRedirect( RequestUtils.getBaseURI( request ) + "/hierarchy/nodeLookup.do?" + "method=displaySearchWithinPrimary&" + NodeSearchAction.RETURN_ACTION_URL_PARAM
        + "=/promotion/pendingStackRankingDetails.do?method=returnNodeLookup" );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward returnNodeLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PendingStackRankingDetailsForm stackRankForm = (PendingStackRankingDetailsForm)form;

    // Get the form back out of the Session to redisplay.
    PendingStackRankingDetailsForm sessionUserForm = (PendingStackRankingDetailsForm)request.getSession().getAttribute( SESSION_PENDING_STACKRANK_FORM );

    if ( sessionUserForm != null )
    {
      try
      {
        BeanUtils.copyProperties( stackRankForm, sessionUserForm );
      }
      catch( Exception e )
      {
        LOG.info( "Copy Properties failed." );
      }
    }

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
      String nodeId = (String)clientStateMap.get( "nodeId" );
      if ( nodeId != null )
      {
        stackRankForm.setNodeId( nodeId );
      }
    }
    catch( InvalidClientStateException e )
    {
      // do nothing as this was an optional parameter
    }
    stackRankForm.setNodeName( RequestUtils.getOptionalParamString( request, "nodeName" ) );
    // copy the nodeName to prevNodeName as well
    stackRankForm.setPreNodeName( stackRankForm.getNodeName() );
    // set null to existing dates, if any
    stackRankForm.setRankingPeriodFrom( null );
    stackRankForm.setRankingPeriodTo( null );

    // clean up the session
    request.getSession().removeAttribute( SESSION_PENDING_STACKRANK_FORM );

    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  /**
   * Returns a reference to the NodeService service.
   * 
   * @return a reference to the NodeService service.
   */
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  /**
   * Returns a reference to the HierarchyService service.
   * 
   * @return a reference to the HierarchyService service.
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

}

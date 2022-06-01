/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/nodetype/NodeTypeAction.java,v $
 */

package com.biperf.core.ui.nodetype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;

/**
 * NodeTypeAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeTypeAction extends BaseDispatchAction
{
  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.debug( "NodeTypeAction.create" );

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }
    String forward = ActionConstants.SUCCESS_CREATE;

    NodeTypeForm form = (NodeTypeForm)actionForm;
    NodeType nodeType = form.toBusinessObject();
    try
    {
      nodeType = getNodeTypeService().saveNodeType( nodeType );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_CREATE;
      return mapping.findForward( forward );
    }

    String queryString1 = "nodeTypeName=" + nodeType.getI18nName();
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "domainId", nodeType.getId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, queryString1 } );
  }

  /**
   * Updates a NodeType record
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.debug( "NodeTypeAction.update" );
    ActionMessages errors = new ActionMessages();

    if ( isCancelled( request ) )
    {
      return mapping.findForward( ActionConstants.CANCEL_FORWARD ); // EARLY EXIT
    }

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_UPDATE;

    NodeTypeForm form = (NodeTypeForm)actionForm;
    NodeType nodeType = form.toBusinessObject();
    try
    {
      getNodeTypeService().saveNodeType( nodeType );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_UPDATE;
    }

    return mapping.findForward( forward );
  }

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.debug( "NodeTypeAction.prepareCreate" );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.debug( ">>> prepareUpdate " );
    NodeTypeForm form = (NodeTypeForm)actionForm;
    Long id = new Long( form.getId() );
    NodeType nodeType = getNodeTypeService().getNodeTypeById( id );
    form.load( nodeType );
    log.debug( "<<< prepareUpdate " );
    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * Removes (logically deletes) a NodeType
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    log.debug( ">> NodeTypeAction.remove" );
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_DELETE;

    NodeTypeListForm form = (NodeTypeListForm)actionForm;
    String[] deletedIds = form.getDelete();
    if ( deletedIds != null )
    {

      log.debug( "deletedIds " + deletedIds.length );
      List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );
      try
      {
        getNodeTypeService().deleteNodeTypes( list );
      }
      catch( ServiceErrorException e )
      {
        log.debug( e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
        forward = ActionConstants.FAIL_DELETE;
      }
    }

    return mapping.findForward( forward );
  }

  /**
   * Get the nodeTypeService from the bean factory.
   * 
   * @return EmployerService
   */
  private NodeTypeService getNodeTypeService()
  {

    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );

  }
}

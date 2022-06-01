/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepElementAction.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ClaimFormAddEditAction.
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
 * <td>Adam</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepElementAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ClaimFormStepElementAction.class );

  /**
   * Selects the action to forward or chain through for the ElementType.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward selectElementTypeView( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ClaimFormStepElementForm claimFormStepElementForm = (ClaimFormStepElementForm)form;

    String forwardAction = "";

    claimFormStepElementForm.setClaimFormStepElementTypeDesc( ClaimFormElementType.lookup( claimFormStepElementForm.getClaimFormStepElementTypeCode() ).getName() );

    forwardAction = "update";
    return mapping.findForward( forwardAction );
  }

  /**
   * Display ClaimForm
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end display

  /**
   * Creates a new claim form
   * 
   * @param mapping ActionMapping
   * @param form ActionForm
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "create";
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    ClaimFormStepElementForm elementForm = (ClaimFormStepElementForm)form;
    String forwardAction = "";

    try
    {
      ClaimFormStepElement claimFormStepElement = elementForm.toDomainObject();
      getClaimFormDefinitionService().addClaimFormStepElement( elementForm.getClaimFormId(), elementForm.getClaimFormStepId(), claimFormStepElement, elementForm.getCmData() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_CREATE;
    }
    else
    {
      forwardAction = ActionConstants.SUCCESS_CREATE;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormId", elementForm.getClaimFormId() );
    clientStateParameterMap.put( "claimFormStepId", elementForm.getClaimFormStepId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=display" } );

  } // end create

  /**
   * Display ClaimForm
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forward = ActionConstants.UPDATE_FORWARD;

    String claimFormElementId = null;
    String claimFormId = null;
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
        claimFormElementId = (String)clientStateMap.get( "claimFormStepElementId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "claimFormStepElementId" );
        claimFormElementId = id.toString();
      }
      try
      {
        claimFormId = (String)clientStateMap.get( "claimFormId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "claimFormId" );
        claimFormId = id.toString();
      }
      if ( claimFormId == null || claimFormElementId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId and claimFormElementId as part of clientState" ) );
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    ClaimFormStepElement claimFormStepElement = getClaimFormDefinitionService().getClaimFormStepElementById( new Long( claimFormElementId ) );
    ClaimForm claimForm = getClaimFormDefinitionService().getClaimFormById( new Long( claimFormId ) );

    if ( claimFormStepElement.getCustomerInformationBlockId() != null )
    {
      forward = "cib";
    }
    else
    {
      ClaimFormStepElementForm elementForm = (ClaimFormStepElementForm)form;
      elementForm.load( claimFormStepElement );
      elementForm.setCmData( getClaimFormDefinitionService().getClaimFormStepElementCMDataHolder( claimForm.getCmAssetCode(), claimFormStepElement ) );
    }

    return mapping.findForward( forward );
  } // end display

  /**
   * Update ClaimFormStepElement.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    String forward = ActionConstants.SUCCESS_UPDATE;

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT
    }
    String claimFormStepId = null;
    String claimFormId = null;
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
      claimFormStepId = (String)clientStateMap.get( "claimFormStepId" );
      claimFormId = (String)clientStateMap.get( "claimFormId" );
      if ( claimFormId == null || claimFormStepId == null )
      {
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    ClaimFormStepElementForm elementForm = (ClaimFormStepElementForm)form;

    try
    {
      getClaimFormDefinitionService().updateClaimFormStepElement( new Long( claimFormId ), new Long( claimFormStepId ), elementForm.toDomainObject(), elementForm.getCmData() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId and claimFormStepId as part of clientState" ) );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_UPDATE;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormId", elementForm.getClaimFormId() );
    clientStateParameterMap.put( "claimFormStepId", elementForm.getClaimFormStepId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=display" } );

  }

  /**
   * Retrieves a ClaimFormDefinitionService
   * 
   * @return ClaimFormDefinitionService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  } // end getClaimFormDefinitionService

}

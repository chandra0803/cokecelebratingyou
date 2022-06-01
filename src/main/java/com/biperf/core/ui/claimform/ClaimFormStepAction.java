/*
 File: ClaimFormStepAction.java
 (c) 2005 BI, Inc.  All rights reserved.
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      Jun 3, 2005   1.0      Created
 
 */

package com.biperf.core.ui.claimform;

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

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.PresentationUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * @author crosenquest
 */
public class ClaimFormStepAction extends BaseDispatchAction
{

  /**
   * Prepare the display for creating a new ClaimFormStep.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages actionErrors = new ActionMessages();
    String forwardTo = ActionConstants.CREATE_FORWARD;

    // Get the ClaimFormStepForm and set it up to create a new ClaimFormStep.
    ClaimFormStepForm claimFormStepForm = new ClaimFormStepForm();
    claimFormStepForm.setMethod( "create" );

    // Get the claimFormId from the request
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
      claimFormId = (String)clientStateMap.get( "claimFormId" );
      if ( claimFormId == null )
      {
        actionErrors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId as part of clientState" ) );
        saveErrors( request, actionErrors );
        return mapping.findForward( ActionConstants.FAIL_CREATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Create a new claimForm and set the id.
    ClaimForm claimForm = new ClaimForm();
    claimForm.setId( new Long( claimFormId ) );

    // Put the claimForm onto the claimFormStep to be loaded onto the ClaimFormStepForm
    ClaimFormStep claimFormStep = new ClaimFormStep();
    claimFormStep.setClaimForm( claimForm );

    // Load the claimFormStepForm
    claimFormStepForm.load( claimFormStep );

    request.setAttribute( "claimFormStep", claimFormStep );

    request.setAttribute( ClaimFormStepForm.FORM_NAME, claimFormStepForm );

    // Get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Create the claimFormStep from the user's form submission.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages actionErrors = new ActionMessages();

    // Get the ClaimFormStepForm and get the ClaimFormStep from that form.
    ClaimFormStepForm claimFormStepForm = (ClaimFormStepForm)form;
    ClaimFormStep claimFormStep = claimFormStepForm.toDomain();
    Long claimFormId = claimFormStepForm.getClaimFormId();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );
    ClaimForm claimForm = getClaimFormDefinitionService().getClaimFormByIdWithAssociations( claimFormId, associationRequestCollection );
    if ( claimForm.getClaimFormModuleType().isSsi() & claimForm.getClaimFormSteps().size() == 1 )
    {
      actionErrors.add( "errorMessage", new ActionMessage( "promotion.ssi.activitysubmission.ONLY_ONE_CLAIM_STEP" ) );
    }
    else
    {
      try
      {
        // Save the claimFormStep for the ClaimForm.
        getClaimFormDefinitionService().saveClaimFormStep( claimFormId, claimFormStep, claimFormStepForm.getName() );
      }
      catch( ServiceErrorException see )
      {
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( see.getServiceErrors(), actionErrors );
      }
    }
    ActionForward forward = null;
    // Manage the errors
    if ( actionErrors.size() > 0 )
    {
      saveErrors( request, actionErrors );
      forward = mapping.findForward( ActionConstants.FAIL_CREATE );
      request.setAttribute( ClaimFormStepForm.FORM_NAME, claimFormStepForm );
    }
    else
    {
      request.setAttribute( "claimFormId", claimFormId );
      request.setAttribute( "claimFormStepId", claimFormStep.getId() );

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimFormStepId", claimFormStep.getId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      forward = ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString } );
    }

    // Get the actionForward to display the create pages.
    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward reorderStep( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    /*
     * if ( !isTokenValid( request, true ) ) { errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new
     * ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) ); saveErrors( request, errors );
     * return mapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY EXIT }
     */
    String forward = ActionConstants.SUCCESS_UPDATE;

    ClaimFormStepViewForm claimFormStepViewForm = (ClaimFormStepViewForm)form;

    Long claimFormStepId = new Long( claimFormStepViewForm.getClaimFormStepId() );
    Long claimFormStepElementId = new Long( claimFormStepViewForm.getClaimFormStepElementId() );
    int newSequenceNum = Integer.parseInt( claimFormStepViewForm.getNewElementSequenceNum() );

    if ( newSequenceNum < 0 )
    {
      newSequenceNum = 0;
    }

    getClaimFormDefinitionService().reorderClaimFormStepElement( claimFormStepId, claimFormStepElementId, newSequenceNum );

    return mapping.findForward( forward );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_DELETE;

    ClaimFormStepViewForm claimFormStepViewForm = (ClaimFormStepViewForm)form;
    String[] deletedIds = claimFormStepViewForm.getDelete();
    try
    {
      log.debug( "deletedIds " + deletedIds.length );
      List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );
      Long claimFormId = new Long( claimFormStepViewForm.getClaimFormId() );
      Long claimFormStepId = new Long( claimFormStepViewForm.getClaimFormStepId() );

      getClaimFormDefinitionService().deleteClaimFormStepElements( claimFormId, claimFormStepId, list );
    }
    catch( NullPointerException e )
    {
      errors.add( "errorMessage", new ActionMessage( "claims.form.step.view.NOTHING_TO_REMOVE" ) );
      saveErrors( request, errors );
    }
    return mapping.findForward( forward );
  }

  /**
   * Update the claimFormStep from the user's form submission.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages actionErrors = new ActionMessages();
    String forwardTo = ActionConstants.SUCCESS_UPDATE;

    // Get the ClaimFormStepForm and get the ClaimFormStep from that form.
    ClaimFormStepForm claimFormStepForm = (ClaimFormStepForm)form;
    ClaimFormStep claimFormStep = claimFormStepForm.toDomain();

    Long claimFormId = claimFormStepForm.getClaimFormId();

    try
    {

      // Update the claimFormStep.
      getClaimFormDefinitionService().saveClaimFormStep( claimFormId, claimFormStep, claimFormStepForm.getName() );

    }
    catch( ServiceErrorException see )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( see.getServiceErrors(), actionErrors );
    }

    // Manage the errors
    if ( actionErrors.size() > 0 )
    {

      saveErrors( request, actionErrors );
      forwardTo = ActionConstants.FAIL_UPDATE;
      request.setAttribute( ClaimFormStepForm.FORM_NAME, claimFormStepForm );

    }
    else
    {
      request.setAttribute( "claimFormId", claimFormId );
      request.setAttribute( "claimFormStepId", claimFormStep.getId() );
    }

    // Get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Get the claimFormDefinitionService from the beanFactory.
   * 
   * @return ClaimFormDefinitionService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  /**
   * Prepare the display for updating an existing ClaimFormStep.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages actionErrors = new ActionMessages();
    String forwardTo = ActionConstants.UPDATE_FORWARD;

    // Get the ClaimFormStepForm and set it up to create a new ClaimFormStep.
    ClaimFormStepForm claimFormStepForm = new ClaimFormStepForm();
    claimFormStepForm.setMethod( "update" );

    // Get the claimFormStepId from the request
    Long claimFormStepId = null;
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
        claimFormStepId = (Long)clientStateMap.get( "claimFormStepId" );
      }
      catch( ClassCastException cce )
      {
        String s = (String)clientStateMap.get( "claimFormStepId" );
        claimFormStepId = new Long( s );
      }

      if ( claimFormStepId == null )
      {
        actionErrors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormStepId as part of clientState" ) );
        saveErrors( request, actionErrors );
        return mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }

    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // Get the claimFormStep from the database and populate the form
    ClaimFormStep claimFormStep = getClaimFormDefinitionService().getClaimFormStep( claimFormStepId );

    // Load the claimFormStepForm
    claimFormStepForm.load( claimFormStep );

    request.setAttribute( "claimFormStep", claimFormStep );
    request.setAttribute( "claimFormId", claimFormStep.getClaimForm().getId() );

    request.setAttribute( ClaimFormStepForm.FORM_NAME, claimFormStepForm );

    // Get the actionForward to display the create pages.
    return mapping.findForward( forwardTo );
  }

}

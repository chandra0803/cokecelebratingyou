/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormAction.java,v $
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
import com.biperf.core.domain.enums.ClaimFormStatusType;
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
 * ClaimFormAction.
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
 * <td>robinsra</td>
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ClaimFormAction.class );

  /**
   * Prepares anything necessary before displaying the create screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    // get the actionForward to display the create pages.
    claimFormForm.setMethod( "create" );
    return mapping.findForward( ActionConstants.CREATE_FORWARD );
  } // end prepareCreate

  /**
   * Creates a new claim form
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "create";

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    ClaimFormForm claimFormForm = (ClaimFormForm)actionForm;
    String forwardAction = "";

    try
    {
      ClaimForm claimForm = getClaimFormDefinitionService().saveClaimForm( claimFormForm.toInsertedDomainObject(), true );
      claimFormForm.setClaimFormId( claimForm.getId().longValue() );
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
    clientStateParameterMap.put( "claimFormId", new Long( claimFormForm.getClaimFormId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );

  } // end create

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    ActionMessages errors = new ActionMessages();
    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    Long claimFormId = new Long( 0 );

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
        String claimFormIdString = (String)clientStateMap.get( "claimFormId" );
        claimFormId = new Long( claimFormIdString );
      }
      catch( ClassCastException cce )
      {
        claimFormId = (Long)clientStateMap.get( "claimFormId" );
      }

      if ( claimFormId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId as part of clientState" ) );
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    ClaimForm claimFormToUpdate = getClaimFormDefinitionService().getClaimFormById( claimFormId );

    claimFormForm.load( claimFormToUpdate );
    claimFormForm.setMethod( "update" );

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  } // end prepareUpdate

  /**
   * Prepares anything necessary before displaying the preview form screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward preparePreview( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();

    // -------------------------
    // Get the claimFormStepId
    // ------------------------
    Long claimFormStepId = null;
    Long claimFormId = null;
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

      if ( RequestUtils.containsAttribute( request, "claimFormStepId" ) )
      {
        claimFormStepId = RequestUtils.getRequiredAttributeLong( request, "claimFormStepId" );
        claimFormId = RequestUtils.getRequiredAttributeLong( request, "claimFormId" );
      }
      else
      {
        claimFormStepId = Long.valueOf( clientStateMap.get( "claimFormStepId" ).toString() );
        claimFormId = Long.valueOf( clientStateMap.get( "claimFormId" ).toString() );
      }
      if ( claimFormStepId == null || claimFormId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId and claimFormStepId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // -------------------------------------------------------------
    // Validate that this claim form step Id is ready to preview
    // -------------------------------------------------------------
    try
    {
      getClaimFormDefinitionService().preparePreviewClaimForm( claimFormStepId );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_ERRORS_ONLY );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormStepId", claimFormStepId );
    clientStateParameterMap.put( "claimFormId", claimFormId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, "preview", new String[] { queryString, "method=preparePreview" } );
  } // end preparePreview

  /**
   * Update the ClaimForm with the data provided through the Form.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "update";

    logger.info( ">>> " + METHOD_NAME );
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return mapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    String forwardTo = ActionConstants.SUCCESS_UPDATE;
    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    if ( isTokenValid( request, true ) )
    {
      ClaimForm updatedClaimForm = claimFormForm.toFullDomainObject();
      try
      {
        getClaimFormDefinitionService().saveClaimForm( updatedClaimForm, true );
      }
      catch( ServiceErrorException se )
      {
        List serviceErrors = se.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      }
    }
    else
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forwardTo = ActionConstants.FAIL_UPDATE;
    }
    logger.info( "<<< " + METHOD_NAME + " forwardTo=" + forwardTo );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormId", new Long( claimFormForm.getClaimFormId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forwardTo, new String[] { queryString, "method=display" } );
  } // end update

  /**
   * Removes ClaimForms
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "remove";
    logger.debug( ">>> " + METHOD_NAME );

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_DELETE;

    ClaimFormForm claimFormForm = (ClaimFormForm)form;
    String[] deletedIds = claimFormForm.getDelete();
    // BugFix 18504
    if ( deletedIds != null )
    {
      log.debug( "deletedIds " + deletedIds.length );
    }
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );
    try
    {
      getClaimFormDefinitionService().deleteClaimForms( list );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_DELETE;
    }

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Copies ClaimForm - Almost like a 'save as'
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward copy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "copy";
    logger.debug( ">>> " + METHOD_NAME );

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_COPY ); // EARLY EXIT
    }

    String forward = ActionConstants.SUCCESS_COPY;

    ClaimFormForm claimFormForm = (ClaimFormForm)form;
    ClaimForm copiedClaimForm = null;

    String newFormName = claimFormForm.getNewFormName();
    Long claimFormId = new Long( claimFormForm.getClaimFormId() );

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimFormId", claimFormId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, "method=display" } );
    }

    try
    {
      copiedClaimForm = getClaimFormDefinitionService().copyClaimForm( claimFormId, newFormName );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_COPY;
      return mapping.findForward( forward );
    }

    logger.info( "<<< " + METHOD_NAME );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormId", copiedClaimForm.getId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=display" } );

  }

  /**
   * Removes ClaimForms
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCopy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "prepareCopy";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.COPY_FORWARD;

    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    Long claimFormId = new Long( claimFormForm.getClaimFormId() );

    claimFormForm.load( getClaimFormDefinitionService().getClaimFormById( claimFormId ) );

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
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
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = "display";

    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    Long claimFormId = new Long( claimFormForm.getClaimFormId() );

    claimFormForm.load( getClaimFormDefinitionService().getClaimFormById( claimFormId ) );

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Reorder ClaimFormStep within ClaimForm
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward reorderStep( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "reorderStep";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    Long claimFormId = new Long( 0 );
    Long claimFormStepId = new Long( 0 );
    Long newIndex = new Long( 0 );

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
        claimFormId = (Long)clientStateMap.get( "claimFormId" );
      }
      catch( ClassCastException cce )
      {
        String claimFormIdString = (String)clientStateMap.get( "claimFormId" );
        claimFormId = new Long( claimFormIdString );
      }

      try
      {
        claimFormStepId = (Long)clientStateMap.get( "claimFormStepId" );
      }
      catch( ClassCastException cce )
      {
        String claimFormStepIdString = (String)clientStateMap.get( "claimFormStepId" );
        claimFormStepId = new Long( claimFormStepIdString );
      }

      newIndex = new Long( RequestUtils.getRequiredParamString( request, "newIndex" ) );
      if ( claimFormId == null && claimFormStepId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimFormId and claimFormStepId as part of clientState" ) );
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    getClaimFormDefinitionService().reorderClaimFormStep( claimFormId, claimFormStepId, newIndex.intValue() );

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormId", claimFormId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=display" } );
  }

  /**
   * deleteClaimFormSteps within ClaimForm
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteClaimFormSteps( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forward = ActionConstants.SUCCESS_UPDATE;

    ActionMessages errors = new ActionMessages();

    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    String[] deletedIds = claimFormForm.getDelete();
    try
    {
      log.debug( "deletedIds " + deletedIds.length );
      List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );
      getClaimFormDefinitionService().deleteClaimFormSteps( new Long( claimFormForm.getClaimFormId() ), list );
    }
    catch( NullPointerException e )
    {
      errors.add( "errorMessage", new ActionMessage( "claims.form.step.view.NOTHING_TO_REMOVE" ) );
      saveErrors( request, errors );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "claimFormId", new Long( claimFormForm.getClaimFormId() ) );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forward, new String[] { queryString, "method=display" } );
  }

  /**
   * Mark Claim Form Complete
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward markComplete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ClaimFormForm claimFormForm = (ClaimFormForm)form;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );
    ClaimForm claimForm = getClaimFormDefinitionService().getClaimFormByIdWithAssociations( new Long( claimFormForm.getClaimFormId() ), associationRequestCollection );

    if ( validClaimElements( claimForm ) )
    {
      claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );
      try
      {
        getClaimFormDefinitionService().saveClaimForm( claimForm, true );
      }
      catch( ServiceErrorException e )
      {
        logger.error( e.getMessage(), e );
      }
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimFormId", new Long( claimFormForm.getClaimFormId() ) );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString, "method=display" } );
    }
    else
    {
      errors.add( "errorMessage", new ActionMessage( "claims.form.step.view.ATLEAST_THREE_FIELDS" ) );
      saveErrors( request.getSession(), errors );
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimFormStepId", claimForm.getClaimFormSteps().get( 0 ).getId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.FAIL_UPDATE, new String[] { queryString } );
    }
  }

  private boolean validClaimElements( ClaimForm claimForm )
  {
    boolean validClaimElements = true;
    if ( claimForm.getClaimFormModuleType().isSsi() )
    {
      int requiredElementCount = 0;
      for ( ClaimFormStepElement element : claimForm.getClaimFormSteps().get( 0 ).getClaimFormStepElements() )
      {
        if ( element.isRequired() )
        {
          requiredElementCount++;
        }
      }
      if ( requiredElementCount < 3 )
      {
        validClaimElements = false;
      }
    }
    return validClaimElements;
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

} // end ClaimFormAction

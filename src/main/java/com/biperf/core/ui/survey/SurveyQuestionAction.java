
package com.biperf.core.ui.survey;

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

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.survey.SurveyAssociationRequest;
import com.biperf.core.service.survey.SurveyQuestionAssociationRequest;
import com.biperf.core.service.survey.SurveyService;
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

public class SurveyQuestionAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SurveyQuestionAction.class );

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

    String forward = ActionConstants.SUCCESS_FORWARD;

    SurveyQuestionForm surveyQuestionForm = (SurveyQuestionForm)form;

    surveyQuestionForm.setMethod( "save" );
    Long surveyQuestionId = surveyQuestionForm.getSurveyQuestionId();

    if ( surveyQuestionId != null && surveyQuestionId.longValue() > 0 )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new SurveyQuestionAssociationRequest( SurveyQuestionAssociationRequest.ALL ) );

      surveyQuestionForm.load( getSurveyService().getSurveyQuestionByIdWithAssociations( surveyQuestionId, associationRequestCollection ) );
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.SURVEY_QUESTION ) );

      surveyQuestionForm.load( getSurveyService().getSurveyByIdWithAssociations( surveyQuestionForm.getSurveyFormId(), associationRequestCollection ) );
    }

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Creates or updates a survey question
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward save( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "save";

    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();
    SurveyQuestionForm surveyQuestionForm = (SurveyQuestionForm)actionForm;

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "surveyFormId", surveyQuestionForm.getSurveyFormId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );
    }

    try
    {
      getSurveyService().saveSurveyQuestion( surveyQuestionForm.getSurveyFormId(), surveyQuestionForm.toDomainObject(), surveyQuestionForm.getSurveyQuestionText() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyFormId", surveyQuestionForm.getSurveyFormId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );

  } // end create

  /**
   * Removes SurveyQuestions
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

    SurveyQuestionForm surveyQuestionForm = (SurveyQuestionForm)form;
    String[] deletedIds = surveyQuestionForm.getDeletedResponses();
    if ( deletedIds != null )
    {
      log.debug( "deletedIds " + deletedIds.length );
    }
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    getSurveyService().deleteSurveyQuestionResponses( surveyQuestionForm.getSurveyQuestionId(), list );

    logger.info( "<<< " + METHOD_NAME );

    return display( mapping, form, request, response );
  }

  /**
   * Reorder Responses within a Question
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward reorder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "reorder";
    logger.debug( ">>> " + METHOD_NAME );
    SurveyQuestionForm surveyQuestionForm = (SurveyQuestionForm)form;
    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    String questionId = null;
    String responseId = null;
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
        responseId = (String)clientStateMap.get( "surveyQuestionResponseId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "surveyQuestionResponseId" );
        if ( id != null )
        {
          responseId = id.toString();
        }
      }
      try
      {
        questionId = (String)clientStateMap.get( "surveyQuestionId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "surveyQuestionId" );
        if ( id != null )
        {
          questionId = id.toString();
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    try
    {
      newIndex = new Long( surveyQuestionForm.getNewResponseSequenceNum() );
    }
    catch( IllegalArgumentException e )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED ) );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_UPDATE;
    }
    else
    {
      getSurveyService().reorderResponse( new Long( questionId ), new Long( responseId ), newIndex.intValue() );
    }

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyQuestionId", questionId );
    return new ActionForward( ClientStateUtils.generateEncodedLink( "", "surveyQuestionView.do?method=display", clientStateParameterMap ), true );
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

} // end SurveyFormAction


package com.biperf.core.ui.survey;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.survey.SurveyAssociationRequest;
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
import com.biperf.util.StringUtils;

public class SurveyFormAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SurveyFormAction.class );

  /**
   * Creates or updates a survey form
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

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
    }

    ActionMessages errors = new ActionMessages();
    SurveyForm surveyForm = (SurveyForm)actionForm;
    String forwardAction = "";
    ViewSurveyForm viewSurveyForm = new ViewSurveyForm();
    try
    {
      Survey survey = getSurveyService().saveSurvey( surveyForm.toDomainObject() );

      AssociationRequestCollection assocReqs = new AssociationRequestCollection();
      assocReqs.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );
      viewSurveyForm.load( getSurveyService().getSurveyByIdWithAssociations( survey.getId(), assocReqs ) );
      request.setAttribute( "viewSurveyForm", viewSurveyForm );
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
    else
    {
      forwardAction = ActionConstants.SUCCESS_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyFormId", viewSurveyForm.getSurveyFormId() );
    clientStateParameterMap.put( "surveyFormName", viewSurveyForm.getSurveyFormName() );
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
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    ViewSurveyForm surveyForm = (ViewSurveyForm)form;
    String[] deletedIds = surveyForm.getDeleteIds();
    if ( deletedIds != null )
    {
      log.debug( "deletedIds " + deletedIds.length );
    }
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    try
    {
      getSurveyService().deleteSurveyQuestions( surveyForm.getSurveyFormId(), list );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage(), e );
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    return display( mapping, form, request, response );
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

    String forward = ActionConstants.SUCCESS_FORWARD;

    SurveyForm surveyForm = (SurveyForm)form;
    Survey copiedSurveyForm = null;
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
      String oldSurveyFormName = (String)clientStateMap.get( "oldSurveyFormName" );
      if ( !StringUtils.isEmpty( oldSurveyFormName ) )
      {
        surveyForm.setSurveyFormName( oldSurveyFormName );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    String newFormName = surveyForm.getCopySurveyFormName();
    Long surveyFormId = surveyForm.getSurveyFormId();
    String surveyFormType = surveyForm.getSurveyFormType();

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "surveyFormId", surveyFormId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, "method=display" } );
    }

    try
    {
      copiedSurveyForm = getSurveyService().copySurvey( surveyFormId, newFormName, surveyFormType );
    }
    catch( UniqueConstraintViolationException e )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "quiz.errors.UNIQUE_CONSTRAINT" ) );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
      return mapping.findForward( forward );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
      return mapping.findForward( forward );
    }

    logger.info( "<<< " + METHOD_NAME );
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyFormId", copiedSurveyForm.getId() );
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

    ViewSurveyForm surveyForm = (ViewSurveyForm)form;

    Long formId = surveyForm.getSurveyFormId();
    surveyForm.load( getSurveyService().getSurveyById( formId ) );

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

    String forward = ActionConstants.SUCCESS_FORWARD;

    SurveyForm surveyForm = (SurveyForm)form;
    surveyForm.setMethod( "save" );
    Long surveyFormId = surveyForm.getSurveyFormId();
    try
    {
      String clientState = request.getParameter( "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      if ( clientState != null )
      {
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        try
        {
          String strSurveyFormId = (String)clientStateMap.get( "surveyFormId" );
          if ( surveyFormId == null && !StringUtils.isEmpty( strSurveyFormId ) )
          {
            surveyFormId = new Long( strSurveyFormId );
          }
        }
        catch( ClassCastException cce )
        {
          Long longSurveyFormId = (Long)clientStateMap.get( "surveyFormId" );
          if ( surveyFormId == null && longSurveyFormId != null )
          {
            surveyFormId = longSurveyFormId;
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.SURVEY_QUESTION ) );
    Survey surveyLib = getSurveyService().getSurveyByIdWithAssociations( surveyFormId, associationRequestCollection );
    surveyForm.load( surveyLib );
    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * View SurveyForm with question table
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward view( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "view";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.SUCCESS_FORWARD;

    ViewSurveyForm surveyForm = (ViewSurveyForm)form;
    surveyForm.setMethod( "save" );
    Long surveyFormId = surveyForm.getSurveyFormId();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.SURVEY_QUESTION ) );
    Survey surveyLib = getSurveyService().getSurveyByIdWithAssociations( surveyFormId, associationRequestCollection );
    surveyForm.load( surveyLib );
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
  public ActionForward reorder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "reorder";
    logger.debug( ">>> " + METHOD_NAME );
    ViewSurveyForm surveyForm = (ViewSurveyForm)form;
    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    String surveyFormId = null;
    Long questionId = new Long( 0 );
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
        surveyFormId = (String)clientStateMap.get( "surveyFormId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "surveyFormId" );
        if ( id != null )
        {
          surveyFormId = id.toString();
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    try
    {
      questionId = new Long( surveyForm.getSurveyQuestionId() );
      newIndex = new Long( surveyForm.getNewQuestionSequenceNum() );
    }
    catch( IllegalArgumentException e )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED ) );
    }

    if ( errors.size() != 0 )
    {
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      getSurveyService().reorderQuestion( new Long( surveyFormId ), questionId, newIndex.intValue() );
    }

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyFormId", surveyFormId );
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
    String forward = ActionConstants.SUCCESS_UPDATE;

    ActionMessages errors = new ActionMessages();

    ViewSurveyForm surveyForm = (ViewSurveyForm)form;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );
    Survey survey = getSurveyService().getSurveyByIdWithAssociations( surveyForm.getSurveyFormId(), associationRequestCollection );

    List<SurveyQuestion> questionList = survey.getSurveyQuestions();

    if ( questionList != null && questionList.size() > 0 )
    {
      surveyForm.setQuestions( questionList );
      Iterator questionListIter = questionList.iterator();
      int activeQuestions = 0;
      while ( questionListIter.hasNext() )
      {
        SurveyQuestion question = (SurveyQuestion)questionListIter.next();
        if ( question.isActive() )
        {
          activeQuestions = activeQuestions + 1;
          if ( question.getResponseType().getCode().equals( SurveyResponseType.STANDARD_RESPONSE )
              && ( question.getSurveyQuestionResponses() == null || question.getSurveyQuestionResponses().isEmpty() ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "survey.errors.NO_RESPONSE", String.valueOf( activeQuestions ) ) );
          }
          else if ( question.getSurveyQuestionResponses().size() < 2 )
          {
            if ( question.getResponseType().getCode().equals( SurveyResponseType.STANDARD_RESPONSE ) )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "survey.errors.QUESTION_MIN_ANSWER" ) );
              break;
            }
          }
        }
      }
    }
    else
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "survey.errors.NO_QUESTIONS" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      survey.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );

      try
      {
        getSurveyService().saveSurvey( survey );
      }
      catch( ServiceErrorException e )
      {
        forward = ActionConstants.FAIL_FORWARD;
        logger.error( e.getMessage(), e );
        List serviceErrors = e.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
        saveErrors( request, errors );
      }
    }
    return mapping.findForward( forward );
  }

  /**
   * Retrieves a SurveyService
   * 
   * @return SurveyService
   */
  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }
} // end SurveyFormAction

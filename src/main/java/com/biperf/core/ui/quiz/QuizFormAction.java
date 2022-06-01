/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/QuizFormAction.java,v $
 */

package com.biperf.core.ui.quiz;

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
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningSlideDetails;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.quiz.QuizAssociationRequest;
import com.biperf.core.service.quiz.QuizService;
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

/**
 * QuizFormAction.
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
 * <td>jenniget</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizFormAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( QuizFormAction.class );

  /**
   * Creates or updates a quiz form
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
    QuizFormForm quizFormForm = (QuizFormForm)actionForm;
    String forwardAction = "";
    ViewQuizFormForm viewQuizFormForm = new ViewQuizFormForm();
    try
    {
      Quiz quiz = getQuizService().saveQuiz( quizFormForm.toDomainObject() );
      AssociationRequestCollection assocReqs = new AssociationRequestCollection();
      assocReqs.add( new QuizAssociationRequest( QuizAssociationRequest.ALL ) );
      viewQuizFormForm.load( getQuizService().getQuizByIdWithAssociations( quiz.getId(), assocReqs ) );
      request.setAttribute( "viewQuizFormForm", viewQuizFormForm );
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
    String toPage = request.getParameter( "toPage" );
    if ( !StringUtils.isEmpty( toPage ) )
    {
      forwardAction = "quizLearning";
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "quizFormId", viewQuizFormForm.getQuizFormId() );
    clientStateParameterMap.put( "quizFormName", viewQuizFormForm.getQuizFormName() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );

  } // end create

  /**
   * Removes QuizQuestions
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

    // String forward = ActionConstants.SUCCESS_DELETE;

    ViewQuizFormForm quizFormForm = (ViewQuizFormForm)form;
    String[] deletedIds = quizFormForm.getDeleteIds();
    if ( deletedIds != null )
    {
      log.debug( "deletedIds " + deletedIds.length );
    }
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    try
    {
      getQuizService().deleteQuizQuestions( quizFormForm.getQuizFormId(), list );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage(), e );
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( e.getServiceErrors(), errors );
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

    QuizFormForm quizFormForm = (QuizFormForm)form;
    Quiz copiedQuizForm = null;
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
      String oldQuizFormName = (String)clientStateMap.get( "oldQuizFormName" );
      if ( !StringUtils.isEmpty( oldQuizFormName ) )
      {
        quizFormForm.setQuizFormName( oldQuizFormName );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    String newFormName = quizFormForm.getCopyQuizFormName();
    Long quizFormId = quizFormForm.getQuizFormId();

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "quizFormId", quizFormId );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString, "method=display" } );
    }

    try
    {
      copiedQuizForm = getQuizService().copyQuiz( quizFormId, newFormName );
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
    clientStateParameterMap.put( "quizFormId", copiedQuizForm.getId() );
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

    ViewQuizFormForm quizFormForm = (ViewQuizFormForm)form;

    Long formId = quizFormForm.getQuizFormId();
    quizFormForm.load( getQuizService().getQuizById( formId ) );

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

    QuizFormForm quizFormForm = (QuizFormForm)form;
    quizFormForm.setMethod( "save" );
    Long quizFormId = quizFormForm.getQuizFormId();
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
          String strQuizFormId = (String)clientStateMap.get( "quizFormId" );
          if ( quizFormId == null && !StringUtils.isEmpty( strQuizFormId ) )
          {
            quizFormId = new Long( strQuizFormId );
          }
        }
        catch( ClassCastException cce )
        {
          Long longQuizFormId = (Long)clientStateMap.get( "quizFormId" );
          if ( quizFormId == null && longQuizFormId != null )
          {
            quizFormId = longQuizFormId;
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.QUIZ_QUESTION ) );
    Quiz quizLib = getQuizService().getQuizByIdWithAssociations( quizFormId, associationRequestCollection );
    quizFormForm.load( quizLib );
    if ( quizLib != null && quizLib.getLearningObjects() != null )
    {
      List<QuizLearningSlideDetails> quizLearningObjects = getQuizService().getQuizLearningObjectforQuizLibrary( quizLib.getLearningObjects() );
      if ( quizLearningObjects != null )
      {
        quizFormForm.loadLearningObjects( quizLearningObjects );
      }
    }
    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * View QuizForm with question table
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

    ViewQuizFormForm quizFormForm = (ViewQuizFormForm)form;
    quizFormForm.setMethod( "save" );
    Long quizFormId = quizFormForm.getQuizFormId();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.QUIZ_QUESTION ) );
    Quiz quizLib = getQuizService().getQuizByIdWithAssociations( quizFormId, associationRequestCollection );
    if ( quizLib != null )
    {
      quizFormForm.load( quizLib );
    }
    if ( quizLib != null && quizLib.getLearningObjects() != null )
    {
      List<QuizLearningSlideDetails> quizLearningObjects = getQuizService().getQuizLearningObjectforQuizLibrary( quizLib.getLearningObjects() );
      quizFormForm.loadLearningObjects( quizLearningObjects );
    }
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
    ViewQuizFormForm quizFormForm = (ViewQuizFormForm)form;
    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    String quizFormId = null;
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
        quizFormId = (String)clientStateMap.get( "quizFormId" );

      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "quizFormId" );
        if ( id != null )
        {
          quizFormId = id.toString();
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    try
    {
      // The requestUtils will throw an exception
      // if the required params are not in the request.
      // quizFormId = quizFormForm.getQuizFormId();
      questionId = new Long( quizFormForm.getQuizQuestionId() );
      newIndex = new Long( quizFormForm.getNewQuestionSequenceNum() );
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
      getQuizService().reorderQuestion( new Long( quizFormId ), questionId, newIndex.intValue() );
    }

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "quizFormId", quizFormId );
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

    ViewQuizFormForm quizFormForm = (ViewQuizFormForm)form;

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.ALL ) );
    Quiz quiz = getQuizService().getQuizByIdWithAssociations( quizFormForm.getQuizFormId(), associationRequestCollection );

    List questionList = quiz.getQuizQuestions();

    if ( questionList != null && questionList.size() > 0 )
    {
      quizFormForm.setQuestions( questionList );
      Iterator questionListIter = questionList.iterator();
      int activeQuestions = 0;
      while ( questionListIter.hasNext() )
      {
        QuizQuestion question = (QuizQuestion)questionListIter.next();
        if ( question.isActive() )
        {
          activeQuestions = activeQuestions + 1;
          if ( question.getQuizQuestionAnswers() == null || question.getQuizQuestionAnswers().size() < 2 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.QUESTION_MIN_ANSWER" ) );
            break;
          }

          Iterator answerListIter = question.getQuizQuestionAnswers().iterator();
          boolean hasCorrectAnswer = false;
          while ( answerListIter.hasNext() )
          {
            QuizQuestionAnswer answer = (QuizQuestionAnswer)answerListIter.next();
            if ( answer.isCorrect() )
            {
              hasCorrectAnswer = true;
              break;
            }
          }
          if ( !hasCorrectAnswer )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.MISSING_CORRECT_ANSWER", question.getQuestionText() ) );
          }
        }
      }

      if ( quiz.getQuizType().getCode().equals( "random" ) )
      {
        if ( quiz.getNumberOfQuestionsAsked() > activeQuestions )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.QUIZ_RANDOM_QUESTION" ) );
        }
      }
      else
      {
        if ( quiz.getPassingScore() > activeQuestions )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.QUIZ_FIXED_QUESTION" ) );
        }
      }
    }
    else
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.errors.NO_QUESTIONS" ) );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = ActionConstants.FAIL_FORWARD;
    }
    else
    {
      quiz.setClaimFormStatusType( ClaimFormStatusType.lookup( ClaimFormStatusType.COMPLETED ) );

      try
      {
        getQuizService().saveQuiz( quiz );
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
   * Retrieves a QuizService
   * 
   * @return QuizService
   */
  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }
} // end QuizFormAction

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizQuestionAction.java,v $
 */

package com.biperf.core.ui.quiz;

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
import com.biperf.core.service.quiz.QuizAssociationRequest;
import com.biperf.core.service.quiz.QuizQuestionAssociationRequest;
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

/**
 * QuizQuestionAction.
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
 * <td>sedey</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizQuestionAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( QuizQuestionAction.class );

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

    QuizQuestionForm quizQuestionForm = (QuizQuestionForm)form;

    quizQuestionForm.setMethod( "save" );
    Long quizQuestionId = quizQuestionForm.getQuizQuestionId();

    if ( quizQuestionId != null && quizQuestionId.longValue() > 0 )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new QuizQuestionAssociationRequest( QuizQuestionAssociationRequest.ALL ) );

      quizQuestionForm.load( getQuizService().getQuizQuestionByIdWithAssociations( quizQuestionId, associationRequestCollection ) );
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.QUIZ_QUESTION ) );

      quizQuestionForm.load( getQuizService().getQuizByIdWithAssociations( quizQuestionForm.getQuizFormId(), associationRequestCollection ) );
    }

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  /**
   * Creates or updates a quiz question
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
    QuizQuestionForm quizQuestionForm = (QuizQuestionForm)actionForm;

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "quizFormId", quizQuestionForm.getQuizFormId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( actionMapping, forwardAction, new String[] { queryString, "method=display" } );
    }

    try
    {
      getQuizService().saveQuizQuestion( quizQuestionForm.getQuizFormId(), quizQuestionForm.toDomainObject(), quizQuestionForm.getQuizQuestionText() );
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
    clientStateParameterMap.put( "quizFormId", quizQuestionForm.getQuizFormId() );
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
    final String METHOD_NAME = "remove";
    logger.debug( ">>> " + METHOD_NAME );

    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( ActionConstants.ERROR_TOKEN_FAILURE, new ActionMessage( ActionConstants.ERROR_TOKEN_EXCEPTION ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_DELETE ); // EARLY EXIT
    }

    QuizQuestionForm quizQuestionForm = (QuizQuestionForm)form;
    String[] deletedIds = quizQuestionForm.getDeletedAnswers();
    if ( deletedIds != null )
    {
      log.debug( "deletedIds " + deletedIds.length );
    }
    List list = PresentationUtils.convertStringArrayToListOfLongs( deletedIds );

    getQuizService().deleteQuizQuestionAnswers( quizQuestionForm.getQuizQuestionId(), list );

    logger.info( "<<< " + METHOD_NAME );

    return display( mapping, form, request, response );
  }

  /**
   * Reorder Answers within a Question
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
    QuizQuestionForm quizQuestionForm = (QuizQuestionForm)form;
    String forward = ActionConstants.SUCCESS_UPDATE;
    ActionMessages errors = new ActionMessages();

    String questionId = null;
    String answerId = null;
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
        answerId = (String)clientStateMap.get( "quizQuestionAnswerId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "quizQuestionAnswerId" );
        if ( id != null )
        {
          answerId = id.toString();
        }
      }
      try
      {
        questionId = (String)clientStateMap.get( "quizQuestionId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "quizQuestionId" );
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
      // The requestUtils will throw an exception
      // if the required params are not in the request.
      // questionId = quizQuestionForm.getQuizQuestionId();
      // answerId = quizQuestionForm.getQuizQuestionAnswerId();
      newIndex = new Long( quizQuestionForm.getNewAnswerSequenceNum() );
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
      getQuizService().reorderAnswer( new Long( questionId ), new Long( answerId ), newIndex.intValue() );
    }

    logger.info( "<<< " + METHOD_NAME );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "quizQuestionId", questionId );
    // String queryString = ClientStateUtils.generateEncodedLink( "", "quizQuestionView.do",
    // clientStateParameterMap );
    return new ActionForward( ClientStateUtils.generateEncodedLink( "", "quizQuestionView.do?method=display", clientStateParameterMap ), true );
  }

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }

} // end QuizFormAction
